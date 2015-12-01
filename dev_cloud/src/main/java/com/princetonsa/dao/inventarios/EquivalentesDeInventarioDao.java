package com.princetonsa.dao.inventarios;

/**
 * Autor : Juan Sebastian Castaño
 * Clase Dao de la funcionalidad de equivalentes de inventario
 */

import java.sql.Connection;
import java.util.HashMap;

public interface EquivalentesDeInventarioDao {
	
	/**
	 * Metodo de consulta de articulos equivalentes
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaEquivalentes (Connection con, int codigoArtPpal);
	
	
	/**
	 * Metodo de insercion de un nuevo articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @param cantidadArtEquivalente
	 * @param usuarioModifica
	 * @return
	 */
	public boolean insertarArticuloEquivalente (Connection con, int codigoArtPpal, int codigoArtEquivalente, int cantidadArtEquivalente, String usuarioModifica);
	
	
	/**
	 * Metodo que elimina un registro de articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @return
	 */
	public boolean eliminarArticuloEquivalente (Connection con,int codigoArtPpal, int codigoArtEquivalente);
	
	/**
	 * Metodo para modificar un registro de articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @param cantidadArtEquivalente
	 * @param usuarioModifica
	 * @return
	 */
	
	public boolean modificarArticuloEquivalente (Connection con,int codigoArtPpal, int codigoArtEquivalente, int cantidadArtEquivalente, String usuarioModifica);
	
	/**
	 * Metodo que Consulta los Campos del Articulo Principal para Filtrar la Busqeuda de Equivalentes
	 * @param con
	 * @param codArtPrincipal
	 * @return
	 */
	public HashMap<String, Object> consultaCamposBusqueda(Connection con, int codArtPrincipal);
	
	/**
	 * Metodo de Consulta Datos Adicionales del Articulo Equivalente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap<String, Object> consultaDatosAd(Connection con, int codigo);


	/**
	 * Metodo para consultar articulos equivalentes
	 * @param con
	 * @param codigoPpal
	 * @param codigoEquivalente
	 * @return
	 */
	public HashMap consultarArticulo(Connection con, int codigoPpal,int codigoEquivalente);
	

}
