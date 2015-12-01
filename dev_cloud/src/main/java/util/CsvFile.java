package util;

import java.io.File;
import java.io.FileWriter;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * Comma separated value
 * @author axioma
 */
public class CsvFile 
{
	
	/**
	 * Método encargado de armar el nombre del archivo plano
	 * @Amended_by Felipe Pérez Granda
	 * @return
	 */
	public static String armarNombreArchivo(String nombreReporte, UsuarioBasico usuarioActual)
	{
		String nombreArchivo = "";
		nombreArchivo = nombreReporte+"-"+usuarioActual.getLoginUsuario()+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraSegundosActual().replace(":", "-")+"-";
		return nombreArchivo;
	}
	
	/**
	 * @param datos
	 * @param institucion
	 * @param nombre
	 * @return
	 */
	public static boolean generarCvs(String datos, int institucion, String nombre)
	{
		String path = ValoresPorDefecto.getArchivosPlanosReportes(institucion);
		try 
		{
			File f= new File(path);
			if (!f.isDirectory() && !f.exists()) 
			{
				if (!f.mkdirs()) 
				{
					return false;
				}
			}
				
			FileWriter fw = new FileWriter(path+nombre);
			
			fw.write(datos);
			fw.close();
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
}
