package util;

import java.io.File;
import java.io.FileWriter;

public class TxtFile
{

	/**
	 * 
	 * @param nombreReporte
	 * @param usuarioActual
	 * @return
	 */
	public static String armarNombreArchivoConPeriodo(String nombreReporte, String tipoReporte, String periodo)
	{
		String nombreArchivo = ""; 
		
		if (!nombreReporte.equals("") && !tipoReporte.equals(""))
			nombreArchivo = nombreReporte+"-"+tipoReporte+"-"+periodo;
		else if (!nombreReporte.equals("") && tipoReporte.equals(""))
			nombreArchivo = nombreReporte+"-"+periodo;
		else if (nombreReporte.equals("") && !tipoReporte.equals(""))
			nombreArchivo = tipoReporte+"-"+periodo;
		
		return nombreArchivo;
	}
	
	
	/**
	 * 
	 * @param datos
	 * @param institucion
	 * @param nombre
	 * @return
	 */
	public static boolean generarTxt(StringBuffer datos, String nombre, String path, String extension)
	{
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
			FileWriter fw = new FileWriter(path+nombre+extension);
			fw.write(datos+"");
			fw.close();
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param datos
	 * @param institucion
	 * @param nombre
	 * @return
	 */
	public static boolean generarTxt(StringBuffer datos, String nombre, String path)
	{
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
			fw.write(datos+"");
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