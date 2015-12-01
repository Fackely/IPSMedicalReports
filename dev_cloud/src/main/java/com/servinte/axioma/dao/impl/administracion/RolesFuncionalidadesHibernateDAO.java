package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;
import com.servinte.axioma.dao.interfaz.administracion.IRolesFuncionalidadesDAO;
import com.servinte.axioma.orm.DependenciasFunc;
import com.servinte.axioma.orm.DependenciasFuncHome;
import com.servinte.axioma.orm.DependenciasFuncId;
import com.servinte.axioma.orm.Funcionalidades;
import com.servinte.axioma.orm.Roles;
import com.servinte.axioma.orm.RolesFuncionalidades;
import com.servinte.axioma.orm.RolesFuncionalidadesId;
import com.servinte.axioma.orm.delegate.DependenciasFuncDelegate;
import com.servinte.axioma.orm.delegate.FuncionalidadesDelegate;
import com.servinte.axioma.orm.delegate.RolesDelegate;
import com.servinte.axioma.orm.delegate.administracion.RolesFuncionalidadesDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IRolesFuncionalidadesDAO}.
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.orm.delegate.administracion.RolesFuncionalidadesDelegate
 */
public class RolesFuncionalidadesHibernateDAO implements IRolesFuncionalidadesDAO{

	
	private RolesFuncionalidadesDelegate rolesFuncionalidadesDelegate;

	
	public RolesFuncionalidadesHibernateDAO() {
		rolesFuncionalidadesDelegate = new RolesFuncionalidadesDelegate();
	}

	
	
	@Override
	public List<DtoModuloRolFuncionalidad> obtenerModuloRolFuncionalidadPorRol(
			String nombreRol) {
		return rolesFuncionalidadesDelegate.obtenerModuloRolFuncionalidadPorRol(nombreRol);
	}



	@Override
	public ArrayList<DtoRolesFuncionalidades> obtenerFuncionalidadesRol(
			String nombreRol) {
		return rolesFuncionalidadesDelegate.obtenerFuncionalidadesRol(nombreRol);
	}



	@Override
	public boolean eliminarRolFuncionalidad(
			DtoRolesFuncionalidades dtoRolesFuncionalidades) {

		rolesFuncionalidadesDelegate.delete(rolesFuncionalidadesDelegate.findById(new RolesFuncionalidadesId(dtoRolesFuncionalidades.getNombreRol(),dtoRolesFuncionalidades.getCodigoFunc())));
		return true;

	}



	@Override
	public void adicionarRolFuncionalidadEspecifica(String nombreRol,
			int codigoFuncionalidad) {
		
		//funcinalidades
		FuncionalidadesDelegate daoF=new FuncionalidadesDelegate();
		Funcionalidades dtoF=new Funcionalidades();
		dtoF=daoF.findById(codigoFuncionalidad);
		
		//roles
		RolesDelegate daoR=new RolesDelegate();
		Roles dtoR=new Roles();
		dtoR=daoR.findById(nombreRol);
		
		//id
		RolesFuncionalidadesId id=new RolesFuncionalidadesId();
		id.setCodigoFunc(codigoFuncionalidad);
		id.setNombreRol(nombreRol);
		
		RolesFuncionalidades dto=new RolesFuncionalidades();
		dto.setFuncionalidades(dtoF);
		dto.setId(id);
		dto.setRoles(dtoR);
		dto.setIsssl(false);
		
		RolesFuncionalidades dtoTempo=new RolesFuncionalidades();
		dtoTempo=rolesFuncionalidadesDelegate.findById(dto.getId());
		if(dtoTempo==null)
			rolesFuncionalidadesDelegate.persist(dto);
		
		
	}

	public boolean adicionarRolFuncionalidad(String nombreRol,
			int codigoFuncionalidad) 
	{
		adicionarRolFuncionalidadEspecifica(nombreRol, codigoFuncionalidad);
		//DependenciasFuncDelegate dao =new DependenciasFuncDelegate();
		ArrayList<Integer> funcionalidadesHija = UtilidadesAdministracion.obtenerFuncionalidadHija(codigoFuncionalidad);
		if (funcionalidadesHija != null) {
			for (Integer codigoFunc : funcionalidadesHija) {
				adicionarRolFuncionalidad(nombreRol, codigoFunc);
			}
			return true;
		} else {
			return false;
		}
	}
	
}
