package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de almacenar la informaci&oacute;n general del reporte
 * plano de valores facturados por convenio.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 06/12/2010
 */
public class DTOGeneralReportePlanoValoresFacturadosPorConvenio implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private String fechaInicialFactura;
	private String fechaFinalFactura;
	private JRBeanCollectionDataSource dsValoresFacturadosConvenio;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * fechaInicialFactura.
	 * 
	 * @return fechaInicialFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaInicialFactura() {
		return fechaInicialFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * fechaInicialFactura
	 * 
	 * @param fechaInicialFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaInicialFactura(String fechaInicialFactura) {
		this.fechaInicialFactura = fechaInicialFactura;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * fechaFinalFactura.
	 * 
	 * @return fechaFinalFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaFinalFactura() {
		return fechaFinalFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * fechaFinalFactura
	 * 
	 * @param fechaFinalFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaFinalFactura(String fechaFinalFactura) {
		this.fechaFinalFactura = fechaFinalFactura;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * dsValoresFacturadosConvenio.
	 * 
	 * @return dsValoresFacturadosConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public JRBeanCollectionDataSource getDsValoresFacturadosConvenio() {
		return dsValoresFacturadosConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * dsValoresFacturadosConvenio
	 * 
	 * @param dsValoresFacturadosConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDsValoresFacturadosConvenio(
			JRBeanCollectionDataSource dsValoresFacturadosConvenio) {
		this.dsValoresFacturadosConvenio = dsValoresFacturadosConvenio;
	}

}
