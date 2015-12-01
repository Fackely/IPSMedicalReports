package com.princetonsa.dao.postgresql.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;


import com.princetonsa.dao.pyp.TiposProgamaDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseTiposProgamaDao;

/**
 * 
 * @author armando
 *
 */
public class PostgresqlTiposProgamaDao implements TiposProgamaDao 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInfomacionBD(Connection con, int institucion)
	{
		return SqlBaseTiposProgamaDao.cargarInfomacionBD(con,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo)
	{
		return SqlBaseTiposProgamaDao.insertarRegistro(con,codigo,institucion,descripcion,activo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public boolean modificarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo)
	{
		return SqlBaseTiposProgamaDao.modificarRegistro(con,codigo,institucion,descripcion,activo);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public  boolean existeModificacion(Connection con, String codigo, int institucion, String descripcion, boolean activo)
	{
		return SqlBaseTiposProgamaDao.existeModificacion(con,codigo,institucion,descripcion,activo);		
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarTipoPrograma(Connection con, String codigo, int institucion)
	{
		return SqlBaseTiposProgamaDao.cargarTipoPrograma(con,codigo,institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String codigo, int institucion)
	{
		return SqlBaseTiposProgamaDao.eliminarRegistro(con,codigo,institucion);
	}
}
