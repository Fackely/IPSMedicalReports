package com.servinte.axioma.servicio.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;
import com.princetonsa.dto.odontologia.DtoRolesTipoDeUsuario;

/**
 * Servicio que le delega al negocio las operaciones del Usuario
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.servicio.impl.administracion.RolesFuncionalidadesServicio
 */

public interface IRolesFuncionalidadesServicio {

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
	public ArrayList<DtoRolesFuncionalidades> obtenerFuncionalidadesRol(String nombreRol);

	/**
	 * 
	 * @param dtoRolesFuncionalidades
	 */
	public boolean eliminarRolFuncionalidad(DtoRolesFuncionalidades dtoRolesFuncionalidades);


	/**
	 * 
	 * @param nombreRol
	 * @param codigoFuncionalidad
	 */
	public boolean adicionarRolFuncionalidad(String nombreRol,int codigoFuncionalidad);
}
