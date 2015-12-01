package com.servinte.axioma.generadorReporte.odontologia.pacientesEstadoPresupuesto;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DTOCentroAtencionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOEstadosReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOGeneralReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
/**
 * 
 * Esta clase se encarga de generar el reporte del detalle de pacientes
 * por estado del presupuesto odontológico
 * 
 * @author Angela Maria Aguirre
 * @since 22/10/2010
 */
public class GeneradorReporteDetallePacientesEstadoPresupuesto extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private DTOCentroAtencionReportePacientesEstadoPresupuesto dtoCentroAtencion;
    private DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros;
    private ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstado;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/reporteDetallePacientesEstadoPresupuesto.jasper";
	private static String RUTA_SUBREPORTE_DETALLE = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/subreporteDetallePacientesEstadoPresupuesto.jasper";		
	private static String NOMBRE_SUBREPORTE_DETALLE = "subreporteDetallePacientesEstadoPresupuesto";
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReporteDetallePacientesEstadoPresupuesto() {
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion, 
	 *		DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros
	 * 		
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReporteDetallePacientesEstadoPresupuesto(DTOCentroAtencionReportePacientesEstadoPresupuesto dtoCentroAtencion, 
			DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros, ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstado){	
		
		this.dtoCentroAtencion = dtoCentroAtencion;
		this.dtoFiltros = dtoFiltros;
		this.listaEstado = listaEstado;
	}
	
	/**
	 * 
	 * Este Método se encarga de crear las fuentes de datos del reporte
	 * 
	 * @return Collection
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		DTOGeneralReportePacientesEstadoPresupuesto dtoGeneral = new DTOGeneralReportePacientesEstadoPresupuesto();
		
		Collection<DTOGeneralReportePacientesEstadoPresupuesto>  collectionDTOGeneral= 
			new ArrayList<DTOGeneralReportePacientesEstadoPresupuesto>();
		
		if (dtoCentroAtencion != null) {
			
			if (dtoFiltros != null) {
				
				String ubicacionLogo = dtoFiltros.getUbicacionLogo();
				String rutaLogo = "../"+dtoFiltros.getRutaLogo();
		
				dtoGeneral.setRazonSocial(dtoFiltros.getRazonSocial());
				dtoGeneral.setFechaInicial(UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaInicial()));
				dtoGeneral.setFechaFinal(UtilidadFecha.conversionFormatoFechaAAp(dtoFiltros.getFechaFinal()));
				dtoGeneral.setEdadPaciente(dtoFiltros.getEtiquetaEdadPaciente());
				dtoGeneral.setSexoPaciente(dtoFiltros.getNombreSexo());
				dtoGeneral.setPaqueteOdontologico(dtoFiltros.getNombrePaquete());
				dtoGeneral.setProgramaeOdontologico(dtoFiltros.getNombrePrograma());
				dtoGeneral.setUsuarioProceso(dtoFiltros.getUsuarioProceso());
				dtoGeneral.setNombreCentroAtencion(dtoCentroAtencion.getNombreCompletoCentroAtencion());
				DTOEstadosReportePacientesEstadoPresupuesto estado = listaEstado.get(0);
				dtoGeneral.setDsListaPacientes(new JRBeanCollectionDataSource(estado.getListaPacientes()));
				dtoGeneral.setNombreEstadoGeneracionReporteDetalle(dtoFiltros.getNombreEstadoPresupuesto());
				boolean existeLogo = existeLogo(rutaLogo);
				
				if (existeLogo) {
					if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
						dtoGeneral.setLogoDerecha(rutaLogo);
						dtoGeneral.setLogoIzquierda(null);
					} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
						dtoGeneral.setLogoIzquierda(rutaLogo);
						dtoGeneral.setLogoDerecha(null);
					}
				}else{
					dtoGeneral.setLogoDerecha(null);
					dtoGeneral.setLogoIzquierda(null);
				}
				
			}		
		}
		collectionDTOGeneral.add(dtoGeneral);
		return collectionDTOGeneral;
	}
	
	/**
	 * 
	 * Este Método se encarga de cargar como parámetros
	 * las plantillas de los subreportes y datos adicionales
	 * 
	 * @return Map
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;

        try {      	
        	
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_DETALLE);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_DETALLE);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_DETALLE, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}
	

	/**
	 * 
	 * Este Método se encarga de devolver la 
	 * ruta del reporte
	 * 
	 * @return String
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}
	
	/**
	 * 
	 * Este Método se encarga de verificar si existe
	 * o no la imagen a cargar según una ruta dada
	 * 
	 * @param String rutaLogo
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean existeLogo (String rutaLogo){
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        boolean existe = false;

        try {
	        myInFile = loader.getResourceAsStream(rutaLogo);
	        
	        if (myInFile != null) {
	           existe = true;
	            myInFile.close();
	        }
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
        
        return existe;
	}
}
