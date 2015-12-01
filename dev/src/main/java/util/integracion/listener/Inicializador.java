package util.integracion.listener;


import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import util.integracion.dtoIntegracion.ConfigComando;
import util.integracion.dtoIntegracion.HttpContextComando;



/**
 * @author: Jesrioro
 * @version: 30/05/2013
 * Clase  que  inicaliza  el listener  con  los  parametros de  
 * configuracion para  el  componente  de  integracion  para  usar  el contexto de  reportes    
 *
 */
public class Inicializador implements ServletContextListener {
	/**
	 * Atributo para  generar  logs  en  esta  clase
	 */
	private Logger logger = Logger.getAnonymousLogger();

	/**
     * Metodo que  inicializa  el  listener  para  el  componente de  integracion
     */
    public void contextInitialized(ServletContextEvent event) {
    	ConfigComando config = new ConfigComando();
    	
    	config.setUbicacion((String)event.getServletContext().getInitParameter("ubicacionIntegracion"));
    	logger.info("incluyendo el parametro de contexto ubicacionIntegracion : "+(String)event.getServletContext().getInitParameter("ubicacionIntegracion"));
    	config.setHostPort((String)event.getServletContext().getInitParameter("hostpuertoIntegracion"));
    	logger.info("incluyendo el parametro de contexto ubicacionIntegracion : "+(String)event.getServletContext().getInitParameter("hostpuertoIntegracion"));
    	//40 minutos de time out
    	logger.info("incluyendo el parametro de contexto timeout dado en segundos: "+(String)event.getServletContext().getInitParameter("timeOutIntegracion"));
    	int timeOut = Integer.parseInt((String)event.getServletContext().getInitParameter("timeOutIntegracion"));
    	//conversion de ms a seg.
    	config.setTimeOut(timeOut * 1000);
    	HttpContextComando contextoComando = new HttpContextComando();
    	contextoComando.init(config);
    	event.getServletContext().setAttribute("contextoComando", contextoComando);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }
	
}

