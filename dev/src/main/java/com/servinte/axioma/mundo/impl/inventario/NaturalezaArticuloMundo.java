/**
 * 
 */
package com.servinte.axioma.mundo.impl.inventario;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.INaturalezaArticuloDAO;
import com.servinte.axioma.mundo.interfaz.inventario.INaturalezaArticuloMundo;
import com.servinte.axioma.orm.NaturalezaArticulo;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class NaturalezaArticuloMundo implements INaturalezaArticuloMundo {
	
	INaturalezaArticuloDAO dao;
	
	public NaturalezaArticuloMundo(){
		dao = InventarioDAOFabrica.crearNaturalezaArticuloDAO();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar las naturalezas de artículos
	 * en el sistema
	 * 
	 * @return ArrayList<NaturalezaArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaArticulo> buscarNaturalezaArticulo(){
		return dao.buscarNaturalezaArticulo(); 
	}

}
