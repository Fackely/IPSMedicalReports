/**
 * 
 */
package com.servinte.axioma.generadorReporte.tesoreria.consolidadoMovimientos;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.interfaz.DtoMovimientoTipoDocumento;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteIngresoOdonto;

import util.ConstantesIntegridadDominio;
import util.UtilidadFileUpload;
import util.reportes.GeneradorReporte;

 /**
 * Esta clase se encarga de obtener los datos necesarios para
 * generar el reporte del consolidado de movimientos de abonos paciente.
 * @author Yennifer Guerrero
 * @since 31/03/2011
 */
public class GeneradorReporteConsolidadoMovimientos extends GeneradorReporte {

	/** Parámetros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private DtoMovmimientosAbonos dtoConsolidado;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/reporteConsolidadoMovimientos.jasper";
	private static String RUTA_SUBREPORTE_INFOPACIENTE = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/subreporteInfoPaciente.jasper";
	private static String NOMBRE_SUBREPORTE_INFOPACIENTE = "subreporteInfoPaciente";
	private static String RUTA_SUBREPORTE_CONSOLIDADO_MOVIMIENTO = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/subreporteConsolidadoMovimiento.jasper";
	private static String NOMBRE_SUBREPORTE_CONSOLIDADO_MOVIMIENTO = "subreporteConsolidadoMovimiento";
	private static String RUTA_SUBREPORTE_DETALLE_MOVIMIENTO = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/subreporteDetalleMovimiento.jasper";
	private static String NOMBRE_SUBREPORTE__DETALLE_MOVIMIENTO = "subreporteDetalleMovimiento";
	private boolean esArchivoPlano = false;
	
	private static String RUTA_REPORTE_PLANO = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/reportePlanoConsolidadoMovimientos.jasper";
	private static String RUTA_SUBREPORTE_PLANO_INFOPACIENTE = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/subreportePlanoInfoPaciente.jasper";
	private static String NOMBRE_SUBREPORTE_PLANO_INFOPACIENTE = "subreportePlanoInfoPaciente";
	private static String RUTA_SUBREPORTE_PLANO_CONSOLIDADO_MOVIMIENTO = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/subreportePlanoConsolidadoMovimiento.jasper";
	private static String NOMBRE_SUBREPORTE_PLANO_CONSOLIDADO_MOVIMIENTO = "subreportePlanoConsolidadoMovimiento";
	private static String RUTA_SUBREPORTE_PLANO_DETALLE_MOVIMIENTO = "com/servinte/axioma/generadorReporte/tesoreria/consolidadoMovimientos/subreportePlanoDetalleMovimiento.jasper";
	private static String NOMBRE_SUBREPORTE_PLANO_DETALLE_MOVIMIENTO = "subreportePlanoDetalleMovimiento";
	
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * Método constructor de la clase
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteConsolidadoMovimientos() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Método constructor de la clase
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteConsolidadoMovimientos(DtoMovmimientosAbonos dtoConsolidado){	
		
		this.dtoConsolidado = dtoConsolidado;
		this.esArchivoPlano = dtoConsolidado.isEsArchivoPlano();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoMovmimientosAbonos>  collectionDTOGeneral= new ArrayList();
			
		RUTA_LOGO = "../"+ dtoConsolidado.getRutaLogo();
		
		if (dtoConsolidado != null) {
			
			String ubicacionLogo = dtoConsolidado.getUbicacionLogo();
			String rutaLogo = "../"+dtoConsolidado.getRutaLogo();
	
			boolean existeLogo = existeLogo(rutaLogo);
			
			if (existeLogo) {
				if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
					dtoConsolidado.setLogoDerecha(rutaLogo);
					dtoConsolidado.setLogoIzquierda(null);
				} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
					dtoConsolidado.setLogoIzquierda(rutaLogo);
					dtoConsolidado.setLogoDerecha(null);
				}
			}else{
				dtoConsolidado.setLogoDerecha(null);
				dtoConsolidado.setLogoIzquierda(null);
			}
				
			ArrayList<DtoPaciente> infoPaciente = new ArrayList<DtoPaciente>();
			infoPaciente.add(dtoConsolidado.getInfoPaciente());
			dtoConsolidado.setDsInfoPaciente(new JRBeanCollectionDataSource(infoPaciente));
			
			if (dtoConsolidado.isEsConsolidado()) {
				dtoConsolidado.setDsConsolidadoMovimientos(new JRBeanCollectionDataSource(dtoConsolidado.getListadoConsolidadoMovimientos()));
				dtoConsolidado.setDsDetalleMovimientos(null);
			}else{
				dtoConsolidado.setDsDetalleMovimientos(new JRBeanCollectionDataSource(dtoConsolidado.getListadoDetalleMovimientos()));
				dtoConsolidado.setDsConsolidadoMovimientos(null);
			}
			
			collectionDTOGeneral.add(dtoConsolidado);
		}
		
		return collectionDTOGeneral;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        

        try {
        	/*
        	 *  Si el archivo a generar no es plano debe tomar las plantillas 
        	 *  de pdf y excel para imprimir la información.
        	 */
        	if (!esArchivoPlano) {
        		
        		 myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_INFOPACIENTE);
     	        if (myInFile == null) {
     	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_INFOPACIENTE);
     	           	
     			}else if (myInFile != null) {
     	            Object mySubreportObj = JRLoader.loadObject(myInFile);
     	            parametrosReporte.put(NOMBRE_SUBREPORTE_INFOPACIENTE, mySubreportObj);
     	            myInFile.close();
     	            myInFile=null;
     	        }
     	        
     	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CONSOLIDADO_MOVIMIENTO);
     	        if (myInFile == null) {
     	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CONSOLIDADO_MOVIMIENTO);
     	           	
     			}else if (myInFile != null) {
     	            Object mySubreportObj = JRLoader.loadObject(myInFile);
     	            parametrosReporte.put(NOMBRE_SUBREPORTE_CONSOLIDADO_MOVIMIENTO, mySubreportObj);
     	            myInFile.close();
     	            myInFile=null;
     	        }
     	        
     	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_DETALLE_MOVIMIENTO);
     	        if (myInFile == null) {
     	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_DETALLE_MOVIMIENTO);
     	           	
     			}else if (myInFile != null) {
     	            Object mySubreportObj = JRLoader.loadObject(myInFile);
     	            parametrosReporte.put(NOMBRE_SUBREPORTE__DETALLE_MOVIMIENTO, mySubreportObj);
     	            myInFile.close();
     	            myInFile=null;
     	        }
			}else{
				//Si debe imprimir un archivo plano debe tomar las plantillas correspondientes.
				
				 myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PLANO_INFOPACIENTE);
	     	        if (myInFile == null) {
	     	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PLANO_INFOPACIENTE);
	     	           	
	     			}else if (myInFile != null) {
	     	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	     	            parametrosReporte.put(NOMBRE_SUBREPORTE_PLANO_INFOPACIENTE, mySubreportObj);
	     	            myInFile.close();
	     	            myInFile=null;
	     	        }
	     	        
	     	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PLANO_CONSOLIDADO_MOVIMIENTO);
	     	        if (myInFile == null) {
	     	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PLANO_CONSOLIDADO_MOVIMIENTO);
	     	           	
	     			}else if (myInFile != null) {
	     	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	     	            parametrosReporte.put(NOMBRE_SUBREPORTE_PLANO_CONSOLIDADO_MOVIMIENTO, mySubreportObj);
	     	            myInFile.close();
	     	            myInFile=null;
	     	        }
	     	        
	     	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PLANO_DETALLE_MOVIMIENTO);
	     	        if (myInFile == null) {
	     	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PLANO_DETALLE_MOVIMIENTO);
	     	           	
	     			}else if (myInFile != null) {
	     	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	     	            parametrosReporte.put(NOMBRE_SUBREPORTE_PLANO_DETALLE_MOVIMIENTO, mySubreportObj);
	     	            myInFile.close();
	     	            myInFile=null;
	     	        }
			}
	       
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}
	

	@Override
	public String obtenerRutaPlantilla() {
		
		if (esArchivoPlano) {
			return RUTA_REPORTE_PLANO;
		}
		
		return RUTA_REPORTE_GENERAL;
	}

}
