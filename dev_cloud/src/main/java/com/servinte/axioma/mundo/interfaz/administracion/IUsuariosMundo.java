package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.mundo.impl.administracion.UsuariosMundo;
import com.servinte.axioma.orm.Usuarios;

/**
 * Define el comportamiento del objeto que implemente de UsuariosMundo
 * 
 * @see UsuariosMundo
 * @author Jorge Armando Agudelo Quintero
 */
public interface IUsuariosMundo {
	

	
	/**
	 * Busca y retorna el usuario en base de datos que tenga el login pasado c&oacute;mo par&aacute;metro.
	 * 
	 * @param login
	 * @return Usuario que tiene el login pasado c&oacute;mo par&aacute;metro.
	 *
	 */
	public Usuarios buscarPorLogin(String login);
	
	
	
	
	/**
	 * Retorna Los usuarios activos relacionados con el centro de atencion y diferentes del usuario enviado
	 * @param usuario
	 * @return List<{@link Usuarios}>
	 */
	public List<DtoUsuarioPersona> obtenerUsuariosActivosDiferenteDe(int codInstitucion, String loginUsuario, boolean incluirInactivos);
	
	
	
	
	/**
	 * Retorna informacion muy basica sobre un usuario/persona del sistema
	 * @param loginUsuario
	 * @return DtoUsuarioPersona
	 */
	public DtoUsuarioPersona obtenerDtoUsuarioPersona(String loginUsuario);




	public ArrayList<DtoUsuarioPersona> obtenerUsuariosSistemas(
			int institucion, boolean filtrarActivos);
	
	
	/**
	 * Retorna informacion muy basica sobre los usuarios que tienen permisos para centros de costo de un 
	 * centro de atención
	 * @param consecutivoCentroAtencion
	 * @return List<{@link DtoUsuarioPersona}>
	 */
	public ArrayList<DtoUsuarioPersona> obtenerUsuariosConPermisosCentroCostoPorCentroAtencion(int consecutivoCentroAtencion);
	
}
