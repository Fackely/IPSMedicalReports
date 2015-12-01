package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Antecedente Mórbido Médico
 *
 *	@version 1.0, Mar 29, 2004
 */

public class SqlBaseAntecedenteMorbidoMedicoDao 
{

    private static Logger logger = Logger.getLogger(SqlBaseAntecedenteMorbidoMedicoDao.class);
    
																  
	private static String existePredefinidoStr = "SELECT COUNT(*) AS antecedente "
																		+ "FROM ant_morbidos_medicos "
																		+ "WHERE codigo_paciente = ? "
																		+ "AND tipo_antecedente_medico = ? ";
																		
																	  
	private static String existeOtroStr = "SELECT COUNT(*) AS antecedente "
																+ "FROM ant_morbido_med_otros "
																+ "WHERE codigo_paciente = ? "
																+ "AND codigo = ? ";
		
	
		/**
		 * Retorna true si ya ha sido ingresado un registro para el paciente dado,
		 * del antecedente mórbido predefinido dado.
		 * @param 	Connection, con. Conexión con la base de datos
		 * @param 	String, codigoPaciente. código en la fuente de datos del
		 * paciente.
		 * @param 	int, codigo. Código en la base de datos del antecedente
		 * 					mórbido predefinido
		 * @return 		Resultado, retorna true si ya existia en la base de datos
		 * 					false de lo contrario con su descripción.
		 */
		public static ResultadoBoolean existeAntecedentePredefinido(	Connection con, 
																										int codigoPaciente, 
																										int codigo)
		{
			PreparedStatement pst=null;
			ResultSet rs=null;
			ResultadoBoolean resultado=null;
			try
			{			
				pst = con.prepareStatement(existePredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, codigoPaciente);
				pst.setInt(2, codigo);
				
				rs = pst.executeQuery();
				if( rs.next() )
				{
					int numFilas = rs.getInt("antecedente");
					
					if( numFilas <= 0 )
					{
						resultado= new ResultadoBoolean(false);
					}
					else
					{
						resultado= new ResultadoBoolean(true);
					}
				}
				else
				{
					resultado=new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				}
			}
			catch(Exception e)
		    {
			    logger.warn("Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n", e);
			    resultado=new ResultadoBoolean (false, "Transacción abortada");
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
		    return resultado;
		}

	

		/**
		 * Retorna true si ya ha sido ingresado un registro para el paciente dado,
		 * del antecedente mórbido predefinido dado.
		 * @param 	Connection, con. Conexión con la base de datos
		 * @param 	String, codigoPaciente. código en la fuente de datos del
		 * paciente.
		 * @param 	int, codigo. Código en la base de datos del antecedente
		 * 					mórbido predefinido
		 * @return 		Resultado, retorna true si ya existia en la base de datos
		 * 					false de lo contrario con su descripción.
		 */
		public static ResultadoBoolean existeAntecedenteOtro(	Connection con, 
																				int codigoPaciente, 
																				int codigo)
		{
			PreparedStatement pst=null;
			ResultSet rs=null;
			ResultadoBoolean result=null;
			try
			{	
				pst = con.prepareStatement(existeOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				
				pst.setInt(1, codigoPaciente);
				pst.setInt(2, codigo);
				
				rs = new ResultSetDecorator(pst.executeQuery());
				
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
					result= new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				}
			}
			catch(Exception e)
		    {
			    logger.warn("ERROR nombre metodo", e);
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
		 * Modifica un antecedente mórbido médico de los adicionales existente en la
		 * base de datos. Este antecedente mórbido NO hace parte de los predefinidos
		 * en la base de datos.
		 * @param		Connection, con. Conexión abierta con la fuente de datos 
		 * @param 	String, codigoPaciente. código en la fuente de datos del
		 * paciente.
		 * @param		int, codigo. Código del antecedente mórbido en la bd.
		 * @param 	String, nombre. Nombre del antecedente mórbido médico nuevo 
		 * 					que se va a ingresar en la base de datos, solo para este 
		 * 					paciente.
		 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
		 * 					En formato de fecha.
		 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
		 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
		 * 					para manejar el antecedente.
		 * @param 	String, observaciones. Cadena con las observaciones escritas por
		 * 					el médico.
		 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
		 * 					"empezar", "finalizar" y "continuar". Los estados están 
		 * 					definidos en la interfaz de Constantes util.ConstantesBD.
		 * @return 		Resultado, retorna si la inserción fue realizada con exito
		 * 					"true" o no "false" y su descripción asociada en caso de 
		 * 					ser necesaria.
		 */		
		public static ResultadoBoolean modificarOtroTransaccional(	Connection con,
																						int codigoPaciente,
																						int codigo,
																						String nombre,
																						String fechaInicio,
																						String tratamiento,
																						String restriccionDietaria,
																						String observaciones,
																						String estado ) throws SQLException
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
				String modificarOtroStr = "UPDATE ant_morbido_med_otros "
					  + "SET nombre = ?, "
					  + "fecha_inicio = ?, "
					  + "tratamiento = ?, "
					  + "restriccion_dietaria = ?, "
					  + "observaciones = ?, "
					  + "fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "
					  + "WHERE codigo_paciente = ? "
					  + "AND codigo = ? ";		
				
				PreparedStatementDecorator modificarOtro =  new PreparedStatementDecorator(con.prepareStatement(modificarOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				modificarOtro.setInt(6, codigoPaciente);
				modificarOtro.setInt(7, codigo);
				modificarOtro.setString(1, nombre);
				modificarOtro.setString(2, fechaInicio);
				modificarOtro.setString(3, tratamiento);
				modificarOtro.setString(4, restriccionDietaria);
				modificarOtro.setString(5, observaciones);
				
				int update = modificarOtro.executeUpdate();
				modificarOtro.close();
				if( update == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
			catch(SQLException e)
			{
				logger.error("Problemas modificando el antecedente morbido Otro código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
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
		 * Modifica un antecedente mórbido médico existente en la base de datos.
		 * Este antecedente mórbido hace parte de los predefinidos en la base de
		 * datos. La modificación hace parte de una transacción
		 * @param		Connection, con. Conexión abierta con la fuente de datos 
		 * @param 	String, codigoPaciente. código en la fuente de datos del
		 * paciente.
		 * @param 	int, codigo. Código del antecedente mórbido en la base de datos.
		 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
		 * 					En formato de fecha.
		 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
		 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
		 * 					para manejar el antecedente.
		 * @param 	String, observaciones. Cadena con las observaciones escritas por
		 * 					el médico.
		 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
		 * 					"empezar", "finalizar" y "continuar". Los estados están 
		 * 					definidos en la interfaz de Constantes util.ConstantesBD.
		 * @return 		booelan, retorna si la inserción fue realizada con exito
		 * 					"true" o no "false"
		 */
		public static ResultadoBoolean modificarPredefinidoTransaccional(	Connection con,
																								int codigoPaciente,
																								int codigo,
																								String fechaInicio,
																								String tratamiento,
																								String restriccionDietaria,
																								String observaciones,
																								String estado ) throws SQLException
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
				String modificarPredefinidoStr = "UPDATE  ant_morbidos_medicos "
					 + "SET fecha_inicio = ?, "
					 + "tratamiento = ?, "
					 + "restriccion_dietaria = ?, "
					 + "observaciones = ?, "
					 +"fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "
					 + "WHERE codigo_paciente = ? "
					 + "AND tipo_antecedente_medico = ? ";		
				
				PreparedStatementDecorator modificarPredefinido =  new PreparedStatementDecorator(con.prepareStatement(modificarPredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				modificarPredefinido.setInt(5, codigoPaciente);
				modificarPredefinido.setInt(6, codigo);
				modificarPredefinido.setString(1, fechaInicio);
				modificarPredefinido.setString(2, tratamiento);
				modificarPredefinido.setString(3, restriccionDietaria);
				modificarPredefinido.setString(4, observaciones);
				
				int update = modificarPredefinido.executeUpdate();
				modificarPredefinido.close();
				if( update == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
			catch(SQLException e)
			{
				logger.error("Problemas modificando el antecedente morbido predefinido código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
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
		 * Inserta un nuevo antecedente mórbido médico en la base de datos. Este
		 * antecedente mórbido NO hace parte de los predefinidos en la base de
		 * datos. La inserción de este antecedente mórbido solo se hace para este
		 * paciente, no aparecera en la lista de opciones de los otros.
		 * @param		Connection, con. Conexión abierta con la fuente de datos 
		 * @param 	String, codigoPaciente. código en la fuente de datos del
		 * paciente.
		 * @param 	String, nombre. Nombre del antecedente mórbido médico nuevo 
		 * 					que se va a ingresar en la base de datos, solo para este 
		 * 					paciente.
		 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
		 * 					En formato de fecha.
		 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
		 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
		 * 					para manejar el antecedente.
		 * @param 	String, observaciones. Cadena con las observaciones escritas por
		 * 					el médico.
		 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
		 * 					"empezar", "finalizar" y "continuar". Los estados están 
		 * 					definidos en la interfaz de Constantes util.ConstantesBD.
		 * @return 		booelan, retorna si la inserción fue realizada con exito
		 * 					"true" o no "false" 
		 */		
		public static ResultadoBoolean insertarOtroTransaccional(	Connection con,
																					int codigoPaciente,
																					int codigo,
																					String nombre,
																					String fechaInicio,
																					String tratamiento,
																					String restriccionDietaria,
																					String observaciones,
																					String estado ) throws SQLException
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
				String insertarOtroStr = "INSERT INTO ant_morbido_med_otros "
					   + "( codigo_paciente, "
					   + "codigo, "
					   + "nombre, "
					   + "fecha_inicio, "
					   + "tratamiento, "
					   + "restriccion_dietaria, "
					   + "observaciones, "
					   + "fecha, "
					   + "hora) "
					   + "VALUES (?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
				
				PreparedStatementDecorator insertarOtro =  new PreparedStatementDecorator(con.prepareStatement(insertarOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				insertarOtro.setInt(1, codigoPaciente);
				insertarOtro.setInt(2, codigo);
				insertarOtro.setString(3, nombre);
				insertarOtro.setString(4, fechaInicio);
				insertarOtro.setString(5, tratamiento);
				insertarOtro.setString(6, restriccionDietaria);
				insertarOtro.setString(7, observaciones);
				
				int insert = insertarOtro.executeUpdate();
				insertarOtro.close();
				if( insert == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
			catch(SQLException e)
			{
				logger.error("Hubo problemas insertando el antecedente morbido médico otro "+nombre+", para el paciente : "+codigoPaciente+". \n"+e);
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
		 * Inserta un nuevo antecedente mórbido médico en la base de datos. Este
		 * antecedente mórbido hace parte de los predefinidos en la base de datos.
		 * @param		Connection, con. Conexión abierta con la fuente de datos 
		 * @param 	String, codigoPaciente. código en la fuente de datos del
		 * paciente.
		 * @param 	int, codigo. Código del antecedente mórbido en la base de datos.
		 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
		 * 					En formato de fecha.
		 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
		 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
		 * 					para manejar el antecedente.
		 * @param 	String, observaciones. Cadena con las observaciones escritas por
		 * 					el médico.
		 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
		 * 					"empezar", "finalizar" y "continuar"
		 * @return 		Resultado, retorna si la inserción fue realizada con exito
		 * 					"true" o no "false" y su descripción asociada en caso de 
		 * 					ser necesaria.
		 */	
		public static ResultadoBoolean insertarPredefinidoTransaccional (Connection con, 
																										int codigoPaciente, 
																										int codigo, 
																										String fechaInicio, 
																										String tratamiento, 
																										String restriccionDietaria, 
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
				 String insertarPredefinidoStr = "INSERT INTO ant_morbidos_medicos "
					  + "(codigo_paciente, "
					  + "tipo_antecedente_medico, "
					  + "fecha_inicio, "
					  + "tratamiento, "
					  + "restriccion_dietaria, "
					  + "observaciones, "
					  + "fecha, "
					  + "hora) "
					  + "VALUES (?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
				 
				PreparedStatementDecorator insertarPredefinido =  new PreparedStatementDecorator(con.prepareStatement(insertarPredefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				insertarPredefinido.setInt(1, codigoPaciente);
				insertarPredefinido.setInt(2, codigo);
				insertarPredefinido.setString(3, fechaInicio);
				insertarPredefinido.setString(4, tratamiento);
				insertarPredefinido.setString(5, restriccionDietaria);
				insertarPredefinido.setString(6, observaciones);
				
				int insert = insertarPredefinido.executeUpdate();
				insertarPredefinido.close();
				if( insert == 0 )
				{
				    myFactory.abortTransaction(con);
				    return new ResultadoBoolean (false, "Transacción abortada");
				}
			}
			catch(SQLException e)
			{
				logger.warn("Hubo problemas insertando el antecedente morbido médico predefinido "+codigo+", para el paciente : "+codigoPaciente+". \n"+e);
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
