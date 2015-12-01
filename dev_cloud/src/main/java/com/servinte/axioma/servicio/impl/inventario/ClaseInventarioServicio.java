/**
 * 
 */
package com.servinte.axioma.servicio.impl.inventario;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.inventario.IClaseInventarioMundo;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.servicio.interfaz.inventario.IClaseInventarioServicio;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class ClaseInventarioServicio implements IClaseInventarioServicio {
	
	IClaseInventarioMundo mundo;
	
	public ClaseInventarioServicio(){
		mundo = InventarioMundoFabrica.crearClaseInventarioMundo();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar las clases de inventarios
	 * en el sistema
	 * 
	 * @return ArrayList<ClaseInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ClaseInventario> buscarClaseInventario(){
		return mundo.buscarClaseInventario();
	}

}
