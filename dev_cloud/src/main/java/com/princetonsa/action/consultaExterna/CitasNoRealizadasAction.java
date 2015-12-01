 	package com.princetonsa.action.consultaExterna;

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
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.consultaExterna.CitasNoRealizadasForm;
import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.Multas;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */
public class CitasNoRealizadasAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(CitasNoRealizadasAction.class);

	private Multas multas = new Multas();

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		Connection connection = null;
		try {
			if (form instanceof CitasNoRealizadasForm) {

				connection = UtilidadBD.abrirConexion();

				// se verifica si la conexion esta nula
				if (connection == null) {
					// de ser asi se envia a una pagina de error.
					request.setAttribute("CodigoDescripcionError",
					"erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				// se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico) request.getSession()
				.getAttribute("usuarioBasico");

				// se obtiene el usuario cargado en sesion.
				PersonaBasica paciente = (PersonaBasica) request.getSession()
				.getAttribute("pacienteActivo");

				// obtenemos el valor de la forma.
				CitasNoRealizadasForm forma = (CitasNoRealizadasForm) form;

				// obtenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				logger
				.info("\n\n***************************************************************************");
				logger.info("EL ESTADO DE CitasNoRealizadasForm ES ====>> "
						+ forma.getEstado());
				logger
				.info("\n***************************************************************************");

				/*
				 * ------------------------------------------------------------------
				 * - ESTADOS PARA LAS CITAS NO REALIZADAS POR RANGO
				 * ------------------
				 * -------------------------------------------------
				 */

				/*
				 * ---------------------------------------------- ESTADO
				 * =================>>>>> NULL
				 * ---------------------------------------------
				 */

				if (estado == null) {
					// se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError",
					"errors.estadoInvalido");
					// se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);

					// se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				} else {
					/*
					 * ESTADO EMPEZAR
					 * 
					 */
					if (estado.equals("empezar")) {
						forma.reset();
						return accionEmpezar(connection, forma, usuario,request,mapping);


					} else if (estado.equals("filtrarUnidadesAgenda")) {
						return accionFiltrarUnidadesAgenda(connection, forma,
								usuario, response);

					} else if (estado.equals("buscar")) {
						return accionBuscar(connection, forma, usuario, response, request, mapping);
					} else if (estado.equals("cargarDetalleRango")) {
						return accionCargaDetalleRango(connection, forma, usuario,
								request, mapping);

					} else if (estado.equals("redireccion")) {
						response.sendRedirect(forma.getLinkSiguiente());
						UtilidadBD.closeConnection(connection);
						return null;

					} else if (estado.equals("guardarRango")) {
						return accionGuardarRango(connection, forma, usuario,
								mapping, request, paciente);

					} else if (estado.equals("ordenar")) {
						return accionOrdenar(connection, forma, usuario, mapping,
								request);

					} else if (estado.equals("confirmarAnularAprobar")) {
						return accionConfirmarAnularAprobar(connection, forma,
								usuario, mapping, request);

					} else if (estado.equals("empezarPaciente")) {
						forma.reset();
						return accionListarPaciente(connection, forma, usuario,
								mapping, request, paciente);

					}
					else if (estado.equals("listarPaciente")) {
						return accionListarPaciente(connection, forma, usuario,
								mapping, request, paciente);

					}
				}
			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
	}

	private ActionForward accionListarPaciente(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request,
			PersonaBasica paciente) {
		forma.setTieneRolGenModFactVar(Utilidades.tieneRolFuncionalidad(usuario
				.getLoginUsuario(), 723));
		forma.setTieneRolAproAnulFactVar(Utilidades.tieneRolFuncionalidad(
				usuario.getLoginUsuario(), 736));
		forma.setInstitucionManejaMulta(ValoresPorDefecto.getInstitucionManejaMultasPorIncumplimiento(usuario.getCodigoInstitucionInt()));
		forma.setOpcionRango(ConstantesBD.acronimoNo);
		
		ActionForward forward = new ActionForward();
		forward = accionVerficarPaciente(connection, forma, paciente, usuario, request,
				mapping);
		
		if(forward!=null)
			return forward;
		
		forma.setResultado(multas.buscarCitasNoRealizadasPaciente(connection,
				forma.getCriterios(), usuario, paciente));
		cargarConcptFactVarias(connection, forma, usuario);
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("citasNoRealizadasPaciente");
	}

	private ActionForward accionVerficarPaciente(Connection con,
			CitasNoRealizadasForm forma, PersonaBasica paciente,
			UsuarioBasico usuario, HttpServletRequest request,
			ActionMapping mapping) {
		if (paciente == null || paciente.getCodigoPersona() <= 0) {
			return ComunAction.accionSalirCasoError(mapping, request, con,
					logger, "errors.paciente.noCargado",
					"errors.paciente.noCargado", true);
		}
		return null;
	}

	private ActionForward accionConfirmarAnularAprobar(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) {

		UtilidadBD.closeConnection(connection);
		return mapping.findForward("confirmarAnularAprobar");
	}

	private ActionForward accionOrdenar(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) {
		forma.setResultado(Multas.ordenarColumna(forma.getResultado(), forma
				.getUltimaPropiedadOrdenada(), forma.getPropiedadOrdenar()));
		forma.setUltimaPropiedadOrdenada(forma.getPropiedadOrdenar());
		UtilidadBD.closeConnection(connection);
		if (forma.getOpcionRango().equals(ConstantesBD.acronimoSi))
			return mapping.findForward("listarCitasNoRealizadasRango");
		else
			return mapping.findForward("citasNoRealizadasPaciente");
	}

	/**
	 * 
	 * metodo que realiza la acción de guardar la informacion refernete a la
	 * citas no realizadas por rango
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionGuardarRango(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) {
		
		ActionErrors errores = new ActionErrors();
		String mensage="";
		if (UtilidadBD.obtenerValorActualTablaConsecutivos(connection, "consecutivo_facturas_varias", usuario.getCodigoInstitucionInt()).toString().equals(ConstantesBD.codigoNuncaValido+"")) {
			errores.add("Campo Motivo no Atención", new ActionMessage("error.facturasVarias.faltaDefinirConsecutivo"));
			saveErrors(request, errores);
		}
		if(!errores.isEmpty()){
			UtilidadBD.closeConnection(connection);
			return mapping.findForward("citasNoRealizadasRango");
		}	
		
		forma.setMensaje(new ResultadoBoolean(false));
		if (forma.getEstadoActualizarCita().equals("")) {
			for (int i = 0; i < forma.getResultado().size(); i++) {
				if (forma.getResultado().get(i).getChkEstado().equals(
						ConstantesBD.acronimoSi)) {
					if(forma.getResultado().get(i).getCodEstadoCita() != 8)
					{
						i = forma.getResultado().size();
						errores.add("Campo Estado a Actualizar", new ActionMessage(
								"errors.required", "El Estado a Actualizar "));
					}
				}
			}
		}
		if(forma.getConceptoFacturaVaria().equals(ConstantesBD.codigoNuncaValido+""))
		{
			for (int i = 0; i < forma.getResultado().size(); i++) {
				if (forma.getResultado().get(i).getChkFactura().equals(
						ConstantesBD.acronimoSi)) {
					i = forma.getResultado().size();
					errores.add("Campo Concepto Factura Varia", new ActionMessage(
							"errors.required", "El Concepto Factura Varia "));
				}
			}
		}
		if (forma.getEstadoActualizarCita().equals(ConstantesBD.acronimoSi)) {
			if (forma.getMotivoNoAtencion().equals("")) {
				errores.add("Campo Motivo no Atención", new ActionMessage("errors.required", "El Motivo no Atención "));
			}
		}
		
		if (errores.isEmpty()) {
			llenarMundo(forma);
			if (multas.ingresarCitasNoRealizadas(connection, usuario)){ 
				logger.info("\n\n***********************************\nPROCESO EXITOSO\n***********************************\n\n");
				mensage = "Proceso Exitoso.";
			}
			else
				mensage = "Proceso No Exitoso.";
		} else{
			saveErrors(request, errores);
			mensage = "Proceso No Exitoso.";}

		if(forma.getOpcionRango().equals(ConstantesBD.acronimoSi))
		{
			forma.setMensaje(new ResultadoBoolean(true,mensage));
			forma.setResultado(multas.buscarCitasNoRealizadasRango(connection,
					forma.getCriterios(), usuario));
			UtilidadBD.closeConnection(connection);
			return mapping.findForward("listarCitasNoRealizadasRango");
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,mensage));
			forma.setResultado(multas.buscarCitasNoRealizadasPaciente(connection,
					forma.getCriterios(), usuario, paciente));
			UtilidadBD.closeConnection(connection);
			return mapping.findForward("citasNoRealizadasPaciente");
		}
	}

	/**
	 * metodo utilizado para llear las propiedades del mundo
	 * 
	 * @param forma
	 */
	private void llenarMundo(CitasNoRealizadasForm forma) {
		multas.setEstadoActualizarCita(forma.getEstadoActualizarCita());
		multas.setMotivoNoAtencion(forma.getMotivoNoAtencion());
		multas.setCitasNoRealizadas(forma.getResultado());
		multas.setConceptoFacturaVaria(forma.getConceptoFacturaVaria());
		multas.setAproAnulFactVar(forma.isAproAnulFactVar());
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargaDetalleRango(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping) {
		forma.setArrayServicios(multas.buscarDetalleCitasRango(connection,
				usuario, (DtoCitasNoRealizadas) forma.getResultado().get(
						forma.getPosArray())));
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("detalleCitaRango");
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param response
	 * @param mapping 
	 * @param request 
	 * @return
	 */
	private ActionForward accionBuscar(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario,
			HttpServletResponse response, HttpServletRequest request, ActionMapping mapping) {
		if(forma.getCriterios("centAten0").toString().equals(ConstantesBD.codigoNuncaValido+""))
		{
			String string = "";
			for (int i = 0; i < Utilidades.convertirAEntero(forma.getCentrosAtencionAutorizados().get("numRegistros").toString()); i++)
			{
				string += forma.getCentrosAtencionAutorizados().get("codigo_"+i).toString();
				if (i != Utilidades.convertirAEntero(forma.getCentrosAtencionAutorizados().get("numRegistros").toString()) -1)
				{
					string += ",";
				}
			}
			forma.setCriterios("centAten0", string);
		}
		logger.info("uni agen"+forma.getCriterios("UnidadAgen1"));
		if(forma.getCriterios("UnidadAgen1").toString().equals(ConstantesBD.codigoNuncaValido+""))
		{
			String string = "";
			for (int i = 0; i < Utilidades.convertirAEntero(forma.getUnidadesAgenda().get("numRegistros").toString()); i++)
			{
				string += forma.getUnidadesAgenda().get("codigo_"+i).toString();
				if (i != Utilidades.convertirAEntero(forma.getUnidadesAgenda().get("numRegistros").toString()) -1)
				{
					string += ",";
				}
			}
			forma.setCriterios("UnidadAgen1", string);
		}
		forma.setResultado(multas.buscarCitasNoRealizadasRango(connection,
							forma.getCriterios(), usuario));
		if (forma.getResultado().size() > 0) {
			forma.setCentroAtencion(forma.getResultado().get(0)
					.getNombreCentroAtencion());
		}
		cargarConcptFactVarias(connection, forma, usuario);
		
		return mapping.findForward("listarCitasNoRealizadasRango");
		
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping 
	 * @param request 
	 */
	private ActionForward accionEmpezar(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.setInstitucionManejaMulta(ValoresPorDefecto.getInstitucionManejaMultasPorIncumplimiento(usuario.getCodigoInstitucionInt()));
		forma.setTieneRolGenModFactVar(Utilidades.tieneRolFuncionalidad(usuario
				.getLoginUsuario(), 723));
		forma.setTieneRolAproAnulFactVar(Utilidades.tieneRolFuncionalidad(
				usuario.getLoginUsuario(), 736));
		cargarCentrosAtencion(connection, forma, usuario);
		cargarUnidadesAgenda(connection, forma, usuario);
		cargarEstado(connection, forma);
		cargarProfesionales(connection, forma);
		forma.setOpcionRango(ConstantesBD.acronimoSi);
		
		UtilidadBD.closeConnection(connection);
		if (Utilidades.convertirAEntero(forma.getCentrosAtencionAutorizados().get("numRegistros")+"")>0)
			return mapping.findForward("citasNoRealizadasRango");
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("No autorización Actividad", new ActionMessage("errors.usuario.noAutorizadoActividad"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}
	}

	private void cargarConcptFactVarias(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario) {
		forma
				.setArrayConcepFact(multas.setArrayConcepFact(connection,
						usuario));
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 */
	private void cargarProfesionales(Connection connection,
			CitasNoRealizadasForm forma) {
		forma.setProfesionales(UtilidadesAdministracion.obtenerProfesionales(
				connection, ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido, false, true, ConstantesBD.codigoNuncaValido));
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 */
	private void cargarEstado(Connection connection, CitasNoRealizadasForm forma) {
		ArrayList<Integer> codigosEstados = new ArrayList<Integer>();
		codigosEstados.add(1);
		codigosEstados.add(2);
		codigosEstados.add(3);
		codigosEstados.add(8);
		forma.setEstados(UtilidadesConsultaExterna.consultaEstadosCita(
				connection, codigosEstados));
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	private void cargarUnidadesAgenda(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario) {
		
		forma
				.setUnidadesAgenda(UtilidadesConsultaExterna
						.unidadesAgendaXUsuario(
								connection,
								usuario.getLoginUsuario(),
								usuario.getCodigoCentroAtencion(),
								ConstantesBD.codigoActividadAutorizadaCitasNoRealizadas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	private void cargarCentrosAtencion(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario) {
		forma.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(
								connection,
								usuario.getLoginUsuario(),
								ConstantesBD.codigoActividadAutorizadaCitasNoRealizadas));
		forma.setCriterios(forma.getIndicesCriterios()[0], usuario.getCodigoCentroAtencion());
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarUnidadesAgenda(Connection connection,
			CitasNoRealizadasForm forma, UsuarioBasico usuario,
			HttpServletResponse response) {

		// Se consultan las unidades de agenda
		forma.getUnidadesAgenda().clear();
		forma
				.setUnidadesAgenda(UtilidadesConsultaExterna
						.unidadesAgendaXUsuario(
								connection,
								usuario.getLoginUsuario(),
								forma.getIndex(),
								ConstantesBD.codigoActividadAutorizadaCitasNoRealizadas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));

		String resultado = "<respuesta>" + "<infoid>"
				+ "<activar-seleccione>S</activar-seleccione>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>UnidadAgen1</id-select>"
				+ "<id-arreglo>unidadAgenda</id-arreglo>" + // nombre de la
				// etiqueta de cada
				// elemento
				"</infoid>";
		HashMap unidadesAgenda = forma.getUnidadesAgenda();
		for (int i = 0; i < Utilidades.convertirAEntero(unidadesAgenda
				.get("numRegistros")
				+ ""); i++) {
			resultado += "<unidadAgenda>";
			resultado += "<codigo>" + unidadesAgenda.get("codigo_" + i)
					+ "</codigo>";
			resultado += "<descripcion>" + unidadesAgenda.get("nombre_" + i)
					+ "</descripcion>";
			resultado += "</unidadAgenda>";
		}

		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(connection);
		// **********SE GENERA RESPUESTA PARA AJAX EN
		// XML**********************************************
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultado);
		} catch (IOException e) {
			logger
					.error("Error al enviar respuesta AJAX en accionFiltrarUnidadesAgenda: "
							+ e);
		}
		return null;
	}
}