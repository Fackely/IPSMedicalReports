package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;

/**
 * Interfaz de Preparacion Toma Inventarios
 * @author garias@princetonsa.com
 */
public interface PreparacionTomaInventarioDao {
	
	/** 
	 * @param con
	 * @param codigo clase
	 * @return
	 */
	public HashMap consultarGrupos(Connection con, int clase);
	
	/** 
	 * @param con
	 * @param codigo clase
	 * @param codigo subgrupo
	 * @return
	 */
	public HashMap consultarSubgrupos(Connection con, int clase, int subgrupo);
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, PreparacionTomaInventario pti);
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public boolean confirmarPreparacion(Connection con, PreparacionTomaInventario pti);
	
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public int CodigoPreparacionMax(Connection con, PreparacionTomaInventario pti);

	/**
	 * 
	 * @param codigoPreparacion
	 * @param almacen
	 * @param seccionesElegidas
	 * @param subseccion
	 * @return
	 */
	public String sqlListadoConteo(int codigoPreparacion, int almacen, HashMap seccionesElegidas, int subseccion);

	
}