/*
 * Jun 29, 2010
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.servinte.axioma.orm.EntidadesFinancieras;
import com.servinte.axioma.orm.EntidadesFinancierasHome;
import com.servinte.axioma.orm.TarjetaFinancieraComision;

/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class EntidadesFinancierasDelegate extends EntidadesFinancierasHome
{
	
	/**
	 * Lista las entidades que esten activas para la isntitucion
	 */
	@SuppressWarnings("unchecked")
	public List<EntidadesFinancieras> listarActivasPorInstitucion(int idInstitucion)
	{
		return (List<EntidadesFinancieras>) sessionFactory.getCurrentSession()
			.createCriteria(EntidadesFinancieras.class)
			.add(Expression.eq("activo", true))
			.add(Expression.eq("instituciones.codigo", idInstitucion))
			.list();
	}
	
	
	/**
	 * 
	 * @param todas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoEntidadesFinancieras> consultarEntidadesFinancieras(boolean todas) 
	{
		ArrayList<DtoEntidadesFinancieras> resultado=new ArrayList<DtoEntidadesFinancieras>();
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(EntidadesFinancieras.class);
		if(!todas)
		{
			criteria.add(Expression.eq("activo", true));
		}
		ArrayList<EntidadesFinancieras> entidades=(ArrayList<EntidadesFinancieras>)criteria.list();
		for(EntidadesFinancieras enti:entidades)
		{
			DtoEntidadesFinancieras dtoE=new DtoEntidadesFinancieras();
			dtoE.setActivo(enti.isActivo());
			dtoE.setCodigo(enti.getCodigo());
			dtoE.setCodigoTercero(enti.getTerceros().getCodigo());
			dtoE.setDescripcionTercero(enti.getTerceros().getDescripcion());
			dtoE.setCodigoTipoEntidad(enti.getTiposEntFinancieras().getCodigo());
			dtoE.setDescripcionTipoEntidad(enti.getTiposEntFinancieras().getDescripcion());
			dtoE.setConsecutivo(enti.getConsecutivo());
			dtoE.setNumeroIdentificacionTercero(enti.getTerceros().getNumeroIdentificacion());
			resultado.add(dtoE);
		}
		return resultado;
	}
	
	
	
	/**
	 * Lista las entidades que esten asociadas a la institucion dada.
	 * Pueden ser todas o las que se encuentren activas
	 * 
	 * @param codigoInstitucion
	 * @param activo
	 * @return List<{@link DtoEntidadesFinancieras}>
	 */
	@SuppressWarnings("unchecked")
	public List<DtoEntidadesFinancieras> obtenerEntidadesPorInstitucion(int codigoInstitucion, boolean activo)
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntidadesFinancieras.class, "entidades_financieras");

		criteria.createAlias("entidades_financieras.terceros", "terceros");
		criteria.createAlias("entidades_financieras.tiposEntFinancieras", "tiposEntFinancieras");
		
		if(activo)
		{
			criteria.add(Restrictions.eq("entidades_financieras.activo", activo));
		}
		
		return (List<DtoEntidadesFinancieras>) criteria.add(Restrictions.eq("instituciones.codigo", codigoInstitucion))
			.addOrder(Property.forName("consecutivo").asc())
			.setProjection(Projections.projectionList()
				.add(Projections.property("entidades_financieras.consecutivo")	, "consecutivo")
				.add(Projections.property("terceros.descripcion")				, "descripcionTercero")
				.add(Projections.property("entidades_financieras.activo")		, "activo")
				.add(Projections.property("entidades_financieras.codigo")		, "codigo")
				.add(Projections.property("terceros.codigo")					, "codigoTercero")
				.add(Projections.property("tiposEntFinancieras.codigo")			, "codigoTipoEntidad")
				.add(Projections.property("tiposEntFinancieras.descripcion")	, "descripcionTipoEntidad")
				.add(Projections.property("terceros.numeroIdentificacion")		, "numeroIdentificacionTercero")
			)
		.setResultTransformer( Transformers.aliasToBean(DtoEntidadesFinancieras.class))
		.list();
	}
	
	
	/**
	 * Lista las entidades que esten asociadas a un c&oacute;digo de tarjeta
	 * @param codigoInstitucion
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoEntidadesFinancieras> obtenerEntidadesPorTarjeta(int codigoTarjeta)
	{
		ArrayList<DtoEntidadesFinancieras> listaEntidadesFinancieras = (ArrayList<DtoEntidadesFinancieras>) sessionFactory.getCurrentSession()
			.createCriteria(TarjetaFinancieraComision.class, "tf")
			.add(Expression.eq("tf.tarjetasFinancieras.consecutivo", codigoTarjeta))
			.createAlias("tf.entidadesFinancieras", "ef")
			.createAlias("ef.terceros", "ter")
				.addOrder(Property.forName("ter.descripcion").asc())
				.setProjection(Projections.projectionList()
					.add(Projections.property("ef.consecutivo"), "consecutivo")
					.add(Projections.property("ter.descripcion"), "descripcionTercero")
			)
			.setResultTransformer( Transformers.aliasToBean(DtoEntidadesFinancieras.class))
			.list();

		return listaEntidadesFinancieras;
	}
}