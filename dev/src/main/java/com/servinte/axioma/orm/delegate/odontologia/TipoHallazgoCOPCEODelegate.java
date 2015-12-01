package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.TipoHallazgoCeoCop;
import com.servinte.axioma.orm.TipoHallazgoCeoCopHome;

/**
 * Clase encargada de ejecutar las transacciones del objeto {@link HallazgoCeoCop}
 * @author diecorqu
 *
 */
public class TipoHallazgoCOPCEODelegate extends TipoHallazgoCeoCopHome {

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TipoHallazgoCeoCop> listarHallazgosCOPCEO() {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(TipoHallazgoCeoCop.class);
		return (ArrayList<TipoHallazgoCeoCop>)criteria.addOrder(Order.asc("codigo")).list();
	}
}
