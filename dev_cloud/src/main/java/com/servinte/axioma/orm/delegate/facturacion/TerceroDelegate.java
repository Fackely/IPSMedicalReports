package com.servinte.axioma.orm.delegate.facturacion;


import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.cargos.DtoTercero;
import com.servinte.axioma.orm.Terceros;
import com.servinte.axioma.orm.TercerosHome;

/**
 * 
 * @author axioma
 *
 */
public class TerceroDelegate extends TercerosHome{
	
	/**
	 * Obtener el tercero por el codigo pk de una entidad subcontratada 
	 * 
	 * @param codEntidadSubcontratada
	 * @return DtoTercero
	 * 
	 * @author Fabián Becerra
	 */
	public DtoTercero obtenerTerceroXEntidadSub (long codEntidadSubcontratada)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Terceros.class, "tercero");
		
		criteria.createAlias("tercero.entidadesSubcontratadases", "entidadSub");
		
		criteria.add(Restrictions.eq("entidadSub.codigoPk", codEntidadSubcontratada));
		
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.distinct(Projections.projectionList()
				.add( Projections.property("tercero.numeroIdentificacion"),"numeroIdentificacion")));
				projectionList.add(Projections.property("tercero.descripcion"), "descripcion");
				projectionList.add(Projections.property("tercero.direccion"), "direccion");
				projectionList.add(Projections.property("tercero.telefono")	, "telefono");
				
				
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoTercero.class));
	
		return (DtoTercero) criteria.uniqueResult();
	}
}
