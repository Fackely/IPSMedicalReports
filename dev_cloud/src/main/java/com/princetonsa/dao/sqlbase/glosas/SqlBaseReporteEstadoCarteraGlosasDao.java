package com.princetonsa.dao.sqlbase.glosas;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlBaseReporteEstadoCarteraGlosasDao
{
	/**
	 * Manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReporteEstadoCarteraGlosasDao.class);

	public static HashMap ejecutarQuery(String consulta) 
	{
		Connection con=null;
		con= UtilidadBD.abrirConexion();
		
		HashMap result = new HashMap ();
		result.put("numRegistros", 0); 
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			logger.info("\n\nla consulta:: "+ps);
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e)
		{
			logger.info("\n problema ejecutando query reporte -->"+e);
		}		
		return result;
	}
}