package com.servinte.axioma.generadorReporte.facturasVarias.consultaMovimientosFacturasVarias;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.princetonsa.actionform.facturasVarias.ConsultaMovimientoFacturaForm;
import com.princetonsa.dao.facturasVarias.UtilidadesFacturasVariasDao;
import com.princetonsa.dto.facturasVarias.DtoConsolodadoFacturaVaria;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;

import com.servinte.axioma.generadorReporte.facturasVarias.dtoFacturasVarias.DtoConsultaMovimientosFacturaVariaGen;


import util.ConstantesIntegridadDominio;
import util.Utilidades;
import util.reportes.GeneradorReporte;

public class GeneradorConsultamovimientosFacturaVaria extends GeneradorReporte{
	
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
    ArrayList<DtoConsolodadoFacturaVaria> listadoResultado;
    ConsultaMovimientoFacturaForm forma;
    private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/facturasVarias/consultaMovimientosFacturasVarias/consultaMovimientosFacturasVarias.jasper";
    private static String RUTA_SUBREPORTE_RESULTADOCONSULTAFACTURAVARIA = "com/servinte/axioma/generadorReporte/facturasVarias/consultaMovimientosFacturasVarias/subreporteConsultaFacturaVaria.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTAFACTURAVARIA = "subreporteConsultaFacturaVaria";
	private static String RUTA_SUBREPORTE_CONSULTAFACTURA = "com/servinte/axioma/generadorReporte/facturasVarias/consultaMovimientosFacturasVarias/subReporteConsultaFactura.jasper";
	private static String NOMBRE_SUBREPORTE_CONSULTAFACTURA = "subReporteConsultaFactura"; 
    private String RUTA_LOGO = "";
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * 
	 */
	public GeneradorConsultamovimientosFacturaVaria() {
		
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado
	 * @param forma
	 *
	 * 
	 */
	public GeneradorConsultamovimientosFacturaVaria(ArrayList<DtoConsolodadoFacturaVaria> listadoResultado,  ConsultaMovimientoFacturaForm forma){
		
		this.listadoResultado = listadoResultado;
		this.forma=forma;
	
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoConsultaMovimientosFacturaVariaGen>  collectionDTOGeneral= new ArrayList();
		DtoConsultaMovimientosFacturaVariaGen dtoGeneral = new DtoConsultaMovimientosFacturaVariaGen();	
		RUTA_LOGO = "../"+ forma.getRutaLogo();
		
		if (forma != null) {
			
			
				
				String ubicacionLogo = forma.getUbicacionLogo();
				String rutaLogo = "../"+forma.getRutaLogo();
		
				dtoGeneral.setRazonSocial(forma.getRazonSocial());
				dtoGeneral.setFechaInicial(forma.getFechaInicialFormateada());
				dtoGeneral.setFechaFinal(forma.getFechaFinalFormateada());
				if(forma.getUsuario().compareTo("Todos")==0){
					dtoGeneral.setUsuario("Todos");
				}else{
					dtoGeneral.setUsuario(listadoResultado.get(0).getUsuario());
				}
				dtoGeneral.setUsuarioProcesa(forma.getNombreUsuarioProceso());
				
				
				
				boolean existeLogo = existeLogo(rutaLogo);
				
				if (existeLogo) {
					if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
						dtoGeneral.setLogoDerecha(rutaLogo);
						dtoGeneral.setLogoIzquierda(null);
					} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
						dtoGeneral.setLogoIzquierda(rutaLogo);
						dtoGeneral.setLogoDerecha(null);
					}
				}else{
					dtoGeneral.setLogoDerecha(null);
					dtoGeneral.setLogoIzquierda(null);
				}
				for (DtoConsolodadoFacturaVaria dto : listadoResultado) {			  
					dto.setDsResultadoConsultaFac(new JRBeanCollectionDataSource(dto.getListadoConsolidado()));
				}
			dtoGeneral.setDsResultado(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}

	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        
		 try {
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESULTADOCONSULTAFACTURAVARIA);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESULTADOCONSULTAFACTURAVARIA);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_RESULTADOCONSULTAFACTURAVARIA, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }	
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CONSULTAFACTURA);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CONSULTAFACTURA);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_CONSULTAFACTURA, mySubreportObj);
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
		return RUTA_REPORTE_GENERAL;
	}
	
	public boolean existeLogo (String rutaLogo){
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        boolean existe = false;

        try {
	        myInFile = loader.getResourceAsStream(rutaLogo);
	        
	        if (myInFile != null) {
	           existe = true;
	            myInFile.close();
	        }
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
        
        return existe;
	}
}
