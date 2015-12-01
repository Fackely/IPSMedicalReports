/*
 * Created on 01-abr-2004
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
 * @author juanda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseAntecedenteTransfusionalDao
{
	private static Logger logger = Logger.getLogger(SqlBaseAntecedenteTransfusionalDao.class);
	
	
	private static String existeStr = "SELECT COUNT(*) AS antecedente "
											 + "FROM ant_transfusionales "
											 + "WHERE codigo_paciente = ? "
											 + "AND codigo = ? ";

	public static ResultadoBoolean insertar(Connection con,
												int codigoPaciente,
												int codigo,
												String componente,
												String fechaTransf,
												String causa,
												String lugar,
												String edad,
												String donante,
												String observaciones)
	{
		try
		{
			String insertarStr = "INSERT INTO ant_transfusionales "
				+ "(codigo_paciente, "
				+ "codigo, "
				+ "componente_transfundido, "
				+ "fecha_transfusion, "
				+ "causa, "
				+ "lugar, "
				+ "edad_paciente, "
				+ "donante, "
				+ "observaciones, fecha, hora) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			insertar.setInt(1, codigoPaciente);
			insertar.setInt(2, codigo);
			insertar.setString(3, componente);
			insertar.setString(4, fechaTransf);
			insertar.setString(5, causa);
			insertar.setString(6, lugar);
			insertar.setString(7, edad);
			insertar.setString(8, donante);
			insertar.setString(9, observaciones);
					
			int insert = insertar.executeUpdate();
					
			if( insert == 0 )
				return new ResultadoBoolean(false, "No se insertó ningún registro en la tabla para el antecedente transfusional "+componente+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando el antecedente transfucional "+componente+", para el paciente : "+codigoPaciente+". \n"+e);
			return new ResultadoBoolean(false, "Hubo problemas insertando el antecedente transfucional "+componente+", para el paciente : "+codigoPaciente+". \n"+e);
		}
		return new ResultadoBoolean(true);		

	}

	public static ResultadoBoolean modificar(	Connection con,
													int codigoPaciente,
													int codigo,
													String fechaTransf,
													String causa,
													String lugar,
													String edad,
													String donante,
													String observaciones)
	{		
		try
		{
			
			String modificarStr = "UPDATE ant_transfusionales "
														  + "SET fecha_transfusion = ?, "
														  + "causa = ?, "
														  + "lugar = ?, "
														  + "edad_paciente = ?, "
														  + "donante = ?, "
														  + "observaciones = ?, fecha = '"+Utilidades.capturarFechaBD()+"', hora = '"+UtilidadFecha.getHoraActual()+"' "
														  + "WHERE codigo_paciente = ? "
														  + "AND codigo = ? ";
			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			modificar.setInt(7, codigoPaciente);
			modificar.setInt(8, codigo);
			modificar.setString(1, fechaTransf);
			modificar.setString(2, causa);
			modificar.setString(3, lugar);
			modificar.setString(4, edad);
			modificar.setString(5, donante);
			modificar.setString(6, observaciones);
					
			int update = modificar.executeUpdate();
					
			if( update == 0 )
				return new ResultadoBoolean(false, "No se modificó ningún registro en la tabla para el antecedente transfusional "+codigo+", para el paciente : "+codigoPaciente );
		}
		catch(SQLException e)
		{
			logger.error("Problemas modificando el antecedente transfusional código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
			return new ResultadoBoolean(false, "Problemas modificando el antecedente transfusional código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
		}
		return new ResultadoBoolean(true);
	}

	public static ResultadoBoolean existeAntecedente(	Connection con, 
																	int codigoPaciente, 
																	int codigo)
	{
		try
		{
			logger.debug("Se va a consultar si existe un antecedente transfusional con los siguientes parametros, numeroId "+codigoPaciente+", código antecedente "+codigo);
	
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
				logger.warn("Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
				return new ResultadoBoolean(false, "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente "+codigo+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
			}
		}
		catch( Exception e )
		{
			logger.error("No se pudo hacer la consulta, para saber si ya existia un antecedente transfusional con código "+codigo);
			return new ResultadoBoolean(false, "No se pudo hacer la consulta, para saber si ya existia un antecedente transfusional con código "+codigo+". "+e);
		}
	}

	public static ResultadoBoolean insertarTransaccional(	Connection con,
									int codigoPaciente,
									int codigo,
									String componente,
									String fechaTransf,
									String causa,
									String lugar,
									String edad,
									String donante,
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
			String insertarStr = "INSERT INTO ant_transfusionales "
				+ "(codigo_paciente, "
				+ "codigo, "
				+ "componente_transfundido, "
				+ "fecha_transfusion, "
				+ "causa, "
				+ "lugar, "
				+ "edad_paciente, "
				+ "donante, "
				+ "observaciones, fecha, hora) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
			
			PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			insertar.setInt(1, codigoPaciente);
			insertar.setInt(2, codigo);
			insertar.setString(3, componente);
			insertar.setString(4, fechaTransf);
			insertar.setString(5, causa);
			insertar.setString(6, lugar);
			insertar.setString(7, edad);
			insertar.setString(8, donante);
			insertar.setString(9, observaciones);

			int insert = insertar.executeUpdate();

			logger.warn("en insertar transaccional  "+insert);

			if( insert == 0 )
			{
				myFactory.abortTransaction(con);
				return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Hubo problemas insertando el antecedente transfucional "+componente+", para el paciente : "+codigoPaciente+". \n"+e);
			myFactory.abortTransaction(con);
			return new ResultadoBoolean (false, "Transacción abortada");
		}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}		
		return new ResultadoBoolean(true);				

	}

	public static ResultadoBoolean modificarTransaccional(	Connection con,
		int codigoPaciente,
		int codigo,
		String fechaTransf,
		String causa,
		String lugar,
		String edad,
		String donante,
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
			String modificarStr = "UPDATE ant_transfusionales "
				  + "SET fecha_transfusion = ?, "
				  + "causa = ?, "
				  + "lugar = ?, "
				  + "edad_paciente = ?, "
				  + "donante = ?, "
				  + "observaciones = ?, fecha = '"+Utilidades.capturarFechaBD()+"', hora = '"+UtilidadFecha.getHoraActual()+"' "
				  + "WHERE codigo_paciente = ? "
				  + "AND codigo = ? ";
			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			modificar.setInt(7, codigoPaciente);
			modificar.setInt(8, codigo);
			modificar.setString(1, fechaTransf);
			modificar.setString(2, causa);
			modificar.setString(3, lugar);
			modificar.setString(4, edad);
			modificar.setString(5, donante);
			modificar.setString(6, observaciones);
			
			int update = modificar.executeUpdate();
		
			if( update == 0 )
			{
				myFactory.abortTransaction(con);
				return new ResultadoBoolean (false, "Transacción abortada");
			}
		}
		catch(SQLException e)
		{
			logger.error("Problemas modificando el antecedente transaccional código "+codigo+" al paciente "+codigoPaciente+". El problema fue : "+e);
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
