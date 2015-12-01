package com.sies.hibernate.delegate;

import java.util.List;

import com.servinte.axioma.orm.Turno;
import com.servinte.axioma.orm.TurnoHome;

public class TurnoDelegate extends TurnoHome {
	@SuppressWarnings("unchecked")
	public List<Turno> listarTurnos() {
		return sessionFactory.getCurrentSession().createCriteria(Turno.class).list();
	}
}