/**
 * 
 */
package com.servinte.axioma.dao.interfaz.inventario;

import com.servinte.axioma.orm.UnidosisXArticulo;

/**
 * Esta clase se encarga de definir la l�gica de negocio
 * para la entidad UnidosisXArticulo
 * 
 * @author Angela Maria Aguirre
 * @since 25/01/2011
 */
public interface IUnidosisXArticuloDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de realizar una consulta por el id
	 * 
	 * 
	 * @param long id
	 * @return UnidosisXArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public UnidosisXArticulo buscarPorID(long id);
	

}
