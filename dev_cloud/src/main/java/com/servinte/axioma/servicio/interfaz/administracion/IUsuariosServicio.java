package com.servinte.axioma.servicio.interfaz.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Usuarios;

/**
 * Servicio que le delega al negocio las operaciones del Usuario
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see com.servinte.axioma.servicio.impl.administracion.UsuariosServicio
 */

public interface IUsuariosServicio {

	/**
	 * Busca y retorna el usuario en base de datos que tenga el login pasado como par&aacute;metro.
	 * 
	 * @param login
	 * @return Usuario que tiene el login pasado como par&aacute;metro.
	 */
	public Usuarios buscarPorLogin (String login);
	
	/**
	 * Retorna informacion muy basica sobre un usuario/persona del sistema
	 * @param loginUsuario
	 * @return DtoUsuarioPersona
	 */
	public DtoUsuarioPersona obtenerDtoUsuarioPersona(String loginUsuario);

	/**
	 * 
	 * @param institucion
	 * @param filtrarActivos
	 * @return
	 */
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosSistemas(int institucion, boolean filtrarActivos);
	
	/**
	 * Retorna informacion muy basica sobre los usuarios que tienen permisos para centros de costo de un 
	 * centro de atención
	 * @param consecutivoCentroAtencion
	 * @return List<{@link DtoUsuarioPersona}>
	 */
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(int consecutivoCentroAtencion);
}
