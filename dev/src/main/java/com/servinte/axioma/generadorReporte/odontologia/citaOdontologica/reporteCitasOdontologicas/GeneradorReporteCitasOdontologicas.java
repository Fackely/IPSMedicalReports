package com.servinte.axioma.generadorReporte.odontologia.citaOdontologica.reporteCitasOdontologicas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.GeneradorReporte;

import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoTipoCitaEstadoCita;

public class GeneradorReporteCitasOdontologicas extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private ArrayList<DtoResultadoReporteCitasOdontologicas> listadoResultado;
    private DtoFiltroReporteCitasOdontologicas filtro;
    private String rutaLogo = "";
	private String NOMBRE_LOGO = "nombreLogo";
	
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/reporteCitasOdonto.jasper";  
    
    private static String RUTA_SUBRPT_INSTITUCION = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/subrptInstitucion.jasper";
    private static String NOMBRE_SUBRPT_INSTITUCION = "subrptInstitucion";
    
    private static String RUTA_SUBRPT_CENTROS_ATENCION = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/subrptCentrosAtencion.jasper";
    private static String NOMBRE_SUBRPT_CENTROS_ATENCION = "subrptCentrosAtencion";
    
    private static String RUTA_SUBRPT_ESPECIALIDADES = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/subrptEspecialidades.jasper";
    private static String NOMBRE_SUBRPT_ESPECIALIDADES = "subrptEspecialidades";
	
    private static String RUTA_SUBRPT_CITAS_CON_CANCELACION = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/subrptCitasConCancelacion.jasper";
	private static String NOMBRE_SUBRPT_CITAS_CON_CANCELACION = "subrptCitasConCancelacion";
    
    private static String RUTA_SUBRPT_SERVICIOS = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/subrptServicios.jasper";
	private static String NOMBRE_SUBRPT_SERVICIOS = "subrptServicios";
   
	private static String RUTA_SUBRPT_CITAS_SIN_CANCEL = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/subrptCitasSinCancelacion.jasper";
	private static String NOMBRE_SUBRPT_CITAS_SIN_CANCEL = "subrptCitasSinCancelacion";
   
	private static String RUTA_SUBRPT_CITAS_RESUMIDO = "com/servinte/axioma/generadorReporte/odontologia/citaOdontologica/reporteCitasOdontologicas/subrptCitasOdontoResumido.jasper";
	private static String NOMBRE_SUBRPT_CITAS_RESUMIDO = "subrptCitasOdontoResumido";
   
	
	
	
	/**
	 * Mètodo constructor de la clase
	 *
	 * @author Yennifer Guerrero
	 */
    public GeneradorReporteCitasOdontologicas() {
	}
    
    /**
     * Mètodo constructor de la clase
     * @param listadoResultado
     * @param filtro
     *
     * @author Yennifer Guerrero
     */
    public GeneradorReporteCitasOdontologicas(ArrayList<DtoResultadoReporteCitasOdontologicas> listadoResultado,
    		DtoFiltroReporteCitasOdontologicas filtro) {
    	
    	this.listadoResultado = listadoResultado;
    	this.filtro = filtro;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoResultadoReporteCitasOdontologicas>  collectionDTOGeneral= new ArrayList();
		
		DtoResultadoReporteCitasOdontologicas dtoGeneral = new  DtoResultadoReporteCitasOdontologicas();	
		rutaLogo = "../"+ filtro.getRutaLogo();
		ArrayList<DtoFiltroReporteCitasOdontologicas> listadoFiltro = new ArrayList<DtoFiltroReporteCitasOdontologicas>();
		
		if (listadoResultado != null) {
			
			if (filtro != null) {
				
				String ubicacionLogo = filtro.getUbicacionLogo();
				String rutaLogo = "../"+filtro.getRutaLogo();
				String servicios= "";
				String tiposCita="";
				String estadosCita="";
				String canceladoPor;
				String prof=(UtilidadTexto.isEmpty(filtro.getProfesional().getPrimerNombre())?"":filtro.getProfesional().getPrimerNombre())+" "+(UtilidadTexto.isEmpty(filtro.getProfesional().getPrimerApellido())?"":filtro.getProfesional().getPrimerApellido());
				prof = UtilidadTexto.isEmpty(prof)?"Todos":prof;
				
				listadoFiltro.add(filtro);
		
				if (filtro.getServicios() != null && filtro.getServicios().size()>0) {
					for (int i = 0; i < filtro.getServicios().size(); i++) {
						servicios += filtro.getServicios().get(i).getDescripcionPropietarioServicio();
					}
				}else if (filtro.getServicios().size()== 0) {
					servicios+= "Todos";
				}
				
				if(filtro.getAyudanteTiposCita().length==0)
				{
					tiposCita="Todos";
				}
				else
				{
					for(int i=0;i<filtro.getAyudanteTiposCita().length;i++)
					{
						tiposCita=tiposCita + filtro.getAyudanteTiposCita()[i] + " ";
					}
				}
				
				if(filtro.getAyudanteEstadosCita().length==0)
				{
					estadosCita="Todos";
				}
				else
				{
					for(int i=0;i<filtro.getAyudanteEstadosCita().length;i++)
					{
						estadosCita=estadosCita+filtro.getAyudanteEstadosCita()[i] + " ";
					}
				}
				
				if(filtro.getCanceladaPor()==ConstantesBD.codigoEstadoCitaCanceladaPaciente)
				{
					canceladoPor="Paciente";
				}
				else if(filtro.getCanceladaPor()==ConstantesBD.codigoEstadoCitaCanceladaInstitucion)
				{
					canceladoPor="Institucion";
				}
				else
				{
					canceladoPor="Ambos";
				}
				
				dtoGeneral.setFechaInicial(filtro.getFechaInicialFormateado());
				dtoGeneral.setFechaFinal(filtro.getFechaFinalFormateado());
				dtoGeneral.setRazonSocial(filtro.getRazonSocial());
				dtoGeneral.setUsuario(filtro.getUsuario().getLogin());
				
				if (UtilidadTexto.isEmpty(filtro.getEspecialidad().getDescripcion())) {
					dtoGeneral.setEspecialidad("Todas");
				}else{
					dtoGeneral.setEspecialidad(filtro.getEspecialidad().getDescripcion());
				}
				
				if (UtilidadTexto.isEmpty(filtro.getUnidadAgenda().getDescripcion())) {
					dtoGeneral.setUnidadAgenda("Todas");
				}else{
					dtoGeneral.setUnidadAgenda(filtro.getUnidadAgenda().getDescripcion());
				}
				
				if (UtilidadTexto.isEmpty(servicios)) {
					dtoGeneral.setServicios(servicios);
				}else{
					dtoGeneral.setServicios(servicios);
				}
				
				dtoGeneral.setTiposCita(tiposCita);
				dtoGeneral.setEstadosCita(estadosCita);				
				dtoGeneral.setProfesional(prof);
				dtoGeneral.setCanceladasPor(canceladoPor);
				
				if (UtilidadTexto.isEmpty(filtro.getCancelacionCita())) {
					dtoGeneral.setConCancelacion("Si");
				}else{
					dtoGeneral.setConCancelacion("Todas");
				}
				
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
			
			for (DtoResultadoReporteCitasOdontologicas registro : listadoResultado) {				
				registro.setDsCentrosAtencion(new JRBeanCollectionDataSource(registro.getCentrosAtencion()));
			
				for (com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte dto : registro.getCentrosAtencion()) {
					dto.setDsEspecialidades(new JRBeanCollectionDataSource(dto.getEspecialidades()));
					
					for (DtoEspecialidadReporte esp : dto.getEspecialidades()) {
						
						if (filtro.getTipoReporte() == 2) {
							esp.setEsResumido(false);
							dtoGeneral.setEsResumido(false);
							esp.setDsCitasConCancelacionInstitucion(new JRBeanCollectionDataSource(esp.getCitasConCancelacionInstitucion()));
							esp.setDsCitasConCancelacionPaciente(new JRBeanCollectionDataSource(esp.getCitasConCancelacionPaciente()));
							esp.setDsCitasSinCancelacion(new JRBeanCollectionDataSource(esp.getCitasSinCancelacion()));
							esp.setDsCitasSinEspecialidad(new JRBeanCollectionDataSource(esp.getCitasSinEspecialidad()));
							
							
							for (DtoCitasReporte cancelInst : esp.getCitasConCancelacionInstitucion()) {
								cancelInst.setDsServicios(new JRBeanCollectionDataSource(cancelInst.getServicios()));
							}
							
							for (DtoCitasReporte cancelPac : esp.getCitasConCancelacionPaciente()) {
								cancelPac.setDsServicios(new JRBeanCollectionDataSource(cancelPac.getServicios()));
							}
							
							for (DtoCitasReporte sinCancel : esp.getCitasSinCancelacion()) {
								sinCancel.setDsServicios(new JRBeanCollectionDataSource(sinCancel.getServicios()));
							}
							
							for (DtoCitasReporte sinEsp : esp.getCitasSinEspecialidad()) {
								sinEsp.setDsServicios(new JRBeanCollectionDataSource(sinEsp.getServicios()));
							}
							
						}else if (filtro.getTipoReporte() == 1) {
							esp.setEsResumido(true);
							dtoGeneral.setEsResumido(true);
							esp.setDsResumenCitas(new JRBeanCollectionDataSource(esp.getResumenCitas()));
							
							int totalCitasEsp = 0;
							
							for (DtoTipoCitaEstadoCita resumen : esp.getResumenCitas()) {
								resumen.setAyudanteTipoCita(ValoresPorDefecto.getIntegridadDominio(resumen.getTipoCita()).toString());
								resumen.setAyudanteEstadoCita(ValoresPorDefecto.getIntegridadDominio(resumen.getEstadoCita()).toString());
								resumen.setDescripcionEspecialidad(esp.getDescripcionEspecialidad());
								totalCitasEsp += resumen.getNumeroCitas(); 
							}
							
							esp.setTotalCitasEspecialidad(totalCitasEsp);
						}
						
						
					}
				}
			}
			
			dtoGeneral.setDsListadoResultado(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}
	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        

        try {
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_INSTITUCION);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_INSTITUCION);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_INSTITUCION, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_CENTROS_ATENCION);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_CENTROS_ATENCION);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_CENTROS_ATENCION, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_ESPECIALIDADES);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_ESPECIALIDADES);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_ESPECIALIDADES, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_CITAS_CON_CANCELACION);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_CITAS_CON_CANCELACION);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_CITAS_CON_CANCELACION, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	       myInFile = loader.getResourceAsStream(RUTA_SUBRPT_SERVICIOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_SERVICIOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_SERVICIOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_CITAS_SIN_CANCEL);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_CITAS_SIN_CANCEL);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_CITAS_SIN_CANCEL, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_CITAS_RESUMIDO);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_CITAS_RESUMIDO);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_CITAS_RESUMIDO, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}

	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}
	
	

}
