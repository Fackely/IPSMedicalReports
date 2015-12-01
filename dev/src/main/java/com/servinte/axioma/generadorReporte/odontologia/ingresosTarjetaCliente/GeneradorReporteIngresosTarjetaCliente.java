package com.servinte.axioma.generadorReporte.odontologia.ingresosTarjetaCliente;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta.DtoInfoVentaTarjeta;

/**
 * Esta clase se encarga de consolidar la informaciòn y las plantillas a utilizar
 * en la creación del reporte de ingresos por tarjeta cliente en formato excel, pdf y archivo plano. 
 * 
 * @author Yennifer Guerrero
 * @since  2/11/2010
 */
public class GeneradorReporteIngresosTarjetaCliente extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private ArrayList<DtoResultadoReporteVentaTarjetas> listadoResultado;
    private DtoFiltroReporteIngresosTarjetasCliente filtroIngresos;
    private String rutaLogo = "";
	private String NOMBRE_LOGO = "nombreLogo";
	
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/reporteIngresosTarjetaCliente.jasper";  
    
    private static String RUTA_SUBRPT_INSTITUCION = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/subrptInstitucion.jasper";
    private static String NOMBRE_SUBRPT_INSTITUCION = "subrptInstitucion";
    
    private static String RUTA_SUBRPT_CENTROS_ATENCION = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/subrptCentrosAtencion.jasper";
    private static String NOMBRE_SUBRPT_CENTROS_ATENCION = "subrptCentrosAtencion";
    
    private static String RUTA_SUBRPT_TIPOS_TARJETAS = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/subrptTiposTarjetas.jasper";
    private static String NOMBRE_SUBRPT_TIPOS_TARJETAS = "subrptTiposTarjetas";
	
    private static String RUTA_SUBRPT_CLASE_VENTA = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/subrptClaseVentaTarjeta.jasper";
	private static String NOMBRE_SUBRPT_CLASE_VENTA = "subrptClaseVentaTarjeta";
    
	private static String RUTA_SUBRPT_INFO_VENTA_TARJETA = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/subrptInfoVentaTarjeta.jasper";
	private static String NOMBRE_SUBRPT_INFO_VENTA_TARJETA = "subrptInfoVentaTarjeta";
    
	private static String RUTA_SUBRPT_INFO_VENTA_TARJETA_PERSONAL = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/subrptInfoVentaTarjetaPersonal.jasper";
    private static String NOMBRE_SUBRPT_INFO_VENTA_TARJETA_PERSONAL = "subrptInfoVentaTarjetaPersonal";
    
    /**
     * Mètodo constructor de la clase
     *
     * @author Yennifer Guerrero
     */
    public GeneradorReporteIngresosTarjetaCliente() {
	}
    
    public GeneradorReporteIngresosTarjetaCliente(ArrayList<DtoResultadoReporteVentaTarjetas> listadoResultado,
    		DtoFiltroReporteIngresosTarjetasCliente filtroIngresos) {
    	
    	this.listadoResultado = listadoResultado;
    	this.filtroIngresos = filtroIngresos;
	}

	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoResultadoReporteVentaTarjetas>  collectionDTOGeneral= new ArrayList();
		
		DtoResultadoReporteVentaTarjetas dtoGeneral = new  DtoResultadoReporteVentaTarjetas();	
		rutaLogo = "../"+ filtroIngresos.getRutaLogo();
		ArrayList<DtoCentroAtencionReporte> centrosAtencion = new ArrayList<DtoCentroAtencionReporte>();
		ArrayList<DtoFiltroReporteIngresosTarjetasCliente> listadoFiltro = new ArrayList<DtoFiltroReporteIngresosTarjetasCliente>();
		
		if (listadoResultado != null) {
			
			if (filtroIngresos != null) {
				
				String ubicacionLogo = filtroIngresos.getUbicacionLogo();
				String rutaLogo = "../"+filtroIngresos.getRutaLogo();
				
				listadoFiltro.add(filtroIngresos);
		
				dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateado());
				dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateado());
				dtoGeneral.setRazonSocial(filtroIngresos.getRazonSocial());
				dtoGeneral.setEdad(filtroIngresos.getRangoEdadConsultada());
				dtoGeneral.setSexoComprador(filtroIngresos.getAyudanteSexocomprador());
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
			
			double valorTotalClaseTarjeta = 0;
			double valorTotalPorCA = 0;
			
			
			for (DtoResultadoReporteVentaTarjetas registro : listadoResultado) {
				double valorPorInstitucion = 0;
				centrosAtencion.addAll(registro.getCentrosAtencion());
				
				registro.setDsCentrosAtencion(new JRBeanCollectionDataSource(registro.getCentrosAtencion()));
				
				for (DtoCentroAtencionReporte dto : centrosAtencion) {
					dto.setDsTiposTarjetas(new JRBeanCollectionDataSource(dto.getTiposTarjetas()));
					
					for (DtoTiposTarjetas tipos : dto.getTiposTarjetas()) {
						tipos.setDsClaseVentaTarjeta(new JRBeanCollectionDataSource(tipos.getClaseVentaTarjeta()));
						
						for (DtoClaseVentaTarjeta info : tipos.getClaseVentaTarjeta()) {
							info.setDsInfoVentaTarjeta(new JRBeanCollectionDataSource(info.getInfoVentaTarjeta()));
							
							for (DtoInfoVentaTarjeta detalle: info.getInfoVentaTarjeta()) {
								valorTotalClaseTarjeta += detalle.getValorTotalVenta();
							}
							info.setValorTotalClaseTarjeta(valorTotalClaseTarjeta);
							valorTotalPorCA += valorTotalClaseTarjeta;
							valorTotalClaseTarjeta = 0;
						}
					}
					
					String valor = UtilidadTexto.formatearValores(valorTotalPorCA);
					dto.setValorTotalPorCAFormateado(valor);
					valorPorInstitucion+= valorTotalPorCA;
					valorTotalPorCA = 0;
				}
				String valor = UtilidadTexto.formatearValores(valorPorInstitucion);
				registro.setValorPorInstitucionFormateado(valor);
				
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
	        
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_TIPOS_TARJETAS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_TIPOS_TARJETAS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_TIPOS_TARJETAS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_CLASE_VENTA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_CLASE_VENTA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_CLASE_VENTA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_INFO_VENTA_TARJETA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_INFO_VENTA_TARJETA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_INFO_VENTA_TARJETA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_INFO_VENTA_TARJETA_PERSONAL);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_INFO_VENTA_TARJETA_PERSONAL);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_INFO_VENTA_TARJETA_PERSONAL, mySubreportObj);
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
