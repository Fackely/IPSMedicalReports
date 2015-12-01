package com.servinte.axioma.generadorReporte.tesoreria.anticiposRecibidosConvenio;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.tesoreria.DtoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoFormaPagoReport;
import com.princetonsa.dto.tesoreria.DtoRecibosConceptoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteIngresosVentaTarjetaArchivoPlano;
import com.servinte.axioma.generadorReporte.tesoreria.dtoReportes.DtoAnticiposRecibidosConvenioPlano;

/**
 * Esta clase se encarga de obtener la información necesaria para la generación del reporte
 * de anticipos recibidos por convenio. 
 * 
 * @author Yennifer Guerrero
 * @since  9/12/2010
 */
public class GeneradorReporteAnticiposRecibidosConvenioPlano extends GeneradorReporte{

	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    ArrayList<DtoAnticiposRecibidosConvenio> listadoResultado;
	DtoReporteAnticiposRecibidosConvenio filtrosAnticipos;
	private static String RUTA_REPORTE_GENERAL_PLANO = "com/servinte/axioma/generadorReporte/tesoreria/anticiposRecibidosConvenio/reporteAnticiposRecibidosConvenioPlano.jasper";
	private static String RUTA_SUBRPT_ARCHIVO_PLANO = "com/servinte/axioma/generadorReporte/tesoreria/anticiposRecibidosConvenio/subreporteAnticiposPorConveniosPlano.jasper";
	private static String NOMBRE_SUBRPT_ARCHIVO_PLANO = "subreporteAnticiposPorConveniosPlano";
	
	/**
	 * Mètodo constructor de la clase
	 *
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteAnticiposRecibidosConvenioPlano(){
		
	}
	
	/**
	 * Mètodo constructor de la clase
	 * @param listadoResultado
	 * @param filtrosAnticipos
	 *
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteAnticiposRecibidosConvenioPlano(ArrayList<DtoAnticiposRecibidosConvenio> listadoResultado,
			DtoReporteAnticiposRecibidosConvenio filtrosAnticipos){
		this.listadoResultado=listadoResultado;
		this.filtrosAnticipos=filtrosAnticipos;
	}
	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        
		 try {
		        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_ARCHIVO_PLANO);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBRPT_ARCHIVO_PLANO);
		           	
				}else if (myInFile != null) {
		            Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBRPT_ARCHIVO_PLANO, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }
		        
			}catch (Exception e) {
				e.printStackTrace();
			}

		
        return parametrosReporte;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoAnticiposRecibidosConvenioPlano>  collectionDTOGeneral= 
			new ArrayList<DtoAnticiposRecibidosConvenioPlano>();
		
		DtoAnticiposRecibidosConvenioPlano dtoGeneral = new DtoAnticiposRecibidosConvenioPlano();
		ArrayList<DtoAnticiposRecibidosConvenioPlano> listadoResultadoPlano = new ArrayList<DtoAnticiposRecibidosConvenioPlano>();
		
		if (listadoResultado != null && listadoResultado.size()>0) {
			
			if (filtrosAnticipos != null) {
				dtoGeneral.setFechaInicial(filtrosAnticipos.getFechaInicialFormateada());
				dtoGeneral.setFechaFinal(filtrosAnticipos.getFechaFinalFormateada());
			}
		
		
		
			for (DtoAnticiposRecibidosConvenio registro : listadoResultado) {
				
				for (DtoRecibosConceptoAnticiposRecibidosConvenio centroAtencion : registro.getListaRecibosCajaXCentroAtencion()) {
					
					for (DtoFormaPagoReport formaPago : centroAtencion.getListaFormaPago()) {
						dtoGeneral = new  DtoAnticiposRecibidosConvenioPlano();
						dtoGeneral.setCodigoInstitucion(registro.getCodigoIns());
						dtoGeneral.setDescripcionInstitucion(registro.getNombreInstitucion());
						dtoGeneral.setConsecutivoCentroAtencion(registro.getConsCentroAtencion());
						dtoGeneral.setDescripcionCentroAtencion(registro.getDescCentroAtencion());
						dtoGeneral.setDescripcionCiudad(registro.getDescripcionCiudad());
						dtoGeneral.setDescripcionRegion(registro.getDescripcionRegionCobertura());
						dtoGeneral.setNumeroIdConvenio(centroAtencion.getNumeroIdConvenio());
						dtoGeneral.setNombreConvenio(centroAtencion.getNombreConvenio());
						dtoGeneral.setFechaRC(UtilidadFecha.conversionFormatoFechaAAp(centroAtencion.getFechaRC()));
						dtoGeneral.setConceptoRC(centroAtencion.getDescripcionConcepto());
						dtoGeneral.setNroRC(centroAtencion.getNumeroRC());
						dtoGeneral.setEstadoRC(centroAtencion.getEstadoRC());
						dtoGeneral.setUsuario(centroAtencion.getLoginUsuario());
						dtoGeneral.setFormaPago(formaPago.getDescripcion());
						dtoGeneral.setValorFormateado(UtilidadTexto.formatearValores(formaPago.getValor()));
						
						listadoResultadoPlano.add(dtoGeneral);
					}
					
				}
								
			}
		
			dtoGeneral.setDsListadoResultado(new JRBeanCollectionDataSource(listadoResultadoPlano));
		}
		
		collectionDTOGeneral.add(dtoGeneral);
		return collectionDTOGeneral;
		
	}

	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL_PLANO;
	}
}
