package com.princetonsa.action.facturacion;
 
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.actionform.facturacion.ArchivoPlanoForm;
import com.princetonsa.dto.interfaz.DtoInterfazAxRips;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ArchivoPlano;


/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class ArchivoPlanoAction  extends Action
{	
	
	Logger logger = Logger.getLogger(ArchivoPlanoAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {		
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof ArchivoPlanoForm) 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("codigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");		 

				ArchivoPlanoForm forma = (ArchivoPlanoForm)form;
				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				logger.info("-------------------------------------");
				logger.info("Valor del Estado >> "+forma.getEstado());
				logger.info("-------------------------------------");

				if(estado == null)
				{
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo de Archivos Planos  (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}


				//********************************COLSANITAS****************************************

				//Busqueda Parametros Archivos Planos Colstanitas 
				else if(estado.equals("ArchPlanoColSa"))
				{
					forma.reset();
					String respuesta = verificarInterfazRips(con,forma,usuario.getCodigoInstitucionInt());

					if(!respuesta.equals(""))
					{				 					 
						request.setAttribute("codigoDescripcionError",respuesta);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaError");
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				 				 
				}
				else if(estado.equals("volverModificarBase"))
				{				 
					UtilidadBD.closeConnection(con);
					forma.setNombreArchivosPlanosMap("indicadorRepetidos",ConstantesBD.acronimoNo);
					forma.setNombreArchivosPlanosMap("numRegistros","0");
					return mapping.findForward("principal");				 				 
				}
				//Busca las cuentas de cobro apartir de un numero de cuenta de cobro
				else if(estado.equals("buscarCCobro"))
				{
					buscarCuentasCobro(con,forma,usuario,response);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				//Verifica la existencia de los Archivos a Generar			 
				else if(estado.equals("preGenerarArchPlanoColsa"))
				{
					if(!verificarArchivosPlanos(con, forma,usuario))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					else
					{
						//Genera la informacion de los archivos Planos
						generarArchivosPlanosColsa(con,forma,usuario);					 

						errores = guardarArchivosPlanosColsa(con,forma,usuario,errores);

						if(!errores.isEmpty())
						{
							saveErrors(request,errores);	
							UtilidadBD.closeConnection(con);			
							return mapping.findForward("principal");
						}
						else
						{						 
							insertarHistorialArchivosPlanos(con, forma, usuario);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("resumenArchivos");
						}					 
					}				 
				}			 
				//Genera el archivo plano para Colsanitas 
				else if(estado.equals("generarArchPlanoColsa"))
				{					 
					//Genera la informacion de los archivos Planos
					generarArchivosPlanosColsa(con,forma,usuario);					 

					errores = guardarArchivosPlanosColsa(con,forma,usuario,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);			
						return mapping.findForward("principal");
					}
					else
					{	
						insertarHistorialArchivosPlanos(con, forma, usuario);
						UtilidadBD.closeConnection(con);					 
						return mapping.findForward("resumenArchivos");
					}					 				 				 			 				 
				}		 
				//Carga el detalle del Archivo Plano Seleccionado			 
				else if(estado.equals("cargarDetArchivoPlano"))
				{
					cargarDetalleArchivoPlano(forma,"");				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenArchivos");				 
				}

				//Carga el detalle del Archivo Plano  de Inconsistencias Seleccionado			 
				else if(estado.equals("cargarDetArchivoPlanoIncon"))
				{
					cargarDetalleArchivoPlano(forma,"Incon");				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenArchivos");				 
				}

				//Elimina un numero de Envio
				else if(estado.equals("eliminarNumeroEnvio"))
				{
					eliminarNumeroEnvio(forma);
					verificarArchivosPlanos(con, forma,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//Ingresa un Numero de Envio
				else if(estado.equals("ingresarNumeroEnvio"))				 
				{
					ingresarNumeroEnvio(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				 
				}

				//****************************************************************************************
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}	
	
	
	
	
	//*******************************METODOS*************************************
	//***************************************************************************
	
	/**
	 * Verifica el estado del parametro general Interfaz Rips
	 * @param Connection con
	 * @param ArchivoPlanoForm forma
	 * @param int intitucion
	 */
	public String verificarInterfazRips(
			Connection con,
			ArchivoPlanoForm forma,
			int institucion)
	{
		HashMap respuesta = new HashMap();
		
		if(ValoresPorDefecto.getInterfazRips(institucion).equals(ConstantesBD.acronimoNo))
			forma.setBusquedaArchivosPlanosMap("interfazRips",ConstantesBD.acronimoNo);		
		else		
		{			
			//Carga la informacion de la tabla interfaz RIPS
			UtilidadBDInterfaz utilidadInterfaz = new UtilidadBDInterfaz();
			respuesta = utilidadInterfaz.consultarInterfazRips(con,institucion);
			forma.setInterfazRips((ArrayList<DtoInterfazAxRips>)respuesta.get("array"));
			
			if(!respuesta.get("error").toString().equals(""))									
				return respuesta.get("error").toString();						
			
			//Inicializa el maoa de Numeros de Envio
			forma.setNumeroEnviosMap("indexEliminado","");	
			forma.setNumeroEnviosMap("indicadorPos","");			
			//------------------------------------------------------------------------
			
			forma.setBusquedaArchivosPlanosMap("interfazRips",ConstantesBD.acronimoSi);
		}
		
		//incializa el mapa de numero de envio 
		forma.setNumeroEnviosMap("numRegistros","0");
		
			
		//incializa los valores de la busqueda
		forma.setBusquedaArchivosPlanosMap("convenio",ConstantesBD.codigoNuncaValido);
		forma.setBusquedaArchivosPlanosMap("numeroEnvio","");
		forma.setBusquedaArchivosPlanosMap("numeroCuentaCobro","");
		forma.setBusquedaArchivosPlanosMap("descripcionConvenio","");		
		forma.setBusquedaArchivosPlanosMap("convenioCCobro","");		
		forma.setBusquedaArchivosPlanosMap("fechaInicial","");
		forma.setBusquedaArchivosPlanosMap("fechaFinal","");
		forma.setBusquedaArchivosPlanosMap("fechaEnvio","");
		forma.setBusquedaArchivosPlanosMap("secuencia","");		
		//almacena la ruta del archivo plano mas reciente del historial
		forma.setBusquedaArchivosPlanosMap("ruta",ArchivoPlano.getUltimaRutaHistorialArchivosPlanos(con, institucion));	
				
		//almacena los convenios de la parametrizacion de Archivo Plano Colsanitas
		forma.setConvenioArray(ArchivoPlano.getConveniosArchivosPlanos(con, institucion));
		
		//Inicializa el mapa de nombre de Archivos Planos a Generar
		forma.setNombreArchivosPlanosMap("numRegistros","0");
		forma.setNombreArchivosPlanosMap("indicadorRepetidos",ConstantesBD.acronimoNo);	
		
		return "";
	}	
	
	
	
	
	/**
	 * Almanena y Verifica los nombres de los Archivos Planos a Generarse          
	 * @param Connection con
	 * @param ArchivoPlanoForm forma
	 * */
	public boolean verificarArchivosPlanos(Connection con, ArchivoPlanoForm forma,UsuarioBasico usuario)
	{		
		HashMap convenios = new HashMap();
		ArchivoPlano archivoPlano = new ArchivoPlano();
		
		logger.info("\n\n\n\n\n");
		logger.info("GENERAR NOMBRES ARCHIVOS PLANOS ******************************** ");
		
		forma.setBusquedaArchivosPlanosMap("institucion",usuario.getCodigoInstitucionInt());
		
		//Tipo de Consulta en base al numero de envio 
		if(forma.getTipoConsuta() == 1)
		{
			//Genera el HashMap con el nombre de lo archivos Planos
			forma.setNombreArchivosPlanosMap(archivoPlano.getNombreArchivoPlano(con,forma.getBusquedaArchivosPlanosMap(),forma.getNumeroEnviosMap()));						
		}
		//Tipo de Consulta en base al convenio 
		else if(forma.getTipoConsuta() == 2)			
		{
			//Incializa los valores del HashMap convenios
			convenios.put("numRegistros","1");
			convenios.put("convenio_0",forma.getBusquedaArchivosPlanosMap("convenio").toString().trim());
			convenios.put("descripcionConvenio_0",forma.getBusquedaArchivosPlanosMap("descripcionConvenio"));
			
			//Genera el HashMap con el nombre de lo archivos Planos
			forma.setNombreArchivosPlanosMap(archivoPlano.getNombreArchivoPlano(con,forma.getBusquedaArchivosPlanosMap(),convenios));									
		}
		//Tipo de Consulta en base a la cuenta de Cobro
		else if(forma.getTipoConsuta() == 3)
		{
			//Incializa los valores del HashMap convenios
			convenios.put("numRegistros","1");
			convenios.put("convenio_0",forma.getBusquedaArchivosPlanosMap("convenioCCobro").toString().trim());
			convenios.put("descripcionConvenio_0",forma.getBusquedaArchivosPlanosMap("descripcionConvenio"));
			
			//Genera el HashMap con el nombre de lo archivos Planos
			forma.setNombreArchivosPlanosMap(archivoPlano.getNombreArchivoPlano(con,forma.getBusquedaArchivosPlanosMap(),convenios));						
		}
				
		//Inicializa los datos para cargar los archivos planos una vez generados
		forma.setNombreArchivosPlanosMap("indicadorPos",ConstantesBD.codigoNuncaValido+"");		
				
		//Evalua si existe nombre de archivos planos ya almacenados en la ruta indicada
		if(forma.getNombreArchivosPlanosMap("indicadorRepetidos").toString().equals(ConstantesBD.acronimoNo))
			return true;	
					
		return false;		
	}
	
	
	
	
	/**
	 * Generacion del Archivo Plano de Colsanitas
	 * @param Connection con 
	 * @param ArchivoPlanoForm forma
	 * */
	public void generarArchivosPlanosColsa(Connection con,ArchivoPlanoForm forma,UsuarioBasico usuario)
	{			
		logger.info("GENERACION ARCHIVO PLANO COLSANITAS*****************************");	
		
		ArchivoPlano archivoPlano = new ArchivoPlano();		
		
		//Tipo de Consulta en base al numero de envio 
		if(forma.getTipoConsuta() == 1)
		{
			//Genera el HashMap de dataFile que contiene los archivos a generar
			forma.setDataFile(archivoPlano.interfazParametrosNumeroEnvio(con,
					forma.getInterfazRips(),
					forma.getNumeroEnviosMap(),
					forma.getBusquedaArchivosPlanosMap(),
					forma.getNombreArchivosPlanosMap()));			
		}
		//Tipo de Consulta en base al convenio
		else if(forma.getTipoConsuta() == 2)
		{
			//Convierte la fecha 
			forma.setBusquedaArchivosPlanosMap("fechaInicialABD",UtilidadFecha.conversionFormatoFechaABD(forma.getBusquedaArchivosPlanosMap("fechaInicial").toString()));
			forma.setBusquedaArchivosPlanosMap("fechaFinalABD",UtilidadFecha.conversionFormatoFechaABD(forma.getBusquedaArchivosPlanosMap("fechaFinal").toString()));
			
			//Genera el HashMap de dataFile que contiene los archivos a generar			
			forma.setDataFile("dtoFacturas_0",archivoPlano.getDtoFacturas(con,forma.getBusquedaArchivosPlanosMap(),2));
			forma.setDataFile("convenio_0",forma.getBusquedaArchivosPlanosMap("convenio").toString().trim());
			forma.setDataFile("archivo_0",archivoPlano.getPosNombreArchivoPlano(forma.getNombreArchivosPlanosMap(),forma.getBusquedaArchivosPlanosMap("convenio").toString()));
			forma.setDataFile("numRegistros","1");
		}
		//Tipo de Consulta en base a la cuenta de cobro
		else if(forma.getTipoConsuta() == 3)
		{
			//Genera el HashMap de dataFile que contiene los archivos a generar
			forma.setBusquedaArchivosPlanosMap("convenio",forma.getBusquedaArchivosPlanosMap("convenioCCobro"));
			forma.setDataFile("dtoFacturas_0",archivoPlano.getDtoFacturas(con,forma.getBusquedaArchivosPlanosMap(),3));
			forma.setDataFile("convenio_0",forma.getBusquedaArchivosPlanosMap("convenioCCobro").toString().trim());
			forma.setDataFile("archivo_0",archivoPlano.getPosNombreArchivoPlano(forma.getNombreArchivosPlanosMap(),forma.getBusquedaArchivosPlanosMap("convenio").toString()));
			forma.setDataFile("numRegistros","1");
		}		
		
		logger.info("FIN GENERACION ARCHIVO PLANO COLSANITAS*****************************");		
	}	
	
	
	/**
	 * Guarda la informacion de los archivos planos "fisicamente" en archivos
	 * @param Connection con
	 * @param ArchivoPlanoForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionErrors errores
	 * */
	public ActionErrors guardarArchivosPlanosColsa(Connection con,ArchivoPlanoForm forma, UsuarioBasico usuario, ActionErrors errores)
	{
		
		logger.info("GUARDAR EN MEMORIA ARCHIVO PLANO COLSANITAS*****************************");	
		
		ArchivoPlano archivoPlano = new ArchivoPlano();		
		
		errores = archivoPlano.guardarArchivosPlanosColsa(forma.getDataFile(),forma.getNombreArchivosPlanosMap(),errores);
		
		if(!errores.isEmpty())
			return errores;
		
		//Actualiza el mapa de Nombres de Archivos Planos con los nuevos archivos creados por inconsistencias
		HashMap tmp;
		tmp =archivoPlano.guardarArchivosInconsistencia(forma.getNombreArchivosPlanosMap(),archivoPlano.getInconsistenciasArray());
		
		if(tmp != null)
			forma.setNombreArchivosPlanosMap(tmp);	
		else
			logger.info("NO EXISTEN INCONSISTENCIAS");
		
		logger.info("valor hashMap nombre archivos planos >> "+forma.getNombreArchivosPlanosMap());
		
		//Genera los archivos .zip de los archivos generados 
		for(int i = 0 ; i < Utilidades.convertirAEntero(forma.getNombreArchivosPlanosMap("numRegistros").toString()); i++)
		{
			forma.setNombreArchivosPlanosMap("existeZip_"+i,ConstantesBD.acronimoNo);
			
			if(forma.getNombreArchivosPlanosMap("esGenerado_"+i).toString().equals(ConstantesBD.acronimoSi))
				forma.setNombreArchivosPlanosMap("existeZip_"+i,ArchivoPlano.generarArchivoZip(forma.getNombreArchivosPlanosMap("ruta_"+i).toString(),forma.getNombreArchivosPlanosMap("nombreArchivo_"+i).toString())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		}
				
		logger.info("FIN GUARDAR EN MEMORIA ARCHIVO PLANO COLSANITAS*****************************");		
		
		return errores;	
	}
	
	
	
	/**
	 * Insertar historial de Busqueda
	 * @param Connection con
	 * @param ArchivoPlanoForm  forma
	 * @param UsuarioBasico usuario
	 * */
	public boolean insertarHistorialArchivosPlanos(Connection con,
			ArchivoPlanoForm forma,
			UsuarioBasico usuario)
	{	
		String cadenaNumeroEnvios = "";
		int i = 0, numRegistros = 0;		
		
		numRegistros = Utilidades.convertirAEntero(forma.getNumeroEnviosMap("numRegistros").toString());
		
		//almacena la informacion de la busqueda en el historial	
		forma.setBusquedaArchivosPlanosMap("institucion",usuario.getCodigoInstitucionInt());		
				
		if(forma.getBusquedaArchivosPlanosMap("convenio").toString().equals(ConstantesBD.codigoNuncaValido+"") ||
				forma.getBusquedaArchivosPlanosMap("convenio").equals(ConstantesBD.codigoNuncaValido))
			forma.setBusquedaArchivosPlanosMap("convenio","");
					
		if(forma.getBusquedaArchivosPlanosMap("interfazRips").toString().equals(ConstantesBD.acronimoSi))
		{
			for(i=0; i<numRegistros; i++)
			{				
				if(forma.getNumeroEnviosMap().containsKey("numeroEnvio_"+i))
					cadenaNumeroEnvios+=forma.getNumeroEnviosMap("numeroEnvio_"+i).toString()+",";
			}
				
			forma.setBusquedaArchivosPlanosMap("numeroEnvio",cadenaNumeroEnvios);			
		}	
		else
			forma.setBusquedaArchivosPlanosMap("numeroEnvio","");
		
		
		if(!forma.getBusquedaArchivosPlanosMap("fechaInicial").toString().equals(""))
			forma.setBusquedaArchivosPlanosMap("fechaInicialABD",UtilidadFecha.conversionFormatoFechaABD(forma.getBusquedaArchivosPlanosMap("fechaInicial").toString()));
		else
			forma.setBusquedaArchivosPlanosMap("fechaInicialABD","");
		
		
		if(!forma.getBusquedaArchivosPlanosMap("fechaFinal").toString().equals(""))
			forma.setBusquedaArchivosPlanosMap("fechaFinalABD",UtilidadFecha.conversionFormatoFechaABD(forma.getBusquedaArchivosPlanosMap("fechaFinal").toString()));
		else
			forma.setBusquedaArchivosPlanosMap("fechaFinalABD","");
			
			
		
		forma.setBusquedaArchivosPlanosMap("fechaEnvioABD",UtilidadFecha.conversionFormatoFechaABD(forma.getBusquedaArchivosPlanosMap("fechaEnvio").toString()));
		forma.setBusquedaArchivosPlanosMap("usuarioModifica",usuario.getLoginUsuario());
		forma.setBusquedaArchivosPlanosMap("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.setBusquedaArchivosPlanosMap("horaModifica",UtilidadFecha.getHoraActual());
		
		logger.info("INSERTAR HISTORIAL ARCHIVOS PLANOS *****************");
		logger.info("\n\n\n\n\n");
		
		if(ArchivoPlano.setHistorialArhivosPlanos(con,forma.getBusquedaArchivosPlanosMap()))
		{
			logger.info("inserto Historial Busqueda");
			return true;			
		}
		
		return false;
	}
	
	
	
	//**********************METODOS DE SOPORTE********************************
	//************************************************************************
	
	/**
	 * Carga el detalle de un archivo seleccionado
	 * @param ArchivoPlanoForm
	 * */
	public void cargarDetalleArchivoPlano(ArchivoPlanoForm forma,String prefijo)
	{
		ArchivoPlano archivoPlano = new ArchivoPlano();
		
		forma.setContenidoArchivo(archivoPlano.cargarArchivo(forma.getNombreArchivosPlanosMap(),prefijo));
				
		forma.setEstado("detalleArchivo");
	}
	
	
	/**
	 * Consulta las Cuentas de Cobro y retorna la respuesta por ajax 
	 * @param Connection con
	 * @param ArchivoPlanoForm forma 
	 * */
	public ActionForward buscarCuentasCobro(
						Connection con,
						ArchivoPlanoForm forma,
						UsuarioBasico usuario,
						HttpServletResponse response)
	{
		
		HashMap resultadoMap = new HashMap();
		boolean exito = false;
		
		//incializa los valores de la busqueda
		resultadoMap.put("institucion",usuario.getCodigoInstitucionInt());		
		resultadoMap.put("cuentaCobro",forma.getNumeroCuentaCobro());		
		
		if(!forma.getNumeroCuentaCobro().equals(""))
		{
			resultadoMap = ArchivoPlano.getCuentasCobro(con, resultadoMap);
		
			if(!resultadoMap.get("numRegistros").equals("0"))
			{		
				exito = true;
				forma.setBusquedaArchivosPlanosMap("numeroCuentaCobro",forma.getNumeroCuentaCobro());
			}
			else
			{
				exito = false;
				forma.setBusquedaArchivosPlanosMap("numeroCuentaCobro","");							
			}
		}
		
		
		UtilidadBD.closeConnection(con);
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{			
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			String respuesta = "<respuesta>";
			if(exito)
			{
				respuesta += "<codigo-convenio>"+resultadoMap.get("convenio")+"</codigo-convenio>"+
				"<nombre-convenio>"+resultadoMap.get("descripcion")+"</nombre-convenio>";				
			}
			else
			{
				respuesta += "<codigo-convenio>0</codigo-convenio>"+
				"<nombre-convenio>VACIO</nombre-convenio>";				
			}
			respuesta += "</respuesta>";
			response.getWriter().flush();
	        response.getWriter().write(respuesta);        
	        response.getWriter().close();
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionBuscarCuentaCobro: "+e);
		}		
		
		return null;
	}	
	
	
	/**
	 * Ingresa un numero de envio
	 * @param ArchivoPlanoForm forma
	 * */
	public void ingresarNumeroEnvio(ArchivoPlanoForm forma)
	{
		//captura la posicion dentro del arraylist que se desea ingresar en el mapa		
		int posArray = Utilidades.convertirAEntero(forma.getNumeroEnviosMap("indicadorPos").toString());
		
		//verifica que el indicador de posicion sea valida
		if(!forma.getNumeroEnviosMap("indicadorPos").toString().equals("") 
				&& !forma.getNumeroEnviosMap("indicadorPos").toString().equals(ConstantesBD.codigoNuncaValido+"")
					&& posArray >= 0 )
		{
			
			int numRegistros = Utilidades.convertirAEntero(forma.getNumeroEnviosMap("numRegistros").toString());			
			
			forma.setNumeroEnviosMap("convenio_"+numRegistros,forma.getInterfazRips().get(posArray).getCodigoConvenio().trim());
			forma.setNumeroEnviosMap("numeroEnvio_"+numRegistros,forma.getInterfazRips().get(posArray).getNumeroEnvio().trim());
			forma.setNumeroEnviosMap("descripcionConvenio_"+numRegistros,forma.getInterfazRips().get(posArray).getDescripcionConvenio());
			forma.setNumeroEnviosMap("indexArray_"+numRegistros,posArray);
			
			numRegistros++;			
			forma.setNumeroEnviosMap("numRegistros",numRegistros);			
	
		}
	}
	
	
	/**
	 * Elimina un registro del mapa de Numeros de Envio	
	 * @param ArchivoPlanoForm forma
	 * */
	public void eliminarNumeroEnvio(ArchivoPlanoForm forma)
	{		
		String[] indices = {"convenio_","numeroEnvio_","descripcionConvenio_","indexArray_"};
		Utilidades.eliminarRegistroMapaGenerico(forma.getNumeroEnviosMap(),forma.getNumeroEnviosMap(),Utilidades.convertirAEntero(forma.getNumeroEnviosMap("indexEliminado").toString()),indices, "numRegistros", "estabd_",ConstantesBD.acronimoNo, false);
		forma.setNumeroEnviosMap("indexEliminado","");	
		forma.setNumeroEnviosMap("indicadorPos","");
	}
}