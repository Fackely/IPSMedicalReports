package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.ComparativoUltimoConteo;

/**
 * Interfaz de Preparacion Toma Inventarios
 * @author garias@princetonsa.com
 */
public interface ComparativoUltimoConteoDao {

	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, ComparativoUltimoConteo cuc);
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap consultarConteosArticulo(Connection con, ComparativoUltimoConteo cuc);

	/**
	 * 
	 * @param articulos
	 * @param almacen
	 * @return
	 */
	public String sqlComparativoUltimoConteo(String articulos, int almacen);
}	