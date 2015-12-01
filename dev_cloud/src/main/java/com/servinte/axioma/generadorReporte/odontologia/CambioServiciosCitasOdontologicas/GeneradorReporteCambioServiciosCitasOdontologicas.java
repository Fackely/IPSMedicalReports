package com.servinte.axioma.generadorReporte.odontologia.CambioServiciosCitasOdontologicas;


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

import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteCambioServiciosCitasOdontologicasGen;






public class GeneradorReporteCambioServiciosCitasOdontologicas extends GeneradorReporte  {

	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listadoResultado;
	private DtoFiltroReporteCambioServCitaOdonto filtroCambioSer;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/CambioServiciosCitasOdontologicas/reporteCambioServCitaOdontologica2.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTA = "com/servinte/axioma/generadorReporte/odontologia/CambioServiciosCitasOdontologicas/subReporteCambioServCitaOdonto2.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTA = "subReporteCambioServCitaOdonto2";
	private static String RUTA_SUBREPORTE_PROFESIONAL = "com/servinte/axioma/generadorReporte/odontologia/CambioServiciosCitasOdontologicas/subReporteProfesional2.jasper";
	private static String NOMBRE_SUBREPORTE_PROFESIONAL = "subReporteProfesional2";
	private String RUTA_LOGO = "";
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Javier Gonzalez
	 */
	public GeneradorReporteCambioServiciosCitasOdontologicas() {
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado
	 * @param filtroCambioSer
	 *
	 * @author Javier Gonzalez
	 */
	public GeneradorReporteCambioServiciosCitasOdontologicas(ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listadoResultado, 
			DtoFiltroReporteCambioServCitaOdonto filtroCambioSer){	
		
		this.listadoResultado = listadoResultado;
		this.filtroCambioSer = filtroCambioSer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
Collection<DtoReporteCambioServiciosCitasOdontologicasGen>  collectionDTOGeneral= new ArrayList();
		
DtoReporteCambioServiciosCitasOdontologicasGen dtoGeneral = new  DtoReporteCambioServiciosCitasOdontologicasGen();	
		RUTA_LOGO = "../"+ filtroCambioSer.getRutaLogo();
		
		if (listadoResultado != null) {
			
			if (filtroCambioSer != null) {
				
				String ubicacionLogo = filtroCambioSer.getUbicacionLogo();
				String rutaLogo = "../"+filtroCambioSer.getRutaLogo();
		
				dtoGeneral.setRazonSocial(filtroCambioSer.getRazonSocial());
				dtoGeneral.setUsuarioProcesa(filtroCambioSer.getUsuarioProcesa());
				dtoGeneral.setFechaInicial(filtroCambioSer.getFechaInicialFormateado());
				dtoGeneral.setFechaFinal(filtroCambioSer.getFechaFinalFormateado());
				dtoGeneral.setSexo(filtroCambioSer.getAyudanteSexo());
				dtoGeneral.setRangoEdadConsultada(filtroCambioSer.getRangoEdadConsultada());
				dtoGeneral.setNombreEspecialidad(filtroCambioSer.getNombreEspecialidad());
				dtoGeneral.setNombrePrograma(filtroCambioSer.getNombrePrograma());
								
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
			
			for (DtoResultadoConsultaCambioServiciosOdontologicos registro : listadoResultado) {
				registro.setDsResultadoProfesional(new JRBeanCollectionDataSource(registro.getListaCambioServicioProfesional()));
			}
			
			dtoGeneral.setDsResultadoCambioServ(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}

	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        

        try {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESULTADOCONSULTA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESULTADOCONSULTA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_RESULTADOCONSULTA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PROFESIONAL);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PROFESIONAL);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_PROFESIONAL, mySubreportObj);
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
