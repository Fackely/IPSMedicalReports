package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;

import util.ConstantesBD;

import com.servinte.axioma.orm.OtrosDiagnosticos;
import com.servinte.axioma.orm.TiposUsuarios;
import com.servinte.axioma.orm.TiposUsuariosHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class TiposUsuariosDelegate extends TiposUsuariosHome{
	
	
	/**
	 * 	Retorna los tipos de usuarios por su actividad asignada
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TiposUsuarios> listarTodosXActividadesXInstitucion(String[] listaIntegridadDominio, int institucion){
		
		return (ArrayList<TiposUsuarios>) sessionFactory.getCurrentSession()
				.createCriteria(TiposUsuarios.class)
				.add(Expression.in("actividad", listaIntegridadDominio))
				.createCriteria("instituciones")
					.add(Expression.eq("codigo", institucion))
				.list();
	}
	
	
	
	
	/**
	 * Lista los resultados seg&uacute;n un parametro espec&iacute;fico de busqueda y una lista de coincidencias
	 * @param campoTabla
	 * @param listaValores
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TiposUsuarios> ListarPorCoincidenciasCampo (String campoTabla, String[] listaValores){
		
		return (ArrayList<TiposUsuarios>) sessionFactory.getCurrentSession()
			.createCriteria(TiposUsuarios.class)
			.add(Expression.in(campoTabla, listaValores))
			.list();
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
