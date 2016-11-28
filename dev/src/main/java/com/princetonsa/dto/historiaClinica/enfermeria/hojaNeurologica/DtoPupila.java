package com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoPupila implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigo;
	private String lado;
	private String fechaRegistro;
	private String horaRegistro;
	private Integer valorTamanio;
	private String abreviaturaTamanio;
	private String nombreTamanio;
	private String nombreReaccion;
	private String observaciones;
	private String usuario;
	
	public BigDecimal getCodigo() {
		return codigo;
	}
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}
	public String getLado() {
		return lado;
	}
	public void setLado(String lado) {
		this.lado = lado;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getHoraRegistro() {
		return horaRegistro;
	}
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}
	public Integer getValorTamanio() {
		return valorTamanio;
	}
	public void setValorTamanio(Integer valorTamanio) {
		this.valorTamanio = valorTamanio;
	}
	public String getAbreviaturaTamanio() {
		return abreviaturaTamanio;
	}
	public void setAbreviaturaTamanio(String abreviaturaTamanio) {
		this.abreviaturaTamanio = abreviaturaTamanio;
	}
	public String getNombreTamanio() {
		return nombreTamanio;
	}
	public void setNombreTamanio(String nombreTamanio) {
		this.nombreTamanio = nombreTamanio;
	}
	public String getNombreReaccion() {
		return nombreReaccion;
	}
	public void setNombreReaccion(String nombreReaccion) {
		this.nombreReaccion = nombreReaccion;
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

}
