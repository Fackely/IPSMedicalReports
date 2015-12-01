/*
 * MAyo 04, 2010
 */
package com.servinte.axioma.business.general;

import java.util.ArrayList;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Roles;
import com.servinte.axioma.orm.delegate.RolesDelegate;


/**
 * @author Cristhian Murillo
 * Define la l&oacute;gica del negocio
 *
 */
public class RolesFuncionalidadesMundo {

	
	/**
	 * Carga los roles asociados a la Instituci&oacute;n del usuario actual
	 * @param usuario
	 * @return {@link CuentaGeneralInterfaz} Objeto validado con cada una de sus cuentas 
	 */
	public static ArrayList<Roles> mostrarRolesXInstitucion(UsuarioBasico usuario){
		
		HibernateUtil.beginTransaction();

		ArrayList<Roles> listaRoles = new RolesDelegate()
			.listarPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion())); 
		
		HibernateUtil.endTransaction();
		
		for(Roles rol: listaRoles){
			rol.getNombreRol();
		}
		
		return listaRoles;
		
	}
	

	/**
	 * Carga los roles asociados a la Instituci&oacute;n del usuario actual
	 * @param usuario
	 * @return {@link CuentaGeneralInterfaz} Objeto validado con cada una de sus cuentas 
	 */
	public static ArrayList<Roles> mostrarRoles(){
		
		HibernateUtil.beginTransaction();

		ArrayList<Roles> listaRoles = (ArrayList<Roles>)( new RolesDelegate()
			.ListarRoles()); 
		
		HibernateUtil.endTransaction();
		
		for(Roles rol: listaRoles){
			rol.getNombreRol();
		}
		
		return listaRoles;
		
	}
	
	
	
	
}
