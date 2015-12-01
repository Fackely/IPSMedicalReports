/*
 * Enero 18, 2006
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Unidades Funcionales
 */
public class SqlBaseUnidadFuncionalDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUnidadFuncionalDao.class);
	
	private static final String consultarSELECT_Str = " SELECT " +
		"acronimo AS codigo, " +
		"descripcion AS descripcion, " +
		"activo AS activo, " +
		"institucion AS institucion, " +
		"'false' AS esta_usado, " + //campo para verificar si el registro está siendo usado en otras tablas
		"'true' AS existe "+ //campo para indicar que es un registro existente
		"FROM unidades_funcionales ";
	/**
	 * Cadena que consulta todas las unidades funcionales
	 * de la institución
	 */
	private static final String consultarListadoStr = consultarSELECT_Str  +
		" WHERE institucion = ? ORDER BY descripcion ";
	
	/**
	 * Cadena que consulta los datos de una unidad funcional
	 */
	private static final String consultarStr = consultarSELECT_Str + 
		" WHERE institucion = ? AND acronimo = ? ";
	
	/**
	 * Cadena que inserta un nuevo registro en unidades funcionales
	 */
	private static final String insertarStr = " INSERT INTO " +
		"unidades_funcionales (acronimo,descripcion,activo,institucion) " +
		"VALUES(?,?,?,?)";
	
	/**
	 * Cadena que modifica una unidad funcional
	 */
	private static final String modificarStr = "UPDATE unidades_funcionales " +
		"SET descripcion = ?, activo = ? WHERE acronimo = ? AND institucion = ?";
	
	/**
	 * Cadena que elimina una unidad funcional
	 */
	private static final String eliminarStr = " DELETE FROM unidades_funcionales " +
		"WHERE acronimo = ? AND institucion = ?";
	
	
	/**
	 * Método implementado para consultar todas la unidades funcionales 
	 * de la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultar(Connection con,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarListadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()) );
			pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar de SqlBaseUnidadFuncionalDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta los dats de una unidad funcional
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static HashMap consultar(Connection con,String codigo,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			pst.setString(2,codigo);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),false,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar(2) de SqlBaseUnidadFuncionalDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para insertar una nueva unidad funcional
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int insertar(Connection con,String codigo,String descripcion,boolean activo,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigo);
			pst.setString(2,descripcion);
			pst.setBoolean(3,activo);
			pst.setInt(4,institucion);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertar de SqlBaseUnidadFuncionalDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para modificar una unidad funcional
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int modificar(Connection con,String codigo,String descripcion,boolean activo,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,descripcion);
			pst.setBoolean(2,activo);
			pst.setString(3,codigo);
			pst.setInt(4,institucion);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar de SqlBaseUnidadFuncionalDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para eliminar una unidad funcional
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static int eliminar(Connection con,String codigo,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigo);
			pst.setInt(2,institucion);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminar de SqlBaseUnidadFuncionalDao: "+e);
			return -1;
		}
	}
	
	
}
