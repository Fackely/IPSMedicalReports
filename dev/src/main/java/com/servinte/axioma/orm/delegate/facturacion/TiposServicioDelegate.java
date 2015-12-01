package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.hibernate.Criteria;

import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.orm.TiposServicioHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class TiposServicioDelegate extends TiposServicioHome {
	
	/**
	 * 	
	 * Este Método se encarga de consultar los tipos de 
	 * servicio en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposServicio> buscarTiposServicio(){
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(TiposServicio.class);
		 
		 return (ArrayList<TiposServicio>)criteria.list();
	}

}
