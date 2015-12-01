package com.servinte.axioma.orm.delegate.interfaz;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.CuentasXEspOdonto;
import com.servinte.axioma.orm.CuentasXEspOdontoHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class CuentasXEspOdontoDelegate extends CuentasXEspOdontoHome{
	
	

		
	/**
	 * Retorna una lista de todos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CuentasXEspOdonto> listarTodos(){
		return (ArrayList<CuentasXEspOdonto>) sessionFactory.getCurrentSession()
				.createCriteria(CuentasXEspOdonto.class)
				.list();
	}
	
	
	/**
	 * Retorna una el objeto por un campo en especifico
	 * @param campoTabla
	 * @param campoDato
	 */
	public CuentasXEspOdonto buscarPorCampo(String campoTabla, String campoDato) {
		try {
			return (CuentasXEspOdonto) sessionFactory.getCurrentSession()
					.createCriteria(CuentasXEspOdonto.class)
					.add(Expression.eq(campoTabla, campoDato))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	

	/**
	 * Lista de acuerdo a la especialidad y a su insitucion
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CuentasXEspOdonto> listarTodosPorEspecialidad(int institucion, String especialidad){
		
		return (ArrayList<CuentasXEspOdonto>) sessionFactory.getCurrentSession()
				.createCriteria(CuentasXEspOdonto.class)
				.createCriteria("especialidades")
					.addOrder(Order.asc("nombre"))
					.add(Expression.eq("tipoEspecialidad", especialidad))
					.createCriteria("instituciones")
						.add(Expression.eq("codigo", institucion))
						
				.list();
	}
	
	
	
	/**
	 * Lista de acuerdo a la especialidad y a su insitucion
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CuentasXEspOdonto> listarPorEspecialidadPorInstitucionPorCentroAtencion(int institucion, String especialidad, int centroAtencion){
		
		return (ArrayList<CuentasXEspOdonto>) sessionFactory.getCurrentSession()
				.createCriteria(CuentasXEspOdonto.class)
				.add(Expression.eq("centroAtencion.consecutivo", centroAtencion))
				.createCriteria("especialidades")
					.addOrder(Order.asc("nombre"))
					.add(Expression.eq("tipoEspecialidad", especialidad))
					.createCriteria("instituciones")
						.add(Expression.eq("codigo", institucion))
						
			.list();
	}
	
	
	
	
	
}
