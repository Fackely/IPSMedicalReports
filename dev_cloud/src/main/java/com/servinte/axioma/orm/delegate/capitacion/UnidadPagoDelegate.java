package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.UnidadPago;
import com.servinte.axioma.orm.UnidadPagoHome;

/**
 * Clase para el acceso a datos de la entidad UnidadPago
 * @author Ricardo Ruiz
 *
 */
public class UnidadPagoDelegate extends UnidadPagoHome{
	
	
	/**
	 * Método utilizado para consultar el valor de unidad de pago para una fecha
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<UnidadPago> consultarUnidadPagoPorFecha(Date fecha){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UnidadPago.class, "unidadPago");
		
		criteria.add(Restrictions.le("unidadPago.fechaInicial", fecha))
				.add(Restrictions.ge("unidadPago.fechaFinal", fecha));
		
		return (ArrayList<UnidadPago>)criteria.list();
	}

}
