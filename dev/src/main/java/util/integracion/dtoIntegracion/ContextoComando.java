package util.integracion.dtoIntegracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.logging.Logger;
/**
 * Clase ContextoComando
 * Clase  que  permite  serializar  Objetos para  interactuar con el  contexto de  Reportes  
 * @author jesrioro
 *
 */
public abstract class ContextoComando {
	/**
	 * Atributo para  generar  logs  en  esta  clase
	 */
	private Logger logger = Logger.getAnonymousLogger();
	/**
	 * Atributo que  alamacena  los  parametros  de  configuracion  para  usar  el  componente de  integracion
	 */
	protected ConfigComando configuracion;
	/**
	 * Atributo  para  la  generacion de  la  clave única
	 */
	private static long idGenerador = 0L;
	
	
	
	/**
	 * Metodo  que  init
	 * @param configuracion
	 */
	public void init(ConfigComando configuracion){
		this.configuracion = configuracion;
	}
	/**
	 * Metodo que  genera  Instancias  de  ComandoPeticion
	 * @return
	 */
	public static synchronized ComandoPeticion nuevaInstancia()
	{
		idGenerador++;
		Calendar calendar = Calendar.getInstance();
		if (idGenerador > 10000000)
		{
			idGenerador = 0;
		}
		String clave  = calendar.getTimeInMillis()+"_"+idGenerador;
		return new ComandoPeticion(clave);
	}
	
	/**
	 * Metodo  para  serializar  un  objeto de   tipo comandoPeticion.
	 * @param comando
	 */
	public void enviar(ComandoPeticion comando)
	{
		ObjectOutputStream ois = null;
		try
		{
			logger.info("Se esta serializando en : "+configuracion.getUbicacion()+comando.getClave());
			ois = new ObjectOutputStream(new FileOutputStream(configuracion.getUbicacion()+comando.getClave()));
			ois.writeObject(comando);
			ois.flush();
		}catch (Exception e) {
			e.printStackTrace();
			logger.warning("Ha ocurrido un error al serializar:" + e.getMessage());
		}
		finally{
			if (ois != null )
			{
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}
	}
	/**
	 * Metodo  para  deserializar  un  objeto de   tipo comandoPeticion,  
	 * se  necesita  la  clave  para  identificar  el  objeto
	 * @param clave
	 * @return
	 */
	public ComandoPeticion recibir(String clave)
	{
		ObjectInputStream ois = null;
		ComandoPeticion comando = null;
		try
		{
			logger.info("Se esta deserializando en : "+configuracion.getUbicacion()+clave);
			ois  = new ObjectInputStream(new FileInputStream(configuracion.getUbicacion()+clave));
			comando = (ComandoPeticion)ois.readObject();
			
		}catch(Exception  e  )
		{
			logger.warning("Ha ocurrido un error al deserializar:" + e.getMessage());
			comando = null;
		}
		finally{
			if (ois != null )
			{
				try {
					ois.close();
				} catch (IOException e) {
					//  Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return comando;
	}
	/**
	 * Metodo  que  elimina  el  objeto  serializado.
	 * se  necesita  la  clave  para  identificar  el  objeto
	 * @param clave
	 */
	public void limpiar(String clave)
	{
		File data = new File(configuracion.getUbicacion()+File.separatorChar+clave);
		logger.info("Ubicando el archivo serializado: "+configuracion.getUbicacion()+clave);
		if (data.exists())
		{
			data.delete();
			logger.info("Se ha eliminado el archivo: "+configuracion.getUbicacion()+clave);
		}
	}
}
