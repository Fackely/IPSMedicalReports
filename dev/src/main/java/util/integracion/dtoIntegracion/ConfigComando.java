package util.integracion.dtoIntegracion;
/**
 * Clase  ConfigComando
 * <br>
 * Contiene  los  atributos  de  configuracion para  establecer  la  comunicacion  con  el 
 * contexto de  reportes.
 * 
 * @author jesrioro
 *
 */
public class ConfigComando {
	
	/**
	 * Cadena  que  contiene  la  Ubicación del  directorio donde  se  realizará  la  serializacion de  objetos
	 */
	private String ubicacion;
	/**
	 * Cadena  que  contiene  la URL del contexto de integracion
	 */
	private String hostPort;
	/**
	 * Cadena  que  contiene el tiempo máximo de espera por respuesta del contexto de integracion
	 */
	private long timeOut;
	
	/**
	 * Constructor de  la  clase
	 */
	public ConfigComando() {
		this.ubicacion="";
		this.hostPort="";
		this.timeOut=0L;
	}


	/**
	 * Metodo  para  Obtener  la  ubicacion del  directorio donde  se  realizará  la  serializacion de  objetos
	 * @return the ubicacion
	 */
	public String getUbicacion() {
		return ubicacion;
	}


	/**
	 * Metodo  para  Asignar  la  ubicacion del  directorio donde  se  realizará  la  serializacion de  objetos
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}


	/**
	 * Metodo  para  Obtener  la URL del contexto de integracion
	 * @return the hostPort
	 */
	public String getHostPort() {
		return hostPort;
	}


	/**
	 * Metodo  para  Asignar  la URL del contexto de integracion
	 * @param hostPort the hostPort to set
	 */
	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}


	/**
	 * Metodo para Obtener el tiempo máximo de espera por respuesta del contexto de integracion
	 * @return the timeOut
	 */
	public long getTimeOut() {
		return timeOut;
	}


	/**
	 * Metodo para Asignar el tiempo máximo de espera por respuesta del contexto de integracion
	 * @param timeOut the timeOut to set
	 */
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	
	
	
	
}
