/*
 * Sep 03, 2008
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoFiltroReporteIndicadoresHospitalizacion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoConsultaIndicadoresHospitalizacion;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Reporte Estadísticas Egresos y Estancias
 */
public interface ReporteEgresosEstanciasDao 
{
	/**
	 * Método para consultar los datos del reporte de resumen mensual egresos
	 */
	public HashMap<String, Object> obtenerDatosResumenMensualEgresos(Connection con,HashMap campos);
	
	/**
	 * Método para obtener la consulta del tipo de reporte de egresos por convenio
	 */
	public String obtenerConsultaReporteEgresosConvenio(HashMap campos);
	
	/**
	 * Método para obtener la consulta del reporte de egresos por lugar de residencia
	 */
	public String obtenerConsultaReporteEgresosLugarResidencia(HashMap campos);
	
	/**
	 * Método para obtener los diagnosticos prioritarios del tipo de reporte 
	 * de diagnóstico de egreso por rango de edad
	 */
	public ArrayList<HashMap<String,Object>> obtenerDiagnosticosPrioridadEgresos(Connection con,HashMap campos);
	
	/**
	 * Método para obtener los datos del tipo reporte Dx de egreso por rango de edad
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String,Object> obtenerDatosReporteDxEgresoRangoEdad(Connection con,HashMap campos);
	
	/**
	 * Método para obtener la consulta del tipo de reporte de total diagnósticos de egreso
	 */
	public String obtenerConsultaReporteTotalDxEgreso(HashMap campos);
	
	/**
	 * Método para obtener la consulta del reporte de la primeras N causas de morbilidad
	 */
	public String obtenerConsultaReporteNPrimerasCausasMorbilidad(HashMap campos);
	
	/**
	 * Método para retornar la consulta del reporte de estancia promedio mensual pacienrtes egresados por rango
	 */
	public String obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(HashMap campos);
	
	/**
	 * Método que arma la consulta para el reporte de pacientes egresados de pediatría por rango de edad
	 */
	public String obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(HashMap campos);
	
	/**
	 * Método para obtener la consulta del reporte de estancia de pacientes mayores de N días
	 */
	public String obtenerConsultaReporteEstanciaPacientesMayorNDias(HashMap campos);
	
	/**
	 * Método para realizar la consulta del reporte indicadores hospitalización
	 */
	public ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> consultarIndicadoresHospitalizacion(Connection con, DtoFiltroReporteIndicadoresHospitalizacion dtoFiltro);
}
