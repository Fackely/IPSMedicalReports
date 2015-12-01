/*
 * @(#)SqlBasePacientesTriageUrgenciasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la los pacientes triage urgencias
 *
 * @version 1.0, Junio 2 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBasePacientesTriageUrgenciasDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBasePacientesTriageUrgenciasDao.class);
	
	
	/**
	 * Cadena Consulta al momento de entrar en la funcionalidad
	 */
	private static String listadoStr= "SELECT "+ 
		"t.consecutivo ||' '|| t.consecutivo_fecha AS numero_triage, "+ 
		"CASE WHEN t.primer_apellido IS NULL THEN '' ELSE  t.primer_apellido end ||' '|| CASE WHEN t.segundo_apellido IS NULL THEN '' ELSE t.segundo_apellido END ||' '|| CASE WHEN t.primer_nombre IS NULL THEN '' ELSE t.primer_nombre END ||' '|| CASE WHEN t.segundo_nombre IS NULL THEN '' ELSE t.segundo_nombre END AS nombre_paciente, "+ 
		"t.tipo_identificacion AS acronimo_tipo_id, "+ 
		"getnombretipoidentificacion(t.tipo_identificacion) AS nombre_tipo_id, "+ 
		"t.numero_identificacion, "+ 
		"ct.nombre AS calificacion_triage, "+ 
		"col.nombre AS nombre_color, "+ 
		"col.descripcion AS color, "+ 
		"t.fecha AS fecha_triage_formato_bd, to_char(t.fecha, 'DD/MM/YYYY') AS fecha_triage, "+ 
		"substr(t.hora, 1, 5) AS hora_triage, "+
		"CASE WHEN t.primer_apellido IS NULL THEN '' ELSE t.primer_apellido END AS primer_apellido_persona, "+ 
		"CASE WHEN t.segundo_apellido IS NULL THEN '' ELSE t.segundo_apellido END AS segundo_apellido_persona, "+
		"CASE WHEN t.primer_nombre IS NULL THEN '' ELSE t.primer_nombre END AS primer_nombre_persona, "+
		"CASE WHEN t.segundo_nombre IS NULL THEN '' ELSE t.segundo_nombre END AS segundo_nombre_persona, "+ 
		"CASE WHEN t.fecha_nacimiento IS NULL THEN '' ELSE to_char(t.fecha_nacimiento, 'DD/MM/YYYY') ||'' END AS fecha_nacimiento_persona," +
		"CASE WHEN t.accidente_trabajo = '"+ConstantesBD.acronimoSi+"' THEN 'Sí' ELSE 'No' END AS accidente_trabajo "+ 
		"FROM triage t "+ 
		"INNER JOIN destino_paciente dp ON (dp.consecutivo=t.destino and dp.indicador_admi_urg= "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
		"INNER JOIN categorias_triage ct ON (ct.consecutivo = t.categoria_triage) "+ 
		"INNER JOIN colores_triage col ON (ct.color=col.codigo) "+ 
		"WHERE "+ 
		"t.no_responde_llamado = "+ValoresPorDefecto.getValorFalseParaConsultas()+" AND "+
		"t.fecha BETWEEN (current_date - 1) AND current_date AND "+
		"t.numero_admision IS NULL AND "+ 
		"t.centro_atencion= ? AND "+ 
		"t.institucion = ? and "+ 
		"tieneCuentaUrgenciasAbierta(t.numero_identificacion ||'',t.tipo_identificacion ||'',"+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoViaIngresoUrgencias +")=0 "+ 
		"ORDER BY numero_triage";	
	
	/**
	 * Cadena Consulta al momento de generar la busuqeda avanzada por un rango de fechas determinado
	 */
	private static String listadoBusquedaAvanzadaStr= "SELECT "+ 
		"t.consecutivo ||' '|| t.consecutivo_fecha AS numero_triage, "+ 
		"CASE WHEN t.primer_apellido IS NULL THEN '' ELSE  t.primer_apellido end ||' '|| CASE WHEN t.segundo_apellido IS NULL THEN '' ELSE t.segundo_apellido END ||' '|| CASE WHEN t.primer_nombre IS NULL THEN '' ELSE t.primer_nombre END ||' '|| CASE WHEN t.segundo_nombre IS NULL THEN '' ELSE t.segundo_nombre END AS nombre_paciente, "+ 
		"t.tipo_identificacion AS acronimo_tipo_id, "+ 
		"getnombretipoidentificacion(t.tipo_identificacion) AS nombre_tipo_id, "+ 
		"t.numero_identificacion, "+ 
		"ct.nombre AS calificacion_triage, "+ 
		"col.nombre AS nombre_color, "+ 
		"col.descripcion AS color, "+ 
		"t.fecha AS fecha_triage_formato_bd, to_char(t.fecha, 'DD/MM/YYYY') AS fecha_triage, "+ 
		"substr(t.hora, 1, 5) AS hora_triage, "+
		"CASE WHEN t.primer_apellido IS NULL THEN '' ELSE t.primer_apellido END AS primer_apellido_persona, "+ 
		"CASE WHEN t.segundo_apellido IS NULL THEN '' ELSE t.segundo_apellido END AS segundo_apellido_persona, "+
		"CASE WHEN t.primer_nombre IS NULL THEN '' ELSE t.primer_nombre END AS primer_nombre_persona, "+
		"CASE WHEN t.segundo_nombre IS NULL THEN '' ELSE t.segundo_nombre END AS segundo_nombre_persona, "+ 
		"CASE WHEN t.fecha_nacimiento IS NULL THEN '' ELSE to_char(t.fecha_nacimiento, 'DD/MM/YYYY') ||'' END AS fecha_nacimiento_persona," +
		"CASE WHEN t.accidente_trabajo = '"+ConstantesBD.acronimoSi+"' THEN 'Sí' ELSE 'No' END AS accidente_trabajo "+ 
		"FROM triage t "+ 
		"INNER JOIN destino_paciente dp ON (dp.consecutivo=t.destino and dp.indicador_admi_urg= "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
		"INNER JOIN categorias_triage ct ON (ct.consecutivo = t.categoria_triage) "+ 
		"INNER JOIN colores_triage col ON (ct.color=col.codigo) ";
		
	/**
	 * listado de pacientes triage urgencias
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listado(	Connection con, 
	        						String codigoCentroAtencion,
	        						int codigoInstitucion
								 )
	{
	    try
		{
	        
	        PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setString(1, codigoCentroAtencion);
			cargarStatement.setInt(2, codigoInstitucion);
			logger.info("\n====>Consulta Empezar: "+listadoStr);
			logger.info("\n====>Centro Atencion: "+codigoCentroAtencion);
			logger.info("\n====>Institucion: "+codigoInstitucion);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			cargarStatement.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la listado: SqlBasePacientesTriageUrgenciasDao "+e.toString());
			return new HashMap();
		}
	}

	/**
	 * listado de pacientes triage urgencias
	 * para un rango de fechas determinado
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listadoBusquedaAvanzada(Connection con, String codigoCentroAtencion, int codigoInstitucion, String fechaInicial, String fechaFinal)
	{
		try
		{
			String consulta=listadoBusquedaAvanzadaStr;
			consulta=consulta+
					"WHERE "+ 
						"t.no_responde_llamado = "+ValoresPorDefecto.getValorFalseParaConsultas()+" AND "+
						"to_char(t.fecha, 'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' AND "+
						"t.numero_admision IS NULL AND "+ 
						"t.centro_atencion= "+codigoCentroAtencion+" AND "+ 
						"t.institucion = "+codigoInstitucion+" and "+ 
						"tieneCuentaUrgenciasAbierta(t.numero_identificacion ||'',t.tipo_identificacion ||'',"+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoViaIngresoUrgencias +")=0 "+ 
						"ORDER BY numero_triage";
	        PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        logger.info("\n====>Consulta Avanzada: "+consulta);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			cargarStatement.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la listado: SqlBasePacientesTriageUrgenciasDao "+e.toString());
			return new HashMap();
		}
	}
	
}