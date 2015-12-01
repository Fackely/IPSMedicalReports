/*
 * Created on 22/07/2005
 *
 * Mercury Todo los Derechos Reservados
 */
package com.mercury.dao.sqlbase.odontologia;

import java.sql.*;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;

/**
 * @author Alejo	
 *
 * Mercury (Parquesoft - Manizales)
 */
public class SqlBaseValoracionOdontologiaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseValoracionOdontologiaDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar 
	 * una entrada en la tabla valoraciones odontologia. (Esto hace parte de la inserción 
	 * de una valoracion de odontología, junto a la inserción de examen clinico, 
	 * análisis, examen endodontico y odontograma). 
	 */
	private final static String insertarValoracionOdontologiaStr="INSERT INTO val_odontologia (num_valoracion) values "+
																							"(?)";
																							
	/**
	 * Cadena constante con el <i>statemente</i> para cargar una valoracion de odontologia
	 */	
	private final static String consultarValoracionOdontologiaStr="SELECT num_valoracion as numValoracion "+
																		"FROM val_odontologia where num_valoracion=?";
    
    public static int insertar(Connection con, int numValoracion) throws SQLException
	{
	    try
	    {
	        PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseValoracionOdontologiaDao.insertarValoracionOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarStatement.setInt(1, numValoracion);
			return insertarStatement.executeUpdate();
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e);
	        throw e;
	    }
	}
	
	public static ResultSetDecorator consultar(Connection con, int numValoracion) throws SQLException
	{
	    try
	    {
	        	PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseValoracionOdontologiaDao.consultarValoracionOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        	consultarStatement.setInt(1, numValoracion);
	        	return new ResultSetDecorator(consultarStatement.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e);
	        throw e;
	    }
	}
}
