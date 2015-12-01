package com.servinte.axioma.generadorReporte.odontologia.ingresosOdontologicos;

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
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteIngresoOdonto;

public class GeneradorReporteIngresosOdontoArchivoPlano extends GeneradorReporte {

	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
	private ArrayList<DtoResultadoConsultaIngresosOdonto> listadoResultado;
	private DtoReporteIngresosOdontologicos filtroIngresos;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/reporteIngresoOdontoArchivoPlano.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTAINGRESOS = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subreporteListadoResultadoPlano.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTAINGRESOS = "subreporteListadoResultadoPlano";
	private static String RUTA_SUBREPORTE_INGRESOSCONSINVALINI = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subrptIngresosConSinValIniPlano.jasper";
	private static String NOMBRE_SUBREPORTE_INGRESOSCONSINVALINI = "subrptIngresosConSinValIniPlano";
	private static String RUTA_SUBREPORTE_CONSOLIDADOINGRESOS = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subrptConsolidadoIngresosOdontoPlano.jasper";
	private static String NOMBRE_SUBREPORTE_CONSOLIDADOINGRESOS = "subrptConsolidadoIngresosOdontoPlano";
	private static String RUTA_SUBREPORTE_TOTALPRESUPUESTOS = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subrptTotalPorEstadoPlano.jasper";
	private static String NOMBRE_SUBREPORTE_TOTALPRESUPUESTOS = "subrptTotalPorEstadoPlano";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteIngresosOdontoArchivoPlano() {
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param dtoResultado
	 * @param dtoFiltro
	 *
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteIngresosOdontoArchivoPlano(ArrayList<DtoResultadoConsultaIngresosOdonto> listadooResultado, 
			DtoReporteIngresosOdontologicos filtroIngresos){	
		
		this.listadoResultado = listadooResultado;
		this.filtroIngresos = filtroIngresos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoReporteIngresoOdonto>  collectionDTOGeneral= new ArrayList();
		
		DtoReporteIngresoOdonto dtoGeneral = new  DtoReporteIngresoOdonto();	
		
		
		if (listadoResultado != null) {
			
			if (filtroIngresos != null) {
				
				dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateado());
				dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateado());
			}
			
			for (DtoResultadoConsultaIngresosOdonto registro : listadoResultado) {
				
				if (!Utilidades.isEmpty(registro.getListaIngresosConValIni())) {
					for (DtoIngresosOdontologicos conValini : registro.getListaIngresosConValIni()) {
						conValini.setConValoracion("Con VAL");
						
						String sexo = conValini.getSexoPaciente().toUpperCase();
						String sexoFemenino= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoFemenino).toString().toUpperCase();
						String sexoMasculino= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoMasculino).toString().toUpperCase();
						
						if (sexo.trim().equals(sexoFemenino)) {
							conValini.setSexoPaciente(ConstantesIntegridadDominio.acronimoFemenino);
						}else if (sexo.trim().equals(sexoMasculino)) {
							conValini.setSexoPaciente(ConstantesIntegridadDominio.acronimoMasculino);
						}
					}
					
					registro.setDsIngresosConValIni(new JRBeanCollectionDataSource(registro.getListaIngresosConValIni()));
				}
				
				if (!Utilidades.isEmpty(registro.getListaIngresosSinValIni())) {
					for (DtoIngresosOdontologicos sinValini : registro.getListaIngresosSinValIni()) {
						sinValini.setConValoracion("Sin VAL");
						
						if (sinValini.getTelefono().trim().equals("0")) {
							sinValini.setTelefono("-");
						}
						if (sinValini.getTelefonoCelularString().trim().equals("0")) {
							sinValini.setTelefonoCelularString("-");
						}
						
						String sexo = sinValini.getSexoPaciente().toUpperCase();
						String sexoFemenino= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoFemenino).toString().toUpperCase();
						String sexoMasculino= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoMasculino).toString().toUpperCase();
						
						if (sexo.trim().equals(sexoFemenino)) {
							sinValini.setSexoPaciente(ConstantesIntegridadDominio.acronimoFemenino);
						}else if (sexo.trim().equals(sexoMasculino)) {
							sinValini.setSexoPaciente(ConstantesIntegridadDominio.acronimoMasculino);
						}
					}
					
					registro.setDsIngresosSinValIni(new JRBeanCollectionDataSource(registro.getListaIngresosSinValIni()));
				}
				
				
				
				if (!Utilidades.isEmpty(registro.getConsolidado())) {
					registro.getConsolidadoIngresosOdonto().setDsTotalPresupuestos(
							new JRBeanCollectionDataSource(registro.getConsolidadoIngresosOdonto().getListaTotalPresupuesto()));
					registro.setDsConsolidado(new JRBeanCollectionDataSource(registro.getConsolidado()));
				}
			}
			
			dtoGeneral.setDsResultadoConsulta(new JRBeanCollectionDataSource(listadoResultado));
			collectionDTOGeneral.add(dtoGeneral);
		}
		
		return collectionDTOGeneral;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        

        try {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESULTADOCONSULTAINGRESOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESULTADOCONSULTAINGRESOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_RESULTADOCONSULTAINGRESOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_INGRESOSCONSINVALINI);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_INGRESOSCONSINVALINI);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_INGRESOSCONSINVALINI, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_CONSOLIDADOINGRESOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_CONSOLIDADOINGRESOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_CONSOLIDADOINGRESOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_TOTALPRESUPUESTOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_TOTALPRESUPUESTOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_TOTALPRESUPUESTOS, mySubreportObj);
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
