package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.constantes.estadosJsp.IconstantesEstadosJsp;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.actionform.tesoreria.ConsultaConsolidadoCierresForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;
import com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre.GeneradorReporteConsultaConsolidadaCierres;
import com.servinte.axioma.generadorReporte.tesoreria.consultaConsolidadCierre.GeneradorReporteConsultaConsolidadaCierresCajaCajero;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

public class ConsultaConsolidadoCierresAction extends Action {

	/**
	 * Mundo asociado a los cierres
	 */
	IConsultaConsolidadoCierresMundo consultaConsolidadoCierresMundo = TesoreriaFabricaMundo
			.crearConsolidadoCierreMundo();

	/**
	 * Log de aplicacion
	 */
	private Logger logger = Logger
			.getLogger(ConsultaConsolidadoCierresAction.class);

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (form instanceof ConsultaConsolidadoCierresForm) {
			ConsultaConsolidadoCierresForm forma = (ConsultaConsolidadoCierresForm) form;
			// SE OBTIENE EL USUARIO
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			String estado = forma.getEstado();
			if (estado.equals(IconstantesEstadosJsp.ESTADO_EMPEZAR)) {
				// se cargan valores iniciales
				valoreiniciales(forma, response, request, mapping, usuario);
			} else if (estado
					.equals(IconstantesEstadosJsp.ESTADO_GENERAR_REPORTE)) {
				// se consultan los datos
				tipoDeReporte(forma, response, request, mapping, usuario);
			} else if (estado
					.equals(IconstantesEstadosJsp.ESTADO_GENERAR_REPORTE_EXPORTAR)) {
				// se exporta el reporte
				tipoDeReporteAExportar(forma, response, request, mapping,
						usuario);
			}

		}
		return mapping.findForward("inicial");
	}

	/**
	 * Se cargan instituciones para validar tabla en jsp
	 * 
	 * @param forma
	 */
	public void listarInstitucionesCentrosAtencionJsp(
			ConsultaConsolidadoCierresForm forma) {
		ArrayList<String> instituciones = new ArrayList<String>();
		ArrayList<String> centrosAtencion = new ArrayList<String>();
		for (int i = 0; i < forma.getConsolidadoCierresCajaCajero().size(); i++) {

			// se garantiza que no este repetida informacion
			if (!instituciones.contains(forma.getConsolidadoCierresCajaCajero()
					.get(i).getIntitucion())) {
				instituciones.add(forma.getConsolidadoCierresCajaCajero()
						.get(i).getIntitucion());
			}
			if (!centrosAtencion.contains(forma
					.getConsolidadoCierresCajaCajero().get(i)
					.getCentroAtencion())) {
				centrosAtencion.add(forma.getConsolidadoCierresCajaCajero()
						.get(i).getCentroAtencion());
			}
		}

		// set a la forma de inctituciones y centros de atencion
		forma.setInstitucionesCajaCajero(instituciones);
		forma.setCentrosAtencionCajaCajero(centrosAtencion);
	}

	/**
	 * Se cargan instituciones para validar tabla en jsp
	 * 
	 * @param forma
	 */
	public void listarInstitucionesCentrosAtencionJspCentroAtencion(
			ConsultaConsolidadoCierresForm forma) {
		ArrayList<String> instituciones = new ArrayList<String>();
		ArrayList<String> centrosAtencion = new ArrayList<String>();
		for (int i = 0; i < forma.getConsolidadoCierres().size(); i++) {

			// se garantiza que no este repetida informacion
			if (!instituciones.contains(forma.getConsolidadoCierres().get(i)
					.getIntitucion())) {
				instituciones.add(forma.getConsolidadoCierres().get(i)
						.getIntitucion());
			}
			if (!centrosAtencion.contains(forma.getConsolidadoCierres().get(i)
					.getCentroAtencion())) {
				centrosAtencion.add(forma.getConsolidadoCierres().get(i)
						.getCentroAtencion());
			}
		}

		// set a la forma de inctituciones y centros de atencion
		forma.setInstitucionesCajaCajero(instituciones);
		forma.setCentrosAtencionCajaCajero(centrosAtencion);
	}

	/**
	 * Se valida el tipo de reporte a consultar
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void tipoDeReporte(ConsultaConsolidadoCierresForm forma,
			HttpServletResponse response, HttpServletRequest request,
			ActionMapping mapping, UsuarioBasico usuario) {
		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			forma.setConsolidadoCierres(new ArrayList<DtoConsolidadoCierreReporte>());
			forma.setConsolidadoCierresCajaCajero(new ArrayList<DtoConsolidadoCierreReporte>());
			forma.setFormasPago(consultaConsolidadoCierresMundo
					.consultarFormaPago());
			// por tipo de reporte selecionado se realiza la consulta
			if (forma
					.getTipoConsolidadoSeleccionado()
					.equals(ConstantesIntegridadDominio.acronimoConsolidadoCentroAtencion)) {
				forma.setCantidadColSpanCierres(forma.getFormasPago().size() + 2);
				consultarCierresPorCentroAtencion(forma, response, request,
						mapping, usuario);
			} else {
				forma.setCantidadColSpanCierres(forma.getFormasPago().size() + 4);
				consultarCierresPorCajacajero(forma, response, request,
						mapping, usuario);
			}
			UtilidadTransaccion.getTransaccion().commit();
		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		

	}

	/**
	 * Cargan todas la instituciones y centros de atencion
	 * 
	 * @param forma
	 */
	private void cargarIntitucionesCentroAtencion(
			ConsultaConsolidadoCierresForm forma) {
		UtilidadTransaccion.getTransaccion().begin();

		// se cargan las instituciones activas y listas de centros de atencion
		forma.setListaInstituciones(consultaConsolidadoCierresMundo
				.listarInstituciones());
		forma.setListaCentros(consultaConsolidadoCierresMundo.listarActivos());
		UtilidadTransaccion.getTransaccion().commit();
	}

	/**
	 * Se consultan los datos de cierres por centro de atencion
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void consultarCierresPorCentroAtencion(
			ConsultaConsolidadoCierresForm forma, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping,
			UsuarioBasico usuario) {
		// se realiza la consulta por centro de atencion

		forma.setConsolidadoCierres(consultaReportePorCentrosAtencion(forma,
				response, request, mapping, usuario));
		listarInstitucionesCentrosAtencionJspCentroAtencion(forma);

		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			// se totalizan los valores de la consulta
			forma.setConsolidadoCierres(consultaConsolidadoCierresMundo
					.totalesCierreCentroAtencion(forma.getConsolidadoCierres()));
			UtilidadTransaccion.getTransaccion().commit();
		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
	}

	/**
	 * Se consultan los datos de cierres por caja & cajero
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void consultarCierresPorCajacajero(
			ConsultaConsolidadoCierresForm forma, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping,
			UsuarioBasico usuario) {

		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			// se realiza la consulta por centro de atencion
			forma.setConsolidadoCierresCajaCajero(consultaConsolidadoCierresMundo
					.totalesXCentroAtencionCajaCajero(consultaReportePorcajaCajero(
							forma, response, request, mapping, usuario)));
			listarInstitucionesCentrosAtencionJsp(forma);

			// se totalizan los valores de la consulta
			forma.setConsolidadoCierresCajaCajero(consultaConsolidadoCierresMundo
					.totalesCierreCentroAtencionCajaCajero(forma
							.getConsolidadoCierresCajaCajero()));
			UtilidadTransaccion.getTransaccion().commit();
		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
	}		
	
	/**
	 * generacion de reporte en formato pdf
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void generarReporteCentroAtencionPDF(
			ConsultaConsolidadoCierresForm forma, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping,
			UsuarioBasico usuario) {

		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			Date fechaIngresada = new Date();
			Instituciones institucionUsuario = new Instituciones();
			CentroAtencion centroAtencionUsuario = new CentroAtencion();
			institucionUsuario = obtenerInstitucionAsociadausuario(usuario
					.getCodigoInstitucionInt());
			centroAtencionUsuario = obtenerCentroAtencionUsuario(usuario
					.getCodigoCentroAtencion());
			// obtiene la fecha en tipo Date de la fecha ingresada por el
			// usuario
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				fechaIngresada = df.parse(forma.getFechaInicial());
			} catch (ParseException e1) {
				logger.warn("No se generar la fecha en el formato dado"
						+ e1.toString());
			}

			// carga de datos de encabezado de repeorte
			Map<String, String> parametros = new HashMap<String, String>();
			parametros.put(IConstantesReporte.ubicacionLogo,
					institucionUsuario.getUbicacionLogoReportes());
			parametros.put(IConstantesReporte.nombreInstitucion,
					institucionUsuario.getRazonSocial());
			parametros.put(IConstantesReporte.nitInstitucion,
					ConstantesBD.reporteLabelnit + institucionUsuario.getNit());
			parametros.put(IConstantesReporte.actividadEconomica,
					institucionUsuario.getActividadEco());
			parametros.put(
					IConstantesReporte.direccion,
					ConstantesBD.reporteLabelDireccion
							+ institucionUsuario.getDireccion()
							+ ConstantesBD.telefono
							+ institucionUsuario.getTelefono());
			parametros.put(IConstantesReporte.centroAtencion,
					ConstantesBD.reporteLabelCentroAtencion
							+ centroAtencionUsuario.getDescripcion());
			
			/**
			 * No se esta validando en el encabezado del reporte si la consulta es por centro de atención
			 * o cajas por cajero
			 * Inc 1993
			 * Diana Ruiz
			 */
			
			if (forma.getTipoConsolidadoSeleccionado().equals(ConstantesIntegridadDominio.acronimoConsolidadoCentroAtencion)) {
				parametros.put(IConstantesReporte.tipoConsulta,
						ConstantesBD.reporteLabelTipoConsulta
								+ ConstantesBD.reportePorCentroAtencion);				
			}else{
				parametros.put(IConstantesReporte.tipoConsulta,
						ConstantesBD.reporteLabelTipoConsulta
								+ ConstantesBD.reportePorCajaCajero);
			}							
			
			parametros.put(IConstantesReporte.fecha, forma.getFechaInicial());
			parametros.put(
					IConstantesReporte.usuarioProceso,
					usuario.getNombreUsuario() + " ("
							+ usuario.getLoginUsuario() + ")");
			parametros.put(IConstantesReporte.ubicacionLogo,
					institucionUsuario.getUbicacionLogoReportes());
			parametros.put(IConstantesReporte.rutaLogo,
					institucionUsuario.getLogo());

			// se valida por tipo de reporte de exportar
			if (forma
					.getTipoConsolidadoSeleccionado()
					.equals(ConstantesIntegridadDominio.acronimoConsolidadoCentroAtencion)) {
				try {
					parametros.put(IConstantesReporte.institucionlabel,
							ConstantesBD.labelInstitucion
									+ forma.getConsolidadoCierres().get(0)
											.getIntitucion());
					GeneradorReporteConsultaConsolidadaCierres generador = new GeneradorReporteConsultaConsolidadaCierres();
					JasperReportBuilder reporte = generador
							.buildReportFormatoA(forma.getConsolidadoCierres(),
									parametros, consultaConsolidadoCierresMundo
											.consultarFormaPago());
					GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();
					String nombreReporte = generadorReporteDinamico
							.exportarReportePDF(reporte,
									ConstantesBD.reportePorCentroAtencion);
					forma.setNombreArchivoGenerado(nombreReporte);

				} catch (Exception e) {
					logger.warn("NO se pudo generar el reporte" + e.toString());
				}
			} else {
				try {
					parametros.put(IConstantesReporte.institucionlabel,
							ConstantesBD.labelInstitucion
									+ forma.getConsolidadoCierresCajaCajero()
											.get(0).getIntitucion());
					GeneradorReporteConsultaConsolidadaCierresCajaCajero generador = new GeneradorReporteConsultaConsolidadaCierresCajaCajero();
					JasperReportBuilder reporte = generador
							.buildReportFormatoA(forma
									.getConsolidadoCierresCajaCajero(),
									parametros, consultaConsolidadoCierresMundo
											.consultarFormaPago());
					GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();
					String nombreReporte = generadorReporteDinamico
							.exportarReportePDF(reporte,
									ConstantesBD.reportePorCajaCajero);
					forma.setNombreArchivoGenerado(nombreReporte);

				} catch (Exception e) {
					logger.warn("NO se pudo generar el reporte" + e.toString());
				}

			}

			// se inicializa el formato de salida de los reportes
			forma.setTipoSalida("");
			UtilidadTransaccion.getTransaccion().commit();
		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
	}

	/**
	 * generar reporte en formato de excel
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void generarReporteCentroAtencionExcel(
			ConsultaConsolidadoCierresForm forma, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping,
			UsuarioBasico usuario) {

		UtilidadTransaccion.getTransaccion().begin();
		try {
			Date fechaIngresada = new Date();
			Instituciones institucionUsuario = new Instituciones();
			CentroAtencion centroAtencionUsuario = new CentroAtencion();
			institucionUsuario = obtenerInstitucionAsociadausuario(usuario
					.getCodigoInstitucionInt());
			centroAtencionUsuario = obtenerCentroAtencionUsuario(usuario
					.getCodigoCentroAtencion());
			// obtiene la fecha en tipo Date de la fecha ingresada por el
			// usuario
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				fechaIngresada = df.parse(forma.getFechaInicial());
			} catch (ParseException e1) {
				logger.warn("No se generar la fecha en el formato dado"
						+ e1.toString());
			}

			// carga de datos de encabezado de repeorte
			Map<String, String> parametros = new HashMap<String, String>();
			parametros.put(IConstantesReporte.ubicacionLogo,
					institucionUsuario.getUbicacionLogoReportes());
			parametros.put(IConstantesReporte.nombreInstitucion,
					institucionUsuario.getRazonSocial());
			parametros.put(IConstantesReporte.nitInstitucion,
					ConstantesBD.reporteLabelnit + institucionUsuario.getNit());
			parametros.put(IConstantesReporte.actividadEconomica,
					institucionUsuario.getActividadEco());
			parametros.put(
					IConstantesReporte.direccion,
					ConstantesBD.reporteLabelDireccion
							+ institucionUsuario.getDireccion()
							+ ConstantesBD.telefono
							+ institucionUsuario.getTelefono());
			parametros.put(IConstantesReporte.centroAtencion,
					ConstantesBD.reporteLabelCentroAtencion
							+ centroAtencionUsuario.getDescripcion());
			parametros.put(IConstantesReporte.tipoConsulta,
					ConstantesBD.reporteLabelTipoConsulta
							+ ConstantesBD.reportePorCentroAtencion);
			parametros.put(IConstantesReporte.fecha, forma.getFechaInicial());
			parametros.put(
					IConstantesReporte.usuarioProceso,
					usuario.getNombreUsuario() + " ("
							+ usuario.getLoginUsuario() + ")");
			parametros.put(IConstantesReporte.ubicacionLogo,
					institucionUsuario.getUbicacionLogoReportes());
			parametros.put(IConstantesReporte.rutaLogo,
					institucionUsuario.getLogo());

			// se valida por tipo de reporte de exportar
			if (forma
					.getTipoConsolidadoSeleccionado()
					.equals(ConstantesIntegridadDominio.acronimoConsolidadoCentroAtencion)) {
				try {
					parametros.put(IConstantesReporte.institucionlabel,
							ConstantesBD.labelInstitucion
									+ forma.getConsolidadoCierres().get(0)
											.getIntitucion());
					GeneradorReporteConsultaConsolidadaCierres generador = new GeneradorReporteConsultaConsolidadaCierres();
					JasperReportBuilder reporte = generador
							.buildReportFormatoA(forma.getConsolidadoCierres(),
									parametros, consultaConsolidadoCierresMundo
											.consultarFormaPago());
					GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();
					String nombreReporte = generadorReporteDinamico
							.exportarReporteExcel(reporte,
									ConstantesBD.reportePorCentroAtencion);
					forma.setNombreArchivoGenerado(nombreReporte);

				} catch (Exception e) {
					logger.warn("NO se pudo generar el reporte" + e.toString());
				}
			} else {
				try {
					parametros.put(IConstantesReporte.institucionlabel,
							ConstantesBD.labelInstitucion
									+ forma.getConsolidadoCierresCajaCajero()
											.get(0).getIntitucion());
					GeneradorReporteConsultaConsolidadaCierresCajaCajero generador = new GeneradorReporteConsultaConsolidadaCierresCajaCajero();
					JasperReportBuilder reporte = generador
							.buildReportFormatoA(forma
									.getConsolidadoCierresCajaCajero(),
									parametros, consultaConsolidadoCierresMundo
											.consultarFormaPago());
					GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();
					String nombreReporte = generadorReporteDinamico
							.exportarReporteExcel(reporte,
									ConstantesBD.reportePorCentroAtencion);
					forma.setNombreArchivoGenerado(nombreReporte);

				} catch (Exception e) {
					logger.warn("NO se pudo generar el reporte" + e.toString());
				}

			}
			forma.setTipoSalida("");

		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		UtilidadTransaccion.getTransaccion().commit();
	}

	/**
	 * Exportar reporte en formato plano
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void generarReporteCentroAtencionPlano(
			ConsultaConsolidadoCierresForm forma, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping,
			UsuarioBasico usuario) {

		UtilidadTransaccion.getTransaccion().begin();
		try {
			Date fechaIngresada = new Date();
			Instituciones institucionUsuario = new Instituciones();
			CentroAtencion centroAtencionUsuario = new CentroAtencion();
			institucionUsuario = obtenerInstitucionAsociadausuario(usuario
					.getCodigoInstitucionInt());
			centroAtencionUsuario = obtenerCentroAtencionUsuario(usuario
					.getCodigoCentroAtencion());
			// obtiene la fecha en tipo Date de la fecha ingresada por el
			// usuario
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				fechaIngresada = df.parse(forma.getFechaInicial());
			} catch (ParseException e1) {
				logger.warn("No se generar la fecha en el formato dado"
						+ e1.toString());
			}

			// carga de datos de encabezado de repeorte
			Map<String, String> parametros = new HashMap<String, String>();
			parametros.put(IConstantesReporte.ubicacionLogo,
					institucionUsuario.getUbicacionLogoReportes());
			parametros.put(IConstantesReporte.nombreInstitucion,
					institucionUsuario.getRazonSocial());
			parametros.put(IConstantesReporte.nitInstitucion,
					ConstantesBD.reporteLabelnit + institucionUsuario.getNit());
			parametros.put(IConstantesReporte.actividadEconomica,
					institucionUsuario.getActividadEco());
			parametros.put(
					IConstantesReporte.direccion,
					ConstantesBD.reporteLabelDireccion
							+ institucionUsuario.getDireccion()
							+ ConstantesBD.telefono
							+ institucionUsuario.getTelefono());
			parametros.put(IConstantesReporte.centroAtencion,
					ConstantesBD.reporteLabelCentroAtencion
							+ centroAtencionUsuario.getDescripcion());
			parametros.put(IConstantesReporte.tipoConsulta,
					ConstantesBD.reporteLabelTipoConsulta
							+ ConstantesBD.reportePorCentroAtencion);
			parametros.put(IConstantesReporte.fecha, forma.getFechaInicial());
			parametros.put(
					IConstantesReporte.usuarioProceso,
					usuario.getNombreUsuario() + " ("
							+ usuario.getLoginUsuario() + ")");
			parametros.put(IConstantesReporte.ubicacionLogo,
					institucionUsuario.getUbicacionLogoReportes());
			parametros.put(IConstantesReporte.rutaLogo,
					institucionUsuario.getLogo());

			// se valida por tipo de reporte de exportar
			if (forma
					.getTipoConsolidadoSeleccionado()
					.equals(ConstantesIntegridadDominio.acronimoConsolidadoCentroAtencion)) {
				try {
					parametros.put(IConstantesReporte.institucionlabel,
							ConstantesBD.labelInstitucion
									+ forma.getConsolidadoCierres().get(0)
											.getIntitucion());
					GeneradorReporteConsultaConsolidadaCierres generador = new GeneradorReporteConsultaConsolidadaCierres();
					JasperReportBuilder reporte = generador
							.buildReportPlanoFormatoA(forma
									.getConsolidadoCierres(), parametros,
									consultaConsolidadoCierresMundo
											.consultarFormaPago());
					GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();
					String nombreReporte = generadorReporteDinamico
							.exportarReporteArchivoPlano(reporte,
									ConstantesBD.reportePorCentroAtencion);
					forma.setNombreArchivoGenerado(nombreReporte);

				} catch (Exception e) {
					logger.warn("NO se pudo generar el reporte" + e.toString());
				}

			} else {
				try {
					parametros.put(IConstantesReporte.institucionlabel,
							ConstantesBD.labelInstitucion
									+ forma.getConsolidadoCierresCajaCajero()
											.get(0).getIntitucion());
					GeneradorReporteConsultaConsolidadaCierresCajaCajero generador = new GeneradorReporteConsultaConsolidadaCierresCajaCajero();
					JasperReportBuilder reporte = generador
							.buildReportPlanoFormatoA(forma
									.getConsolidadoCierresCajaCajero(),
									parametros, consultaConsolidadoCierresMundo
											.consultarFormaPago());
					GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();
					String nombreReporte = generadorReporteDinamico
							.exportarReporteArchivoPlano(reporte,
									ConstantesBD.reportePorCentroAtencion);
					forma.setNombreArchivoGenerado(nombreReporte);

				} catch (Exception e) {
					logger.warn("NO se pudo generar el reporte" + e.toString());
				}
			}

			forma.setTipoSalida("");

		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		UtilidadTransaccion.getTransaccion().commit();
	}

	/**
	 * Se valida el tipo de reporte a exportar
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void tipoDeReporteAExportar(ConsultaConsolidadoCierresForm forma,
			HttpServletResponse response, HttpServletRequest request,
			ActionMapping mapping, UsuarioBasico usuario) {

		// segun el tipo de formato seleccionado de exporta
		if (forma.getTipoSalida().equals(ConstantesBD.reportePdf)) {
			generarReporteCentroAtencionPDF(forma, response, request, mapping,
					usuario);
		} else if (forma.getTipoSalida().equals(ConstantesBD.reportePlano)) {
			generarReporteCentroAtencionPlano(forma, response, request,
					mapping, usuario);
		} else if (forma.getTipoSalida().equals(ConstantesBD.reporteExcel)) {
			generarReporteCentroAtencionExcel(forma, response, request,
					mapping, usuario);
		}

	}

	/**
	 * @param codigo
	 * @return Institucion
	 */
	public Instituciones obtenerInstitucionAsociadausuario(Integer codigo) {
		return consultaConsolidadoCierresMundo
				.consultarIntitucionusuario(codigo);
	}

	/**
	 * @param codigo
	 * @return CentroAtencion
	 */
	public CentroAtencion obtenerCentroAtencionUsuario(Integer codigo) {
		return consultaConsolidadoCierresMundo
				.obtenerCentroAtencionUsuario(codigo);
	}

	/**
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 * @return lista con los cierres consoldiados
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaReportePorCentrosAtencion(
			ConsultaConsolidadoCierresForm forma, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping,
			UsuarioBasico usuario) {
		ArrayList<DtoConsolidadoCierreReporte> cierres = new ArrayList<DtoConsolidadoCierreReporte>();
		UtilidadTransaccion.getTransaccion().begin();
		try {
			// se realiza la consulta de cierre
			cierres = consultaConsolidadoCierresMundo
					.consultarconsolidadoCierre(forma.getFechaInicial(),
							forma.getCentroAtencionSeleccionado(),
							forma.getInstitucionSeleccionada());

		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		UtilidadTransaccion.getTransaccion().commit();
		return cierres;
	}

	/**
	 * 
	 * 
	 * Carga de valores iniciales
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void valoreiniciales(ConsultaConsolidadoCierresForm forma,
			HttpServletResponse response, HttpServletRequest request,
			ActionMapping mapping, UsuarioBasico usuario) {

		UtilidadTransaccion.getTransaccion().begin();
		try {
			// valores inciales de formulario y campos
			forma.setNombreArchivoGenerado(null);
			listaTiposConsolidados(forma, response, request, mapping);
			cargarListaEmpresa(forma, response, request, mapping, usuario);
			cargarListaCentrosAtencion(forma);
			forma.setFechaInicial("");
			forma.setConsolidadoCierres(new ArrayList<DtoConsolidadoCierreReporte>());
			forma.setConsolidadoCierresCajaCajero(new ArrayList<DtoConsolidadoCierreReporte>());
			forma.setTipoConsolidadoSeleccionado("");
			forma.setInstitucionSeleccionada("");
			forma.setCentroAtencionSeleccionado("");
			forma.setTipoSalida("");
			cargarIntitucionesCentroAtencion(forma);

		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		UtilidadTransaccion.getTransaccion().commit();
	}

	/**
	 * Se Listan los tipos de consolidados parametrizados en el sistema
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 */
	public void listaTiposConsolidados(ConsultaConsolidadoCierresForm forma,
			HttpServletResponse response, HttpServletRequest request,
			ActionMapping mapping) {
		// SOLO SE USAN DOS TIPO DE RPEORTES QUE ESTAN PARAMETRIZADOS EN LOS
		// REPORTES
		String[] listadoReportes = new String[] {
				ConstantesIntegridadDominio.acronimoConsolidadoCentroAtencion,
				ConstantesIntegridadDominio.acronimoConsolidadoCajaCajero };

		Connection con = UtilidadBD.abrirConexion();

		// SE CREA EL ARREGLO CON LOS VALORES
		ArrayList<DtoIntegridadDominio> listadoComboTiposConsolidados = Utilidades
				.generarListadoConstantesIntegridadDominio(con,
						listadoReportes, false);

		UtilidadBD.closeConnection(con);

		forma.setListaComboTipoConsolidado(listadoComboTiposConsolidados);
	}

	/**
	 * Listas de empresas para combo
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 */
	public void cargarListaEmpresa(ConsultaConsolidadoCierresForm forma,
			HttpServletResponse response, HttpServletRequest request,
			ActionMapping mapping, UsuarioBasico usuario) {
		UtilidadTransaccion.getTransaccion().begin();
		;

		try {
			forma.setEsMultiempresa(UtilidadTexto.getBoolean(ValoresPorDefecto
					.getInstitucionMultiempresa(usuario
							.getCodigoInstitucionInt())));
			if (forma.getEsMultiempresa()) {
				forma.setListaComboInsitucion(consultaConsolidadoCierresMundo
						.consultarEmpresaInstitucionXinstitucion(usuario
								.getCodigoInstitucionInt()));
			
			forma.setListaInstituciones(consultaConsolidadoCierresMundo
					.listarInstituciones());
			
			
			}

		}// CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		UtilidadTransaccion.getTransaccion().commit();
	}

	/**
	 * Listas de centros de atencion
	 * 
	 * @param forma
	 */
	public void cargarListaCentrosAtencion(ConsultaConsolidadoCierresForm forma) {
		UtilidadTransaccion.getTransaccion().begin();
		;
		try {
			forma.setListaCentrosAtencion(consultaConsolidadoCierresMundo
					.consultarTodosCentrosAtencion());
		}// CONTROL DE ERRORES
		catch (Exception e) {

			UtilidadTransaccion.getTransaccion().rollback();
		}

		UtilidadTransaccion.getTransaccion().commit();

	}

	/**
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @param usuario
	 * @return lista con los cierres consoldiados
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaReportePorcajaCajero(
			ConsultaConsolidadoCierresForm forma, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping,
			UsuarioBasico usuario) {
		ArrayList<DtoConsolidadoCierreReporte> cierres = new ArrayList<DtoConsolidadoCierreReporte>();
		// se consultan los cierres por caja / cajero
		cierres = consultaConsolidadoCierresMundo
				.consultarconsolidadoCierreCajaCajero(forma.getFechaInicial(),
						forma.getCentroAtencionSeleccionado(),
						forma.getInstitucionSeleccionada());

		// se organiza la lista de forma desendente por hora de apertura
		SortGenerico sortG = new SortGenerico(
				ConstantesBD.ordenamientoPorHoraAperura, false);
		Collections.sort(cierres, sortG);

		return cierres;
	}

}
