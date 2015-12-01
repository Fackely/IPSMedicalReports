package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import util.UtilidadTexto;

import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.PaquetesOdontologicosHome;

public class PaquetesOdontologicosDelegate extends PaquetesOdontologicosHome {

	/**
	 * 
	 * @param codigo
	 * @param codigoInstitucion
	 */
	public boolean existePaqueteOdontologico(String codigo, int codigoInstitucion) 
	{
		String sql="select count(1) as temporal from odontologia.paquetes_odontologicos where codigo='"+codigo+"' and institucion="+codigoInstitucion;
		Integer resultado=(Integer)(HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("temporal",Hibernate.INTEGER).uniqueResult());
		return resultado.intValue()>0;
	}

	/**
	 * 
	 * @param codigoEspecialidad 
	 * @param descripcionPaquete 
	 * @param codigoPaquete 
	 * @return
	 */
	public ArrayList<PaquetesOdontologicos> listarPaquetesOdontologicos(String codigoPaquete, String descripcionPaquete, int codigoEspecialidad) 
	{
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PaquetesOdontologicos.class);
		criteria.addOrder(Property.forName("descripcion").asc());
		
		if(!UtilidadTexto.isEmpty(codigoPaquete))
		{
			criteria.add(Expression.eq("codigo", codigoPaquete));
		}
		if(!UtilidadTexto.isEmpty(descripcionPaquete))
		{
			//criteria.add(Expression.like("descripcion", descripcionPaquete));
			criteria.add(Restrictions.sqlRestriction("lower(descripcion) like lower(?)", "%"+descripcionPaquete+"%", Hibernate.STRING));
		}
		if(codigoEspecialidad>0)
		{
			criteria.add(Expression.eq("especialidades.codigo", codigoEspecialidad));
		}
		return (ArrayList<PaquetesOdontologicos>) criteria.list();
	}
}
