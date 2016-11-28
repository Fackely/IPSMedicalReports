package com.princetonsa.dto.historiaClinica.enfermeria.escalaGlasgow;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoEscalaGlasgow implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigo;
	private String fechaHora;
	private String aperturaOjos;
	private String respuestaVerbal;
	private String respuestaMotora;
	private String observaciones;
	private String usuario;
	private String escalaGlasgow;
	
	public BigDecimal getCodigo() {
		return codigo;
	}
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}
	public String getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getAperturaOjos() {
		return aperturaOjos;
	}
	public void setAperturaOjos(String aperturaOjos) {
		this.aperturaOjos = aperturaOjos;
	}
	public String getRespuestaVerbal() {
		return respuestaVerbal;
	}
	public void setRespuestaVerbal(String respuestaVerbal) {
		this.respuestaVerbal = respuestaVerbal;
	}
	public String getRespuestaMotora() {
		return respuestaMotora;
	}
	public void setRespuestaMotora(String respuestaMotora) {
		this.respuestaMotora = respuestaMotora;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getEscalaGlasgow() {
		return escalaGlasgow;
	}
	public void setEscalaGlasgow(String escalaGlasgow) {
		this.escalaGlasgow = escalaGlasgow;
	}
}
