/**
 * Juan David Ramírez 24/07/2006
 * Princeton S.A.
 */
package util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * @author Juan David Ramírez
 *
 */
public class EnviarALogin implements HttpSessionListener
{
	Logger logger=Logger.getLogger(EnviarALogin.class);
	/**
	 * Registra el numero de usuarios del sistema
	 * que tienen sesión abierta
	 */
	private int numeroUsuarios=0;

	/**
	 * Incrementa el numero de usuarios
	 * al loguearse en el sistema
	 */
	public void sessionCreated(HttpSessionEvent evento)
	{
		numeroUsuarios++;
		logger.info("Ingreso Usuario "+evento.getSession().getId()+" Número Usuarios: "+numeroUsuarios);
	}

	/**
	 * Decrementa el número de usuarios
	 * al caducar la sesión
	 */
	public void sessionDestroyed(HttpSessionEvent evento)
	{
		UtilidadSesion.eliminarUsuarioSession(Utilidades.convertirAEntero(evento.getSession().getAttribute("codigoSession")+""));
		if(numeroUsuarios>0)
		{
			numeroUsuarios--;
			logger.info("Usuario Caducado "+evento.getSession().getId()+" Número Usuarios: "+numeroUsuarios);
		}
	}

}
