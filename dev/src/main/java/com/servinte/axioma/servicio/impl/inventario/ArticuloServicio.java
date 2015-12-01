package com.servinte.axioma.servicio.impl.inventario;

import java.sql.Connection;
import java.util.Collection;

import com.princetonsa.mundo.Articulo;
import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.inventario.IArticuloMundo;
import com.servinte.axioma.servicio.interfaz.inventario.IArticuloServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class ArticuloServicio implements IArticuloServicio {
	
	IArticuloMundo mundo;
	
	public ArticuloServicio(){
		 mundo = InventarioMundoFabrica.crearArticuloMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los artículos
	 * registrados en el sistema
	 * @param Connection con,String[] criteriosBusqueda, Articulo articulo
	 * @return Collection
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Collection realizarBusqueda(String[] criteriosBusqueda, Articulo articulo){
		return mundo.realizarBusqueda(criteriosBusqueda, articulo);
	}

}
