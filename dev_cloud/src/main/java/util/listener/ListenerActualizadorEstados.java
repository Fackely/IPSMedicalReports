package util.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


import util.odontologia.ActualizacionEstadosOdontologicos;


/**
 * Actualizar los estado del Plan de tratamiento y presupuesto 
 * ANEXO 885 VERSION 1.4
 * @author axioma
 *
 */


public class ListenerActualizadorEstados extends Thread implements ServletContextListener{
	
	/**
	 * 
	 */
	private boolean ejecutarProceso=false;
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(ListenerActualizadorEstados.class); 
	
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
		logger.info("******************************************************************************************************");
		logger.info("\n\n\nSE HA INICIADO EL HILO DE ACTUALIZACION DE ESTADOS PARA EL PLAN DE TRATAMIENTO Y PRESUPUESTO \n\n\n");
		logger.info("******************************************************************************************************");
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
			logger.info("******************************************************************************************************");
			logger.info("EJECUTANDO HILO TIMER ESTADOS PRESUPUESTO - PLAN TRATAMIENTO ");
			logger.info("******************************************************************************************************");
			sleep(120000);//esperamos 2 minuto para que suba el tomcat
			
			logger.info("LLEGA PASA EL SLEEP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+UtilidadFecha.getHoraActual());
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		while(ejecutarProceso)
		{
			String fechaFinal= UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), 1, false);
			String horaFinal="";
			int institucion=Utilidades.convertirAEntero(System.getProperty("CODIGOINSTITUCION"));
			
			if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion)) && UtilidadFecha.validacionHora(ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion)).puedoSeguir)
			{
				horaFinal= ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion);
			}
			
			logger.info("HORA FINAL---->"+horaFinal);
			
			if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion)) && UtilidadFecha.validacionHora(horaFinal).puedoSeguir)
			{
				if(UtilidadFecha.esHoraMenorQueOtraReferencia(UtilidadFecha.getHoraActual(), horaFinal))
				{
					fechaFinal= UtilidadFecha.getFechaActual();
				}
				
				long milisegundosEspera= UtilidadFecha.numeroMilisegundosEntreFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraSegundosActual(), fechaFinal, horaFinal);
				
				try 
				{
					logger.info("EJECUTANDO HILO TIMER ESTADOS ESPERA EN MILISEGUNDOS--->"+milisegundosEspera);
					while (milisegundosEspera>600000)
					{
						sleep(600000);
						milisegundosEspera-= 600000;
						
						//pasada la hora volvemos a calcular el tiempo porque posiblemente cambien el parametro
						if(!horaFinal.equals(ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion)))
						{
							horaFinal= ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion);
							if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion)) && UtilidadFecha.validacionHora(horaFinal).puedoSeguir)
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
				ActualizacionEstadosOdontologicos.actualizarEstados(institucion);
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
