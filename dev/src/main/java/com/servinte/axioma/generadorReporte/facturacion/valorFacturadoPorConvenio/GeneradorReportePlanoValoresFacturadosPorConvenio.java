package com.servinte.axioma.generadorReporte.facturacion.valorFacturadoPorConvenio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.axioma.util.log.Log4JManager;

import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.facturacion.DTOConveniosReporteValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DTOGeneralReportePlanoValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DTOReportePlanoValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.princetonsa.enu.general.CarpetasArchivos;

/**
 * 
 * Esta clase se encarga de generar el reporte plano de los valores facturados
 * por convenio
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 06/12/2010
 */
public class GeneradorReportePlanoValoresFacturadosPorConvenio extends
		GeneradorReporte {

	/** Par&aacute;metros del reporte. */
	Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	private DtoReporteValoresFacturadosPorConvenio dtoFiltros;
	private DTOConveniosReporteValoresFacturadosPorConvenio convenio;
	private static String RUTA_REPORTE = "com/servinte/axioma/generadorReporte/facturacion/valorFacturadoPorConvenio/reportePlanoValoresFacturadosConvenio.jasper";
	private static String RUTA_SUBREPORTE = "com/servinte/axioma/generadorReporte/facturacion/valorFacturadoPorConvenio/subreportePlanoValoresFacturadosConvenio.jasper";
	private static String NOMBRE_SUBREPORTE = "subreportePlanoValoresFacturadosConvenio";
	private static String ETIQUETA_TODAS="Todas";
	private static String ETIQUETA_TODOS="Todos";

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReportePlanoValoresFacturadosPorConvenio() {
	}

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @param convenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReportePlanoValoresFacturadosPorConvenio(
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

		DTOGeneralReportePlanoValoresFacturadosPorConvenio dtoGeneral = 
			new DTOGeneralReportePlanoValoresFacturadosPorConvenio();
		Collection<DTOGeneralReportePlanoValoresFacturadosPorConvenio>  collectionDTOGeneral= 
			new ArrayList<DTOGeneralReportePlanoValoresFacturadosPorConvenio>();

		ArrayList<DTOReportePlanoValoresFacturadosPorConvenio> listaReportePlano = 
			new ArrayList<DTOReportePlanoValoresFacturadosPorConvenio>();
		DTOReportePlanoValoresFacturadosPorConvenio dtoReportePlano = null;

		DTOConveniosReporteValoresFacturadosPorConvenio convenioValoresFacturados = convenio;

		if ((convenioValoresFacturados.getListaValoresFacturadosConvenios() != null)
				&& (convenioValoresFacturados.getListaValoresFacturadosConvenios().size() > 0)) {

			ArrayList<Double> listaValoresFacturados = null;
			ArrayList<Double> listaValoresFacturasAnuladas = null;
			ArrayList<String> listaTotalFacturado = null;

			int i = 0;
			int codEmpresa = convenioValoresFacturados.getListaValoresFacturadosConvenios().get(i).getCodigoEmpresa();
			int codConvenio = convenioValoresFacturados.getListaValoresFacturadosConvenios().get(i).getCodigoConvenio();
			Double valorFacturado = 0D;
			Double valorFacturasAnuladas = 0D;
			Double totalFacturado = 0D;
			Double valorNetoFacturado = 0D;		

			for (DTOFacturasConvenios registro : convenioValoresFacturados.getListaValoresFacturadosConvenios()) {
				if (codEmpresa == registro.getCodigoEmpresa()) {
					if (registro.getEstadoFactura() == 1) {
						totalFacturado = totalFacturado + registro.getValor();
					}
				} else {
					codEmpresa = registro.getCodigoEmpresa();
					if (listaTotalFacturado == null) {
						listaTotalFacturado = new ArrayList<String>();
					}
					listaTotalFacturado.add(UtilidadTexto.formatearValores(totalFacturado));
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
			listaValoresFacturados.add(valorFacturado);

			if (listaValoresFacturasAnuladas == null) {
				listaValoresFacturasAnuladas = new ArrayList<Double>();
			}
			listaValoresFacturasAnuladas.add(valorFacturasAnuladas);

			if (listaTotalFacturado == null) {
				listaTotalFacturado = new ArrayList<String>();
			}
			listaTotalFacturado.add(UtilidadTexto.formatearValores(totalFacturado));

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
						dtoReportePlano = new DTOReportePlanoValoresFacturadosPorConvenio();
					}

					dtoRegistrado=true;
					if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreEmpresaInstitucion())) {
						dtoReportePlano.setNombreInstitucion(dtoFiltros.getNombreEmpresaInstitucion());
					} else {
						dtoReportePlano.setNombreInstitucion(ETIQUETA_TODAS);
					}
					if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreCentroAtencion())) {
						dtoReportePlano.setNombreCentroAtencion(dtoFiltros.getNombreCentroAtencion());
					} else {
						dtoReportePlano.setNombreCentroAtencion(ETIQUETA_TODOS);
					}
					dtoReportePlano.setNit(sNit.replaceAll(",","."));
					dtoReportePlano.setNombreEmpresa(registro.getNombreEmpresa());
					dtoReportePlano.setRangoFechasFacturas(convenioValoresFacturados.getFechaInicialFactura()+ " - " + convenioValoresFacturados.getFechaFinalFactura());
					if (codConvenio == registro.getCodigoConvenio()) {
						dtoReportePlano.setNombreConvenio(registro.getNombreConvenio() + '-' + registro.getNumeroContrato());
					} else {
						codConvenio = registro.getCodigoConvenio();

						dtoRegistrado=false;
						dtoReportePlano.setValorFacturado(listaValoresFacturados.get(i));
						if (listaValoresFacturasAnuladas.get(i).equals(0.00)) {
							dtoReportePlano.setValorFacturasAnuladas(0D);
						} else {
							dtoReportePlano.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
						}
						
						valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
						dtoReportePlano.setValorNetoFacturado(valorNetoFacturado);
						
						dtoReportePlano.setTotalFacturado(listaTotalFacturado.get(j));
						i++;

						listaReportePlano.add(dtoReportePlano);

						if (!dtoRegistrado) {
							dtoReportePlano = new DTOReportePlanoValoresFacturadosPorConvenio();
						}

						dtoRegistrado=true;
						if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreEmpresaInstitucion())) {
							dtoReportePlano.setNombreInstitucion(dtoFiltros.getNombreEmpresaInstitucion());
						} else {
							dtoReportePlano.setNombreInstitucion(ETIQUETA_TODAS);
						}
						if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreCentroAtencion())) {
							dtoReportePlano.setNombreCentroAtencion(dtoFiltros.getNombreCentroAtencion());
						} else {
							dtoReportePlano.setNombreCentroAtencion(ETIQUETA_TODOS);
						}
						dtoReportePlano.setNit(sNit.replaceAll(",","."));
						dtoReportePlano.setNombreEmpresa(registro.getNombreEmpresa());
						dtoReportePlano.setRangoFechasFacturas(convenioValoresFacturados.getFechaInicialFactura()+ " - " + convenioValoresFacturados.getFechaFinalFactura());
						if (codConvenio == registro.getCodigoConvenio()) {
							dtoReportePlano.setNombreConvenio(registro.getNombreConvenio() + '-' + registro.getNumeroContrato());
						} else {
							codConvenio = registro.getCodigoConvenio();

							dtoRegistrado=false;
							dtoReportePlano.setValorFacturado(listaValoresFacturados.get(i));
							if (listaValoresFacturasAnuladas.get(i).equals(0.00)) {
								dtoReportePlano.setValorFacturasAnuladas(0D);
							} else {
								dtoReportePlano.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
							}
							
							valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
							dtoReportePlano.setValorNetoFacturado(valorNetoFacturado);
							
							dtoReportePlano.setTotalFacturado(listaTotalFacturado.get(j));
							i++;

							listaReportePlano.add(dtoReportePlano);
						}
					}

				} else {

					codEmpresa = registro.getCodigoEmpresa();
					codConvenio = registro.getCodigoConvenio();

					dtoRegistrado=false;
					dtoReportePlano.setValorFacturado(listaValoresFacturados.get(i));
					if (listaValoresFacturasAnuladas.get(i).equals(0.00)) {
						dtoReportePlano.setValorFacturasAnuladas(0D);
					} else {
						dtoReportePlano.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
					}
					
					valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
					dtoReportePlano.setValorNetoFacturado(valorNetoFacturado);
					
					dtoReportePlano.setTotalFacturado(listaTotalFacturado.get(j));
					i++;
					j++;

					listaReportePlano.add(dtoReportePlano);

					if (!dtoRegistrado) {
						dtoReportePlano = new DTOReportePlanoValoresFacturadosPorConvenio();
					}

					dtoRegistrado=true;
					if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreEmpresaInstitucion())) {
						dtoReportePlano.setNombreInstitucion(dtoFiltros.getNombreEmpresaInstitucion());
					} else {
						dtoReportePlano.setNombreInstitucion(ETIQUETA_TODAS);
					}
					if (!UtilidadTexto.isEmpty(dtoFiltros.getNombreCentroAtencion())) {
						dtoReportePlano.setNombreCentroAtencion(dtoFiltros.getNombreCentroAtencion());
					} else {
						dtoReportePlano.setNombreCentroAtencion(ETIQUETA_TODOS);
					}
					dtoReportePlano.setNit(sNit.replaceAll(",","."));
					dtoReportePlano.setNombreEmpresa(registro.getNombreEmpresa());
					dtoReportePlano.setRangoFechasFacturas(convenioValoresFacturados.getFechaInicialFactura()+ " - " + convenioValoresFacturados.getFechaFinalFactura());
					if (codConvenio == registro.getCodigoConvenio()) {
						dtoReportePlano.setNombreConvenio(registro.getNombreConvenio() + '-' + registro.getNumeroContrato());
					} else {
						codConvenio = registro.getCodigoConvenio();

						dtoRegistrado=false;
						dtoReportePlano.setValorFacturado(listaValoresFacturados.get(i));
						if (listaValoresFacturasAnuladas.get(i).equals(0.00)) {
							dtoReportePlano.setValorFacturasAnuladas(0D);
						} else {
							dtoReportePlano.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
						}
						valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
						dtoReportePlano.setValorNetoFacturado(valorNetoFacturado);
						
						dtoReportePlano.setTotalFacturado(listaTotalFacturado.get(j));
						i++;

						listaReportePlano.add(dtoReportePlano);
					}

				}

			}
			dtoReportePlano.setValorFacturado(listaValoresFacturados.get(i));
			if (listaValoresFacturasAnuladas.get(i).equals(0.00)) {
				dtoReportePlano.setValorFacturasAnuladas(0D);
			} else {
				dtoReportePlano.setValorFacturasAnuladas(listaValoresFacturasAnuladas.get(i));
			}
			
			valorNetoFacturado= listaValoresFacturados.get(i)- listaValoresFacturasAnuladas.get(i);
			dtoReportePlano.setValorNetoFacturado(valorNetoFacturado);
			
			dtoReportePlano.setTotalFacturado(listaTotalFacturado.get(j));

			listaReportePlano.add(dtoReportePlano);

			dtoGeneral.setFechaInicialFactura(convenioValoresFacturados.getFechaInicialFactura());
			dtoGeneral.setFechaFinalFactura(convenioValoresFacturados.getFechaFinalFactura());
			dtoGeneral.setDsValoresFacturadosConvenio(new JRBeanCollectionDataSource(listaReportePlano));
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
			myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE);
			if (myInFile == null) {
				myInFile = new FileInputStream(RUTA_SUBREPORTE);
			} else if (myInFile != null) {
				Object mySubreportObj = JRLoader.loadObject(myInFile);
				parametrosReporte
						.put(NOMBRE_SUBREPORTE, mySubreportObj);
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
		return RUTA_REPORTE;
	}

	/**
	 * M&eacute;todo encargado de sobreescribir m&eacute;todo
	 * exportarReporteTextoPlano de la superclase.
	 * 
	 * @param jasperPrint
	 * @param nombreReporte
	 * 
	 * @return nombreArchivo
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@Override
	public String exportarReporteTextoPlano(JasperPrint jasperPrint, String nombreReporte) {

		String nombreArchivo = CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator") + "ReporteValoresFacturadosConvenio" + new Random().nextInt() + ".csv";
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();

		File dir = new File(path + CarpetasArchivos.REPORTES_JASPER + System.getProperty("file.separator"));

		if (!dir.exists()) {
			if (dir.mkdirs()) {
				Log4JManager.info("***********************************se crea el directorio "+dir);
			}
		}

		try {

			JRCsvExporter csvExporter = new JRCsvExporter();
			csvExporter.setParameter(JRExporterParameter.JASPER_PRINT,
					jasperPrint);

			csvExporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, "|");

			csvExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					path + nombreArchivo);

			csvExporter.exportReport();

		} catch (JRException e) {
			e.printStackTrace();
		}

		return nombreArchivo;

	}

}
