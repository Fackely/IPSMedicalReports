package com.servinte.axioma.generadorReporte.tesoreria.anticiposRecibidosConvenio;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesIntegridadDominio;
import util.reportes.GeneradorReporte;

import java.io.InputStream;
import com.princetonsa.dto.tesoreria.DtoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoRecibosConceptoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;

public class GeneradorReporteAnticiposRecibidosConvenio extends GeneradorReporte {

	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    ArrayList<DtoAnticiposRecibidosConvenio> listadoResultado;
	DtoReporteAnticiposRecibidosConvenio filtrosAnticipos;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/tesoreria/anticiposRecibidosConvenio/reporteAnticiposRecibidosConvenio.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTAANTICIPOS = "com/servinte/axioma/generadorReporte/tesoreria/anticiposRecibidosConvenio/subreporteListadoResultado.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTAANTICIPOS = "subreporteListadoResultado";
	private static String RUTA_SUBREPORTE_ANTICIPOSPORCONVENIOS = "com/servinte/axioma/generadorReporte/tesoreria/anticiposRecibidosConvenio/subreporteAnticiposPorConvenios.jasper";
	private static String NOMBRE_SUBREPORTE_ANTICIPOSPORCONVENIOS = "subreporteAnticiposPorConvenios";
	private static String RUTA_SUBREPORTE_FORMASDEPAGO = "com/servinte/axioma/generadorReporte/tesoreria/anticiposRecibidosConvenio/subreporteFormasPago.jasper";
	private static String NOMBRE_SUBREPORTE_FORMASDEPAGO = "subreporteFormasPago"; 
	
	private static String RUTA_SUBREPORTE_ANTICIPOSPORCONVENIOS_SIN_CONCEPTO = "com/servinte/axioma/generadorReporte/tesoreria/anticiposRecibidosConvenio/subreporteAnticiposPorConveniosSinConcepto.jasper";
	private static String NOMBRE_SUBREPORTE_ANTICIPOSPORCONVENIOS_SIN_CONCEPTO = "subreporteAnticiposPorConveniosSinConcepto";
	
	
	
	private String RUTA_LOGO = "";
	
	public GeneradorReporteAnticiposRecibidosConvenio(){
		
	}
	
	public GeneradorReporteAnticiposRecibidosConvenio(ArrayList<DtoAnticiposRecibidosConvenio> listadoResultado,
			DtoReporteAnticiposRecibidosConvenio filtrosAnticipos){
		this.listadoResultado=listadoResultado;
		this.filtrosAnticipos=filtrosAnticipos;
	}
	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        
		 try {
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESULTADOCONSULTAANTICIPOS);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESULTADOCONSULTAANTICIPOS);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_RESULTADOCONSULTAANTICIPOS, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }
		        
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ANTICIPOSPORCONVENIOS);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ANTICIPOSPORCONVENIOS);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_ANTICIPOSPORCONVENIOS, mySubreportObj);
		            myInFile.close();
		            myInFile=null; 
		        } 
		        
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_FORMASDEPAGO);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_FORMASDEPAGO);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_FORMASDEPAGO, mySubreportObj);
		            myInFile.close();
		            myInFile=null; 
		        } 
		        
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ANTICIPOSPORCONVENIOS_SIN_CONCEPTO);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ANTICIPOSPORCONVENIOS_SIN_CONCEPTO);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_ANTICIPOSPORCONVENIOS_SIN_CONCEPTO, mySubreportObj);
		            myInFile.close();
		            myInFile=null; 
		        } 
			 
			}catch (Exception e) {
				e.printStackTrace();
			}

		
        return parametrosReporte;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		DtoConsolidadoAnticiposRecibidosConvenio dtoGeneral = new DtoConsolidadoAnticiposRecibidosConvenio();
		Collection<DtoConsolidadoAnticiposRecibidosConvenio>  collectionDTOGeneral= 
			new ArrayList<DtoConsolidadoAnticiposRecibidosConvenio>();
		
		ArrayList<DtoAnticiposRecibidosConvenio> listaCentroAtencionReporte= 
			new ArrayList<DtoAnticiposRecibidosConvenio>();
		
			
		RUTA_LOGO = "../"+ filtrosAnticipos.getRutaLogo();
		
		if (listadoResultado != null && listadoResultado.size()>0) {
			
			boolean mostrarConceptos = true;
			
			if (filtrosAnticipos != null) {
				
				String ubicacionLogo = filtrosAnticipos.getUbicacionLogo();
				String rutaLogo = "../"+filtrosAnticipos.getRutaLogo();
				mostrarConceptos = filtrosAnticipos.isMostrarConceptos();
		
				dtoGeneral.setRazonSocial(filtrosAnticipos.getRazonSocial());
				dtoGeneral.setFechaInicial(filtrosAnticipos.getFechaInicialFormateada());
				dtoGeneral.setFechaFinal(filtrosAnticipos.getFechaFinalFormateada());
				dtoGeneral.setNombreUsuarioProceso(filtrosAnticipos.getNombreUsuarioProceso());
				
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
		
		
		
		for (DtoAnticiposRecibidosConvenio centroAntencion : listadoResultado) {	
			centroAntencion.setMostrarConceptos(mostrarConceptos);
			
			for(DtoRecibosConceptoAnticiposRecibidosConvenio recibos: centroAntencion.getListaRecibosCajaXCentroAtencion()){
				recibos.setDsListaFormaPago(new JRBeanCollectionDataSource(recibos.getListaFormaPago()));
				}				
					centroAntencion.setDsListadoRecibosCajaXCentroAtencion(new JRBeanCollectionDataSource(
							centroAntencion.getListaRecibosCajaXCentroAtencion()));
			
					listaCentroAtencionReporte.add(centroAntencion);				
			}
				dtoGeneral.setDsConsolidadoConsulta(new JRBeanCollectionDataSource(listaCentroAtencionReporte));	
		}
		collectionDTOGeneral.add(dtoGeneral);
		return collectionDTOGeneral;
		
	}

	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}

}
