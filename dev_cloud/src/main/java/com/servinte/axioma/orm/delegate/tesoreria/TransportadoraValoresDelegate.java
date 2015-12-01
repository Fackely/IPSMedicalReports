package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import util.UtilidadTexto;

import com.servinte.axioma.orm.TransportadoraValores;
import com.servinte.axioma.orm.TransportadoraValoresHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class TransportadoraValoresDelegate extends TransportadoraValoresHome{
	
	
	/**
	 * LISTA TODAS LA TRANPORTADORAS CON SUS RESPECTIVAS ASOCIACIONES
	 * @param dtoTransportadora
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion )
	{
		
		Criteria criteriosList= sessionFactory.getCurrentSession().createCriteria(TransportadoraValores.class);
		
		if(!UtilidadTexto.isEmpty(dtoTransportadora.getCodigo()))
		{
			criteriosList.add(Expression.eq("codigo", dtoTransportadora.getCodigo()));
		}
		
		if(dtoTransportadora.getCodigoPk()>0)
		{
			criteriosList.add(Expression.ne("codigoPk", dtoTransportadora.getCodigoPk()));
		}
		
		if(dtoTransportadora.getActivo()>0)
		{
			criteriosList.add(Restrictions.eq("activo",dtoTransportadora.getActivo()));
		}
		

		criteriosList.createAlias("terceros","_terceros").addOrder(Order.asc("_terceros.descripcion")); //CARGA TERCEROS

		
		if(institucion>0)
		{
			criteriosList.createCriteria("centrosAtenTransportadoras").createCriteria("centroAtencion").add(Expression.eq("instituciones.codigo", institucion)); 
		}	

		criteriosList.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
				
//		List<TransportadoraValores> listadoTransportadoraValores = criteriosList.list();
//		
//		for (TransportadoraValores transportadoraValores : listadoTransportadoraValores) {
//			
//			transportadoraValores.getTerceros();
//			
//		}

		return criteriosList.list();
	}
	
	
	
	
	/**
	 * LISTA TODAS LA TRANPORTADORAS CON SUS RESPECTIVAS ASOCIACIONES
	 * @param dtoTransportadora
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion , int consecutivoCentroAtencion )
	{
		
		Criteria criteriosList= sessionFactory.getCurrentSession().createCriteria(TransportadoraValores.class);
		
		if(!UtilidadTexto.isEmpty(dtoTransportadora.getCodigo()))
		{
			criteriosList.add(Expression.eq("codigo", dtoTransportadora.getCodigo()));
		}
		
		if(dtoTransportadora.getCodigoPk()>0)
		{
			criteriosList.add(Expression.ne("codigoPk", dtoTransportadora.getCodigoPk()));
		}
		
		if(!UtilidadTexto.isEmpty(dtoTransportadora.getActivo()))
		{
			criteriosList.add(Restrictions.eq("activo",dtoTransportadora.getActivo()));
		}
		
		criteriosList.createAlias("terceros","_terceros").addOrder(Order.asc("_terceros.descripcion")); //CARGA TERCEROS

		criteriosList.createAlias("centrosAtenTransportadoras", "centroAtenTrans");
		criteriosList.createAlias("centroAtenTrans.centroAtencion", "centroAtencion");
		
		if(consecutivoCentroAtencion>0)
		{
			criteriosList.add(Restrictions.eq("centroAtencion.consecutivo", consecutivoCentroAtencion));
		}
		
		if(institucion>0)
		{
			criteriosList.createAlias("centroAtencion.instituciones", "instituciones").add(Expression.eq("instituciones.codigo", institucion)); 
		}	

		criteriosList.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
		
		
		return criteriosList.list();
	}

}
