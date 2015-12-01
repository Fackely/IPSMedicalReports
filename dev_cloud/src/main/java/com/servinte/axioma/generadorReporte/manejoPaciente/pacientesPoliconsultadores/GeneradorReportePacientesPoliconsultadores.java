package com.servinte.axioma.generadorReporte.manejoPaciente.pacientesPoliconsultadores;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import util.ConstantesIntegridadDominio;
import util.Utilidades;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores;
import com.servinte.axioma.dto.manejoPaciente.PacientePoliconsultadoresPlanoDto;

public class GeneradorReportePacientesPoliconsultadores extends GeneradorReporte {

	private static String RUTA_REPORTE_GENERAL 					= 	"com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/ReportePacientesPoliconsultadores.jasper";
	private static String RUTA_SUBREPORTE_CONVENIO 				= 	"com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/subReporteConvenio.jasper";
	private static String NOMBRE_SUBREPORTE_CONVENIO			= 	"subReporteConvenio";
	private static String RUTA_SUBREPORTE_DETALLES 				=	"com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/subReporteDetalles.jasper";
	private static String NOMBRE_SUBREPORTE_DETALLES			=	"subReporteDetalles";
	private static String RUTA_SUBREPORTE_DETALLES_EXTERNA 		= 	"com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/subReporteDetallesExterna.jasper";
	private static String NOMBRE_SUBREPORTE_DETALLES_EXTERNA 	= 	"subReporteDetallesExterna";
	private static String RUTA_SUBREPORTE_DETALLES_SIMPLE 		=	"com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/subReporteDetallesSimple.jasper";
	private static String NOMBRE_SUBREPORTE_DETALLES_SIMPLE		=	"subReporteDetallesSimple";
	private static String RUTA_SUBREPORTE_PACIENTES_PLANO	=	"com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/subReportePaciente.jasper";
	private static String NOMBRE_SUBREPORTE_PACIENTES_PLANO		=	"subReportePaciente";
	
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private DtoPacientesPoliconsultadores dtoResultadoOrden;
	private PacientePoliconsultadoresPlanoDto dtoResultadoOrdenPlano;
	private String tipoSalida = null;
	
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	
	
	/**
	 * constructor de la clase 
	 * @param dtoResultado
	 * @author César Gómez
	 */
	public GeneradorReportePacientesPoliconsultadores(DtoPacientesPoliconsultadores dtoResultado, String tipoSalida){
		this.dtoResultadoOrden  = dtoResultado;
		this.tipoSalida = tipoSalida;
	}
	
	public GeneradorReportePacientesPoliconsultadores(PacientePoliconsultadoresPlanoDto dtoResultadoPlano, String tipoSalida){
		this.dtoResultadoOrdenPlano = dtoResultadoPlano;
		this.tipoSalida = tipoSalida;
	}
	
	
	/**
	 * constructor de la clase
	 * @author César Gómez
	 */
	public GeneradorReportePacientesPoliconsultadores(){
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Collection obtenerDatosReporte() {
		Collection<DtoPacientesPoliconsultadores>  collectionDTOGeneral= new ArrayList<DtoPacientesPoliconsultadores>(0);
		Collection<PacientePoliconsultadoresPlanoDto> collectionDtoPlano = new ArrayList<PacientePoliconsultadoresPlanoDto>(0);
		DtoPacientesPoliconsultadores dtoGeneral = new  DtoPacientesPoliconsultadores();	
		
		if (tipoSalida.equals("1")||tipoSalida.equals("3")) {
		
		RUTA_LOGO = "../"+ dtoResultadoOrden.getRutaLogo();
		
		if (dtoResultadoOrden != null) 
		{
			String ubicacionLogo = dtoResultadoOrden.getUbicacionLogo();
			String rutaLogo = "../"+dtoResultadoOrden.getRutaLogo();
	
			if(!Utilidades.isEmpty(dtoResultadoOrden.getListaConvenios()))
			{
				dtoGeneral.setSaltoPaginaReporte(true);
			}
			
			dtoGeneral.setRazonSocial(dtoResultadoOrden.getRazonSocial());
			dtoGeneral.setInstitucion(dtoResultadoOrden.getInstitucion());
			dtoGeneral.setNit(dtoResultadoOrden.getNit());
			dtoGeneral.setActividadEconomica(dtoResultadoOrden.getActividadEconomica());
			dtoGeneral.setCentroAtencion(dtoResultadoOrden.getCentroAtencion());
			dtoGeneral.setDireccion(dtoResultadoOrden.getDireccion());
			dtoGeneral.setTelefono(dtoResultadoOrden.getTelefono());
			dtoGeneral.setTipoReporte(dtoResultadoOrden.getTipoReporte());
			dtoGeneral.setUsuario(dtoResultadoOrden.getUsuario());
			dtoGeneral.setTipoImpresion(dtoResultadoOrden.getTipoImpresion());	
			dtoGeneral.setFechaInicial(dtoResultadoOrden.getFechaInicial());
			dtoGeneral.setFechaFinal(dtoResultadoOrden.getFechaFinal());
			dtoGeneral.setCantidadServicios(dtoResultadoOrden.getCantidadServicios());
			dtoGeneral.setListaConvenios(dtoResultadoOrden.getListaConvenios());
			dtoGeneral.setDtoDetalles(dtoResultadoOrden.getDtoDetalles());			
			
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
			List<DtoPacientesPoliconsultadores> listaFinalConvenios=new ArrayList<DtoPacientesPoliconsultadores>(0);
			String cantidad=dtoGeneral.getCantidadServicios();
			Integer cantidadEnt = null;
			if(cantidad!=null && !cantidad.trim().isEmpty()){
					cantidadEnt=Integer.parseInt(cantidad);
			}
			for( int i = 0; i < dtoGeneral.getListaConvenios().size(); i++ ){
					dtoGeneral.getListaConvenios().get(i).setJRDDetalles( new JRBeanCollectionDataSource( dtoResultadoOrden.getListaConvenios().get(i).getListaDetalles() ) );
					int count=0;
						
					for( int j = 0; j < dtoGeneral.getListaConvenios().get(i).getListaDetalles().size(); j++ ){
						dtoGeneral.getListaConvenios().get(i).getListaDetalles().get(j).setJRDUnidades(new JRBeanCollectionDataSource( dtoResultadoOrden.getListaConvenios().get(i).getListaDetalles().get(j).getListaUnidades() ) );
							count+=dtoGeneral.getListaConvenios().get(i).getListaDetalles().get(j).getCantidadIngresos();
							
					}
					
					
					if(cantidadEnt!=null&&count>=cantidadEnt.intValue()){
						listaFinalConvenios.add(dtoGeneral.getListaConvenios().get(i));
					}else{
						if(cantidadEnt==null){
							listaFinalConvenios.add(dtoGeneral.getListaConvenios().get(i));
						}
					}
				}
				dtoGeneral.setJRDConvenio( new JRBeanCollectionDataSource( listaFinalConvenios ) );
				
			}
			collectionDTOGeneral.add(dtoGeneral);
			return collectionDTOGeneral;
		}else{
			collectionDtoPlano.add(dtoResultadoOrdenPlano);
			return collectionDtoPlano;			
		}
		
	}
	
	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {		
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        
        try {
	    	if(tipoSalida.equals("1")||tipoSalida.equals("3")){
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CONVENIO);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CONVENIO);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_CONVENIO, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_DETALLES);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_DETALLES);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_DETALLES, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_DETALLES_EXTERNA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_DETALLES_EXTERNA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_DETALLES_EXTERNA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_DETALLES_SIMPLE);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_DETALLES_SIMPLE);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_DETALLES_SIMPLE, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	    	}else{
	    		if(tipoSalida.equals("2")){
			        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PACIENTES_PLANO);
			        if (myInFile == null) {
			           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PACIENTES_PLANO);
			           	
					}else if (myInFile != null) {
			            Object mySubreportObj = JRLoader.loadObject(myInFile);
			            parametrosReporte.put(NOMBRE_SUBREPORTE_PACIENTES_PLANO, mySubreportObj);
			            myInFile.close();
			            myInFile=null;
			        }
	    		}
	    	}
	        
	        /**FIXME ------->AQUI VA CARGA DE LOS SUBREPORTES*/	        
		}catch (Exception e) {
			e.printStackTrace();
		}
				
		return parametrosReporte;
	}

	@Override
	public String obtenerRutaPlantilla() {			
		if (tipoSalida.equals("2")) {
			RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/ReportePacientesPoliconsultadoresPlano.jasper";
		} else {
			RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/manejoPaciente/pacientesPoliconsultadores/ReportePacientesPoliconsultadores.jasper";
		}
		return RUTA_REPORTE_GENERAL;
	}
}
