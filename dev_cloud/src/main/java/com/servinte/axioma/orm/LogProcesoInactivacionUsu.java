package com.servinte.axioma.orm;

// Generated Dec 9, 2010 9:46:02 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * LogProcesoInactivacionUsu generated by hbm2java
 */
public class LogProcesoInactivacionUsu implements java.io.Serializable {

	private int codigo;
	private Date fechaEjecucion;
	private String horaEjecucion;
	private String exitoso;
	private Integer cantidad;

	public LogProcesoInactivacionUsu() {
	}

	public LogProcesoInactivacionUsu(int codigo, Date fechaEjecucion,
			String horaEjecucion, String exitoso) {
		this.codigo = codigo;
		this.fechaEjecucion = fechaEjecucion;
		this.horaEjecucion = horaEjecucion;
		this.exitoso = exitoso;
	}

	public LogProcesoInactivacionUsu(int codigo, Date fechaEjecucion,
			String horaEjecucion, String exitoso, Integer cantidad) {
		this.codigo = codigo;
		this.fechaEjecucion = fechaEjecucion;
		this.horaEjecucion = horaEjecucion;
		this.exitoso = exitoso;
		this.cantidad = cantidad;
	}

	public LogProcesoInactivacionUsu(Date fechaEjecucion,
			String horaEjecucion, String exitoso, Integer cantidad) {
		this.fechaEjecucion = fechaEjecucion;
		this.horaEjecucion = horaEjecucion;
		this.exitoso = exitoso;
		this.cantidad = cantidad;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Date getFechaEjecucion() {
		return this.fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public String getHoraEjecucion() {
		return this.horaEjecucion;
	}

	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}

	public String getExitoso() {
		return this.exitoso;
	}

	public void setExitoso(String exitoso) {
		this.exitoso = exitoso;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

}
