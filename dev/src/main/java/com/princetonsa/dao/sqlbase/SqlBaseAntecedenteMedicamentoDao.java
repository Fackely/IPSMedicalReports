/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Antecedente Medicamento
 *
 *	@version 1.0, Mar 29, 2004
 */
public class SqlBaseAntecedenteMedicamentoDao 
{
	private static Logger logger = Logger.getLogger(SqlBaseAntecedenteMedicamentoDao.class);
	
	private static String insertarStr = "INSERT INTO ant_medicamentos "
															+ "(codigo_paciente, "
															+ "codigo, "
															+ "nombre, "
															+ "dosificacion, "
															+ "frecuencia, "
															+ "fecha_inicio, "
															+ "fecha_finalizacion, "
															+ "observaciones,dosis_diaria,cantidad,tiempo_tratamiento,codigo_articulo," +
																	"tipo_frecuencia, unidosis) "
															+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?,?) ";	
															
	private static String modificarStr = "UPDATE ant_medicamentos "
															  + "SET dosificacion = ?, "
															  + "frecuencia = ?, "
															  + "fecha_inicio = ?, "
															  + "fecha_finalizacion = ?, "
															  + "observaciones = ?,dosis_diaria=?,cantidad=?,tiempo_tratamiento=?, tipo_frecuencia=?, unidosis=? "
															  + "WHERE codigo_paciente = ? "
															  + "AND codigo = ? ";
	
	private static String existeStr = "SELECT COUNT(*) AS antecedente "
														 + "FROM ant_medicamentos "
														 + "WHERE codigo_paciente = ? "
														 + "AND codigo = ? ";
	
															

	/**
	 * @param con
	 * @param codigoPaciente
	 * @param codigo
	 * @param codigoA
	 * @param nombre
	 * @param dosis
	 * @param frecuencia
	 * @param fechaInicio
	 * @param fechaFin
	 * @param observaciones
	 * @param estado
	 * @param cantidad
	 * @param dosisD
	 * @param tiempoT
	 * @param tipofrecuencia
	 * @param unidosis
	 * @return
	 * @throws SQLException	 */
	public static ResultadoBoolean insertarTransaccional(	Connection con,
			int codigoPaciente,
			int codigo,
			String codigoA,
			String nombre,
			String dosis,
			String frecuencia,
			String fechaInicio,
			String fechaFin,
			String observaciones,
			String estado, String cantidad, String dosisD, String tiempoT,
			String tipofrecuencia, String unidosis) throws SQLException
	{
	
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

	    if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
	    {
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo empezar la transacción");
		    }
	    }
		PreparedStatement pst=null;	    
	    try
	    {
		    pst = con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				    
		    pst.setInt(1, codigoPaciente);
		    pst.setInt(2, codigo);
		    pst.setString(3, nombre);
		    pst.setString(4, dosis);
		    pst.setString(5, frecuencia);
		    pst.setString(6, fechaInicio);
		    pst.setString(7, fechaFin);
		    pst.setString(8, observaciones);
		    pst.setString(9, dosisD);

		    if(!UtilidadTexto.isEmpty(cantidad))
				pst.setString(10, cantidad);
			else
				pst.setString(10,"");
				    
		    pst.setString(11, tiempoT);
		    pst.setInt(12, Utilidades.convertirAEntero(codigoA));
		    
		    pst.setString(13, tipofrecuencia);
		    pst.setString(14, unidosis);
		    
		    int insert = pst.executeUpdate();

		    if( insert == 0 )
		    {
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
		    }
	    }
	    catch(SQLException e)
	    {
		    logger.warn("Hubo problemas insertando el antecedente medicamento "+nombre+", para el paciente : "+codigoPaciente+". \n"+e);
		    myFactory.abortTransaction(con);
		    return new ResultadoBoolean (false, "Transacción abortada");
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	    if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
	    {
		    myFactory.endTransaction(con);
	    }		
	    return new ResultadoBoolean(true);				

	}

	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @param codigo
	 * @param nombre
	 * @param dosis
	 * @param frecuencia
	 * @param fechaInicio
	 * @param fechaFin
	 * @param observaciones
	 * @param cantidad
	 * @param dosisD
	 * @param tiempoT
	 * @param tipofrecuencia
	 * @param unidosis
	 * @return	 */
	public static ResultadoBoolean insertar(	Connection con,
												int codigoPaciente,
												int codigo,
												String nombre,
												String dosis,
												String frecuencia,
												String fechaInicio,
												String fechaFin,
												String observaciones, String cantidad, String dosisD, String tiempoT,
												String tipofrecuencia, String unidosis)
	{
		PreparedStatement pst=null; 
		try
		{
			pst = con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigo);
			pst.setString(3, nombre);
			pst.setString(4, dosis);
			pst.setString(5, frecuencia);
			pst.setString(6, fechaInicio);
			pst.setString(7, fechaFin);
			pst.setString(8, observaciones);
		    pst.setString(9, dosisD);
		    pst.setString(10, cantidad);
		    pst.setString(11, tiempoT);
					
			    
		    pst.setString(13, tipofrecuencia);
		    pst.setString(14, unidosis);
			    
			int insert = pst.executeUpdate();
					
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla para el antecedente medicamento "+nombre+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteMedicamentoDao.class);
			logger.warn("Hubo problemas insertando el antecedente medicamento "+nombre+", para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando el antecedente medicamento "+nombre+", para el paciente : "+codigoPaciente+". \n"+e);
		}
		return new ResultadoBoolean(true);		
	}
	

	/**
	 * @param con
	 * @param codigoPaciente
	 * @param codigo
	 * @param dosis
	 * @param frecuencia
	 * @param fechaInicio
	 * @param fechaFin
	 * @param observaciones
	 * @param cantidad
	 * @param dosisD
	 * @param tiempoT
	 * @param tipofrecuencia
	 * @param unidosis
	 * @return
	 */
	public static ResultadoBoolean modificar(	Connection con,
												int codigoPaciente,
												int codigo,
												String dosis,
												String frecuencia,
												String fechaInicio,
												String fechaFin,
												String observaciones, String cantidad, String dosisD, String tiempoT, 
												String tipofrecuencia, String unidosis)
	{
		try
		{
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			modificar.setString(1, dosis);
			modificar.setString(2, frecuencia);
			modificar.setString(3, fechaInicio);
			modificar.setString(4, fechaFin);
			modificar.setString(5, observaciones);
			modificar.setString(6, dosisD);
			modificar.setObject(7, cantidad);
			modificar.setString(8, tiempoT);

		    modificar.setString(9, tipofrecuencia);
		    modificar.setString(10, unidosis);
			
			
			modificar.setInt(11, codigoPaciente);
			modificar.setInt(12, codigo);

			int update = modificar.executeUpdate();
					
			if( update == 0 )
				return new ResultadoBoolean(false, "No se modificó ningún registro en la tabla para el antecedente medicamento "+codigo+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteMedicamentoDao.class);
			logger.error("Problemas modificando el antecedente medicamento código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
			return new ResultadoBoolean(false, "Problemas modificando el antecedente medicamento código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
		}
		return new ResultadoBoolean(true);
	}


	/**
	 * @param con
	 * @param codigoPaciente
	 * @param codigo
	 * @return	 */
	public static ResultadoBoolean existeAntecedente(	Connection con, 
														int codigoPaciente, 
														int codigo)
	{
		try
		{	
			PreparedStatementDecorator existeAntecedente =  new PreparedStatementDecorator(con.prepareStatement(existeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			existeAntecedente.setInt(1, codigoPaciente);
			existeAntecedente.setInt(2, codigo);
			
			ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedente.executeQuery());
			
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
				final Logger logger = Logger.getLogger(SqlBaseAntecedenteMedicamentoDao.class);
				logger.warn("Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch( Exception e )
		{
			final Logger logger = Logger.getLogger(SqlBaseAntecedenteMedicamentoDao.class);
			logger.error("No se pudo hacer la consulta, para saber si ya existia un antecedente medicamento con código "+codigo);
			return new ResultadoBoolean(false, "No se pudo hacer la consulta, para saber si ya existia un antecedente medicamento con código "+codigo+". "+e);
		}
	}

	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @param codigo
	 * @param dosis
	 * @param frecuencia
	 * @param fechaInicio
	 * @param fechaFin
	 * @param observaciones
	 * @param estado
	 * @param cantidad
	 * @param dosisD
	 * @param tiempoT
	 * @param tipofrecuencia
	 * @param unidosis
	 * @return
	 * @throws SQLException	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con,
			int codigoPaciente,
			int codigo,
			String dosis,
			String frecuencia,
			String fechaInicio,
			String fechaFin,
			String observaciones,
			String estado, String cantidad, String dosisD, String tiempoT,
			String tipofrecuencia, String unidosis) throws SQLException
	{

	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
	    {
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean (false, "No se pudo empezar la transacción");
		    }
	    }
	    
	    logger.info("cantidad>>>>>>>>>>"+codigo);
	    logger.info("cantidad>>>>>>>>>>"+codigoPaciente);

	    try
	    {
		    PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		    modificar.setString(1, dosis);
		    modificar.setString(2, frecuencia);
		    modificar.setString(3, fechaInicio);
		    modificar.setString(4, fechaFin);
		    modificar.setString(5, observaciones);
			modificar.setString(6, dosisD);

			if(!UtilidadTexto.isEmpty(cantidad))
				modificar.setObject(7, cantidad);
			else
				modificar.setObject(7,null);
			modificar.setString(8, tiempoT);
			
		    modificar.setString(9, tipofrecuencia);
		    modificar.setString(10, unidosis);

			
		    modificar.setInt(11, codigoPaciente);
		    modificar.setInt(12, codigo);

		    int update = modificar.executeUpdate();

		    if( update == 0 )
		    {
			    myFactory.abortTransaction(con);
			    return new ResultadoBoolean (false, "Transacción abortada");
		    }
	    }
	    catch(SQLException e)
	    {
		    logger.error("Problemas modificando el antecedente medicamento código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
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