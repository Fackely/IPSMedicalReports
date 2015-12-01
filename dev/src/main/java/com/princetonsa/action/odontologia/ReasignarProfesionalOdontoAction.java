/**
 * 
 */
package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;

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
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.odontologia.ReasignarProfesionalOdontoForm;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoReasignarProfesionalOdonto;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.ReasignarProfesionalOdonto;
import com.princetonsa.mundo.parametrizacion.RegistroUnidadesConsulta;
import com.princetonsa.sort.odontologia.SortGenerico;

/**
 * @author Jairo Andr�s G�mez
 * noviembre 9 / 2009
 *
 */
public class ReasignarProfesionalOdontoAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ReasignarProfesionalOdontoAction.class);

	private ReasignarProfesionalOdonto mundo = new ReasignarProfesionalOdonto();

	/**
	 * M�todo execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Connection connection = null;
		try{
		if (form instanceof ReasignarProfesionalOdontoForm) {

			
			connection = UtilidadBD.abrirConexion();

			// se verifica si la conexion esta nula
			if (connection == null) {
				// de ser asi se envia a una pagina de error.
				request.setAttribute("CodigoDescripcionError", "erros.problemasBd");
				return mapping.findForward("paginaError");
			}

			// se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

			// se obtiene el usuario cargado en sesion.
//			PersonaBasica paciente = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");

			// obtenemos el valor de la forma.
			ReasignarProfesionalOdontoForm forma = (ReasignarProfesionalOdontoForm) form;

			// obtenemos el estado que contiene la forma.
			String estado = forma.getEstado();

			logger.info("\n\n***************************************************************************");
			logger.info("EL ESTADO DE ReasignarProfesionalOdontoForm ES ====>> " + forma.getEstado());
			logger.info("\n***************************************************************************");

			/*
			 * estados
			 */

			if (estado == null) {
				// se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError", "errors.estadoInvalido");
				// se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);

				// se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			} else {
				
				/*
				 * empezar
				 */
				if (estado.equals("empezar")) {
					forma.reset();
					return accionEmpezar(connection, forma, usuario,request,mapping);
				}else if (estado.equals("empezarReasignar")) {
					return accionEmpezarReasignar(connection, forma, usuario,mapping);
				}else if (estado.equals("filtrarUnidadesAgenda")) {
					return accionFiltrarUnidadesAgenda(connection, forma, usuario, response);
				}else if (estado.equals("filtrarProfesionalesSalud")) {
					return accionFiltrarProfesionalesSalud(connection, forma, usuario, response);
				}else if (estado.equals("buscarServiciosReasignar")) {
					forma.setMensaje(new ResultadoBoolean(false));
					return accionBuscarServiciosReasignar(connection, forma, usuario, mapping, request);
				} else if (estado.equals("redireccion")) {
					response.sendRedirect(forma.getLinkSiguiente());
					cierraConexion(connection);
					return null;
				}else if (estado.equals("guardarReasigancion")) {
					return accionReasignarProfesionales(connection, forma, usuario, mapping, request);
				}else if (estado.equals("empezarConsultaLog")) {
					return accionEmpezarConsultarLog(connection, forma, usuario, mapping, request);
				}else if (estado.equals("buscarLog")) {
					return accionBuscarLog(connection, forma, usuario, mapping, request);
				}else if (estado.equals("ordenar")) {
					return accionOrdenar(mapping, forma, usuario, connection);
				}
			}
		}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
	
	private ActionForward accionOrdenar(ActionMapping mapping,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario, Connection connection) {
		
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getArrayResultado(),sortG);
		
		cierraConexion(connection);
		
		if (forma.getEstadoAnterior().equals("buscarLog")){
			forma.setEstado("buscarLog");
			return mapping.findForward("consultaLog");
		}
		else if(forma.getEstadoAnterior().equals("buscarServiciosReasignar")){
			forma.setEstado("buscarServiciosReasignar");
			return mapping.findForward("reasignaProfesional");
		}
		return mapping.findForward("menu");
	}

	private ActionForward accionBuscarLog(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = validaciones(forma, 3);
		if (!errors.isEmpty()){
			saveErrors(request, errors);
			return mapping.findForward("busquedaLog");
		}
		forma.setArrayResultado(mundo.consultarLogReasignacionProfesional(connection, forma.getBusquedaServicios()));
		return mapping.findForward("consultaLog");
	}

	private ActionForward accionEmpezarConsultarLog(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) {

//		se asigna el centro de atenci�n q se encuentra en sesi�n.
		forma.getBusquedaServicios().setCentroAtencionBusqueda(usuario.getCodigoCentroAtencion());
		
//		se consultan los centros de atenci�n validos para el usuario en sesi�n
		forma.setCentrosAtencion(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(
				connection, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional, 
				ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
		
//		se consultan los profesionales de la salud
		forma.setProfesionalesSalud(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), 
				0, false, true, new ArrayList<HashMap<String, Object>>()));
		
//		se consultan los usuarios del sistema
		forma.setUsuariosProceso(UtilidadesAdministracion.obtenerUsuarios(new Usuario(), usuario.getCodigoInstitucionInt(), true));
		
		cierraConexion(connection);
		return mapping.findForward("busquedaLog");
	}

	/**
	 * M�todo utilizado para modificar la tabla de agenda odontologica para cambiar el profesional de
	 * la salud asignado y crear el log en la tabla log_reasignar_prof_odo
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionReasignarProfesionales(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = validaciones(forma, 2);
		if (!errors.isEmpty()){
			saveErrors(request, errors);
			return mapping.findForward("reasignaProfesional");
		}
		logger.info("imopersion busqueda servi....");
		forma.getBusquedaServicios().imprimir();
		logger.info("\n\nimopersion array busq....");
		forma.getArrayResultado().get(0).imprimir();
		DtoInfoFechaUsuario fechaUsuario = new DtoInfoFechaUsuario(usuario.getLoginUsuario());
		mundo.setList(new ArrayList<DtoReasignarProfesionalOdonto>());
		ListIterator<DtoReasignarProfesionalOdonto> iterator = forma.getArrayResultado().listIterator();
		while (iterator.hasNext()){
			DtoReasignarProfesionalOdonto dto = iterator.next();
			if (dto.getRegistroSeleccionado().equals(ConstantesBD.acronimoSi)){
				dto.setFechaProceso(fechaUsuario.getFechaModifica());
				dto.setHoraProceso(fechaUsuario.getHoraModifica());
				dto.setUsuarioProceso(fechaUsuario.getUsuarioModifica());
				dto.setCodigoAgenda(dto.getCodigoPkAgendaOdonto());
				dto.setCodigoMedicoAnterior(forma.getBusquedaServicios().getProfesionalSaludInicial());
				dto.setCodigoMedicoAsignado(forma.getNuevoProfesionalSalud());
				dto.setCentroAtencion(forma.getBusquedaServicios().getCentroAtencionBusqueda());
				dto.setInstitucion(usuario.getCodigoInstitucionInt());
				mundo.getList().add(dto);
			}
		}
		forma.setMensaje(mundo.reasignarProfesionalSalud(connection, fechaUsuario));
		return accionBuscarServiciosReasignar(connection, forma, usuario, mapping, request);
	}

	/**
	 * se consulta la informacion de los servicios a reasignar profesional y los 
	 * posibles profesionales por los que puede ser reemplazado
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionBuscarServiciosReasignar(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = validaciones(forma, 1);
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			return mapping.findForward("busquedaServicios");
		}
		
//		se consultan los servicios disponnibles por los parametros seleccionados
		forma.setArrayResultado(mundo.consultarServiciosAReasignar(connection, forma.getBusquedaServicios()));
		
//		se consultan los profesionales de la salud
		forma.setProfesionalesSalud(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), 
				0, false, true, new ArrayList<HashMap<String, Object>>()));
		cierraConexion(connection);
		return mapping.findForward("reasignaProfesional");
	}

	/**
	 * M�todo que los campos de la busqueda de servicios para cambiar el profesional asignado
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarReasignar(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
//		se asigna el centro de atenci�n q se encuentra en sesi�n.
		forma.getBusquedaServicios().setCentroAtencionBusqueda(usuario.getCodigoCentroAtencion());
		
//		se consultan los centros de atenci�n validos para el usuario en sesi�n
		forma.setCentrosAtencion(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(
				connection, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional, 
				ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
		
//		se consultan las unidades de agenda seg�n el centro de atenci�n.
		forma.setUnidadesAgenda(UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(connection, usuario.getLoginUsuario(), 
				forma.getBusquedaServicios().getCentroAtencionBusqueda(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional, 
				ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, ""));
		
//		se consultan los profesionales de la salud
		forma.setProfesionalesSalud(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), 
				0, false, true, new ArrayList<HashMap<String, Object>>()));
		
		cierraConexion(connection);
		return mapping.findForward("busquedaServicios");
	}

	/**
	 * M�todo que inicializa el forward de la reasignacion de profesionales a servicios odontologicos
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			HttpServletRequest request, ActionMapping mapping){
		cierraConexion(connection);
		return mapping.findForward("menu");
	}
	
	/**
	 * M�todo utilizado para cerrar la conexion y manejar la excepci�n
	 */
	private void cierraConexion(Connection connection){
		try {
			UtilidadBD.cerrarConexion(connection);
		} catch (SQLException e) {
			logger.info("error cerrando la conexi�n");
			e.printStackTrace();
		}
	}
	
	/**
	 * M�todo que carga las unidades de agenda seg�n el centro de atenci�n asincronamente
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarUnidadesAgenda(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			HttpServletResponse response) {

		// Se consultan las unidades de agenda
		forma.getUnidadesAgenda().clear();
		forma.setUnidadesAgenda(UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(connection, usuario.getLoginUsuario(), 
				forma.getIndex(), ConstantesBD.codigoActividadAutorizadaReasignarProfesional, 
				ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, ""));
		
		
		String resultado = "<respuesta>" + "<infoid>"
				+ "<activar-seleccione>S</activar-seleccione>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>unidadAgendaBusqueda</id-select>"
				+ "<id-arreglo>unidadesAgenda</id-arreglo>" + 
				"</infoid>";
//		ArrayList<HashMap> unidadesAgenda = ;
		
		ListIterator<HashMap> iterator = forma.getUnidadesAgenda().listIterator();
		
		while(iterator.hasNext()){
			HashMap unidadAgenda = iterator.next();
			
			if (!(unidadAgenda.get("nombre")+"").equals("Todos Autorizados") && !(unidadAgenda.get("nombre")+"").equals("Seleccione")){
				resultado += "<unidadesAgenda>";
				resultado += "<codigo>" + unidadAgenda.get("codigo")
						+ "</codigo>";
				resultado += "<descripcion>" + unidadAgenda.get("nombre")
						+ "</descripcion>";
				resultado += "</unidadesAgenda>";
			}
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
	
	/**
	 * M�todo que carga los profesionales de la salud seg�n la unidad de agenda asincronamente
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarProfesionalesSalud(Connection connection,
			ReasignarProfesionalOdontoForm forma, UsuarioBasico usuario,
			HttpServletResponse response) {

//		se cargan los servicios de la unidad de agenda
		HashMap<String, Object> serviciosUnidadConsulta = RegistroUnidadesConsulta.obtenerServiciosUnidadConsulta(connection, forma.getIndex()+"", "", usuario.getCodigoInstitucionInt(), false, ConstantesBD.codigoNuncaValido);
		int contadorConsulta = 0, contadorProcedimiento = 0;
//		se verifica si los servicios son de consulta y/o procedimiento
		for(int i = 0; i < Utilidades.convertirAEntero(serviciosUnidadConsulta.get("numRegistros")+""); i++){
			if ((serviciosUnidadConsulta.get("tipoServicio_"+i)+"").equals(ConstantesBD.codigoServicioInterconsulta+""))
				contadorConsulta++;
			else if ((serviciosUnidadConsulta.get("tipoServicio_"+i)+"").equals(ConstantesBD.codigoServicioProcedimiento+""))
				contadorProcedimiento++;
		}
		
//		se consultan los profesionales de la salud
		forma.getProfesionalesSalud().clear();
		
		if (contadorConsulta > 0 && contadorProcedimiento > 0){
			ArrayList<HashMap<String, Object>> centrosCostoUnidadAgenda = UtilidadesConsultaExterna.consultarCentrosCostoXUnidadAgenda(connection, forma.getIndex()+"",usuario.getCodigoCentroAtencion());
			forma.setProfesionalesSalud(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), 
					mundo.consultarEspecialidadUnidadAgenda(connection, forma.getIndex()), false, true, centrosCostoUnidadAgenda));
		}
		else if(contadorConsulta > 0){
			forma.setProfesionalesSalud(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), 
					mundo.consultarEspecialidadUnidadAgenda(connection, forma.getIndex()), false, true, 
					new ArrayList<HashMap<String, Object>>()));
		}
		else if(contadorProcedimiento > 0){
			ArrayList<HashMap<String, Object>> centrosCostoUnidadAgenda = UtilidadesConsultaExterna.consultarCentrosCostoXUnidadAgenda(connection, forma.getIndex()+"",usuario.getCodigoCentroAtencion());
			forma.setProfesionalesSalud(UtilidadesAdministracion.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), 
					0, false, true, centrosCostoUnidadAgenda));
		}
		
		String resultado = "<respuesta>" + "<infoid>"
				+ "<activar-seleccione>S</activar-seleccione>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>profesionalSaludInicial</id-select>"
				+ "<id-arreglo>profesionalesSalud</id-arreglo>" + 
				"</infoid>";
		
		ListIterator<HashMap<String, Object>> iterator = forma.getProfesionalesSalud().listIterator();
		
		while(iterator.hasNext()){
			HashMap<String, Object> profesionalSalud = iterator.next();
			resultado += "<profesionalesSalud>";
			resultado += "<codigo>" + profesionalSalud.get("codigo")
					+ "</codigo>";
			resultado += "<descripcion>" + profesionalSalud.get("nombre")
					+ "</descripcion>";
			resultado += "</profesionalesSalud>";
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
	
	/**
	 * M�todo que realiza las validaciones de cada formulario
	 * @param forma
	 * @param tipoValidacion
	 * @return
	 */
	public ActionErrors validaciones(ReasignarProfesionalOdontoForm forma, int tipoValidacion){
		ActionErrors errors = new ActionErrors();
		boolean siFecha = true;
		switch (tipoValidacion) {
		case 1:
			if (forma.getBusquedaServicios().getFechaInicialBusqueda().equals("")){
				errors.add("", new ActionMessage("errors.required", "La Fecha Inicial"));
				siFecha = false;
			}
			if (forma.getBusquedaServicios().getFechaFinalBusqueda().equals("")){
				errors.add("", new ActionMessage("errors.required", "La Fecha Final"));
				siFecha = false;
			}
			if (forma.getBusquedaServicios().getCentroAtencionBusqueda() == ConstantesBD.codigoNuncaValido)
				errors.add("", new ActionMessage("errors.required", "El Centro de atenci�n"));
			if (forma.getBusquedaServicios().getProfesionalSaludInicial() == ConstantesBD.codigoNuncaValido)
				errors.add("", new ActionMessage("errors.required", "El Profesional de la salud inicial"));
			if(siFecha){
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getBusquedaServicios().getFechaInicialBusqueda(), UtilidadFecha.getFechaActual())){
					errors.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "La fecha inicial", "la fecha actual"));
				}
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getBusquedaServicios().getFechaFinalBusqueda(), forma.getBusquedaServicios().getFechaInicialBusqueda())){
					errors.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "La fecha final", "la fecha inicial"));
				}
			}
			break;

		case 2:
			ListIterator<DtoReasignarProfesionalOdonto> iterator = forma.getArrayResultado().listIterator();
			boolean flag = false;
			while(iterator.hasNext()){
				DtoReasignarProfesionalOdonto dto = iterator.next();
				if(dto.getRegistroSeleccionado().equals(ConstantesBD.acronimoSi)){
					flag = true;
					break;
				}
			}
			if(!flag)
				errors.add("", new ActionMessage("errors.seleccion","registro por lo menos."));
			if (forma.getNuevoProfesionalSalud() == ConstantesBD.codigoNuncaValido)
				errors.add("", new ActionMessage("errors.required", "El Nuevo Profesional de la Salud"));
			break;
			
		case 3:
			if (forma.getBusquedaServicios().getFechaInicialBusqueda().equals("")){
				errors.add("", new ActionMessage("errors.required", "La Fecha Inicial"));
				siFecha = false;
			}
			if (forma.getBusquedaServicios().getFechaFinalBusqueda().equals("")){
				errors.add("", new ActionMessage("errors.required", "La Fecha Final"));
				siFecha = false;
			}
			if (forma.getBusquedaServicios().getCentroAtencionBusqueda() == ConstantesBD.codigoNuncaValido)
				errors.add("", new ActionMessage("errors.required", "El Centro de atenci�n"));
			if(siFecha){
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getBusquedaServicios().getFechaInicialBusqueda(), UtilidadFecha.getFechaActual())){
					errors.add("", new ActionMessage("errors.debeSerMenorIgualGenerico", "La fecha inicial", "la fecha actual"));
				}
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getBusquedaServicios().getFechaFinalBusqueda(), forma.getBusquedaServicios().getFechaInicialBusqueda())){
					errors.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "La fecha final", "la fecha inicial"));
				}
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getBusquedaServicios().getFechaFinalBusqueda(), UtilidadFecha.getFechaActual())){
					errors.add("", new ActionMessage("errors.debeSerMenorIgualGenerico", "La fecha final", "la fecha actual"));
				}
			}
			break;

		default:
			break;
		}
		return errors;
	}
}
