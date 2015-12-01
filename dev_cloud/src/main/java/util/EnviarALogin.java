/**
 * Juan David Ram�rez 24/07/2006
 * Princeton S.A.
 */
package util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * @author Juan David Ram�rez
 *
 */
public class EnviarALogin implements HttpSessionListener
{
	Logger logger=Logger.getLogger(EnviarALogin.class);
	/**
	 * Registra el numero de usuarios del sistema
	 * que tienen sesi�n abierta
	 */
	private int numeroUsuarios=0;

	/**
	 * Incrementa el numero de usuarios
	 * al loguearse en el sistema
	 */
	public void sessionCreated(HttpSessionEvent evento)
	{
		numeroUsuarios++;
		logger.info("Ingreso Usuario "+evento.getSession().getId()+" N�mero Usuarios: "+numeroUsuarios);
	}

	/**
	 * Decrementa el n�mero de usuarios
	 * al caducar la sesi�n
	 */
	public void sessionDestroyed(HttpSessionEvent evento)
	{
		UtilidadSesion.eliminarUsuarioSession(Utilidades.convertirAEntero(evento.getSession().getAttribute("codigoSession")+""));
		if(numeroUsuarios>0)
		{
			numeroUsuarios--;
			logger.info("Usuario Caducado "+evento.getSession().getId()+" N�mero Usuarios: "+numeroUsuarios);
		}
	}

}
