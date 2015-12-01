package com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteArticulosAutorizados;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

public class GeneradorReporteFormatoCapitacionAutorArticulos extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();    
    private DtoGeneralReporteArticulosAutorizados dto;
    private String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionMedicamentosInsumos/formatoAutorizacionMedicamentosInsumos.jasper";
	private String RUTA_REPORTE_GENERAL_MEDIA_CARTA = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionMedicamentosInsumos/formatoAutorizacionMedicamentosInsumosMediaCarta.jasper";	
	private String RUTA_SUBREPORTE = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionMedicamentosInsumos/subreporteAutorizacionInsumos.jasper";		
	private String RUTA_SUBREPORTE_DETALLE = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionMedicamentosInsumos/subreporteDetalleMedInsumAutorizados.jasper";
	private String RUTA_SUBREPORTE_PACIENTE = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionMedicamentosInsumos/subreporteSeccionPaciente.jasper";
	private String NOMBRE_SUBREPORTE= "subreporteAutorizacionInsumos";
	private String NOMBRE_SUBREPORTE_DETALLE= "subreporteDetalleMedInsumAutorizados";
	private String NOMBRE_SUBREPORTE_PACIENTE= "subreporteSeccionPaciente";
	
	/**
	 * M&eacute;todo constructor de la clase
	 * @author, Diana Carolina G
	 */
	public GeneradorReporteFormatoCapitacionAutorArticulos (){
		
	}
	
	/**
	 * M&eacute;todo constructor de la clase
	 * DtoGeneralReporteArticulosAutorizados dto
	 * @author, Diana Carolina G
	 */
	public GeneradorReporteFormatoCapitacionAutorArticulos (DtoGeneralReporteArticulosAutorizados dto){
		this.dto=dto;
	}
	
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoGeneralReporteArticulosAutorizados>  collectionDTOGeneral= 
			new ArrayList<DtoGeneralReporteArticulosAutorizados>();	
		ArrayList<DTOReporteAutorizacionSeccionAutorizacion> listaAutorizacion= 
			new ArrayList<DTOReporteAutorizacionSeccionAutorizacion>();
		
		ArrayList<DTOReporteAutorizacionSeccionPaciente> listaPaciente = 
			new ArrayList<DTOReporteAutorizacionSeccionPaciente>();
				
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
		
		if(dto.getListaArticulos()!=null && dto.getListaArticulos().size()>0){
			dto.setDsAutorizacionArticulos(new JRBeanCollectionDataSource(dto.getListaArticulos()));
		}
		if(dto.getDtoAutorizacion()!=null){
			listaAutorizacion.add(dto.getDtoAutorizacion());
			dto.setDsAutorizacion(new JRBeanCollectionDataSource(listaAutorizacion));
		}
		if(dto.getDtoPaciente()!=null){
			listaPaciente.add(dto.getDtoPaciente());
			dto.setDsPaciente(new JRBeanCollectionDataSource(listaPaciente));
		}		
		collectionDTOGeneral.add(dto);
		
		return collectionDTOGeneral;
	}

	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;

        try {  
        	
        	myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PACIENTE);
        	if (myInFile == null) {
        		myInFile = new FileInputStream(RUTA_SUBREPORTE_PACIENTE); 	           	
        	}else if (myInFile != null) {
        		Object mySubreportObj = JRLoader.loadObject(myInFile);
        		parametrosReporte.put(NOMBRE_SUBREPORTE_PACIENTE, mySubreportObj);
        		myInFile.close(); 
        	}
        	
        	myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE);
 	        if (myInFile == null) {
 	           	myInFile = new FileInputStream(RUTA_SUBREPORTE); 	           	
 			}else if (myInFile != null) {
 	            Object mySubreportObj = JRLoader.loadObject(myInFile);
 	            parametrosReporte.put(NOMBRE_SUBREPORTE, mySubreportObj);
 	            myInFile.close(); 
 	            myInFile = null;
 			}
 	        
 	       myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_DETALLE);
	       if (myInFile == null) {
	           myInFile = new FileInputStream(RUTA_SUBREPORTE_DETALLE); 	           	
		   }else if (myInFile != null) {
	           Object mySubreportObj = JRLoader.loadObject(myInFile);
	           parametrosReporte.put(NOMBRE_SUBREPORTE_DETALLE, mySubreportObj);
	           myInFile.close(); 
		   }
	       
 	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}

	@Override
	public String obtenerRutaPlantilla() {
		
		if(dto.getTipoReporteMediaCarta()!= null &&
				dto.getTipoReporteMediaCarta().equals(ConstantesBD.acronimoSi)){
			return RUTA_REPORTE_GENERAL_MEDIA_CARTA;
		}		
		return RUTA_REPORTE_GENERAL;
	}
	
	

}
