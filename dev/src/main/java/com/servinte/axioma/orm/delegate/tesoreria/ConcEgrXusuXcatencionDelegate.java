package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;

import util.ConstantesBD;

import com.servinte.axioma.orm.ConcEgrXusuXcatencion;
import com.servinte.axioma.orm.ConcEgrXusuXcatencionHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class ConcEgrXusuXcatencionDelegate extends ConcEgrXusuXcatencionHome{
	
	

	/**
	 * 	Retorna una lista de los activos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConcEgrXusuXcatencion> listarActivos(){
		return (ArrayList<ConcEgrXusuXcatencion>) sessionFactory.getCurrentSession()
				.createCriteria(ConcEgrXusuXcatencion.class)
				.add(Expression.eq("activo", ConstantesBD.acronimoSi))
				.list();
	}
	
	
	/**
	 * Retorna una lista discriminando su estado (activo)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConcEgrXusuXcatencion> listarTodos(){
		return (ArrayList<ConcEgrXusuXcatencion>) sessionFactory.getCurrentSession()
				.createCriteria(ConcEgrXusuXcatencion.class)
				.list();
	}
	
	
	
	
	/**
	 * 	Retorna una lista de todos discriminando su estado (activo)
	 * 	de acuerdo a la insitucion del usuario actual
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConcEgrXusuXcatencion> listarTodosPorInstitucion(int institucion){
		
		return (ArrayList<ConcEgrXusuXcatencion>) sessionFactory.getCurrentSession()
				.createCriteria(ConcEgrXusuXcatencion.class)
				.createCriteria("instituciones").add(Expression.eq("codigo", institucion))
				.list();
	}
	
	
	
	/**
	 * Retorna una el objeto por su codigo
	 * @param codigo
	 */
	public ConcEgrXusuXcatencion buscarPorCodigo(String codigo) {
		try {
			return (ConcEgrXusuXcatencion) sessionFactory.getCurrentSession()
					.createCriteria(ConcEgrXusuXcatencion.class)
					.add(Expression.eq("codigo_pk", codigo))
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
	public ConcEgrXusuXcatencion buscarPorCampo(String campoTabla, String campoDato) {
		try {
			return (ConcEgrXusuXcatencion) sessionFactory.getCurrentSession()
					.createCriteria(ConcEgrXusuXcatencion.class)
					.add(Expression.eq(campoTabla, campoDato))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	

	/**
	 * Lista los que esten activos y a su vez el concepto de egreso
	 * tenga estado activo tambien. El listado es de acuerdo a la insitucion del usuario actual
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConcEgrXusuXcatencion> listarActivosYconcepEgresoActivoPorInstitucion(int institucion){
		
		return (ArrayList<ConcEgrXusuXcatencion>) sessionFactory.getCurrentSession()
				.createCriteria(ConcEgrXusuXcatencion.class)
				.add(Expression.eq("activo", ConstantesBD.acronimoSi))
				.createCriteria("conceptosDeEgreso")
					.add(Expression.eq("activo", ConstantesBD.acronimoSi))
					.createCriteria("instituciones")
						.add(Expression.eq("codigo", institucion))
				.list();
	}
	

	/**
	 * Lista los que esten activos y a su vez el concepto de egreso
	 * tenga estado activo tambien. El listado es de acuerdo a la insitucion y centro de atencion
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ConcEgrXusuXcatencion> listarActivosYconcepEgresoActivoPorInstitucionYCentroAtencion(int institucion, int centroAtencion){
		
		return (ArrayList<ConcEgrXusuXcatencion>) sessionFactory.getCurrentSession()
				.createCriteria(ConcEgrXusuXcatencion.class)
				.add(Expression.eq("activo", ConstantesBD.acronimoSi))
				.add(Expression.eq("centroAtencion.consecutivo", centroAtencion))
				
				.createCriteria("conceptosDeEgreso")
					.add(Expression.eq("activo", ConstantesBD.acronimoSi))
					.createCriteria("instituciones")
						.add(Expression.eq("codigo", institucion))
				.list();
	}
	
	
	
	
	
	
	
	
	
	
}
