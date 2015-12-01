package com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
/**
 * 
 * Esta clase se encarga de generar el reporte en formato Versalles
 * para las autorizaciones de servicios
 * 
 * @author Angela Maria Aguirre
 * @since 21/12/2010
 */
public class GeneradorReporteFormatoCapitacionAutorservicio extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();    
    private DtoGeneralReporteServiciosAutorizados dto;
    private String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionServicios/formatoAutorizacionServicios.jasper";
	private String RUTA_REPORTE_GENERAL_MEDIA_CARTA = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionServicios/formatoAutorizacionServiciosMediaCarta.jasper";	
	private String RUTA_SUBREPORTE = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionServicios/subreporteAutorizacionServicios.jasper";		
	private String RUTA_SUBREPORTE_PACIENTE = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionServicios/subreporteAutorServiciosSeccionPaciente.jasper";
	private String RUTA_SUBREPORTE_DETALLE = "com/servinte/axioma/generadorReporte/manejoPaciente/formatosCapitacion/formatosAutorizacionServicios/subreporteDetalleServiciosAutorizados.jasper";
	private String NOMBRE_SUBREPORTE_PACIENTE= "subreporteAutorServiciosSeccionPaciente";
	private String NOMBRE_SUBREPORTE= "subreporteAutorizacionServicios";
	private String NOMBRE_SUBREPORTE_DETALLE= "subreporteDetalleServiciosAutorizados";
		
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReporteFormatoCapitacionAutorservicio() {
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * DTOReporteEstandarAutorizacionServicios dto 		
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReporteFormatoCapitacionAutorservicio(DtoGeneralReporteServiciosAutorizados dto){	
		
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
		
		Collection<DtoGeneralReporteServiciosAutorizados>  collectionDTOGeneral= 
			new ArrayList<DtoGeneralReporteServiciosAutorizados>();	
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
		
		if(dto.getListaServicios()!=null && dto.getListaServicios().size()>0){
			dto.setDsAutorizacionServicios(new JRBeanCollectionDataSource(dto.getListaServicios()));
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
 	            myInFile = null;
 			}
 	        
 	       myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_DETALLE);
	       if (myInFile == null) {
	           myInFile = new FileInputStream(RUTA_SUBREPORTE_DETALLE); 	           	
		   }else if (myInFile != null) {
	           Object mySubreportObj = JRLoader.loadObject(myInFile);
	           parametrosReporte.put(NOMBRE_SUBREPORTE_DETALLE, mySubreportObj);
	           myInFile.close(); 
	           myInFile = null;
		   }
	       
	       myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PACIENTE);
	       if (myInFile == null) {
	           myInFile = new FileInputStream(RUTA_SUBREPORTE_PACIENTE); 	           	
		   }else if (myInFile != null) {
	           Object mySubreportObj = JRLoader.loadObject(myInFile);
	           parametrosReporte.put(NOMBRE_SUBREPORTE_PACIENTE, mySubreportObj);
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
		
		if(dto.getTipoReporteMediaCarta()!=null && 
				dto.getTipoReporteMediaCarta().equals(ConstantesBD.acronimoSi)){
			return RUTA_REPORTE_GENERAL_MEDIA_CARTA;
		}		
		return RUTA_REPORTE_GENERAL;
	}	
}
