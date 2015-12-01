/*
 * Creado en May 31, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;


public class SqlBaseUnidadMedidaDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUnidadMedidaDao.class);
	
	/**
	 * Método que cargar las unidades de medida parametrizadas a la institución
	 * @param con
	 * @return HashMap
	 */
	public static HashMap consultarUnidadesMedidaInstitucion(Connection con)
	{
		String consultaStr="SELECT acronimo AS acronimo, acronimo AS acronimoant, nombre AS unidad, nombre AS unidadant, " +
											"				unidosis AS unidosis, unidosis AS unidosisant, activo AS activo, activo AS activoant, 1 AS esta_grabado" +
											"		 FROM unidad_medida " +
											" 				ORDER BY unidad";	
						
			try
			{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("consulta->"+consultaStr);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
			}
			catch (SQLException e)
			{
			logger.error("Error en consultarUnidadesMedidaInstitucion de SqlBaseUnidadMedidaDao: "+e);
			return null;
			}
	}
	
	/**
	 * Método que guarda las nuevas unidades de medida o modifica
	 * las unidades de medida ya ingresadas 
	 * @param con
	 * @param acronimo
	 * @param unidad
	 * @param unidosis
	 * @param activo
	 * @param acronimoAnt
	 * @param esInsertar
	 * @return
	 */
	public static int insertarModificarUnidadMedida(Connection con, String acronimo, String unidad, boolean unidosis, boolean activo, String acronimoAnt, boolean esInsertar)
	{
		PreparedStatementDecorator ps;
		String consultaStr="";
		int resp=ConstantesBD.codigoNuncaValido;
		
		try
		{
			if (esInsertar)
				{
				consultaStr="INSERT INTO unidad_medida (acronimo, nombre, unidosis, activo) " +
																" VALUES (?, ?, ?, ?)" ;
						
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, acronimo);
				ps.setString(2, unidad);
				ps.setBoolean(3, unidosis);
				ps.setBoolean(4, activo);
				}
			else
				{
				consultaStr="UPDATE unidad_medida set acronimo=?, nombre=?, unidosis=?, activo=? " +
											" WHERE acronimo = ? ";

				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, acronimo);
				ps.setString(2, unidad);
				ps.setBoolean(3, unidosis);
				ps.setBoolean(4, activo);
				ps.setString(5, acronimoAnt);
				}
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de datos en insertarModificarUnidadMedida : SqlBaseUnidadMedidaDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
	/**
	 * Método que elimina el registro seleccionado de la unidad de medida
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static int eliminarUnidadMedida(Connection con, String acronimo)
	{
		PreparedStatementDecorator ps;
		int resp=ConstantesBD.codigoNuncaValido;
		
		try
		{
			String	consultaStr="DELETE FROM unidad_medida " +
													"WHERE acronimo = ?" ;
					
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, acronimo);
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la eliminación del registro de Unidad de Medida: SqlBaseUnidadMedidaDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
}
