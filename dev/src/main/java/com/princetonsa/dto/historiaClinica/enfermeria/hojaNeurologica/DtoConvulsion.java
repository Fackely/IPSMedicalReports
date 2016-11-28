package com.princetonsa.dto.historiaClinica.enfermeria.hojaNeurologica;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoConvulsion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigo;
	private String fecha;
	private String hora;
	private String tipoConvulsion;
	private String usuario;
	private String observacion;
	
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
	public String getTipoConvulsion() {
		return tipoConvulsion;
	}
	public void setTipoConvulsion(String tipoConvulsion) {
		this.tipoConvulsion = tipoConvulsion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
