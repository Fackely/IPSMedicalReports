package com.servinte.axioma.orm.delegate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.servinte.axioma.orm.Funcionalidades;
import com.servinte.axioma.orm.FuncionalidadesHome;

public class FuncionalidadesDelegate extends FuncionalidadesHome{

	public List<Funcionalidades> listarTodas() {
		return sessionFactory.getCurrentSession().createCriteria(Funcionalidades.class).list();
	}

	
	
	
	
	
	/**
	 * Busca las funcionalidades asociadas al rol
	 * 
	 * @param nombreRol
	 * @return Relacion de las funcionalidades con Modulo y Rol
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoModuloRolFuncionalidad> obtenerModuloRolFuncionalidadPorRol (String nombreRol)
	{
		Criteria criteria 	= sessionFactory.getCurrentSession().createCriteria(Funcionalidades.class, "funcionalidades");
		
		criteria.createAlias("funcionalidades.rolesFuncionalidadeses"	,"roles_func");
		criteria.createAlias("roles_func.roles"							,"roles");
		
		criteria.add(Restrictions.like("roles.nombreRol"				, "%"+nombreRol+"%"));
		criteria.add(Restrictions.eq("funcionalidades.esParametrizable"	, true));
		
		List<Funcionalidades> listaFuncionalidades = (List<Funcionalidades>) criteria.list();
		
		
		ArrayList<DtoModuloRolFuncionalidad> listaDtoModuloRolFuncionalidad = new ArrayList<DtoModuloRolFuncionalidad>();
		
		for(Funcionalidades func:listaFuncionalidades)
		{
			//FIXME terminar esta consulta 
			//System.out.println(func.get);
			Funcionalidades submodulo;
			while(!(submodulo=(Funcionalidades) func.getFuncionalidadesesForFuncionalidadHija()).isDeboImprimir())
			{
				DtoModuloRolFuncionalidad dtoModuloRolFuncionalidad = new DtoModuloRolFuncionalidad();
				dtoModuloRolFuncionalidad.setFuncionalidadesPadre(func);
				dtoModuloRolFuncionalidad.setFuncionalidadesHija(submodulo);
				//dtoModuloRolFuncionalidad.setModulos(func.getModuloses());
				//dtoModuloRolFuncionalidad.setRoles(func.getRolesFuncionalidadeses());
				
				listaDtoModuloRolFuncionalidad.add(dtoModuloRolFuncionalidad);
			}
			
			//submodulo.getModuloses().get
		}
		
		return listaDtoModuloRolFuncionalidad;
	}
	

	
	
	
	
	public static void main(String[] args) {
		FuncionalidadesDelegate d = new FuncionalidadesDelegate();
		List<DtoModuloRolFuncionalidad> lista=d.obtenerModuloRolFuncionalidadPorRol("gener");
		
	}






	public ArrayList<Funcionalidades> listarTodasParametrizables() {
		return ( ArrayList<Funcionalidades>)sessionFactory.getCurrentSession().createCriteria(Funcionalidades.class).add(Restrictions.eq("esParametrizable", true)).addOrder(Order.asc("nombreFunc")).list();
	}
}
