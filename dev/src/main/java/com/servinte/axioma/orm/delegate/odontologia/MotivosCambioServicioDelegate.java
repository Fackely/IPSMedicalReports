/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Expression;

import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.MotivosCambiosServicios;
import com.servinte.axioma.orm.MotivosCambiosServiciosHome;

/**
 * @author armando
 *
 */
public class MotivosCambioServicioDelegate extends MotivosCambiosServiciosHome 
{

	public boolean existePaqueteOdontologico(String codigo,int codigoInstitucion) 
	{
		String sql="select count(1) as temporal from odontologia.motivos_cambios_servicios where codigo='"+codigo+"' and institucion="+codigoInstitucion;
		Integer resultado=(Integer)(HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql).addScalar("temporal",Hibernate.INTEGER).uniqueResult());
		return resultado.intValue()>0;
		
	}

	public ArrayList<MotivosCambiosServicios> consultarMotivosCambioServicios(int codigoInstitucion, String tipoMotivo) 
	{
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(MotivosCambiosServicios.class);
		criteria.add(Expression.eq("instituciones.codigo", codigoInstitucion));
		if(tipoMotivo != null && !tipoMotivo.isEmpty())
		{
			criteria.add(Expression.eq("tipo", tipoMotivo));
		}
		return (ArrayList<MotivosCambiosServicios>) criteria.list();
	}

}
