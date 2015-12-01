package com.servinte.axioma.mundo.interfaz.inventario;

import java.sql.Connection;
import java.util.Collection;

import com.princetonsa.mundo.Articulo;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public interface IArticuloMundo {
	
	/**
	 * 
	 * Este Método se encarga de consultar los artículos
	 * registrados en el sistema
	 * @param Connection con,String[] criteriosBusqueda, Articulo articulo
	 * @return Collection
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Collection realizarBusqueda(String[] criteriosBusqueda, Articulo articulo);

}
