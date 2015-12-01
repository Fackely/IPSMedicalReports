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

import util.reportes.GeneradorReporte;

public class GeneradorREportePlanoPresupuestosOdontologicosComtratadosConPromocion extends GeneradorReporte{
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listadoResultado;
	DtoReportePresupuestosOdontologicosContratadosConPromocion filtroIngresos;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContrataPromocion/reportePresupuestoOdoContratadoPromocion.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTA = "com/servinte/axioma/generadorReporte/odontologia/presupuestosOdontoContrataPromocion/subreportePlanoPromo.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTA = "subreportePlanoPromo";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Javier Gonzalez
	 */
	public GeneradorREportePlanoPresupuestosOdontologicosComtratadosConPromocion() {
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado Arraylist de dtos de la consulta
	 *
	 * @author Javier Gonzalez
	 */
	public GeneradorREportePlanoPresupuestosOdontologicosComtratadosConPromocion(ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listadoResultado, 
			DtoReportePresupuestosOdontologicosContratadosConPromocion filtroIngresos){	
		
		this.listadoResultado = listadoResultado;
		this.filtroIngresos = filtroIngresos;
	}
	@Override
	public Collection obtenerDatosReporte() {
		Collection<DtoReportePresupuestosOdontologicosContratadosConPromocionGen>  collectionDTOGeneral= new ArrayList();
		DtoReportePresupuestosOdontologicosContratadosConPromocionGen dtoGeneral = new DtoReportePresupuestosOdontologicosContratadosConPromocionGen();
		if (listadoResultado != null) {
			dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateada());
			dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateada());
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
