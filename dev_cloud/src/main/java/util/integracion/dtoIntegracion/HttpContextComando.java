package util.integracion.dtoIntegracion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 * Clase HttpContextComando
 * Permite  enviar   datos  al  contexto de  reportes.   
 * @author jesrioro
 *
 */
public class HttpContextComando extends ContextoComando {
	/**
	 * Atributo para  generar  logs  en  esta  clase
	 */
	private Logger logger = Logger.getAnonymousLogger();
	
	/**
	 * Metodo  que  envia  la  informacion al  contexto de  reportes  junto  con  los datos del objeto serializado  
	 */
	public void enviar(ComandoPeticion comando){
		
		super.enviar(comando);
		BufferedReader in = null;
		try {
			logger.info("Se envia el mensaje a : "+super.configuracion.getHostPort()+"/"+comando.getPathInfo()+"?claveComandoIntegracion="+comando.getClave()+"&"+comando.getQueryString());
	        URL url = new URL(super.configuracion.getHostPort()+"/"+comando.getPathInfo()+"?claveComandoIntegracion="+comando.getClave()+"&"+comando.getQueryString());
	        URLConnection connection = url.openConnection();
	        connection.setReadTimeout((int)super.configuracion.getTimeOut());
	        connection.setDoOutput(true);
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;
				while ((inputLine = in.readLine()) != null)
				{
					comando.setRespuesta(inputLine);
				}
			
	        
		} catch (Exception e) {
			logger.warning("Ha ocurrido un error intentando enviar la peticion http:"+e.getMessage());
			comando.setRespuesta(null);
		}
		finally{
			if (in != null)
			{
				try {
					in.close();
				} catch (IOException e) {
					logger.warning("Error cerrando la conexion al servidor Http: "+e.getMessage());
				}	
			}
			
		}
//		response.sendRedirect(super.configuracion.getHostPort()+"/"+comando.getPathInfo()+"?"+comando.getQueryString());
	}

}
