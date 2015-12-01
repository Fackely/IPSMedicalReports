package com.servinte.axioma.generadorReporte.odontologia.pacientesEstadoPresupuesto;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.UtilidadFecha;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DTOCentroAtencionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOEstadosReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOGeneralReportePlanoPacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOReportePlanoDetallePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
/**
 * 
 * Esta clase se encarga de generar el reporte plano de pacientes
 * por estado del presupuesto odontológico
 * 
 * @author Angela Maria Aguirre
 * @since 22/10/2010
 */
public class GeneradorReportePlanoDetallePacientesEstadoPresupuesto extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private DTOCentroAtencionReportePacientesEstadoPresupuesto dtoCentroAtencion;
    private DtoReporteConsultaPacienteEstadoPresupuesto filtro;
    private ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstado;
	private static String RUTA_REPORTE = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/reportePlanoDetallePacientesEstadoPresupuesto.jasper";
	private static String RUTA_SUBREPORTE = "com/servinte/axioma/generadorReporte/odontologia/pacientesEstadoPresupuesto/subreportePlanoDetallePacientesEstadoPresupuesto.jasper";
		
	private static String NOMBRE_SUBREPORTE= "subreportePlanoDetallePacientesEstadoPresupuesto";
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReportePlanoDetallePacientesEstadoPresupuesto() {
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion, 
	 *		DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros
	 * 		
	 * @author, Angela Maria Aguirre
	 */
	public GeneradorReportePlanoDetallePacientesEstadoPresupuesto(
			DTOCentroAtencionReportePacientesEstadoPresupuesto dtoCentroAtencion, 
			DtoReporteConsultaPacienteEstadoPresupuesto filtro, 
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstado){	
		
		this.dtoCentroAtencion = dtoCentroAtencion;
		this.filtro = filtro;
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
		
		DTOGeneralReportePlanoPacientesEstadoPresupuesto dtoGeneral = new 
		DTOGeneralReportePlanoPacientesEstadoPresupuesto();
		
		Collection<DTOGeneralReportePlanoPacientesEstadoPresupuesto>  collectionDTOGeneral= 
			new ArrayList<DTOGeneralReportePlanoPacientesEstadoPresupuesto>();
		
		ArrayList<DTOReportePlanoDetallePacientesEstadoPresupuesto> listaReportePlano = 
			new ArrayList<DTOReportePlanoDetallePacientesEstadoPresupuesto>();		
		
		DTOReportePlanoDetallePacientesEstadoPresupuesto dtoReportePlano = null;		
		String[] cadena = dtoCentroAtencion.getNombreCompletoCentroAtencion().split("-");
		String[] cadenaPais =(cadena[1].trim()).split("\\("); 
		String nombrePais = (cadenaPais[cadenaPais.length-1]).substring(0, (cadenaPais[cadenaPais.length-1]).length()-1);
		
		DTOEstadosReportePacientesEstadoPresupuesto estado = listaEstado.get(0);
		
		if (estado.getListaPacientes() != null && estado.getListaPacientes().size()>0) {	
			
			for(DTOPacientesReportePacientesEstadoPresupuesto registro:estado.getListaPacientes()){				
				
				dtoReportePlano = new DTOReportePlanoDetallePacientesEstadoPresupuesto();
				String[] region = (cadena[2]).split(":");
				
				dtoReportePlano.setFechaModificacion(UtilidadFecha.conversionFormatoFechaAAp(
						registro.getFechaModificacionPresupuesto()));				
				dtoReportePlano.setEdadPaciente(registro.getEdadPaciente());
				dtoReportePlano.setSexoPaciente(registro.getSexoPaciente());
				dtoReportePlano.setEstadoPresupuesto(filtro.getNombreEstadoPresupuesto());
				dtoReportePlano.setNombreCentroAtencion(cadena[0]);
				dtoReportePlano.setNombreCiudad(cadenaPais[0]);
				dtoReportePlano.setNombreRegion(region[1].trim());				
				dtoReportePlano.setNombrePais(nombrePais);
				dtoReportePlano.setTipoID(registro.getTipoIdentificacion());
				dtoReportePlano.setNumeroID(registro.getNumeroIdentificacion());
				dtoReportePlano.setPrimerApellido(registro.getApellidoPaciente());
				dtoReportePlano.setSegundoApellido(registro.getSegundoApellidoPaciente());
				dtoReportePlano.setPrimerNombre(registro.getNombrePaciente());
				dtoReportePlano.setSegundoNombre(registro.getSegundoNombrePaciente());
				dtoReportePlano.setValorTotalPresupuesto(registro.getValorTotalPresupuesto().trim());
				dtoReportePlano.setNumeroPresupuesto(String.valueOf(registro.getNumeroPresupuesto()).trim());
				dtoReportePlano.setNombreCentroAtencionDuenio(registro.getCentroAtencionDueno());
				
				listaReportePlano.add(dtoReportePlano);											
			}
			dtoGeneral.setDsDetallePacientesEstadoPresupuesto(new JRBeanCollectionDataSource(listaReportePlano));
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
