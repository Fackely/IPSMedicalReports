package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

/**
 * Esta clase se encarga de almacenar los datos de las facturas de los convenios
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 03/12/2010
 */
public class DTOFacturasConvenios implements Serializable {

	private static final long serialVersionUID = 1L;
	private int codigoEmpresa;
	private int codigoConvenio;
	private Date fechaFactura;
	private int estadoFactura;
	private String nit;
	private String nombreEmpresa;
	private String nombreConvenio;
	private String numeroContrato;
	private Double valor;

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
	 * M&eacute;todo encargado de obtener el valor del atributo fechaFactura.
	 * 
	 * @return fechaFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Date getFechaFactura() {
		return fechaFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo fechaFactura.
	 * 
	 * @param fechaFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo estadoFactura.
	 * 
	 * @return estadoFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getEstadoFactura() {
		return estadoFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * estadoFactura.
	 * 
	 * @param estadoFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setEstadoFactura(int estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nit.
	 * 
	 * @return nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo nit.
	 * 
	 * @param nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreEmpresa.
	 * 
	 * @return nombreEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEmpresa.
	 * 
	 * @param nombreEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreConvenio.
	 * 
	 * @return nombreConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreConvenio.
	 * 
	 * @param nombreConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo numeroContrato.
	 * 
	 * @return numeroContrato
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * numeroContrato.
	 * 
	 * @param numeroContrato
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valor.
	 * 
	 * @return valor
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo valor.
	 * 
	 * @param valor
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}

}
