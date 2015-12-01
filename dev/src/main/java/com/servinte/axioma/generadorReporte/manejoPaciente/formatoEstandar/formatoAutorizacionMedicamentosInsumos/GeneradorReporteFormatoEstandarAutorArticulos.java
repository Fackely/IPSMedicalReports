package com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionMedicamentosInsumos;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOReporteEstandarAutorizacionServiciosArticulos;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorEstado;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

/**
 * 
 * Esta clase se encarga de generar el reporte en formato estándar
 * para las autorizaciones de articulos
 * 
 * @author Diana Carolina G
 * @since 22/12/2010
 */


public class GeneradorReporteFormatoEstandarAutorArticulos extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();    
    private DTOReporteEstandarAutorizacionServiciosArticulos dto;
    private String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionMedicamentosInsumos/autorizacionArticulos.jasper";
	private String RUTA_REPORTE_GENERAL_MEDIA_CARTA = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionMedicamentosInsumos/autorizacionArticulosMediaCarta.jasper";	
	private String RUTA_SUBREPORTE_MEDICAMENTOS = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionMedicamentosInsumos/subreporteMedicamentosAutorizados.jasper";		
	private String NOMBRE_SUBREPORTE_MEDICAMENTOS= "subreporteMedicamentosAutorizados";
	private String RUTA_SUBREPORTE_INSUMOS = "com/servinte/axioma/generadorReporte/manejoPaciente/formatoEstandar/formatoAutorizacionMedicamentosInsumos/subreporteInsumosAutorizados.jasper";		
	private String NOMBRE_SUBREPORTE_INSUMOS= "subreporteInsumosAutorizados";

	/**
	 * 
	 * Método constructor de la clase
	 * @author, Diana Carolina G
	 */
	public GeneradorReporteFormatoEstandarAutorArticulos () {
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * DTOReporteEstandarAutorizacionServicios dto 		
	 * @author, Diana Carolina G
	 */
	public GeneradorReporteFormatoEstandarAutorArticulos(DTOReporteEstandarAutorizacionServiciosArticulos dto){	
														 
		this.dto = dto;
	}
	
	/**
	 * 
	 * Este Método se encarga de crear las fuentes de datos del reporte
	 * 
	 * @return Collection
	 * @author, Diana Carolina G
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DTOReporteEstandarAutorizacionServiciosArticulos>  collectionDTOGeneral= 
			new ArrayList<DTOReporteEstandarAutorizacionServiciosArticulos>();	
		
		//DTOReporteEstandarAutorizacionServiciosArticulos dtoGeneral = new DTOReporteEstandarAutorizacionServiciosArticulos();
		ArrayList<DtoArticulosAutorizaciones> listaMedicamentos = new ArrayList<DtoArticulosAutorizaciones>();
		ArrayList<DtoArticulosAutorizaciones> listaInsumos = new ArrayList<DtoArticulosAutorizaciones>();
		
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
		
		if(dto.getListaArticulosAutorizados()!=null && dto.getListaArticulosAutorizados().size()>0){
			
			for(DtoArticulosAutorizaciones articulo: dto.getListaArticulosAutorizados() ){
				
				if(articulo.getEsMedicamento()==ConstantesBD.acronimoSiChar){
					listaMedicamentos.add(articulo);
				}
				else{
					listaInsumos.add(articulo);
				}
			}
			
			dto.setDsMedicamentosAutorizados(
					new JRBeanCollectionDataSource(listaMedicamentos));
			dto.setDsInsumosAutorizados(
					new JRBeanCollectionDataSource(listaInsumos));
		}		
		
		/*if(!UtilidadTexto.isEmpty(dto.getRutaLogo())){
			boolean existeLogo = existeLogo(dto.getRutaLogo());
			if(!existeLogo){ 
				dto.setRutaLogo(null);
			}else{
				String rutaLogo = "../"+dto.getRutaLogo();
				dto.setRutaLogo(rutaLogo);
			}
		} */
		
		collectionDTOGeneral.add(dto);
		
		return collectionDTOGeneral;
		
	}
	
	/**
	 * 
	 * Este Método se encarga de cargar como parámetros
	 * las plantillas de los subreportes y datos adicionales
	 * 
	 * @return Map
	 * @author, Diana Carolina G
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;

        try {     
        	
        	myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_MEDICAMENTOS);
 	        if (myInFile == null) {
 	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_MEDICAMENTOS); 	           	
 			}else if (myInFile != null) {
 	            Object mySubreportObj = JRLoader.loadObject(myInFile);
 	            parametrosReporte.put(NOMBRE_SUBREPORTE_MEDICAMENTOS, mySubreportObj);
 	            myInFile.close();
 	            myInFile=null;
 			}
 	        
 	       myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_INSUMOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_INSUMOS); 	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_INSUMOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
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
	 * @author, Diana Carolina G
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
