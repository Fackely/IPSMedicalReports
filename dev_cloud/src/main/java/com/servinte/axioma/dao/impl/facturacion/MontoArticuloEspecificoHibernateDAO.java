package com.servinte.axioma.dao.impl.facturacion;

import com.servinte.axioma.dao.interfaz.facturacion.IMontoArticuloEspecificoDAO;
import com.servinte.axioma.orm.delegate.facturacion.MontoArticuloEspecificoDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoArticuloEspecificoHibernateDAO implements
		IMontoArticuloEspecificoDAO {
	
	MontoArticuloEspecificoDelegate delegate;
	
	public MontoArticuloEspecificoHibernateDAO(){
		delegate = new MontoArticuloEspecificoDelegate();
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un artículo específico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarArticuloEspecifico(int id){
		return delegate.eliminarArticuloEspecifico(id);
	}

}
