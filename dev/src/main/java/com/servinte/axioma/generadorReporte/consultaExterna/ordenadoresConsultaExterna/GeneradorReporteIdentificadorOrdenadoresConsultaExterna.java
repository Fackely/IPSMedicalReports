/**
 * 
 */
package com.servinte.axioma.generadorReporte.consultaExterna.ordenadoresConsultaExterna;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesIntegridadDominio;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.reportes.GeneradorReporte;

import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto;

/**
 * Se encarga de generar el reporte para identificar Ordenadores de Consulta Externa
 * 
 * @author jeilones
 * @created 13/11/2012
 *
 */
public class GeneradorReporteIdentificadorOrdenadoresConsultaExterna extends
		GeneradorReporte {

	private GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto;
	
	/** Parametros del reporte.*/
    private Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/reporteIdenOrdenadoresConsulta.jasper";
	
	private static String RUTA_SUBREPORTE_PROFESIONALES = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/subReporteProfesionales.jasper";
	private static String NOMBRE_SUBREPORTE_PROFESIONALES = "subReporteProfesionales";
	private static String RUTA_SUBREPORTE_UNIDADES_CONSULTA = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/subReporteUnidadesConsulta.jasper";
	private static String NOMBRE_SUBREPORTE_UNIDADES_CONSULTA = "subReporteUnidadesConsulta";
	private static String RUTA_SUBREPORTE_ORDENADORES = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/subReporteOrdenadoresConsultaExterna.jasper";
	private static String NOMBRE_SUBREPORTE_ORDENADORES = "subReporteOrdenadoresConsultaExterna";
	
	private static String RUTA_SUBREPORTE_GRUPOS_SERVICIO= "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/subReporteGruposServicio.jasper";
	private static String NOMBRE_SUBREPORTE_GRUPOS_SERVICIO= "subReporteGruposServicio";
	private static String RUTA_SUBREPORTE_CLASES_INVENTARIO= "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/subReporteClasesInventario.jasper";
	private static String NOMBRE_SUBREPORTE_CLASES_INVENTARIO= "subReporteClasesInventario";
	
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	
	/**
	 * 
	 * @author jeilones
	 * @created 13/11/2012
	 */
	public GeneradorReporteIdentificadorOrdenadoresConsultaExterna(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto) {
		this.grupoOrdenadoresConsultaExternaDto=grupoOrdenadoresConsultaExternaDto;
	}

	/* (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerDatosReporte()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Collection obtenerDatosReporte() {
		Collection<GrupoOrdenadoresConsultaExternaDto>  collectionDTOGeneral= new ArrayList<GrupoOrdenadoresConsultaExternaDto>();
		
		RUTA_LOGO = "../"+ grupoOrdenadoresConsultaExternaDto.getRutaLogo();
		
		String ubicacionLogo = grupoOrdenadoresConsultaExternaDto.getUbicacionLogo();
		String rutaLogo = "../"+grupoOrdenadoresConsultaExternaDto.getRutaLogo();
		
		boolean existeLogo = existeLogo(rutaLogo);
		
		if (existeLogo) {
			if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
				grupoOrdenadoresConsultaExternaDto.setLogoDerecha(rutaLogo);
				grupoOrdenadoresConsultaExternaDto.setLogoIzquierda(null);
			} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
				grupoOrdenadoresConsultaExternaDto.setLogoIzquierda(rutaLogo);
				grupoOrdenadoresConsultaExternaDto.setLogoDerecha(null);
			}
		}else{
			grupoOrdenadoresConsultaExternaDto.setLogoDerecha(null);
			grupoOrdenadoresConsultaExternaDto.setLogoIzquierda(null);
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.HOJA_CALCULO.getCodigo()
				||grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.PDF.getCodigo()){
			if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf){
				grupoOrdenadoresConsultaExternaDto.setGrupoProfesionales(new JRBeanCollectionDataSource(grupoOrdenadoresConsultaExternaDto.getProfesionales(),false));
			}else{
				if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase){
					grupoOrdenadoresConsultaExternaDto.setGrupoGruposServicio(new JRBeanCollectionDataSource(grupoOrdenadoresConsultaExternaDto.getGruposServicio(),false));
					grupoOrdenadoresConsultaExternaDto.setGrupoClasesInventario(new JRBeanCollectionDataSource(grupoOrdenadoresConsultaExternaDto.getClasesInventario(),false));
				}
			}
		}else{
			if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.PLANO.getCodigo()){
				grupoOrdenadoresConsultaExternaDto.setGrupoOrdenadoresConsultaExternaPlano(new JRBeanCollectionDataSource(grupoOrdenadoresConsultaExternaDto.getOrdenadoresConsultaExternaPlano(),false));
			}
		}
		collectionDTOGeneral.add(grupoOrdenadoresConsultaExternaDto);
		
		
		return collectionDTOGeneral;
	}

	/* (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerDatosAdicionalesReporte()
	 */
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		try {
			ClassLoader loader = this.getClass().getClassLoader();
			InputStream myInFile = null;
			
			Locale locale = new Locale("es_COL");
			parametrosReporte.put(JRParameter.REPORT_LOCALE, locale);
			
			if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.HOJA_CALCULO.getCodigo()
					||grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.PDF.getCodigo()){
		
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PROFESIONALES);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PROFESIONALES);
		           	
				}else if (myInFile != null) {
					Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_PROFESIONALES, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }
		        
		        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_UNIDADES_CONSULTA);
		        if (myInFile == null) {
		           	myInFile = new FileInputStream(RUTA_SUBREPORTE_UNIDADES_CONSULTA);
		           	
				}else if (myInFile != null) {
					Object mySubreportObj = JRLoader.loadObject(myInFile);
		            parametrosReporte.put(NOMBRE_SUBREPORTE_UNIDADES_CONSULTA, mySubreportObj);
		            myInFile.close();
		            myInFile=null;
		        }
		        
		        if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf){
			        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ORDENADORES);
			        if (myInFile == null) {
			           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ORDENADORES);
			           	
					}else if (myInFile != null) {
						Object mySubreportObj = JRLoader.loadObject(myInFile);
			            parametrosReporte.put(NOMBRE_SUBREPORTE_ORDENADORES, mySubreportObj);
			            myInFile.close();
			            myInFile=null;
			        }
		        }else{
		        	if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase){
		        		myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_GRUPOS_SERVICIO);
				        if (myInFile == null) {
				           	myInFile = new FileInputStream(RUTA_SUBREPORTE_GRUPOS_SERVICIO);
				           	
						}else if (myInFile != null) {
							Object mySubreportObj = JRLoader.loadObject(myInFile);
				            parametrosReporte.put(NOMBRE_SUBREPORTE_GRUPOS_SERVICIO, mySubreportObj);
				            myInFile.close();
				            myInFile=null;
				        }
				        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CLASES_INVENTARIO);
				        if (myInFile == null) {
				           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CLASES_INVENTARIO);
				           	
						}else if (myInFile != null) {
							Object mySubreportObj = JRLoader.loadObject(myInFile);
				            parametrosReporte.put(NOMBRE_SUBREPORTE_CLASES_INVENTARIO, mySubreportObj);
				            myInFile.close();
				            myInFile=null;
				        }
		        	}
		        }
			}else{
				if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.PLANO.getCodigo()){
					myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ORDENADORES);
			        if (myInFile == null) {
			           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ORDENADORES);
			           	
					}else if (myInFile != null) {
						Object mySubreportObj = JRLoader.loadObject(myInFile);
			            parametrosReporte.put(NOMBRE_SUBREPORTE_ORDENADORES, mySubreportObj);
			            myInFile.close();
			            myInFile=null;
			        }
				}
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
        return parametrosReporte;
	}

	/* (non-Javadoc)
	 * @see util.reportes.GeneradorReporte#obtenerRutaPlantilla()
	 */
	@Override
	public String obtenerRutaPlantilla() {
		if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.HOJA_CALCULO.getCodigo()
			||grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.PDF.getCodigo()){
			if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf){
				RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/reporteIdenOrdenadoresConsulta.jasper";
				
				RUTA_SUBREPORTE_PROFESIONALES = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/subReporteProfesionales.jasper";
				NOMBRE_SUBREPORTE_PROFESIONALES = "subReporteProfesionales";
				
				RUTA_SUBREPORTE_UNIDADES_CONSULTA = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/subReporteUnidadesConsulta.jasper";
				NOMBRE_SUBREPORTE_UNIDADES_CONSULTA = "subReporteUnidadesConsulta";
				
				RUTA_SUBREPORTE_ORDENADORES = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/subReporteOrdenadoresConsultaExterna.jasper";
				NOMBRE_SUBREPORTE_ORDENADORES = "subReporteOrdenadoresConsultaExterna";
			}else{
				if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase){
					RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/reporteIdenOrdenadoresConsulta.jasper";
					
					RUTA_SUBREPORTE_GRUPOS_SERVICIO = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/subReporteGruposServicio.jasper";
					NOMBRE_SUBREPORTE_GRUPOS_SERVICIO = "subReporteGruposServicio";
					
					RUTA_SUBREPORTE_CLASES_INVENTARIO = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/subReporteClasesInventario.jasper";
					NOMBRE_SUBREPORTE_CLASES_INVENTARIO = "subReporteClasesInventario";
					
					RUTA_SUBREPORTE_UNIDADES_CONSULTA = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/subReporteUnidadesConsulta.jasper";
					NOMBRE_SUBREPORTE_UNIDADES_CONSULTA = "subReporteUnidadesConsulta";
					
					RUTA_SUBREPORTE_PROFESIONALES = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/subReporteProfesionales.jasper";
					NOMBRE_SUBREPORTE_PROFESIONALES = "subReporteProfesionales";
				}
			}
		}else{
			if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoImpresion()==EnumTiposSalida.PLANO.getCodigo()){
				if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf){
					RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/reporteIdenOrdenadoresConsultaPlano.jasper";
					
					RUTA_SUBREPORTE_ORDENADORES = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/subReporteOrdenadoresConsultaExternaPlano.jasper";
					NOMBRE_SUBREPORTE_ORDENADORES = "subReporteOrdenadoresConsultaExternaPlano";
				}else{
					if(grupoOrdenadoresConsultaExternaDto.getCodigoTipoReporte()==ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase){
						RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/reporteArchivoPlano/reporteIdenOrdenadoresConsultaPlano.jasper";
						
						RUTA_SUBREPORTE_ORDENADORES = "com/servinte/axioma/generadorReporte/consultaExterna/ordenadoresConsultaExterna/gruposServicioClaseInventario/reporteArchivoPlano/subReporteOrdenadoresConsultaExternaPlano.jasper";
						NOMBRE_SUBREPORTE_ORDENADORES = "subReporteOrdenadoresConsultaExternaPlano";
						
					}
				}
			}
		}
		return RUTA_REPORTE_GENERAL;
	}

	/**
	 * @return the grupoOrdenadoresConsultaExternaDto
	 */
	public GrupoOrdenadoresConsultaExternaDto getGrupoOrdenadoresConsultaExternaDto() {
		return grupoOrdenadoresConsultaExternaDto;
	}

	/**
	 * @param grupoOrdenadoresConsultaExternaDto the grupoOrdenadoresConsultaExternaDto to set
	 */
	public void setGrupoOrdenadoresConsultaExternaDto(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto) {
		this.grupoOrdenadoresConsultaExternaDto = grupoOrdenadoresConsultaExternaDto;
	}

}
