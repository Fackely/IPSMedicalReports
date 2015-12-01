/*
 * Creado 30/03/07
 */

package com.sies.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sies.dao.sqlbase.SqlBaseCategoriaDao;
import com.sies.dao.sqlbase.SqlBaseOcupacionDao;
import com.sies.dao.OcupacionDao;

/**
 * @author mono
 */

public class PostgresqlOcupacionDao implements OcupacionDao{
	
	private static Logger logger = Logger.getLogger(SqlBaseCategoriaDao.class);
	
	/**
	 * Se definió esta sentencia aquí, debido a que en Oracle y Postgresql son diferentes 
	 */
	private static String consultaOcupacionExisteStr="SELECT getConsultarOcupacion (?) AS existe";
	
	/**
	 * Metodo que inserta una ocupacional sistema
	 */
	public int insertarOcupacion (Connection con, int codigo, String nombre)
	{
		return SqlBaseOcupacionDao.insertarOcupacion(con,codigo,nombre);
	}
	
	/**
	 * Permite modificar una ocupacion
	 */
	public void modificarOcupacion (Connection con, int codigo, String nombre)
	{
		SqlBaseOcupacionDao.modificarOcupacion(con,codigo,nombre);
	}
	
	/**
	 * Consulta las ocupaciones existentes en la B.D
	 */
	public Collection<HashMap<String, Object>> consultarOcupacion(Connection con)
	{
		return SqlBaseOcupacionDao.consultarOcupacion(con);
	}
	
	/**
	 * Metodo que permite eliminar una ocupacion enc aso de que no tenga ninguna relacion con otras tablas
	 */
	public int eliminarOcupacion (Connection con, int codigo)
	{
		return SqlBaseOcupacionDao.eliminarOcupacion(con,codigo);
	}
	
	/**
	 * Metodo que consulta si una ocupacion existe
	 * @param con
	 * @param nombre
	 */
	public boolean consultaOcupacionExiste(Connection con, String nombre)
	{
		try
		{
			PreparedStatement consultaOcupacionExisteStatement = con.prepareStatement(consultaOcupacionExisteStr);
			consultaOcupacionExisteStatement.setString(1, nombre);
			ResultSet rs = consultaOcupacionExisteStatement.executeQuery();
			if(rs.next())
			{
				return rs.getBoolean("existe");
			}
			else
				return false;
		}
		catch (SQLException e)
		{
			logger.error("Error Al consultar las ocupaciones existentes: "+e);
			return false;
		}
	}
	
	/*************************** ASIGNAR SALARIO BASE A OCUPACIONES *******************************/
	 
	/**
	 * Metodo que modifica los datos de una ocupacion
	 */
	public void modificarOcupacionSalarioBase (Connection con, int ocupacion, int valor, String fechaInicio, String fechaFin, int tipoVinculacion)
	{
		SqlBaseOcupacionDao.modificarOcupacionSalarioBase(con,ocupacion,valor,fechaInicio,fechaFin,tipoVinculacion);
	}
	
	/**
	 * Metodo que consulta una coleccionde ocupaciones alamacenadas en la B.D.
	 */
	public Collection consultarOcupacionSalarioBase (Connection con, int ocupacion)
	{
		return SqlBaseOcupacionDao.consultarOcupacionSalarioBase(con,ocupacion);
	}
	
	/**
	 * Metodo que lista los tipos de vinculaciones que estan almacenados en la B.D.
	 */
	public Collection<HashMap<String, Object>> listadoTiposVinculaciones(Connection con)
	{
		return SqlBaseOcupacionDao.listadoTiposVinculaciones(con);
	}

	/**
	 * Lista los tipos de turnos que tiene el sistema
	 */
	public Collection<HashMap<String, Object>> listadoTiposTurnos (Connection con)
	{
		return SqlBaseOcupacionDao.listadoTiposTurnos(con);
	}
}



