/*
 * Marzo 16, 2006
 */
package com.princetonsa.action.salasCirugia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.cargos.EsquemasTarifariosForm;
import com.princetonsa.actionform.carteraPaciente.DocumentosGarantiaForm;
import com.princetonsa.actionform.salasCirugia.ExTarifasAsociosForm;
import com.princetonsa.actionform.salasCirugia.PorcentajesCxMultiplesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.salasCirugia.ExTarifasAsocios;
/** 
 * @author Sebastián Gómez 
 * @author Modificado Nov 07 Jose Eduardo Arias Doncel
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Excepciones Tarifas Asocios
 */
public class ExTarifasAsociosAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ExTarifasAsociosAction.class);
	
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
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ExTarifasAsociosForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				ExTarifasAsociosForm excepcionForm =(ExTarifasAsociosForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=excepcionForm.getEstado();
				ActionErrors errores = new ActionErrors();
				logger.warn("estado Excepcion Tarifas Asocios-->"+estado);


				if(estado == null)
				{
					excepcionForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Excepciones Tarifas Asocios (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				//*******ESTADOS DE INGRESAR/MODIFICAR ENCABEZADO**********************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,excepcionForm,mapping,usuario);
				}
				else if (estado.equals("busqueda")) //estado que es llamado cuando se selecciona un convenio
				{
					accionBusquedaEncabezado(con,excepcionForm,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				
				}
				else if (estado.equals("nuevoEncabezado"))
				{
					return accionNuevoEncabezado(excepcionForm,request,mapping);
				}
				else if (estado.equals("guardarEncabezado")) //inserción y actualización de registros
				{
					errores = accionValidarGuardarEncabezado(excepcionForm,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}

					return accionGuardarEncabezado(con,excepcionForm,mapping,usuario,request);
				}
				else if (estado.equals("eliminarEncabezado"))
				{
					return accionEliminarEncabezado(con,excepcionForm,mapping,usuario,request);
				}

				//*******ESTADOS DE INGRESAR/MODIFICAR MEDIO**********************
				else if(estado.equals("buscarMedia") || estado.equals("buscarAvanzadaMedia"))
				{
					accionBusquedaMedio(con,excepcionForm,usuario);				
					UtilidadBD.closeConnection(con);				
					return mapping.findForward("medio");				 
				}
				else if(estado.equals("irVigencia"))
				{				
					excepcionForm.setBusquedaAvanzadaMap(new HashMap());
					accionBusquedaEncabezado(con,excepcionForm,usuario);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevoMedia"))
				{
					return accionNuevoMedia(excepcionForm,mapping,request);
				}
				else if (estado.equals("guardarMedia")) //inserción y actualización de registros
				{
					errores = accionValidarGuardarMedio(excepcionForm,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("medio");
					}

					return accionGuardarMedio(con,excepcionForm,mapping,usuario,request);
				}
				else if(estado.equals("eliminarMedia"))
				{
					return accionEliminarMedio(con, excepcionForm, mapping, usuario, request);
				}
				else if(estado.equals("cambiarTipo"))				
				{
					accionCambiarTipo(con,excepcionForm,response);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				//*******ESTADOS DE INGRESAR/MODIFICAR DETALLE**********************

				//Detalle de Excepciones Tarifas Asocios
				else if(estado.equals("irDetalle"))
				{
					accionBuscarDetalle(con,excepcionForm,usuario); 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("nuevoDetalle"))
				{
					errores = accionNuevoDetalle(excepcionForm);

					if(!errores.isEmpty())
						saveErrors(request,errores);								

					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarDetalle"))
				{
					return accionEliminarDetalle(con,excepcionForm,mapping,usuario,request);
				}
				else if(estado.equals("guardarDetalle"))
				{
					errores = accionValidarGuardarDetalle(excepcionForm,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalle");
					}

					return accionGuardarDetalle(con,excepcionForm, mapping, usuario, request);
				}			
				else if(estado.equals("irMedia"))
				{
					excepcionForm.setBusquedaAvanzadaMap(new HashMap());
					accionBusquedaMedio(con,excepcionForm,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("medio");
				}
				else if(estado.equals("buscarAvanzadaDetalle"))
				{
					accionBuscarDetalle(con,excepcionForm,usuario); 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");					
				}
				else if(estado.equals("buscarEspecialidad") || estado.equals("buscarGrupoServicio"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busqueda");				
				}				
				else
				{
					excepcionForm.reset();
					logger.warn("Estado no valido dentro del flujo de Excepciones Tarifas Asocios (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
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
	
	
	//************************************************************************************
	//******************METODOS DEL ENCABEZADO *******************************************
	//************************************************************************************
	
	
	
	/**
	 * Método que inicia la funcionalidad
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ExTarifasAsociosForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		
		//se resetea Form
		forma.reset();		
		excepcion.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setInstitucion(usuario.getCodigoInstitucion());		 
		forma.setCentroCostoMap(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(),ConstantesBD.codigoTipoAreaDirecto+"",false,usuario.getCodigoCentroAtencion(),false));
		
		
		//carga la informacion de las vias de ingreso y el respectivo HashMap de Tipo paciente
		forma.setViaIngresoMap((HashMap)Utilidades.obtenerViasIngreso(con,false));
		forma.setViaIngresoMap(excepcion.cargarTipoPaciente(con,forma.getViaIngresoMap()));
		
		//logger.info(" \n\n valir del vias de ingreso >> "+forma.getViaIngresoMap());
		
		
		//Carga mapas basicos para la funcionalidad
		forma.setTipoPacienteMap(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con,"1"));
		
		//Detalle		
		forma.setTipoServicioArray(UtilidadesFacturacion.obtenerTiposServicio(con,"'"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioPartosCesarea+"','"+ConstantesBD.codigoServicioNoCruentos+"'",""));
		//logger.info("tamaño del array >> "+forma.getTipoServicioArray().size());
		forma.setEspecialidadMap(Utilidades.obtenerEspecialidades());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Método que consulta los registros parametrizados en excepcion tarifas asocios
	 * según el convenio
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private void accionBusquedaEncabezado(Connection con, ExTarifasAsociosForm forma,UsuarioBasico usuario) 
	{
		//Instanciación de ExTarifasAsocios
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		HashMap parametros  = new HashMap();
		
		//Carga datos Basicos
		excepcion.setCodigoConvenio(forma.getConvenio());		
		
		//se consultan los registros
		parametros.put("convenio",forma.getConvenio());		
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		forma.setEncabezadoMap(excepcion.consultarEncabezado(con, parametros));
		
		//logger.info("valor del mapa encabezado >> "+forma.getEncabezadoMap());	
	}
	
	
	/**
	 * Ingresa un nuevo campo al mapa
	 * @param EsquemasTarifariosForm forma
	 * @param ActionMapping mapping
	 * */
	private ActionForward accionNuevoEncabezado(ExTarifasAsociosForm forma, HttpServletRequest reques, ActionMapping mapping)
	{
		int numRegistros = Integer.parseInt(forma.getEncabezadoMap("numRegistros").toString());
		
		for(int i=0; i<numRegistros; i++)
		{
			if(forma.getEncabezadoMap("estabd_"+i).toString().equals(ConstantesBD.acronimoNo))
			{
				ActionErrors errores =  new ActionErrors();
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Guardar el registro Nro. "+(i+1)+" antes de adicionar un Nuevo Registro."));
				saveErrors(reques,errores);
				return mapping.findForward("principal");
			}				
		}
		
		forma.setEncabezadoMap("codigo_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setEncabezadoMap("institucion_"+numRegistros,forma.getInstitucion());
		forma.setEncabezadoMap("convenio_"+numRegistros,forma.getConvenio());
		forma.setEncabezadoMap("nombre_convenio_"+numRegistros,forma.getNombreConvenio());
		forma.setEncabezadoMap("fecha_inicial_"+numRegistros,"");
		forma.setEncabezadoMap("fecha_final_"+numRegistros,"");		
		forma.setEncabezadoMap("cuanto_detalle_"+numRegistros,0);
		forma.setEncabezadoMap("estabd_"+numRegistros,ConstantesBD.acronimoNo);		
		forma.setEncabezadoMap("numRegistros",numRegistros+1);
		return mapping.findForward("principal");		
	}
	
	
	/**
	 * Validaciones para Guardar el Encabezado
	 * @param ExTarifasAsociosForm porcentajesForm
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionValidarGuardarEncabezado(ExTarifasAsociosForm forma,ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getEncabezadoMap("numRegistros").toString());
		
		for(int i = 0; i< numRegistros; i++)
		{		
			
			if(forma.getEncabezadoMap("fecha_inicial_"+i).equals(""))
				errores.add("fechainicial", new ActionMessage("errors.required","La Fecha inicial del registro "+(i+1)));    		
			
    		if(forma.getEncabezadoMap("fecha_final_"+i).equals(""))
    			errores.add("fechafinal", new ActionMessage("errors.required","La Fecha final del registro "+(i+1)));    		

    		if(!forma.getEncabezadoMap("fecha_inicial_"+i).equals("") && 
					!forma.getEncabezadoMap("fecha_final_"+i).equals(""))
    		{
    			if(!UtilidadFecha.validarFecha(forma.getEncabezadoMap("fecha_inicial_"+i)+"") ||
    					!UtilidadFecha.validarFecha(forma.getEncabezadoMap("fecha_final_"+i)+""))
    						errores.add("fechas", new ActionMessage("errors.required","Verifique el Formato de Fechas para el campo "+(i+1)+". "));
    		}
    		
    		if(!forma.getEncabezadoMap("fecha_inicial_"+i).equals("") && 
					!forma.getEncabezadoMap("fecha_final_"+i).equals("") &&
			    		UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getEncabezadoMap("fecha_final_"+i)+""), UtilidadFecha.conversionFormatoFechaAAp(forma.getEncabezadoMap("fecha_inicial_"+i)+"")))
			    			errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final del Registro "+(i+1)+" debe ser Mayor o Igual a la Inicial. ",UtilidadFecha.conversionFormatoFechaAAp(forma.getEncabezadoMap("fecha_inicial_"+i)+"")));

    		if(!forma.getEncabezadoMap("fecha_inicial_"+i).equals("") && 
					!forma.getEncabezadoMap("fecha_final_"+i).equals("")&&
						forma.getEncabezadoMap("estabd_"+i).equals(ConstantesBD.acronimoNo)&&    			    		
							UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getEncabezadoMap("fecha_final_"+i)+""), UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))	    	
	    						errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final del Registro "+(i+1)+" debe ser Mayor a la Fecha del Sistema. ",UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())));
	    	
    		if(!errores.isEmpty())
    			return errores;
    		
			for(int j = i; j < numRegistros; j++)
			{				
				if(!forma.getEncabezadoMap("fecha_inicial_"+i).equals("") && 
						!forma.getEncabezadoMap("fecha_final_"+i).equals("") && 
							!forma.getEncabezadoMap("fecha_inicial_"+j).equals("") && 
								!forma.getEncabezadoMap("fecha_final_"+j).equals("") )
				{
					if(j>i &&  UtilidadFecha.existeTraslapeEntreFechas(
								forma.getEncabezadoMap("fecha_inicial_"+i).toString(),
								forma.getEncabezadoMap("fecha_final_"+i).toString(),
								forma.getEncabezadoMap("fecha_inicial_"+j).toString(),
								forma.getEncabezadoMap("fecha_final_"+j).toString()))
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Existe Cruce de Fechas. la Fecha "+forma.getEncabezadoMap("fecha_inicial_"+i).toString()+" - "+forma.getEncabezadoMap("fecha_final_"+i).toString()+" del Registro Numero "+(i+1)+". Y la Fecha "+forma.getEncabezadoMap("fecha_inicial_"+j).toString()+" - "+forma.getEncabezadoMap("fecha_final_"+j).toString()+" del Registro Numero "+(j+1)+"."));
									return errores;								
								}
				}
			}
		}		
		return errores;	
	}
	
	
	/**
	 * Método que ingresar o actualiza los registros del Encabezado
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */	
	private ActionForward accionGuardarEncabezado(Connection con, 
										ExTarifasAsociosForm forma, 
										ActionMapping mapping, 
										UsuarioBasico usuario, 
										HttpServletRequest request) 
	{
		int auxI0 = 0;
		boolean resp0 = false;
		
		//Se instancia objeto de Excepciones
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		//manejo de errores
		ActionErrors errores = new ActionErrors();
		HashMap parametros = new HashMap();
		UtilidadBD.iniciarTransaccion(con);
		
		int numRegistros = Integer.parseInt(forma.getEncabezadoMap("numRegistros").toString());
		forma.setEstado("otro");
		
		for(int i=0;i<numRegistros && errores.isEmpty(); i++)
		{
			auxI0 = ConstantesBD.codigoNuncaValido;
			//se toma el código del registro
			if(forma.getEncabezadoMap().containsKey("codigo_"+i) &&
					!forma.getEncabezadoMap("codigo_"+i).toString().equals(""))
				auxI0 = Integer.parseInt(forma.getEncabezadoMap("codigo_"+i).toString());
		
			//se verifica si el registro es nuevo o ya existe
			if(auxI0>0)
			{
				//*****************MODIFICACIÓN DE REGISTRO********************************************
				//se consulta registro antiguo
				
				parametros.put("codigo",auxI0);
				parametros.put("institucion",usuario.getCodigoInstitucionInt());
				HashMap registroAntiguo = excepcion.consultarEncabezado(con, parametros);
				
				//logger.info("valor modificar >> actual >> "+forma.getEncabezadoMap()+" >> veijo >> "+registroAntiguo+" >> codigo "+auxI0+" >> i >> "+i);
				
				//se verifica si el registro fue modificado
				if(this.fueModificadoEncabezado(registroAntiguo,forma.getEncabezadoMap(),i))
				{				
					//se actualiza registro
					parametros = this.llenarMapaEncabezado(forma.getEncabezadoMap(), i, usuario);
					resp0 = excepcion.modificarEncabezado(con, parametros);
					
					if(!resp0)
						errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","El registros Nº "+(i+1)+" no pudo ser actualizado. Por favor verifique"));
					else
					{
						//se genera log
						this.generarLogEncabezado(registroAntiguo,forma.getEncabezadoMap(),i,ConstantesBD.tipoRegistroLogModificacion,usuario);
						forma.setEstado("guardarEncabezado");
					}
				}
				
				//**************************************************************************************
			}
			else
			{
				//*****************INSERCIÓN DE REGISTRO*************************************************
				//se inserta registro
				parametros = this.llenarMapaEncabezado(forma.getEncabezadoMap(),i, usuario);				
				resp0 = excepcion.insertarEncabezado(con, parametros);
				
				if(!resp0)
					errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","El registros Nº "+(i+1)+" no pudo ser insertado. Por favor verifique"));
				
				//***************************************************************************************
			}
		}
		
		if(errores.isEmpty())
		{
			forma.setEstado("guardarEncabezado");
			parametros = new HashMap();
			parametros.put("institucion",usuario.getCodigoInstitucionInt());
			parametros.put("convenio",forma.getConvenio());
			forma.setEncabezadoMap(excepcion.consultarEncabezado(con, parametros));
			UtilidadBD.finalizarTransaccion(con);			 
		}
		else		
			UtilidadBD.abortarTransaccion(con);		
		
		saveErrors(request,errores);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Método implementado para verificar si un registro del encabezado fue modificado
	 * con respecto a su original en la BD
	 * @param registroAntiguo
	 * @param registroNuevo
	 * @param pos
	 * @return
	 */
	
	private boolean fueModificadoEncabezado(HashMap registroAntiguo, HashMap registroNuevo, int pos) 
	{
		boolean fueModificado = false;
		
		if(!registroAntiguo.get("fecha_inicial_0").toString().trim().equals(registroNuevo.get("fecha_inicial_"+pos).toString().trim()) ||
				!registroAntiguo.get("fecha_final_0").toString().trim().equals(registroNuevo.get("fecha_final_"+pos).toString().trim()))
			fueModificado = true;
		
		return fueModificado;
	}	
	
	
	/**
	 * @param HashMap datos
	 * @param int pos
	 * */
	private HashMap llenarMapaEncabezado(HashMap datos, int pos, UsuarioBasico usuario)
	{
		HashMap respuesta = new HashMap();
		
		respuesta.put("codigo",datos.get("codigo_"+pos));
		respuesta.put("institucion",usuario.getCodigoInstitucionInt());
		respuesta.put("convenio",datos.get("convenio_"+pos));
		respuesta.put("fecha_inicial",UtilidadFecha.conversionFormatoFechaABD(datos.get("fecha_inicial_"+pos).toString()));
		respuesta.put("fecha_final",UtilidadFecha.conversionFormatoFechaABD(datos.get("fecha_final_"+pos).toString()));
		respuesta.put("usuario_modifica",usuario.getLoginUsuario());
		respuesta.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		respuesta.put("hora_modifica",UtilidadFecha.getHoraActual());
		
		return respuesta;
		
	}
	
	
	/**
	 * Método implementado para generar el LOG Modificacion en el Encabezado
	 * @param registroAntiguo (antiguo)
	 * @param registroNuevo (nuevo)
	 * @param pos
	 * @param tipo
	 * @param usuario
	*/
	private void generarLogEncabezado(HashMap registroAntiguo, HashMap registroNuevo,int pos, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL VIGENCIAS EXCEPCIONES TARIFAS QUIRURGICAS (ENCABEZADO)===== " +
			"\n*  Codigo [" +registroAntiguo.get("codigo_0")+"] "+
			"\n*  Institucion ["+registroAntiguo.get("institucion_0")+"] " +
			"\n*  Convenio ["+registroAntiguo.get("convenio_0")+"] " +
			"\n*  Nombre Convenio ["+registroAntiguo.get("nombre_convenio_0")+"] " +
			"\n*  Fecha Inicial ["+registroAntiguo.get("fecha_inicial_0")+"] " +
			"\n*  Fecha Final ["+registroAntiguo.get("fecha_final_0")+"] " +			
			""  ;
		    
		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN VIGENCIAS EXCEPCIONES TARIFAS QUIRURGICAS (ENCABEZADO)===== " +
		    "\n*  Codigo [" +registroNuevo.get("codigo_"+pos)+"] "+
			"\n*  Institucion ["+registroNuevo.get("institucion_"+pos)+"] " +
			"\n*  Convenio ["+registroNuevo.get("convenio_"+pos)+"] " +
			"\n*  Nombre Convenio ["+registroNuevo.get("nombre_convenio_"+pos)+"] " +
			"\n*  Fecha Inicial ["+registroNuevo.get("fecha_inicial_"+pos)+"] " +
			"\n*  Fecha Final ["+registroNuevo.get("fecha_final_"+pos)+"] " +			
			""  ;
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA VIGENCIAS EXCEPCIONES TARIFAS QUIRURGICAS (ENCABEZADO)===== " +
		    "\n*  Codigo [" +registroNuevo.get("codigo_"+pos)+"] "+
			"\n*  Institucion ["+registroNuevo.get("institucion_"+pos)+"] " +
			"\n*  Convenio ["+registroNuevo.get("convenio_"+pos)+"] " +
			"\n*  Nombre Convenio ["+registroNuevo.get("nombre_convenio_"+pos)+"] " +
			"\n*  Fecha Inicial ["+registroNuevo.get("fecha_inicial_"+pos)+"] " +
			"\n*  Fecha Final ["+registroNuevo.get("fecha_final_"+pos)+"] " +			
			"";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logExcepcionTarifasQuirurgicasCodigo, log, tipo,usuario.getLoginUsuario());
		
	}	
	
	
	/**
	 * Método implementado para eliminar un registro del mapa de encabezados
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */	
	private ActionForward accionEliminarEncabezado(Connection con, 
												   ExTarifasAsociosForm forma, 
												   ActionMapping mapping, 
												   UsuarioBasico usuario, 
												   HttpServletRequest request) 
	{
		int pos = Integer.parseInt(forma.getIndicadorEncabezado().toString());
		int numRegistros = Integer.parseInt(forma.getEncabezadoMap("numRegistros").toString());
		boolean resp0 = true;
		
		//se toma el código del registro
		int auxI0 = Integer.parseInt(forma.getEncabezadoMap("codigo_"+pos).toString());
		
		//Se instancia objeto de Excepciones
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		
		//se instancian errores
		ActionErrors errores = new ActionErrors();
		
		HashMap parametros = new HashMap();
		
		//se verifica si el registro es nuevo o ya existe en la base de datos
		if(auxI0>0)
		{
			//*********ELIMINACION DEL REGISTRO DESDE LA BASE DE DATOS**************			
			parametros.put("codigo",auxI0);
			parametros.put("institucion",usuario.getCodigoInstitucionInt());			
			resp0 =	excepcion.EliminarEncabezado(con, parametros);
			
			if(!resp0)
				errores.add("error al eliminar",new ActionMessage("errors.sinEliminar"));
			else
			{
				//se genera LOG
				this.generarLogEncabezado(null,forma.getEncabezadoMap(),pos,ConstantesBD.tipoRegistroLogEliminacion,usuario);
				forma.setEstado("guardarEncabezado");
			}
			//************************************************************************
		}
		
		
		saveErrors(request,errores);
		//si la eliminación es exitosa se elimina del mapa
		if(resp0)
		{
			//*******ELIMINACIÓN DEL REGISTRO EN EL MAPA***********************
			for(int i=pos;i<(numRegistros-1);i++)
			{
				forma.setEncabezadoMap("codigo_"+i,forma.getEncabezadoMap("codigo_"+(i+1)));
				forma.setEncabezadoMap("institucion_"+i,forma.getEncabezadoMap("institucion_"+(i+1)));
				forma.setEncabezadoMap("convenio_"+i,forma.getEncabezadoMap("convenio_"+(i+1)));
				forma.setEncabezadoMap("nombre_convenio_"+i,forma.getEncabezadoMap("nombre_convenio_"+(i+1)));
				forma.setEncabezadoMap("fecha_inicial_"+i,forma.getEncabezadoMap("fecha_inicial_"+(i+1)));
				forma.setEncabezadoMap("fecha_final_"+i,forma.getEncabezadoMap("fecha_final_"+(i+1)));
				forma.setEncabezadoMap("cuanto_detalle_"+i,forma.getEncabezadoMap("cuanto_detalle_"+(i+1)));
				forma.setEncabezadoMap("estabd_"+i,forma.getEncabezadoMap("estabd_"+(i+1)));
				
			}
			//se elimina último registro
			pos = numRegistros - 1;
			forma.getEncabezadoMap().remove("codigo_"+pos);
			forma.getEncabezadoMap().remove("institucion_"+pos);
			forma.getEncabezadoMap().remove("convenio_"+pos);
			forma.getEncabezadoMap().remove("nombre_convenio_"+pos);
			forma.getEncabezadoMap().remove("fecha_inicial_"+pos);
			forma.getEncabezadoMap().remove("fecha_final_"+pos);
			forma.getEncabezadoMap().remove("cuanto_detalle_"+pos);
			forma.getEncabezadoMap().remove("estabd_"+pos);			
			//******************************************************************
			
			//se actualiza tamaño del mapa			
			forma.setEncabezadoMap("numRegistros",pos+"");
			
			//logger.info("mapa encabezado despues de la eliminacion >> "+forma.getEncabezadoMap());
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	


	//******************METODOS DEL MEDIO ************************************************
	//************************************************************************************
	
	
	

	
	/**
	 * Método que consulta los registros parametrizados en Tarifas Asocios por Via Ingreso, Tipo de Paciente, Centro Costo
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private void accionBusquedaMedio(Connection con, ExTarifasAsociosForm forma, UsuarioBasico usuario) 
	{
		//Instanciación de ExTarifasAsocios
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		HashMap parametros  = new HashMap();		
		
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		parametros.put("activo",ValoresPorDefecto.getValorTrueParaConsultas());
		forma.setGrupoServicioArray(Utilidades.obtenerGrupoServicios(con,parametros));
		
		forma.setTipoCirugiaArray(Utilidades.obtenerTipoCirugia(con, ""));
		forma.setAsocioArray(Utilidades.obtenerAsocios(con, "", "", ""));

						
		//se consultan los registros		
		parametros.put("codigo_encab",forma.getEncabezadoMap("codigo_"+forma.getIndicadorEncabezado()));		
		
		//Se Filtra la consulta Avanzada
		if(forma.getBusquedaAvanzadaMap().containsKey("via_ingresoB") && 
				!forma.getBusquedaAvanzadaMap("via_ingresoB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("via_ingresoB",forma.getBusquedaAvanzadaMap("via_ingresoB"));
				
		if(forma.getBusquedaAvanzadaMap().containsKey("tipo_pacienteB") &&
				!forma.getBusquedaAvanzadaMap("tipo_pacienteB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("tipo_pacienteB",forma.getBusquedaAvanzadaMap("tipo_pacienteB"));
		
		if(forma.getBusquedaAvanzadaMap().containsKey("centro_costoB") &&
				!forma.getBusquedaAvanzadaMap("centro_costoB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("centro_costoB",forma.getBusquedaAvanzadaMap("centro_costoB"));
		
		forma.setMediaMap(excepcion.consultarMedia(con, parametros));
		
		//logger.info("\n valor del mapa medio >> "+forma.getMediaMap()+" BUSQUEDA >> "+forma.getBusquedaAvanzadaMap()+" \n ");	
	}
	
	
	/**
	 * Ingresa un nuevo campo al HashMap Media Excepciones por Vigencia
	 * @param ExTarifasAsociosForm
	 * @param ActionMapping
	 * @param HttpServletRequest request
	 * */
	private ActionForward accionNuevoMedia(ExTarifasAsociosForm forma,ActionMapping mapping,HttpServletRequest request)
	{
		int numRegistros = Integer.parseInt(forma.getMediaMap("numRegistros").toString());
		ActionErrors errores = new ActionErrors();
		
		//Si existen registros nuevos sin guardar no se da respuesta a la peticion
		for(int i = 0; i < numRegistros; i++)
		{
			if(forma.getMediaMap("estabd_"+i).toString().equals(ConstantesBD.acronimoNo))
			{
				errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","Debe Guardar El Registro Nuevo Nro. "+(i+1)+" antes de adicionar un nuevo registro."));
				saveErrors(request,errores);
				return mapping.findForward("medio");
			}
		}	
		
		forma.setMediaMap("codigo_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setMediaMap("codigo_encab_"+numRegistros,forma.getEncabezadoMap("codigo_"+forma.getIndicadorEncabezado()));
		forma.setMediaMap("via_ingreso_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setMediaMap("nombre_via_ingreso_"+numRegistros,"");
		forma.setMediaMap("tipo_paciente_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setMediaMap("nombre_tipo_paciente_"+numRegistros,"");
		forma.setMediaMap("centro_costo_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setMediaMap("nombre_ccosto_"+numRegistros,"");
		forma.setMediaMap("cuanto_detalle_"+numRegistros,0);
		forma.setMediaMap("estabd_"+numRegistros,ConstantesBD.acronimoNo);		
		forma.setMediaMap("numRegistros",numRegistros+1);
		
		return mapping.findForward("medio");
		
	}
	
	
	/**
	 * @param HashMap datos
	 * @param int pos
	 * */
	private HashMap llenarMapaMedia(HashMap datos, int pos, UsuarioBasico usuario)
	{
		HashMap respuesta = new HashMap();
		
		respuesta.put("codigo",datos.get("codigo_"+pos));
		respuesta.put("codigo_encab",datos.get("codigo_encab_"+pos));
		respuesta.put("via_ingreso",datos.get("via_ingreso_"+pos));
		respuesta.put("tipo_paciente",datos.get("tipo_paciente_"+pos));
		respuesta.put("centro_costo",datos.get("centro_costo_"+pos));		
		respuesta.put("usuario_modifica",usuario.getLoginUsuario());
		respuesta.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		respuesta.put("hora_modifica",UtilidadFecha.getHoraActual());
		
		return respuesta;
		
	}
	
	
	/**
	 * Método implementado para eliminar un registro del mapa Medio
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */	
	private ActionForward accionEliminarMedio(Connection con, 
											  ExTarifasAsociosForm forma, 
											  ActionMapping mapping, 
											  UsuarioBasico usuario, 
											  HttpServletRequest request) 
	{
		int pos = Integer.parseInt(forma.getIndicadorMedio().toString());		
		int numRegistros = Integer.parseInt(forma.getMediaMap("numRegistros").toString());
		boolean resp0 = true;
		
		//se toma el código del registro
		int auxI0 = Integer.parseInt(forma.getMediaMap("codigo_"+pos).toString());
		int codigo_encab = Integer.parseInt(forma.getEncabezadoMap("codigo_"+forma.getIndicadorEncabezado()).toString());
		
		//Se instancia objeto de Excepciones
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		
		//se instancian errores
		ActionErrors errores = new ActionErrors();
		
		HashMap parametros = new HashMap();
		
		//se verifica si el registro es nuevo o ya existe en la base de datos
		if(auxI0>0)
		{
			//*********ELIMINACION DEL REGISTRO DESDE LA BASE DE DATOS**************			
			parametros.put("codigo",auxI0);
			parametros.put("codigo_encab",codigo_encab);			
			resp0 =	excepcion.EliminarMedia(con, parametros);
			
			if(!resp0)
				errores.add("error al eliminar",new ActionMessage("errors.sinEliminar"));
			else
			{
				//se genera LOG
				this.generarLogMedio(forma.getEncabezadoMap(),Integer.parseInt(forma.getIndicadorEncabezado()),null,forma.getMediaMap(),pos,ConstantesBD.tipoRegistroLogEliminacion,usuario);
				forma.setEstado("guardarMedio");				
			}
			//************************************************************************
		}
		
		
		saveErrors(request,errores);
		//si la eliminación es exitosa se elimina del mapa
		if(resp0)
		{
			//*******ELIMINACIÓN DEL REGISTRO EN EL MAPA***********************
			for(int i=pos;i<(numRegistros-1);i++)
			{
				forma.setMediaMap("codigo_"+i,forma.getMediaMap("codigo_"+(i+1)));
				forma.setMediaMap("codigo_encab_"+i,forma.getMediaMap("codigo_encab_"+(i+1)));
				forma.setMediaMap("via_ingreso_"+i,forma.getMediaMap("via_ingreso_"+(i+1)));
				forma.setMediaMap("nombre_via_ingreso_"+i,forma.getMediaMap("nombre_via_ingreso_"+(i+1)));
				forma.setMediaMap("tipo_paciente_"+i,forma.getMediaMap("tipo_paciente_"+(i+1)));
				forma.setMediaMap("nombre_tipo_paciente_"+i,forma.getMediaMap("nombre_tipo_paciente_"+(i+1)));
				forma.setMediaMap("centro_costo_"+i,forma.getMediaMap("centro_costo_"+(i+1)));				
				forma.setMediaMap("nombre_ccosto_"+i,forma.getMediaMap("nombre_ccosto_"+(i+1)));
				forma.setMediaMap("cuanto_detalle_"+i,forma.getMediaMap("cuanto_detalle_"+(i+1)));
				forma.setMediaMap("estabd_"+i,forma.getMediaMap("estabd_"+(i+1)));
				
			}
			//se elimina último registro
			pos = numRegistros - 1;
			forma.getMediaMap().remove("codigo_"+pos);
			forma.getMediaMap().remove("codigo_encab_"+pos);
			forma.getMediaMap().remove("via_ingreso_"+pos);
			forma.getMediaMap().remove("nombre_via_ingreso_"+pos);
			forma.getMediaMap().remove("tipo_paciente_"+pos);
			forma.getMediaMap().remove("nombre_tipo_paciente_"+pos);
			forma.getMediaMap().remove("centro_costo_"+pos);
			forma.getMediaMap().remove("nombre_ccosto_"+pos);			
			forma.getMediaMap().remove("cuanto_detalle_"+pos);
			forma.getMediaMap().remove("estabd_"+pos);			
			//******************************************************************
			
			//se actualiza tamaño del mapa			
			forma.setMediaMap("numRegistros",pos+"");
			
			//logger.info("mapa encabezado despues de la eliminacion MEDIO >> "+forma.getMediaMap());
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("medio");
	}
	
	
	/**
	 * Validaciones para Guardar las Excepciones por Vigencias
	 * @param ExTarifasAsociosForm forma
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionValidarGuardarMedio(ExTarifasAsociosForm forma,ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getMediaMap("numRegistros").toString());		
		
		for(int i = 0; i< numRegistros; i++)
		{
			for(int k = 0 ; k < numRegistros; k++)
			{
				if(k != i && 
					(
							
					(forma.getMediaMap("via_ingreso_"+i).equals(forma.getMediaMap("via_ingreso_"+k)) || 
						forma.getMediaMap("via_ingreso_"+i).equals(ConstantesBD.codigoNuncaValido+""))
						
						  &&
								
					(forma.getMediaMap("tipo_paciente_"+i).equals(forma.getMediaMap("tipo_paciente_"+k)) || 
						forma.getMediaMap("tipo_paciente_"+i).equals(ConstantesBD.codigoNuncaValido+""))
										
					      &&
								
					(forma.getMediaMap("centro_costo_"+i).equals(forma.getMediaMap("centro_costo_"+k)) || 
						forma.getMediaMap("centro_costo_"+i).equals(ConstantesBD.codigoNuncaValido+""))								
					)
				)							
				{
					String add = "";
					if(forma.getMediaMap("via_ingreso_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
						add += " via Ingreso : Todos. ";
					if(forma.getMediaMap("tipo_paciente_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
						add += " tipo Paciente : Todos. ";
					if(forma.getMediaMap("centro_costo_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
						add += " Centro Costo : Todos. ";
					
					if(!add.equals(""))
						add = "("+add+")";
						
					
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El Registro Nro. "+(i+1)+" ya incluye el Registros Nro. "+(k+1)+". "+add));
					return errores;
				}				
			}
		}
				
		return errores;	
	}
	
	
	
		
	
	
	/**
	 * Método que ingresar o actualiza los registros de Excepciones por Vigencia
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */	
	private ActionForward accionGuardarMedio(Connection con, 
											ExTarifasAsociosForm forma, 
											ActionMapping mapping, 
											UsuarioBasico usuario, 
											HttpServletRequest request) 
	{
		int auxI0 = 0;
		boolean resp0 = false;
		
		//Se instancia objeto de Excepciones
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		//manejo de errores
		ActionErrors errores = new ActionErrors();
		HashMap parametros = new HashMap();
		
		int numRegistros = Integer.parseInt(forma.getMediaMap("numRegistros").toString());
		int codigo_encab = Integer.parseInt(forma.getEncabezadoMap("codigo_"+forma.getIndicadorEncabezado()).toString());		
		UtilidadBD.iniciarTransaccion(con);
		
		forma.setEstado("otro");
		
		
		for(int i=0;i<numRegistros && errores.isEmpty();i++)
		{
			//se toma el código del registro
			auxI0 = ConstantesBD.codigoNuncaValido;
			if(forma.getMediaMap().containsKey("codigo_"+i) && 
					!forma.getMediaMap("codigo_"+i).toString().equals(""))
				auxI0 = Integer.parseInt(forma.getMediaMap("codigo_"+i).toString());
			
			//se verifica si el registro es nuevo o ya existe
			if(auxI0>0)
			{
				//*****************MODIFICACIÓN DE REGISTRO********************************************
				//se consulta registro antiguo
				
				parametros.put("codigo",auxI0);
				parametros.put("codigo_encab",codigo_encab);
				HashMap registroAntiguo = excepcion.consultarMedia(con, parametros);				
				
				//logger.info("registro antiguo >> "+registroAntiguo+" >> "+forma.getMediaMap());
				
				//se verifica si el registro fue modificado
				if(this.fueModificadoMedio(registroAntiguo,forma.getMediaMap(),i))
				{				
					//se actualiza registro
					parametros = this.llenarMapaMedia(forma.getMediaMap(), i, usuario);
					resp0 = excepcion.modificarMedia(con, parametros);
					
					if(!resp0)
						errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","El registros Nº "+(i+1)+" no pudo ser actualizado. Por favor verifique"));
					else
					{
						//se genera log
						this.generarLogMedio(forma.getEncabezadoMap(),Integer.parseInt(forma.getIndicadorEncabezado()),registroAntiguo,forma.getMediaMap(),i,ConstantesBD.tipoRegistroLogModificacion,usuario);												
						forma.setEstado("guardarMedia");
					}
				}
				
				//**************************************************************************************
			}
			else
			{
				//*****************INSERCIÓN DE REGISTRO*************************************************
				//se inserta registro				
				parametros = this.llenarMapaMedia(forma.getMediaMap(),i, usuario);
				//logger.info("valor parametros action >> "+parametros);
				resp0 = excepcion.insertarMedia(con, parametros);
								
				if(!resp0)
					errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","El registros Nº "+(i+1)+" no pudo ser insertado. Por favor verifique"));
								
				//***************************************************************************************
			}
		}
				
		if(errores.isEmpty())
		{
			parametros = new HashMap();
			parametros.put("codigo_encab",codigo_encab);
			forma.setMediaMap(excepcion.consultarMedia(con, parametros));
			forma.setEstado("guardarMedia");
			UtilidadBD.finalizarTransaccion(con);
		}
		else		
			UtilidadBD.abortarTransaccion(con);		
		
		saveErrors(request,errores);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("medio");
	}
	
	
	/**
	 * Método implementado para verificar si un registro del encabezado fue modificado
	 * con respecto a su original en la BD
	 * @param registroAntiguo
	 * @param registroNuevo
	 * @param pos
	 * @returnArchivos Planos Colsanitas
	 */
	
	private boolean fueModificadoMedio(HashMap registroAntiguo, HashMap registroNuevo, int pos) 
	{
		boolean fueModificado = false;
		
		if(!registroAntiguo.get("via_ingreso_0").toString().trim().equals(registroNuevo.get("via_ingreso_"+pos).toString().trim()) ||
				!registroAntiguo.get("tipo_paciente_0").toString().trim().equals(registroNuevo.get("tipo_paciente_"+pos).toString().trim()) ||
					!registroAntiguo.get("centro_costo_0").toString().trim().equals(registroNuevo.get("centro_costo_"+pos).toString().trim()))
			fueModificado = true;
		
		
		//logger.info("fue modificado medio >> "+fueModificado+" >> "+pos);
		
		return fueModificado;		
	}
	
	/**
	 * Método implementado para generar el LOG Modificacion de Excepciones por Vigencia
	 * @param registroAntiguo (antiguo)
	 * @param registroNuevo (nuevo)
	 * @param pos
	 * @param tipo
	 * @param usuario
	*/
	private void generarLogMedio(HashMap vigencias, 
								 int posVigencia,
								 HashMap registroAntiguo, 
								 HashMap registroNuevo,
								 int pos, 
								 int tipo, 
								 UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL EXCEPCIONES TARIFAS QUIRURGICAS POR VIGENCIA===== " +
		    "\n   (ENCABEZADO) "+
			"\n*  Codigo [" +vigencias.get("codigo_"+posVigencia)+"] "+
			"\n*  Institucion ["+vigencias.get("institucion_"+posVigencia)+"] " +
			"\n*  Convenio ["+vigencias.get("convenio_"+posVigencia)+"] " +
			"\n*  Nombre Convenio ["+vigencias.get("nombre_convenio_"+posVigencia)+"] " +
			"\n*  Fecha Inicial ["+vigencias.get("fecha_inicial_"+posVigencia)+"] " +
			"\n*  Fecha Final ["+vigencias.get("fecha_final_"+posVigencia)+"] " +
			"\n"+ 
			"\n   (DETALLE) " +
			"\n*  Codigo [" +registroAntiguo.get("codigo_0")+"] ";
		    
		    if(registroAntiguo.get("via_ingreso_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Via Ingreso [Todos] ";
		    else
		    	log+="\n*  Via Ingreso [" +registroAntiguo.get("via_ingreso_0")+"] ";
			
			log+="\n*  Nombre Via Ingreso [" +registroAntiguo.get("nombre_via_ingreso_0")+"] ";		    
		    
			if(registroAntiguo.get("tipo_paciente_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Tipo Paciente [Todos] ";
		    else
		    	log+="\n*   Tipo Paciente [" +registroAntiguo.get("tipo_paciente_0")+"] ";		    
			
			log+="\n*  Nombre Tipo Paciente [" +registroAntiguo.get("nombre_tipo_paciente_0")+"] ";			
			
			if(registroAntiguo.get("centro_costo_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Centro Costo [Todos] ";
		    else
		    	log+="\n*  Centro Costo [" +registroAntiguo.get("centro_costo_0")+"] ";			
			
			log+="\n*  Nombre Centro Costo [" +registroAntiguo.get("nombre_ccosto_0")+"] "+			
			""  ;
		    
		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN EXCEPCIONES TARIFAS QUIRURGICAS POR VIGENCIA ===== " +		     
			"\n   (DETALLE) " +
			"\n*  Codigo [" +registroNuevo.get("codigo_"+pos)+"] ";
			
			if(registroNuevo.get("via_ingreso_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Via Ingreso [Todos] ";
		    else
		    	log+="\n*  Via Ingreso [" +registroNuevo.get("via_ingreso_"+pos)+"] ";
			
			log+="\n*  Nombre Via Ingreso [" +registroNuevo.get("nombre_via_ingreso_"+pos)+"] ";		    
		    
			if(registroNuevo.get("tipo_paciente_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Tipo Paciente [Todos] ";
		    else
		    	log+="\n*   Tipo Paciente [" +registroNuevo.get("tipo_paciente_"+pos)+"] ";		    
			
			log+="\n*  Nombre Tipo Paciente [" +registroNuevo.get("nombre_tipo_paciente_"+pos)+"] ";			
			
			if(registroNuevo.get("centro_costo_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Centro Costo [Todos] ";
		    else
		    	log+="\n*  Centro Costo [" +registroNuevo.get("centro_costo_"+pos)+"] ";			
			
			log+="\n*  Nombre Centro Costo [" +registroNuevo.get("nombre_ccosto_"+pos)+"] "+			
			""  ;			
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
			
			log="\n            ====INFORMACION ORIGINAL EXCEPCIONES TARIFAS QUIRURGICAS POR VIGENCIA===== " +
		    "\n   (ENCABEZADO) "+
			"\n*  Codigo [" +vigencias.get("codigo_"+posVigencia)+"] "+
			"\n*  Institucion ["+vigencias.get("institucion_"+posVigencia)+"] " +
			"\n*  Convenio ["+vigencias.get("convenio_"+posVigencia)+"] " +
			"\n*  Nombre Convenio ["+vigencias.get("nombre_convenio_"+posVigencia)+"] " +
			"\n*  Fecha Inicial ["+vigencias.get("fecha_inicial_"+posVigencia)+"] " +
			"\n*  Fecha Final ["+vigencias.get("fecha_final_"+posVigencia)+"] " +
			"\n"+ 
			"";
			
			 log+="\n\n            ====INFORMACION ELIMINADA EXCEPCIONES TARIFAS QUIRURGICAS POR VIGENCIA ===== " +		     
				"\n   (DETALLE) " +
				"\n*  Codigo [" +registroNuevo.get("codigo_"+pos)+"] ";
				
			if(registroNuevo.get("via_ingreso_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Via Ingreso [Todos] ";
		    else
		    	log+="\n*  Via Ingreso [" +registroNuevo.get("via_ingreso_"+pos)+"] ";
			
			log+="\n*  Nombre Via Ingreso [" +registroNuevo.get("nombre_via_ingreso_"+pos)+"] ";		    
		    
			if(registroNuevo.get("tipo_paciente_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Tipo Paciente [Todos] ";
		    else
		    	log+="\n*   Tipo Paciente [" +registroNuevo.get("tipo_paciente_"+pos)+"] ";		    
			
			log+="\n*  Nombre Tipo Paciente [" +registroNuevo.get("nombre_tipo_paciente_"+pos)+"] ";			
			
			if(registroNuevo.get("centro_costo_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Centro Costo [Todos] ";
		    else
		    	log+="\n*  Centro Costo [" +registroNuevo.get("centro_costo_"+pos)+"] ";			
			
			log+="\n*  Nombre Centro Costo [" +registroNuevo.get("nombre_ccosto_"+pos)+"] "+			
			""  ;				   
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logExcepcionTarifasQuirurgicasCodigo, log, tipo,usuario.getLoginUsuario());
		
	}
	
	
	/*-----------------------------------------------------
	 * METODOS CAMBIO UBICACION GEOGRAFICA 
	 * ---------------------------------------------------*/
	
	/**
	 * Recarga el tipo paciente dependiendo de la Via de Ingreso
	 * @param Connection con
	 * @param ExTarifasAsociosForm forma
	 * @param HttpServletResponse response
	 * */
	public ActionForward accionCambiarTipo(Connection con,ExTarifasAsociosForm forma,HttpServletResponse response)
	{
		String resultado = "<respuesta>";
		String codigoIngreso = "";
		
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
				
		codigoIngreso = forma.getCodigoViaIngreso();
		forma.setTipoPacienteMap(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, codigoIngreso));
		arregloAux = forma.getTipoPacienteMap();
		
		
		//logger.info("NÚMERO DE ELEMENTO ARREGLO Tipo Paciente => "+arregloAux.size());
		//logger.info("CODIGO VIA INGRESO=>*"+codigoIngreso+"*");
		
		//Revision de los Tipo Paciente segun la via de ingreso seleccionado
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			if(elemento.get("codigoViaIngreso").toString().equals(codigoIngreso))
				resultado += "<tipo>" +
					"<codigo-via>"+elemento.get("codigoViaIngreso")+"</codigo-via>"+
					"<codigo-tipo>"+elemento.get("codigoTipoPaciente")+"</codigo-tipo>"+
					"<nombre-tipo>"+elemento.get("nombreTipoPaciente")+"</nombre-tipo>"+
				 "</tipo>";
		}
		
		resultado += "</respuesta>";

		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.reset();
			response.resetBuffer();
			
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().flush();			
	        response.getWriter().write(resultado);
	        response.getWriter().close();
	        
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en cargar Tipos paciente por Via de Ingreso: "+e);
		}
		return null;
	}		
	
	
	
	
	
	//******************METODOS DEL DETALLE **********************************************
	//************************************************************************************
	
	/**
	 * Busca el detalle de Excepciones Tarifas Asocios
	 * @param Connection con
	 * @param ExceptionForm forma
	 * @param usuario 
	 * */
	public void accionBuscarDetalle(Connection con,ExTarifasAsociosForm forma,UsuarioBasico usuario)
	{
		ExTarifasAsocios excepciones = new ExTarifasAsocios();
		HashMap parametros = new HashMap();
		
		parametros.put("codigo_encab_xvit",forma.getMediaMap("codigo_"+forma.getIndicadorMedio()));
		
		//Se Filtra la consulta Avanzada
		if(forma.getBusquedaAvanzadaMap().containsKey("tipo_servicioB") && 
				!forma.getBusquedaAvanzadaMap("tipo_servicioB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("tipo_servicioB",forma.getBusquedaAvanzadaMap("tipo_servicioB"));
				
		if(forma.getBusquedaAvanzadaMap().containsKey("grupo_servicioB") &&
				!forma.getBusquedaAvanzadaMap("grupo_servicioB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("grupo_servicioB",forma.getBusquedaAvanzadaMap("grupo_servicioB"));
		
		if(forma.getBusquedaAvanzadaMap().containsKey("especialidadB") &&
				!forma.getBusquedaAvanzadaMap("especialidadB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("especialidadB",forma.getBusquedaAvanzadaMap("especialidadB"));
				
		if(forma.getBusquedaAvanzadaMap().containsKey("servicioB") &&
				!forma.getBusquedaAvanzadaMap("servicioB").equals(ConstantesBD.codigoNuncaValido+"") && 
					!forma.getBusquedaAvanzadaMap("servicioB").equals(""))
			parametros.put("servicioB",forma.getBusquedaAvanzadaMap("servicioB"));		
		
		if(forma.getBusquedaAvanzadaMap().containsKey("tipo_cirugiaB") &&
				!forma.getBusquedaAvanzadaMap("tipo_cirugiaB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("tipo_cirugiaB",forma.getBusquedaAvanzadaMap("tipo_cirugiaB"));
		
		if(forma.getBusquedaAvanzadaMap().containsKey("asocioB") &&
				!forma.getBusquedaAvanzadaMap("asocioB").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("asocioB",forma.getBusquedaAvanzadaMap("asocioB"));
		
		
		//logger.info("valor de parametros para consulta detalle >> "+parametros);
		
		forma.setDetalleMap(excepciones.consultarDetalle(con, parametros));		
		
		//logger.info("valor mapa consulta detalle >> "+forma.getDetalleMap());
	}	
	
	
	/**
	 * Ingresa un  nuevo registro al HashMap del detalle
	 * @param ExcepcionesForm forma
	 * */
	public ActionErrors accionNuevoDetalle(ExTarifasAsociosForm forma)
	{
		ActionErrors errores = new ActionErrors();
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());
		
		//Si existen registros nuevos sin guardar no se da respuesta a la peticion
		for(int i = 0; i < numRegistros; i++)
		{
			if(forma.getDetalleMap("estabd_"+i).toString().equals(ConstantesBD.acronimoNo))
			{
				errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","Debe Guardar El Registro Nuevo Nro. "+(i+1)+" antes de adicionar un nuevo registro."));
				return errores;
			}
		}			
		
		forma.setDetalleMap("codigo_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("codigo_encab_xvit_"+numRegistros,forma.getMediaMap("codigo_"+forma.getIndicadorMedio()));
		forma.setDetalleMap("tipo_servicio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");	
		forma.setDetalleMap("nombre_tipo_servicio_"+numRegistros,"");
		forma.setDetalleMap("especialidad_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombre_especialidad_"+numRegistros,"");
		forma.setDetalleMap("grupo_servicio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombre_grupo_servicio_"+numRegistros,"");
		forma.setDetalleMap("servicio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombreservicio_"+numRegistros,"");
		forma.setDetalleMap("tipo_cirugia_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombre_tipo_cirugia_"+numRegistros,"");
		forma.setDetalleMap("asocio_"+numRegistros,ConstantesBD.codigoNuncaValido+"");
		forma.setDetalleMap("nombre_asocio_"+numRegistros,"");
		forma.setDetalleMap("rango_inicial_"+numRegistros,"");
		forma.setDetalleMap("rango_final_"+numRegistros,"");
		forma.setDetalleMap("porcentaje_"+numRegistros,"");
		forma.setDetalleMap("valor_"+numRegistros,"");
		forma.setDetalleMap("tipo_excepcion_"+numRegistros,"");
		forma.setDetalleMap("estabd_"+numRegistros,ConstantesBD.acronimoNo);
		
		forma.setDetalleMap("numRegistros",numRegistros+1);
		
		return errores;
	}
	
	
	/**
	 * Método implementado para eliminar un registro del mapa Detalle
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */	
	private ActionForward accionEliminarDetalle(Connection con, 
											  ExTarifasAsociosForm forma, 
											  ActionMapping mapping, 
											  UsuarioBasico usuario, 
											  HttpServletRequest request) 
	{
		int pos = Integer.parseInt(forma.getIndicadorDetalle().toString());		
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());
		boolean resp0 = true;
		
		//se toma el código del registro
		int auxI0 = Integer.parseInt(forma.getDetalleMap("codigo_"+pos).toString());
		int codigo_encab = Integer.parseInt(forma.getMediaMap("codigo_"+forma.getIndicadorMedio()).toString());
		
		//Se instancia objeto de Excepciones
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		
		//se instancian errores
		ActionErrors errores = new ActionErrors();
		
		HashMap parametros = new HashMap();
		
		//se verifica si el registro es nuevo o ya existe en la base de datos
		if(auxI0>0)
		{
			//*********ELIMINACION DEL REGISTRO DESDE LA BASE DE DATOS**************			
			parametros.put("codigo",auxI0);
			parametros.put("codigo_encab_xvit",codigo_encab);			
			resp0 =	excepcion.EliminarDetalle(con, parametros);
			
			if(!resp0)
				errores.add("error al eliminar",new ActionMessage("errors.sinEliminar"));
			else
			{
				//se genera LOG
				this.generarLogDetalle(forma.getEncabezadoMap(),Integer.parseInt(forma.getIndicadorEncabezado()),forma.getMediaMap(),Integer.parseInt(forma.getIndicadorMedio()),null,forma.getDetalleMap(),pos,ConstantesBD.tipoRegistroLogEliminacion,usuario);
				forma.setEstado("guardarDetalle");
			}
			//************************************************************************
		}
		
		
		saveErrors(request,errores);
		//si la eliminación es exitosa se elimina del mapa
		if(resp0)
		{
			//*******ELIMINACIÓN DEL REGISTRO EN EL MAPA***********************
			for(int i=pos;i<(numRegistros-1);i++)
			{
				forma.setDetalleMap("codigo_"+i,forma.getDetalleMap("codigo_"+(i+1)));
				forma.setDetalleMap("codigo_encab_xvit_"+i,forma.getDetalleMap("codigo_encab_xvit_"+(i+1)));
				forma.setDetalleMap("tipo_servicio_"+i,forma.getDetalleMap("tipo_servicio_"+(i+1)));
				forma.setDetalleMap("nombre_tipo_servicio_"+i,forma.getDetalleMap("nombre_tipo_servicio_"+(i+1)));
				forma.setDetalleMap("especialidad_"+i,forma.getDetalleMap("especialidad_"+(i+1)));
				forma.setDetalleMap("nombre_especialidad_"+i,forma.getDetalleMap("nombre_especialidad_"+(i+1)));
				forma.setDetalleMap("grupo_servicio_"+i,forma.getDetalleMap("grupo_servicio_"+(i+1)));
				forma.setDetalleMap("nombre_grupo_servicio_"+i,forma.getDetalleMap("nombre_grupo_servicio_"+(i+1)));				
				forma.setDetalleMap("servicio_"+i,forma.getDetalleMap("servicio_"+(i+1)));
				forma.setDetalleMap("nombreservicio_"+i,forma.getDetalleMap("nombreservicio_"+(i+1)));
				forma.setDetalleMap("tipo_cirugia_"+i,forma.getDetalleMap("tipo_cirugia_"+(i+1)));
				forma.setDetalleMap("nombre_tipo_cirugia_"+i,forma.getDetalleMap("nombre_tipo_cirugia_"+(i+1)));
				forma.setDetalleMap("asocio_"+i,forma.getDetalleMap("asocio_"+(i+1)));
				forma.setDetalleMap("nombre_asocio_"+i,forma.getDetalleMap("nombre_asocio_"+(i+1)));
				forma.setDetalleMap("rango_inicial_"+i,forma.getDetalleMap("rango_inicial_"+(i+1)));
				forma.setDetalleMap("rango_final_"+i,forma.getDetalleMap("rango_final_"+(i+1)));
				forma.setDetalleMap("porcentaje_"+i,forma.getDetalleMap("cuanto_detalle_"+(i+1)));
				forma.setDetalleMap("valor_"+i,forma.getDetalleMap("porcentaje_"+(i+1)));
				forma.setDetalleMap("tipo_excepcion_"+i,forma.getDetalleMap("tipo_excepcion_"+(i+1)));			
				forma.setDetalleMap("estabd_"+i,forma.getDetalleMap("estabd_"+(i+1)));
				
			}
			//se elimina último registro
			pos = numRegistros - 1;
			forma.getDetalleMap().remove("codigo_"+pos);
			forma.getDetalleMap().remove("codigo_encab_xvit_"+pos);
			forma.getDetalleMap().remove("tipo_servicio_"+pos);
			forma.getDetalleMap().remove("nombre_tipo_servicio_"+pos);
			forma.getDetalleMap().remove("especialidad_"+pos);
			forma.getDetalleMap().remove("nombre_especialidad_"+pos);
			forma.getDetalleMap().remove("grupo_servicio_"+pos);
			forma.getDetalleMap().remove("nombre_grupo_servicio_"+pos);			
			forma.getDetalleMap().remove("servicio_"+pos);
			forma.getDetalleMap().remove("nombreservicio_"+pos);
			
			forma.getDetalleMap().remove("tipo_cirugia_"+pos);
			forma.getDetalleMap().remove("nombre_tipo_cirugia_"+pos);
			forma.getDetalleMap().remove("asocio_"+pos);
			forma.getDetalleMap().remove("nombre_asocio_"+pos);
			forma.getDetalleMap().remove("rango_inicial_"+pos);
			forma.getDetalleMap().remove("rango_final_"+pos);
			forma.getDetalleMap().remove("porcentaje_"+pos);
			forma.getDetalleMap().remove("valor_"+pos);			
			forma.getDetalleMap().remove("tipo_excepcion_"+pos);
			forma.getDetalleMap().remove("estabd_"+pos);			
			
			//******************************************************************
			
			//se actualiza tamaño del mapa			
			forma.setDetalleMap("numRegistros",pos+"");
			
			//logger.info("mapa despues de la eliminacion Detalle >> "+forma.getDetalleMap());
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
	
	
	
	
	/**
	 * Método implementado para generar el LOG Modificacion de Detallede Excepciones Asocios
	 * @param registroAntiguo (antiguo)
	 * @param registroNuevo (nuevo)
	 * @param pos
	 * @param tipo
	 * @param usuario
	*/
	private void generarLogDetalle(HashMap vigencias, 
								 int posVigencia,
								 HashMap excepcionesXVigencia,
								 int posExcepciones,
								 HashMap registroAntiguo, 
								 HashMap registroNuevo,
								 int pos, 
								 int tipo, 
								 UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL DETALLE EXCEPCIONES TARIFAS QUIRURGICAS===== " +
		    "\n   (VIGENCIA -> ENCABEZADO) "+
			"\n*  Codigo [" +vigencias.get("codigo_"+posVigencia)+"] "+
			"\n*  Institucion ["+vigencias.get("institucion_"+posVigencia)+"] " +
			"\n*  Convenio ["+vigencias.get("convenio_"+posVigencia)+"] " +
			"\n*  Nombre Convenio ["+vigencias.get("nombre_convenio_"+posVigencia)+"] " +
			"\n*  Fecha Inicial ["+vigencias.get("fecha_inicial_"+posVigencia)+"] " +
			"\n*  Fecha Final ["+vigencias.get("fecha_final_"+posVigencia)+"] " +
			"\n"+ 
			"\n   (EXCEPCIONES POR VIGENCIA -> MEDIO) " +
			"\n*  Codigo [" +excepcionesXVigencia.get("codigo_"+posExcepciones)+"] ";
		    
		    //--------
		    if(excepcionesXVigencia.get("via_ingreso_"+posExcepciones).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Via Ingreso [Todos] ";
		    else
		    	log+="\n*  Via Ingreso [" +excepcionesXVigencia.get("via_ingreso_"+posExcepciones)+"] ";
			
			log+="\n*  Nombre Via Ingreso [" +excepcionesXVigencia.get("nombre_via_ingreso_"+posExcepciones)+"] ";		    
		    
			//--------
			if(excepcionesXVigencia.get("tipo_paciente_"+posExcepciones).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Tipo Paciente [Todos] ";
		    else
		    	log+="\n*   Tipo Paciente [" +excepcionesXVigencia.get("tipo_paciente_"+posExcepciones)+"] ";		    
			
			log+="\n*  Nombre Tipo Paciente [" +excepcionesXVigencia.get("nombre_tipo_paciente_"+posExcepciones)+"] ";			
			
			//--------
			if(excepcionesXVigencia.get("centro_costo_"+posExcepciones).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Centro Costo [Todos] ";
		    else
		    	log+="\n*  Centro Costo [" +excepcionesXVigencia.get("centro_costo_"+posExcepciones)+"] ";			
			
			log+="\n*  Nombre Centro Costo [" +excepcionesXVigencia.get("nombre_ccosto_"+posExcepciones)+"] "+			
			"\n"  +
			
			"\n  (EXCEPCIONES TARIFAS ASOCIOS -> DETALLE) " +
			"\n* Codigo [" +registroAntiguo.get("codigo_0") + "] ";
			
			//----------
			if(registroAntiguo.get("tipo_servicio_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Servicio [Ninguno] ";
			else
				log+="\n* Tipo Servicio ["+registroAntiguo.get("tipo_servicio_0")+"] ";
			
			log+="\n* Nombre Tipo Servicio ["+registroAntiguo.get("nombre_tipo_servicio_0")+"] ";
			
			//----------
			if(registroAntiguo.get("especialidad_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Especialidad [Ninguno] ";
			else
				log+="\n* Especialidad ["+registroAntiguo.get("especialidad_0")+"] ";
			
			log+="\n* Nombre Especialidad ["+registroAntiguo.get("nombre_especialidad_0")+"] ";
			
						
			//----------
			if(registroAntiguo.get("grupo_servicio_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Grupo Servicio [Ninguno] ";
			else
				log+="\n* Grupo Servicio ["+registroAntiguo.get("grupo_servicio_0")+"] ";
			
			log+="\n* Nombre Grupo Servicio ["+registroAntiguo.get("nombre_grupo_servicio_0")+"] ";
			
			
			//----------
			if(registroAntiguo.get("servicio_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Servicio [Ninguno] ";
			else
				log+="\n* Servicio ["+registroAntiguo.get("servicio_0")+"] ";
			
			log+="\n* Nombre Servicio ["+registroAntiguo.get("nombreservicio_0")+"] ";
			
			
			//----------
			if(registroAntiguo.get("tipo_cirugia_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Cirugia [Ninguno] ";
			else
				log+="\n* Tipo Cirugia ["+registroAntiguo.get("tipo_cirugia_0")+"] ";
			
			log+="\n* Nombre Tipo Cirugia ["+registroAntiguo.get("nombre_tipo_cirugia_0")+"] ";
			
			//----------
			if(registroAntiguo.get("asocio_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Asocio [Ninguno] ";
			else
				log+="\n* Asocio ["+registroAntiguo.get("asocio_0")+"] ";
			
			log+="\n* Nombre Asocio ["+registroAntiguo.get("nombre_asocio_0")+"] ";
			
			
			//----------			
			log+=
			"\n* Rango Inicial ["+registroAntiguo.get("rango_inicial_0")+"] " +
			"\n* Rango Final ["+registroAntiguo.get("rango_final_0")+"] " +
			"\n* Porcentaje ["+registroAntiguo.get("porcentaje_0")+"] " +
			"\n* Valor ["+registroAntiguo.get("valor_0")+"] ";
			
			//----------
			if(registroAntiguo.get("tipo_excepcion_0").toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Excepcion [Ninguno] ";
			else
			{
				if(registroAntiguo.get("tipo_excepcion_0").toString().equals(ConstantesIntegridadDominio.acronimoIncremento))
					log+="\n* Tipo Excepcion [Incremento] ";
				if(registroAntiguo.get("tipo_excepcion_0").toString().equals(ConstantesIntegridadDominio.acronimoDecremento))
					log+="\n* Tipo Excepcion [Decremento] ";
			}			
			//----------			
		    
			
			 log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DETALLE EXCEPCIONES TARIFAS QUIRURGICAS ===== " +	
			"\n* Codigo [" +registroNuevo.get("codigo_"+pos) + "] ";
			
			//----------
			if(registroNuevo.get("tipo_servicio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Servicio [Ninguno] ";
			else
				log+="\n* Tipo Servicio ["+registroNuevo.get("tipo_servicio_"+pos)+"] ";
			
			log+="\n* Nombre Tipo Servicio ["+registroNuevo.get("nombre_tipo_servicio_"+pos)+"] ";
			
			//----------
			if(registroNuevo.get("especialidad_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Especialidad [Ninguno] ";
			else
				log+="\n* Especialidad ["+registroNuevo.get("especialidad_"+pos)+"] ";
			
			log+="\n* Nombre Especialidad ["+registroNuevo.get("nombre_especialidad_"+pos)+"] ";
			
						
			//----------
			if(registroNuevo.get("grupo_servicio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Grupo Servicio [Ninguno] ";
			else
				log+="\n* Grupo Servicio ["+registroNuevo.get("grupo_servicio_"+pos)+"] ";
			
			log+="\n* Nombre Grupo Servicio ["+registroNuevo.get("nombre_grupo_servicio_"+pos)+"] ";
			
			
			//----------
			if(registroNuevo.get("servicio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Servicio [Ninguno] ";
			else
				log+="\n* Servicio ["+registroNuevo.get("servicio_"+pos)+"] ";
			
			log+="\n* Nombre Servicio ["+registroNuevo.get("nombreservicio_"+pos)+"] ";
			
			
			//----------
			if(registroNuevo.get("tipo_cirugia_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Cirugia [Ninguno] ";
			else
				log+="\n* Tipo Cirugia ["+registroNuevo.get("tipo_cirugia_"+pos)+"] ";
			
			log+="\n* Nombre Tipo Cirugia ["+registroNuevo.get("nombre_tipo_cirugia_"+pos)+"] ";
			
			//----------
			if(registroNuevo.get("asocio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Asocio [Ninguno] ";
			else
				log+="\n* Asocio ["+registroNuevo.get("asocio_"+pos)+"] ";
			
			log+="\n* Nombre Asocio ["+registroNuevo.get("nombre_asocio_"+pos)+"] ";
			
			
			//----------			
			log+=
			"\n* Rango Inicial ["+registroNuevo.get("rango_inicial_"+pos)+"] " +
			"\n* Rango Final ["+registroNuevo.get("rango_final_"+pos)+"] " +
			"\n* Porcentaje ["+registroNuevo.get("porcentaje_"+pos)+"] " +
			"\n* Valor ["+registroNuevo.get("valor_"+pos)+"] ";
			
			//----------
			if(registroNuevo.get("tipo_excepcion_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Excepcion [Ninguno] ";
			else
			{
				if(registroNuevo.get("tipo_excepcion_"+pos).toString().equals(ConstantesIntegridadDominio.acronimoIncremento))
					log+="\n* Tipo Excepcion [Incremento] ";
				if(registroNuevo.get("tipo_excepcion_"+pos).toString().equals(ConstantesIntegridadDominio.acronimoDecremento))
					log+="\n* Tipo Excepcion [Decremento] ";
			}			
			//----------		  
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
			
			log="\n            ====INFORMACION ELIMINADA DETALLE EXCEPCIONES TARIFAS QUIRURGICAS===== " +
		    "\n   (VIGENCIA -> ENCABEZADO) "+
			"\n*  Codigo [" +vigencias.get("codigo_"+posVigencia)+"] "+
			"\n*  Institucion ["+vigencias.get("institucion_"+posVigencia)+"] " +
			"\n*  Convenio ["+vigencias.get("convenio_"+posVigencia)+"] " +
			"\n*  Nombre Convenio ["+vigencias.get("nombre_convenio_"+posVigencia)+"] " +
			"\n*  Fecha Inicial ["+vigencias.get("fecha_inicial_"+posVigencia)+"] " +
			"\n*  Fecha Final ["+vigencias.get("fecha_final_"+posVigencia)+"] " +
			"\n"+ 
			"\n   (EXCEPCIONES POR VIGENCIA -> MEDIO) " +
			"\n*  Codigo [" +excepcionesXVigencia.get("codigo_"+posExcepciones)+"] ";
		    
		    //--------
		    if(excepcionesXVigencia.get("via_ingreso_"+posExcepciones).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Via Ingreso [Todos] ";
		    else
		    	log+="\n*  Via Ingreso [" +excepcionesXVigencia.get("via_ingreso_"+posExcepciones)+"] ";
			
			log+="\n*  Nombre Via Ingreso [" +excepcionesXVigencia.get("nombre_via_ingreso_"+posExcepciones)+"] ";		    
		    
			//--------
			if(excepcionesXVigencia.get("tipo_paciente_"+posExcepciones).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Tipo Paciente [Todos] ";
		    else
		    	log+="\n*   Tipo Paciente [" +excepcionesXVigencia.get("tipo_paciente_"+posExcepciones)+"] ";		    
			
			log+="\n*  Nombre Tipo Paciente [" +excepcionesXVigencia.get("nombre_tipo_paciente_"+posExcepciones)+"] ";			
			
			//--------
			if(excepcionesXVigencia.get("centro_costo_"+posExcepciones).toString().equals(ConstantesBD.codigoNuncaValido+""))
		    	log+="\n*  Centro Costo [Todos] ";
		    else
		    	log+="\n*  Centro Costo [" +excepcionesXVigencia.get("centro_costo_"+posExcepciones)+"] ";			
			
			log+="\n*  Nombre Centro Costo [" +excepcionesXVigencia.get("nombre_ccosto_"+posExcepciones)+"] ";			
			
			
			 log+="\n\n DETALLE ELIMINADO EXCEPCIONES TARIFAS QUIRURGICAS " +	
			"\n* Codigo [" +registroNuevo.get("codigo_"+pos) + "] ";
			
			//----------
			if(registroNuevo.get("tipo_servicio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Servicio [Ninguno] ";
			else
				log+="\n* Tipo Servicio ["+registroNuevo.get("tipo_servicio_"+pos)+"] ";
			
			log+="\n* Nombre Tipo Servicio ["+registroNuevo.get("nombre_tipo_servicio_"+pos)+"] ";
			
			//----------
			if(registroNuevo.get("especialidad_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Especialidad [Ninguno] ";
			else
				log+="\n* Especialidad ["+registroNuevo.get("especialidad_"+pos)+"] ";
			
			log+="\n* Nombre Especialidad ["+registroNuevo.get("nombre_especialidad_"+pos)+"] ";
			
						
			//----------
			if(registroNuevo.get("grupo_servicio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Grupo Servicio [Ninguno] ";
			else
				log+="\n* Grupo Servicio ["+registroNuevo.get("grupo_servicio_"+pos)+"] ";
			
			log+="\n* Nombre Grupo Servicio ["+registroNuevo.get("nombre_grupo_servicio_"+pos)+"] ";
			
			
			//----------
			if(registroNuevo.get("servicio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Servicio [Ninguno] ";
			else
				log+="\n* Servicio ["+registroNuevo.get("servicio_"+pos)+"] ";
			
			log+="\n* Nombre Servicio ["+registroNuevo.get("nombreservicio_"+pos)+"] ";
			
			
			//----------
			if(registroNuevo.get("tipo_cirugia_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Cirugia [Ninguno] ";
			else
				log+="\n* Tipo Cirugia ["+registroNuevo.get("tipo_cirugia_"+pos)+"] ";
			
			log+="\n* Nombre Tipo Cirugia ["+registroNuevo.get("nombre_tipo_cirugia_"+pos)+"] ";
			
			//----------
			if(registroNuevo.get("asocio_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Asocio [Ninguno] ";
			else
				log+="\n* Asocio ["+registroNuevo.get("asocio_"+pos)+"] ";
			
			log+="\n* Nombre Asocio ["+registroNuevo.get("nombre_asocio_"+pos)+"] ";
			
			
			//----------			
			log+=
			"\n* Rango Inicial ["+registroNuevo.get("rango_inicial_"+pos)+"] " +
			"\n* Rango Final ["+registroNuevo.get("rango_final_"+pos)+"] " +
			"\n* Porcentaje ["+registroNuevo.get("porcentaje_"+pos)+"] " +
			"\n* Valor ["+registroNuevo.get("valor_"+pos)+"] ";
			
			//----------
			if(registroNuevo.get("tipo_excepcion_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
				log+="\n* Tipo Excepcion [Ninguno] ";
			else
			{
				if(registroNuevo.get("tipo_excepcion_"+pos).toString().equals(ConstantesIntegridadDominio.acronimoIncremento))
					log+="\n* Tipo Excepcion [Incremento] ";
				if(registroNuevo.get("tipo_excepcion_"+pos).toString().equals(ConstantesIntegridadDominio.acronimoDecremento))
					log+="\n* Tipo Excepcion [Decremento] ";
			}			
			//----------					   
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logExcepcionTarifasQuirurgicasCodigo, log, tipo,usuario.getLoginUsuario());
		
	}
	
	
	
	/**
	 * Almacena la informacion del mapa en un formato son indice
	 * @param HashMap datos
	 * @param int pos
	 * @param UsuarioBasico usuario
	 * */
	public HashMap llenarMapaDetalle(HashMap datos,int pos,UsuarioBasico usuario)
	{
		HashMap respuesta = new HashMap();
		
		respuesta.put("codigo",datos.get("codigo_"+pos));
		respuesta.put("codigo_encab_xvit",datos.get("codigo_encab_xvit_"+pos));
		respuesta.put("tipo_servicio",datos.get("tipo_servicio_"+pos));		
		respuesta.put("especialidad",datos.get("especialidad_"+pos));		
		respuesta.put("grupo_servicio",datos.get("grupo_servicio_"+pos));						
		respuesta.put("servicio",datos.get("servicio_"+pos));		
		respuesta.put("tipo_cirugia",datos.get("tipo_cirugia_"+pos));		
		respuesta.put("asocio",datos.get("asocio_"+pos));		
		respuesta.put("rango_inicial",datos.get("rango_inicial_"+pos));
		respuesta.put("rango_final",datos.get("rango_final_"+pos));
		respuesta.put("porcentaje",datos.get("porcentaje_"+pos));
		respuesta.put("valor",datos.get("valor_"+pos));
		respuesta.put("tipo_excepcion",datos.get("tipo_excepcion_"+pos));	
		
		respuesta.put("usuario_modifica",usuario.getLoginUsuario());
		respuesta.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		respuesta.put("hora_modifica",UtilidadFecha.getHoraActual());
		
		return respuesta;	
	}
	
	
	/**
	 * Validaciones para Guardar las Excepciones del Detalle
	 * @param ExTarifasAsociosForm forma
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionValidarGuardarDetalle(ExTarifasAsociosForm forma,ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());
		forma.setEstado("otro");
		
		for(int i = 0; i< numRegistros; i++)
		{
			if(forma.getDetalleMap("tipo_servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") &&
					forma.getDetalleMap("especialidad_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") &&
						forma.getDetalleMap("grupo_servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+"") &&
							forma.getDetalleMap("servicio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Se debe Ingresar por lo menos uno de los siguientes items Tipo Servicio,Especialidad,Grupo Servicio,Servicio. en el Registro Nro. "+(i+1)));
			
			
			//VALIDACION CAMPO PORCENTAJE - VALOR***********************************
			String auxS0 = forma.getDetalleMap("porcentaje_"+i).toString();
			String auxS1 = forma.getDetalleMap("valor_"+i).toString();
			if(auxS0.equals("")&&auxS1.equals(""))
				errores.add("campo porcentaje-valor requerido",new ActionMessage("errors.notEspecific","Falta definir porcentaje o valor en el Registro Nro. "+(i+1)));
			else
			{
				//validacion PORCENTAJE ****************************************
				if(!auxS0.equals(""))
					if(Double.parseDouble(auxS0)<0)
						errores.add("campo porcentaje menor que cero",new ActionMessage("errors.floatMayorQue","El porcentaje en el Registro Nro. "+(i+1),"0%"));					
				
				//validacion VALOR ***********************************************
				if(!auxS1.equals(""))
					if(Double.parseDouble(auxS1)<0)
						errores.add("campo valor mayor que cero",new ActionMessage("errors.floatMayorQue","El valor en el Registro Nro. "+(i+1),"0"));
			}
			
			//Validacion del Rango Inicial y Rango Final
			auxS0 = forma.getDetalleMap("rango_inicial_"+i).toString();
			auxS1 = forma.getDetalleMap("rango_final_"+i).toString();
			
			if(auxS0.equals("") || auxS1.equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El valor de Rango Inicial y Rango Final son requeridos en el Registro Nro. "+(i+1)));
				return errores;
			}
			else
			{
				if(Double.parseDouble(auxS0) > Double.parseDouble(auxS1))
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El valor de Rango Final debe ser Mayor al valor del Rango  Final en el Registro Nro. "+(i+1)));	
			}		
				
			
			for(int j = 0; j< numRegistros; j++)
			{
				if(j!=i &&
						forma.getDetalleMap("tipo_servicio_"+i).toString().equals(forma.getDetalleMap("tipo_servicio_"+j).toString()) &&
							forma.getDetalleMap("especialidad_"+i).toString().equals(forma.getDetalleMap("especialidad_"+j).toString()) &&
								forma.getDetalleMap("grupo_servicio_"+i).toString().equals(forma.getDetalleMap("grupo_servicio_"+j).toString()) &&
									forma.getDetalleMap("servicio_"+i).toString().equals(forma.getDetalleMap("servicio_"+j).toString()) &&
										forma.getDetalleMap("tipo_cirugia_"+i).toString().equals(forma.getDetalleMap("tipo_cirugia_"+j).toString()) &&
											forma.getDetalleMap("asocio_"+i).toString().equals(forma.getDetalleMap("asocio_"+j).toString()))
				{
					if(!forma.getDetalleMap("rango_inicial_"+j).toString().equals("") && !forma.getDetalleMap("rango_final_"+j).toString().equals(""))
					{
						if(UtilidadValidacion.hayCruceNumeros(Integer.parseInt(forma.getDetalleMap("rango_inicial_"+j).toString()),Integer.parseInt(forma.getDetalleMap("rango_final_"+j).toString()),Integer.parseInt(forma.getDetalleMap("rango_inicial_"+i).toString()),Integer.parseInt(forma.getDetalleMap("rango_final_"+i).toString())))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Entre los Registros Nro. "+(i+1)+" y Nro. "+(j+1)+" Existe Traslape de Rangos."));						
							j = numRegistros;
						}
					}
				}
			}				
		}
		
		return errores;
	}
	
	
	
	/**
	 * Método que ingresar o actualiza los registros de Excepciones por Vigencia
	 * @param con
	 * @param excepcionForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */	
	private ActionForward accionGuardarDetalle(Connection con, 
											ExTarifasAsociosForm forma, 
											ActionMapping mapping, 
											UsuarioBasico usuario, 
											HttpServletRequest request) 
	{
		int auxI0 = 0;
		boolean resp0 = false;
		
		//Se instancia objeto de Excepciones
		ExTarifasAsocios excepcion = new ExTarifasAsocios();
		//manejo de errores
		ActionErrors errores = new ActionErrors();
		HashMap parametros = new HashMap();
		
		int numRegistros = Integer.parseInt(forma.getDetalleMap("numRegistros").toString());
		int codigo_encab = Integer.parseInt(forma.getMediaMap("codigo_"+forma.getIndicadorMedio()).toString());		
		
		
		forma.setEstado("otro");
		
		
		for(int i=0;i<numRegistros;i++)
		{
			//se toma el código del registro
			auxI0 = Integer.parseInt(forma.getDetalleMap("codigo_"+i).toString());			
			//se verifica si el registro es nuevo o ya existe
			if(auxI0>0)
			{
				//*****************MODIFICACIÓN DE REGISTRO********************************************
				//se consulta registro antiguo
				
				parametros.put("codigo",auxI0);
				parametros.put("codigo_encab_xvit",codigo_encab);
				HashMap registroAntiguo = excepcion.consultarDetalle(con, parametros);				
				
				//logger.info("registro antiguo >> "+registroAntiguo+" >> "+forma.getDetalleMap());
				
				//se verifica si el registro fue modificado
				if(this.fueModificadoDetalle(registroAntiguo,forma.getDetalleMap(),i))
				{				
					//se actualiza registro
					parametros = this.llenarMapaDetalle(forma.getDetalleMap(), i, usuario);
					resp0 = excepcion.modificarDetalle(con, parametros);
					
					if(!resp0)
						errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","El registros Nº "+(i+1)+" no pudo ser actualizado. Por favor verifique"));
					else
					{
						//se genera log
						this.generarLogDetalle(forma.getEncabezadoMap(),Integer.parseInt(forma.getIndicadorEncabezado()),forma.getMediaMap(),Integer.parseInt(forma.getIndicadorMedio()),registroAntiguo,forma.getDetalleMap(),i,ConstantesBD.tipoRegistroLogModificacion,usuario);												
						forma.setEstado("guardarDetalle");
					}
				}
				
				//**************************************************************************************
			}
			else
			{
				//*****************INSERCIÓN DE REGISTRO*************************************************
				//se inserta registro
				parametros = this.llenarMapaDetalle(forma.getDetalleMap(),i, usuario);
				//logger.info("valor parametros action >> "+parametros);
				resp0 = excepcion.insertarDetalle(con, parametros);
				
				
				if(!resp0)
					errores.add("no pudo ser modificado",new ActionMessage("errors.notEspecific","El registros Nº "+(i+1)+" no pudo ser insertado. Por favor verifique."));
				else
				{
					forma.setEstado("guardarDetalle");
					parametros = new HashMap();
					parametros.put("codigo_encab_xvit",codigo_encab);			
					forma.setDetalleMap(excepcion.consultarDetalle(con, parametros));
				}
				//***************************************************************************************
			}
		}
		
		saveErrors(request,errores);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
	
	
	
	
	/**
	 * Método implementado para verificar si un registro del detalle fue modificado
	 * con respecto a su original en la BD
	 * @param registroAntiguo
	 * @param registroNuevo
	 * @param pos
	 * @return 
	 */
	
	private boolean fueModificadoDetalle(HashMap registroAntiguo, HashMap registroNuevo, int pos) 
	{
		boolean fueModificado = false;
		
		if(!registroAntiguo.get("tipo_servicio_0").toString().trim().equals(registroNuevo.get("tipo_servicio_"+pos).toString().trim()) ||
				!registroAntiguo.get("especialidad_0").toString().trim().equals(registroNuevo.get("especialidad_"+pos).toString().trim()) ||
					!registroAntiguo.get("grupo_servicio_0").toString().trim().equals(registroNuevo.get("grupo_servicio_"+pos).toString().trim())||
						!registroAntiguo.get("servicio_0").toString().trim().equals(registroNuevo.get("servicio_"+pos).toString().trim())||
							!registroAntiguo.get("tipo_cirugia_0").toString().trim().equals(registroNuevo.get("tipo_cirugia_"+pos).toString().trim())||
								!registroAntiguo.get("asocio_0").toString().trim().equals(registroNuevo.get("asocio_"+pos).toString().trim())||
									!registroAntiguo.get("rango_inicial_0").toString().trim().equals(registroNuevo.get("rango_inicial_"+pos).toString().trim())||
										!registroAntiguo.get("rango_final_0").toString().trim().equals(registroNuevo.get("rango_final_"+pos).toString().trim())||
											!registroAntiguo.get("porcentaje_0").toString().trim().equals(registroNuevo.get("porcentaje_"+pos).toString().trim())||
												!registroAntiguo.get("valor_0").toString().trim().equals(registroNuevo.get("valor_"+pos).toString().trim())||
													!registroAntiguo.get("tipo_excepcion_0").toString().trim().equals(registroNuevo.get("tipo_excepcion_"+pos).toString().trim()))
			fueModificado = true;
		
		
		//logger.info("fue modificado medio >> "+fueModificado+" >> "+pos);
		
		return fueModificado;		
	}
	
	//*********************************************************************************	
}