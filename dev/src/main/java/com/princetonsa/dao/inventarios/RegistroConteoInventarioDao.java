package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.RegistroConteoInventario;





public interface RegistroConteoInventarioDao {
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, RegistroConteoInventario pti);
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public boolean guardarConteo(Connection con, RegistroConteoInventario pti);
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public boolean anularConteos(Connection con, RegistroConteoInventario pti, String estado);

	/**
	 * 
	 * @param secciones
	 * @param subseccion
	 * @param cadenaPreparada
	 * @param almacen
	 * @param ordArticulo
	 * @param codigoInstitucion
	 * @return
	 */
	public String sqlReporteRegistroConteoInventario(HashMap secciones,
			int subseccion, String cadenaPreparada, int almacen,
			String ordArticulo, int codigoInstitucion);
	
}