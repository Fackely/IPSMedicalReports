package com.servinte.axioma.generadorReporte.facturacion.tarifaPorEsquemaTarifario;

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

import com.princetonsa.dto.facturacion.DTOEsquemasReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DTOGeneralReportePlanoTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DTOReportePlanoTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DTOTarifasServicios;

/**
 * 
 * Esta clase se encarga de generar el reporte plano de las tarifas por esquema
 * tarifario
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 23/11/2010
 */
public class GeneradorReportePlanoTarifasPorEsquemaTarifario extends
		GeneradorReporte {

	/** Par&aacute;metros del reporte. */
	Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	private boolean utilizaProgramasOdontologicos;
	private DTOEsquemasReporteTarifasPorEsquemaTarifario esquemaTarifario;
	private static String RUTA_REPORTE = "com/servinte/axioma/generadorReporte/facturacion/tarifaPorEsquemaTarifario/reportePlanoTarifasEsquemaTarifario.jasper";
	private static String RUTA_SUBREPORTE = "com/servinte/axioma/generadorReporte/facturacion/tarifaPorEsquemaTarifario/subreportePlanoTarifasEsquemaTarifario.jasper";
	private static String NOMBRE_SUBREPORTE = "subreportePlanoTarifasEsquemaTarifario";

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReportePlanoTarifasPorEsquemaTarifario() {
	}

	/**
	 * 
	 * M&eacute;todo constructor de la clase.
	 * 
	 * @param utilizaProgramasOdontologicos
	 * @param esquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public GeneradorReportePlanoTarifasPorEsquemaTarifario(
			boolean utilizaProgramasOdontologicos,
			DTOEsquemasReporteTarifasPorEsquemaTarifario esquemaTarifario) {
		this.utilizaProgramasOdontologicos = utilizaProgramasOdontologicos;
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

		DTOGeneralReportePlanoTarifasPorEsquemaTarifario dtoGeneral = 
			new DTOGeneralReportePlanoTarifasPorEsquemaTarifario();

		Collection<DTOGeneralReportePlanoTarifasPorEsquemaTarifario>  collectionDTOGeneral= 
			new ArrayList<DTOGeneralReportePlanoTarifasPorEsquemaTarifario>();

		ArrayList<DTOReportePlanoTarifasPorEsquemaTarifario> listaReportePlano = 
			new ArrayList<DTOReportePlanoTarifasPorEsquemaTarifario>();

		DTOReportePlanoTarifasPorEsquemaTarifario dtoReportePlano = null;

		DTOEsquemasReporteTarifasPorEsquemaTarifario esquema = esquemaTarifario;

		if ((esquema.getListaTarifasServicios() != null)
				&& (esquema.getListaTarifasServicios().size() > 0)) {

			ArrayList<String> listaTarifas = null;
			if (utilizaProgramasOdontologicos) {
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

				dtoReportePlano = new DTOReportePlanoTarifasPorEsquemaTarifario();

				dtoReportePlano.setNombreEsquemaTarifario(esquema.getNombreEsquemaTarifario());
				if (utilizaProgramasOdontologicos) {
					dtoReportePlano.setCodPrograma(String.valueOf(registro.getCodigoPrograma()));
					dtoReportePlano.setNombrePrograma(registro.getNombrePrograma());

					if (codProg == registro.getCodigoPrograma()) {
						dtoReportePlano.setValor(listaTarifas.get(i));
					} else {
						codProg = registro.getCodigoPrograma();
						dtoReportePlano.setValor(listaTarifas.get(i+1));
						i++;
					}
				}

				dtoReportePlano.setNombreEspecialidad(registro.getNombreEspecialdad());
				dtoReportePlano.setCodigoServicio(registro.getCodigoServicio());
				dtoReportePlano.setDescripcionServicio(registro.getDescripcionServicio());
				if (registro.getTarifaServicio() != 0D) {
					dtoReportePlano.setTarifa(UtilidadTexto
							.formatearValores(registro.getTarifaServicio()));
				} else {
					dtoReportePlano.setTarifa("No definido");
				}

				listaReportePlano.add(dtoReportePlano);
			}
			dtoGeneral.setDsTarifasEsquemaTarifario(new JRBeanCollectionDataSource(listaReportePlano));
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

}
