package com.servinte.axioma.orm;

// Generated 20/05/2011 04:42:54 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * LogDetalleParamPresup generated by hbm2java
 */
public class LogDetalleParamPresup implements java.io.Serializable {

	private long codigo;
	private LogParamPresupuestoCap logParamPresupuestoCap;
	private String nivelAtencion;
	private String nivelModificacion;
	private String descipcion;
	private String mes;
	private BigDecimal porcentajeAnterior;
	private BigDecimal porcentajeActual;

	public LogDetalleParamPresup() {
	}

	public LogDetalleParamPresup(long codigo,
			LogParamPresupuestoCap logParamPresupuestoCap,
			String nivelAtencion, String nivelModificacion, String descipcion,
			String mes, BigDecimal porcentajeAnterior,
			BigDecimal porcentajeActual) {
		this.codigo = codigo;
		this.logParamPresupuestoCap = logParamPresupuestoCap;
		this.nivelAtencion = nivelAtencion;
		this.nivelModificacion = nivelModificacion;
		this.descipcion = descipcion;
		this.mes = mes;
		this.porcentajeAnterior = porcentajeAnterior;
		this.porcentajeActual = porcentajeActual;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public LogParamPresupuestoCap getLogParamPresupuestoCap() {
		return this.logParamPresupuestoCap;
	}

	public void setLogParamPresupuestoCap(
			LogParamPresupuestoCap logParamPresupuestoCap) {
		this.logParamPresupuestoCap = logParamPresupuestoCap;
	}

	public String getNivelAtencion() {
		return this.nivelAtencion;
	}

	public void setNivelAtencion(String nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	public String getNivelModificacion() {
		return this.nivelModificacion;
	}

	public void setNivelModificacion(String nivelModificacion) {
		this.nivelModificacion = nivelModificacion;
	}

	public String getDescipcion() {
		return this.descipcion;
	}

	public void setDescipcion(String descipcion) {
		this.descipcion = descipcion;
	}

	public String getMes() {
		return this.mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public BigDecimal getPorcentajeAnterior() {
		return this.porcentajeAnterior;
	}

	public void setPorcentajeAnterior(BigDecimal porcentajeAnterior) {
		this.porcentajeAnterior = porcentajeAnterior;
	}

	public BigDecimal getPorcentajeActual() {
		return this.porcentajeActual;
	}

	public void setPorcentajeActual(BigDecimal porcentajeActual) {
		this.porcentajeActual = porcentajeActual;
	}

}
