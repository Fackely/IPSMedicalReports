package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import util.ConstantesBD;
import com.servinte.axioma.orm.ConceptosDeEgreso;
import com.servinte.axioma.orm.ConceptosDeEgresoHome;
import com.servinte.axioma.orm.OtrosDiagnosticos;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class ConceptosDeEgresoDelegate extends ConceptosDeEgresoHome{
	
	

	/**
	 * 	Retorna una lista de los activos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConceptosDeEgreso> listarActivos(){
		return (ArrayList<ConceptosDeEgreso>) sessionFactory.getCurrentSession()
				.createCriteria(ConceptosDeEgreso.class)
				.add(Expression.eq("activo", ConstantesBD.acronimoSi))
				.list();
	}
	
	
	/**
	 * Retorna una lista discriminando su estado (activo)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConceptosDeEgreso> listarTodos(){
		return (ArrayList<ConceptosDeEgreso>) sessionFactory.getCurrentSession()
				.createCriteria(ConceptosDeEgreso.class)
				.list();
	}
	
	
	
	
	/**
	 * 	Retorna una lista de todos discriminando su estado (activo)
	 * 	de acuerdo a la insitucion del usuario actual
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConceptosDeEgreso> listarTodosPorInstitucion(int institucion){
		
		return (ArrayList<ConceptosDeEgreso>) sessionFactory.getCurrentSession()
				.createCriteria(ConceptosDeEgreso.class)
				.createCriteria("instituciones").add(Expression.eq("codigo", institucion))
				.list();
	}
	
	
	
	/**
	 * Retorna una el objeto por su codigo
	 * @param codigo
	 */
	public ConceptosDeEgreso buscarPorCodigo(String codigo) {
		try {
			return (ConceptosDeEgreso) sessionFactory.getCurrentSession()
					.createCriteria(ConceptosDeEgreso.class)
					.add(Expression.eq("codigo", codigo))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
	/**
	 * Retorna una el objeto por un campo en especifico
	 * @param campoTabla
	 * @param campoDato
	 */
	public ConceptosDeEgreso buscarPorCampo(String campoTabla, String campoDato) {
		try {
			return (ConceptosDeEgreso) sessionFactory.getCurrentSession()
					.createCriteria(ConceptosDeEgreso.class)
					.add(Expression.eq(campoTabla, campoDato))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	/**
	 * 	Retorna una lista de los activos por la insitucion
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConceptosDeEgreso> listarActivosPorInstitucion(int institucion){
		
		return (ArrayList<ConceptosDeEgreso>) sessionFactory.getCurrentSession()
				.createCriteria(ConceptosDeEgreso.class)
				.add(Expression.eq("activo", ConstantesBD.acronimoSi))
				.createCriteria("instituciones").add(Expression.eq("codigo", institucion))
				.addOrder(Order.asc("codigo"))
				.list();
	}
	
	
	
}
