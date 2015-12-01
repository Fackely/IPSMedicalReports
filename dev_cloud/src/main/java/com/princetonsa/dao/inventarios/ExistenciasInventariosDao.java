/*
 * Creado el 3/01/2006
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface ExistenciasInventariosDao
{

	/**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen
	 * @param con
	 * @param codAlmacen
	 * @return
	 */
	public abstract HashMap consultarArticulosAlmacen(Connection con, int codAlmacen, String mostrarExt, int institucion);

	/**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen,clase,grupo,subgrupo
	 * En caso de que clase,grupo o subgrupo sean igual a 0 no los toma en el filtro de la consulta
	 * @param con
	 * @param codAlmacen
	 * @param String mostrarExt
	 * @return
	 */
	public abstract HashMap consultarArticulosAlmacenClaseGrupoSubgrupo(Connection con,int codAlmacen, String clase, String grupo, String subgrupo,String mostrarExt,int institucion);

	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y codigo del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @return
	 */
	public abstract HashMap consultarArticulosAlmacenCodArticulo(Connection con, int codAlmacen, String codArticulo, String mostrarExt, int institucion);

	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y descripcion del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @return
	 */
	
	public abstract HashMap consultarArticulosAlmacenDescArticulo(Connection con, int codAlmacen, String descArticulo, String mostrarExt, int institucion);

}
