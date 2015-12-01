package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.FinalidadesServicio;
import com.servinte.axioma.orm.FinalidadesServicioHome;

public class FinalidadesServicioDelegate extends FinalidadesServicioHome{

	/**
	 * Este Método se encarga de consultar las finalidades del servicio
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesServicio>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<FinalidadesServicio> consultarFinalidadesServicio(){
		
		ArrayList<FinalidadesServicio> lista =  (ArrayList<FinalidadesServicio>)sessionFactory.getCurrentSession()
		.createCriteria(FinalidadesServicio.class, "finalidadServicio")
		.addOrder(Order.asc("finalidadServicio.nombre"))
		.list();
	
		return lista;	
		
	}
	
}
