package com.servinte.axioma.generadorReporte.odontologia.ingresosOdontologicos;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesIntegridadDominio;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteIngresoOdonto;
/**
 * Esta clase se encarga de consolidar la información a ser 
 * mostrada en el reporte de ingresos odontológicos.
 *
 * @author Yennifer Guerrero
 * @since 
 *
 */
public class GeneradorReporteIngresoOdontologico extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
	private ArrayList<DtoResultadoConsultaIngresosOdonto> listadoResultado;
	private DtoReporteIngresosOdontologicos filtroIngresos;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/reporteIngresoOdonto.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTAINGRESOS = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subreporteListadoResultado.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTAINGRESOS = "subreporteListadoResultado";
	private static String RUTA_SUBREPORTE_INGRESOSCONVALINI = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subreporteIngresosConValIni.jasper";
	private static String NOMBRE_SUBREPORTE_INGRESOSCONVALINI = "subreporteIngresosConValIni";
	private static String RUTA_SUBREPORTE_INGRESOSINVALINI = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subreporteIngresosSinValIni.jasper";
	private static String NOMBRE_SUBREPORTE_INGRESOSSINVALINI = "subreporteIngresosSinValIni";
	private static String RUTA_SUBREPORTE_CONSOLIDADOINGRESOS = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subrptConsolidadoIngresosOdonto.jasper";
	private static String NOMBRE_SUBREPORTE_CONSOLIDADOINGRESOS = "subrptConsolidadoIngresosOdonto";
	private static String RUTA_SUBREPORTE_TOTALPRESUPUESTOS = "com/servinte/axioma/generadorReporte/odontologia/ingresosOdontologicos/subrptTotalPorEstado.jasper";
	private static String NOMBRE_SUBREPORTE_TOTALPRESUPUESTOS = "subrptTotalPorEstado";
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteIngresoOdontologico() {
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param dtoResultado
	 * @param dtoFiltro
	 *
	 * @author Yennifer Guerrero
	 */
	public GeneradorReporteIngresoOdontologico(ArrayList<DtoResultadoConsultaIngresosOdonto> listadooResultado, 
			DtoReporteIngresosOdontologicos filtroIngresos){	
		
		this.listadoResultado = listadooResultado;
		this.filtroIngresos = filtroIngresos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoReporteIngresoOdonto>  collectionDTOGeneral= new ArrayList();
		
		DtoReporteIngresoOdonto dtoGeneral = new  DtoReporteIngresoOdonto();	
		RUTA_LOGO = "../"+ filtroIngresos.getRutaLogo();
		
		if (listadoResultado != null) {
			
			if (filtroIngresos != null) {
				
				String ubicacionLogo = filtroIngresos.getUbicacionLogo();
				String rutaLogo = "../"+filtroIngresos.getRutaLogo();
		
				dtoGeneral.setRazonSocial(filtroIngresos.getRazonSocial());
				dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateado());
				dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateado());
				dtoGeneral.setSexo(filtroIngresos.getAyudanteSexoPaciente());
				dtoGeneral.setEdad(filtroIngresos.getRangoEdadConsultada());
				dtoGeneral.setNombreProfesional(filtroIngresos.getNombreProfesional());
				dtoGeneral.setEstadoPresupuesto(filtroIngresos.getNombreEstadoPresupuesto());
				dtoGeneral.setIndicativoContrato(filtroIngresos.getNombreIndicativo());
				dtoGeneral.setNombreEspecialidad(filtroIngresos.getNombreEspecialidad());
				dtoGeneral.setNombrePaquete(filtroIngresos.getNombrePaquete());
				dtoGeneral.setNombrePrograma(filtroIngresos.getNombrePrograma());
				dtoGeneral.setUsuarioProcesa(filtroIngresos.getNombreUsuario());
				
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
			
			for (DtoResultadoConsultaIngresosOdonto registro : listadoResultado) {
				registro.setDsIngresosConValIni(new JRBeanCollectionDataSource(registro.getListaIngresosConValIni()));
				registro.setDsIngresosSinValIni(new JRBeanCollectionDataSource(registro.getListaIngresosSinValIni()));
				registro.getConsolidadoIngresosOdonto().setDsTotalPresupuestos(
						new JRBeanCollectionDataSource(registro.getConsolidadoIngresosOdonto().getListaTotalPresupuesto()));
				registro.setDsConsolidado(new JRBeanCollectionDataSource(registro.getConsolidado()));
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
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_INGRESOSCONVALINI);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_INGRESOSCONVALINI);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_INGRESOSCONVALINI, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_INGRESOSINVALINI);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_INGRESOSINVALINI);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_INGRESOSSINVALINI, mySubreportObj);
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
