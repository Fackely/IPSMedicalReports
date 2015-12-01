/*
 * Junio 27, 2006
 */
package com.princetonsa.action.interfaz;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.interfaz.GeneracionInterfazForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.interfaz.CampoInterfaz;
import com.princetonsa.mundo.interfaz.GeneracionInterfaz;
import com.princetonsa.mundo.interfaz.ParamInterfaz;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author sgomez
 *	 Action usado para controlar los procesos de generación de interfaz
 * 	facturación
 */
public class GeneracionInterfazAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GeneracionInterfazAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{


		Connection con=null;
		//SE ABRE CONEXION

		try{


			if (response==null); //Para evitar que salga el warning
			if(form instanceof GeneracionInterfazForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				GeneracionInterfazForm interfazForm =(GeneracionInterfazForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=interfazForm.getEstado(); 
				logger.warn("estado GeneracionInterfazAction-->"+estado);


				if(estado == null)
				{
					interfazForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Generacion de Interfaz Facturación (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,interfazForm,mapping);
				}
				else if (estado.equals("parametrizar"))
				{
					return accionParametrizar(con,interfazForm,mapping,usuario,request);
				}
				else if (estado.equals("validar"))
				{
					return accionValidar(con,interfazForm,mapping,usuario,request);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,interfazForm,response,mapping,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,interfazForm,mapping);
				}
				else
				{
					interfazForm.reset();
					logger.warn("Estado no valido dentro del flujo de Generacion Interfaz Facturación (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					return mapping.findForward("paginaError");
				}
			}			
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;	
}
	
	
	/**
	 * Método implementado para ordenar el listado de los registros
	 * del archivo de salida generado
	 * @param con
	 * @param interfazForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, GeneracionInterfazForm interfazForm, ActionMapping mapping) 
	{
		
		int numeroElementos = interfazForm.getNumMapArchivo();
		int numCampos = Integer.parseInt(interfazForm.getMapArchivo("numCampos").toString());
		HashMap mapaAux = interfazForm.getMapArchivo();
		
		
		//columnas del listado
		String[] indices= new String[numCampos];
		for(int i=0;i<numCampos;i++)
			indices[i] = i+"_";
		
		interfazForm.setMapArchivo(Listado.ordenarMapa(indices,
				interfazForm.getIndice(),
				interfazForm.getUltimoIndice(),
				interfazForm.getMapArchivo(),
				interfazForm.getNumMapArchivo()));
		
		interfazForm.setMapArchivo("numMapArchivo",numeroElementos+"");
		interfazForm.setMapArchivo("numCampos",numCampos+"");
		
		//se reestablecen los encabezados del mapa
		for(int i=0;i<numCampos;i++)
			interfazForm.setMapArchivo("titulo_"+i,mapaAux.get("titulo_"+i));
		
		interfazForm.setUltimoIndice(interfazForm.getIndice());
		
		this.cerrarConexion(con);
		return mapping.findForward("resultado");
	}


	/**
	 * Método implementado para paginar el listado de 
	 * registros generados en el archivo de salida
	 * @param con
	 * @param interfazForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, GeneracionInterfazForm interfazForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(interfazForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de GeneracionInterfazAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en GeneracionInterfazAction", "errors.problemasDatos", true);
		}
	}


	/**
	 * Método implementado para realizar validaciones antes de iniciar el proceso
	 * de generar la interfaz
	 * @param con
	 * @param interfazForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionValidar(Connection con, GeneracionInterfazForm interfazForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws IPSException 
	{
		//Se inicializa el vector de mensajes
		interfazForm.setConfirmaciones(new ArrayList());
		
		//******VERIFICAR SI ARCHIVOS EXISTEN PARA SOLICITAR CONFIRMACIÓN*********************
		if(!interfazForm.isConfirmacionArchivosExistentes())
		{
			boolean huboExistentes = false;
			//se define el tipo de confirmacion
			interfazForm.setTipoConfirmacion("archivo");
			
			//Se verifica archivo de salida
			File archivo = new File(interfazForm.getPathSalida());
			if(archivo.exists())
			{
				huboExistentes = true;
				interfazForm.getConfirmaciones().add("El nombre del archivo de salida ya existe en el sistema y es posible sobreescribirlo.");
			}
			
			//Se verifica archivo de inconsistencias
			archivo = new File(interfazForm.getPathInconsistencias());
			if(archivo.exists())
			{
				huboExistentes = true;
				interfazForm.getConfirmaciones().add("El nombre del archivo de inconsistencias ya existe en el sistema y es posible sobreescribirlo.");
			}
			
			if(huboExistentes)
			{
				this.cerrarConexion(con);
				return mapping.findForward("confirmacion");
			}
			
		}
		//*************************************************************************************
		
		//***********CONSULTAR INFORMACIÓN DE FACTURAS/ANULACIONES*****************************
		GeneracionInterfaz interfaz = new GeneracionInterfaz();
		//se asignan campos de parametrizacion al mundo
		interfaz.setCampos("tipoInterfaz",interfazForm.getTipoInterfaz());
		interfaz.setCampos("institucion",usuario.getCodigoInstitucion());
		interfaz.setCampos("fechaInicial",interfazForm.getFechaInicial());
		interfaz.setCampos("fechaFinal",interfazForm.getFechaFinal());
		interfaz.setCampos("documentoInicial",interfazForm.getDocumentoInicial());
		interfaz.setCampos("documentoFinal",interfazForm.getDocumentoFinal());
		interfaz.setCampos("convenio",interfazForm.getConvenio()>0?interfazForm.getConvenio()+"":"");
		
		//se consulta la informacion de facturas
		interfazForm.setFacturas(interfaz.consultarFacturasAnulaciones(con));
		interfazForm.setNumFacturas(Integer.parseInt(interfazForm.getFacturas("numRegistros").toString()));
		
		
		//se verifica si existe información de facturas
		if(interfazForm.getNumFacturas()<=0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("sin informacion de facturas / anulaciones",new ActionMessage("error.interfaz.generacionInterfaz.sinInfoFacturas"));
			
			interfazForm.setEstado("parametrizar");
			
			saveErrors(request,errores);
			this.cerrarConexion(con);
			return mapping.findForward("paginaInicial");
		}
		
		//***************************************************************************************
		
		//*****CONSULTAR GENERACIONES DE INTERFAZ PREVIAS**************************************
		if(!interfazForm.isConfirmacionGeneracionesAnteriores())
		{
			boolean huboGeneraciones = false;
			//se define el tipo de confirmacion
			interfazForm.setTipoConfirmacion("generacion");
			
			HashMap generaciones = interfaz.consultarGeneracionesInterfazPrevias(con);
			for(int i=0;i<Integer.parseInt(generaciones.get("numRegistros").toString());i++)
			{
				huboGeneraciones = true;
				String mensaje = "Ya se ha generado interfaz para la fecha "+UtilidadFecha.conversionFormatoFechaAAp(generaciones.get("fecha_grabacion_"+i).toString())+
					" con el usuario "+generaciones.get("usuario_"+i)+" entre los rangos "+UtilidadFecha.conversionFormatoFechaAAp(generaciones.get("fecha_inicial_"+i).toString())+
					" y "+UtilidadFecha.conversionFormatoFechaAAp(generaciones.get("fecha_final_"+i).toString());
				interfazForm.getConfirmaciones().add(mensaje);
					
			}
			
			if(huboGeneraciones)
			{
				this.cerrarConexion(con);
				return mapping.findForward("confirmacion");
			}
		}
		//**************************************************************************************
		
		//SI SE PASAN TODAS LAS VALIDACIONES SE VA DIRECTO A GENERAR
		return accionGenerar(con,interfazForm,mapping,usuario,request);
	}
	
	
	/**
	 * Método implementado para generar la interfaz 
	 * @param con
	 * @param interfazForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGenerar(Connection con, GeneracionInterfazForm interfazForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws IPSException 
	{
		//Se instancia objeto de GeneracionInterfaz
		GeneracionInterfaz interfaz = new GeneracionInterfaz();
		
		//se carga información general de parametrización de búsqueda
		this.cargarInformacionGeneral(interfaz,interfazForm,usuario);
		
		//se carga la parametrización general de los campos interfaz
		this.cargarParametrosCamposInterfaz(con,interfaz,usuario);
		
		//generación de la interfaz
		interfazForm.setExitoGeneracion(interfaz.generarInterfaz(con));
		
		//se verifica éxito de la generación
		if(!interfazForm.isExitoGeneracion())
		{
			saveErrors(request,interfaz.getErrores());
			//SE CARGA ARCHIVO DE INCONSISTENCIAS
			if(!interfaz.cargarArchivoInconsistencias())
				saveErrors(request,interfaz.getErrores());
		}
		else
		{
			//SE CARGA ARCHIVO DE SALIDA
			if(!interfaz.cargarArchivoSalida())
				saveErrors(request,interfaz.getErrores());
		}
		
		//se toma fecha / hora generacion interfaz
		interfazForm.setFechaGeneracion(UtilidadFecha.getFechaActual());
		interfazForm.setHoraGeneracion(UtilidadFecha.getHoraActual());
		///se consulta el nombre del convenio
		if(interfazForm.getConvenio()>0)
		{
			Convenio convenio = new Convenio();
			convenio.cargarResumen(con,interfazForm.getConvenio());
			interfazForm.setNombreConvenio(convenio.getNombre());
		}
		//se toma el maxpageItems
		interfazForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		
		interfazForm.setMapArchivo(interfaz.getMapArchivo());
		interfazForm.setNumMapArchivo(interfaz.getNumMapArchivo());
		
		this.cerrarConexion(con);
		return mapping.findForward("resumen");
	}


	/**
	 * Método implementado para cargar la información general de los campos interfaz
	 * @param con
	 * @param interfaz
	 * @param usuario
	 */
	private void cargarParametrosCamposInterfaz(Connection con, GeneracionInterfaz interfaz, UsuarioBasico usuario) 
	{
		//Se consulta la informacion general de la parametrizacion de campos----------------------------------------------
		CampoInterfaz campoInterfaz = new CampoInterfaz();
		campoInterfaz.cargarInformacionGral(con,usuario.getCodigoInstitucionInt());
		interfaz.setSeparadorCampos(campoInterfaz.getSeparadorCampos());
		interfaz.setSeparadorDecimales(campoInterfaz.getSeparadorDecimales());
		interfaz.setIdentificadorFinArchivo(campoInterfaz.getIdentificadorFinArchivo());
		interfaz.setPresentaDevolucionPaciente(UtilidadTexto.getBoolean(campoInterfaz.getPresentaDevolucionPaciente()));
		interfaz.setValorNegativoDevolPaciente(UtilidadTexto.getBoolean(campoInterfaz.getValorNegativoDevolPaciente()));
		interfaz.setDescripcionDebito(campoInterfaz.getDescripcionDebito());
		interfaz.setDescripcionCredito(campoInterfaz.getDescripcionCredito());
		
	}


	/***
	 * Método implementado para cargar al objeto GeneracionInterfaz la información
	 * básica que se parametrizó.
	 * @param interfaz
	 * @param interfazForm
	 * @param usuario
	 */
	private void cargarInformacionGeneral(GeneracionInterfaz interfaz, GeneracionInterfazForm interfazForm, UsuarioBasico usuario) 
	{
		//se asigna la informacion de busqueda parametrizada
		interfaz.setFechaInicial(interfazForm.getFechaInicial());
		interfaz.setFechaFinal(interfazForm.getFechaFinal());
		interfaz.setCodigoTipoInterfaz(interfazForm.getTipoInterfaz());
		switch(interfazForm.getTipoInterfaz())
		{
			case ConstantesBD.tipoInterfazFacturacion:
				interfaz.setTipoInterfaz("Facturación");
				interfazForm.setNombreTipoInterfaz("Facturación");
			break;
			case ConstantesBD.tipoInterfazAnulacion:
				interfaz.setTipoInterfaz("Anulación");
				interfazForm.setNombreTipoInterfaz("Anulación");
			break;
			case ConstantesBD.tipoInterfazAmbos:
				interfaz.setTipoInterfaz("Ambos");
				interfazForm.setNombreTipoInterfaz("Ambos");
			break;
		}
		interfaz.setUsuarioResponsable(usuario.getLoginUsuario());
		interfaz.setDocumentoInicial(interfazForm.getDocumentoInicial());
		interfaz.setDocumentoFinal(interfazForm.getDocumentoFinal());
		
		
		
		//se asigna la institucion
		interfaz.setInstitucion(usuario.getCodigoInstitucion());
		
		///se asigna la informacion de los PATH -------------------------------------
		String aux = interfazForm.getPathSalida();
		int indice = interfazForm.getPathSalida().lastIndexOf(System.getProperty("file.separator"));
		interfaz.setPathArchivo(aux.substring(0,indice));
		interfaz.setNombreArchivo(aux.substring(indice+1,aux.length()));
		
		aux = interfazForm.getPathInconsistencias();
		indice = interfazForm.getPathInconsistencias().lastIndexOf(System.getProperty("file.separator"));
		interfaz.setPathInconsistencias(aux.substring(0,indice));
		interfaz.setNombreInconsistencias(aux.substring(indice+1,aux.length()));
		
		//Se cargan datos de FACTURAS y REGISTROS --------------------------------------------------------------
		interfaz.setRegistros(interfazForm.getRegistros());
		interfaz.setNumRegistros(interfazForm.getNumRegistros());
		interfaz.setFacturas(interfazForm.getFacturas());
		interfaz.setNumFacturas(interfazForm.getNumFacturas());
		
		
	}


	/**
	 * Método que verifica que se tengan registros parametrizados para el
	 * tipo de interfaz definida a generar
	 * @param con
	 * @param interfazForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionParametrizar(Connection con, GeneracionInterfazForm interfazForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		// Se instancia objeto ParamInterfaz
		ParamInterfaz paramInterfaz = new ParamInterfaz();
		//se consultan los registros parametrizados
		Collection colRegistros = paramInterfaz.listadoRegistrosInterfaz(con,usuario.getCodigoInstitucionInt(),interfazForm.getTipoInterfaz(),true);
		
		//se verifica si hay registros parametrizados
		if(colRegistros.size()<=0)
		{
			if(interfazForm.getTipoInterfaz()>0)
			{
				ActionErrors errores = new ActionErrors();
				errores.add("Sin informacion de Registros",new ActionMessage("error.interfaz.generacionInterfaz.NoExisteParamRegistros"));
				saveErrors(request,errores);
			}
			
			//se actualiza estado
			interfazForm.setEstado("empezar");
			
		}
		else
		{
			//Se consulta la informacion general de la parametrizacion de campos
			CampoInterfaz campoInterfaz = new CampoInterfaz();
			campoInterfaz.cargarInformacionGral(con,usuario.getCodigoInstitucionInt());
			
			
			
			//se asigna el path de salida segun parametrizacion de Campos Interfaz
			if(interfazForm.getPathSalida().equals(""))
				interfazForm.setPathSalida(campoInterfaz.getPathArchivoSalida()+
					(campoInterfaz.getPathArchivoSalida().endsWith(System.getProperty("file.separator"))?"":System.getProperty("file.separator"))+
					campoInterfaz.getNombreArchivoSalida());
			
			//se asigna el path de inconsistencias segun parametrizacion de Campos Interfaz
			if(interfazForm.getPathInconsistencias().equals(""))
				interfazForm.setPathInconsistencias(
					campoInterfaz.getPathArchivoInconsistencias()+
					(campoInterfaz.getPathArchivoInconsistencias().endsWith(System.getProperty("file.separator"))?"":System.getProperty("file.separator"))+
					campoInterfaz.getNombreArchivoInconsistencias());
			
			
			interfazForm.setEstado("parametrizar");
			
			//Se cargan los registros al mapa de la forma
			int contador = 0;
			Iterator iterador = colRegistros.iterator();
			while(iterador.hasNext())
			{
				HashMap registroDB = (HashMap)iterador.next();
				interfazForm.setRegistros("codigo_"+contador,registroDB.get("codigo"));
				interfazForm.setRegistros("consecutivo_"+contador,registroDB.get("consecutivo"));
				interfazForm.setRegistros("descripcion_"+contador,registroDB.get("descripcion"));
				interfazForm.setRegistros("codigoTipoRegistro_"+contador,registroDB.get("codigo_tipo_registro"));
				interfazForm.setRegistros("nombreTipoRegistro_"+contador,registroDB.get("tipo_registro"));
				interfazForm.setRegistros("codigoTipoInterfaz_"+contador,registroDB.get("tipo_interfaz"));
				
				contador++;
			}
			interfazForm.setRegistros("numRegistros",contador+"");
			interfazForm.setNumRegistros(contador);
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaInicial");
	}

	/**
	 * Método implementado para postular el inicio del flujo de la generación de interfaz 
	 * @param con
	 * @param interfazForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, GeneracionInterfazForm interfazForm, ActionMapping mapping) 
	{
		interfazForm.reset();
		this.cerrarConexion(con);
		return mapping.findForward("paginaInicial");
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	
}
