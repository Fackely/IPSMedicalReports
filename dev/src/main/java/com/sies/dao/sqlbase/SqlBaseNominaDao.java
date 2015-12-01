package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.sies.mundo.UtilidadSiEs;

import util.UtilidadBD;

public class SqlBaseNominaDao
{
	static Logger logger=Logger.getLogger(SqlBaseNominaDao.class);
	
	/**
	 * Consulta los tipos de vinculación
	 * @return Collection<HashMap<String, Object>>
	 */
	public static Collection<HashMap<String, Object>> listarTiposVinculacion(Connection con)
	{
		String sentenciaSql;
		if(UtilidadSiEs.esAxioma())
		{
			sentenciaSql="SELECT codigo AS codigo, nombre AS nombre FROM administracion.tipos_vinculacion ORDER BY nombre";
		}
		else
		{
			sentenciaSql="SELECT codigo AS codigo, nombre AS nombre FROM tipos_vinculacion ORDER BY nombre";
		}
		try
		{
			PreparedStatement stm=con.prepareStatement(sentenciaSql);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		} catch (SQLException e)
		{
			logger.error("Error consultando los tipos de vinculación: "+e);
		}
		return null;
	}

}
