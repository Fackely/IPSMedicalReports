/*
 * Created on Aug 20, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.EstanciaAutomaticaDao;
import com.princetonsa.dao.sqlbase.SqlBaseEstanciaAutomaticaDao;

/**
 * @author sebacho
 *
 * Implementación Postgres de las funciones de acceso a la fuente de datos
 * para la Estancia Automática
 */
public class PostgresqlEstanciaAutomaticaDao implements EstanciaAutomaticaDao {

	/**
	 * Cadena que consulta el último traslado cama de la cuenta en una fecha
	 * menor al día de estancia
	 */
	private static final String consultaTrasladosCuentaSinDiaEstanciaStr="SELECT "+ 
		"tc.codigo_nueva_cama AS cama,"+
		"c.centro_costo AS centro_costo," +
		"cc.tipo_entidad_ejecuta AS tipo_entidad_ejecuta, "+
		"c.es_uci AS es_uci "+ 
		"FROM traslado_cama tc, camas1 c, centros_costo cc "+  
		"WHERE "+ 
		"c.codigo=tc.codigo_nueva_cama AND "+ 
		"tc.fecha_asignacion<? AND "+ 
		"tc.cuenta=? ";
	
	/**
	 * Cadena que consulta los tipos de monitoreo de los encabezados históricos
	 * de la orden médica, de acuerdo a la cuenta del paciente y a fechas ordenes
	 * menores al día de la estancia
	 */
	private static final String consultaTiposMonitoreoSinDiaEstanciaStr="SELECT "+ 
		"t.codigo AS tipo_monitoreo,"+
		"t.prioridad_cobro AS prioridad,"+
		"t.servicio AS servicio "+  
		"FROM ordenes_medicas om "+ 
		"INNER JOIN encabezado_histo_orden_m eh ON(om.codigo=eh.orden_medica) "+ 
		"INNER JOIN orden_tipo_monitoreo ot ON(ot.codigo_histo_encabezado=eh.codigo) "+ 
		"INNER JOIN tipo_monitoreo t ON(t.codigo=ot.tipo_monitoreo) "+
		"WHERE om.cuenta=? AND eh.fecha_orden<? " +
		"ORDER BY eh.fecha_orden DESC, eh.hora_orden DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
	
	/**
	 * Cadena que inserta el LOG Base de Datos del proceso de 
	 * generación de Estancia Automática
	 */
	private static final String insertarLogEstanciaAutomaticaStr="INSERT INTO estancia_automatica " +
			"(codigo,tipo,fecha_inicial_estancia,fecha_final_estancia,fecha_grabacion,hora_grabacion,usuario,institucion,paciente,area,inconsistencia,reporte,centro_atencion,ind_gen_est) " +
			"VALUES " +
			"(nextval('seq_estancia_automatica'),?,?,?,CURRENT_DATE,CURRENT_TIME,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para consultar lso registros del LOG BD de Estancia Automática
	 */
	private static final String consultaLogEstanciaAutomaticaStr="SELECT "+ 
		"t.nombre AS tipo,"+
		"to_char(e.fecha_inicial_estancia,'DD/MM/YYYY') AS fecha_inicial,"+
		"to_char(e.fecha_final_estancia,'DD/MM/YYYY') AS fecha_final,"+
		"to_char(fecha_grabacion,'DD/MM/YYYY') ||' '|| to_char(hora_grabacion,'HH24:MI') AS fecha_grabacion,"+
		"getnomcentrocosto(e.area) AS area ,"+
		"administracion.getnombremedico(e.paciente) AS paciente,"+
		"e.usuario AS usuario,"+
		"CASE WHEN e.inconsistencia="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS inconsistencia,"+
		"e.reporte AS reporte, " +
		"getIntegridadDominio(e.ind_gen_est) as ind_gen_est "+ 
		"FROM "+ 
		"estancia_automatica e " +
		"inner join tipos_estancia t on(t.codigo=e.tipo) "+
		"WHERE "+
		"e.fecha_grabacion between ? AND ? ";
	/**
	 * Método usado para consultar las cuentas a las cuales se les va a
	 * generar una solicitud de estancia y su cargo, en la opción
	 * de Estancia Automática Por Área
	 * @param con
	 * @param centroCosto
	 * @param fechaEstancia
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultaCuentasEstanciaPorArea(Connection con,String fechaEstancia,int centroAtencion, int institucion,int codigoCentroCosto)
	{
		return SqlBaseEstanciaAutomaticaDao.consultaCuentasEstanciaPorArea(con,fechaEstancia,centroAtencion, institucion, codigoCentroCosto);
	}
	
	/**
	 * Método que consulta los traslados cama de la cuenta que
	 * va a generar solicitud de estancia
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @return
	 */
	public HashMap consultarTrasladosCuenta(Connection con,int idCuenta,String fechaEstancia,int centroCosto)
	{
		String consulta=consultaTrasladosCuentaSinDiaEstanciaStr;
		if(centroCosto!=0){
			consulta+=" AND c.centro_costo="+centroCosto;
		}else{
			consulta+=" AND c.centro_costo = cc.codigo";
		}
			
		consulta+=" ORDER BY tc.fecha_asignacion DESC,tc.hora_asignacion DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
		return SqlBaseEstanciaAutomaticaDao.consultarTrasladosCuenta(con,idCuenta,fechaEstancia,centroCosto,consulta);
	}
	
	/**
	 * Método usado para consultar los tipos de monitoreo de una cuenta 
	 * teniendo como referencia de búsqueda la fecha de estancia
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @return
	 */
	public HashMap consultarTiposMonitoreo(Connection con,int idCuenta,String fechaEstancia)
	{
		return SqlBaseEstanciaAutomaticaDao.consultarTiposMonitoreo(con,idCuenta,fechaEstancia,consultaTiposMonitoreoSinDiaEstanciaStr);
	}
	
	/**
	 * Metodo que consulta el servicio del cargo en la tabla
	 * servicios_cama
	 * @param con
	 * @param cama
	 * @param tipoMonitoreo
	 * @return
	 */
	public int consultarServicioCama(Connection con,int cama,int tipoMonitoreo)
	{
		return SqlBaseEstanciaAutomaticaDao.consultarServicioCama(con,cama,tipoMonitoreo);
	}
	
	/**
	 * Método usado para registrar el LOG tipo Base de Datos del proceso
	 * de generación de Estancia Automática
	 * @param con
	 * @param tipo
	 * @param fechaInicialEstancia
	 * @param fechaFinalEstancia
	 * @param usuario
	 * @param institucion
	 * @param area
	 * @param paciente
	 * @param centroAtencion
	 * @param inconsistencia
	 * @param reporte
	 * @return
	 */
	public int insertarLogEstanciaAutomatica(Connection con,int tipo,String fechaInicialEstancia,String fechaFinalEstancia,String usuario,int institucion,int area,int paciente,int centroAtencion,boolean inconsistencia,String reporte, String indica)
	{
		return SqlBaseEstanciaAutomaticaDao.insertarLogEstanciaAutomatica(con,tipo,fechaInicialEstancia,fechaFinalEstancia,usuario,institucion,area,paciente,centroAtencion,inconsistencia,reporte,insertarLogEstanciaAutomaticaStr, indica);
	}
	
	/**
	 * Método que consulta y verifica que sobre la cuenta se
	 * pueda generar estancia automática para el día asignado.
	 * En el caso de que no se pueda generar estancia para ese día
	 * el método retorna un HashMap vacío
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public HashMap consultaCuentaEstanciaPorPaciente(Connection con,int idCuenta,String fechaEstancia,int institucion)
	{
		return SqlBaseEstanciaAutomaticaDao.consultaCuentaEstanciaPorPaciente(con,idCuenta,fechaEstancia,institucion);
	}
	
	/**
	 * Método usaod para consultar los registros del registro LOG
	 * de Estancia Automática
	 * @param con
	 * @param anio
	 * @param mes
	 * @param area
	 * @param tipo
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultaLogEstanciaAutomatica(Connection con,int anio,int mes,int area,int tipo,String usuario,int centroAtencion, String indica)
	{
		return SqlBaseEstanciaAutomaticaDao.consultaLogEstanciaAutomatica(con,anio,mes,area,tipo,usuario,centroAtencion,consultaLogEstanciaAutomaticaStr, indica);
	}
	
	/**
	 * Método que consulta el numero de estancias generadas para una cuenta 
	 * en un rango de fechas determinado
	 * @param con
	 * @param campos
	 * @return
	 */
	public int numeroEstanciasPaciente(Connection con,HashMap campos)
	{
		return SqlBaseEstanciaAutomaticaDao.numeroEstanciasPaciente(con,campos);
	}

}
