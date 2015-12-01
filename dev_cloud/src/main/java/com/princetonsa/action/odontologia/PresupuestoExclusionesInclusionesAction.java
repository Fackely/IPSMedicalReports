package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoDefinirSolucitudDsctOdon;
import util.odontologia.InfoInclusionExclusionBoca;
import util.odontologia.InfoInclusionExclusionPiezaSuperficie;
import util.odontologia.InfoSeccionInclusionExclusion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.PresupuestoExclusionesInclusionesForm;
import com.princetonsa.dto.odontologia.DtoConcurrenciaPresupuesto;
import com.princetonsa.dto.odontologia.DtoContratarInclusion;
import com.princetonsa.dto.odontologia.DtoEncabezadoInclusion;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoRegistroContratarInclusion;
import com.princetonsa.dto.odontologia.DtoRegistroGuardarExclusion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.PresupuestoExclusionesInclusiones;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.mundo.odontologia.ValidacionesPresupuesto;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.PresupuestoExclusionesInclusionesMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.ExcluPresuEncabezado;
import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;

/**
 * 
 * @author axioma
 *
 */
public class PresupuestoExclusionesInclusionesAction extends Action
{

	private Logger logger = Logger.getLogger(PresupuestoExclusionesInclusionesAction.class);
	
	IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
	
	

	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof PresupuestoExclusionesInclusionesForm)
		{
			PresupuestoExclusionesInclusionesForm forma = (PresupuestoExclusionesInclusionesForm) form;

			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			ActionForward validacionesAcceso= validacionesAcceso(mapping, request,usuario, paciente, forma);
			
			logger.info("ESTADO -------------------------->");
			logger.info("ESTADO--------------->"+forma.getEstado());
			
			String estado = forma.getEstado();
			
			if(validacionesAcceso!=null)
				return validacionesAcceso;
			

			if(estado.equals("empezar"))
			{
				return accionEmpezar(mapping, forma, usuario, paciente, request, true /*crearBloqueo*/);
			}
//			else if(estado.equals("calcularTarifaSeccionInclusionPiezaSuperficie"))
//			{
//				return accionCalcularTarifaSeccionInclusionPiezaSuperficie(usuario, paciente, request, forma, mapping);
//			}
//			else if(estado.equals("calcularTarifaSeccionInclusionBoca"))
//			{
//				return accionCalcularTarifaSeccionInclusionBoca(usuario, paciente, request, forma, mapping);
//			}
//			else if(estado.equals("guardar"))
//			{
//				//return accionGuardar(mapping, forma, usuario, paciente, request, response);
//			}
//			else if(estado.equals("mostrarErrores"))
//			{
//				return mapping.findForward("paginaPrincipal");
//			}
			else if(estado.equals("historico"))
			{
				return accionHistorico(mapping, forma, usuario, paciente);
			}
			else if(estado.equals("imprimirHistorico"))
			{
				return accionImprimirHistorico(forma, usuario, request, mapping);
			}
			else if(estado.equals("procesoCancelado"))
			{
				return accionProcesoCancelado(paciente.getCodigoCuenta(), forma.getSiguientePagina(), mapping, response, request.getSession().getId());
			}
			else if(estado.equals("volver"))
			{
				accionProcesoCancelado(paciente.getCodigoCuenta(), forma.getSiguientePagina(), mapping, response, request.getSession().getId());
				return mapping.findForward("menu");
				
			}else if (estado.equals("actualizarProgramaServicio")){
				
				String seccionActualizar = forma.getSeccionActualizar();
			
				actualizarProgramaServicio(forma);

				return mapping.findForward(seccionActualizar);
			
			}else if (estado.equals("generarPresupuesto")){
				
				generarPresupuesto (forma, paciente, usuario);

				forma.setMostrarNuevasInclusiones(true);
				
				return mapping.findForward("seccionInclusiones");
			}
			else if(estado.equals("seleccionarPrograma"))
			{
				//forma.resetTotales();

				IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio=
						PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();

				// Se selecciona o se elimina la selección del programa
				presupuestoExclusionesInclusionesServicio.seleccionarPrograma(
						forma.getRegistrosContratarInclusion(), 
						forma.getIndexRegInclusion(), 
						forma.getIndexConvenio(),
						forma.isCheckPropiedadPrograma());

			
				// Se calculan los totales
				presupuestoExclusionesInclusionesServicio.calcularTotalesConvenios(
						forma.getRegistrosContratarInclusion(), 
						forma.getListaSumatoriaConvenios(),
						forma.getTotalesContratarInclusion());

				
				return mapping.findForward("seccionInclusiones");
			}
			else if(estado.equals("seleccionPromocion"))
			{
				
				//forma.resetTotales();
				
				IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio=
					PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();

				// Se selecciona la promoción
				presupuestoExclusionesInclusionesServicio.seleccionarPromocion(
						forma.getRegistrosContratarInclusion(), 
						forma.getIndexRegInclusion(), 
						forma.getIndexConvenio(),
						forma.isCheckPropiedadPrograma());
				
				// Se calculan los totales
				presupuestoExclusionesInclusionesServicio.calcularTotalesConvenios(
						forma.getRegistrosContratarInclusion(), 
						forma.getListaSumatoriaConvenios(),
						forma.getTotalesContratarInclusion());
			
				
				return mapping.findForward("seccionInclusiones");

			}
			else if(estado.equals("seleccionarTodosProgramasConvenio"))
			{
				//forma.resetTotales();
				
				IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio=
					PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();

				// Se seleccionan todos los programas del convenio
				presupuestoExclusionesInclusionesServicio.seleccionarTodosProgramasConvenio(
						forma.getRegistrosContratarInclusion(),
						forma.getIndexConvenio(),
						forma.isCheckPropiedadPrograma());

				// Se calculan los totales
				presupuestoExclusionesInclusionesServicio.calcularTotalesConvenios(
						forma.getRegistrosContratarInclusion(), 
						forma.getListaSumatoriaConvenios(),
						forma.getTotalesContratarInclusion());
				
				
				return mapping.findForward("seccionInclusiones");

			}
			else if(estado.equals("contratarInclusiones"))
			{
				String estadoContratar = ConstantesIntegridadDominio.acronimoContratado;
				return guardarInclusiones(mapping, forma, usuario, paciente, request, response, true, estadoContratar);
			}
			else if(estado.equals("contratarInclusionesSinDescuento"))
			{
				String estadoContratar = ConstantesIntegridadDominio.acronimoContratado;
				return guardarInclusiones(mapping, forma, usuario, paciente, request, response, false, estadoContratar);
			}
			else if(estado.equals("generarSolicitudDescuento"))
			{
				String estadoContratar = ConstantesIntegridadDominio.acronimoPrecontratado;
				return guardarInclusiones(mapping, forma, usuario, paciente, request, response, false, estadoContratar); //  estaba en true
			}
			else if (estado.equals("eliminarRegistro")){
				
				eliminarRegistroInclusionAContratar(forma);
				
				return mapping.findForward("seccionContratarInclusiones");
				
			}else if (estado.equals("recargarSeccionInclusiones")){
				
				return mapping.findForward("seccionInclusiones");
				
			}else if (estado.equals("guardarExclusiones")){
				
				return guardarExclusiones(mapping, forma, usuario, paciente, request, response);
			
			}else if (estado.equals("mostrarRegistroExclusion")){
				
				mostrarRegistroExclusion(forma);
				return mapping.findForward("registroExclusiones");
			
			}else if (estado.equals("mostrarDetalleRegistroInclusion")){
				
				mostrarRegistroInclusion(forma, usuario, paciente.getCodigoIngreso());
				return mapping.findForward("detalleRegistroInclusion");
			
			}else if(estado.equals("anularPrecontratacionInclusion")){
				
				anularPrecontratacionInclusion(forma, usuario);
				return mapping.findForward("detalleRegistroInclusion");
			
			}else if (estado.equals("volverAPrincipal")){
				
				return mapping.findForward(recargarPrincipal(forma));

				//return accionEmpezar(mapping, forma, usuario, paciente, request, false);
			
			}else if(estado.equals("contratarInclusionPrecontratada")){
			
				forma.resetValidacionesContratarInclusiones();
				return contratarInclusionPrecontratada(mapping, forma, usuario, paciente, request, response);
			
			}else if(estado.equals("continuarContratarInclusionPrecontratada")){
			
				if(forma.getDecisionPreguntaContratarInclusion().equals(ConstantesBD.acronimoNo)
					&& (!forma.getDtoContratarInclusion().getValidacion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_CONTRATAR_APLICAR_NUEVOS_BONOS_PROMOCIONES)
						||!forma.getDtoContratarInclusion().getValidacion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO))){
					
					forma.resetValidacionesContratarInclusiones();
					return mapping.findForward("detalleRegistroInclusion");
				
				}else{
					
					return contratarInclusionPrecontratada(mapping, forma, usuario, paciente, request, response);
				}
			}
		}
		
		return null;
	}

	

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward validacionesAcceso(ActionMapping mapping,
			HttpServletRequest request, UsuarioBasico usuario,
			PersonaBasica paciente, PresupuestoExclusionesInclusionesForm forma)
	{
		Connection con=UtilidadPersistencia.getPersistencia().obtenerConexion();
		//1. VALIDAR PACIENTE CARGADO EN SESION
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
			{	
				logger.warn("Paciente no válido (null)");			
				request.setAttribute("codigoDescripcionError", "errors.paciente.noCargadoExclusionInclusion");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.paciente.noCargadoExclusionInclusion"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
		}
		 
		//2 PACIENTE CON INGRESO ABIERTO
		else if (!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
			{
				logger.warn("INGRESO CERRADO (null)");			
				request.setAttribute("codigoDescripcionError","errors.paciente.noIngresoExclusionInclusion");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.paciente.noIngresoExclusionInclusion"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
		}
		
		//3 PACIENTE CON CUENTA ACTIVA O FACTURADA PARCIAL DE LA VIA CONSULTA EXTERNA
		else if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta()) <= 0)
		{
			if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
			{
				logger.warn("Cuenta no Activa (null)");			
				request.setAttribute("codigoDescripcionError", "error.odontologia.cuentavia");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.odontologia.cuentavia"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
		}
		else if (paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
			{
				logger.warn("Via de ingreso no es CONSULTA EXTERNA (null)");			
				request.setAttribute("codigoDescripcionError","error.cita.asignacion.viaIngreso");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.cita.asignacion.viaIngreso"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
		}
		
		//4. PACIENTE CON PRESUPUESTO CONTRATADO
		else if(PresupuestoOdontologico.cargarPresupuestoContratado(new BigDecimal(paciente.getCodigoIngreso()))== null)
		{
			if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
			{
				logger.warn("Via de ingreso no es CONSULTA EXTERNA (null)");			
				request.setAttribute("codigoDescripcionError","error.odontologia.presupuesto.contratadoExclusionInclusion");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.odontologia.presupuesto.contratadoExclusionInclusion"));
				saveErrors(request, errores);
		       	UtilidadBD.closeConnection(con);
		       	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
		}
		
//		//5. DEBEMOS VALIDAR LA EXISTENCIA DEL CONSECUTIVO DE INCLUSIONES/EXCLUSIONES
//		double valorConsecutivo=Utilidades.convertirADouble(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoInclusionProgServOdo, usuario.getCodigoInstitucionInt()));
//		
//		if(valorConsecutivo<=0)
//		{	
//			ActionErrors errores= new ActionErrors();
//			logger.info("error en el consecutivo");
//			errores.add("", new ActionMessage("error.faltaDefinirConsecutivo","Inclusión Programa/Servicio Odontológico"));
//			saveErrors(request, errores);
//			if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
//			{
//				UtilidadBD.closeConnection(con);
//		        return mapping.findForward("paginaErroresActionErrors");
//			}
//			else
//			{
//				UtilidadBD.closeConnection(con);
//			   	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
//			}
//		}
		
		DtoConcurrenciaPresupuesto dtoConcurrencia = PresupuestoOdontologico.estaEnProcesoPresupuesto(con, paciente.getCodigoCuenta(), request.getSession().getId(), false);
		if(dtoConcurrencia.existeConcurrencia())
		{
			logger.error("ya existe un usuario generando presupuesto!!!!");
			saveErrors(request, dtoConcurrencia.getErrorConcurrencia());
			if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
			{
				UtilidadBD.closeConnection(con);
		        return mapping.findForward("paginaErroresActionErrors");
			}
			else
			{
				UtilidadBD.closeConnection(con);
			   	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			}
		}
		
		UtilidadBD.closeConnection(con);
		return null;
	}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
			PresupuestoExclusionesInclusionesForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request, boolean crearBloqueo) 
	{
		//iniciamos el proceso de generacion presupuesto
		if(crearBloqueo)
		{	
			DtoConcurrenciaPresupuesto dtoConcurrencia = PresupuestoOdontologico.estaEnProcesoPresupuesto(paciente.getCodigoCuenta(), "", false);
			if(!dtoConcurrencia.existeConcurrencia())
			{
				if(!PresupuestoOdontologico.empezarBloqueoPresupuesto(paciente.getCodigoCuenta(), usuario.getLoginUsuario(), request.getSession().getId(), true))
				{	
					return ComunAction.accionSalirCasoError(mapping, request, null, logger, "", "errors.problemasBd", true);
				}
			}
			else
			{
				saveErrors(request, dtoConcurrencia.getErrorConcurrencia());
				if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()))
				{
				    return mapping.findForward("paginaErroresActionErrors");
				}
				else
				{
				   	return mapping.findForward("paginaErroresActionErrorsSinCabezote");
				}
			}
		}
		
		forma.reset(usuario.getCodigoInstitucionInt());
		
		forma.setInfoPresupuestoExclusionesInclusiones(PresupuestoExclusionesInclusiones.cargar(new BigDecimal(paciente.getCodigoIngreso()), forma.getUtilizaProgramas(), paciente.getCodigoPersona()));
		forma.setConsecutivoPresupuesto(forma.getInfoPresupuestoExclusionesInclusiones().getConsecutivoPresupuesto());
		//cargamos los mensajes cuando no existen exclusiones / inclusiones
		
		forma.setMensajeInclusiones(PresupuestoExclusionesInclusiones.cargarMensajesInclusion(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPlanTratamiento(), forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto(), forma.getInfoPresupuestoExclusionesInclusiones().getExisteInclusiones(), forma.getUtilizaProgramas()));
		forma.setMensajeExclusiones(PresupuestoExclusionesInclusiones.cargarMensajesExclusion(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPlanTratamiento(), forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto(), forma.getInfoPresupuestoExclusionesInclusiones().getExisteExclusiones(), forma.getUtilizaProgramas()));
		
		//UtilidadTransaccion.getTransaccion().begin();
		
//		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
//		
//		forma.setRegistrosExclusion((ArrayList<ExcluPresuEncabezado>) presupuestoExclusionesInclusionesServicio.cargarRegistrosExclusion(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto().longValue()));
//		forma.setRegistrosInclusion((ArrayList<DtoEncabezadoInclusion>) presupuestoExclusionesInclusionesServicio.cargarRegistrosInclusion(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto().longValue()));
//
//		forma.setExistenInclusionesPrecontratadas(verificarInclusionesPrecontratadas (forma.getRegistrosInclusion()));
//		
		cargarRegistrosExclusion(forma);
		cargarRegistrosInclusion(forma);
		
		return mapping.findForward("paginaPrincipal");
	}
	
	
	/**
	 * Método que se encarga de consultar nuevamente 
	 * 
	 * @param forma
	 */
	private void cargarRegistrosInclusion (PresupuestoExclusionesInclusionesForm forma){
		
		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
		
		forma.setRegistrosInclusion((ArrayList<DtoEncabezadoInclusion>) presupuestoExclusionesInclusionesServicio.cargarRegistrosInclusion(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto().longValue()));
		forma.setExistenInclusionesPrecontratadas(verificarInclusionesPrecontratadas (forma.getRegistrosInclusion()));
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void cargarRegistrosExclusion (PresupuestoExclusionesInclusionesForm forma){
		
		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
		forma.setRegistrosExclusion((ArrayList<ExcluPresuEncabezado>) presupuestoExclusionesInclusionesServicio.cargarRegistrosExclusion(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto().longValue()));		
	}

//
//	/**
//	 * 
//	 * @param usuario
//	 * @param paciente
//	 * @param request
//	 * @param forma
//	 * @param mapping
//	 * @return
//	 */
//	private ActionForward accionCalcularTarifaSeccionInclusionPiezaSuperficie(UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, PresupuestoExclusionesInclusionesForm forma, ActionMapping mapping) 
//	{
//		int indiceSeccion= Utilidades.convertirAEntero(request.getParameter("indiceSeccion"));
//		InfoInclusionExclusionPiezaSuperficie info= forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaPiezasDentalesSuperficies().get(indiceSeccion);
//		info.setInfoTarifa(obtenerInfoTarifaOdontologica(usuario, paciente, request, forma, mapping));
//		if(info.getInfoTarifa()!=null)
//		{	
//			info.setValorTarifa(info.getInfoTarifa().getValorTotalProgramaServicioConvenio());
//			info.setErroresCalculoTarifa(info.getInfoTarifa().getErroresTotalesStr("<br>"));
//			
//			if(!info.getErroresCalculoTarifa().isEmpty())
//			{
//				forma.setInfoTarifaAjax(info.getErroresCalculoTarifa());
//				forma.setSeleccionarBonoYPromocionAjax(false);
//				forma.setColorAjax("black");
//			}
//			else
//			{	
//				//SI NO EXISTE NI BONO NI PROMOCION
//				if(info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()<=0
//						&& info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()<=0)
//				{	
//					forma.setInfoTarifaAjax(info.getValorTarifaFormateado());
//					forma.setSeleccionarBonoYPromocionAjax(false);
//					forma.setColorAjax("black");
//				}
//				//SI SOLO EXISTE BONO
//				else if(info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()>0
//					&& info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()<=0)
//				{	
//					forma.setSeleccionarBonoYPromocionAjax(false);
//					if(info.getInfoTarifa().getDetalleBonoDescuento().isSeleccionadoPorcentaje())
//					{
//						forma.setInfoTarifaAjax("Bono: "+info.getInfoTarifa().getDetalleBonoDescuento().getPorcentajeDescuento()+"%");
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					else
//					{
//						forma.setInfoTarifaAjax("Bono: "+UtilidadTexto.formatearValores(info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()));
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					forma.setInfoTarifaAjax(forma.getInfoTarifaAjax()+" \n "+info.getValorTarifaFormateado());
//					forma.setColorAjax("green");
//				}
//				//SI SOLO EXISTE PROMOCION
//				else if(info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()>0
//					&& info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()<=0)
//				{	
//					forma.setSeleccionarBonoYPromocionAjax(false);
//					if(info.getInfoTarifa().getDetallePromocionDescuento().isSeleccionadoPorcentaje())
//					{
//						forma.setInfoTarifaAjax("Promocion: "+info.getInfoTarifa().getDetallePromocionDescuento().getPorcentajePromocion()+"%");
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					else
//					{
//						forma.setInfoTarifaAjax("Promocion: "+UtilidadTexto.formatearValores(info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()));
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					forma.setInfoTarifaAjax(forma.getInfoTarifaAjax()+" \n "+info.getValorTarifaFormateado());
//					forma.setColorAjax("blue");
//				}
//				//CUANDO EXISTE BONO Y PROMOCION
//				else
//				{
//					forma.setSeleccionarBonoYPromocionAjax(true);
//					forma.setInfoTarifaAjax("");
//					if( info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue() > info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue() )
//					{
//						info.getInfoTarifa().setSeleccionadoBonoInclusion(false);
//					}
//					else
//					{
//						info.getInfoTarifa().setSeleccionadoBonoInclusion(true);
//					}
//					request.setAttribute("indiceSeccion", indiceSeccion);
//					forma.setPropiedadFormaAjax("listaPiezasDentalesSuperficies");
//				}
//			}	
//		}
//		else
//		{
//			info.setValorTarifa(BigDecimal.ZERO);
//			info.setErroresCalculoTarifa("No existe tarifa");
//			forma.setInfoTarifaAjax("No existe tarifa");
//		}
//		return mapping.findForward("calculoValorTarifa");
//	}

//	/**
//	 * 
//	 * @param usuario
//	 * @param paciente
//	 * @param request
//	 * @param forma
//	 * @param mapping
//	 * @return
//	 */
//	private InfoPresupuestoXConvenioProgramaServicio obtenerInfoTarifaOdontologica(UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, PresupuestoExclusionesInclusionesForm forma, ActionMapping mapping)
//	{
//		InfoPresupuestoXConvenioProgramaServicio infoPresupuesto=null;
//		int indiceConvenioContrato= Utilidades.convertirAEntero(request.getParameter("indiceConvenioContrato"));
//		
//		int servicio= ConstantesBD.codigoNuncaValido;
//		double programa= ConstantesBD.codigoNuncaValidoDouble;
//		
//		if(forma.getUtilizaProgramas())
//		{
//			programa+= Utilidades.convertirADouble(request.getParameter("programaServicio"));
//		}
//		else
//		{
//			servicio+= Utilidades.convertirAEntero(request.getParameter("programaServicio"));
//		}
//		
//		InfoConvenioContratoPresupuesto infoConvenioContratoSel= null;
//		for(InfoConvenioContratoPresupuesto infoConvenioContrato: forma.getInfoPresupuestoExclusionesInclusiones().getListaConveniosContratos())
//		{
//			if(infoConvenioContrato.getIndice()==indiceConvenioContrato)
//			{
//				infoConvenioContratoSel= infoConvenioContrato;
//				break;
//			}
//		}
//		
//		if(infoConvenioContratoSel!=null)
//		{	
//			infoPresupuesto=CargosOdon.obtenerPresupuestoXConvenio(	
//																	servicio, 
//																	programa, 
//																	/*cantidad*/ 1, 
//																	infoConvenioContratoSel.getConvenio().getCodigo(), 
//																	infoConvenioContratoSel.getContrato().getCodigo(),
//																	UtilidadFecha.getFechaActual()/*fechaCalculoVigencia*/, 
//																	usuario.getCodigoInstitucionInt(),  
//																	new BigDecimal(paciente.getCodigoCuenta()));
//		}
//		return infoPresupuesto;
//	}
//	
//	/**
//	 * 
//	 * @param usuario
//	 * @param paciente
//	 * @param request
//	 * @param forma
//	 * @param mapping
//	 * @return
//	 */
//	private ActionForward accionCalcularTarifaSeccionInclusionBoca(UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, PresupuestoExclusionesInclusionesForm forma, ActionMapping mapping) 
//	{
//		int indiceSeccion= Utilidades.convertirAEntero(request.getParameter("indiceSeccion"));
//		InfoInclusionExclusionBoca info= forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaBoca().get(indiceSeccion);
//		info.setInfoTarifa(obtenerInfoTarifaOdontologica(usuario, paciente, request, forma, mapping));
//		if(info.getInfoTarifa()!=null)
//		{	
//			info.setValorTarifa(info.getInfoTarifa().getValorTotalProgramaServicioConvenio());
//			info.setErroresCalculoTarifa(info.getInfoTarifa().getErroresTotalesStr("<br>"));
//			
//			if(!info.getErroresCalculoTarifa().isEmpty())
//			{
//				forma.setInfoTarifaAjax(info.getErroresCalculoTarifa());
//				forma.setSeleccionarBonoYPromocionAjax(false);
//				forma.setColorAjax("black");
//			}
//			else
//			{	
//				//SI NO EXISTE NI BONO NI PROMOCION
//				if(info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()<=0
//						&& info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()<=0)
//				{	
//					forma.setInfoTarifaAjax(info.getValorTarifaFormateado());
//					forma.setSeleccionarBonoYPromocionAjax(false);
//					forma.setColorAjax("black");
//				}
//				//SI SOLO EXISTE BONO
//				else if(info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()>0
//					&& info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()<=0)
//				{	
//					forma.setSeleccionarBonoYPromocionAjax(false);
//					if(info.getInfoTarifa().getDetalleBonoDescuento().isSeleccionadoPorcentaje())
//					{
//						forma.setInfoTarifaAjax("Bono: "+info.getInfoTarifa().getDetalleBonoDescuento().getPorcentajeDescuento()+"%");
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					else
//					{
//						forma.setInfoTarifaAjax("Bono: "+UtilidadTexto.formatearValores(info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()));
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					forma.setInfoTarifaAjax(forma.getInfoTarifaAjax()+" <br> "+info.getValorTarifaFormateado());
//					forma.setColorAjax("green");
//				}
//				//SI SOLO EXISTE PROMOCION
//				else if(info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()>0
//					&& info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue()<=0)
//				{	
//					forma.setSeleccionarBonoYPromocionAjax(false);
//					if(info.getInfoTarifa().getDetallePromocionDescuento().isSeleccionadoPorcentaje())
//					{
//						forma.setInfoTarifaAjax("Promoción: "+info.getInfoTarifa().getDetallePromocionDescuento().getPorcentajePromocion()+"%");
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					else
//					{
//						forma.setInfoTarifaAjax("Promoción: "+UtilidadTexto.formatearValores(info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue()));
//						logger.info("InfotarifaAjax-->"+forma.getInfoTarifaAjax());
//					}
//					forma.setInfoTarifaAjax(forma.getInfoTarifaAjax()+" <br> "+info.getValorTarifaFormateado());
//					forma.setColorAjax("blue");
//				}
//				//CUANDO EXISTE BONO Y PROMOCION
//				else
//				{
//					forma.setSeleccionarBonoYPromocionAjax(true);
//					forma.setInfoTarifaAjax("");
//					if( info.getInfoTarifa().getDetallePromocionDescuento().getValorPromocion().doubleValue() > info.getInfoTarifa().getDetalleBonoDescuento().getValorDctoCALCULADO().doubleValue() )
//					{
//						info.getInfoTarifa().setSeleccionadoBonoInclusion(false);
//					}
//					else
//					{
//						info.getInfoTarifa().setSeleccionadoBonoInclusion(true);
//					}
//					request.setAttribute("indiceSeccion", indiceSeccion);
//					forma.setPropiedadFormaAjax("listaBoca");
//				}
//			}	
//		}
//		else
//		{
//			info.setValorTarifa(BigDecimal.ZERO);
//			info.setErroresCalculoTarifa("No existe tarifa");
//			forma.setInfoTarifaAjax("No existe tarifa");
//		}
//		return mapping.findForward("calculoValorTarifa");
//	}
//	

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionHistorico(ActionMapping mapping,
			PresupuestoExclusionesInclusionesForm forma, UsuarioBasico usuario,
			PersonaBasica paciente)
	{
		forma.resetHistorico(usuario.getCodigoInstitucionInt());
		if(UtilidadTexto.isEmpty(forma.getCodigoPkPresupuestoXREQUEST()+""))
		{	
			forma.setInfoHistorico(PresupuestoExclusionesInclusiones.cargarHistorico( new BigDecimal(paciente.getCodigoIngreso()), forma.getUtilizaProgramas(), usuario.getCodigoInstitucionInt()));
		}
		else
		{
			logger.info("VA HA CARGAR LA INFORMACION DE PRESUPUESTO--->"+forma.getCodigoPkPresupuestoXREQUEST());
			forma.setInfoHistorico(PresupuestoExclusionesInclusiones.cargarHistoricoDadoCodigoPk(new BigDecimal(forma.getCodigoPkPresupuestoXREQUEST()), forma.getUtilizaProgramas(), usuario.getCodigoInstitucionInt()));
		}
		return mapping.findForward("historico");
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionImprimirHistorico(	PresupuestoExclusionesInclusionesForm forma,
													UsuarioBasico usuario,
													HttpServletRequest request,
													ActionMapping mapping) 
    {
        DesignEngineApi comp;        
    	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"odontologia/","HistoricoInclusionesExclusionesPresupuesto.rptdesign");
        comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());     
        if(!institucionBasica.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
            v.add("Actividad Económica: "+institucionBasica.getActividadEconomica());
        v.add(institucionBasica.getDireccion()+"          "+institucionBasica.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
        comp.lowerAliasDataSet();
        String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        comp.updateJDBCParameters(newPathReport);
        return mapping.findForward("historico");    
    }
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param paginaSiguiente
	 * @param mapping
	 * @param response
	 * @return
	 */
	private ActionForward accionProcesoCancelado( int codigoCuenta, String paginaSiguiente, ActionMapping mapping, HttpServletResponse response, String idSesion) 
	{
		if(codigoCuenta>0)
		{	
			@SuppressWarnings("unused")
			boolean resultado=PresupuestoOdontologico.cancelarProcesoPresupuesto(codigoCuenta, idSesion);
		
			if(!paginaSiguiente.equals(""))
			{
				try
				{
					logger.info("paginaSiguiente "+paginaSiguiente);
					response.sendRedirect(paginaSiguiente);
				}
				catch (IOException e)
				{
					logger.error("Error redireccionando a la página seleccionada "+e);
				}
			}
		}	
		return null;
	}
	
	
	/**
	 * Método que se encarga de seleccionar uno o todos los registros de 
	 * Inclusiones o Exclusiones del paciente
	 * 
	 * @param forma
	 */
	private void actualizarProgramaServicio (PresupuestoExclusionesInclusionesForm forma){

		ArrayList<InfoInclusionExclusionPiezaSuperficie> registrosPiezasDentales = new ArrayList<InfoInclusionExclusionPiezaSuperficie>();
		ArrayList<InfoInclusionExclusionBoca> registrosBoca = new ArrayList<InfoInclusionExclusionBoca>();
		
		if(forma.getSeccionActualizar().equals("seccionInclusiones")){
			
			registrosPiezasDentales = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaPiezasDentalesSuperficies();
			registrosBoca = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaBoca();
			
			forma.setMostrarNuevasInclusiones(true);
			
		}else if(forma.getSeccionActualizar().equals("seccionExclusiones")){
			
			registrosPiezasDentales = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionExclusiones().getListaPiezasDentalesSuperficies();
			registrosBoca = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionExclusiones().getListaBoca();
			
			forma.setMostrarNuevasExclusiones(true);
		}
		
		if(forma.getSubSeccionActualizar().equals("piezaDental")){
			
			actualizarPiezasDentales (registrosPiezasDentales, forma.getPosicionRegistroActualizar());
			
		}else if(forma.getSubSeccionActualizar().equals("boca")){
			
			actualizarBoca(registrosBoca, forma.getPosicionRegistroActualizar());

		}else if(forma.getSubSeccionActualizar().equals("todas")){
			
			actualizarPiezasDentales (registrosPiezasDentales, forma.getPosicionRegistroActualizar());
			actualizarBoca(registrosBoca, forma.getPosicionRegistroActualizar());
		}
		
		forma.resetActualizarPrograma();
	}

	/**
	 * Método que se encarga de actualizar uno o todos los registros de tipo 
	 * hallazgo en Boca de las inclusiones o exclusiones del paciente
	 * 
	 * @param registrosPiezasDentales
	 * @param posicionRegistroActualizar
	 */
	private void actualizarPiezasDentales(ArrayList<InfoInclusionExclusionPiezaSuperficie> registrosPiezasDentales, int posicionRegistroActualizar) {
		
		if(posicionRegistroActualizar > ConstantesBD.codigoNuncaValido && registrosPiezasDentales.size() > posicionRegistroActualizar){
			
			InfoInclusionExclusionPiezaSuperficie incExcPiezaDental = registrosPiezasDentales.get(posicionRegistroActualizar);

			if(incExcPiezaDental!=null){
				
				if (incExcPiezaDental.isActivo()){
					
					incExcPiezaDental.setActivo(false);
					
				}else{
					
					incExcPiezaDental.setActivo(true);
				}
			}
			
		}else if(posicionRegistroActualizar == ConstantesBD.codigoNuncaValido){
			
			for (InfoInclusionExclusionPiezaSuperficie incExcPiezaDental : registrosPiezasDentales) {
				
				if(incExcPiezaDental!=null && incExcPiezaDental.getPrecontratada().equals(ConstantesBD.acronimoNo)){
					
					incExcPiezaDental.setActivo(true);
				}
			}
		}
	}

	/**
	 * Método que se encarga de actualizar uno o todos los registros de tipo 
	 * hallazgo en Boca de las inclusiones o exclusiones del paciente
	 * 
	 * @param registrosBoca
	 * @param posicionRegistroActualizar
	 */
	private void actualizarBoca(ArrayList<InfoInclusionExclusionBoca> registrosBoca, int posicionRegistroActualizar) {
		
		if(posicionRegistroActualizar > ConstantesBD.codigoNuncaValido && registrosBoca.size() > posicionRegistroActualizar){
			
			InfoInclusionExclusionBoca incExcBoca = registrosBoca.get(posicionRegistroActualizar);

			if(incExcBoca!=null){
				
				if (incExcBoca.isActivo()){
					
					incExcBoca.setActivo(false);
					
				}else{
					
					incExcBoca.setActivo(true);
				}
			}
			
		}else if(posicionRegistroActualizar == ConstantesBD.codigoNuncaValido){
			
			for (InfoInclusionExclusionBoca incExcBoca : registrosBoca) {
				
				if(incExcBoca!=null){
	
					incExcBoca.setActivo(true);
				}
			}
		}
	}
	
	
	
	/**
	 * Método que permite generar el presupuesto con los programas / servicios
	 * previamente seleccionados.
	 * 
	 * 
	 * @param forma
	 * @param paciente 
	 * @param usuario 
	 */
	private void generarPresupuesto (PresupuestoExclusionesInclusionesForm forma, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException {
		
		ArrayList<DtoPresuOdoProgServ> listaProgramasServicios = new ArrayList<DtoPresuOdoProgServ>();
		
		ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas = new ArrayList<InfoInclusionExclusionPiezaSuperficie>();
		ArrayList<InfoInclusionExclusionBoca> inclusionesBoca = new ArrayList<InfoInclusionExclusionBoca>();
		
		if(forma.getSeccionActualizar().equals("seccionInclusiones")){
			
			inclusionesPiezas = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaPiezasDentalesSuperficies();
			inclusionesBoca = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaBoca();
			
			IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
			
			listaProgramasServicios = presupuestoExclusionesInclusionesServicio.obtenerListadoInclusiones(inclusionesPiezas, inclusionesBoca, usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
		}
		
		////hacemos el calculo de las cantidades dependiendo del numero de superficies
		//calcularCantidadesXNumeroSuperficies(forma); ANTES SE LLAMABA ASÏ
		 
		IPresupuestoOdontologicoServicio presupuestoOdontologicoServicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		presupuestoOdontologicoServicio.calcularCantidadesXNumeroSuperficies(listaProgramasServicios, forma.getUtilizaProgramas(), false);
		 
		////ahora que ya tenemos el presupuesto, debemos hacer el calculo de las tarifas ENVIANDOLE LA CANTIDAD QUE YA LA TENEMOS AGRUPADA
		/////cargar el detalle de los convenios
		logger.info("EL NUMERO DE SERV/PROG AGRUPADOS SON:::"+listaProgramasServicios.size());

		// calcularTarifas(forma, usuario, paciente);	 
		 
		presupuestoOdontologicoServicio.calcularTarifas(listaProgramasServicios, forma.getInfoPresupuestoExclusionesInclusiones().getListaConveniosContratos(), 
				 usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), paciente.getCodigoCuenta(), forma.getUtilizaProgramas());

		logger.info("llenamos el dtoPresupuesto!!!!!");

		//forma.setDtoPresupuesto(presupuestoOdontologicoServicio.crearEncabezadoPresupuesto(usuario, paciente, forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPlanTratamiento()));
		//forma.getDtoPresupuesto().setDtoPresuOdoProgServ(listaProgramasServicios);
		 
		//crearValoresTotalesXConvenio(forma, usuario, paciente);
		 
		forma.setListaSumatoriaConvenios(presupuestoOdontologicoServicio.crearValoresTotalesXConvenio(listaProgramasServicios, forma.getInfoPresupuestoExclusionesInclusiones().getListaConveniosContratos()));

		
		for (DtoPresupuestoTotalConvenio totalConvenio : forma.getListaSumatoriaConvenios()) {
			
			totalConvenio.setExisteConvenioEnIngreso(ValidacionesPresupuesto.existeConvenioEnIngreso(paciente.getCodigoIngreso(), totalConvenio.getConvenio().getCodigo()));
		}
		
		///hacemos el ordenamiento x especialidad
		Collections.sort(listaProgramasServicios);
	
		forma.setListaProgramasServicios(listaProgramasServicios);
		
		forma.setRegistrosContratarInclusion(estructurarContratarInclusiones(forma));
		
	}
	
	
	/**
	 * 
	 * Método que permite agrupar en un solo objeto, la información para mostrar los registros
	 * asociados a la generación del presupuesto.
	 * 
	 * 
	 * @param forma
	 * @return 
	 */
	private ArrayList<DtoRegistroContratarInclusion> estructurarContratarInclusiones (PresupuestoExclusionesInclusionesForm forma){

		ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion = new ArrayList<DtoRegistroContratarInclusion>();
		
		ArrayList<DtoPresuOdoProgServ> listaProgramaServicio = forma.getListaProgramasServicios();
		ArrayList<DtoPresupuestoTotalConvenio> listadoConvenios = forma.getListaSumatoriaConvenios();
		
		ArrayList<String> especialidades = obtenerEspecialidades(listaProgramaServicio);
		
		for (String especialidad : especialidades) {
			
			for (DtoPresuOdoProgServ programaServicio : listaProgramaServicio) {
				
				if(programaServicio.getEspecialidad().equals(especialidad)){
					
					DtoRegistroContratarInclusion registroInclusion = new DtoRegistroContratarInclusion();
					
					registroInclusion.setProgramaServicio(programaServicio);
					
					ArrayList<DtoPresupuestoOdoConvenio> listadoPresupuestoOdoConvenio = new ArrayList<DtoPresupuestoOdoConvenio>();
					
					for (DtoPresupuestoTotalConvenio presupuestoTotalConvenio : listadoConvenios) {
						
						for (DtoPresupuestoOdoConvenio presupuestoOdoConvenio: programaServicio.getListPresupuestoOdoConvenio()) {
							
							if(presupuestoTotalConvenio.getConvenio().getCodigo() == presupuestoOdoConvenio.getConvenio().getCodigo()){

								if (!presupuestoTotalConvenio.isExisteConvenioEnIngreso() || (presupuestoTotalConvenio.isEsConvenioTarjetaCliente() && !presupuestoTotalConvenio.isTarjetaActiva())) {
								
									presupuestoOdoConvenio.getConvenio().setActivo(false);
									presupuestoTotalConvenio.getConvenio().setActivo(false);
									
								}else{
									
									presupuestoOdoConvenio.getConvenio().setActivo(true);
									presupuestoTotalConvenio.getConvenio().setActivo(true);
								}

								listadoPresupuestoOdoConvenio.add(presupuestoOdoConvenio);
							}
						}
					}
					
					registroInclusion.setListPresupuestoOdoConvenio(listadoPresupuestoOdoConvenio);
					
					registrosContratarInclusion.add(registroInclusion);
				}
			}
		}
		
		return registrosContratarInclusion;
	}
	
	/**
	 * Método que se encarga de extraer las especialidades asociadas a los programa / servicio
	 * 
	 * @param listaProgramasServicios
	 * @return
	 */
	private ArrayList<String> obtenerEspecialidades (ArrayList<DtoPresuOdoProgServ> listaProgramasServicios){
		
		ArrayList<String> especialidades = new ArrayList<String>();
		
		for (DtoPresuOdoProgServ programaServicio : listaProgramasServicios) {
			
			if(!especialidades.contains(programaServicio.getEspecialidad())){
				
				especialidades.add(programaServicio.getEspecialidad());
			}
		}
		
		return especialidades;
	}
	
	
	
	
	/**
	 * Método que se encarga de realizar todo el proceso necesario para la contratación
	 * de las inclusiones.
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param response
	 * @param validarDescuento indica si se debe o no validar solicitud de descuento
	 * @param estado Indica si se ca a contratar o a precontratar
	 * @return
	 */
	private ActionForward guardarInclusiones (ActionMapping mapping,
											PresupuestoExclusionesInclusionesForm forma, 
											UsuarioBasico usuario,
											PersonaBasica paciente,
											HttpServletRequest request,
											HttpServletResponse response,
											boolean validarDescuento,
											String estado) throws IPSException
	{
		
		
		Connection con=UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadTransaccion.getTransaccion().begin();
		
		ActionErrors errores = new ActionErrors();
		
		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();

		DtoContratarInclusion dtoContratarInclusion = new DtoContratarInclusion(null, forma.getRegistrosContratarInclusion(),	forma.getListaSumatoriaConvenios(), 
				forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto(), forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPlanTratamiento(),
			usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
			
		DtoPresupuestoOdontologicoDescuento dtoDcto=null;
		
		if(estado.equals(ConstantesIntegridadDominio.acronimoPrecontratado))
		{
			dtoDcto= new DtoPresupuestoOdontologicoDescuento();			
			dtoDcto.setEstado(ConstantesIntegridadDominio.acronimoXDefinir);
			dtoDcto.setPresupuesto(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto());
			dtoDcto.setUsuarioFechaModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			dtoDcto.setUsuarioFechaSolicitud(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			dtoDcto.setInclusion(true);
			
			dtoContratarInclusion.setValidacion(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO);
			dtoContratarInclusion.setDecision(ConstantesBD.acronimoSi);
		
		}else{
			
			dtoContratarInclusion.setValidacion("");
			dtoContratarInclusion.setDecision("");
		}
		
		dtoContratarInclusion.setDtoDcto(dtoDcto);
		dtoContratarInclusion.setErrores(errores);
		dtoContratarInclusion.setPaciente(paciente);
		dtoContratarInclusion.setConnection(con);
		dtoContratarInclusion.setEstado(estado);
		
		
		/*
		 * Se debe tener en cuenta que el valor total inclusión para descuento sea mayor a cero, para poder generar una solicitud de descuento
		 * odontológico
		 */
		if(validarDescuento && forma.getTotalesContratarInclusion().getTotalInclusionesParaDescuento()!=null
			&& forma.getTotalesContratarInclusion().getTotalInclusionesParaDescuento().doubleValue() > 0){
			
			dtoContratarInclusion.setValidarDescuento(validarDescuento);
		
		}else{
			
			dtoContratarInclusion.setValidarDescuento(false);
		}
	
		forma.setDtoContratarInclusion(dtoContratarInclusion);
		
		ResultadoBoolean resultado = presupuestoExclusionesInclusionesServicio.guardarInclusiones(dtoContratarInclusion);
		
		saveErrors(request, errores);
		
		if(resultado!=null && resultado.isResultado()){
		
			if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO))
			{
				forma.setPermiteGenerarSolicitudDescuento(true);
				UtilidadTransaccion.getTransaccion().commit();
				return mapping.findForward("paginaPrincipal");
			}
			else
			{
				forma.setPermiteGenerarSolicitudDescuento(false);
			}
			
			UtilidadTransaccion.getTransaccion().commit();
		
			forma.setEstado("procesoExitoso");

			if(estado.equals(ConstantesIntegridadDominio.acronimoContratado)){
				
				forma.setClave("InclusionesExclusionesForm.procesoContratacionExitoso");
				
			}else {
				
				forma.setClave("InclusionesExclusionesForm.procesoPrecontratacionExitoso");
			}
		
			return mapping.findForward(recargarPrincipal(forma));
			
			//return accionEmpezar(mapping, forma, usuario, paciente, request, false);
			
		}else if(resultado == null || resultado.getDescripcion().equals("errors.problemasBd")){
			
			UtilidadTransaccion.getTransaccion().rollback();
			accionProcesoCancelado(paciente.getCodigoCuenta(), forma.getSiguientePagina(), mapping, response, request.getSession().getId());
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "", resultado.getDescripcion(), true);
		
		}else {
			
			//se presentaron otros errores
			
			
			UtilidadTransaccion.getTransaccion().rollback();
			
			forma.setEstado("erroresContratacion");
			return mapping.findForward("paginaPrincipal");
		}
	}

	
	/**
	 * Método que elimina un registro de inclusión que ha sido seleccionado
	 * previamente para contratar.
	 * 
	 * @param forma
	 */
	private void eliminarRegistroInclusionAContratar(PresupuestoExclusionesInclusionesForm forma) throws IPSException {
		
		if(forma.getPosicionRegistroAEliminar()!=ConstantesBD.codigoNuncaValido){
			
			forma.getRegistrosContratarInclusion().remove(forma.getPosicionRegistroAEliminar());
		}
		
		//forma.resetTotales();

		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio=
				PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
	
		// Se calculan los totales
		presupuestoExclusionesInclusionesServicio.calcularTotalesConvenios(
				forma.getRegistrosContratarInclusion(), 
				forma.getListaSumatoriaConvenios(),
				forma.getTotalesContratarInclusion());
	}
	
	
	/**
	 * 
	 * Método que centraliza el proceso de registro de exclusiones.
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward guardarExclusiones (ActionMapping mapping,
			PresupuestoExclusionesInclusionesForm forma, 
			UsuarioBasico usuario,
			PersonaBasica paciente,
			HttpServletRequest request,
			HttpServletResponse response){
		
	
		Connection con=UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadTransaccion.getTransaccion().begin();

		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
	
		ArrayList<DtoRegistroGuardarExclusion> listadoRegistroGuardarExclusion = obtenerListadoExclusiones(forma, usuario);
		
		ActionErrors errores = new ActionErrors();
		
		BigDecimal codigoPresupuesto = forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto();
		
		ResultadoBoolean resultado = presupuestoExclusionesInclusionesServicio.guardarExclusiones(errores, listadoRegistroGuardarExclusion, con, usuario.getLoginUsuario(), 
				codigoPresupuesto, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		
		saveErrors(request, errores);
		
		if(resultado!=null && resultado.isResultado()){
			
			UtilidadTransaccion.getTransaccion().commit();
			
			forma.setEstado("procesoExitoso");

			forma.setClave("InclusionesExclusionesForm.procesoExclusionExitoso");

			return mapping.findForward(recargarPrincipal(forma));

		}else if(resultado == null || resultado.getDescripcion().equals("errors.problemasBd")){
			
			accionProcesoCancelado(paciente.getCodigoCuenta(), forma.getSiguientePagina(), mapping, response, request.getSession().getId());
		
			UtilidadTransaccion.getTransaccion().rollback();

			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", resultado.getDescripcion(), true);
		
		}else {
			
			//se presentaron otros errores
		
			UtilidadTransaccion.getTransaccion().rollback();
			
			forma.setEstado("erroresExclusion");
		}
	
		
		return mapping.findForward("paginaPrincipal");
	}
	
	
	/**
	 * Método que se encarga de transformar la información de los listados de Exclusiones Pieza Dental y Boca
	 * en un solo listado de {@link DtoRegistroGuardarExclusion}
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ArrayList<DtoRegistroGuardarExclusion> obtenerListadoExclusiones (PresupuestoExclusionesInclusionesForm forma, UsuarioBasico usuario){

		ArrayList<InfoInclusionExclusionPiezaSuperficie> exclusionesPiezas = new ArrayList<InfoInclusionExclusionPiezaSuperficie>();
		ArrayList<InfoInclusionExclusionBoca> exclusionesBoca = new ArrayList<InfoInclusionExclusionBoca>();
		
		exclusionesPiezas = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionExclusiones().getListaPiezasDentalesSuperficies();
		exclusionesBoca = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionExclusiones().getListaBoca();

		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
		
		ArrayList<DtoRegistroGuardarExclusion> listaRegistroGuardarExclusion = presupuestoExclusionesInclusionesServicio.obtenerListadoExclusiones(exclusionesPiezas, exclusionesBoca, usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
		
		return listaRegistroGuardarExclusion;
	}
	
	/**
	 * Método que asigna el registro de Exclusión que se desea consultar
	 * a la propiedad de la forma que permite mostrarlo
	 * 
	 * @param forma 
	 * 
	 */
	private void mostrarRegistroExclusion(PresupuestoExclusionesInclusionesForm forma) {
		
		if(forma.getIndiceRegistroExclusion() > ConstantesBD.codigoNuncaValido){
			
			forma.setRegistroExclusionDetalle(forma.getRegistrosExclusion().get(forma.getIndiceRegistroExclusion()));
		}
	}

	
	/**
	 * Método que asigna el registro de Inlcusión que se desea consultar
	 * a la propiedad de la forma que permite mostrarlo
	 * 
	 * @param forma
	 * @param usuario 
	 * @param codigoIngreso 
	 */
	private void mostrarRegistroInclusion(PresupuestoExclusionesInclusionesForm forma, UsuarioBasico usuario, int codigoIngreso) throws IPSException {
		
		if(forma.getIndiceInclusionMostrar() > ConstantesBD.codigoNuncaValido){
			
			forma.resetValidacionesContratarInclusiones();
			
			IncluPresuEncabezado incluPresuEncabezado = forma.getRegistrosInclusion().get(forma.getIndiceInclusionMostrar()).getIncluPresuEncabezado();
			
			/*
			 * Cuando se contrata una inclusión en estado precontratada. 
			 * El encabezado debe actualizarse más adelante* para que tome 
			 * los ultimos cambios realizados 
			 */
			//forma.setRegistroInclusionDetalle(incluPresuEncabezado);
			
			IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
	
			ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaPiezasDentalesSuperficies();
			ArrayList<InfoInclusionExclusionBoca> inclusionesBoca = forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones().getListaBoca();
			
			/* ***********-*/
			ArrayList<DtoRegistroContratarInclusion> registrosInclusion = presupuestoExclusionesInclusionesServicio.cargarDetalleRegistroInclusion(incluPresuEncabezado.getCodigoPk(), inclusionesPiezas, 
																			inclusionesBoca, usuario);
			
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios = obtenerListadoSumatoriaConvenios(registrosInclusion, codigoIngreso);

			//forma.resetTotales();
			
			DtoContratarInclusion dtoContratarInclusion = new DtoContratarInclusion(forma.getRegistrosInclusion().get(forma.getIndiceInclusionMostrar()), registrosInclusion,
					listaSumatoriaConvenios, forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto(), forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPlanTratamiento(),
					usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
			
		
			presupuestoExclusionesInclusionesServicio.calcularTotalesConvenios(dtoContratarInclusion.getRegistrosContratarInclusion(), dtoContratarInclusion.getListaSumatoriaConvenios(), 
																				dtoContratarInclusion.getEncabezadoInclusion().getTotalesContratarInclusion());
	
			dtoContratarInclusion.getEncabezadoInclusion().recalcularTotalesConDescuentoInclusion();

			forma.setDtoContratarInclusion(dtoContratarInclusion);
			
		}
	}
	
	
	/**

	 * Método que permite obtener los convenios que estan relacionados con el 
	 * registro de inclusión
	 * 
	 * @param registrosInclusionDetalle
	 * @param codigoIngreso
	 * @return
	 */
	private ArrayList<DtoPresupuestoTotalConvenio> obtenerListadoSumatoriaConvenios(ArrayList<DtoRegistroContratarInclusion> registrosInclusionDetalle, int codigoIngreso){
		
		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio=PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
		
		return presupuestoExclusionesInclusionesServicio.obtenerListadoSumatoriaConvenios(registrosInclusionDetalle, codigoIngreso);
	}
	
	/**
	 * Método que anula un registro de Inclusiones en estado precontratado
	 * 
	 * @param forma
	 * @param usuarioBasico
	 */
	private void anularPrecontratacionInclusion(PresupuestoExclusionesInclusionesForm forma, UsuarioBasico usuarioBasico) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		IncluPresuEncabezado incluPresuEncabezado = forma.getDtoContratarInclusion().getEncabezadoInclusion().getIncluPresuEncabezado();
				
		ResultadoBoolean resultado = new ResultadoBoolean();
		
		if(incluPresuEncabezado != null){
			
			IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
			
			DtoEncabezadoInclusion registroInclusion = forma.getDtoContratarInclusion().getEncabezadoInclusion();
			
			InfoDefinirSolucitudDsctOdon solicitudDescuento = registroInclusion.getSolicitudDescuento();

			resultado = presupuestoExclusionesInclusionesServicio.anularSolicitudDescuentoOdontologico(solicitudDescuento.getCodigoPkPresupuestoDctoOdon(), usuarioBasico.getLoginUsuario(), usuarioBasico.getCodigoInstitucionInt());
			
			if(resultado!=null && resultado.isResultado()){
				
				IUsuariosServicio usuariosServicio = AdministracionFabricaServicio.crearUsuariosServicio();
				Usuarios usuario = usuariosServicio.buscarPorLogin(usuarioBasico.getLoginUsuario());
				
				ICentroAtencionServicio centroAtencionServicio = AdministracionFabricaServicio.crearCentroAtencionServicio();
				CentroAtencion centroAtencion = centroAtencionServicio.buscarPorCodigoPK(usuarioBasico.getCodigoCentroAtencion());
				
				incluPresuEncabezado.setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
				incluPresuEncabezado.setUsuarios(usuario);
				incluPresuEncabezado.setFecha(UtilidadFecha.getFechaActualTipoBD());
				incluPresuEncabezado.setHora(UtilidadFecha.getHoraActual());
				
				incluPresuEncabezado.setCentroAtencion(centroAtencion);
				
				if(presupuestoExclusionesInclusionesServicio.actualizarIncluPresuEncabezado (incluPresuEncabezado)){
					
					forma.setEstado("procesoAnulacionExitoso");
					incluPresuEncabezado.setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
					
					habilitarInclusionesPrecontratadas (forma.getDtoContratarInclusion().getRegistrosContratarInclusion(), forma.getInfoPresupuestoExclusionesInclusiones().getSeccionInclusiones());
					
					UtilidadTransaccion.getTransaccion().commit();
				
				}else{
					
					UtilidadTransaccion.getTransaccion().rollback();
				}
				
			}else{
				
				if(resultado!=null && resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.NO_DEFINIDO_PARAMETRO_MOTIVO_ANULACION)){
					
					forma.setNoDefinidoMotivoAnulacion(true);
				}
				
				UtilidadTransaccion.getTransaccion().rollback();
			}
		}
	}
	
	
	/**
	 * Método que habilita nuevamente las inclusiones asociadas a un proceso
	 * de contratación que ha sido anulado
	 * 
	 * @param registrosInclusionDetalle
	 * @param infoSeccionInclusionExclusion 
	 */
	private void habilitarInclusionesPrecontratadas(ArrayList<DtoRegistroContratarInclusion> registrosInclusionDetalle, InfoSeccionInclusionExclusion infoSeccionInclusionExclusion) {
		
		
		boolean modificado;
		
		for (DtoRegistroContratarInclusion inclusion : registrosInclusionDetalle) {
			
			modificado = false;
			
			for (InfoInclusionExclusionPiezaSuperficie incluPiezaDental : infoSeccionInclusionExclusion.getListaPiezasDentalesSuperficies()) {
				
				if(inclusion.getProgramaServicio().getCodigoProgramaHallazgoPieza().longValue() == incluPiezaDental.getCodigoPkProgramaHallazgoPieza().longValue()){
					
					incluPiezaDental.setPrecontratada(ConstantesBD.acronimoNo);
					modificado = true;
					break;
				}
			}
			
			if(!modificado){
				
				for (InfoInclusionExclusionBoca incluBoca : infoSeccionInclusionExclusion.getListaBoca()) {
					
					if(inclusion.getProgramaServicio().getCodigoProgramaHallazgoPieza().longValue() == incluBoca.getCodigoPkProgramaHallazgoPieza().longValue()){
						
						incluBoca.setPrecontratada(ConstantesBD.acronimoNo);
						modificado = true;
						break;
					}
				}
			}
		}
	}

//	/**
//	 * Método que anula la solicitud de descuento Odontológico
//	 * asociada al encabezado de inclusión 
//	 * 
//	 * @param codigoPresupuestoDctoOdon
//	 * @param loginUsuario
//	 * @param codigoInstitucion
//	 * @return true en caso de realizar la modificación, false en caso contrario
//	 */
//	private boolean anularSolicitudDescuentoOdontologico (BigDecimal codigoPresupuestoDctoOdon, String loginUsuario, int codigoInstitucion){
//		
//		DtoPresupuestoOdontologicoDescuento presupuestoDescuentoOdontologico = new DtoPresupuestoOdontologicoDescuento();
//		
//		presupuestoDescuentoOdontologico.setCodigo(codigoPresupuestoDctoOdon);
//		presupuestoDescuentoOdontologico.setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado);
//		presupuestoDescuentoOdontologico.setUsuarioFechaModifica(new DtoInfoFechaUsuario(loginUsuario));
//
//		int codigoMotivo = ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(codigoInstitucion);
//		
//		BigDecimal motivo = new BigDecimal(codigoMotivo);
//	
//		presupuestoDescuentoOdontologico.setMotivo(motivo.doubleValue());
//
//		return AutorizacionDescuentosOdon.modificarPresupuestoDescuento(presupuestoDescuentoOdontologico);
//		
//		
//		
//	}
	
	
	/**
	 * Método que permite realizar la contratación definitiva de una solicitud de inclusión
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward contratarInclusionPrecontratada (ActionMapping mapping, PresupuestoExclusionesInclusionesForm forma, UsuarioBasico usuario, PersonaBasica paciente, 
			HttpServletRequest request, HttpServletResponse response) throws IPSException{
		
		Connection con=UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadTransaccion.getTransaccion().begin();
		
		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
		
		ActionErrors errores = new ActionErrors();

		if(forma.isTomarDecisionProcesoContratacion()){
		
			forma.getDtoContratarInclusion().setDecision(forma.getDecisionPreguntaContratarInclusion());
			forma.resetValidacionesContratarInclusiones();
			
			if(forma.getDtoContratarInclusion().getValidacion().equals
					(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO)
			 && forma.getDtoContratarInclusion().getDecision().equals(ConstantesBD.acronimoSi)){
				
				DtoPresupuestoOdontologicoDescuento dtoDcto=null;
				
				dtoDcto= new DtoPresupuestoOdontologicoDescuento();			
				dtoDcto.setEstado(ConstantesIntegridadDominio.acronimoXDefinir);
				dtoDcto.setPresupuesto(forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto());
				dtoDcto.setUsuarioFechaModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				dtoDcto.setUsuarioFechaSolicitud(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
				dtoDcto.setInclusion(true);
				
			    forma.getDtoContratarInclusion().setDtoDcto(dtoDcto);
			}
		
		}else{
			
			forma.getDtoContratarInclusion().setEstado(ConstantesIntegridadDominio.acronimoContratarPrecontratado);
			forma.getDtoContratarInclusion().setValidarDescuento(false);
			forma.getDtoContratarInclusion().setValidacion("");
			forma.getDtoContratarInclusion().setDecision("");
		}
		
		forma.getDtoContratarInclusion().setErrores(errores);
		forma.getDtoContratarInclusion().setPaciente(paciente);
		forma.getDtoContratarInclusion().setConnection(con);
		
		ResultadoBoolean resultado = presupuestoExclusionesInclusionesServicio.guardarInclusiones(forma.getDtoContratarInclusion());
		
		saveErrors(request, errores);
		
		if(resultado!=null && resultado.isResultado()){
			
			if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.NO_DEFINIDO_PARAMETRO_MOTIVO_ANULACION)){
				
				forma.setNoDefinidoMotivoAnulacion(true);
				return mapping.findForward("detalleRegistroInclusion");
			
			}else{
				
				String pregunta = realizarPreguntasContratacion(resultado, forma.getDtoContratarInclusion());
				
				if(!pregunta.equals("")){
					
					forma.setPreguntaContratarPrecontrado(pregunta);
					forma.setTomarDecisionProcesoContratacion(true);
					forma.getDtoContratarInclusion().setValidacion(resultado.getDescripcion());
					return mapping.findForward("detalleRegistroInclusion");
				}
			}
			
			forma.setEstado("procesoExitoso");
			
			if(forma.getDtoContratarInclusion().getEstado().equals(ConstantesIntegridadDominio.acronimoContratado)){
				
				forma.setClave("InclusionesExclusionesForm.procesoContratacionExitoso");
				
			}else {
				
				forma.setClave("InclusionesExclusionesForm.procesoPrecontratacionExitoso");
			}

			UtilidadTransaccion.getTransaccion().commit();
			
			long codigoIncluPresuEncabezado = forma.getDtoContratarInclusion().getEncabezadoInclusion().getIncluPresuEncabezado().getCodigoPk();

			/*
			 * Se actualiza la información registrada en el encabezado de la inclusión.
			 */
			forma.getDtoContratarInclusion().getEncabezadoInclusion().setIncluPresuEncabezado(
					 presupuestoExclusionesInclusionesServicio.cargarEncabezadoInclusion(codigoIncluPresuEncabezado));

		}else if(resultado == null || resultado.getDescripcion().equals("errors.problemasBd")){
			
			accionProcesoCancelado(paciente.getCodigoCuenta(), forma.getSiguientePagina(), mapping, response, request.getSession().getId());
		
			UtilidadTransaccion.getTransaccion().rollback();

			ComunAction.accionSalirCasoError(mapping, request, con, logger, "", resultado.getDescripcion(), true);
		
		}else {
			
			//se presentaron otros errores
		
			UtilidadTransaccion.getTransaccion().rollback();
			
			forma.setEstado("erroresContratacion");
		}

		return mapping.findForward("detalleRegistroInclusion");
	}
	
	
//	/**
//	 * @param mapping
//	 * @param forma
//	 * @param usuario
//	 * @param paciente
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	private ActionForward continuarContratarInclusionPrecontratada(
//			ActionMapping mapping, PresupuestoExclusionesInclusionesForm forma,
//			UsuarioBasico usuario, PersonaBasica paciente,
//			HttpServletRequest request, HttpServletResponse response) {
//	
//		
//		Connection con=UtilidadPersistencia.getPersistencia().obtenerConexion();
//		UtilidadTransaccion.getTransaccion().begin();
//		
//		IPresupuestoExclusionesInclusionesServicio presupuestoExclusionesInclusionesServicio = PresupuestoFabricaServicio.crearPresupuestoExclusionesInclusionesServicio();
//		
//		ActionErrors errores = new ActionErrors();
//
//		forma.getDtoContratarInclusion().setDecision(forma.getDecisionPreguntaContratarInclusion());
//		forma.resetValidacionesContratarInclusiones();
//		
//		ResultadoBoolean resultado = presupuestoExclusionesInclusionesServicio.guardarInclusiones(forma.getDtoContratarInclusion());
//		
//		saveErrors(request, errores);
//		
//		if(resultado!=null && resultado.isResultado()){
//			
//			String pregunta = realizarPreguntasContratacion(resultado, forma.getDtoContratarInclusion());
//			
//			if(!pregunta.equals("")){
//				
//				forma.setPreguntaContratarPrecontrado(pregunta);
//				forma.setTomarDecisionProcesoContratacion(true);
//				forma.getDtoContratarInclusion().setValidacion(resultado.getDescripcion());
//				return mapping.findForward("detalleRegistroInclusion");
//			}
//
//			UtilidadTransaccion.getTransaccion().commit();
//		
//			forma.setEstado("procesoExitoso");
//			
//			forma.setClave("InclusionesExclusionesForm.procesoContratacionExitoso");
//
//		}else if(resultado == null || resultado.getDescripcion().equals("errors.problemasBd")){
//			
//			accionProcesoCancelado(paciente.getCodigoCuenta(), forma.getSiguientePagina(), mapping, response, request.getSession().getId());
//		
//			UtilidadTransaccion.getTransaccion().rollback();
//
//			ComunAction.accionSalirCasoError(mapping, request, con, logger, "", resultado.getDescripcion(), true);
//		
//		}else {
//			
//			//se presentaron otros errores
//		
//			UtilidadTransaccion.getTransaccion().rollback();
//			
//			forma.setEstado("erroresContratacion");
//		}
//
//		return mapping.findForward("detalleRegistroInclusion");
//		
//		
//	}
	
	
	/**
	 * Método que se encarga de verificar si se debe realizar alguna pregunta
	 * al usuario generada del proceso de validación de la contratación de una inclusión
	 * en estado precontratada.
	 * 
	 * @param resultado
	 * @return Clave que indica la pregunta que se debe realizar
	 */
	private String realizarPreguntasContratacion (ResultadoBoolean resultado, DtoContratarInclusion dtoContratarInclusion){
		
		MessageResources mensages=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.InclusionesExclusionesForm");
		String pregunta = "";
		
		if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_CONTRATAR_SIN_BONOS_VIGENTES))
		{

			pregunta = mensages.getMessage("InclusionesExclusionesForm.bonosNoVigentes");
					
		}else if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_CONTRATAR_SIN_PROMOCIONES_VIGENTES))
		{
			
			pregunta = mensages.getMessage("InclusionesExclusionesForm.promocionesNoVigentes");
			
		}else if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_CONTRATAR_DESCUENTO_AUTORIZADO_VENCIDO))
		{
			
			pregunta = mensages.getMessage("InclusionesExclusionesForm.descuentoAutorizadoVencido");
			
		}else if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_CONTRATAR_DESCUENTO_PDEF_PPAUTO))
		{
			
			pregunta = mensages.getMessage("InclusionesExclusionesForm.descuentoPorDefiniOPendienteAutorizar");
		
		}else if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_CONTRATAR_DESCUENTO_NOAUTO_ANUL))
		{
			
			pregunta = mensages.getMessage("InclusionesExclusionesForm.descuentoNoAutorizadaOAnulada");
		
		}else if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_CONTRATAR_APLICAR_NUEVOS_BONOS_PROMOCIONES))
		{
			
			if(dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoXDefinir)
				|| dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoPendienteXAutorizar)
				|| dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)){
				
				
				pregunta = mensages.getMessage("InclusionesExclusionesForm.nuevosBonosYPromocionesAnulaDescuento", dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getEstadoSolicitud());
			
			}else {
				
				pregunta = mensages.getMessage("InclusionesExclusionesForm.nuevosBonosYPromocionesSolicitudOtroEstado", dtoContratarInclusion.getEncabezadoInclusion().getSolicitudDescuento().getEstadoSolicitud());
				
			}
		}else if(resultado.getDescripcion().equals(PresupuestoExclusionesInclusionesMundo.PREGUNTAR_GENERAR_SOLICITUD_DESCUENTO)){
			
			pregunta = mensages.getMessage("InclusionesExclusionesForm.deseaGenerarSolicitudDescuento");
		}
		
		return pregunta;
		
	}
	

	/**
	 * Método que determinar si existen o no registros de inclusión en estado precontratado
	 * 
	 * @param registrosInclusion 
	 * @return true si existen registros en estado precontratado, false de lo contrario
	 */
	private boolean verificarInclusionesPrecontratadas(ArrayList<DtoEncabezadoInclusion> registrosInclusion) {
		
		for (DtoEncabezadoInclusion inclusion : registrosInclusion) {
			
			if(inclusion.getIncluPresuEncabezado().getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado)){
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que recarga la página principal de la funcionalidad Inclusiones / Exclusiones
	 * una vez se ha realizado una operación de contratar, precontratar o guardar.
	 * 
	 * @param forma
	 * @return String con el nombre de la página a recargar
	 */
	private String recargarPrincipal(PresupuestoExclusionesInclusionesForm forma){
		
		BigDecimal codigoPresupuesto = forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPresupuesto();
		BigDecimal codigoPlanTratamiento = forma.getInfoPresupuestoExclusionesInclusiones().getCodigoPlanTratamiento();
		boolean utilizaProgramas = forma.getUtilizaProgramas();
		
		forma.resetAtributosProcesoContratacion();
		
		forma.getInfoPresupuestoExclusionesInclusiones().setSeccionInclusiones(PresupuestoExclusionesInclusiones.cargarInclusionesPlanTratamiento(codigoPlanTratamiento, 
				ConstantesIntegridadDominio.acronimoEstadoPendiente, ConstantesIntegridadDominio.acronimoAutorizado, utilizaProgramas));

		forma.getInfoPresupuestoExclusionesInclusiones().setSeccionExclusiones(PresupuestoExclusionesInclusiones.cargarExclusionesPresupuesto(codigoPresupuesto, 
				ConstantesIntegridadDominio.acronimoExcluido, ConstantesIntegridadDominio.acronimoAutorizado, utilizaProgramas));
		
		cargarRegistrosExclusion(forma);
		
		cargarRegistrosInclusion(forma);
	
		return "paginaPrincipal";
	}
	
}
