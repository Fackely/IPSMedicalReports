/*
 * @(#)SqlBasePacientesUrgenciasPorValorarDao.java
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

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la los pacientes de urgencias por valorar
 *
 * @version 1.0, Junio 3 / 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class SqlBasePacientesUrgenciasPorValorarDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBasePacientesUrgenciasPorValorarDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar todos los datos de los pacientes de urgencias por valorar
	 */
	private static final String consultarPacientesUrgPorValorarStr = " SELECT t.consecutivo as consecutivotriage,  " +
																	 " c.codigo_paciente as codigopaciente, " +
																	 " t.consecutivo_fecha as fechapk, " +
																	 " getnombrepersona(c.codigo_paciente) as nombrepaciente, " +
																	 " per.tipo_identificacion as tipoid, " +
																	 " per.numero_identificacion as numeroid, " +
																	 " case when t.fecha is null then '' else t.fecha||'' end as fechatriage, " +
																	 " case when t.hora is null then '' else t.hora||'' end as horatriage, " +
																	 " to_char(au.fecha_admision, '"+ConstantesBD.formatoFechaAp+"') as fechaadmision, " +
																	 " au.hora_admision||'' as horaadmision, " +
																	 " ct.consecutivo as codigoategoria, " +
																	 " case when ct.nombre is null then ''  else ct.nombre end as calificaciontriage, " +
																	 " case when colt.nombre is null then '' else colt.nombre end as nombrecolor," +
																	 " t.sala_espera as codigosala, " +
																	 " case when sl.descripcion is null or sl.descripcion = '' then ''  else sl.descripcion end as sala, " +
																	 " coalesce(c.observaciones,'') as observaciones " +
																	 " FROM admisiones_urgencias au " +
																	 " LEFT OUTER JOIN triage t ON(t.consecutivo=au.consecutivo_triage AND t.consecutivo_fecha=au.consecutivo_triage_fecha) " +
																	 " LEFT OUTER JOIN salas sl ON(t.sala_espera=sl.consecutivo) " +
																	 " INNER JOIN cuentas c ON(au.cuenta=c.id) " +
																	 " INNER JOIN personas per ON(c.codigo_paciente=per.codigo) " +
																	 " INNER JOIN solicitudes sol ON(c.id=sol.cuenta) " +
																	 " INNER JOIN centros_costo cc ON(c.area=cc.codigo) " +
																	 " LEFT OUTER JOIN categorias_triage ct ON(t.categoria_triage=ct.consecutivo) " +
																	 " LEFT OUTER JOIN colores_triage colt ON(ct.color=colt.codigo) " +
																	 " WHERE c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias +
																	 " AND c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaActiva +
																	 " AND (sol.tipo="+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" AND sol.estado_historia_clinica ="+ConstantesBD.codigoEstadoHCSolicitada+") " +
																	 " AND cc.centro_atencion = ? " +
																	 " AND cc.institucion = ?  " +
																	 " ORDER BY au.fecha_admision ASC,  au.hora_admision ASC";
	
	
	/**
	 * Método para consultar los todos los datos de los pcientes de triage de via de ingreso de
	 * urgencias pendientes de valoracion inicial de urgencias
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarPacientesUrgPorValorar(Connection con, int codigoCentroAtencion, int institucion) throws SQLException
	{
		try
		{
			logger.info("-->"+consultarPacientesUrgPorValorarStr);
			logger.info("-->"+codigoCentroAtencion);
			logger.info("-->"+institucion);
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarPacientesUrgPorValorarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoCentroAtencion);
			cargarStatement.setInt(2, institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()),true, false);
			cargarStatement.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error(e+" Error en consultarPacientesUrgPorValorar [SqlBasePacientesUrgenciasPorValorarDao]: ",e);
			return new HashMap();
		}
	}
	
}

