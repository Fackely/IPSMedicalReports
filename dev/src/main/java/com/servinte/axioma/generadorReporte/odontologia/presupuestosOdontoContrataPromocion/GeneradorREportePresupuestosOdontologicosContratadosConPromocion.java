package com.servinte.axioma.generadorReporte.odontologia.presupuestosOdontoContrataPromocion;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReportePresupuestosOdontologicosContratadosConPromocionGen;

import util.ConstantesIntegridadDominio;
import util.reportes.GeneradorReporte;

public class GeneradorREportePresupuestosOdontologicosContratadosConPromocion extends GeneradorReporte {

	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listadoResultado;
	DtoReportePresupuestosOdontologicosContratadosConPromocion filtroIngresos;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContrataPromocion/reportePresupuestosOdontologicosContratadosConPromocion.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTAPRESUPUESTOS = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContrataPromocion/subreportePresupuestosOdontoPromo.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTAPRESUPUESTOS = "subreportePresupuestosOdontoPromo";
	private static String RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOS = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContrataPromocion/subReportePromocion.jasper";
	private static String NOMBRE_SUBREPORTE_PRESUPUESTOSPORESTADOS = "subReportePromocion"; 
	private String RUTA_LOGO = "";
	private  String NOMBRE_LOGO = "nombreLogo";
	
	public GeneradorREportePresupuestosOdontologicosContratadosConPromocion() {
		
	}
	
	public GeneradorREportePresupuestosOdontologicosContratadosConPromocion(ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listadoResultado, 
			DtoReportePresupuestosOdontologicosContratadosConPromocion filtroIngresos){	
		
		this.listadoResultado = listadoResultado;
		this.filtroIngresos = filtroIngresos;
	}
	
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoReportePresupuestosOdontologicosContratadosConPromocionGen>  collectionDTOGeneral= new ArrayList();
		
		DtoReportePresupuestosOdontologicosContratadosConPromocionGen dtoGeneral = new DtoReportePresupuestosOdontologicosContratadosConPromocionGen();	
		RUTA_LOGO = "../"+ filtroIngresos.getRutaLogo();
		
		if (listadoResultado != null) {
			
			if (filtroIngresos != null) {
				
				String ubicacionLogo = filtroIngresos.getUbicacionLogo();
				String rutaLogo = "../"+filtroIngresos.getRutaLogo();
		
				dtoGeneral.setRazonSocial(filtroIngresos.getRazonSocial());
				dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateada());
				dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateada());
				dtoGeneral.setUsuarioProcesa(filtroIngresos.getNombreUsuarioProceso());
				dtoGeneral.setTotalPromociones(listadoResultado.get(listadoResultado.size()-1).getTotalPromociones());
				
				
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
				
			}
									
			for (DtoConsolidadoPresupuestoContratadoPorPromocion dto : listadoResultado) {			  
				dto.setDsResultadoPromocion(new JRBeanCollectionDataSource(dto.getListadoConsolidadoPorPromocion()));
			}
			dtoGeneral.setDsResultadoPresupuPromo(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}

	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        
		 try {
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESULTADOCONSULTAPRESUPUESTOS);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESULTADOCONSULTAPRESUPUESTOS);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_RESULTADOCONSULTAPRESUPUESTOS, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOS);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOS);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_PRESUPUESTOSPORESTADOS, mySubreportObj);
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
