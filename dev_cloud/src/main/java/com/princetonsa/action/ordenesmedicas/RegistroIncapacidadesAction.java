package com.princetonsa.action.ordenesmedicas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.fechas.UtilidadesFecha;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.ibm.icu.util.Calendar;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.RegistroIncapacidadesForm;
import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.RegistroIncapacidades;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.fabrica.ordenes.OrdenesFabricaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosDAO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */
public class RegistroIncapacidadesAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(RegistroIncapacidadesAction.class);

	private RegistroIncapacidades mundo = new RegistroIncapacidades();

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		Connection connection = null;
		try {
			if (form instanceof RegistroIncapacidadesForm) {

				connection = UtilidadBD.abrirConexion();

				if (connection == null) {
					request.setAttribute("CodigoDescripcionError", "erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
				RegistroIncapacidadesForm forma = (RegistroIncapacidadesForm) form;
				String estado = forma.getEstado();

				logger.info("\n\n***************************************************************************");
				logger.info("EL ESTADO DE RegistroIncapacidadesForm ES ====>> " + forma.getEstado());
				logger.info("\n*****************************************************************************");

				forma.setMensaje(new ResultadoBoolean(false));

				/**
				 * estados Registro Incapacidades
				 */
				if (estado == null) {
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar")) 
				{
					forma.reset();
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("menu");
				}
				else if (estado.equals("consultar")) 
				{
					forma.reset();
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("menuConsultar");
				}
				else if (estado.equals("ingresarModificar")) 
				{
					forma.resetDummy();
					return accionIngresarModificar(connection, forma, usuario, mapping, request, paciente);
				}
				else if (estado.equals("ingresar")) 
				{
					return accionIngresar(connection, forma, mapping, request, paciente, usuario);
				}
				else if (estado.equals("modificar")) 
				{
					return accionModificar(connection, forma, mapping, request, paciente, usuario);
				}
				else if (estado.equals("consultarPaciente")) 
				{
					return accionConsultarPaciente(connection, forma, mapping, request, paciente, usuario);
				}
				else if (estado.equals("consultarRango")) 
				{
					return accionConsultarRango(connection, forma, mapping, usuario);
				}
				else if (estado.equals("detallePaciente")) 
				{
					return accionDetallePaciente(connection, forma, mapping, request);
				}
				else if (estado.equals("consultarPacientexRango")) 
				{
					return accionPacientexRango(connection, forma, mapping);
				}
				else if(estado.equals("imprimirReporte"))
				{
					return accionValidarConveniosIngreso(connection, forma, mapping, usuario, request, false);
				}
				else if(estado.equals("imprimirReporteConsulta"))
				{
					return this.accionImprimirReporte(connection, forma, mapping, usuario, request, true);
				}
				else if(estado.equals("seleccionImpresionAnulacion"))
				{
					forma.setRegistroIncapacidad(mundo.consultarIncapacidadPorIngreso(connection, forma.getIngreso()));
					forma.setTiposIncapacidad(mundo.consultarTiposIncapacidad(connection));
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("seleccionImpresionAnulacion");
				}
				else if(estado.equals("anularIncapacidad"))
				{
					logger.info("estado:::"+forma.getEstado());
					logger.info("estadoAnterior:::"+forma.getEstadoAnterior());
					return accionAnularIncapacidad(connection, forma, mapping, request, usuario, paciente);
				}
				else if(estado.equals("confirmarConvenio"))
				{
					UtilidadBD.cerrarConexion(connection);
					logger.info("enter a seleccionConvenio");
					return mapping.findForward("seleccionConvenio");
				}
				else if(estado.equals("seleccionIngreso"))
				{
					return accionSeleccionarIngreso(connection, forma, usuario, mapping, request);
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
	
	private ActionForward accionAnularIncapacidad(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping,
			HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) throws SQLException {
		if(mundo.actualizarEstadoIncapacidad(connection, ConstantesIntegridadDominio.acronimoEstadoAnulado, Integer.valueOf(forma.getIdIncapacidadConsulta()), 
				new DtoInfoFechaUsuario(usuario.getLoginUsuario())).equals(ConstantesBD.acronimoNo)){
			ActionErrors errors = new ActionErrors();
			errors.add("", new ActionMessage("errors.noAnulada"));
			saveErrors(request, errors);
		}
		else{
			forma.setMensaje(new ResultadoBoolean(true,"ANULACIÓN EXITOSA!!!"));
		}
		if (forma.getEstadoAnterior().equals("consultarPaciente"))
			return accionConsultarPaciente(connection, forma, mapping, request, paciente, usuario);
		else
			return accionPacientexRango(connection, forma, mapping);
	}

	private ActionForward accionValidarConveniosIngreso(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request, boolean b) {
		if(forma.getArrayConveniosIngreso().size()>1){
			if (!forma.getRegistroIncapacidad().getConsecutivo().equals("")){
				validarConsecutivoIncapacidades(connection, forma, usuario, false);
				if(forma.getAdvertencia().isTrue()){
					return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "No se cuenta con parametrización de " +
							"consecutivos de Incapacidades. Debe generar el consecutivo para poder imprimir la incapacidad", "No se " +
									"cuenta con parametrización de consecutivos de Incapacidades. Debe generar el consecutivo para poder " +
									"imprimir la incapacidad", false);
				}
			}
		}
		return this.accionImprimirReporte(connection, forma, mapping, usuario, request, false);
	}

	private ActionForward accionPacientexRango(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping) {
		forma.setArrayListDtoIncapacidad(mundo.consultarIncapacidadesPaciente(connection, forma.getPacienteSeleccionadoRango()));
		return mapping.findForward("consultaIncapacidadesPaciente");
	}

	private ActionForward accionDetallePaciente(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping, HttpServletRequest request) throws SQLException {
		
		ActionErrors errors = validarBusqueda(forma);
		
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			return mapping.findForward("busquedaIncapacidades");
		}
		
		for(int i = 0; i < Utilidades.convertirAEntero(forma.getCentrosAtencion().get("numRegistros")+"");i++){
			if((forma.getCentrosAtencion().get("consecutivo_"+i)+"").equals(forma.getSelBusqueda().getCodCentroAtencion()+"")){
				forma.getSelBusqueda().setCentroAtencion(forma.getCentrosAtencion().get("descripcion_"+i)+"");
				break;
			}
		}
		
		for(int i = 0; i < Utilidades.convertirAEntero(forma.getViasIngreso().get("numRegistros")+"");i++){
			if((forma.getViasIngreso().get("codigo_"+i)+"").equals(forma.getSelBusqueda().getViaIngreso()+"")){
				forma.getSelBusqueda().setNomViaIngreso(forma.getViasIngreso().get("nombre_"+i)+"");
				break;
			}
		}
		
		for(int i = 0; i < forma.getConvenios().size(); i++){
			int flag = Utilidades.convertirAEntero(((HashMap)forma.getConvenios().get(i)).get("codigoConvenio")+"");
			if(flag == forma.getSelBusqueda().getConvenio()){
				forma.getSelBusqueda().setNomConvenio((((HashMap)forma.getConvenios().get(i)).get("nombreConvenio"))+"");
				break;
			}		
		}
		
		forma.setArraySelBusqueda(mundo.consultarPacientesIncapacidad(connection, forma.getSelBusqueda()));
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("resultadoPacientes");
	}

	private ActionErrors validarBusqueda(RegistroIncapacidadesForm forma) {
		ActionErrors errors = new ActionErrors();
		boolean flagFechas = true;
		if(forma.getSelBusqueda().getCodCentroAtencion() == ConstantesBD.codigoNuncaValido){
			errors.add("Error Busqueda", new ActionMessage("errors.required", "El Centro de Atención"));
		}
		if(forma.getSelBusqueda().getFechaInicialGeneracion().equals("")){
			flagFechas = false;
			errors.add("Error Busqueda", new ActionMessage("errors.required", "La Fecha Inicial Generación"));
		}
		if(forma.getSelBusqueda().getFechaFinalGeneracion().equals("")){
			flagFechas = false;
			errors.add("Error Busqueda", new ActionMessage("errors.required", "La Fecha Final Generación"));
		}
		if (flagFechas){
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getSelBusqueda().getFechaFinalGeneracion(), UtilidadFecha.getFechaActual())){
				errors.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Final Generación","Actual"));
			}
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getSelBusqueda().getFechaInicialGeneracion(), UtilidadFecha.getFechaActual())){
				errors.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial Generación","Actual"));
			}
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getSelBusqueda().getFechaFinalGeneracion(), forma.getSelBusqueda().getFechaInicialGeneracion())){
				errors.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Final Generación", "Inicial Generación"));
			}
		}
		return errors;
	}
	
	private ActionForward accionConsultarRango(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping,
			UsuarioBasico usuario) throws SQLException {
		
		forma.getSelBusqueda().setCodCentroAtencion(usuario.getCodigoCentroAtencion());
		forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		forma.setViasIngreso((HashMap)Utilidades.obtenerViasIngreso(connection,false));
		forma.setConvenios(Utilidades.obtenerConvenios(connection, "", "", false, "", true));
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("busquedaIncapacidades");
	}

	private ActionForward accionConsultarPaciente(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente,
			UsuarioBasico usuario) throws SQLException 
	{
		/*
		 * Validación que se encuentre cargado un paciente
		 */
		String forward = "";
		forward = accionVerficarPaciente(paciente);
		
		if(!forward.equals(""))
			return ComunAction.accionSalirCasoError(mapping, request, connection,logger, forward,forward, true);
		
		forma.setArrayListDtoIncapacidad(mundo.consultarIncapacidadesPaciente(connection, paciente.getCodigoPersona()));
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("consultaIncapacidadesPaciente");
	}

	private ActionForward accionModificar(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente,			UsuarioBasico usuario) throws SQLException 
	{
		boolean transaccion = UtilidadBD.iniciarTransaccion(connection);
		if (transaccion)
		{
			ActionErrors errors = validacionCamposInsertarModificar(forma,paciente);
			if(!errors.isEmpty()){
				saveErrors(request, errors);
				return mapping.findForward("ingresarModificar");
			}
			DtoInfoFechaUsuario dtoInfoFechaUsuario = new DtoInfoFechaUsuario(usuario.getLoginUsuario());
			
			validarConsecutivoIncapacidades(connection, forma, usuario, false);
			
			mundo.setDtoRegistroIncapacidades(forma.getRegistroIncapacidad());
			
			if (mundo.insertarLogIncapacidad(connection, forma.getLogRegistroIncapacidad(), dtoInfoFechaUsuario).equals(ConstantesBD.acronimoNo))
			{
				UtilidadBD.abortarTransaccion(connection);
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(connection, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), forma.getRegistroIncapacidad().getConsecutivo(),ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				forma.setMensaje(new ResultadoBoolean(true, "PROCESO NO EXITOSO POR FAVOR VERIFIQUE."));
			}
			else
			{
				if (mundo.actualizarIncapacidad(connection, dtoInfoFechaUsuario).equals(ConstantesBD.acronimoNo))
				{
					UtilidadBD.abortarTransaccion(connection);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(connection, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), forma.getRegistroIncapacidad().getConsecutivo(),ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					forma.setMensaje(new ResultadoBoolean(true, "PROCESO NO EXITOSO POR FAVOR VERIFIQUE."));
					
				}
				else
				{
					if(forma.getEsDummy().equals(ConstantesBD.acronimoSi))
						forma.setCerrarPopUpDummy(ConstantesBD.acronimoSi);
					forma.setMensaje(new ResultadoBoolean(true, "PROCESO EXITOSO."));
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(connection, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), forma.getRegistroIncapacidad().getConsecutivo(),ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					UtilidadBD.finalizarTransaccion(connection);
				}
					
			}
		
		}
		forma.setArrayConveniosIngreso(mundo.consultaConveniosIngreso(connection, forma.getRegistroIncapacidad().getCodigoPk()));
		logger.info("tamanio array convenios:::"+forma.getArrayConveniosIngreso().size());
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("ingresarModificar");
	}

	private ActionErrors validacionCamposInsertarModificar(
			RegistroIncapacidadesForm forma, PersonaBasica paciente) {
		ActionErrors errors = new ActionErrors();
		if(forma.isSeleccionarDiagnostico()){
			if (forma.getDiagnosticoSeleccionado() == null || forma.getDiagnosticoSeleccionado().trim().isEmpty()){
				errors.add("", new ActionMessage("errors.required", "El Diagnóstico"));
			}
		}
		if (Utilidades.convertirAEntero(forma.getRegistroIncapacidad().getDiasIncapacidad())<1){
			errors.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "El número de días de incapacidad","1"));
		}
		if(forma.getRegistroIncapacidad().getFechaInicioIncapacidad().equals("")){
			errors.add("Error Busqueda", new ActionMessage("errors.required", "La Fecha Inicio Incapacidad"));
		}
		else{
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getRegistroIncapacidad().getFechaInicioIncapacidad(), UtilidadFecha.getFechaActual())){
				if(forma.getRegistroIncapacidad().getProrroga().equals(ConstantesBD.acronimoNo)){
					errors.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicio Incapacidad","Actual"));
				}
				else{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getLogRegistroIncapacidad().getFechaFinIncapacidad(), UtilidadFecha.getFechaActual())){
						if(UtilidadFecha.numeroDiasEntreFechas(forma.getLogRegistroIncapacidad().getFechaFinIncapacidad(),forma.getRegistroIncapacidad().getFechaInicioIncapacidad())>1){
							errors.add("", new ActionMessage("prompt.generico", "Siendo prorroga la fecha inicial máximo debe ser mayor 1 día a la fecha final de la Incapacidad vigente."));
						}
					}
					
				}
			}
			String fechaIngreso=paciente.getFechaIngreso();
			if(forma.getIngresoSeleccionado() != null && !forma.getIngresoSeleccionado().isEmpty()){
				for(DtoIngresos ingreso:forma.getIngresosPaciente()){
					if(ingreso.getIngreso()==Integer.valueOf(forma.getIngresoSeleccionado())){
						fechaIngreso=ingreso.getFechaIngreso();
						break;
					}
				}
			}
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getRegistroIncapacidad().getFechaInicioIncapacidad(),fechaIngreso)){
				errors.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicio Incapacidad","Ingreso"));
			}
			int tmp =UtilidadFecha.numeroDiasEntreFechas(forma.getRegistroIncapacidad().getFechaInicioIncapacidad(),UtilidadFecha.getFechaActual());
			if ( Utilidades.convertirAEntero(forma.getRegistroIncapacidad().getDiasIncapacidad()) < tmp){
				errors.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "El número de días de incapacidad","la diferencia entre la fecha de ingreso y la fecha actual"));
			}
		}
		if(forma.getRegistroIncapacidad().getProrroga().equals(ConstantesBD.codigoNuncaValido+"")){
			errors.add("", new ActionMessage("errors.required", "La Prorroga"));
		}
		if(forma.getRegistroIncapacidad().getTipoIncapacidad() == ConstantesBD.codigoNuncaValido){
			errors.add("", new ActionMessage("errors.required", "El Tipo de Incapacidad"));
		}
		
		return errors;
	}

	private ActionForward accionIngresar(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente, 
			UsuarioBasico usuario) throws SQLException 
	{
		ActionErrors errors = validacionCamposInsertarModificar(forma, paciente);
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			return mapping.findForward("ingresarModificar");
		}
		
		DtoInfoFechaUsuario dtoInfoFechaUsuario = new DtoInfoFechaUsuario(usuario.getLoginUsuario());
		
		validarConsecutivoIncapacidades(connection, forma, usuario, false);
		int codigoIngreso=paciente.getCodigoIngreso();
		if(forma.getIngresoSeleccionado() != null && !forma.getIngresoSeleccionado().isEmpty()){
			codigoIngreso=Integer.valueOf(forma.getIngresoSeleccionado());
		}
		
		mundo.setDtoRegistroIncapacidades(forma.getRegistroIncapacidad());
		mundo.getDtoRegistroIncapacidades().setCodigoPaciente(paciente.getCodigoPersona());
		mundo.getDtoRegistroIncapacidades().setIngreso(codigoIngreso);
		mundo.getDtoRegistroIncapacidades().setCodigoMedico(usuario.getCodigoPersona());
		if (forma.getEspecialidadDummy() == null)
			forma.setEspecialidadDummy("");
		if(forma.getEsDummy().equals(ConstantesBD.acronimoSi)){
			mundo.getDtoRegistroIncapacidades().setEspecialidad(Utilidades.convertirAEntero(forma.getEspecialidadDummy()));
		}
		else{
			mundo.getDtoRegistroIncapacidades().setEspecialidad(Integer.valueOf(forma.getEspecialidadProfesional()));
		}
		
		mundo.getDtoRegistroIncapacidades().setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
		if(forma.isSeleccionarDiagnostico()){
			String[] diag=forma.getDiagnosticoSeleccionado().split(ConstantesBD.separadorSplit);
			forma.setAcronimoDx(diag[0]);
			forma.setTipoCie(Integer.valueOf(diag[1]));
		}
		mundo.getDtoRegistroIncapacidades().setAcronimoDiagnostico(forma.getAcronimoDx());
		mundo.getDtoRegistroIncapacidades().setTipoCie(forma.getTipoCie());
		UtilidadBD.iniciarTransaccion(connection);
		if(mundo.insertarIncapacidad(connection, dtoInfoFechaUsuario).equals(ConstantesBD.acronimoSi))
		{
			if(forma.getEsDummy().equals(ConstantesBD.acronimoSi))
				forma.setCerrarPopUpDummy(ConstantesBD.acronimoSi);
			forma.setMensaje(new ResultadoBoolean(true, "PROCESO EXITOSO."));
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(connection, ConstantesBD.nombreConsecutivoIncapacidades,usuario.getCodigoInstitucionInt(), forma.getRegistroIncapacidad().getConsecutivo(),ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			UtilidadBD.finalizarTransaccion(connection);
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(connection, ConstantesBD.nombreConsecutivoIncapacidades,usuario.getCodigoInstitucionInt(), forma.getRegistroIncapacidad().getConsecutivo(),ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			forma.setMensaje(new ResultadoBoolean(true, "PROCESO NO EXITOSO POR FAVOR VERIFIQUE."));
		}
		DtoRegistroIncapacidades dtoRegistroIncapacidades = mundo.consultarIncapacidadPorIngreso(connection, codigoIngreso);
		forma.setArrayConveniosIngreso(mundo.consultaConveniosIngreso(connection, dtoRegistroIncapacidades.getCodigoPk()));
		logger.info("tamanio array convenios ingresa:::"+forma.getArrayConveniosIngreso().size());
		forma.setEstado("modificar");
		forma.setSeleccionarEspecialidad(false);
		forma.setSeleccionarDiagnostico(false);
		forma.setDescripcionDx(dtoRegistroIncapacidades.getDiagnostico());
		forma.getRegistroIncapacidad().setCodigoPk(dtoRegistroIncapacidades.getCodigoPk());
		forma.setLogRegistroIncapacidad(new DtoRegistroIncapacidades(forma.getRegistroIncapacidad()));
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("ingresarModificar");
	}

	/*
	 * método que inicia la funcionalidad de ingresar/modificar
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionIngresarModificar(Connection connection,
				RegistroIncapacidadesForm forma, UsuarioBasico usuario,
				ActionMapping mapping, HttpServletRequest request,
				PersonaBasica paciente) throws SQLException 
	{
		logger.info("codigo ingreso: "+paciente.getCodigoIngreso());
		logger.info("codigo cuenta: "+paciente.getCodigoCuenta());
//		Validación si es profesional de la salud
		if (!UtilidadValidacion.esProfesionalSalud(usuario))
			return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "errors.noProfesionalSalud", "errors.noProfesionalSalud", true);
		
//		Validación Paciente

		RespuestaValidacion validacion = new RespuestaValidacion("",true);
		boolean solicitudFacturada = false;
		
		
		logger.info("numSOlicitud;;:::"+forma.getSolicitudDummy());
		if (forma.getSolicitudDummy() != null && !forma.getSolicitudDummy().equals(""))
		{
			
			solicitudFacturada = mundo.verificaSolicitudFacturada(connection, Utilidades.convertirAEntero(forma.getSolicitudDummy())).equals(ConstantesBD.acronimoSi);
			
			
		}
		
		logger.info("es solicitud facutrada ? "+solicitudFacturada);
		if(!solicitudFacturada)
		{
			//Se valida si la funcionalidad es llamada desde Valoraciones, Evoluciones, Atención Citas
			if(forma.getEsDummy().equals(ConstantesBD.acronimoSi)){
				validacion = UtilidadValidacion.esValidoPacienteCargado(connection, paciente);
			}
			else{
				// Validar Paciente Cargado
				if(paciente.getCodigoPersona()<1){
				    validacion=new RespuestaValidacion("errors.paciente.noCargado",false);
				}
				if(validacion.puedoSeguir){
					if(UtilidadValidacion.esCuentaValida(connection, paciente.getCodigoCuenta())<1){
						List<DtoIngresos> ingresos = new ArrayList<DtoIngresos>();
						//Según la via de ingreso se deben listar los ingresos de las últimas 24 horas
						//TODO Obtener los ingresos creados para el paciente, cuya fecha/hora  de egreso administrativo (boleta de salida) 
						//este dentro de las últimas 24 horas. (Se debe implementar una vez se desarrolle el tema de boleta de salida)
						
						//Para pacientes con Vía de Ingreso = CONSULTA EXTERNA, se deben listar los ingresos cuya fecha de atención 
						//de las citas se encuentre dentro de las últimas 24 horas.  
						try{
							HibernateUtil.beginTransaction();
							IIngresosDAO ingresosDAO= ManejoPacienteDAOFabrica.crearinIngresosDAO();
							Calendar fechaActual=Calendar.getInstance();
							String horaActual=fechaActual.get(Calendar.HOUR_OF_DAY)+":"+fechaActual.get(Calendar.MINUTE);
							fechaActual.add(Calendar.DATE, -1);
							
							ingresos.addAll(ingresosDAO.consultarIngresosConsultaExternaPorFechaHoraEstadoCita(paciente.getCodigoPersona(),
													fechaActual.getTime(), horaActual, ConstantesBD.codigoEstadoCitaAtendida));
							HibernateUtil.endTransaction();
						}
						catch (Exception e) {
							HibernateUtil.abortTransaction();
							Log4JManager.error("ERROR ", e);
						}
						if(ingresos.isEmpty()){
							validacion=new RespuestaValidacion("errors.paciente.noEgresosUltimoDia",false);
						}
						else{
							for(DtoIngresos dtoIngreso: ingresos){
								DtoRegistroIncapacidades dtoRegistroIncapacidades = mundo.consultarIncapacidadPorIngreso(connection, dtoIngreso.getIngreso());
								if(dtoRegistroIncapacidades.getEstado().equals("NADA")){
									dtoIngreso.setTieneRegistroIncapacidad(false);
								}
								else{
									dtoIngreso.setTieneRegistroIncapacidad(true);
								}
							}
							forma.setIngresosPaciente(ingresos);
							forma.setMostrarIngresos(true);
							return mapping.findForward("ingresarModificar");
						}
					}
				}
			}
		}
		
		if (!validacion.puedoSeguir)
			return ComunAction.accionSalirCasoError(mapping, request, connection, logger, validacion.textoRespuesta, validacion.textoRespuesta, true);
		
//		se valida para vias de ingreso urgencias y hospitalizacion que el paciente cuente con un egreso
		if (forma.getEsDummy().equals(ConstantesBD.acronimoNo)){
			if (paciente.getCodigoUltimaViaIngreso() == (ConstantesBD.codigoViaIngresoUrgencias) || paciente.getCodigoUltimaViaIngreso() == (ConstantesBD.codigoViaIngresoHospitalizacion)){
				if(!UtilidadValidacion.existeEgresoCompleto(connection, paciente.getCodigoCuenta())){
					return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "error.facturacion.pacienteSinSalidaoEgreso", "error.facturacion.pacienteSinSalidaoEgreso", true);
				}
			}
		}
		
//		Validación Ingreso
		if (!solicitudFacturada)
		{
			if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(connection, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
				return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "errors.paciente.noIngreso", "errors.paciente.noIngreso", true);
		}
//		obtener tipos incapacidad
		forma.setTiposIncapacidad(mundo.consultarTiposIncapacidad(connection));
//		obtener incapacidad sí existe
		DtoRegistroIncapacidades dtoRegistroIncapacidades = mundo.consultarIncapacidadPorIngreso(connection, paciente.getCodigoIngreso());
//		obtiene los convenios del ingreso
		forma.setArrayConveniosIngreso(mundo.consultaConveniosIngreso(connection, dtoRegistroIncapacidades.getCodigoPk()));
		
//		se obtiene el diagnostico
		int solicitud;
		solicitud = UtilidadesHistoriaClinica.consultarUltimaEvolucionIngreso(connection, paciente.getCodigoIngreso());
		if (solicitud == 0){
			solicitud = UtilidadesHistoriaClinica.consultarUltimaValoracionIngreso(connection, paciente.getCodigoIngreso());
			if(solicitud == 0 && paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoConsultaExterna){
				try{
					HibernateUtil.beginTransaction();
					IIngresosDAO ingresosDAO= ManejoPacienteDAOFabrica.crearinIngresosDAO();
					InfoDatos datos=ingresosDAO.consultarUltimoDiagnosticoRespuestaSolicitudes(paciente.getCodigoIngreso(),
													ConstantesBD.codigoTipoSolicitudProcedimiento);
					if(datos != null){
						forma.setAcronimoDx(datos.getDescripcion());
						forma.setTipoCie(datos.getCodigo());
						forma.setDescripcionDx(datos.getDescripcionInd());
						forma.setSeleccionarDiagnostico(false);
					}
					else{
						forma.setSeleccionarDiagnostico(true);
					}
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					HibernateUtil.abortTransaction();
					Log4JManager.error("ERROR ", e);
				}
			}
			else{
				InfoDatos datos = mundo.consultarDiagnosticoValoraciones(connection, solicitud);
				if(datos.getDescripcion() != null && !datos.getDescripcion().isEmpty()
						&& datos.getCodigo() > 0){
					forma.setAcronimoDx(datos.getDescripcion());
					forma.setTipoCie(datos.getCodigo());
					forma.setDescripcionDx(datos.getDescripcionInd());
					forma.setSeleccionarDiagnostico(false);
				}
				else{
					forma.setSeleccionarDiagnostico(true);
				}
			}
		}
		else{
			InfoDatos datos = mundo.consultarDiagnosticoEvoluciones(connection, solicitud);
			if(datos.getDescripcion() != null && !datos.getDescripcion().isEmpty()
					&& datos.getCodigo() > 0){
				forma.setAcronimoDx(datos.getDescripcion());
				forma.setTipoCie(datos.getCodigo());
				forma.setDescripcionDx(datos.getDescripcionInd());
				forma.setSeleccionarDiagnostico(false);
			}
			else{
				forma.setSeleccionarDiagnostico(true);
			}
		}
		
		/*
		 * se valida si es dummy para colocar el campo activo en N.
		 */
		if (forma.getEsDummy().equals(ConstantesBD.acronimoSi)){
			forma.getRegistroIncapacidad().setActivo(ConstantesBD.acronimoNo);
		}
		
//		se evalua si cargo incapacidad o la modifico
		if(dtoRegistroIncapacidades.getEstado().equals("NADA"))
		{
			forma.setEstado("ingresar");
			
			/*
			 * se valida si es dummy para colocar el campo activo en N para indicar que 
			 * fue relizada desde fuera de la funcionalidad.
			 */
			if (forma.getEsDummy().equals(ConstantesBD.acronimoSi)){
				forma.getRegistroIncapacidad().setActivo(ConstantesBD.acronimoNo);
				forma.setSeleccionarEspecialidad(false);
			}
			else{
				forma.setSeleccionarEspecialidad(true);
				forma.setEspecialidadesProfesional(usuario.getEspecialidades1());
			}
			
			if (paciente.getCodigoUltimaViaIngreso() == (ConstantesBD.codigoViaIngresoUrgencias) || paciente.getCodigoUltimaViaIngreso() == (ConstantesBD.codigoViaIngresoHospitalizacion)){
				forma.getRegistroIncapacidad().setFechaInicioIncapacidad(paciente.getFechaIngreso());
			}
			
			String result = accionValidacionesIngresar(connection, forma, usuario, paciente,solicitudFacturada);
			if(!result.equals(""))
				return ComunAction.accionSalirCasoError(mapping, request, connection, logger, result, result, true);
		}
		else
		{
			forma.setEstado("modificar");
			forma.setSeleccionarEspecialidad(false);
			forma.setRegistroIncapacidad(new DtoRegistroIncapacidades(dtoRegistroIncapacidades));
			forma.setDescripcionDx(dtoRegistroIncapacidades.getDiagnostico());
			forma.setLogRegistroIncapacidad(new DtoRegistroIncapacidades(dtoRegistroIncapacidades));
			
			/*
			 * se valida si es dummy para colocar el campo activo en C para
			 * indicar que se realizaron cambios.
			 */
			if (forma.getEsDummy().equals(ConstantesBD.acronimoSi)){
				forma.getRegistroIncapacidad().setActivo(ConstantesBD.acronimoCambio);
			}
			
			String result = accionValidacionesModificar(connection, forma, usuario, request);
			if(!result.equals("")){
				request.setAttribute("codigoDescripcionError",result);
				if (forma.getEsDummy().equals(ConstantesBD.acronimoSi))
					request.setAttribute("ocultarEncabezadoErrores",true);
				UtilidadBD.cerrarConexion(connection);
				
				return mapping.findForward("paginaError");
			}
			
//			valida las fechas de ingreso porque esto puede validar la incapacidad
			if(validaEgreso(connection, forma.getRegistroIncapacidad().getGrabacion(), paciente.getCodigoCuenta()))
			{
				if(mundo.actualizarEstadoIncapacidad(connection, ConstantesIntegridadDominio.acronimoEstadoAnulado, forma.getRegistroIncapacidad().getCodigoPk(), new DtoInfoFechaUsuario(usuario.getLoginUsuario())).equals(ConstantesBD.acronimoNo))
					return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "errors.problemasBd", "errors.problemasBd", true);
				return accionIngresarModificar(connection, forma, usuario, mapping, request, paciente);
			}
		}
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("ingresarModificar");
	}

	private boolean validaEgreso(Connection connection,
			DtoInfoFechaUsuario dtoInfoFechaUsuario, int codigoCuenta) 
	{
		if(mundo.egresoPosteriorConReversion(connection, dtoInfoFechaUsuario, codigoCuenta)>0)
		{
			return true;
		}
		return false;
	}

	private String accionValidacionesIngresar(Connection connection,
			RegistroIncapacidadesForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, boolean solicitudFacturada) 
	{
		/*
		 * validación si la cuenta tiene estados validos...
		 */
		if(!solicitudFacturada)
		{
			HojaAnestesia hojaAnestesia = new HojaAnestesia();
			if(!hojaAnestesia.validacionEsCuentaValida(connection, paciente))
				return "errors.paciente.cuentaNoActiva";
		}
		
		if(forma.getEsDummy().equals(ConstantesBD.acronimoNo)){
			/*
			 * Validación si el paciente ha tenido valoraciones
			 */
			try {
				if (paciente.getCodigoUltimaViaIngreso() == (ConstantesBD.codigoViaIngresoUrgencias) || paciente.getCodigoUltimaViaIngreso() == (ConstantesBD.codigoViaIngresoHospitalizacion)){
					if(!UtilidadValidacion.tieneValoraciones(connection, paciente.getCodigoCuenta())){
						return "errors.noHayValoracionInicial";
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Validacion si esta definido el consecutivo de incapaciadades
		 */
		validarConsecutivoIncapacidades(connection, forma, usuario, true);
		
		return "";
	}
	
	private String accionValidacionesModificar(Connection connection,
			RegistroIncapacidadesForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		/*
		 * valido si es dummy y la especialidad enviada al generar el egreso es la misma de la incapacidad ya generada
		 */
		if (forma.getEsDummy().equals(ConstantesBD.acronimoSi)){
			logger.info("forma.getRegistroIncapacidad().getEspecialidad();;;::"+forma.getRegistroIncapacidad().getEspecialidad());
			logger.info("forma.getEspecialidadDummy();;;::"+forma.getEspecialidadDummy());
			if(forma.getRegistroIncapacidad().getEspecialidad() != Utilidades.convertirAEntero(forma.getEspecialidadDummy())){
				ActionErrors errors = new ActionErrors();
				return "errors.noSeleccionoMismaEspecialidadQueIncapacidad";
			}
		}
		
		/*
		 * se valida si se tiene el mismo diagnostico que antes
		 */
		logger.info("forma.getRegistroIncapacidad().getTipoCie()::::"+forma.getRegistroIncapacidad().getTipoCie());
		logger.info("forma.getTipoCie()::::"+forma.getTipoCie());
		logger.info("forma.getRegistroIncapacidad().getAcronimoDiagnostico()::::"+forma.getRegistroIncapacidad().getAcronimoDiagnostico());
		logger.info("forma.getAcronimoDx()::::"+forma.getAcronimoDx());
		if(forma.getRegistroIncapacidad().getProrroga().equals(ConstantesBD.acronimoSi)){
			if(forma.getTipoCie()!=forma.getRegistroIncapacidad().getTipoCie() || !forma.getAcronimoDx().equals(forma.getRegistroIncapacidad().getAcronimoDiagnostico())){
				return "error.ordenmedica.diagnosticoDistinto";
			}
		}
		
		/*
		 * Validacion si esta definido el consecutivo de incapaciadades
		 */
		if(Utilidades.convertirAEntero(forma.getRegistroIncapacidad().getConsecutivo()) < 0)
		{
			validarConsecutivoIncapacidades(connection, forma, usuario, true);
			
		}
		
		if(forma.getEsDummy().equals(ConstantesBD.acronimoNo)){
			/*
			 * Valisdacion si profesional de la salud tiene la misma especialidad que la incapacidad 
			 */
			String[] especilidadesMedico = usuario.getCodEspecialidadesMedico().split(",");
			boolean flag = false;
			for (int i=0; i< especilidadesMedico.length; i++)
			{
				if (forma.getRegistroIncapacidad().getEspecialidad() == Utilidades.convertirAEntero(especilidadesMedico[i]))
				{
					flag = true;
					break;
				}
			}
			if (!flag)
				return "errors.noTieneMismaEspecialidadQueIncapacidad";
		}
		
		return "";
	}

	private void validarConsecutivoIncapacidades(Connection con, RegistroIncapacidadesForm forma, UsuarioBasico usuario, boolean eliminar) 
	{
		forma.getRegistroIncapacidad().setConsecutivo(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIncapacidades, usuario.getCodigoInstitucionInt()));
		forma.setValorConsecutivoIngreso(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIncapacidades, usuario.getCodigoInstitucionInt()));
		if(Utilidades.convertirAEntero(forma.getRegistroIncapacidad().getConsecutivo())<0)
			forma.setAdvertencia(new ResultadoBoolean(true, "No se cuenta con parametrización de consecutivos de Incapacidades. La incapacidad quedara guardada pero no se generará hasta que este no se defina el consecutivo"));
		else
			forma.setAnioConsecutivoIngreso(UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoIncapacidades, usuario.getCodigoInstitucionInt(),forma.getRegistroIncapacidad().getConsecutivo()));

		if (eliminar){
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), forma.getValorConsecutivoIngreso(),ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		}
	}
	
	private String accionVerficarPaciente(PersonaBasica paciente) {
		if (paciente == null || paciente.getCodigoPersona() <= 0) {
			return "errors.paciente.noCargado";
		}
		return "";
	}
	
	
	private ActionForward accionImprimirReporte(Connection connection,
			RegistroIncapacidadesForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request, boolean esConsulta) {
		logger.info("forma.getPrioridadSeleccionada():::::"+forma.getPrioridadSeleccionada());
		int flagCodigo = 0;
		int flagPrioridad = 0;
		if(esConsulta){
			flagCodigo = Integer.valueOf(forma.getIdIncapacidadConsulta());
			flagPrioridad = 1;
		}
		else{
			flagCodigo = forma.getRegistroIncapacidad().getCodigoPk();
			flagPrioridad = forma.getPrioridadSeleccionada();
			ActionErrors errors = new ActionErrors();
			if (flagCodigo<1){
				errors.add("Error incapacidad no Guardada", new ActionMessage("errors.incapacidadNoGuardada"));
				saveErrors(request, errors);
				if(forma.getRegistroIncapacidad().getEstado() == null || 
						forma.getRegistroIncapacidad().getEstado().isEmpty() || 
						forma.getRegistroIncapacidad().getEstado().equals("NADA"))
				{
					forma.setEstado("ingresar");
				}
				return mapping.findForward("ingresarModificar");
			}
		}
		logger.info("priorida:::::::"+flagPrioridad);
		logger.info("codigo:::::::"+flagCodigo);
		
		String nombreRptDesign = "registroIncapacidades.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"ordenes/",nombreRptDesign);
		
		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
		
		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
		v=new Vector();
		v.add(""+ins.getRazonSocial());
		v.add("NIT: "+ins.getNit()+"\n"+ins.getDireccion()+"\nTels: "+ins.getTelefono());
		comp.insertLabelInGridOfMasterPage(0,1,v);
		
		// Parametros de Generación
		comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
		v=new Vector();
		
		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
		
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport+"");
			
		if(!newPathReport.equals("")){
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport+"&codigoPK="+flagCodigo+"&prioridad="+flagPrioridad);
		}

		UtilidadBD.closeConnection(connection);
		if(esConsulta)
			return mapping.findForward("consultaIncapacidadesPaciente");
		forma.setEstado(forma.getEstadoAnterior());
		return mapping.findForward("ingresarModificar");
	}
	
	private ActionForward accionSeleccionarIngreso(Connection connection,
			RegistroIncapacidadesForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) throws SQLException 
	{
		
		RespuestaValidacion validacion = new RespuestaValidacion("",true);
		boolean solicitudFacturada = false;
		int codigoIngresoSeleccionado=Integer.valueOf(forma.getIngresoSeleccionado());
		DtoIngresos dtoIngresoSeleccionado=new DtoIngresos();
		for(DtoIngresos dtoIngreso:forma.getIngresosPaciente()){
			if(dtoIngreso.getIngreso()==codigoIngresoSeleccionado){
				dtoIngresoSeleccionado=dtoIngreso;
				break;
			}
		}
		
		//	obtener tipos incapacidad
		forma.setTiposIncapacidad(mundo.consultarTiposIncapacidad(connection));
		//	obtener incapacidad sí existe
		DtoRegistroIncapacidades dtoRegistroIncapacidades = mundo.consultarIncapacidadPorIngreso(connection, codigoIngresoSeleccionado);
		//	obtiene los convenios del ingreso
		forma.setArrayConveniosIngreso(mundo.consultaConveniosIngreso(connection, dtoRegistroIncapacidades.getCodigoPk()));
		
		//	se obtiene el diagnostico
		int solicitud;
		solicitud = UtilidadesHistoriaClinica.consultarUltimaEvolucionIngreso(connection, codigoIngresoSeleccionado);
		if (solicitud == 0){
			solicitud = UtilidadesHistoriaClinica.consultarUltimaValoracionIngreso(connection, codigoIngresoSeleccionado);
			if(solicitud == 0 && dtoIngresoSeleccionado.getViaIngreso() == ConstantesBD.codigoViaIngresoConsultaExterna){
				try{
					HibernateUtil.beginTransaction();
					IIngresosDAO ingresosDAO= ManejoPacienteDAOFabrica.crearinIngresosDAO();
					InfoDatos datos=ingresosDAO.consultarUltimoDiagnosticoRespuestaSolicitudes(dtoIngresoSeleccionado.getIngreso(),
													ConstantesBD.codigoTipoSolicitudProcedimiento);
					if(datos != null){
						forma.setAcronimoDx(datos.getDescripcion());
						forma.setTipoCie(datos.getCodigo());
						forma.setDescripcionDx(datos.getDescripcionInd());
						forma.setSeleccionarDiagnostico(false);
					}
					else{
						forma.setSeleccionarDiagnostico(true);
					}
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					HibernateUtil.abortTransaction();
					Log4JManager.error("ERROR ", e);
				}
			}
			else{
				InfoDatos datos = mundo.consultarDiagnosticoValoraciones(connection, solicitud);
				if(datos.getDescripcion() != null && !datos.getDescripcion().isEmpty()
						&& datos.getCodigo() > 0){
					forma.setAcronimoDx(datos.getDescripcion());
					forma.setTipoCie(datos.getCodigo());
					forma.setDescripcionDx(datos.getDescripcionInd());
					forma.setSeleccionarDiagnostico(false);
				}
				else{
					forma.setSeleccionarDiagnostico(true);
				}
			}
		}
		else{
			InfoDatos datos = mundo.consultarDiagnosticoEvoluciones(connection, solicitud);
			if(datos.getDescripcion() != null && !datos.getDescripcion().isEmpty()
					&& datos.getCodigo() > 0){
				forma.setAcronimoDx(datos.getDescripcion());
				forma.setTipoCie(datos.getCodigo());
				forma.setDescripcionDx(datos.getDescripcionInd());
				forma.setSeleccionarDiagnostico(false);
			}
			else{
				forma.setSeleccionarDiagnostico(true);
			}
		}
		
		//	se evalua si cargo incapacidad o la modifico
		if(dtoRegistroIncapacidades.getEstado().equals("NADA"))
		{
			forma.setEstado("ingresar");
			forma.setSeleccionarEspecialidad(true);
			forma.setEspecialidadesProfesional(usuario.getEspecialidades1());
			forma.getRegistroIncapacidad().setFechaInicioIncapacidad(dtoIngresoSeleccionado.getFechaIngreso());
			if (dtoIngresoSeleccionado.getViaIngreso() == (ConstantesBD.codigoViaIngresoUrgencias) || dtoIngresoSeleccionado.getViaIngreso() == (ConstantesBD.codigoViaIngresoHospitalizacion)){
				if(!UtilidadValidacion.tieneValoraciones(connection, dtoIngresoSeleccionado.getIdCuenta()))
				{
					return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "errors.noHayValoracionInicial", "errors.noHayValoracionInicial", true); 
				}
			}
			/*
			 * Validacion si esta definido el consecutivo de incapaciadades
			 */
			validarConsecutivoIncapacidades(connection, forma, usuario, true);
		}
		else
		{
			forma.setEstado("modificar");
			forma.setSeleccionarEspecialidad(false);
			forma.setSeleccionarDiagnostico(false);
			forma.setRegistroIncapacidad(new DtoRegistroIncapacidades(dtoRegistroIncapacidades));
			forma.setDescripcionDx(dtoRegistroIncapacidades.getDiagnostico());
			forma.setLogRegistroIncapacidad(new DtoRegistroIncapacidades(dtoRegistroIncapacidades));
			String result="";
			if(forma.getRegistroIncapacidad().getProrroga().equals(ConstantesBD.acronimoSi)){
				if(forma.getTipoCie()!=forma.getRegistroIncapacidad().getTipoCie() || !forma.getAcronimoDx().equals(forma.getRegistroIncapacidad().getAcronimoDiagnostico())){
					result="error.ordenmedica.diagnosticoDistinto";
				}
			}
			if(result.equals("")){
				/*
				 * Validacion si esta definido el consecutivo de incapaciadades
				 */
				if(Utilidades.convertirAEntero(forma.getRegistroIncapacidad().getConsecutivo()) < 0)
				{
					validarConsecutivoIncapacidades(connection, forma, usuario, true);
					
				}
				/*
				 * Valisdacion si profesional de la salud tiene la misma especialidad que la incapacidad 
				 */
				String[] especilidadesMedico = usuario.getCodEspecialidadesMedico().split(",");
				boolean flag = false;
				for (int i=0; i< especilidadesMedico.length; i++)
				{
					if (forma.getRegistroIncapacidad().getEspecialidad() == Utilidades.convertirAEntero(especilidadesMedico[i]))
					{
						flag = true;
						break;
					}
				}
				if (!flag){
					result="errors.noTieneMismaEspecialidadQueIncapacidad";
				}
			}
			if(!result.equals("")){
				request.setAttribute("codigoDescripcionError",result);
				return mapping.findForward("paginaError");
			}
		}
		forma.setMostrarIngresos(false);
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("ingresarModificar");
	}
}