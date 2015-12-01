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
import com.princetonsa.dto.odontologia.DTOGeneralReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOInstitucionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
/**
 * 
 * Esta clase se encarga de generar el reporte de pacientes
 * por estado del presupuesto odontológico
 * 
 * @author Angela Maria Aguirre
 * @since 22/10/2010
 */
public class GeneradorReportePacientesEstadoPresupuesto extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion;
    private DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/reportePacientesEstadoPresupuesto.jasper";
	private static String RUTA_SUBREPORTE_INSTITUCION = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/subreporteDetalleEstadoPresupuestoInstitucion.jasper";
	private static String RUTA_SUBREPORTE_CENTRO_ATENCION = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/subreporteDetalleEstadoPresupuestoCentroAtencion.jasper";
	private static String RUTA_SUBREPORTE_ESTADOS_PRESUPUESTO = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/subreporteDetalleEstadoPresupuestoEstado.jasper";
		
	private static String NOMBRE_SUBREPORTE_INSTITUCION= "subreporteDetalleEstadoPresupuestoInstitucion";
	private static String NOMBRE_SUBREPORTE_CENTRO_ATENCION= "subreporteDetalleEstadoPresupuestoCentroAtencion";
	private static String NOMBRE_SUBREPORTE_ESTADOS_PRESUPUESTO= "subreporteDetalleEstadoPresupuestoEstado";
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReportePacientesEstadoPresupuesto() {
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion, 
	 *		DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros
	 * 		
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReportePacientesEstadoPresupuesto(ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion, 
			DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros){	
		
		this.listaInstitucion = listaInstitucion;
		this.dtoFiltros = dtoFiltros;
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
		
		ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucionReporte = 
			new ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto>();		
		
		if (listaInstitucion != null && listaInstitucion.size()>0) {
			
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
			
			for (DTOInstitucionReportePacientesEstadoPresupuesto institucion : listaInstitucion) {			
				
				for(DTOCentroAtencionReportePacientesEstadoPresupuesto centroAtencion:institucion.getListaCentroAtencion()){
					
					centroAtencion.setDsListaEstados(new JRBeanCollectionDataSource( centroAtencion.getListaEstados()));
				}				
				institucion.setDsListaCentroAtrencion(new JRBeanCollectionDataSource(
						institucion.getListaCentroAtencion()));
				
				listaInstitucionReporte.add(institucion);				
			}
			dtoGeneral.setDsListaInstitucion(new JRBeanCollectionDataSource(listaInstitucionReporte));			
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
        	
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_INSTITUCION);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_INSTITUCION);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_INSTITUCION, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CENTRO_ATENCION);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CENTRO_ATENCION);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_CENTRO_ATENCION, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ESTADOS_PRESUPUESTO);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ESTADOS_PRESUPUESTO);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ESTADOS_PRESUPUESTO, mySubreportObj);
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
