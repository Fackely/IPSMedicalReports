package com.servinte.axioma.generadorReporte.salas.peticiones;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.util.JRLoader;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.salas.DtoPeticion;

public class GeneradorPDFPeticion extends GeneradorReporte {

	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    ArrayList<DtoPeticion> listadoResultado;
	DtoPeticion datosPeticion;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/salas/peticiones/peticionCirugia.jasper";
	private static String NOMBRE_REPORTE_GENERAL_PETICIONES = "peticionCirugia";
	private static String RUTA_SUBREPORTE_SERVICIOS= "com/servinte/axioma/generadorReporte/salas/peticiones/subServicios.jasper";
	private static String NOMBRE_SUBREPORTE_SERVICIOS = "subServicios";
	private static String RUTA_SUBREPORTE_PROFESIONALES= "com/servinte/axioma/generadorReporte/salas/peticiones/subProfesionales.jasper";
	private static String NOMBRE_SUBREPORTE_PROFESIONALES= "subProfesionales";
	private static String RUTA_SUBREPORTE_MATERIALES= "com/servinte/axioma/generadorReporte/salas/peticiones/subMateriales.jasper";
	private static String NOMBRE_SUBREPORTE_MATERIALES= "subMateriales";
	
	public GeneradorPDFPeticion(DtoPeticion peticion) {
		// TODO Auto-generated constructor stub
		listadoResultado=new ArrayList<DtoPeticion>(0);
		listadoResultado.add(peticion);
		this.datosPeticion=peticion;
	}

	@Override
	public Collection obtenerDatosReporte() {
		
		if(!UtilidadTexto.isEmpty(datosPeticion.getLogoIzquierda())){
			String rutaLogo = "../"+datosPeticion.getLogoIzquierda();
			/*if(rutaLogo!=null){
				rutaLogo=rutaLogo.replace("../", "");
			}
			rutaLogo="../../../../../../web/"+rutaLogo;*/
			boolean existeLogo = existeLogo(rutaLogo);
			if(!existeLogo){ 
				datosPeticion.setLogoIzquierda(null);
			}else{
				datosPeticion.setLogoIzquierda(rutaLogo);
			}
		}
		
		return listadoResultado;
	}

	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
	     try {
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_SERVICIOS);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_SERVICIOS);
		           	
				}
		        if (myInFile != null) {
			        Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_SERVICIOS, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PROFESIONALES);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PROFESIONALES);
		           	
				}
		        if (myInFile != null) {
			        Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_PROFESIONALES, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }
	            myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_MATERIALES);
		        
	            if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_MATERIALES);
		           	
				}
	            if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_MATERIALES, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
	            }
	 	}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
		
	}

	@Override
	public String obtenerRutaPlantilla() {
		// TODO Auto-generated method stub
		return RUTA_REPORTE_GENERAL;
	}

}
