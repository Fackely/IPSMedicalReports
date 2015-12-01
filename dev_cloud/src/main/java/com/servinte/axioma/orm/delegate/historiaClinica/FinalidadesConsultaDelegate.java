package com.servinte.axioma.orm.delegate.historiaClinica;

import java.util.ArrayList;

import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.FinalidadesConsulta;
import com.servinte.axioma.orm.FinalidadesConsultaHome;

public class FinalidadesConsultaDelegate extends FinalidadesConsultaHome{

	/**
	 * Este M�todo se encarga de consultar las finalidades consulta
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesConsulta>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FinalidadesConsulta> consultarFinalidadesConsulta(){
		
		ArrayList<FinalidadesConsulta> lista =  (ArrayList<FinalidadesConsulta>)sessionFactory.getCurrentSession()
		.createCriteria(FinalidadesConsulta.class, "finalidadConsulta")
		.addOrder(Order.asc("finalidadConsulta.nombre"))
		.list();
	
		return lista;	
		
	}
	
}
