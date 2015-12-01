
/*
 * Creado   24/05/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.cartera;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cartera.ConceptosCarteraForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.ConceptosCartera;
/**
 * Clase para manejar
 *
 * @version 1.0, 24/05/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class ConceptosCarteraAction extends Action 
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConceptosCarteraAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
		Connection con = null;
		try{
	    if(form instanceof ConceptosCarteraForm )
	    {
	        
		    
		    //intentamos abrir una conexion con la fuente de datos 
			con = openDBConnection(con); 
			
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			//se instancia la variable para manejar los errores.
			ActionErrors errores=new ActionErrors();
			
			ConceptosCarteraForm conceptosForm = (ConceptosCarteraForm) form;			
			HttpSession sesion = request.getSession();
			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			
			ConceptosCartera mundoConceptos = new ConceptosCartera ();
			String estado = conceptosForm.getEstado();
			
			//bandera para sacar los mensajes
			int bandera= conceptosForm.getBandera();
			
			conceptosForm.setMensaje(new ResultadoBoolean(false));
			logger.warn(" [ConceptosCarteraAction]  estado --> " + estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}			
			/////////////////////////////ESTADOS PAGO/////////////////////////////////
			else if (estado.equals("empezarPagos"))//esmpezar la modificación, inserción ó busqueda avanzada de pagos
			{
			    conceptosForm.reset(usuario.getCodigoInstitucionInt());			    
			    conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosPago(con,false,usuario.getCodigoInstitucionInt()));
			    mundoConceptos.reset();
			    conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosPago(con,false,usuario.getCodigoInstitucionInt()));//copia de los registros de la BD, sin ninguna modificación
			    this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarPagos");
			}
			else if(estado.equals("ingresarNuevoPagos"))//ingresar un registro nuevo de Pagos
			{
			   return this.ingresarNuevoPagos(con,conceptosForm,response,request,false); 
			}
			else if (estado.equals("salirGuardarPagos"))
			{
			  return this.salirGuardarPagos(con,conceptosForm,mapping,usuario,sesion,false);  
			}
			else if (estado.equals("busquedaAvanzadaPagos"))
			{
			    this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarPagos");
			}
			else if (estado.equals("ejecutarBusquedaPagos"))
			{
			  return this.busquedaAvanzadaPagos(con,conceptosForm,mapping,usuario,false);  
			}
			else if (estado.equals("eliminarPagos"))
			{
			  return this.eliminarPagos(con,conceptosForm,mapping,response,request,false);  
			}
			else if(estado.equals("empezarConsultaPagos"))
			{
			    conceptosForm.reset(usuario.getCodigoInstitucionInt());			    
			    conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosPago(con,false,usuario.getCodigoInstitucionInt()));	
			    conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
			    this.cerrarConexion(con);
				return mapping.findForward("consultaPagos");   
			}
			else if (estado.equals("busquedaAvanzadaPagosConsulta"))
			{
			    this.cerrarConexion(con);
				return mapping.findForward("consultaPagos");
			}
			else if (estado.equals("ejecutarBusquedaPagosConsulta"))
			{
			  return this.busquedaAvanzadaPagos(con,conceptosForm,mapping,usuario,false);  
			}
			else if (estado.equals("ordenarColumnaPagos"))
			{
			  return this.ordenarPagos(con,conceptosForm,mapping);  
			}
			else if (estado.equals("redireccion"))// estado para mantener los datos del pager
			{			    
			    UtilidadBD.cerrarConexion(con);
			    conceptosForm.getLinkSiguiente();
			    response.sendRedirect(conceptosForm.getLinkSiguiente());
			    return null;
			}
			//////////////////////////////FIN ESTADOS PAGO///////////////////////////////////

			
			
			//////////////////////////////MANEJO DE LOS ESTADOS PARA CONCEPTOS GLOSAS///////////////////////////////
			else if (estado.equals("empezarGlosas"))
			{
/*				logger.info("===> La bandera viene en: "+conceptosForm.getBandera());
				switch (bandera){
					case 1:	{
						logger.info("===> El usuario hizo una eliminación");
						logger.info("===> Voy a imprimir en JSP el mensaje");
						conceptosForm.setMensaje(new ResultadoBoolean(true,"SE ELIMINÓ CORRECTAMENTE, SE GUARDARON LOS CAMBIOS CON ÉXITO!!!"));
					}
					break;
					case 2: {
						logger.info("===> El usuario hizo una modificación");
						logger.info("===> Voy a imprimir en JSP el mensaje");
						conceptosForm.setMensaje(new ResultadoBoolean(true,"SE MODIFICÓ CORRECTAMENTE, SE GUARDARON LOS CAMBIOS CON ÉXITO!!!"));
					}
					break;
					case 3:	{
						logger.info("===> El usuario hizo una inserción");
						logger.info("===> Voy a imprimir en JSP el mensaje");
						conceptosForm.setMensaje(new ResultadoBoolean(true,"SE INSERTÓ CORRECTAMENTE, SE GUARDARON LOS CAMBIOS CON ÉXITO!!!"));
					}
					break;
				}
				
				conceptosForm.reset();
				mundoConceptos.reset();
				conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
				mundoConceptos.reset();
				conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
				logger.info("===> Vamos a cargar los conceptos generales");
				mundoConceptos.cargarConceptosGenerales(con, conceptosForm, usuario);
				logger.info("===> Vamos a cargar los conceptos especificos");
				mundoConceptos.cargarConceptosEspecificos(con, conceptosForm, usuario); */				

				conceptosForm.reset(usuario.getCodigoInstitucionInt());
				mundoConceptos.reset();
				this.cerrarConexion(con);
				return mapping.findForward("principal");
				
				//return mapping.findForward("ingresarModificarGlosas");
				//return this.redireccion(con,conceptosForm,response,request,"ingresarModificarConceptosGlosas.jsp");   
			}

			
			else if (estado.equals("recargarTipoGlosa")){
				// se modifica para cargar perfectamente				
				mundoConceptos.cargarConceptosGenerales(con, conceptosForm, usuario);
				mundoConceptos.cargarConceptosEspecificos(con, conceptosForm, usuario);
				this.cerrarConexion(con);				
				return mapping.findForward("ingresarModificarGlosas");				
			}
			
			else if (estado.equals("ingmodConceptoGlosa")){
				logger.info("===> La bandera viene en: "+conceptosForm.getBandera());
				/*switch (bandera) {
					case 1:	{
						conceptosForm.setMensaje(new ResultadoBoolean(true,"SE ELIMINÓ CORRECTAMENTE, SE GUARDARON LOS CAMBIOS CON ÉXITO!!!"));
					}
					break;
					
					case 2: {
						conceptosForm.setMensaje(new ResultadoBoolean(true,"SE MODIFICÓ CORRECTAMENTE, SE GUARDARON LOS CAMBIOS CON ÉXITO!!!"));
					}
					break;
					
					case 3:	{
						conceptosForm.setMensaje(new ResultadoBoolean(true,"SE INSERTÓ CORRECTAMENTE, SE GUARDARON LOS CAMBIOS CON ÉXITO!!!"));
					}
					break;
				}*/
				mundoConceptos.reset();
				conceptosForm.reset(usuario.getCodigoInstitucionInt());
				conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
				//mundoConceptos.reset();
				conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
				
				mundoConceptos.cargarConceptosGenerales(con, conceptosForm, usuario);
				mundoConceptos.cargarConceptosEspecificos(con, conceptosForm, usuario);
				
				logger.info("\n\nCONCEPTOS GLOSA ACTION------->>"+conceptosForm.getMapConceptos());
				
				activarEliminar(con, conceptosForm, mundoConceptos);
				
				this.cerrarConexion(con);
				
				return mapping.findForward("ingresarModificarGlosas");
			}

			else if (estado.equals("consultarGlosas"))
			{
				conceptosForm.reset(usuario.getCodigoInstitucionInt());	
				mundoConceptos.reset();
				conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
				conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
				logger.info("===> Vamos a cargar los conceptos generales");
				mundoConceptos.cargarConceptosGenerales(con, conceptosForm, usuario);
				logger.info("===> Vamos a cargar los conceptos especificos");
				mundoConceptos.cargarConceptosEspecificos(con, conceptosForm, usuario);
				this.cerrarConexion(con);
				return mapping.findForward("consultarGlosas");
			}
			
			
			
			else if(estado.equals("ingresarNuevoConceptoGlosa"))
			{
				return this.ingresarNuevoConceptoGlosa(con,conceptosForm,response,request); 
			}
			else if(estado.equals("eliminarConceptoGlosa"))
			{
				return this.eliminarGlosas(con,conceptosForm,response,request,mapping);
			}
			else if(estado.equals("GuardarConceptoGlosas"))
			{
				return this.salirGuardarGlosas(con,conceptosForm,response,usuario);
			}
			else if (estado.equals("busquedaAvanzadaGlosas"))
			{
				conceptosForm.setCodigoConcepto("");
				conceptosForm.setDescripcion("");
				conceptosForm.setCuentaDebito("");
				conceptosForm.setCuentaCredito("");
				
				conceptosForm.setAccion(estado);
								
			    this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarGlosas");
			}
			else if (estado.equals("ejecutarBusquedaGlosas"))
			{
			  return this.busquedaAvanzadaGlosas(con,conceptosForm,mapping,usuario);  
			}

			else if (estado.equals("busquedaAvanzadaGlosasConsulta"))
			{
				conceptosForm.setCodigoConcepto("");
				conceptosForm.setDescripcion("");
				
				conceptosForm.setTipoConcepto("");
				conceptosForm.setConceptoGeneral("");
				conceptosForm.setConceptoEspecifico("");
				
				conceptosForm.setCuentaDebito("");
				conceptosForm.setCuentaCredito("");
			    this.cerrarConexion(con);
				return mapping.findForward("consultarGlosas");
			}
			else if (estado.equals("ejecutarBusquedaGlosasConsulta"))
			{
			  return this.busquedaAvanzadaGlosas(con,conceptosForm,mapping,usuario);  
			}		
			else if (estado.equals("ordenarGlosas"))
			{
		      String[] indices={"codigoConcepto_","descripcion_", "tipoConcepto_", "conceptoGeneral_", "conceptoEspecifico_", "cuentadebito_","cuentacredito_" };
			  return this.ordenarGlosasYRespuestasGlosas(con,conceptosForm,mapping,indices,"consultarGlosas");  
			}
			
			////////////////////////////FIN MANEJO DE LOS ESTADOS PARA CONCEPTOS GLOSAS/////////////////////////////
			
			//////////////////////////////MANEJO DE LOS ESTADOS PARA CONCEPTOS RESPUESTAS GLOSAS///////////////////////////////
			else if (estado.equals("empezarRespuestaGlosas"))
			{
			 conceptosForm.reset(usuario.getCodigoInstitucionInt());	
			 mundoConceptos.reset();
			 conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosRespuestasGlosas(con,false,usuario.getCodigoInstitucionInt()));
			 mundoConceptos.reset();
			 conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosRespuestasGlosas(con,false,usuario.getCodigoInstitucionInt()));
			 this.cerrarConexion(con);
			 return mapping.findForward("ingresarModificarRespuestasGlosas");
			}			
			
			else if(estado.equals("ingresarNuevoConceptoRespuestaGlosa"))
			{
				return this.ingresarNuevoConceptoRespuestaGlosa(con,conceptosForm,response,request); 
			}
			else if(estado.equals("eliminarConceptoRespuestaGlosa"))
			{
				return this.eliminarRespuestaGlosas(con,conceptosForm,response,request,mapping);
			}
			else if(estado.equals("GuardarConceptoRespuestasGlosas"))
			{
				return this.salirGuardarRepuestasGlosas(con,conceptosForm,response,usuario);
			}
			else if (estado.equals("busquedaAvanzadaRespuestasGlosas"))
			{
				conceptosForm.setCodigoConcepto("");
				conceptosForm.setDescripcion("");
			    this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarRespuestasGlosas");
			}
			else if (estado.equals("ejecutarBusquedaRespuestasGlosas"))
			{
			  return this.busquedaAvanzadaRespuestasGlosas(con,conceptosForm,mapping,usuario);
			}
			else if (estado.equals("consultarRespuestasGlosas"))
			{
				conceptosForm.reset(usuario.getCodigoInstitucionInt());	
				 mundoConceptos.reset();
				 conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosRespuestasGlosas(con,false,usuario.getCodigoInstitucionInt()));
				 conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
				 this.cerrarConexion(con);
				 return mapping.findForward("consultarRespuestasGlosas");
			}
			else if (estado.equals("busquedaAvanzadaRespuestasGlosasConsulta"))
			{
				conceptosForm.setCodigoConcepto("");
				conceptosForm.setDescripcion("");
				conceptosForm.setCuentaDebito("");
				conceptosForm.setCuentaCredito("");
			    this.cerrarConexion(con);
				return mapping.findForward("consultarRespuestasGlosas");
			}
			else if (estado.equals("ejecutarBusquedaRespuestasGlosasConsulta"))
			{
			  return this.busquedaAvanzadaRespuestasGlosas(con,conceptosForm,mapping,usuario);  
			}		
			else if (estado.equals("ordenarRespuestasGlosas"))
			{
				String[] indices={"codigoRespuestaConcepto_","descripcion_"};
				return this.ordenarGlosasYRespuestasGlosas(con,conceptosForm,mapping,indices,"consultarRespuestasGlosas");  
			}
			
			////////////////////////////FIN MANEJO DE LOS ESTADOS PARA CONCEPTOS RESPUESTAS GLOSAS/////////////////////////////
			
			
			//////////////////////////MANEJO DE LOS ESTADOS PARA CONCEPTOS CASTIGOS///////////////////////////////
			
			else if (estado.equals("empezarCastigo"))
			{
			 conceptosForm.reset(usuario.getCodigoInstitucionInt());	
			 mundoConceptos.reset();
			 conceptosForm.setMapConceptos(mundoConceptos.consultarConceptoCastigo(con,false,usuario.getCodigoInstitucionInt()));	
			 this.cerrarConexion(con);
			 return mapping.findForward("ingresarModificarCastigo");
			}			
			
			else if(estado.equals("ingresarNuevoConceptoCastigo"))
			{
				return this.ingresarNuevoConceptoCastigo(con,conceptosForm,response,usuario); 
			}
			else if(estado.equals("guardarConceptoCastigo"))
			{
				return this.salirGuardarCastigo(con,conceptosForm,mapping,usuario);
			}
			else if (estado.equals("busquedaAvanzadaCastigo"))
			{
				this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarCastigo");
			}
			else if (estado.equals("resultadoBusquedaCastigo"))
			{
				this.cerrarConexion(con);
 				return this.resultadoBusquedaAvanzadaCastigo(conceptosForm,mapping,con,usuario);
			}
			else if (estado.equals("consultarCastigo"))
			{
				 conceptosForm.reset(usuario.getCodigoInstitucionInt());	
				 mundoConceptos.reset();
				 conceptosForm.setMapConceptos(mundoConceptos.consultarConceptoCastigo(con,false,usuario.getCodigoInstitucionInt()));
				 conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
				 this.cerrarConexion(con);
				 return mapping.findForward("consultarCastigo");
			}
			else if (estado.equals("busquedaAvanzadaCastigoConsulta"))
			{
			    this.cerrarConexion(con);
				return mapping.findForward("consultarCastigo");
			}
			else if (estado.equals("ejecutarBusquedaCastigoConsulta"))
			{
				return this.resultadoBusquedaAvanzadaCastigo(conceptosForm,mapping,con,usuario);  
			}
			else if(estado.equals("Castigo"))
			{
				return this.eliminarConceptoCastigo(conceptosForm,mapping,con,usuario);
			}
			
			else if(estado.equals("empezarConsultaCastigo"))
			{
				conceptosForm.reset(usuario.getCodigoInstitucionInt());			    
			    conceptosForm.setMapConceptos(mundoConceptos.consultarConceptoCastigo(con,false,usuario.getCodigoInstitucionInt()));	
			    conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
			    this.cerrarConexion(con);
				return mapping.findForward("consultarCastigo");   
			}
			else if (estado.equals("ordenarColumnaCastigo"))
			{
			  return this.ordenarCastigo(con,conceptosForm,mapping);  
			}
			
			////////////////////////////FIN MANEJO DE LOS ESTADOS PARA CONCEPTOS CASTIGOS////////////////////////////
			
			///////////////////////////ESTADOS AJUSTES//////////////////////////////////////////////////////////////
			
			else if (estado.equals("empezarAjustes"))//esmpezar la modificación, inserción ó busqueda avanzada de ajustes
			{
			    conceptosForm.reset(usuario.getCodigoInstitucionInt());			    
			    conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosAjustes(con,false,usuario.getCodigoInstitucionInt()));
			    mundoConceptos.reset();
			    conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosAjustes(con,false,usuario.getCodigoInstitucionInt()));//copia de los registros de la BD, sin ninguna modificación			    
			    this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarAjustes");
			}
			else if(estado.equals("ingresarNuevoAjuste"))//ingresar un registro nuevo de Pagos
			{
			   return this.ingresarNuevoPagos(con,conceptosForm,response,request,true); 
			}
			else if (estado.equals("eliminarAjuste"))
			{
			  return this.eliminarPagos(con,conceptosForm,mapping,response,request,true);  
			}
			else if (estado.equals("busquedaAvanzadaAjustes"))
			{
				conceptosForm.resetBusqueda();
			    this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarAjustes");
			}
			else if (estado.equals("ejecutarBusquedaAjustes"))
			{
			  return this.busquedaAvanzadaPagos(con,conceptosForm,mapping,usuario,true);  
			}
			else if (estado.equals("salirGuardarAjustes"))
			{
			  return this.salirGuardarPagos(con,conceptosForm,mapping,usuario,sesion,true);  
			}
			else if(estado.equals("empezarConsultaAjustes"))
			{
			    conceptosForm.reset(usuario.getCodigoInstitucionInt());			    
			    conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosAjustes(con,false,usuario.getCodigoInstitucionInt()));	
			    conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
			    this.cerrarConexion(con);
				return mapping.findForward("consultaAjustes");   
			}
			else if (estado.equals("busquedaAvanzadaAjusteConsulta"))
			{
				conceptosForm.resetBusqueda();
			    this.cerrarConexion(con);
				return mapping.findForward("consultaAjustes");
			}
			else if (estado.equals("ejecutarBusquedaAjusteConsulta"))
			{
			  return this.busquedaAvanzadaPagos(con,conceptosForm,mapping,usuario,true);  
			}
			else if (estado.equals("ordenarColumnaAjustes"))
			{
			  return this.ordenarAjustes(con,conceptosForm,mapping);  
			}
			
			///////////////////////////FIN ESTADOS AJUSTES//////////////////////////////////////////////////////////////
			
			/*	estados ingresar y modificar conceptos  */
			else if (estado.equals("modificarConceptoGlosa"))
			{				
			  return this.accionModificarConceptoGlosa(con,conceptosForm,mapping,usuario);  
			}
			
			else if(estado.equals("llenarSelectConGral"))
			{
				return accionLlenarSelectConGral(con, conceptosForm, response, mapping, usuario);
			}		
			
			else if (estado.equals("ingresarConceptoGlosa"))
			{
			  return this.accionIngresarConceptoGlosa(con,conceptosForm,mapping, usuario);  
			}
			
			else if (estado.equals("guardarConceptoModificado"))
			{
				errores=validarConceptoAModificar(con,conceptosForm,mundoConceptos);
				if (!errores.isEmpty())
				{
					saveErrors(request,errores);	
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresarModificarGlosas");
				}
			  return this.accionGuardarConceptoModificado(con,conceptosForm,mapping,mundoConceptos,usuario);  
			}
			
			else if (estado.equals("insertarNuevoConcepto"))
			{
			  return this.accionInsertarNuevoConcepto(con,conceptosForm,mapping,mundoConceptos,usuario);  
			}
			
			else if (estado.equals("eliminarConcepto"))
			{
			  return this.accionEliminarConcepto(con,conceptosForm,mapping,mundoConceptos,usuario);  
			}
			else if (estado.equals("ordenar"))
			{
				return accionOrdenar(con, conceptosForm, mapping);
			}
			
			/* fin estados ingresar y modificar conceptos */
	    }
	    else
		{
			logger.error("El form no es compatible con el form de ConceptosCarteraForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
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
	 * Método implementado para ordenar el listado de facturas
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con,ConceptosCarteraForm forma, ActionMapping mapping) 
	{
		String[] indices = ConceptosCartera.indicesListadoConceptos;
		int numConceptos = Utilidades.convertirAEntero(forma.getMapConceptos("numRegistros")+"");
		forma.setMapConceptos(Listado.ordenarMapa(indices,
				forma.getIndice(),
				forma.getUltimoIndice(),
				forma.getMapConceptos(),
				numConceptos));
		
		forma.setMapConceptos("numRegistros",numConceptos+"");
		
		forma.setUltimoIndice(forma.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarModificarGlosas");
	}

	/**
	 * 	
	 * @param con
	 * @param conceptosForm
	 * @param mundoConceptos
	 */
	private void activarEliminar(Connection con,
			ConceptosCarteraForm conceptosForm, ConceptosCartera mundoConceptos) {
		for(int k=0;k< Utilidades.convertirAEntero(conceptosForm.getMapConceptos("numRegistros")+"");k++)
		{						
			if(mundoConceptos.consultarConceptosGlosa(con, conceptosForm.getMapConceptos("codigoConcepto_"+k)+""))
			{
				conceptosForm.setMapConceptos("puedoeliminar_"+k, ConstantesBD.acronimoSi);
			}
			else
				conceptosForm.setMapConceptos("puedoeliminar_"+k, ConstantesBD.acronimoNo);
		}
	}
	
	
	
	/**
	 * Metodo encargado de eliminar un concepto Glosa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundoConceptos
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarConcepto(Connection con,ConceptosCarteraForm forma, ActionMapping mapping,ConceptosCartera mundo, UsuarioBasico usuario) 
	{		
		logger.info("\n\nCOD CONCEPTO------------>"+forma.getMapConceptos("codigoConcepto_"+forma.getPosicionConceptoModificar()));
		
		if(mundo.eliminarConceptoGlosas(con, forma.getMapConceptos("codigoConcepto_"+forma.getPosicionConceptoModificar())+"", usuario.getCodigoInstitucionInt()))
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"No se actualizaron los datos satisfactoriamente."));	
		
		generarLogConceptosGlosas(forma,usuario,true);
		
		forma.setMapConceptos(mundo.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
		activarEliminar(con, forma, mundo);
		
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificarGlosas");	
	}
	
	/**
	 * @param conceptosForm, usuario, eliminacion
	 */
	private void generarLogConceptosGlosas(ConceptosCarteraForm conceptosForm, UsuarioBasico usuario, boolean eliminacion) {
		String log = "";
		int tipoLog=0;

		if(eliminacion) {			
			
	            log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
		        			 "\n*  Código Concepto Glosa [" +conceptosForm.getMapConceptos("codigoConcepto_"+conceptosForm.getPosicionConceptoModificar())+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Tipo Concepto ["+conceptosForm.getMapConceptosBD("tipoConcepto_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Concepto General ["+conceptosForm.getMapConceptosBD("conceptoGeneral_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Concepto Especifico ["+conceptosForm.getMapConceptosBD("conceptoEspecifico_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Cuenta Debito ["+conceptosForm.getMapConceptosBD("cuentadebito_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Cuenta Credito ["+conceptosForm.getMapConceptosBD("cuentacredito_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
        }

		
		if(!eliminacion) {
			
        	
	            log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
		        			 "\n*  Código Concepto Glosa [" +conceptosForm.getMapConceptosBD("codigoConcepto_"+conceptosForm.getPosicionConceptoModificar())+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcion_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Tipo Concepto ["+conceptosForm.getMapConceptosBD("tipoConcepto_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Concepto General ["+conceptosForm.getMapConceptosBD("conceptoGeneral_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Concepto Especifico ["+conceptosForm.getMapConceptosBD("conceptoEspecifico_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Cuenta Debito ["+conceptosForm.getMapConceptosBD("cuentadebito_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n*  Cuenta Credito ["+conceptosForm.getMapConceptosBD("cuentacredito_"+conceptosForm.getPosicionConceptoModificar())+""+"] " +
		        			 "\n   ============INFORMACION MODIFICADA=========== " +
		        			 "\n*  Código Concepto Glosa [" +conceptosForm.getConceptoModificarMap("codConcepto")+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getConceptoModificarMap("descConcepto")+""+"] " +
		        			 "\n*  Tipo Concepto["+conceptosForm.getConceptoModificarMap("tipoConcepto")+""+"] " +
		        			 "\n*  Concepto General ["+conceptosForm.getConceptoModificarMap("conceptoGral")+""+"] " +
		        			 "\n*  Concepto Especifico ["+conceptosForm.getConceptoModificarMap("conceptoEsp")+""+"] " +
		        			 "\n*  Cuenta Debito ["+conceptosForm.getConceptoModificarMap("cuentaDebito")+""+"] " +
		        			 "\n*  Cuenta Credito ["+conceptosForm.getConceptoModificarMap("cuentaCredito")+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            			
	            
	            tipoLog=ConstantesBD.tipoRegistroLogModificacion;
        }        
        LogsAxioma.enviarLog(ConstantesBD.logConceptosGlosasCodigo,log,tipoLog,usuario.getLoginUsuario());		
        
        logger.info("\n\nSE GUARDO EL LOG EXITOSAMENTE");
     }

	/**
	 * Metodo encargado de almacenar un nuevo concepto
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param mundoConceptos
	 * @param usuario
	 * @return
	 */
	private ActionForward accionInsertarNuevoConcepto(Connection con,ConceptosCarteraForm forma, ActionMapping mapping,ConceptosCartera mundo, UsuarioBasico usuario) 
	{
		logger.info("\n\nMAPA NUEVO CONCEPTO-------->"+forma.getConceptoModificarMap());
		
		if(mundo.insertarConceptoGlosas(con,forma.getConceptoModificarMap("codConcepto")+"",usuario.getCodigoInstitucionInt(),forma.getConceptoModificarMap("descConcepto")+"",Utilidades.convertirADouble(forma.getConceptoModificarMap("cuentaDebito")+""),Utilidades.convertirADouble(forma.getConceptoModificarMap("cuentaCredito")+""),forma.getConceptoModificarMap("tipoConcepto")+"",forma.getConceptoModificarMap("codConceptoGral")+"",forma.getConceptoModificarMap("codConceptoEsp")+"",usuario.getLoginUsuario()))
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"No se actualizaron los datos satisfactoriamente."));		
		int n= Utilidades.convertirAEntero(forma.getMapConceptos("numRegistros")+"");
		
		forma.setMapConceptos("codigoConcepto_"+n,forma.getConceptoModificarMap("codConcepto")+"");
		forma.setMapConceptos("descripcion_"+n,forma.getConceptoModificarMap("descConcepto")+"");
		forma.setMapConceptos("tipoConcepto_"+n,forma.getConceptoModificarMap("tipoConcepto")+"");
		forma.setMapConceptos("conceptoGeneral_"+n,forma.getConceptoModificarMap("conceptoGral")+"");
		forma.setMapConceptos("conceptoEspecifico_"+n,forma.getConceptoModificarMap("conceptoEsp")+"");
		forma.setMapConceptos("codConceptGral_"+n,forma.getConceptoModificarMap("codConceptoGral")+"");
		forma.setMapConceptos("codConceptEspe_"+n,forma.getConceptoModificarMap("codConceptoEsp")+"");
		
		if((forma.getConceptoModificarMap("cuentaDebito")+"").equals("null")){
			forma.setMapConceptos("cuentadebito_"+n,"");
		}else{
			forma.setMapConceptos("cuentadebito_"+n,forma.getConceptoModificarMap("cuentaDebito")+"");
		}
		
		if((forma.getConceptoModificarMap("cuentaCredito")+"").equals("null")){
			forma.setMapConceptos("cuentacredito_"+n,"");
		}else{
			forma.setMapConceptos("cuentacredito_"+n,forma.getConceptoModificarMap("cuentaCredito")+"");
		}
		
		forma.setMapConceptos("numregistros",n+1);
		
		forma.setMapConceptos(mundo.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
		activarEliminar(con, forma, mundo);
		
		logger.info("\n\nMAPA CONCEPTOS MODIFICADO-------->"+forma.getMapConceptos());
		
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificarGlosas");		
	}


	/**
	 * Metodo encargado de guardar las modificaciones realizadas a un concepto Glosa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarConceptoModificado(Connection con,ConceptosCarteraForm forma, ActionMapping mapping, ConceptosCartera mundo, UsuarioBasico usuario) 
	{	   
		boolean guardar=false;
		guardar=mundo.modificarConceptoGlosas(con, forma.getMapConceptos("codigoConcepto_"+forma.getPosicionConceptoModificar())+"", usuario.getCodigoInstitucionInt(), forma.getConceptoModificarMap("codConcepto")+"", forma.getConceptoModificarMap("descConcepto")+"", Utilidades.convertirADouble(forma.getConceptoModificarMap("cuentaDebito")+""), Utilidades.convertirADouble(forma.getConceptoModificarMap("cuentaCredito")+""), forma.getTipoConcepto(), forma.getConceptoModificarMap("codConceptoGral")+"", forma.getConceptoModificarMap("codConceptoEsp")+"", usuario.getLoginUsuario());
		
		if(guardar)
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"No se actualizaron los datos satisfactoriamente."));		
		
		forma.setMapConceptos("codigoConcepto_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("codConcepto")+"");
		forma.setMapConceptos("descripcion_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("descConcepto")+"");
		forma.setMapConceptos("tipoConcepto_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("tipoConcepto")+"");
		forma.setMapConceptos("conceptoGeneral_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("conceptoGral")+"");
		forma.setMapConceptos("conceptoEspecifico_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("conceptoEsp")+"");
		forma.setMapConceptos("codConceptGral_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("codConceptoGral")+"");
		forma.setMapConceptos("codConceptEspe_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("codConceptoEsp")+"");
		
		if((forma.getConceptoModificarMap("cuentaDebito")+"").equals("null")){
			forma.setMapConceptos("cuentadebito_"+forma.getPosicionConceptoModificar(),"");
		}else{
			forma.setMapConceptos("cuentadebito_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("cuentaDebito")+"");
		}
		
		if((forma.getConceptoModificarMap("cuentaCredito")+"").equals("null")){
			forma.setMapConceptos("cuentacredito_"+forma.getPosicionConceptoModificar(),"");
		}else{
			forma.setMapConceptos("cuentacredito_"+forma.getPosicionConceptoModificar(),forma.getConceptoModificarMap("cuentaCredito")+"");
		}				
		
		generarLogConceptosGlosas(forma,usuario,false);
		
		forma.setMapConceptos(mundo.consultarConceptosGlosas(con,false,usuario.getCodigoInstitucionInt()));
		activarEliminar(con, forma, mundo);
		
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificarGlosas");
	}


	/**
	 * Metodo para insertar un nuevo registro de conceptos Glosa
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarConceptoGlosa(Connection con,ConceptosCarteraForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		HashMap<String,Object> nuevo= new HashMap<String,Object>();
		forma.setConceptoModificarMap(nuevo);
		
				
		//forma.setArregloConceptosGenerales(Utilidades.obtenerConceptosGenerales(con,usuario.getCodigoInstitucionInt(), forma.getTipoConcepto()));					
		//forma.getConceptGrales().add(forma.getArregloConceptosGenerales());
		
		
		forma.setArregloConceptosEspecificos(Utilidades.obtenerConceptosEspecificos(con, usuario.getCodigoInstitucionInt()));				
		forma.getConceptEspec().add(forma.getArregloConceptosEspecificos());
		
	    this.cerrarConexion(con);
	    return mapping.findForward("ingresarModificarGlosas");
	}

	/**
	 * Metodo para modificar un registro de conceptos Glosa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionErrors validarConceptoAModificar(Connection con,ConceptosCarteraForm forma,ConceptosCartera mundo)
	{
		ActionErrors errores = new ActionErrors();
		boolean mensaje=false;
		
		int posicion= Utilidades.convertirAEntero(forma.getPosicionConceptoModificar())+1;
		if(forma.getConceptoModificarMap("codConcepto").equals(""))
    	{
    		errores.add("descripcion",new ActionMessage("errors.required","El codigo del Concepto para el registro "+posicion+", Por Favor seleccione uno."));
    	}
		if(forma.getConceptoModificarMap("descConcepto").equals(""))
    	{
    		errores.add("descripcion",new ActionMessage("errors.required","La descripcion del Concepto para el registro "+posicion+", Por Favor seleccione uno."));
    	}
    	if(forma.getConceptoModificarMap("tipoConcepto").equals("-1"))
    	{
    		errores.add("descripcion",new ActionMessage("errors.required","El tipo de Concepto para el registro "+posicion+", Por Favor seleccione uno."));
    	}
    	if(forma.getConceptoModificarMap("codConceptoGral").equals("-1") || forma.getConceptoModificarMap("codConceptoGral").equals(""))
    	{
    		errores.add("descripcion",new ActionMessage("errors.required","El Concepto General para el registro "+posicion+", Por Favor seleccione uno."));
    	}
    	if(forma.getConceptoModificarMap("codConceptoEsp").equals("-1"))
    	{
    		errores.add("descripcion",new ActionMessage("errors.required","El Concepto Especifico para el registro "+posicion+", Por Favor seleccione uno."));
    	}
				
		return errores;
	}
	
	/**
	 * Metodo para modificar un registro de conceptos Glosa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificarConceptoGlosa(Connection con,ConceptosCarteraForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{		
		forma.setTipoConcepto(forma.getMapConceptos("tipoConcepto_"+forma.getPosicionConceptoModificar())+"");
		forma.setConceptoModificarMap("codConcepto",forma.getMapConceptos("codigoConcepto_"+forma.getPosicionConceptoModificar())+"");
		forma.setConceptoModificarMap("descConcepto",forma.getMapConceptos("descripcion_"+forma.getPosicionConceptoModificar())+"");
		forma.setConceptoModificarMap("tipoConcepto",forma.getMapConceptos("tipoConcepto_"+forma.getPosicionConceptoModificar())+"");
		forma.setConceptoModificarMap("conceptoGral",forma.getMapConceptos("conceptoGeneral_"+forma.getPosicionConceptoModificar())+"");
		forma.setConceptoModificarMap("conceptoEsp",forma.getMapConceptos("conceptoEspecifico_"+forma.getPosicionConceptoModificar())+"");
		if((forma.getMapConceptos("cuentadebito_"+forma.getPosicionConceptoModificar())+"").equals("null")){
			forma.setConceptoModificarMap("cuentaDebito","");
		}else{
			forma.setConceptoModificarMap("cuentaDebito",forma.getMapConceptos("cuentadebito_"+forma.getPosicionConceptoModificar())+"");
			forma.setConceptoModificarMap("anioCuenta", forma.getMapConceptos("anioCuenta_"+forma.getPosicionConceptoModificar()+""));
			forma.setConceptoModificarMap("numeroCuentaContable", forma.getMapConceptos("numeroCuentaContable_"+forma.getPosicionConceptoModificar()+""));
		}
		if((forma.getMapConceptos("cuentacredito_"+forma.getPosicionConceptoModificar())+"").equals("null")){
			forma.setConceptoModificarMap("cuentaCredito","");
		}else{
			forma.setConceptoModificarMap("cuentaCredito",forma.getMapConceptos("cuentacredito_"+forma.getPosicionConceptoModificar())+"");
			forma.setConceptoModificarMap("anioCuentaCredito", forma.getMapConceptos("anioCuentaCredito_"+forma.getPosicionConceptoModificar()+""));
			forma.setConceptoModificarMap("numeroCuentaContableCredito", forma.getMapConceptos("numeroCuentaContableCredito_"+forma.getPosicionConceptoModificar()+""));
		}
		
		forma.setArregloConceptosGenerales(Utilidades.obtenerConceptosGenerales(con,usuario.getCodigoInstitucionInt(), forma.getTipoConcepto()));					
		forma.getConceptGrales().add(forma.getArregloConceptosGenerales());
		
		forma.setConceptoModificarMap("codConceptoGral",forma.getMapConceptos("codConceptGral_"+forma.getPosicionConceptoModificar())+"");
		
		
		forma.setArregloConceptosEspecificos(Utilidades.obtenerConceptosEspecificos(con, usuario.getCodigoInstitucionInt()));				
		forma.getConceptEspec().add(forma.getArregloConceptosEspecificos());
		
		forma.setConceptoModificarMap("codConceptoEsp",forma.getMapConceptos("codConceptEspe_"+forma.getPosicionConceptoModificar())+"");
		
		this.cerrarConexion(con);
	    return mapping.findForward("ingresarModificarGlosas");
	}	
	
	/**
	 * Metodo para llenar el select dinamico de coneptos Generales 
	 * @param con
	 * @param forma
	 * @param response
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionLlenarSelectConGral(Connection con,ConceptosCarteraForm forma, HttpServletResponse response, ActionMapping mapping, UsuarioBasico usuario) 
	{	
		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
			"<id-select>codConceptoGral</id-select>" +
			"<id-arreglo>conceptGrales</id-arreglo>" +
		"</infoid>" ;
	
		forma.setArregloConceptosGenerales(Utilidades.obtenerConceptosGenerales(con,usuario.getCodigoInstitucionInt(), forma.getTipoConcepto()));					
		forma.getConceptGrales().add(forma.getArregloConceptosGenerales());
				
		for(int i = 0; i<Utilidades.convertirAEntero(forma.getArregloConceptosGenerales("numRegistros").toString()); i++)
		{
			resultado += "<conceptGrales>";
				resultado += "<codigo>"+forma.getArregloConceptosGenerales("codigo_"+i)+"</codigo>";
				resultado += "<descripcion>"+forma.getArregloConceptosGenerales("consecutivo_"+i)+" - "+forma.getArregloConceptosGenerales("descripcion_"+i)+"</descripcion>";
			resultado += "</conceptGrales>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionLlenarSelectConGral: "+e);
		}
		return null;
	}


	/**
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward busquedaAvanzadaRespuestasGlosas(Connection con, ConceptosCarteraForm conceptosForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		ConceptosCartera mundoConceptos = new ConceptosCartera ();
		
		conceptosForm.setMapConceptos(new HashMap());
		mundoConceptos.reset();
		mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());
		mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
		conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosRespuestasGlosas(con,true,usuario.getCodigoInstitucionInt()));
		 conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
        if(conceptosForm.getAccion().equals("busquedaAvanzadaRespuestasGlosasConsulta"))
        {
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            this.cerrarConexion(con);        
            return mapping.findForward("consultarRespuestasGlosas");
        }
        else if(conceptosForm.getAccion().equals("busquedaAvanzadaRespuestasGlosas"))
        {
    		conceptosForm.setMapConceptosBD(new HashMap());
    		mundoConceptos.reset();
    		mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());
    		mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
    		conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosRespuestasGlosas(con,true,usuario.getCodigoInstitucionInt()));
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            this.cerrarConexion(con);        
            return mapping.findForward("ingresarModificarRespuestasGlosas");
        }            
        else
        {
            logger.warn("accion no valida dentro de la busqueda avanzada");
            return null;
        }
	}


		/**
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param response
	 * @param usuario
	 * @param sesion
	 * @return
	 */
	private ActionForward salirGuardarRepuestasGlosas(Connection con, ConceptosCarteraForm conceptosForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean enTransaccion=false;
		int numeroRegistros=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
		//manejo de acceso a la BD en transaccion
		try
		{
			enTransaccion=myFactory.beginTransaction(con);
	        ConceptosCartera mundoConceptos = new ConceptosCartera ();
	        //eliminar
	        int regEliminados=Integer.parseInt(conceptosForm.getMapConceptos("registrosEliminados")+"");
	        for(int i=0;i<regEliminados;i++)
	        {
	        	if(enTransaccion)
	        	{
	        		enTransaccion=mundoConceptos.eliminarConceptoRespuestaGlosas(con,(conceptosForm.getMapConceptos("codigoRespuestaConceptoAntiguoEliminado_"+i)+""),usuario.getCodigoInstitucionInt());
	        		generarLogRespuestasGlosas(conceptosForm,usuario,true,i);
	        	}
	        	else
	        	{
	        		i=regEliminados;
	        	}
	        }
	        //insertar modificar.
	        for(int k=0;k<numeroRegistros;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("BD")&& enTransaccion)
		        	{
		        		if(registroModificadoConceptoRespuestaGlosa(conceptosForm,k))
		        		{
		        			enTransaccion=mundoConceptos.modificarConceptoRespuestaGlosas(con,(conceptosForm.getMapConceptosBD("codigoRespuestaConceptoAntiguo_"+k)+""),usuario.getCodigoInstitucionInt(),(conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+k)+""),(conceptosForm.getMapConceptos("descripcion_"+k)+""));
		        			generarLogRespuestasGlosas(conceptosForm,usuario,false,k);
		        		}
		        	}
		        	//insertar
		        	if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundoConceptos.insertarConceptoRespuestaGlosas(con,(conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+k)+""),usuario.getCodigoInstitucionInt(),(conceptosForm.getMapConceptos("descripcion_"+k)+""));
		        	}
	        	}
	        	else
	        	{
	        		k=numeroRegistros;
	        	}
	        }
	        if(enTransaccion)
	        	myFactory.endTransaction(con);
	        else
	        	myFactory.abortTransaction(con);
		}
		catch(SQLException e)
		{
			logger.error("Se presento error al guardar los datos "+e);
			
		}
		cerrarConexion(con);
		try 
		{
			response.sendRedirect("conceptosCartera.do?estado=empezarRespuestaGlosas");
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
        return null;
	}


		/**
		 * @param conceptosForm
		 * @param usuario
		 * @param b
		 * @param i
		 */
		private void generarLogRespuestasGlosas(ConceptosCarteraForm conceptosForm, UsuarioBasico usuario, boolean eliminacion, int pos)
		{
			String log = "";
			int tipoLog=0;
	        if(eliminacion)
	        {
		            log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			        			 "\n*  Código Respuesta Concepto [" +conceptosForm.getMapConceptos("codigoRespuestaConceptoEliminado_"+pos)+""+"] "+
			        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcionEliminado_"+pos)+""+"] " +
			        			 "\n========================================================\n\n\n ";
		            tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
	        }
	        if(!eliminacion)
	        {
	        	
		            log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			        			 "\n*  Código Respuesta Concepto [" +conceptosForm.getMapConceptosBD("codigoRespuestaConceptoAntiguo_"+pos)+""+"] "+
			        			 "\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcion_"+pos)+""+"] " +
			        			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+pos)+""+"] "+
			        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+pos)+""+"] " +
			        			 "\n========================================================\n\n\n ";
		            tipoLog=ConstantesBD.tipoRegistroLogModificacion;
	        }        
	        LogsAxioma.enviarLog(ConstantesBD.logConceptosRespuestasGlosasCodigo,log,tipoLog,usuario.getLoginUsuario());		
	     }


		/**
		 * @param conceptosForm
		 * @param k
		 * @return
		 */
		private boolean registroModificadoConceptoRespuestaGlosa(ConceptosCarteraForm conceptosForm, int k) 
		{
			return (
					!((conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+k)+"").equals(conceptosForm.getMapConceptosBD("codigoRespuestaConcepto_"+k)+"")) ||
					!((conceptosForm.getMapConceptos("descripcion_"+k)+"").equals(conceptosForm.getMapConceptosBD("descripcion_"+k)+""))
					);
		}


		/**
	 * @param con
	 * @param conceptosForm
	 * @param response
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward eliminarRespuestaGlosas(Connection con, ConceptosCarteraForm conceptosForm, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping) 
	{
		int pos=conceptosForm.getIndex();
		int numReg=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(conceptosForm.getMapConceptos("registrosEliminados")+"");
		if(!UtilidadTexto.getBoolean(conceptosForm.getMapConceptos("existeRelacion_"+pos)+""))
		{
			if((conceptosForm.getMapConceptos("tipo_"+pos)+"").equals("BD"))
			{
				conceptosForm.setMapConceptos("codigoRespuestaConceptoEliminado_"+regEliminados,conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+pos));
				conceptosForm.setMapConceptos("codigoRespuestaConceptoAntiguoEliminado_"+regEliminados,conceptosForm.getMapConceptos("codigoRespuestaConceptoAntiguo_"+pos));
				conceptosForm.setMapConceptos("descripcionEliminado_"+regEliminados,conceptosForm.getMapConceptos("descripcion_"+pos));
		        conceptosForm.setMapConceptos("registrosEliminados",(regEliminados+1)+"");
			}
			for(int i=pos;i<ultimoRegistro;i++)
			{
				conceptosForm.setMapConceptos("codigoRespuestaConcepto_"+i,conceptosForm.getMapConceptos("codigoRespuestaConcepto_"+(i+1)));
				conceptosForm.setMapConceptos("descripcion_"+i,conceptosForm.getMapConceptos("descripcion_"+(i+1)));
				conceptosForm.setMapConceptos("tipo_"+i,conceptosForm.getMapConceptos("tipo_"+(i+1)));
				conceptosForm.setMapConceptos("existeRelacion_"+i,conceptosForm.getMapConceptos("existeRelacion_"+(i+1)));
				conceptosForm.setMapConceptos("codigoRespuestaConceptoAntiguo_"+i,conceptosForm.getMapConceptos("codigoRespuestaConceptoAntiguo_"+(i+1)));
				//tambien los registros de la BD
				conceptosForm.setMapConceptosBD("codigoRespuestaConcepto_"+i,conceptosForm.getMapConceptosBD("codigoRespuestaConcepto_"+(i+1)));
				conceptosForm.setMapConceptosBD("descripcion_"+i,conceptosForm.getMapConceptosBD("descripcion_"+(i+1)));
				conceptosForm.setMapConceptosBD("tipo_"+i,conceptosForm.getMapConceptosBD("tipo_"+(i+1)));
				conceptosForm.setMapConceptosBD("existeRelacion_"+i,conceptosForm.getMapConceptosBD("existeRelacion_"+(i+1)));
				conceptosForm.setMapConceptosBD("codigoRespuestaConceptoAntiguo_"+i,conceptosForm.getMapConceptosBD("codigoRespuestaConceptoAntiguo_"+(i+1)));
			}
			conceptosForm.getMapConceptos().remove("codigoRespuestaConcepto_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("descripcion_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("tipo_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("existeRelacion_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("codigoRespuestaConceptoAntiguo_"+ultimoRegistro);
			//tambien los de la bd
			conceptosForm.getMapConceptosBD().remove("codigoRespuestaConcepto_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("descripcion_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("tipo_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("existeRelacion_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("codigoRespuestaConceptoAntiguo_"+ultimoRegistro);
			//actualizando numero de registros.
			conceptosForm.setMapConceptos("numRegistros",ultimoRegistro+"");
			if(conceptosForm.getIndex()==ultimoRegistro)
			{
				return this.redireccion(con,conceptosForm,response,request,"ingresarModificarConceptosRespuestasGlosas.jsp");
			}
		}
		else
		{
			//En caso de que toque mostrar la opcion eliminar para registros que no se pueden, aquipo posiblemente
			//podría generar el mensaje.
		}
		
	    this.cerrarConexion(con);
	    return mapping.findForward("ingresarModificarRespuestasGlosas");
	}


		/**
	 * @param con
	 * @param conceptosForm
	 * @param response
	 * @param request
	 * @return
	 */
	private ActionForward ingresarNuevoConceptoRespuestaGlosa(Connection con, ConceptosCarteraForm conceptosForm, HttpServletResponse response, HttpServletRequest request) 
	{
		 int posicion = Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
	        conceptosForm.setMapConceptos("codigoRespuestaConcepto_"+posicion,"");
	        conceptosForm.setMapConceptos("descripcion_"+posicion,"");
	        conceptosForm.setMapConceptos("tipo_"+posicion,"MEM");
	        conceptosForm.setMapConceptos("existeRelacion_"+posicion,"false");
	        posicion ++;
	        conceptosForm.setMapConceptos("numRegistros",posicion+"");
	        return this.redireccion(con,conceptosForm,response,request,"ingresarModificarConceptosRespuestasGlosas.jsp");  	
     }


		/**
	 * @param con
	 * @param conceptosForm
	 * @param mapping
		 * @param indices
		 * @param forward
	 * @return
	 */
	private ActionForward ordenarGlosasYRespuestasGlosas(Connection con, ConceptosCarteraForm conceptosForm, ActionMapping mapping, String[] indices, String forward) 
	{
        conceptosForm.setMapConceptos(Listado.ordenarMapa(indices,
										                conceptosForm.getPatronOrdenar(),
										                conceptosForm.getUltimoPatron(),
										                conceptosForm.getMapConceptos(),
										                conceptosForm.getNumRegistros()));
        conceptosForm.setUltimoPatron(conceptosForm.getPatronOrdenar());
        this.cerrarConexion(con);
        return mapping.findForward(forward);	
}


		/**
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward busquedaAvanzadaGlosas(Connection con, ConceptosCarteraForm conceptosForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		ConceptosCartera mundoConceptos = new ConceptosCartera ();
		
		conceptosForm.setMapConceptos(new HashMap());
		mundoConceptos.reset();
		mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());
		mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
		
		mundoConceptos.setTipoConcepto(conceptosForm.getTipoConcepto());
		mundoConceptos.setConceptoGeneral(conceptosForm.getConceptoGeneral());
		mundoConceptos.setConceptoEspecifico(conceptosForm.getConceptoEspecifico());
		
		mundoConceptos.setCuentaDebito((conceptosForm.getCuentaDebito().equals("null"))||(conceptosForm.getCuentaDebito().equals(""))?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getCuentaDebito()));
		mundoConceptos.setCuentaCredito((conceptosForm.getCuentaCredito().equals("null"))||(conceptosForm.getCuentaCredito().equals(""))?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getCuentaCredito()));
		conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosGlosas(con,true,usuario.getCodigoInstitucionInt()));
		conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));
		 
		 
        if(conceptosForm.getAccion().equals("busquedaAvanzadaGlosasConsulta"))
        {
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            this.cerrarConexion(con);        
            return mapping.findForward("consultarGlosas");
        }
        else if(conceptosForm.getAccion().equals("busquedaAvanzadaGlosas"))
        {
    		conceptosForm.setMapConceptosBD(new HashMap());
    		mundoConceptos.reset();
    		mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());
    		mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
    		
    		mundoConceptos.setTipoConcepto(conceptosForm.getTipoConcepto());
    		mundoConceptos.setConceptoGeneral(conceptosForm.getConceptoGeneral());
    		mundoConceptos.setConceptoEspecifico(conceptosForm.getConceptoEspecifico());
    		
    		mundoConceptos.setCuentaDebito((conceptosForm.getCuentaDebito().equals("null"))||(conceptosForm.getCuentaDebito().equals(""))?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getCuentaDebito()));
    		mundoConceptos.setCuentaCredito((conceptosForm.getCuentaCredito().equals("null"))||(conceptosForm.getCuentaCredito().equals(""))?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getCuentaCredito()));
    		conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosGlosas(con,true,usuario.getCodigoInstitucionInt()));
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            this.cerrarConexion(con);        
            return mapping.findForward("ingresarModificarGlosas");
        }            
        else
        {
            logger.warn("accion no valida dentro de la busqueda avanzada");
            return null;
        }
	}


	/**
     * Metodo para ordenar el HashMap, por la columna
     * que se elija en el formulario.
     * @param con Connection, conexión con la fuente de datos
     * @param conceptosForm ConceptosCarteraForm
     * @param mapping ActionMapping
     * @return findForward ActionForward
     */
    private ActionForward ordenarPagos(Connection con, 
											            ConceptosCarteraForm conceptosForm, 
											            ActionMapping mapping) 
    {
        String[] indices={
				            "codigoConcepto_",
				            "codigoTipo_",
				            "codigoCuenta_", 
				            "porcentaje_",
				            "descripcion_",
				            "descripcionTipo_"				            
    					};
        
       conceptosForm.setMapConceptos(Listado.ordenarMapa(indices,
													                conceptosForm.getPatronOrdenar(),
													                conceptosForm.getUltimoPatron(),
													                conceptosForm.getMapConceptos(),
													                conceptosForm.getNumRegistros()));
        conceptosForm.setUltimoPatron(conceptosForm.getPatronOrdenar());
        
        this.cerrarConexion(con);
		return mapping.findForward("consultaPagos");
    }
    
    /**
     * Metodo para ordenar el HashMap, por la columna
     * que se elija en el formulario.
     * @param con Connection, conexión con la fuente de datos
     * @param conceptosForm ConceptosCarteraForm
     * @param mapping ActionMapping
     * @return findForward ActionForward
     */
    private ActionForward ordenarAjustes(Connection con, 
											            ConceptosCarteraForm conceptosForm, 
											            ActionMapping mapping) 
    {
        String[] indices={
				            "codigoConcepto_",				            
				            "codigoCuenta_", 
				            "descripcion_",
				            "codigoNaturaleza_",
				            "tipoCartera_", 
				            "desccuentacontable_",
				            "nomtipocartera_"
    					};
        
       conceptosForm.setMapConceptos(Listado.ordenarMapa(indices,
													                conceptosForm.getPatronOrdenar(),
													                conceptosForm.getUltimoPatron(),
													                conceptosForm.getMapConceptos(),
													                conceptosForm.getNumRegistros()));
        conceptosForm.setUltimoPatron(conceptosForm.getPatronOrdenar());
        
        this.cerrarConexion(con);
		return mapping.findForward("consultaAjustes");
    }


    /**
     * Metodo para ordenar el HashMap, por la columna
     * que se elija en el formulario.
     * @param con Connection, conexión con la fuente de datos
     * @param conceptosForm ConceptosCarteraForm
     * @param mapping ActionMapping
     * @return findForward ActionForward
     */
    private ActionForward ordenarCastigo(Connection con, 
											            ConceptosCarteraForm conceptosForm, 
											            ActionMapping mapping) 
    {
        String[] indices={
				            "codigoConcepto_",
				            "descripcion_",
				            "ajusteCredito_"				            
    					};
        
       conceptosForm.setMapConceptos(Listado.ordenarMapa(indices,
													                conceptosForm.getPatronOrdenar(),
													                conceptosForm.getUltimoPatron(),
													                conceptosForm.getMapConceptos(),
													                conceptosForm.getNumRegistros()));
        conceptosForm.setUltimoPatron(conceptosForm.getPatronOrdenar());
        
        this.cerrarConexion(con);
		return mapping.findForward("consultarCastigo");
    }
	
	/**
	 * @param con
	 * @param conceptosForm
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	private ActionForward eliminarGlosas(Connection con, ConceptosCarteraForm conceptosForm, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping)
	{
		int pos=conceptosForm.getIndex();
		int numReg=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(conceptosForm.getMapConceptos("registrosEliminados")+"");
		if(!UtilidadTexto.getBoolean(conceptosForm.getMapConceptos("existeRelacion_"+pos)+""))
		{
			if((conceptosForm.getMapConceptos("tipo_"+pos)+"").equals("BD"))
			{
				conceptosForm.setMapConceptos("codigoConceptoEliminado_"+regEliminados,conceptosForm.getMapConceptos("codigoConcepto_"+pos));
				conceptosForm.setMapConceptos("codigoConceptoAntiguoEliminado_"+regEliminados,conceptosForm.getMapConceptos("codigoConceptoAntiguo_"+pos));
				conceptosForm.setMapConceptos("descripcionEliminado_"+regEliminados,conceptosForm.getMapConceptos("descripcion_"+pos));
				conceptosForm.setMapConceptos("cuentadebitoEliminado_"+regEliminados,conceptosForm.getMapConceptos("cuentadebito_"+pos));
				conceptosForm.setMapConceptos("cuentacreditoEliminado_"+regEliminados,conceptosForm.getMapConceptos("cuentacredito_"+pos));
		        conceptosForm.setMapConceptos("registrosEliminados",(regEliminados+1)+"");
			}
			for(int i=pos;i<ultimoRegistro;i++)
			{
				conceptosForm.setMapConceptos("codigoConcepto_"+i,conceptosForm.getMapConceptos("codigoConcepto_"+(i+1)));
				conceptosForm.setMapConceptos("descripcion_"+i,conceptosForm.getMapConceptos("descripcion_"+(i+1)));
				conceptosForm.setMapConceptos("cuentadebito_"+i,conceptosForm.getMapConceptos("cuentadebito_"+(i+1)));
				conceptosForm.setMapConceptos("cuentacredito_"+i,conceptosForm.getMapConceptos("cuentacredito_"+(i+1)));
				conceptosForm.setMapConceptos("tipo_"+i,conceptosForm.getMapConceptos("tipo_"+(i+1)));
				conceptosForm.setMapConceptos("existeRelacion_"+i,conceptosForm.getMapConceptos("existeRelacion_"+(i+1)));
				conceptosForm.setMapConceptos("codigoConceptoAntiguo_"+i,conceptosForm.getMapConceptos("codigoConceptoAntiguo_"+(i+1)));
				//tambien los registros de la BD
				conceptosForm.setMapConceptosBD("codigoConcepto_"+i,conceptosForm.getMapConceptosBD("codigoConcepto_"+(i+1)));
				conceptosForm.setMapConceptosBD("descripcion_"+i,conceptosForm.getMapConceptosBD("descripcion_"+(i+1)));
				conceptosForm.setMapConceptosBD("cuentadebito_"+i,conceptosForm.getMapConceptosBD("cuentadebito_"+(i+1)));
				conceptosForm.setMapConceptosBD("cuentacredito_"+i,conceptosForm.getMapConceptosBD("cuentacredito_"+(i+1)));
				conceptosForm.setMapConceptosBD("tipo_"+i,conceptosForm.getMapConceptosBD("tipo_"+(i+1)));
				conceptosForm.setMapConceptosBD("existeRelacion_"+i,conceptosForm.getMapConceptosBD("existeRelacion_"+(i+1)));
				conceptosForm.setMapConceptosBD("codigoConceptoAntiguo_"+i,conceptosForm.getMapConceptosBD("codigoConceptoAntiguo_"+(i+1)));
			}
			conceptosForm.getMapConceptos().remove("codigoConcepto_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("descripcion_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("cuentadebito_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("cuentacredito_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("tipo_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("existeRelacion_"+ultimoRegistro);
			conceptosForm.getMapConceptos().remove("codigoConceptoAntiguo_"+ultimoRegistro);
			//tambien los de la bd
			conceptosForm.getMapConceptosBD().remove("codigoConcepto_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("descripcion_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("cuentadebito_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("cuentacredito_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("tipo_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("existeRelacion_"+ultimoRegistro);
			conceptosForm.getMapConceptosBD().remove("codigoConceptoAntiguo_"+ultimoRegistro);
			//actualizando numero de registros.
			conceptosForm.setMapConceptos("numRegistros",ultimoRegistro+"");
			if(conceptosForm.getIndex()==ultimoRegistro)
			{
				return this.redireccion(con,conceptosForm,response,request,"ingresarModificarConceptosGlosas.jsp");
			}
		}
		else
		{
			//En caso de que toque mostrar la opcion eliminar para registros que no se pueden, aquipo posiblemente
			//podría generar el mensaje.
		}
		
	    this.cerrarConexion(con);
	    return mapping.findForward("ingresarModificarGlosas");
		
	}


	/**
	 * Metodo implementado para eliminar registros,
	 * de la BD que no posean relación con aplicación 
	 * de pagos, como registros nuevos que no han
	 * sido guardados en la BD.
     * @param con Connection, conexión con la fuente de datos
     * @param conceptosForm ConceptosCarteraForm
     * @param mapping ActionMapping
     * @param usuario UsuarioBasico
     * @param esAjuste boolean, para reutilizar el metodo 
     * 							segun la funcionalidad,sea de ajustes 
     * 							o de pagos.
     * @return findForward ActionForward
     */
    private ActionForward eliminarPagos(Connection con, 
											            ConceptosCarteraForm conceptosForm, 
											            ActionMapping mapping,
											            HttpServletResponse response,
											            HttpServletRequest request,
											            boolean esAjuste) 
    {
       int pos = conceptosForm.getPosRegistro(),posEli;
       posEli = conceptosForm.getNumRegEliminados();
       String cadenaRedireccion="";
       
       if((conceptosForm.getMapConceptos("existeRelacion_"+pos)+"").equals("false"))
       {
           if((conceptosForm.getMapConceptos("tipo_"+pos)+"").equals("BD"))
           {
               conceptosForm.setRegistrosEliminados(posEli,conceptosForm.getMapConceptos("codigoConceptoOld_"+pos));               
               posEli ++;
               conceptosForm.setNumRegEliminados(posEli);
               eliminarBD (conceptosForm,esAjuste);
           }
           eliminarMemoria(conceptosForm,esAjuste);   
       }    
       
       this.cerrarConexion(con);
       //conceptosForm.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
       if(pos==(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"")))
       {
	       if(!esAjuste)
	           cadenaRedireccion = "ingresarModificarConceptosPago.jsp";//sendRedirect para pagos
	       else
	           cadenaRedireccion = "ingresarModificarConceptosAjustes.jsp";//sendRedirect para ajustes
	      
	       return this.redireccion(con,conceptosForm,response,request,cadenaRedireccion);	      
       }
       else
       {
	       if(esAjuste)       
	           return mapping.findForward("ingresarModificarAjustes");
	       else if(!esAjuste)
	           return mapping.findForward("ingresarModificarPagos");
	       else
	           return null;
       }
    }

    /**
     * Metodo para eliminar el registro del 
     * HashMap.
     * @param conceptosForm ConceptosCarteraForm,
     * @param esAjuste boolean,para reutilizar el metodo 
     * 							segun la funcionalidad,sea de ajustes 
     * 							o de pagos.
     */
    private void eliminarMemoria(ConceptosCarteraForm conceptosForm,boolean esAjuste)
    {
        int nuevaPos=-1,posDel=-1;
        int esUltimo=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"")-1;
        
        posDel = conceptosForm.getPosRegistro();        
        //if(conceptosForm.getCodigoConcepto().equals((conceptosForm.getMapConceptos("codigoConcepto_"+posDel)+"")))
        {
          conceptosForm.getMapConceptos().remove("codigoConcepto_"+posDel);
          conceptosForm.getMapConceptos().remove("codigoConceptoOld_"+posDel);
          if(!esAjuste)//para eliminar index's pagos
          {
	          conceptosForm.getMapConceptos().remove("codigoTipo_"+posDel);
	          conceptosForm.getMapConceptos().remove("porcentaje_"+posDel);
	          conceptosForm.getMapConceptos().remove("descripcionTipo_"+posDel);
          }
          else if(esAjuste)//para eliminar index's  de ajustes
          {
              conceptosForm.getMapConceptos().remove("codigoNaturaleza_"+posDel);
              conceptosForm.getMapConceptos().remove("descripcionNaturaleza_"+posDel);
              conceptosForm.getMapConceptos().remove("tipoCartera_"+posDel);
          }
          conceptosForm.getMapConceptos().remove("codigoCuenta_"+posDel);          
          conceptosForm.getMapConceptos().remove("descripcion_"+posDel);          
          conceptosForm.getMapConceptos().remove("tipo_"+posDel);
          conceptosForm.getMapConceptos().remove("existeRelacion_"+posDel);
          nuevaPos = Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
          nuevaPos = nuevaPos-1;
          conceptosForm.setMapConceptos("numRegistros",nuevaPos+"");
                    
          if(posDel != esUltimo)//si no es el ultimo registro, se organiza el HashMap
          {               
           for(int k=posDel;k<Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");k++)
           {
	         conceptosForm.setMapConceptos("codigoConcepto_"+k,conceptosForm.getMapConceptos("codigoConcepto_"+(k+1)));  
	         conceptosForm.setMapConceptos("codigoConceptoOld_"+k,conceptosForm.getMapConceptos("codigoConceptoOld_"+(k+1)));
	         if(!esAjuste)//para mover index's pagos
	         {
		         conceptosForm.setMapConceptos("codigoTipo_"+k,conceptosForm.getMapConceptos("codigoTipo_"+(k+1)));
		         conceptosForm.setMapConceptos("porcentaje_"+k,conceptosForm.getMapConceptos("porcentaje_"+(k+1)));
		         conceptosForm.setMapConceptos("descripcionTipo_"+k,conceptosForm.getMapConceptos("descripcionTipo_"+(k+1)));
	         }
	         else if(esAjuste)//para mover index's  de ajustes
	         {
	             conceptosForm.setMapConceptos("codigoNaturaleza_"+k,conceptosForm.getMapConceptos("codigoNaturaleza_"+(k+1)));
		         conceptosForm.setMapConceptos("descripcionNaturaleza_"+k,conceptosForm.getMapConceptos("descripcionNaturaleza_"+(k+1)));
		         conceptosForm.setMapConceptos("tipoCartera_"+k,conceptosForm.getMapConceptos("tipoCartera_"+(k+1)));
	         }
	         conceptosForm.setMapConceptos("codigoCuenta_"+k,conceptosForm.getMapConceptos("codigoCuenta_"+(k+1)));
	         conceptosForm.setMapConceptos("descripcion_"+k,conceptosForm.getMapConceptos("descripcion_"+(k+1)));
	         conceptosForm.setMapConceptos("tipo_"+k,conceptosForm.getMapConceptos("tipo_"+(k+1)));
	         conceptosForm.setMapConceptos("existeRelacion_"+k,conceptosForm.getMapConceptos("existeRelacion_"+(k+1)));
           }
          }
       }            
    }    	
	
    /**
     *  Metodo para eliminar el registro del 
     * 	HashMap.
     * @param conceptosForm ConceptosCarteraForm
     * @param esAjuste boolean,para reutilizar el metodo 
     * 							segun la funcionalidad,sea de ajustes 
     * 							o de pagos.
     */
    private void eliminarBD (ConceptosCarteraForm conceptosForm,boolean esAjuste)
    {
        int nuevaPos=-1,posDel=-1;
        int esUltimo=Integer.parseInt(conceptosForm.getMapConceptosBD("numRegistros")+"")-1;  
        posDel = conceptosForm.getPosRegistro();    
        
        marcarRegEliminados (conceptosForm,esAjuste);
        conceptosForm.getMapConceptosBD().remove("codigoConcepto_"+posDel);
        conceptosForm.getMapConceptosBD().remove("codigoConceptoOld_"+posDel);
        conceptosForm.getMapConceptosBD().remove("codigoCuenta_"+posDel);        
        if(!esAjuste)
        {
            conceptosForm.getMapConceptosBD().remove("codigoTipo_"+posDel);
            conceptosForm.getMapConceptosBD().remove("descripcionTipo_"+posDel);
            conceptosForm.getMapConceptosBD().remove("porcentaje_"+posDel); 
        }
        else if(esAjuste)//para eliminar index's  de ajustes
        {
            conceptosForm.getMapConceptosBD().remove("codigoNaturaleza_"+posDel);
            conceptosForm.getMapConceptosBD().remove("descripcionNaturaleza_"+posDel);
            conceptosForm.getMapConceptosBD().remove("tipoCartera_"+posDel);
        }
        conceptosForm.getMapConceptosBD().remove("descripcion_"+posDel);        
        conceptosForm.getMapConceptosBD().remove("tipo_"+posDel);
        conceptosForm.getMapConceptosBD().remove("existeRelacion_"+posDel);
        nuevaPos = Integer.parseInt(conceptosForm.getMapConceptosBD("numRegistros")+"");
        nuevaPos = nuevaPos-1;
        conceptosForm.setMapConceptosBD("numRegistros",nuevaPos+"");
        
        if(posDel != esUltimo)//si no es el ultimo registro, se organiza el HashMap
        {               
         for(int k=posDel;k<Integer.parseInt(conceptosForm.getMapConceptosBD("numRegistros")+"");k++)
         {
	         conceptosForm.setMapConceptosBD("codigoConcepto_"+k,conceptosForm.getMapConceptosBD("codigoConcepto_"+(k+1)));  
	         conceptosForm.setMapConceptosBD("codigoConceptoOld_"+k,conceptosForm.getMapConceptosBD("codigoConceptoOld_"+(k+1)));
	         conceptosForm.setMapConceptosBD("codigoCuenta_"+k,conceptosForm.getMapConceptosBD("codigoCuenta_"+(k+1)));	         
	         if(!esAjuste)
	         {
	             conceptosForm.setMapConceptosBD("codigoTipo_"+k,conceptosForm.getMapConceptosBD("codigoTipo_"+(k+1)));
	             conceptosForm.setMapConceptosBD("porcentaje_"+k,conceptosForm.getMapConceptosBD("porcentaje_"+(k+1)));
	             conceptosForm.setMapConceptosBD("descripcionTipo_"+k,conceptosForm.getMapConceptosBD("descripcionTipo_"+(k+1)));  
	         }
	         else if(esAjuste)//para mover index's  de ajustes
	         {
	             conceptosForm.setMapConceptosBD("codigoNaturaleza_"+k,conceptosForm.getMapConceptos("codigoNaturaleza_"+(k+1)));
		         conceptosForm.setMapConceptosBD("descripcionNaturaleza_"+k,conceptosForm.getMapConceptos("descripcionNaturaleza_"+(k+1)));
		         conceptosForm.setMapConceptosBD("tipoCartera_"+k,conceptosForm.getMapConceptos("tipoCartera_"+(k+1)));
	         }
	         conceptosForm.setMapConceptosBD("descripcion_"+k,conceptosForm.getMapConceptosBD("descripcion_"+(k+1)));	         
	         conceptosForm.setMapConceptosBD("tipo_"+k,conceptosForm.getMapConceptosBD("tipo_"+(k+1)));
	         conceptosForm.setMapConceptosBD("existeRelacion_"+k,conceptosForm.getMapConceptosBD("existeRelacion_"+(k+1)));
         }
        }
    }
    
    /**
     * Metodo para realizar una copia de el 
     * registro que fue eliminado, con llave diferente
     * @param conceptosForm ConceptosCarteraForm
     */
    private void marcarRegEliminados (ConceptosCarteraForm conceptosForm,boolean esAjuste)
    {
        int posDel = conceptosForm.getPosRegistro();
        conceptosForm.setMapConceptosBD("codigoConceptoDel_"+posDel,conceptosForm.getMapConceptosBD("codigoConceptoOld_"+posDel));        
        conceptosForm.setMapConceptosBD("codigoCuentaDel_"+posDel,conceptosForm.getMapConceptosBD("codigoCuenta_"+posDel));
        conceptosForm.setMapConceptosBD("descripcionDel_"+posDel,conceptosForm.getMapConceptosBD("descripcion_"+posDel));
        if(!esAjuste)
        {
	        conceptosForm.setMapConceptosBD("descripcionTipDel_"+posDel,conceptosForm.getMapConceptosBD("descripcionTipo_"+posDel));  
	        conceptosForm.setMapConceptosBD("codigoTipoDel_"+posDel,conceptosForm.getMapConceptosBD("codigoTipo_"+posDel));
	        conceptosForm.setMapConceptosBD("porcentajeDel_"+posDel,conceptosForm.getMapConceptosBD("porcentaje_"+posDel));
        }
        else if(esAjuste)
        {
            conceptosForm.setMapConceptosBD("codigoNaturalezaDel_"+posDel,conceptosForm.getMapConceptosBD("codigoNaturaleza_"+posDel));            
        }
    }
	/**
	 * Metodo implementado para realizar la 
	 * busqueda Avanzada de Pagos Cartera.
     * @param con Connection, conexión con la fuente de datos.
     * @param conceptosForm ConceptosCarteraForm,
     * @param mapping ActionMapping
     * @param usuario UsuarioBasico
     * @return findForward
     */
    private ActionForward busquedaAvanzadaPagos(Connection con, 
												            ConceptosCarteraForm conceptosForm, 
												            ActionMapping mapping,
												            UsuarioBasico usuario,
												            boolean esAjuste) 
    {
        ConceptosCartera mundoConceptos = new ConceptosCartera ();
        mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());       
        mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
        mundoConceptos.setCodCuenta(conceptosForm.getCodigoCuenta());
        if(!esAjuste)
        {
            mundoConceptos.setCodTipo(conceptosForm.getCodigoTipo());
        	mundoConceptos.setPorcentaje(conceptosForm.getPorcentaje());
        	conceptosForm.getMapConceptos().clear();
        	conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosPago(con,true,usuario.getCodigoInstitucionInt()));
        }
        else if(esAjuste)
        {
            mundoConceptos.setTipoCartera(conceptosForm.getTipoCartera());
            mundoConceptos.setCodNaturaleza(conceptosForm.getCodNaturaleza());
            conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosAjustes(con,true,usuario.getCodigoInstitucionInt()));
        } 
         
        conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));//actualizar el numero de registros
        
        this.cerrarConexion(con);        
        if(conceptosForm.getAccion().equals("busquedaAvanzadaPagosConsulta"))
        {
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            return mapping.findForward("consultaPagos");
        }
        else if(conceptosForm.getAccion().equals("busquedaAvanzadaPagos"))
        {
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            return mapping.findForward("ingresarModificarPagos");
        }
        else if(conceptosForm.getAccion().equals("busquedaAvanzadaAjustes"))
        {
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            return mapping.findForward("ingresarModificarAjustes");
        }  
        if(conceptosForm.getAccion().equals("busquedaAvanzadaAjusteConsulta"))
        {           
            conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            return mapping.findForward("consultaAjustes");
        }
        else
        {
            logger.warn("accion no valida dentro de la busqueda avanzada");
            return null;
        }
    }


    /**
	 * Metodo implementado para realizar las
	 * operaciones de insertar, modificar y eliminar
	 * sobre la BD.
	 * Se valida que tipo de operación se realizara 
	 * segun los movimientos ejectados en el formulario
	 * para los registros.
     * @param con Connection, 
     * @param conceptosForm ConceptosCarteraForm,
     * @param mapping ActionMapping, 
     * @param response HttpServletResponse,
     * @return findForward ActionForward,
     */
    private ActionForward salirGuardarPagos(Connection con, 
												            ConceptosCarteraForm conceptosForm, 
												            ActionMapping mapping,												            
												            UsuarioBasico usuario,
												            HttpSession sesion,
												            boolean esAjuste) 
    {
        ConceptosCartera mundoConceptos = new ConceptosCartera ();
        String codigoConcepto,descripcion,codigoConceptoOld="";
        int tipoCartera=0;
        int codTipo=-1,indexMod=-1,codNaturaleza=-1;
        double codigoCueta = -1;
        double porcentaje=-1;
        boolean insertar=false, modificar=false,eliminar=false;
        
        if(!conceptosForm.getRegistrosEliminados().isEmpty())
        {
          for(int k=0;k<conceptosForm.getRegistrosEliminados().size();k++)
          {              
              try 
              {
                if(!esAjuste)
                    eliminar= mundoConceptos.insertarModificarEliminarConceptosPagoTrans(con,
                        																	conceptosForm.getRegistrosEliminados(k)+"",
                        																	"",
                        																	usuario.getCodigoInstitucionInt(),"",-1,-1,-1,
                													                        false,false,true);
                else if(esAjuste)
                    eliminar=mundoConceptos.insertarModificarEliminarConceptosAjusteTrans(con,
                            																	conceptosForm.getRegistrosEliminados(k)+"",
                            																	"",usuario.getCodigoInstitucionInt(),
                            																	"",-1,codNaturaleza,
                            																	false,false,true,
                            																	0);
                    
                if(!eliminar)
                    logger.warn("No se elimino el registro [codigo-> "+conceptosForm.getRegistrosEliminados(k)+" ]");
                else if(eliminar)
                    generarLogEliminacion(conceptosForm,conceptosForm.getPosRegistro(),usuario,sesion,esAjuste);
                
            } catch (SQLException e) 
            {
              e.printStackTrace();
            }
          }
        }
        
        for(int k =0;k < Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");k++)
        {
            codigoConcepto = conceptosForm.getMapConceptos("codigoConcepto_"+k)+"";
            codigoConceptoOld = conceptosForm.getMapConceptos("codigoConceptoOld_"+k)+"";
            descripcion = conceptosForm.getMapConceptos("descripcion_"+k)+"";
            if(!esAjuste)
            {
                codTipo = Integer.parseInt(conceptosForm.getMapConceptos("codigoTipo_"+k)+"");
            	porcentaje = Double.parseDouble(conceptosForm.getMapConceptos("porcentaje_"+k)+"");
            	codigoCueta = (conceptosForm.getMapConceptos("codigoCuenta_"+k)+"").equals("null")||(conceptosForm.getMapConceptos("codigoCuenta_"+k)+"").equalsIgnoreCase("")?0:Double.parseDouble(conceptosForm.getMapConceptos("codigoCuenta_"+k)+"");
            }
            else if(esAjuste)
            {
            	codigoCueta = (conceptosForm.getMapConceptos("codigoCuenta_"+k)+"").equals("null")||(conceptosForm.getMapConceptos("codigoCuenta_"+k)+"").equalsIgnoreCase("")?0:Double.parseDouble(conceptosForm.getMapConceptos("codigoCuenta_"+k)+"");
                codNaturaleza = Integer.parseInt(conceptosForm.getMapConceptos("codigoNaturaleza_"+k)+"");
                tipoCartera = Integer.parseInt(conceptosForm.getMapConceptos("tipoCartera_"+k)+"");
            }
            
            if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("BD"))
            {
                indexMod=verificarModificados(conceptosForm,k,esAjuste);               
                if((conceptosForm.getMapConceptos("tipoModificado_"+k)+"").equals("Mod"))
                {
                    llenarLog(conceptosForm,indexMod,esAjuste);                    
                    try 
    	            {
                        if(!esAjuste)
                            modificar=mundoConceptos.insertarModificarEliminarConceptosPagoTrans(con,
    															                        codigoConcepto,
    															                        codigoConceptoOld,
    															                        usuario.getCodigoInstitucionInt(),
    															                        descripcion,
    															                        codTipo,
    															                        codigoCueta,
    															                        porcentaje,
    															                        false,true,false);
                        else if (esAjuste)
                            modificar = mundoConceptos.insertarModificarEliminarConceptosAjusteTrans(con,
																	                                    codigoConcepto,
																	                                    codigoConceptoOld,
																	                                    usuario.getCodigoInstitucionInt(),
																	                                    descripcion,
																	                                    codigoCueta,
																	                                    codNaturaleza,
																	                                    false,true,false,
																	                                    tipoCartera);
                        
                        if(!modificar)
                            logger.warn("No se modifico el registro [codigo-> "+codigoConcepto+" ]");
                        else if(modificar)
                            generarLog(conceptosForm,k,usuario,sesion,esAjuste);
                            
    	            } 
    	            catch (SQLException e) 
    	            {
    	              e.printStackTrace();
    	            }
                }
            }
            
            if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("MEM"))
            {
                try 
	            {
                    if(!esAjuste)
	                    insertar=mundoConceptos.insertarModificarEliminarConceptosPagoTrans(con,
																		                        codigoConcepto,
																		                        "",
																		                        usuario.getCodigoInstitucionInt(),
																		                        descripcion,
																		                        codTipo,
																		                        codigoCueta,
																		                        porcentaje,
																		                        true,false,false);
                    else if(esAjuste)
                        insertar=mundoConceptos.insertarModificarEliminarConceptosAjusteTrans(con,
																	                                codigoConcepto,
																	                                "",
																	                                usuario.getCodigoInstitucionInt(),
																	                                descripcion,
																	                                codigoCueta,
																	                                codNaturaleza,
																	                                true,false,false,
																	                                tipoCartera);
                    if(!insertar)
                        logger.warn("No se inserto el registro [codigo-> "+codigoConcepto+" ]");
	            } 
	            catch (SQLException e) 
	            {
	              e.printStackTrace();
	            }
            }
        }
        conceptosForm.reset(usuario.getCodigoInstitucionInt());		
        if(!esAjuste)
        {
            conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosPago(con,false,usuario.getCodigoInstitucionInt()));
            mundoConceptos.reset();
		    conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosPago(con,false,usuario.getCodigoInstitucionInt()));//copia de los registros de la BD, sin ninguna modificación
        }            
        else if(esAjuste)
        {
            conceptosForm.setMapConceptos(mundoConceptos.consultarConceptosAjustes(con,false,usuario.getCodigoInstitucionInt()));
            mundoConceptos.reset();
		    conceptosForm.setMapConceptosBD(mundoConceptos.consultarConceptosAjustes(con,false,usuario.getCodigoInstitucionInt()));//copia de los registros de la BD, sin ninguna modificación
        }
        
        this.cerrarConexion(con);
        conceptosForm.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Éxito"));
        
        if(!esAjuste)
            return mapping.findForward("ingresarModificarPagos");
        else if(esAjuste)
            return mapping.findForward("ingresarModificarAjustes");
        else
            return null;
    }

    
    /**
     * Metodo para verificar que registros fueron modificados (HashMap)
     * @param conceptosForm ConceptosCarteraForm,  
     * @param esAjuste boolean, si es la funcionalidad de ajustes o de pagos    
     */
    public int verificarModificados (ConceptosCarteraForm conceptosForm,									  
									  int index,boolean esAjuste)
    {
        boolean esModificado = false;
             
        int codTipo=-1,codCuenta=-1,indexMod=-1,codNaturaleza=-1;
        String descripcion="",codigo="",tipoCartera="";
        double porcentaje = -1,codigoCueta=-1;
        
        codigo = conceptosForm.getMapConceptos("codigoConcepto_"+index)+"";
        descripcion = conceptosForm.getMapConceptos("descripcion_"+index)+"";
        
           
        if(!esAjuste)
        {
            codTipo = Integer.parseInt(conceptosForm.getMapConceptos("codigoTipo_"+index)+"");
        	porcentaje = Double.parseDouble(conceptosForm.getMapConceptos("porcentaje_"+index)+"");
        	codCuenta =(conceptosForm.getMapConceptos("codigoCuenta_"+index)+"").equals("null")||(conceptosForm.getMapConceptos("codigoCuenta_"+index)+"").equalsIgnoreCase("")?ConstantesBD.codigoNuncaValido:Integer.parseInt(conceptosForm.getMapConceptos("codigoCuenta_"+index)+"");
        }
        else if(esAjuste)
        {
        	codigoCueta = (conceptosForm.getMapConceptos("codigoCuenta_"+index)+"").equals("null")||(conceptosForm.getMapConceptos("codigoCuenta_"+index)+"").equalsIgnoreCase("")?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getMapConceptos("codigoCuenta_"+index)+"");
            codNaturaleza = Integer.parseInt(conceptosForm.getMapConceptos("codigoNaturaleza_"+index)+"");
            tipoCartera= conceptosForm.getMapConceptos("tipoCartera_"+index)+"";
        }
        
        if(!codigo.equals(conceptosForm.getMapConceptosBD("codigoConcepto_"+index)+""))
            esModificado = true; 
        if( !descripcion.equals(conceptosForm.getMapConceptosBD("descripcion_"+index)+"") )
            esModificado = true;	
        
        if(!esAjuste)
        {
		    if(codTipo != Integer.parseInt(conceptosForm.getMapConceptosBD("codigoTipo_"+index)+""))
		         esModificado = true;	
	        if(codCuenta != ((conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+"").equals("null")||(conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+"").equalsIgnoreCase("")?ConstantesBD.codigoNuncaValido:Integer.parseInt(conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+"")))
	        {
	        	 esModificado = true;
	        }
	        if(porcentaje != Double.parseDouble(conceptosForm.getMapConceptosBD("porcentaje_"+index)+""))
	            esModificado = true;
        }
        if(esAjuste)
        {
            if(codNaturaleza != Integer.parseInt(conceptosForm.getMapConceptosBD("codigoNaturaleza_"+index)+""))
	             esModificado = true;
	        if(codigoCueta != ((conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+"").equals("null")||(conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+"").equalsIgnoreCase("")?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+"")))
	            esModificado = true;   
	        if(!tipoCartera.equals(conceptosForm.getMapConceptosBD("tipoCartera_"+index)+""))
	        	esModificado=true;
        }
        
        if(!esModificado)
            conceptosForm.setMapConceptos("tipoModificado_"+index,"noMod");	                     
 	    if(esModificado)
 	    {
 	       conceptosForm.setMapConceptos("tipoModificado_"+index,"Mod");
 	       esModificado = false;
 	    }
 	    indexMod = index;
	 
	   return indexMod;
    }

    /**
	 * Metodo para realizar el ingreso de un registro
	 * nuevo para pagos, el cual es almacenado dentro 
	 * del HashMap, como un registro en limpio, al
	 * que luego se le ingresaran datos en el formulario.
	 * @param con Connection
     * @param conceptosForm ConceptosCarteraForm 
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     * @param esAjuste boolean, para reutilizar el metodo para pagos y ajustes.
     * @return findForward ActionForward
     */
    private ActionForward ingresarNuevoPagos(Connection con,
            								 ConceptosCarteraForm conceptosForm,            								 
            								 HttpServletResponse response,
				            				 HttpServletRequest request,
				            				 boolean esAjuste) 
    {
        int posicion = Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
        String cadenaRedireccion = "";
        
        conceptosForm.setMapConceptos("codigoConcepto_"+posicion,"");
        if(!esAjuste)//para el caso de que sea la funcionalidad de pagos
        {
            cadenaRedireccion = "ingresarModificarConceptosPago.jsp";//sendRedirect para pagos
            conceptosForm.setMapConceptos("codigoTipo_"+posicion,-1+"");//inicializar el codigo del tipo, para validaciones
            conceptosForm.setMapConceptos("porcentaje_"+posicion,0+"");
            conceptosForm.setMapConceptos("descripcionTipo_"+posicion,"Seleccione");//inicializar el nombre del tipo, para mostrar en el formulario
        }
        else if(esAjuste)//para el caso en el que sea la funcionalidad de ajustes
        {
            cadenaRedireccion = "ingresarModificarConceptosAjustes.jsp";//sendRedirect para ajustes
            conceptosForm.setMapConceptos("codigoNaturaleza_"+posicion,-1+"");  
            conceptosForm.setMapConceptos("descripcionNaturaleza_"+posicion,"Seleccione");
        }
        conceptosForm.setMapConceptos("codigoCuenta_"+posicion,-1+"");        
        conceptosForm.setMapConceptos("descripcion_"+posicion,"");        
        conceptosForm.setMapConceptos("tipo_"+posicion,"MEM");//marcado como registro que pertenece a la memoria.
        conceptosForm.setMapConceptos("existeRelacion_"+posicion,"false");//es un registro nuevo,por lo tanto no tiene relación con aplicación pagos.
        
        posicion ++;
        conceptosForm.setMapConceptos("numRegistros",posicion+"");//actualizar el número de registros.
		conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
     
		//Verificar la ruta de la página que corresponda		
		return this.redireccion(con,conceptosForm,response,request,cadenaRedireccion);   
    }
    
    /**
     * metodo para validar si hay relación de un 
     * concepto de pago con aplicaciones pagos.
     * @param con Connection,
     * @param codigoConcepto String,
     * @return existeRelacion boolean,
     */
    public boolean existeRelacionConceptos (Connection con, String codigoConcepto,int institucion)
    {
        ConceptosCartera mundoConceptos = new ConceptosCartera ();
        boolean existeRelacion = false;
        int numReg = mundoConceptos.existeRelacionAplicacionPagos(con,codigoConcepto,institucion);
        if(numReg > 0)
            existeRelacion = true;
        return existeRelacion;
    }
    
    /**
     * Metodo para cargar la información del registro antes
     * de la modificación
     * @param conceptosForm ConceptosCarteraForm 
     * @param index int
     */
    public void llenarLog (ConceptosCarteraForm conceptosForm,int index,boolean esAjuste)
    {
        if(!esAjuste)
	        conceptosForm.setLog(	"\n            ====INFORMACION ORIGINAL===== " +
						            "\n*  Código Concepto [" +conceptosForm.getMapConceptosBD("codigoConcepto_"+index)+""+"] "+
						 			"\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcion_"+index)+""+"] " +
						 			"\n*  Tipo   ["+conceptosForm.getMapConceptosBD("descripcionTipo_"+index)+""+"] "+
						 			"\n*  Cuenta ["+conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+""+"] "+
						 			"\n*  Porcentaje ["+conceptosForm.getMapConceptosBD("porcentaje_"+index)+""+"] "+	 			
						 			"\n\n"
						 		);
        else if(esAjuste)
            conceptosForm.setLog(	"\n            ====INFORMACION ORIGINAL===== " +
		            "\n*  Código Concepto [" +conceptosForm.getMapConceptosBD("codigoConcepto_"+index)+""+"] "+
		 			"\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcion_"+index)+""+"] " +
		 			"\n*  Naturaleza   ["+conceptosForm.getMapConceptosBD("codigoNaturaleza_"+index)+""+"] "+
		 			"\n*  Cuenta ["+conceptosForm.getMapConceptosBD("codigoCuenta_"+index)+""+"] "+
		 			"\n*  Tipo Cartera ["+conceptosForm.getMapConceptosBD("nomtipocartera_"+index)+""+"] "+
		 			"\n\n"
		 		);
    }
    
    /**
     * Metodo para generar el log despues de la 
     * modificación
     * @param conceptosForm ConceptosCarteraForm
     * @param esAjuste boolean, tipo de funcionalidad, ajustes o pagos
     * @param index int
     */
    public void generarLog (ConceptosCarteraForm conceptosForm,int index,UsuarioBasico usuario,HttpSession sesion,boolean esAjuste)
    {
        String log="";
        usuario=(UsuarioBasico)sesion.getAttribute("usuarioBasico");
        if(!esAjuste)
        {       	
        	
	        log=conceptosForm.getLog() + 
	        							"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
	        							"\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoConcepto_"+index)+""+"] "+
	        							"\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+index)+""+"] " +
	        							"\n*  Tipo    ["+conceptosForm.getMapConceptos("codigoTipo_"+index)+""+"] "+
	        							"\n*  Cuenta ["+conceptosForm.getMapConceptos("codigoCuenta_"+index)+""+"] "+
	        							"\n*  Porcentaje ["+conceptosForm.getMapConceptos("porcentaje_"+index)+""+"] "+			
	        							"\n========================================================\n\n\n " ;       
	        LogsAxioma.enviarLog(ConstantesBD.logConceptosPagoCarteraCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
        }
        else if(esAjuste)
        {
        	        	
            log=conceptosForm.getLog() + 
										"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
										"\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoConcepto_"+index)+""+"] "+
										"\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+index)+""+"] " +
										"\n*  Naturaleza    ["+conceptosForm.getMapConceptos("codigoNaturaleza_"+index)+""+"] "+
										"\n*  Cuenta ["+conceptosForm.getMapConceptos("codigoCuenta_"+index)+""+"] "+
										"\n*  Tipo Cartera ["+conceptosForm.getMapConceptos("nomtipocartera_"+index)+""+"] "+
										"\n========================================================\n\n\n " ;       
            LogsAxioma.enviarLog(ConstantesBD.logConceptosAjustesCarteraCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());  
        }
    }
    
    /**
     * Metodo para generar el log despues de la 
     * eliminación
     * @param conceptosForm ConceptosCarteraForm
     * @param index int
     * @param usuario UsuarioBasico
     * @param sesion HttpSession
     */
    public void generarLogEliminacion (ConceptosCarteraForm conceptosForm,
																            int pos,
																            UsuarioBasico usuario,
																            HttpSession sesion,
																            boolean esAjuste)
    {
        String log = "";
        usuario=(UsuarioBasico)sesion.getAttribute("usuarioBasico");
        if(!esAjuste)
        {
	            log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
		        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptosBD("codigoConceptoDel_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcionDel_"+pos)+""+"] " +
		        			 "\n*  Tipo    ["+conceptosForm.getMapConceptosBD("codigoTipoDel_"+pos)+""+"] "+
		        			 "\n*  Cuenta ["+conceptosForm.getMapConceptosBD("codigoCuentaDel_"+pos)+""+"] "+
		        			 "\n*  Porcentaje ["+conceptosForm.getMapConceptosBD("porcentajeDel_"+pos)+""+"] "+			
		        			 "\n========================================================\n\n\n ";
	      	        
	        LogsAxioma.enviarLog(ConstantesBD.logConceptosPagoCarteraCodigo,log,ConstantesBD.tipoRegistroLogEliminacion,usuario.getLoginUsuario());
        }
        else if(esAjuste)
        {
            log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Código Concepto [" +conceptosForm.getMapConceptosBD("codigoConceptoDel_"+pos)+""+"] "+
			 "\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcionDel_"+pos)+""+"] " +
			 "\n*  Naturaleza    ["+conceptosForm.getMapConceptosBD("codigoNaturalezaDel_"+pos)+""+"] "+
			 "\n*  Cuenta ["+conceptosForm.getMapConceptosBD("codigoCuentaDel_"+pos)+""+"] "+				
			 "\n*  Tipo Cartera ["+conceptosForm.getMapConceptosBD("nomtipocartera_"+pos)+""+"] "+
			 "\n========================================================\n\n\n ";
			
			LogsAxioma.enviarLog(ConstantesBD.logConceptosAjustesCarteraCodigo,log,ConstantesBD.tipoRegistroLogEliminacion,usuario.getLoginUsuario()); 
        }
            
    }
    
    /**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param con, Connection con la fuente de datos.
     * @param poolesForm ConceptosCarteraForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     * @param String enlace
     * @return null
     */
    public ActionForward redireccion (	Connection con,
            											ConceptosCarteraForm conceptosForm,
						            					HttpServletResponse response,
						            					HttpServletRequest request, String enlace)
    {
    	conceptosForm.setOffset(((int)((Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"")-1)/conceptosForm.getMaxPageItems()))*conceptosForm.getMaxPageItems());
        if(request.getParameter("ultimaPage")==null)
        {
           if(conceptosForm.getNumRegistros() > (conceptosForm.getOffset()+conceptosForm.getMaxPageItems()))
               conceptosForm.setOffset(((int)(conceptosForm.getNumRegistros()/conceptosForm.getMaxPageItems()))*conceptosForm.getMaxPageItems());
            try 
            {
                response.sendRedirect(enlace+"?pager.offset="+conceptosForm.getOffset());
            } catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");
            
            int posOffSet=ultimaPagina.indexOf("offset=")+7;
            //conceptosForm.setOffset((Integer.parseInt(ultimaPagina.substring(posOffSet,ultimaPagina.length() ))));
            if(conceptosForm.getNumRegistros()>(conceptosForm.getOffset()+conceptosForm.getMaxPageItems()))
                conceptosForm.setOffset(conceptosForm.getOffset()+conceptosForm.getMaxPageItems());
             
            try 
            {
                
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+conceptosForm.getOffset());
            } 
            catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
       this.cerrarConexion(con);
       return null;
    }
	
	/**
	 * @param con
	 * @param conceptosForm
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward salirGuardarGlosas(Connection con, ConceptosCarteraForm conceptosForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		logger.info("777777777777777777777777777777777");
		logger.info("===> Entré a salirGuardarGlosas en el estado : " + conceptosForm.getEstado());
		logger.info("Conceptos: " + conceptosForm.getMapConceptos());
		logger.info("Conceptos BD: " + conceptosForm.getMapConceptosBD());		

		
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean enTransaccion=false;
		String usuarioModifica="";
		usuarioModifica=usuario.getLoginUsuario();
		int numeroRegistros=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
		
		//manejo de acceso a la BD en transaccion
		try
		{
			enTransaccion=myFactory.beginTransaction(con);
	        ConceptosCartera mundoConceptos = new ConceptosCartera ();
	        //eliminar
	        int regEliminados=Integer.parseInt(conceptosForm.getMapConceptos("registrosEliminados")+"");
	        for(int i=0;i<regEliminados;i++)
	        {
	        	if(enTransaccion)
	        	{
	        		logger.info("===> Eliminé !!! :D");
	        		enTransaccion=mundoConceptos.eliminarConceptoGlosas(
	        				con,
	        				(conceptosForm.getMapConceptos("codigoConceptoAntiguoEliminado_"+i)+""),
	        				usuario.getCodigoInstitucionInt());
	        		generarLogGlosas(conceptosForm,usuario,true,i);
	        		//Mandé la Bandera en 1 para eliminar
	        		conceptosForm.setBandera(1);
	        	}
	        	else
	        	{
	        		logger.info("===> No Eliminé !!! :(");
	        		i=regEliminados;
	        	}
	        }
	        //insertar modificar.
	        logger.info("===> Voy a entrar al for de insertar modificar");
	        for(int k=0;k<numeroRegistros;k++)
	        {
	        	//logger.info("===> Entré al for "+k+" veces");
	        	if(enTransaccion)
	        	{
		        	//modificar
	        		//logger.info("===> Entré a modificar");
		        	if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("BD")&& enTransaccion)
		        	{
		        		if(registroModificadoConceptoGlosa(conceptosForm,k))
		        		{
		        			logger.info("===> Entré a registroModificadoConceptoGlosa");
		        			logger.info("Concepto Gral: " + conceptosForm.getMapConceptos());
		        			
		        			enTransaccion=mundoConceptos.modificarConceptoGlosas(
		        					con,
		        					(conceptosForm.getMapConceptosBD("codigoConceptoAntiguo_"+k)+""),
		        					usuario.getCodigoInstitucionInt(),(conceptosForm.getMapConceptos("codigoConcepto_"+k)+""),
		        					(conceptosForm.getMapConceptos("descripcion_"+k)+""),
		        					(conceptosForm.getMapConceptos("cuentadebito_"+k)+"").equalsIgnoreCase("null")||
		        						(conceptosForm.getMapConceptos("cuentadebito_"+k)+"").equalsIgnoreCase("")
		        						?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getMapConceptos("cuentadebito_"+k)+""),
		        					(conceptosForm.getMapConceptos("cuentacredito_"+k)+"").equals("null")||
		        						(conceptosForm.getMapConceptos("cuentacredito_"+k)+"").equalsIgnoreCase("")
		        						?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getMapConceptos("cuentacredito_"+k)+""),
    		        				(conceptosForm.getMapConceptos("tipoConcepto_"+k)+""),
    		        				(conceptosForm.getMapConceptos("codConceptGral_"+k)+""),
    		        				(conceptosForm.getMapConceptos("codConceptEspe_"+k)+""),
    		        				usuarioModifica);
		        			if(enTransaccion)
			    	        {
			    	        	logger.info("===> Modifiqué !!! :D");
			    	        	//Mandé la Bandera en 2 para modificar
				        		conceptosForm.setBandera(2);
			    	        	myFactory.endTransaction(con);
			    	        }
			    	        	
			    	        else
			    	        {
			    	        	logger.info("===> No Modifiqué ... :(");
			    	        	myFactory.abortTransaction(con);
			    	        }
		        			generarLogGlosas(conceptosForm,usuario,false,k);
		        		}
		        	}
		        	//insertar
		        	if((conceptosForm.getMapConceptos("tipo_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundoConceptos.insertarConceptoGlosas(
		        				con,
		        				(conceptosForm.getMapConceptos("codigoConcepto_"+k)+""),
		        				usuario.getCodigoInstitucionInt(),
		        				(conceptosForm.getMapConceptos("descripcion_"+k)+""),
		        				(conceptosForm.getMapConceptos("cuentadebito_"+k)+"").equalsIgnoreCase("null")||
		        					(conceptosForm.getMapConceptos("cuentadebito_"+k)+"").equalsIgnoreCase("")
		        					?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getMapConceptos("cuentadebito_"+k)+""),
		        				(conceptosForm.getMapConceptos("cuentacredito_"+k)+"").equals("null")||
		        					(conceptosForm.getMapConceptos("cuentacredito_"+k)+"").equalsIgnoreCase("")
		        					?ConstantesBD.codigoNuncaValido:Double.parseDouble(conceptosForm.getMapConceptos("cuentacredito_"+k)+""),
		        				(conceptosForm.getMapConceptos("tipoConcepto_"+k)+""),
		        				(conceptosForm.getMapConceptos("conceptoGeneral_"+k)+""),
		        				(conceptosForm.getMapConceptos("conceptoEspecifico_"+k)+""),
		        				usuarioModifica);
		        		if(enTransaccion)
		    	        {
		    	        	logger.info("===> Inserté !!! :D");
		    	        	//Mandé la Bandera en 3 para insertar
			        		conceptosForm.setBandera(3);
		    	        	myFactory.endTransaction(con);
		    	        }
		    	        	
		    	        else
		    	        {
		    	        	logger.info("===> No Inserté ... :(");
		    	        	myFactory.abortTransaction(con);
		    	        }
		        	}
	        	}
	        	else
	        	{
	        		k=numeroRegistros;
	        	}
	        }
	        if(enTransaccion)
	        {
	        	logger.info("===> Vamos a guardar los cambios con éxito");
	        	myFactory.endTransaction(con);
	        }
	        	
	        else
	        {
	        	myFactory.abortTransaction(con);
	        }
	        	
		}
		catch(SQLException e)
		{
			logger.error("Se presento error al guardar los datos "+e);
			
		}
		cerrarConexion(con);
		try 
		{
			response.sendRedirect("conceptosCartera.do?estado=ingmodConceptoGlosa");
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
        return null;
	}

	/**
	 * @param conceptosForm
	 * @param usuario
	 * @param eliminacion
	 * @param pos
	 */
	private void generarLogGlosas(ConceptosCarteraForm conceptosForm, UsuarioBasico usuario, boolean eliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
        if(eliminacion)
        {
	            log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
		        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoConceptoEliminado_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcionEliminado_"+pos)+""+"] " +
		        			 "\n*  Cuenta Debito    ["+((conceptosForm.getMapConceptos("cuentadebitoEliminado_"+pos)+"").equals("null")?"":conceptosForm.getMapConceptos("cuentadebitoEliminado_"+pos))+""+"] "+
		        			 "\n*  Cuenta Credito ["+((conceptosForm.getMapConceptos("cuentacreditoEliminado_"+pos)+"").equals("null")?"":conceptosForm.getMapConceptos("cuentacreditoEliminado_"+pos))+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
        }
        if(!eliminacion)
        {
        	
	            log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
		        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptosBD("codigoConceptoAntiguo_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptosBD("descripcion_"+pos)+""+"] " +
		        			 "\n*  Cuenta Debito    ["+((conceptosForm.getMapConceptosBD("cuentadebito_"+pos)+"").equals("null")?"":conceptosForm.getMapConceptosBD("cuentadebito_"+pos))+""+"] "+
		        			 "\n*  Cuenta Credito ["+((conceptosForm.getMapConceptosBD("cuentacredito_"+pos)+"").equals("null")?"":conceptosForm.getMapConceptosBD("cuentacredito_"+pos))+""+"] "+
		        			 "\n   ===========INFORMACION MODIFICADA========== 	" +
		        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoConcepto_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+pos)+""+"] " +
		        			 "\n*  Cuenta Debito    ["+((conceptosForm.getMapConceptos("cuentadebito_"+pos)+"").equals("null")?"":conceptosForm.getMapConceptos("cuentadebito_"+pos))+""+"] "+
		        			 "\n*  Cuenta Credito ["+((conceptosForm.getMapConceptos("cuentacredito_"+pos)+"").equals("null")?"":conceptosForm.getMapConceptos("cuentacredito_"+pos))+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogModificacion;
        }        
        LogsAxioma.enviarLog(ConstantesBD.logConceptosGlosasCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * @param conceptosForm
	 * @param k
	 * @return
	 */
	private boolean registroModificadoConceptoGlosa(ConceptosCarteraForm conceptosForm, int k) 
	{
		logger.info("===> Entré a Método registroModificadoConceptoGlosa");
		logger.info("===> MapConceptos: "+conceptosForm.getMapConceptos("tipoConcepto_"+k)+"");
		logger.info("===> MapConceptosBD: "+conceptosForm.getMapConceptosBD("tipoConcepto_"+k)+"");
		return (
				!((conceptosForm.getMapConceptos("codigoConcepto_"+k)+"").equals(conceptosForm.getMapConceptosBD("codigoConcepto_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("descripcion_"+k)+"").equals(conceptosForm.getMapConceptosBD("descripcion_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("cuentadebito_"+k)+"").equals(conceptosForm.getMapConceptosBD("cuentadebito_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("cuentacredito_"+k)+"").equals(conceptosForm.getMapConceptosBD("cuentacredito_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("tipoConcepto_"+k)+"").equals(conceptosForm.getMapConceptosBD("tipoConcepto_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("codConceptGral_"+k)+"").equals(conceptosForm.getMapConceptosBD("codConceptGral_"+k)+"")) ||
				!((conceptosForm.getMapConceptos("codConceptEspe_"+k)+"").equals(conceptosForm.getMapConceptosBD("codConceptEspe_"+k)+""))
				);
	}

	/** CASTIGO
	 * @param con
	 * @param conceptosForm
	 * @param mapping
	 * @param response
	 * @param usuario
	 * @param sesion
	 * @return
	 */
	private ActionForward salirGuardarCastigo(Connection con, ConceptosCarteraForm conceptoForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		ConceptosCartera mundoCastigo = new ConceptosCartera ();
        String codigoConcepto,descripcion="",codigoConceptoOld="",ajusteCredito="",existeRelacion="";
        boolean resultado;
        boolean puedoModificar; //variable que indica si el registro se puede modificar
        //iteracion de todo el listado de conceptos castigo
        for(int k =0;k < Integer.parseInt(conceptoForm.getMapConceptos("numRegistros")+"");k++)
        {
        	//se cargan los datos de cada registros
            codigoConcepto = conceptoForm.getMapConceptos("codigoConcepto_"+k)+"";
            codigoConceptoOld = conceptoForm.getMapConceptos("codigoConceptoAntiguo_"+k)+"";
            descripcion = conceptoForm.getMapConceptos("descripcion_"+k)+"";
            ajusteCredito = conceptoForm.getMapConceptos("ajusteCredito_"+k)+"";
            existeRelacion=conceptoForm.getMapConceptos("existeRelacion_"+k)+"";
            puedoModificar=false;
            //se verifica si el registro existe en la BD (indicando así que es para MODIFICAR)
            if((conceptoForm.getMapConceptos("tipo_"+k)+"").equals("BD"))
            {   
            	///sección para verificar si el registro fue modificado
            	mundoCastigo.setCodigo(codigoConceptoOld);
            	mundoCastigo.setDescripcion("");
            	mundoCastigo.setAjusteCredito("-1");
            	HashMap registroViejo=mundoCastigo.consultarConceptoCastigo(con,true,usuario.getCodigoInstitucionInt());
            	if(codigoConcepto.compareTo(registroViejo.get("codigoConcepto_0")+"")!=0||
            	   descripcion.compareTo(registroViejo.get("descripcion_0")+"")!=0||
				   ajusteCredito.compareTo(registroViejo.get("ajusteCredito_0")+"")!=0)
            		puedoModificar=true;
            	
            	
            	//verificar si está en uso el registro
            	if(existeRelacion!=null&&existeRelacion.equals("true"))
            	{
            		//se revisa si se cambió el código
            		if(!codigoConcepto.equals(codigoConceptoOld))
            			puedoModificar=false;
            	}
            	
            	//si se puede modificar se modifica
            	if(puedoModificar)
            	{
	            	try
					 {
	            		
	                 	resultado=mundoCastigo.insertarModificarEliminarConceptoCastigoTrans(con,usuario.getCodigoInstitucionInt(),
	    															                        codigoConceptoOld,
	    															                        codigoConcepto,
	    															                        descripcion,
	    															                        ajusteCredito,"modificar");
	                    if(!resultado)
	                        logger.warn("No se pudo modificar el registro:"+codigoConcepto);
	                    else 
	                    {	
	                    	//se actualiza el concepto Antiguo
	                    	conceptoForm.setMapConceptos("codigoConceptoAntiguo_"+k,codigoConcepto);
	                    	generarLogCastigos(con,conceptoForm,usuario, ConstantesBD.tipoRegistroLogModificacion,registroViejo, k);
	                    }
					 }
	                 catch(SQLException e)
					 {;}
            	}
    	    }
            //se verifica si el registro es nuevo (indicando así que es para insertar)
            if((conceptoForm.getMapConceptos("tipo_"+k)+"").equals("MEM"))
            {
            	try
				{
            		resultado=mundoCastigo.insertarModificarEliminarConceptoCastigoTrans(con,usuario.getCodigoInstitucionInt(),
            																		"",
                																	codigoConcepto,
																		            descripcion,
																					ajusteCredito,
																					"insertar");
                    if(!resultado)
                        logger.warn("No se pudo insertar el registro:"+codigoConcepto);
                    else
                    	//se actualiza el concepto Antiguo
                    	conceptoForm.setMapConceptos("codigoConceptoAntiguo_"+k,codigoConcepto);
				}
                catch(SQLException e)
				{;}
	        }
        }
        //se conserva el offset
        int offset=conceptoForm.getOffset();
        conceptoForm.reset(usuario.getCodigoInstitucionInt());		
        conceptoForm.setOffset(offset);
        conceptoForm.setMapConceptos(mundoCastigo.consultarConceptoCastigo(con,false,usuario.getCodigoInstitucionInt()));
        mundoCastigo.reset();
        conceptoForm.setMapConceptosBD(mundoCastigo.consultarConceptoCastigo(con,false,usuario.getCodigoInstitucionInt()));//copia de los registros de la BD, sin ninguna modificación
                
        this.cerrarConexion(con);
        return mapping.findForward("ingresarModificarCastigo");
	}

	/**
	 * @param con
	 * @param conceptosForm
	 * @param usuario
	 * @param i
	 * @param registroViejo
	 */
	private void generarLogCastigos(Connection con, ConceptosCarteraForm conceptosForm, UsuarioBasico usuario, int i, HashMap registroViejo, int pos) {
		String log = "";
		int tipoLog=0;
        if(i==ConstantesBD.tipoRegistroLogEliminacion)
        {
	            log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
		        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoConcepto_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+pos)+""+"] " +
		        			 "\n*  Ajuste Credito ["+Utilidades.obtenerDescripcionConceptoAjuste(con,conceptosForm.getMapConceptos("ajusteCredito_"+pos)+"",usuario.getCodigoInstitucionInt())+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
        }
        if(i==ConstantesBD.tipoRegistroLogModificacion)
        {
        	
	            log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
		        			 "\n*  Código Concepto [" +registroViejo.get("codigoConceptoAntiguo_0")+""+"] "+
		        			 "\n*  Descripción ["+registroViejo.get("descripcion_0")+""+"] " +
		        			 "\n*  Ajuste Credito ["+Utilidades.obtenerDescripcionConceptoAjuste(con,registroViejo.get("ajusteCredito_0")+"",usuario.getCodigoInstitucionInt())+""+"] "+
		        			 "\n   ===========INFORMACION MODIFICADA========== 	" +
		        			 "\n*  Código Concepto [" +conceptosForm.getMapConceptos("codigoConcepto_"+pos)+""+"] "+
		        			 "\n*  Descripción ["+conceptosForm.getMapConceptos("descripcion_"+pos)+""+"] " +
		        			 "\n*  Ajuste Credito ["+Utilidades.obtenerDescripcionConceptoAjuste(con,conceptosForm.getMapConceptos("ajusteCredito_"+pos)+"",usuario.getCodigoInstitucionInt())+""+"] " +
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogModificacion;
        }        
        LogsAxioma.enviarLog(ConstantesBD.logConceptoCastigoCodigo,log,tipoLog,usuario.getLoginUsuario());
	
	}

	/**
	 * Método encargado de insertar un nuevo concepto de glosa
	 * @param con
	 * @param conceptosForm
	 * @param response
	 * @param request
	 * @return
	 */
	private ActionForward ingresarNuevoConceptoGlosa(Connection con, ConceptosCarteraForm conceptosForm, HttpServletResponse response, HttpServletRequest request) 
	{
		 int posicion = Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
	        conceptosForm.setMapConceptos("codigoConcepto_"+posicion,"");
	        conceptosForm.setMapConceptos("descripcion_"+posicion,"");
	        conceptosForm.setMapConceptos("conceptoGeneral_"+posicion,"");
	        conceptosForm.setMapConceptos("conceptoEspecifico_"+posicion,"");
	        conceptosForm.setMapConceptos("cuentadebito_"+posicion,"");
	        conceptosForm.setMapConceptos("cuentacredito_"+posicion,"");
	        conceptosForm.setMapConceptos("tipo_"+posicion,"MEM");
	        conceptosForm.setMapConceptos("existeRelacion_"+posicion,"false");
	        posicion ++;
	        conceptosForm.setMapConceptos("numRegistros",posicion+"");
	        
	        logger.info("\n\nMAPA CONCEPTOS----------->"+conceptosForm.getMapConceptos()+"\n\n");

	        return this.redireccion(con,conceptosForm,response,request,"ingresarModificarConceptosGlosas.jsp");   
	}
	
	/**
	 * @param con
	 * @param conceptosForm
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward ingresarNuevoConceptoCastigo(Connection con, ConceptosCarteraForm conceptosForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		 int posicion = Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
	        
	        conceptosForm.setMapConceptos("codigoConcepto_"+posicion,"");
	        conceptosForm.setMapConceptos("descripcion_"+posicion,"");
	        conceptosForm.setMapConceptos("ajusteCredito_"+posicion,"");
	        conceptosForm.setMapConceptos("eliminado_"+posicion,"no");
	        conceptosForm.setMapConceptos("existeRelacion_"+posicion,"false");
	        conceptosForm.setMapConceptos("tipo_"+posicion,"MEM");//marcado como registro que pertenece a la memoria.
	        posicion ++;
	        conceptosForm.setMapConceptos("numRegistros",posicion+"");
			
	        int maxPageItems=Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
	        int numRegistros=Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+"");
	        int sobrante=numRegistros%maxPageItems;
	        if(sobrante==0)
	        	conceptosForm.setOffset(numRegistros-maxPageItems);
	        else
	        	conceptosForm.setOffset(numRegistros-sobrante);
	        
	        try
			{
	        	response.sendRedirect("../ingresarModificarConceptoCastigo/conceptoCastigo.jsp?pager.offset="+conceptosForm.getOffset());
			}
	        catch(IOException e)
			{
	        	e.printStackTrace();
			}
	        
	        this.cerrarConexion(con);
	        return null;
	        
	    
	}
	/**
	 * 
	 * @param conceptosCarteraForm ConceptosCarteraForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página "consultarConceptoCastigo.jsp"
	 * @throws SQLException
	 */
	private ActionForward resultadoBusquedaAvanzadaCastigo(ConceptosCarteraForm conceptosForm, 
														   ActionMapping mapping, 
														   Connection con,  UsuarioBasico usuario) throws SQLException
	{
		ConceptosCartera mundoConceptos = new ConceptosCartera ();
        mundoConceptos.setCodigo(conceptosForm.getCodigoConcepto());       
        mundoConceptos.setDescripcion(conceptosForm.getDescripcion());
        mundoConceptos.setAjusteCredito(conceptosForm.getAjusteCredito());
       	conceptosForm.getMapConceptos().clear();
      	conceptosForm.setMapConceptos(mundoConceptos.consultarConceptoCastigo(con,true,usuario.getCodigoInstitucionInt()));
         
        conceptosForm.setNumRegistros(Integer.parseInt(conceptosForm.getMapConceptos("numRegistros")+""));//actualizar el numero de registros
        
       
        //se limpian los campos de la búsqueda avanzada
        conceptosForm.setCodigoConcepto("");
        conceptosForm.setDescripcion("");
        conceptosForm.setAjusteCredito("-1");
        
        this.cerrarConexion(con);        
        
        if(conceptosForm.getAccion().equals("busquedaAvanzadaCastigoConsulta"))
        {
        	conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            return mapping.findForward("consultarCastigo");
        }
        else if(conceptosForm.getAccion().equals("busquedaAvanzadaCastigo"))
        {
        	 conceptosForm.setAccion("");//limpiar la acción para no mostrar en el formulario, la busqueda Avanzada
            return mapping.findForward("ingresarModificarCastigo");
        }
        else
        {
            logger.warn("acción no válida dentro de la busqueda avanzada");
            return null;
        }
	}
	
	 /**
	  * Método usado para eliminar un registro en el listado de Conceptos Castigo
	  * @param conceptoForm
	  * @param mapping
	  * @param con
	  * @param usuario
	  * @return
	  * @throws SQLException
	  */
	 private ActionForward eliminarConceptoCastigo(ConceptosCarteraForm conceptoForm,
	 												ActionMapping mapping,Connection con, UsuarioBasico usuario) throws SQLException
     {
	 	ConceptosCartera conceptos=new ConceptosCartera();
	 	int pos = conceptoForm.getPosRegistro();
	 	boolean resp=true;
	 	
	 	//se verifica si el registro está siendo usado en otras funcionalidades
	    if((conceptoForm.getMapConceptos("existeRelacion_"+pos)+"").equals("false"))
	    {
	    	//se verifica si el registro existe en la BD para ejecutar el proceso de eliminación
	    	if((conceptoForm.getMapConceptos("tipo_"+pos)+"").equals("BD")) //eliminar bd
	        {
	    		String codigoConcepto=conceptoForm.getMapConceptos("codigoConcepto_"+pos)+"";
	    		String codigoConceptoAntiguo=conceptoForm.getMapConceptos("codigoConceptoAntiguo_"+pos)+"";
	    		String descripcion=conceptoForm.getMapConceptos("descripcion_"+pos)+"";
	    		String ajuste=conceptoForm.getMapConceptos("ajusteCredito_"+pos)+"";
	    		resp=conceptos.insertarModificarEliminarConceptoCastigoTrans(con,usuario.getCodigoInstitucionInt(),codigoConceptoAntiguo,codigoConcepto,descripcion,ajuste,"eliminar");
	    		this.generarLogCastigos(con,conceptoForm,usuario,ConstantesBD.tipoRegistroLogEliminacion,null,pos);
	        }
	    	
	    	//se el proceso es exitoso se borra el registro del mapa
	    	if(resp)
	    	{
	    		int numRegistros=Integer.parseInt(conceptoForm.getMapConceptos("numRegistros")+"");
	    		
	    		//se elimina registro del mapa
	    		for(int i=pos;i<(numRegistros-1);i++)
	    		{
	    			conceptoForm.setMapConceptos("codigoConcepto_"+i,conceptoForm.getMapConceptos("codigoConcepto_"+(i+1)));
				    conceptoForm.setMapConceptos("codigoConceptoAntiguo_"+i,conceptoForm.getMapConceptos("codigoConceptoAntiguo_"+(i+1)));
				    conceptoForm.setMapConceptos("descripcion_"+i,conceptoForm.getMapConceptos("descripcion_"+(i+1)));
				    conceptoForm.setMapConceptos("ajusteCredito_"+i,conceptoForm.getMapConceptos("ajusteCredito_"+(i+1)));
				    conceptoForm.setMapConceptos("existeRelacion_"+i,conceptoForm.getMapConceptos("existeRelacion_"+(i+1)));
				    conceptoForm.setMapConceptos("tipo_"+i,conceptoForm.getMapConceptos("tipo_"+(i+1)));
	    		}
	    		numRegistros--;
	    		//se elimina último registros (sobra)
	    		conceptoForm.getMapConceptos().remove("codigoConcepto_"+numRegistros);
	    		conceptoForm.getMapConceptos().remove("codigoConceptoAntiguo_"+numRegistros);
	    		conceptoForm.getMapConceptos().remove("descripcion_"+numRegistros);
	    		conceptoForm.getMapConceptos().remove("ajusteCredito_"+numRegistros);
	    		conceptoForm.getMapConceptos().remove("existeRelacion_"+numRegistros);
	    		conceptoForm.getMapConceptos().remove("tipo_"+numRegistros);
	    		
	    		//se actualiza tamaño del HashMap
	    		conceptoForm.setMapConceptos("numRegistros",numRegistros+"");
	    	}
	    		
	    }   
	    
	  
        this.cerrarConexion(con);									
        return mapping.findForward("ingresarModificarCastigo");
     }
	 
	/**
	 * 
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{

		if(con != null)
		return con;
					
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
			}
			catch(Exception e)
			{
			    logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
				return null;
			}
					
			return con;
	}
		 
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try{
	        if (con!=null&&!con.isClosed())
	        {
	        	UtilidadBD.closeConnection(con);
	        }
	    }
	    catch(Exception e){
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
