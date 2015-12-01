/*
 * Ene 06, 2008
 */
package com.princetonsa.action.glosas;

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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.glosas.RegistroAuditoriaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.Glosas;

/**
 * @author Sebastián Gómez - Luis Felipe Perez Granda
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Registro Auditoría (GLOSAS)
 */
public class RegistroAuditoriaAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(RegistroAuditoriaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @author Sebastián Gómez
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response ) throws Exception
		{

		Connection con=null;
		try{


			if (response==null); //Para evitar que salga el warning
			if(form instanceof RegistroAuditoriaForm)
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
				RegistroAuditoriaForm registroForm =(RegistroAuditoriaForm)form;
				Glosas mundo = new Glosas();
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");


				String estado=registroForm.getEstado();
				logger.info("**********************************************");
				logger.warn("===> Estado RegistroAuditoriaAction--> "+estado+" "+registroForm.getLinkSiguiente());
				logger.info("**********************************************");

				if(estado == null)
				{
					registroForm.reset();	
					logger.warn("Estado no valido dentro del flujo de REGISTRO AUDITORIA (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con, registroForm, mapping, usuario);
				}
				else if(estado.equals("buscar"))
				{
					ActionErrors errores = new ActionErrors();
					errores = validarBusquedaFacturas(con, registroForm, errores); 
					if(!errores.isEmpty())
					{
						logger.info("===> Hubo errores :(");
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("busquedaFacturas");
					}
					else
					{
						return accionBuscarFacturas(con, registroForm, mundo, mapping, usuario, response, request);
					}
				}
				else if (estado.equals("redireccion")){
					UtilidadBD.closeConnection(con);
					logger.info(registroForm.getLinkSiguiente());
					response.sendRedirect(registroForm.getLinkSiguiente());
					return null;
				}else if (estado.equals("redireccionSolicitud")){
					UtilidadBD.closeConnection(con);
					logger.info(registroForm.getLinkSiguienteSolicitudes());
					response.sendRedirect(registroForm.getLinkSiguienteSolicitudes());
					return null;
				}else if (estado.equals("redireccionAsocios")){
					UtilidadBD.closeConnection(con);
					logger.info(registroForm.getLinkSiguienteAsocios());
					response.sendRedirect(registroForm.getLinkSiguienteAsocios());
					return null;
				}


				else if (estado.equals("ordenarFacturas"))
				{
					return accionOrdenarFacturas(con, registroForm, mapping);
				}
				else if (estado.equals("guardarEncabezado"))
				{
					return accionGuardarEncabezado(con,registroForm,usuario,request,response, mapping);
				}
				else if (estado.equals("insertarObservacionAuditora"))
				{
					String obs=UtilidadTexto.agregarTextoAObservacionFechaGrabacion(registroForm.getListadoFacturas().get("observaciones_"+registroForm.getIndiceObservaciones()).toString(),registroForm.getObservacionesDetalle(), usuario, true);
					Glosas.actualizarObservacionAuditoria(registroForm.getListadoFacturas().get("codigoAuditoria_"+registroForm.getIndiceObservaciones())+"",obs);
					registroForm.getListadoFacturas().put("observaciones_"+registroForm.getIndiceObservaciones(),obs);
					UtilidadBD.closeConnection(con);
					registroForm.setObservacionesDetalle("");
					return mapping.findForward("observacionesFacturas");
				}
				else if (estado.equals("volverListadoFacturas"))
				{
					return accionVolverListadoFacturas(con,registroForm,mapping);
				}
				else if (estado.equals("detalleFactura"))
				{
					return accionDetalleFactura(con,registroForm,mapping,usuario,request);
				}
				else if (estado.equals("eliminarDetalle")) //estado para eliminar un detalle de la factura
				{
					return accionEliminarDetalle(con,registroForm,mapping,usuario);
				}
				else if (estado.equals("eliminarConceptoDetalle")) //estado para eliminaar un concepto del detalle
				{
					return accionEliminarConceptoDetalle(con,registroForm,mapping);
				}
				else if (estado.equals("eliminarConceptoDetalleAsocio")) //estado para eliminar un concepto de un asocio de un detalle
				{
					return accionEliminarConceptoDetalleAsocio(con,registroForm,mapping);
				}
				else if (estado.equals("guardarConceptoDetalle")) //Estado para almacenar el concepto de un detalle factua (LLAMADO DESDE POPUP)
				{
					return accionGuardarConceptoDetalle(con,registroForm,mapping,usuario);
				}
				else if (estado.equals("guardarConceptoDetalleAsocio")) //Estado para almacenar el concepto de un detalle de asocio (LLAMADO DESDE POPUP)
				{
					return accionGuardarConceptoDetalleAsocio(con,registroForm,mapping,usuario);
				}
				else if (estado.equals("cambioConceptoDevolucion"))
				{
					return accionCambioConceptoDevolucion(con,registroForm,mapping,usuario);
				}
				else if (estado.equals("recargarFrameDetalle"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("frameDetalleSolicitud");
				}
				else if (estado.equals("recargarDetalleAsocios"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("popUpSeleccionAsociosFactura");
				}
				else if (estado.equals("guardarAsocios"))
				{
					return accionGuardarAsocios(con,registroForm,mapping,usuario,request);
				}
				else if(estado.equals("guardarFrameDetalle"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("frameDetalleSolicitud");
				}
				else if (estado.equals("guardarDetalle"))
				{
					return accionGuardarDetalle(con,registroForm,mapping,usuario,request);
				}
				///***********ESTADOS PARA LA BUSQUEDA AVANZADA DE SOLICITUDES*************************
				else if (estado.equals("iniciarBusquedaSolicitudes")||estado.equals("realizarBusquedaSolicitudes"))
				{
					return accionBusquedaSolicitudes(con,registroForm,mapping,usuario, response);
				}
				else if (estado.equals("seleccionarBusquedaSolicitudes"))
				{
					return accionSeleccionarBusquedaSolicitudes(con,registroForm,mapping,usuario);
				}
				//**************************************************************************************
				else if(estado.equals("ordenarSolicitudesFactura"))
				{
					return this.accionOrdenarSolicitudesFactura(registroForm, con, mapping, response);
				}
				else
				{
					registroForm.reset();
					logger.warn("Estado no valido dentro del flujo de RegistroAuditoriaAction (null) ");
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

	/**
     * 
     * @param forma
     */    
    private ActionForward accionOrdenarSolicitudesFactura(RegistroAuditoriaForm forma, Connection con, ActionMapping mapping, HttpServletResponse response) 
    {
    	String[] indices = Glosas.indicesSolicitudesFacturas;
		int numReg = Utilidades.convertirAEntero(forma.getGlosa().getBusquedaSolicitudes("numRegistros")+"");
		forma.getGlosa().setBusquedaSolicitudes(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoIndice(), forma.getGlosa().getBusquedaSolicitudes(), numReg));
		forma.setUltimoIndice(forma.getPatronOrdenar());
		forma.getGlosa().setBusquedaSolicitudes("numRegistros",numReg+"");
		
		UtilidadBD.closeConnection(con);
		if(UtilidadTexto.isEmpty(forma.getLinkSiguienteSolicitudes()))
		{		
			return mapping.findForward("busquedaAvanzadaSolicitudes");
		}
		else
		{
			forma.setLinkSiguiente(forma.getLinkSiguienteSolicitudes().replaceAll("pager.offset=\\d+", "pager.offset=0"));
			logger.info(forma.getLinkSiguienteSolicitudes());
			try 
			{
				response.sendRedirect(forma.getLinkSiguienteSolicitudes());
			} 
			catch (IOException e) 
			{
				logger.info(""+e.toString());
			}
			return null;
		}
    }
	
	
	/**
	 * Método para hacer un guardar total de la glosa de la factura
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDetalle(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		UtilidadBD.iniciarTransaccion(con);
		//Para verificar si la glosa es nueva o es una modificacion
		boolean esNueva = registroForm.getGlosa().getGlosaSistema().equals("")?true:false;
		
		Glosas mundoGlosa = new Glosas();
		mundoGlosa.setGlosa(registroForm.getGlosa());
		mundoGlosa.guardarGlosa(con, usuario, "RegistroAuditoria");
		
		if(mundoGlosa.getErrores().isEmpty())
		{
			UtilidadBD.finalizarTransaccion(con);
			registroForm.setGlosa(mundoGlosa.cargarGlosaAuditoria(con, registroForm.getCodigoAuditoria(),"", usuario,false));
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, mundoGlosa.getErrores());
			registroForm.setEstado("");
			//Si la glosa es nueva, se finaliza el uso del consecutivo
			if(esNueva&&!mundoGlosa.getGlosa().getGlosaSistema().equals(""))
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoGlosas,usuario.getCodigoInstitucionInt(), mundoGlosa.getGlosa().getGlosaSistema(),ConstantesBD.acronimoNo, ConstantesBD.acronimoNo );
		}
		
		registroForm.setCodigoFac(mundoGlosa.getGlosa().getFacturas().get(mundoGlosa.getGlosa().getPosicionFactura()).getCodigoFactura());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturas");
	}

	/**
	 * Método para guardar los asocios de un servicio de cirugia
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarAsocios(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		int posicionFactura = registroForm.getGlosa().getPosicionFactura();
		int posicionDetalle = registroForm.getGlosa().getFacturas().get(posicionFactura).getPosicionDetalle();
		
		//**************VALIDACIONES DATOS DE LOS ASOCIOS*********************************************************
		Glosas mundoGlosa = new Glosas();
		mundoGlosa.setGlosa(registroForm.getGlosa());
		registroForm.getGlosa().getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).setValorGlosaStr(registroForm.getSumatoriaValorGlosa()+"");
		mundoGlosa.validacionCamposAsocio(posicionFactura, posicionDetalle, true, false, "RegistroAuditoria");
		errores = mundoGlosa.getErrores();
		//**********************************************************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			registroForm.setEstado("");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionAsociosFactura");
	}


	/**
	 * Método para eliminar un concepto de detalle de asocio
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConceptoDetalleAsocio(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping) 
	{
		int posFactura = registroForm.getGlosa().getPosicionFactura();
		int posDetalle = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		int posAsocio = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionAsocio();
		int posConcepto = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionConcepto();
		
		
		registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getAsocios().get(posAsocio).getConceptos().get(posConcepto).setEliminado(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionAsociosFactura");
	}


	/**
	 * Método para guardar un concepto de detalle de asocio
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarConceptoDetalleAsocio(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		boolean existeConcepto = false;
		int posFactura = registroForm.getGlosa().getPosicionFactura();
		int posDetalle = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		int posAsocio = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionAsocio();
		
		//Se limpia mensaje de error
		registroForm.getGlosa().getFacturas().get(posFactura).setMensajeError("");
		
		//***********VALIDACION DE LA SELECCION DEL CONCEPTO***************************
		for(DtoConceptoGlosa concepto:registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getAsocios().get(posAsocio).getConceptos())
			if(concepto.getCodigoConcepto().equals(registroForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto())&&!concepto.isEliminado())
				existeConcepto = true;
		//*****************************************************************************
		
		//Si el concepto no fue seleccionado se agrega
		if(!existeConcepto)
		{
			DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
			nuevoConcepto.setCodigoConcepto(registroForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto());
			nuevoConcepto.setDescripcion(registroForm.getGlosa().getConceptoSeleccionado().getDescripcion());
			nuevoConcepto.setTipo(registroForm.getGlosa().getConceptoSeleccionado().getTipo());
			nuevoConcepto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			
			registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getAsocios().get(posAsocio).getConceptos().add(nuevoConcepto);
		}
		else
			registroForm.getGlosa().getFacturas().get(posFactura).setMensajeError("El concepto "+registroForm.getGlosa().getConceptoSeleccionado().getDescripcion()+" ya fue seleccionado");
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionConceptoDetalleAsocio");
	}


	/**
	 * Método implementado para seleccionar los detalles de la factura y agregarlos a la estructura de la glosa de la factura
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSeleccionarBusquedaSolicitudes(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		Glosas mundoGlosa = new Glosas();
		registroForm.setGlosa(mundoGlosa.agregarNuevosDetalleFacturaBusqueda(con,registroForm.getGlosa()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaAvanzadaSolicitudes");
	}



	/**
	 * Método para abrir la busqueda avanzada de solicitudes 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaSolicitudes(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping,UsuarioBasico usuario, HttpServletResponse response) 
	{
		int posicionFactura = registroForm.getGlosa().getPosicionFactura();
		int codigoTarifarioOficial = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()),true);
		
		if(registroForm.getEstado().equals("iniciarBusquedaSolicitudes"))
		{
			//Si la busqueda apenas empeiza se inicializan los campos de busqueda
			registroForm.getGlosa().setNumeroSolicitudBusqueda("");
			registroForm.getGlosa().setCodigoServicioBusqueda("");
			registroForm.getGlosa().setDescripcionServicioBusqueda("");
			registroForm.getGlosa().setCodigoArticuloBusqueda("");
			registroForm.getGlosa().setDescripcionArticuloBusqueda("");
			registroForm.getGlosa().setSeleccionTodosBusqueda(ConstantesBD.acronimoNo);
		}
		
		String codigoEstandarBusquedaArticulo = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(usuario.getCodigoInstitucionInt());
		
		Glosas mundoGlosa = new Glosas();
		registroForm.getGlosa().setBusquedaSolicitudes(mundoGlosa.cargarSolicitudesFactura(
			con, 
			Integer.parseInt(registroForm.getGlosa().getFacturas().get(posicionFactura).getCodigoFactura()), //codigo de la factura 
			registroForm.getGlosa().getFacturas().get(posicionFactura).obtenerCodigosDetalleFactura(),  //codigos de los detalles de factura ingresados
			codigoTarifarioOficial, //codigo tarifario oficial
			Utilidades.convertirAEntero(registroForm.getGlosa().getNumeroSolicitudBusqueda()), //numero de la solicitud 
			registroForm.getGlosa().getCodigoServicioBusqueda(), //codigo servicio
			registroForm.getGlosa().getDescripcionServicioBusqueda(), //descripcion servicio 
			Utilidades.convertirAEntero(registroForm.getGlosa().getCodigoArticuloBusqueda()), //codigo articulo 
			registroForm.getGlosa().getDescripcionArticuloBusqueda(), //descripcoin articulo
			codigoEstandarBusquedaArticulo) // Codigo Estandar Busqueda por Articulo
		);
		registroForm.getGlosa().setTarifarioOficialBusqueda(UtilidadesFacturacion.obtenerNombreTarifarioOficial(con,codigoTarifarioOficial));
		
		Utilidades.imprimirMapa(registroForm.getGlosa().getBusquedaSolicitudes());
		
		UtilidadBD.closeConnection(con);
		
		logger.info("******************************************************************************");
		logger.info("Link Siguiente: "+registroForm.getLinkSiguienteSolicitudes());
		//logger.info("Link Siguiente Asocio: "+registroForm.getLinkSiguiente().contains("Asocios"));
		logger.info("******************************************************************************");
		
		if(UtilidadTexto.isEmpty(registroForm.getLinkSiguienteSolicitudes()))//|| registroForm.getLinkSiguiente().contains("Asocios"))
		{		
			logger.info("entra ha esta parte esta bien");
			registroForm.setLinkSiguiente("");
			return mapping.findForward("busquedaAvanzadaSolicitudes");
		}
		else
		{
			logger.info("******************************************************************************");
			logger.info("Link Siguiente: "+registroForm.getLinkSiguienteSolicitudes());
			logger.info("Link Siguiente Asocio: "+registroForm.getLinkSiguienteSolicitudes().contains("Asocios"));
			logger.info("******************************************************************************");
			registroForm.setLinkSiguiente(registroForm.getLinkSiguienteSolicitudes().replaceAll("pager.offset=\\d+", "pager.offset=0"));
			logger.info(registroForm.getLinkSiguienteSolicitudes());
			try 
			{
				response.sendRedirect(registroForm.getLinkSiguienteSolicitudes());
			} 
			catch (IOException e) 
			{
				logger.info(""+e.toString());
			}
			//OJJJJJJJJJJJJJJJJJJOOOOOOOOOOOOOOOOOOOOOOOOOO
			//por favor no colocar este findForward porque se daña toda la parte de solicitudes
			//ademas no tiene sentido hacer un sendRedirect en la parte de arriba para finalmente enviarlo a un jsp especifico,
			//si depronto es requerido hacerlo deberia resetear la variable linksiguiente para que el workflow sea por el if de arriba
			//return mapping.findForward("busquedaAvanzadaSolicitudes");
			return null;
		}
	}

	/**
	 * Método usdo para volver el listado de facturas
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVolverListadoFacturas(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaFacturas");
	}

	/**
	 * Método implementado para manejar la seleccion/deseleccion del concepto de devolucion
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCambioConceptoDevolucion(Connection con,
			RegistroAuditoriaForm registroForm, ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		
		int posFactura = registroForm.getGlosa().getPosicionFactura();
		
		//Se toma la informacion del concepto Seleccionado
		for(HashMap<String, Object> elemento:registroForm.getConceptosDevolucion())
			if(registroForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).getCodigoConcepto().equals(elemento.get("codigo").toString()))
			{
				registroForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setDescripcion(elemento.get("nombre").toString());
				registroForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setTipo(elemento.get("tipoConcepto").toString());
				registroForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			}
		
		Glosas mundoGlosa = new Glosas();
		registroForm.setGlosa(mundoGlosa.procesarCambioConceptoDevolucion(con, registroForm.getGlosa(), "RegistroAuditoria"));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método para eliminar un concepto detalle
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConceptoDetalle(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping) 
	{
		int posFactura = registroForm.getGlosa().getPosicionFactura();
		int posDetalle = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		int posConcepto = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionConcepto();
		
		registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().get(posConcepto).setEliminado(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método para eliminar un registro del detalle de la factura de la glosa
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarDetalle(Connection con,
			RegistroAuditoriaForm registroForm, ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		int posFactura = registroForm.getGlosa().getPosicionFactura();
		int posDetalle = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		
		registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).setEliminado(true);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método implementado para guardar un concepto glosa de un detalle de factura
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarConceptoDetalle(Connection con,
			RegistroAuditoriaForm registroForm, ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		boolean existeConcepto = false;
		int posFactura = registroForm.getGlosa().getPosicionFactura();
		int posDetalle = registroForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		
		//Se limpia mensaje de error
		registroForm.getGlosa().getFacturas().get(posFactura).setMensajeError("");
		
		//***********VALIDACION DE LA SELECCION DEL CONCEPTO***************************
		for(DtoConceptoGlosa concepto:registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos())
			if(concepto.getCodigoConcepto().equals(registroForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto())&&!concepto.isEliminado())
				existeConcepto = true;
		//*****************************************************************************
		
		//Si el concepto no fue seleccionado se agrega
		if(!existeConcepto)
		{
			DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
			nuevoConcepto.setCodigoConcepto(registroForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto());
			nuevoConcepto.setDescripcion(registroForm.getGlosa().getConceptoSeleccionado().getDescripcion());
			nuevoConcepto.setTipo(registroForm.getGlosa().getConceptoSeleccionado().getTipo());
			nuevoConcepto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			
			registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().add(nuevoConcepto);
			//cambio temporal para arreglo de ajustes en la respuesta de glosa
			//registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().clear();
			//registroForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().add(nuevoConcepto);
		}
		else
			registroForm.getGlosa().getFacturas().get(posFactura).setMensajeError("El concepto "+registroForm.getGlosa().getConceptoSeleccionado().getDescripcion()+" ya fue seleccionado");
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionConceptoDetalleFactura");
	}

	/**
	 * Método para cargar el detalle de la factura
	 * @author Felipe Pérez Granda
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario 
	 * @param request 
	 * @return
	 */
	private ActionForward accionDetalleFactura(Connection con, RegistroAuditoriaForm registroForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		//*************VALIDACIONES INGRESO DETALLE*****************************************************************
		//Se verifica que se tenga parametrizado consecutivo disponible de glosas
		String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoGlosas, usuario.getCodigoInstitucionInt());
		if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
			errores.add("Falta consecutivo disponible",new ActionMessage("error.faltaDefinirConsecutivo","GLOSAS"));
		else if(Utilidades.convertirAEntero(consecutivo)==ConstantesBD.codigoNuncaValido)
			errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de glosas"));
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("busquedaFacturas");
		}
		//**********************************************************************************************************
		
		
		int posicion = registroForm.getPosicion();
		String codigoAuditoria = registroForm.getListadoFacturas("codigoAuditoria_"+posicion)+"";
		logger.info("===> La posición es: "+posicion);
		logger.info("===> El codigo de auditoría es: "+codigoAuditoria);
		
		Glosas mundoGlosa = new Glosas();
		DtoGlosa glosa = new DtoGlosa();
		glosa = mundoGlosa.cargarGlosaAuditoria(con, codigoAuditoria, "", usuario,false);
		
		
		registroForm.setGlosa(glosa);
		registroForm.setCodigoAuditoria(codigoAuditoria);
		
		
		//Se llena arreglo de conceptos de devolucion
		registroForm.setConceptosDevolucion(Utilidades.obtenerConceptosGlosa(con, ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion, usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturas");
	}

	/**
	 * Método para guardar los encabezados de glosa de las facturas seleccionadas
	 * @param con
	 * @param registroForm
	 * @param usuario 
	 * @param request 
	 * @param response 
	 * @return
	 */
	private ActionForward accionGuardarEncabezado(Connection con,RegistroAuditoriaForm registroForm, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) 
	{
		ActionErrors errores = new ActionErrors();
		
		//***************VALIDACIONES**************************************************
		//Se verifica que se haya seleccionado al menos una factura, de lo contrario mostrar mensaje de error
		boolean seleccionadas = false;
		for(int i=0;i<registroForm.getNumListadoFacturas();i++)
			//Se verifica que se haya seleccionado la factura y que no sea externa
			if(UtilidadTexto.getBoolean(registroForm.getListadoFacturas("seleccionado_"+i).toString())&&!UtilidadTexto.getBoolean(registroForm.getListadoFacturas("externa_"+i).toString()))
				seleccionadas = true;
		
		if(!seleccionadas)
		{
			registroForm.setEstado("");	
			errores.add("", new ActionMessage("errors.minimoCampos","la selección de una factura","auditoría"));
			saveErrors(request, errores);
			return mapping.findForward("busquedaFacturas");
		}
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			Glosas mundoGlosa = new Glosas();
			ResultadoBoolean resultado = mundoGlosa.auditarFacturasSeleccionadas(con, registroForm.getListadoFacturas(), usuario);
			
			if(resultado.isTrue())
			{

				registroForm.setEstadoAuditoria(true);
				UtilidadBD.finalizarTransaccion(con);
				
				//Se cargan de nuevo las facturas (pero solo las que fueron auditadas)
				registroForm.getCriterios().put("estadoAuditoria", true);
				registroForm.setListadoFacturas(mundoGlosa.consultarFacturas(con, registroForm.getCriterios()));
				registroForm.setAuditadas(true);
			}
			else
			{
				UtilidadBD.abortarTransaccion(con);
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				saveErrors(request, errores);
			}
		}	
		//*****************************************************************************
		
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar("",registroForm.getMaxPageItems(),registroForm.getNumListadoFacturas(),response, request, "busquedaFacturas.jsp", false);
		
	}

	/**
	 * Método implementado para realizar la ordenacion del listado de facturas
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarFacturas(Connection con,RegistroAuditoriaForm registroForm, ActionMapping mapping) 
	{
		String[] indices = Glosas.indicesListadoFacturas;
		int numFacturas = registroForm.getNumListadoFacturas();
		
		registroForm.setListadoFacturas(Listado.ordenarMapa(indices,
				registroForm.getIndice(),
				registroForm.getUltimoIndice(),
				registroForm.getListadoFacturas(),
				numFacturas));
		
		registroForm.setListadoFacturas("numRegistros",numFacturas+"");
		
		registroForm.setUltimoIndice(registroForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaFacturas");
	}

	/**
	 * Método para iniciar el flujo del registro de auditoria
	 * @author Sebastián Gómez
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,
			RegistroAuditoriaForm registroForm, ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		registroForm.reset();
		
		registroForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		/*
		 * Se cargan los arreglos
		 */
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAuditor(usuario.getCodigoInstitucionInt())))
			registroForm.setArregloConvenios(UtilidadesFacturacion.obtenerConvenioPorUsuario(con, usuario.getLoginUsuario(), 
				ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor, true));
		else
			registroForm.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		
		registroForm.setArregloViasIngreso(Utilidades.obtenerViasIngresoTipoPaciente(con));
		registroForm.setArregloEstadosPaciente(UtilidadesFacturacion.obtenerEstadosPacienteFactura(con));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaFacturas");
	}
	
	private ActionForward accionBuscarFacturas(Connection con, RegistroAuditoriaForm forma, Glosas mundo, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
	{
		String 
			facturaInicial = forma.getFacturaInicial(),
			facturaFinal = forma.getFacturaFinal(),
			codigoTipoPaciente = forma.getCodigoTipoPaciente(),
			fechaElaboracionInicial = forma.getFechaElaboracionInicial().trim(),
			fechaElaboracionFinal = forma.getFechaElaboracionFinal().trim();
		int 
			codigoConvenio = forma.getCodigoConvenio(),
			codigoViaIngreso = forma.getCodigoViaIngreso(),			
			estadoPaciente = forma.getEstadoPaciente();
		HashMap <String, Object>
			criterios = new HashMap<String, Object>(),
			listadoFacturas = new HashMap<String, Object>();
		boolean
			estadoAuditoria = forma.isEstadoAuditoria();
		
		logger.info("\n\n\n");
		logger.info("===> factura inicial = *"+facturaInicial+"*");
		logger.info("===> factura final = *"+facturaFinal+"*");
		logger.info("===> fecha elaboración inicial = *"+fechaElaboracionInicial+"*");
		logger.info("===> fecha elaboración final = *"+fechaElaboracionFinal+"*");
		logger.info("===> convenio = *"+codigoConvenio+"*");
		logger.info("===> via ingreso = *"+codigoViaIngreso+"*");
		logger.info("===> tiop paciente = *"+codigoTipoPaciente+"*");
		logger.info("===> estado paciente = *"+estadoPaciente+"*");
		logger.info("===> estado auditoria = *"+estadoAuditoria+"*");
		
		criterios.put("facturaInicial", facturaInicial);
		criterios.put("facturaFinal", facturaFinal);
		criterios.put("fechaElaboracionInicial", fechaElaboracionInicial);
		criterios.put("fechaElaboracionFinal", fechaElaboracionFinal);
		criterios.put("convenio", codigoConvenio);
		criterios.put("viaIngreso",forma.getVianIngresoTipoPaciente());
		criterios.put("tipoPaciente", codigoTipoPaciente);
		criterios.put("estadoPaciente", estadoPaciente);
		criterios.put("estadoAuditoria", estadoAuditoria);
		
		/*Modificacion tarea 87586 - Si el usuario es auditor, y se selecciona TODOS en Convenios,
		 * es necesario realizar la búsqueda de las facturas con respecto a todos los convenios que 
		 * tiene asociados el usuario. Si el usuario no es auditor, la búsqueda se realiza normalmente 
		*/	
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAuditor(usuario.getCodigoInstitucionInt()))
		&&((criterios.get("convenio").equals(ConstantesBD.codigoNuncaValido))))
		{
			if (forma.getArregloConvenios().size()>0)
			{
				for(int i=0;i<forma.getArregloConvenios().size();i++)
					criterios.put("convenio_"+i, forma.getArregloConvenios().get(i).get("codigoConvenio"));
				
				criterios.put("numConvenios", forma.getArregloConvenios().size());
				listadoFacturas = mundo.consultarFacturas(con, criterios);
			}
		}
		else
		{
			listadoFacturas = mundo.consultarFacturas(con, criterios);
		}
		//Fin Modificacion tarea 87586
		
		forma.setCriterios(criterios);
		forma.setListadoFacturas(listadoFacturas);
		forma.setRealizoBusqueda(true);
		forma.setAuditadas(forma.isEstadoAuditoria());
		
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar("", forma.getMaxPageItems(), forma.getNumListadoFacturas(), response, request, "busquedaFacturas.jsp", false);
	}
	
	/**
	 * Método encargado de validar la búsqueda de las facturas
	 * @author Felipe Pérez Granda 
	 * @param con
	 * @param forma
	 * @param errores
	 * @return ActionErrors
	 */
	private ActionErrors validarBusquedaFacturas(Connection con, RegistroAuditoriaForm forma , ActionErrors errores)
	{
		/*
		 * Validaciones al momento buscar una factura
		 */
		if(forma.getEstado().equals("buscar"))
		{
			String 
				facturaInicial = forma.getFacturaInicial(),
				facturaFinal = forma.getFacturaFinal(),
				fechaElaboracionInicial = forma.getFechaElaboracionInicial().trim(),
				viaIngTipoPac = forma.getVianIngresoTipoPaciente(),
				fechaElaboracionFinal = forma.getFechaElaboracionFinal().trim();
			int 
				codigoConvenio = forma.getCodigoConvenio(),
				codigoViaIngreso = forma.getCodigoViaIngreso(),
				estadoPaciente = forma.getEstadoPaciente();
			
			boolean 
				fecha=true,
				fechaCorrecta=true;
			
			logger.info("===> factura inicial = *"+facturaInicial+"*");
			logger.info("===> factura final = *"+facturaFinal+"*");
			logger.info("===> fecha elaboración inicial = *"+fechaElaboracionInicial+"*");
			logger.info("===> fecha elaboración final = *"+fechaElaboracionFinal+"*");
			logger.info("===> convenio = *"+codigoConvenio+"*");
			logger.info("===> via ingreso tipo paciente = *"+viaIngTipoPac+"*");
			logger.info("===> estado paciente = *"+estadoPaciente+"*");
			
			/*
			 * Si TODOS los campos vienen vacíos, es necesario seleccionar por lo menos un parámetro de búsqueda
			 */
			if(facturaInicial.equals("") &&
				facturaFinal.equals("") &&
				fechaElaboracionInicial.equals("") &&
				fechaElaboracionFinal.equals("") &&
				codigoConvenio == ConstantesBD.codigoNuncaValido &&
				viaIngTipoPac == "" &&
				estadoPaciente == ConstantesBD.codigoNuncaValido)
			{
				errores.add("Búsqueda Factura", new ActionMessage("error.errorEnBlanco", 
						"Es Necesario Seleccionar por lo Menos Un Parámetro de Búsqueda. "));
			}
			else
			{
				/* **********************************************
				 * INICIO Validaciones de los números de facturas
				 * *********************************************/
				
				/*
				 * Si se digita información en factura inicial, se debe de validar que factura final esté lleno
				 */
				if(!facturaInicial.equals("") && facturaFinal.equals(""))
				{
					errores.add("Factura Final", new ActionMessage("errors.required", "Factura Final "));
				}
				
				/*
				 * Se debe validar que el numero de factura inicial sea mayor a 0
				 */
				if(Utilidades.convertirAEntero(facturaInicial.toString())<=0 && !facturaInicial.equals(""))
				{
					errores.add("Factura Inicial", new ActionMessage("error.errorEnBlanco", "La Factura Inicial Debe ser Mayor a 0. "));
				}
				
				/*
				 * Si se digita información en la factura final, se debe validar que factura inicial esté lleno
				 */
				if(!facturaFinal.equals("") && facturaInicial.equals(""))
				{
					errores.add("Factura Incial", new ActionMessage("errors.required", "Factura Inicial "));
				}
				
				/*
				 * Se debe validar que el número ingresado en factura final sea mayor o igual al número ingresado en la factura inicial
				 */
				if(!facturaInicial.equals("") && !facturaFinal.equals("") &&
					(Utilidades.convertirAEntero(facturaFinal.toString()) < Utilidades.convertirAEntero(facturaInicial.toString())))
				{
					errores.add("Factura Final", new ActionMessage("error.errorEnBlanco", 
							"La Factura Final Debe ser Mayor o Igual a la Factura Inicial."));
				}
				
				/* *******************************************
				 * FIN Validaciones de los números de facturas
				 * ******************************************/
				
				/* ************************************************
				 * INICIO Validaciones de las fechas de elaboración
				 * ***********************************************/
				
				/*
				 * Se debe de digitar las fechas de elaboracion en un formato dd/mm/aaaa
				 */
				if(!fechaElaboracionInicial.equals(""))
				{
					fechaCorrecta = UtilidadFecha.validarFecha(fechaElaboracionInicial);
					if(!fechaCorrecta)
					{
						errores.add("Fecha Elaboración Inicial", new ActionMessage("error.errorEnBlanco", 
							"La Fecha de Elaboración Inicial "+fechaElaboracionInicial+" Debe estar en formato dd/mm/aaaa" ));
					}
					
				}
				
				if(!fechaElaboracionFinal.equals(""))
				{
					fechaCorrecta = UtilidadFecha.validarFecha(fechaElaboracionFinal);
					if(!fechaCorrecta)
					{
						errores.add("Fecha Elaboración Final", new ActionMessage("error.errorEnBlanco", 
							"La Fecha de Elaboración Final "+fechaElaboracionFinal+" Debe estar en formato dd/mm/aaaa" ));
					}
					
				}
				
				logger.info("===> fechaCorrecta = "+fechaCorrecta);
				/*
				 * fechaCorrecta me indica si las fechas digitadas están en un formato dd/mm/aaaa
				 */
				if(fechaCorrecta)
				{
					/*
					 * Si se digita información en la fecha de elaboración final, la fecha de elaboración inicial es requerido
					 */
					if(!fechaElaboracionFinal.equals("") && fechaElaboracionInicial.equals(""))
					{
						fecha=false;
						errores.add("Fecha Elaboracion Inicial",new ActionMessage("errors.required","La Fecha de Elaboración Inicial"));
					}
					
					/*
					 * Si se digita información en la fecha de elaboración inicial, la fecha de elaboración final es requerido
					 */
					if(!fechaElaboracionInicial.equals("") && fechaElaboracionFinal.equals(""))
					{
						fecha=false;
						errores.add("Fecha Elaboracion Final",new ActionMessage("errors.required","La Fecha de Elaboración Final"));
					}
					
					/* 
					 * La fecha de elaboración inicial debe ser menor o igual a la fecha actual del sistema
					 */
					if(!fechaElaboracionInicial.equals("") &&
						!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaElaboracionInicial,UtilidadFecha.getFechaActual()))
					{
						fecha=false;
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
								fechaElaboracionInicial, "Actual "+UtilidadFecha.getFechaActual()));
					}
					
					/* 
					 * La fecha de elaboración final debe ser menor o igual a la fecha actual del sistema
					 */
					if(!fechaElaboracionFinal.equals("") &&
						!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaElaboracionFinal,UtilidadFecha.getFechaActual()))
					{
						fecha=false;
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+
								fechaElaboracionFinal, "Actual "+UtilidadFecha.getFechaActual()));
					}
					
					/*
					 * La fecha de elaboración final debe ser mayor a la fecha de elaboracion incial
					 */
					if(!fechaElaboracionInicial.equals("") && !fechaElaboracionFinal.equals(""))
					{
						if (UtilidadFecha.numeroMesesEntreFechasExacta(fechaElaboracionInicial, 
								forma.getFechaElaboracionFinal()) == -1)
						{
							fecha=false;
							errores.add("Fechas de Elaboración", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
									fechaElaboracionInicial, "Final "+fechaElaboracionFinal));
						}
					}
					
					/*
					 * El máximo rango entre fecha debe de ser de 3 meses
					 */
					if (fecha)
					{
						if(!fechaElaboracionInicial.equals("") && !fechaElaboracionFinal.equals(""))
						{
							logger.info("===> El rango de fechas en días es: "+UtilidadFecha.numeroDiasEntreFechas(fechaElaboracionInicial, 
								fechaElaboracionFinal));
							logger.info("===> El rango de fechas en meses es: "+
								UtilidadFecha.numeroMesesEntreFechasExacta(fechaElaboracionInicial, fechaElaboracionFinal));
							if(UtilidadFecha.numeroDiasEntreFechas(fechaElaboracionInicial, fechaElaboracionFinal)>92)
							{
								
								/*
								 * En donde el numero final al cual se compara, es el numero de meses del rango.
								 */
								errores.add("Fechas de Elaboración", new ActionMessage("errors.debeSerNumeroMenorIgual",
										"El rango de dias entre fechas de elaboración", "3 Meses"));
							}	
						}
					}
				}
				
				/* *********************************************
				 * FIN Validaciones de las fechas de elaboración
				 * ********************************************/
			}	
		}	
		return errores;
	}
}