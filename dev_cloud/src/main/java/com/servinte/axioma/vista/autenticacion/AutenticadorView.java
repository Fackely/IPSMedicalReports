/**
 * 
 */
package com.servinte.axioma.vista.autenticacion;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.usuario.UsuarioDTO;
import com.servinte.axioma.mundo.excepcion.autenticacion.AutenticacionExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.IntentosSuperadosExcepcion;
import com.servinte.axioma.servicio.autenticacion.IAutenticacionServicio;
import com.servinte.axioma.servicio.fabrica.AutenticacionServicioFabrica;
import com.servinte.axioma.vista.helper.DWRHelper;
import com.servinte.axioma.vista.helper.autenticacion.AutenticacionHelper;

/**
 * Clase que ofrece la funcionalidad de autenticaci&oacute;n para la capa de
 * presentaci&oacute;n.
 * 
 * @author Fernando Ocampo
 * @anexo 1034
 */
public class AutenticadorView {

	/**
	 * Objeto de la capa de servicio el cual provee funcionalidades de
	 * autenticaci&oacute;n.
	 */
	private IAutenticacionServicio autenticacionServicio;

	public AutenticadorView() {
		inicializar();
	}

	/**
	 * Inicializa recursos en este objeto.
	 */
	private void inicializar() {
		autenticacionServicio = AutenticacionServicioFabrica
				.crearAuntenticacionServicio();
	}

	/**
	 * Ofrece la funcionalidad de autenticar un usuario y retorna un 
	 * mensaje de respuesta indicando si fue exitoso la autenticaci&oacute;n 
	 * o supero el n&uacute;mero de intentos.
	 * 
	 * El resultado de autenticar es guardado en sesi&oacute;n para comparar
	 * posteriormente en la petici&oacute;n del formulario que requir&iacute;o
	 * la confirmaci&oacute;n de autenticaci&oacute;n.
	 * 
	 * La llave en los atributos de sesi&oacute;n es la enumeraci&oacute;n
	 * {@link EAtributosAutenticacion#LLAVE_RESULTADO_AUTENTICACION}
	 * y el valor es de tipo {@link EAtributosAutenticacion} con sus posibles 
	 * valores {@link EAtributosAutenticacion#VALOR_SI_AUTENTICO} para
	 * indicar que autentico exitosamente y
	 * {@link EAtributosAutenticacion#VALOR_INTENTOS_SUPERADOS} para indicar que
	 * supero los intentos permitidos por la logica de negocio.
	 * 
	 * @param password
	 *            Contrase&ntilde;a.
	 * @return Retorna un mensaje de respuesta indicando si fue exitoso la
	 * autenticaci&oacute;n o supero el n&uacute;mero de intentos.
	 * @throws AutenticacionExcepcion
	 *             Si el usuario no es quien dice ser.
	 * @throws IllegalArgumentException
	 *             El sistema esta en un estado inconsistente.
	 */
	public String autenticar(String password) {

		String strRespuesta = null;
		UsuarioDTO usrdto = AutenticacionHelper.crearUsuarioDTO(DWRHelper
				.getHTTPSession(false), password);

		try {
			autenticacionServicio.autenticarUsuarioConIntentos(usrdto);
			// guardamos el hash en la sesión.
			AutenticacionHelper.registrarResultadoAutenticacion(
					DWRHelper.getHTTPSession(false), 
					EAtributosAutenticacion.VALOR_SI_AUTENTICO);
			strRespuesta = "Contraseña validada correctamente";
		} catch (AutenticacionExcepcion ex) {
			// registramos intento si no se pudo autenticar
			AutenticacionHelper.registrarIntentoLogin(
					DWRHelper.getHTTPSession(false));
			throw ex;
		} catch (IntentosSuperadosExcepcion ex) {
			// se agoto las oportunidades de autenticacion
			/*
			AutenticacionHelper.registrarResultadoAutenticacion(
					DWRHelper.getHTTPSession(false), 
					EAtributosAutenticacion.VALOR_INTENTOS_SUPERADOS);
			strRespuesta = ex.getMessage();
			*/
			throw ex;
		} catch (Exception e) {
			// Es un error grave
			Log4JManager.error(e);
			throw new IllegalArgumentException(
					"Ocurrio un error inesperado, intente nuevamente", e);
		}

		// Inicializamos contador de intentos a 0
		AutenticacionHelper.inicializarIntentosAutenticacion(DWRHelper
				.getHTTPSession(false));
		return strRespuesta;
	}

}
