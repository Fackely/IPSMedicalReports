package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IRolesFuncionalidadesDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IRolesFuncionalidadesMundo;


/**
 * L&oacute;gica de Negocio para todo lo relacionado con RolesFuncionalidades
 * 
 * 
 * @author Cristhian Murillo
 * @see IRolesFuncionalidadesMundo
 */

public class RolesFuncionalidadesMundo implements IRolesFuncionalidadesMundo {

	
	private IRolesFuncionalidadesDAO rolesFuncionalidadesDAO;
	
	public RolesFuncionalidadesMundo() {
		inicializar();
	}

	private void inicializar() {
		rolesFuncionalidadesDAO = AdministracionFabricaDAO.crearRolesFuncionalidadesDAO();
	}

	
	
	@Override
	public List<DtoModuloRolFuncionalidad> obtenerModuloRolFuncionalidadPorRol(
			String nombreRol) {
		return rolesFuncionalidadesDAO.obtenerModuloRolFuncionalidadPorRol(nombreRol);
	}

	@Override
	public ArrayList<DtoRolesFuncionalidades> obtenerFuncionalidadesRol(
			String nombreRol) {
		return rolesFuncionalidadesDAO.obtenerFuncionalidadesRol(nombreRol);
	}

	@Override
	public boolean eliminarRolFuncionalidad(
			DtoRolesFuncionalidades dtoRolesFuncionalidades) {
		return rolesFuncionalidadesDAO.eliminarRolFuncionalidad(dtoRolesFuncionalidades);
	}

	@Override
	public boolean adicionarRolFuncionalidad(String nombreRol,
			int codigoFuncionalidad) {
		return rolesFuncionalidadesDAO.adicionarRolFuncionalidad(nombreRol,codigoFuncionalidad);
	}
	
	
	
}
