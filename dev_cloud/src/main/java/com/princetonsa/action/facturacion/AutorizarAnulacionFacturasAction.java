package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.AutorizarAnulacionFacturasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.AutorizarAnulacionFacturas;

public class AutorizarAnulacionFacturasAction extends Action 
{

	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(AutorizarAnulacionFacturasAction.class);
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{	
			if (form instanceof AutorizarAnulacionFacturasForm) 
			{
				AutorizarAnulacionFacturasForm forma=(AutorizarAnulacionFacturasForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ActionErrors errores = new ActionErrors();

				AutorizarAnulacionFacturas mundo = new AutorizarAnulacionFacturas();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de AutorizarAnulacionFacturasAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				//////////// Estados para Autorizar la Anulacion de Facturas.
				else if(estado.equals("empezar"))
				{
					forma.reset();
					errores = validacionesInicio(con, forma, usuario);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}
					forma.setMapaListadoSolicitudes(mundo.consultarListadoSolicitudes(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("detalleSolicitud"))
				{
					forma.setMapaDetalleSolicitud(mundo.consultarDetalleSolicitud(con, forma.getMapaListadoSolicitudes().get("numerosolicitud_"+forma.getIndiceDetalle())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("guardarAutorizar"))
				{
					errores = ValidacionesGuardarAutorizacion(con,forma);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalle");
					}
					this.accionGuardarSolicitudAutorizacion(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumen");
				}
				/////////// Estados para Anular Autorizaciones de Facturas. 
				else if(estado.equals("empezarAnular"))
				{
					forma.reset();
					errores = validacionesInicio(con, forma, usuario);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaErroresActionErrors");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("anularAutorizaciones");
				}
				else if(estado.equals("buscarAnular"))
				{
					errores = ValidacionesBusquedaAutorizacionesAnular(con,forma);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("anularAutorizaciones");
					}
					forma.setMapaListadoAprobadas(mundo.consultarListadoAprobadas(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getConsecutivoFactura(), forma.getNumeroAutorizacion(), forma.getUsuarioAutoriza()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoAnular");
				}
				else if(estado.equals("volverListado"))
				{
					forma.setMapaListadoAprobadas(mundo.consultarListadoAprobadas(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getConsecutivoFactura(), forma.getNumeroAutorizacion(), forma.getUsuarioAutoriza()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoAnular");
				}
				else if(estado.equals("detalleAutorizacion"))
				{
					forma.setMapaDetalleAutorizacion(mundo.consultarResumenAutorizar(con, forma.getMapaListadoAprobadas().get("numeroautoriza_"+forma.getIndiceDetalle())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAnular");
				}
				else if(estado.equals("guardarAnular"))
				{
					errores = ValidacionesGuardarAnular(con,forma);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalleAnular");
					}
					this.accionGuardarAnularAutorizacion(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenAnular");
				}
				/////////// Estados para Consultar Autorizaciones de Facturas.
				else if(estado.equals("empezarConsulta"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("consultarAutorizaciones");
				}
				else if(estado.equals("filtrarUsuarios"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("consultarAutorizaciones");
				}
				else if(estado.equals("buscarConsulta"))
				{
					errores = ValidacionesBusquedaAutorizaciones(con,forma);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("consultarAutorizaciones");
					}
					forma.setMapaConsultaListado(mundo.consultaListadoAutorizaciones(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getConsecutivoFactura(), forma.getEstadoAutorizacion(), forma.getUsuarioAutoriza(), forma.getMotivoAnulacion(), forma.getCentroAtencion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoAutorizaciones");
				}
				else if(estado.equals("detalleConsulta"))
				{
					forma.setMapaConsultadDetalle(mundo.consultaDetalleAutorizacion(con, forma.getMapaConsultaListado().get("numerosolicitud_"+forma.getIndiceDetalle())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAutorizaciones");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de AUTORIZACION ANULACION FACTURAS");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de AutorizarAnulacionFacturasForm");
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
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionErrors validacionesInicio(Connection con, AutorizarAnulacionFacturasForm forma, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(!UtilidadValidacion.usuarioAutorizadoAnular(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion()))
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","USUARIO NO PERMITIDO PARA INGRESAR A ESTA OPCION."));
		}
		return errores;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionErrors ValidacionesBusquedaAutorizaciones(Connection con, AutorizarAnulacionFacturasForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getFechaInicial().equals("")&&forma.getFechaFinal().equals("")&&forma.getConsecutivoFactura().equals("")&&forma.getEstadoAutorizacion().equals("")&&forma.getUsuarioAutoriza().equals("")&&forma.getMotivoAnulacion().equals("")&&forma.getCentroAtencion().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Para Realizar la busqueda debe diligenciar al menos un campo. Por favor verifique."));
		}
		if(!UtilidadTexto.isEmpty(forma.getFechaInicial()) || !UtilidadTexto.isEmpty(forma.getFechaFinal()))
		{
			boolean centinelaErrorFechas=false;
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaInicial()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+forma.getFechaInicial()));
				centinelaErrorFechas=true;
			}
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaFinal()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+forma.getFechaFinal()));
				centinelaErrorFechas=true;
			}
			
			if(!centinelaErrorFechas)
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(), forma.getFechaFinal()))
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+forma.getFechaInicial(), "final "+forma.getFechaFinal()));
				}
			}
			if(!centinelaErrorFechas)
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(), UtilidadFecha.getFechaActual()))
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+forma.getFechaInicial(), "Actual "+UtilidadFecha.getFechaActual()));
				}
			}
			if(!centinelaErrorFechas)
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(), UtilidadFecha.getFechaActual()))
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+forma.getFechaFinal(), "Actual "+UtilidadFecha.getFechaActual()));
				}
			}
		}
		return errores;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionErrors ValidacionesBusquedaAutorizacionesAnular(Connection con, AutorizarAnulacionFacturasForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getFechaInicial().equals("")&&forma.getFechaFinal().equals("")&&forma.getConsecutivoFactura().equals("")&&forma.getNumeroAutorizacion().equals("")&&forma.getUsuarioAutoriza().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Para Realizar la busqueda debe diligenciar al menos un campo. Por favor verifique."));
		}
		if(!UtilidadTexto.isEmpty(forma.getFechaInicial()) || !UtilidadTexto.isEmpty(forma.getFechaFinal()))
		{
			boolean centinelaErrorFechas=false;
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaInicial()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+forma.getFechaInicial()));
				centinelaErrorFechas=true;
			}
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaFinal()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+forma.getFechaFinal()));
				centinelaErrorFechas=true;
			}
			
			if(!centinelaErrorFechas)
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(), forma.getFechaFinal()))
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+forma.getFechaInicial(), "final "+forma.getFechaFinal()));
				}
			}
			if(!centinelaErrorFechas)
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(), UtilidadFecha.getFechaActual()))
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+forma.getFechaInicial(), "Actual "+UtilidadFecha.getFechaActual()));
				}
			}
			if(!centinelaErrorFechas)
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(), UtilidadFecha.getFechaActual()))
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+forma.getFechaFinal(), "Actual "+UtilidadFecha.getFechaActual()));
				}
			}
		}
		return errores;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarAnularAutorizacion(Connection con, AutorizarAnulacionFacturasForm forma, AutorizarAnulacionFacturas mundo, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		HashMap vo = new HashMap();
		
		vo.put("observaciones", forma.getObservaciones());
		vo.put("estado", ConstantesIntegridadDominio.acronimoEstadoAutorizacionAnulada);
		vo.put("usuarioanula", usuario.getLoginUsuario());
		vo.put("fechaanula", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		vo.put("horaanula", UtilidadFecha.getHoraActual());
		vo.put("numerosolicitud", forma.getMapaDetalleAutorizacion().get("numerosolicitud_0"));
		transaccion= mundo.insertarAnulacionAutorizacion(con, vo);
		
		if(transaccion)
		{
			forma.setMapaResumenAnulacion(mundo.consultarResumenAutorizar(con, forma.getMapaDetalleAutorizacion().get("numerosolicitud_0")+""));
			
		}
		else
		{
			transaccion=false;
		}
		if(transaccion)
		{
			//forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			//forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionErrors ValidacionesGuardarAnular(Connection con, AutorizarAnulacionFacturasForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getObservaciones().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El Campo Observaciones Autorización Anulada "));
		}
		
		return errores;
	}



	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarSolicitudAutorizacion(Connection con, AutorizarAnulacionFacturasForm forma, AutorizarAnulacionFacturas mundo, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		HashMap vo = new HashMap();
		
		vo.put("codigosolicitud", forma.getMapaDetalleSolicitud().get("numerosolicitud_0"));
		vo.put("observaciones", forma.getObservaciones());
		vo.put("autorizada", forma.getAutorizaAnulacion());
		if(forma.getAutorizaAnulacion().equals(ConstantesBD.acronimoSi))
			vo.put("estado", ConstantesIntegridadDominio.acronimoEstadoAprobado);
		else
			vo.put("estado", ConstantesIntegridadDominio.acronimoEstadoPendiente);
		vo.put("fechaautoriza", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		vo.put("horaautoriza", UtilidadFecha.getHoraActual());
		transaccion= mundo.insertarAutorizacion(con, vo);
		
		if(transaccion)
		{
			forma.setMapaResumenAutorizar(mundo.consultarResumenAutorizar(con, forma.getMapaDetalleSolicitud().get("numerosolicitud_0")+""));
			transaccion=true;
		}
		else
		{
			transaccion=false;
		}
		if(transaccion)
		{
			//forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			//forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionErrors ValidacionesGuardarAutorizacion(Connection con, AutorizarAnulacionFacturasForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getAutorizaAnulacion().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El campo Anulación Autorizada "));
		}
		
		return errores;
		
	}

	
	
}
