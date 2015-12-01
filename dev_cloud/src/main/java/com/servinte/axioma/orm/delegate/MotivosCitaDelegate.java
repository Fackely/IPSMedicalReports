package com.servinte.axioma.orm.delegate;

import java.util.List;

import org.hibernate.criterion.Expression;

import util.ConstantesIntegridadDominio;

import com.servinte.axioma.orm.MotivosCita;
import com.servinte.axioma.orm.MotivosCitaHome;

public class MotivosCitaDelegate extends MotivosCitaHome{
	@SuppressWarnings("unchecked")
	public List<MotivosCita> listarMotivos()
	{
		return (List<MotivosCita>)sessionFactory.getCurrentSession().createCriteria(MotivosCita.class).
				add(Expression.eq("tipoMotivo", ConstantesIntegridadDominio.acronimoMotivoNoconfirma)).list();
	}

}
