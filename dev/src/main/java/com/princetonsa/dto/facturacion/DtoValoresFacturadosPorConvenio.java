package com.princetonsa.dto.facturacion;

import java.io.Serializable;

/**
 * Esta clase se encarga de determinar los valores facturados para un convenio.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 06/12/2010
 */
public class DtoValoresFacturadosPorConvenio implements Serializable {

	private static final long serialVersionUID = 1L;
	private int codigoConvenio;
	private int codigoEmpresa;
	private Double valorFacturado;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoConvenio.
	 * 
	 * @return codigoConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoConvenio.
	 * 
	 * @param codigoConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoEmpresa.
	 * 
	 * @return codigoEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEmpresa.
	 * 
	 * @param codigoEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valorFacturado.
	 * 
	 * @return valorFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getValorFacturado() {
		return valorFacturado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * valorFacturado.
	 * 
	 * @param valorFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValorFacturado(Double valorFacturado) {
		this.valorFacturado = valorFacturado;
	}

}
