package com.servinte.axioma.generadorReporte.odontologia.presupuestosOdontoContratados;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import util.ConstantesIntegridadDominio;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorEstado;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;

public class GeneradorReportePresupuestosOdontoContratados extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultado;
	DtoReportePresupuestosOdontologicosContratados filtroIngresos;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContratados/reportePresupuestosContratados.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTAPRESUPUESTOS = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContratados/subreporteListadoResultado.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTAPRESUPUESTOS = "subreporteListadoResultado";
	private static String RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOS = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContratados/subreportePresupuestosPorEstados.jasper";
	private static String NOMBRE_SUBREPORTE_PRESUPUESTOSPORESTADOS = "subreportePresupuestosPorEstados"; 
	/*private static String RUTA_ESTILOS="com/servinte/axioma/generadorReporte/estilos/estiloReportes.jrtx";
	private static String NOMBRE_RUTA_ESTILOS="estiloReportes"; */
	
	/*************  Planos *****************
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContratados/reportePresupuestosContratadosPlano.jasper";
	private static String RUTA_SUBREPORTE_PRESUPUESTOSPORESTADOSPLANO = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContratados/subreportePresupuestosPorEstadosPlano.jasper";
	private static String NOMBRE_SUBREPORTE_PRESUPUESTOSPORESTADOSPLANO = "subreportePresupuestosPorEstadosPlano";
	***********  Planos ******************/
	
	private String RUTA_LOGO = "";
	private  String NOMBRE_LOGO = "nombreLogo";
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Diana Carolina G
	 */
	public GeneradorReportePresupuestosOdontoContratados() {
	
	}
	
	
	public GeneradorReportePresupuestosOdontoContratados(ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultado, 
			DtoReportePresupuestosOdontologicosContratados filtroIngresos){	
		
		this.listadoResultado = listadoResultado;
		this.filtroIngresos = filtroIngresos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoConsolidadoPresupuestoContratadoPorEstado>  collectionDTOGeneral= new ArrayList();
		
		DtoConsolidadoPresupuestoContratadoPorEstado dtoGeneral = new DtoConsolidadoPresupuestoContratadoPorEstado();	
		RUTA_LOGO = "../"+ filtroIngresos.getRutaLogo();
		
		if (listadoResultado != null) {
			
			if (filtroIngresos != null) {
				
				String ubicacionLogo = filtroIngresos.getUbicacionLogo();
				String rutaLogo = "../"+filtroIngresos.getRutaLogo();
		
				dtoGeneral.setRazonSocial(filtroIngresos.getRazonSocial());
				dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateada());
				dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateada());
				dtoGeneral.setAyudanteNombreServicio(filtroIngresos.getAyudanteServicio());
				dtoGeneral.setNombreUsuarioProceso(filtroIngresos.getNombreUsuarioProceso());
				
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
			
			dtoGeneral.setDsResultadoConsulta(new JRBeanCollectionDataSource(listadoResultado));  //Se crea el Datasource del Consolidado del Reporte
			
			for (DtoConsolidadoPresupuestoContratadoPorEstado dto : listadoResultado) {			  //Se crea el Datasource del Consolidado por Estado
				dto.setDsListadoConsolidadoPorEstado(new JRBeanCollectionDataSource(dto.getListadoConsolidadoPorEstado()));
			}
			
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
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
	
}
