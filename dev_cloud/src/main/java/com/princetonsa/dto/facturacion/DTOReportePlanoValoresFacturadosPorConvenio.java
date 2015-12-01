package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

public class DTOReportePlanoValoresFacturadosPorConvenio implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombreInstitucion;
	private String nombreCentroAtencion;
	private String nit;
	private String nombreEmpresa;
	private String nombreConvenio;
	private Double valorFacturado;
	private Double valorFacturasAnuladas;
	private String totalFacturado;
	private Double valorNetoFacturado;
	private String rangoFechasFacturas;	


	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreInstitucion.
	 * 
	 * @return nombreInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreInstitucion.
	 * 
	 * @param nombreInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @return nombreCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @param nombreCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nit.
	 * 
	 * @return nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nit.
	 * 
	 * @param nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEmpresa.
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
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreConvenio.
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
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * valorFacturado.
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

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * valorFacturasAnuladas.
	 * 
	 * @return valorFacturasAnuladas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getValorFacturasAnuladas() {
		return valorFacturasAnuladas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * valorFacturasAnuladas.
	 * 
	 * @param valorFacturasAnuladas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValorFacturasAnuladas(Double valorFacturasAnuladas) {
		this.valorFacturasAnuladas = valorFacturasAnuladas;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * totalFacturado.
	 * 
	 * @return totalFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getTotalFacturado() {
		return totalFacturado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * totalFacturado.
	 * 
	 * @param totalFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setTotalFacturado(String totalFacturado) {
		this.totalFacturado = totalFacturado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * valorNetoFacturado.
	 * 
	 * @param valorNetoFacturado
	 * @author Diana Ruiz
	 */	

	public void setValorNetoFacturado(Double valorNetoFacturado) {
		this.valorNetoFacturado = valorNetoFacturado;
	}
	
	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valorNetoFacturado.
	 * 
	 * @return valorNetoFacturado
	 * @author Diana Ruiz
	 */

	public Double getValorNetoFacturado() {
		return valorNetoFacturado;
	}

	
	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * RangoFechasFacturas.
	 * 
	 * @param RangoFechasFacturas
	 * @author Diana Ruiz
	 */	

	public void setRangoFechasFacturas(String rangoFechasFacturas) {
		this.rangoFechasFacturas = rangoFechasFacturas;
	}
	
	/**
	 * M&eacute;todo encargado de obtener el valor del atributo RangoFechasFacturas.
	 * 
	 * @return RangoFechasFacturas
	 * @author Diana Ruiz
	 */

	public String getRangoFechasFacturas() {
		return rangoFechasFacturas;
	}

}
