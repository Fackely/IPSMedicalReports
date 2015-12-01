/**
 * Juan David Ramírez 21/06/2006
 * Princeton S.A.
 */
package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author Juan David Ramírez
 *
 */
public class Inconsistencias
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(Inconsistencias.class);
	
	/**
	 * Método para generar los archivos de ionconsistencias inconsistencias 
	 * @param nombreArchivo Archivo al cual se van a guardar las inconsistencias
	 * @param inconsistencias Cadena con datos de las inconsistencias
	 * @return true si se inbgresaron datos en el archivo de inconsistencias
	 */
	public static boolean generarArchivoInconsistencia(String nombreArchivo, StringBuffer inconsistencias)
	{
		String pathLogs=LogsAxioma.getRutaLogs();
		String separador=System.getProperty("file.separator");
		
		nombreArchivo+=".txt";
		String[] directorios=nombreArchivo.split(separador);
		String folder="";
		for(int i=0; i<directorios.length-1; i++)
		{
			folder+=separador+directorios[i];
		}

		
		nombreArchivo=pathLogs+nombreArchivo;

		File directorio = new File(pathLogs, folder);

		if (!directorio.isDirectory() && !directorio.exists()) {
			if (!directorio.mkdirs()) {
				logger.error("Error creando el directorio "
						+ folder);
			}
		}
		
		
		File archivo=new File(nombreArchivo);
		if(archivo.isDirectory())
		{
			logger.error("El path "+nombreArchivo+" es un directorio");
			return false;
		}
		if(!archivo.exists())
		{
			try
			{
				archivo.createNewFile();
			}
			catch (IOException e)
			{
				logger.error("Error creando el archivo "+nombreArchivo+": "+e);
				return false;
			}
		}
		if(archivo.canWrite())
		{
			try
			{
				FileWriter writer=new FileWriter(archivo, true);
				writer.append(inconsistencias.toString());
				writer.close();
				return true;
			}
			catch (IOException e)
			{
				logger.error("Error escribiendo en el archivo "+nombreArchivo+": "+e);
				return false;
			}
//			archivo.setReadOnly();
		}
		else
		{
			logger.error("El archivo "+nombreArchivo+" no se puede escribir");
			return false;
		}
	}
}
