package com.servinte.axioma.generadorReporte.odontologia.CambioServiciosCitasOdontologicas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteCambioServiciosCitasOdontologicasGen;






public class GeneradorReportePlanoCambioServiciosCitasOdontologicas extends GeneradorReporte  {

	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listadoResultado;
	
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/CambioServiciosCitasOdontologicas/reporteCambioServicioCitaOdontoPlano.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTA = "com/servinte/axioma/generadorReporte/odontologia/CambioServiciosCitasOdontologicas/subReporteCambioServicioCitaOdontoPlano.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTA = "subReporteCambioServicioCitaOdontoPlano";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlanoCambioServiciosCitasOdontologicas() {
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado Arraylist de dtos de la consulta
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlanoCambioServiciosCitasOdontologicas(ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listadoResultado){	
		
		this.listadoResultado = listadoResultado;
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection obtenerDatosReporte() {
Collection<DtoReporteCambioServiciosCitasOdontologicasGen>  collectionDTOGeneral= new ArrayList();
		
DtoReporteCambioServiciosCitasOdontologicasGen dtoGeneral = new  DtoReporteCambioServiciosCitasOdontologicasGen();	
		
		if (listadoResultado != null) {
			
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
