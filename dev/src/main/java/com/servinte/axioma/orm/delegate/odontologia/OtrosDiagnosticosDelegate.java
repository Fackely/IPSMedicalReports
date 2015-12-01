package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;

import util.ConstantesBD;

import com.servinte.axioma.orm.OtrosDiagnosticos;
import com.servinte.axioma.orm.OtrosDiagnosticosHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class OtrosDiagnosticosDelegate extends OtrosDiagnosticosHome{
	
	
	/**
	 * 	Retorna una lista de los activos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OtrosDiagnosticos> listarActivos(){
		
		return (ArrayList<OtrosDiagnosticos>) sessionFactory.getCurrentSession()
				.createCriteria(OtrosDiagnosticos.class)
				.add(Expression.eq("activo", ConstantesBD.acronimoSi))
				.list();
	}
	
	
	/**
	 * Retorna una lista de todos los diagnosticos discriminando su estado (activo)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OtrosDiagnosticos> listarTodos(){
		
		return (ArrayList<OtrosDiagnosticos>) sessionFactory.getCurrentSession()
				.createCriteria(OtrosDiagnosticos.class)
				.list();
	}
	
	
	
	
	/**
	 * 	Retorna una lista de todos los diagnosticos discriminando su estado (activo)
	 * 	de acuerdo a la insitucion del usuario actual
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OtrosDiagnosticos> listarTodosPorInstitucion(int institucion){
		
		return (ArrayList<OtrosDiagnosticos>) sessionFactory.getCurrentSession()
				.createCriteria(OtrosDiagnosticos.class)
				.createCriteria("instituciones").add(Expression.eq("codigo", institucion))
				.list();
	}
	
	
	
	/**
	 * Retorna una el objeto por su codigo
	 * @param codigo
	 */
	public OtrosDiagnosticos buscarPorCodigo(String codigo) {
		try {
			OtrosDiagnosticos instance = (OtrosDiagnosticos) sessionFactory.getCurrentSession()
					.createCriteria(OtrosDiagnosticos.class)
					.add(Expression.eq("codigo", codigo))
					.uniqueResult();
			return instance;
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
	/**
	 * Retorna una el objeto por un campo en especifico
	 * @param campoTabla
	 * @param campoDato
	 */
	public OtrosDiagnosticos buscarPorCampo(String campoTabla, String campoDato) {
		try {
			return (OtrosDiagnosticos) sessionFactory.getCurrentSession()
					.createCriteria(OtrosDiagnosticos.class)
					.add(Expression.eq(campoTabla, campoDato))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
}
