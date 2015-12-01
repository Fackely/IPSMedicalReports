package com.servinte.axioma.generadorReporte.facturacion.valorFacturadoPorConvenio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.facturacion.DTOConveniosReporteValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DTOGeneralReporteValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.princetonsa.enu.general.CarpetasArchivos;

/**
 * 
 * Esta clase se encarga de generar el reporte de los valores facturados por
 * convenio
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 06/12/2010
 */
public class GeneradorReporteValoresFacturadosPorConvenio extends
		GeneradorReporte {

	/** Par&aacute;metros del reporte. */
	Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	private DtoReporteValoresFacturadosPorConvenio dtoFiltros;
	private DTOConveniosReporteValoresFacturadosPorConvenio convenio;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/facturacion/valorFacturadoPorConvenio/reporteValoresFacturadosConvenio.jasper";
	private static String RUTA_SUBREPORTE_TARIFAS = "com/servinte/axioma/generadorReporte/facturacion/valorFacturadoPorConvenio/subreporteValoresFacturadosConvenio.jasper";
	private static String NOMBRE_SUBREPORTE_TARIFAS = "subreporteValoresFacturadosConvenio";
	private static String ETIQUETA_TODAS="Todas";
	private static String ETIQUETA_TODOS="Todos";

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReporteValoresFacturadosPorConvenio() {
	}

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @param dtoFiltros
	 * @param convenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReporteValoresFacturadosPorConvenio(
			DtoReporteValoresFacturadosPorConvenio dtoFiltros,
			DTOConveniosReporteValoresFacturadosPorConvenio convenio) {
		this.dtoFiltros = dtoFiltros;
		this.convenio = convenio;
	}

	/**
	 * 
	 * M&eacute;todo encargado de crear las fuentes de datos del reporte.
	 * 
	 * @return collectionDTOGeneral
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection obtenerDatosReporte() {

		DTOGeneralReporteValoresFacturadosPorConvenio dtoGeneral = 
			new DTOGeneralReporteValoresFacturadosPorConvenio();

		Collection<DTOGeneralReporteValoresFacturadosPorConvenio> collectionDTOGeneral = 
			new ArrayList<DTOGeneralReporteValoresFacturadosPorConvenio>();

		ArrayList<DtoReporteValoresFacturadosPorConvenio> listaReporte = 
			new ArrayList<DtoReporteValoresFacturadosPorConvenio>();

		DtoReporteValoresFacturadosPorConvenio dtoReporte = null;
		if (dtoFiltros != null) {

			DTOConveniosReporteValoresFacturadosPorConvenio convenioValoresFacturados = convenio;

			String rutaLogo = "../" + dtoFiltros.getRutaLogo();

			dtoGeneral.setRazonSocial(dtoFiltros.getRazonSocial());
			dtoGeneral.setNit(dtoFiltros.getNit());
			dtoGeneral.setActividadEconomica(dtoFiltros.getActividadEconomica());
			dtoGeneral.setFechaInicialFactura(UtilidadFecha
					.conversionFormatoFechaAAp(dtoFiltros.getFechaInicial()));
			dtoGeneral.setFechaFinalFactura(UtilidadFecha
					.conversionFormatoFechaAAp(dtoFiltros.getFechaFinal()));
			if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreEmpresaInstitucion())) {
				dtoGeneral.setNombreInstitucion(dtoFiltros.getNombreEmpresaInstitucion());
			} else {
				dtoGeneral.setNombreInstitucion(ETIQUETA_TODAS);
			}
			if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreCentroAtencion())) {
				dtoGeneral.setNombreCentroAtencion(dtoFiltros.getNombreCentroAtencion());
			} else {
				dtoGeneral.setNombreCentroAtencion(ETIQUETA_TODOS);
			}
			dtoGeneral.setUsuarioProceso(dtoFiltros.getUsuarioProceso());

			boolean existeLogo = existeLogo(rutaLogo);
			if (existeLogo) {
				dtoGeneral.setLogoDerecha(rutaLogo);
			}

			ArrayList<Double> listaValoresFacturados = null;
			ArrayList<Double> listaValoresFacturasAnuladas = null;
			ArrayList<Double> listaTotalFacturado = null;

			int i = 0;
			int codEmpresa = convenioValoresFacturados.getListaValoresFacturadosConvenios().get(i).getCodigoEmpresa();
			int codConvenio = convenioValoresFacturados.getListaValoresFacturadosConvenios().get(i).getCodigoConvenio();
			Double valorFacturado = 0D;
			Double valorFacturasAnuladas = 0D;
			Double totalFacturado = 0D;
			Double valorNetoFacturado = 0D;
			
			//*********************
			Double sumatoriaValorFacturado = 0D;
			Double sumatoriaValorFacturasAnuladas = 0D;
			Double sumatoriaValorNetoFacturadoAnulado= 0D;		
			//**********************

			for (DTOFacturasConvenios registro : convenioValoresFacturados.getListaValoresFacturadosConvenios()) {
				if (codEmpresa == registro.getCodigoEmpresa()) {
					if (registro.getEstadoFactura() == 1) {
						totalFacturado = totalFacturado + registro.getValor();
					}
				} else {
					codEmpresa = registro.getCodigoEmpresa();
					if (listaTotalFacturado == null) {
						listaTotalFacturado = new ArrayList<Double>();
					}
					listaTotalFacturado.add(totalFacturado);
					totalFacturado = 0D;
					if (registro.getEstadoFactura() == 1) {
						totalFacturado = totalFacturado + registro.getValor();
					}
				}

				if (codConvenio == registro.getCodigoConvenio()) {
					if (registro.getEstadoFactura() == 1) {
						valorFacturado = valorFacturado + registro.getValor();
					} else {
						valorFacturasAnuladas = valorFacturasAnuladas + registro.getValor();
					}
				} else {
					codConvenio = registro.getCodigoConvenio();

					if (listaValoresFacturados == null) {
						listaValoresFacturados = new ArrayList<Double>();
					}
					
					valorFacturado = valorFacturado + valorFacturasAnuladas;
					listaValoresFacturados.add(valorFacturado);
					
					valorFacturado = 0D;

					if (listaValoresFacturasAnuladas == null) {
						listaValoresFacturasAnuladas = new ArrayList<Double>();
					}
					listaValoresFacturasAnuladas.add(valorFacturasAnuladas);
					
					valorFacturasAnuladas = 0D;

					if (registro.getEstadoFactura() == 1) {
						valorFacturado = valorFacturado + registro.getValor();
					} else {
						valorFacturasAnuladas = valorFacturasAnuladas + registro.getValor();
					}
				}
			}
			if (listaValoresFacturados == null) {
				listaValoresFacturados = new ArrayList<Double>();
			}
			
			valorFacturado = valorFacturado + valorFacturasAnuladas;
			listaValoresFacturados.add(valorFacturado);
			
			if (listaValoresFacturasAnuladas == null) {
				listaValoresFacturasAnuladas = new ArrayList<Double>();
			}
			listaValoresFacturasAnuladas.add(valorFacturasAnuladas);
			
			if (listaTotalFacturado == null) {
				listaTotalFacturado = new ArrayList<Double>();
			}
			listaTotalFacturado.add(totalFacturado);

			int j = 0;
			i = 0;
			codEmpresa = convenioValoresFacturados.getListaValoresFacturadosConvenios().get(i).getCodigoEmpresa();
			codConvenio = convenioValoresFacturados.getListaValoresFacturadosConvenios().get(i).getCodigoConvenio();
			boolean dtoRegistrado = false;
			String sNit = "";
			for (DTOFacturasConvenios registro : convenioValoresFacturados.getListaValoresFacturadosConvenios()) {

				sNit = UtilidadTexto.formatearValores(registro.getNit(), "###,##0");
				if (codEmpresa == registro.getCodigoEmpresa()) {

					if (!dtoRegistrado) {
						dtoReporte = new DtoReporteValoresFacturadosPorConvenio();
					}

					dtoRegistrado=true;
					dtoReporte.setNitEmpresa(sNit.replaceAll(",","."));
					dtoReporte.setNombreEmpresa(registro.getNombreEmpresa());
					if (codConvenio == registro.getCodigoConvenio()) {
						dtoReporte.setNombreConvenio(registro.getNombreConvenio() + '-' + registro.getNumeroContrato());
					} else {
						codConvenio = registro.getCodigoConvenio();

						dtoRegistrado=false;
						
						dtoReporte.setValorFacturado(listaValoresFacturados.get(i));						
						sumatoriaValorFacturado=sumatoriaValorFacturado+listaValoresFacturados.get(i);
						
						dtoReporte.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
						sumatoriaValorFacturasAnuladas=sumatoriaValorFacturasAnuladas+listaValoresFacturasAnuladas.get(i);
						
						valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
						sumatoriaValorNetoFacturadoAnulado=sumatoriaValorNetoFacturadoAnulado+valorNetoFacturado;
						
						dtoReporte.setValorNetoFacturado(valorNetoFacturado);
						dtoReporte.setTotalFacturado(listaTotalFacturado.get(j));

						i++;

						listaReporte.add(dtoReporte);

						if (!dtoRegistrado) {
							dtoReporte = new DtoReporteValoresFacturadosPorConvenio();
						}

						dtoRegistrado=true;
						dtoReporte.setNitEmpresa(sNit.replaceAll(",","."));
						dtoReporte.setNombreEmpresa(registro.getNombreEmpresa());
						if (codConvenio == registro.getCodigoConvenio()) {
							dtoReporte.setNombreConvenio(registro.getNombreConvenio() + '-' + registro.getNumeroContrato());
						} else {
							codConvenio = registro.getCodigoConvenio();

							dtoRegistrado=false;
														
							dtoReporte.setValorFacturado(listaValoresFacturados.get(i));
							sumatoriaValorFacturado=sumatoriaValorFacturado+listaValoresFacturados.get(i);
							
							dtoReporte.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
							sumatoriaValorFacturasAnuladas=sumatoriaValorFacturasAnuladas+listaValoresFacturasAnuladas.get(i);
							
							valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
							sumatoriaValorNetoFacturadoAnulado=sumatoriaValorNetoFacturadoAnulado+valorNetoFacturado;
							
							dtoReporte.setValorNetoFacturado(valorNetoFacturado);
							dtoReporte.setTotalFacturado(listaTotalFacturado.get(j));
							
							i++;

							listaReporte.add(dtoReporte);
						}
					}

				} else {

					codEmpresa = registro.getCodigoEmpresa();
					codConvenio = registro.getCodigoConvenio();

					dtoRegistrado=false;
					dtoReporte.setValorFacturado(listaValoresFacturados.get(i));
					sumatoriaValorFacturado=sumatoriaValorFacturado+listaValoresFacturados.get(i);
					
					dtoReporte.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
					sumatoriaValorFacturasAnuladas=sumatoriaValorFacturasAnuladas+listaValoresFacturasAnuladas.get(i);
					
					valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
					sumatoriaValorNetoFacturadoAnulado=sumatoriaValorNetoFacturadoAnulado+valorNetoFacturado;
					
					dtoReporte.setValorNetoFacturado(valorNetoFacturado);
					dtoReporte.setTotalFacturado(listaTotalFacturado.get(j));
					
					i++;
					j++;

					listaReporte.add(dtoReporte);

					if (!dtoRegistrado) {
						dtoReporte = new DtoReporteValoresFacturadosPorConvenio();
					}

					dtoRegistrado=true;
					dtoReporte.setNitEmpresa(sNit.replaceAll(",","."));
					dtoReporte.setNombreEmpresa(registro.getNombreEmpresa());
					if (codConvenio == registro.getCodigoConvenio()) {
						dtoReporte.setNombreConvenio(registro.getNombreConvenio() + '-' + registro.getNumeroContrato());
					} else {
						codConvenio = registro.getCodigoConvenio();

						dtoRegistrado=false;
						
						dtoReporte.setValorFacturado(listaValoresFacturados.get(i));
						sumatoriaValorFacturado=sumatoriaValorFacturado+listaValoresFacturados.get(i);
						
						dtoReporte.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
						sumatoriaValorFacturasAnuladas=sumatoriaValorFacturasAnuladas+listaValoresFacturasAnuladas.get(i);
						
						valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
						sumatoriaValorNetoFacturadoAnulado=sumatoriaValorNetoFacturadoAnulado+valorNetoFacturado;
						
						dtoReporte.setValorNetoFacturado(valorNetoFacturado);
						dtoReporte.setTotalFacturado(listaTotalFacturado.get(j));

						i++;

						listaReporte.add(dtoReporte);
					}

				}

			}
			dtoReporte.setValorFacturado(listaValoresFacturados.get(i));
			sumatoriaValorFacturado=sumatoriaValorFacturado+listaValoresFacturados.get(i);
			
			dtoReporte.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
			sumatoriaValorFacturasAnuladas=sumatoriaValorFacturasAnuladas+listaValoresFacturasAnuladas.get(i);
			
			valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
			sumatoriaValorNetoFacturadoAnulado=sumatoriaValorNetoFacturadoAnulado+valorNetoFacturado;
			
			dtoReporte.setValorNetoFacturado(valorNetoFacturado);
			dtoReporte.setTotalFacturado(listaTotalFacturado.get(j));
			
			dtoReporte.setSumatoriaValorFacturado(sumatoriaValorFacturado);
			dtoReporte.setSumatoriaValorFacturasAnuladas(sumatoriaValorFacturasAnuladas);
			dtoReporte.setSumatoriaValorNetoFacturadoAnulado(sumatoriaValorNetoFacturadoAnulado);
			
			listaReporte.add(dtoReporte);
						
			dtoGeneral.setDsListaValoresFacturadosConvenios(new JRBeanCollectionDataSource(
					listaReporte));
		}
		collectionDTOGeneral.add(dtoGeneral);

		return collectionDTOGeneral;

	}

	/**
	 * 
	 * M&eacute;todo encargado de cargar como par&aacute;metros las plantillas
	 * de los subreportes y datos adicionales.
	 * 
	 * @return parametrosReporte
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;

		try {
			myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_TARIFAS);
			if (myInFile == null) {
				myInFile = new FileInputStream(RUTA_SUBREPORTE_TARIFAS);
			} else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
				parametrosReporte
						.put(NOMBRE_SUBREPORTE_TARIFAS, mySubreportObj);
				myInFile.close();
				myInFile = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parametrosReporte;
	}

	/**
	 * 
	 * M&eacute;todo encargado de devolver la ruta del reporte.
	 * 
	 * @return RUTA_REPORTE_GENERAL
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}

	/**
	 * M&eacute;todo encargado de sobreescribir m&eacute;todo exportarReportePDF
	 * de la superclase.
	 * 
	 * @param jasperPrint
	 * @param nombreReporte
	 * @return nombreArchivo
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@Override
	public String exportarReportePDF(JasperPrint jasperPrint, String nombreReporte) {

		String nombreArchivo = CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + "ReporteValoreFacturadosConvenio" + new Random().nextInt() + ".pdf";
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();

		File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));

		if (!dir.exists()) {
			if (dir.mkdirs()) {
				Log4JManager.info("***********************************se crea el directorio "+dir);
			}
		}

		try {
			JasperExportManager.exportReportToPdfFile(jasperPrint, path +nombreArchivo);
		} catch (JRException e) {
			e.printStackTrace();
		}

		return nombreArchivo;

	}

	/**
	 * M&eacute;todo encargado de sobreescribir m&eacute;todo exportarReporteExcel
	 * de la superclase.
	 * 
	 * @param jasperPrint
	 * @param nombreReporte
	 * @return nombreArchivo
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@Override
	public String exportarReporteExcel(JasperPrint jasperPrint, String nombreReporte) {

		String nombreArchivo = CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + "ReporteValoreFacturadosConvenio" + new Random().nextInt()+ ".xls";
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();

		File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));

		if (!dir.exists()) {
			if (dir.mkdirs()) {
				Log4JManager.info("***********************************se crea el directorio "+dir);
			}
		}

		try {

			JExcelApiExporter xlsExporter = new JExcelApiExporter();
			xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,
					jasperPrint);
			xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					path + nombreArchivo);

			xlsExporter.exportReport();

		} catch (JRException e) {
			e.printStackTrace();
		}

		return nombreArchivo;

	}

	/**
	 * 
	 * M&eacute;todo encargado de verificar si existe o no la imagen a cargar
	 * seg&uacute;n una ruta dada
	 * 
	 * @param rutaLogo
	 * @return imagenExiste
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public boolean existeLogo(String rutaLogo) {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
		boolean imagenExiste = false;

		try {
			myInFile = loader.getResourceAsStream(rutaLogo);

			if (myInFile != null) {
				imagenExiste = true;
				myInFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imagenExiste;
	}

}
