/*
 * Marzo 18, 2008
 * Proyect axioma
 * Paquete util.facturacion
 * @author Cesar Giovanny Arias Galeano
 */
package util.listener;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.facturacion.EstanciaAutomaticaAction;
import com.princetonsa.actionform.facturacion.EstanciaAutomaticaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.princetonsa.mundo.manejoPaciente.CensoCamas;

/**
 * @author Cesar Giovanny Arias Galeano - lgchavez@princetonsa.com
 *
 */
public class HiloEstanciaAutomatica extends Thread implements ServletContextListener{
		
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
		logger.info("SE HA FINALIZADO EL HILO DE PROCESO ESTANCIA AUTOM�TICA");
	}
	
	/**
	 * Metodo para empezar el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0)
	{
		logger.info("SE HA INICIADO EL HILO DE PROCESO ESTANCIA AUTOM�TICA");
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
			logger.info("\n\n\n \n\n\n [Estancia Automatica] Estoy esperando 3 minutos para continuar...");
			sleep(120000);//esperamos 4 minutos para que suba el tomcat
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		while(ejecutarProceso)
		{
			
			try
			{
				int hora=Utilidades.convertirAEntero(UtilidadFecha.getHoraActual().split(":")[0]);
				Connection con=null;
				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexi�n"+e.toString());
				}
				
				HashMap mapaInstituciones=new HashMap();
				HashMap mapaCentrosAtencion=new HashMap();
				
				mapaInstituciones=Utilidades.obtenerInstituciones(con);
				logger.info("\n \n \n \n [Estancia Automatica] INSTITUCIONES ASOCIADAS "+mapaInstituciones.get("numRegistros"));
				//Utilidades.imprimirMapa(mapaInstituciones);
				
				for (int i=0; i<Utilidades.convertirAEntero(mapaInstituciones.get("numRegistros")+"");i++)
				{
				
				logger.info("\n\n\n [HAY QUE HACER ESTANCIA AUTOMATICA ? ] >>>"+ValoresPorDefecto.getGenerarEstanciaAutomatica(Integer.parseInt(mapaInstituciones.get("codigo_"+i).toString()))+"\n\n");
					
				if (ValoresPorDefecto.getGenerarEstanciaAutomatica(Utilidades.convertirAEntero(mapaInstituciones.get("codigo_"+i).toString())).equals(ConstantesBD.acronimoSi))
					{
							
							if (UtilidadCadena.noEsVacio(ValoresPorDefecto.getHoraGenerarEstanciaAutomatica(Utilidades.convertirAEntero(mapaInstituciones.get("codigo_"+i).toString()))))
								horaDefault=Integer.parseInt(ValoresPorDefecto.getHoraGenerarEstanciaAutomatica(Utilidades.convertirAEntero(mapaInstituciones.get("codigo_"+i).toString())).split(":")[0]);
								
							logger.info("\n***************************************************************" +
										"\n\n [ESTANCIA AUTOMATICA] \n\n\n [HORA SISTEMA] >>>"+hora+" \n[HORA PARAMETRO GENERACION] >>>"+horaDefault+"\n\n\n" +
										"\n***************************************************************");
							
							if(hora==horaDefault)
								{
									
								mapaCentrosAtencion=Utilidades.obtenerCentrosAtencion(Utilidades.convertirAEntero(mapaInstituciones.get("codigo_"+i).toString()));
								logger.info("\n \n \n \n "+mapaInstituciones.get("codigo_"+i).toString()+"\n \n[ESTANCIA AUTOMATICA] CENTROS DE ATENCION ASOCIADO A LA INSTITUCION >>>"+mapaCentrosAtencion);
								logger.info("NUMERO DE REGISTROS DEL MAPA CENTROS DE ATENCION >>>"+Utilidades.convertirAEntero(mapaCentrosAtencion.get("numRegistros").toString()));
								
								for (int j=0; j<Utilidades.convertirAEntero(mapaCentrosAtencion.get("numRegistros").toString());j++)
									{
								
										logger.info("\n\n\n "+j+" \n\n\n****************************************************************" +
																		"\n\n\nINICIANDO THREAD PROCESO ESTANCIA AUTOM�TICA.\n\n" +
																		"***************************************************************");
										
										EstanciaAutomatica mundo= new EstanciaAutomatica();
										mundo.setFechaInicialEstancia(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
										mundo.setFechaInicialEstancia(UtilidadFecha.incrementarDiasAFecha(mundo.getFechaInicialEstancia(), -1, true));
										mundo.setCentroAtencion(Utilidades.convertirAEntero(mapaCentrosAtencion.get("consecutivo_"+j)+""));
										logger.info("\n "+j+" \n FECHA ESTANCIA >>>"+mundo.getFechaInicialEstancia());
										logger.info("\n "+j+" \n CENTRO DE ATENCION >>>"+mundo.getCentroAtencion()+" >>>"+mapaCentrosAtencion.get("descripcion_"+j).toString());
										try
										{
											mundo.accionGenerarArea(con, mundo.getFechaInicialEstancia(),mundo.getCentroAtencion(), Utilidades.convertirAEntero(mapaInstituciones.get("codigo_"+i)+""));
										}
										catch (Exception e) {
											logger.info("\n\n\n\n\n\n <<<<< GENERO UN ERROR INTERNO LA FUNCION >>>>"+e+"\n\n\n\n\n\n\n");
										}
										logger.info("\n\n\n "+j+" \n\n\n*********************************************************************" +
																		"\n\n\n <<<<<< FINALIZANDO THREAD PROCESO ESTANCIA AUTOM�TICA. "+j+" >>>>>" +
																		"\n\n\n" +
																		"********************************************************************");

									}
								try
								{
									UtilidadBD.cerrarConexion(con);
								}
								catch(SQLException e)
								{
									logger.error("Error al cerrar la conexi�n!!!"+e);
								}	
								}
							else
							{
								logger.info("\n\n\n*****************************************************************" +
											"\n\n [LA INSTITUCION NO TIENE PROGRAMADA LA ESTANCIA AUTOMATICA EN LA HORA ACTUAL] \n\n" +
												  "*****************************************************************");
							}
								
						}
				else
				{
					logger.info("\n\n\n " +
						    	"*********************************************************************************\n\n" +
						    	"[LA INSTITUCION NO TIENE PROGRAMADO REALIZAR LA ESTANCIA AUTOMATICA] \n\n" +
						    	"*********************************************************************************\n\n");
				}
				}
				
				try
				{
					UtilidadBD.cerrarConexion(con);
				}
				catch(SQLException e)
				{
					logger.error("Error al cerrar la conexi�n!!!"+e);
					break;
				}				
				logger.info("\n\n\n******************************************************************************************" +
							"\n\n\n[ESPERANDO 60 MINUTOS PARA LA PROXIMA VERIFICACION EN PROCESO ESTANCIA AUTOMATICA.] \n\n\n" +
							"******************************************************************************************");
				sleep(3600000);//esperamos 60 minutos para la proxima ejecucion.
								
			}
			catch(Exception e)
			{
				logger.info("[Error Estancia Automatica]-->"+e);
				break;
			}
		}
	}
}
