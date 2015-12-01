/*
 * Ago 13, 2006
 */
package com.princetonsa.action.pyp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

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
import util.InfoDatosInt;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pyp.ProgramasPYPForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.pyp.DtoObservacionProgramaPYP;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.parametrizacion.Servicios;
import com.princetonsa.mundo.pyp.ProgramasPYP;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudConsultaExterna;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Medicos;

/**
 * @author Sebastián Gómez
 * 
 *         Clase usada para controlar los procesos de la funcionalidad Programas
 *         de Promoción y Prevención
 */
public class ProgramasPYPAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(ProgramasPYPAction.class);

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		Connection con = null;
		try{

			if (response == null)
				; // Para evitar que salga el warning
			if (form instanceof ProgramasPYPForm) {

				// SE ABRE CONEXION
				try {
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
					.getConnection();
				} catch (SQLException e) {
					logger.warn("No se pudo abrir la conexión" + e.toString());
				}

				// OBJETOS A USAR
				ProgramasPYPForm programasForm = (ProgramasPYPForm) form;
				HttpSession session = request.getSession();
				UsuarioBasico usuario = (UsuarioBasico) session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
				String estado = programasForm.getEstado();
				logger.warn("estado ProgramasPYPAction--> " + estado);

				// verificar si es null (Paciente está cargado)
				if ((paciente == null 
						|| paciente.getTipoIdentificacionPersona().equals("")))
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado","errors.paciente.noCargado", true);
				// verificar si es profesional de la salid
				if (!UtilidadValidacion.esProfesionalSalud(usuario)) {
					if (usuario != null
							&& UtilidadValidacion.estaMedicoInactivo(con, usuario.getCodigoPersona(), usuario.getCodigoInstitucionInt()))
						return ComunAction.accionSalirCasoError(mapping, request,con, logger, "No es profesional Salud","errors.noProfesionalSalud", true);
					else
						return ComunAction.accionSalirCasoError(mapping, request,con, logger, "Esta profesional inactivo","errors.profesionalSaludInactivo", true);
				}

				if (estado == null) {
					programasForm.reset();
					logger.warn("Estado no valido dentro del flujo de Programas PYP (null) ");
					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				} else if (estado.equals("empezar") || estado.equals("recargar")
						|| estado.equals("empezarConsultaExterna")
						|| estado.equals("resumenAtenciones")) {
					return accionEmpezar(con, programasForm, mapping, paciente,usuario, request);
				} else if (estado.equals("guardarProgramas")) {
					return accionGuardarProgramas(con, programasForm, mapping,usuario, paciente, request);
				} else if (estado.equals("detalleActividades")) {
					return accionDetalleActividades(con, programasForm, mapping,usuario, paciente);
				} else if (estado.equals("historicoActividad")) {
					return accionHistoricoActividad(con, programasForm, mapping);
				} else if (estado.equals("abrirObservaciones")) {
					return accionAbrirObservaciones(con, programasForm, mapping, usuario, paciente);
				} else if (estado.equals("guardarObservacion")) {
					return accionGuardarObservacion(con, programasForm, mapping, usuario, paciente);
				} else if (estado.equals("imprimirObservaciones")) {
					return accionImprimirObservaciones(con, programasForm, mapping, usuario, paciente, request);
				}	
				// ********ESTADOS VINCULADOS CON LAS ACCIONES
				// **********************************************************
				// Estados de la accion PROGRAMAR
				else if (estado.equals("accionProgramar")) {
					return accionAccionProgramar(con, programasForm, mapping,
							usuario, paciente, request);
				} else if (estado.equals("guardarOrdenAmb")) {
					return accionGuardarOrdenAmb(con, programasForm, mapping,
							paciente, usuario, request);
				} else if (estado.equals("guardarProgramar")) {
					return accionGuardarProgramar(con, programasForm, mapping,
							usuario, paciente, request);
				}
				// Estados de la accion SOLICITAR
				else if (estado.equals("accionSolicitar")) {
					return accionAccionSolicitar(con, programasForm, mapping,
							usuario, paciente, request, response);
				} else if (estado.equals("guardarSolicitar")) {
					return accionGuardarSolicitar(con, programasForm, mapping,
							usuario, paciente, request);
				}
				// Estados de la accion EJECUTAR
				else if (estado.equals("accionValidarEjecutar")) {
					if (accionAccionValidarEjecutar(con, programasForm, mapping,usuario, paciente, request, response)) {
						this.cerrarConexion(con);
						return mapping.findForward("validarCC");
					}
					programasForm.setEstado("accionEjecutar");
					return accionAccionEjecutar(con, programasForm, mapping,usuario, paciente, request, response);
				} else if (estado.equals("accionEjecutar")) {
					return accionAccionEjecutar(con, programasForm, mapping,usuario, paciente, request, response);
				} else if (estado.equals("guardarEjecutar")) {
					return accionGuardarEjecutar(con, programasForm, mapping,usuario, paciente, request);
				}
				// Estados de la accion CANCELAR
				else if (estado.equals("accionCancelar")) {
					return accionAccionCancelar(con, programasForm, mapping,
							request);
				} else if (estado.equals("procesarCancelar")) {
					return accionProcesarCancelar(con, programasForm, mapping,
							usuario, request);
				} else if (estado.equals("guardarCancelar")) {
					return accionGuardarCancelar(con, programasForm, mapping,
							usuario, paciente, request);
				}
				// ********ESTADOS VINCULADOS CON EL FLUJO PYP DE CONSULTA
				// EXTERNA******************************
				else if (estado.equals("recargarActividades")) {
					return accionRecargarActividades(con, programasForm, mapping);
				} else if (estado.equals("guardarEjecutarCE")) {
					return accionGuardarEjecutarCE(con, programasForm, mapping,usuario, paciente, request);
				}
				// *****GENERAR ACA EL ESTADO PARA GUARDAR LA SOLICITUD + CC

				// *********************************************************************************************
				else {
					programasForm.reset();
					logger
					.warn("Estado no valido dentro del flujo de ProgramasPYPAction (null) ");
					request.setAttribute("codigoDescripcionError",
					"errors.formaTipoInvalido");
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
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirObservaciones(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) {
		
		String nombreRptDesign = "ObservacionesProgramaPYP.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"pyp/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        // Nombre Institución, titulo
        comp.insertGridHeaderOfMasterPageWithName(0,1,1,4, "titulo");
        v=new Vector();
        v.add(ins.getRazonSocial());
        v.add("NIT: "+ins.getNit());
        v.add("Dirección: "+ins.getDireccion());
        v.add("Teléfono: "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        
        comp.insertLabelInGridPpalOfHeader(1, 0, "HISTORIAL DE OBSERVACIONES PROGRAMAS / ACTIVIDADES PYP");
        
        // Fecha hora de proceso y usuario
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        //******************
        
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);        
		
		// Se añaden Parametros
		newPathReport += 	"&paciente=" + paciente.getCodigoPersona() +
							"&programa=" + programasForm.getProgramas().get("codigo_programa_"+programasForm.getPosicion()) +
							"&institucion= "+usuario.getCodigoInstitucion();
		
		if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
		
		this.cerrarConexion(con);
		return mapping.findForward("observacionesPYP");
	}
	
	/**
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionGuardarObservacion(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente) {
		
		// Se prepara el dto con los datos a guardar
		programasForm.getNuevaObservacionPYP().setInstitucion(usuario.getCodigoInstitucionInt());
		programasForm.getNuevaObservacionPYP().setPaciente(paciente.getCodigoPersona());
		programasForm.getNuevaObservacionPYP().setPrograma(Utilidades.convertirAEntero(programasForm.getProgramas().get("codigo_programa_"+programasForm.getPosicion())+""));
		programasForm.getNuevaObservacionPYP().setUsuario(usuario.getLoginUsuario());
		// Guardar
		ProgramasPYP.guardarObservacioProgramaPYP(con, programasForm.getNuevaObservacionPYP());
		// Consultar nuevamente la lista de observaciones
		programasForm.setObservacionesProgramaPYP(ProgramasPYP.obtenerObservacionesProgramaPYP(con, paciente.getCodigoPersona(), Utilidades.convertirAEntero(programasForm.getProgramas().get("codigo_programa_"+programasForm.getPosicion())+""), usuario.getCodigoInstitucionInt()));
		// Se resetea el objeto de nueva observacion
		programasForm.setNuevaObservacionPYP(new DtoObservacionProgramaPYP());
		
		return mapping.findForward("observacionesPYP");
	}

	/**
	 * Accion para abrir el popup en el que se consultan y registran las observaciones de un programa PYP
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionAbrirObservaciones(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
		programasForm.setObservacionesProgramaPYP(ProgramasPYP.obtenerObservacionesProgramaPYP(con, paciente.getCodigoPersona(), Utilidades.convertirAEntero(programasForm.getProgramas().get("codigo_programa_"+programasForm.getPosicion())+""), usuario.getCodigoInstitucionInt()));
		return mapping.findForward("observacionesPYP");
	}

	/**
	 * Método implementado para guardar en sesión la informacion de ejecucion de
	 * actividad para el flujo de pyp en consulta externa
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGuardarEjecutarCE(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) throws NumberFormatException, IPSException {
		String tipoServicio = programasForm.getTipoServicioCE();
		int pos = programasForm.getPosicionActividad();
		int posP = programasForm.getPosicion();
		String estadoActividad = programasForm.getActividades(
				"codigo_estado_actividad_" + pos).toString();

		// Se toma
		// mapaPYPConsultaExterna*******************************************
		ActionErrors errores = new ActionErrors();
		HashMap mapaConsulta = new HashMap();
		if (request.getSession().getAttribute("mapaPYPConsultaExterna") == null)
			mapaConsulta.put("numRegistros", "0");
		else
			mapaConsulta = (HashMap) request.getSession().getAttribute("mapaPYPConsultaExterna");
		// se toma la posicion del mapa
		int posM = Integer.parseInt(mapaConsulta.get("numRegistros").toString());
		// *********************************************************************************

		// **********SE VERIFICA SI ES UNIFICAR PYP O NO******************************************************
		if (UtilidadTexto.getBoolean(programasForm.getUnificarPYP())) {
			// UNIFICAR PYP SI--------------------------------
			mapaConsulta.put("marcarPYP", "true");
			mapaConsulta.put("cuentaAbierta", programasForm.isCuentaActiva());
			mapaConsulta.put("numSolConsulta", programasForm.getNumSolConsulta());
			mapaConsulta.put("unificarPYP_" + posM, "true");
			mapaConsulta.put("tipoServicio_" + posM, tipoServicio);

			// Se inserta informacion del programa
			mapaConsulta.put("consecutivoTablaProg_" + posM, programasForm.getProgramas("consecutivo_" + posP));
			mapaConsulta.put("pacientePrograma_" + posM, paciente.getCodigoPersona());
			mapaConsulta.put("codigoPrograma_" + posM, programasForm.getProgramas("codigo_programa_" + posP));
			mapaConsulta.put("usuarioPrograma_" + posM, usuario.getLoginUsuario());
			mapaConsulta.put("institucionPrograma_" + posM, programasForm.getProgramas("institucion_" + posP));

			// Se inserta informacion de la actividad
			mapaConsulta.put("consecutivoTablaAct_" + posM, programasForm.getActividades("consecutivo_" + pos));
			mapaConsulta.put("consecutivoActividad_" + posM, programasForm.getActividades("consecutivo_actividad_" + pos));
			mapaConsulta.put("codigoActividad_" + posM, programasForm.getActividades("codigo_actividad_" + pos));
			mapaConsulta.put("estadoActividad_" + posM, estadoActividad);
			mapaConsulta.put("numeroOrdenActividad_" + posM, programasForm.getActividades("numero_orden_" + pos));
			mapaConsulta.put("numeroSolicitudActividad_" + posM, "0");
			mapaConsulta.put("fechaActividad_" + posM, UtilidadFecha.getFechaActual());
			mapaConsulta.put("horaActividad_" + posM, UtilidadFecha.getHoraActual());
			mapaConsulta.put("usuarioActividad_" + posM, usuario.getLoginUsuario());
			mapaConsulta.put("centroAtencionActividad_" + posM, usuario.getCodigoCentroAtencion());
			// se verifica la frecuencia
			if (!programasForm.getActividades("frecuencia_" + pos).toString().equals(""))
				mapaConsulta.put("frecuenciaActividad_" + posM, programasForm.getActividades("frecuencia_" + pos));

			posM++;
			mapaConsulta.put("numRegistros", posM);
			
			// Se asigna mapa a sesión
			request.getSession().setAttribute("mapaPYPConsultaExterna",mapaConsulta);

			// Se actualiza estado de la actividad
			programasForm.setActividades("codigo_estado_actividad_" + pos,ConstantesBD.codigoEstadoProgramaPYPEjecutado);

			programasForm.setCodigoMedicoCE(usuario.getCodigoPersona() + "");
			programasForm.setInstitucionCE(usuario.getCodigoInstitucion());

		} else {
			// ********SE PREPARAN LOS DATOS DEL
			// MAPA************************************
			mapaConsulta.put("marcarPYP", "false");
			mapaConsulta.put("cuentaAbierta", programasForm.isCuentaActiva());
			mapaConsulta.put("numSolConsulta", programasForm.getNumSolConsulta());
			mapaConsulta.put("unificarPYP_" + posM, "false");
			mapaConsulta.put("tipoServicio_" + posM, tipoServicio);

			// Se inserta informacion del programa
			mapaConsulta.put("consecutivoTablaProg_" + posM, programasForm.getProgramas("consecutivo_" + posP));
			mapaConsulta.put("pacientePrograma_" + posM, paciente.getCodigoPersona());
			mapaConsulta.put("codigoPrograma_" + posM, programasForm.getProgramas("codigo_programa_" + posP));
			mapaConsulta.put("usuarioPrograma_" + posM, usuario.getLoginUsuario());
			mapaConsulta.put("institucionPrograma_" + posM, programasForm.getProgramas("institucion_" + posP));

			// Se inserta informacion de la actividad
			mapaConsulta.put("consecutivoTablaAct_" + posM, programasForm.getActividades("consecutivo_" + pos));
			mapaConsulta.put("consecutivoActividad_" + posM, programasForm.getActividades("consecutivo_actividad_" + pos));
			mapaConsulta.put("codigoActividad_" + posM, programasForm.getActividades("codigo_actividad_" + pos));
			mapaConsulta.put("estadoActividad_" + posM, programasForm.getActividades("codigo_estado_actividad_" + pos));
			mapaConsulta.put("numeroOrdenActividad_" + posM, programasForm.getActividades("numero_orden_" + pos));
			mapaConsulta.put("numeroSolicitudActividad_" + posM, programasForm.getActividades("numero_solicitud_" + pos));
			mapaConsulta.put("fechaActividad_" + posM, UtilidadFecha.getFechaActual());
			mapaConsulta.put("horaActividad_" + posM, UtilidadFecha.getHoraActual());
			mapaConsulta.put("usuarioActividad_" + posM, usuario.getLoginUsuario());
			mapaConsulta.put("centroAtencionActividad_" + posM, usuario.getCodigoCentroAtencion());
			// se verifica la frecuencia
			if (!programasForm.getActividades("frecuencia_" + pos).toString().equals(""))
				mapaConsulta.put("frecuenciaActividad_" + posM, programasForm.getActividades("frecuencia_" + pos));
			// *******************************************************************

			mapaConsulta.put("especialidad_" + posM, programasForm.getEspecialidadCE());
			// Se verifica el tipo de servicio
			if (tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+ "")) {
				// TIPO SERVICIO CONSULTA
				mapaConsulta.put("diagnostico_" + posM, programasForm.getDiagnosticoCE());
				mapaConsulta.put("valorFicha_" + posM, programasForm.getValorFichaCE());
				mapaConsulta.put("finalidad_" + posM, programasForm.getFinalidadCE());
				mapaConsulta.put("causaExterna_" + posM, programasForm.getCausaExternaCE());
				mapaConsulta.put("finalidadServicio_" + posM, "");
			} else {
				// TIPO SERVICIO PROCEDIMIENTOS
				mapaConsulta.put("respuesta_" + posM, programasForm.getRespuestaCE());
				mapaConsulta.put("finalidadServicio_" + posM, programasForm.getFinalidadServicioCE().split( ConstantesBD.separadorSplit)[0]);
			}

			posM++;
			mapaConsulta.put("numRegistros", posM);
			// Se asigna mapa a sesión
			if (!programasForm.isPacienteCuentaCerrada())
				request.getSession().setAttribute("mapaPYPConsultaExterna",mapaConsulta);

			// Se actualiza estado de la actividad
			programasForm.setActividades("codigo_estado_actividad_" + pos,ConstantesBD.codigoEstadoProgramaPYPEjecutado);

		}

		if (programasForm.isPacienteCuentaCerrada()){
			programasForm.setConsultaExterna(true);
			ejecutarActividadCuentaCerrada(con, paciente, usuario, request, mapaConsulta, programasForm, errores);
		}

		this.cerrarConexion(con);
		return mapping.findForward("ejecutarActividadCE");
	}

	/**
	 * Método implementado para recargar la página de actividades
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRecargarActividades(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping) {
		this.cerrarConexion(con);
		return mapping.findForward("detalleActividades");
	}

	/**
	 * Método implementado para anular la orden ambulatoria asociada a la
	 * actividad a cancelar
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private ActionForward accionProcesarCancelar(Connection con, ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = null;
		try{
			errores = new ActionErrors();
			int pos = programasForm.getPosicionActividad();
			String tipoActividad = programasForm.getTipoActividad();
	
			// Se instancia el objeto de OrdenesAmbulatorias
			OrdenesAmbulatorias orden = new OrdenesAmbulatorias();
	
			// **********SE REALIZAN LAS VALIDACIONES*******************************
			if (programasForm.getMotivoCancelacion().equals("")){
				errores.add("Error en Motivo Cancelacion", new ActionMessage(
						"errors.required", "El motivo de cancelación"));
			}
			// ********************************************************************
	
			if (errores.isEmpty()) {
				HashMap campos = new HashMap();
				if (tipoActividad.equals(ConstantesBD.acronimoTipoActividadPYPServicio)){
					campos.put("codigo", programasForm.
							getActividades("numero_orden_" + pos));
				}else{
					campos.put("codigo", programasForm.
							getArticulos("numero_orden_"+ pos));
				}
				campos.put("usuario", usuario.getLoginUsuario());
				campos.put("fecha", UtilidadFecha.getFechaActual());
				campos.put("hora", UtilidadFecha.getHoraActual());
				campos.put("motivo", programasForm.getMotivoCancelacion());
	
				boolean resultado = orden.anularOrden(con, campos);
	
				if(resultado){
					/**FIXME ANULACION PYP Y AUTORIZACION---------------*/
					programasForm.setNumeroOrden(campos.get("codigo")+"");
					this.cargarInfoParaAnulacionAutorizacion(programasForm, usuario);
				}else{
					errores.add("Error ANULANDO orden ambulatoria",
							new ActionMessage("errors.noSeGraboInformacion",
									"DE LA ANULACION DE LA ORDEN AMBULATORIA"));
				}
			}

			if (!errores.isEmpty()) {
				programasForm.setEstado("accionCancelar");
				saveErrors(request, errores);
			}
		}catch(IPSException ipse){
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString()));
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}

		this.cerrarConexion(con);
		return mapping.findForward("cancelacionActividad");
	}

	/**
	 * Método para ingresar/modificar una actividad PROGRAMADA
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionGuardarProgramar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) {
		int posA = programasForm.getPosicionActividad();
		int posP = programasForm.getPosicion();
		int resp0 = 0, resp1 = 0;
		boolean fueInsertado = false;

		String tipoActividad = programasForm.getTipoActividad();
		String consecutivoActividad = "";
		String nombreActividad = "";

		String numeroOrden = programasForm.getNumeroOrden();
		ActionErrors errores = new ActionErrors();

		// Se instancia objeto programasPYP
		ProgramasPYP programas = new ProgramasPYP();

		// ****SE VERIFICA EL PROGRAMA************************
		String consecutivoPrograma = programasForm.getProgramas(
				"consecutivo_" + posP).toString();

		UtilidadBD.iniciarTransaccion(con);
		if (consecutivoPrograma.equals("0")) {
			// No existe programa se debe insertar
			programas.setCampos("codigoPaciente", paciente.getCodigoPersona());
			programas.setCampos("codigoPrograma", programasForm
					.getProgramas("codigo_programa_" + posP));
			programas.setCampos("usuario", usuario.getLoginUsuario());
			programas.setCampos("institucion", programasForm
					.getProgramas("institucion_" + posP));
			resp0 = programas.insertarPrograma(con);

			consecutivoPrograma = resp0 + "";
			fueInsertado = true;
		} else
			resp0 = 1;
		// ******************************************************

		// se limpia objeto
		programas.clean();
		logger.info("consecutivoPrograma => " + consecutivoPrograma);
		if (resp0 > 0) {
			// *********SE VERIFICA ACTIVIDAD****************************
			if (tipoActividad
					.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
				logger.info("es Servicio");
				consecutivoActividad = programasForm.getActividades(
						"consecutivo_" + posA).toString();
				nombreActividad = programasForm.getActividades(
						"nombre_actividad_" + posA).toString();
			} else {
				logger.info("es Articulo");
				consecutivoActividad = programasForm.getArticulos(
						"consecutivo_" + posA).toString();
				nombreActividad = programasForm.getArticulos(
						"nombre_actividad_" + posA).toString();
			}

			// se parametrizan campos comunes
			programas.setCampos("estadoPrograma",
					ConstantesBD.codigoEstadoProgramaPYPProgramado);
			programas.setCampos("numeroOrden", numeroOrden);
			programas.setCampos("fechaProgramar", UtilidadFecha
					.getFechaActual());
			programas.setCampos("horaProgramar", UtilidadFecha.getHoraActual());
			programas.setCampos("usuarioProgramar", usuario.getLoginUsuario());
			logger.info("consecutivoActividad Antesinsetrar=> "
					+ consecutivoActividad);

			// ********SE DEBE INSERTAR
			// ACTIVIDAD********************************************
			programas.setCampos("consecutivoPrograma", consecutivoPrograma);
			if (tipoActividad
					.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
				programas.setCampos("consecutivoActividad", programasForm
						.getActividades("consecutivo_actividad_" + posA));
				if (!programasForm.getActividades("frecuencia_" + posA)
						.toString().equals(""))
					programas.setCampos("frecuencia", programasForm
							.getActividades("frecuencia_" + posA));
			} else {
				programas.setCampos("consecutivoActividad", programasForm
						.getArticulos("consecutivo_actividad_" + posA));
				if (!programasForm.getArticulos("frecuencia_" + posA)
						.toString().equals(""))
					programas.setCampos("frecuencia", programasForm
							.getArticulos("frecuencia_" + posA));
			}

			programas.setCampos("centroAtencion", usuario
					.getCodigoCentroAtencion());

			resp1 = programas.insertarActividad(con);
			logger.info("Pasó por aqui =>resp1=> " + resp1);
			consecutivoActividad = resp1 + "";
			// se verifica resultado transaccion actividad
			if (resp1 <= 0)
				errores.add("Error al insertar actividad", new ActionMessage(
						"error.pyp.actividadesPYP.error", "al insertar",
						nombreActividad));

			// **********************************************************

		} else
			errores.add("Error al insertar el programa", new ActionMessage(
					"error.pyp.programasPYP.error", "al insertar",
					programasForm.getProgramas("nombre_programa_" + posP)));

		// Se verifica resultado de la transaccion
		if (resp0 > 0 && resp1 > 0) {
			// Se actualizan datos del programa en el caso de que haya sido
			// insertado
			if (fueInsertado) {
				programasForm.setProgramas("consecutivo_" + posP,
						consecutivoPrograma);
				programasForm.setProgramas("existe_" + posP, "true");
				programasForm.setProgramas("fecha_inicial_" + posP,
						UtilidadFecha.getFechaActual());
			}

			// se actualizan los consecutivos
			programasForm.setConsecutivoActividad(consecutivoActividad);
			programasForm.setConsecutivoPrograma(consecutivoPrograma);
			logger.info("consecutivoActividad => " + consecutivoActividad);
			logger.info("consecutivoPrograma => " + consecutivoPrograma);
			UtilidadBD.finalizarTransaccion(con);
			programasForm.setEstado("guardar"); // estado de confirmacion éxito
												// transacción
			// se recargan las actividades
			return accionDetalleActividades(con, programasForm, mapping,
					usuario, paciente);

		} else {
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			this.cerrarConexion(con);
			return mapping.findForward("detalleActividades");
		}
	}

	/**
	 * Método implementado para insertar una orden ambulatoria
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private ActionForward accionGuardarOrdenAmb(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			PersonaBasica paciente, UsuarioBasico usuario,
			HttpServletRequest request) throws NumberFormatException, IPSException {
		// ***************VALIDACION DE LOS
		// DATOS************************************
		ActionErrors errores = new ActionErrors();
		String tipoActividad = programasForm.getTipoActividad();
		int pos = programasForm.getPosicionActividad();
		String codigoActividad = "";
		String tipoServicio = "";
		// Inserción de la solicitud de ordenes ambulatorias
		OrdenesAmbulatorias orden = new OrdenesAmbulatorias();

		if (tipoActividad.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
			codigoActividad = programasForm.getActividades(
					"codigo_actividad_" + pos).toString();
			tipoServicio = Utilidades.obtenerTipoServicio(con, codigoActividad);

			// se verifica la especialidad de médico
			if (programasForm.getEspecialidadOcupacion().equals(""))
				errores.add("Error en especialidad ordena", new ActionMessage(
						"errors.required", "La especialidad que ordena"));

			if (!tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta
					+ "")) {
				// se verifica la finalidad del servicio
				if (programasForm.getFinalidadServicio().equals(""))
					errores.add("Error en finalidad del servicio",
							new ActionMessage("errors.required",
									"La finalidad del servicio"));
			}

			orden.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios
					+ "");
		} else {
			// Validaciones solo aplican para medicamentos
			if (programasForm.isMedicamento()) {

				// Se verifica la dosis
				if (programasForm.getDosis().equals(""))
					errores.add("Error en dosis", new ActionMessage(
							"errors.required", "La dosis"));

				// Se verifica la unidosis
				if (programasForm.getUnidosis().equals(""))
					errores.add("Error en unidosis", new ActionMessage(
							"errors.required", "La unidosis"));

				// Se verifica frecuencia
				if (programasForm.getFrecuencia().equals(""))
					errores.add("Error en frecuencia", new ActionMessage(
							"errors.required", "La frecuencia"));

				// se verifica el tipo de frecuencia
				if (programasForm.getTipoFrecuencia().equals(""))
					errores.add("Error en tipo de frecuencia",
							new ActionMessage("errors.required",
									"El tipo de frecuencia"));

				// se verifica la via
				if (programasForm.getVia().equals(""))
					errores.add("Error en Via", new ActionMessage(
							"errors.required", "La vía"));

				// se verifica los días de tratamiento
				if (programasForm.getDiasTratamiento().equals(""))
					errores.add("Error en Días de tratamiento",
							new ActionMessage("errors.required",
									"El campo días de tratamiento"));
				else {
					try {
						int dias = Integer.parseInt(programasForm
								.getDiasTratamiento());
						if (dias <= 0)
							errores
									.add(
											"Error en Días de tratamiento",
											new ActionMessage(
													"errors.integerMayorQue",
													"El campo días de tratamiento",
													"0"));
					} catch (Exception e) {
						errores.add("Error en Días de tratamiento",
								new ActionMessage("errors.integerMayorQue",
										"El campo días de tratamiento", "0"));
					}
				}
			}

			// se verifica la cantidad
			if (programasForm.getCantidad().equals(""))
				errores.add("Error en Cantidad", new ActionMessage(
						"errors.required", "El total de unidades ordenadas"));
			else {
				try {
					int cantidad = Integer
							.parseInt(programasForm.getCantidad());
					if (cantidad <= 0)
						errores.add("Error en Cantidad", new ActionMessage(
								"errors.integerMayorQue",
								"El total de unidades ordenadas", "0"));
				} catch (Exception e) {
					errores.add("Error en Cantidad", new ActionMessage(
							"errors.integerMayorQue",
							"El total de unidades ordenadas", "0"));
				}
			}

			orden.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos
					+ "");
		}
		// ***************************************************************************

		boolean errorGenerarAutoriz=false;
		if (errores.isEmpty()) {

			orden.setInstitucion(usuario.getCodigoInstitucionInt());
			orden.setCodigoPaciente(paciente.getCodigoPersona() + "");
			orden.setPyp(true);
			orden.setUrgente(false);
			orden.setCentroAtencion(usuario.getCodigoCentroAtencion() + "");
			orden.setProfesional(usuario.getCodigoPersona() + "");
			orden.setLoginUsuario(usuario.getLoginUsuario());
			String especialidad = programasForm.getEspecialidadOcupacion().split(ConstantesBD.separadorSplit)[0];
			orden.setEspecialidad(especialidad.equals("") ? ConstantesBD.codigoEspecialidadMedicaNinguna+"": especialidad);
			orden.setFechaOrden(programasForm.getFechaProgramacion());
			orden.setHora(programasForm.getHoraProgramacion());
			orden.setObservaciones(programasForm.getObservaciones());
			orden.setEstadoOrden(ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+ "");
			orden.setConsultaExterna(false);
			orden.setUsuarioConfirma(usuario.getLoginUsuario());
			orden.setFechaConfirma(programasForm.getFechaProgramacion());
			orden.setHoraConfirma(programasForm.getHoraProgramacion());
			orden.setCentroCostoSolicita(usuario.getCodigoCentroCosto() + "");

			if (programasForm.isPacienteCuentaCerrada()){
				orden.setIdIngresoPaciente(Utilidades.convertirAEntero(programasForm.getUltimaCuentaAsociada().get("ingreso")+ ""));
				orden.setCuentaPaciente(Utilidades.convertirAEntero(programasForm.getUltimaCuentaAsociada().get("cuenta")+""));
			}else
			  {
				orden.setIdIngresoPaciente(paciente.getCodigoIngreso());				
			    orden.setCuentaPaciente(paciente.getCodigoCuenta());
			   }

			HashMap mapa = new HashMap();
			// simulacion de la parte de servicios
			if (tipoActividad.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {

				mapa.put("codigo_0", programasForm.getActividades("codigo_actividad_" + pos));
				if (!tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta + ""))
					mapa.put("finalidad_0", programasForm.getFinalidadServicio().split(ConstantesBD.separadorSplit)[0]);
				else
					mapa.put("finalidad_0", "");
				
				mapa.put("cantidad_0", "1");
				mapa.put("urgente_0", ValoresPorDefecto.getValorFalseParaConsultas());
				mapa.put("numRegistros", "1");
				mapa.put("descripcionServicio_0", programasForm.getActividades("nombre_actividad_" + pos));
				orden.setServicios(mapa);
			} else {
				mapa.put("articulo_0", programasForm.getArticulos("codigo_actividad_" + pos));
				mapa.put("dosis_0", programasForm.getDosis());
				mapa.put("unidosis_0", programasForm.getUnidosis());
				mapa.put("via_0", programasForm.getVia());
				mapa.put("cantidad_0", programasForm.getCantidad());
				mapa.put("observaciones_0", programasForm.getObservaciones());
				mapa.put("frecuencia_0", programasForm.getFrecuencia());
				mapa.put("medicamento_0", programasForm.isMedicamento() + "");
				mapa.put("duraciontratamiento_0", programasForm.getDiasTratamiento());
				mapa.put("tipofrecuencia_0", programasForm.getTipoFrecuencia());
				mapa.put("descripcionArticulo_0", programasForm.getArticulos("nombre_actividad_" + pos));
				mapa.put("numRegistros", "1");
				orden.setArticulos(mapa);
			}

			boolean resp = false;
			if (Double.parseDouble(orden.guardarOrdenAmbulatoria(con, true,usuario, paciente, request, errores)[0]) > 0){
				resp = true;
				if(!errores.isEmpty()){
					errorGenerarAutoriz=true;
					saveErrors(request, errores);
				}
			}

			if (resp){
				programasForm.setNumeroOrden(Utilidades.obtenerCodigoOrdenAmbulatoria(con, orden.getNumeroOrden(), usuario.getCodigoInstitucionInt()));
			}else{
				errores.add("Error generando orden ambulatoria",new ActionMessage("errors.noSeGraboInformacion","DE LA ORDEN AMBULATORIA"));
			}
		}

		if(!errorGenerarAutoriz){
			if (!errores.isEmpty()) {
				programasForm.setEstado("accionProgramar");
				saveErrors(request, errores);
			}
		}

		this.cerrarConexion(con);
		return mapping.findForward("programarActividad");
	}

	/**
	 * Método implementado para insertar/modificar una actividad ejecutada
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private ActionForward accionGuardarEjecutar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) {

		int posA = programasForm.getPosicionActividad();
		int posP = programasForm.getPosicion();
		int resp0 = 0, resp1 = 0, resp2 = 0, resp3 = 0;
		boolean fueInsertado = false;
		String consecutivoActividad = "";
		String nombreActividad = "";
		String estadoActividad = "";
		String numeroSolicitud = programasForm.getNumeroSolicitud();
		ActionErrors errores = new ActionErrors();

		// Se instancia objeto programasPYP
		ProgramasPYP programas = new ProgramasPYP();

		// ****SE VERIFICA EL PROGRAMA************************
		String consecutivoPrograma = programasForm.getProgramas(
				"consecutivo_" + posP).toString();

		UtilidadBD.iniciarTransaccion(con);
		if (consecutivoPrograma.equals("0")) {
			// No existe programa se debe insertar
			programas.setCampos("codigoPaciente", paciente.getCodigoPersona());
			programas.setCampos("codigoPrograma", programasForm
					.getProgramas("codigo_programa_" + posP));
			programas.setCampos("usuario", usuario.getLoginUsuario());
			programas.setCampos("institucion", programasForm
					.getProgramas("institucion_" + posP));
			resp0 = programas.insertarPrograma(con);

			consecutivoPrograma = resp0 + "";
			fueInsertado = true;
		} else
			resp0 = 1;
		// ******************************************************

		// se limpia objeto
		programas.clean();
		if (resp0 > 0) {
			// *********SE VERIFICA ACTIVIDAD****************************

			consecutivoActividad = programasForm.getActividades(
					"consecutivo_" + posA).toString();
			nombreActividad = programasForm.getActividades(
					"nombre_actividad_" + posA).toString();
			estadoActividad = programasForm.getActividades(
					"codigo_estado_actividad_" + posA).toString();

			// se parametrizan campos comunes
			programas.setCampos("estadoPrograma",
					ConstantesBD.codigoEstadoProgramaPYPEjecutado);
			programas.setCampos("numeroSolicitud", numeroSolicitud);
			programas
					.setCampos("fechaEjecutar", UtilidadFecha.getFechaActual());
			programas.setCampos("horaEjecutar", UtilidadFecha.getHoraActual());
			programas.setCampos("usuarioEjecutar", usuario.getLoginUsuario());

			// Se verifica si la actividad ya existe o fue ejecutada para el
			// paciente en PYP
			if (consecutivoActividad.equals("0") 
					|| estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado) 
					|| estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado)) {
				// ********SE DEBE INSERTAR
				// ACTIVIDAD********************************************
				programas.setCampos("consecutivoPrograma", consecutivoPrograma);
				programas.setCampos("consecutivoActividad", programasForm.getActividades("consecutivo_actividad_" + posA));
				if (!programasForm.getActividades("frecuencia_" + posA).toString().equals(""))
					programas.setCampos("frecuencia", programasForm.getActividades("frecuencia_" + posA));
				programas.setCampos("centroAtencion", usuario.getCodigoCentroAtencion());

				resp1 = programas.insertarActividad(con);
				consecutivoActividad = resp1 + "";
				resp3 = 1;
				// se verifica resultado transaccion actividad
				if (resp1 <= 0)
					errores.add("Error al modificar actividad",new ActionMessage("error.pyp.actividadesPYP.error","al insertar", nombreActividad));
			} else {
				// ******SE DEBE MODIFICAR LA
				// ACTIVIDAD***************************************
				programas.setCampos("consecutivoActividadPYP",consecutivoActividad);

				resp1 = programas.modificarActividad(con);
				// se verifica resultado transaccion actividad
				if (resp1 <= 0)
					errores.add("Error al modificar actividad",new ActionMessage("error.pyp.actividadesPYP.error","al actualizar", nombreActividad));

				// si la actividad estaba en estado programado se debe
				// actualizar la orden ambulatoria con el numero de la solicitud
				if (estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado)) {
					// ******************SE ACTUALIZA LA ORDEN
					// AMBULATORIA*******************
					HashMap campos = new HashMap();
					campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
					campos.put("numeroSolicitud", numeroSolicitud);
					campos.put("numeroOrden", programasForm.getActividades("numero_orden_" + posA));
					campos.put("usuario", usuario.getLoginUsuario());

					resp3 = OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,campos);

					if (resp3 <= 0)
						errores.add("Error generando orden ambulatoria",new ActionMessage("errors.noSeGraboInformacion","AL ACTUALIZAR LA ORDEN AMBULATORIA"));

					// ********************************************************************************
				} else
					resp3 = 1;

			}
			// **********************************************************
			if (resp1 > 0) {
				// **********VERIFICACION DEL
				// ACUMULADO********************************
				logger
						.info("\n\n\n\nVOY A ACTUALIZAR EL ACUMULADFO DE PYP\n\n\n");
				// como no existe acumulado para esta actividad se inserta una
				// nueva
				resp2 = Utilidades.actualizarAcumuladoPYP(con, numeroSolicitud,usuario.getCodigoCentroAtencion() + "");
				if (resp2 <= 0)
					errores.add("Error al ingresar acumulado",new ActionMessage("error.pyp.actividadesPYP.error","al actualizar el acumulado de",nombreActividad));

				// *******************************************************************
			}
		} else
			errores.add("Error al insertar el programa", new ActionMessage(
					"error.pyp.programasPYP.error", "al insertar",
					programasForm.getProgramas("nombre_programa_" + posP)));

		// Se verifica resultado de la transaccion
		if (resp0 > 0 && resp1 > 0 && resp2 > 0 && resp3 > 0) {
			// Se actualizan datos del programa en el caso de que haya sido
			// insertado
			if (fueInsertado) {
				programasForm.setProgramas("consecutivo_" + posP,
						consecutivoPrograma);
				programasForm.setProgramas("existe_" + posP, "true");
				programasForm.setProgramas("fecha_inicial_" + posP,
						UtilidadFecha.getFechaActual());
			}

			// /se actualizan los consecutivos
			programasForm.setConsecutivoActividad(consecutivoActividad);
			programasForm.setConsecutivoPrograma(consecutivoPrograma);

			UtilidadBD.finalizarTransaccion(con);
			programasForm.setEstado("guardar"); // estado de confirmacion éxito
												// transacción
			// se recargan las actividades
			return accionDetalleActividades(con, programasForm, mapping,
					usuario, paciente);

		} else {
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			this.cerrarConexion(con);
			return mapping.findForward("detalleActividades");
		}
	}

	/**
	 * Método para ingresar/modificar la solicitud de una actividad
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private ActionForward accionGuardarSolicitar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) {
		int posA = programasForm.getPosicionActividad();
		int posP = programasForm.getPosicion();
		@SuppressWarnings("unused")
		int resp0 = 0, resp1 = 0, resp2 = 0, resp3 = 0;
		boolean fueInsertado = false;
		String tipoActividad = programasForm.getTipoActividad();
		String consecutivoActividad = "";
		String nombreActividad = "";
		String estadoActividad = "";
		String numeroSolicitud = programasForm.getNumeroSolicitud();
		ActionErrors errores = new ActionErrors();
		// Se instancia objeto programasPYP
		ProgramasPYP programas = new ProgramasPYP();

		// ****SE VERIFICA EL PROGRAMA************************
		String consecutivoPrograma = programasForm.getProgramas(
				"consecutivo_" + posP).toString();

		UtilidadBD.iniciarTransaccion(con);
		if (consecutivoPrograma.equals("0")) {
			// No existe programa se debe insertar
			programas.setCampos("codigoPaciente", paciente.getCodigoPersona());
			programas.setCampos("codigoPrograma", programasForm
					.getProgramas("codigo_programa_" + posP));
			programas.setCampos("usuario", usuario.getLoginUsuario());
			programas.setCampos("institucion", programasForm
					.getProgramas("institucion_" + posP));
			resp0 = programas.insertarPrograma(con);

			consecutivoPrograma = resp0 + "";
			fueInsertado = true;
		} else
			resp0 = 1;
		// ******************************************************

		// se limpia objeto
		programas.clean();
		if (resp0 > 0) {
			// *********SE VERIFICA ACTIVIDAD****************************
			if (tipoActividad
					.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
				logger.info("es Servicio");
				consecutivoActividad = programasForm.getActividades(
						"consecutivo_" + posA).toString();
				nombreActividad = programasForm.getActividades(
						"nombre_actividad_" + posA).toString();
				estadoActividad = programasForm.getActividades(
						"codigo_estado_actividad_" + posA).toString();
			} else {
				logger.info("es Articulo");
				consecutivoActividad = programasForm.getArticulos(
						"consecutivo_" + posA).toString();
				nombreActividad = programasForm.getArticulos(
						"nombre_actividad_" + posA).toString();
				estadoActividad = programasForm.getArticulos(
						"codigo_estado_actividad_" + posA).toString();
			}

			// se parametrizan campos comunes
			programas.setCampos("estadoPrograma",
					ConstantesBD.codigoEstadoProgramaPYPSolicitado);
			programas.setCampos("numeroSolicitud", numeroSolicitud);
			programas.setCampos("fechaSolicitar", UtilidadFecha
					.getFechaActual());
			programas.setCampos("horaSolicitar", UtilidadFecha.getHoraActual());
			programas.setCampos("usuarioSolicitar", usuario.getLoginUsuario());
			// Se verifica si la actividad ya existe para el paciente en PYP
			if (consecutivoActividad.equals("0")
					|| estadoActividad
							.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado)
					|| estadoActividad
							.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado)) {
				// ********SE DEBE INSERTAR
				// ACTIVIDAD********************************************
				programas.setCampos("consecutivoPrograma", consecutivoPrograma);
				if (tipoActividad
						.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
					programas.setCampos("consecutivoActividad", programasForm
							.getActividades("consecutivo_actividad_" + posA));
					if (!programasForm.getActividades("frecuencia_" + posA)
							.toString().equals(""))
						programas.setCampos("frecuencia", programasForm
								.getActividades("frecuencia_" + posA));
				} else {
					programas.setCampos("consecutivoActividad", programasForm
							.getArticulos("consecutivo_actividad_" + posA));
					if (!programasForm.getArticulos("frecuencia_" + posA)
							.toString().equals(""))
						programas.setCampos("frecuencia", programasForm
								.getArticulos("frecuencia_" + posA));
				}

				programas.setCampos("centroAtencion", usuario
						.getCodigoCentroAtencion());

				resp1 = programas.insertarActividad(con);
				logger.info("Pasó por aqui =>resp1=> " + resp1);
				consecutivoActividad = resp1 + "";
				resp3 = 1;
				// se verifica resultado transaccion actividad
				if (resp1 <= 0)
					errores.add("Error al insertar actividad",
							new ActionMessage("error.pyp.actividadesPYP.error",
									"al insertar", nombreActividad));

			} else if (!estadoActividad
					.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado)) {
				// ******SE DEBE MODIFICAR LA
				// ACTIVIDAD***************************************
				programas.setCampos("consecutivoActividadPYP",
						consecutivoActividad);
				resp1 = programas.modificarActividad(con);
				// se verifica resultado transaccion actividad
				if (resp1 <= 0)
					errores.add("Error al modificar actividad",
							new ActionMessage("error.pyp.actividadesPYP.error",
									"al actualizar", nombreActividad));

				// si la actividad estaba en estado programado se debe
				// actualizar la orden ambulatoria
				if (estadoActividad
						.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado)) {
					// ******************SE ACTUALIZA LA ORDEN
					// AMBULATORIA*******************
					HashMap campos = new HashMap();
					campos
							.put(
									"estadoOrden",
									ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
					campos.put("numeroSolicitud", numeroSolicitud);
					if (tipoActividad
							.equals(ConstantesBD.acronimoTipoActividadPYPServicio))
						campos.put("numeroOrden", programasForm
								.getActividades("numero_orden_" + posA));
					else
						campos.put("numeroOrden", programasForm
								.getArticulos("numero_orden_" + posA));

					resp3 = OrdenesAmbulatorias
							.actualizarSolicitudEnOrdenAmbulatoria(con, campos);

					if (resp3 <= 0)
						errores.add("Error generando orden ambulatoria",
								new ActionMessage(
										"errors.noSeGraboInformacion",
										"AL ACTUALIZAR LA ORDEN AMBULATORIA"));
					// ********************************************************************************
				} else
					resp3 = 1;

			} else {
				logger.info("numeroSolicitud==========> "
						+ programasForm.getActividades("numero_solicitud_"
								+ posA));
				resp1 = 1;
				resp3 = 1;
			}
			// **********************************************************

		} else
			errores.add("Error al insertar el programa", new ActionMessage(
					"error.pyp.programasPYP.error", "al insertar",
					programasForm.getProgramas("nombre_programa_" + posP)));

		// Se verifica resultado de la transaccion
		if (resp0 > 0 && resp1 > 0 && resp3 > 0) {
			// Se actualizan datos del programa en el caso de que haya sido
			// insertado
			if (fueInsertado) {
				programasForm.setProgramas("consecutivo_" + posP,
						consecutivoPrograma);
				programasForm.setProgramas("existe_" + posP, "true");
				programasForm.setProgramas("fecha_inicial_" + posP,
						UtilidadFecha.getFechaActual());
			}

			// se actualizan los consecutivos
			programasForm.setConsecutivoActividad(consecutivoActividad);
			programasForm.setConsecutivoPrograma(consecutivoPrograma);
			UtilidadBD.finalizarTransaccion(con);
			programasForm.setEstado("guardar"); // estado de confirmacion éxito
												// transacción
			// se recargan las actividades
			return accionDetalleActividades(con, programasForm, mapping,
					usuario, paciente);

		} else {
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			this.cerrarConexion(con);
			return mapping.findForward("detalleActividades");
		}
	}

	/**
	 * Método que guarda la cancelacion de
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 * @throws IPSException 
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionGuardarCancelar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) throws IPSException 
	{
		ActionErrors errores = null;
		try{
			int pos = programasForm.getPosicionActividad();
			String tipoActividad = programasForm.getTipoActividad();
			String nombreActividad = "";
			String consecutivoActividad = "";
			String consecutivoPrograma = programasForm.getProgramas(
					"consecutivo_" + programasForm.getPosicion()).toString();
			errores = new ActionErrors();
	
			// Se instancia objeto de ProgramasPYP
			ProgramasPYP programas = new ProgramasPYP();
	
			if (tipoActividad.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
				consecutivoActividad = programasForm.getActividades(
						"consecutivo_" + pos).toString();
				nombreActividad = programasForm.getActividades(
						"nombre_actividad_" + pos).toString();
			} else {
				consecutivoActividad = programasForm.getArticulos(
						"consecutivo_" + pos).toString();
				nombreActividad = programasForm.getArticulos(
						"nombre_actividad_" + pos).toString();
			}
	
			programas.setCampos("consecutivoActividadPYP", consecutivoActividad);
			programas.setCampos("estadoPrograma",
					ConstantesBD.codigoEstadoProgramaPYPCancelado);
			programas.setCampos("motivoCancelacion", programasForm
					.getMotivoCancelacion());
			programas.setCampos("usuarioCancelar", usuario.getLoginUsuario());
			programas.setCampos("fechaCancelar", UtilidadFecha
					.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			programas.setCampos("horaCancelar", UtilidadFecha.getHoraActual());
	
			int resp = programas.modificarActividad(con);
	
			if (resp <= 0) {
				programasForm.setEstado("accionCancelar");
				errores.add("Error al cancelar ", new ActionMessage(
						"error.pyp.actividadesPYP.error", "al cancelar",
						nombreActividad));
			}
	
			// /Se verifica resultado de la transaccion
			if (resp > 0) {
				// se actualizan los consecutivos
				programasForm.setConsecutivoActividad(consecutivoActividad);
				programasForm.setConsecutivoPrograma(consecutivoPrograma);
				UtilidadBD.finalizarTransaccion(con);
				programasForm.setEstado("guardar"); // estado de confirmacion éxito
													// transacción
				
				// se recargan las actividades
				return accionDetalleActividades(con, programasForm, mapping,
						usuario, paciente);
	
			} else {
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
				this.cerrarConexion(con);
				return mapping.findForward("detalleActividades");
			}
		}
		catch(Exception e){
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}
		return accionDetalleActividades(con, programasForm, mapping,
				usuario, paciente);
	}

	/**
	 * Método implementado para cancelar una actividad
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionAccionCancelar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			HttpServletRequest request) {
		String tipoActividad = programasForm.getTipoActividad();
		int pos = programasForm.getPosicionActividad();
		ActionErrors errores = new ActionErrors();

		boolean puedoCancelar = false;
		if (tipoActividad.equals(ConstantesBD.acronimoTipoActividadPYPServicio))
			puedoCancelar = UtilidadTexto.getBoolean(programasForm
					.getActividades("cancelar_" + pos).toString());
		else
			puedoCancelar = UtilidadTexto.getBoolean(programasForm
					.getArticulos("cancelar_" + pos).toString());

		this.cerrarConexion(con);
		if (puedoCancelar) {
			// Se limpia el motivo de cancelacion
			programasForm.setMotivoCancelacion("");
			return mapping.findForward("cancelacionActividad");
		} else {
			errores
					.add(
							"No se puede cancelar",
							new ActionMessage(
									"errors.notEspecific",
									"No se puede cancelar la actividad, no se encuentra programada. Por favor verifique"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}

	}

	/**
	 * Método que consulta el histórico de una actividad PYP
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionHistoricoActividad(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping) {
		int posA = programasForm.getPosicionActividad();
		int posP = programasForm.getPosicion();
		String tipoActividad = programasForm.getTipoActividad();

		// Se instancia objeto de ProgramasPYP
		ProgramasPYP programas = new ProgramasPYP();
		programas.setCampos("consecutivoProgramaPYP", programasForm
				.getProgramas("consecutivo_" + posP));
		if (tipoActividad.equals(ConstantesBD.acronimoTipoActividadPYPServicio))
			programas.setCampos("consecutivoActividad", programasForm
					.getActividades("consecutivo_actividad_" + posA));
		else
			programas.setCampos("consecutivoActividad", programasForm
					.getArticulos("consecutivo_actividad_" + posA));

		// se consulta el histórico de la actividad
		programasForm.setHistorico(programas.consultarHistoricosActividad(con));
		programasForm.setNumHistorico(Integer.parseInt(programasForm
				.getHistorico("numRegistros").toString()));

		this.cerrarConexion(con);
		return mapping.findForward("historicoActividad");
	}

	/**
	 * Método implementado para realizar accion EJECUTAR sobre una actividad
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean accionAccionValidarEjecutar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request, HttpServletResponse response) {
		
		// *********CAMBIOS ENTIDADES SUBCONtRATADAS
		int pos = programasForm.getPosicionActividad();
		String codigoActividad = programasForm.getActividades("codigo_actividad_" + pos).toString();
		String estadoActividad = programasForm.getActividades("codigo_estado_actividad_" + pos).toString();
		String tipoServ = Utilidades.obtenerTipoServicio(con, codigoActividad);

		logger.info("\n\n valor de estado de la solicitud >> "+ estadoActividad);

		if ((tipoServ.equals(ConstantesBD.codigoServicioInterconsulta + ""))
				&& !programasForm.isConfirmarEjecucion()
				&& !estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado)) {
			
			programasForm.setCentrosCosto(UtilidadesFacturacion.consultarCentrosCostoGrupoServicio(con, Utilidades.obtenerGrupoServicio(con, Utilidades.convertirAEntero(codigoActividad)),usuario.getCodigoCentroAtencion(), false, true));

			programasForm.setCodigoServicio(Utilidades.obtenerCodigoPropietarioServicio(con, programasForm.getActividades("codigo_actividad_" + pos).toString(), ConstantesBD.codigoTarifarioCups));
			programasForm.setNombreServicio(Servicios.obtenerNombreServicio(con, Utilidades.convertirAEntero(programasForm.getActividades("codigo_actividad_" + pos).toString()), ConstantesBD.codigoTarifarioCups));
			programasForm.getActividades("numero_solicitud_" + pos);

			programasForm.setNombreServicio(programasForm.getActividades("nombre_actividad_" + pos).toString());
			logger.info("\n\n\nACA SE CONSULTAN LOS CC!!!!!!!!");
			return true;
		}

		return false;
		// *******FIN CAMBIOS ENTIDADES SUBCONTRATADAS
	}

	/**
	 * Método implementado para realizar accion EJECUTAR sobre una actividad
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param response
	 * @return
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private ActionForward accionAccionEjecutar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, IPSException {
		
		ActionErrors errores = new ActionErrors();
		// Se toman datos de la forma
		String codigoActividad = "";
		String nombreActividad = "";
		String estadoActividad = "";
		boolean consultaExterna = programasForm.isConsultaExterna();
		int pos = programasForm.getPosicionActividad();
		int posP = programasForm.getPosicion();

		// Se instancia el objeto ProgramasPYP
		ProgramasPYP programas = new ProgramasPYP();

		codigoActividad = programasForm.getActividades("codigo_actividad_" + pos).toString();
		nombreActividad = programasForm.getActividades("nombre_actividad_" + pos).toString();
		estadoActividad = programasForm.getActividades("codigo_estado_actividad_" + pos).toString();
		programas.setCampos("consecutivoActividad", programasForm.getActividades("consecutivo_actividad_" + pos));

		programas.setCampos("institucion", usuario.getCodigoInstitucion());

		// *******SE VERIFICA SI ES PYP DE CONSULTA EXTERNA*****************
		// A) Si es de consulta externa con cuenta cerrada

		logger.info("Consulta Externa: "+consultaExterna);
		logger.info("Paciente Cuenta Cerrada: "+programasForm.isPacienteCuentaCerrada());
		
		if (consultaExterna || programasForm.isPacienteCuentaCerrada()) {

			/*************** FLUJO CONSULTA EXTERNA DE PYP ********************************************************************/
			// Se obitene el tipo de servicio de la actividad
			String tipoServicio = Utilidades.obtenerTipoServicio(con,codigoActividad);
			String unificarPYP = programasForm.getUnificarPYP();
			// Si el tipo de servicio es CONSULTA
			logger.info("\n\nEL TIPO DE SERVICIO ES-------->" + tipoServicio);
			if (tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+ "")) {
				// Se verifica parámetro de unificar PYP
				if (UtilidadTexto.getBoolean(unificarPYP) && !estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado)) {
					programasForm.setCodigoMedicoCE(usuario.getCodigoPersona()+ "");
					programasForm.setInstitucionCE(usuario.getCodigoInstitucion());
					this.cerrarConexion(con);
					return mapping.findForward("ejecutarActividadCE");
				}

			}

			if (programasForm.isCuentaActiva() && !tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+ "")) {
				// Como es un servicio diferente a procedimientos, se debe
				// continuar con el flujo normal de PYP
				
				if(programasForm.isPacienteCuentaCerrada())
					return accionGuardarEjecutarCE(con, programasForm, mapping, usuario, paciente, request);
				
			} else {
				// /*************VERIFICAR SI LA ACTIVIDAD YA SE EJECUTÓ************************
				ActionForward revision = verificarActividadEjecutar(con,paciente, programasForm, mapping, request);
				if (revision != null) {
					this.cerrarConexion(con);
					return revision;
				}
				// *********************************************************************************

				// Caso en el que es tipo Servicio Consulta y unificar PYP es
				// falso o es tipo Servicio Procedimiento
				// se asigna el tipo de servicio
				programasForm.setTipoServicioCE(tipoServicio);
				// se limpian datos
				programasForm.resetConsultaExterna();
				programasForm.setCodigoMedicoCE(usuario.getCodigoPersona() + "");
				programasForm.setInstitucionCE(usuario.getCodigoInstitucion());

				// *******SE CONSULTA LA FINALIDAD DE LA ACTIVIDAD
				// *********************************************************
				String finalidad = consultarFinalidadActividad(con,programasForm);

				if (!finalidad.equals("") && !finalidad.equals("null@@@@@null")) {
					if (tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+ ""))
						programasForm.setFinalidadCE(finalidad);
					else
						programasForm.setFinalidadServicioCE(finalidad);
				} else {
					this.cerrarConexion(con);
					errores.add("Actividad sin finalidad", new ActionMessage("error.pyp.programasPYP.actividadSinFinalidad",nombreActividad));
					saveErrors(request, errores);
					return mapping.findForward("paginaErroresActionErrors");
				}

				if (programasForm.isPacienteCuentaCerrada()) {
					programasForm.setNumSolConsulta("");
					programasForm.setUnificarPYP("");
				}

				this.cerrarConexion(con);
				return mapping.findForward("ejecutarActividadCE");
			}

			/*************** FIN FLUJO CONSULTA EXTERNA DE PYP ********************************************************************/

		}

		/*************** FLUJO NORMAL DE PYP ********************************************************************/

		// Validación de los convenios del ingreso
		if (!this.puedoGenerarCargoPyp(con, paciente)) {
			errores.add("", new ActionMessage("error.pyp.programasPYP.ingresoSinConveniosPyp"));
			saveErrors(request, errores);
			this.cerrarConexion(con);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		}

		// Se consultan los centros de atención de la actividad
		String centrosAtencion = programas.consultarCentrosAtencionActividad(con).replace(",", ConstantesBD.separadorSplit);

		// se verifica si habían centros de atencion
		if (!centrosAtencion.equals("")) {
			
			// *************VERIFICAR SI LA ACTIVIDAD YA SE
			// EJECUTÓ************************
			ActionForward revision = verificarActividadEjecutar(con, paciente,programasForm, mapping, request);
			if (revision != null) {
				this.cerrarConexion(con);
				return revision;
			}
			// *********************************************************************************

			logger.info("\n\n\n\n\n¡¡¡¡¡¡¡¡¡ACA ES ANTES DE ENTRAR A LA VALIDACION DE ENTIDADES SUBCONTRATADAS!!!!!!!!!!!!!");

			String tipoServicio = Utilidades.obtenerTipoServicio(con,codigoActividad);
			if (estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado)) {
				// realizar solo la respuesta.
				String numeroSolicitud = programasForm.getActividades(
						"numero_solicitud_" + pos).toString();

				// la respuesta funciona es con el codigo numerico que se guarda
				// en el atributo tipo de la tabla solicitud.
				String tipoSolicitud = Utilidades.obtenerCodigoTipoSolicitud(
						con, numeroSolicitud);
				String pagina = "";

				logger
						.info("\nEL NRO DE LA SOLICITUD ES--->"
								+ numeroSolicitud);
				// Obtener el CC de la solicitud y asimismo el tipo de entidad
				// ejecuta de ese CC para las validaciones en entidad
				// subcontratada
				String CCSolicitud = Utilidades.obtenerCCSolicitadoSolicitud(
						con, numeroSolicitud);
				logger.info("\nEL CENTRO DE COSOTO DE LA SOLICITUD ES---->"
						+ CCSolicitud);
				String tipoEntidadEjecuta = Utilidades.getTipoEntidadEjecutaCC(
						con, CCSolicitud);
				logger.info("\nEL TIPO DE ENTIDAD QUE EJECUTA ES --->"
						+ tipoEntidadEjecuta);
				boolean tieneAutorizacion = false;
				tieneAutorizacion = Utilidades.existeAutorizacionSolicitud(con,
						numeroSolicitud);
				logger.info("\nTIENE AUTORIZACION --->" + tieneAutorizacion);
				String tipoAutorizacion = "";
				if (tieneAutorizacion)
					tipoAutorizacion = Utilidades
							.obtenerTipoAutorizacionEntidadSubcontratada(con,
									numeroSolicitud);

				// Por cambio en entidades subcontratadas se pregunta
				// adicionalmente si el tipo de entidad que ejecuta es INTERNO

				if ((Integer.parseInt(tipoSolicitud) == ConstantesBD.codigoTipoSolicitudProcedimiento)
						&& tipoEntidadEjecuta
								.equals(ConstantesIntegridadDominio.acronimoInterna)) {
					// Se verifica el rol de la funcionalidad RESPONDER
					// PROCEDIMIENTOS
					if (Utilidades.tieneRolFuncionalidad(con, usuario
							.getLoginUsuario(), 179)) {
						pagina = "../respuestaProcedimientos/respuestaProcedimientos.do?estado=elegirPlantilla&numeroSolicitud="
								+ numeroSolicitud
								+ "&servicio="
								+ codigoActividad + "&vieneDePyp=true";// &ind=0";
						this.cerrarConexion(con);
						try {
							response.sendRedirect(pagina);
							return null;
						} catch (Exception e) {
							logger
									.error("Error en accionAccionEjecutar de ProgramasPYPAction: "
											+ e);
						}

					} else
						errores.add("Usuario sin permiso", new ActionMessage(
								"errors.usuarioSinRolFuncionalidad", usuario
										.getLoginUsuario(),
								"Responder Procedimientos"));
				}

				// Cambio por Entidades Subcontratadas si tipo de entidad que
				// ejecuta de la autorizacion de entidades subcontratadas es
				// EXTERNO O AMBOS SE CONTINUA CON EL SIGUIENTE FLUJO
				else if ((Integer.parseInt(tipoSolicitud) == ConstantesBD.codigoTipoSolicitudProcedimiento)
						&& (tipoEntidadEjecuta
								.equals(ConstantesIntegridadDominio.acronimoExterna) || tipoEntidadEjecuta
								.equals(ConstantesIntegridadDominio.acronimoAmbos))) {
					if (tieneAutorizacion) {
						// Si el tipo de autorizacion es interna se sigue
						// normalmente la funcionalidad
						if (tipoAutorizacion
								.equals(ConstantesIntegridadDominio.acronimoInterna)) {
							if (Utilidades.tieneRolFuncionalidad(con, usuario
									.getLoginUsuario(), 179)) {
								pagina = "../respuestaProcedimientos/respuestaProcedimientos.do?estado=elegirPlantilla&numeroSolicitud="
										+ numeroSolicitud
										+ "&servicio="
										+ codigoActividad + "&vieneDePyp=true";// &ind=0";
								this.cerrarConexion(con);
								try {
									response.sendRedirect(pagina);
									return null;
								} catch (Exception e) {
									logger
											.error("Error en accionAccionEjecutar de ProgramasPYPAction: "
													+ e);
								}

							} else
								errores
										.add(
												"Usuario sin permiso",
												new ActionMessage(
														"errors.usuarioSinRolFuncionalidad",
														usuario
																.getLoginUsuario(),
														"Responder Procedimientos"));
						}
						// Si el tipo de autorizacion es externa, es encesario
						// verificar si el usuario actual se encuentre
						// parametrizado en
						else if (tipoAutorizacion
								.equals(ConstantesIntegridadDominio.acronimoExterna)) {
							String usuarioEstaAutorizado = Utilidades
									.verificarregistroUsuarioEntidadSubcontratada(
											con, numeroSolicitud, usuario
													.getLoginUsuario());
							if (!UtilidadTexto.isEmpty(usuarioEstaAutorizado)) {
								if (Utilidades.tieneRolFuncionalidad(con,
										usuario.getLoginUsuario(), 179)) {
									pagina = "../respuestaProcedimientos/respuestaProcedimientos.do?estado=elegirPlantilla&numeroSolicitud="
											+ numeroSolicitud
											+ "&servicio="
											+ codigoActividad
											+ "&vieneDePyp=true";// &ind=0";
									this.cerrarConexion(con);
									try {
										response.sendRedirect(pagina);
										return null;
									} catch (Exception e) {
										logger
												.error("Error en accionAccionEjecutar de ProgramasPYPAction: "
														+ e);
									}

								} else
									errores
											.add(
													"Usuario sin permiso",
													new ActionMessage(
															"errors.usuarioSinRolFuncionalidad",
															usuario
																	.getLoginUsuario(),
															"Responder Procedimientos"));
							} else {
								String nombreEntidadSubcontrada = UtilidadesManejoPaciente
										.consultarDescripcionEntidadSubcontratadaAutorizacionSolicitud(
												con, numeroSolicitud);
								errores
										.add(
												"Usuario no autorizado para responder orden",
												new ActionMessage(
														"error.usuarioNoAutorizadoPararesponderOrdenesEntSub",
														nombreEntidadSubcontrada));
							}
						}
					} else {
						errores.add("Solicitud no autorizada",
								new ActionMessage("error.solicitudAutorizada"));
					}
				}
				// termina la validacion

				else {
					// Se verifica el rol de la funcionalidad RESPONDER
					// CONSULTAS
					/*
					 * if(Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario
					 * (),178)) {
					 */
					// ****CONSULTAR LA FINALIDAD DE LA ACTIVIDAD***************
					String finalidad = consultarFinalidadActividad(con,programasForm);

					pagina = "../valoracionConsulta/valoracion.do?estado=empezar&"
							+ "numeroSolicitud="
							+ numeroSolicitud
							+ "&"
							+ "vieneDePyp=true&"
							+ "ocultarEncabezado=true&"
							+ "codigoEspecialidad="
							+ UtilidadesFacturacion.obtenerEspecialidadServicio(con,Integer.parseInt(codigoActividad)).getCodigo();
					this.cerrarConexion(con);
					if (!finalidad.equals("")&& !finalidad.equals("null@@@@@null")) {
						String[] vector = finalidad.split(ConstantesBD.separadorSplit);
						pagina += "&codigoFinalidadConsulta=" + vector[0]
								+ "&nombreFinalidadConsulta=" + vector[1];

						try {
							response.sendRedirect(pagina);
							return null;
						} catch (Exception e) {
							logger
									.error("Error en accionAccionEjecutar de ProgramasPYPAction: "
											+ e);
						}
					} else
						errores
								.add(
										"Actividad sin finalidad",
										new ActionMessage(
												"error.pyp.programasPYP.actividadSinFinalidad",
												nombreActividad));

					/*
					 * } else errores.add("Usuario sin permiso",new
					 * ActionMessage
					 * ("errors.usuarioSinRolFuncionalidad",usuario.
					 * getLoginUsuario(),"Responder Interconsultas"));
					 */
				}

			} else {

				// Se verifica el rol de la funcionalidad SOLICITAR CONSULTAS
				if (tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta + "")) {
					// Se verifica si se va a confirmar la ejecucion de la
					// actividad
					if (programasForm.isConfirmarEjecucion()) {
						programasForm.setConfirmarEjecucion(false);
						try {

							// ****************SE GENERA SOLICITUD DE
							// CONSULTA*******************************************************
							Cargos cargos = new Cargos();
							cargos.setPyp(true);
							boolean resultado = false;
							int resultado1 = 0;

							// se genera la
							// solicitud--------------------------------------------------
							SolicitudConsultaExterna lsce_solicitud = new SolicitudConsultaExterna();
							lsce_solicitud
									.setCentroCostoSolicitante(new InfoDatosInt(
											paciente.getCodigoArea()));
							// Se ingresa el centro de costo que se selecciono
							// con el cambio por Entidades Subcontratadas
							if (!programasForm.getCentroCostoSeleccionado()
									.equals("")) {
								lsce_solicitud
										.setCentroCostoSolicitado(new InfoDatosInt(
												Utilidades
														.convertirAEntero(programasForm
																.getCentroCostoSeleccionado())));
								logger.info("EL VALOR DEL CC----->"
										+ programasForm
												.getCentroCostoSeleccionado());
							} else {
								lsce_solicitud
										.setCentroCostoSolicitado(new InfoDatosInt(
												usuario.getCodigoCentroCosto()));
								logger.info("EL VALOR DEL CC----->"
										+ usuario.getCodigoCentroCosto());
							}

							int cuentaTemp = paciente.getCodigoCuenta();
							// if(programasForm.isPacienteCuentaCerrada())
							// cuentaTemp =
							// Utilidades.convertirAEntero(programasForm.getUltimaCuentaAsociada().get("cuenta")+"");

							lsce_solicitud.setCobrable(true);
							lsce_solicitud.setCodigoCuenta(cuentaTemp);
							lsce_solicitud.setCodigoServicioSolicitado(Integer
									.parseInt(codigoActividad));
							lsce_solicitud
									.setEspecialidadSolicitante(new InfoDatosInt(
											ConstantesBD.codigoEspecialidadMedicaTodos));
							lsce_solicitud
									.setEstadoHistoriaClinica(new InfoDatosInt(
											ConstantesBD.codigoEstadoHCSolicitada));
							lsce_solicitud.setFechaSolicitud(UtilidadFecha
									.conversionFormatoFechaAAp(new Date()));
							lsce_solicitud.setHoraSolicitud(UtilidadFecha
									.conversionFormatoHoraAAp(new Date()));
							// lsce_solicitud.setNumeroAutorizacion("");
							lsce_solicitud
									.setOcupacionSolicitado(new InfoDatosInt(
											usuario.getCodigoOcupacionMedica()));
							lsce_solicitud.setTipoSolicitud(new InfoDatosInt(
									ConstantesBD.codigoTipoSolicitudCita));
							lsce_solicitud.setUrgente(false);
							lsce_solicitud.setVaAEpicrisis(false);
							lsce_solicitud.setSolPYP(true);
							lsce_solicitud
									.insertarSolicitudConsultaExternaTransaccional(
											con,
											ConstantesBD.continuarTransaccion);
							if (lsce_solicitud.getNumeroSolicitud() > 0)
								resultado = true;

							if (resultado) {
								// Se genera el cargo
								resultado1 = 1;
								// cargo.setEsCita(false); //se dice que no es
								// cita para que pueda generar cargo pendiente
								// en caso de que no haya tarifa
								// cargo.setServicio(Integer.parseInt(codigoActividad));
								// String[]
								// erroresCargo=cargo.generarCargoTransaccional(con,lsce_solicitud.getNumeroSolicitud(),ConstantesBD.codigoCentroCostoConsultaExterna,paciente.getCodigoContrato(),paciente.getEstaContratoVencido(),paciente.getCodigoUltimaViaIngreso(),ConstantesBD.codigoTipoSolicitudCita,usuario.getLoginUsuario(),1,0,Integer.parseInt(codigoActividad),false,"",//aqui
								// van las observaciones cuando se
								// requieranConstantesBD.continuarTransaccion,false,/*utilizarValorTarifaOpcional*/-1/*valorTarifaOpcional*/);

								cargos
										.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(
												con,
												usuario,
												paciente,
												false/* dejarPendiente */,
												lsce_solicitud
														.getNumeroSolicitud(),
												ConstantesBD.codigoTipoSolicitudCita /* codigoTipoSolicitudOPCIONAL */,
												cuentaTemp,
												ConstantesBD.codigoCentroCostoConsultaExterna/* codigoCentroCostoEjecutaOPCIONAL */,
												Integer
														.parseInt(codigoActividad)/* codigoServicioOPCIONAL */,
												1/* cantidadServicioOPCIONAL */,
												ConstantesBD.codigoNuncaValidoDouble/* valorTarifaOPCIONAL */,
												ConstantesBD.codigoNuncaValido /* codigoEvolucionOPCIONAL */,
												/*
												 * "" --
												 * numeroAutorizacionOPCIONAL
												 */
												""/* esPotatil */, false, "", "" /* subCuentaCoberturaOPCIONAL */
										);
								Vector erroresCargo = cargos
										.getInfoErroresCargo()
										.getMensajesErrorDetalle();

								for (int l = 0; l < erroresCargo.size(); l++) {
									// su acontece un error diferente a que no
									// haya tarifa
									// no se puede continuar
									if (!erroresCargo.get(l).toString().equals(
											"error.cargo.noHayValorTarifa"))
										resultado1 = 0;
								}

							}

							// ************************************************************************************************

							// se verifica éxito de la transaccion
							if (resultado && resultado1 > 0) {
								String numeroSolicitud = lsce_solicitud
										.getNumeroSolicitud()
										+ "";
								int resp0 = 0, resp1 = 0, resp2 = 0;
								boolean fueInsertado = false;

								// ****SE VERIFICA EL
								// PROGRAMA************************
								String consecutivoPrograma = programasForm
										.getProgramas("consecutivo_" + posP)
										.toString();
								String consecutivoActividad = "";

								UtilidadBD.iniciarTransaccion(con);
								if (consecutivoPrograma.equals("0")) {
									// No existe programa se debe insertar
									programas.setCampos("codigoPaciente",
											paciente.getCodigoPersona());
									programas
											.setCampos(
													"codigoPrograma",
													programasForm
															.getProgramas("codigo_programa_"
																	+ posP));
									programas.setCampos("usuario", usuario
											.getLoginUsuario());
									programas
											.setCampos(
													"institucion",
													programasForm
															.getProgramas("institucion_"
																	+ posP));
									resp2 = programas.insertarPrograma(con);

									consecutivoPrograma = resp2 + "";
									fueInsertado = true;
								} else
									resp2 = 1;
								// ******************************************************

								programas.clean();
								logger.info("consecutivoPrograma=> "
										+ consecutivoPrograma);
								// *********SE VERIFICA
								// ACTIVIDAD****************************
								if (resp2 > 0) {
									consecutivoActividad = programasForm
											.getActividades(
													"consecutivo_" + pos)
											.toString();
									nombreActividad = programasForm
											.getActividades(
													"nombre_actividad_" + pos)
											.toString();
									estadoActividad = programasForm
											.getActividades(
													"codigo_estado_actividad_"
															+ pos).toString();

									// se parametrizan campos comunes
									programas.clean();
									programas
											.setCampos(
													"estadoPrograma",
													ConstantesBD.codigoEstadoProgramaPYPSolicitado);
									programas.setCampos("numeroSolicitud",
											numeroSolicitud);
									programas.setCampos("fechaSolicitar",
											UtilidadFecha.getFechaActual());
									programas.setCampos("horaSolicitar",
											UtilidadFecha.getHoraActual());
									programas.setCampos("usuarioSolicitar",
											usuario.getLoginUsuario());

									logger.info("consecutivoActividad=> "
											+ consecutivoActividad
											+ ", estadoActividad=> "
											+ estadoActividad);
									// Se verifica si la actividad ya existe o
									// fue ejecutada para el paciente en PYP
									if (consecutivoActividad.equals("0")
											|| estadoActividad
													.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado)
											|| estadoActividad
													.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado)) {
										// ********SE DEBE INSERTAR
										// ACTIVIDAD********************************************
										programas.setCampos(
												"consecutivoPrograma",
												consecutivoPrograma);
										programas
												.setCampos(
														"consecutivoActividad",
														programasForm
																.getActividades("consecutivo_actividad_"
																		+ pos));
										if (!programasForm.getActividades(
												"frecuencia_" + pos).toString()
												.equals(""))
											programas
													.setCampos(
															"frecuencia",
															programasForm
																	.getActividades("frecuencia_"
																			+ pos));
										programas
												.setCampos(
														"centroAtencion",
														usuario
																.getCodigoCentroAtencion());

										resp0 = programas
												.insertarActividad(con);
										consecutivoActividad = resp0 + "";
										resp1 = 1;
										// se verifica resultado transaccion
										// actividad
										if (resp0 <= 0)
											errores
													.add(
															"Error al insertar actividad",
															new ActionMessage(
																	"error.pyp.actividadesPYP.error",
																	"al insertar",
																	nombreActividad));

									} else {
										// ******SE DEBE MODIFICAR LA
										// ACTIVIDAD***************************************
										programas.setCampos(
												"consecutivoActividadPYP",
												consecutivoActividad);

										resp0 = programas
												.modificarActividad(con);
										// se verifica resultado transaccion
										// actividad
										if (resp0 <= 0)
											errores
													.add(
															"Error al modificar actividad",
															new ActionMessage(
																	"error.pyp.actividadesPYP.error",
																	"al actualizar",
																	nombreActividad));

										// si la actividad estaba en estado
										// programado se debe actualizar la
										// orden ambulatoria con el numero de la
										// solicitud
										if (estadoActividad
												.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado)) {
											// ******************SE ACTUALIZA LA
											// ORDEN
											// AMBULATORIA*******************
											HashMap campos = new HashMap();
											campos
													.put(
															"estadoOrden",
															ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
											campos.put("numeroSolicitud",
													numeroSolicitud);
											campos
													.put(
															"numeroOrden",
															programasForm
																	.getActividades("numero_orden_"
																			+ pos));

											resp1 = OrdenesAmbulatorias
													.actualizarSolicitudEnOrdenAmbulatoria(
															con, campos);

											if (resp1 <= 0)
												errores
														.add(
																"Error generando orden ambulatoria",
																new ActionMessage(
																		"errors.noSeGraboInformacion",
																		"AL ACTUALIZAR LA ORDEN AMBULATORIA"));
											else
												// se actualiza permiso del
												// registro
												programasForm
														.setActividades(
																"cancelar_"
																		+ pos,
																ValoresPorDefecto
																		.getValorFalseParaConsultas());

											// ********************************************************************************
										} else
											resp1 = 1;

									}
									// **************************************************************************************
								}

								// /Se verifica el rol de la funcionalidad
								// RESPONDER CONSULTAS
								if (resp0 > 0 && resp1 > 0 && resp2 > 0) {
									UtilidadBD.finalizarTransaccion(con);
									// se actualizan los consecutivos
									programasForm
											.setConsecutivoActividad(consecutivoActividad);
									programasForm
											.setConsecutivoPrograma(consecutivoPrograma);

									// Se actualizan datos del programa en el
									// caso de que haya sido insertado
									if (fueInsertado) {
										programasForm.setProgramas(
												"consecutivo_" + posP,
												consecutivoPrograma);
										programasForm.setProgramas("existe_"
												+ posP, "true");
										programasForm.setProgramas(
												"fecha_inicial_" + posP,
												UtilidadFecha.getFechaActual());
									}

									// Se actualiza registro en el mapa
									programasForm
											.setActividades(
													"codigo_estado_actividad_"
															+ pos,
													programas
															.getCampos("estadoPrograma"));
									programasForm
											.setActividades(
													"numero_solicitud_" + pos,
													programas
															.getCampos("numeroSolicitud"));
									programasForm.setActividades("consecutivo_"
											+ pos, consecutivoActividad);
									programasForm.setActividades("programar_"
											+ pos, "false");
									programasForm.setActividades("existe_"
											+ pos, "true");

									/*
									 * if(Utilidades.tieneRolFuncionalidad(con,usuario
									 * .getLoginUsuario(),178)) {
									 */
									// ****CONSULTAR LA FINALIDAD DE LA
									// ACTIVIDAD***************
									String finalidad = consultarFinalidadActividad(
											con, programasForm);

									String pagina = "../valoracionConsulta/valoracion.do?estado=empezar"
											+ "&numeroSolicitud="
											+ numeroSolicitud
											+ "&vieneDePyp=true"
											+ "&ocultarEncabezado=true"
											+ "&codigoEspecialidad="
											+ UtilidadesFacturacion
													.obtenerEspecialidadServicio(
															con,
															Integer
																	.parseInt(codigoActividad))
													.getCodigo();
									this.cerrarConexion(con);
									if (!finalidad.equals("")
											&& !finalidad
													.equals("null@@@@@null")) {
										String[] vector = finalidad
												.split(ConstantesBD.separadorSplit);
										pagina += "&codigoFinalidadConsulta="
												+ vector[0]
												+ "&nombreFinalidadConsulta="
												+ vector[1];

										try {
											response.sendRedirect(pagina);
											return null;
										} catch (Exception e) {
											logger
													.error("Error en accionAccionEjecutar de ProgramasPYPAction: "
															+ e);
										}
									} else
										errores
												.add(
														"Actividad sin finalidad",
														new ActionMessage(
																"error.pyp.programasPYP.actividadSinFinalidad",
																nombreActividad));

									/*
									 * } else
									 * errores.add("Usuario sin permiso",new
									 * ActionMessage
									 * ("errors.usuarioSinRolFuncionalidad"
									 * ,usuario
									 * .getLoginUsuario(),"Responder Interconsultas"
									 * ));
									 */
								} else
									UtilidadBD.abortarTransaccion(con);

							} else
								errores
										.add(
												"Error generando la solicitud",
												new ActionMessage(
														"error.solicitudgeneral.noGenerada",
														"de consulta, por favor verifique el valor de la tarifa y/o diligencie complejidad"));

						} catch (Exception e) {
							logger
									.error("Error generando la solicitud de consulta externa: "
											+ e);
							errores
									.add(
											"Error generando la solicitud",
											new ActionMessage(
													"error.solicitudgeneral.noGenerada",
													"de consulta, por favor verifique el valor de la tarifa y/o diligencie complejidad"));

						}
					} else {
						// SE redirecciona la confirmacion de la actividad
						this.cerrarConexion(con);
						return mapping.findForward("confirmarEjecucion");
					}

				} else {
					// /Se verifica el rol de la funcionalidad SOLICITAR
					// PROCEDIMIENTOS
					if (Utilidades.tieneRolFuncionalidad(con, usuario
							.getLoginUsuario(), 181)) {
						// ****CONSULTAR LA FINALIDAD DE LA
						// ACTIVIDAD***************
						String finalidad = consultarFinalidadActividad(con,
								programasForm);

						String pagina = "../solicitudes/solicitar.do?estado=insertar&indicativoOrdenAmbulatoria=false&codigoTipoSolicitud="
								+ tipoServicio
								+ "&centrosAtencionPYP="
								+ centrosAtencion
								+ "&postularServicio=true&solPYP=true&codigoServicioPostular="
								+ Utilidades.obtenerCodigoPropietarioServicio(
										con, codigoActividad,
										ConstantesBD.codigoTarifarioCups)
								+ "&accionPYP=ejecutar" 
								+ "&fichaEpidemiologica=true";
						this.cerrarConexion(con);
						if (!finalidad.equals("") && !finalidad.equals("null@@@@@null")) {
							String[] vector = finalidad.split(ConstantesBD.separadorSplit);
							pagina += "&finalidad=" + vector[0]+ "&nombreFinalidad=" + vector[1];
							try {
								response.sendRedirect(pagina);
								return null;
							} catch (Exception e) {
								logger.error("Error en accionAccionEjecutar de ProgramasPYPAction: "+ e);
							}
						} else
							errores.add("Actividad sin finalidad",new ActionMessage("error.pyp.programasPYP.actividadSinFinalidad",nombreActividad));

					} else
						errores.add("Usuario sin permiso", new ActionMessage("errors.usuarioSinRolFuncionalidad", usuario.getLoginUsuario(),"Solicitar Procedimientos"));
				}
			}
		} else
			errores.add("Actividad sin centros de atencion", new ActionMessage("error.pyp.programasPYP.actividadSinCentroAtencion",nombreActividad));

		if (!errores.isEmpty()) {
			programasForm.setEstado("detalleActividades");
			saveErrors(request, errores);
		}

		this.cerrarConexion(con);
		return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		/*************** FIN FLUJO NORMAL DE PYP ********************************************************************/

	}

	/**
	 * Método implementado para verificar que la actividad no se haya ejecutado
	 * el mismo día
	 * 
	 * @param con
	 * @param paciente
	 * @param programasForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward verificarActividadEjecutar(Connection con,
			PersonaBasica paciente, ProgramasPYPForm programasForm,
			ActionMapping mapping, HttpServletRequest request) {
		logger.info("Entró a método VERIFICAR ACTIVIDAD EJECUTAR");
		// ****************SE TOMA INFORMACION
		// BÁSICA********************************************************
		int posP = programasForm.getPosicion();
		int posA = programasForm.getPosicionActividad();

		String codigoPrograma = programasForm.getProgramas(
				"codigo_programa_" + posP).toString();
		String institucion = programasForm.getProgramas("institucion_" + posP)
				.toString();
		String codigoActividad = programasForm.getActividades(
				"consecutivo_actividad_" + posA).toString();
		String nombreActividad = programasForm.getActividades(
				"nombre_actividad_" + posA).toString();
		// **********************************************************************************************************

		// **********SE REALIZA LA
		// CONSULTA*********************************************************
		ProgramasPYP programas = new ProgramasPYP();
		programas.setCampos("codigoPersona", paciente.getCodigoPersona());
		programas.setCampos("codigoPrograma", codigoPrograma);
		programas.setCampos("institucion", institucion);
		programas.setCampos("codigoActividad", codigoActividad);

		boolean existe = programas.permiteEjecutarActividad(con);
		// ****************************************************************************************
		logger.info("EXISTE ACTIVIDAD EJECUYTADA ? " + existe);
		if (existe) {
			
			
			ActionErrors errores = new ActionErrors();
			errores.add("Actividad ya ejecutada", new ActionMessage(
					"error.pyp.programasPYP.actividadYaEjecutada",
					nombreActividad));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		} else
			return null;
	}

	/**
	 * Método implementado para verificar que la actividad no se haya ejecutado
	 * el mismo día
	 * 
	 * @param con
	 * @param paciente
	 * @param programasForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	private ActionForward verificarPermitirEjecutarVariasVeces(Connection con,
			PersonaBasica paciente, ProgramasPYPForm programasForm,
			ActionMapping mapping, HttpServletRequest request) {
		logger.info("Entró a método VERIFICAR ACTIVIDAD EJECUTAR");
		// ****************SE TOMA INFORMACION
		// BÁSICA********************************************************
		int posP = programasForm.getPosicion();
		int posA = programasForm.getPosicionActividad();

		String codigoPrograma = programasForm.getProgramas(
				"codigo_programa_" + posP).toString();
		String institucion = programasForm.getProgramas("institucion_" + posP)
				.toString();
		String codigoActividad = programasForm.getActividades(
				"consecutivo_actividad_" + posA).toString();
		String nombreActividad = programasForm.getActividades(
				"nombre_actividad_" + posA).toString();
		// **********************************************************************************************************

		// **********SE REALIZA LA
		// CONSULTA*********************************************************
		ProgramasPYP programas = new ProgramasPYP();
		programas.setCampos("codigoPersona", paciente.getCodigoPersona());
		programas.setCampos("codigoPrograma", codigoPrograma);
		programas.setCampos("institucion", institucion);
		programas.setCampos("codigoActividad", codigoActividad);

		boolean existe = programas.estaActividadEjecutada(con);
		// ****************************************************************************************
		logger.info("EXISTE ACTIVIDAD EJECUYTADA ? " + existe);
		if (existe) {
			ActionErrors errores = new ActionErrors();
			errores.add("Actividad ya ejecutada", new ActionMessage(
					"error.pyp.programasPYP.actividadYaEjecutada",
					nombreActividad));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		} else
			return null;
	}
	
	
	/**
	 * Método implementado para realizar accion SOLICITAR sobre una actividad
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionAccionSolicitar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request, HttpServletResponse response) throws IPSException {
		ActionErrors errores = new ActionErrors();

		// Se toman datos de la forma
		String codigoActividad = "";
		String nombreActividad = "";
		String acronimoActividad = programasForm.getTipoActividad();
		int pos = programasForm.getPosicionActividad();

		// Se instancia el objeto ProgramasPYP
		ProgramasPYP programas = new ProgramasPYP();
		if (acronimoActividad
				.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
			codigoActividad = programasForm.getActividades(
					"codigo_actividad_" + pos).toString();
			nombreActividad = programasForm.getActividades(
					"nombre_actividad_" + pos).toString();
			programas.setCampos("consecutivoActividad", programasForm
					.getActividades("consecutivo_actividad_" + pos));
		} else {
			codigoActividad = programasForm.getArticulos(
					"codigo_actividad_" + pos).toString();
			nombreActividad = programasForm.getArticulos(
					"nombre_actividad_" + pos).toString();
			programas.setCampos("consecutivoActividad", programasForm
					.getArticulos("consecutivo_actividad_" + pos));
		}
		programas.setCampos("institucion", usuario.getCodigoInstitucion());

		// Validación de los convenios del ingreso
		if (!this.puedoGenerarCargoPyp(con, paciente)) {
			errores.add("", new ActionMessage(
					"error.pyp.programasPYP.ingresoSinConveniosPyp"));
			saveErrors(request, errores);
			this.cerrarConexion(con);
			return mapping.findForward("paginaErroresActionErrors");
		}

		// Se consultan los centros de atención de la actividad
		String centrosAtencion = programas.consultarCentrosAtencionActividad(
				con).replace(",", ConstantesBD.separadorSplit);

		if (acronimoActividad
				.equals(ConstantesBD.acronimoTipoActividadPYPServicio)) {
			// se verifica si habían centros de atencion
			if (!centrosAtencion.equals("")) {

				// Se verifica que el servicio tenga centros de costo
				// parametrizados en la funcionalidad
				// de centros costo x grupo servicio
				if (!UtilidadValidacion
						.existeCentroCostoXGrupoServicioActividad(con,
								programasForm.getActividades(
										"consecutivo_actividad_" + pos)
										.toString(), usuario
										.getCodigoCentroAtencion(), usuario
										.getCodigoInstitucionInt())) {
					errores.add("Actividad sin centros costo x grupo servicio",
							new ActionMessage(
									"error.pyp.actividadesPYP.sinGrupo",
									nombreActividad));
					saveErrors(request, errores);
					this.cerrarConexion(con);
					return mapping.findForward("paginaErroresActionErrors");

				}

				// se obtiene el tipo de servicio
				String tipoServicio = Utilidades.obtenerTipoServicio(con,
						codigoActividad);

				if (tipoServicio
						.equals(ConstantesBD.codigoServicioPartosCesarea + "")
						|| tipoServicio
								.equals(ConstantesBD.codigoServicioPaquetes
										+ "")
						|| tipoServicio
								.equals(ConstantesBD.codigoServicioQuirurgico
										+ "")) {
					// Se verifica el rol de la funcionalidad SOLICITAR CIRUGÍAS
					if (Utilidades.tieneRolFuncionalidad(con, usuario
							.getLoginUsuario(), 326)) {
						this.cerrarConexion(con);
						// en caso de que sea en un pop poner el cerra en la
						// pagina de la solicitud.
						String pagina = "../solicitudesCx/solicitudesCx.do?estado=empezarPYP&indicativoOrdenAmbulatoria=false&postularServicio=true&solPYP=true&codigoServicioPostular="
								+ codigoActividad
								+ "&centrosAtencionPYP="
								+ centrosAtencion;
						try {
							response.sendRedirect(pagina);
							return null;
						} catch (IOException e) {
							logger
									.error("Error en accionAccionSolicitar de ProgramasPYPAction: "
											+ e);
						}
					} else
						errores.add("Usuario sin permiso", new ActionMessage(
								"errors.usuarioSinRolFuncionalidad", usuario
										.getLoginUsuario(),
								"Solicitar Cirugías"));

				} else if (!tipoServicio
						.equals(ConstantesBD.codigoServicioInterconsulta + "")) {
					// /Se verifica el rol de la funcionalidad SOLICITAR
					// PROCEDIMIENTOS
					if (Utilidades.tieneRolFuncionalidad(con, usuario
							.getLoginUsuario(), 181)) {
						// ****CONSULTAR LA FINALIDAD DE LA
						// ACTIVIDAD***************
						String finalidad = consultarFinalidadActividad(con,
								programasForm);

						
						// en caso de que sea en un pop poner el cerra en la
						// pagina de la solicitud.
						String pagina = "../solicitudes/solicitar.do?estado=insertar&indicativoOrdenAmbulatoria=false&codigoTipoSolicitud="
								+ ConstantesBD.codigoServicioProcedimiento
								+ "&centrosAtencionPYP="
								+ centrosAtencion
								+ "&postularServicio=true&solPYP=true&codigoServicioPostular="
								+ Utilidades.obtenerCodigoPropietarioServicio(
										con, codigoActividad,
										ConstantesBD.codigoTarifarioCups)
								+ "&accionPYP=solicitar" +
								  "&fichaEpidemiologica=true";
						
													 

						this.cerrarConexion(con);

						if (!finalidad.equals("")) {
							String[] vector = finalidad
									.split(ConstantesBD.separadorSplit);
							pagina += "&finalidad=" + vector[0]
									+ "&nombreFinalidad=" + vector[1];
						}

						try {
							response.sendRedirect(pagina);
							return null;
						} catch (IOException e) {
							logger
									.error("Error en accionAccionSolicitar de ProgramasPYPAction: "
											+ e);
						}
					} else
						errores.add("Usuario sin permiso", new ActionMessage(
								"errors.usuarioSinRolFuncionalidad", usuario
										.getLoginUsuario(),
								"Solicitar Procedimientos"));
				}
			} else
				errores
						.add(
								"Actividad sin centros de atencion",
								new ActionMessage(
										"error.pyp.programasPYP.actividadSinCentroAtencion",
										nombreActividad));
		} else if (acronimoActividad
				.equals(ConstantesBD.acronimoTipoActividadPYPArticulo)) {
			// Se verifica el rol de la funcionalidad SOLICITAR MEDICAMENTOS
			if (Utilidades.tieneRolFuncionalidad(con,
					usuario.getLoginUsuario(), 165)) {
				this.cerrarConexion(con);
				String pagina = "../solicitarMedicamentos/solicitarMedicamentos.do?estado=empezar&indicativoOrdenAmbulatoria=false&postularArticulo=true&solPYP=true&codigoArticuloPostular="
						+ codigoActividad;
				try {
					response.sendRedirect(pagina);
					return null;
				} catch (IOException e) {
					logger
							.error("Error en accionAccionSolicitar de ProgramasPYPAction: "
									+ e);
				}
			} else
				errores.add("Usuario sin permiso", new ActionMessage(
						"errors.usuarioSinRolFuncionalidad", usuario
								.getLoginUsuario(), "Solicitar Medicamentos"));
		}

		if (!errores.isEmpty()) {
			programasForm.setEstado("detalleActividades");
			saveErrors(request, errores);
		}

		this.cerrarConexion(con);
		return mapping.findForward("paginaErroresActionErrors");
	}

	/**
	 * Método que verifica si puedo generar cargo pyp
	 * 
	 * @param con
	 * @param paciente
	 */
	private boolean puedoGenerarCargoPyp(Connection con, PersonaBasica paciente) throws IPSException {
		boolean sePuede = true;

		if (paciente.getCodigoIngreso() > 0) {
			// Se consultan los responsables PYP del ingreso
			ArrayList<DtoSubCuentas> dtoSubCuentasVector = UtilidadesHistoriaClinica
					.obtenerResponsablesIngreso(con, paciente
							.getCodigoIngreso(), false, new String[0], true,
							"" /* subCuenta */, ConstantesBD.codigoNuncaValido /*
																				 * Vía
																				 * de
																				 * ingreso
																				 */);

			// si el ingreso no tiene convenios pyp, no se puede generar el
			// cargo
			if (dtoSubCuentasVector.size() <= 0)
				sePuede = false;
		}

		return sePuede;
	}

	/**
	 * Método implementado para realizar accion PROGRAMAR sobre una actividad
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionAccionProgramar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) {
		programasForm.resetProgramacion();
		// se asigna la fecha del sistema
		programasForm.setFechaProgramacion(UtilidadFecha.getFechaActual());
		programasForm.setHoraProgramacion(UtilidadFecha.getHoraActual());
		programasForm.setCodigoMedico(usuario.getCodigoPersona() + "");
		programasForm.setInstitucion(usuario.getCodigoInstitucion());

		// ********CONSULTA DE LA FINALIDAD*********************
		int pos = programasForm.getPosicionActividad();
		if (programasForm.getTipoActividad().equals(
				ConstantesBD.acronimoTipoActividadPYPServicio)) {
			String tipoServicio = Utilidades.obtenerTipoServicio(con,
					programasForm.getActividades("codigo_actividad_" + pos)
							.toString());
			if (!tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta
					+ ""))
				programasForm.setFinalidadServicio(consultarFinalidadActividad(
						con, programasForm));
		}
		// ******SE VERIFICA SI ES MEDICAMENTO******************
		else {
			programasForm.setMedicamento(UtilidadValidacion.esMedicamento(con,
					Integer.parseInt(programasForm.getArticulos(
							"codigo_actividad_" + pos).toString())));
		}

		this.cerrarConexion(con);
		return mapping.findForward("programarActividad");
	}

	/**
	 * Método implementado para consultar la finalidad de la actividad
	 * 
	 * @param con
	 * @param programasForm
	 * @return
	 */
	private String consultarFinalidadActividad(Connection con,
			ProgramasPYPForm programasForm) {
		int posP = programasForm.getPosicion();
		int posA = programasForm.getPosicionActividad();
		String finalidad = "";

		if (programasForm.getTipoActividad().equals(
				ConstantesBD.acronimoTipoActividadPYPServicio)) {
			ProgramasPYP programas = new ProgramasPYP();
			programas.setCampos("codigoPrograma", programasForm
					.getProgramas("codigo_programa_" + posP));
			programas.setCampos("institucion", programasForm
					.getProgramas("institucion_" + posP));
			programas.setCampos("consecutivoActividad", programasForm
					.getActividades("consecutivo_actividad_" + posA));
			programas.setCampos("tipoServicio", Utilidades.obtenerTipoServicio(
					con, programasForm.getActividades(
							"codigo_actividad_" + posA).toString()));

			finalidad = programas.consultarFinalidadActividad(con);
		}

		return finalidad;
	}

	/**
	 * Método implementado para listar las actividades de un programa
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionDetalleActividades(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente) {
		
		// se toma la posicion del registro programa seleccionado
		int pos = programasForm.getPosicion();
		boolean consultaExterna = programasForm.isConsultaExterna();
		logger.info("posicion MAPA PROGRAMAS detalleActividades=> "+ programasForm.getPosicion());
		logger.info("posicion ACTIVIDAD detalleActividades=> "+ programasForm.getPosicionActividad());

		// Se instancia objeto de programasPYP
		ProgramasPYP programas = new ProgramasPYP();

		// se asigna los datos del programa
		programas.setCampos("consecutivoPrograma", programasForm.getProgramas("consecutivo_" + pos));
		programas.setCampos("codigoPrograma", programasForm.getProgramas("codigo_programa_" + pos));
		programas.setCampos("listadoConvenios", programasForm.getListadoConvenios());

		// ********SE CONSULTAN LAS ACTIVIDADES DEL PROGRAMA
		// *****************************************
		// SE VERIFICA SI ES CONSULTA
		// EXTERNA---------------------------------------------
		logger.info("ES CONSULTA EXTERNA? " + consultaExterna);

		
		if (consultaExterna) {
			/****** FLUJO CONSULTA EXTERNA DE LA FUNCIONALIDAD PROGRAMAS PYP *************************/
			String numSolConsulta = programasForm.getNumSolConsulta();


			logger.info("**Valor numSolConsulta: " + programasForm.getNumSolConsulta());

			try {
				Solicitud solicitud = new Solicitud();
				
				// se carga la solicitud de consulta
				solicitud.cargar(con, Integer.parseInt(numSolConsulta));
				
				logger.info("**Valor getCodigoCuenta: " + solicitud.getCodigoCuenta());
				
				// se carga la cuenta de la solicitud
				Cuenta cuenta = new Cuenta();
				cuenta.cargarCuenta(con, solicitud.getCodigoCuenta() + "");

				// se carga la informacion que se necesitaba
				programas.setCampos("tipoRegimen", programasForm.getListadoTiposRegimen());

				paciente.setCodigoCuenta(solicitud.getCodigoCuenta());
				paciente.setCodigoUltimaViaIngreso(Integer.parseInt(cuenta.getCodigoViaIngreso()));

			} catch (SQLException e) {
				logger.error("Error cargando informacion de la solicitud Consulta en accionDetalleActividades de ProgramacionPYPAction: "+ e);
			}
			// Se cargan las actividades
			programasForm.setActividades(programas.consultarActividadesProgramaPaciente(con, paciente,usuario, true, programasForm.isPacienteCuentaCerrada(), programasForm.getUltimaCuentaAsociada()));
			programasForm.setNumActividades(Integer.parseInt(programasForm.getActividades("numRegistros").toString()));

			// Se verifica si hay alguna actividad que tenga la solicitud
			// consulta para inhabilitarle la accion ejecutar
			if (Utilidades.esSolicitudPYP(con, Integer.parseInt(numSolConsulta))) {
				for (int i = 0; i < programasForm.getNumActividades(); i++) {
					String aux = programasForm.getActividades("numero_solicitud_" + i).toString();
					// se inhabilita el ejecutar
					if (aux.equals(numSolConsulta))
						programasForm.setActividades("ejecutar_" + i, "false");
				}
			}
			/****** FIN FLUJO CONSULTA EXTERNA DE LA FUNCIONALIDAD PROGRAMAS PYP *************************/
		} else {
			/****** FLUJO NORMAL DE LA FUNCIONALIDAD PROGRAMAS PYP *************************/
			// Se verifica si el paciente tiene cuenta Activa y su convenio es
			// PYP
			if (programasForm.isCuentaActiva()&& programasForm.isEsConvenioPYP()) {
				programas.setCampos("tipoRegimen", programasForm.getListadoTiposRegimen());
				programasForm.setActividades(programas.consultarActividadesProgramaPaciente(con, paciente,usuario, true, programasForm.isPacienteCuentaCerrada(),programasForm.getUltimaCuentaAsociada()));
			} else {
				programasForm.setActividades(programas.consultarActividadesProgramaPaciente(con, paciente,usuario, false, programasForm.isPacienteCuentaCerrada(),programasForm.getUltimaCuentaAsociada()));
			}
			programasForm.setNumActividades(Integer.parseInt(programasForm.getActividades("numRegistros").toString()));
			/****** FIN FLUJO NORMAL DE LA FUNCIONALIDAD PROGRAMAS PYP *************************/
		}
		// *************************************************************************************************************

		// *****SE CONSULTAN LOS ARTICULOS DEL
		// PROGRAMA***********************************************************
		programasForm.setArticulos(programas.consultarArticulosProgramaPaciente(con, paciente, usuario));
		programasForm.setNumArticulos(Integer.parseInt(programasForm.getArticulos("numRegistros").toString()));
		// *******************************************************************************************************

		this.cerrarConexion(con);
		return mapping.findForward("detalleActividades");
	}

	/**
	 * Método implementado para modificar programas PYP de un paciente
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private ActionForward accionGuardarProgramas(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			UsuarioBasico usuario, PersonaBasica paciente,
			HttpServletRequest request) {
		// variables auxiliares
		String auxS0 = "";
		int resp = 0;
		ActionErrors errores = new ActionErrors();

		// Se instancia objeto de ProgramasPYP
		ProgramasPYP programas = new ProgramasPYP();

		UtilidadBD.iniciarTransaccion(con);
		for (int i = 0; i < programasForm.getNumProgramas(); i++) {
			auxS0 = programasForm.getProgramas("consecutivo_" + i).toString();

			// Se verifica estado del registro
			if (!auxS0.equals("0")) {
				// *****MODIFICACION DE REGISTRO******************************
				this.llenarMundo(programas, programasForm, i, usuario,paciente);

				// Se consulta registro BD
				HashMap registroDB = programas.consultarPrograma(con);

				// Se verifica si el registro fue modificado
				if (this.fueModificado(registroDB, programasForm, i)) {

					resp = programas.modificarPrograma(con);
					if (resp <= 0)
						errores.add("Error modificando programas",new ActionMessage("error.pyp.programasPYP.error","al modificar",programasForm.getProgramas("nombre_programa_"+ i)));

				}
				// ***********************************************************
			}
		}

		if (errores.isEmpty()) {
			UtilidadBD.finalizarTransaccion(con);
			return accionEmpezar(con, programasForm, mapping, paciente,usuario, request);
		} else {
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, errores);
			programasForm.setEstado("empezar");
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}

	}

	/**
	 * Método que verifica si un programa fue modificado
	 * 
	 * @param registroDB
	 * @param programasForm
	 * @param pos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean fueModificado(HashMap registroDB,
			ProgramasPYPForm programasForm, int pos) {
		boolean fueModificado = false;

		// Se verifica fecha Final
		String aux = registroDB.get("fecha_final_0").toString();
		String aux1 = programasForm.getProgramas("fecha_final_" + pos)
				.toString();
		if (!aux.equals(aux1))
			fueModificado = true;

		// Se verifica motivo finalizacion
		aux = registroDB.get("motivo_finalizacion_0").toString();
		aux1 = programasForm.getProgramas("motivo_finalizacion_" + pos)
				.toString();
		if (!aux.equals(aux1))
			fueModificado = true;

		return fueModificado;
	}

	/**
	 * Método implementado para cargar al mundo los datos de la forma
	 * relacionados con los programas PYP
	 * 
	 * @param programas
	 * @param programasForm
	 * @param pos
	 * @param paciente
	 * @param usuario
	 */
	private void llenarMundo(ProgramasPYP programas,
			ProgramasPYPForm programasForm, int pos, UsuarioBasico usuario,
			PersonaBasica paciente) {
		programas.setCampos("consecutivo", programasForm
				.getProgramas("consecutivo_" + pos));
		programas.setCampos("codigoPaciente", paciente.getCodigoPersona());
		programas.setCampos("codigoPrograma", programasForm
				.getProgramas("consecutivo_" + pos));
		programas.setCampos("usuario", usuario.getLoginUsuario());
		programas.setCampos("institucion", programasForm
				.getProgramas("institucion_" + pos));
		programas.setCampos("fechaFinal", programasForm
				.getProgramas("fecha_final_" + pos));
		programas.setCampos("motivoFinalizacion", programasForm
				.getProgramas("motivo_finalizacion_" + pos));
		programas.setCampos("usuarioFinaliza", usuario.getLoginUsuario());

	}

	/**
	 * Método implementado para iniciar el flujo de la funcionalidad, efectuando
	 * las consultas y valdiaciones de los programas respectivos del paciente
	 * 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,
			ProgramasPYPForm programasForm, ActionMapping mapping,
			PersonaBasica paciente, UsuarioBasico usuario,
			HttpServletRequest request) {
		
		// logger.info("MAPA ACTIVIDAD accionEmpezar=> "+programasForm.getActividades());
		logger.info("consecutivo ACTIVIDAD accionEmpezar=> "+ programasForm.getConsecutivoActividad());
		logger.info("consecutivo Programa accionEmpezar=> "+ programasForm.getConsecutivoPrograma());

		// Se verifica lo de resumen de atenciones**************************
		boolean soloConsulta = false;
		if (programasForm.getEstado().equals("resumenAtenciones")) {
			soloConsulta = true;
			programasForm.setEstado("empezar");
		}
		// ******************************************************************

		// Se verifica si es de consulta externa****************************
		boolean consultaExterna = false;
		if (programasForm.getEstado().equals("empezarConsultaExterna")) {
			consultaExterna = true;
			programasForm.setEstado("empezar");
		}
		// **************************************************************

		if (programasForm.getEstado().equals("empezar")) {

			// se conservan datos en el caso de que sea pyp consulta externa
			String numSolPYP = programasForm.getNumSolConsulta();
			String unificarPYP = programasForm.getUnificarPYP();

			programasForm.reset();

			programasForm.setNumSolConsulta(numSolPYP);
			programasForm.setUnificarPYP(unificarPYP);
			programasForm.setConsultaExterna(consultaExterna);
			programasForm.setSoloConsulta(soloConsulta);
			programasForm.setEstado("empezar");

			// Se consulta si el paciente tiene una ultima atención facturada;
			// con un convenio checkeado como pyp
			programasForm.setUltimaCuentaAsociada(UtilidadesFacturacion.obtenerInfoUltimaCuentaFacturada(con, paciente.getCodigoPersona(), true));

			// validacion del cabezote
			String ocultarCabezote = request.getParameter("ocultarCabezote");
			if (util.UtilidadCadena.noEsVacio(ocultarCabezote)&& !ocultarCabezote.equals("false"))
				programasForm.setOcultarCabezote(true);
			else
				programasForm.setOcultarCabezote(false);

			// ********VALIDACIONES
			// INICIALES********************************************************

			// Se hace la validacion estandar del paciente
			RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente, programasForm.getUltimaCuentaAsociada().get("cuenta")+ "");

			logger.info("GIO 1");
			if (!resp.puedoSeguir)
			{
				logger.info("GIO 2");
				programasForm.setCuentaActiva(false);
			}
			else if (!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				logger.info("GIO 3");
				programasForm.setCuentaActiva(false);
			} 
			else 
			{
				logger.info("GIO 4");
				obtenerConveniosPyp(con, programasForm, paciente.getCodigoIngreso()+ "");
				// Se verifica estado de la cuenta
				programasForm.setCuentaActiva(true);
				// Se consultan los convenios pyp
				obtenerConveniosPyp(con, programasForm, paciente.getCodigoIngreso()+ "");
			}
			// ****************************************************************************************
			
			if(paciente.getCodigoCuenta()<=0 && !UtilidadTexto.isEmpty(programasForm.getUltimaCuentaAsociada().get("cuenta")+"")){
				logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nGIOOOOOOOOOOOOOOOOOOOOOOO");
				programasForm.setPacienteCuentaCerrada(true);
				obtenerConveniosPyp(con, programasForm, programasForm.getUltimaCuentaAsociada().get("ingreso")+ "");
				logger.info("GIO 5");
				if(!programasForm.isConsultaExterna())
					programasForm.setCuentaActiva(true);
			}
		}

		// *******CONSULTA DE LOS PROGRAMAS CALIFICADOS POR EL PACIENTE********************************
		// Se instancia objeto de ProgramasPYP
		ProgramasPYP programas = new ProgramasPYP();

		// SE VERIFICA SI ES DE CONSULTA EXTERNA
		logger.info("Es consulta externa? "+ programasForm.isConsultaExterna());
		if (programasForm.isConsultaExterna()) {

			/******** FLUJO CONSULTAR EXTERNA DE PYP ********************************************************************************/
			String numSolConsulta = programasForm.getNumSolConsulta();

			try {
				Solicitud solicitud = new Solicitud();
				// se carga la solicitud de consulta
				solicitud.cargar(con, Integer.parseInt(numSolConsulta));
				// se carga la cuenta de la solicitud
				Cuenta cuenta = new Cuenta();
				cuenta.cargarCuenta(con, solicitud.getCodigoCuenta() + "");

				// se carga la informacion que se necesitaba

				paciente.setCodigoCuenta(solicitud.getCodigoCuenta());
				paciente.setCodigoUltimaViaIngreso(Integer.parseInt(cuenta.getCodigoViaIngreso()));

				// se consultan los convenios pyp
				obtenerConveniosPyp(con, programasForm, cuenta.getCodigoIngreso()+ "");

			} catch (SQLException e) {
				logger.error("Error cargando informacion de la solicitud Consulta en accionDetalleActividades de ProgramacionPYPAction: "+ e);
			}
			programasForm.setProgramas(programas.consultarProgramasPaciente(
					con, paciente, usuario,
					programasForm.getListadoConvenios(), true, programasForm.getUltimaCuentaAsociada(), 
					programasForm.isPacienteCuentaCerrada()));
			
			programasForm.setNumProgramas(Integer.parseInt(programasForm.getProgramas("numRegistros").toString()));
			/******** FIN FLUJO CONSULTAR EXTERNA DE PYP ********************************************************************************/
		} else {
			/******** FLUJO NORMAL DE PYP ********************************************************************************/
			// Si la cuenta está activa y el convenio es por PYP se consulta los
			// programas por convenio

			logger.info("isCuentaActiva " + programasForm.isCuentaActiva());
			logger.info("isEsConvenioPYP " + programasForm.isEsConvenioPYP());

			if (programasForm.isCuentaActiva()
					&& programasForm.isEsConvenioPYP())
				programasForm.setProgramas(programas.consultarProgramasPaciente(con, paciente, usuario,
								programasForm.getListadoConvenios(), true,
								programasForm.getUltimaCuentaAsociada(),
								programasForm.isPacienteCuentaCerrada()));
			else

				programasForm.setProgramas(programas.consultarProgramasPaciente(con, paciente, usuario,
								programasForm.getListadoConvenios(), false,
								programasForm.getUltimaCuentaAsociada(),
								programasForm.isPacienteCuentaCerrada()));

			programasForm.setNumProgramas(Integer.parseInt(programasForm.getProgramas("numRegistros").toString()));
			/******** FIN FLUJO NORMAL DE PYP ********************************************************************************/
		}

		// se verifica si hay programas existentes
		programasForm.setProgramasExistentes(this
				.verificarProgramasExistentes(programasForm));

		// se asigna el centro de atencion del paciente
		programasForm.setCentroAtencionPaciente(paciente
				.getNombreCentroAtencionPYP());
		// ********************************************************************************************

		// ****RECUPERACION DE POSICION EN EL CASO DE QUE SE HAYA HECHO UNA
		// RECARGA*****************
		this.recuperarPosiciones(programasForm);
		// /****************************************************************************************

		logger.info("consecutivo ACTIVIDAD accionEmpezar(2)=> "+ programasForm.getConsecutivoActividad());
		logger.info("consecutivo Programa accionEmpezar(2)=> "+ programasForm.getConsecutivoPrograma());

		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para consultar los convenios pyp de un ingreso
	 * 
	 * @param con
	 * @param programasForm
	 * @param idIngreso
	 */
	@SuppressWarnings("rawtypes")
	private void obtenerConveniosPyp(Connection con,
			ProgramasPYPForm programasForm, String idIngreso) {
		String listadoConvenios = "", listadoTiposRegimen = "";
		ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String, Object>>();
		convenios = UtilidadesHistoriaClinica.obtenerConveniosPypIngreso(con,
				idIngreso);

		if (convenios.size() > 0) {
			for (int i = 0; i < convenios.size(); i++) {
				if (!listadoConvenios.equals(""))
					listadoConvenios += ",";
				listadoConvenios += ((HashMap) convenios.get(i)).get("codigo")
						.toString();

				if (!listadoTiposRegimen.equals(""))
					listadoTiposRegimen += ",";
				listadoTiposRegimen += "'"
						+ ((HashMap) convenios.get(i)).get("tipoRegimen")
								.toString() + "'";
			}

			programasForm.setEsConvenioPYP(true);
			programasForm.setListadoConvenios(listadoConvenios);
			programasForm.setListadoTiposRegimen(listadoTiposRegimen);

		} else {
			programasForm.setEsConvenioPYP(false);
			programasForm.setListadoConvenios("");
			programasForm.setListadoTiposRegimen("");
		}

	}

	/**
	 * Método impelementado para recuperar las posiciones de los registros
	 * activos seleccionados tanto de programa como de actividad
	 * 
	 * @param forma
	 */
	private void recuperarPosiciones(ProgramasPYPForm forma) {
		// Recuperacion de la posicion del programa
		String aux = "";
		for (int i = 0; i < forma.getNumProgramas(); i++) {
			aux = forma.getProgramas("consecutivo_" + i).toString();
			if (forma.getConsecutivoPrograma().equals(aux)) {
				forma.setPosicion(i);
				logger.info("Encontr+o consecutivoPrograma en pos=> " + i);
			}
		}

		// Recuperar la posicion de la actividad
		boolean encontro = false;
		// 1) Acitvidades
		for (int i = 0; i < forma.getNumActividades(); i++) {
			aux = forma.getActividades("consecutivo_" + i).toString();
			if (forma.getConsecutivoActividad().equals(aux)) {
				logger.info("Encontr+o consecutivoActividad en pos=> " + i);
				forma.setPosicionActividad(i);
				encontro = true;
			}
		}

		// 2) Articulos (SOLO EN EL CASO SI NO SE ENCONTRÓ EN ACTIVIDADES)
		if (!encontro) {
			for (int i = 0; i < forma.getNumArticulos(); i++) {
				aux = forma.getArticulos("consecutivo_" + i).toString();
				if (forma.getConsecutivoActividad().equals(aux)) {
					logger.info("Encontr+o consecutivoArticulo en pos=> " + i);
					forma.setPosicionActividad(i);
					encontro = true;
				}
			}
		}

	}

	/**
	 * Método que verifica si el paciente tiene programas existentes
	 * 
	 * @param programasForm
	 * @return
	 */
	private boolean verificarProgramasExistentes(ProgramasPYPForm programasForm) {
		boolean existentes = false;

		for (int i = 0; i < programasForm.getNumProgramas(); i++) {
			if (UtilidadTexto.getBoolean(programasForm.getProgramas(
					"existe_" + i).toString())
					&& !UtilidadTexto.getBoolean(programasForm.getProgramas(
							"finalizado_" + i).toString()))
				existentes = true;
		}

		return existentes;
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo recursos), usado ante
	 * todo al momento de hacer un forward
	 * 
	 * @param con
	 *            Conexión con la fuente de datos
	 */
	private void cerrarConexion(Connection con) {
		try {
			UtilidadBD.closeConnection(con);
		} catch (Exception e) {
			logger
					.error(e
							+ "Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: "
							+ e.toString());
		}
	}

	
	/**
	 * 
	 * @param con
	 * @param paciente
	 * @param profesional 
	 * @param request
	 * @param mapa
	 * @param programasForm 
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ejecutarActividadCuentaCerrada(Connection con, PersonaBasica paciente, UsuarioBasico profesional, HttpServletRequest request, HashMap mapa, ProgramasPYPForm programasForm, ActionErrors errores) throws NumberFormatException, IPSException 
	{
		Utilidades.imprimirMapa(mapa);
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		logger.info("numRegistros "+numRegistros);
		if(numRegistros>0)
		{
			ProgramasPYP programas = new ProgramasPYP();
			//Inserción de la solicitud de ordenes ambulatorias
			OrdenesAmbulatorias orden = new OrdenesAmbulatorias();
			//registro de programas ya ingresados
			Vector registro = new Vector();
			
			Cuenta mundoCuenta = new Cuenta();
			Utilidades.imprimirMapa(programasForm.getUltimaCuentaAsociada());
			mundoCuenta.cargarCuenta(con, programasForm.getUltimaCuentaAsociada().get("cuenta")+"");
			
			for(int i=0;i<numRegistros;i++)
			{
				int resp0 = 0,resp1= 0,resp2=0,resp3=0,resp4=0;
				programas.clean();
				String consecutivoPrograma = mapa.get("consecutivoTablaProg_"+i).toString();
				String consecutivoActividad = mapa.get("consecutivoTablaAct_"+i).toString();
				String estadoActividad = mapa.get("estadoActividad_"+i).toString();
				String numeroOrden="";
				
				//******SE VERIFICA SI YA SE EJECUTÓ LA ACTIVIDAD********************************+
				programas.setCampos("codigoPersona",paciente.getCodigoPersona());
				programas.setCampos("codigoPrograma",mapa.get("codigoPrograma_"+i));
				programas.setCampos("institucion",mapa.get("institucionPrograma_"+i));
				programas.setCampos("codigoActividad",mapa.get("consecutivoActividad_"+i));
				
				//Se verifica si es de unificar PYP
				//if(UtilidadTexto.getBoolean(mapa.get("unificarPYP_"+i).toString())){
				//	/*******UNIFICAR PYP SI*************************************************************************/
				//} else {

					/*******UNIFICAR PYP NO*************************************************************************/
					//si no existe la actividad o está en estado diferente a solicitada
					logger.info("SECCION UNIFICAR NO===============consecutivoActividad=> "+consecutivoActividad+", estadoActividad=>"+estadoActividad);
					if(consecutivoActividad.equals("0")||!estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado))
					{
						logger.info("El estado de la actividad es=> "+estadoActividad);
						logger.info("SE INSERTA LA ORDEN AMBULATORIA CUENTA CERRADA");
						
						//1) insertar Orden Ambulatoria************************************************
						//Si ya está programada no se debe ingresar nueva orden ambulatoria
						if(!estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado))
						{
							
							orden.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios+"");
							orden.setInstitucion(profesional.getCodigoInstitucionInt());
							orden.setCodigoPaciente(paciente.getCodigoPersona()+"");
							orden.setPyp(true);
							orden.setUrgente(false);
							orden.setCentroAtencion(profesional.getCodigoCentroAtencion()+"");
							orden.setProfesional(profesional.getCodigoPersona()+"");
							orden.setLoginUsuario(profesional.getLoginUsuario());
							orden.setEspecialidad(mapa.get("especialidad_"+i).toString()); 
							orden.setFechaOrden(UtilidadFecha.getFechaActual());
							orden.setHora(UtilidadFecha.getHoraActual());
							orden.setObservaciones("");
							orden.setEstadoOrden(ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+"");
							orden.setConsultaExterna(true);
							orden.setUsuarioConfirma(profesional.getLoginUsuario());
							orden.setFechaConfirma(UtilidadFecha.getFechaActual());
							orden.setHoraConfirma(UtilidadFecha.getHoraActual());
							orden.setCentroCostoSolicita(profesional.getCodigoCentroCosto()+"");
							
							orden.setIdIngresoPaciente(Utilidades.convertirAEntero(programasForm.getUltimaCuentaAsociada().get("ingreso")+""));
							
							HashMap mapaOrden=new HashMap();
							//simulacion de la parte de servicios
							mapaOrden.put("codigo_0",mapa.get("codigoActividad_"+i));
							mapaOrden.put("finalidad_0",mapa.get("finalidadServicio_"+i).toString()); 
							mapaOrden.put("cantidad_0","1");
							mapaOrden.put("urgente_0",ValoresPorDefecto.getValorFalseParaConsultas());
							mapaOrden.put("numRegistros","1");
							
							mapaOrden.put("descripcionServicio_0", programasForm.getActividades("nombre_actividad_" + i));
							
							orden.setServicios(mapaOrden);
							
							if(Double.parseDouble(orden.guardarOrdenAmbulatoria(con,false, profesional, paciente, request, errores)[0])>0)
							{
								logger.info("\n\n\n\nSE INSERTAN DATOS ADICIONALES DE LA ORDEN AMBULATORIA  giooo");
								
								resp0=1;
								//se consulta el código del número de orden resultante
								numeroOrden = Utilidades.obtenerCodigoOrdenAmbulatoria(con,orden.getNumeroOrden(),profesional.getCodigoInstitucionInt());
								logger.info("numeroOrden "+numeroOrden);
								
								
								//**********SE INSERTAN DATOS ADICIONALES DE LA ORDEN AMBULATORIA************************
								HashMap mapaRef = new HashMap();
								String[] vector;
								
								if(mapa.get("diagnostico_"+i)!=null&&!mapa.get("diagnostico_"+i).toString().equals(""))
								{
									vector = mapa.get("diagnostico_"+i).toString().split(ConstantesBD.separadorSplit);
									mapaRef.put("acronimoDiag",vector[0]);
									mapaRef.put("tipoCieDiag",vector[1]);
									
									//*****SE GENERA LA RESPUESTA DE EPIDEMIOLOGÍA********************+
									resp0 = Utilidades.actualizarDatosEpidemiologia(
										con, 
										mapa.get("diagnostico_"+i).toString(),
										mapa.get("valorFicha_"+i)==null?"":mapa.get("valorFicha_"+i).toString(),
										"0", 
										paciente, 
										profesional);
									//******************************************************************
								}
								if(mapa.get("finalidad_"+i)!=null&&!mapa.get("finalidad_"+i).toString().equals(""))
								{
									vector = mapa.get("finalidad_"+i).toString().split(ConstantesBD.separadorSplit);
									mapaRef.put("finalidad",vector[0]);
								}	
								if(mapa.get("causaExterna_"+i)!=null&&!mapa.get("causaExterna_"+i).toString().equals(""))
								{
									vector = mapa.get("causaExterna_"+i).toString().split(ConstantesBD.separadorSplit);
									mapaRef.put("causaExterna",vector[0]);
								}	
								if(mapa.get("respuesta_"+i)!=null)
									mapaRef.put("resultados",mapa.get("respuesta_"+i));
								mapaRef.put("numeroOrden",numeroOrden);
								//mapaRef.put("numSolConsulta",numSolConsulta);
								
								//if(resp0>0)
									resp0 = OrdenesAmbulatorias.ingresarInformacionReferenciaExterna(con,mapaRef);
								//******************************************************************************
								//}
							} 
							else
							{
								logger.info("\n\n\n\n\n\n\n\n\n NUMERO ORDEN "+numeroOrden);
								numeroOrden = mapa.get("numeroOrdenActividad_"+i).toString();
								logger.info("numeroOrdenActividad "+mapa.get("numeroOrdenActividad_"+i));
								resp0 = 1;
							}
							//*****************************************************************************/**
						}
					}
						
					
					//2) Se inserta el programa********************************** 
					if(consecutivoPrograma.equals("0"))
					{
						logger.info("SE INSERTA EL PROGRAMA PYP CUENTA CERRADA / consecutivoPrograma "+consecutivoPrograma);
						
						String codigoPrograma = mapa.get("codigoPrograma_"+i).toString();
						String institucionPrograma = mapa.get("institucionPrograma_"+i).toString();
						
						//Se verifica si el programa ya fue insertado, si ya fue insertado
						//retorna el consecutivo del programa si no retorna una cdena vacía
						consecutivoPrograma = fueInsertadoPrograma(con,registro,codigoPrograma,institucionPrograma,paciente.getCodigoPersona()+""); 
						
						logger.info("consecutivoPrograma "+consecutivoPrograma);
						if(consecutivoPrograma.equals(""))
						{
							//No existe programa se debe insertar
							programas.setCampos("codigoPaciente",paciente.getCodigoPersona());
							programas.setCampos("codigoPrograma",codigoPrograma);
							programas.setCampos("usuario",mapa.get("usuarioPrograma_"+i));
							programas.setCampos("institucion",institucionPrograma);
							resp1 = programas.insertarPrograma(con);
							
							consecutivoPrograma = resp1 + "";
							registro.add(programas.getCampos("codigoPrograma")+ConstantesBD.separadorSplit+programas.getCampos("institucion")+ConstantesBD.separadorSplit+consecutivoPrograma);
						}
						else
							resp1 = 1;
					} else
							resp1 = 1;
						
					
					
					//3) Se inserta la actividad************************************
					programas.clean();
					if(resp1>0)
					{	
						logger.info("SE INSERTA LA ACTIVIDAD PYP CUENTA CERRADA");
						
						//se parametrizan campos comunes
						programas.setCampos("estadoPrograma",ConstantesBD.codigoEstadoProgramaPYPEjecutado);
						programas.setCampos("numeroOrden",numeroOrden);
						programas.setCampos("fechaEjecutar",UtilidadFecha.getFechaActual());
						programas.setCampos("horaEjecutar",UtilidadFecha.getHoraActual());
						programas.setCampos("usuarioEjecutar",mapa.get("usuarioActividad_"+i));
						
						logger.info("consecutivoActividad=> "+consecutivoActividad+", estadoActividad=> "+estadoActividad);
						///Se verifica si la actividad ya existe o fue ejecutada para el paciente en PYP
						if(consecutivoActividad.equals("0")
							||estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado)
							||estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado))
						{
							//********SE DEBE INSERTAR ACTIVIDAD********************************************
							programas.setCampos("consecutivoPrograma",consecutivoPrograma);
							programas.setCampos("consecutivoActividad",mapa.get("consecutivoActividad_"+i));
							if(mapa.get("frecuenciaActividad_"+i)!=null)
								programas.setCampos("frecuencia",mapa.get("frecuenciaActividad_"+i));
							programas.setCampos("centroAtencion",mapa.get("centroAtencionActividad_"+i));
							
							resp2 = programas.insertarActividad(con);
							consecutivoActividad = resp2 + "";
							resp3 = 1;	
						}
						else
						{
							logger.info("NUMERO ORDEN: "+numeroOrden);
							
							//******SE DEBE MODIFICAR LA ACTIVIDAD***************************************
							programas.setCampos("consecutivoActividadPYP",consecutivoActividad);
							
							resp2 = programas.modificarActividad(con);
							logger.info("\n\n\n\n\n\nSe modificó la actividad!!!! "+resp2);
							
							// si la actividad estaba en estado programado se debe
							// actualizar la orden ambulatoria con el numero de la solicitud
							/*if (estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado)) {
								// ******************SE ACTUALIZA LA ORDEN
								// AMBULATORIA*******************
								HashMap campos = new HashMap();
								campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
								campos.put("numeroSolicitud", numeroSolicitud);
								campos.put("numeroOrden", programasForm.getActividades("numero_orden_" + posA));
								campos.put("usuario", usuario.getLoginUsuario());

								resp3 = OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,campos);

								if (resp3 <= 0)
									errores.add("Error generando orden ambulatoria",new ActionMessage("errors.noSeGraboInformacion","AL ACTUALIZAR LA ORDEN AMBULATORIA"));

								// ********************************************************************************
							} else
								resp3 = 1;*/
						}
					}
					
					logger.info("estadoActividad "+estadoActividad);
					
					/*if(!estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado))
					{
						logger.info("\n\n\n\n\n\n\n\nSE INSERTAN DATOS ADICIONALES DE LA ORDEN AMBULATORIA");
						
						Utilidades.imprimirMapa(mapa);
						
						//**********SE INSERTAN DATOS ADICIONALES DE LA ORDEN AMBULATORIA************************
						HashMap mapaRef = new HashMap();
						String[] vector;
						
						if(mapa.get("diagnostico_"+i)!=null&&!mapa.get("diagnostico_"+i).toString().equals(""))
						{
							vector = mapa.get("diagnostico_"+i).toString().split(ConstantesBD.separadorSplit);
							mapaRef.put("acronimoDiag",vector[0]);
							mapaRef.put("tipoCieDiag",vector[1]);
						}
						if(mapa.get("finalidad_"+i)!=null&&!mapa.get("finalidad_"+i).toString().equals(""))
						{
							vector = mapa.get("finalidad_"+i).toString().split(ConstantesBD.separadorSplit);
							mapaRef.put("finalidad",vector[0]);
						}	
						if(mapa.get("causaExterna_"+i)!=null&&!mapa.get("causaExterna_"+i).toString().equals(""))
						{
							vector = mapa.get("causaExterna_"+i).toString().split(ConstantesBD.separadorSplit);
							mapaRef.put("causaExterna",vector[0]);
						}	
						if(mapa.get("respuesta_"+i)!=null)
							mapaRef.put("resultados",mapa.get("respuesta_"+i));
						
						mapaRef.put("numeroOrden",mapa.get("numeroOrdenActividad_"+i));
						
						// Para este flujo no se cuenta con una solicitud, por tal motivo no se envia
						//mapaRef.put("numSolConsulta","");
						
						resp3 = OrdenesAmbulatorias.ingresarInformacionReferenciaExterna(con,mapaRef);
						//******************************************************************************
						 
					}*/
					
					//fin insertar actividad---------
					if(resp2>0)
					{
						//**********VERIFICACION DEL ACUMULADO********************************
						//Se consulta el convenio pyp asociado a la solicitud
						//DtoConvenio convenio = UtilidadesHistoriaClinica.obtenerConvenioPypSolicitud(con, numSolConsulta);
						
						logger.info("CONVENIO PARA LA ACUMULACION=> "+programasForm.getUltimaCuentaAsociada().get("cuenta"));
						
						programas.clean();
						programas.setCampos("codigoPaciente",paciente.getCodigoPersona());
						programas.setCampos("centroAtencion",mapa.get("centroAtencionActividad_"+i));
						programas.setCampos("tipoRegimen",programasForm.getUltimaCuentaAsociada().get("tipoRegimen"));
						programas.setCampos("codigoPrograma",mapa.get("codigoPrograma_"+i));
						programas.setCampos("consecutivoActividad",mapa.get("consecutivoActividad_"+i));
						programas.setCampos("codigoConvenio",programasForm.getUltimaCuentaAsociada().get("cuenta"));
						programas.setCampos("fecha",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
						programas.setCampos("institucion",mapa.get("institucionPrograma_"+i));
						
						//como no existe acumulado para esta actividad se inserta una nueva
						resp4 = programas.actualizarAcumuladoActividades(con);
						
						
						//*******************************************************************
					}
						
					if(resp0<=0||resp1<=0||resp2<=0||resp3<=0||resp4<=0)
					{
						//this.errores.add("",new ActionMessage("errors.problemasGenericos","ejecutando la actividad PYP con codigo "+consecutivoActividad+", del programa "+consecutivoPrograma));
						//logger.error("Error actualizando informacion de PYP en ingresarInformacionPYP resp0=>"+resp0+", resp1=>"+resp1+", resp2=>"+resp2+", resp3=>"+resp3+", resp4=>"+resp4);
					}
						
					/*******FIN UNIFICAR PYP NO*************************************************************************/
					logger.info("FIN UNIFICAR PYP NO");
				}
			//}
		}
		programasForm.setConsultaExterna(false);
	}
	
	
	/**
	 * Método qque verifica si ya fue insertado un programa
	 * @param con 
	 * @param registro
	 * @param codigoPrograma
	 * @param institucionPrograma
	 * @param codigoPaciente 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String fueInsertadoPrograma(Connection con, Vector registro, String codigoPrograma, String institucionPrograma, String codigoPaciente) 
	{
		String[] vector;
		String consecutivoPrograma = "";
		
		if(registro.size()>0)
		{
			int contador = 0;
			while(contador<registro.size())
			{
				vector = registro.get(contador).toString().split(ConstantesBD.separadorSplit);
				if(vector[0].equals(codigoPrograma)&&vector[1].equals(institucionPrograma))
					consecutivoPrograma = vector[2];
				contador++;
			}
		}
		
		if(consecutivoPrograma.equals(""))
			//si el programa no se encontro en el arreglo se debe verificar que no se haya
			//insertado en la base de datos
			consecutivoPrograma = ProgramasPYP.consultarConsecutivoProgramaExistente(con,codigoPaciente,codigoPrograma,institucionPrograma);
		
		return consecutivoPrograma;
	}
	
	/**
	 * Metodo que se encarga de enviar los datos necesarios para validar la anulacion de la
	 * autorizacion
	 * 
	 * @param programasForm
	 * @param usuario
	 * @throws IPSException
	 */
	public void cargarInfoParaAnulacionAutorizacion(ProgramasPYPForm programasForm,
			UsuarioBasico usuario)throws IPSException
	{
		AnulacionAutorizacionSolicitudDto anulacionDto	= null;
		ManejoPacienteFacade manejoPacienteFacade		= null;
		Medicos medicos	= null;
		try{
			anulacionDto= new AnulacionAutorizacionSolicitudDto();
			anulacionDto.setMotivoAnulacion(programasForm.getMotivoCancelacion());
			anulacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
			anulacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
			medicos		= new Medicos(); 
			medicos.setCodigoMedico(usuario.getCodigoPersona());
			anulacionDto.setMedicoAnulacion(medicos);
			anulacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
			anulacionDto.setCodigoOrdenAmbulatoria(Utilidades.convertirALong(programasForm.getNumeroOrden()));
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			manejoPacienteFacade.validarAnulacionAutorizacionCapitaSolictud(anulacionDto, 
					ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido,programasForm.getTipoActividad(),usuario.getCodigoInstitucionInt());
			
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
		
}
