/*
 * Created on 31-mar-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.IdentificadoresExcepcionesSql;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author juanda
 *
 */
public class SqlBaseAntecedentesTransfusionalesDao
{

	private static Logger logger = Logger.getLogger(SqlBaseAntecedentesTransfusionalesDao.class);

	private static final String existeAntecedentePacienteStr = "SELECT count(*)  as antecedentes "
																						 + "FROM antecedentes_pacientes "
																						 + "WHERE codigo_paciente=? ";

	private static final String insertarAntecedentePacienteStr = "INSERT INTO antecedentes_pacientes " 
																						   + "(codigo_paciente) "
																						   + "VALUES (?) ";

	private static final String existenAntecedentesTransfusionalesStr 	= "SELECT count(*)  as antecedentes "
																										+ "FROM antece_transfusionales "
																										+ "WHERE codigo_paciente=? " ;

	private static final String consultarAntecedentesTransfusionalesStr 	= "SELECT observaciones as observaciones, fecha as fecha, hora as hora "
																											+ "FROM antece_transfusionales "
																											+ "WHERE codigo_paciente=? " ;								  


	//private static final String modificarAntecedentesTransfusionalesStr 	= "UPDATE antece_transfusionales "
//																												+ "SET observaciones = ? "
	//																											+ "WHERE codigo_paciente=? " ;

	private static final String consultarTransfusionesStr	= "SELECT codigo AS codigo, "
																					+ "componente_transfundido AS componente, "
																					+ "fecha_transfusion AS fecha, "
																					+ "causa AS causa, "
																					+ "lugar AS lugar, "
																					+ "edad_paciente AS edad, "
																					+ "donante AS donante, "
																					+ "observaciones AS observaciones, fecha as fecha_ant, hora as hora "
																					+ "FROM ant_transfusionales "																															 
																					+ "WHERE codigo_paciente=? " ;		

	public static ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator existeAntecedentePaciente =  new PreparedStatementDecorator(con.prepareStatement(existeAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			existeAntecedentePaciente.setInt(1, codigoPaciente);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedentePaciente.executeQuery());
			
			if( resultado.next() )
			{
				int numFilas = resultado.getInt("antecedentes");
				
				if( numFilas <= 0 )
				{
					return new ResultadoBoolean(false);
				}
				else
				{
					return new ResultadoBoolean(true);
				}
			}
			else
			{
				logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch( SQLException e )
		{
			logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
		}	
	}

	public static ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator insertarAntecedenteGeneral =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarAntecedenteGeneral.setInt(1, codigoPaciente);
			
			int insert = insertarAntecedenteGeneral.executeUpdate();
			
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla general de antecedentes, para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);
			if(e.getSQLState().equals(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente))
			{
				//return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \nOtro Usuario está insertanto Antecedentes.");
				//ya existe, es como si lo hubiera insertado.
				return new ResultadoBoolean(true);
			}
			return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);			
		}
		
		return new ResultadoBoolean(true);
	}

	public static ResultadoBoolean existenAntecedentesTransfusionales( Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator existeAntecedentesTransfusionales =  new PreparedStatementDecorator(con.prepareStatement(existenAntecedentesTransfusionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			existeAntecedentesTransfusionales.setInt(1, codigoPaciente);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedentesTransfusionales.executeQuery());
			
			if( resultado.next() )
			{
				int numFilas = resultado.getInt("antecedentes");
				
				if( numFilas <= 0 )
				{
					return new ResultadoBoolean(false);
				}
				else
				{
					return new ResultadoBoolean(true);
				}
			}
			else
			{
				logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes Transfusionales previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes Transfusionales previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch( SQLException e )
		{
			logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes Transfusionales previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes Transfusionales previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
		}	

	}
	
	public static ResultSetDecorator consultarAntecedentesTransfusionales( Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator consultaAntecedentesTransfusionales =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesTransfusionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consultaAntecedentesTransfusionales.setInt(1, codigoPaciente);
			
			return new ResultSetDecorator(consultaAntecedentesTransfusionales.executeQuery());			
		}
		catch( SQLException e )
		{
			logger.warn("Hubo problemas consultando los antecedentes transfusionales generales previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			return null;
		}	
	}
	
	public static ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
	{
		try
		{
			String insertarAntecedentesTransfusionalesStr	= "INSERT INTO "
				+ "antece_transfusionales "
				+ "(codigo_paciente, "
				+ "observaciones, fecha, hora) "
				+ "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			PreparedStatementDecorator insertarAntecedenteTransfusional =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesTransfusionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarAntecedenteTransfusional.setInt(1, codigoPaciente);
			insertarAntecedenteTransfusional.setString(2, observaciones);
			
			int insert = insertarAntecedenteTransfusional.executeUpdate();
			
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente+". \n"+e);			
		}
		
		return new ResultadoBoolean(true);
	}

	public static ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
	{
		try
		{
			String modificarAntecedentesTransfusionalesStr 	= "UPDATE antece_transfusionales "
				+ "SET observaciones = observaciones||'\n'||?, fecha = '"+Utilidades.capturarFechaBD()+"', hora = '"+UtilidadFecha.getHoraActual()+"' "
				+ "WHERE codigo_paciente=? " ;
			
			PreparedStatementDecorator modificarAntecedenteTransfusional =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedentesTransfusionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			modificarAntecedenteTransfusional.setInt(2, codigoPaciente);
			modificarAntecedenteTransfusional.setString(1, observaciones);
			
			int update = modificarAntecedenteTransfusional.executeUpdate();
			
			if( update == 0 )
				return new ResultadoBoolean(false, "No se actualizo ningún registro en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas actualizando en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas actualizando en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente+". \n"+e);			
		}
		
		return new ResultadoBoolean(true);

	}

	public static ResultSetDecorator consultarTransfusiones( Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator consultaTransfusiones =  new PreparedStatementDecorator(con.prepareStatement(consultarTransfusionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consultaTransfusiones.setInt(1, codigoPaciente);
			
			return new ResultSetDecorator(consultaTransfusiones.executeQuery());			
		}
		catch( SQLException e )
		{
			logger.warn("Hubo problemas consultando las transfusiones de antecedentes transfusionales previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			return null;
		}	
	}

	public static ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
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
			String insertarAntecedentesTransfusionalesStr	= "INSERT INTO "
				+ "antece_transfusionales "
				+ "(codigo_paciente, "
				+ "observaciones, fecha, hora) "
				+ "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			PreparedStatementDecorator insertarAntecedenteTransfusional =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesTransfusionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarAntecedenteTransfusional.setInt(1, codigoPaciente);
			insertarAntecedenteTransfusional.setString(2, observaciones);
			
			int insert = insertarAntecedenteTransfusional.executeUpdate();
			
			if( insert == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente+". \n"+e);
		    myFactory.abortTransaction(con);
		    return new ResultadoBoolean (false, "Transacción abortada");
		}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
				
		return new ResultadoBoolean(true);		
	}
	public static ResultadoBoolean modificarTransaccional(Connection con,	int codigoPaciente, String observaciones, String estado)throws SQLException
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
			String modificarAntecedentesTransfusionalesStr 	= "UPDATE antece_transfusionales "
				+ "SET observaciones = observaciones||'\n'||?, fecha = '"+Utilidades.capturarFechaBD()+"', hora = '"+UtilidadFecha.getHoraActual()+"' "
				+ "WHERE codigo_paciente=? " ;
			
			PreparedStatementDecorator modificarAntecedenteTransfusional =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedentesTransfusionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			modificarAntecedenteTransfusional.setInt(2, codigoPaciente);
			modificarAntecedenteTransfusional.setString(1, observaciones);
			
			int update = modificarAntecedenteTransfusional.executeUpdate();
			
			if( update == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas actualizando en la tabla de antecedentes transfusionales, para el paciente : "+codigoPaciente+". \n"+e);
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
