/**
 * 
 */
package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de RolesFuncionalidades.
 * 
 * @author Cristhian Murillo
 */
public interface IRolesFuncionalidadesDAO {

	/**
	 * Busca las funcionalidades asociadas al rol
	 * 
	 * @param nombreRol
	 * @return Relacion de las funcionalidades con Modulo y Rol
	 */
	public List<DtoModuloRolFuncionalidad> obtenerModuloRolFuncionalidadPorRol (String nombreRol);

	/**
	 * 
	 * @param nombreRol
	 * @return
	 */
	public ArrayList<DtoRolesFuncionalidades> obtenerFuncionalidadesRol(
			String nombreRol);

	public boolean eliminarRolFuncionalidad(
			DtoRolesFuncionalidades dtoRolesFuncionalidades);

	public boolean adicionarRolFuncionalidad(String nombreRol,
			int codigoFuncionalidad);

	public void adicionarRolFuncionalidadEspecifica(String nombreRol,
			int codigoFuncionalidad);	
}
