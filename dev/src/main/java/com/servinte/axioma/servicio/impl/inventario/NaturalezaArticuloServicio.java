/**
 * 
 */
package com.servinte.axioma.servicio.impl.inventario;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.inventario.INaturalezaArticuloMundo;
import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.servicio.interfaz.inventario.INaturalezaArticuloServicio;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class NaturalezaArticuloServicio implements INaturalezaArticuloServicio {
	
	INaturalezaArticuloMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 */
	public NaturalezaArticuloServicio(){
		mundo = InventarioMundoFabrica.crearNaturalezaArticuloMundo();
	}

	
	/**
	 * 	
	 * Este M�todo se encarga de consultar las naturalezas de art�culos
	 * en el sistema
	 * 
	 * @return ArrayList<NaturalezaArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaArticulo> buscarNaturalezaArticulo() {
		return mundo.buscarNaturalezaArticulo();
	}
	
	

}
