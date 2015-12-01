package com.princetonsa.dto.facturacion;

import java.io.Serializable;

public class DTOReportePlanoTarifasPorEsquemaTarifario implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombreEsquemaTarifario;
	private String codPrograma = null;
	private String nombrePrograma;
	private String nombreEspecialidad;
	private String valor;
	private String codigoServicio;
	private String descripcionServicio;
	private String tarifa;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEsquemaTarifario.
	 * 
	 * @return nombreEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEsquemaTarifario() {
		return nombreEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEsquemaTarifario.
	 * 
	 * @param nombreEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario) {
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * codPrograma.
	 * 
	 * @return codPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodPrograma() {
		return codPrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codPrograma.
	 * 
	 * @param codPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodPrograma(String codPrograma) {
		this.codPrograma = codPrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombrePrograma.
	 * 
	 * @return nombrePrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombrePrograma.
	 * 
	 * @param nombrePrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEspecialidad.
	 * 
	 * @return nombreEspecialidad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEspecialidad.
	 * 
	 * @param nombreEspecialidad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valor.
	 * 
	 * @return valor
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo valor.
	 * 
	 * @param valor
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoServicio.
	 * 
	 * @return codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoServicio.
	 * 
	 * @param codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * descripcionServicio.
	 * 
	 * @return descripcionServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * descripcionServicio.
	 * 
	 * @param descripcionServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo tarifa.
	 * 
	 * @return tarifa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getTarifa() {
		return tarifa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo tarifa.
	 * 
	 * @param tarifa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}

}
