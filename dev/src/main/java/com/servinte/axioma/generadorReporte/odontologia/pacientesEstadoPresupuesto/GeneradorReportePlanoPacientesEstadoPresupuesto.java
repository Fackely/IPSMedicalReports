package com.servinte.axioma.generadorReporte.odontologia.pacientesEstadoPresupuesto;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DTOCentroAtencionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOEstadosReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOGeneralReportePlanoPacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOInstitucionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOReportePlanoPacientesEstadoPresupuesto;
/**
 * 
 * Esta clase se encarga de generar el reporte  plano de pacientes
 * por estado del presupuesto odontológico
 * 
 * @author Angela Maria Aguirre
 * @since 22/10/2010
 */
public class GeneradorReportePlanoPacientesEstadoPresupuesto extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion;
	private static String RUTA_REPORTE = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/reportePlanoPacientesEstadoPresupuesto.jasper";
	private static String RUTA_SUBREPORTE = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/subreportePlanoPacientesEstadoPresupuesto.jasper";
		
	private static String NOMBRE_SUBREPORTE= "subreportePlanoPacientesEstadoPresupuesto";
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReportePlanoPacientesEstadoPresupuesto() {
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion, 
	 *		DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros
	 * 		
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReportePlanoPacientesEstadoPresupuesto(
			ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion){	
		
		this.listaInstitucion = listaInstitucion;
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
		
		DTOGeneralReportePlanoPacientesEstadoPresupuesto dtoGeneral = new 
			DTOGeneralReportePlanoPacientesEstadoPresupuesto();
		
		Collection<DTOGeneralReportePlanoPacientesEstadoPresupuesto>  collectionDTOGeneral= 
			new ArrayList<DTOGeneralReportePlanoPacientesEstadoPresupuesto>();
		
		ArrayList<DTOReportePlanoPacientesEstadoPresupuesto> listaReportePlano = 
			new ArrayList<DTOReportePlanoPacientesEstadoPresupuesto>();		
		
		DTOReportePlanoPacientesEstadoPresupuesto dtoReportePlano = null;
		
		if (listaInstitucion != null && listaInstitucion.size()>0) {
			
			for (DTOInstitucionReportePacientesEstadoPresupuesto institucion : listaInstitucion) {			
				for(DTOCentroAtencionReportePacientesEstadoPresupuesto centroAtencion:institucion.getListaCentroAtencion()){
					String[] cadena = centroAtencion.getNombreCompletoCentroAtencion().split("-");
					String[] cadenaPais =(cadena[1].split("\\(")); 
					String nombrePais = ((cadenaPais[cadenaPais.length-1]).trim()).substring(0, ((cadenaPais[cadenaPais.length-1]).trim()).length()-1);
					
					for( DTOEstadosReportePacientesEstadoPresupuesto estado: centroAtencion.getListaEstados()){
						dtoReportePlano = new DTOReportePlanoPacientesEstadoPresupuesto();
						dtoReportePlano.setNombreInstitucion(institucion.getNombreInstitucion());					
						dtoReportePlano.setNombreCentroAtencion(cadena[0]);
						dtoReportePlano.setNombreCiudad(cadenaPais[0]);
						dtoReportePlano.setNombrePais(nombrePais);
						String[] region = (cadena[2]).split(":");
						
						dtoReportePlano.setNombreRegion(region[1].trim());
						dtoReportePlano.setEstadoPresupuesto(estado.getNombreEstado());
						dtoReportePlano.setCantidadPresupuesto(estado.getCantidadPresupuesto().toString());
						listaReportePlano.add(dtoReportePlano);
					}
				}							
			}
			dtoGeneral.setDsPacientesEstadoPresupuesto(new JRBeanCollectionDataSource(listaReportePlano));
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
        	
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE, mySubreportObj);
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
		return RUTA_REPORTE;
	}
	
}
