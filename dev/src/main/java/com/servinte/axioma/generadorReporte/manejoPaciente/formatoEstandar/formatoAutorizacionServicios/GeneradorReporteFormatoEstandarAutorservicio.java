package com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionServicios;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.manejoPaciente.DTOReporteEstandarAutorizacionServiciosArticulos;
/**
 * 
 * Esta clase se encarga de generar el reporte en formato estándar
 * para las autorizaciones de servicios
 * 
 * @author Angela Maria Aguirre
 * @since 21/12/2010
 */
public class GeneradorReporteFormatoEstandarAutorservicio extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();    
    private DTOReporteEstandarAutorizacionServiciosArticulos dto;
    private String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionServicios/reporteAutorizacionServicios.jasper";
	private String RUTA_REPORTE_GENERAL_MEDIA_CARTA = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionServicios/reporteAutorizacionServiciosMediaCarta.jasper";	
	private String RUTA_SUBREPORTE = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionServicios/subReporteAutorizacionServicios.jasper";		
	private String NOMBRE_SUBREPORTE= "subReporteAutorizacionServicios";
	//private String RUTA_SUBREPORTE_MEDIA_CARTA = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionServicios/subreporteAutorizacionServiciosMediaCarta.jasper";	
	//private String NOMBRE_SUBREPORTE_MEDIA_CARTA= "subreporteAutorizacionServiciosMediaCarta";
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReporteFormatoEstandarAutorservicio() {
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * DTOReporteEstandarAutorizacionServicios dto 		
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReporteFormatoEstandarAutorservicio(DTOReporteEstandarAutorizacionServiciosArticulos dto){	
		
		this.dto = dto;
	}
	
	/**
	 * 
	 * Este Método se encarga de crear las fuentes de datos del reporte
	 * 
	 * @return Collection
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DTOReporteEstandarAutorizacionServiciosArticulos>  collectionDTOGeneral= 
			new ArrayList<DTOReporteEstandarAutorizacionServiciosArticulos>();	
		
		if(dto.getListaServiciosAutorizados()!=null && dto.getListaServiciosAutorizados().size()>0){
			dto.setDsServiciosAutorizados(
					new JRBeanCollectionDataSource(dto.getListaServiciosAutorizados()));
		}		
		
		if(!UtilidadTexto.isEmpty(dto.getRutaLogo())){
			String rutaLogo = "../"+dto.getRutaLogo();
			boolean existeLogo = existeLogo(rutaLogo);
			if(!existeLogo){ 
				dto.setRutaLogo(null);
			}else{
				dto.setRutaLogo(rutaLogo);
			}
		}
		
		collectionDTOGeneral.add(dto);
		
		return collectionDTOGeneral;
	}
	
	/**
	 * 
	 * Este Método se encarga de cargar como parámetros
	 * las plantillas de los subreportes y datos adicionales
	 * 
	 * @return Map
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;

        try {     
        	
        	myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE);
 	        if (myInFile == null) {
 	           	myInFile = new FileInputStream(RUTA_SUBREPORTE); 	           	
 			}else if (myInFile != null) {
 	            Object mySubreportObj = JRLoader.loadObject(myInFile);
 	            parametrosReporte.put(NOMBRE_SUBREPORTE, mySubreportObj);
 	            myInFile.close(); 	           
 			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}
	

	/**
	 * 
	 * Este Método se encarga de devolver la 
	 * ruta del reporte
	 * 
	 * @return String
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public String obtenerRutaPlantilla() {	
		
		if(dto.getFormatoMediaCarta().equals(ConstantesBD.acronimoSi)){
			return RUTA_REPORTE_GENERAL_MEDIA_CARTA;
		}		
		return RUTA_REPORTE_GENERAL;
	}	
}
