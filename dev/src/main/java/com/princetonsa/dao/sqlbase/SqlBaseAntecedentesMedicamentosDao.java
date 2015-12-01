/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Antecedentes Medicamentos
 *
 *	@version 1.0, Mar 30, 2004
 */
public class SqlBaseAntecedentesMedicamentosDao 
{
	private static Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);

	private static final String existeAntecedentePacienteStr = "SELECT count(*)  as antecedentes "
																									 + "FROM antecedentes_pacientes "
																									 + "WHERE codigo_paciente=? ";

	private static final String insertarAntecedentePacienteStr = "INSERT INTO antecedentes_pacientes " 
																									   + "(codigo_paciente) "
																									   + "VALUES (?) ";
																									   
	private static final String existenAntecedentesMedicamentosStr 	= "SELECT count(*)  as antecedentes "
																													+ "FROM antece_medicamentos "
																													+ "WHERE codigo_paciente=? ";
																													
	private static final String consultarAntecedentesMedicamentosStr 	= "SELECT observaciones as observaciones, fecha as fecha, hora as hora "
																														+ "FROM antece_medicamentos "
																														+ "WHERE codigo_paciente=? ";
																														
																													
	private static final String consultarMedicamentosStr	= "SELECT codigo AS codigo, "
																								+ "nombre AS nombre, "
																								+ "codigo_articulo AS codigoa, "
																								+ "dosificacion AS dosis, "
																								+ "frecuencia AS frecuencia, "
																								+ "tipo_frecuencia AS tipofrecuencia, "
																								+ "unidosis AS unidosis, "
																								+ "dosis_diaria AS dosisd, "
																								+ "cantidad AS cantidad, "
																								+ "tiempo_tratamiento AS tiempot, "
																								+ "fecha_inicio AS fechaInicio, "
																								+ "fecha_finalizacion AS fechaFin, "
																								+ "fecha AS fecha, "
																								+ "hora AS hora, "
																								+ "observaciones AS observaciones "
																								+ "FROM ant_medicamentos "																															 
																								+ "WHERE codigo_paciente=? ";
	
	private static String[] indicesMapFC={"formac_","concentracion_"}; 
	private static String consultaStrFC="SELECT getNomFormaFarmaceutica(a.forma_farmaceutica) AS formac, concentracion AS concentracion FROM articulo a WHERE a.codigo = ? ";
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> consultaFormaConc (Connection con, int codigoArticulo)
	{
		HashMap<String, Object> resultadosFC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrFC, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst.setInt(1, codigoArticulo);
			
			resultadosFC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true, true);
			
			resultadosFC.put("INDICES",indicesMapFC);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de la Forma Farmaceutica y la Concentracion del Articulo Seleccionado");
		}
		return resultadosFC;
	}
	
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
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
					logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
					return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				}
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
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
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
			logger.warn("Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);			
		}
		
		return new ResultadoBoolean(true);
	}

	public static ResultadoBoolean existenAntecedentesMedicamentos( Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator existeAntecedentesMedicamentos =  new PreparedStatementDecorator(con.prepareStatement(existenAntecedentesMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			existeAntecedentesMedicamentos.setInt(1, codigoPaciente);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedentesMedicamentos.executeQuery());
			
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
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes medicamentos previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes medicamentos previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch( SQLException e )
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
			logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes medicamentos previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no antecedentes medicamentos previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
		}	

	}
	

	/**
	* @see com.princetonsa.dao.AntecedentesMedicamentosDao#existenAntecedentesMedicamentos(java.sql.Connection, java.lang.String, java.lang.String)
	*/
	public static ResultSetDecorator consultarAntecedentesMedicamentos( Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator consultaAntecedentesMedicamentos =  new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			consultaAntecedentesMedicamentos.setInt(1, codigoPaciente);
		
			return new ResultSetDecorator(consultaAntecedentesMedicamentos.executeQuery());			
		}
		catch( SQLException e )
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
			logger.warn("Hubo problemas consultando los antecedentes medicamentos generales previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
			return null;
		}	
	}	 

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#insertar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */	
	public static ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
	{
		try
		{
			String insertarAntecedentesMedicamentosStr	 = "INSERT INTO "
				+ "antece_medicamentos "
				+ "(codigo_paciente, "
				+ "observaciones, fecha, hora) "
				+ "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			PreparedStatementDecorator insertarAntecedenteMedicamento =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarAntecedenteMedicamento.setInt(1, codigoPaciente);
			insertarAntecedenteMedicamento.setString(2, observaciones);
			
			int insert = insertarAntecedenteMedicamento.executeUpdate();
			
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
			logger.warn("Hubo problemas insertando en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente+". \n"+e);			
		}
	
		return new ResultadoBoolean(true);
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#modificar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
	{
		try
		{
			int update;
					
			ResultSetDecorator observacion=consultarAntecedentesMedicamentos(con,codigoPaciente);
			
			if(observacion.next())
				if(observacion.getString("observaciones")!=null || observacion.getString("observaciones")!="")
					observaciones=observacion.getString("observaciones")+observaciones;
			
			
			String modificarAntecedentesMedicamentosStr 	= "UPDATE antece_medicamentos "
				+ "SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' " 
				+ "WHERE codigo_paciente=? ";
			
			PreparedStatementDecorator modificarAntecedenteMedicamento =  new PreparedStatementDecorator(con, modificarAntecedentesMedicamentosStr);
			
			modificarAntecedenteMedicamento.setString(1, observaciones);
			modificarAntecedenteMedicamento.setInt(2, codigoPaciente);

			Log4JManager.info(modificarAntecedenteMedicamento);
			update = modificarAntecedenteMedicamento.executeUpdate();
						
			if( update == 0 )
			{
				return new ResultadoBoolean(false, "No se actualizo ningún registro en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente );
			}
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
			logger.error("Hubo problemas actualizando en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente+". \n",e);
			return new ResultadoBoolean(false, "Hubo problemas actualizando en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente+". \n"+e);			
		}
	
		return new ResultadoBoolean(true);

	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#existenAntecedentesMedicamentos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public static ResultSetDecorator consultarMedicamentos( Connection con, int codigoPaciente)
	{
			try
			{
				logger.info("consultarMedicamentosStr->"+consultarMedicamentosStr+" ->"+codigoPaciente);
				PreparedStatementDecorator consultaMedicamentos =  new PreparedStatementDecorator(con.prepareStatement(consultarMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				consultaMedicamentos.setInt(1, codigoPaciente);
			
				return new ResultSetDecorator(consultaMedicamentos.executeQuery());			
			}
			catch( SQLException e )
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesMedicamentosDao.class);
				logger.warn("Hubo problemas consultando los medicamentos de antecedentes medicamentos previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
				return null;
			}	
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
			String insertarAntecedentesMedicamentosStr	 = "INSERT INTO "
				+ "antece_medicamentos "
				+ "(codigo_paciente, "
				+ "observaciones, fecha, hora) "
				+ "VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			PreparedStatementDecorator insertarAntecedenteMedicamento =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarAntecedenteMedicamento.setInt(1, codigoPaciente);
			insertarAntecedenteMedicamento.setString(2, observaciones);
			
			int insert = insertarAntecedenteMedicamento.executeUpdate();
			
			if( insert == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente+". \n"+e);
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
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
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
			
			String modificarAntecedentesMedicamentosStr 	= "UPDATE antece_medicamentos "
				+ "SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora="+UtilidadFecha.getHoraActual()+" " 
				+ "WHERE codigo_paciente=? ";
			
			PreparedStatementDecorator modificarAntecedenteMedicamento =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedentesMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			modificarAntecedenteMedicamento.setInt(2, codigoPaciente);
			modificarAntecedenteMedicamento.setString(1, observaciones);
			
			int update = modificarAntecedenteMedicamento.executeUpdate();
			
			if( update == 0 )
			{
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas actualizando en la tabla de antecedentes medicamentos, para el paciente : "+codigoPaciente+". \n"+e);
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
