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
	 * Este M�todo se encarga de consultar las naturalezas de art�culos
	 * en el sistema
	 * 
	 * @return ArrayList<NaturalezaArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaArticulo> buscarNaturalezaArticulo();

}
