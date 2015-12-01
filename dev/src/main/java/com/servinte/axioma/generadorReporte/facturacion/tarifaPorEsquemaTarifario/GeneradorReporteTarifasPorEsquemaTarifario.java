package com.servinte.axioma.generadorReporte.facturacion.tarifaPorEsquemaTarifario;

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

import com.princetonsa.dto.facturacion.DTOEsquemasReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DTOGeneralReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;

/**
 * 
 * Esta clase se encarga de generar el reporte de las tarifas por esquema
 * tarifario
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 23/11/2010
 */
public class GeneradorReporteTarifasPorEsquemaTarifario extends
		GeneradorReporte {

	/** Par&aacute;metros del reporte. */
	Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	private DtoReporteTarifasPorEsquemaTarifario dtoFiltros;
	private DTOEsquemasReporteTarifasPorEsquemaTarifario esquemaTarifario;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/facturacion/tarifaPorEsquemaTarifario/reporteTarifasEsquemaTarifario.jasper";
	private static String RUTA_SUBREPORTE_TARIFAS = "com/servinte/axioma/generadorReporte/facturacion/tarifaPorEsquemaTarifario/subreporteTarifasEsquemaTarifario.jasper";
	private static String NOMBRE_SUBREPORTE_TARIFAS = "subreporteTarifasEsquemaTarifario";

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReporteTarifasPorEsquemaTarifario() {
	}

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @param dtoFiltros
	 * @param esquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReporteTarifasPorEsquemaTarifario(
			DtoReporteTarifasPorEsquemaTarifario dtoFiltros,
			DTOEsquemasReporteTarifasPorEsquemaTarifario esquemaTarifario) {
		this.dtoFiltros = dtoFiltros;
		this.esquemaTarifario = esquemaTarifario;
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

		DTOGeneralReporteTarifasPorEsquemaTarifario dtoGeneral = 
			new DTOGeneralReporteTarifasPorEsquemaTarifario();

		Collection<DTOGeneralReporteTarifasPorEsquemaTarifario> collectionDTOGeneral = 
			new ArrayList<DTOGeneralReporteTarifasPorEsquemaTarifario>();

		ArrayList<DtoReporteTarifasPorEsquemaTarifario> listaReporte = 
			new ArrayList<DtoReporteTarifasPorEsquemaTarifario>();

		DtoReporteTarifasPorEsquemaTarifario dtoReporte = null;
		if (dtoFiltros != null) {

			DTOEsquemasReporteTarifasPorEsquemaTarifario esquema = esquemaTarifario;

			String ubicacionLogo = dtoFiltros.getUbicacionLogo();
			String rutaLogo = "../" + dtoFiltros.getRutaLogo();

			dtoGeneral.setRazonSocial(dtoFiltros.getRazonSocial());
			dtoGeneral.setNombreEsquemaTarifario(dtoFiltros.getNombreEsquemaTarifario());
			dtoGeneral.setNombreServicio(dtoFiltros.getDescripcionServicio());
			dtoGeneral.setUsuarioProceso(dtoFiltros.getUsuarioProceso());

			boolean existeLogo = existeLogo(rutaLogo);
			if (existeLogo) {
				if (ubicacionLogo
						.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
					dtoGeneral.setLogoDerecha(rutaLogo);
					dtoGeneral.setLogoIzquierda(null);
				} else if (ubicacionLogo
						.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
					dtoGeneral.setLogoIzquierda(rutaLogo);
					dtoGeneral.setLogoDerecha(null);
				}
			} else {
				dtoGeneral.setLogoDerecha(null);
				dtoGeneral.setLogoIzquierda(null);
			}

			ArrayList<String> listaTarifas = null;
			if (dtoFiltros.isUtilizaProgramasOdontologicos()) {
				int i = 0;
				long codProg = esquema.getListaTarifasServicios().get(i).getCodigoPrograma();
				Double valor = 0D;
				for (DTOTarifasServicios registro : esquema.getListaTarifasServicios()) {
					if (codProg == registro.getCodigoPrograma()) {
						valor = valor + registro.getTarifaServicio();
					} else {
						if (listaTarifas == null) {
							listaTarifas = new ArrayList<String>();
						}
						listaTarifas.add(UtilidadTexto.formatearValores(valor));
						codProg = registro.getCodigoPrograma();
						valor = 0D;
						valor = valor + registro.getTarifaServicio();
					}
					i++;
				}
				if (listaTarifas == null) {
					listaTarifas = new ArrayList<String>();
				}
				listaTarifas.add(UtilidadTexto.formatearValores(valor));
			}

			int i = 0;
			long codProg = esquema.getListaTarifasServicios().get(i).getCodigoPrograma();
			for (DTOTarifasServicios registro : esquema.getListaTarifasServicios()) {

				dtoReporte = new DtoReporteTarifasPorEsquemaTarifario();

				if (dtoFiltros.isUtilizaProgramasOdontologicos()) {
					dtoReporte.setCodPrograma(String.valueOf(registro.getCodigoPrograma()));
					dtoReporte.setUtilizaProgramasOdontologicos(dtoFiltros.isUtilizaProgramasOdontologicos());
					dtoReporte.setNombrePrograma(registro.getNombrePrograma());
					dtoReporte.setNombreEspecialidad(registro.getNombreEspecialdad());
	
					if (codProg == registro.getCodigoPrograma()) {
						dtoReporte.setValor(listaTarifas.get(i));
					} else {
						codProg = registro.getCodigoPrograma();
						dtoReporte.setValor(listaTarifas.get(i+1));
						i++;
					}
				}

				dtoReporte.setCodigoServicio(String.valueOf(registro.getCodigoServicio()));
				dtoReporte.setDescripcionServicio(registro.getDescripcionServicio());
				if (registro.getTarifaServicio() != 0D) {
					dtoReporte.setTarifaServicio(UtilidadTexto
							.formatearValores(registro.getTarifaServicio()));
				} else {
					dtoReporte.setTarifaServicio("No definido");
				}

				listaReporte.add(dtoReporte);
			}
			dtoGeneral.setDsListaTarifasServicios(new JRBeanCollectionDataSource(listaReporte));
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
