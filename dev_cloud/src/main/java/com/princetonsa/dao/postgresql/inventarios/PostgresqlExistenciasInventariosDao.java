/*
 * Creado el 3/01/2006
 * Juan David Ramírez López
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ExistenciasInventariosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseExistenciasInventariosDao;

public class PostgresqlExistenciasInventariosDao implements ExistenciasInventariosDao
{

	/**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen
	 * @param con
	 * @param codAlmacen
	 * @return
	 */
	public HashMap consultarArticulosAlmacen(Connection con, int codAlmacen, String mostrarExt, int institucion)
	{
		return SqlBaseExistenciasInventariosDao.consultarArticulosAlmacen(con,codAlmacen, mostrarExt, institucion);
	}
	
	/**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen,clase,grupo,subgrupo
	 * En caso de que clase,grupo o subgrupo sean igual a 0 no los toma en el filtro de la consulta
	 * @param con
	 * @param codAlmacen
	 * @param String mostrarExt
	 * @return
	 */
	public HashMap consultarArticulosAlmacenClaseGrupoSubgrupo(Connection con, int codAlmacen, String clase, String grupo, String subgrupo,String mostrarExt,int institucion)
	{
		return SqlBaseExistenciasInventariosDao.consultarArticulosAlmacenClaseGrupoSubgrupo(con,codAlmacen,clase,grupo,subgrupo,mostrarExt,institucion);
	}
	
	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y codigo del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @return
	 */
	public HashMap consultarArticulosAlmacenCodArticulo(Connection con, int codAlmacen, String codArticulo, String mostrarExt, int institucion)
	{
		return SqlBaseExistenciasInventariosDao.consultarArticulosAlmacenCodArticulo(con,codAlmacen,codArticulo, mostrarExt, institucion);
	}
	

	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y descripcion del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @return
	 */	
	public HashMap consultarArticulosAlmacenDescArticulo(Connection con, int codAlmacen, String descArticulo, String mostrarExt, int institucion)
	{
		return SqlBaseExistenciasInventariosDao.consultarArticulosAlmacenDescArticulo(con,codAlmacen,descArticulo,mostrarExt,institucion);
	}
}