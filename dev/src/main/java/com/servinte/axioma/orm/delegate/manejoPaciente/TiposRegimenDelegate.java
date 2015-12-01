package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.TiposRegimen;
import com.servinte.axioma.orm.TiposRegimenHome;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad Tipos de R&eacute;gimen
 * 
 * @author Angela Maria Aguirre
 * @since 19/08/2010
 */
public class TiposRegimenDelegate extends TiposRegimenHome {
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Tipos de R&eacute;gimen.
	 * 
	 * @return ArrayList<TiposRegimen>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TiposRegimen> consultarTiposRegimen(){
		ArrayList<TiposRegimen> lista =  (ArrayList<TiposRegimen>)sessionFactory.getCurrentSession()
		.createCriteria(TiposRegimen.class, "tipoRegimen")
		.addOrder(Order.asc("tipoRegimen.nombre"))
		.list();
	
	return lista;
	}

}
