package com.servinte.axioma.generadorReporte.odontologia.promocionesOdontologicas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReportePromocionesOdontologicasGen;

/**
 * Esta clase se encarga de generar el reporte de promociones odontológicas en formato archivo plano
 * 
 * @author Fabian Becerra
 *
 */
public class GeneradorReportePlanoPromocionesOdontologicas extends GeneradorReporte{
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> listadoResultado;
	private DtoFiltroReportePromocionesOdontologicas filtroPromociones;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/promocionesOdontologicas/reportePromocionesOdontologicasPlano.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTA = "com/servinte/axioma/generadorReporte/odontologia/promocionesOdontologicas/subReportePromocionesOdontologicasPlano.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTA = "subReportePromocionesOdontologicasPlano";
	
	/**
	 * 
	 * Méodo constructor de la clase 
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlanoPromocionesOdontologicas() {
	}
	
	/**
	 * Método constructor de la clase 
	 * @param listadoResultado
	 * @param filtroPromociones
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlanoPromocionesOdontologicas(ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> listadoResultado, 
			DtoFiltroReportePromocionesOdontologicas filtroPromociones){	
		
		this.listadoResultado = listadoResultado;
		this.filtroPromociones = filtroPromociones;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection obtenerDatosReporte() {
		Collection<DtoReportePromocionesOdontologicasGen>  collectionDTOGeneral= new ArrayList();
		
		DtoReportePromocionesOdontologicasGen dtoGeneral = new  DtoReportePromocionesOdontologicasGen();	
		
		if (listadoResultado != null) {
			
			if (filtroPromociones != null) {
				
			dtoGeneral.setDsResultadoPromociones(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
			}
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
