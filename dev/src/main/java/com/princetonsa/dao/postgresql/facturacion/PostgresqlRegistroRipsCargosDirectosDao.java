/*
 * 11 Abril, 2008
 */
package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.RegistroRipsCargosDirectosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseRegistroRipsCargosDirectosDao;

/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Registro Rips Cargos Directos
 */
public class PostgresqlRegistroRipsCargosDirectosDao implements
		RegistroRipsCargosDirectosDao 
{
	/**
	 * M�todo para consultar el listado de cuentas que tienen soolicitudes de cargos directos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> listadoCuentas(Connection con,HashMap campos)
	{
		return SqlBaseRegistroRipsCargosDirectosDao.listadoCuentas(con, campos);
	}
	
	/**
	 * M�todo que consulta el listado de solicitudes de cargos directos de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> listadoSolicitudes(Connection con,HashMap campos)
	{
		return SqlBaseRegistroRipsCargosDirectosDao.listadoSolicitudes(con, campos);
	}
}
