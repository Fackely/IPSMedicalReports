/*
 * Julio 13, 2010
 */
package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.orm.RolesFuncionalidades;
import com.servinte.axioma.orm.RolesFuncionalidadesHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class RolesFuncionalidadesDelegate extends RolesFuncionalidadesHome{
	
	
	
	/**
	 * Busca las funcionalidades asociadas al rol
	 * 
	 * @param nombreRol
	 * @return Relacion de las funcionalidades con Modulo y Rol
	 */
	@SuppressWarnings("unchecked")
	//FIXME este metodo sera reemplazado por el de funcionalidades delegate
	public List<DtoModuloRolFuncionalidad> obtenerModuloRolFuncionalidadPorRol (String nombreRol){
		
		Criteria criteria 	= sessionFactory.getCurrentSession().createCriteria(RolesFuncionalidades.class, "rolesFunc");
		
		criteria.createAlias("rolesFunc.roles"				,"roles");
		criteria.createAlias("rolesFunc.funcionalidades"	,"funcionalidades",Criteria.FULL_JOIN);
		criteria.createAlias("funcionalidades.moduloses"	,"moduloses");
		
		criteria.add(Restrictions.like("roles.nombreRol", "%"+nombreRol+"%"));
		criteria.add(Restrictions.eq("funcionalidades.esParametrizable", true));
		
		
		
		ProjectionList projectionList 	= Projections.projectionList();
		projectionList.add( Projections.property("roles")													,"roles");
		projectionList.add( Projections.property("funcionalidades")											,"funcionalidadesPadre");
		//projectionList.add( Projections.property("moduloses")												,"modulos");
		
		criteria.setProjection(projectionList);

		criteria.setResultTransformer(Transformers.aliasToBean(DtoModuloRolFuncionalidad.class));
		
		List<DtoModuloRolFuncionalidad> lista = (List<DtoModuloRolFuncionalidad>) criteria.list();
		for (DtoModuloRolFuncionalidad dtoModuloRolFuncionalidad : lista) {
			dtoModuloRolFuncionalidad.getRoles().getNombreRol();
			dtoModuloRolFuncionalidad.getFuncionalidadesPadre().getNombreFunc();
		}
		
		return lista;
	}	
		
	/**
	 * Retorna todas las funcionalidades de un rol
	 * @author Diana Ruiz - Cristhian Murillo
	 * @param nombreRol
	 * @return ArrayList<DtoRolesFuncionalidades>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoRolesFuncionalidades> obtenerFuncionalidadesRol(String nombreRol) 
	{
		Criteria criteria 	= sessionFactory.getCurrentSession().createCriteria(RolesFuncionalidades.class, "rolesFuncionalidades");	
		
		criteria.createAlias("rolesFuncionalidades.funcionalidades"		,"funcionalidades");
		criteria.createAlias("rolesFuncionalidades.roles"				,"roles");
			
		criteria.add(Restrictions.eq("roles.nombreRol"			, nombreRol));
			
		criteria.setProjection(Projections.projectionList()	
			.add(Projections.property("rolesFuncionalidades.id.codigoFunc")		,"codigoFunc")
			.add(Projections.property("roles.nombreRol")						,"nombreRol")
			.add(Projections.property("funcionalidades.nombreFunc")				,"nombreFuncionalidad")
		)
		
		.setResultTransformer( Transformers.aliasToBean(DtoRolesFuncionalidades.class));
		
        criteria.addOrder(Order.asc("funcionalidades.nombreFunc"));
		
        ArrayList<DtoRolesFuncionalidades> listaRolesFuncionalidades = (ArrayList<DtoRolesFuncionalidades>)criteria.list();
		
		return listaRolesFuncionalidades;
	}




	
}
