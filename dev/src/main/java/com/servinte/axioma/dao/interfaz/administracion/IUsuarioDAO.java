/**
 * 
 */
package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.dto.usuario.UsuarioDTO;
import com.servinte.axioma.dao.excepcion.DAOExcepcion;
import com.servinte.axioma.orm.Usuarios;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de usuarios.
 * 
 * @author Fernando Ocampo
 *
 */
public interface IUsuarioDAO {

	
	
	/**
	 * Busca y retorna el usuario en base de datos que tenga el login y
	 * contrase&ntilde;a pasadas c&oacute;mo parametro.
	 * 
	 * @param usuario Usuario a validar.
	 * @return Usuario que tiene el login y contrase&ntilde;a pasadas 
	 * 		   c&oacute;mo parametro.
	 * @throws DAOExcepcion Si ocurre un error al invocar el m&eacute;todo.
	 * @anexo 1034
	 */
	public Usuarios buscarPorLoginYContrasena(UsuarioDTO usuario) throws DAOExcepcion;
	
	
	/**
	 * Busca y retorna el usuario en base de datos que tenga el login pasado c&oacute;mo par&aacute;metro.
	 * 
	 * @param login
	 * @return Usuario que tiene el login pasado c&oacute;mo par&aacute;metro.
	 * @anexo 226
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


	/**
	 * 
	 * @param institucion
	 * @param filtrarActivos
	 * @return
	 */
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
