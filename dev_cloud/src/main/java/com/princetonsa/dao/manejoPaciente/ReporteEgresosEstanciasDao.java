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
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * Reporte Estad�sticas Egresos y Estancias
 */
public interface ReporteEgresosEstanciasDao 
{
	/**
	 * M�todo para consultar los datos del reporte de resumen mensual egresos
	 */
	public HashMap<String, Object> obtenerDatosResumenMensualEgresos(Connection con,HashMap campos);
	
	/**
	 * M�todo para obtener la consulta del tipo de reporte de egresos por convenio
	 */
	public String obtenerConsultaReporteEgresosConvenio(HashMap campos);
	
	/**
	 * M�todo para obtener la consulta del reporte de egresos por lugar de residencia
	 */
	public String obtenerConsultaReporteEgresosLugarResidencia(HashMap campos);
	
	/**
	 * M�todo para obtener los diagnosticos prioritarios del tipo de reporte 
	 * de diagn�stico de egreso por rango de edad
	 */
	public ArrayList<HashMap<String,Object>> obtenerDiagnosticosPrioridadEgresos(Connection con,HashMap campos);
	
	/**
	 * M�todo para obtener los datos del tipo reporte Dx de egreso por rango de edad
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String,Object> obtenerDatosReporteDxEgresoRangoEdad(Connection con,HashMap campos);
	
	/**
	 * M�todo para obtener la consulta del tipo de reporte de total diagn�sticos de egreso
	 */
	public String obtenerConsultaReporteTotalDxEgreso(HashMap campos);
	
	/**
	 * M�todo para obtener la consulta del reporte de la primeras N causas de morbilidad
	 */
	public String obtenerConsultaReporteNPrimerasCausasMorbilidad(HashMap campos);
	
	/**
	 * M�todo para retornar la consulta del reporte de estancia promedio mensual pacienrtes egresados por rango
	 */
	public String obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(HashMap campos);
	
	/**
	 * M�todo que arma la consulta para el reporte de pacientes egresados de pediatr�a por rango de edad
	 */
	public String obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(HashMap campos);
	
	/**
	 * M�todo para obtener la consulta del reporte de estancia de pacientes mayores de N d�as
	 */
	public String obtenerConsultaReporteEstanciaPacientesMayorNDias(HashMap campos);
	
	/**
	 * M�todo para realizar la consulta del reporte indicadores hospitalizaci�n
	 */
	public ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> consultarIndicadoresHospitalizacion(Connection con, DtoFiltroReporteIndicadoresHospitalizacion dtoFiltro);
}
