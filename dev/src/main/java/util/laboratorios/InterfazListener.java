/*
 * Nov 17, 2006
 */
package util.laboratorios;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.Utilidades;

/**
 * @author Sebastián Gómez 
 *
 *Clase que sirve de Listener para manejar los  procesos de 
 *actualización de registros de procedimientos con la interfaz de laboratorios
 */
public class InterfazListener extends Thread implements ServletContextListener 
{
	/**
	 * Variable que activa el hilo
	 */
	private boolean valor;
	
	/**
	 * Variable para saber el cliente al cual aplica la interfaz de labortarios
	 */
	private String cliente;
	
	/**
	 * Milisegundos de espera para leer la interfaz de laboratorios 
	 */
	private int milisegundos;
	
	/**
	 * Codigio de la institucion activa en el sistema
	 */
	private int codigoInstitucion;
	
	/**
	 * Codigo del usuario o profesional que carga la respuesta de procedimientos
	 */
	private int codigoUsuario;
	
	/**
	 * Ruta física donde se guardan los archivos adjuntos de axioma
	 */
	private String pathAdjuntos;
	
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(InterfazListener.class);
	

	/**
	 * Método invocado cuando se baja el contexto
	 */
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		this.valor = false;
		interrupt();
	}

	/**
	 * Método invocado cuando se sube el contexto
	 */
	public void contextInitialized(ServletContextEvent arg0) 
	{
		logger.info("Se inicia HILO INTERFAZ LABORATORIOS");
		this.valor = true;
		//SE TOMAN LOS PARÁMETROS DEL WEB.XML
		this.cliente = arg0.getServletContext().getInitParameter("CLIENTE");
		this.milisegundos = Utilidades.convertirAEntero(arg0.getServletContext().getInitParameter("TIEMPOINTERFAZLABORATORIOS"));
		this.codigoInstitucion = Utilidades.convertirAEntero(arg0.getServletContext().getInitParameter("CODIGOINSTITUCION"));
		this.codigoUsuario = Utilidades.convertirAEntero(arg0.getServletContext().getInitParameter("USUARIOLABORATORIOS"));
		this.pathAdjuntos = arg0.getServletContext().getInitParameter("FILEPATHLABORATORIOS");
		start();
		
	}

	/**
	 * Método donde se corre el hilo que revisa cada 5 minutos
	 * la tabla interfaz_laboratorio
	 */
	public void run() 
	{
		logger.info("***********PARÁMETROS LEÍDOS DESDE LA INTERFAZ DE LABORATORIOS***********");
		logger.info("cliente: "+this.cliente);
		logger.info("milisegundos: "+this.milisegundos);
		logger.info("path adjuntos: "+this.pathAdjuntos);
		if(this.milisegundos>0)
		{
		
			while(valor)
			{
				try 
				{
					logger.info("Esperando....");
					//Espera de milisegundos
					sleep(this.milisegundos);
					//Llamado a metodo que toma la informacion del laboratorio
					logger.info("Voy a Leer tabla temporal!!");
					if(this.cliente.equals(ConstantesBD.clienteSUBA))
						InterfazLaboratorios.tomarInformacionTablaLaboratorioSUBA();
					else
						InterfazLaboratorios.tomarInformacionTablaLaboratorioSHAIO02(this.pathAdjuntos);
					
				} 
				catch (InterruptedException e) 
				{
					logger.info("Se finaliza HILO INTERFAZ LABORATORIOS");
				}
				
			}
		}
		else
			logger.error("MILISEGUNDOS INVÁLIDOS PARA REALIZAR LA LECTURA DE INTERFAZ LABORATORIOS!!: "+milisegundos);
	}

}