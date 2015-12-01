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
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Antecedentes Mórbidos
 *
 *	@version 1.0, Mar 31, 2004
 */
public class SqlBaseAntecedentesMorbidosDao {
	
	private static Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);

	private static final String existeAntecedentePacienteStr = "SELECT count(*)  as antecedentes "
																							 + "FROM antecedentes_pacientes "
																							 + "WHERE codigo_paciente=? ";
																							 
	private static final String insertarAntecedentePacienteStr = "INSERT INTO antecedentes_pacientes " 
																								+ "(codigo_paciente) "
																								+ "VALUES (?) ";
																								
	private static final String existenAntecedentesMorbidosStr = "SELECT count(*)  as antecedentes "
																								  + "FROM antecedentes_morbidos "
																								  + "WHERE codigo_paciente=? ";																									
																							 
	private static final String consultarAntecedentesMorbidosStr = "SELECT observaciones as observaciones, fecha as fecha, hora as hora "
																									+ "FROM antecedentes_morbidos "
																									+ "WHERE codigo_paciente=? ";			
																								
																									 
	private static final String consultarAntecedentesMorbidosMedicosPredef = "SELECT am.tipo_antecedente_medico AS tipoAntecedente, "
																														 + "am.fecha_inicio AS fechaInicio, "
																														 + "am.tratamiento AS tratamiento, "
																														 + "am.restriccion_dietaria AS restriccionDietaria, "
																														 + "am.observaciones AS observaciones, "
																														 + "amp.nombre AS nombreAntecedente, "
																														 + "am.fecha, "
																														 + "am.hora "
																														 + "FROM ant_morbidos_medicos am, "
																														 + "tipos_ant_medicos amp "
																														 + "WHERE am.codigo_paciente=? " 
																														 + "AND am.tipo_antecedente_medico = amp.codigo ";																							  																											  
	
	private static final String consultarAntecedentesMorbidosMedicosOtros = "SELECT codigo AS codigo, "
																														+ "nombre AS nombre, "
																														+ "fecha_inicio AS fechaInicio, "
																														+ "tratamiento AS tratamiento, "
																														+ "restriccion_dietaria AS restriccionDietaria, "
																														+ "observaciones AS observaciones, "
																														+ "fecha AS fecha, "
																														+ "hora AS hora "
																														+ "FROM ant_morbido_med_otros "																															 
																														+ "WHERE codigo_paciente=? ";
	
	private static final String consultarAntecedentesMorbidosQuirurgicos = "SELECT codigo AS codigo, "
																													+ "nombre AS nombre, "
																													+ "fecha AS fecha, "
																													+ "causa AS causa, "
																													+ "complicaciones AS complicaciones, "
																													+ "recomendaciones AS recomendaciones, "
																													+ "observaciones AS observaciones, "
																													+ "fecha_ant AS fecha, "
																													+ "hora_ant AS hora "
																													+ "FROM ant_morbidos_quirurgicos "																															 
																													+ "WHERE codigo_paciente=? ";
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#existenAntecedentes(java.sql.Connection, java.lang.String, java.lang.String)
	 */
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
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
					logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
					return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				}
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			}	
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#insertarAntecedenteGeneral(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
			try
			{
				PreparedStatementDecorator insertarAntecedenteGeneral =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				insertarAntecedenteGeneral.setInt(1, codigoPaciente);
			
				int insert = insertarAntecedenteGeneral.executeUpdate();
			
				if( insert == 0 )
				{
					return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla general de antecedentes, para el paciente : "+codigoPaciente );
				}
			}
			catch(SQLException e)
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
				logger.warn("Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);			
			}
		
			return new ResultadoBoolean(true);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#existenAntecedentesMorbidos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean existenAntecedentesMorbidos( Connection con, int codigoPaciente)
	{
			try
			{
				PreparedStatementDecorator existeAntecedentesMorbidos =  new PreparedStatementDecorator(con.prepareStatement(existenAntecedentesMorbidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				existeAntecedentesMorbidos.setInt(1, codigoPaciente);
			
				ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedentesMorbidos.executeQuery());
			
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
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
					logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes mórbidos previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
					return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes mórbidos previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				}
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes mórbidos previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes mórbidos previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			}	
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#existenAntecedentesMorbidos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public static ResultSetDecorator consultarAntecedentesMorbidos( Connection con, int codigoPaciente)
	{
			try
			{
				PreparedStatementDecorator consultaAntecedentesMorbidos =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesMorbidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				consultaAntecedentesMorbidos.setInt(1, codigoPaciente);
			
				return new ResultSetDecorator(consultaAntecedentesMorbidos.executeQuery());			
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
				logger.warn("Hubo problemas consultando los antecedentes mórbidos generales previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
				return null;
			}	
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#insertar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
	{
		try
		{
			String insertarAntecedentesMorbidosStr = "INSERT INTO antecedentes_morbidos(codigo_paciente, observaciones, fecha, hora) "
				  + "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') "; 	
			
			PreparedStatementDecorator insertarAntecedenteMorbido =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesMorbidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			insertarAntecedenteMorbido.setInt(1, codigoPaciente);
			insertarAntecedenteMorbido.setString(2, observaciones);
		
			int insert = insertarAntecedenteMorbido.executeUpdate();
		
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
			logger.warn("Hubo problemas insertando en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente+". \n"+e);			
		}
	
		return new ResultadoBoolean(true);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#modificar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
	{
			try
			{
				String modificarAntecedentesMorbidosStr = "UPDATE antecedentes_morbidos SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "
				 + "WHERE codigo_paciente=? ";
				
				PreparedStatementDecorator modificarAntecedenteMorbido =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedentesMorbidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				modificarAntecedenteMorbido.setInt(2, codigoPaciente);
				modificarAntecedenteMorbido.setString(1, observaciones);
			
				int update = modificarAntecedenteMorbido.executeUpdate();
			
				if( update == 0 )
					return new ResultadoBoolean(false, "No se actualizo ningún registro en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente );
			}
			catch(SQLException e)
			{	
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
				logger.warn("Hubo problemas actualizando en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas actualizando en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente+". \n"+e);			
			}
			return new ResultadoBoolean(true);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#cargarMorbidosMedicosPredefinidos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public static ResultSetDecorator cargarMorbidosMedicosPredefinidos(Connection con, int codigoPaciente)
	{
			try
			{
				PreparedStatementDecorator consultaMorbidosMedicosPredefinidos =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesMorbidosMedicosPredef,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				consultaMorbidosMedicosPredefinidos.setInt(1, codigoPaciente);
			
				return new ResultSetDecorator(consultaMorbidosMedicosPredefinidos.executeQuery());	
			}
			catch(SQLException e)
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
				logger.warn("Hubo problemas cargando los antecedentes mórbidos médicos predefinidos, para el paciente : "+codigoPaciente+". \n"+e);
				return null;
			}
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#cargarMorbidosMedicosOtros(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public static ResultSetDecorator cargarMorbidosMedicosOtros(Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator consultaMorbidosMedicosOtros =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesMorbidosMedicosOtros,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consultaMorbidosMedicosOtros.setInt(1, codigoPaciente);
			
			return new ResultSetDecorator(consultaMorbidosMedicosOtros.executeQuery());	
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
			logger.warn("Hubo problemas cargando los antecedentes mórbidos médicos otros, para el paciente : "+codigoPaciente+". \n"+e);
			return null;
		}
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#cargarMorbidosQuirurgicos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public static ResultSetDecorator cargarMorbidosQuirurgicos(Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator consultaMorbidosQuirurgicos =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesMorbidosQuirurgicos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			consultaMorbidosQuirurgicos.setInt(1, codigoPaciente);
		
			return new ResultSetDecorator(consultaMorbidosQuirurgicos.executeQuery());	
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMorbidosDao.class);
			logger.warn("Hubo problemas cargando los antecedentes mórbidos quirúrgicos, para el paciente : "+codigoPaciente+". \n"+e);
			return null;
		}
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
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
			String insertarAntecedentesMorbidosStr = "INSERT INTO antecedentes_morbidos(codigo_paciente, observaciones, fecha, hora) "
				  + "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') "; 	
			
			PreparedStatementDecorator insertarAntecedenteMorbido =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesMorbidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarAntecedenteMorbido.setInt(1, codigoPaciente);
			insertarAntecedenteMorbido.setString(2, observaciones);
			
			int insert = insertarAntecedenteMorbido.executeUpdate();
			
			if( insert == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente+". \n"+e);
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
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificarTransaccional(Connection con,	int codigoPaciente, String observaciones, String estado) throws SQLException
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
			String modificarAntecedentesMorbidosStr = "UPDATE antecedentes_morbidos SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "
			 + "WHERE codigo_paciente=? ";
			
			PreparedStatementDecorator modificarAntecedenteMorbido =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedentesMorbidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			modificarAntecedenteMorbido.setInt(2, codigoPaciente);
			modificarAntecedenteMorbido.setString(1, observaciones);
			
			int update = modificarAntecedenteMorbido.executeUpdate();
			
			if( update == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas actualizando en la tabla de antecedentes mórbidos, para el paciente : "+codigoPaciente+". \n"+e);
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
