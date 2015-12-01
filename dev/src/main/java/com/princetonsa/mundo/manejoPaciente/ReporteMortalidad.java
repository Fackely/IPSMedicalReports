/*
 * Agosto 25, 2008
 */
package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ReporteMortalidadDao;
import com.princetonsa.mundo.atencion.Diagnostico;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Reporte Mortalidad
 */
public class ReporteMortalidad 
{

	/**
	 * Método para obtener el DAO de reporte mortalidad
	 * @return
	 */
	public static ReporteMortalidadDao reporteMortalidadDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteMortalidadDao();
	}
	
	/**
	 * Método para obtener las consultas del reporte de
	 * tasa de mortalidad rango edad sexo
	 * @param codigoCentroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoViaIngreso
	 * @param codigoSexo
	 * @param codigoInstitucion
	 * @return
	 */
	public static String[] obtenerConsultasTasaMortalidadRangoEdadSexo(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,int codigoSexo,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoSexo", codigoSexo);
		campos.put("codigoInstitucion", codigoInstitucion);
		return reporteMortalidadDao().obtenerConsultasTasaMortalidadRangoEdadSexo(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de número de pacientes fallecidos
	 * por diagnóstico de muerte
	 */
	public static String obtenerConsultasNumeroPacFallecidosDxMuerte(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,HashMap<String, Object> diagnosticosMuerte)
	{
		
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		return reporteMortalidadDao().obtenerConsultasNumeroPacFallecidosDxMuerte(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de mortalidad mensual por convenio
	 */
	public static String obtenerConsultaMortalidadMensualConvenio(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,HashMap<String, Object> convenios,Diagnostico diagnosticoEgreso,int edad)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion",codigoCentroAtencion);
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("convenios",convenios);
		campos.put("diagnosticoEgreso",diagnosticoEgreso);
		campos.put("edad",edad);
		return reporteMortalidadDao().obtenerConsultaMortalidadMensualConvenio(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de listado de pacientes fallecidos
	 */
	public static String obtenerConsultaListadoPacientesFallecidos(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,int edad,Diagnostico diagnosticoEgreso,HashMap<String, Object> diagnosticoMuerte,int codigoConvenio,int estancia)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("edad", edad);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("diagnosticoMuerte", diagnosticoMuerte);
		campos.put("estancia", estancia);
		campos.put("codigoConvenio", codigoConvenio);
		return reporteMortalidadDao().obtenerConsultaListadoPacientesFallecidos(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de mortalidad por diagnostico de muerte y centro de costo
	 */
	public static String obtenerConsultaMortalidadDxMuerteCentroCosto(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,HashMap diagnosticosMuerte,HashMap centrosCosto)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("centrosCosto", centrosCosto);
		return reporteMortalidadDao().obtenerConsultaMortalidadDxMuerteCentroCosto(campos);
	}
	
	/**
	 * Método que define la consulta del reporte de estancia promedio de pacientes fallecidos por rango de edad
	 */
	public static String[] obtenerConsultasEstanciaPromPacFallRangoEdad(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,int estancia,HashMap diagnosticosMuerte,Diagnostico diagnosticoEgreso,HashMap centrosCosto,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("estancia", estancia);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("centrosCosto", centrosCosto);
		campos.put("codigoInstitucion", codigoInstitucion);
		return reporteMortalidadDao().obtenerConsultasEstanciaPromPacFallRangoEdad(campos);
		
		
	}
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad por rango de tiempos
	 */
	public static String obtenerConsultaMortalidadRangoTiempos(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,HashMap diagnosticosMuerte,HashMap centrosCosto,Diagnostico diagnosticoEgreso,int edad,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("centrosCosto", centrosCosto);
		campos.put("edad", edad);
		campos.put("codigoInstitucion", codigoInstitucion);
		return reporteMortalidadDao().obtenerConsultaMortalidadRangoTiempos(campos);
	}
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad por sexo
	 */
	public static String obtenerConsultaMortalidadSexo(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,int codigoSexo,int edad,HashMap diagnosticosMuerte,HashMap centrosCosto,Diagnostico diagnosticoEgreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoSexo", codigoSexo);
		campos.put("edad", edad);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("centrosCosto", centrosCosto);
		return reporteMortalidadDao().obtenerConsultaMortalidadSexo(campos);
	}
	
	/**
	 * Método para carga los datos de la tasa de mortalidad global de hospitalizados
	 */
	public static HashMap<String, Object> obtenerDatosTasaMortalidadGlobalHospitalizados(Connection con,int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoSexo,int edad,HashMap diagnosticosMuerte,HashMap centrosCosto,Diagnostico diagnosticoEgreso,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoSexo", codigoSexo);
		campos.put("edad", edad);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("centrosCosto", centrosCosto);
		campos.put("codigoInstitucion", codigoInstitucion);
		return reporteMortalidadDao().obtenerDatosTasaMortalidadGlobalHospitalizados(con, campos);
	}
	
	/**
	 * Método para obtener el total de egresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerTotalEgresos(Connection con,int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoSexo,int edad,HashMap diagnosticosMuerte,HashMap centrosCosto,Diagnostico diagnosticoEgreso,int codigoViaIngreso,String mesEgreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoSexo", codigoSexo);
		campos.put("edad", edad);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("centrosCosto", centrosCosto);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("mesEgreso", mesEgreso);
		
		return reporteMortalidadDao().obtenerTotalEgresos(con, campos);
	}
	
	/**
	 * Método para obtener datos de la tasa de mortalidad global de las vias de ingreso
	 * que no manejan rangos de tiempos
	 * @param con
	 * @param codigoCentroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoSexo
	 * @param edad
	 * @param diagnosticosMuerte
	 * @param centrosCosto
	 * @param diagnosticoEgreso
	 * @param codigoViaIngreso
	 * @param codigoTipoPaciente
	 * @return
	 */
	public static HashMap<String, Object> obtenerDatosTasaMortalidadGlobalSinRangos(Connection con,int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoSexo,int edad,HashMap diagnosticosMuerte,HashMap centrosCosto,Diagnostico diagnosticoEgreso,int codigoViaIngreso,String codigoTipoPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoSexo", codigoSexo);
		campos.put("edad", edad);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("centrosCosto", centrosCosto);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		return reporteMortalidadDao().obtenerDatosTasaMortalidadGlobalSinRangos(con, campos);
	}
	
	/**
	 * Método para obtener la consultar del reporte de mortalidad rango de tiempo y centro de costo
	 */
	public static String obtenerConsultaMortalidadRangoTiempoCentroCosto(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,int codigoSexo,HashMap diagnosticosMuerte,HashMap centrosCosto,Diagnostico diagnosticoEgreso,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoSexo", codigoSexo);
		campos.put("diagnosticosMuerte", diagnosticosMuerte);
		campos.put("diagnosticoEgreso", diagnosticoEgreso);
		campos.put("centrosCosto", centrosCosto);
		campos.put("codigoInstitucion", codigoInstitucion);
		
		return reporteMortalidadDao().obtenerConsultaMortalidadRangoTiempoCentroCosto(campos);
	}
}
