package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;

import com.servinte.axioma.orm.Roles;
import com.servinte.axioma.orm.RolesUsuarios;
import com.servinte.axioma.orm.RolesUsuariosHome;
import com.servinte.axioma.orm.Usuarios;

/**
 * 
 * @author axioma
 */
public class RolesUsuariosDelegate extends RolesUsuariosHome {
	
	
	
	/**
	 * buscarRolUsuario
	 * @param usuario
	 * @param rol
	 * @return
	 */
	public RolesUsuarios buscarRolUsuario(Usuarios usuario, Roles rol) {
		try {
			return (RolesUsuarios) sessionFactory.getCurrentSession()
				.createCriteria(RolesUsuarios.class)
				.add(Expression.eq("usuarios", usuario))
				.add(Expression.eq("roles", rol))
				.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
	/**
	 * Lista los resultados según un parametro específico de busqueda y una lista de coincidencias
	 * @param campoTabla
	 * @param listaValores
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Roles> ListarPorCampoCoincidencia (String campoTabla, String[] listaValores){
		
		return (ArrayList<Roles>) sessionFactory.getCurrentSession()
			.createCriteria(Roles.class)
			.add(Expression.in(campoTabla, listaValores))
			.list();
		
	}
	
	
	

	/**
	 * 
	 * @param usuario
	 * @param rol
	 * @return
	 */
	public RolesUsuarios traerRolUsuario(Usuarios usuario) {
		try {
			return (RolesUsuarios) sessionFactory.getCurrentSession()
				.createCriteria(RolesUsuarios.class)
				.add(Expression.eq("usuarios", usuario))
				.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	

}
