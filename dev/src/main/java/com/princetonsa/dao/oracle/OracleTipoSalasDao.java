/*
 * Created on Sep 1, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.TipoSalasDao;
import com.princetonsa.dao.sqlbase.SqlBaseTipoSalasDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Parametrización Tipos de Salas
 */
public class OracleTipoSalasDao implements TipoSalasDao {
	
	/**
	 * Cadena usada para insertar un nuevo tipo de sala
	 */
	private static final String insertarTipoSalaStr=" INSERT INTO tipos_salas " +
													" (codigo," +
													" descripcion, " +
													" es_quirurgica," +
													" es_urgencias," +
													" institucion) " +
													" VALUES(seq_tipos_salas.nextval,?,?,?,?)";
	
	/**
	 * Método usado para cargar todos los tipos de salas existentes
	 * según la institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarTiposSalas(Connection con,int institucion)
	{
		return SqlBaseTipoSalasDao.cargarTiposSalas(con,institucion);
	}
	
	/**
	 * Método usado para ingresar al sistema un nuevo tipo de sala
	 * @param con
	 * @param descripcion
	 * @param urgencias
	 * @param quirurgica
	 * @param institucion
	 * @return
	 */
	public int insertarTipoSala(Connection con,String descripcion,boolean quirurgica, boolean urgencias, int institucion)
	{
		return SqlBaseTipoSalasDao.insertarTipoSala(con,descripcion,quirurgica,urgencias,institucion,insertarTipoSalaStr);
	}
	
	/**
	 * Método usado para actualizar los datos de un registro
	 * en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param quirurgica
	 * @return
	 */
	public int actualizarTipoSala(Connection con,int codigo,String descripcion,boolean quirurgica, boolean urgencias)
	{
		return SqlBaseTipoSalasDao.actualizarTipoSala(con,codigo,descripcion,quirurgica, urgencias);
	}
	
	/**
	 * Método usado para eliminar un registro en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarTipoSala(Connection con,int codigo)
	{
		return SqlBaseTipoSalasDao.eliminarTipoSala(con,codigo);
	}
	
	/**
	 * Método usado para cargar los datos de un tipo de sala
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarTipoSala(Connection con,int codigo)
	{
		return SqlBaseTipoSalasDao.cargarTipoSala(con,codigo);
	}
	
	/**
	 * Método encargado de consultar los datos tipo salas que estén usados en grupo servicios
	 * @param Connection con
	 * @param int codigo
	 * @return HashMap
	 */
	public HashMap consultaTipoSalasGruposServicios(Connection con, int codigo)
	{
		return SqlBaseTipoSalasDao.consultaTipoSalasGruposServicios(con,codigo);
	}
	
}
