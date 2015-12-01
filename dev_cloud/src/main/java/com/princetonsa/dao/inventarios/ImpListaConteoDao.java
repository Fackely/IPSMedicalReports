package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.ImpListaConteo;


/**
 * Interfaz de articulos por almacen
 * @author lgchavez@princetonsa.com
 */
public interface ImpListaConteoDao {
	
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public HashMap consultar(Connection con, ImpListaConteo a);

	/**
	 * 
	 * @param con
	 * @param secciones
	 * @param subseccion
	 * @param cadenaPreparada
	 * @param codigos
	 * @param almacen
	 * @param indArticulo
	 * @param ordArticulo
	 * @param institucion 
	 * @return
	 */
	public String sqlReporteListadoConteo(Connection con, HashMap secciones,
			int subseccion, String cadenaPreparada, HashMap codigos,
			int almacen, String indArticulo, String ordArticulo, int institucion);
	
	
}