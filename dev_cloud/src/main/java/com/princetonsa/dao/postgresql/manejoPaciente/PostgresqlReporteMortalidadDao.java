/*
 * Ago 25, 2008
 */
package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ReporteMortalidadDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseReporteMortalidadDao;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Reporte Mortalidad
 */
public class PostgresqlReporteMortalidadDao  implements ReporteMortalidadDao
{
	
	/**
	 * Método que edita las consultas del reporte de tasa de mortalidad 
	 * por rango de edad y sexo
	 */
	public String[] obtenerConsultasTasaMortalidadRangoEdadSexo (HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultasTasaMortalidadRangoEdadSexo(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de número de pacientes fallecidos
	 * por diagnóstico de muerte
	 */
	public String obtenerConsultasNumeroPacFallecidosDxMuerte(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultasNumeroPacFallecidosDxMuerte(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de mortalidad mensual por convenio
	 */
	public String obtenerConsultaMortalidadMensualConvenio(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultaMortalidadMensualConvenio(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de listado de pacientes fallecidos
	 */
	public String obtenerConsultaListadoPacientesFallecidos(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultaListadoPacientesFallecidos(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de mortalidad por diagnostico de muerte y centro de costo
	 */
	public String obtenerConsultaMortalidadDxMuerteCentroCosto(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultaMortalidadDxMuerteCentroCosto(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de estancia promedio de pacientes fallecidos por rango de edad
	 */
	public String[] obtenerConsultasEstanciaPromPacFallRangoEdad(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultasEstanciaPromPacFallRangoEdad(campos);
	}
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad por rango de tiempos
	 */
	public String obtenerConsultaMortalidadRangoTiempos(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultaMortalidadRangoTiempos(campos);
	}
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad por sexo
	 */
	public String obtenerConsultaMortalidadSexo(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultaMortalidadSexo(campos);
	}
	
	/**
	 * Método para carga los datos de la tasa de mortalidad global de hospitalizados
	 */
	public HashMap<String, Object> obtenerDatosTasaMortalidadGlobalHospitalizados(Connection con,HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerDatosTasaMortalidadGlobalHospitalizados(con, campos);
	}
	
	/**
	 * Método para obtener el total de egresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerTotalEgresos(Connection con,HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerTotalEgresos(con, campos);
	}
	
	/**
	 * Método para obtener datos de la tasa de mortalidad global de las vias de ingreso
	 * que no manejan rangos de tiempos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerDatosTasaMortalidadGlobalSinRangos(Connection con,HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerDatosTasaMortalidadGlobalSinRangos(con, campos);
	}
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad rango de tiempo y centro de costo
	 */
	public String obtenerConsultaMortalidadRangoTiempoCentroCosto(HashMap campos)
	{
		return SqlBaseReporteMortalidadDao.obtenerConsultaMortalidadRangoTiempoCentroCosto(campos);
	}
}
