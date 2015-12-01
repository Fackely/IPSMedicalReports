/**
 * 
 */
package com.servinte.axioma.mundo.impl.autenticacion;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.usuario.UsuarioDTO;
import com.servinte.axioma.dao.excepcion.DAOExcepcion;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IUsuarioDAO;
import com.servinte.axioma.mundo.excepcion.autenticacion.AutenticacionExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.IntentosSuperadosExcepcion;
import com.servinte.axioma.mundo.interfaz.autenticacion.IAutenticacionMundo;
import com.servinte.axioma.orm.Usuarios;

/**
 * Contiene la l&oacute;gica de negocio para autenticar un usuario en 
 * el sistema.
 * 
 * @author Fernando Ocampo
 * @see IAutenticacionMundo
 * @anexo 1034
 */
public class AutenticacionMundo implements IAutenticacionMundo {

	/**
	 * DAO de los datos de usuarios.
	 */
	private IUsuarioDAO usuarioDAO;

	public AutenticacionMundo() {
		inicializar();
	}

	private void inicializar() {
		usuarioDAO = AdministracionFabricaDAO.crearUsuarioDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.autenticacion.IAutenticacionMundo#autenticarUsuario(com.princetonsa.dto.usuario.UsuarioDTO)
	 */
	@Override
	public void autenticarUsuario(UsuarioDTO usuario)
			throws AutenticacionExcepcion {
		Usuarios usuarioEntidad = null;
		try {
			usuarioEntidad = usuarioDAO.buscarPorLoginYContrasena(usuario);
		} catch (DAOExcepcion e) {
			throw new AutenticacionExcepcion("Ocurrio un error al consultar el " +
					"usuario por login y contraseña, intente más tarde.",e);
		} catch (Exception e) {
			String strMensaje = "Ocurrio un error al acceder los " +
				"datos, intente más tarde.";
			Log4JManager.error(strMensaje, e);
			throw new AutenticacionExcepcion(strMensaje,e);
		}
		
		if(usuarioEntidad == null) {
			throw new AutenticacionExcepcion("La contraseña no coincide con " +
					"la del usuario . Por favor Verifique");
		}
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.autenticacion.IAutenticacionMundo#autenticarUsuarioConIntentos(com.princetonsa.dto.usuario.UsuarioDTO)
	 */
	@Override
	public void autenticarUsuarioConIntentos(UsuarioDTO usuario)
			throws AutenticacionExcepcion, IntentosSuperadosExcepcion {
		// El numero 3 deberia ser configurable o parametrizable.
		if(usuario.getIntentos() < 3) {
			autenticarUsuario(usuario);
		} else { // Si los intentos son mas de 3 arroja excepcion.
			throw new IntentosSuperadosExcepcion("La contraseña no coincide con la " +
					"del usuario en sesión, Superó su número de intentos fallidos " +
					"para realizar Confirmación de contraseña.");
		}
		
	}

}
