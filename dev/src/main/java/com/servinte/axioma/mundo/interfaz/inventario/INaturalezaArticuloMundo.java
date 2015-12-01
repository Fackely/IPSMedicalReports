/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.inventario;

import java.util.ArrayList;

import com.servinte.axioma.orm.NaturalezaArticulo;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface INaturalezaArticuloMundo {
	
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
