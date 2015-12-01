package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;

import com.servinte.axioma.orm.RegistroEgresosDeCaja;
import com.servinte.axioma.orm.RegistroEgresosDeCajaHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class RegistroEgresosDeCajaDelegate extends RegistroEgresosDeCajaHome{
	
	
	
	/**
	 * Lista todos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<RegistroEgresosDeCaja> listarTodos(){
		return (ArrayList<RegistroEgresosDeCaja>) sessionFactory.getCurrentSession()
				.createCriteria(RegistroEgresosDeCaja.class)
				.list();
	}

	
	
	/**
	 * Retorna una el objeto por un campo en especifico
	 * @param campoTabla
	 * @param campoDato
	 */
	public RegistroEgresosDeCaja buscarPorCampo(String campoTabla, String campoDato) {
		try {
			return (RegistroEgresosDeCaja) sessionFactory.getCurrentSession()
					.createCriteria(RegistroEgresosDeCaja.class)
					.add(Expression.eq(campoTabla, campoDato))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
	
}
