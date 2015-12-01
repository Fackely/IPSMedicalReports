package com.servinte.axioma.generadorReporte.odontologia.ingresosTarjetaCliente;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.UtilidadTexto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta.DtoInfoVentaTarjeta;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoReporteIngresosVentaTarjetaArchivoPlano;

public class GeneradorReporteIngresosTarjetaClienteArchivoPlano extends GeneradorReporte {
	
	/** Parámetros del reporte.*/
    Map<String, Object> parametrosReporte = new HashMap<String, Object>();
    
    private ArrayList<DtoResultadoReporteVentaTarjetas> listadoResultado;
    private DtoFiltroReporteIngresosTarjetasCliente filtroIngresos;
    
	private static String RUTA_REPORTE_GENERAL_PLANO = "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/reporteIngresosTarjetaClientePlano.jasper";  
    private static String RUTA_SUBRPT_ARCHIVO_PLANO= "com/servinte/axioma/generadorReporte/odontologia/ingresosTarjetaCliente/subrptIngresosTarjetaClientePlano.jasper";
    private static String NOMBRE_SUBRPT_ARCHIVO_PLANO = "subrptIngresosTarjetaClientePlano";
    
    /**
     * Mètodo constructor de la clase
     *
     * @author Yennifer Guerrero
     */
    public GeneradorReporteIngresosTarjetaClienteArchivoPlano() {
	}
    
    /**
     * Mètodo constructor de la clase
     * @param listadoResultado
     * @param filtroIngresos
     *
     * @author Yennifer Guerrero
     */
    public GeneradorReporteIngresosTarjetaClienteArchivoPlano(ArrayList<DtoResultadoReporteVentaTarjetas> listadoResultado,
    		DtoFiltroReporteIngresosTarjetasCliente filtroIngresos) {
    	
    	this.listadoResultado = listadoResultado;
    	this.filtroIngresos = filtroIngresos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {
		
		Collection<DtoReporteIngresosVentaTarjetaArchivoPlano>  collectionDTOGeneral= new ArrayList();
		
		DtoReporteIngresosVentaTarjetaArchivoPlano dtoGeneral = new  DtoReporteIngresosVentaTarjetaArchivoPlano();	
		ArrayList<DtoFiltroReporteIngresosTarjetasCliente> listadoFiltro = new ArrayList<DtoFiltroReporteIngresosTarjetasCliente>();
		ArrayList<DtoReporteIngresosVentaTarjetaArchivoPlano> listadoResultadoPlano = new ArrayList<DtoReporteIngresosVentaTarjetaArchivoPlano>();
		
		if (listadoResultado != null) {
			
			if (filtroIngresos != null) {
				listadoFiltro.add(filtroIngresos);
			}
			
			for (DtoResultadoReporteVentaTarjetas registro : listadoResultado) {
				double valorPorInstitucion = 0;
				
				for (DtoCentroAtencionReporte dto : registro.getCentrosAtencion()) {
					double valorTotalPorCA = 0;
					
					
					for (DtoTiposTarjetas tipos : dto.getTiposTarjetas()) {
						for (DtoClaseVentaTarjeta info : tipos.getClaseVentaTarjeta()) {
							double valorTotalClaseTarjeta = 0;
							
							
							for (DtoInfoVentaTarjeta detalle: info.getInfoVentaTarjeta()) {
								dtoGeneral = new  DtoReporteIngresosVentaTarjetaArchivoPlano();									
								dtoGeneral.setDescripcionEmpresaInstitucion(registro.getDescripcionEmpresaInstitucion());
								dtoGeneral.setDescripcionCentroAtencion(dto.getDescripcionCentroAtencion());
								dtoGeneral.setFechaInicial(filtroIngresos.getFechaInicialFormateado());
								dtoGeneral.setFechaFinal(filtroIngresos.getFechaFinalFormateado());
								dtoGeneral.setEdad(detalle.getEdadComprador());
								dtoGeneral.setSexoComprador(detalle.getSexoComprador());								
								dtoGeneral.setDescripcionCentroAtencion(dto.getDescripcionCentroAtencion());								
								dtoGeneral.setDescripcionRegionCA(dto.getDescripcionRegionCA());								
								dtoGeneral.setDescripcionPais(dto.getDescripcionPais());								
								dtoGeneral.setDescripcionDepartamento(dto.getDescripcionDepartamento());								
								dtoGeneral.setDescripcionTipoTarjeta(tipos.getDescripcionTipoTarjeta());
								dtoGeneral.setClaseTarjeta(info.getClaseTarjeta());
								dtoGeneral.setDescripcionClaseTarjeta(info.getDescripcionClaseTarjeta());
								dtoGeneral.setSerialInicial(detalle.getSerialInicial());
								dtoGeneral.setSerialFinal(detalle.getSerialFinal());
								dtoGeneral.setCantidad(detalle.getCantidad());
								dtoGeneral.setFechaVenta(detalle.getFechaVenta());
								dtoGeneral.setNroFactura(detalle.getNroFactura());
								
								String[] cadena = detalle.getUsuarioVendedor().split(" ");
								
								if (cadena.length == 1) {
									dtoGeneral.setPrimerNombreVendedor(cadena[0]);
									
								}else if (cadena.length == 2) {
									dtoGeneral.setPrimerNombreVendedor(cadena[0]);
									dtoGeneral.setPrimerApellidoVendedor(cadena[1]);
								}else if (cadena.length == 3) {
									dtoGeneral.setPrimerNombreVendedor(cadena[0]);
									dtoGeneral.setPrimerApellidoVendedor(cadena[2]);
								}else if (cadena.length == 4) {
									dtoGeneral.setPrimerNombreVendedor(cadena[0]);
									dtoGeneral.setPrimerApellidoVendedor(cadena[2]);
								}
								
								dtoGeneral.setConvenioTarifa(detalle.getConvenioTarifa());
								dtoGeneral.setPrimerNombreComprador(detalle.getPrimerNombreComprador());
								dtoGeneral.setPrimerApellidoComprador(detalle.getPrimerApellidoComprador());								
								dtoGeneral.setValorTotalVenta(detalle.getValorTotalVenta());
								dtoGeneral.setCodigoEmpresaInstitucion(registro.getCodigoEmpresaInstitucion());
								dtoGeneral.setConsecutivoCentroAtencion(dto.getConsecutivoCentroAtencion());
								dtoGeneral.setValorTotalFormateado(info.getValorTotalFormateado());
								dtoGeneral.setValorTotalPorCAFormateado(dto.getValorTotalPorCAFormateado());
								dtoGeneral.setValorPorInstitucionFormateado(registro.getValorPorInstitucionFormateado());
								valorTotalClaseTarjeta += detalle.getValorTotalVenta();
								dtoGeneral.setCodigoTipoTarjeta(tipos.getCodigoTipoTarjeta());
								
								listadoResultadoPlano.add(dtoGeneral);
							}
							info.setValorTotalClaseTarjeta(valorTotalClaseTarjeta);
							valorTotalPorCA += valorTotalClaseTarjeta;
						}
					}
					
					String valor = UtilidadTexto.formatearValores(valorTotalPorCA);
					dto.setValorTotalPorCAFormateado(valor);
					valorPorInstitucion+= valorTotalPorCA;
				}
				String valor = UtilidadTexto.formatearValores(valorPorInstitucion);
				registro.setValorPorInstitucionFormateado(valor);
			}
			
			dtoGeneral.setDsListadoResultado(new JRBeanCollectionDataSource(listadoResultadoPlano));
			collectionDTOGeneral.add(dtoGeneral);
		}
		return collectionDTOGeneral;
	}
	
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        

        try {
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBRPT_ARCHIVO_PLANO);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBRPT_ARCHIVO_PLANO);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBRPT_ARCHIVO_PLANO, mySubreportObj);
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
		return RUTA_REPORTE_GENERAL_PLANO;
	}

}
