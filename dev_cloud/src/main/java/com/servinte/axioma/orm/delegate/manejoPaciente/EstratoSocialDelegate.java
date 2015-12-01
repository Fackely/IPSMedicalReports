package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.EstratosSocialesHome;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad Estratos Sociales
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
@SuppressWarnings("unchecked")
public class EstratoSocialDelegate extends EstratosSocialesHome {
	
	/**
	 * Este Método se encarga de consultar los estratos Sociales
	 * que esten relacionados al régimen específico
	 * 
	 * @param String
	 * @return ArrayList<EstratosSociales>
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<EstratosSociales> consultarEstratosSocilaesPorRegimen(
			String acronimoRegimenConvenio){
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(EstratosSociales.class,"estratoSocial")
			.createAlias("estratoSocial.tiposRegimen", "tipoRegimen")
			.add(Restrictions.eq("tipoRegimen.acronimo", acronimoRegimenConvenio));
		
		ArrayList<EstratosSociales> listaEstratos =(ArrayList<EstratosSociales>)criteria.list();
		
		if(listaEstratos!=null && listaEstratos.size()>0){
			for(EstratosSociales estrato : listaEstratos){
				estrato.getTiposRegimen().getAcronimo();
				estrato.getTiposRegimen().getNombre();
			}
		}
		
		return listaEstratos;
	}
	
	/**
	 * Este Método se encarga de consultar los estratos Sociales
	 * registrados
	 * 
	 * @param String
	 * @return ArrayList<EstratosSociales>
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<EstratosSociales> consultarEstratoSocial(){
		
		ArrayList<EstratosSociales> lista =  (ArrayList<EstratosSociales>)sessionFactory.getCurrentSession()
		.createCriteria(EstratosSociales.class, "estratoSocial")
		.addOrder(Order.asc("estratoSocial.descripcion"))
		.list();
	
		return lista;	
		
	}

}
