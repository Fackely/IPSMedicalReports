package com.servinte.axioma.servicio.interfaz.inventario;

import java.util.Collection;

import com.princetonsa.mundo.Articulo;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public interface IArticuloServicio {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los art�culos
	 * registrados en el sistema
	 * @param Connection con,String[] criteriosBusqueda, Articulo articulo
	 * @return Collection
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Collection realizarBusqueda(String[] criteriosBusqueda, Articulo articulo);

}
