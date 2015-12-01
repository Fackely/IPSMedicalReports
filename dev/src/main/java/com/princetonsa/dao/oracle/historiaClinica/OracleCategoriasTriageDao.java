/*
 * @author armando
 */
package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.CategoriasTriageDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseCategoriasTriageDao;

/**
 * 
 * @author artotor
 *
 */
public class OracleCategoriasTriageDao implements CategoriasTriageDao 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarInformacion(Connection con, int institucion)
	{ 
		return SqlBaseCategoriasTriageDao.cargarInformacion(con, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivo)
	{
		return SqlBaseCategoriasTriageDao.eliminarRegistro(con,consecutivo);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @param destino
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int color)
	{
		return SqlBaseCategoriasTriageDao.existeModificacion(con,consecutivo,codigo,descripcion,color);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public ResultSetDecorator cargarCategoriaTriage(Connection con, int consecutivo)
	{
		return SqlBaseCategoriasTriageDao.cargarCategoriaTriage(con,consecutivo);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @param destino
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, String codigo, String descripcion, int color)
	{
		return SqlBaseCategoriasTriageDao.modificarRegistro(con,consecutivo,codigo,descripcion,color);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @param destino
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, int color)
	{ 
		String cadena="insert into categorias_triage (consecutivo,codigo,institucion,nombre,color) values (seq_categorias_triage.nextval,?,?,?,?)";
		return SqlBaseCategoriasTriageDao.insertarRegistro(con,codigo,institucion,descripcion,color,cadena);
	}
	
	/**
	 * 
	 * @param con
	 * @param categoria
	 * @param destinos
	 */
	public boolean actualizarRelacionesCategoriasDestinos(Connection con, int categoria, HashMap destinos)
	{
		return SqlBaseCategoriasTriageDao.actualizarRelacionesCategoriasDestinos(con,categoria,destinos);
	}


	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public int obtenerConsecutivoCategoriaTriage(Connection con, String codigo, int institucion, String descripcion, int color)
	{
		return SqlBaseCategoriasTriageDao.obtenerConsecutivoCategoriaTriage(con,codigo,institucion,descripcion,color);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param destinos
	 * @return
	 */
	public boolean insertarRelacionesCategoriasDestinos(Connection con, int consecutivo, HashMap destinos)
	{
		return SqlBaseCategoriasTriageDao.insertarRelacionesCategoriasDestinos(con,consecutivo,destinos);
	}
	

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap cargarRelacionesDestionsCategorias(Connection con, String consecutivo)
	{
		return SqlBaseCategoriasTriageDao.cargarRelacionesDestionsCategorias(con,consecutivo);
	}
}
