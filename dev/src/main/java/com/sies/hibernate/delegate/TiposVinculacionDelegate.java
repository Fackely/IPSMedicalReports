package com.sies.hibernate.delegate;

import java.util.List;

import com.servinte.axioma.orm.TiposVinculacion;
import com.servinte.axioma.orm.TiposVinculacionHome;

public class TiposVinculacionDelegate extends TiposVinculacionHome {
	@SuppressWarnings("unchecked")
	public List<TiposVinculacion> listarTiposVinculacion() {
		return sessionFactory.getCurrentSession().createCriteria(TiposVinculacion.class).list();
	}
}