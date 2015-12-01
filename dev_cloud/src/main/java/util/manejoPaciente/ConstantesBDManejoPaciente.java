/*
 * Jun 21, 2007
 */
package util.manejoPaciente;

/**
 * Manejo de las constantes del módulod el Manejo del Paciente
 * @author Sebastián Gómez R.
 *
 */
public interface ConstantesBDManejoPaciente 
{
	/***************************************************************************
	 * CONSTANTES PARA TIPOS DE SANGRE
	 **************************************************************************/
	public static final String codigoTipoSangreAPositivo = "1";
	public static final String codigoTipoSangreANegativo = "2";
	public static final String codigoTipoSangreBPositivo = "3";
	public static final String codigoTipoSangreBNegativo = "4";
	public static final String codigoTipoSangreOPositivo = "5";
	public static final String codigoTipoSangreONegativo = "6";
	public static final String codigoTipoSangreABPositivo = "7";
	public static final String codigoTipoSangreABNegativo = "8";
	public static final String codigoTipoSangreDesconocido = "9";
	
	/***************************************************************************
	 * CONSTANTES PARA TIPOS DE REPORTE
	 **************************************************************************/
	public static final int tipoReporteResumenMensualEgresos = 1;
	public static final int tipoReporteDiagnosticosEgresosRangoEdad = 2;
	public static final int tipoReporteEstanciaPromMensualPacEgresadosRan = 3;
	public static final int tipoReportePacEgresadosPediatriaRangoEdad = 4;
	public static final int tipoReporteTasaMortalidadRangoEdadSexo = 5;
	public static final int tipoReporteEstanciaPromPacFallecidosRangoEdad = 6;
	public static final int tipoReporteMortalidadRangoTiempos = 7;
	public static final int tipoReporteMortalidadGlobal = 8;
	public static final int tipoReporteMortalidadRangoTiempoCentroCosto = 9;
	public static final int tipoReporteNumeroPacientesFallecidosDxMuerte = 10;
	public static final int tipoReporteMortalidadMensualConvenio = 11;
	public static final int tipoReporteListadoPacientesFallecidos = 12;
	public static final int tipoReporteMortalidadDxMuerteCentroCosto = 13;
	public static final int tipoReporteMortalidadSexo = 14;
	public static final int tipoReporteSatisfaccionGeneral = 15;
	public static final int tipoReporteMotCalificacionCalidadAtencion = 16;
	public static final int tipoReporteServiciosRealizados = 17;
	public static final int tipoReporteServiciosRealizadosXConvenio = 18;
	public static final int tipoReporteServiciosRealizadosXEspecialidad = 19;
	public static final int tipoReporteIngresosPorConvenio = 20;
	public static final int tipoReporteReingresos = 21;
	public static final int tipoReporteTotalReingresosPorConvenio = 22;
	public static final int tipoReporteAtencionPorRangoEdad = 23;
	public static final int tipoReporteAtencionPorEmpresaYConvenio = 24;
	public static final int tipoReporteEgresosConvenio = 25;
	public static final int tipoReporteEgresosLugarResidencia = 26;
	public static final int tipoReporteTotalDiagnosticoEgreso = 27;
	public static final int tipoReporteNPrimerasCausasMorbilidad = 28;
	public static final int tipoReporteIndicadoresHospitalizacion = 29;
	public static final int tipoReporteEstanciaPacienteMayorNDias = 30;
	
	/***************************************************************************
	 * FUNCIONALIDADES REPORTES
	 **************************************************************************/
	public static final int funcReporteEgresosEstancias = 1;
	public static final int funcReporteMortalidad = 2;
	
	
	/***************************************************************************
	 * TIPOS DE PERSONAS
	 **************************************************************************/
	public static final int codigoTipoPersonaNatural=1;
	public static final int codigoTipoPersonaJuridico=2;
	
	/***************************************************************************
	 * TIPOS DE AREAS
	 **************************************************************************/
	public static final int codigoTipoAreaSubAlmacen=3;
	
	/**=============TIPOS DE AUTORIZACIONES CONSULTA AUTORIZACIONES POR RANGO=========================================*/
	public static final String codigoTipoAutorizacionServicio = "1";
	public static final String codigoTipoAutorizacionMedIns = "2";
	public static final String codigoTipoAutorizacionServicioIngre = "3";
	public static final String codigoTipoAutorizacionMedInsIngre = "4";
	public static final String codigoTipoAutorizacionIngresoEstancia = "5";
	
	public static final int codigoTipoConsecutivoAutorizacionCapitacion= 1;
	public static final int codigoTipoConsecutivoAutorizacionEntidadSub= 2;
	
	public static final int codigoTipoReporteOrdenarodesConsultaExternaProf= 1;
	public static final int codigoTipoReporteOrdenarodesConsultaExternaGrupoClase= 2;
}
