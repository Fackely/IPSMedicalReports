package com.princetonsa.dao.postgresql.administracion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.TiposMonedaDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseTiposMonedaDao;


/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class PostgresqlTiposMonedaDao implements TiposMonedaDao {

	/**
	 * Consultar los tipod de monedas
	 */
	public HashMap consultarTiposMoneda(Connection con) 
	{
		return SqlBaseTiposMonedaDao.consultarTiposMoneda(con);
	}

	/**
	 * Eliminar los tipos de monedas
	 */
	public boolean eliminarTiposMoneda(Connection con, int codigo) 
	{
		return SqlBaseTiposMonedaDao.eliminarTiposMoneda(con, codigo);
	}

	/**
	 * Insertar los tipos de monedas
	 */
	public boolean insertarTiposMoneda(Connection con, HashMap vo) 
	{
		return SqlBaseTiposMonedaDao.insertarTiposMoneda(con, vo, DaoFactory.POSTGRESQL);
	}

	/**
	 * Modificar los tipos de monedas
	 */
	public boolean modificarTiposMoneda(Connection con, HashMap vo) 
	{
		return SqlBaseTiposMonedaDao.modificarTiposMoneda(con, vo, DaoFactory.POSTGRESQL);
	}

}
