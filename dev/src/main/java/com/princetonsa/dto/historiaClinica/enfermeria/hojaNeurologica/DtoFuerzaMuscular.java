package com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoFuerzaMuscular implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigo;
	private String fecha;
	private String hora;
	private String miembro;
	private String costado;
	private String resultado;
	private String usuario;
	private String observaciones;
	
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
	public String getMiembro() {
		return miembro;
	}
	public void setMiembro(String miembro) {
		this.miembro = miembro;
	}
	public String getCostado() {
		return costado;
	}
	public void setCostado(String costado) {
		this.costado = costado;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
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

}
