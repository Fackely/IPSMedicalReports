package util.interfaces;
import java.util.Vector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 * Clase que sirve de Listener para manejar los  procesos de interfaz tesoreria
 */
public class InterfazTesoreriaListener extends Thread implements ServletContextListener 
{
	/**
	 * Variable que activa el hilo
	 */
	private boolean valor;
	
	/**
	 * Varibale que maneja el tiempo de ejecucion para el hilo.
	 */
	private long intervalorEjecucion=60000;
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(InterfazTesoreriaListener.class);
	

	/**
	 * Método invocado cuando se baja el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		this.valor = false;
		interrupt();
		logger.info("Se finaliza HILO INTERFAZ TESORERIA");
	}

	/**
	 * Método invocado cuando se sube el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0) 
	{
		logger.info("Se inicia HILO INTERFAZ TESORERIA");
		this.valor = true;
		start();
		
	}

	/**
	 * 
	 */
	public void run() 
	{
		try 
		{
			sleep(intervalorEjecucion);
		} catch (InterruptedException e1) 
		{
			logger.info("Se finaliza HILO INTERFAZ TESORERIA");
		}
		Vector<String> instituciones=new Vector<String>();
		instituciones=Utilidades.obetenerCodigosInstituciones();
		while(valor)
		{
			for(int i=0;i<instituciones.size();i++)
			{
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazAbonosTesoreria(Integer.parseInt(instituciones.get(i)))))
				{
					UtilidadBDInterfaz interfaz=new UtilidadBDInterfaz();
					logger.info("\n\n\n\n\n************************************************************************************");
					logger.info("                   PROCESANDO ABONOS INSTITUCION "+instituciones.get(i));
					interfaz.cargarAbonosInterfazToAxioma(Integer.parseInt(instituciones.get(i)));
					logger.info("\n\n************************************************************************************\n\n\n\n\n");
				}
			}
			try 
			{
				sleep(intervalorEjecucion);
			} catch (InterruptedException e1) 
			{
				logger.info("Se finaliza HILO INTERFAZ TESORERIA");
			}
		}
	}

}