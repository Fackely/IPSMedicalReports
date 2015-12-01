package util.timer;



import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.Utilidades;

import util.odontologia.ActualizacionEstadosOdontologicos;



public class TimerActualizadorEstadosOdontologicos implements ServletContextListener
{
	
	
	/**
	 * 
	 * 
	 * 
	 */
	static long milisegundosEspera = 0;	
	static int institucion = 0;	
	/**
	 * 
	 * 
	 * 
	 */
	public static Logger logger=Logger.getLogger(TimerActualizadorEstadosOdontologicos.class);
	/***
	 * 
	 * 
	 */
	
	
	public static Timer timer = new Timer();
	
	
	/**
	 * Variable que activa el hilo
	 */
	
	private boolean valor;
	
	/**
	 * Método invocado cuando se baja el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		this.valor = false;
		timer.cancel();
	}

	/**
	 * Método invocado cuando se sube el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0) 
	{
		
		logger.info("EJECUTANDO HILO TIMER ESTADOS****************************************************************************************************************+**");
		logger.info("EJECUTANDO HILO TIMER ESTADOS****************************************************************************************************************+**");
		this.valor = true;
		
		actualizarEstadosOdontologicos();
	}
	
	public  boolean actualizarEstadosOdontologicos()
	{
		 
		final boolean retorna= true;
	
		
		
		TimerTask task2 = new TimerTask() {
			
			public void run() 
				{
	                
					String fechaFinal= UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), 1, false);
					String horaFinal="00:00:00";
					milisegundosEspera = UtilidadFecha.numeroMilisegundosEntreFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraSegundosActual(), fechaFinal, horaFinal);
					institucion=Utilidades.convertirAEntero(System.getProperty("CODIGOINSTITUCION"));
								
					
				}
			};
		
		
		TimerTask task = new TimerTask() {
			public void run() 
			{
                
				logger.info("EJECUTANDO HILO TIMER ESTADOS");
				
				logger.info("EJECUTANDO HILO TIMER ESTADOS****************************************************************************************************************+**");
				logger.info("EJECUTANDO HILO TIMER ESTADOS****************************************************************************************************************+**");
				logger.info("EJECUTANDO HILO TIMER ESTADOS****************************************************************************************************************+**");
				ActualizacionEstadosOdontologicos.actualizarEstados(institucion);
				
							
				
			}
		};
		
		
		
		
		
		
		
		
		timer.schedule(task2, 10000);
	    timer.schedule(task, milisegundosEspera);
		
		
		
		return retorna;
		

		
	}
}
	