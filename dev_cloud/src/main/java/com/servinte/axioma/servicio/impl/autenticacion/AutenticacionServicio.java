/**
 * 
 */
package com.servinte.axioma.servicio.impl.autenticacion;


import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.usuario.UsuarioDTO;
import com.servinte.axioma.mundo.excepcion.autenticacion.AutenticacionExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.IntentosSuperadosExcepcion;
import com.servinte.axioma.mundo.fabrica.AutenticacionMundoFabrica;
import com.servinte.axioma.mundo.interfaz.autenticacion.IAutenticacionMundo;
import com.servinte.axioma.servicio.autenticacion.IAutenticacionServicio;
import com.servinte.axioma.servicio.excepcion.ServicioException;

/**
 * Implementaci&oacute;n de la interfaz {@link IAutenticacionServicio}.
 * 
 * @author Fernando Ocampo
 * 
 */
public class AutenticacionServicio implements IAutenticacionServicio {

	/**
	 * Referencia objeto del mundo para l&oacute;gica
	 * de autenticaci&oacute;n.
	 */
	private IAutenticacionMundo autenticacionMundo;

	public AutenticacionServicio() {
		inicializar();
	}
	
	/**
	 * Inicializa los datos de este objeto.
	 */
	private void inicializar() {
		autenticacionMundo = AutenticacionMundoFabrica.crearAutenticacionMundo();
	}

	/**
	 * @see com.servinte.axioma.servicio.autenticacion.IAutenticacionServicio#autenticarUsuario(com.princetonsa.dto.usuario.UsuarioDTO)
	 */
	@Override
	public void autenticarUsuario(UsuarioDTO usuario) throws ServicioException {
		try {
			autenticacionMundo.autenticarUsuario(usuario);
		} catch(AutenticacionExcepcion ex) {
			// Cuando es excepcion del mundo no se hace log.
			throw ex;
		} catch (Throwable e) {
			Log4JManager.error("Ocurrio un error inexperado al invocar " +
					"el mundo", e);
			throw new ServicioException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.autenticacion.IAutenticacionServicio#autenticarUsuarioConIntentos(com.princetonsa.dto.usuario.UsuarioDTO)
	 */
	@Override
	public void autenticarUsuarioConIntentos(UsuarioDTO usuario)
			throws ServicioException {
		try {
			autenticacionMundo.autenticarUsuarioConIntentos(usuario);
		} catch(AutenticacionExcepcion ex) {
			// Cuando es excepcion del mundo no se hace log.
			throw ex;
		} catch(IntentosSuperadosExcepcion ex) {
			// Cuando es excepcion del mundo no se hace log.
			throw ex;
		} catch (Throwable e) {
			Log4JManager.error("Ocurrio un error inexperado al invocar " +
					"el mundo", e);
			throw new ServicioException(e);
		}
	}

}
