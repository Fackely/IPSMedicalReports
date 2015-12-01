package com.servinte.axioma.generadorReporte.facturacion.valorHonorariosMedico;

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
import util.Utilidades;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.facturacion.DtoHonorariosMedico;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;

public class GeneradorReporteValorHonorariosMedico extends GeneradorReporte{

	
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private DtoOrdenesAmbulatorias dtoResultadoOrden;
	private DtoHonorariosMedico dtoResultado;

	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/facturacion/valorHonorariosMedicos/valorHonorariosMedico.jasper";
	
	private static String RUTA_SUBREPORTE_HONORARIOS = "com/servinte/axioma/generadorReporte/facturacion/valorHonorariosMedicos/subReporteFacturasHonorarios.jasper";
	private static String NOMBRE_SUBREPORTE_HONORARIOS= "subReporteFacturasHonorarios";
	private static String RUTA_SUBREPORTE_VALOR_HONORARIOS = "com/servinte/axioma/generadorReporte/facturacion/valorHonorariosMedicos/subReporteValorFacturasHonorarios.jasper";
	private static String NOMBRE_SUBREPORTE_VALOR_HONORARIOS = "subReporteValorFacturasHonorarios";
	private static String RUTA_SUBREPORTE_VALOR_ANULADAS = "com/servinte/axioma/generadorReporte/facturacion/valorHonorariosMedicos/subReporteValorFacturasAnuladas.jasper";
	private static String NOMBRE_SUBREPORTE_VALOR_ANULADAS = "subReporteValorFacturasAnuladas";
	private static String RUTA_SUBREPORTE_RESUMEN = "com/servinte/axioma/generadorReporte/facturacion/valorHonorariosMedicos/subReporteResumenPool.jasper";
	private static String NOMBRE_SUBREPORTE_RESUMEN = "subReporteResumenPool";
	
	/**FIXME ------->AQUI VA LA RUTA Y EL NOMBRE DE LOS SUBREPORTES*/
	
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Camilo Gómez
	 */
	public GeneradorReporteValorHonorariosMedico() {
	
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado    
	      	        
	
	 * @param filtroCambioSer
	 *
	 * @author Camilo Gómez
	 */
	public GeneradorReporteValorHonorariosMedico(DtoHonorariosMedico dtoResultado){	
		
		this.dtoResultado  = dtoResultado;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override	
	public Collection obtenerDatosReporte() 
	{
		Collection<DtoHonorariosMedico>  collectionGeneral= new ArrayList();
		DtoHonorariosMedico dtoHonorarios = new  DtoHonorariosMedico();
		
			
		if (dtoResultado != null) 
		{
			dtoHonorarios.setFechaInicial(dtoResultado.getFechaInicial());	
			dtoHonorarios.setFechaFinal(dtoResultado.getFechaFinal());
			dtoHonorarios.setEsAnulado(dtoResultado.getEsAnulado());
			dtoHonorarios.setEsFacturado(dtoResultado.getEsFacturado());
			dtoHonorarios.setEsResumen(dtoResultado.getEsResumen());
			if(dtoResultado.getEsResumen()==1){
			dtoHonorarios.setTituloResumen(dtoResultado.getTituloResumen());
			dtoHonorarios.setTotalesResumen(dtoResultado.getTotalesResumen());
			}
			
			//---------------Servicios
			for (DtoHonorariosMedico dtoProf : dtoResultado.getListaProfesionales()) 
			{					
							
				//Honorarios
				if(!Utilidades.isEmpty(dtoProf.getListaFacturas()))
					dtoProf.setJRDListaFacturas(new JRBeanCollectionDataSource(dtoProf.getListaFacturas()));
				
				//Fact Anuladas
				
				if(!Utilidades.isEmpty(dtoProf.getListaFacturasAnuladas()))
					dtoProf.setJRDListaFacturasAnuladas(new JRBeanCollectionDataSource(dtoProf.getListaFacturasAnuladas()));
			}
				
			
			
			dtoHonorarios.setJRDListaProfesionales(new JRBeanCollectionDataSource(dtoResultado.getListaProfesionales()));
			if (dtoResultado.getEsResumen()==1){
			dtoHonorarios.setJRDListaResumenPool(new JRBeanCollectionDataSource(dtoResultado.getListaResumenPool()));
			}
			collectionGeneral.add(dtoHonorarios);
			
		}
		
		return collectionGeneral;
	}

	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
      

        try {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_HONORARIOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_HONORARIOS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_HONORARIOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_VALOR_HONORARIOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_VALOR_HONORARIOS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_VALOR_HONORARIOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_VALOR_ANULADAS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_VALOR_ANULADAS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_VALOR_ANULADAS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESUMEN);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESUMEN);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_RESUMEN, mySubreportObj);
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
