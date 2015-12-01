/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.autenticacion;

import com.princetonsa.dto.usuario.UsuarioDTO;
import com.servinte.axioma.mundo.excepcion.autenticacion.AutenticacionExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.IntentosSuperadosExcepcion;

/**
 * Define la l&oacute;gica de negocio relacionada con la autenticaci&oacute;n de
 * usuarios en el sistema.
 * 
 * @author Fernando Ocampo
 * @see com.servinte.axioma.mundo.impl.autenticacion.AutenticacionMundo
 * @anexo 1034
 */
public interface IAutenticacionMundo {

	/**
	 * Valida si el usuario pasado c&oacute;mo parametro es un usuario valido
	 * del sistema.
	 * 
	 * @param usuario
	 *            Usuario a autenticar.
	 * @throws AutenticacionExcepcion
	 *             Si el usuario no es valido o ocurre un error al invocar el
	 *             servicio.
	 */
	public void autenticarUsuario(UsuarioDTO usuario)
			throws AutenticacionExcepcion;

	/**
	 * Valida si el usuario pasado c&oacute;mo parametro es un usuario valido.
	 * Tambi&eacute;n valida que la cantidad de intentos realizados por el
	 * usuario no supere la establecida en el sistema.
	 * 
	 * @param usuario
	 *            Usuario a autenticar.
	 * @throws AutenticacionExcepcion
	 *             Si el usuario no es valido u ocurre un error al invocar 
	 *             el servicio.
	 * @throws IntentosSuperadosExcepcion
	 * 			   Si el usuario ha intentado autenticarse m&aacute;s de
	 * 			   las veces permitidas. 
	 */
	public void autenticarUsuarioConIntentos(UsuarioDTO usuario)
			throws AutenticacionExcepcion, IntentosSuperadosExcepcion;
}
