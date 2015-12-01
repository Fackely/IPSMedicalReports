package util.facturacion;

import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.princetonsa.dto.interfaz.DtoInterfazConsumosXFacturar;
import com.princetonsa.mundo.facturacion.ConsumosXFacturar;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInterfaz;

/**
 * Proceso automatico para la lectura de los consumos pendientes de facturar con tarifa generada
 * @author wilson
 *
 */
public class ConsumosXFacturarListener extends Thread implements ServletContextListener  
{
	/**
	 * Variable que activa el hilo
	 */
	private boolean valor;
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ConsumosXFacturarListener.class);
	
	
	/**
	 * metodo llamado cuando se baja el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		this.valor = false;
		interrupt();
	}

	/**
	 * metodo llamado cuando se sube el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0) 
	{
		logger.info("\n\n****************************************************************************************************************");
		logger.info("SE INICIA LISTENER CONSUMOS X FACTURAR");
		logger.info("****************************************************************************************************************\n\n");
		this.valor = true;
		start();
	}

	/**
	 * Método donde se corre el hilo que revisa a las 12:00 p.m
	 * los consumos pendientes de facturar y los inserta en la tabla de interfaz Shaio 
	 * AX_INGXFAC
	 */
	public void run() 
	{
		int institucion=ConstantesBD.codigoNuncaValido;
		try 
		{
			sleep(10000);
		} 
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		institucion=Utilidades.convertirAEntero(System.getProperty("CODIGOINSTITUCION"));
		
		logger.info("\n\n****************************************************************************************************************");
		logger.info("CODIGO INSTITUCION-->"+institucion);
		logger.info("****************************************************************************************************************\n\n");
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getConsolidarCargos(institucion)))
		{	
			try 
			{
				while(valor)
				{
					String fechaFinal= UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), 1, false);
					String horaFinal="00:00:00";
					long milisegundosEspera= UtilidadFecha.numeroMilisegundosEntreFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraSegundosActual(), fechaFinal, horaFinal);
					logger.info("\n\n****************************************************************************************************************");
					logger.info("CORRIENDO HILO CONSUMOS X FACTURAR, FALTAN "+milisegundosEspera+" milisegundos PARA INICIAR EL PROCESO.......");
					logger.info("FECHA INICIAL->"+UtilidadFecha.getFechaActual()+" HORA INI->"+UtilidadFecha.getHoraSegundosActual()+" FECHA FINAL->"+fechaFinal+" HORA FINAL->"+horaFinal);
					logger.info("Esperando....");
					logger.info("****************************************************************************************************************\n\n");
					sleep(milisegundosEspera);
					logger.info("INICIA PROCESO");
					UtilidadBDInterfaz interfazUtil= new UtilidadBDInterfaz();
					ArrayList<DtoInterfazConsumosXFacturar> arrayDto= ConsumosXFacturar.obtenerConsumosXFacturar(institucion);
					interfazUtil.insertarInterfazConsumosXFacturar(arrayDto, institucion,false);
				} 
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
				logger.info("Se finaliza HILO INTERFAZ CONSUMOS X FACTURAR");
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				logger.info("Se finaliza HILO INTERFAZ CONSUMOS X FACTURAR");
			}
		}
		else
		{	
			logger.info("\n\n****************************************************************************************************************");
			logger.info("NO ESTA ACTIVO EL VALOR POR DEFECTO DE CONSOLIDAR CARGOS PARA ACTIVAR HILO CONSUMOS X FACTURAR....");
			logger.info("****************************************************************************************************************\n\n");
		}	
	}
	
}
