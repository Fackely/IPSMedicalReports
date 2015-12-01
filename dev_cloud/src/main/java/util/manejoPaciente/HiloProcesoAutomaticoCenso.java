/*
 * Sep 25, 2007
 * Proyect axioma
 * Paquete util.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package util.manejoPaciente;



import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.mundo.manejoPaciente.CensoCamas;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class HiloProcesoAutomaticoCenso extends Thread implements ServletContextListener{
	

	
	/**
	 * 
	 */
	private boolean ejecutarProceso=false;
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(HiloProcesoAutomaticoCenso.class);
	
	/**
	 * Metodo para destruir el contexto
	 */
	
	
	public void contextDestroyed(ServletContextEvent arg0)
	{
		interrupt();
		logger.info("SE HA FINALIZADO EL HILO DE PROCESO AUTOMATICO DE CENSO");
	}
	
	/**
	 * Metodo para empezar el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0)
	{
		logger.info("SE HA INICIADO EL HILO DE PROCESO AUTOMATICO DE CENSO");
		this.ejecutarProceso=true;
		start();
	}

	/**
	 * 
	 */
	public void run()
	{
		int horaDefault=0;
			
		try 
		{
			logger.info("Estoy esperando 3 minutos para continuar...");
			sleep(180000);//esperamos 3 minutos para que suba el tomcat
			logger.info("Estoy esperando 3 minutos para continuar...");
		} catch (InterruptedException e) 
		{
			
			e.printStackTrace();
		}
		
		int institucion = Utilidades.convertirAEntero(System.getProperty("CODIGOINSTITUCION"));
		while(ejecutarProceso)
		{
			try
			{
				int hora=Utilidades.convertirAEntero(UtilidadFecha.getHoraActual().split(":")[0]);
			
			logger.info("\n:::::::::::::::::::::::::::: EL VALOR DEL CORTE HISTORICO DE CAMAS :::::::::::::::::::::");
			logger.info("el valor es "+ValoresPorDefecto.getHoraCorteHistoricoCamas(institucion));
			if (UtilidadCadena.noEsVacio(ValoresPorDefecto.getHoraCorteHistoricoCamas(institucion)))
				horaDefault=Integer.parseInt(ValoresPorDefecto.getHoraCorteHistoricoCamas(institucion));
			
			if(hora==horaDefault)
					{
						
						logger.info("INICIANDO THREAD DEL PROCESO AUTOMATICO DEL CENSO.");
						CensoCamas.corrrerProcesoAutomatico();
						logger.info("FINALIZANDO THREAD DEL PROCESO AUTOMATICO DEL CENSO.");
					}
					logger.info("ESPERANDO 60 MINUTOS PARA LA PROXIMA VERIFICACION EN PROCESO AUTOMATICO CENSO.");
					sleep(3600000);//esperamos 60 minutos para la proxima ejecucion.
					
				
			}
			catch(Exception e)
			{
				logger.info("-->"+e);
				
				try
				{
					sleep(180000);//esperamos 3 para que se soluciones el error
				}
				catch (InterruptedException e1) 
				{
					
					e1.printStackTrace();
				}
			}
		}
	}
}
