package util;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import org.apache.log4j.Logger;



/**
 * <CODE>LogReporteArchivoPlano</CODE>  
 * ESTA CLASE SIRVE PARA GUARDAR EN UN ARCHIVO PLANO LOS REPORTES
 *  
 * 
 * ARMA UN ARCHIVO .CVS  Y UN ARCHIVO .ZIP 
 * @author axioma
 *
 */
public class LogReporteArchivoPlano 
{
	
	private static Logger logger = Logger.getLogger(LogReporteArchivoPlano.class);
	
	private String ruta;
	private String nombreArchivo;
	private boolean existeReporte;
	
	/**
	 * SI EXISTEN PROBLEMAS EN LA CONSTRUCCION DEL ARCHIVO EL RESUMEN SE MUESTRA EL RESUMEN
	 *  
	 */
	private final String RESUMEN_PROBLEMA= "No se pudo comprimir el archivo. Por favor reportar el error con el administrador del sistema Muchas Gracias";
	
	
	/**
	 * EXTENCIONES DE LOS ARCHIVOS
	 */
	public  static final String PUNTO_ZIP=".zip";
	public static final String PUNTO_CVS=".csv"; 
	
	
	
	/**
	 * CONSTRUTOR 
	 * RECIBE EL NOMBRE DEL ARCHIVO CSV Y EL NOMBRE DEL ARCHIVO COMPRIMIDO .ZIP 
	 * @param nombreArchivoCSV
	 * @param nombreArchivoZip
	 */
	
	public LogReporteArchivoPlano(String nombreArchivo)
	{
		this.ruta=ValoresPorDefecto.getFilePath(); // RUTA CARGADA DESDE EL XML 
		this.setNombreArchivo(nombreArchivo);
		this.setExisteReporte(Boolean.FALSE);
	}
	
	
	
	
	
	/**
	 * GENERA EL ARCHIVO PLANO Y GENERA UN ARCHIVO COMPRIMIDO
	 * @param archivo
	 */
	public void generaArchivoPlano(StringBuilder archivo)
	
	{
		organizarNombreFecharHoraArchivos();
    	
		try
		{
			
			File pathArchivo = new File(this.getRuta() , this.getNombreArchivo()+PUNTO_CVS);
			File directorioV = new File(this.getRuta());
			
			/*
			 * VALIDACION DIRECTORIO
			 */
			if (!directorioV.exists()) {
				if (!directorioV.mkdirs()) 
				{
					logger.error("Error creando el directorio");
				}
			}
			
			
			FileWriter archivoLog = new FileWriter(pathArchivo);
			archivoLog.write(archivo.toString());
			archivoLog.flush();
			archivoLog.close();
			crearArchivoZip();	
		}
		catch (Exception e) 
		{
			logger.info(e);
		}
		
	}




	/**
	 * ORGANIZA EL NOMBRE DEL ARCHIVO
	 */
	private void organizarNombreFecharHoraArchivos() 
	{
		int numeroNombre = generaNumeroArchivo();
		this.setNombreArchivo(this.getNombreArchivo()+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+numeroNombre);
    }



	
	/**
	 * CREAR ARCHIVO COMPRIMIDO
	 */
	private void crearArchivoZip() {
		
		/**
		 * TODO FALTA EL COMPRIDO PARA WINDOWS---
		 */
		BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getFilePath()+this.getNombreArchivo()+PUNTO_ZIP+" "+ValoresPorDefecto.getFilePath()+this.getNombreArchivo()+PUNTO_CVS);
		if(UtilidadFileUpload.existeArchivo(this.getRuta(), this.getNombreArchivo()+PUNTO_ZIP))
		{
			this.setExisteReporte(Boolean.TRUE);
		}
	}
	
	
	
	




	/**
	 * GENERA UN NUMERO ALEATORIO PARA LE ARCHIVO PLANO
	 * @return
	 */
	private int generaNumeroArchivo() 
	{
		Random r=new Random();
    	int numeroNombre = r.nextInt();
		return numeroNombre;
	}
	
	
	
	

	
	
	
	/**
	 * ATRIBUTOS
	 * @return
	 */
	
	
	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}


	public void setExisteReporte(boolean existeReporte) {
		this.existeReporte = existeReporte;
	}



	public boolean isExisteReporte() {
		return existeReporte;
	}





	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}





	public String getNombreArchivo() {
		return nombreArchivo;
	}

	

	
	

}
