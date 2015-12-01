package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Antecedente Mórbido Quirúrgico
 *
 *	@version 1.0, Mar 31, 2004
 */
public class SqlBaseAntecedenteMorbidoQuirurgicoDao 
{
	private static Logger logger = Logger.getLogger(SqlBaseAntecedenteMorbidoQuirurgicoDao.class);
	
															
	private static String existeStr = "SELECT COUNT(*) AS antecedente "
														 + "FROM ant_morbidos_quirurgicos "
														 + "WHERE codigo_paciente = ? "
														 + "AND codigo = ? ";
														
															
															
	
	

	public static  ResultadoBoolean existeAntecedente(	Connection con, 
																	int codigoPaciente, 
																	int codigo)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		ResultadoBoolean result=null;
		try
		{	
			pst =  con.prepareStatement(existeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigo);
			
			rs = pst.executeQuery();
			if( rs.next() )
			{
				int numFilas = rs.getInt("antecedente");
				
				if( numFilas <= 0 )
				{
					result= new ResultadoBoolean(false);
				}
				else
				{
					result= new ResultadoBoolean(true);
				}
			}
			else
			{
				result=new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch(Exception e)
	    {
		    logger.warn("ERROR existeAntecedente", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	    return result;
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteMorbidoQuirurgicoDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean insertarTransaccional(	Connection con,
																		int codigoPaciente,
																		int codigo,
																		String nombre,
																		String fecha,
																		String causa,
																		String complicaciones,
																		String recomendaciones,
																		String observaciones,
																		String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo empezar la transacción");
		    }
		}

		try
		{
			String insertarStr = "INSERT INTO ant_morbidos_quirurgicos "
				+ "(codigo_paciente, "
				+ "codigo, "
				+ "nombre, "
				+ "fecha, "
				+ "causa, "
				+ "complicaciones, "
				+ "recomendaciones, "
				+ "observaciones, "
				+ "fecha_ant, "
				+ "hora_ant) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			
			PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			insertar.setInt(1, codigoPaciente);
			insertar.setInt(2, codigo);
			insertar.setString(3, nombre);
			insertar.setString(4, fecha);
			insertar.setString(5, causa);
			insertar.setString(6, complicaciones);
			insertar.setString(7, recomendaciones);
			insertar.setString(8, observaciones);
					
			int insert = insertar.executeUpdate();
			insertar.close();		
			if( insert == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando el antecedente morbido quirurgico "+nombre+", para el paciente : "+codigoPaciente+". \n"+e);
		    myFactory.abortTransaction(con);
		    return new ResultadoBoolean (false, "Transacción abortada");
		}
				
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}		
		return new ResultadoBoolean(true);				

	}

	/**
	 * @see com.princetonsa.dao.AntecedenteMorbidoQuirurgicoDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con,
																			int codigoPaciente,
																			int codigo,
																			String fecha,
																			String causa,
																			String complicaciones,
																			String recomendaciones,
																			String observaciones,
																			String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo empezar la transacción");
		    }
		}

		try
		{
			String modificarStr = "UPDATE ant_morbidos_quirurgicos "
				  + "SET fecha = ?, "
				  + "causa = ?, "
				  + "complicaciones = ?, "
				  + "recomendaciones = ?, "
				  + "observaciones = ?, "
				  + "fecha_ant='"+Utilidades.capturarFechaBD()+"', hora_ant='"+UtilidadFecha.getHoraActual()+"' "
				  + "WHERE codigo_paciente = ? "
				  + "AND codigo = ? ";
			
			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			modificar.setInt(6, codigoPaciente);
			modificar.setInt(7, codigo);
			modificar.setString(1, fecha);
			modificar.setString(2, causa);
			modificar.setString(3, complicaciones);
			modificar.setString(4, recomendaciones);
			modificar.setString(5, observaciones);
					
			int update = modificar.executeUpdate();
			modificar.close();		
			if( update == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.error("Problemas modificando el antecedente morbido quirurgico código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
		    myFactory.abortTransaction(con);
		    return new ResultadoBoolean (false, "Transacción abortada");
		}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);

	}


}
