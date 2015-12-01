package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;
import com.servinte.axioma.business.general.RolesFuncionalidadesMundo;

/**
 * Define el comportamiento del objeto que implemente de RolesFuncionalidadesMundo
 * 
 * @see RolesFuncionalidadesMundo
 * @author Cristhian Murillo
 */
public interface IRolesFuncionalidadesMundo {
	

	/**
	 * Busca las funcionalidades asociadas al rol
	 * 
	 * @param nombreRol
	 * @return Relacion de las funcionalidades con Modulo y Rol
	 */
	public List<DtoModuloRolFuncionalidad> obtenerModuloRolFuncionalidadPorRol (String nombreRol);

	public ArrayList<DtoRolesFuncionalidades> obtenerFuncionalidadesRol(
			String nombreRol);

	/**
	 * 
	 * @param dtoRolesFuncionalidades
	 * @return
	 */
	public boolean eliminarRolFuncionalidad(
			DtoRolesFuncionalidades dtoRolesFuncionalidades);

	/**
	 * 
	 * @param nombreRol
	 * @param codigoFuncionalidad
	 * @return
	 */
	public boolean adicionarRolFuncionalidad(String nombreRol,
			int codigoFuncionalidad);
	
	
}
