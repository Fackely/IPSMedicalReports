package com.princetonsa.action.facturacion;

import java.math.BigDecimal;
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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.SolicitarAnulacionFacturasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.SolicitarAnulacionFacturas;
import com.princetonsa.mundo.facturacion.ValidacionesAnulacionFacturas;

public class SolicitarAnulacionFacturasAction extends Action 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(SolicitarAnulacionFacturasAction.class);
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof SolicitarAnulacionFacturasForm) 
			{
				SolicitarAnulacionFacturasForm forma=(SolicitarAnulacionFacturasForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ActionErrors errores = new ActionErrors();

				SolicitarAnulacionFacturas mundo = new SolicitarAnulacionFacturas();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de SolicitarAnulacionFacturasAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				//////////// Estados para las Solicitudes de Anulacion de Facturas.
				else if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					forma.resetMensaje();
					errores = ValidacionesBusquedaFactura(con,forma,mundo,usuario);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						forma.setMapaSolictudes(new HashMap());
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					forma.resetBusqueda();
					forma.setMapaSolictudes(mundo.consultarFactura(con, forma.getConsecutivoFactura(), usuario.getCodigoInstitucionInt()));

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarSolicitud"))
				{
					errores = ValidacionesGuardarSolicitud(con,forma);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					this.accionGuardarSolicitudAnulacion(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenSolicitud");
				}
				/////////////////// Estados Para las Anulaciones de Solicitudes de Facturas.
				else if(estado.equals("empezarAnular"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("anularSolicitudes");
				}
				else if(estado.equals("buscarAnular"))
				{
					errores = ValidacionesBusquedaSolicitudesAnular(con,forma);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("anularSolicitudes");
					}
					forma.setMapaListadoSolicitudes(mundo.consultarSolictudesAnular(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getConsecutivoFactura(), forma.getCodigoSolicitud(), forma.getUsuarioAutoriza(), forma.getMotivoAnulacion(), forma.getCentroAtencion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoAnular");
				}
				else if(estado.equals("detalleSolicitud"))
				{
					forma.setMapaDetalleSolictud(mundo.consultaDetalleSolicitud(con, forma.getMapaListadoSolicitudes().get("numerosolicitud_"+forma.getIndiceDetalle())+""));
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
					this.accionGuardarAnularSolictud(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenAnular");
				}
				else if(estado.equals("volverListado"))
				{
					forma.setMapaListadoSolicitudes(mundo.consultarSolictudesAnular(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getConsecutivoFactura(), forma.getCodigoSolicitud(), forma.getUsuarioAutoriza(), forma.getMotivoAnulacion(), forma.getCentroAtencion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoAnular");
				}
				//////////////Estados Para la Consulta de Solicitudes.
				else if(estado.equals("empezarConsulta"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("anularSolicitudes");
				}
				else if(estado.equals("buscarConsulta"))
				{
					errores = ValidacionesBusquedaSolicitudesAnular(con,forma);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("anularSolicitudes");
					}
					forma.setMapaListadoSolicitudes(mundo.consultarSolictudesAnular(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getConsecutivoFactura(), forma.getCodigoSolicitud(), forma.getUsuarioAutoriza(), forma.getMotivoAnulacion(), forma.getCentroAtencion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoAnular");
				}
				else if(estado.equals("detalleConsulta"))
				{
					forma.setMapaDetalleSolictud(mundo.consultaDetalleSolicitud(con, forma.getMapaListadoSolicitudes().get("numerosolicitud_"+forma.getIndiceDetalle())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleAnular");
				}
				else
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucion());
					logger.warn("Estado no valido dentro del flujo de SOLICITAR ANULACION FACTURAS");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de SolicitarAnulacionFacturasForm");
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
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarAnularSolictud(Connection con, SolicitarAnulacionFacturasForm forma, SolicitarAnulacionFacturas mundo, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		HashMap vo = new HashMap();
		
		vo.put("observaciones", forma.getObservaciones());
		vo.put("estado", ConstantesIntegridadDominio.acronimoEstadoAnulado);
		vo.put("usuarioanula", usuario.getLoginUsuario());
		vo.put("fechaanula", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		vo.put("horaanula", UtilidadFecha.getHoraActual());
		vo.put("numerosolicitud", forma.getMapaDetalleSolictud().get("numerosolicitud_0"));
		transaccion= mundo.insertarAnulacionSolicitud(con, vo);
		
		if(transaccion)
		{
			forma.setMapaResumenAnulacion(mundo.consultarResumenSolictud(con, Utilidades.convertirAEntero(forma.getMapaDetalleSolictud().get("numerosolicitud_0")+"")));
			
		}
		else
		{
			transaccion=false;
		}
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionErrors ValidacionesGuardarAnular(Connection con, SolicitarAnulacionFacturasForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getObservaciones().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El campo observaciones de anulación de solicitud "));
		}
		
		return errores;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionErrors ValidacionesBusquedaSolicitudesAnular(Connection con, SolicitarAnulacionFacturasForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getFechaInicial().equals("")&&forma.getFechaFinal().equals("")&&forma.getConsecutivoFactura().equals("")&&forma.getCodigoSolicitud().equals("")&&forma.getUsuarioAutoriza().equals("")&&forma.getMotivoAnulacion().equals("")&&forma.getCentroAtencion().equals(""))
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
	private ActionErrors ValidacionesGuardarSolicitud(Connection con, SolicitarAnulacionFacturasForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getUsuarioAutoriza().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El Usuario que autoriza "));
		}
		if(forma.getMotivoAnulacion().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El Motivo de la anulación "));
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
	private void accionGuardarSolicitudAnulacion(Connection con, SolicitarAnulacionFacturasForm forma, SolicitarAnulacionFacturas mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		int codigoSolicitud= ConstantesBD.codigoNuncaValido;
		
		HashMap vo = new HashMap();
		
		vo.put("codigofactura", forma.getMapaSolictudes().get("codigofactura_0"));
		vo.put("consecutivofactura", forma.getMapaSolictudes().get("consecutivofactura_0"));
		vo.put("usuarioautoriza", forma.getUsuarioAutoriza());
		vo.put("motivoanulacion", forma.getMotivoAnulacion());
		vo.put("observaciones", forma.getObservaciones());
		vo.put("estado", ConstantesIntegridadDominio.acronimoEstadoPendiente);
		vo.put("centroatencion", usuario.getCodigoCentroAtencion());
		vo.put("usuariosolicita", usuario.getLoginUsuario());
		vo.put("fechasolicita", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		vo.put("horasolicita", UtilidadFecha.getHoraActual());
		codigoSolicitud= mundo.insertarSolictud(con, vo);
		
		if(codigoSolicitud>0)
		{
			forma.setMapaResumenSolictud(mundo.consultarResumenSolictud(con, codigoSolicitud));
			transaccion=true;
		}
		else
		{
			transaccion=false;
		}
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionErrors ValidacionesBusquedaFactura(Connection con, SolicitarAnulacionFacturasForm forma, SolicitarAnulacionFacturas mundo, UsuarioBasico usuario) 
	{
		
		ActionErrors errores = new ActionErrors();
		
		if(forma.getConsecutivoFactura().equals(""))
		{
			errores.add("", new ActionMessage("errors.notEspecific","Por  favor ingrese el # de factura para realizar la solicitud."));
		}
		
		forma.setCodigoFactura(Utilidades.obtenerCodigoFactura(Utilidades.convertirAEntero(forma.getConsecutivoFactura()), usuario.getCodigoInstitucionInt()));
		
		ValidacionesAnulacionFacturas objectValidacionesAnulacion= new ValidacionesAnulacionFacturas();
		
		if((Factura.busquedaPorConsecutivoDianEInstitucion(con,forma.getConsecutivoFactura(), usuario.getCodigoInstitucionInt(),"")).size()==0)
	    {
			errores.add("descripcion",new ActionMessage("errors.notEspecific","El # de Factura "+forma.getConsecutivoFactura()+" no existe. Por Favor verifique"));	
	    }
		int estadoFactura= Factura.cargarFactura(new BigDecimal(forma.getCodigoFactura()), false).getEstadoFacturacion().getCodigo();
		
		if(estadoFactura!=ConstantesBD.codigoEstadoFacturacionFacturada)
		{
		    errores.add("No cumple con el estado de facturacion", new ActionMessage("error.facturacion.estadoFacturacion", "Facturada"));
		}
		if(UtilidadValidacion.estadoSolicitudAnulacion(con, forma.getCodigoFactura()))
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Para El # de Factura "+forma.getConsecutivoFactura()+" ya existe una solicitud de anulacion."));
		}
		return errores;
	}
	
	
	
}
