package com.servinte.axioma.orm.delegate.administracion;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.dto.administracion.DtoBarrio;
import com.servinte.axioma.orm.Barrios;
import com.servinte.axioma.orm.BarriosHome;

public class BarriosDelegate extends BarriosHome{

	
	/**
	 * Consulta que permite la consulta del Barrio por codigo barrio, ciudad, departamento y pais
	 * 
	 * @param codigoBarrio
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Barrios findByCodigoBarrio(String codigoBarrio, String codigoCiudad,
			String codigoDepartamento, String codigoPais) {
		try {
			List<Barrios> results = (List<Barrios>)sessionFactory.getCurrentSession().createCriteria(
								Barrios.class, "barrio")
								.createAlias("barrio.ciudades", "ciudad")
							.add(Restrictions.eq("barrio.codigoBarrio",codigoBarrio))
							.add(Restrictions.eq("ciudad.id.codigoCiudad",codigoCiudad))
							.add(Restrictions.eq("ciudad.id.codigoDepartamento",codigoDepartamento))
							.add(Restrictions.eq("ciudad.id.codigoPais",codigoPais))
							.list();
			if(results != null && !results.isEmpty()){
				return results.get(0);
			}
			return null;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * Lista todos los Barrios parametrizados en el sistema
	 * @return La lista de todos los barrios en la estructura DtoBarrio
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public List<DtoBarrio> listarAllBarrios(){
		  
		 Criteria criteria= sessionFactory.getCurrentSession()
			.createCriteria(Barrios.class, "barrio")
			.createAlias("barrio.ciudades", "ciudad");
		 ProjectionList projection = Projections.projectionList();		
			projection.add(Projections.property("ciudad.id.codigoCiudad"),"codigoCiudad");
			projection.add(Projections.property("ciudad.id.codigoDepartamento"),"codigoDepartamento");
			projection.add(Projections.property("ciudad.id.codigoPais"),"codigoPais");
			projection.add(Projections.property("barrio.codigoBarrio"),"codigoBarrio");
			criteria.setProjection(projection);
			criteria.setResultTransformer(Transformers.aliasToBean(DtoBarrio.class));
			List<DtoBarrio> listaBarrios =  criteria.list();
		 
		return listaBarrios;
	}
	
}
