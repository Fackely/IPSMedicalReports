package com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoControlEsfinteres implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigo;
	private String fecha;
	private String hora;
	private String ctrlEsfinter;
	private String usuario;
	private String observaciones;
	private Boolean ausente;
	
	public BigDecimal getCodigo() {
		return codigo;
	}
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getCtrlEsfinter() {
		return ctrlEsfinter;
	}
	public void setCtrlEsfinter(String ctrlEsfinter) {
		this.ctrlEsfinter = ctrlEsfinter;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Boolean getAusente() {
		return ausente;
	}
	public void setAusente(Boolean ausente) {
		this.ausente = ausente;
	}
	
	
}
