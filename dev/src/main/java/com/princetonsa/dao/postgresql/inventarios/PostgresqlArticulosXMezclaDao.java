/**
 * Juan David Ramírez 31/05/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.inventarios.ArticulosXMezclaDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArticulosXMezclaDao;

/**
 * @author Juan David Ramírez
 *
 */
public class PostgresqlArticulosXMezclaDao implements ArticulosXMezclaDao
{
	/**
	 * Método para consultar lkas opciones de los selectores de la funcionalidad
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoConsulta
	 * @return
	 */
	public Collection consultarTipos(Connection con, int codigoInstitucion, int tipoConsulta)
	{
		return SqlBaseArticulosXMezclaDao.consultarTipos(con, codigoInstitucion, tipoConsulta);
	}

	/**
	 * Método para ingresar o modificar los datos de
	 * los artículos por mezcla
	 * @param con
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public int ingresarModificar(Connection con, Vector articulosIngresados, Vector articulosEliminados, int mezcla)
	{
		return SqlBaseArticulosXMezclaDao.ingresarModificar(con, articulosIngresados, articulosEliminados, mezcla);
	}

	/**
	 * Método para ingresar o modificar los datos de
	 * los artículos por mezcla
	 * @param con
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public HashMap consultar(Connection con, int codigoInstitucion, int codigoMezcla)
	{
		return SqlBaseArticulosXMezclaDao.consultar(con, codigoInstitucion, codigoMezcla);
	}

}
