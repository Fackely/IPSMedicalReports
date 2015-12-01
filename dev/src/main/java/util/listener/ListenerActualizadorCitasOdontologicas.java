package util.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.ActualizacionCitasOdontologicas;

/**
 * 
 * @author axioma
 *
 */
public class ListenerActualizadorCitasOdontologicas extends Thread implements ServletContextListener{
	
	/**
	 * 
	 */
	private boolean ejecutarProceso=false;
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(HiloEstanciaAutomatica.class);
	
	/**
	 * Metodo para destruir el contexto
	 */
	
	
	public void contextDestroyed(ServletContextEvent arg0)
	{
		interrupt();
		logger.info("SE HA FINALIZADO EL HILO DE ACT ESTADOS");
	}
	
	/**
	 * Metodo para empezar el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0)
	{
		logger.info("*********************************************************************************************");
		logger.info("-----------------SE HA INICIADO EL HILO DE PROCESO ESTANCIA AUTOMÁTICA-----------------------");
		logger.info("----------------->ACTUALIZACION DE CITAS ODONTOLOGICAS<--------------------------------------");
		logger.info("*********************************************************************************************");
		
		this.ejecutarProceso=true;
		start();
	}

	/**
	 * 
	 */
	public void run()
	{	
		try 
		{
			
			sleep(120000);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		
		try 
		{
			sleep(14000);
		} 
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		
		
		while(ejecutarProceso)
		{
			
			
			String fechaFinal= UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), 1, false);
			String horaFinal="";
			int institucion=Utilidades.convertirAEntero(System.getProperty("CODIGOINSTITUCION"));
			
			
			if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(institucion)) && UtilidadFecha.validacionHora(ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(institucion)).puedoSeguir)
			{
				horaFinal= ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(institucion);
			}
			
			logger.info("HORA FINAL---->"+horaFinal);
			
			if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(institucion)) && UtilidadFecha.validacionHora(horaFinal).puedoSeguir)
			{
				if(UtilidadFecha.esHoraMenorQueOtraReferencia(UtilidadFecha.getHoraActual(), horaFinal))
				{
					fechaFinal= UtilidadFecha.getFechaActual();
				}
				
				long milisegundosEspera= UtilidadFecha.numeroMilisegundosEntreFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraSegundosActual(), fechaFinal, horaFinal);
				
				try 
				{
					logger.info("EJECUTANDO HILO TIMER ESTADOS ESPERA EN MILISEGUNDOS--->"+milisegundosEspera);
					logger.info("*********************************************************************************************");
					logger.info("--------------RUN--SE HA INICIADO EL HILO DE PROCESO ESTANCIA AUTOMÁTICA-----------------------");
					logger.info("----------------->ACTUALIZACION DE CITAS ODONTOLOGICAS<--------------------------------------");
					logger.info("*********************************************************************************************");
					while (milisegundosEspera>600000)
					{
						sleep(600000);
						milisegundosEspera-= 600000;
						
						//pasada la hora volvemos a calcular el tiempo porque posiblemente cambien el parametro
						if(!horaFinal.equals(ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(institucion)))
						{
							horaFinal= ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(institucion);
							if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(institucion)) && UtilidadFecha.validacionHora(horaFinal).puedoSeguir)
							{
								if(UtilidadFecha.esHoraMenorQueOtraReferencia(UtilidadFecha.getHoraActual(), horaFinal))
								{
									fechaFinal= UtilidadFecha.getFechaActual();
								}
								milisegundosEspera= UtilidadFecha.numeroMilisegundosEntreFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraSegundosActual(), fechaFinal, horaFinal);
							}
						}	
						logger.info("MILISEGUNDOS DE ESPERA-->"+milisegundosEspera);
					}
					if(milisegundosEspera>0)
					{	
						sleep(milisegundosEspera);
					}
					sleep(60000);//60 segundos NO BORRAR
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				
				logger.info("MILI SEGUNDO"+milisegundosEspera);
				//ActualizacionCitasOdontologicas.actualizarCitas(institucion);
			}
			else
			{
				//esperamos un tiempo prudente para verificar si cambian el parametro de la hora
				try {
					sleep(600000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		
		}
	}
}

