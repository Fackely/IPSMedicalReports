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
import com.princetonsa.dto.facturasVarias.DtoConsolodadoFacturaVaria;
import com.servinte.axioma.generadorReporte.facturasVarias.dtoFacturasVarias.DtoConsultaMovimientosFacturaVariaGen;

import util.reportes.GeneradorReporte;

public class generadorConsultaMovimientosFacturaVariaPlano extends GeneradorReporte{


	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
    ArrayList<DtoConsolodadoFacturaVaria> listadoResultado;
    ConsultaMovimientoFacturaForm forma;
    private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/facturasVarias/consultaMovimientosFacturasVarias/consultaMovimientosFacturasVariasPlano.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTAFACTURAVARIA = "com/servinte/axioma/generadorReporte/facturasVarias/consultaMovimientosFacturasVarias/subreporteConsultaFacturaPlano.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTAFACTURAVARIA = "subreporteConsultaFacturaPlano";
    
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * 
	 */
	public generadorConsultaMovimientosFacturaVariaPlano() {
		
	}
	
	/**
	 * Método constructor de la clase 
	 * @param listadoResultado
	 * @param forma
	 *
	 *
	 */
	public generadorConsultaMovimientosFacturaVariaPlano(ArrayList<DtoConsolodadoFacturaVaria> listadoResultado,  ConsultaMovimientoFacturaForm forma){
		
		this.listadoResultado = listadoResultado;
		this.forma=forma;
	
	}
	@Override
	public Collection obtenerDatosReporte() {
		Collection<DtoConsultaMovimientosFacturaVariaGen>  collectionDTOGeneral= new ArrayList();
		DtoConsultaMovimientosFacturaVariaGen dtoGeneral = new DtoConsultaMovimientosFacturaVariaGen();	
		if (forma != null) {
			
			dtoGeneral.setFechaInicial(forma.getFechaInicialFormateada());
			dtoGeneral.setFechaFinal(forma.getFechaFinalFormateada());
			dtoGeneral.setUsuario(forma.getUsuario());
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
