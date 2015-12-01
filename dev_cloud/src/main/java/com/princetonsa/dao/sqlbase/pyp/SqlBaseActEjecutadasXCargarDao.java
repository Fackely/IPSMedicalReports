/*
 * Nov 14, 2006
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad Actividades PYP Ejecutadas por Cargar
 */
public class SqlBaseActEjecutadasXCargarDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseActEjecutadasXCargarDao.class);
	
	/**
	 * Cadena que consulta ordenes ambulatorias pendientes de cargar por convenio
	 */
	private static final String consultarOrdenesAmbXConvenioStrOld = "SELECT "+ 
		"'true' AS seleccion, "+ 
		"c.id AS id_cuenta, "+
		"sc.convenio AS codigo_convenio, "+
		"c.estado_cuenta AS estado_cuenta, "+
		"oa.codigo AS codigo_orden, "+
		"oa.consecutivo_orden AS orden, "+
		"to_char(oa.fecha,'DD/MM/YYYY') AS fecha, "+
		"p.tipo_identificacion || ' ' || p.numero_identificacion AS identificacion, "+
		"oa.codigo_paciente AS codigo_paciente, "+
		"getnombrepersona(oa.codigo_paciente) AS paciente, "+
		"getnombreusuario2(oa.usuario_solicita) AS profesional, "+
		"getnombreespecialidad(oa.especialidad_solicita) AS especialidad, " +
		"roa.numero_sol_ref AS numero_solicitud "+
		"FROM cuentas c " +
		"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso) "+ 
		"INNER JOIN solicitudes s ON(s.cuenta=c.id) "+ 
		"INNER JOIN ref_orden_ambulatoria roa ON(roa.numero_sol_ref=s.numero_solicitud) "+ 
		"INNER JOIN ordenes_ambulatorias oa ON(oa.codigo=roa.codigo_orden) "+ 
		"INNER JOIN personas p ON(p.codigo=oa.codigo_paciente) ";
	
	/**
	 * Cadena que consulta ordenes ambulatorias pendientes de cargar por convenio
	 */
	private static final String consultarOrdenesAmbXConvenioStr = "SELECT "+ 
			"'true' AS seleccion, "+ 
			"c.id AS id_cuenta, "+
			"sc.convenio AS codigo_convenio, "+
			"c.estado_cuenta AS estado_cuenta, "+
			"oa.codigo AS codigo_orden, "+
			"oa.consecutivo_orden AS orden, "+
			"to_char(oa.fecha,'DD/MM/YYYY') AS fecha, "+
			"p.tipo_identificacion || ' ' || p.numero_identificacion AS identificacion, "+
			"oa.codigo_paciente AS codigo_paciente, "+
			"getnombrepersona(oa.codigo_paciente) AS paciente, "+
			"getnombreusuario2(oa.usuario_solicita) AS profesional, "+
			"getnombreespecialidad(oa.especialidad_solicita) AS especialidad, " +
			"roa.numero_sol_ref AS numero_solicitud "+
		"FROM ordenes_ambulatorias oa " +
		"INNER JOIN ingresos i ON (oa.ingreso=i.id) " +
		"INNER JOIN sub_cuentas sc ON (sc.ingreso=i.id AND sc.nro_prioridad=1) " +
		"INNER JOIN personas p ON (p.codigo=oa.codigo_paciente) " +
		"INNER JOIN ref_orden_ambulatoria roa ON(roa.codigo_orden=oa.codigo) " +
		"LEFT OUTER JOIN solicitudes s ON (s.numero_solicitud=roa.numero_sol_ref) " +
		"LEFT OUTER JOIN cuentas c on (s.cuenta=c.id) ";
			
	/**
	 * Cadena que consulta el detalle de una orden ambulatoria
	 */
	private static final String consultarDetalleOrdenAmbStr = "SELECT "+ 
			"oa.codigo AS codigo, "+
			"oa.consecutivo_orden AS orden, "+
			"CASE WHEN oa.urgente ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Sí' ELSE 'No' END As urgente, "+
			"getnomcentroatencion(oa.centro_atencion_solicita) AS centro_atencion, "+
			"oa.codigo_paciente as codigo_paciente, "+
			"getnombreusuario2(oa.usuario_solicita) AS profesional, "+
			"oa.usuario_solicita AS login_profesional, "+
			"getnombreespecialidad(oa.especialidad_solicita) AS especialidad, " +
			"oa.especialidad_solicita, "+
			"to_char(oa.fecha,'DD/MM/YYYY') AS fecha, "+
			"oa.hora AS hora, "+
			"CASE WHEN oa.observaciones IS NULL THEN '' ELSE oa.observaciones END AS observaciones, "+
			"doas.servicio AS codigo_servicio, "+
			"'(' || getcodigoespecialidad(doas.servicio) || '-' || doas.servicio || ') ' || getnombreservicio(doas.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
			"gettiposervicio(doas.servicio) AS codigo_tipo_servicio, "+
			"getnomtiposervicio(doas.servicio) AS nombre_tipo_servicio, "+
			"doas.cantidad AS cantidad, "+
			"CASE WHEN roa.acronimo_diagnostico IS NULL THEN '' ELSE roa.acronimo_diagnostico END As acronimo_diagnostico, "+
			"CASE WHEN roa.cie_diagnostico IS NULL THEN 0 ELSE roa.cie_diagnostico END AS cie_diagnostico, "+
			"CASE WHEN roa.acronimo_diagnostico IS NULL THEN '' ELSE getnombrediagnostico(roa.acronimo_diagnostico,roa.cie_diagnostico) END As nombre_diagnostico, "+ 
			"CASE WHEN roa.finalidad IS NULL THEN '' ELSE roa.finalidad END AS codigo_fin_consulta, "+
			"CASE WHEN roa.finalidad IS NULL THEN '' ELSE getnomfinalidadconsulta(roa.finalidad) END AS nombre_fin_consulta, "+
			"CASE WHEN doas.finalidad IS NULL THEN 0 ELSE doas.finalidad END AS codigo_fin_servicio, "+
			"CASE WHEN doas.finalidad IS NULL THEN '' ELSE getnomfinalidadservicio(doas.finalidad) END AS nombre_fin_servicio, "+
			"CASE WHEN roa.causa_externa IS NULL THEN 0 ELSE roa.causa_externa END as codigo_causa_externa, "+
			"CASE WHEN roa.causa_externa IS NULL THEN '' ELSE getnombrecausaexterna(roa.causa_externa) END as nombre_causa_externa, "+
			"CASE WHEN roa.resultados IS NULL THEN '' ELSE roa.resultados END as resultados, " +
			"m.ocupacion_medica, " +
			"oa.centro_costo_solicita," +
			"m.codigo_medico "+ 
		"FROM " +
			"ordenes_ambulatorias oa "+ 
		"INNER JOIN " +
			"det_orden_amb_servicio doas ON(doas.codigo_orden=oa.codigo) "+ 
		"INNER JOIN " +
			"ref_orden_ambulatoria roa ON(roa.codigo_orden=doas.codigo_orden) " +
		"INNER JOIN " +
			"usuarios u ON (u.login=oa.usuario_solicita) " +
		"INNER JOIN " +
			"medicos m ON (m.codigo_medico=u.codigo_persona) "+ 
		"WHERE " +
			"oa.codigo = ?";
	

	
	/**
	 * Método implementado para cargar las ordenes ambulatorias pendientes por cargar de un convenio específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarOrdenesAmbXConvenio(Connection con,HashMap campos)
	{
		try
		{
			// Se cambia la estructura de la consulta ya que cuando se ejecutan actividades pyp con la cuenta cerrada
			// ya no se asocia una solicitud en la tabla ref_ordem_ambulatoria
			// String consulta = consultarOrdenesAmbXConvenioStrOld + " WHERE ";
			String consulta = consultarOrdenesAmbXConvenioStr + " WHERE ";
			
			
			//Se verifica si hay convenio
			int codigoConvenio = Integer.parseInt(campos.get("codigoConvenio").toString());
			if(codigoConvenio>0)
				consulta += " sc.convenio = "+codigoConvenio+" AND ";
			
			consulta+= " oa.estado = "+ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+" AND " +
				"oa.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND " +
				"oa.consulta_externa = "+ValoresPorDefecto.getValorTrueParaConsultas()+
				" ORDER BY codigo_orden ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			logger.info("SQL / consultarOrdenesAmbXConvenioStr / "+consulta);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarOrdenesAmbXConvenio de SqlBaseActEjecutadasXCargarDao: "+e);
			return null;
		}
		
	}
	
	/**
	 * Método implementado para cargar información detallada de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarDetalleOrdenAmb(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDetalleOrdenAmbStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("codigoOrden")+""));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),false,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDetalleOrdenAmb de SqlBaseActEjecutadasXCargarDao:  "+e);
			return null;
		}
	}
}
