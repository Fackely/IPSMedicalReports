package com.princetonsa.util.birt.reports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.emitter.csv.CSVRenderOption;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFileUpload;
import util.ValoresPorDefecto;

public class ExtractCSV 
{
	/**
	 * Loggers de la clase ExtractCSV
	 */
	static Logger logger = Logger.getLogger(ExtractCSV.class);
	
	
	/**
	 * 
	 * @param pathReporte
	 * @param nombreArchivoCsv
	 */
	public static void extraerDatosCsv(String pathReporte,String nombreArchivoCsv) 
	{
		IReportEngine engine=null;
		EngineConfig config = null;
		
		//Configure the Engine and start the Platform
		config = new EngineConfig( );
		config.setEngineHome( ParamsBirtApplication.getBirtRuntimePath());
		//set log config using ( null, Level ) if you do not want a log file
		config.setLogConfig(ParamsBirtApplication.getBirtRuntimePath()+"logs", Level.FINEST);
				
		//get the factory from the platform
		IReportEngineFactory factory = (IReportEngineFactory) Platform
	    .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		//create the engine
		IRunAndRenderTask task = null;
		try 
		{
		engine = factory.createReportEngine( config );
		engine.changeLogLevel( Level.FINEST );
		
		//Open a report design - use design to modify design, retrieve embedded images etc. 
		IReportRunnable design;
		
			design = engine.openReportDesign(pathReporte);
			//Create task to run the report - use the task to execute and run the report,
			task = engine.createRunAndRenderTask(design); 
			
			String format = "CSV";
			
			//Set rendering options - such as file or stream output, 
			//output format, whether it is embeddable, etc.

			if (format.equals("CSV")) 
			{
				CSVRenderOption csvOptions = new CSVRenderOption();
	       	  
	        	csvOptions.setOutputFormat( format );
	        	csvOptions.setOutputFileName(ValoresPorDefecto.getFilePath()+nombreArchivoCsv);
	        	task.setRenderOption( csvOptions );
	    		task.run();
	 		}   
			
		} 
		catch (EngineException e) 
		{
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if(task != null) {
				task.close();
			}
			if(engine != null) {
				engine. destroy();
			}
		}
	}
	
	/**
	 * Método implementado para extraer los datos de un archivo csv a partir de un rptdesign y comprimirlo
	 * @param pathReporte
	 * @param nombreArchivo
	 * @return
	 */
	public static ResultadoBoolean extraerArchivoCsvComprimido(String pathReporte,String nombreArchivo)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(false,""); //CONSECUTIVO
		Random r=new Random();
    	int numeroNombre = r.nextInt();
    	String nombreArchivoCsv=nombreArchivo + numeroNombre  +".csv";
    	String nombreArchivoZip=nombreArchivo + numeroNombre  +".zip";
    	extraerDatosCsv(pathReporte, nombreArchivoCsv);
    	
    	//se genera el archivo en formato Zip
		BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getFilePath()+nombreArchivoZip+" "+ValoresPorDefecto.getFilePath()+nombreArchivoCsv);
		
		if(!UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getFilePath(), nombreArchivoZip))
			resultado.setDescripcion("No se pudo comprimir el archivo. Por favor reportar el error con el administrador del sistema");
		else
		{
			resultado.setResultado(true);
			//Se concatena la URL del archivo y la ruta real del archivo
			resultado.setDescripcion(System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivoZip+ConstantesBD.separadorSplit+ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+nombreArchivoZip);
		}
    	return resultado;
	}
	
	/**
	 * Método implementado para extraer los datos de un archivo csv a partir de un rptdesign armando el encabezado y luego comprimirlo
	 * @author Felipe Pérez Granda
	 * @param pathReporte
	 * @param nombreArchivo
	 * @param encabezado String que contiene el encabezado del reporte para agregarlo al archivo plano
	 * @return ResultadoBoolean, El resultado boolean contiene en su descripción los siguientes datos en éstas posiciones:
	 * Posición 0 [0] = URL Archivo Plano
	 * Posición 1 [1] = Path Archivo Plano con Extensión .zip
	 * Posición 2 [2] = Path Archivo Plano con Extensión .csv
	 */
	public static ResultadoBoolean extraerArchivoCsvConEncabezadoComprimido(String pathReporte,String nombreArchivo, String encabezado)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(false,"");
		Random r=new Random();
    	int numeroNombre = r.nextInt();
    	if(numeroNombre < 0)
    	{
    		numeroNombre = numeroNombre * (-1);
    	}
    	String nombreArchivoCsv=nombreArchivo + numeroNombre  +".csv";
    	String nombreArchivoZip=nombreArchivo + numeroNombre  +".zip";
    	String contenidoArchivo = "";
    	String dato= "";
    	
    	extraerDatosCsv(pathReporte, nombreArchivoCsv);
    	
    	/* ****************************
   		 * INICIO Generar Archivo Plano
   		 * ****************************/
    	HashMap<String, Object> mapaContenido=new HashMap<String, Object>();
   		
   		try
		{
			int contador=0;
			String cadena="";
			//******SE INICIALIZA ARCHIVO*************************
			File archivo = new File(ValoresPorDefecto.getFilePath(),nombreArchivoCsv);
			FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				//se almacena cada línea en el hashmap
				mapaContenido.put(contador+"",cadena);
				contador++;
				cadena=buffer.readLine();
			}
			mapaContenido.put("numElementos",contador+"");
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			//logger.info("===> El mapa leido es: "+mapaContenido);
			
			/* *******************
			 * INICIO Escribir el Archivo
			 * *******************/
			
			FileWriter streamIncon=new FileWriter(archivo,false); 
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			contenidoArchivo = encabezado;
			
			for(int i=0;i<Integer.parseInt(mapaContenido.get("numElementos").toString());i++)
			{
				contenidoArchivo += "\n";
				dato = mapaContenido.get(i+"")+"";
				contenidoArchivo += dato;
			}
		
			bufferIncon.write(contenidoArchivo);
			bufferIncon.close();
		
			//return contenido;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+ValoresPorDefecto.getFilePath()+" al cargarlo: "+e);
			//return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+ValoresPorDefecto.getFilePath()+" al cargarlo: "+e);
			//return null;
		}
		
		/* ***********************
		 * FIN Escribir el Archivo
		 * ***********************/

		/* *************************
   		 * FIN Generar Archivo Plano
   		 * *************************/
    	
    	
    	
    	//se genera el archivo en formato Zip
		BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getFilePath()+nombreArchivoZip+" "+ValoresPorDefecto.getFilePath()+nombreArchivoCsv);
		
		if(!UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getFilePath(), nombreArchivoZip))
			resultado.setDescripcion("No se pudo comprimir el archivo. Por favor reportar el error con el administrador del sistema");
		else
		{
			resultado.setResultado(true);
			//Se concatena la URL del archivo y la ruta real del archivo
			resultado.setDescripcion(
					System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+nombreArchivoZip+
					ConstantesBD.separadorSplit+ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+nombreArchivoZip+
					ConstantesBD.separadorSplit+ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+nombreArchivoCsv);
		}
    	return resultado;
	}
}
