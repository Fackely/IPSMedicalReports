package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Collection;

import util.Answer;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public interface ArticuloCatalogoDao {
	
	/**
	 * Insertar
	 */
	public abstract int insertar(Connection con, HashMap vo);
	
	/**
	 * Buscar
	 */
	public Collection buscar(Connection con, String proveedor,String descripcion,String ref_proveedor, String codigo_axioma, int acronimo);

	/**
	 * Cargar articulo de catalogo
	 * @param con
	 * @param descripcion
	 * @return
	 */

	public abstract HashMap consultarArticuloCatalogo(Connection con,int acronimo);
	
	/**
	 * Modificar catalogo_proveedor con el codigo axioma
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarArticuloCatalogo(Connection con, int codigoArticulo,int acronimo);

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public abstract HashMap consultarTarifasArticulos(Connection con, String codigoArticulo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean guardarEsquemasInventario(Connection con, HashMap vo);

}
