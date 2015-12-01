package com.servinte.axioma.generadorReporte.odontologia.contrato;

/**
 * Esta clase se encarga de
 * @author, Angela Maria Aguirre
 * @version
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.odontologia.DtoFormatoImpresionContratoOdontologico;

//FIXME terminar el repoerte pára pdf que pueda imprimir html
public class GeneradorReporteContratoPresupuestoOdontologico extends GeneradorReporte{

	/**
	 * Almacena la ruta de la plantilla del reporte
	 */
	private static String RUTA_PLANTILLA="com/servinte/axioma/generadorReporte/odontologia/contrato/contratoOdontologico.jasper";
	private static String RUTA_PLANTILLA_SUBREPORTE="com/servinte/axioma/generadorReporte/odontologia/contrato/contratoOdontologico.jasper";
	private static String NOMBRE_SUBREPORTE="subReporteAnexosImpresos";
	
	
	/**
	 * Este método se encarga de realizar las consultas para
	 * obtener los datos del reporte
	 * 
	 * @param Map, con los datos necesarios para las consultas
	 * @return Collecion, con los datos obtenidos
	 * @author, Angela Maria Aguirre
	 * 
	 */

	public Collection obtenerDatosReporte(Object objReporte) {
		
		if(objReporte instanceof DtoFormatoImpresionContratoOdontologico)
		{
			DtoFormatoImpresionContratoOdontologico dtoReporte = (DtoFormatoImpresionContratoOdontologico)objReporte;
			
			Collection<String> colSubreporte = new ArrayList<String>();
			
			for (DtoCheckBox valor : dtoReporte.getListaAnexosImpresos()) 
			{
				colSubreporte.add(valor.getNombre());
			}
			
			JRBeanCollectionDataSource subReporteAnexosImpresosDS = new JRBeanCollectionDataSource(colSubreporte);
			dtoReporte.setAnexosImpresosDS(subReporteAnexosImpresosDS);
			
			
			Collection<DtoFormatoImpresionContratoOdontologico> colReporte = new ArrayList<DtoFormatoImpresionContratoOdontologico>();
			colReporte.add(dtoReporte);
			
			return colReporte;
		}
		
		else 
		{
			return null;
		}
	}
	
	
	/**
	 * Este método se encarga de
	 * 
	 */
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		return null;
		/*
		try {
			Map<String, Object> mapa = new HashMap<String, Object>();
			ClassLoader loader = this.getClass().getClassLoader();
			
			InputStream subFile = null;
			subFile = loader.getSystemResourceAsStream(RUTA_PLANTILLA_SUBREPORTE);
			if(subFile==null){
				subFile = new FileInputStream(RUTA_PLANTILLA_SUBREPORTE);
			}else{
				Object object =JRLoader.loadObject(subFile);
				mapa.put(NOMBRE_SUBREPORTE, object);
			}
			return mapa;
			
		} catch (Exception e) {
				e.printStackTrace();
		}				
		return null;
		*/
	}

	
	/**
	 * Este m&eacute;todo se encarga de retornar la ruta
	 * donde se encuentra la plantilla del reporte
	 * 
	 * @return String, con la ruta de la plantilla
	 * 
	 */
	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_PLANTILLA;
	}


	/* (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerDatosReporte()
	 */
	@Override
	public Collection obtenerDatosReporte() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
