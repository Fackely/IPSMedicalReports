package com.servinte.axioma.generadorReporte.odontologia.citaOdontologica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;
import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoImpresionCitaOdontologica;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoServicioImpresion;
import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.servicio.fabrica.odontologia.ventaTarjeta.VentaTarjetaServicioFabrica;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IBeneficiarioServicio;

/**
 * Clase que genera el Formato de impresión de la cita 
 * Odontológica
 *
 * @author Jorge Armando Agudelo Quintero
 * @since
 *
 */
public class ImpresionCitaOdontologica extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporte/citaOdontologicaPOS.jasper";
	private static String RUTA_SUBREPORTE_SERVICIOS = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporte/serviciosSubreporte.jasper";
	
	private InstitucionBasica institucionBasica;
	private ArrayList<DtoServicioOdontologico> servicios;
	private DtoCitaOdontologica citaOdontologica;

	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 */
	public ImpresionCitaOdontologica() {
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param dtoResultado
	 * @param dtoFiltro
	 *
	 * @author Yennifer Guerrero
	 * @param serviciosOdon 
	 * @param citaOdontologica 
	 */
	public ImpresionCitaOdontologica(InstitucionBasica institucionBasica, ArrayList<DtoServicioOdontologico> servicios, DtoCitaOdontologica citaOdontologica){	
		
		this.institucionBasica = institucionBasica;
		
		this.servicios = servicios;
		this.citaOdontologica = citaOdontologica;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoImpresionCitaOdontologica>  collectionDTOGeneral= new ArrayList();
		
		DtoImpresionCitaOdontologica dtoImpresionCitaOdontologica = cargarDatosEncabezado();

		cargarServicios(dtoImpresionCitaOdontologica);
		
		collectionDTOGeneral.add(dtoImpresionCitaOdontologica);
		
		return collectionDTOGeneral;
	}

	
	/**
	 * Método que carga la información del encabezado del formato de impresión
	 * @return
	 */
	private DtoImpresionCitaOdontologica cargarDatosEncabezado() {
		
		DtoImpresionCitaOdontologica dtoImpresionCitaOdontologica = new  DtoImpresionCitaOdontologica();	
		String nombreProfesional = "";
		String digitoVerificacion = !UtilidadTexto.isEmpty(institucionBasica.getDigitoVerificacion()) ? " - " + institucionBasica.getDigitoVerificacion() : "";
		
		dtoImpresionCitaOdontologica.setRazonSocial(institucionBasica.getRazonSocial());
		dtoImpresionCitaOdontologica.setNit(institucionBasica.getNit()  + digitoVerificacion );
		dtoImpresionCitaOdontologica.setCentroAtencion(citaOdontologica.getUsuarioRegistra().getCentroAtencion());
		dtoImpresionCitaOdontologica.setCodigoCita(citaOdontologica.getCodigoPk());
		dtoImpresionCitaOdontologica.setFechaCita(citaOdontologica.getFechaRegistra());
		dtoImpresionCitaOdontologica.setHoraCita(citaOdontologica.getHoraInicio());
		dtoImpresionCitaOdontologica.setNombrePaciente(citaOdontologica.getNombreCompletoPaciente());
		dtoImpresionCitaOdontologica.setIdentificacionPaciente(citaOdontologica.getNumeroIdentificacionPac());
		
		if (citaOdontologica.getEstado().trim().equals(ConstantesIntegridadDominio.acronimoAsignado)) {
			nombreProfesional = citaOdontologica.getAgendaOdon().getNombreMedico();
			dtoImpresionCitaOdontologica.setNombreEspecialidad(citaOdontologica.getAgendaOdon().getNombreEspecialidadUniAgen());
		}else{
			nombreProfesional = citaOdontologica.getNombreProfesional();
			dtoImpresionCitaOdontologica.setNombreEspecialidad(citaOdontologica.getNomEspeUconsulta());
		}
		
		if (nombreProfesional == null) {
			nombreProfesional = "";
		}
		
		dtoImpresionCitaOdontologica.setNombreProfesional(nombreProfesional);
			
		dtoImpresionCitaOdontologica.setTipoIdentificacion(citaOdontologica.getTipoIdentificacionPac());
		
		IBeneficiarioServicio servicioBeneficiario=VentaTarjetaServicioFabrica.crearBeneficiarioServicio();
		BeneficiarioTarjetaCliente beneficiario=servicioBeneficiario.obtenerBeneficiarioPaciente(citaOdontologica.getCodigoPaciente());
		
		if(beneficiario!=null && beneficiario.getNumTarjeta()!=null){
			
			dtoImpresionCitaOdontologica.setTarjeta(beneficiario.getNumTarjeta());
			
		}else if(beneficiario!=null && !"".equals(beneficiario.getSerial())){
			
			dtoImpresionCitaOdontologica.setTarjeta(beneficiario.getSerial()+"");
		
		}else{
			
			dtoImpresionCitaOdontologica.setTarjeta("");
		}
		
		return dtoImpresionCitaOdontologica;
	}
	
	
	/**
	 * Método que carga los servicios asociados a la cita
	 * 
	 * @param dtoImpresionCitaOdontologica
	 */
	private void cargarServicios(DtoImpresionCitaOdontologica dtoImpresionCitaOdontologica) {
		
		dtoImpresionCitaOdontologica.setImprimirColumHeader(false);
		dtoImpresionCitaOdontologica.setImprimirDetalle(false);
		
		/*
		 * Arreglo con los servicios asociados a la cita
		 */
		ArrayList<DtoServicioImpresion> serviciosImpresion = new ArrayList<DtoServicioImpresion>();
		
		/*
		 * Información de los Servicios
		 */
		
		String utilizaProgramasOdontologicos = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(citaOdontologica.getUsuarioRegistra().getCodigoInstitucionInt());
		String aplicaA= "";
	
		String superficies= "";
		
		for (DtoServicioOdontologico servicio : servicios) {
			
			if(ConstantesBD.acronimoSi.equals(servicio.getAsociarSerCita())){
				
				DtoServicioImpresion servicioImpresion = new DtoServicioImpresion();
				
				servicioImpresion.setNombreServicio(servicio.getDescripcionServicio());
				//servicioImpresion.setCodigoServicio(servicio.getIndicativo());
				
				if(utilizaProgramasOdontologicos.equals(ConstantesBD.acronimoSi)){
				
					aplicaA= "";
					
					if(citaOdontologica.getTipo().equals(ConstantesIntegridadDominio.acronimoTratamiento)){
						
						if(servicio.getSeccionPlanTrata().equals(ConstantesIntegridadDominio.acronimoBoca)){
							
							aplicaA += ConstantesIntegridadDominio.acronimoBoca + " " + servicio.getDespPiezaDental();
						
						}else{
							
							aplicaA += ConstantesIntegridadDominio.acronimoDiente + " " + servicio.getDespPiezaDental();
						}
					}
				
					servicioImpresion.setAplicaA(aplicaA);

				}else{
				
					superficies= "";
					
			    	for(DtoSuperficiesPorPrograma superficie:servicio.getSuperficies()){
			    		
			    		superficies=superficies+(!superficies.isEmpty() ? " - ": "");
			    	 	superficies=superficies+superficie.getDescripcionSuperficie();
			    	}
					
			    	//superficies = servicio.getSeccionPlanTrata().equals(ConstantesIntegridadDominio.acronimoBoca) ? ConstantesIntegridadDominio.acronimoBoca : servicio.getDespPiezaDental();
					
			    	servicioImpresion.setAplicaA(superficies);
				}
				
				serviciosImpresion.add(servicioImpresion);
			}
		}
		
		if(serviciosImpresion.size() > 0){
			
			dtoImpresionCitaOdontologica.setImprimirColumHeader(true);
			dtoImpresionCitaOdontologica.setImprimirDetalle(true);
		}
		
		dtoImpresionCitaOdontologica.setDsServicios(new JRBeanCollectionDataSource(serviciosImpresion));
	}
	
	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		
//		ClassLoader loader = this.getClass().getClassLoader();
//		InputStream myInFile = null;
		
		parametrosReporte.put("SUBREPORT_DIR", RUTA_SUBREPORTE_SERVICIOS);
		 
//		try {
//			
//			myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_SERVICIOS);
//			
//	        if (myInFile == null) {
//	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_SERVICIOS);
//	           	
//			}else if (myInFile != null) {
//	            Object mySubreportObj = JRLoader.loadObject(myInFile);
//	            parametrosReporte.put(RUTA_SUBREPORTE_SERVICIOS, mySubreportObj);
//	            myInFile.close();
//	            myInFile=null;
//	        }
//	        
//		}catch (Exception e) {
//			
//			e.printStackTrace();
//		}
			
	    return parametrosReporte;
	}
	

	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}

}
