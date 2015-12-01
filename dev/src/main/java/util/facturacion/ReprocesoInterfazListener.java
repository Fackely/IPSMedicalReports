package util.facturacion;



import java.util.ArrayList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import com.princetonsa.dto.interfaz.DtoInterfazAxArt;
import com.princetonsa.dto.interfaz.DtoInterfazConsumosXFacturar;
import com.princetonsa.dto.interfaz.DtoInterfazDetalleFactura;
import com.princetonsa.dto.interfaz.DtoInterfazNutricion;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.mundo.facturacion.ConsumosXFacturar;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInterfaz;
import util.interfaces.UtilidadBDInventarios;
import util.interfaces.UtilidadInsertarAxArt;

/**
 * @author Jhony Alexander Duque A.
 *
 */
public class ReprocesoInterfazListener extends Thread implements ServletContextListener{
	

	
	/**
	 * 
	 */
	private boolean ejecutarProceso=false;
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(ReprocesoInterfazListener.class);
	
	/**
	 * Metodo para destruir el contexto
	 */
	
	
	public void contextDestroyed(ServletContextEvent arg0)
	{
		interrupt();
		logger.info("SE HA FINALIZADO EL HILO DE REPROCESO DE INTERFAZ ");
	}
	
	/**
	 * Metodo para empezar el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0)
	{
		logger.info("SE HA INICIADO EL HILO DE REPROCESO DE INTERFAZ...");
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
			String segEspReproc=ValoresPorDefecto.getTiemSegVeriInterShaioProc(institucion);
			horaDefault=Utilidades.convertirAEntero(segEspReproc);
			if (UtilidadCadena.noEsVacio(segEspReproc) && horaDefault>0)
			{
				
				try
				{
					logger.info("\n :::::::::::::::::::::::::::::::::LA INTERFAZ SE VERIFICA CADA "+horaDefault+" SEGUNDO/S::::::::::::::::::::::::::::::::::::::::");
					//logger.info("\n\n ################################# interfaz nutricion activa S/N -->"+ValoresPorDefecto.getInterfazNutricion(institucion));
					//logger.info("\n\n ################################# interfaz getArticuloInventario activa S/N -->"+ValoresPorDefecto.getArticuloInventario(institucion));
					//logger.info("\n\n ################################# interfaz getInterfazContableFacturas activa S/N -->"+ValoresPorDefecto.getInterfazContableFacturas(institucion));
					logger.info("\n\n INICIANDO THREAD DEL REPROCESO INTERFAZ.");
						//******************************************************************************
						//Se instancias las utilidades
						//-----------------------------------------------------------------------------
							UtilidadBDInterfaz interfazUtil = new UtilidadBDInterfaz();
							UtilidadBDInventarios utilidadBDInventarios = new UtilidadBDInventarios();
							UtilidadInsertarAxArt utilidadInsertarAxArt = new UtilidadInsertarAxArt();
						//*****************************************************************************
						//Reproceso de la tabla interfaz.ax_porfac (consumos por facturar)
						//-----------------------------------------------------------------------------
							if (UtilidadTexto.getBoolean(ValoresPorDefecto.getConsolidarCargos(institucion)))
							{
								ArrayList<DtoInterfazConsumosXFacturar> arrayDtoConsumos = ConsumosXFacturar.obtenerConsumosXFacturarReproceso(institucion);
								interfazUtil.insertarInterfazConsumosXFacturar(arrayDtoConsumos, institucion,true);
							}
						//-------------------------------------------------------------------------------
						//*******************************************************************************
						//Reproceso de la tabla ax_nutri (nutricion)
						//---------------------------------------------------------------------------------
							if (UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazNutricion(institucion)))
							{
								ArrayList<DtoInterfazNutricion> arrayDtoNutri = UtilidadBDInterfaz.consultaDatosResprocesoNutricion(institucion);
								interfazUtil.insertarInterfazNutricionReproceso(arrayDtoNutri, institucion+"", true);
							}
						//---------------------------------------------------------------------------------
						//*********************************************************************************
						//Reproceso de la tabla ax_inv (inventario)
						//---------------------------------------------------------------------------------
							if (UtilidadTexto.getBoolean(ValoresPorDefecto.getArticuloInventario(institucion)))
							{	
								ArrayList<DtoInterfazTransaccionAxInv> arrayDtoInv = utilidadBDInventarios.consultaDatosResprocesoInventarios(institucion);
								utilidadBDInventarios.insertarInterfazTransaccionInvReproceso(arrayDtoInv, institucion, true);
							}
						//----------------------------------------------------------------------------------
						//***********************************************************************************
						//Reproceso de la tabla ax_art (articulos)
						//-----------------------------------------------------------------------------------
							if (UtilidadTexto.getBoolean(ValoresPorDefecto.getArticuloInventario(institucion)))
							{
								ArrayList<DtoInterfazAxArt> arrayDtoArt = utilidadInsertarAxArt.consultaDatosResprocesoArticulos(institucion);
								utilidadInsertarAxArt.insertarArticulosReproceso(arrayDtoArt, institucion,true);
							}
						//-----------------------------------------------------------------------------------
						//***********************************************************************************
						//Reproceso de la tabla ax_dpaq (detalle facturas paquetes)
						//------------------------------------------------------------------------------------
							if (UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableFacturas(institucion)))
							{
								ArrayList<DtoInterfazDetalleFactura> arrayDtoDpaq = interfazUtil.consultaDatosResprocesoDfacDpaq(true, institucion);
								interfazUtil.insertarDetalleFacturaReproceso(arrayDtoDpaq, true,institucion);
							}
						//------------------------------------------------------------------------------------
						//************************************************************************************
						//Reproceso de la tabla ax_dfac (detalle facturas)
						//------------------------------------------------------------------------------------
							if (UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableFacturas(institucion)))
							{
								ArrayList<DtoInterfazDetalleFactura> arrayDtoFac = interfazUtil.consultaDatosResprocesoDfacDpaq(false, institucion);
								interfazUtil.insertarDetalleFacturaReproceso(arrayDtoFac, false,institucion);
							}
						//------------------------------------------------------------------------------------
					logger.info("\n\n::::::::::::::::::::::::::::::FINALIZANDO THREAD DEL REPROCESO INTERFAZ.");
					
					
					logger.info("\n::::::::::::::::::::::::::::::::::ESPERANDO "+horaDefault+" SEGUNDO/S PARA LA PROXIMA VERIFICACION EN REPROCESO INTERFAZ...");
					sleep(horaDefault*1000);//esperamos los segundos indicados en el parametro general para la proxima ejecucion.
						
					
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
}
