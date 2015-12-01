package com.princetonsa.action.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.actionform.tesoreria.NotasPacientesForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.enu.administracion.EmunConsecutivosTesoreriaCentroAtencion;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoBusquedaNotasPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.dto.tesoreria.DtoNotasPorNaturaleza;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;
import com.servinte.axioma.generadorReporte.tesoreria.notasPacientes.GeneradorReporteNotasPacientes;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;

public class NotasPacientesAction extends Action {
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RecibosCajaAction.class);
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.NotasPacientesForm");
	
	private INotaPacienteMundo notaPacienteMundo = 
		TesoreriaFabricaMundo.crearNotaPacienteMundo();

	public ActionForward execute(	ActionMapping mapping, 	
															ActionForm form, 
															HttpServletRequest request, 
															HttpServletResponse response) throws Exception
															{

		Connection con = null;	
		try{

			if(form instanceof NotasPacientesForm) {


				NotasPacientesForm formNotaDevAbonosPaciente=(NotasPacientesForm) form;
				ActionErrors errores = new ActionErrors();

				HttpSession sesion = request.getSession();			
				UsuarioBasico usuario = null;
				usuario = getUsuarioBasicoSesion(sesion);
				PersonaBasica persona = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");


				logger.warn("institucionBasica->"+institucionBasica.getCodigo());
				logger.warn("institucionBasica->"+institucionBasica.getCodigoInstitucionBasica());

				String estado = formNotaDevAbonosPaciente.getEstado();
				logger.warn("estado->"+estado);

				try {
					if(estado.equals("empezar")) {
						formNotaDevAbonosPaciente.reset();
						if(ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt()) != 
							ConstantesBD.codigoNuncaValido+"" && 
							!UtilidadTexto.isEmpty(ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt()))) {
							con = UtilidadBD.abrirConexion();
							boolean consecutivoEncontrado = validarConsecutivoDisponible(con, request, usuario,formNotaDevAbonosPaciente, errores);
							UtilidadBD.closeConnection(con);
							if (!consecutivoEncontrado) {
								return mapping.findForward("paginaMensajeBloqueo");
							}
							else {
								return accionEmpezarPaciente(formNotaDevAbonosPaciente,usuario,mapping);
							}
						} else {
							errores.add("ParametroFaltante", 
									new ActionMessage("error.parametrosGenerales.faltaDefinirParametroModulo", 
											"Naturaleza de Notas de Pacientes a Manejar", "Tesorería"));
							saveErrors(request, errores);
							return mapping.findForward("paginaMensajeBloqueo");
						}
					} else if("determinarValidacionCampo".equals(estado)) {
						determinarValidacionIdentificacion(formNotaDevAbonosPaciente, request, usuario.getCodigoInstitucionInt());
						return mapping.findForward("numIdentificacionPaciente");
					} else if(estado.equals("validarPaciente")) {
						determinarValidacionIdentificacion(formNotaDevAbonosPaciente, request, usuario.getCodigoInstitucionInt());
						return accionValidarPaciente(formNotaDevAbonosPaciente,usuario,mapping,persona,request,errores);
					} else if(estado.equals("cargarListaConceptosNotasPacientes")) {
						formNotaDevAbonosPaciente.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptoNotaPacientexNaturalezaEstadoActivo(
								formNotaDevAbonosPaciente.getNaturalezaNotasPacienteSeleccionada()));
						return mapping.findForward("listaConceptosNotasPacientes");
					} else if(estado.equals("guardarNotaPaciente")) {
						return guardarNotaPaciente(formNotaDevAbonosPaciente,usuario,mapping,persona,request,errores,con);
					} else if(estado.equals("busquedaNotasPacientePorRango")){
						formNotaDevAbonosPaciente.reset();
						formNotaDevAbonosPaciente.setBusquedaPorRango(true);
						return mapping.findForward(cargarBusquedaNotasPacientePorRango(con, request, usuario, formNotaDevAbonosPaciente, errores));
					} else if(estado.equals("consultarNotasPacientePorRango")) {
						formNotaDevAbonosPaciente.getCentrosAtencionSeleccionados();
						return mapping.findForward(consultarNotasPacientePorRango(con, request, usuario, formNotaDevAbonosPaciente, errores, institucionBasica));
					} else if(estado.equals("mostrarDetalleNotaPaciente")) {
						formNotaDevAbonosPaciente.setNombreArchivo("");
						return mapping.findForward(mostrarDetalleNotaPaciente(request, usuario, formNotaDevAbonosPaciente, errores));
					} else if(estado.equals("mostrarDetalleNotaPacientePorIngreso")) {
						formNotaDevAbonosPaciente.setNombreArchivo("");
						return mapping.findForward(mostrarDetalleNotaPacientePorIngreso(request, usuario, formNotaDevAbonosPaciente, errores));
					} else if(estado.equals("imprimirNotasPacientePorRango")){
						int idTipoSalida = Integer.parseInt(formNotaDevAbonosPaciente.getTipoSalida());
						if (idTipoSalida != ConstantesBD.codigoNuncaValido) {
							GeneradorReporteDinamico generadorReporte = new GeneradorReporteDinamico();
							String nombreArchivo = "";
							if(idTipoSalida == EnumTiposSalida.PDF.getCodigo()){
								nombreArchivo=generadorReporte.exportarReportePDF(
										this.generarReporteConsolidado(request, usuario, formNotaDevAbonosPaciente, errores, institucionBasica, EnumTiposSalida.PDF.getCodigo()), 
										nombreArchivo);
							} else if(idTipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo()){
								nombreArchivo=generadorReporte.exportarReporteExcel(
										this.generarReporteConsolidado(request, usuario, formNotaDevAbonosPaciente, errores, institucionBasica, EnumTiposSalida.HOJA_CALCULO.getCodigo()), 
										nombreArchivo);
							} else if(idTipoSalida == EnumTiposSalida.PLANO.getCodigo()){
								nombreArchivo=generadorReporte.exportarReporteArchivoPlano(
										this.generarReporteConsolidado(request, usuario, formNotaDevAbonosPaciente, errores, institucionBasica, EnumTiposSalida.PLANO.getCodigo()), 
										nombreArchivo);
							}
							formNotaDevAbonosPaciente.setNombreArchivo(nombreArchivo);
							formNotaDevAbonosPaciente.setTipoSalida(ConstantesBD.codigoNuncaValido+"");
						}
						return mapping.findForward("resultadoNotasPacientePorRango");
					} else if(estado.equals("cargarListaConceptosBusqueda")) {
						if (formNotaDevAbonosPaciente.getNaturalezaNotaBusqueda().
								equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
							formNotaDevAbonosPaciente.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptosNotaPaciente());
						} else {
							formNotaDevAbonosPaciente.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptoNotaPacientexNaturaleza(
									formNotaDevAbonosPaciente.getNaturalezaNotaBusqueda()));
						}
						return mapping.findForward("listaConceptosNotasPacientesBusqueda");
					} else if(estado.equals("busquedaNotasPacientePorPaciente")){
						formNotaDevAbonosPaciente.reset();
						formNotaDevAbonosPaciente.setBusquedaPorRango(false);
						PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
						if (paciente.getCodigoPersona() == ConstantesBD.codigoNuncaValido) {
							errores.add("PacienteNoCargado", new ActionMessage("errors.paciente.noCargado"));
							saveErrors(request, errores);
							return mapping.findForward("paginaMensajeBloqueo");
						} else {
							return mapping.findForward(
									consultarNotasPacientePorPaciente(
											request, usuario, formNotaDevAbonosPaciente, 
											errores, institucionBasica, paciente.getCodigoPersona()));
						}
					} else if(estado.equals("imprimirDetalleNotasPaciente")){
						String nombreArchivo = "";
						GeneradorReporteDinamico generadorReporte = new GeneradorReporteDinamico();
						JasperReportBuilder reporte = 
							this.generarReporteDetalle(request, usuario, formNotaDevAbonosPaciente, errores, institucionBasica);
						if (reporte != null) {
							nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreArchivo);
							formNotaDevAbonosPaciente.setNombreArchivo(nombreArchivo);
						} else {
							errores.add("errorGenerandoArchivo", new ActionMessage("errors.notEspecific", "Error generando el archivo. Intentelo de nuevo."));
							saveErrors(request, errores);
						}
						if (formNotaDevAbonosPaciente.isInsercionCorrecta()) {
							return mapping.findForward("paginaPrincipal");
						} else {
							return mapping.findForward("resumenNotaPaciente");
						}
					} else if(estado.equals("imprimirDetalleNotasPacienteXIngreso")){
						String nombreArchivo = "";
						GeneradorReporteDinamico generadorReporte = new GeneradorReporteDinamico();
						JasperReportBuilder reporte = 
							this.generarReporteDetalleXIngreso(request, usuario, formNotaDevAbonosPaciente, errores, institucionBasica);
						if (reporte != null) {
							nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreArchivo);
							formNotaDevAbonosPaciente.setNombreArchivo(nombreArchivo);
						} else {
							errores.add("errorGenerandoArchivo", new ActionMessage("errors.notEspecific", "Error generando el archivo. Intentelo de nuevo."));
							saveErrors(request, errores);
						}
						if (formNotaDevAbonosPaciente.isInsercionCorrecta()) {
							return mapping.findForward("paginaPrincipal");
						} else {
							return mapping.findForward("resumenNotaPacientePorIngreso");
						}
					} else if(estado.equals("ordenarResultadosNotasPacientePorRango")) {
						return mapping.findForward(accionOrdenar(formNotaDevAbonosPaciente, "resultadosNotasPacientePorRango"));
					} else if(estado.equals("volverBusquedaNotasPacientePorRango")) {
						formNotaDevAbonosPaciente.setCentrosAtencionSeleccionadosTmp(formNotaDevAbonosPaciente.getCentrosAtencionSeleccionados());
						formNotaDevAbonosPaciente.setCentrosAtencionSeleccionados(new String[formNotaDevAbonosPaciente.getListaCentrosAtencion().size()]);
						formNotaDevAbonosPaciente.setNaturalezaNotaBusqueda("");
						formNotaDevAbonosPaciente.setEsUnicoResultado(false);
						return mapping.findForward("busquedaNotasPacientePorRango");
					} 
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("codigoDescripcionError", "errors.ingresoDatos");
					return mapping.findForward("paginaError");
				}


			} else {
				logger.error("El form no es compatible con el form de RecibosCajaForm");
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
	 * accion para empezar el flujo de agenda odontol&oacute;gica por paciente
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezarPaciente(NotasPacientesForm forma,UsuarioBasico usuario, ActionMapping mapping) 
	{
		forma.reset();
		forma.setTipoFlujoPaciente(ConstantesBD.acronimoSi);		
		forma.setNumDiasAntFechaActual(ValoresPorDefecto.getNumDiasAntFActualAgendaOd(usuario.getCodigoInstitucionInt()));
		forma.setMultiploMinGenCitas(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt()));
		forma.setValidaPresupuestoContratado(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()));
		forma.setUtilProgOdonPlanTra(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
		forma.setMinutosEsperaAsgCitOdoCad(ValoresPorDefecto.getMinutosEsperaAsignarCitasOdoCaducadas(usuario.getCodigoInstitucionInt()));
		forma.setListTipIdent(Utilidades.obtenerTiposIdentificacion("ingresoPaciente",usuario.getCodigoInstitucionInt()));
		forma.setInstitucionMultiempresa(UtilidadTexto.getBoolean(
				ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())));
		
		UtilidadTransaccion.getTransaccion().begin();
		forma.setCentroAtencionRegistro(notaPacienteMundo.obtenerCentroAtencion(usuario.getCodigoCentroAtencion()));
		forma.setListaCentrosAtencion(notaPacienteMundo.obtenerCentrosAtencionActivosUsuario(usuario.getLoginUsuario()));
		if (!forma.isInstitucionMultiempresa()) {
			forma.setCentroAtencionOrigenHelper(usuario.getCodigoCentroAtencion());
		}
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("paginaPrincipal");
	}
	

	/**
	 * METODO QUE GUARDA LA INFORMACION DE LAS DEVOLUCIONES DE ABONO DEL PACIENTE
	 */
	
	private ActionForward guardarNotaPaciente(NotasPacientesForm forma,UsuarioBasico usuario, 
			ActionMapping mapping, PersonaBasica persona, HttpServletRequest request,
			ActionErrors errores,Connection con) {
		
		try {
			 // DEVOLUCION ABONO
			boolean existeValorNota = true;
			boolean existeValorNegativoNuevoSaldo = false;
			boolean consecutivoInvalido = false;
			int index = 0;
			double valorTotal = 0;
			ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> listaDevolucionAbonosInicial = forma.getDtoNotaPaciente().getListaDtoInfoIngresoPacienteControlarAbonoPacientes();
			for (DtoInfoIngresoPacienteControlarAbonoPacientes dtoInfoNotaPaciente : listaDevolucionAbonosInicial) {
				String valor = request.getParameter("listaInfoNotaPaciente["+index+"].valorDevolucion");
				if (!UtilidadTexto.isEmpty(valor)) {
					valor = valor.replaceAll(",", "");
					double valorNota = Double.parseDouble(valor);
					if (valorNota > 0) {
						dtoInfoNotaPaciente.setValorDevolucion(valorNota);
						valorTotal += valorNota;
					} else if (valorNota < 0) {
						existeValorNegativoNuevoSaldo = true;
					}
				} else {
					existeValorNota = false;
				}
				index++;
			}
			
			if (valorTotal == 0) {
				existeValorNota = false;
			}
			
			/**
			 * Digito algun valor para la insercion de la nota
			 */
			if (!existeValorNegativoNuevoSaldo)	{
				if (existeValorNota) {
					BigDecimal consecutivoNotaPacienteCentroAtencion = BigDecimal.ZERO;
					BigDecimal consecutivoEscogido = BigDecimal.ZERO;
					String consecutivoNotaPaciente = "";
					int codigoInstitucion = usuario.getCodigoInstitucionInt();
					int codigoCentroAtencion = usuario.getCodigoCentroAtencion();
					int insercionMovimientosAbonos = ConstantesBD.codigoNuncaValido;
	
					boolean manejaConsecutivoNotasPacientesXCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosNotasPacientesCentroAtencion(codigoInstitucion));
					if (manejaConsecutivoNotasPacientesXCentroAtencion) {
						if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoDebito)) {
							consecutivoNotaPacienteCentroAtencion =  ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(
									codigoCentroAtencion, EmunConsecutivosTesoreriaCentroAtencion.NotasDebitoPacientes.getNombreConsecutivoBaseDatos(), codigoInstitucion);
						} else if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoCredito)) {
							consecutivoNotaPacienteCentroAtencion =  ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(
									codigoCentroAtencion, EmunConsecutivosTesoreriaCentroAtencion.NotasCreditoPacientes.getNombreConsecutivoBaseDatos(), codigoInstitucion);
						}
						if (consecutivoNotaPacienteCentroAtencion.intValue() != 0 && consecutivoNotaPacienteCentroAtencion.intValue() != ConstantesBD.codigoNuncaValido) {
							consecutivoEscogido = consecutivoNotaPacienteCentroAtencion;
						} else {
							consecutivoInvalido = true;
						}
						logger.warn("consecutivo por centro de atencion->"+consecutivoNotaPacienteCentroAtencion);
					} else {
						if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoDebito)) {
							consecutivoNotaPaciente = UtilidadBD.obtenerValorConsecutivoDisponible(
									ConstantesBD.nombreConsecutivoNotasDebitoPacientes, usuario.getCodigoInstitucionInt());
						} else if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoCredito)) {
							consecutivoNotaPaciente = UtilidadBD.obtenerValorConsecutivoDisponible(
									ConstantesBD.nombreConsecutivoNotasCreditoPacientes, usuario.getCodigoInstitucionInt());
						}
						if (!UtilidadTexto.isEmpty(consecutivoNotaPaciente) && !consecutivoNotaPaciente.equals("-1")) {
							consecutivoEscogido = new BigDecimal(consecutivoNotaPaciente);
						} else {
							consecutivoInvalido = true;
						}
						
						logger.warn("consecutivo->"+consecutivoNotaPaciente);
					}
					// DATOS DEVOLUCION_ABONO 
	
					if (!consecutivoInvalido) {
						//CODIGO PACIENTE
						Pacientes pacientes = new Pacientes();
						pacientes.setCodigoPaciente(persona.getCodigoPersona());
						//FECHA
						UtilidadFecha.getFechaActualTipoBD();
						//HORA
						UtilidadFecha.getHoraActual();
						//USUARIO
						Usuarios usuarios = new Usuarios();
						usuarios.setLogin(usuario.getLoginUsuario());
						
						/**
						 * Parametro general Controlar Abono de Pacientes por No de Ingreso 
						 * Devuelve S o N
						 */
	
						String controlAbonoPacientePorIngreso = ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(codigoInstitucion);
	
	
	
						ArrayList<Integer> listaPosicionesBorrar = new ArrayList<Integer>();
	
						/**
						 * Lista que debe insertar menos los DTOs que no tiene un valor de devolucion
						 */
	
						ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>  listaDevolucionAbonos = new ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>();
	
						for (int m =0; m <listaDevolucionAbonosInicial.size(); m++ ) 
						{
							if(listaDevolucionAbonosInicial.get(m).getValorDevolucion() != 0){ 
								listaDevolucionAbonos.add(listaDevolucionAbonosInicial.get(m));
								//listaPosicionesBorrar.add(m);
							}
						} 
	
						for (int s = listaPosicionesBorrar.size()-1; s >= 0; s-- ){
							listaDevolucionAbonos.remove(s);
						}
	
						UtilidadTransaccion.getTransaccion().begin();
	
						/**
						 * Insercion de todas las devoluciones de abonos que se generaron
						 */
	
						boolean insercionNotaPaciente = notaPacienteMundo.guardarDevolucionAbono(consecutivoEscogido,
								forma.getObservacionesNotaPaciente(),
								pacientes,
								usuarios,
								forma.getCentroAtencionOrigen(),
								forma.getCentroAtencionRegistro(),
								forma.getNaturalezaNotasPacienteSeleccionada(),
								forma.getDtoConceptoNotaPaciente(),
								listaDevolucionAbonos
						);
	
						UtilidadTransaccion.getTransaccion().commit();
	
	
	
						if(insercionNotaPaciente == true){
	
							/**
							 * Cargue de la informacion para mostrar e insertarla en 
							 * movimientos abonos
							 */
	
							UtilidadTransaccion.getTransaccion().begin();
							ArrayList<DTONotaPaciente> listaDTONotaPaciente = 
								notaPacienteMundo.obtenerDevolucionAbonoPacienteConsecutivo(pacientes.getCodigoPaciente(), 
										consecutivoEscogido, forma.getNaturalezaNotasPacienteSeleccionada(), controlAbonoPacientePorIngreso);
	
							/**
							 * Insersion en movimientos abono de las devoluciones generadas
							 */
							
							int tipoMovimento = (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoDebito)) ?
									ConstantesBD.tipoMovimientoSalidaNotaDebitoPaciente : ConstantesBD.tipoMovimientoEntradaNotaCreditoPaciente;
							
							Connection conH = UtilidadPersistencia.getPersistencia().obtenerConexion();
							
							if(forma.isControlaAbonoPacienteXIngreso()){
								for(DTONotaPaciente notaPaciente:listaDTONotaPaciente) {
									insercionMovimientosAbonos = notaPacienteMundo.insertarMovimientoAbonos(conH, notaPaciente.getCodPaciente(), (int)notaPaciente.getCodigoPkDetalleNotaPaciente(), tipoMovimento, notaPaciente.getValorDevolucion().doubleValue(), codigoInstitucion, (Integer)notaPaciente.getIngreso(), codigoCentroAtencion);
								}	
							} else {
								for(DTONotaPaciente notaPaciente:listaDTONotaPaciente) {
									int ingresoPk = notaPacienteMundo.obtenerIngresoPaciente(notaPaciente.getCodPaciente());
									if (ingresoPk != ConstantesBD.codigoNuncaValido) {
										insercionMovimientosAbonos = notaPacienteMundo.insertarMovimientoAbonos(conH, notaPaciente.getCodPaciente(), consecutivoEscogido.intValue(), tipoMovimento, notaPaciente.getValorDevolucion().doubleValue(), codigoInstitucion, ingresoPk, codigoCentroAtencion);
									} else {
										insercionMovimientosAbonos = notaPacienteMundo.insertarMovimientoAbonos(conH, notaPaciente.getCodPaciente(), consecutivoEscogido.intValue(), tipoMovimento, notaPaciente.getValorDevolucion().doubleValue(), codigoInstitucion, null, codigoCentroAtencion);
									}	
								}
							}
							UtilidadTransaccion.getTransaccion().commit();
							
							if(insercionNotaPaciente == true && insercionMovimientosAbonos != ConstantesBD.codigoNuncaValido){
								
								//* Mostrar la interface de resumen
	
								forma.setInsercionCorrecta(true);
	
								//* No mostrar la interface de insercion de nuevas devoluciones
	
								forma.setExistePaciente(false);
								
								
								/**
								 * Cargue de la informacion para recalcular nuevamente el saldo actual
								 */
		
								UtilidadTransaccion.getTransaccion().begin();
								listaDTONotaPaciente = 
									notaPacienteMundo.obtenerDevolucionAbonoPacienteConsecutivo(pacientes.getCodigoPaciente(), 
											consecutivoEscogido, forma.getNaturalezaNotasPacienteSeleccionada(), controlAbonoPacientePorIngreso);
								UtilidadTransaccion.getTransaccion().commit();
								
								/**
								 * Variables que Muestra de resultado al usuario
								 */
		
								String fecha = UtilidadFecha.getFechaActual();
								String hora = UtilidadFecha.getHoraActual();
								
								forma.setConsecutivo(consecutivoEscogido);
								forma.setFechaNota(fecha);
								forma.setHoraNota(hora);
								forma.setUsuarioGeneraNota(usuario.getNombreUsuario());
								forma.setLoginUsuario(usuario.getLoginUsuario());
								
								forma.getDtoNotaPaciente().setListaInfoNotaPaciente(listaDTONotaPaciente);
								forma.getDtoNotaPaciente().setConsecutivo(consecutivoEscogido);
								forma.getDtoNotaPaciente().setFecha(fecha);
								forma.getDtoNotaPaciente().setHora(hora);
								forma.getDtoNotaPaciente().setUsuarioGeneraNota(usuario.getNombreUsuario() + " (" +usuario.getLoginUsuario() + ")");
								forma.getDtoNotaPaciente().setCentroAtencionOrigen(forma.getCentroAtencionOrigen().getConsecutivo());
								forma.getDtoNotaPaciente().setNombreCentroAtencionOrigen(forma.getCentroAtencionOrigen().getDescripcion());
								forma.getDtoNotaPaciente().setNombreCompletoPaciente(forma.getNombreCompleto());
								forma.getDtoNotaPaciente().setIdentificacionPaciente(forma.getTipoIdentificacionPac() + " " + forma.getNumeroIdentificacion());
								forma.getDtoNotaPaciente().setNaturalezaNota(forma.getNombreNaturalezaNota());
								forma.getDtoNotaPaciente().setDescripcionConcepto(forma.getDtoConceptoNotaPaciente().getDescripcion());
								forma.getDtoNotaPaciente().setObservaciones(forma.getObservacionesNotaPaciente());
								
								if (manejaConsecutivoNotasPacientesXCentroAtencion) {
									if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoDebito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(EmunConsecutivosTesoreriaCentroAtencion.NotasDebitoPacientes.getNombreConsecutivoBaseDatos(), 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
									} else if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoCredito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(EmunConsecutivosTesoreriaCentroAtencion.NotasCreditoPacientes.getNombreConsecutivoBaseDatos(), 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
									}
								} else {
									if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoDebito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoNotasDebitoPacientes, 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
									} else if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoCredito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoNotasCreditoPacientes, 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
									}
								}
								
							} else {
//								UtilidadTransaccion.getTransaccion().rollback();
								if (manejaConsecutivoNotasPacientesXCentroAtencion) {
									if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoDebito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(EmunConsecutivosTesoreriaCentroAtencion.NotasDebitoPacientes.getNombreConsecutivoBaseDatos(), 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
									} else if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoCredito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(EmunConsecutivosTesoreriaCentroAtencion.NotasCreditoPacientes.getNombreConsecutivoBaseDatos(), 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
									}
								} else {
									if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoDebito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoNotasDebitoPacientes, 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
									} else if (forma.getNaturalezaNotasPacienteSeleccionada().equals(ConstantesIntegridadDominio.acronimoCredito)) {
										UtilidadBD.cambiarUsoFinalizadoConsecutivo(ConstantesBD.nombreConsecutivoNotasCreditoPacientes, 
												codigoInstitucion, consecutivoEscogido.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
									}
								}
								errores.add("", new ActionMessage("errors.notEspecific", "Error Guardando los datos"));
							}
						}
					} else {
						errores.add("", new ActionMessage("errors.notEspecific", "Ocurrio un error obteniendo el consecutivo. Intentelo de nuevo"));
					}
				} else {
					errores.add("", new ActionMessage("errors.notEspecific", "Debe digitar el valor de la nota."));
				}
			} else {
				errores.add("", new ActionMessage("errors.notEspecific", "Existe algun valor negativo."));
			}
			saveErrors(request, errores);
			return mapping.findForward("paginaPrincipal");	
		} catch(Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			errores.add("", new ActionMessage("errors.notEspecific", "Error en la inserción de los datos."));
			saveErrors(request, errores);
			e.printStackTrace();
			return mapping.findForward("paginaError");
		}
	}


	
	/**
	 * METODO QUE CARGA LA INFORMACION DE LA DEVOLUCION  ABONO
	 */
	
	private ActionForward accionValidarPaciente(NotasPacientesForm forma,UsuarioBasico usuario, 
			ActionMapping mapping, PersonaBasica persona, HttpServletRequest request, 
			ActionErrors errores) 
	{
		String existePaciente = (String) request.getParameter("existePaciente");
		String origen = (String) request.getParameter("origen");
		int codigoInstitucion=usuario.getCodigoInstitucionInt();
		
		if(!UtilidadTexto.getBoolean(existePaciente) && "ingresoPacienteOdon".equals(origen)){
			
			forma.setTipoIdentificacionPac("");
			forma.setNumeroIdentificacionPac("");
			
			forma.setEstado("empezar");
			
			return mapping.findForward("paginaPrincipal");
			
		}
			
		
		Connection con = UtilidadBD.abrirConexion();
		
		try 
		{
			forma.setExistePaciente(UtilidadValidacion.existePaciente(con, forma.getTipoIdentificacionPac(), forma.getNumeroIdentificacionPac()));
			
			if(forma.isExistePaciente())
			{
				/**
				 * Cargando el paciente
				 */
				persona.setCodigoPersona(UtilidadValidacion.getCodigoPersona(con, forma.getTipoIdentificacionPac(), forma.getNumeroIdentificacionPac()));
				forma.getPaciente().setCodigo(persona.getCodigoPersona());
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, persona, request);
				
				forma.setNumeroIdentificacion(persona.getNumeroIdentificacionPersona());
				forma.setNombreCompleto(persona.getNombrePersona());

				/**
				 * Se obtiene la naturaleza de Notas pacientes parametrizada
				 */
				String naturalezaNotas = ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt());
				forma.setNaturalezaNotasPaciente(naturalezaNotas);
				
				if (naturalezaNotas.equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
					forma.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptoNotaPacientexNaturalezaEstadoActivo(
							ConstantesIntegridadDominio.acronimoDebito));
				} else {
					forma.setNaturalezaNotasPacienteSeleccionada(naturalezaNotas);
					forma.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptoNotaPacientexNaturalezaEstadoActivo(
							forma.getNaturalezaNotasPaciente()));
				}
				
				/**
					 * Parametro general Controlar Abono de Pacientes por No de Ingreso 
					 * Devuelve S o N
					 */
					String controlAbonoPacientePorIngreso = ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(codigoInstitucion);
					
					/**
					 * Mostrar la logica de Registrar Nota de Devolucion Abonos Paciente
					 */
					
					if(controlAbonoPacientePorIngreso.equals(ConstantesBD.acronimoSi)){

						/**
						 * Mostrar la logica de Registrar Nota Paciente por Ingreso (varios ingresos del paciente) 
						 * totalizado
						 */
						UtilidadTransaccion.getTransaccion().begin();
						ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> dtoNotasPaciente =
							notaPacienteMundo.cargarIngresosPorPaciente(persona.getCodigoPersona(),controlAbonoPacientePorIngreso);	
						if (!dtoNotasPaciente.isEmpty()) {
							forma.setControlaAbonoPacienteXIngreso(true);//Afecta la interfaz
							forma.getDtoNotaPaciente().setListaDtoInfoIngresoPacienteControlarAbonoPacientes(dtoNotasPaciente);
							forma.setTotalValorDevolucion("0");
							forma.setTotalNuevoSaldo(forma.getDtoNotaPaciente().getTotalNuevoSaldo());
						} else {
							forma.setExistePaciente(false);
							errores.add("SinInformacion", 
									new ActionMessage("errors.notEspecific", 
											fuenteMensaje.getMessage("notasPaciente.mensaje.pacienteSinNotas")));
							saveErrors(request, errores);
						}
						UtilidadTransaccion.getTransaccion().commit();
						return mapping.findForward("paginaPrincipal");
					}
							/**
							 * Carga la informacion saldos del paciente
							 */
					else {
						UtilidadTransaccion.getTransaccion().begin();
						forma.setControlaAbonoPacienteXIngreso(false);//Afecta la interfaz
						forma.getDtoNotaPaciente().setListaDtoInfoIngresoPacienteControlarAbonoPacientes(
								notaPacienteMundo.cargarAbonoDisponiblePorPaciente(persona.getCodigoPersona()));
						forma.setTotalValorDevolucion("0");
						forma.setTotalNuevoSaldo(forma.getDtoNotaPaciente().getTotalNuevoSaldo());
						UtilidadTransaccion.getTransaccion().commit();
						return mapping.findForward("paginaPrincipal");
					}
				}
			else
			{
					errores.add("", new ActionMessage("errors.notEspecific", "El Paciente no existe"));
					saveErrors(request, errores);
					return mapping.findForward("paginaPrincipal");	
			}
			
		}catch (SQLException e) 
		{
			Log4JManager.error("Error validando si existe el paciente: ",e);
		}
		
		UtilidadBD.closeConnection(con);

		return mapping.findForward("paginaPrincipal");


	}
	
	/**
	 * Método que obtiene el código del plan de tratamiento 
	 * del paciente según un conjunto de estado.
	 * 
	 * @param forma
	 * @return 
	 */
	private int obtenerCodigoPlanTratamientoPorEstados (NotasPacientesForm forma, ArrayList<String> estados){
		
		ArrayList<BigDecimal> codigosPlanT = PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
		
		if(codigosPlanT!=null)
		{
			return codigosPlanT.size();
			
		}else{
			
			return ConstantesBD.codigoNuncaValido;
		}
	}
	/**
	 * 
	 * Método que se encarga de determinar como se debe validar el campo
	 * de ingreso de número de identificación del paciente
	 * 
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 */
	private void determinarValidacionIdentificacion(NotasPacientesForm forma, HttpServletRequest request, int codigoInstitucion) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		ITiposIdentificacionServicio tiposIdentificacionServicio = AdministracionFabricaServicio.crearTiposIdentificacionServicio();

		String numDigCaptNumIdPac = ValoresPorDefecto.getNumDigCaptNumIdPac(codigoInstitucion);
		
		if(UtilidadTexto.isNumber(numDigCaptNumIdPac)){
			
			forma.setNumDigCaptNumIdPac(Integer.parseInt(numDigCaptNumIdPac));
			
		}else{
			
			forma.setNumDigCaptNumIdPac(20);
		}
		
		if(!"".equals(forma.getTipoIdentificacionPac())){
			
			TiposIdentificacion tipoIdentificacion = tiposIdentificacionServicio.obtenerTipoIdentificacionPorAcronimo(forma.getTipoIdentificacionPac());
			
			if(tipoIdentificacion!=null){
				
				if(tipoIdentificacion.getSoloNumeros() != null && 
						tipoIdentificacion.getSoloNumeros().equals(ConstantesBD.acronimoSi.charAt(0))){
					
					request.setAttribute("validacionCampo", "soloNumero");

				}else{
					
					request.setAttribute("validacionCampo", "alfanumerico");
				}
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	


	/**
	 * Método encarcado de validar si existe un consecutivo disponible para 
	 * registrar la nota
	 * @param con 
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private boolean validarConsecutivoDisponible(
			Connection con, HttpServletRequest request, 
			UsuarioBasico usuario, NotasPacientesForm forma, 
			ActionErrors errores) {
		
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		int codigoCentroAtencion = usuario.getCodigoCentroAtencion();		
		String consecutivoDebito = "";
		String consecutivoCredito = "";
		boolean consecutivoEncontrado = false;
		boolean manejaConsecutivoNotasPacientesXCentroAtencion = 
			UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosNotasPacientesCentroAtencion(codigoInstitucion));
		//SIGUIENTE CONSECUTIVO POR CENTRO DE ATENCION
		//ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion

		//SIGUIENTE CONSECUTIVO INSTITUCION
		//UtilidadBD.obtenerValorConsecutivoDisponible

		String naturalezaNotasPacientes = ValoresPorDefecto.getNaturalezaNotasPacientesManejar(codigoInstitucion);
		
		if (manejaConsecutivoNotasPacientesXCentroAtencion) {
			if (naturalezaNotasPacientes.equals(ConstantesIntegridadDominio.acronimoDebito)) {
				consecutivoDebito = ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(
						codigoCentroAtencion, EmunConsecutivosTesoreriaCentroAtencion.NotasDebitoPacientes.getNombreConsecutivoBaseDatos(), UtilidadFecha.getAnioActual()).toString();
				if (!UtilidadTexto.isEmpty(consecutivoDebito) && UtilidadTexto.isNumber(consecutivoDebito)) {
					int consec = Integer.parseInt(consecutivoDebito);
					if (consec > 0) {
						consecutivoEncontrado = true;
					}else{
						errores.add("falta definir consecutivo Notas Devolucion Abonos Paciente", 
								new ActionMessage("errors.faltaDefinirConsecutivoCentroAtencionNotasDebito"));
					}
				}
			} else if (naturalezaNotasPacientes.equals(ConstantesIntegridadDominio.acronimoCredito)) {
				consecutivoCredito = ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(
						codigoCentroAtencion, EmunConsecutivosTesoreriaCentroAtencion.NotasCreditoPacientes.getNombreConsecutivoBaseDatos(), UtilidadFecha.getAnioActual()).toString();
				if (!UtilidadTexto.isEmpty(consecutivoCredito) && UtilidadTexto.isNumber(consecutivoCredito)) {
					int consec = Integer.parseInt(consecutivoCredito);
					if (consec > 0) {
						consecutivoEncontrado = true;
					}else{
						errores.add("falta definir consecutivo Notas Devolucion Abonos Paciente", 
								new ActionMessage("errors.faltaDefinirConsecutivoCentroAtencionNotasCredito"));
					}
				}
			} else if (naturalezaNotasPacientes.equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
				consecutivoDebito = ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(
						codigoCentroAtencion, EmunConsecutivosTesoreriaCentroAtencion.NotasDebitoPacientes.getNombreConsecutivoBaseDatos(), UtilidadFecha.getAnioActual()).toString();
				consecutivoCredito = ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(
						codigoCentroAtencion, EmunConsecutivosTesoreriaCentroAtencion.NotasCreditoPacientes.getNombreConsecutivoBaseDatos(), UtilidadFecha.getAnioActual()).toString();
				if (!UtilidadTexto.isEmpty(consecutivoDebito) && UtilidadTexto.isNumber(consecutivoDebito) &&
						!UtilidadTexto.isEmpty(consecutivoCredito) && UtilidadTexto.isNumber(consecutivoCredito)) {
					int consecDebito = Integer.parseInt(consecutivoDebito);
					int consecCredito = Integer.parseInt(consecutivoCredito);
					if (consecDebito > 0 && consecCredito > 0) {
						consecutivoEncontrado = true;
					}else{
						errores.add("falta definir consecutivo Notas Devolucion Abonos Paciente", 
								new ActionMessage("errors.faltaDefinirConsecutivoCentroAtencionNotasDebitoCredito"));
					}
				}
			}
		} else{
			if (naturalezaNotasPacientes.equals(ConstantesIntegridadDominio.acronimoDebito)) {
				consecutivoDebito = UtilidadBD.obtenerValorActualTablaConsecutivos(
						con, ConstantesBD.nombreConsecutivoNotasDebitoPacientes, usuario.getCodigoInstitucionInt());
				if (!UtilidadTexto.isEmpty(consecutivoDebito) && UtilidadTexto.isNumber(consecutivoDebito)) {
					int consec = Integer.parseInt(consecutivoDebito);
					if (consec > 0) {
						consecutivoEncontrado = true;
					}else{
						errores.add("falta definir consecutivo Notas Devolucion Abonos Paciente", 
								new ActionMessage("errors.faltaDefinirConsecutivoNotasDebito"));
					}
				}
			} else if (naturalezaNotasPacientes.equals(ConstantesIntegridadDominio.acronimoCredito)) {
				consecutivoCredito = UtilidadBD.obtenerValorActualTablaConsecutivos(
						con, ConstantesBD.nombreConsecutivoNotasCreditoPacientes, usuario.getCodigoInstitucionInt());
				if (!UtilidadTexto.isEmpty(consecutivoCredito) && UtilidadTexto.isNumber(consecutivoCredito)) {
					int consec = Integer.parseInt(consecutivoCredito);
					if (consec > 0) {
						consecutivoEncontrado = true;
					}else{
						errores.add("falta definir consecutivo Notas Devolucion Abonos Paciente", 
								new ActionMessage("errors.faltaDefinirConsecutivoNotasCredito"));
					}
				}
			} else if (naturalezaNotasPacientes.equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
				consecutivoDebito = UtilidadBD.obtenerValorActualTablaConsecutivos(
						con, ConstantesBD.nombreConsecutivoNotasDebitoPacientes, usuario.getCodigoInstitucionInt());
				consecutivoCredito = UtilidadBD.obtenerValorActualTablaConsecutivos(
						con, ConstantesBD.nombreConsecutivoNotasCreditoPacientes, usuario.getCodigoInstitucionInt());
				if (!UtilidadTexto.isEmpty(consecutivoDebito) && UtilidadTexto.isNumber(consecutivoDebito) &&
						!UtilidadTexto.isEmpty(consecutivoCredito) && UtilidadTexto.isNumber(consecutivoCredito)) {
					int consecDebito = Integer.parseInt(consecutivoDebito);
					int consecCredito = Integer.parseInt(consecutivoCredito);
					if (consecDebito > 0 && consecCredito > 0) {
						consecutivoEncontrado = true;
					}else{
						errores.add("falta definir consecutivo Notas Devolucion Abonos Paciente", 
								new ActionMessage("errors.faltaDefinirConsecutivoNotasDebitoCredito"));
					}
				}
			}
		}
		saveErrors(request, errores);
		return consecutivoEncontrado;
	}
	
	/**
	 * Método encargado de inicializar los valores para la busqueda de Notas Paciente
	 * @param con
	 * @param request
	 * @param usuario
	 * @param forma
	 * @param errores
	 * @return
	 */
	private String cargarBusquedaNotasPacientePorRango(Connection con, HttpServletRequest request, 
			UsuarioBasico usuario, NotasPacientesForm forma, ActionErrors errores) {
		if (ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())
				.equals(ConstantesBD.acronimoSi)) {
			forma.setListaEmpresaInstitucion(FacturacionServicioFabrica
					.crearEmpresasInstitucionServicio()
					.listarEmpresaInstitucion());
			forma.setInstitucionMultiempresa(true);
		} else {
			forma.setInstitucionMultiempresa(false);
		}

		UtilidadTransaccion.getTransaccion().begin();
		ArrayList<CentroAtencion> centrosAtencion = 
			notaPacienteMundo.obtenerCentrosAtencionActivosUsuario(usuario.getLoginUsuario());
		UtilidadTransaccion.getTransaccion().commit();
		
		forma.setListaCentrosAtencion(centrosAtencion);

		forma.setCentroAtencionOrigenHelper(ConstantesBD.codigoNuncaValido);
		
		forma.setCentrosAtencionSeleccionados(new String[centrosAtencion.size()]);
		forma.setCentrosAtencionSeleccionadosTmp(new String[centrosAtencion.size()]);
		
		forma.setListaUsuarios(AdministracionFabricaServicio
				.crearUsuariosServicio().obtenerUsuariosSistemas(
						usuario.getCodigoInstitucionInt(), true));
		/**
		 * Se obtiene la naturaleza de Notas pacientes parametrizada
		 */
		String naturalezaNotas = ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt());
		forma.setNaturalezaNotasPaciente(naturalezaNotas);
		
		String manejoEspecialInstOdonto = ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt());
		forma.setManejoEspecialInstOdonto(UtilidadTexto.getBoolean(manejoEspecialInstOdonto));
		
		String controlaAbonoPacientePorNumIngreso = ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(usuario.getCodigoInstitucionInt());
		forma.setControlaAbonoPacienteXIngreso(UtilidadTexto.getBoolean(controlaAbonoPacientePorNumIngreso));
		
		if (naturalezaNotas.equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
			forma.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptosNotaPaciente());
		} else if (naturalezaNotas.equals(ConstantesIntegridadDominio.acronimoDebito)) {
			forma.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptoNotaPacientexNaturaleza(
					forma.getNaturalezaNotasPaciente()));
		} else if (naturalezaNotas.equals(ConstantesIntegridadDominio.acronimoCredito)) {
			forma.setListaConceptosNotasPacientes(notaPacienteMundo.listarConceptoNotaPacientexNaturaleza(
					forma.getNaturalezaNotasPaciente()));
		}
		
		return "busquedaNotasPacientePorRango";
	}
	
	/**
	 * Método encargado de consultar las notas pacientes por rango según los 
	 * filtros ingresados 
	 * @param con
	 * @param request
	 * @param usuario
	 * @param forma
	 * @param errores
	 * @param institucionBasica
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String consultarNotasPacientePorRango(Connection con, HttpServletRequest request, 
			UsuarioBasico usuario, NotasPacientesForm forma, ActionErrors errores, InstitucionBasica institucionBasica) {
		boolean existeError = false;
		DtoBusquedaNotasPacientePorRango dtoBusqueda = new DtoBusquedaNotasPacientePorRango();
		ArrayList<Integer> codigoCentrosAtencion = new ArrayList<Integer>();
		String nombreInstitucion = ""; 
		String paginaCargar = "resultadoNotasPacientePorRango";
		try {
			if (!UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda()) || !UtilidadTexto.isEmpty(forma.getFechaFinalBusqueda())) {
				if (UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda()) || UtilidadTexto.isEmpty(forma.getFechaFinalBusqueda())) {
					errores.add("", new ActionMessage("errors.required","Fecha Inicial Generacion Notas Y Fecha Final Generacion Notas"));
					existeError = true;
				}
			}
			if (!UtilidadTexto.isEmpty(forma.getNumeroNotaInicialBusqueda()) || !UtilidadTexto.isEmpty(forma.getNumeroNotaFinalBusqueda())) {
				if (UtilidadTexto.isEmpty(forma.getNumeroNotaInicialBusqueda()) || UtilidadTexto.isEmpty(forma.getNumeroNotaFinalBusqueda())) {
					errores.add("", new ActionMessage("errors.required","Nro. Nota Inicial y Nro. Nota Final"));
					existeError = true;
				}
			}
			if (UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda()) && UtilidadTexto.isEmpty(forma.getFechaFinalBusqueda()) &&
					UtilidadTexto.isEmpty(forma.getNumeroNotaInicialBusqueda()) && UtilidadTexto.isEmpty(forma.getNumeroNotaFinalBusqueda())) {
				errores.add("", new ActionMessage("errors.required","Es requerido ingresar el rango de fechas o el rango de números de notas para realizar la consulta.  Por favor Verifique"));
				existeError = true;
			}
			if(!existeError) {
				if (!UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda())) {
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicialBusqueda(), 
							UtilidadFecha.conversionFormatoFechaAAp(Utilidades.capturarFechaBD()))) {
						errores.add("FechaInicial", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", 
								fuenteMensaje.getMessage("notasPaciente.busqueda.fechaInicial"), 
								fuenteMensaje.getMessage("notasPaciente.busqueda.fechaActual")));
						existeError = true;
					}
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicialBusqueda(), forma.getFechaFinalBusqueda()) ||
							!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinalBusqueda(), 
									UtilidadFecha.conversionFormatoFechaAAp(Utilidades.capturarFechaBD()))) {
						errores.add("FechaFinal", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", 
								fuenteMensaje.getMessage("notasPaciente.busqueda.fechaFinal"),
								fuenteMensaje.getMessage("notasPaciente.busqueda.fechaActual")));
						existeError = true;
					}
				}
			}
			if (!existeError) {
				if (forma.isInstitucionMultiempresa()) {
					if (forma.getEmpresaInstitucionHelper() != ConstantesBD.codigoNuncaValidoLong) {
						dtoBusqueda.setCodigoEmpresaInstitucion(forma.getEmpresaInstitucion().getCodigo());
					} else {
						dtoBusqueda.setCodigoEmpresaInstitucion(ConstantesBD.codigoNuncaValidoLong);
					}
				} else {
					nombreInstitucion = institucionBasica.getRazonSocial();
					dtoBusqueda.setCodigoEmpresaInstitucion(ConstantesBD.codigoNuncaValidoLong);
				}
				
				for (int i = 0; i < forma.getCentrosAtencionSeleccionados().length; i++) {
					if(!UtilidadTexto.isEmpty(forma.getCentrosAtencionSeleccionados()[i])) {
						int codigoCentro = Integer.parseInt(forma.getCentrosAtencionSeleccionados()[i]);
						codigoCentrosAtencion.add(codigoCentro);
					}
				}
				dtoBusqueda.setCodigosCentrosAtencion(codigoCentrosAtencion);
				if (!UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda())) {
					dtoBusqueda.setFechaInicialGeneracion(Date.valueOf(
							UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialBusqueda())));
					dtoBusqueda.setFechaFinalGeneracion(Date.valueOf(
							UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalBusqueda())));
				}
				if (!UtilidadTexto.isEmpty(forma.getNumeroNotaInicialBusqueda())) {
					dtoBusqueda.setNumeroInicialNota(Integer.parseInt(forma.getNumeroNotaInicialBusqueda()));
					dtoBusqueda.setNumeroFinalNota(Integer.parseInt(forma.getNumeroNotaFinalBusqueda()));
				} else {
					dtoBusqueda.setNumeroInicialNota(ConstantesBD.codigoNuncaValidoLong);
					dtoBusqueda.setNumeroFinalNota(ConstantesBD.codigoNuncaValidoLong);
				}
				dtoBusqueda.setNaturalezaNota(forma.getNaturalezaNotaBusqueda());
				if (forma.getDtoConceptoNotaPaciente() != null && 
						!forma.getDtoConceptoNotaPaciente().equals(ConstantesBD.codigoNuncaValido+"")) {
					dtoBusqueda.setCodigoPkConceptoNotasPaciente(forma.getDtoConceptoNotaPaciente().getCodigoPk());
				} else {
					dtoBusqueda.setCodigoPkConceptoNotasPaciente(ConstantesBD.codigoNuncaValidoLong);
				}
				if (forma.getUsuarioGenera() != null && 
						!forma.getUsuarioGenera().equals(ConstantesBD.codigoNuncaValido+"")) {
					dtoBusqueda.setUsuarioGeneraNotas(forma.getUsuarioGenera().getLogin());
				}
				
				UtilidadTransaccion.getTransaccion().begin();
				
				LinkedHashMap<String, DtoNotasPorNaturaleza> resultados = 
					notaPacienteMundo.consultarNotasPacientePorRango(dtoBusqueda, 
							forma.isInstitucionMultiempresa(), nombreInstitucion, 
							forma.isControlaAbonoPacienteXIngreso(),
							forma.isManejoEspecialInstOdonto(),
							forma);
				
				int numeroNotas = 0;
				long consecutivoNotaUnico = ConstantesBD.codigoNuncaValidoLong;
				if (!resultados.isEmpty()) {
					if (resultados.size() == 1) {
						Iterator iterator = resultados.keySet().iterator();
						while (iterator.hasNext()){
							String key = (String) iterator.next();
							DtoNotasPorNaturaleza dtoNotasPaciente = (DtoNotasPorNaturaleza) resultados.get(key);
							numeroNotas = dtoNotasPaciente.getDtoResumenNotasCredito().size() + 
										  dtoNotasPaciente.getDtoResumenNotasDebito().size();
							if (numeroNotas == 1) {
								if(!dtoNotasPaciente.getDtoResumenNotasCredito().isEmpty()) {
									consecutivoNotaUnico = dtoNotasPaciente.getDtoResumenNotasCredito().get(0).getCodigoPkNotaPaciente();
								}
								if(!dtoNotasPaciente.getDtoResumenNotasDebito().isEmpty()) {
									consecutivoNotaUnico = dtoNotasPaciente.getDtoResumenNotasDebito().get(0).getCodigoPkNotaPaciente();
								}
							}
						}
					} 
					if (numeroNotas == 1) {
						forma.setEsUnicoResultado(true);
						forma.setCodigoPkNotaSeleccionado(String.valueOf(consecutivoNotaUnico));
						if (forma.isControlaAbonoPacienteXIngreso()) {
							paginaCargar = mostrarDetalleNotaPacientePorIngreso(request,usuario, forma, errores);
						} else {
							paginaCargar = mostrarDetalleNotaPaciente(request,usuario, forma, errores);
						}
					} else {
						forma.setMapaDtoResumenNotasPaciente(resultados);
					}
				} else {
					forma.setCentrosAtencionSeleccionadosTmp(forma.getCentrosAtencionSeleccionados());
					forma.setCentrosAtencionSeleccionados(new String[forma.getListaCentrosAtencion().size()]);
					errores.add("SinResultados", new ActionMessage("errores.modTesoreria.noResultados"));
					paginaCargar = "busquedaNotasPacientePorRango";
				}
				UtilidadTransaccion.getTransaccion().commit();
			} else {
				forma.setCentrosAtencionSeleccionadosTmp(forma.getCentrosAtencionSeleccionados());
				forma.setCentrosAtencionSeleccionados(new String[forma.getListaCentrosAtencion().size()]);
				paginaCargar = "busquedaNotasPacientePorRango";
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			saveErrors(request, errores);
			return "busquedaNotasPacientePorRango";
		}
		saveErrors(request, errores);
		return paginaCargar;
	}
	
	/**
	 * Método encargado de cargar los datos del detalle de la Nota Paciente
	 * seleccionada
	 * @param request
	 * @param usuario
	 * @param forma
	 * @param errores
	 * @return
	 */
	private String mostrarDetalleNotaPaciente(HttpServletRequest request, 
			UsuarioBasico usuario, NotasPacientesForm forma, ActionErrors errores) {
		if (!UtilidadTexto.isEmpty(forma.getCodigoPkNotaSeleccionado())) {
			try {
				long consecutivoNota = Long.parseLong(forma.getCodigoPkNotaSeleccionado());
				UtilidadTransaccion.getTransaccion().begin();
				
				DTONotaPaciente dtoNotaPaciente = notaPacienteMundo.obtenerNotaPacienteConsecutivo(consecutivoNota, 
						forma.isControlaAbonoPacienteXIngreso());
				
				if (dtoNotaPaciente != null) {
					forma.setDtoNotaPaciente(dtoNotaPaciente);
				}
				else {
					errores.add("ERROR", new ActionMessage("errors.required","Ocurrio un error"));
				}
				
				UtilidadTransaccion.getTransaccion().commit();
			} catch (Exception e) {
				e.printStackTrace();
				saveErrors(request, errores);
				return "resumenNotaPaciente";
			}
		} else {
			errores.add("ERROR", new ActionMessage("errors.required","Ocurrio un error"));
		}
		saveErrors(request, errores);
		return "resumenNotaPaciente";
	}
	
	/**
	 * Método encargado de cargar el detalle de la nota paciente seleccionada
	 * para notas que menejan ingreso por centro atención
	 * @param request
	 * @param usuario
	 * @param forma
	 * @param errores
	 * @return
	 */
	private String mostrarDetalleNotaPacientePorIngreso(HttpServletRequest request, 
					UsuarioBasico usuario, NotasPacientesForm forma, ActionErrors errores) {
		if (!UtilidadTexto.isEmpty(forma.getCodigoPkNotaSeleccionado())) {
			try {
				long consecutivoNota = Long.parseLong(forma.getCodigoPkNotaSeleccionado());
				UtilidadTransaccion.getTransaccion().begin();
				
				DTONotaPaciente dtoNotaPaciente = notaPacienteMundo.obtenerNotaPacienteConsecutivo(consecutivoNota, 
						forma.isControlaAbonoPacienteXIngreso());
				
				if (dtoNotaPaciente != null) {
					forma.setDtoNotaPaciente(dtoNotaPaciente);
				}
				else {
					errores.add("ERROR", new ActionMessage("errors.required","Ocurrio un error"));
				}
				
				UtilidadTransaccion.getTransaccion().commit();
			} catch (Exception e) {
				e.printStackTrace();
				saveErrors(request, errores);
				return "resumenNotaPacientePorIngreso";
			}
		} else {
			errores.add("ERROR", new ActionMessage("errors.required","Ocurrio un error"));
		}
		saveErrors(request, errores);
		return "resumenNotaPacientePorIngreso";
	}
	
	/**
	 * Método encargado de consultar las notas paciente para un paciente específico
	 * @param request
	 * @param usuario
	 * @param forma
	 * @param errores
	 * @param institucionBasica
	 * @param codigoPersona
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String consultarNotasPacientePorPaciente(HttpServletRequest request, 
			UsuarioBasico usuario, NotasPacientesForm forma, ActionErrors errores, 
			InstitucionBasica institucionBasica, int codigoPersona) {
		String paginaCargar = "resultadoNotasPacientePorRango";
		
		boolean esInstitucionMultiempresa = UtilidadTexto.getBoolean(
				ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
		
		String naturalezaNotas = ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt());
		forma.setNaturalezaNotasPaciente(naturalezaNotas);
		
		String manejoEspecialInstOdonto = ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt());
		forma.setManejoEspecialInstOdonto(UtilidadTexto.getBoolean(manejoEspecialInstOdonto));
		
		String controlaAbonoPacientePorNumIngreso = ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(usuario.getCodigoInstitucionInt());
		forma.setControlaAbonoPacienteXIngreso(UtilidadTexto.getBoolean(controlaAbonoPacientePorNumIngreso));
		
		UtilidadTransaccion.getTransaccion().begin();
		
		LinkedHashMap<String, DtoNotasPorNaturaleza> resultados = 
			notaPacienteMundo.consultarNotasPacientePorPaciente(
				codigoPersona, esInstitucionMultiempresa, institucionBasica.getRazonSocial(),
				forma.isControlaAbonoPacienteXIngreso(), forma);
		int numeroNotas = 0;
		long consecutivoNotaUnico = ConstantesBD.codigoNuncaValidoLong;
		if (!resultados.isEmpty()) {
			if (resultados.size() == 1) {
				Iterator iterator = resultados.keySet().iterator();
				while (iterator.hasNext()){
					String key = (String) iterator.next();
					DtoNotasPorNaturaleza dtoNotasPaciente = (DtoNotasPorNaturaleza) resultados.get(key);
					numeroNotas = dtoNotasPaciente.getDtoResumenNotasCredito().size() + 
								  dtoNotasPaciente.getDtoResumenNotasDebito().size();
					if (numeroNotas == 1) {
						if(!dtoNotasPaciente.getDtoResumenNotasCredito().isEmpty()) {
							consecutivoNotaUnico = dtoNotasPaciente.getDtoResumenNotasCredito().get(0).getCodigoPkNotaPaciente();
						}
						if(!dtoNotasPaciente.getDtoResumenNotasDebito().isEmpty()) {
							consecutivoNotaUnico = dtoNotasPaciente.getDtoResumenNotasDebito().get(0).getCodigoPkNotaPaciente();
						}
					}
				}
			} 
			if (numeroNotas == 1) {
				forma.setEsUnicoResultado(true);
				forma.setCodigoPkNotaSeleccionado(String.valueOf(consecutivoNotaUnico));
				if (forma.isControlaAbonoPacienteXIngreso()) {
					paginaCargar = mostrarDetalleNotaPacientePorIngreso(request,usuario, forma, errores);
				} else {
					paginaCargar = mostrarDetalleNotaPaciente(request,usuario, forma, errores);
				}
				
			} else {
				forma.setMapaDtoResumenNotasPaciente(resultados);
			}
		} else {
			errores.add("SinResultados", new ActionMessage("errors.notEspecific", 
					fuenteMensaje.getMessage("notasPaciente.mensaje.noSeHanGeneradoNotas")));
			paginaCargar = "sinResultados";
		}
		UtilidadTransaccion.getTransaccion().commit();
		saveErrors(request, errores);
		return paginaCargar;
	}
	
	/**
	 * Método encargado de generar el reporte consolidado de notas pacientes
	 * en todos sus formatos
	 * @param request
	 * @param usuarioBasico
	 * @param forma
	 * @param errores
	 * @param institucionBasica
	 * @param idTipoSalida
	 * @return
	 */
	private JasperReportBuilder generarReporteConsolidado(HttpServletRequest request, 
			UsuarioBasico usuarioBasico, NotasPacientesForm forma, ActionErrors errores, 
			InstitucionBasica institucionBasica, int idTipoSalida) {
		
		GeneradorReporteNotasPacientes generadorReporte = new GeneradorReporteNotasPacientes();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Boolean> paramsValidacion = new HashMap<String, Boolean>();
		JasperReportBuilder reporteGenerado = null;
		DtoPersonas dtoPersona = null;
		try {
			if (!forma.isBusquedaPorRango()) {
				PersonaBasica personaBasica =(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				UtilidadTransaccion.getTransaccion().begin();
				dtoPersona = 
					notaPacienteMundo.obtenerDatosPersona(personaBasica.getCodigoPersona());
				UtilidadTransaccion.getTransaccion().commit();
			}
			
			params.put(IConstantesReporte.nombreInstitucion, institucionBasica.getRazonSocial());
			params.put(IConstantesReporte.nitInstitucion, institucionBasica.getDescripcionTipoIdentificacion() + ": " + institucionBasica.getNit());
			params.put(IConstantesReporte.actividadEconomica, institucionBasica.getActividadEconomica());
			params.put(IConstantesReporte.fecha, Utilidades.capturarFechaBD());
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			params.put(IConstantesReporte.fechaProcesa, sdf.format(UtilidadFecha.getFechaActualTipoBD())+" "+UtilidadFecha.getHoraActual());
			params.put(IConstantesReporte.rutaLogo, institucionBasica.getLogoReportes());
			params.put(IConstantesReporte.ubicacionLogo, institucionBasica.getUbicacionLogo());
			params.put(IConstantesReporte.usuarioProceso, usuarioBasico.getNombreUsuario());
			params.put(IConstantesReporte.loginUsuarioProceso, usuarioBasico.getLoginUsuario());
			
			if (!forma.isBusquedaPorRango()) {
				params.put(IConstantesReporte.nombrepaciente, dtoPersona.getNombreCompleto());
				params.put(IConstantesReporte.tipoNumeroID, dtoPersona.getTipoIdentificacion()+ " " + dtoPersona.getNumeroIdentificacion());
				params.put(IConstantesReporte.centroAtencionDuenio, dtoPersona.getDescripcionCentroAtencionDuenio());
			}
			
			paramsValidacion.put(IConstantesReporte.institucionMultiempresa, forma.isInstitucionMultiempresa());
			paramsValidacion.put(IConstantesReporte.manejoEspecialInstOdonto, forma.isManejoEspecialInstOdonto());
			paramsValidacion.put(IConstantesReporte.controlAbonoPacientePorIngreso, forma.isControlaAbonoPacienteXIngreso());
			paramsValidacion.put(IConstantesReporte.reporteRango, forma.isBusquedaPorRango());
			
			if(idTipoSalida == EnumTiposSalida.PDF.getCodigo() || idTipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo()){
				reporteGenerado = generadorReporte.buildReporteConsolidadoNotasPacientes(forma.getMapaDtoResumenNotasPaciente(), params, paramsValidacion);
			} else if(idTipoSalida == EnumTiposSalida.PLANO.getCodigo()){
				reporteGenerado = generadorReporte.buildReporteConsolidadoPlano(forma.getListaDtoResumenNotasPaciente(), params, paramsValidacion); 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reporteGenerado;
	}
	
	/**
	 * Método encargado de generar el reporte de notas pacientes
	 * @param request
	 * @param usuarioBasico
	 * @param forma
	 * @param errores
	 * @param institucionBasica
	 * @return
	 */
	private JasperReportBuilder generarReporteDetalle(HttpServletRequest request, 
			UsuarioBasico usuarioBasico, NotasPacientesForm forma, ActionErrors errores, 
			InstitucionBasica institucionBasica) {
		
		GeneradorReporteNotasPacientes generadorReporte = new GeneradorReporteNotasPacientes();
		Map<String, String> params = new HashMap<String, String>();
		JasperReportBuilder reporteGenerado = null;
		try {
			params.put(IConstantesReporte.nombreInstitucion, institucionBasica.getRazonSocial());
			params.put(IConstantesReporte.nitInstitucion, 
					institucionBasica.getDescripcionTipoIdentificacion() + ": " + institucionBasica.getNit());
			params.put(IConstantesReporte.actividadEconomica, institucionBasica.getActividadEconomica());
			params.put(IConstantesReporte.centroAtencionOrigen, forma.getDtoNotaPaciente().getNombreCentroAtencionOrigen());
			UtilidadTransaccion.getTransaccion().begin();
			params.put(IConstantesReporte.direccionCentroAtencionOrigen, 
					notaPacienteMundo.obtenerDatosCentroAtencionDuenioPaciente(forma.getDtoNotaPaciente().getCentroAtencionOrigen()));
			UtilidadTransaccion.getTransaccion().commit();
			params.put(IConstantesReporte.fecha, Utilidades.capturarFechaBD());
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			params.put(IConstantesReporte.fechaProcesa, sdf.format(UtilidadFecha.getFechaActualTipoBD())+" "+UtilidadFecha.getHoraActual());
			params.put(IConstantesReporte.rutaLogo, institucionBasica.getLogoReportes());
			params.put(IConstantesReporte.ubicacionLogo, institucionBasica.getUbicacionLogo());
			params.put(IConstantesReporte.usuarioProceso, usuarioBasico.getNombreUsuario());
			params.put(IConstantesReporte.loginUsuarioProceso, usuarioBasico.getLoginUsuario());
			
			reporteGenerado = generadorReporte.buildReporteNotasPacientes(forma.getDtoNotaPaciente(), params);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reporteGenerado;
	}
	
	/**
	 * Método encargado de generar el reporte de notas pacientes por ingreso
	 * @param request
	 * @param usuarioBasico
	 * @param forma
	 * @param errores
	 * @param institucionBasica
	 * @return
	 */
	private JasperReportBuilder generarReporteDetalleXIngreso(HttpServletRequest request, 
			UsuarioBasico usuarioBasico, NotasPacientesForm forma, ActionErrors errores, 
			InstitucionBasica institucionBasica) {
		
		GeneradorReporteNotasPacientes generadorReporte = new GeneradorReporteNotasPacientes();
		Map<String, String> params = new HashMap<String, String>();
		JasperReportBuilder reporteGenerado = null;
		try {
			params.put(IConstantesReporte.nombreInstitucion, institucionBasica.getRazonSocial());
			params.put(IConstantesReporte.nitInstitucion, 
					institucionBasica.getDescripcionTipoIdentificacion() + ": " + institucionBasica.getNit());
			params.put(IConstantesReporte.actividadEconomica, institucionBasica.getActividadEconomica());
			params.put(IConstantesReporte.centroAtencionOrigen, forma.getDtoNotaPaciente().getNombreCentroAtencionOrigen());
			UtilidadTransaccion.getTransaccion().begin();
			params.put(IConstantesReporte.direccionCentroAtencionOrigen, 
					notaPacienteMundo.obtenerDatosCentroAtencionDuenioPaciente(forma.getDtoNotaPaciente().getCentroAtencionOrigen()));
			UtilidadTransaccion.getTransaccion().commit();
			params.put(IConstantesReporte.fecha, Utilidades.capturarFechaBD());
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			params.put(IConstantesReporte.fechaProcesa, sdf.format(UtilidadFecha.getFechaActualTipoBD())+" "+UtilidadFecha.getHoraActual());
			params.put(IConstantesReporte.rutaLogo, institucionBasica.getLogoReportes());
			params.put(IConstantesReporte.ubicacionLogo, institucionBasica.getUbicacionLogo());
			params.put(IConstantesReporte.usuarioProceso, usuarioBasico.getNombreUsuario());
			params.put(IConstantesReporte.loginUsuarioProceso, usuarioBasico.getLoginUsuario());
			
			reporteGenerado = generadorReporte.buildReporteNotasPacientesXIngreso(forma.getDtoNotaPaciente(), params);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reporteGenerado;
	}
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	private String accionOrdenar(NotasPacientesForm forma, String tipo){
		
		String retorno = "";
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar() + "descendente")) {
			ordenamiento = true;
		}
		
		SortGenerico sortG = new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		if ("resultadosNotasPacientePorRango".equals(tipo)) {
			if (forma.getNaturalezaOrdenar().equals(ConstantesIntegridadDominio.acronimoDebito)) {
				Collections.sort(forma.getMapaDtoResumenNotasPaciente().get(forma.getClaveOrdenar()).getDtoResumenNotasDebito(), sortG);
			}
			if (forma.getNaturalezaOrdenar().equals(ConstantesIntegridadDominio.acronimoCredito)) {
				Collections.sort(forma.getMapaDtoResumenNotasPaciente().get(forma.getClaveOrdenar()).getDtoResumenNotasCredito(), sortG);
			}
			retorno = "resultadoNotasPacientePorRango";
		} 
		return retorno;
	}
	
	/**
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
