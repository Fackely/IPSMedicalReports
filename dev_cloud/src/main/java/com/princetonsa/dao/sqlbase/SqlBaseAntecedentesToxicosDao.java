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
 * utilizan SQL estándar. Métodos particulares a Antecedente Tóxico.
 *
 *	@version 1.0, Abril 1, 2004
 */
public class SqlBaseAntecedentesToxicosDao 
{
	private static Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);

	private static final String existeAntecedentePacienteStr = "SELECT count(*)  as antecedentes "
																									 + "FROM antecedentes_pacientes "
																									 + "WHERE codigo_paciente=? "; 
	
	private static final String insertarAntecedentePacienteStr = "INSERT INTO antecedentes_pacientes " 
																									   + "(codigo_paciente) "
																									   + "VALUES (?) ";
	

	private static final String existenAntecedentesToxicosStr = "SELECT count(*)  as antecedentes "
																									  + "FROM antecedentes_toxicos "
																									  + "WHERE codigo_paciente=? ";																								   																									   
	
	private static final String consultarAntecedentesToxicosStr = "SELECT observaciones as observaciones, fecha as fecha, hora as hora "
																										+ "FROM antecedentes_toxicos "
																										+ "WHERE codigo_paciente=? ";		
																										
																									  
	private static final String consultarAntecedentesToxicosPredef = "SELECT at.codigo AS codigoAntecedente, "																											
																											+ "at.actual AS habitoActual, "
																											+ "at.cantidad AS cantidad, "
																											+ "at.frecuencia AS frecuencia, "
																											+ "at.tiempo_habito AS tiempo, "
																											+ "at.fecha_grabacion AS fechaGrabacion, "
																											+ "at.hora_grabacion AS horaGrabacion, "
																											+ "at.observaciones AS observaciones, "
																											+ "atp.nombre AS nombreAntecedente, at.fecha as fecha, at.hora as hora "
																											+ "FROM ant_toxicos_tipos at, "
																											+ "tipos_ant_toxicos atp "
																											+ "WHERE at.codigo_paciente=? "
																											+ "AND at.cod_tipo_antecedente = atp.codigo "
																											+ "ORDER BY fechaGrabacion DESC, horaGrabacion DESC ";

	private static final String consultarAntecedentesToxicosOtros = "SELECT at.codigo AS codigoAntecedente, "
																											+ "at.actual AS habitoActual, "
																											+ "at.cantidad AS cantidad, "
																											+ "at.frecuencia AS frecuencia, "
																											+ "at.tiempo_habito AS tiempo, "
																											+ "at.fecha_grabacion AS fechaGrabacion, "
																											+ "at.hora_grabacion AS horaGrabacion, "
																											+ "at.observaciones AS observaciones, "
																											+ "at.nombre AS nombreAntecedente, at.fecha as fecha, at.hora as hora "
																											+ "FROM ant_toxicos_otros at "
																											+ "WHERE at.codigo_paciente=? " 
																											+ "ORDER BY fechaGrabacion DESC, horaGrabacion DESC ";																		

																							
		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#existenAntecedentes(java.sql.Connection, java.lang.String, java.lang.String)
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
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
					logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
					return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				}
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			}	
		}
	
		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#insertarAntecedenteGeneral(java.sql.Connection, java.lang.String, java.lang.String)
		 */
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
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);			
			}
		
			return new ResultadoBoolean(true);
		}

		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#existenAntecedentesTo
		 * xicos(java.sql.Connection, java.lang.String, java.lang.String)
		 */
		public static ResultadoBoolean existenAntecedentesToxicos( Connection con, int codigoPaciente )
		{
			try
			{
				PreparedStatementDecorator existeAntecedentesToxicos =  new PreparedStatementDecorator(con.prepareStatement(existenAntecedentesToxicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				existeAntecedentesToxicos.setInt(1, codigoPaciente);
			
				ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedentesToxicos.executeQuery());
			
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
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
					logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes tóxicos previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
					return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes tóxicos previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				}
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes tóxicos previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes tóxicos previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			}	
		}
		
		/**
		 * @see com.princetonsa.dao.
		 * AntecedentesToxicosDao#existenAntecedentesToxicos (java.sql.Connection,
		 * java.lang.String, java.lang.String)
		 */
		public static ResultSetDecorator consultarAntecedentesToxicos( Connection con, int codigoPaciente)
		{
			try
			{
				PreparedStatementDecorator consultaAntecedentesToxicos =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesToxicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				consultaAntecedentesToxicos.setInt(1, codigoPaciente);
			
				return new ResultSetDecorator(consultaAntecedentesToxicos.executeQuery());			
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas consultando los antecedentes tóxicos generales previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
				return null;
			}	
		}
		
		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#insertar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
		 */
		public static ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
		{
			try
			{
				String insertarAntecedentesToxicosStr  = "INSERT INTO "
					  + "antecedentes_toxicos "
					  + "(codigo_paciente, "
					  + "observaciones, fecha, hora) "
					  + "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') "; 
				
				PreparedStatementDecorator insertarAntecedenteToxicos =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesToxicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				insertarAntecedenteToxicos.setInt(1, codigoPaciente);
				insertarAntecedenteToxicos.setString(2, observaciones);
				
				int insert = insertarAntecedenteToxicos.executeUpdate();
				
				if( insert == 0 )
					return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente );
			}
			catch(SQLException e)
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas insertando en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente+". \n"+e);			
			}
			
			return new ResultadoBoolean(true);
		}
		
		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#modificar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
		 */
		public static ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
		{
			try
			{
				String modificarAntecedentesToxicosStr = "UPDATE antecedentes_toxicos "
					 + "SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "
					 + "WHERE codigo_paciente=? ";
				
				PreparedStatementDecorator modificarAntecedenteToxicos =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedentesToxicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				modificarAntecedenteToxicos.setInt(2, codigoPaciente);
				modificarAntecedenteToxicos.setString(1, observaciones);
				
				int update = modificarAntecedenteToxicos.executeUpdate();
				
				if( update == 0 )
					return new ResultadoBoolean(false, "No se actualizo ningún registro en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente );
			}
			catch(SQLException e)
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas actualizando en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente+". \n"+e);
				return new ResultadoBoolean(false, "Hubo problemas actualizando en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente+". \n"+e);			
			}
			
			return new ResultadoBoolean(true);
	
		}

		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#cargarToxicosPredefinidos
		 * (java.sql.Connection, java.lang.String, java.lang.String)
		 */
		public static ResultSetDecorator cargarToxicosPredefinidos(Connection con, int codigoPaciente)
		{
			try
			{
				PreparedStatementDecorator consultaToxicosPredefinidos =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesToxicosPredef,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				consultaToxicosPredefinidos.setInt(1, codigoPaciente);
				
				return new ResultSetDecorator(consultaToxicosPredefinidos.executeQuery());	
			}
			catch(SQLException e)
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas cargando los antecedentes tóxicos predefinidos, para el paciente : "+codigoPaciente+". \n"+e);
				return null;
			}
		}

		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#cargarToxicosOtros(java.
		 * sql. Connection, java.lang.String, java.lang.String)
		 */
		public static ResultSetDecorator cargarToxicosOtros(Connection con, int codigoPaciente)
		{
			try
			{
				PreparedStatementDecorator consultaToxicosOtros =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesToxicosOtros,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				consultaToxicosOtros.setInt(1, codigoPaciente);
				
				return new ResultSetDecorator(consultaToxicosOtros.executeQuery());	
			}
			catch(SQLException e)
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesToxicosDao.class);
				logger.warn("Hubo problemas cargando los antecedentes tóxicos otros, para el paciente : "+codigoPaciente+". \n"+e);
				return null;
			}
		}

		/**
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
				String insertarAntecedentesToxicosStr  = "INSERT INTO "
					  + "antecedentes_toxicos "
					  + "(codigo_paciente, "
					  + "observaciones, fecha, hora) "
					  + "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') "; 
				
				PreparedStatementDecorator insertarAntecedenteToxicos =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesToxicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				insertarAntecedenteToxicos.setInt(1, codigoPaciente);
				insertarAntecedenteToxicos.setString(2, observaciones);
				
				int insert = insertarAntecedenteToxicos.executeUpdate();
				
				if( insert == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
			catch(SQLException e)
			{
				logger.warn("Hubo problemas insertando en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente+". \n"+e);
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
		 * @see com.princetonsa.dao.AntecedentesToxicosDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
		 */
		public static ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
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
				String modificarAntecedentesToxicosStr = "UPDATE antecedentes_toxicos "
					 + "SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "
					 + "WHERE codigo_paciente=? ";
				
				PreparedStatementDecorator modificarAntecedenteToxicos =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedentesToxicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				modificarAntecedenteToxicos.setInt(2, codigoPaciente);
				modificarAntecedenteToxicos.setString(1, observaciones);
				
				int update = modificarAntecedenteToxicos.executeUpdate();
				
				if( update == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
			catch(SQLException e)
			{
				logger.warn("Hubo problemas actualizando en la tabla de antecedentes tóxicos, para el paciente : "+codigoPaciente+". \n"+e);
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
