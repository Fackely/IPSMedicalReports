package util.interfaces;

import java.util.Vector;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class InterfazTercerosListener extends Thread implements ServletContextListener{
	
	/**
	 * Variable que activa el hilo
	 */
	private boolean valor;
	
	/**
	 * Variable para manejar el tiempo de ejecucion del hilo (se maneja en milisegundos)
	 */
	private long valorEjecucion=60000;
	
	/**
	 * Maneja los logs de la clase
	 */
	private Logger logger=Logger.getLogger(InterfazTercerosListener.class);
	
	/**
	 * Metodo para destruir el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0)
	{
		this.valor=false;
		interrupt();
		logger.info("SE HA FINALIZADO EL HILO INTERFAZ TERCEROS");
	}
	
	/**
	 * Metodo para empezar el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0)
	{
		logger.info("SE HA INICIADO EL HILO TERCEROS");
		this.valor=true;
		start();
	}
	
	/**
	 * 
	 */
	public void run()
	{
		try
		{
			sleep(valorEjecucion);
		}
		catch(InterruptedException e1)
		{
			logger.info("SE HA FINALIZADO EL HILO INTERFAZ TERCEROS");
		}
		Vector<String> instituciones=new Vector<String>();
		instituciones=Utilidades.obetenerCodigosInstituciones();
		while(valor)
		{
			for(int i=0;i<instituciones.size();i++)
			{
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazTerceros(Integer.parseInt(instituciones.get(i)))))
				{
					UtilidadBDTerceros interfaz=new UtilidadBDTerceros();
					logger.info("\n\n\n\n*******************************************************************************************");
					logger.info("                         INSERTANDO TERCEROS "+instituciones.get(i));
					interfaz.cargarAxTerToTercerosAxioma(Integer.parseInt(instituciones.get(i)));
					logger.info("\n\n***************************************************************************************\n\n\n\n");
				}
			}
			try
			{
				sleep(valorEjecucion);
			}
			catch (InterruptedException e1) 
			{
				logger.info("SE HA FINALIZADO EL HILO INTERFAZ TERCEROS");
			}
		}
	}
}
