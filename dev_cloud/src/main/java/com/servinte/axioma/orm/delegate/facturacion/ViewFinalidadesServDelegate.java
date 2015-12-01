package com.servinte.axioma.orm.delegate.facturacion;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


import com.servinte.axioma.orm.ViewFinalidadesServ;
import com.servinte.axioma.orm.ViewFinalidadesServHome;

public class ViewFinalidadesServDelegate extends ViewFinalidadesServHome {
	
	/**
	 * M&eacute;todo encargado de obtener las finalidades
	 * de los servicios existentes en el sistema
	 * @param codigoServicio
	 * @return List<ViewFinalidadesServ>
	 * @author Diana Carolina G
	 */
	@SuppressWarnings("unchecked")
	public List<ViewFinalidadesServ> obtenerViewFinalidadesServ(int codigoServicio){
		Criteria criteria= sessionFactory.getCurrentSession()
		.createCriteria(ViewFinalidadesServ.class);
		
		if(codigoServicio>0)
		{
			criteria.add(Restrictions.eq("id.codigo", codigoServicio));
		}
		return criteria.list();
	}

}
