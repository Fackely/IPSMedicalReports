/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.postgresql.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.RegistroSaldosInicialesCapitacionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseRegistroSaldosInicialesCapitacionDao;

/**
 * Implementación postgres de las funciones de acceso a la fuente de datos
 *
 * @version 1.0, 08/08/2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class PostgresqlRegistroSaldosInicialesCapitacionDao implements RegistroSaldosInicialesCapitacionDao
{
	/**
	 * busqueda de las cuentas de cobro 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 * @throws SQLException 
	 * @throws SQLException
	 */	
	public HashMap busquedaCuentasCobro(	Connection con,
												HashMap criteriosBusquedaMap
											  ) throws SQLException
	{
		return SqlBaseRegistroSaldosInicialesCapitacionDao.busquedaCuentasCobro(con, criteriosBusquedaMap);
	}
	
	/**
	 * metodo que carga en el mapa los cargues que tienen saldo pendiente = 0 ó ajustes sin aprobar
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public HashMap existenCarguesSaldoPendienteCeroOAjustesSinAprobar(Connection con, HashMap criteriosBusquedaMap)
	{
		return SqlBaseRegistroSaldosInicialesCapitacionDao.existenCarguesSaldoPendienteCeroOAjustesSinAprobar(con, criteriosBusquedaMap);
	}
}
