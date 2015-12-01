package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public interface ITiposServicioMundo {
	
	/**
	 * 	
	 * Este Método se encarga de consultar los tipos de servicios
	 * en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposServicio> buscarTiposServicio();

}
