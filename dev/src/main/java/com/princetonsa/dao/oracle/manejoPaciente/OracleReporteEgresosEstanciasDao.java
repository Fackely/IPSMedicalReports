/*
 * Sep 03, 2008
 */
package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ReporteEgresosEstanciasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseReporteEgresosEstanciasDao;
import com.princetonsa.dto.manejoPaciente.DtoFiltroReporteIndicadoresHospitalizacion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoConsultaIndicadoresHospitalizacion;

/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Reporte Egresos y Estancias
 */
public class OracleReporteEgresosEstanciasDao implements ReporteEgresosEstanciasDao 
{
	/**
	 * M�todo para consultar los datos del reporte de resumen mensual egresos
	 */
	public HashMap<String, Object> obtenerDatosResumenMensualEgresos(Connection con,HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerDatosResumenMensualEgresos(con, campos);
	}
	
	/**
	 * M�todo para obtener la consulta del tipo de reporte de egresos por convenio
	 */
	public String obtenerConsultaReporteEgresosConvenio(HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerConsultaReporteEgresosConvenio(campos, DaoFactory.ORACLE);
	}
	
	/**
	 * M�todo para obtener la consulta del reporte de egresos por lugar de residencia
	 */
	public String obtenerConsultaReporteEgresosLugarResidencia(HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerConsultaReporteEgresosLugarResidencia(campos, DaoFactory.ORACLE);
	}
	
	/**
	 * M�todo para obtener los diagnosticos prioritarios del tipo de reporte 
	 * de diagn�stico de egreso por rango de edad
	 */
	public ArrayList<HashMap<String,Object>> obtenerDiagnosticosPrioridadEgresos(Connection con,HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerDiagnosticosPrioridadEgresos(con, campos, DaoFactory.ORACLE);
	}
	
	/**
	 * M�todo para obtener los datos del tipo reporte Dx de egreso por rango de edad
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String,Object> obtenerDatosReporteDxEgresoRangoEdad(Connection con,HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerDatosReporteDxEgresoRangoEdad(con, campos);
	}
	
	/**
	 * M�todo para obtener la consulta del tipo de reporte de total diagn�sticos de egreso
	 */
	public String obtenerConsultaReporteTotalDxEgreso(HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerConsultaReporteTotalDxEgreso(campos);
	}
	
	/**
	 * M�todo para obtener la consulta del reporte de la primeras N causas de morbilidad
	 */
	public String obtenerConsultaReporteNPrimerasCausasMorbilidad(HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerConsultaReporteNPrimerasCausasMorbilidad(campos, DaoFactory.ORACLE);
	}
	
	/**
	 * M�todo para retornar la consulta del reporte de estancia promedio mensual pacienrtes egresados por rango
	 */
	public String obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(campos);
	}
	
	/**
	 * M�todo que arma la consulta para el reporte de pacientes egresados de pediatr�a por rango de edad
	 */
	public String obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(campos);
	}
	
	/**
	 * M�todo para obtener la consulta del reporte de estancia de pacientes mayores de N d�as
	 */
	public String obtenerConsultaReporteEstanciaPacientesMayorNDias(HashMap campos)
	{
		return SqlBaseReporteEgresosEstanciasDao.obtenerConsultaReporteEstanciaPacientesMayorNDias(campos);
	}
	
	/**
	 * M�todo para realizar la consulta del reporte indicadores hospitalizaci�n
	 */
	public ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> consultarIndicadoresHospitalizacion(Connection con, DtoFiltroReporteIndicadoresHospitalizacion dtoFiltro)
	{
		return SqlBaseReporteEgresosEstanciasDao.consultarIndicadoresHospitalizacion(con, dtoFiltro);
	}
}
