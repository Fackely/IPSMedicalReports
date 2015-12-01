package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de almacenar la informaci&oacute;n general del reporte
 * plano de tarifas por esquemas tarifarios.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 23/11/2010
 */
public class DTOGeneralReportePlanoTarifasPorEsquemaTarifario implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private JRBeanCollectionDataSource dsTarifasEsquemaTarifario;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * dsTarifasEsquemaTarifario.
	 * 
	 * @return dsTarifasEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public JRBeanCollectionDataSource getDsTarifasEsquemaTarifario() {
		return dsTarifasEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * dsTarifasEsquemaTarifario
	 * 
	 * @param dsTarifasEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDsTarifasEsquemaTarifario(
			JRBeanCollectionDataSource dsTarifasEsquemaTarifario) {
		this.dsTarifasEsquemaTarifario = dsTarifasEsquemaTarifario;
	}

}
