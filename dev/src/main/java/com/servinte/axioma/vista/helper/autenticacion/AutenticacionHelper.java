/**
 * 
 */
package com.servinte.axioma.vista.helper.autenticacion;

import javax.servlet.http.HttpSession;

import com.princetonsa.dto.usuario.UsuarioDTO;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.vista.autenticacion.EAtributosAutenticacion;

import util.ConstantesBD;
import util.MD5Hash;

/**
 * Clase para ayudar a los objetos de la vista en procesar objetos, dar
 * formatos, entre otras.
 * 
 * @author Fernando Ocampo
 * 
 */
public final class AutenticacionHelper {

	private AutenticacionHelper() {
	}

	/**
	 * Genera un valor hash a partir del valor pasado por parametro.
	 * 
	 * @param valor con el cual generar la llave
	 * @return Valor hash
	 */
	public final static String obtenerValorHash(String valor) {
		String dato = valor + "FloatNaN1�" + System.currentTimeMillis();
		return MD5Hash.hashPassword(dato);
	}

	/**
	 * Obtiene los datos del usuario que esta en sesi&oacute;n.
	 * 
	 * @param session
	 * @return
	 */
	public final static UsuarioBasico getUsuarioBasico(HttpSession session) {
		UsuarioBasico usuario = null;
		if(session != null) {
			usuario = (UsuarioBasico)session.getAttribute(
					EAtributosAutenticacion.LLAVE_USUARIO_BASICO.getValor());
		}
		return usuario;
	}

	
	/**
	 * Obtiene el c&oacute;digo del centro de atenci&oacute;n del usuario de la sessi&oacute;n dada
	 * @param session
	 * @return
	 */
	public final static int getCodigoCentroAtencion(HttpSession session){
		int codigo = ConstantesBD.codigoNuncaValido;
		
		UsuarioBasico usuarioBasico = getUsuarioBasico(session);
		if(usuarioBasico != null){
			codigo = usuarioBasico.getCodigoCentroAtencion();
		}
		
		return codigo; 
	} 
	
	
	
	/**
	 * Retorna el nombre del usuario que esta autenticado en sesi&oacute;n. La
	 * sesi&oacute;n se obtiene de la petici&oacute;n.
	 * 
	 * @param session Sesi&oacute;n de la petici&oacute;n que requiere obtener 
	 * el nombre de usuario de sesi&oacute;n.
	 * @return Nombre del usuario en sesi&oacute;n.
	 */
	public final static String getNombreUsuarioBasico(HttpSession session) {
		String nombreUsuario = null;
		UsuarioBasico usuarioBasico = AutenticacionHelper.getUsuarioBasico(
				session);
		if(usuarioBasico != null) {
			nombreUsuario = usuarioBasico.getNombreUsuario();
		}
		return nombreUsuario;
	}


	/**
	 * Retorna el login del usuario que esta autenticado en sesi&oacute;n. La
	 * sesi&oacute;n se obtiene de la petici&oacute;n.
	 * 
	 * @param session Sesi&oacute;n de la petici&oacute;n que requiere obtener 
	 * el login de usuario de sesi&oacute;n.
	 * @return login del usuario en sesi&oacute;n.
	 */
	public final static String getLoginUsuarioBasico(HttpSession session) {
		String loginUsuario = null;
		UsuarioBasico usuarioBasico = AutenticacionHelper.getUsuarioBasico(
				session);
		if(usuarioBasico != null) {
			loginUsuario = usuarioBasico.getLoginUsuario();
		}
		return loginUsuario;
	}

	/**
	 * Retorna el n&uacute;mero de intentos de autenticaci&oacute;n realizados
	 * por el usuario.
	 * 
	 * @param sesion sesi&oacute;n http.
	 * @return n&uacute;mero de intentos de autenticaci&oacute;n.
	 */
	public final static byte getNumeroIntentos(HttpSession sesion) {
		byte intentos = (byte)0;
		if(sesion != null) {
			Object oIntentos = sesion.getAttribute(
					EAtributosAutenticacion.LLAVE_INTENTOS_LOGIN.getValor());
			if(oIntentos instanceof Byte) {
				intentos = (Byte)oIntentos;
			}
		}
		return intentos;
	}

	/**
	 * Crea y retorna una instancia de {@link UsuarioDTO} con datos que estan
	 * en sesi&oacute;n.
	 * 
	 * @param session Sesi&oacute;n del usuario que requiere autenticaci&oacute;n.
	 * @param password Contrase&ntilde;a del usuario a autenticar.
	 * @return una instancia de {@link UsuarioDTO}
	 */
	public final static UsuarioDTO crearUsuarioDTO(HttpSession session, String password) {
		String loginUsuario = AutenticacionHelper.getLoginUsuarioBasico(session);
		byte numeroIntentos = AutenticacionHelper.getNumeroIntentos(session);
		UsuarioDTO usrdto = new UsuarioDTO();
		usrdto.setIntentos(numeroIntentos);
		usrdto.setPassword(password);
		usrdto.setUsuario(loginUsuario);
		return usrdto;
	}

	/**
	 * Remueve el atributo de intentos de autenticaci&oacute;n del usuario
	 * de la sesi&oacute;n.
	 * 
	 * @param session Sesi&oacute;n del usuario que requiere autenticaci&oacute;n.
	 */
	public final static void inicializarIntentosAutenticacion(HttpSession session) {
		if(session != null) {
			session.removeAttribute(
					EAtributosAutenticacion.LLAVE_INTENTOS_LOGIN.getValor());
		}
	}

	/**
	 * Contabiliza la cantidad de intentos que un usuario ha hecho
	 * al autenticarse y lo almacena en sesi&oacute;n en el 
	 * del atributo {@link EAtributosAutenticacion#LLAVE_INTENTOS_LOGIN}.
	 */
	public final static void registrarIntentoLogin(HttpSession session) {
		Byte intentos = (Byte) session.getAttribute(
				EAtributosAutenticacion.LLAVE_INTENTOS_LOGIN.getValor());
		// Si ya hay sumo los intentos
		if(intentos != null) {
			intentos = new Byte((byte)(intentos + 1));
			session.setAttribute(
					EAtributosAutenticacion.LLAVE_INTENTOS_LOGIN.getValor(), 
					intentos);
		} else {
			// sino hay empiezo con 1
			session.setAttribute(
					EAtributosAutenticacion.LLAVE_INTENTOS_LOGIN.getValor(), 
					new Byte((byte)1));
		}
	}

	/**
	 * Registra en sesi&oacute;n el resultado obtenido despu&eacute;s de
	 * autenticar un usuario.
	 * 
	 * @param session Sesi&oacute;n del usuario que requiere autenticaci&oacute;n.
	 */
	public final static void registrarResultadoAutenticacion(HttpSession session,
			EAtributosAutenticacion resultado) {
		if(session != null) {
			session.setAttribute(
					EAtributosAutenticacion.LLAVE_RESULTADO.getValor(),
					resultado);
		}
	}

	/**
	 * Retorna <code>true</code> si el resultado de autenticaci&oacute;n obtenido de
	 * {@link com.servinte.axioma.vista.autenticacion.AutenticadorView} es igual a
	 * {@link EAtributosAutenticacion#VALOR_SI_AUTENTICO}. En caso contrario retorna
	 * <code>false</code>. Una vez obtenido el resultado remueve el atributo de sesion.
	 * 
	 * @param session
	 * @return <code>true</code> si el resultado de autenticaci&oacute;n obtenido es
	 * {@link EAtributosAutenticacion#VALOR_SI_AUTENTICO}.
	 */
	public final static boolean isAutenticoExitoso(HttpSession session) {
		boolean autenticoExitoso = false;
		EAtributosAutenticacion resultado = obtenerResultadoAutenticacion(session);
		if(resultado != null) {
			switch(resultado) {
			case VALOR_SI_AUTENTICO:
				autenticoExitoso = true;
				break;
			}
		}
		// Remuevo el atributo de sesion.
		session.removeAttribute(
				EAtributosAutenticacion.LLAVE_RESULTADO.getValor());
		return autenticoExitoso;
	}

	/**
	 * Retorna el resultado de un intento de autenticaci&oacute;n. Si la sesi&oacute;n
	 * es nula o no hay atributo retorna <code>null</code>.
	 * 
	 * @param session Sesi&oacute;n del usuario que requiere autenticaci&oacute;n.
	 * @return el resultado de un intento de autenticaci&oacute;n.
	 */
	private final static EAtributosAutenticacion obtenerResultadoAutenticacion(
			HttpSession session) {
		Object resultado = null;
		if(session != null) {
			resultado = session.getAttribute(
				EAtributosAutenticacion.LLAVE_RESULTADO.getValor());
		}
		if(resultado instanceof EAtributosAutenticacion) {
			return (EAtributosAutenticacion) resultado;
		}
		return null;		
	}
}
