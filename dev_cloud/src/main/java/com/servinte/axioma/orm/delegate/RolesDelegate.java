package com.servinte.axioma.orm.delegate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Funcionalidades;
import com.servinte.axioma.orm.Roles;
import com.servinte.axioma.orm.RolesHome;

public class RolesDelegate extends RolesHome{
	public boolean validarRolesUsuario(UsuarioBasico usuarioBasico, String pathFuncionalidad)
	{
		return sessionFactory.getCurrentSession().
				createCriteria(Funcionalidades.class).
				add(Expression.like("archivoFunc", "%"+pathFuncionalidad+"/%")).
				createCriteria("rolesFuncionalidadeses").
				createCriteria("roles").
				createCriteria("usuarioses").
				add(Expression.eq("login", usuarioBasico.getLoginUsuario())).
				list().size()>0;
	}
	
	@SuppressWarnings("unchecked")
	public List<Roles> ListarRoles()
	{
		return sessionFactory.getCurrentSession().createCriteria(Roles.class).addOrder(Order.asc("nombreRol")).list();
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
			//.add(Expression.in("tiposUsuarios.actividad", listaValores))
			.add(Expression.in(campoTabla, listaValores))
			.list();
		
	}
	
	
	/**
	 * Lista los resultados seg&uacute;n un parametro espec&iacute;fico de busqueda y una lista de coincidencias
	 * @param campoTabla
	 * @param listaValores
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Roles> ListarPorActividad (String[] listaValores){
		
		return (ArrayList<Roles>) sessionFactory.getCurrentSession()
			.createCriteria(Roles.class)
			.createCriteria("tiposUsuarioses")
				.add(Expression.in("actividad", listaValores))
			.list();
		
	}
	
	
	
	/**
	 * Lista los roles de acuerdo a la Instituci&oacute;n
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Roles> listarPorInstitucion (int institucion){
		
		return (ArrayList<Roles>) sessionFactory.getCurrentSession()
			.createCriteria(Roles.class)
			.createCriteria("instituciones")
				.add(Expression.eq("codigo", institucion))
			.list();
		
	}
	
	
	
	
	
}
