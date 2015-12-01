/**
 * 
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ServiciosSircDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseServiciosSircDao;

/**
 * @author axioma
 *
 */
public class PostgresqlServiciosSircDao implements ServiciosSircDao {

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.historiaClinica.ServiciosSircDao#consultarServicioSirc(java.sql.Connection, java.util.HashMap)
	 */
	public HashMap consultarServicioSirc(Connection con, HashMap vo) {

		return SqlBaseServiciosSircDao.consultarServicioSirc(con,vo);		
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.historiaClinica.ServiciosSircDao#eliminarRegistro(java.sql.Connection, java.lang.String)
	 */
	public boolean eliminarRegistro(Connection con, int codigo, int institucion) {
		return SqlBaseServiciosSircDao.eliminarRegistro(con,codigo,institucion);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.historiaClinica.ServiciosSircDao#insertarServicioSirc(java.sql.Connection, java.util.HashMap)
	 */
	public boolean insertarServicioSirc(Connection con, HashMap vo) {
		
		return SqlBaseServiciosSircDao.insertarServicioSirc(con,vo);		
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.historiaClinica.ServiciosSircDao#modificarServicioSirc(java.sql.Connection, java.util.HashMap)
	 */
	public boolean modificarServicioSirc(Connection con, HashMap vo) {
		return SqlBaseServiciosSircDao.modificarServicioSirc(con,vo);
	}
	
	/**
	 * Consulta de Servicios Sirc Detalle
	 * @param Connection con
	 * @param HashMap vo 
	 * **/
	public HashMap consultarServicioSircDetalle(Connection con, HashMap vo)
	{
		return SqlBaseServiciosSircDao.consultarServicioSircDetalle(con, vo);		
	}
	
	
	/**
	 * Inserta un registro Detalle de Servicios Sirc
	 * HashMap vo 
	 * **/
	public boolean insertarServiciosSircDetalle(Connection con, HashMap vo)
	{
		return SqlBaseServiciosSircDao.insertarServiciosSircDetalle(con, vo);
	}
	
	/**
	 * Eliminar un registro Detalle de Servicios Sirc
	 * @param Connection con
	 * @param HashMap vo
	 * */
	public boolean eliminarServiciosSircDetalle(Connection con, HashMap vo)
	{
		return SqlBaseServiciosSircDao.eliminarServiciosSircDetalle(con, vo);
	}
}
