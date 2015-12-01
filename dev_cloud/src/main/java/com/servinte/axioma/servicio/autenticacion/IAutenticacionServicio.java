/**
 * 
 */
package com.servinte.axioma.servicio.autenticacion;

import com.princetonsa.dto.usuario.UsuarioDTO;
import com.servinte.axioma.mundo.excepcion.autenticacion.AutenticacionExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.IntentosSuperadosExcepcion;
import com.servinte.axioma.servicio.excepcion.ServicioException;

/**
 * Define el comportamiento del servicio para autenticaci&oacute;n de usuarios
 * del sistema.
 * 
 * @author Fernando Ocampo
 * @version 0.1.0
 * @see com.servinte.axioma.servicio.impl.autenticacion.AutenticacionServicio
 * @anexo 1034
 */
public interface IAutenticacionServicio {

	/**
	 * Valida si el usuario pasado c&oacute;mo parametro es un usuario valido.
	 * 
	 * @param usuario
	 *            Usuario a autenticar.
	 * @throws ServicioException
	 *             Si ocurre un error al invocar el servicio.
	 * @throws AutenticacionExcepcion
	 *             Si el usuario no es valido o ocurre un error al invocar el
	 *             servicio.
	 */
	public void autenticarUsuario(UsuarioDTO usuario) throws ServicioException;

	/**
	 * Valida si el usuario pasado c&oacute;mo parametro es un usuario valido.
	 * Tambi&eacute;n valida que la cantidad de intentos realizados por el
	 * usuario no supere la establecida en el sistema.
	 * 
	 * @param usuario
	 *            Usuario a autenticar.
	 * @throws ServicioException
	 *             Si ocurre un error al invocar el servicio.
	 * @throws AutenticacionExcepcion
	 *             Si el usuario no es valido u ocurre un error al invocar 
	 *             el servicio.
	 * @throws IntentosSuperadosExcepcion
	 * 			   Si el usuario ha intentado autenticarse m&aacute;s de
	 * 			   las veces permitidas. 
	 */
	public void autenticarUsuarioConIntentos(UsuarioDTO usuario)
			throws ServicioException;
}
