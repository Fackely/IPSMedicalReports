/*
 * Creado el 03-Ago-2005
 *
 * AXIOMA
 *
 * Autor: Carlos Peralta	
 * cperalta@princetonSA.com
 * 
 * BackUpBaseDatos.java
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 3 /Ago/ 2005
 */
public class BackUpBaseDatos
{ 
	
	/**
	 * Manejo de errores de la utilidad de logs
	 */
	private static Logger logger=Logger.getLogger(BackUpBaseDatos.class);
	
	/**
	 * Información que se desea enviar al archivo de log
	 */
	private static String log="";
	
	/**
	 * Nombre del archivo que se está utilizando
	 */
	private static String archivo="";
	
	/**
	 * Definicion del tipo de log (Inserción, modificación ó eliminación)
	 */
	private static int tipoLog=0;
	
	/**
	 * Ruta de los logs definida en ../WEB-INF/web.xml
	 */
	private static String rutaBackUp="";
	
	/**
	 * Folder en el cual se hizo la inserción del log
	 */
	private static String folderLog="";
	
	
	/**
	 * Metodo que que recibe el tipo de base de datos y realiza el back up de la
	 * base de datos en el servidor
	 * @param constanteArchivo
	 * @param texto
	 * @param Usuario
	 * @return
	 */
	public static boolean realizarBackUp(int constanteArchivo, String Usuario)
	{
		String separador=System.getProperty("file.separator");
		if(!rutaBackUp.equals("")&&rutaBackUp!=null)
		{
			String textoTipo="";
			switch(constanteArchivo)
			{
			
				case ConstantesBD.logBackUpBaseDatosPostgresCodigo:
					archivo = ConstantesBD.logBackUpBaseDatosPostgresNombre;
					folderLog=ConstantesBD.logFolderBackUpBaseDatos+separador+ConstantesBD.logBackUpBaseDatosPostgresNombre;
					break;
					
				case ConstantesBD.logBackUpBaseDatosOracleCodigo:
					archivo = ConstantesBD.logBackUpBaseDatosOracleNombre;
					folderLog=ConstantesBD.logFolderBackUpBaseDatos+separador+ConstantesBD.logBackUpBaseDatosOracleNombre;
					break;
					
				default:
					logger.error("Esta tratando de realizar el Back Up de la Base de datos, por favor utilice la clase ConstantesBD para definir el nombre del log");
					return false;
			}
			
			
			try
			{
				if(!folderLog.equals(""))	
				{
					File directorio = new File(rutaBackUp, folderLog);
										
					if(!directorio.isDirectory() && !directorio.exists())	
					{
						if(!directorio.mkdirs()) 
						{
							logger.error("Error creando el directorio "+folderLog);
						}
					}
					archivo=folderLog+separador+archivo;
					folderLog="";
				}
				String nombreBackUp=rutaBackUp+archivo+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual()+Usuario+".sql";
				
				String tipoBD = System.getProperty("TIPOBD");
				if(tipoBD.equals("POSTGRESQL"))
				{
					textoTipo="pg_dump -d -f "+ nombreBackUp + " -U axioma axioma ";
					EjecutarComandoSO(textoTipo);
				}
				
				return true;
			}
			catch(Exception e)
			{
				logger.error("Error en el manejo del BackUp de axioma: "+e);
				return false;
			}
		}
		else
		{
			logger.error("No ha definido una ruta para el manejo del BackUp");
			return false;
		}
	}
	/**
	 * @return Retorna el nombre del archivo en el cual se insertó el último log
	 */
	public static String getArchivo()
	{
		return archivo;
	}

	/**
	 * @return Retorna el texto que fue mandado al log
	 */
	public static String getLog()
	{
		return log;
	}

	/**
	 * @return Retorna la ruta donde seon guardados los BackUps
	 */
	public static String getRutaBackUp()
	{
		return rutaBackUp;
	}

	/**
	 * @return Retorna el tipo de accion realizada (Insertar, Modificar, Eliminar)
	 */
	public static int getTipoLog()
	{
		return tipoLog;
	}
	
	
	
	/**
	 * Ejecutar el comando para sacaer el dump de la base de datos
	 * @param cmdlinea
	 */
	 public static int EjecutarComandoSO (String cmdlinea) 
     {
		 logger.info("\n\n valor del comando a ejecutar >> "+cmdlinea);
             try 
             {
                     String linea;                    
                     Process p = Runtime.getRuntime().exec(cmdlinea);                    
                     BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                     while ((linea = input.readLine()) != null) 
                     {
                             logger.info(linea);
                     }
                     input.close();
                     return 0;
             } 
             catch (Exception err) 
             {            	
                     err.printStackTrace();
                     return -1;
             }
     }

	
	/**
	 * @param ruta
	 */
	public static void setRutaBackUp(String ruta)
	{
	    if(!System.getProperty("file.separator").equals("/"))
		{
	    	rutaBackUp=ruta.replace('/','\\');
			
		}
		else
		{
			rutaBackUp=ruta;
		}
		File directorioLogs=new File(rutaBackUp);
		if(!directorioLogs.isDirectory())
		{
			if(!directorioLogs.mkdirs())
			{
				logger.error("Error creando el directorio del BackUp"+ruta);
			}
		}
	}

	/**
	 * @return Retorna folderLog.
	 */
	public static String getFolderLog()
	{
		return folderLog;
	}
}
