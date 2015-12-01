/*
 * Created on Apr 1, 2004
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
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author wrios
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseAntecedenteToxicoDao 
{
    
    private static Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);
    
	

	private static String modificarPredefinidoStr = "UPDATE  ant_toxicos_tipos "
																			 + "SET actual = ?, "
																			 + "cantidad = ?, "
																			 + "tiempo_habito = ?, "
																			 + "frecuencia = ?, "
																			 + "observaciones = ?, "	
																			 + "fecha = '"+Utilidades.capturarFechaBD()+"', "	
																			 + "hora = '"+UtilidadFecha.getHoraActual()+"' "
																			 + "WHERE codigo_paciente = ? "
																			 + "AND codigo = ? ";
																			 
	private static String modificarOtroStr 	= "UPDATE ant_toxicos_otros "
																	+ "SET actual = ?, "
																	+ "cantidad = ?, "
																	+ "tiempo_habito = ?, "
																	+ "frecuencia = ?, "																			 
																	+ "observaciones = ?, fecha = '"+Utilidades.capturarFechaBD()+"', hora = '"+UtilidadFecha.getHoraActual()+"' "
																	+ "WHERE codigo_paciente = ? "
																	+ "AND codigo = ? ";
																		 
	private static String existePredefinidoStr = "SELECT COUNT(*) AS antecedente "
																		+ "FROM ant_toxicos_tipos "
																		+ "WHERE codigo_paciente = ? "
																		+ "AND codigo = ? ";

	private static String existeOtroStr = "SELECT COUNT(*) AS antecedente "
															+ "FROM ant_toxicos_otros "
															+ "WHERE codigo_paciente = ? "
															+ "AND codigo = ? ";


	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarOtro(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean insertarOtro(	Connection con,
														int codigoPaciente,
														int codigo,
														String nombre,
														boolean habitoActual,
														String cantidad,
														String frecuencia,
														String tiempo,
														String observaciones)
	{
		try
		{
			String insertarOtroStr = "INSERT INTO ant_toxicos_otros "
				+ "(codigo_paciente, "
				+ "codigo, "
				+ "nombre, "
				+ "actual, "
				+ "cantidad, "
				+ "tiempo_habito, "
				+ "frecuencia, "
				+ "fecha_grabacion, "
				+ "hora_grabacion, "																																		  
				+ "observaciones, fecha, hora ) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			PreparedStatementDecorator insertarOtro =  new PreparedStatementDecorator(con.prepareStatement(insertarOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			insertarOtro.setInt(1, codigoPaciente);
			insertarOtro.setInt(2, codigo);
			insertarOtro.setString(3, nombre);
			insertarOtro.setBoolean(4, habitoActual);
			insertarOtro.setString(5, cantidad);
			insertarOtro.setString(6, tiempo);
			insertarOtro.setString(7, frecuencia);
			insertarOtro.setString(8, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			insertarOtro.setString(9, UtilidadFecha.getHoraActual());
			insertarOtro.setString(10, observaciones);
			
			int insert = insertarOtro.executeUpdate();
			
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla para el antecedente tóxico adicional "+nombre+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);	
			logger.warn("Hubo problemas insertando el antecedente morbido tóxico otro "+nombre+", para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando el antecedente tóxico otro "+nombre+", para el paciente : "+codigoPaciente+". \n"+e); 
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarPredefinido(java.sql.Connection, java.lang.String, java.lang.String, int, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificarPredefinido(	Connection con,
																								int codigoPaciente,
																								int codigo,
																								boolean habitoActual,
																								String cantidad,
																								String frecuencia,
																								String tiempo,
																								String observaciones)
	{
		try
		{
			PreparedStatementDecorator modificarPredefinido =  new PreparedStatementDecorator(con.prepareStatement(modificarPredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			modificarPredefinido.setInt(6, codigoPaciente);
			modificarPredefinido.setInt(7, codigo);
			modificarPredefinido.setBoolean(1, habitoActual);
			modificarPredefinido.setString(2, cantidad);
			modificarPredefinido.setString(3, tiempo);
			modificarPredefinido.setString(4, frecuencia);
			modificarPredefinido.setString(5, observaciones);
			
			int update = modificarPredefinido.executeUpdate();
			
			if( update == 0 )
				return new ResultadoBoolean(false, "No se modificó ningún registro en la tabla para el antecedente tóxico predefinido"+codigo+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);
			logger.error("Problemas modificando el antecedente tóxico predefinido código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
			return new ResultadoBoolean(false, "Problemas modificando el antecedente tóxico predefinido código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarOtro(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificarOtro(	Connection con,
															int codigoPaciente,
															int codigo,
															boolean habitoActual,
															String cantidad,
															String frecuencia,
															String tiempo,
															String observaciones)
	{
		try
		{
			PreparedStatementDecorator modificarOtro =  new PreparedStatementDecorator(con.prepareStatement(modificarOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			modificarOtro.setInt(6, codigoPaciente);
			modificarOtro.setInt(7, codigo);
			modificarOtro.setBoolean(1, habitoActual);
			modificarOtro.setString(2, cantidad);
			modificarOtro.setString(3, tiempo);
			modificarOtro.setString(4, frecuencia);
			modificarOtro.setString(5, observaciones);
			
			int update = modificarOtro.executeUpdate();
			
			if( update == 0 )
				return new ResultadoBoolean(false, "No se modificó ningún registro en la tabla para el antecedente tóxico adicional "+codigo+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);
			logger.error("Problemas modificando el antecedente tóxico Otro código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
			return new ResultadoBoolean(false, "Problemas modificando el antecedente tóxico Otro código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#existeAntecedentePredefinido(java.sql.Connection, java.lang.String, java.lang.String, int)
	 */
	public static ResultadoBoolean existeAntecedentePredefinido(	Connection con,
																					int codigoPaciente,
																					int codigo)
	{
		try
		{			
			PreparedStatementDecorator existeAntecedentePredefinido =  new PreparedStatementDecorator(con.prepareStatement(existePredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			existeAntecedentePredefinido.setInt(1, codigoPaciente);
			existeAntecedentePredefinido.setInt(2, codigo);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedentePredefinido.executeQuery());
				
			if( resultado.next() )
			{
				int numFilas = resultado.getInt("antecedente");
				
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
				final Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch( Exception e )
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);
			logger.error("No se pudo hacer la consulta, para saber si ya existia un antecedente tóxico con código "+codigo);
			return new ResultadoBoolean(false, "No se pudo hacer la consulta, para saber si ya existia un antecedente tóxico con código "+codigo+". "+e);
		}
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#existeAntecedenteOtro(java.sql.Connection, java.lang.String, java.lang.String, int)
	 */
	public static ResultadoBoolean existeAntecedenteOtro(	Connection con,
																			int codigoPaciente,
																			int codigo)
	{
		try
		{	
			PreparedStatementDecorator existeAntecedenteOtro =  new PreparedStatementDecorator(con.prepareStatement(existeOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			existeAntecedenteOtro.setInt(1, codigoPaciente);
			existeAntecedenteOtro.setInt(2, codigo);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedenteOtro.executeQuery());
			
			if( resultado.next() )
			{
				int numFilas = resultado.getInt("antecedente");
				
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
				final Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch( Exception e )
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteToxicoDao.class);
			logger.error("No se pudo hacer la consulta, para saber si ya existia un antecedente tóxico con código "+codigo);
			return new ResultadoBoolean(false, "No se pudo hacer la consulta, para saber si ya existia un antecedente tóxico con código "+codigo+". "+e);
		}
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarPredefinidoTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificarPredefinidoTransaccional(	Connection con,
																							int codigoPaciente,
																							int codigo,
																							boolean habitoActual,
																							String cantidad,
																							String frecuencia,
																							String tiempo,
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
			PreparedStatementDecorator modificarPredefinido =  new PreparedStatementDecorator(con.prepareStatement(modificarPredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			modificarPredefinido.setInt(6, codigoPaciente);
			modificarPredefinido.setInt(7, codigo);
			modificarPredefinido.setBoolean(1, habitoActual);
			modificarPredefinido.setString(2, cantidad);
			modificarPredefinido.setString(3, tiempo);
			modificarPredefinido.setString(4, frecuencia);
			modificarPredefinido.setString(5, observaciones);
			
			int update = modificarPredefinido.executeUpdate();
			
			if( update == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.error("Problemas modificando el antecedente tóxico predefinido código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
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
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#modificarOtroTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificarOtroTransaccional(	Connection con,
																					int codigoPaciente,
																					int codigo,
																					boolean habitoActual,
																					String cantidad,
																					String frecuencia,
																					String tiempo,
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
			PreparedStatementDecorator modificarOtro =  new PreparedStatementDecorator(con.prepareStatement(modificarOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			modificarOtro.setInt(6, codigoPaciente);
			modificarOtro.setInt(7, codigo);
			modificarOtro.setBoolean(1, habitoActual);
			modificarOtro.setString(2, cantidad);
			modificarOtro.setString(3, tiempo);
			modificarOtro.setString(4, frecuencia);
			modificarOtro.setString(5, observaciones);
			
			int update = modificarOtro.executeUpdate();
			
			if( update == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.error("Problemas modificando el antecedente tóxico Otro código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
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
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarOtroTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean insertarOtroTransaccional(	Connection con,
																				int codigoPaciente,
																				int codigo,
																				String nombre,
																				boolean habitoActual,
																				String cantidad,
																				String frecuencia,
																				String tiempo,
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
			String insertarOtroStr = "INSERT INTO ant_toxicos_otros "
				+ "(codigo_paciente, "
				+ "codigo, "
				+ "nombre, "
				+ "actual, "
				+ "cantidad, "
				+ "tiempo_habito, "
				+ "frecuencia, "
				+ "fecha_grabacion, "
				+ "hora_grabacion, "																																		  
				+ "observaciones, fecha, hora ) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			
			PreparedStatementDecorator insertarOtro =  new PreparedStatementDecorator(con.prepareStatement(insertarOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			insertarOtro.setInt(1, codigoPaciente);
			insertarOtro.setInt(2, codigo);
			insertarOtro.setString(3, nombre);
			insertarOtro.setBoolean(4, habitoActual);
			insertarOtro.setString(5, cantidad);
			insertarOtro.setString(6, tiempo);
			insertarOtro.setString(7, frecuencia);
			insertarOtro.setString(8, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			insertarOtro.setString(9, UtilidadFecha.getHoraActual());
			insertarOtro.setString(10, observaciones);
			
			int insert = insertarOtro.executeUpdate();
			
			if( insert == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.error("Hubo problemas insertando el antecedente tóxico otro "+nombre+", para el paciente : "+codigoPaciente+". \n"+e);
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
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarPredefinido(java.
	 * sql.Connection, java.lang.String, java.lang.String, int, boolean, java.
	 * lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean insertarPredefinido(	Connection con,	
																	int codigoPaciente,
																	int codTipoPredefinido, 
																	boolean habitoActual,
																	String cantidad,
																	String frecuencia,
																	String tiempo,
																	String observaciones,
																	String insertarPredefinidoStr)
	{		
		try
		{			
			PreparedStatementDecorator insertarPredefinido =  new PreparedStatementDecorator(con.prepareStatement(insertarPredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			insertarPredefinido.setInt(1, codigoPaciente);
			insertarPredefinido.setInt(2, codTipoPredefinido);
			insertarPredefinido.setBoolean(3, habitoActual);
			insertarPredefinido.setString(4, cantidad);
			insertarPredefinido.setString(5, tiempo);
			insertarPredefinido.setString(6, frecuencia);
			insertarPredefinido.setString(7, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			insertarPredefinido.setString(8, UtilidadFecha.getHoraActual());
			insertarPredefinido.setString(9, observaciones);
			
			int insert = insertarPredefinido.executeUpdate();
			
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla para el antecedente tóxico predefinido "+codTipoPredefinido+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando el antecedente tóxico predefinido "+codTipoPredefinido+", para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando el antecedente tóxico predefinido "+codTipoPredefinido+", para el paciente : "+codigoPaciente+". \n"+e);
		}
		return new ResultadoBoolean(true);				
	}

	/**
	 * @see com.princetonsa.dao.AntecedenteToxicoDao#insertarPredefinidoTransaccional(java.sql.Connection, java.lang.String, java.lang.String, int, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean insertarPredefinidoTransaccional(	Connection con,
																							int codigoPaciente,
																							int codTipoPredefinido,
																							boolean habitoActual,
																							String cantidad,
																							String frecuencia,
																							String tiempo,
																							String observaciones,
																							String estado,
																							String insertarPredefinidoStr) throws SQLException
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
			PreparedStatementDecorator insertarPredefinido =  new PreparedStatementDecorator(con.prepareStatement(insertarPredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			insertarPredefinido.setInt(1, codigoPaciente);
			insertarPredefinido.setInt(2, codTipoPredefinido);
			insertarPredefinido.setBoolean(3, habitoActual);
			insertarPredefinido.setString(4, cantidad);
			insertarPredefinido.setString(5, tiempo);
			insertarPredefinido.setString(6, frecuencia);
			insertarPredefinido.setString(7, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			insertarPredefinido.setString(8, UtilidadFecha.getHoraActual());
			insertarPredefinido.setString(9, observaciones);
			
			int insert = insertarPredefinido.executeUpdate();
			
			if( insert == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando el antecedente tóxico predefinido "+codTipoPredefinido+", para el paciente : "+codigoPaciente+". \n"+e);
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
