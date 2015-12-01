/*
 * Creado el 17-may-2004
 *
 * AXIOMA
 *
 * Autor: Juan David Ramírez
 * juan@princetonSA.com
 * 
 * logsAxioma.java
 */
package util;

import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;

/**
 * @author Juan David Ramírez
 * 
 * juan@princetonSA.com
 */
public class LogsAxioma {

	/**
	 * Manejo de errores de la utilidad de logs
	 */
	private static Logger logger = Logger.getLogger(LogsAxioma.class);

	/**
	 * Información que se desea enviar al archivo de log
	 */
	private static String log = "";

	/**
	 * Nombre del archivo que se está utilizando
	 */
	private static String archivo = "";

	/**
	 * Definicion del tipo de log (Inserción, modificación ó eliminación)
	 */
	private static int tipoLog = 0;

	/**
	 * Ruta de los logs definida en ../WEB-INF/web.xml
	 */
	private static String rutaLogs = "";

	/**
	 * Folder en el cual se hizo la inserción del log
	 */
	private static String folderLog = "";

	/**
	 * Clase para manejar logs específicos de la aplicación como contratos,
	 * empresas, etc.
	 * 
	 * @param constanteArchivo
	 *            archivo al cual se le va añadir el log (Contrato, Empresa,
	 *            ...) Este se toma de constatesBD
	 * @param texto
	 *            Cadena que se imprime en el archivo
	 * @param tipo
	 *            Utilizado para definir el tipo de acción (Insertar, Modificar,
	 *            Eliminar)
	 * @return true si fue correcta la inserción en el archivo de Log false si
	 *         no se insertó nada en el archivo de Log
	 */
	public static boolean enviarLog(int constanteArchivo, String texto,	int tipo, String Usuario) 
	{
	
		String separador = System.getProperty("file.separator");
		if (!rutaLogs.equals("") && rutaLogs != null) {
			String textoTipo = "";
			logger.info("/////////////////////////////////////");
			logger.info(" \n\n\n\n\n\n\n\n");
			logger.info("ConstanteArchivo ----------> "+ constanteArchivo);
			logger.info("\n\n\n\n\n\n");
			
			switch (constanteArchivo) {
			case 1:
				archivo = ConstantesBD.logContratoNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logContratoNombre;
				break;
			case 2:
				archivo = ConstantesBD.logEmpresaNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logEmpresaNombre;
				break;
			case 3:
				archivo = ConstantesBD.logConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logConvenioNombre;
				break;
			case 4:
				archivo = ConstantesBD.logAsocioCuentasNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador + ConstantesBD.logAsocioCuentasNombre;
				break;
			case 5:
				archivo = ConstantesBD.logTarifasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logTarifasNombre;
				break;
			case ConstantesBD.logExcepcionesTarifasCodigo:
				archivo = ConstantesBD.logExcepcionesTarifasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador + ConstantesBD.logFolderMantenimientoTablas + separador + ConstantesBD.logExcepcionesTarifasNombre;
				break;
			case 7:
				archivo = ConstantesBD.logRecargosTarifasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logRecargosTarifasNombre;
				break;
			case 8:
				archivo = ConstantesBD.logTerceroNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logTerceroNombre;
				break;
			case 9:
				archivo = ConstantesBD.logClasificacionSocioEconomicaNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logClasificacionSocioEconomicaNombre;
				break;
			case 10:
				archivo = ConstantesBD.logTopesFacturacionNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logTopesFacturacionNombre;
				break;
			case 11:
				archivo = ConstantesBD.logMontosCobroNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logMontosCobroNombre;
				break;
			case 12:
				archivo = ConstantesBD.logExcepcionesServiciosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logExcepcionesServiciosNombre;
				break;
			case 13:
				archivo = ConstantesBD.logPagosPacienteNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logPagosPacienteNombre;
				break;
			case 14:
				archivo = ConstantesBD.logCIENombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador + ConstantesBD.logCIENombre;
				break;
			case 15:
				archivo = ConstantesBD.logRegistroDiagnosticoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador + ConstantesBD.logRegistroDiagnosticoNombre;
				break;
			case 16:
				archivo = ConstantesBD.logModificacionUnidadNombre;
				folderLog = ConstantesBD.logFolderModuloConsultaExterna
						+ separador + ConstantesBD.logModificacionUnidadNombre;
				break;
			case 17:
				archivo = ConstantesBD.logRegistroExcepcionesNaturalezaPacienteNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador
						+ ConstantesBD.logRegistroExcepcionesNaturalezaPacienteNombre;
				break;
			case 18:
				archivo = ConstantesBD.logSustitutosInventarioNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
				 		+ separador
						+ ConstantesBD.logFolderMantenimientoInventario 
						+ separador
						+ ConstantesBD.logSustitutosInventarioNombre;
				break;
			case 19:
				archivo = ConstantesBD.logTarifasInventarioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logTarifasInventarioNombre;
				break;
			case ConstantesBD.logPedidosInsumos:
				archivo = ConstantesBD.logPedidosInsumosNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderPedidos + separador
						+ ConstantesBD.logPedidosInsumosNombre;
				break;
			case ConstantesBD.logEsquemasTarifariosCodigo:
				archivo = ConstantesBD.logEsquemasTarifariosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logEsquemasTarifariosNombre;
				break;
			case ConstantesBD.logRequisitosPacienteCodigo:
				archivo = ConstantesBD.logRequisitosPacienteNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logRequisitosPacienteNombre;
				break;
			case ConstantesBD.logRequisitosPacienteConvenioCodigo:
				archivo = ConstantesBD.logRequisitosPacienteConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logRequisitosPacienteConvenioNombre;

				break;
			case ConstantesBD.logParamInstitucionCodigo:
				archivo = ConstantesBD.logParamInstitucionNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logParamInstitucionNombre;
				break;
			case ConstantesBD.logExcepcionesFarmaciaConvenioCodigo:
				archivo = ConstantesBD.logExcepcionesFarmaciaConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logExcepcionesFarmaciaConvenioNombre;
				break;
			case ConstantesBD.logParticipacionPoolXTarifasCodigo:
				archivo = ConstantesBD.logParticipacionPoolXTarifasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logParticipacionPoolXTarifasNombre;
				break;
			case ConstantesBD.logMedicosXPoolCodigo:
				archivo = ConstantesBD.logMedicosXPoolNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logMedicosXPoolNombre;
				break;
			case ConstantesBD.logDistribucionCuentaCodigo:
				archivo = ConstantesBD.logDistribucionCuentaNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderModuloDistribucionCuenta
						+ separador + archivo;
				break;
			case ConstantesBD.logGeneracionExcepcionesFarmaciaCodigo:
				archivo = ConstantesBD.logGeneracionExcepcionesFarmaciaNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logGeneracionExcepcionesFarmaciaNombre;
				break;
			case ConstantesBD.logCoberturaAccidentesTransitoCodigo:
				archivo = ConstantesBD.logCoberturaAccidentesTransitoNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderCoberturaAccidentes;
				break;
			case ConstantesBD.logRegistroPoolesCodigo:
				archivo = ConstantesBD.logRegistroPoolesNombre;
				folderLog = ConstantesBD.logFolderModuloPooles + separador
						+ ConstantesBD.logRegistroPoolesNombre;
				break;
			case ConstantesBD.logSolicitudMedimcanetosCodigo:
				archivo = ConstantesBD.logSolicitudMedimcanetosNombre;
				folderLog = ConstantesBD.logFolderModuloInsumosMedicamentos
						+ separador
						+ ConstantesBD.logSolicitudMedimcanetosNombre;
				break;
			case ConstantesBD.logAbonosYDescuentosCodigo:
				archivo = ConstantesBD.logAbonosYDescuentosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logAbonosYDescuentosNombre;
				break;
			case ConstantesBD.logRevisionCuentaRequisitosCodigo:
				archivo = ConstantesBD.logRevisionCuentaRequisitosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logRevisionCuentaNombre;
				break;
			case ConstantesBD.logRevisionCuentaPoolCodigo:
				archivo = ConstantesBD.logRevisionCuentaPoolNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logRevisionCuentaNombre;
				break;
			case ConstantesBD.logNumeroAutorizacionCodigo:
				archivo = ConstantesBD.logNumeroAutorizacionNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logNumeroAutorizacionNombre;
				break;
			case ConstantesBD.logComentarioProcedimientoCodigo:
				archivo = ConstantesBD.logComentarioProcedimientoNombre;
				folderLog = ConstantesBD.logFolderModuloOrdenesMedicas
						+ separador
						+ ConstantesBD.logComentarioProcedimientoNombre;
				break;
			case ConstantesBD.logParametrosGeneralesCodigo:
				archivo = ConstantesBD.logParametrosGeneralesNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador + ConstantesBD.logParametrosGeneralesNombre;
				break;
			case ConstantesBD.logSalarioMinimoCodigo:
				archivo = ConstantesBD.logSalarioMinimoNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logSalarioMinimoNombre;
				break;
			case ConstantesBD.logModificarCuentaCodigo:
				archivo = ConstantesBD.logModificarCuentaNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador + ConstantesBD.logModificarCuentaNombre;
				break;
			case ConstantesBD.logArticuloCodigo:
				archivo = ConstantesBD.logArticuloNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador 
						+ ConstantesBD.logFolderMantenimientoInventario
						+ separador + ConstantesBD.logArticuloNombre;
				break;
			case ConstantesBD.logCuentasCobroCodigo:
				archivo = ConstantesBD.logCuentasCobroNombre;
				folderLog = ConstantesBD.logFolderCartera + separador
						+ ConstantesBD.logCuentasCobroNombre;
				break;
			case ConstantesBD.logCategoriaCodigo:
				archivo = ConstantesBD.logCategoriaNombre;
				folderLog = ConstantesBD.logFolderModuloCuadroTurnos
						+ separador + ConstantesBD.logCategoriaNombre;
				break;
			case ConstantesBD.logNovedadCodigo:
				archivo = ConstantesBD.logNovedadNombre;
				folderLog = ConstantesBD.logFolderModuloCuadroTurnos
						+ separador + ConstantesBD.logNovedadNombre;
				break;
			case ConstantesBD.logMotivoAnulacionFacturasCodigo:
				archivo = ConstantesBD.logMotivoAnulacionFacturasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logMotivoAnulacionFacturasNombre;
				break;
			case ConstantesBD.logServicioCodigo:
				archivo = ConstantesBD.logServicioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logServicioNombre;
				break;
			case ConstantesBD.logRequisitosRadicacionCodigo:
				archivo = ConstantesBD.logRequisitosRadicacionNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logRequisitosRadicacionNombre;
				break;
			case ConstantesBD.logVacacionesCodigo:
				archivo = ConstantesBD.logVacacionesNombre;
				folderLog = ConstantesBD.logFolderModuloCuadroTurnos
						+ separador + ConstantesBD.logVacacionesNombre;
				break;
			case ConstantesBD.logRequisitosRadicacionConvenioCodigo:
				archivo = ConstantesBD.logRequisitosRadicacionConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logRequisitosRadicacionConvenioNombre;
				break;
			case ConstantesBD.logConceptosPagoCarteraCodigo:
				archivo = ConstantesBD.logConceptosPagoCarteraNombre;
				folderLog = ConstantesBD.logFolderCartera + separador
						+ ConstantesBD.logConceptosPagoCarteraNombre;
				break;
			case ConstantesBD.logConceptosAjustesCarteraCodigo:
				archivo = ConstantesBD.logConceptosAjustesCarteraNombre;
				folderLog = ConstantesBD.logFolderCartera + separador
						+ ConstantesBD.logConceptosAjustesCarteraNombre;
				break;
			case ConstantesBD.logActualizacionAutorizacionCodigo:
				archivo = ConstantesBD.logActualizacionAutorizacionNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador
						+ ConstantesBD.logActualizacionAutorizacionNombre;
				break;
			case ConstantesBD.logConceptosGlosasCodigo:
				archivo = ConstantesBD.logConceptosGlosasNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas + separador + ConstantesBD.logFolderMantenimiento+ separador+  archivo;
				break;
			case ConstantesBD.logConceptosRespuestasGlosasCodigo:
				archivo = ConstantesBD.logConceptosRespuestasGlosasNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas + separador + ConstantesBD.logFolderMantenimiento+ separador+  archivo;
				break;
			case ConstantesBD.logNovedadEnfermeraCodigo:
				archivo = ConstantesBD.logNovedadEnfermeraNombre;
				folderLog = ConstantesBD.logFolderModuloCuadroTurnos
						+ separador + ConstantesBD.logNovedadEnfermeraNombre;
				break;
			case ConstantesBD.logEnfermeraCategoriaCodigo:
				archivo = ConstantesBD.logEnfermeraCategoriaNombre;
				folderLog = ConstantesBD.logFolderModuloCuadroTurnos
						+ separador + ConstantesBD.logEnfermeraCategoriaNombre;
				break;
			case ConstantesBD.logRipsCodigo:
				archivo = ConstantesBD.logRipsNombre;
				folderLog = ConstantesBD.logFolderRIPS + separador
						+ ConstantesBD.logRipsNombre;
				break;
			case ConstantesBD.logHojaObstetricaCodigo:
				archivo = ConstantesBD.logHojaObstetricaNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica
						+ separador + ConstantesBD.logHojaObstetricaNombre;
				break;
			case ConstantesBD.logRegistroSaldoInicialCodigo:
				archivo = ConstantesBD.logRegistroSaldoInicialNombre;
				folderLog = ConstantesBD.logFolderCartera + separador
						+ ConstantesBD.logRegistroSaldoInicialNombre;
				break;
			case ConstantesBD.logConsecutivosDisponiblesCodigo:
				archivo = ConstantesBD.logConsecutivosDisponiblesNombre;
				folderLog = ConstantesBD.logFolderAdministracion + separador
						+ ConstantesBD.logConsecutivosDisponiblesNombre;
				break;

			case ConstantesBD.logCierreCarteraCodigo:
				archivo = ConstantesBD.logCierreCarteraNombre;
				folderLog = ConstantesBD.logFolderCartera + separador
						+ ConstantesBD.logCierreCarteraNombre;
				break;
			case ConstantesBD.logTiposMonitoreoCodigo:
				archivo = ConstantesBD.logTiposMonitoreoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador + ConstantesBD.logTiposMonitoreoNombre;
				break;
			case ConstantesBD.logTipoSalasCodigo:
				archivo = ConstantesBD.logTipoSalasNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logTipoSalasNombre;
				break;
			case ConstantesBD.logSalasCodigo:
				archivo = ConstantesBD.logSalasNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logSalasNombre;
				break;
			case ConstantesBD.logGruposCodigo:
				archivo = ConstantesBD.logGruposNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logGruposNombre;
				break;
			case ConstantesBD.logPorcentajesCxMultiplesCodigo:
				archivo = ConstantesBD.logPorcentajesCxMultiplesNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador
						+ ConstantesBD.logPorcentajesCxMultiplesNombre;
				break;
			case ConstantesBD.logAsociosServiciosTarifasCodigo:
				archivo = ConstantesBD.logAsociosServiciosTarifasNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador
						+ ConstantesBD.logAsociosServiciosTarifasNombre;
				break;
			case ConstantesBD.logCajasCodigo:
				archivo = ConstantesBD.logCajasNombre;
				folderLog = ConstantesBD.logFolderModuloTesoreria + separador
						+ ConstantesBD.logFolderMantenimiento + separador
						+ ConstantesBD.logCajasNombre;
				break;
			case ConstantesBD.logCajasCajerosCodigo:
				archivo = ConstantesBD.logCajasCajerosNombre;
				folderLog = ConstantesBD.logFolderModuloTesoreria + separador
						+ ConstantesBD.logFolderMantenimiento + separador
						+ ConstantesBD.logCajasCajerosNombre;
				break;
			case ConstantesBD.logFormasPagoCodigo:
				archivo = ConstantesBD.logFormasPagoNombre;
				folderLog = ConstantesBD.logFolderModuloTesoreria + separador
						+ ConstantesBD.logFolderMantenimiento + separador
						+ ConstantesBD.logFormasPagoNombre;
				break;
			case ConstantesBD.logEntidadesFinancierasCodigo:
				archivo = ConstantesBD.logEntidadesFinancierasNombre;
				folderLog = ConstantesBD.logFolderModuloTesoreria + separador
						+ ConstantesBD.logFolderMantenimiento + separador
						+ ConstantesBD.logEntidadesFinancierasNombre;
				break;
			case ConstantesBD.logExcepcionAsocioTipoSalaCodigo:
				archivo = ConstantesBD.logExcepcionAsocioTipoSalaNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador
						+ ConstantesBD.logExcepcionAsocioTipoSalaNombre;
				break;
			case ConstantesBD.logAsociosTipoServicioCodigo:
				archivo = ConstantesBD.logAsociosTipoServicioNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logAsociosTipoServicioNombre;
				break;
			case ConstantesBD.logTarjetasFinancierasCodigo:
				archivo = ConstantesBD.logTarjetasFinancierasNombre;
				folderLog = ConstantesBD.logFolderModuloTesoreria + separador
						+ ConstantesBD.logFolderMantenimiento + separador
						+ ConstantesBD.logTarjetasFinancierasNombre;
				break;
			case ConstantesBD.logTesoreriaCodigo:
				archivo = ConstantesBD.logTesoreriaNombre;
				folderLog = ConstantesBD.logFolderModuloTesoreria + separador
						+ ConstantesBD.logFolderMantenimiento + separador
						+ ConstantesBD.logTesoreriaNombre;
				break;
			case ConstantesBD.logConceptoCastigoCodigo:
				archivo = ConstantesBD.logConceptoCastigoNombre;
				folderLog = ConstantesBD.logFolderCartera + separador
						+ ConstantesBD.logFolderMantenimiento + separador
						+ ConstantesBD.logConceptoCastigoNombre;
				break;
			case ConstantesBD.logAsociosXUvrCodigo:
				archivo = ConstantesBD.logAsociosXUvrNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logAsociosXUvrNombre;
				break;
			case ConstantesBD.logExcepcionesQXCodigo:
				archivo = ConstantesBD.logExcepcionesQXNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logExcepcionesQXNombre;
				break;
			case ConstantesBD.logIngresoConsumoMaterialesQxCodigo:
				archivo = ConstantesBD.logIngresoConsumoMaterialesQxNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderLiquidacion
						+ separador
						+ ConstantesBD.logIngresoConsumoMaterialesQxNombre;
				break;
			case ConstantesBD.logModificarPeticionQXCodigo:
				archivo = ConstantesBD.logModificarPeticionQXNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderPeticiones
						+ separador + ConstantesBD.logModificarPeticionQXNombre;
				break;
			case ConstantesBD.logTiposTransaccionesInvCodigo:
				archivo = ConstantesBD.logTiposTransaccionesInvNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderMantenimientoInventario
						+ separador
						+ ConstantesBD.logTiposTransaccionesInvNombre;
				break;
			case ConstantesBD.logTransaccionesValidasXCCInvCodigo:
				archivo = ConstantesBD.logTransaccionesValidasXCCInvNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderMantenimientoInventario
						+ separador
						+ ConstantesBD.logTransaccionesValidasXCCInvNombre;
				break;
			case ConstantesBD.logMotivoDevolucionInvCodigo:
				archivo = ConstantesBD.logMotivoDevolucionInvNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderMantenimientoInventario
						+ separador + ConstantesBD.logMotivoDevolucionInvNombre;
				break;
			case ConstantesBD.logUsuariosXAlmacenInvCodigo:
				archivo = ConstantesBD.logUsuariosXAlmacenInvNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderMantenimientoInventario
						+ separador + ConstantesBD.logUsuariosXAlmacenInvNombre;
				break;
			case ConstantesBD.logReversionLiquidacionQxCodigo:
				archivo = ConstantesBD.logReversionLiquidacionQxNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderLiquidacion
						+ separador
						+ ConstantesBD.logReversionLiquidacionQxNombre;
				break;
			case ConstantesBD.logSolicitudesCxCodigo:
				archivo = ConstantesBD.logSolicitudesCxNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderSolicitudesCx
						+ separador + ConstantesBD.logSolicitudesCxNombre;
				break;
			case ConstantesBD.logNominaCodigo:
				archivo = ConstantesBD.logNominaNombre;
				folderLog = ConstantesBD.logFolderModuloCuadroTurnos
						+ separador + ConstantesBD.logNominaNombre;
				break;
			case ConstantesBD.logRegistroTransaccionesInvCodigo:
				archivo = ConstantesBD.logRegistroTransaccionesInvNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderTransaccionesInventario
						+ separador
						+ ConstantesBD.logRegistroTransaccionesInvNombre;
				break;
			case ConstantesBD.logInformacionPolizaCodigo:
				archivo = ConstantesBD.logInformacionPolizaNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador + ConstantesBD.logFolderInformacionPoliza
						+ separador + ConstantesBD.logInformacionPolizaNombre;
				break;
			case ConstantesBD.logCierreInventariosCodigo:
				archivo = ConstantesBD.logCierreInventariosNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderCierreInventario + separador
						+ ConstantesBD.logCierreInventariosNombre;
				break;
			case ConstantesBD.logFormatoImpresionPresupuestoCodigo:
				archivo = ConstantesBD.logFormatoImpresionPresupuestoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador
						+ ConstantesBD.logFolderMantenimientoManejoPaciente
						+ separador
						+ ConstantesBD.logFormatoImpresionPresupuestoNombre;
				break;
			case ConstantesBD.logGruposServiciosCodigo:
				archivo = ConstantesBD.logGruposServiciosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderMantenimientoTablas + separador
						+ ConstantesBD.logGruposServiciosNombre;
				break;
			case ConstantesBD.logSolicitudTrasladoAlmacenCodigo:
				archivo = ConstantesBD.logSolicitudTrasladoAlmacenNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador
						+ ConstantesBD.logFolderTrasladosInventario + separador
						+ ConstantesBD.logSolicitudTrasladoAlmacenNombre;
				break;
			case ConstantesBD.logCargosDirectosServiciosCodigo:
				archivo = ConstantesBD.logCargosDirectosServiciosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderCargosDirectos + separador
						+ ConstantesBD.logCargosDirectosServiciosNombre;
				break;
			case ConstantesBD.logCargosDirectosArticulosCodigo:
				archivo = ConstantesBD.logCargosDirectosArticulosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion + separador
						+ ConstantesBD.logFolderCargosDirectos + separador
						+ ConstantesBD.logCargosDirectosArticulosNombre;
				break;
			case ConstantesBD.logRegEnferMedDietaElimCodigo:
				archivo = ConstantesBD.logRegEnferMedDietaElimNombre;
				folderLog = ConstantesBD.logFolderModuloEnfermeria + separador
						+ ConstantesBD.logFolderRegistroEnfermeria;
				break;
			case ConstantesBD.logExcepcionTarifasQuirurgicasCodigo:
				archivo = ConstantesBD.logExcepcionTarifasQuirurgicasNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador
						+ ConstantesBD.logExcepcionTarifasQuirurgicasNombre;
				break;
			case ConstantesBD.logRegEnferMedDietaModCodigo:
				archivo = ConstantesBD.logRegEnferMedDietaModNombre;
				folderLog = ConstantesBD.logFolderModuloEnfermeria + separador
						+ ConstantesBD.logFolderRegistroEnfermeria;
				break;
			case ConstantesBD.logInterfazFactServicioGrpModCodigo:
				archivo = ConstantesBD.logInterfazFactServicioGrpMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactServicioTipoModCodigo:
				archivo = ConstantesBD.logInterfazFactServicioTipoMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactInventarioClaseInvModCodigo:
				archivo = ConstantesBD.logInterfazFactInventarioClaseInvMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactInventarioGrupoInvModCodigo:
				archivo = ConstantesBD.logInterfazFactInventarioGrupoInvMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactInventarioSubGrupoInvModCodigo:
				archivo = ConstantesBD.logInterfazFactInventarioSubGrupoInvMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactInventarioArticuloInvModCodigo:
				archivo = ConstantesBD.logInterfazFactInventarioArticuloInvMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactServicioEspModCodigo:
				archivo = ConstantesBD.logInterfazFactServicioEspMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactServicioSerModCodigo:
				archivo = ConstantesBD.logInterfazFactServicioSerMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactCuentasConveniosModCodigo:
				archivo = ConstantesBD.logInterfazFactCuentasConveniosMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logInterfazFactParamInfoGralCamposInterfazModCodigo:
				archivo = ConstantesBD.logInterfazFactParamInfoGralCamposInterfazMod;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logUnidadesFuncionalesCodigo:
				archivo = ConstantesBD.logUnidadesFuncionalesNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
						+ separador
						+ ConstantesBD.logFolderMantenimientoManejoPaciente
						+ separador + ConstantesBD.logUnidadesFuncionalesNombre;
				break;
			case ConstantesBD.logHojaQuirugicaModCodigo:
				archivo = ConstantesBD.logHojaQuirugicaMod;
				folderLog = ConstantesBD.logFolderModuloOrdenesMedicas
						+ separador + ConstantesBD.logFolderResponderCirugias;
				break;
			case ConstantesBD.logReasignarProfesionalCodigo:
				archivo = ConstantesBD.logReasignarProfesionalNombre;
				folderLog =  ConstantesBD.logFolderModuloConsultaExterna
					+ separador + ConstantesBD.logReasignarProfesionalNombre;
				break;
			case ConstantesBD.logCentrosCostoCodigo:
				archivo = ConstantesBD.logCentrosCostoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
							+ separador
							+ ConstantesBD.logFolderMantenimientoManejoPaciente
							+ separador
							+ ConstantesBD.logCentrosCostoNombre;
				break;
			case ConstantesBD.logCentrosAtencionCodigo:
				archivo = ConstantesBD.logCentrosAtencionNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
							+ separador
							+ ConstantesBD.logFolderMantenimientoManejoPaciente
							+ separador
							+ ConstantesBD.logCentrosAtencionNombre;
				break;
			case ConstantesBD.logCentroCostoXgrupoServicioElimCodigo:
				archivo = ConstantesBD.logCentroCostoXgrupoServicioElimNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
							+ separador
							+ ConstantesBD.logFolderMantenimiento
							+ separador
							+ ConstantesBD.logCentroCostoXgrupoServicioElimNombre;
				break;
			case ConstantesBD.logCentroCostoXUnidadesConsultaCodigo:
				archivo = ConstantesBD.logCentroCostoXUnidadesConsultaNombre;
				folderLog = ConstantesBD.logFolderModuloConsultaExterna
							+ separador
							+ ConstantesBD.logFolderMantenimiento
							+ separador
							+ ConstantesBD.logCentroCostoXUnidadesConsultaNombre;
				break;
			case ConstantesBD.logInterfazCuentaInterfazUnidadFuncionalCodigo:
				archivo = ConstantesBD.logInterfazCuentaInterfazUnidadFuncional;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logCentrosCostoXViaIngresoCodigo:
				archivo = ConstantesBD.logCentrosCostoXViaIngresoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
							+ separador
							+ ConstantesBD.logFolderMantenimientoManejoPaciente
							+ separador
							+ ConstantesBD.logCentrosCostoXViaIngresoNombre;
				break;
			case ConstantesBD.logInterfazCuentaInterfazUnidadFuncionalCCCodigo:
				archivo = ConstantesBD.logInterfazCuentaInterfazUnidadFuncionalCC;
				folderLog = ConstantesBD.logFolderModuloInterfaz + separador
						+ ConstantesBD.logFolderInterfazFacturacion;
				break;
			case ConstantesBD.logGruposEtareosCodigo:
				archivo = ConstantesBD.logGruposEtareosNombre;
				folderLog = ConstantesBD.logFolderModuloCapitacion + separador
						+ ConstantesBD.logFolderMantenimientoCapitacion + separador
						+ ConstantesBD.logGruposEtareosNombre;
				break;
			case ConstantesBD.logOrdenesRespCirugiaHojaQuirurDiagSerDiagCodigo:
				archivo = ConstantesBD.logOrdenesRespCirugiaHojaQuirurDiagSerDiag;
				folderLog = ConstantesBD.logFolderModuloOrdenesMedicas + separador
						+ ConstantesBD.logFolderResponderCirugias;
				break;
			case ConstantesBD.logDestinoTriageCodigo:
				archivo = ConstantesBD.logDestinoTriageNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica + separador
						+ ConstantesBD.logFolderMantenimiento + separador 
						+ ConstantesBD.logDestinoTriageNombre;
				break;
			case ConstantesBD.logSistemasMotivoConsultaUrgCodigo:
				archivo = ConstantesBD.logSistemasMotivoConsultaUrgNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica + separador
						+ ConstantesBD.logFolderMantenimiento + separador 
						+ ConstantesBD.logSistemasMotivoConsultaUrgNombre;
				break;
			case ConstantesBD.logMezclasCodigo:
				archivo = ConstantesBD.logMezclasNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador
							+ ConstantesBD.logFolderMantenimientoInventario
							+ separador
							+ ConstantesBD.logMezclasNombre;
				break;
			case ConstantesBD.logArticulosXMezclaCodigo:
				archivo = ConstantesBD.logArticulosXMezclaNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador
							+ ConstantesBD.logFolderMantenimientoInventario
							+ separador
							+ ConstantesBD.logArticulosXMezclaNombre;
				break;
			case ConstantesBD.logSignosSintomasXSistemaCodigo:
				archivo = ConstantesBD.logSignosSintomasXSistemaUvrNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logSignosSintomasXSistemaUvrNombre;
				break;	
			case ConstantesBD.logCategoriasTriageCodigo:
				archivo = ConstantesBD.logCategoriasTriageNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logCategoriasTriageNombre;
				break;	
			case ConstantesBD.logUnidadMedidaCodigo:
				archivo = ConstantesBD.logUnidadMedidaNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logUnidadMedidaNombre;
				break;		
			case ConstantesBD.logSignosSintomasCodigo:
				archivo = ConstantesBD.logSignosSintomas;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logSignosSintomas;
				break;		
			case ConstantesBD.logNivelServicioCodigo:
				archivo = ConstantesBD.logNivelServicio;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logNivelServicio;
				break;		
			case ConstantesBD.logExcepcionNivelServicioCodigo:
				archivo = ConstantesBD.logExcepcionNivelServicio;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logExcepcionNivelServicio;
				break;	
			case ConstantesBD.logCuentasCobroCapitacionCodigo:
				archivo = ConstantesBD.logCuentasCobroCapitacionNombre;
				folderLog = ConstantesBD.logFolderModuloCapitacion + separador
						+ ConstantesBD.logCuentasCobroCapitacionNombre;
				break;
			case ConstantesBD.logCuentasProcesoFacturacionCodigo:
				archivo = ConstantesBD.logCuentasProcesoFacturacionNombre;
				folderLog = ConstantesBD.logFolderModuloMantenimientoAdministracion + separador
						+ ConstantesBD.logCuentasProcesoFacturacionNombre;
				break;
				
			case ConstantesBD.logTiposProgramaPYPCodigo:
				archivo = ConstantesBD.logTiposProgramaPYPNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logTiposProgramaPYPNombre;
				break;

			case ConstantesBD.logProgramaPYPCodigo:
				archivo = ConstantesBD.logProgramaPYPNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logProgramaPYPNombre;
				break;
			case ConstantesBD.logActividadesPYPCodigo:
				archivo = ConstantesBD.logActividadesPYPNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logActividadesPYPNombre;
				break;
			case ConstantesBD.logContratoCargueCodigo:
				archivo = ConstantesBD.logContratoCargueNombre;
				folderLog = ConstantesBD.logFolderModuloCapitacion
							+ separador + ConstantesBD.logFolderSubirPacientes
							+ separador + ConstantesBD.logContratoCargueNombre;
				break;
			case ConstantesBD.logProgramasActividadesConvenioCodigo:
				archivo = ConstantesBD.logProgramasActividadesConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logProgramasActividadesConvenioNombre;
				break;	
			case ConstantesBD.logProgramaArticuloPypCodigo:
				archivo = ConstantesBD.logProgramaArticuloPyp;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logProgramaArticuloPyp;
				break;
			case ConstantesBD.logCalificacionXCumpliMetasCodigo:
				archivo = ConstantesBD.logCalificacionXCumpliMetas;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logCalificacionXCumpliMetas;
				break;	
			case ConstantesBD.logMetasPYPCodigo:
				archivo = ConstantesBD.logMetasPYPNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logMetasPYPNombre;
				break;
			case ConstantesBD.logActividadesProgramaPYPCodigo:
				archivo = ConstantesBD.logActividadesProgramaPYPNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logActividadesProgramaPYPNombre;
				break;            
			case ConstantesBD.logTipoCalificacionMetaPypCodigo:
				archivo = ConstantesBD.logTipoCalificacionMetaPypNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logTipoCalificacionMetaPypNombre;
				break;            
			case ConstantesBD.logActividadesPypCentroAtencionCodigo:
				archivo = ConstantesBD.logActividadesPypCentroAtencionNombre;
				folderLog = ConstantesBD.logFolderModuloPYP
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logActividadesPypCentroAtencionNombre;
				break;            
			case ConstantesBD.logGruposEtareosCredDesaCodigo:
				archivo = ConstantesBD.logGruposEtareosCredDesaNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+ separador
						+ ConstantesBD.logFolderMantenimientoManejoPaciente+ separador
						+ ConstantesBD.logGruposEtareosCredDesaNombre;
				break;
			case ConstantesBD.logUnidadPagoCapitacionCodigo:
				archivo = ConstantesBD.logUnidadPagoCapitacionNombre;
				folderLog = ConstantesBD.logFolderModuloCapitacion + separador
						+ ConstantesBD.logFolderMantenimientoCapitacion+ separador
						+ ConstantesBD.logUnidadPagoCapitacionNombre;
				break;
			case ConstantesBD.logRegistroAccidentesTransitoCodigo:
				archivo = ConstantesBD.logRegistroAccidentesTransitoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente + separador + ConstantesBD.logFolderFosyga + separador 
						+ ConstantesBD.logRegistroAccidentesTransitoNombre;
				break;
			case ConstantesBD.logModificacionCamaCodigo:
				archivo = ConstantesBD.logModificacionCamaNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente + separador + ConstantesBD.logFolderMantenimientoManejoPaciente + separador
						+ ConstantesBD.logModificacionCamaNombre;
				break;	

			case ConstantesBD.logMotivosSircCodigo:
				archivo = ConstantesBD.logMotivosSircNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica + separador
						+ ConstantesBD.logFolderMantenimiento + separador 
						+ ConstantesBD.logMotivosSircNombre;;
				break;

			case ConstantesBD.logInstitucionesSircCodigo:
				archivo = ConstantesBD.logInstitucionesSircNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica + separador
						+ ConstantesBD.logFolderMantenimiento + separador 
						+ ConstantesBD.logInstitucionesSircNombre;;
				break;
				
			case ConstantesBD.logServiciosSircCodigo:

				archivo = ConstantesBD.logServiciosSircNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica + separador
						+ ConstantesBD.logFolderMantenimiento + separador 
						+ ConstantesBD.logServiciosSircNombre;;
				break;
			case ConstantesBD.logIndicativosolicitudGrupoServicioCodigo:
				archivo = ConstantesBD.logIndicativosolicitudGrupoServicioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+ separador
						+ ConstantesBD.logFolderMantenimiento + separador 
						+ ConstantesBD.logIndicativosolicitudGrupoServicioNombre;;
				break;
			case ConstantesBD.logParametrizacionCurvaAlertaNombreCodigo:
				archivo = ConstantesBD.logParametrizacionCurvaAlertaNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logParametrizacionCurvaAlertaNombre;
				break;
				
			case ConstantesBD.logIndicativoCargoViaIngresoGrupoServicioCodigo:
				archivo = ConstantesBD.logIndicativoCargoViaIngresoGrupoServicioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logIndicativoCargoViaIngresoGrupoServicioNombre;
				break;	
				
			case ConstantesBD.logConsultoriosCodigo:
				archivo = ConstantesBD.logConsultoriosNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logConsultoriosNombre;
				break;
			case ConstantesBD.logPaquetesMaterialesQxCodigo:
				archivo = ConstantesBD.logPaquetesMaterialesQxNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logPaquetesMaterialesQxNombre;
				break;
			case ConstantesBD.logCancelacionAgendaCodigo:
				archivo = ConstantesBD.logCancelacionAgendaNombre;
				folderLog = ConstantesBD.logFolderModuloConsultaExterna
							+ separador + ConstantesBD.logCancelacionAgendaNombre;
				break;
			
			case ConstantesBD.logFormaFarmaceuticaCodigo:
				archivo = ConstantesBD.logFormaFarmaceuticaNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
					+ separador + ConstantesBD.logFolderMantenimientoInventario + separador + ConstantesBD.logFormaFarmaceuticaNombre;
				break;
			case ConstantesBD.logModificarPacienteCodigo:
				archivo = ConstantesBD.logModificarPacienteNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
							+ separador + ConstantesBD.logModificarPacienteNombre;
				break;
			case ConstantesBD.logModificarClaseGrupoSubgrupoInvCodigo:
				archivo = ConstantesBD.logModificarClaseGrupoSubgrupoInvNombre;
				folderLog = ConstantesBD.logFolderModuloInventario+separador+ConstantesBD.logFolderMantenimientoInventario
							+ separador + ConstantesBD.logModificarClaseGrupoSubgrupoInvNombre;
				break;
			case ConstantesBD.logCuentasInventarioUnidadFuncionalCodigo:
				archivo = ConstantesBD.logCuentasInventarioUnidadFuncionalNombre;
				folderLog = ConstantesBD.logFolderModuloInterfaz+separador+ConstantesBD.logFolderInventarios
							+ separador + ConstantesBD.logCuentasInventarioUnidadFuncionalNombre;
				break;	
			case ConstantesBD.logCoberturaCodigo:
				archivo = ConstantesBD.logCoberturaNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logCoberturaNombre;
				break;
			case ConstantesBD.logPaquetesCodigo:
				archivo = ConstantesBD.logPaquetesNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logPaquetesNombre;
				break;
				
			case ConstantesBD.logHorarioAtencionCodigo:
				archivo = ConstantesBD.logHorarioAtencionNombre;
				folderLog = ConstantesBD.logFolderModuloConsultaExterna+separador+ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logHorarioAtencionNombre;
				break;
				
			case ConstantesBD.logInclusionesExclusionesCodigo:
				archivo = ConstantesBD.logInclusionesExclusionesNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logInclusionesExclusionesNombre;
				break;
			case ConstantesBD.logExamenCondiTomaCodigo:
				archivo = ConstantesBD.logExamenCondiTomaNombre;
				folderLog = ConstantesBD.logFolderModuloAgendaProcedimiento+separador+ConstantesBD.logFolderMantenimientoAgendaProcedimiento
							+ separador + ConstantesBD.logExamenCondiTomaNombre;
				break;
			case ConstantesBD.logRipsCapitacionCodigo:
				archivo = ConstantesBD.logRipsCapitacionNombre;
				folderLog = ConstantesBD.logFolderModuloCapitacion + separador + ConstantesBD.logRipsCapitacionNombre;
				break;
			case ConstantesBD.logPaquetesConvenioCodigo:
				archivo = ConstantesBD.logPaquetesConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logPaquetesConvenioNombre;
				break;
				
			case ConstantesBD.logRegistroEventoCatastroficoCodigo:
				archivo = ConstantesBD.logRegistroEventoCatastroficoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente + separador + ConstantesBD.logFolderFosyga + separador 
						+ ConstantesBD.logRegistroEventoCatastroficoNombre;
				break;
			case ConstantesBD.logServiciosGruposEsteticosCodigo:
				archivo = ConstantesBD.logAbonosYDescuentosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logServiciosGruposEsteticosNombre;
				break;
			case ConstantesBD.logGeneracionAnexosForecatCodigo:
				archivo = ConstantesBD.logGeneracionAnexosForecatNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+separador+ConstantesBD.logFolderFosyga
							+ separador + ConstantesBD.logGeneracionAnexosForecatNombre;
				break;
			case ConstantesBD.logTiposConvenioCodigo:
				archivo = ConstantesBD.logTiposConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logTiposConvenioNombre;
				break;
				
			case ConstantesBD.logUnidadProcedimientoCodigo:
				archivo = ConstantesBD.logUnidadProcedimientoNombre;
				folderLog = ConstantesBD.logFolderModuloAgendaProcedimiento+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logUnidadProcedimientoNombre;
				break;
				
			case ConstantesBD.logComponentesPaquetesCodigo:
				archivo = ConstantesBD.logComponentesPaquetesNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas+separador+ConstantesBD.logFolderPaquetes
							+ separador + ConstantesBD.logComponentesPaquetesNombre;
				break;
				
			case ConstantesBD.logPisosCodigo:
				archivo = ConstantesBD.logPisosNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logPisosNombre;
				break;
				
			case ConstantesBD.logTipoHabitacionCodigo:
				archivo = ConstantesBD.logTipoHabitacionNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logTipoHabitacionNombre;
				break;
				
			case ConstantesBD.logHabitacionesCodigo:
				archivo = ConstantesBD.logHabitacionesNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logHabitacionesNombre;
				break;
				
			case ConstantesBD.logAlmacenParametrosCodigo:
				archivo = ConstantesBD.logAlmacenParametrosNombre;
				folderLog = ConstantesBD.logFolderModuloInventario + separador 
						+ ConstantesBD.logFolderMantenimientoInventario
						+ separador + ConstantesBD.logAlmacenParametrosNombre;
				break;
				
			case ConstantesBD.logDetalleCoberturaCodigo:
				archivo = ConstantesBD.logDetalleCoberturaNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas+separador+ConstantesBD.logFolderCoberturas+separador+ConstantesBD.logDetalleCoberturaNombre;
			break;
			case ConstantesBD.logCoberturaConvenioCodigo:
				archivo = ConstantesBD.logCoberturaConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas+separador+ConstantesBD.logFolderCoberturas+separador+ConstantesBD.logCoberturaConvenioNombre;
			break;
			case ConstantesBD.logDescuentosComercialesCodigo:
				archivo = ConstantesBD.logDescuentosComercialesNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas+separador+ConstantesBD.logDescuentosComercialesNombre;
			break;
			
			case ConstantesBD.logTiposUsuarioCamaCodigo:
				archivo = ConstantesBD.logTiposUsuarioCamaNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logTiposUsuarioCamaNombre;
				break;
				
			case ConstantesBD.logDetalleInclusionesExclusionesCodigo:
				archivo = ConstantesBD.logDetalleInclusionesExclusionesNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas+separador+ConstantesBD.logDetalleInclusionesExclusionesNombre;
			break;
			case ConstantesBD.logInclusionesExclusionesConvenioCodigo:
				archivo = ConstantesBD.logInclusionesExclusionesConvenioNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas+separador+ConstantesBD.logInclusionesExclusionesConvenioNombre;
			break;
			
			
			case ConstantesBD.logTiposAmbulanciaCodigo:
				archivo = ConstantesBD.logTiposAmbulanciaNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logTiposAmbulanciaNombre;
				break;
				
			case ConstantesBD.logNaturalezaArticulosCodigo:
				archivo = ConstantesBD.logNaturalezaArticulosNombre;
				folderLog = ConstantesBD.logFolderInventarios+separador+ConstantesBD.logFolderMantenimientoInventario
							+ separador + ConstantesBD.logNaturalezaArticulosNombre;
				break;
				
			case ConstantesBD.logViasIngresoCodigo:
				archivo = ConstantesBD.logViasIngresoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente+separador+ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logViasIngresoNombre;
				break;
				
			case ConstantesBD.logUbicacionGeograficaCodigo:
				archivo = ConstantesBD.logUbicacionGeograficaNombre;
				folderLog = ConstantesBD.logFolderAdministracion+separador+ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logUbicacionGeograficaNombre;
				break;
				
			case ConstantesBD.logTiposMonedaCodigo:
				archivo = ConstantesBD.logTiposMonedaNombre;
				folderLog = ConstantesBD.logFolderAdministracion+separador+ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logTiposMonedaNombre;
				break;
				
			case ConstantesBD.logConsentimientoInformadoCodigo:
				archivo = ConstantesBD.logConsentimientoInformadoNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica+separador+ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logConsentimientoInformadoNombre;
				break;
				
			case ConstantesBD.logRegistroDocumentoGarantiaCodigo:
				archivo = ConstantesBD.logRegistroDocumentosGarantiaNombre;				
				folderLog = ConstantesBD.logFolderModuloCarteraPaciente+separador+ConstantesBD.logFolderDocumentosGarantia
							+ separador + ConstantesBD.logRegistroDocumentosGarantiaNombre;
				break;
			
			case ConstantesBD.logServiciosAutomaticosCodigo:
				archivo = ConstantesBD.logServiciosAutomaticosNombre;				
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logServiciosAutomaticosNombre;
				break;
				
			case ConstantesBD.logConceptosPagoPoolesCodigo:
				archivo = ConstantesBD.logConceptosPagoPoolesNombre;				
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logFolderModuloPooles+ separador + ConstantesBD.logConceptosPagoPoolesNombre;
				break;
				
			case ConstantesBD.logConceptosPagoPoolesXConvenioCodigo:
				archivo = ConstantesBD.logConceptosPagoPoolesXConvenioNombre;				
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logFolderMantenimientoTablas
							+ separador + ConstantesBD.logFolderModuloPooles+ separador + ConstantesBD.logConceptosPagoPoolesXConvenioNombre;
				break;
				
			case ConstantesBD.logFactorConversionMonedasCodigo:
				archivo = ConstantesBD.logFactorConversionMonedasNombre;				
				folderLog = ConstantesBD.logFolderAdministracion+separador+ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logFactorConversionMonedasNombre;
				break;
			
			case ConstantesBD.logCancelacionCitasCodigo:
				archivo = ConstantesBD.logCancelacionCitasNombre;				
				folderLog = ConstantesBD.logFolderModuloConsultaExterna+separador+ConstantesBD.logCancelacionCitasNombre;
				break;
				
			case ConstantesBD.logArchivoPlanoColsanitasCodigo:
				archivo = ConstantesBD.logArchivoPlanoColsanitasNombre;				
				folderLog = ConstantesBD.logFolderModuloFacturacion+separador+ConstantesBD.logArchivoPlanoColsanitasNombre;
				break;
			case ConstantesBD.logAsociosSalaCirugiaCodigo:
				archivo = ConstantesBD.logAsociosSalaCirugiaNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logAsociosSalaCirugiaNombre;
				break;
			case ConstantesBD.logProcedimientosPaquetesQuirugicosCodigo:
				archivo = ConstantesBD.logProcedimientosPaquetesQuirugicosNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logProcedimientosPaquetesQuirugicosNombre;
				break;
			case ConstantesBD.logFarmaciaCentroCostoCodigo:
				archivo = ConstantesBD.logFarmaciaCentroCostoNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logFarmaciaCentroCostoNombre;
				break;
			case ConstantesBD.logSeccionesCodigo:
				archivo = ConstantesBD.logSeccionesNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logSeccionesNombre;
				break;
			case ConstantesBD.logMotivosSatisfaccionInsatisfaccionCodigo:
				archivo = ConstantesBD.logMotivosSatisfaccionInsatisfaccionNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logMotivosSatisfaccionInsatisfaccionNombre;
				break;	
			case ConstantesBD.logArticulosPorAlmacenCodigo:
				archivo = ConstantesBD.logArticulosPorAlmacenNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logArticulosPorAlmacenNombre;
				break;				
			case ConstantesBD.logSubseccionesCodigo:
				archivo = ConstantesBD.logSubseccionesNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logSubseccionesNombre;
				break;
			case ConstantesBD.logConceptosFacturasVariasCodigo:
				archivo = ConstantesBD.logConceptosFacturasVariasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturasVarias
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logConceptosFacturasVariasNombre;
				break;
				
				
			case ConstantesBD.logDeudoresCodigo:
				archivo = ConstantesBD.logDeudoresNombre;
				folderLog = ConstantesBD.logFolderModuloFacturasVarias
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logDeudoresNombre;
				break;
				
				
			case ConstantesBD.logListadoIngresosCodigo:
				archivo = ConstantesBD.logListadoIngresosNombre;
				folderLog = ConstantesBD.logFolderModuloListadoIngresos
							+ separador + ConstantesBD.logFolderActualizacionAutorizaciones
							+ separador + ConstantesBD.logListadoIngresosNombre;
				break;
			case ConstantesBD.logEquivalentesDeInventarioCodigo:
				archivo = ConstantesBD.logEquivalentesDeInventarioNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logEquivalentesDeInventarioNombre;
				break;
			case ConstantesBD.logServiciosAsocioCodigo:
				archivo = ConstantesBD.logServiciosAsocioNombre;
				folderLog = ConstantesBD.logFolderModuloServiciosAsocios
							+ separador + ConstantesBD.logFolderServiciosAsocios
							+ separador + ConstantesBD.logServiciosAsocioNombre;
				break;	
			case ConstantesBD.logMotivoCierreAperturaIngresoCodigo:
				archivo = ConstantesBD.logMotivoCierreAperturaIngresoNombre;
				folderLog = ConstantesBD.logFolderModuloManejoPaciente
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logMotivoCierreAperturaIngresoNombre;
				break;
			case ConstantesBD.logSustitutosNoPosCodigo:
				archivo = ConstantesBD.logSustitutosNoPosNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logSustitutosNoPosNombre;
				break;
			case ConstantesBD.logRegistroConteoInventariosCodigo:
				archivo = ConstantesBD.logRegistroConteoInventariosNombre;
				folderLog = ConstantesBD.logFolderModuloInventario
							+ separador + ConstantesBD.logFolderInventarioFisicoInventario
							+ separador + ConstantesBD.logRegistroConteoInventariosNombre;
				break;
			case ConstantesBD.logLiquidacionServiciosCodigo:
				archivo = ConstantesBD.logLiquidacionServiciosNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
							+ separador + ConstantesBD.logFolderLiquidacion 
							+ separador + ConstantesBD.logLiquidacionServiciosNombre;
				break;
			case ConstantesBD.logTiposTratamientosOdontologicosCodigo:
				archivo = ConstantesBD.logTiposTratamientosOdontologicosNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logTiposTratamientosOdontologicosNombre;
				break;
			case ConstantesBD.logRegistroRipsCargosDirectosCodigo:
				archivo = ConstantesBD.logRegistroRipsCargosDirectosNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
							+ separador + ConstantesBD.logFolderCargosDirectos 
							+ separador + ConstantesBD.logRegistroRipsCargosDirectosNombre;
				break;
			case ConstantesBD.logParamArchivoPlanoIndCalidadCodigo:
				archivo = ConstantesBD.logParamArchivoPlanoIndCalidadNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
							+ separador + ConstantesBD.logFolderArchivosPlanos
							+ separador + ConstantesBD.logParamArchivoPlanoIndCalidadNombre;
				break;
			case ConstantesBD.logUnidadAgendaUsuarioCentroCodigo:
				archivo = ConstantesBD.logUnidadAgendaUsuarioCentroNombre;
				folderLog = ConstantesBD.logFolderModuloConsultaExterna
							+ separador + ConstantesBD.logFolderMantenimiento
							+ separador + ConstantesBD.logUnidadAgendaUsuarioCentroNombre;
				break;
			case ConstantesBD.logGeneracionModificacionAjustesFacVariasCodigo:
				archivo = ConstantesBD.logGeneracionModificacionAjustesFacVariasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturasVarias
							+ separador + ConstantesBD.logFolderAjustesFacturasVarias
							+ separador + ConstantesBD.logGeneracionModificacionAjustesFacVariasNombre;
				break;
			
			case ConstantesBD.logServiciosViaAccesoCodigo:
				archivo = ConstantesBD.logServiciosViaAccesoNombre;
				folderLog = ConstantesBD.logFolderModuloSalasCirugia
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logServiciosViaAccesoNombre;
				break;
			case ConstantesBD.logParametrizacionEscalas:
					archivo = ConstantesBD.logParametrizacionEscalasNombre;
					folderLog = ConstantesBD.logFolderModuloHistoriaClinica
							+ separador + ConstantesBD.logFolderCamposParametrizables
							+ separador + ConstantesBD.logParametrizacionEscalasNombre;
				break;
			
			case ConstantesBD.logServiciosXTipoTratamientoOdontologicoCodigo:
				archivo = ConstantesBD.logServiciosXTipoTratamientoOdontologicoNombre;
				folderLog = ConstantesBD.logFolderModuloHistoriaClinica
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logServiciosXTipoTratamientoOdontologicoNombre;
			break;
				
			case ConstantesBD.logConceptosGeneralesGlosasCodigo:
				archivo = ConstantesBD.logConceptosGeneralesGlosasNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logConceptosGeneralesGlosasNombre;
			break;
			
			case ConstantesBD.logConceptosEspecificosGlosasCodigo:
				archivo = ConstantesBD.logConceptosEspecificosGlosasNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas
						+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logConceptosEspecificosGlosasNombre;
			break;
			
			case ConstantesBD.logAplicacionPagosFacturasVariasCodigo:
				archivo = ConstantesBD.logAplicacionPagosFacturasVariasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturasVarias
						+ separador + ConstantesBD.logFolderPagosFacturasVarias
						+ separador + ConstantesBD.logAplicacionPagosFacturasVariasNombre;
			break;
			
			case ConstantesBD.logTotalFacturadoConvenioContratoCodigo:
				archivo = ConstantesBD.logTotalFacturadoConvenioContratoNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador + ConstantesBD.logFolderReportes 
						+ separador + ConstantesBD.logTotalFacturadoConvenioContratoNombre;
			break;
			
			case ConstantesBD.logListadoGlosasCodigo:
				archivo = ConstantesBD.logListadoGlosasNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas
						+ separador + ConstantesBD.logRegistroGlosas
						+ separador + ConstantesBD.logConsultarImprimirGlosas
						+ separador + ConstantesBD.logListadoGlosasNombre;
			break;
			
			case ConstantesBD.logImpresionGlosaSolicitudCodigo:
				archivo = ConstantesBD.logImpresionGlosaSolicitudNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas
						+ separador + ConstantesBD.logRegistroGlosas 
						+ separador + ConstantesBD.logConsultarImprimirGlosas
						+ separador + ConstantesBD.logImpresionGlosaSolicitudNombre;
			break;
			
			case ConstantesBD.logEliminarExceCCInterconsultasCodigo:
				archivo = ConstantesBD.logEliminarExceCCInterconsultasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador + ConstantesBD.logMantenimientoFacturacion
						+ separador + ConstantesBD.logServicios
						+ separador + ConstantesBD.logEliminarExceCCInterconsultasNombre;
			break;
			
			case ConstantesBD.logImpresionDetalleGlosaCodigo:
				archivo = ConstantesBD.logImpresionDetalleGlosaNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas
						+ separador + ConstantesBD.logRegistroGlosas 
						+ separador + ConstantesBD.logConsultarImprimirGlosas
						+ separador + ConstantesBD.logImpresionDetalleGlosaNombre;
			break;
			
			case ConstantesBD.logModificacionPreglosaCodigo:
				archivo = ConstantesBD.logModificacionPreglosaNombre;
				folderLog = ConstantesBD.logFolderModuloGlosas
						+ separador + ConstantesBD.logAuditoriasGlosas 
						+ separador + ConstantesBD.logAuditoriasGlosas
						+ separador + ConstantesBD.logModificacionPreglosaNombre;
			break;
			
			case ConstantesBD.logHonorariosEspecialidadServicios:
				archivo = ConstantesBD.logHonorariosEspecialidadNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador + ConstantesBD.logFolderMantenimientoTablas
						+ separador + "Pooles"
						+ separador + ConstantesBD.logHonorariosEspecialidadNombre;
			break;
			
			case ConstantesBD.logBeneficiarios:
				archivo = ConstantesBD.logBeneficiariosNombre;
				folderLog = ConstantesBD.logFolderModuloOdontologia
				     + separador + ConstantesBD.logFolderAdministracion
						+ separador + ConstantesBD.logBeneficiariosNombre;
			break;
			
			case ConstantesBD.logHallazgosPS:
				archivo = ConstantesBD.logHallazgoProgramaServicioNombre;
				folderLog = ConstantesBD.logFolderModuloOdontologia 
			        	+ separador + ConstantesBD.logFolderMantenimiento
						+ separador + ConstantesBD.logHallazgoProgramaServicioNombre;
			break;
			
			case ConstantesBD.logContactosEmpresa:
				archivo = ConstantesBD.logContactosEmpresaNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador + ConstantesBD.logMantenimientoFacturacion 
						+ separador + ConstantesBD.logContactosEmpresaNombre;
			break;
			
			case ConstantesBD.logEmisionBonosDescuentosOdontologicos :
				archivo = ConstantesBD.logEmisionBonosDescuentoOdontologico;
				folderLog = ConstantesBD.logFolderModuloOdontologia
						+ separador + ConstantesBD.logMantenimientoFacturacion 
						+ separador + ConstantesBD.logContactosEmpresaNombre;
			break;
			
			case ConstantesBD.logSoportesFacturasCodigo :
				archivo = ConstantesBD.logSoportesFacturasNombre;
				folderLog = ConstantesBD.logFolderModuloFacturacion
						+ separador + ConstantesBD.logMantenimientoFacturacion 
						+ separador + ConstantesBD.logSoportesFacturasNombre;
			break;
			
			case ConstantesBD.logAliadosOdontologicosCodigo :
				archivo = ConstantesBD.logAliadosOdontologicosNombre;
				folderLog = ConstantesBD.logFolderModuloOdontologia
						+ separador + ConstantesBD.logMantenimientoFacturacion 
						+ separador + ConstantesBD.logAliadosOdontologicosNombre;
			break;
			
			case ConstantesBD.logHonorariosPooles:
				archivo=ConstantesBD.logHonorariosPoolTarifasConvenios;
				folderLog=ConstantesBD.logFolderModuloFacturacion
							+ separador + ConstantesBD.logMantenimientoFacturacion 
							+ separador + ConstantesBD.logHonorariosPoolTarifasConvenios;
				break;
				
				
			case ConstantesBD.logTransportadoraValores:
					archivo=ConstantesBD.logTransportadoraValoresN;
					folderLog=ConstantesBD.logFolderModuloTesoreria+
							separador + ConstantesBD.logMantenimientoTesoreria+
							separador + ConstantesBD.logTransportadoraValoresN;
					break;
			case ConstantesBD.codigoCuotoOdontEspecialidad:
					archivo=ConstantesBD.logCuotaOdontologicas;
					folderLog=ConstantesBD.logFolderModuloOdontologia+
						separador + ConstantesBD.logCuotaOdontologicas+
						separador + ConstantesBD.logCuotaOdontologicas;
				break;
			
			default:
				logger.error("Esta tratando de insertar en un archivoLog que no existe, por favor utilice la clase ConstantesBD para definir el nombre del log");
				return false;
			}

			switch (tipo) {
			case 1:
				textoTipo = "[Registro Nuevo]";
				tipoLog = tipo;
				break;
			case 2:
				textoTipo = "[Registro Modificado]";
				tipoLog = tipo;
				break;
			case 3:
				textoTipo = "[Registro Eliminado]";
				tipoLog = tipo;
				break;
			default:
				logger
						.error("Esta tratando de insertar un tipo de registro no valido, por favor utilice la clase ConstantesBD para definir el tipo de registro");
				return false;
			}

			try {
				if (!folderLog.equals("")) {
					logger.info("\n\n\n\n");
					logger.info("*************  WRITE LOG	**************************************************");
					
					logger.info("rutaLogs----->"+rutaLogs);
					logger.info("rutaLogs----->"+folderLog);
				
					File directorio = new File(rutaLogs, folderLog);

					if (!directorio.isDirectory() && !directorio.exists()) {
						if (!directorio.mkdirs()) {
							logger.error("Error creando el directorio "
									+ folderLog);
						}
					}
					archivo = folderLog + separador + archivo;
					folderLog = "";/* @todo para que no dañe los demas logs */
					
					logger.info("archivo-->"+archivo);
					logger.info("folderLog->"+folderLog);
					
				}
				FileWriter archivoLog = new FileWriter(rutaLogs
						+ archivo
						+ ".log."
						+ UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha
								.getFechaActual()), true);
				log = textoTipo + " Fecha [" + UtilidadFecha.getFechaActual()
						+ "] Hora [" + UtilidadFecha.getHoraActual()
						+ "] Usuario [" + Usuario + "]\t" + texto + "\n";
				logger.info("\t\t\t\t------------------log*->"+log);
				
				archivoLog.write(log);
				archivoLog.close();
				return true;
			} catch (Exception e) {
				logger.error("Error en el manejo de logs de axioma: " + e);
				return false;
			}
		} else {
			logger.error("No ha definido una ruta para el manejo de los logs");
			return false;
		}
	}

	/**
	 * @return Retorna el nombre del archivo en el cual se insertó el último log
	 */
	public static String getArchivo() {
		return archivo;
	}

	/**
	 * @return Retorna el texto que fue mandado al log
	 */
	public static String getLog() {
		return log;
	}

	/**
	 * @return Retorna la ruta donde seon guardados los logs
	 */
	public static String getRutaLogs() {
		return rutaLogs;
	}

	/**
	 * @return Retorna el tipo de accion realizada (Insertar, Modificar,
	 *         Eliminar)
	 */
	public static int getTipoLog() {
		return tipoLog;
	}

	/**
	 * @param ruta
	 */
	public static void setRutaLogs(String ruta) {
		if (!System.getProperty("file.separator").equals("/")) {
			rutaLogs = ruta.replace('/', '\\');

		} else {
			rutaLogs = ruta;
		}
		File directorioLogs = new File(rutaLogs);
		if (!directorioLogs.isDirectory()) {
			if (!directorioLogs.mkdirs()) {
				logger.error("Error creando el directorio de los logs" + ruta);
			}
		}
	}

	/**
	 * @return Retorna folderLog.
	 */
	public static String getFolderLog() {
		return folderLog;
	}
}
