package com.princetonsa.action.glosas;

import java.io.IOException;
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
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.glosas.RegistrarModificarGlosasForm;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.dto.glosas.DtoObsFacturaGlosas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.AprobarGlosas;
import com.princetonsa.mundo.glosas.ConfirmarAnularGlosas;
import com.princetonsa.mundo.glosas.Glosas;
import com.princetonsa.mundo.glosas.RegistrarModificarGlosas;

/**
 * 
 * @author Angela María Angel amangel@princetonsa.com
 * 
 */

public class RegistrarModificarGlosasAction extends Action {
	private Logger logger = Logger
			.getLogger(RegistrarModificarGlosasAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		Connection con = null;
		try{
			if (form instanceof RegistrarModificarGlosasForm) {

				con = UtilidadBD.abrirConexion();

				// se verifica si la conexion esta nula
				if (con == null) {
					// de ser asi se envia a una pagina de error.
					request.setAttribute("CodigoDescripcionError",
					"errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				// se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico) request.getSession()
				.getAttribute("usuarioBasico");

				// se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica) request.getSession()
				.getAttribute("pacienteActivo");

				// se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica) request
				.getSession().getAttribute("institucionBasica");

				// se instancia la forma
				RegistrarModificarGlosasForm forma = (RegistrarModificarGlosasForm) form;

				// se instancia el mundo
				RegistrarModificarGlosas mundo = new RegistrarModificarGlosas();

				// optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				// se instancia la variable para manejar los errores.
				ActionErrors errores = new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				// se instancia la variable para manejar los errores.

				logger
				.info("\n\n***************************************************************************");
				logger
				.info(" 	  EL ESTADO DE REGISTRAR MODIFICAR GLOSAS ES ====>> "
						+ estado);
				logger
				.info("\n***************************************************************************");

				// ESTADO --> NULL
				if (estado == null) {
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError",
					"Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				} else if (estado.equals("empezar")) {
					return this.accionEmpezar(forma, mundo, con, mapping, usuario,
							request);
				} else if (estado.equals("buscar")) {
					return this.accionBuscar(con, forma, mundo, mapping, usuario,
							request, response);
				} else if (estado.equals("mostrarGlosa")) {
					return this.accionMostrarGlosa(forma, mundo, con, mapping,
							usuario);
				} else if (estado.equals("filtroContratos")) {
					return accionFiltrarContratos(con, forma, response);
				} else if (estado.equals("guardar")) {
					return this.accionGuardar(forma, mundo, con, mapping, usuario,
							request);
				} else if (estado.equals("buscarFact")) {
					return this.accionBuscarFact(forma, mundo, con, mapping,
							usuario, request);
				} else if (estado.equals("historicoGlosa")) {
					return this.accionHistoricoGlosa(forma, mundo, con, mapping,
							usuario);
				} else if (estado.equals("guardarFacturas")) {
					return this.accionGuardarFacturas(forma, mundo, con, mapping,
							usuario, request);
				} else if (estado.equals("eliminar")) {
					return this.accionEliminar(forma, mundo, con, mapping, usuario);
				} else if (estado.equals("mostrarConceptosPorFactura")) {
					return this.accionMostrarConceptos(forma, mundo, con, mapping,
							usuario);
				} else if (estado.equals("subirConcepto")) {
					return this.accionSubirConcepto(forma, mundo, con, mapping,
							usuario);
				} else if (estado.equals("cerrarPopUpConceptos")) {
					return this.accionCerrarPopUpConcepto(forma, mundo, con,
							mapping, usuario);
				}
				// ############ NUEVO INGRESAR DETALLE GLOSA POR FACTURA
				// ###########//

				else if (estado.equals("detalleFactura")) {
					return accionDetalleFactura(con, forma, mapping, usuario,
							request);
				} else if (estado.equals("detalleFacturaExt")) {
					return accionDetalleFacturaExt(con, forma, mapping, usuario,
							request);
				} else if (estado.equals("recargarFrameDetalle")) {
					UtilidadBD.closeConnection(con);
					return mapping.findForward("frameDetalleSolicitud");
				} else if (estado.equals("iniciarBusquedaSolicitudes")
						|| estado.equals("realizarBusquedaSolicitudes")) {
					return accionBusquedaSolicitudes(con, forma, mapping, usuario,
							response);
				} else if (estado.equals("seleccionarBusquedaSolicitudes")) {
					return accionSeleccionarBusquedaSolicitudes(con, forma,
							mapping, usuario);
				} else if (estado.equals("volverListadoFacturas")) {
					return accionVolverListadoFacturas(con, forma, mapping);
				} else if (estado.equals("eliminarDetalle")) // estado para eliminar
					// un detalle de la
					// factura
				{
					return accionEliminarDetalle(con, forma, mapping, usuario);
				} else if (estado.equals("guardarFrameDetalle")) {
					UtilidadBD.closeConnection(con);
					return mapping.findForward("frameDetalleSolicitud");
				} else if (estado.equals("guardarConceptoDetalle")) // Estado para
					// almacenar el
					// concepto de
					// un detalle
					// factua
					// (LLAMADO
					// DESDE POPUP)
				{
					return accionGuardarConceptoDetalle(con, forma, mapping,
							usuario);
				} else if (estado.equals("guardarDetalle")) {
					return accionGuardarDetalle(con, forma, mapping, usuario,
							request);
				} else if (estado.equals("guardarConceptoDetalleAsocio")) // Estado
					// para
					// almacenar
					// el
					// concepto
					// de un
					// detalle
					// de
					// asocio
					// (LLAMADO
					// DESDE
					// POPUP)
				{
					return accionGuardarConceptoDetalleAsocio(con, forma, mapping,
							usuario);
				} else if (estado.equals("recargarDetalleAsocios")) {
					UtilidadBD.closeConnection(con);
					return mapping.findForward("popUpSeleccionAsociosFactura");
				} else if (estado.equals("guardarAsocios")) {
					return accionGuardarAsocios(con, forma, mapping, usuario,
							request);
				} else if (estado.equals("eliminarConceptoDetalle")) // estado para
					// eliminaar
					// un
					// concepto
					// del
					// detalle
				{
					return accionEliminarConceptoDetalle(con, forma, mapping);
				} else if (estado.equals("eliminarConceptoDetalleAsocio")) // estado
					// para
					// eliminar
					// un
					// concepto
					// de un
					// asocio
					// de un
					// detalle
				{
					return accionEliminarConceptoDetalleAsocio(con, forma, mapping);
				} else if (estado.equals("cambioConceptoDevolucion")) {
					return accionCambioConceptoDevolucion(con, forma, mapping,
							usuario);
				} else if (estado.equals("redireccion")) {
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				} else if (estado.equals("ordenar")) {

				}
				else if(estado.equals("ordenarSolicitudesFactura"))
				{
					return this.accionOrdenarSolicitudesFactura(forma, con, mapping, response);
				}
				else if(estado.equals("observacionAuditoria"))
				{
					return this.accionObservacionAuditoria(forma, con, mapping, response);
				}
				else if(estado.equals("insertarObservacionAuditora"))
				{
					return this.accionInsertarObservacionAuditora(forma, con, mapping, response,usuario);
				}
				else if(estado.equals("aprobarGlosa"))
				{
					return this.accionAprobarGlosa(forma, con, mapping, response,request,usuario);
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
	 * @param con
	 * @param mapping
	 * @param response
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAprobarGlosa(
			RegistrarModificarGlosasForm forma, Connection con,
			ActionMapping mapping, HttpServletResponse response,
			HttpServletRequest request, UsuarioBasico usuario) 
	{
		//se instancia el mundo de aprobacion
		AprobarGlosas mundo = new AprobarGlosas();
		ActionErrors errores=new ActionErrors();
		errores=validarGlosa(con,forma,mundo);
		
		if (!errores.isEmpty())
		{
			saveErrors(request,errores);	
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleGlosa");
		}
		return this.guardarAprobarGlosa(forma, mundo, con, mapping, usuario);		
		
		
	}

	private ActionForward guardarAprobarGlosa(
			RegistrarModificarGlosasForm forma, AprobarGlosas mundo,
			Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		if(mundo.guardar(con,forma.getInformacionGlosa("glosasis")+"", usuario.getLoginUsuario()))
			forma.setMensaje(new ResultadoBoolean(true,"Glosa Aprobada Exitosamente.	"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"La glosa no se actualizó satisfactoriamente."));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosa");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionErrors validarGlosa(Connection con,
			RegistrarModificarGlosasForm forma, AprobarGlosas mundo) 
	{
		ActionErrors errors = new ActionErrors();
		boolean mensaje=false;
		
		if(!RegistrarModificarGlosas.consultarEstadoGlosa(con, forma.getInformacionGlosa("codglosa")+"").equals(ConstantesIntegridadDominio.acronimoEstadoGlosaRegistrada)&&!RegistrarModificarGlosas.consultarEstadoGlosa(con, forma.getInformacionGlosa("codglosa")+"").equals(ConstantesIntegridadDominio.acronimoEstadoGlosaConfirmada))
			errors.add("descripcion",new ActionMessage("prompt.generico","Para aprobar la glosa esta debe estar en estado 'Radicada o Confirmada. Por favor Verifique."));
		mensaje=ConfirmarAnularGlosas.validarAnuConfGlosa(con, forma.getInformacionGlosa("codglosa")+"",forma.getValor()+"");
		if(mensaje==false)
			errors.add("descripcion",new ActionMessage("prompt.generico","El Total de la Glosa esta descuadrado respecto al detalle de solicitudes. Por favor Verifique."));

		mensaje=mundo.validarAprobarGlosa(con, forma.getInformacionGlosa("codglosa")+"",forma.getValor()+"");
		if(mensaje==false)
			errors.add("descripcion",new ActionMessage("prompt.generico","El Total de la Glosa esta descuadrado respecto al detalle de facturas. Por favor Verifique."));
		return errors;
	}

	private ActionForward accionInsertarObservacionAuditora(
			RegistrarModificarGlosasForm forma, Connection con,
			ActionMapping mapping, HttpServletResponse response, UsuarioBasico usuario) {
		
		if(!forma.getObservacionesDetalle().isEmpty())
		{
			DtoObsFacturaGlosas dto=new DtoObsFacturaGlosas();
			dto.setCodigoFactura(Utilidades.convertirAEntero(forma.getCodFactura()));
			dto.setFechaObservacion(UtilidadFecha.getFechaActual(con));
			dto.setHoraObservacion(UtilidadFecha.getHoraActual(con));
			dto.setObservacion(forma.getObservacionesDetalle());
			dto.setUsuarioObservacion(usuario.getLoginUsuario());
			RegistrarModificarGlosas.insertarObservacion(con, dto);
		}
		return this.accionObservacionAuditoria(forma, con, mapping, response);
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param response
	 * @return
	 */
	private ActionForward accionObservacionAuditoria(
			RegistrarModificarGlosasForm forma, Connection con,
			ActionMapping mapping, HttpServletResponse response) 
	{
		forma.setObservacionesDetalle("");
		forma.setObservacionesFacturaGlosas(RegistrarModificarGlosas.consultarObservacionesAuditoriasGlosas(con,Utilidades.convertirAEntero(forma.getCodFactura())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("observacionesFactura");
	}

	/**
     * 
     * @param forma
     */    
    private ActionForward accionOrdenarSolicitudesFactura(RegistrarModificarGlosasForm forma, Connection con, ActionMapping mapping, HttpServletResponse response) 
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
	
	// #################################### NUEVO INGRESAR DETALLE GLOSA POR
	// FACTURA ##########################################//

	private ActionForward accionDetalleFacturaExt(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request) {
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturaExterna");
	}

	/**
	 * Método implementado para manejar la seleccion/deseleccion del concepto de
	 * devolucion
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCambioConceptoDevolucion(Connection con,RegistrarModificarGlosasForm forma, ActionMapping mapping,UsuarioBasico usuario) {

		int posFactura = forma.getGlosa().getPosicionFactura();

		// Se toma la informacion del concepto Seleccionado
		for (HashMap<String, Object> elemento : forma.getConceptosDevolucion())
			if (forma.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).getCodigoConcepto().equals(elemento.get("codigo").toString())) {
				forma.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setDescripcion(elemento.get("nombre").toString());
				forma.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setTipo(elemento.get("tipoConcepto").toString());
				// forma.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setCodigo(elemento.get("codigo").toString());
				forma.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			}

		
		logger.info("\n\ndescripcion concepto:: "+forma.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).getDescripcion());
		logger.info("\n\ntipo:: "+forma.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).getTipo());
		
		Glosas mundoGlosa = new Glosas();
		forma.setGlosa(mundoGlosa.procesarCambioConceptoDevolucion(con, forma.getGlosa(), "RegistrarModificarGlosas"));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método para eliminar un concepto de detalle de asocio
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConceptoDetalleAsocio(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping) {
		int posFactura = forma.getGlosa().getPosicionFactura();
		int posDetalle = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionDetalle();
		int posAsocio = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionAsocio();
		int posConcepto = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionConcepto();

		forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(
				posDetalle).getAsocios().get(posAsocio).getConceptos().get(
				posConcepto).setEliminado(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionAsociosFactura");
	}

	/**
	 * Método para eliminar un concepto detalle
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConceptoDetalle(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping) {
		int posFactura = forma.getGlosa().getPosicionFactura();
		int posDetalle = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionDetalle();
		int posConcepto = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionConcepto();

		forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(
				posDetalle).getConceptos().get(posConcepto).setEliminado(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método para guardar los asocios de un servicio de cirugia
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarAsocios(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();

		int posicionFactura = forma.getGlosa().getPosicionFactura();
		int posicionDetalle = forma.getGlosa().getFacturas().get(
				posicionFactura).getPosicionDetalle();

		// **************VALIDACIONES DATOS DE LOS
		// ASOCIOS*********************************************************
		Glosas mundoGlosa = new Glosas();
		mundoGlosa.setGlosa(forma.getGlosa());
		mundoGlosa.validacionCamposAsocio(posicionFactura, posicionDetalle,
				true, false, "RegistrarModificarGlosa");
		errores = mundoGlosa.getErrores();
		// **********************************************************************************************************

		if (!errores.isEmpty()) {
			saveErrors(request, errores);
			forma.setEstado("");
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionAsociosFactura");
	}

	/**
	 * Método para guardar un concepto de detalle de asocio
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarConceptoDetalleAsocio(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario) {
		boolean existeConcepto = false;
		int posFactura = forma.getGlosa().getPosicionFactura();
		int posDetalle = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionDetalle();
		int posAsocio = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionAsocio();

		// Se limpia mensaje de error
		forma.getGlosa().getFacturas().get(posFactura).setMensajeError("");

		// ***********VALIDACION DE LA SELECCION DEL
		// CONCEPTO***************************
		for (DtoConceptoGlosa concepto : forma.getGlosa().getFacturas().get(
				posFactura).getDetalle().get(posDetalle).getAsocios().get(
				posAsocio).getConceptos())
			if (concepto.getCodigoConcepto().equals(
					forma.getGlosa().getConceptoSeleccionado()
							.getCodigoConcepto())
					&& !concepto.isEliminado())
				existeConcepto = true;
		// *****************************************************************************

		// Si el concepto no fue seleccionado se agrega
		if (!existeConcepto) {
			DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
			nuevoConcepto.setCodigoConcepto(forma.getGlosa()
					.getConceptoSeleccionado().getCodigoConcepto());
			nuevoConcepto.setDescripcion(forma.getGlosa()
					.getConceptoSeleccionado().getDescripcion());
			nuevoConcepto.setTipo(forma.getGlosa().getConceptoSeleccionado()
					.getTipo());
			nuevoConcepto.setCodigoInstitucion(usuario
					.getCodigoInstitucionInt());

			forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(
					posDetalle).getAsocios().get(posAsocio).getConceptos().add(
					nuevoConcepto);
		} else
			forma.getGlosa().getFacturas().get(posFactura).setMensajeError(
					"El concepto "
							+ forma.getGlosa().getConceptoSeleccionado()
									.getDescripcion() + " ya fue seleccionado");

		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionConceptoDetalleAsocio");
	}

	/**
	 * Método para hacer un guardar total de la glosa de la factura
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDetalle(Connection con,RegistrarModificarGlosasForm forma, ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request) {

		UtilidadBD.iniciarTransaccion(con);
		// Para verificar si la glosa es nueva o es una modificacion
		boolean esNueva = forma.getGlosa().getGlosaSistema().equals("") ? true
				: false;

		Glosas mundoGlosa = new Glosas();
		mundoGlosa.setGlosa(forma.getGlosa());
		mundoGlosa.guardarGlosa(con, usuario, "RegistrarModificarGlosa");

		if (mundoGlosa.getErrores().isEmpty()) {
			UtilidadBD.finalizarTransaccion(con);
			forma.setGlosa(mundoGlosa.cargarGlosaAuditoria(con, forma
					.getCodigoAuditoria(), "", usuario, false));
			
			
			
			//******************** Cambiado de sitio x tarea 129176

			String posicion = forma.getPosicion();
			forma.setListadoFacturas("codigoFatura_" + posicion, forma
					.getMapaDetalleGlosa("codfactura_" + posicion)
					+ "");
			forma.setListadoFacturas("consecutivoFactura_" + posicion, forma
					.getMapaDetalleGlosa("factura_" + posicion)
					+ "");
			forma.setListadoFacturas("nombreConvenio_" + posicion, forma
					.getConvenioP());
			forma.setListadoFacturas("codigoAuditoria_" + posicion, forma
					.getMapaDetalleGlosa("codglosa_" + posicion)
					+ "");
			forma.setListadoFacturas("numRegistros", "1");

			String codigoAuditoria = forma.getListadoFacturas("codigoAuditoria_"
					+ posicion)
					+ "";
			String codigoFactura = forma.getMapaDetalleGlosa("codigoaudi_"
					+ posicion)
					+ "";
			logger.info("===> Listado facturas: " + forma.getListadoFacturas());
			logger.info("===> La posición es: " + posicion);
			logger.info("===> El codigo de glosa es: " + codigoAuditoria);
			logger.info("===> El codigo de auditoría es: " + codigoFactura);
			
			mundoGlosa = new Glosas();
			DtoGlosa glosa = new DtoGlosa();
			glosa = mundoGlosa.cargarGlosaAuditoria(con, codigoAuditoria,
					codigoFactura, usuario, false);

			forma.setGlosa(glosa);
			forma.setCodigoAuditoria(codigoAuditoria);

			// Se llena arreglo de conceptos de devolucion
			forma.setConceptosDevolucion(Utilidades.obtenerConceptosGlosa(con,
					ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion,
					usuario.getCodigoInstitucionInt()));

			// ********************
			
			
			
			
		} else {
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, mundoGlosa.getErrores());
			forma.setEstado("");
			// Si la glosa es nueva, se finaliza el uso del consecutivo
			if (esNueva && !mundoGlosa.getGlosa().getGlosaSistema().equals(""))
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(
						con, ConstantesBD.nombreConsecutivoGlosas, usuario
										.getCodigoInstitucionInt(),
						mundoGlosa
						.getGlosa().getGlosaSistema(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturas");
	}

	/**
	 * Método implementado para guardar un concepto glosa de un detalle de
	 * factura
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarConceptoDetalle(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario) {
		boolean existeConcepto = false;
		int posFactura = forma.getGlosa().getPosicionFactura();
		int posDetalle = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionDetalle();

		// Se limpia mensaje de error
		forma.getGlosa().getFacturas().get(posFactura).setMensajeError("");

		// ***********VALIDACION DE LA SELECCION DEL
		// CONCEPTO***************************
		for (DtoConceptoGlosa concepto : forma.getGlosa().getFacturas().get(
				posFactura).getDetalle().get(posDetalle).getConceptos())
			if (concepto.getCodigoConcepto().equals(
					forma.getGlosa().getConceptoSeleccionado()
							.getCodigoConcepto())
					&& !concepto.isEliminado())
				existeConcepto = true;
		// *****************************************************************************

		// Si el concepto no fue seleccionado se agrega
		if (!existeConcepto) {
			DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
			nuevoConcepto.setCodigoConcepto(forma.getGlosa()
					.getConceptoSeleccionado().getCodigoConcepto());
			nuevoConcepto.setCodigo(forma.getGlosa().getConceptoSeleccionado()
					.getCodigo());
			nuevoConcepto.setDescripcion(forma.getGlosa()
					.getConceptoSeleccionado().getDescripcion());
			nuevoConcepto.setTipo(forma.getGlosa().getConceptoSeleccionado()
					.getTipo());
			nuevoConcepto.setCodigoInstitucion(usuario
					.getCodigoInstitucionInt());

			forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().add(nuevoConcepto);
			//cambio temporal para arreglo de ajustes en la respuesta de glosa
			//forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().clear();
			//forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().add(nuevoConcepto);
		} else
			forma.getGlosa().getFacturas().get(posFactura).setMensajeError(
					"El concepto "
							+ forma.getGlosa().getConceptoSeleccionado()
									.getDescripcion() + " ya fue seleccionado");

		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionConceptoDetalleFactura");
	}

	/**
	 * Método para cargar el detalle de la factura
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionDetalleFactura(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();

		// *************VALIDACIONES INGRESO
		// DETALLE*****************************************************************
		// Se verifica que se tenga parametrizado consecutivo disponible de
		// glosas
		String consecutivo = UtilidadBD.obtenerValorActualTablaConsecutivos(
				con, ConstantesBD.nombreConsecutivoGlosas, usuario
						.getCodigoInstitucionInt());
		if (!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
			errores.add("Falta consecutivo disponible", new ActionMessage(
					"error.faltaDefinirConsecutivo", "GLOSAS"));
		else if (Utilidades.convertirAEntero(consecutivo) == ConstantesBD.codigoNuncaValido)
			errores.add("Consecutivo no es entero", new ActionMessage(
					"errors.integer", "el consecutivo de glosas"));

		if (!errores.isEmpty()) {
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("busquedaFacturas");
		}
		// **********************************************************************************************************

		String posicion = forma.getPosicion();
		forma.setListadoFacturas("codigoFatura_" + posicion, forma
				.getMapaDetalleGlosa("codfactura_" + posicion)
				+ "");
		forma.setListadoFacturas("consecutivoFactura_" + posicion, forma
				.getMapaDetalleGlosa("factura_" + posicion)
				+ "");
		forma.setListadoFacturas("nombreConvenio_" + posicion, forma
				.getConvenioP());
		forma.setListadoFacturas("codigoAuditoria_" + posicion, forma
				.getMapaDetalleGlosa("codglosa_" + posicion)
				+ "");
		forma.setListadoFacturas("numRegistros", "1");

		String codigoAuditoria = forma.getListadoFacturas("codigoAuditoria_"
				+ posicion)
				+ "";
		String codigoFactura = forma.getMapaDetalleGlosa("codigoaudi_"
				+ posicion)
				+ "";
		logger.info("===> Listado facturas: " + forma.getListadoFacturas());
		logger.info("===> La posición es: " + posicion);
		logger.info("===> El codigo de glosa es: " + codigoAuditoria);
		logger.info("===> El codigo de auditoría es: " + codigoFactura);

		Glosas mundoGlosa = new Glosas();
		DtoGlosa glosa = new DtoGlosa();
		glosa = mundoGlosa.cargarGlosaAuditoria(con, codigoAuditoria,
				codigoFactura, usuario, false);

		forma.setGlosa(glosa);
		forma.setCodigoAuditoria(codigoAuditoria);

		// Se llena arreglo de conceptos de devolucion
		forma.setConceptosDevolucion(Utilidades.obtenerConceptosGlosa(con,
				ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion,
				usuario.getCodigoInstitucionInt()));
		return mapping.findForward("detalleFacturas");
	}

	/**
	 * Método para abrir la busqueda avanzada de solicitudes
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaSolicitudes(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletResponse response) {

		int posicionFactura = forma.getGlosa().getPosicionFactura();
		int codigoTarifarioOficial = Utilidades.convertirAEntero(
				ValoresPorDefecto
						.getCodigoManualEstandarBusquedaServicios(usuario
								.getCodigoInstitucionInt()), true);

		if (forma.getEstado().equals("iniciarBusquedaSolicitudes")) {
			// Si la busqueda apenas empieza se inicializan los campos de
			// busqueda
			forma.getGlosa().setNumeroSolicitudBusqueda("");
			forma.getGlosa().setCodigoServicioBusqueda("");
			forma.getGlosa().setDescripcionServicioBusqueda("");
			forma.getGlosa().setCodigoArticuloBusqueda("");
			forma.getGlosa().setDescripcionArticuloBusqueda("");
			forma.getGlosa().setSeleccionTodosBusqueda(ConstantesBD.acronimoNo);
		}

		String codigoEstandarBusquedaArticulo = ValoresPorDefecto
				.getCodigoManualEstandarBusquedaArticulos(usuario
						.getCodigoInstitucionInt());

		Glosas mundoGlosa = new Glosas();
		forma.getGlosa().setBusquedaSolicitudes(
				mundoGlosa.cargarSolicitudesFactura(con, Integer.parseInt(forma
						.getGlosa().getFacturas().get(posicionFactura)
						.getCodigoFactura()), // codigo de la factura
						forma.getGlosa().getFacturas().get(posicionFactura)
								.obtenerCodigosDetalleFactura(), // codigos de
						// los
						// detalles
						// de
						// factura
						// ingresados
						codigoTarifarioOficial, // codigo tarifario oficial
						Utilidades.convertirAEntero(forma.getGlosa()
								.getNumeroSolicitudBusqueda()), // numero de la
						// solicitud
						forma.getGlosa().getCodigoServicioBusqueda(), // codigo
						// servicio
						forma.getGlosa().getDescripcionServicioBusqueda(), // descripcion
						// servicio
						Utilidades.convertirAEntero(forma.getGlosa()
								.getCodigoArticuloBusqueda()), // codigo
						// articulo
						forma.getGlosa().getDescripcionArticuloBusqueda(), // descripcoin
						// articulo
						codigoEstandarBusquedaArticulo)// Codigo Estandar
				// Busqueda por Articulo
				);
		forma.getGlosa().setTarifarioOficialBusqueda(
				UtilidadesFacturacion.obtenerNombreTarifarioOficial(con,
						codigoTarifarioOficial));
		UtilidadBD.closeConnection(con);

		if (UtilidadTexto.isEmpty(forma.getLinkSiguiente())
				|| forma.getLinkSiguiente().contains("Asocios")) {
			forma.setLinkSiguiente("");
			return mapping.findForward("busquedaAvanzadaSolicitudes");
		} else {
			forma.setLinkSiguiente(forma.getLinkSiguiente().replaceAll(
					"pager.offset=\\d+", "pager.offset=0"));
			logger.info(forma.getLinkSiguiente());
			try {
				response.sendRedirect(forma.getLinkSiguiente());
			} catch (IOException e) {
				logger.info("" + e.toString());
			}
			return null;
		}
	}

	/**
	 * Método implementado para seleccionar los detalles de la factura y
	 * agregarlos a la estructura de la glosa de la factura
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSeleccionarBusquedaSolicitudes(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario) {
		Glosas mundoGlosa = new Glosas();
		forma.setGlosa(mundoGlosa.agregarNuevosDetalleFacturaBusqueda(con,
				forma.getGlosa()));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaAvanzadaSolicitudes");
	}

	/**
	 * Método usado para volver el listado de facturas
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVolverListadoFacturas(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping) {
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosa");
	}

	/**
	 * Método para eliminar un registro del detalle de la factura de la glosa
	 * 
	 * @param con
	 * @param registroForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarDetalle(Connection con,
			RegistrarModificarGlosasForm forma, ActionMapping mapping,
			UsuarioBasico usuario) {
		int posFactura = forma.getGlosa().getPosicionFactura();
		int posDetalle = forma.getGlosa().getFacturas().get(posFactura)
				.getPosicionDetalle();
		forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(
				posDetalle).setEliminado(true);
		forma.getGlosa().getFacturas().get(posFactura).getDetalle().get(
				posDetalle).getConceptos().get(0).setEliminado(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	// ##################################################### FIN
	// ##############################################################//

	/**
	 * Metodo utilizado para cargar y mostrar el mapa de Conceptos
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionMostrarConceptos(
			RegistrarModificarGlosasForm forma, RegistrarModificarGlosas mundo,
			Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		// se consultan todos los conceptos tipo glosa
		forma.setConceptosParamMap(mundo.consultaConceptos(con, "",
				ConstantesIntegridadDominio.acronimoTipoGlosaGlosa));

		// se consultan los conceptos de la factura de la glosa
		/*
		 * forma.setConceptosDetalleFacturaMap(mundo.consultarTodosConceptosFacturas
		 * (con, forma.getInformacionGlosa("codglosa")+"")); for(int
		 * j=0;j<Utilidades
		 * .convertirAEntero(forma.getConceptosDetalleFacturaMap(
		 * "numRegistros")+"");j++) {
		 * forma.setConceptosDetalleFacturaMap("consulta_"+j,
		 * ConstantesBD.acronimoSi);
		 * forma.setConceptosDetalleFacturaMap("devolucion_"+j,
		 * ConstantesBD.acronimoNo); }
		 */
		for (int x = 0; x < Integer.parseInt(forma
				.getConceptosParamMap("numRegistros")
				+ ""); x++) {
			forma.setConceptosParamMap("selec_" + x, ConstantesBD.acronimoNo);
			for (int i = 0; i < Integer.parseInt(forma
					.getConceptosDetalleFacturaMap("numRegistros")
					+ ""); i++) {
				if ((forma.getConceptosDetalleFacturaMap("fac_" + i) + "")
						.equals(forma.getFactura())) {
					// se cargan los conceptos relacionados con la Factura
					// seleccionada
					if ((forma.getConceptosDetalleFacturaMap("codigo_" + i) + "")
							.equals((forma.getConceptosParamMap("codconcepto_"
									+ x) + "")))
						forma.setConceptosParamMap("selec_" + x,
								ConstantesBD.acronimoSi);
					else
						forma.setConceptosParamMap("selec_" + x,
								ConstantesBD.acronimoNo);
				}
			}
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("conceptosPorFactura");
	}

	/**
	 * Metodo para subir un concepto a una factura determinada
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSubirConcepto(
			RegistrarModificarGlosasForm forma, RegistrarModificarGlosas mundo,
			Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		String nombreConcepto = "";
		String tipoConcepto = "";

		for (int k = 0; k < forma.getConceptosAdd().length; k++) {
			forma.setConceptoSel(forma.getConceptosAdd()[k]);

			for (int i = 0; i < Utilidades.convertirAEntero(forma
					.getConceptosParamMap("numRegistros")
					+ ""); i++) {
				// se verifica el nombre del concepto segun el codigo concepto
				// seleccionado y se marca como seleccionado
				if ((forma.getConceptosParamMap("selec_" + i) + "")
						.equals(ConstantesBD.acronimoNo)) {
					if ((forma.getConceptosParamMap("codconcepto_" + i) + "")
							.equals(forma.getConceptoSel())) {
						nombreConcepto = forma
								.getConceptosParamMap("descconcepto_" + i)
								+ "";
						tipoConcepto = forma
								.getConceptosParamMap("tipoconcepto_" + i)
								+ "";
						forma.setConceptosParamMap("selec_" + i,
								ConstantesBD.acronimoSi);
					}
				}
			}

			// se almacena el nuevo concepto para la Factura en el mapa de
			// conceptos por Factura
			int numReg = Utilidades.convertirAEntero(forma
					.getConceptosDetalleFacturaMap("numRegistros")
					+ "");
			forma.setConceptosDetalleFacturaMap("codigo_" + numReg, forma
					.getConceptoSel());
			forma.setConceptosDetalleFacturaMap("descripcion_" + numReg,
					nombreConcepto);
			forma.setConceptosDetalleFacturaMap("fac_" + numReg, forma
					.getFactura());
			forma.setConceptosDetalleFacturaMap("consulta_" + numReg,
					ConstantesBD.acronimoNo);
			forma.setConceptosDetalleFacturaMap("seleccionado_" + numReg,
					ConstantesBD.acronimoSi);
			forma.setConceptosDetalleFacturaMap("devolucion_" + numReg,
					ConstantesBD.acronimoNo);
			forma.setConceptosDetalleFacturaMap("tipoconcepto_" + numReg,
					tipoConcepto);
			forma.setConceptosDetalleFacturaMap("numRegistros", numReg + 1);

		}

		UtilidadBD.closeConnection(con);
		//se cambio el return de conceptosPorFactura a seccionConceptosFactura para recargar la zona solicitada
		//return mapping.findForward("seccionConceptosFactura");  
		return mapping.findForward("conceptosPorFactura");
	}

	/**
	 * Metodo que permite cerrar la ventana PoP Up Conceptos Esto se hizo por
	 * actualizar los check del mapa detalle en la forma
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCerrarPopUpConcepto(
			RegistrarModificarGlosasForm forma, RegistrarModificarGlosas mundo,
			Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		UtilidadBD.closeConnection(con);
		return mapping.findForward("conceptosPorFactura");
	}

	/**
	 * Método para filtrar los contratos al cambiar el convenio
	 * 
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarContratos(Connection con,
			RegistrarModificarGlosasForm generarForm,
			HttpServletResponse response) {
		String resultado = "<respuesta>" + "<infoid>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>codigoContrato</id-select>"
				+ "<id-arreglo>contrato</id-arreglo>" + "</infoid>";

		generarForm.setArregloContratos(Utilidades.obtenerContratos(con,
				Utilidades.convertirAEntero(generarForm.getConvenioP().split(
						ConstantesBD.separadorSplit)[0]), false, true));

		for (HashMap elemento : generarForm.getArregloContratos()) {
			resultado += "<contrato>";
			resultado += "<codigo>" + elemento.get("codigo") + "</codigo>";
			resultado += "<descripcion>"
					+ elemento.get("numerocontrato")
					+ " Vig: "
					+ UtilidadFecha.conversionFormatoFechaAAp(elemento
							.get("fechainicialbd")
							+ "")
					+ " - "
					+ UtilidadFecha.conversionFormatoFechaAAp(elemento
							.get("fechafinal")
							+ "") + "</descripcion>";
			resultado += "</contrato>";
		}

		resultado += "</respuesta>";

		UtilidadBD.closeConnection(con);
		// **********SE GENERA RESPUESTA PARA AJAX EN
		// XML**********************************************
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultado);
		} catch (IOException e) {
			logger
					.error("Error al enviar respuesta AJAX en accionFiltrarContratos: "
							+ e);
		}
		return null;
	}

	/**
	 * Metodo que actualiza el registro como eliminado
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(RegistrarModificarGlosasForm forma,
			RegistrarModificarGlosas mundo, Connection con,
			ActionMapping mapping, UsuarioBasico usuario) {
		forma.setMapaDetalleGlosa("eliminar_" + forma.getPosicion(), "S");
		forma.setMapaDetalleGlosa("valorglosa_" + forma.getPosicion(),
				UtilidadTexto.formatearValores("0", 2, true, false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosa");
	}

	/**
	 * Metodo que guarda las facturas de la Glosa
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarFacturas(
			RegistrarModificarGlosasForm forma, RegistrarModificarGlosas mundo,
			Connection con, ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request) {
		String operacion = "";
		int temp = 0;
		int codaudi = 0;
		int n = Utilidades.convertirAEntero(forma
				.getMapaDetalleGlosa("numRegistros")
				+ "");

		ActionErrors errores = new ActionErrors();
		int valorTotal = 0;

		for (int i = 0; i < (Utilidades.convertirAEntero(forma
				.getMapaDetalleGlosa("numRegistros")
				+ "")); i++) {
			int cont = 0;
			if ((forma.getMapaDetalleGlosa("tipofactura_" + i) + "")
					.equals("false")
					&& (forma.getMapaDetalleGlosa("eliminar_" + i) + "")
							.equals(ConstantesBD.acronimoNo)) {
				for (int j = 0; j < (Utilidades.convertirAEntero(forma
						.getConceptosDetalleFacturaMap("numRegistros")
						+ "")); j++) {
					if ((forma.getMapaDetalleGlosa("codfactura_" + i) + "")
							.equals(forma.getConceptosDetalleFacturaMap("fac_"
									+ j)
									+ "")) {
						cont++;
					}
				}
				if (cont == 0) {
					forma.setMapaDetalleGlosa("mostrarLink_" + i, "N");
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"prompt.generico",
											"Es necesario asignar por lo menos un concepto para la factura numero "
													+ (forma
															.getMapaDetalleGlosa("factura_"
																	+ i))));
				}
			}
		}

		for (int j = 0; j < Utilidades.convertirAEntero(forma
				.getMapaDetalleGlosa("numRegistros")
				+ ""); j++) {
			valorTotal += Utilidades.convertirAEntero(forma
					.getMapaDetalleGlosa("valorglosa_" + j)
					+ "");

			if ((Utilidades.convertirADouble(forma
					.getMapaDetalleGlosa("valorglosa_" + j)
					+ "") == 0 || (forma.getMapaDetalleGlosa("valorglosa_" + j) + "")
					.equals(""))
					&& ((forma.getMapaDetalleGlosa("eliminar_" + j) + "")
							.equals(ConstantesBD.acronimoNo))) {
				forma.setMapaDetalleGlosa("mostrarLink_" + j, "N");
				errores.add("descripcion", new ActionMessage("prompt.generico",
						"El valor de la Factura numero "
								+ forma.getMapaDetalleGlosa("factura_" + j)
								+ " debe ser mayor a cero"));
			}
		}

		if (Utilidades.convertirAEntero(forma.getValor() + "") < valorTotal)
			errores.add("descripcion", new ActionMessage("prompt.generico",
					"Total de Glosa esta Descuadrado"));

		if (!errores.isEmpty())
			saveErrors(request, errores);
		else {
			boolean transaccion = UtilidadBD.iniciarTransaccion(con);
 			
			for (int i = 0; i < n; i++) {
				String consulta = (forma.getMapaDetalleGlosa("esconsulta_" + i) + "");
				if (consulta.equals(ConstantesBD.acronimoNo)
						&& (forma.getMapaDetalleGlosa("eliminar_" + i) + "")
								.equals(ConstantesBD.acronimoNo)) {
					HashMap<String, Object> criterios = new HashMap<String, Object>();
					operacion = "INSERTAR";
					criterios.put("codigofactura", forma
							.getMapaDetalleGlosa("codfactura_" + i)
							+ "");
					criterios.put("usuario", usuario.getLoginUsuario());
					criterios.put("contrato", forma
							.getMapaDetalleGlosa("contrato_" + i)
							+ "");
					criterios.put("valorglosafact", forma
							.getMapaDetalleGlosa("valorglosa_" + i)
							+ "");
					criterios.put("saldofact", forma
							.getMapaDetalleGlosa("saldofactura_" + i)
							+ "");
					criterios.put("fechaelab", forma
							.getMapaDetalleGlosa("fechaelab_" + i)
							+ "");
					criterios.put("cuentacobro", forma
							.getMapaDetalleGlosa("cuentacobro_" + i)
							+ "");
					criterios.put("institucion", usuario
							.getCodigoInstitucionInt());
					criterios.put("fecharad", forma
							.getMapaDetalleGlosa("fecharadicacion_" + i)
							+ "");
					criterios.put("glosa", forma
							.getInformacionGlosa("codglosa")
							+ "");
					criterios.put("valorglosa", forma.getValor());
										
					criterios.put("reiterada", forma.getMapaDetalleGlosa("reiterada_"+i)+ "");
			
					
					if (transaccion)
						codaudi = mundo.guardarFacturas(con, criterios,
								operacion);

					logger.info("\n\nCOD AUDI------->" + codaudi);

					if (codaudi < 0)
						transaccion = false;
					else {
						forma.setMapaDetalleGlosa("esconsulta_" + i, "S");
						forma.setMapaDetalleGlosa("codigoaudi_" + i, codaudi);
						forma.setMapaDetalleGlosa("codglosa_" + i, forma
								.getInformacionGlosa("codglosa")
								+ "");
					}
				} else if (consulta.equals(ConstantesBD.acronimoSi)) {
					// si la factura fue ingresada en la funcionalidad registrar
					// modificar glosas y fue seleccionada
					// para borrar de la lista, se elimina la Factura con sus
					// detalles y asocios y los Conceptos de cada uno
					if ((forma.getMapaDetalleGlosa("puedoeliminar_" + i) + "")
							.equals(ConstantesBD.acronimoSi)
							&& (forma.getMapaDetalleGlosa("eliminar_" + i) + "")
									.equals(ConstantesBD.acronimoSi)) {
						if (transaccion)
							transaccion = mundo.eliminarFacturaGlosa(con, forma
									.getMapaDetalleGlosa("codigoaudi_" + i)
									+ "");
					} else {
						if ((forma.getMapaDetalleGlosa("eliminar_" + i) + "")
								.equals(ConstantesBD.acronimoNo)) {
							HashMap<String, Object> criterios = new HashMap<String, Object>();
							operacion = "ACTUALIZAR";
							criterios.put("codigoaudi", forma
									.getMapaDetalleGlosa("codigoaudi_" + i)
									+ "");
							criterios.put("valorglosafact", forma
									.getMapaDetalleGlosa("valorglosa_" + i)
									+ "");
							criterios.put("valorglosa", forma.getValor());
							criterios.put("glosa", forma
									.getInformacionGlosa("codglosa")
									+ "");

							if (transaccion)
								codaudi = mundo.guardarFacturas(con, criterios,
										operacion);
							if (codaudi < 0)
								transaccion = false;
						}
					}
				}
				for (int k = 0; k < (Utilidades.convertirAEntero(forma
						.getConceptosDetalleFacturaMap("numRegistros")
						+ "")); k++) {
					if ((forma.getConceptosDetalleFacturaMap("consulta_" + k) + "")
							.equals(ConstantesBD.acronimoNo)
							&& (forma
									.getConceptosDetalleFacturaMap("seleccionado_"
											+ k) + "")
									.equals(ConstantesBD.acronimoSi)) {
						// se insertan los conceptos que se seleccionaron para
						// la factura
						if ((forma.getConceptosDetalleFacturaMap("fac_" + k) + "")
								.equals(forma.getMapaDetalleGlosa("codfactura_"
										+ i)
										+ "")) {
							HashMap<String, Object> criterios = new HashMap<String, Object>();
							criterios.put("codigoaudi", codaudi);
							criterios.put("codconcepto", forma
									.getConceptosDetalleFacturaMap("codigo_"
											+ k)
									+ "");
							criterios.put("institucion", usuario
									.getCodigoInstitucionInt());
							transaccion = mundo.guardarConceptoFactura(con,
									criterios);
							if (transaccion)
								forma.setConceptosDetalleFacturaMap("consulta_"
										+ k, "S");
						}
					}
				}
			}
			if (transaccion) {
				for (int i = 0; i < (Utilidades.convertirAEntero(forma
						.getMapaDetalleGlosa("numRegistros")
						+ "")); i++) {
					forma.setMapaDetalleGlosa("mostrarLink_" + i, "S");
				}
				forma.setMensaje(new ResultadoBoolean(true,
						"Operaciones Realizadas con Exito"));
				logger
						.info("\n\n#####################TRANSACCION FINALIZADA CON EXITO###################");
				UtilidadBD.finalizarTransaccion(con);
			} else {
				forma.setMensaje(new ResultadoBoolean(true,
						"Las Operaciones NO finalizaron satisfactoriamente."));
				logger
						.info("\n\n#####################ERROR. TRANSACCION ABORTADA######################");
				UtilidadBD.abortarTransaccion(con);
			}
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosa");
	}

	/**
	 * Metodo que muestra el historico segun el codigo de la Glosa
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionHistoricoGlosa(
			RegistrarModificarGlosasForm forma, RegistrarModificarGlosas mundo,
			Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		// se carga el historico de las Glosas relacionadas con la Factura
		// seleccionada
		forma.setHistoricoMap(mundo.accionHistoricoGlosa(con, forma
				.getCodFactura()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoGlosa");
	}

	/**
	 * Inicia en el forward de Registrar Modificar Glosas
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(RegistrarModificarGlosasForm forma,
			RegistrarModificarGlosas mundo, Connection con,
			ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		forma.reset(usuario.getCodigoInstitucionInt());

		// ***************SE TOMA Y SE VALIDA EL CONSECUTIVO DE
		// INGRESO**********************************************************
		String valorConsecutivoGlosa = UtilidadBD
				.obtenerValorConsecutivoDisponible(
						ConstantesBD.nombreConsecutivoGlosas, usuario
								.getCodigoInstitucionInt());
		String anioConsecutivoIngreso = UtilidadBD.obtenerAnioConsecutivo(
				ConstantesBD.nombreConsecutivoGlosas, usuario
						.getCodigoInstitucionInt(), valorConsecutivoGlosa);

		if (!UtilidadCadena.noEsVacio(valorConsecutivoGlosa)
				|| valorConsecutivoGlosa.equals("-1"))
			errores.add("Falta consecutivo disponible", new ActionMessage(
					"error.glosas.faltaDefinirConsecutivo", "el registro de"));
		else {
			try {
				Integer.parseInt(valorConsecutivoGlosa);
			} catch (Exception e) {
				logger
						.error("Error en validacionConsecutivoDisponibleIngreso:  "
								+ e);
				errores.add("Consecutivo no es entero", new ActionMessage(
						"errors.integer", "el consecutivo de la Glosa"));
			}
		}

		// Si hay algun error con el consecutivo de glosa se debe manejar el
		// error
		if (!errores.isEmpty()) {
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(
				con, ConstantesBD.nombreConsecutivoGlosas, usuario
								.getCodigoInstitucionInt(),
				valorConsecutivoGlosa, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		// ***********************************************************************************************************************

		forma.setInformacionGlosa("disablecontrato", "false");

		if (UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarUsuarioGlosa(usuario.getCodigoInstitucionInt()))) {
			forma.setArregloConveniosPrincipal(UtilidadesFacturacion.obtenerConvenioPorUsuario(
									con,
									usuario.getLoginUsuario(),
									ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa,
									true));

			forma.setArregloConvMap("numRegistros", forma.getArregloConveniosPrincipal().size());
			for (int i = 0; i < forma.getArregloConveniosPrincipal().size(); i++) {
				forma.setArregloConvMap("codigoConvenio_" + i, forma.getArregloConveniosPrincipal().get(i).get("codigoConvenio"));
			}
		} else
			forma.setArregloConveniosPrincipal(Utilidades.obtenerConvenios(con,"", "", false, "", false));

		if(forma.getArregloConveniosPrincipal().size()==1)
			forma.setArregloContratos(Utilidades.obtenerContratos(con,Utilidades.convertirAEntero(forma.getArregloConvMap("codigoConvenio_0").toString()), false, true));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarModificarGlosas");
	}

	/**
	 * Metodo para buscar Glosas
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionBuscar(Connection con,
			RegistrarModificarGlosasForm forma, RegistrarModificarGlosas mundo,
			ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request, HttpServletResponse response) {
		if (UtilidadTexto.getBoolean(ValoresPorDefecto
				.getValidarUsuarioGlosa(usuario.getCodigoInstitucionInt()))) {
			logger
					.info("Mostrar convenios donde el usuario logueado en sesion se encuentre como tipo Glosa en la tabla usuarios_glosas_conv");
			forma
					.setArregloConvenios(UtilidadesFacturacion
							.obtenerConvenioPorUsuario(
									con,
									usuario.getLoginUsuario(),
									ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa,
									true));
		} else
			forma.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "",
					false, "", false));
		forma.setConvenio("");

		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarModificarGlosas");
	}

	/**
	 * Retorna el forward para mostrar la glosa
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @return
	 */
	private ActionForward accionMostrarGlosa(
			RegistrarModificarGlosasForm forma, RegistrarModificarGlosas mundo,
			Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		int temp = 0;
		forma.setArregloContratos(Utilidades.obtenerContratos(con, Utilidades
				.convertirAEntero(forma.getConvenioP().split(
						ConstantesBD.separadorSplit)[0]), false, true));
		forma.setContratosGlosaMap(mundo.consultaContratosFactura(con, forma
				.getInformacionGlosa("codglosa")
				+ ""));
		for (int z = 0; z < Utilidades.convertirAEntero(forma
				.getContratosGlosaMap("numRegistros")
				+ ""); z++) {
			for (int i = 0; i < Utilidades.convertirAEntero(forma
					.getContratosGlosaMap("numRegistros")
					+ ""); i++) {
				if (i != z
						&& !(forma.getContratosGlosaMap("contrato_" + i) + "")
								.equals(forma.getContratosGlosaMap("contrato_"
										+ z)
										+ ""))
					temp = 1;
			}
		}
		if (temp == 1)
			forma.setInformacionGlosa("disablecontrato", "true");

		forma.setInformacionGlosa("esconsulta", ConstantesBD.acronimoSi);

		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarModificarGlosas");
	}

	/**
	 * Metodo para guardar Glosa modificada
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardar(RegistrarModificarGlosasForm forma,
			RegistrarModificarGlosas mundo, Connection con,
			ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request) {
		HashMap<String, Object> mapaAux = new HashMap<String, Object>();
		String conse = "";

		forma.setCodConvenio(forma.getConvenioP());

		// se guarda en el mapa los datos que vienen de la consulta de la Glosa
		// o los ingresados
		// por el usuario para insertar o modificar la Glosa
		mapaAux.put("codglosa", forma.getInformacionGlosa("codglosa"));
		mapaAux.put("fechareg", forma.getFechaRegistro());
		mapaAux.put("convenio", forma.getConvenioP());
		mapaAux.put("glosaent", forma.getGlosaEntidad());
		mapaAux.put("fechanot", forma.getFechaNotificacion());
		mapaAux.put("valor", forma.getValor());
		mapaAux.put("observ", forma.getObservaciones());
		mapaAux.put("institucion", usuario.getCodigoInstitucion());
		mapaAux.put("contrato", forma.getCodigoContrato());

		// se guarda la Glosa ya sea para insertar o para actualizar
		// se almacena en la variable valorGuardar el codigo de la Glosa
		if (forma.getBanderaGuardar().equals("INSERT")) {
			// cuando se va a insertar una nueva Glosa se genera un consecutivo
			// para la glosa sistema
			conse = UtilidadBD.obtenerValorConsecutivoDisponible(
					ConstantesBD.nombreConsecutivoGlosas, usuario
							.getCodigoInstitucionInt());
			forma.setInformacionGlosa("glosasis", conse);
		}
		int valorGuardar = mundo.guardar(con, mapaAux, usuario
				.getLoginUsuario(), forma, conse);
		if (forma.getBanderaGuardar().equals("INSERT")) {
			if (valorGuardar > 0)
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(
						con, ConstantesBD.nombreConsecutivoGlosas, usuario
										.getCodigoInstitucionInt(),
						conse, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			else
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(
						con, ConstantesBD.nombreConsecutivoGlosas, usuario
										.getCodigoInstitucionInt(),
						conse, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		}

		// se consulta el detalle de la Glosa segun el codigo
		forma.setMapaDetalleGlosa(mundo.consultarDetalleGlosa(con,
				valorGuardar, usuario.getCodigoInstitucionInt()));

		for (int i = 0; i < (Utilidades.convertirAEntero(forma
				.getMapaDetalleGlosa("numRegistros")
				+ "")); i++) {
			forma.setMapaDetalleGlosa("mostrarLink_" + i, "S");
		}
		// se consultan los conceptos de Asocios y de Solicitudes que tiene la
		// Glosa
		forma.setConceptosDetalleFacturaMap(mundo
				.consultarTodosConceptosFacturas(con, valorGuardar + ""));

		// se consulta por cada factura si tiene historico de glosas
		for (int i = 0; i < (Utilidades.convertirAEntero(forma
				.getMapaDetalleGlosa("numRegistros")
				+ "")); i++) {
			forma.setMapaDetalleGlosa("mostrarHistorico_" + i,
					ConstantesBD.acronimoNo);
			HashMap<String, Object> resultados = new HashMap<String, Object>();
			resultados.putAll(mundo.accionHistoricoGlosa(con, forma
					.getMapaDetalleGlosa("codfactura_" + i)
					+ ""));
			if ((Utilidades.convertirAEntero(resultados.get("numRegistros")
					+ "")) > 0)
				forma.setMapaDetalleGlosa("mostrarHistorico_" + i,
						ConstantesBD.acronimoSi);
		}

		for (int j = 0; j < Utilidades.convertirAEntero(forma
				.getConceptosDetalleFacturaMap("numRegistros")
				+ ""); j++) {
			forma.setConceptosDetalleFacturaMap("consulta_" + j,
					ConstantesBD.acronimoSi);
			forma.setConceptosDetalleFacturaMap("seleccionado_" + j,
					ConstantesBD.acronimoNo);
			forma.setConceptosDetalleFacturaMap("devolucion_" + j,
					ConstantesBD.acronimoNo);
		}

		forma.setInformacionGlosa("codglosa", valorGuardar);

		for (int i = 0; i < Utilidades.convertirAEntero(forma
				.getMapaDetalleGlosa("numRegistros")
				+ ""); i++) {
			forma.setMapaDetalleGlosa("eliminar_" + i, "N");
			forma.setMapaDetalleGlosa("esconsulta_" + i, "S");
			// se valida si la Glosa fue insertada en Registro Auditoria o en
			// Registro Glosa para determinar si se puede eliminar o no de BD
			if ((forma.getMapaDetalleGlosa("fueauditada_" + i) + "")
					.equals(ConstantesBD.acronimoSi))
				forma.setMapaDetalleGlosa("puedoeliminar_" + i, "N");
			else
				forma.setMapaDetalleGlosa("puedoeliminar_" + i, "S");
		}

		forma.setConvenioP(Utilidades.obtenerNombreConvenioOriginal(con,
				Utilidades.convertirAEntero(forma.getConvenioP() + "")));

		// Se valida si el tipo de concepto de las facturas de la Glosa es tipo
		// devolucion
		for (int k = 0; k < (Utilidades.convertirAEntero(forma
				.getMapaDetalleGlosa("numRegistros")
				+ "")); k++) {
			for (int h = 0; h < (Utilidades.convertirAEntero(forma
					.getConceptosDetalleFacturaMap("numRegistros")
					+ "")); h++) {
				if ((forma.getMapaDetalleGlosa("codfactura_" + k) + "")
						.equals(forma.getConceptosDetalleFacturaMap("fac_" + h)
								+ "")) {
					if ((forma.getConceptosDetalleFacturaMap("tipoconcepto_"
							+ h) + "")
							.equals(ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion)) {
						forma.setConceptosDetalleFacturaMap("devolucion_" + h,
								ConstantesBD.acronimoSi);
					}
				}
			}
		}
		if (valorGuardar > 0)
			forma.setMensaje(new ResultadoBoolean(true,
					"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,
					"La glosa no se actualizó satisfactoriamente."));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosa");
	}

	/**
	 * Metodo para guardar Glosa modificada
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionBuscarFact(RegistrarModificarGlosasForm forma,
			RegistrarModificarGlosas mundo, Connection con,
			ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		int aux = 0;
		int aux1 = 0;
		int numeroActualGlosasPorFactura = 0;
		int numeroDiasGlosa = 0;
		int numDias = 0;
		String reiterada=ConstantesBD.acronimoNo;
		String numero = ValoresPorDefecto
				.getNumeroGlosasRegistradasXFactura(usuario
						.getCodigoInstitucionInt());
		String numeroDias = ValoresPorDefecto
				.getNumeroDiasNotificarGlosa(usuario.getCodigoInstitucionInt());

		if (forma.getFacturaABuscar().equals(""))
			errores.add("descripcion", new ActionMessage("errors.required",
					"La Factura"));
		else {
			// se consulta la factura filtrando la busqueda por consecutivo
			// factura, convenio y contrato
			tempMap.put("factura", forma.getFacturaABuscar());
			tempMap.put("convenio", forma.getCodConvenio());
			tempMap.put("contrato", forma.getCodigoContrato());
			forma.setUnicaFacturaMap(mundo.consultarUnicaFactura(con, tempMap));

			Utilidades.imprimirMapa(forma.getUnicaFacturaMap());

			if (forma.getUnicaFacturaMap().containsKey("fecharadicacion") && !(forma.getUnicaFacturaMap("fecharadicacion")+"").isEmpty()) {
				logger.info("\n\nfecha radicacion fact: "+forma.getUnicaFacturaMap("fecharadicacion"));
				String fechaRad = (forma.getUnicaFacturaMap("fecharadicacion") + "").substring(0, 10);
				forma.setUnicaFacturaMap("fecharadicacion", fechaRad);
			}

		}
		if (Utilidades.convertirAEntero(forma
				.getUnicaFacturaMap("numRegistros")
				+ "") > 0) {
			// se valida que el numero de facturas por glosa no exceda el
			// parametro de glosas registradas por factura
			numeroActualGlosasPorFactura = mundo
					.consultarNumeroGlosasPorFactura(con, forma
							.getUnicaFacturaMap("codfactura")
							+ "");

			logger.info("\n\nNUM GLOSAS POR FACTURA----->"
					+ numeroActualGlosasPorFactura + " Parametro--->" + numero);
			
			if(numeroActualGlosasPorFactura > 0)
				reiterada= ConstantesBD.acronimoSi;
				
			if (Utilidades.convertirAEntero(numero) <= numeroActualGlosasPorFactura)
				errores
						.add(
								"descripcion",
								new ActionMessage(
										"prompt.generico",
										"Factura "
												+ forma.getFacturaABuscar()
												+ " Excede el Numero de Registro de Glosas Permitido. Ver Historico"));
			else {
				numDias = mundo.consultaFechaRadicacion(con, forma
						.getUnicaFacturaMap("codfactura")
						+ "", forma.getMapaDetalleGlosa("codglosa_0") + "");

				if (Utilidades.convertirAEntero(numeroDias) < numDias)
					errores
							.add(
									"descripcion",
									new ActionMessage(
											"prompt.generico",
											"La Factura "
													+ forma.getFacturaABuscar()
													+ " supera el numero de dias para Notificar Glosa."));
				else {
					for (int j = 0; j < Utilidades.convertirAEntero(forma
							.getMapaDetalleGlosa("numRegistros")
							+ ""); j++) {
						if ((forma.getMapaDetalleGlosa("factura_" + j) + "")
								.equals(forma.getUnicaFacturaMap("factura")
										+ "")) {
							aux = 1;
							// si la factura ya se encuentra en relacionada a la
							// Glosa se valida si fue seleccionada para
							// elmiminar
							// y si fue ingresada en la funcionalidad Registrar
							// Glosa y en este caso no se almacena en el mapa
							// Detalle Glosa
							if ((forma
									.getMapaDetalleGlosa("puedoeliminar_" + j) + "")
									.equals("S")
									&& (forma.getMapaDetalleGlosa("eliminar_"
											+ j) + "").equals("S")) {
								aux = 0;
								aux1 = 1;
								forma.setMapaDetalleGlosa("eliminar_" + j, "N");
							}
						}

						if (aux != 0)
							errores
									.add(
											"descripcion",
											new ActionMessage(
													"prompt.generico",
													"La Factura Seleccionada ya se Encuentra Relacionada a la Glosa"));
					}
				}
			}
		} else
			errores
					.add(
							"descripcion",
							new ActionMessage("prompt.generico",
									"La Factura que digito no es valida para Generar Glosa. Por favor verifique"));

		if (!errores.isEmpty())
			saveErrors(request, errores);
		else {
			// se almacena la nueva factura en el mapa del Detalle de la Glosa
			if (aux1 != 1) {
				forma.setMapaDetalleGlosa("numRegistros", Utilidades
						.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") + 1);
				forma.setMapaDetalleGlosa("codfactura_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), forma
						.getUnicaFacturaMap("codfactura"));
				forma.setMapaDetalleGlosa("factura_"+ (Utilidades.convertirAEntero(forma.getMapaDetalleGlosa("numRegistros")+ "") - 1), forma.getUnicaFacturaMap("factura"));
				forma.setMapaDetalleGlosa("contrato_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), forma
						.getUnicaFacturaMap("contrato"));
				forma.setMapaDetalleGlosa("fechaelab_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), forma
						.getUnicaFacturaMap("fechaelab"));
				forma.setMapaDetalleGlosa("cuentacobro_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), forma
						.getUnicaFacturaMap("cuentacobro"));
				forma.setMapaDetalleGlosa("fecharadicacion_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), forma
						.getUnicaFacturaMap("fecharadicacion"));
				forma.setMapaDetalleGlosa("saldofactura_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), forma
						.getUnicaFacturaMap("saldofactura"));
				forma.setMapaDetalleGlosa("esconsulta_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), ConstantesBD.acronimoNo);
				forma.setMapaDetalleGlosa("valorglosa_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), UtilidadTexto.formatearValores("0",
						2, true, false));
				forma.setMapaDetalleGlosa("eliminar_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), ConstantesBD.acronimoNo);
				forma.setMapaDetalleGlosa("puedoeliminar_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), ConstantesBD.acronimoSi);
				forma.setMapaDetalleGlosa("mostrarHistorico_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), ConstantesBD.acronimoNo);
				forma.setMapaDetalleGlosa("mostrarLink_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), ConstantesBD.acronimoNo);
				forma.setMapaDetalleGlosa("tipofactura_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), forma
						.getUnicaFacturaMap("tipofactura"));
				forma.setMapaDetalleGlosa("reiterada_"
						+ (Utilidades.convertirAEntero(forma
								.getMapaDetalleGlosa("numRegistros")
								+ "") - 1), reiterada);
						
				HashMap<String, Object> resultado = new HashMap<String, Object>();

				resultado.putAll(mundo.accionHistoricoGlosa(con, forma
						.getMapaDetalleGlosa("codfactura_"
								+ (Utilidades.convertirAEntero(forma
										.getMapaDetalleGlosa("numRegistros")
										+ "") - 1))
						+ ""));
				if ((Utilidades.convertirAEntero(resultado.get("numRegistros")
						+ "")) > 0)
					forma.setMapaDetalleGlosa("mostrarHistorico_"
							+ (Utilidades.convertirAEntero(forma
									.getMapaDetalleGlosa("numRegistros")
									+ "") - 1), ConstantesBD.acronimoSi);
			}

			forma.setFacturaABuscar("");
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosa");
	}
}