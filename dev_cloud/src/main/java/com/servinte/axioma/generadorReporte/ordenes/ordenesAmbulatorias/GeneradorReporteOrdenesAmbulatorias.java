package com.servinte.axioma.generadorReporte.ordenes.ordenesAmbulatorias;

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
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;

public class GeneradorReporteOrdenesAmbulatorias extends GeneradorReporte{

	
	/** Parametros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private DtoOrdenesAmbulatorias dtoResultadoOrden;

	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/ordenAmbulatoria.jasper";
	private static String RUTA_REPORTE_GENERAL_MEDIA_CARTA = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/ordenAmbulatoriaMediaCarta.jasper";
	
	private static String RUTA_SUBREPORTE_SERVICIOS = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteServicios.jasper";
	private static String NOMBRE_SUBREPORTE_SERVICIOS= "subReporteServicios";
	private static String RUTA_SUBREPORTE_SERVICIOS_ORDENES = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteServiciosOrdenes.jasper";
	private static String NOMBRE_SUBREPORTE_SERVICIOS_ORDENES = "subReporteServiciosOrdenes";
	private static String RUTA_SUBREPORTE_SERVICIOS_OTROS = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteServiciosOtros.jasper";
	private static String NOMBRE_SUBREPORTE_SERVICIOS_OTROS = "subReporteServiciosOtros";
	
	private static String RUTA_SUBREPORTE_ARTICULOS = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulos.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOS = "subReporteArticulos";
	private static String RUTA_SUBREPORTE_ARTICULOS_MEDICAMENTOS = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulosMedicamentos.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOS_MEDICAMENTOS = "subReporteArticulosMedicamentos";
	private static String RUTA_SUBREPORTE_ARTICULOS_INSUMOS = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulosInsumos.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOS_INSUMOS = "subReporteArticulosInsumos";
	private static String RUTA_SUBREPORTE_ARTICULOS_OTROS = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulosOtros.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOS_OTROS = "subReporteArticulosOtros";
	
	private static String RUTA_SUBREPORTE_ARTICULOSCE = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulosCE.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOSCE = "subReporteArticulosCE";
	private static String RUTA_SUBREPORTE_ARTICULOS_MEDICAMENTOSCE = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulosMedicamentosCE.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOS_MEDICAMENTOSCE = "subReporteArticulosMedicamentosCE";
	
	private static String RUTA_SUBREPORTE_ARTICULOS_INSUMOSCE = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulosInsumos.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOS_INSUMOSCE = "subReporteArticulosInsumosCE";
	private static String RUTA_SUBREPORTE_ARTICULOS_OTROSCE = "com/servinte/axioma/generadorReporte/ordenes/ordenesAmbulatorias/subReporteArticulosOtros.jasper";
	private static String NOMBRE_SUBREPORTE_ARTICULOS_OTROSCE = "subReporteArticulosOtrosCE";
	
	
	/**FIXME ------->AQUI VA LA RUTA Y EL NOMBRE DE LOS SUBREPORTES*/
	
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Camilo Gómez
	 */
	public GeneradorReporteOrdenesAmbulatorias() {
	
	}
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @param listadoResultado
	 * @param filtroCambioSer
	 *
	 * @author Camilo Gómez
	 */
	public GeneradorReporteOrdenesAmbulatorias(DtoOrdenesAmbulatorias dtoResultado){	
		
		this.dtoResultadoOrden  = dtoResultado;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override	
	public Collection obtenerDatosReporte() 
	{
		Collection<DtoOrdenesAmbulatorias>  collectionDTOGeneral= new ArrayList();
		DtoOrdenesAmbulatorias dtoGeneral = new  DtoOrdenesAmbulatorias();	
		
		RUTA_LOGO = "../"+ dtoResultadoOrden.getRutaLogo();
		
		if (dtoResultadoOrden != null) 
		{
			String ubicacionLogo = dtoResultadoOrden.getUbicacionLogo();
			String rutaLogo = "../"+dtoResultadoOrden.getRutaLogo();
	
			if(!Utilidades.isEmpty(dtoResultadoOrden.getListaServicios()) && !Utilidades.isEmpty(dtoResultadoOrden.getListaArticulos()))
			{
				dtoGeneral.setSaltoPaginaReporte(true);
			}
			
			dtoGeneral.setRazonSocial(dtoResultadoOrden.getRazonSocial());
			//dtoGeneral.setPrimerNombreUsuario(dtoResultadoOrden.getPrimerNombreUsuario()+" "+dtoResultadoOrden.getPrimerApellidoUsuario());
			dtoGeneral.setUsuario(dtoResultadoOrden.getUsuario());
			dtoGeneral.setDireccion(dtoResultadoOrden.getDireccion());//El telefono ya esta concatenado con la direccion desde el action
			dtoGeneral.setActividadEconomica(dtoResultadoOrden.getActividadEconomica());
			dtoGeneral.setNit(dtoResultadoOrden.getNit());
			dtoGeneral.setTipoImpresion(dtoResultadoOrden.getTipoImpresion());	
			
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
				
			
			//---------------Servicios
			for (DtoOrdenesAmbulatorias dtoServicios : dtoResultadoOrden.getListaServicios()) 
			{	
				String rutaFirma =dtoServicios.getFirmaDigital();
				boolean existeFirma=false;
				if(!UtilidadTexto.isEmpty(rutaFirma))
					existeFirma=existeLogo(rutaFirma);
				
				if(existeFirma)
					dtoServicios.setFirmaDigitalMedico(rutaFirma);
				else
					dtoServicios.setFirmaDigitalMedico(null);
				
				//Ordenes
				if(!Utilidades.isEmpty(dtoServicios.getListaServiciosOrdenes()))
					dtoServicios.setJRDlistaServiciosOrdenes(new JRBeanCollectionDataSource(dtoServicios.getListaServiciosOrdenes()));
				
				//Otros
				if(!Utilidades.isEmpty(dtoServicios.getListaServiciosOtros()))
					dtoServicios.setJRDlistaServiciosOtros(new JRBeanCollectionDataSource(dtoServicios.getListaServiciosOtros()));
			}
			
			//--------------Articulos
			int tipoOrdenArt=0;
			for (DtoOrdenesAmbulatorias dtoArticulos : dtoResultadoOrden.getListaArticulos()) 
			{	
				String rutaFirma =dtoArticulos.getFirmaDigital();
				boolean existeFirma=false;
				if(!UtilidadTexto.isEmpty(rutaFirma))
					existeFirma=existeLogo(rutaFirma);
				
				if(existeFirma)
					dtoArticulos.setFirmaDigitalMedico(rutaFirma);
				else
					dtoArticulos.setFirmaDigitalMedico(null);
				
				//Medicamentos
				if(!Utilidades.isEmpty(dtoArticulos.getListaArticulosMedicamentos()))
					{
					char controlEsp='N';
					boolean esNormal=false;
					boolean esControl=false;
					for (DtoOrdenesAmbulatorias dtoArticulos1 : dtoArticulos.getListaArticulosMedicamentos()) 
					{
						
						if (dtoArticulos1.getControlEspecial().equals('N'))
						{
							esNormal=true;
						}
						if (dtoArticulos1.getControlEspecial().equals('S'))
						{
							esControl=true;
							controlEsp='S';
						}
						
						
					}
					// codigo pra verificar si solo hay ordenes de un tipo o ambas
					if (esNormal && !esControl)
					{ tipoOrdenArt=1;}
					if (!esNormal && esControl)
					{ tipoOrdenArt=2;}
					if (esNormal && esControl)
					{ tipoOrdenArt=3;}
					dtoArticulos.setControlEspecial(controlEsp);
					dtoArticulos.setJRDlistaArticulosMedicamentos(new JRBeanCollectionDataSource(dtoArticulos.getListaArticulosMedicamentos()));
					dtoArticulos.setJRDlistaArticulosMedicamentosCE(new JRBeanCollectionDataSource(dtoArticulos.getListaArticulosMedicamentos()));
					}
				//Insumos
				if(!Utilidades.isEmpty(dtoArticulos.getListaArticulosInsumos()))
					{dtoArticulos.setJRDlistaArticulosInsumos(new JRBeanCollectionDataSource(dtoArticulos.getListaArticulosInsumos()));
					dtoArticulos.setJRDlistaArticulosInsumosCE(new JRBeanCollectionDataSource(dtoArticulos.getListaArticulosInsumos()));
					}
				//Otros
				if(!Utilidades.isEmpty(dtoArticulos.getListaArticulosOtros()))
					{dtoArticulos.setJRDlistaArticulosOtros(new JRBeanCollectionDataSource(dtoArticulos.getListaArticulosOtros()));
					dtoArticulos.setJRDlistaArticulosOtrosCE(new JRBeanCollectionDataSource(dtoArticulos.getListaArticulosOtros()));
					} }
			
			dtoGeneral.setJRDlistaServicios(new JRBeanCollectionDataSource(dtoResultadoOrden.getListaServicios()));
			dtoGeneral.setJRDlistaArticulos(new JRBeanCollectionDataSource(dtoResultadoOrden.getListaArticulos()));
			dtoGeneral.setJRDlistaArticulosCE(new JRBeanCollectionDataSource(dtoResultadoOrden.getListaArticulos()));
			dtoGeneral.setTipoReportesArt(tipoOrdenArt);
			collectionDTOGeneral.add(dtoGeneral);
			
		}
		
		return collectionDTOGeneral;
	}

	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
      

        try {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_SERVICIOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_SERVICIOS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_SERVICIOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_SERVICIOS_ORDENES);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_SERVICIOS_ORDENES);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_SERVICIOS_ORDENES, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_SERVICIOS_OTROS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_SERVICIOS_OTROS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_SERVICIOS_OTROS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOSCE);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOSCE);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOSCE, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOS_MEDICAMENTOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOS_MEDICAMENTOS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOS_MEDICAMENTOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOS_MEDICAMENTOSCE);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOS_MEDICAMENTOSCE);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOS_MEDICAMENTOSCE, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOS_INSUMOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOS_INSUMOS);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOS_INSUMOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOS_INSUMOSCE);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOS_INSUMOSCE);
	           	
			}else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOS_INSUMOSCE, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOS_OTROS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOS_OTROS);
	           	
			}else if (myInFile != null) {
			    Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOS_OTROS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ARTICULOS_OTROSCE);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ARTICULOS_OTROSCE);
	           	
			}else if (myInFile != null) {
			    Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ARTICULOS_OTROSCE, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        /**FIXME ------->AQUI VA CARGA DE LOS SUBREPORTES*/
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}

	@Override
	public String obtenerRutaPlantilla() {
		
		if(dtoResultadoOrden.getFormatoMediaCarta().equals(ConstantesBD.acronimoSi)){
			return RUTA_REPORTE_GENERAL_MEDIA_CARTA;
		}		
		return RUTA_REPORTE_GENERAL;
	}
}
