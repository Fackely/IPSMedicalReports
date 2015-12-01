/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.inventario;

import java.util.ArrayList;

import com.servinte.axioma.orm.ClaseInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface IClaseInventarioServicio {
	
	
	/**
	 * 	
	 * Este Método se encarga de consultar las clases de inventarios
	 * en el sistema
	 * 
	 * @return ArrayList<ClaseInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ClaseInventario> buscarClaseInventario();
}