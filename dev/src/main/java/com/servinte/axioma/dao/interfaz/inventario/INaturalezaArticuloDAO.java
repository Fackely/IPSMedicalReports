/**
 * 
 */
package com.servinte.axioma.dao.interfaz.inventario;

import java.util.ArrayList;

import com.servinte.axioma.orm.NaturalezaArticulo;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface INaturalezaArticuloDAO {
	
	/**
	 * 	
	 * Este Método se encarga de consultar las naturalezas de artículos
	 * en el sistema
	 * 
	 * @return ArrayList<NaturalezaArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaArticulo> buscarNaturalezaArticulo();

}
