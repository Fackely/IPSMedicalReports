/*
 * Mayo 16, 2007
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;


/**
 * @author Sebastián Gómez
 * 
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a UtilidadBD
 *
 */
public class SqlBaseUtilidadesBDDao 
{
	/**
	 * Para hacer logs de esta clase.
	 */
	private static Logger logger = Logger.getLogger(SqlBaseUtilidadesBDDao.class);

	
	/**
	 * Metodo que indica si una consulta esta o no bloqueada (select for update) en caso de que sea verdadero entonces retorna true,
	 * POR FAVOR SOLO USAR ESTE METODO A NIVEL DEL DAO
	 * @param consulta
	 * @return
	 */
	public static boolean estaConsultaBloqueada(String consulta)
	{
		String consultaFun= "SELECT getEsConsultaBloqueada('"+consulta+"') as consultabloqueda";
		logger.info("\n\n estaConsultaBloqueada->"+consultaFun+"\n\n");
		boolean retorna= false;
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaFun,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getBoolean("consultabloqueda");
			}
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
            logger.info("Se presento un error en  estaConsultaBloqueada: "+e);
            e.printStackTrace();
		}
		
		logger.info("NO EVALUO SI LA CONSULTA ESTA O NO BLOQUEDA");
		return retorna;
	}
	
	
	/**
	 * Metodo que devuelve la cantidad de veces
	 * que es utilizado un key en una tabla
	 * @param connection
	 * @param tabla
	 * @param nombreKey
	 * @param valuekey
	 * @return
	 */
	public static int estaUtilizadoEnTabla (Connection connection,String tabla,String nombreKey, int valuekey)
	{
		String cadena = "SELECT COUNT (*) FROM "+tabla+" WHERE "+nombreKey+"="+valuekey;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e) 
		{
		  logger.info("\n problema al consultar los datos en la tabla "+tabla+" para saber si esta siendo utilizado el registro "+valuekey+" "+e);		}
		
		return 0;
	}


	/**
	 * Metodo que cierra:
	 * 1. PreparedStatementDecorator
	 * 2. ResultSetDecorator
	 * 3. Connection
	 * Si falla una de los tres item imprime una exception SQLException
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param ps
	 * @param rs
	 * @param con
	 */
	public static void cerrarObjetosPersistencia(Statement ps,ResultSet rs,Connection con)
	{
	         
		if (con != null) {
			UtilidadBD.closeConnection(con);
		}

		try {

			if (rs != null) {
				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {

			if (ps != null) {
				ps.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	  }
	
	
	
}
