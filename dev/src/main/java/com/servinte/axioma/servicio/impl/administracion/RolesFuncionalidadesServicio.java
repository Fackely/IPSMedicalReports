package com.servinte.axioma.servicio.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;
import com.princetonsa.dto.odontologia.DtoRolesTipoDeUsuario;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IRolesFuncionalidadesMundo;
import com.servinte.axioma.servicio.interfaz.administracion.IRolesFuncionalidadesServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link IRolesFuncionalidadesServicio}
 * 
 * @author Cristhian Murillo
 *
 */
public class RolesFuncionalidadesServicio implements IRolesFuncionalidadesServicio {

	IRolesFuncionalidadesMundo rolesFuncionalidadesMundo;
	
	
	public RolesFuncionalidadesServicio() {
		rolesFuncionalidadesMundo = AdministracionFabricaMundo.crearloroRolesFuncionalidadesMundo();
	} 
	
	
	@Override
	public List<DtoModuloRolFuncionalidad> obtenerModuloRolFuncionalidadPorRol(
			String nombreRol) {
		return rolesFuncionalidadesMundo.obtenerModuloRolFuncionalidadPorRol(nombreRol);
	}


	@Override
	public ArrayList<DtoRolesFuncionalidades> obtenerFuncionalidadesRol(
			String nombreRol) {
		return rolesFuncionalidadesMundo.obtenerFuncionalidadesRol(nombreRol);
	}


	@Override
	public boolean eliminarRolFuncionalidad(
			DtoRolesFuncionalidades dtoRolesFuncionalidades) 
	{
		return rolesFuncionalidadesMundo.eliminarRolFuncionalidad(dtoRolesFuncionalidades);
	}


	@Override
	public boolean adicionarRolFuncionalidad(String nombreRol,
			int codigoFuncionalidad) {
		return rolesFuncionalidadesMundo.adicionarRolFuncionalidad(nombreRol,codigoFuncionalidad);
	}
	
	


	
	
}
