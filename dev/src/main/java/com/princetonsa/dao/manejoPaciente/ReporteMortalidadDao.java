/*
 * Agosto 25, 2008
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Reporte Mortalidad
 */
public interface ReporteMortalidadDao 
{
	/**
	 * Método que edita las consultas del reporte de tasa de mortalidad 
	 * por rango de edad y sexo
	 */
	public String[] obtenerConsultasTasaMortalidadRangoEdadSexo (HashMap campos);
	
	/**
	 * Método que define la consulta del reporte de número de pacientes fallecidos
	 * por diagnóstico de muerte
	 */
	public String obtenerConsultasNumeroPacFallecidosDxMuerte(HashMap campos);
	
	
	/**
	 * Método que define la consulta del reporte de mortalidad mensual por convenio
	 */
	public String obtenerConsultaMortalidadMensualConvenio(HashMap campos);
	
	/**
	 * Método que define la consulta del reporte de listado de pacientes fallecidos
	 */
	public String obtenerConsultaListadoPacientesFallecidos(HashMap campos);
	
	/**
	 * Método que define la consulta del reporte de mortalidad por diagnostico de muerte y centro de costo
	 */
	public String obtenerConsultaMortalidadDxMuerteCentroCosto(HashMap campos);
	
	/**
	 * Método que define la consulta del reporte de estancia promedio de pacientes fallecidos por rango de edad
	 */
	public String[] obtenerConsultasEstanciaPromPacFallRangoEdad(HashMap campos);
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad por rango de tiempos
	 */
	public String obtenerConsultaMortalidadRangoTiempos(HashMap campos);
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad por sexo
	 */
	public String obtenerConsultaMortalidadSexo(HashMap campos);
	
	/**
	 * Método para carga los datos de la tasa de mortalidad global de hospitalizados
	 */
	public HashMap<String, Object> obtenerDatosTasaMortalidadGlobalHospitalizados(Connection con,HashMap campos);
	
	/**
	 * Método para obtener el total de egresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerTotalEgresos(Connection con,HashMap campos);
	
	/**
	 * Método para obtener datos de la tasa de mortalidad global de las vias de ingreso
	 * que no manejan rangos de tiempos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerDatosTasaMortalidadGlobalSinRangos(Connection con,HashMap campos);
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad rango de tiempo y centro de costo
	 */
	public String obtenerConsultaMortalidadRangoTiempoCentroCosto(HashMap campos);
}
