package util.interfaces;

import java.util.Vector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class InterfazInventariosListener extends Thread implements ServletContextListener{
	
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
	private Logger logger=Logger.getLogger(InterfazInventariosListener.class);
	
	/**
	 * Metodo para destruir el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0)
	{
		this.valor=false;
		interrupt();
		logger.info("SE HA FINALIZADO EL HILO INTERFAZ INVENTARIOS");
	}
	
	/**
	 * Metodo para empezar el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0)
	{
		logger.info("SE HA INICIADO EL HILO INVENTARIOS");
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
			logger.info("SE HA FINALIZADO EL HILO INTERFAZ INVENTARIOS");
		}
		Vector<String> instituciones=new Vector<String>();
		instituciones=Utilidades.obetenerCodigosInstituciones();
		while(valor)
		{
			for(int i=0;i<instituciones.size();i++)
			{
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazCompras(Integer.parseInt(instituciones.get(i)))))
				{
					UtilidadBDInventarios interfaz=new UtilidadBDInventarios();
					logger.info("\n\n\n\n*******************************************************************************************");
					logger.info("                         INSERTANDO CONVENIO X PROVEEDOR "+instituciones.get(i));
					interfaz.cargarAxConProvToConveniosProveedorAxioma(Integer.parseInt(instituciones.get(i)));
					logger.info("\n\n***************************************************************************************\n\n\n\n");
				}
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getArticuloInventario(Integer.parseInt(instituciones.get(i)))))
				{
					UtilidadBDInventarios interfazAxArt=new UtilidadBDInventarios();
					logger.info("\n\n\n\n*******************************************************************************************");
					logger.info("                         ACTUALIZANDO CODIGO INTERFAZ EN ARTICULO "+instituciones.get(i));
					interfazAxArt.cargarAxArtToArticulo(Integer.parseInt(instituciones.get(i)));
					logger.info("\n\n***************************************************************************************\n\n\n\n");
				}
				
				if(!UtilidadTexto.isEmpty(ValoresPorDefecto.getLoginUsuario(Integer.parseInt(instituciones.get(i)))))
				{
					UtilidadBDInventarios interfazAxInv=new UtilidadBDInventarios();
					logger.info("\n\n\n\n*******************************************************************************************");
					logger.info("                         INSERTANDO TRANSACCIONES "+instituciones.get(i));
					interfazAxInv.cargarAxInvToTransacciones(Integer.parseInt(instituciones.get(i)),ValoresPorDefecto.getLoginUsuario(Integer.parseInt(instituciones.get(i))));
					logger.info("\n\n***************************************************************************************\n\n\n\n");
				}
			}
			try
			{
				sleep(valorEjecucion);
			}
			catch (InterruptedException e1) 
			{
				logger.info("SE HA FINALIZADO EL HILO INTERFAZ INVENTARIOS");
			}
		}
	}
}