/*
 * Sep 03, 2008
 */
package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ReporteEgresosEstanciasDao;
import com.princetonsa.dto.manejoPaciente.DtoFiltroReporteIndicadoresHospitalizacion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoConsultaIndicadoresHospitalizacion;

/**
 * @author Sebasti�n G�mez 
 *
 *Clase que representa el Mundo con sus atributos y m�todos de la funcionalidad
 * Reporte Estad�sticas de Egresos y Estancias
 */
public class ReporteEgresosEstancias 
{
	/**
	 * M�todo para obtener el DAO de reporte egresos y estancias
	 * @return
	 */
	public static ReporteEgresosEstanciasDao reporteEgresosEstanciasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteEgresosEstanciasDao();
	}
	
	/**
	 * M�todo para consultar los datos del reporte de resumen mensual egresos
	 */
	public static HashMap<String, Object> obtenerDatosResumenMensualEgresos(Connection con,int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaingreso,String codigoTipoPaciente,String estadoFacturacion,int codigoSexo,HashMap centrosCosto,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaingreso);
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		campos.put("estadoFacturacion", estadoFacturacion);
		campos.put("codigoSexo", codigoSexo);
		campos.put("centrosCosto", centrosCosto);
		campos.put("codigoInstitucion", codigoInstitucion);
		return reporteEgresosEstanciasDao().obtenerDatosResumenMensualEgresos(con, campos);
	}
	
	/**
	 * M�todo para obtener la consulta del tipo de reporte de egresos por convenio
	 */
	public static String obtenerConsultaReporteEgresosConvenio(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,int prioridad,String estadoFacturacion,int codigoSexo,HashMap<String,Object> convenios)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion",codigoCentroAtencion);
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("codigoTipoPaciente",codigoTipoPaciente);
		campos.put("prioridad",prioridad);
		campos.put("estadoFacturacion",estadoFacturacion);
		campos.put("codigoSexo",codigoSexo);
		campos.put("convenios",convenios);
		return reporteEgresosEstanciasDao().obtenerConsultaReporteEgresosConvenio(campos);
	}
	
	/**
	 * M�todo para obtener la consulta del reporte de egresos por lugar de residencia
	 */
	public static String obtenerConsultaReporteEgresosLugarResidencia(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,int codigoSexo,String prioridad,String codigoPais,HashMap ciudades,HashMap diagnosticosEgreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		campos.put("codigoSexo", codigoSexo);
		campos.put("prioridad", prioridad);
		campos.put("codigoPais", codigoPais);
		campos.put("ciudades", ciudades);
		campos.put("diagnosticosEgreso", diagnosticosEgreso);
		return reporteEgresosEstanciasDao().obtenerConsultaReporteEgresosLugarResidencia(campos);
	}
	
	/**
	 * M�todo para obtener los diagnosticos prioritarios del tipo de reporte 
	 * de diagn�stico de egreso por rango de edad
	 */
	public static ArrayList<HashMap<String,Object>> obtenerDiagnosticosPrioridadEgresos(Connection con,int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,int prioridad,String estadoFacturacion,int codigoSexo,HashMap centrosCosto,HashMap convenios)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		campos.put("prioridad", prioridad);
		campos.put("estadoFacturacion", estadoFacturacion);
		campos.put("codigoSexo", codigoSexo);
		campos.put("convenios", convenios);
		campos.put("centrosCosto", centrosCosto);
		return reporteEgresosEstanciasDao().obtenerDiagnosticosPrioridadEgresos(con, campos);
	}
	
	/**
	 * M�todo para obtener los datos del tipo reporte Dx de egreso por rango de edad
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String,Object> obtenerDatosReporteDxEgresoRangoEdad(Connection con,int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,String estadoFacturacion,int codigoSexo,HashMap centrosCosto,HashMap convenios,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("codigoInstitucion", codigoInstitucion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		campos.put("estadoFacturacion", estadoFacturacion);
		campos.put("codigoSexo", codigoSexo);
		campos.put("convenios", convenios);
		campos.put("centrosCosto", centrosCosto);
		
		return reporteEgresosEstanciasDao().obtenerDatosReporteDxEgresoRangoEdad(con, campos);
	}
	
	/**
	 * M�todo para obtener la consulta del tipo de reporte de total diagn�sticos de egreso
	 */
	public static String obtenerConsultaReporteTotalDxEgreso(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,String estadoFacturacion,int codigoSexo,int codigoCentroCosto,HashMap diagnosticosEgreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		campos.put("estadoFacturacion", estadoFacturacion);
		campos.put("codigoSexo", codigoSexo);
		campos.put("codigoCentroCosto", codigoCentroCosto);
		campos.put("diagnosticosEgreso", diagnosticosEgreso);
		return reporteEgresosEstanciasDao().obtenerConsultaReporteTotalDxEgreso(campos);
	}
	
	/**
	 * M�todo para obtener la consulta del reporte de la primeras N causas de morbilidad
	 */
	public static String obtenerConsultaReporteNPrimerasCausasMorbilidad(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,int prioridad,int codigoSexo,String estadoSalida)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("fechaInicial", fechaInicial);
		campos.put("fechaFinal", fechaFinal);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		campos.put("prioridad", prioridad);
		campos.put("codigoSexo", codigoSexo);
		campos.put("estadoSalida", estadoSalida);
		return reporteEgresosEstanciasDao().obtenerConsultaReporteNPrimerasCausasMorbilidad(campos);
	}
	
	/**
	 * M�todo para retornar la consulta del reporte de estancia promedio mensual pacienrtes egresados por rango
	 */
	public static String obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,int codigoSexo,String estadoFacturacion,HashMap diagnosticosEgreso,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion",codigoCentroAtencion);
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("codigoTipoPaciente",codigoTipoPaciente);
		campos.put("codigoSexo",codigoSexo);
		campos.put("estadoFacturacion",estadoFacturacion);
		campos.put("diagnosticosEgreso",diagnosticosEgreso);
		campos.put("codigoInstitucion",codigoInstitucion);
		return reporteEgresosEstanciasDao().obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(campos);
	}
	
	/**
	 * M�todo que arma la consulta para el reporte de pacientes egresados de pediatr�a por rango de edad
	 */
	public static String obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,int codigoSexo,String estadoFacturacion,HashMap diagnosticosEgreso,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion",codigoCentroAtencion);
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("codigoTipoPaciente",codigoTipoPaciente);
		campos.put("codigoSexo",codigoSexo);
		campos.put("estadoFacturacion",estadoFacturacion);
		campos.put("diagnosticosEgreso",diagnosticosEgreso);
		campos.put("codigoInstitucion",codigoInstitucion);
		return reporteEgresosEstanciasDao().obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(campos);
	}
	
	/**
	 * M�todo para obtener la consulta del reporte de estancia de pacientes mayores de N d�as
	 */
	public static String obtenerConsultaReporteEstanciaPacientesMayorNDias(int codigoCentroAtencion,String fechaInicial,String fechaFinal,int codigoViaIngreso,String codigoTipoPaciente,int codigoSexo,String tipoEgreso,int prioridad,String estadoSalida,HashMap convenios)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroAtencion",codigoCentroAtencion);
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("codigoTipoPaciente",codigoTipoPaciente);
		campos.put("codigoSexo",codigoSexo);
		campos.put("tipoEgreso",tipoEgreso);
		campos.put("prioridad",prioridad);
		campos.put("estadoSalida",estadoSalida);
		campos.put("convenios",convenios);
		
		return reporteEgresosEstanciasDao().obtenerConsultaReporteEstanciaPacientesMayorNDias(campos);
	}
	
	/**
	 * M�todo para realizar la consulta del reporte indicadores hospitalizaci�n
	 */
	public static ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> consultarIndicadoresHospitalizacion(Connection con, DtoFiltroReporteIndicadoresHospitalizacion dtoFiltro)
	{
		return reporteEgresosEstanciasDao().consultarIndicadoresHospitalizacion(con, dtoFiltro);
	}
}
