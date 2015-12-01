package com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.axioma.util.log.Log4JManager;

import util.reportes.GeneradorReporte;


/**
 * @author hermorhu
 * @created 26-Oct-2012 
 */
public class GeneradorReporteCurvasCrecimiento extends GeneradorReporte {

	private static final String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/historiaClinica/curvasCrecimiento/ReporteCurvasCrecimientoDesarrollo.jasper";
	
	private static final String RUTA_SUBREPORTE_SIGNOS_VITALES = "com/servinte/axioma/generadorReporte/historiaClinica/curvasCrecimiento/subReporteSignosVitales.jasper";
	
	private static final String NOMBRE_SUBREPORTE_SIGNOS_VITALES = "subReporteSignosVitales";
	
	/** Parametros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private CurvaCrecimientoDesarrolloDto dtoCurvaCrecimientoDesarrollo = new CurvaCrecimientoDesarrolloDto();
	
	/**
	 * 
	 */
	public GeneradorReporteCurvasCrecimiento() {
		super();
	}

	/**
	 * @param dtoCurvaCrecimientoDesarrollo
	 */
	public GeneradorReporteCurvasCrecimiento(CurvaCrecimientoDesarrolloDto dtoCurvaCrecimientoDesarrollo) {
		super();
		this.dtoCurvaCrecimientoDesarrollo = dtoCurvaCrecimientoDesarrollo;
	}

	/** (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerDatosReporte()
	 */
	@Override
	public Collection<CurvaCrecimientoDesarrolloDto> obtenerDatosReporte() {
		Collection<CurvaCrecimientoDesarrolloDto> collectionDtoCurva = new ArrayList<CurvaCrecimientoDesarrolloDto>();
		CurvaCrecimientoDesarrolloDto dtoCurva = new CurvaCrecimientoDesarrolloDto();
		
		//Se llena el dto con los datos necesarios para crear el reporte 
		if(dtoCurvaCrecimientoDesarrollo != null){
			
			dtoCurva.setRazonSocial(dtoCurvaCrecimientoDesarrollo.getRazonSocial());
			dtoCurva.setNit(dtoCurvaCrecimientoDesarrollo.getNit());
			dtoCurva.setActividadEconomica(dtoCurvaCrecimientoDesarrollo.getActividadEconomica());
			dtoCurva.setDireccion(dtoCurvaCrecimientoDesarrollo.getDireccion());
			dtoCurva.setTelefono(dtoCurvaCrecimientoDesarrollo.getTelefono());
			dtoCurva.setCentroAtencion(dtoCurvaCrecimientoDesarrollo.getCentroAtencion());
			dtoCurva.setTipoReporte(dtoCurvaCrecimientoDesarrollo.getTipoReporte());
			
			dtoCurva.setPaciente(dtoCurvaCrecimientoDesarrollo.getPaciente());
			dtoCurva.setIdentificacion(dtoCurvaCrecimientoDesarrollo.getIdentificacion());
			dtoCurva.setFechaNacimiento(dtoCurvaCrecimientoDesarrollo.getFechaNacimiento());
			dtoCurva.setEdad(dtoCurvaCrecimientoDesarrollo.getEdad());
			dtoCurva.setSexo(dtoCurvaCrecimientoDesarrollo.getSexo());
			dtoCurva.setEstadoCivil(dtoCurvaCrecimientoDesarrollo.getEstadoCivil());
			dtoCurva.setOcupacion(dtoCurvaCrecimientoDesarrollo.getOcupacion());
			dtoCurva.setTipoAfiliacion(dtoCurvaCrecimientoDesarrollo.getTipoAfiliacion());
			dtoCurva.setAcompanantePaciente(dtoCurvaCrecimientoDesarrollo.getAcompanantePaciente());
			dtoCurva.setTelAcompanante(dtoCurvaCrecimientoDesarrollo.getTelAcompanante());
			dtoCurva.setParentescoAcompanante(dtoCurvaCrecimientoDesarrollo.getParentescoAcompanante());
			dtoCurva.setResponsablePaciente(dtoCurvaCrecimientoDesarrollo.getResponsablePaciente());
			dtoCurva.setTelResponsable(dtoCurvaCrecimientoDesarrollo.getTelResponsable());
			dtoCurva.setParentescoResponsable(dtoCurvaCrecimientoDesarrollo.getParentescoResponsable());
			dtoCurva.setViaIngreso(dtoCurvaCrecimientoDesarrollo.getViaIngreso());
			dtoCurva.setFechaIngreso(dtoCurvaCrecimientoDesarrollo.getFechaIngreso());
			dtoCurva.setViaEgreso(dtoCurvaCrecimientoDesarrollo.getFechaEgreso());
			dtoCurva.setCama(dtoCurvaCrecimientoDesarrollo.getCama());
			dtoCurva.setResponsable(dtoCurvaCrecimientoDesarrollo.getResponsable());
			
			dtoCurva.setListaSignosVitales(dtoCurvaCrecimientoDesarrollo.getListaSignosVitales());
			
			dtoCurva.setTituloGrafica(dtoCurvaCrecimientoDesarrollo.getTituloGrafica());
			dtoCurva.setDescripcionGrafica(dtoCurvaCrecimientoDesarrollo.getDescripcionGrafica());
			
			dtoCurva.setImagenCurva(dtoCurvaCrecimientoDesarrollo.getImagenCurva());
			
			if(existeLogo(dtoCurvaCrecimientoDesarrollo.getLogo())){
				dtoCurva.setLogo(dtoCurvaCrecimientoDesarrollo.getLogo());
			}else{
				dtoCurva.setLogo(null);
			}
			
			if(existeLogo(dtoCurvaCrecimientoDesarrollo.getImagenIzquierda())){
				dtoCurva.setImagenIzquierda(dtoCurvaCrecimientoDesarrollo.getImagenIzquierda());
			}else{
				dtoCurva.setImagenIzquierda(null);
			}
			
			if(existeLogo(dtoCurvaCrecimientoDesarrollo.getImagenDerecha())){
				dtoCurva.setImagenDerecha(dtoCurvaCrecimientoDesarrollo.getImagenDerecha());
			}else{
				dtoCurva.setImagenDerecha(null);
			}
			
			if(existeLogo(dtoCurvaCrecimientoDesarrollo.getFirmaElectronica())){
				dtoCurva.setFirmaElectronica(dtoCurvaCrecimientoDesarrollo.getFirmaElectronica());
			}else{
				dtoCurva.setFirmaElectronica(null);
			}
			
			dtoCurva.setJRDSignosVitales(new JRBeanCollectionDataSource(dtoCurvaCrecimientoDesarrollo.getListaSignosVitales()));
			
			collectionDtoCurva.add(dtoCurva);
			
		}
		
		return collectionDtoCurva;
	}

	/** (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerDatosAdicionalesReporte()
	 */
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
		
		try{
			//Se obtiene el subreporte de Signos Vitales
			myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_SIGNOS_VITALES);
		
			if(myInFile == null) {
				myInFile = new FileInputStream(RUTA_SUBREPORTE_SIGNOS_VITALES);
			}else {
				Log4JManager.info(myInFile.getClass());
				Object mySubreportObj = JRLoader.loadObject(myInFile);
		        parametrosReporte.put(NOMBRE_SUBREPORTE_SIGNOS_VITALES, mySubreportObj);
		        Log4JManager.info(myInFile.getClass()+""+mySubreportObj);
		    }
		}catch (JRException e) {
			Log4JManager.error(e);
		}catch (FileNotFoundException e) {
			Log4JManager.error(e);
		}finally{
			try{
				if(myInFile != null){
					myInFile.close();
					myInFile=null;
				}
			}catch (IOException e) {
				Log4JManager.error(e);
			}	
		}     
		
		return parametrosReporte;
	}

	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}

}
