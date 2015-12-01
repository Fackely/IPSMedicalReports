package com.servinte.axioma.generadorReporte.ordenes.solicitudMedicamentos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

/**
 * @author hermorhu
 * @created 08-Mar-2013 
 */
public class GeneradorReporteSolicitudMedicamentos extends GeneradorReporte{

	private static final String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/ordenes/solicitudMedicamentos/reporteSolicitudMedicamentos.jasper";
	
	private static final String RUTA_SUBREPORTE_MEDICAMENTOS_SOLICITADOS = "com/servinte/axioma/generadorReporte/ordenes/solicitudMedicamentos/subReporteMedicamentosSolicitados.jasper";
	private static final String NOMBRE_SUBREPORTE_MEDICAMENTOS_SOLICITADOS = "subReporteMedicamentosSolicitados";
	
	private static final String RUTA_SUBREPORTE_INSUMOS_SOLICITADOS = "com/servinte/axioma/generadorReporte/ordenes/solicitudMedicamentos/subReporteInsumosSolicitados.jasper";
	private static final String NOMBRE_SUBREPORTE_INSUMOS_SOLICITADOS = "subReporteInsumosSolicitados";
	
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private ImpresionSolicitudMedicamentosDto solicitudMedicamentosDto = new ImpresionSolicitudMedicamentosDto();
	
	/**
	 * 
	 */
	public GeneradorReporteSolicitudMedicamentos() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param solicitudMedicamentosDto
	 */
	public GeneradorReporteSolicitudMedicamentos(ImpresionSolicitudMedicamentosDto solicitudMedicamentosDto) {
		super();
		this.solicitudMedicamentosDto = solicitudMedicamentosDto;
	}
    
	/** (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerDatosReporte()
	 */
	@Override
	public Collection<ImpresionSolicitudMedicamentosDto> obtenerDatosReporte() {
		Collection<ImpresionSolicitudMedicamentosDto> collectionSolicitudMedicamentosDto = new ArrayList<ImpresionSolicitudMedicamentosDto>();
		
		if(solicitudMedicamentosDto != null) {
			
			if(!UtilidadTexto.isEmpty(solicitudMedicamentosDto.getRutaLogo()) && !UtilidadTexto.isEmpty(solicitudMedicamentosDto.getUbicacionLogo())){
				
				if(existeLogo(solicitudMedicamentosDto.getRutaLogo())) {
					if (solicitudMedicamentosDto.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
						solicitudMedicamentosDto.setLogoIzquierda(null);
						solicitudMedicamentosDto.setLogoDerecha(solicitudMedicamentosDto.getRutaLogo());
					} else if (solicitudMedicamentosDto.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
						solicitudMedicamentosDto.setLogoIzquierda(solicitudMedicamentosDto.getRutaLogo());
						solicitudMedicamentosDto.setLogoDerecha(null);
					}
				}
			}
			
			if(solicitudMedicamentosDto.getListaMedicamentosInsumosSolicitados() != null && !solicitudMedicamentosDto.getListaMedicamentosInsumosSolicitados().isEmpty()) {
				
				List<ImpresionMedicamentoInsumoSolicitadoDto> medicamentosSolicitados = new ArrayList<ImpresionMedicamentoInsumoSolicitadoDto>();
				List<ImpresionMedicamentoInsumoSolicitadoDto> insumosSolicitados = new ArrayList<ImpresionMedicamentoInsumoSolicitadoDto>();
				
				for(ImpresionMedicamentoInsumoSolicitadoDto medicamentoInsumoDto : solicitudMedicamentosDto.getListaMedicamentosInsumosSolicitados()) {
					if(medicamentoInsumoDto.getEsMedicamento().equals(ConstantesBD.acronimoSi)) {
						medicamentosSolicitados.add(medicamentoInsumoDto);
					}else {
						insumosSolicitados.add(medicamentoInsumoDto);
					}
				}
				solicitudMedicamentosDto.setJRDMedicamentosSolicitados(new JRBeanCollectionDataSource(medicamentosSolicitados));
				solicitudMedicamentosDto.setJRDInsumosSolicitados(new JRBeanCollectionDataSource(insumosSolicitados));
			}
			
			if(existeLogo(solicitudMedicamentosDto.getFirmaDigital())){
				solicitudMedicamentosDto.setFirmaDigital(solicitudMedicamentosDto.getFirmaDigital());
			}else{
				solicitudMedicamentosDto.setFirmaDigital(null);
			}
			
			collectionSolicitudMedicamentosDto.add(solicitudMedicamentosDto);
		}
		
		return collectionSolicitudMedicamentosDto;
	}
	
	/** (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerDatosAdicionalesReporte()
	 */
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
		
		try {
			myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_MEDICAMENTOS_SOLICITADOS);
		
			if(myInFile == null) {
				myInFile = new FileInputStream(RUTA_SUBREPORTE_MEDICAMENTOS_SOLICITADOS);
			} else {
				Log4JManager.info(myInFile.getClass());
				Object mySubreportObj = JRLoader.loadObject(myInFile);
		        parametrosReporte.put(NOMBRE_SUBREPORTE_MEDICAMENTOS_SOLICITADOS, mySubreportObj);
		        Log4JManager.info(myInFile.getClass()+""+mySubreportObj);
		    }
			
			myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_INSUMOS_SOLICITADOS);
			
			if(myInFile == null) {
				myInFile = new FileInputStream(RUTA_SUBREPORTE_INSUMOS_SOLICITADOS);
			} else {
				Log4JManager.info(myInFile.getClass());
				Object mySubreportObj = JRLoader.loadObject(myInFile);
		        parametrosReporte.put(NOMBRE_SUBREPORTE_INSUMOS_SOLICITADOS, mySubreportObj);
		        Log4JManager.info(myInFile.getClass()+""+mySubreportObj);
		    }
		 }catch (JRException e) {
			Log4JManager.error(e);
		} catch (FileNotFoundException e) {
			Log4JManager.error(e);
		} finally {
			try {
				if(myInFile != null) {
					myInFile.close();
					myInFile=null;
				}
			} catch (IOException e) {
				Log4JManager.error(e);
			}	
		}     
		
		return parametrosReporte;
	}

	/** (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerRutaPlantilla()
	 */
	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}

}
