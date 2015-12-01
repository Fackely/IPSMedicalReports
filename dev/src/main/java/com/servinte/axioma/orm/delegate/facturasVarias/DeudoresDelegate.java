package com.servinte.axioma.orm.delegate.facturasVarias;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.orm.Deudores;
import com.servinte.axioma.orm.DeudoresHome;

public class DeudoresDelegate extends DeudoresHome {

	public Deudores consultarDeudor(DtoPersonas dtoCompradorTarjeta) {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Deudores.class, "deudores");
		criteria.createAlias("deudores.pacientes", "pacientes");
		criteria.createAlias("pacientes.personas", "personas");
		
		criteria.add(Restrictions.eq("personas.tiposIdentificacion.acronimo", dtoCompradorTarjeta.getTipoIdentificacion()));
		criteria.add(Restrictions.eq("personas.numeroIdentificacion", dtoCompradorTarjeta.getNumeroIdentificacion()));
		
		return (Deudores) criteria.uniqueResult();
	}

}
