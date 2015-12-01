package com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionIngresoEstancia;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAdmision;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteIngresoEstancia;



import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

public class GeneradorReporteFormatoCapitacionAutorIngresoEstancia extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    private DtoGeneralReporteIngresoEstancia dto;
    private String RUTA_REPORTE_GENERAL_MEDIA_CARTA = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionIngresoEstancia/formatoAutorizacionIngresoEstanciaMediaCarta.jasper";
    private String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionIngresoEstancia/formatoAutorizacionIngresoEstancia.jasper";
	private String RUTA_SUBREPORTE_ADMISION = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionIngresoEstancia/subreporteDatosAdmision.jasper";
	private String RUTA_SUBREPORTE_AUTORIZACION = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionIngresoEstancia/subreporteAutorizacionIngresoEstancia.jasper";
	private String NOMBRE_SUBREPORTE_ADMISION = "subreporteDatosAdmision";
	private String NOMBRE_SUBREPORTE_AUTORIZACION = "subreporteAutorizacionIngresoEstancia"; 
	
	
	/**
	 * M&eacute;todo constructor de la clase
	 * @author, Diana Carolina G :)
	 */
	public GeneradorReporteFormatoCapitacionAutorIngresoEstancia(){
		
	}
	
	/**
	 * M&eacute;todo constructor de la clase
	 * DtoGeneralReporteIngresoEstancia dto
	 * @author, Diana Carolina G
	 */
	public GeneradorReporteFormatoCapitacionAutorIngresoEstancia(DtoGeneralReporteIngresoEstancia dto){
		this.dto = dto;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoGeneralReporteIngresoEstancia> collectionDTOGeneral=
			new ArrayList<DtoGeneralReporteIngresoEstancia>();
		
		ArrayList<DTOReporteAutorizacionSeccionAutorizacion> listaAutorizacion = 
			new ArrayList<DTOReporteAutorizacionSeccionAutorizacion>(); 
		
		ArrayList<DTOReporteAutorizacionSeccionAdmision> listaAdmision =
			new ArrayList<DTOReporteAutorizacionSeccionAdmision>(); 
		
		if(!UtilidadTexto.isEmpty(dto.getRutaLogo())&& !UtilidadTexto.isEmpty(dto.getUbicacionLogo())){
			
			String ubicacionLogo = dto.getUbicacionLogo();
			String rutaLogo = "../"+dto.getRutaLogo();			
			boolean existeLogo = existeLogo(rutaLogo);
			
			if (existeLogo) {
				if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
					dto.setLogoDerecha(rutaLogo);
					dto.setLogoIzquierda(null);
				} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
					dto.setLogoIzquierda(rutaLogo);
					dto.setLogoDerecha(null);
				}
			}else{
				dto.setLogoDerecha(null);
				dto.setLogoIzquierda(null);
			}			
		}
		
		if(dto.getDtoAdmisionIngresoEstancia()!=null){
			listaAdmision.add(dto.getDtoAdmisionIngresoEstancia());
			dto.setDsAdmisionIngresoEstancia(new JRBeanCollectionDataSource(listaAdmision));
		}
		
		
		if(dto.getDtoAutorizacion()!=null){
			listaAutorizacion.add(dto.getDtoAutorizacion());
			dto.setDsAutorizacion(new JRBeanCollectionDataSource(listaAutorizacion));
		} 
		
		collectionDTOGeneral.add(dto);
		
		return collectionDTOGeneral;
	}
	
	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
		
		try {
			
			myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ADMISION);
        	if (myInFile == null) {
        		myInFile = new FileInputStream(RUTA_SUBREPORTE_ADMISION); 	           	
        	}else if (myInFile != null) {
        		Object mySubreportObj = JRLoader.loadObject(myInFile);
        		parametrosReporte.put(NOMBRE_SUBREPORTE_ADMISION, mySubreportObj);
        		myInFile.close(); 
        	}
        	
        	myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_AUTORIZACION);
        	if (myInFile == null) {
        		myInFile = new FileInputStream(RUTA_SUBREPORTE_AUTORIZACION); 	           	
        	}else if (myInFile != null) {
        		Object mySubreportObj = JRLoader.loadObject(myInFile);
        		parametrosReporte.put(NOMBRE_SUBREPORTE_AUTORIZACION, mySubreportObj);
        		myInFile.close(); 
        	} 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 return parametrosReporte;
	}

	

	@Override
	public String obtenerRutaPlantilla() {
		
		if(dto.getTipoReporteMediaCarta().equals(ConstantesBD.acronimoSi)){
			return RUTA_REPORTE_GENERAL_MEDIA_CARTA;
		}	
		return RUTA_REPORTE_GENERAL;
		
	}

}
