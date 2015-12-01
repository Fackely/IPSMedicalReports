package com.servinte.axioma.orm.delegate.administracion;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.dto.administracion.DtoLocalidad;
import com.servinte.axioma.orm.Localidades;
import com.servinte.axioma.orm.LocalidadesHome;

public class LocalidadesDelegate extends  LocalidadesHome{
	

	/**
	 * Lista todas las Localidades parametrizadas en el sistema
	 * @return La lista de todas las localidades en la estructura DtoLocalidad
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public List<DtoLocalidad> listarAllLocalidades(){
		  
		 Criteria criteria= sessionFactory.getCurrentSession()
			.createCriteria(Localidades.class, "localidad");
		 ProjectionList projection = Projections.projectionList();		
			projection.add(Projections.property("localidad.id.codigoCiudad"),"codigoCiudad");
			projection.add(Projections.property("localidad.id.codigoDepartamento"),"codigoDepartamento");
			projection.add(Projections.property("localidad.id.codigoPais"),"codigoPais");
			projection.add(Projections.property("localidad.id.codigoLocalidad"),"codigoLocalidad");
			criteria.setProjection(projection);
			criteria.setResultTransformer(Transformers.aliasToBean(DtoLocalidad.class));
			List<DtoLocalidad> listaLocalidades =  criteria.list();
		 
		return listaLocalidades;
	}
}
