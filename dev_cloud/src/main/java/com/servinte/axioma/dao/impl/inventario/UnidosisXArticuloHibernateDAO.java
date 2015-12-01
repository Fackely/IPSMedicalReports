/**
 * 
 */
package com.servinte.axioma.dao.impl.inventario;

import com.servinte.axioma.dao.interfaz.inventario.IUnidosisXArticuloDAO;
import com.servinte.axioma.orm.UnidosisXArticulo;
import com.servinte.axioma.orm.delegate.inventario.UnidosisXArticuloDelegate;

/**
 * Esta clase se encarga de ejecutar la lógica de negocio
 * para la entidad UnidosisXArticulo
 * 
 * @author Angela Maria Aguirre
 * @since 25/01/2011
 */
public class UnidosisXArticuloHibernateDAO implements IUnidosisXArticuloDAO {

	UnidosisXArticuloDelegate delegate;
	
	
	public UnidosisXArticuloHibernateDAO(){
		delegate = new UnidosisXArticuloDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de realizar una consulta por el id
	 * 
	 * 
	 * @param long id
	 * @return UnidosisXArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public UnidosisXArticulo buscarPorID(long id){
		return delegate.findById(id);
	}

}
