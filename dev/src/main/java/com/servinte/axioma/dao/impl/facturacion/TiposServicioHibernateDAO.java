package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.facturacion.ITiposServicioDAO;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.orm.delegate.facturacion.TiposServicioDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class TiposServicioHibernateDAO implements ITiposServicioDAO {
	TiposServicioDelegate delegate;
	
	public TiposServicioHibernateDAO(){
		delegate = new TiposServicioDelegate();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar los tipos de servicios
	 * en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposServicio> buscarTiposServicio(){
		return delegate.buscarTiposServicio();
	}

}
