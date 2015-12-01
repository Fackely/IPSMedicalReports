package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;

/**
 * HistoricoDistribucion generated by hbm2java
 */
public class HistoricoDistribucion implements java.io.Serializable {

	private HistoricoDistribucionId id;
	private Ingresos ingresos;
	private Convenios convenios;
	private BigDecimal porcentajeAutorizado;
	private BigDecimal montoAutorizado;
	private String nroAutorizacion;

	public HistoricoDistribucion() {
	}

	public HistoricoDistribucion(HistoricoDistribucionId id, Ingresos ingresos,
			Convenios convenios) {
		this.id = id;
		this.ingresos = ingresos;
		this.convenios = convenios;
	}

	public HistoricoDistribucion(HistoricoDistribucionId id, Ingresos ingresos,
			Convenios convenios, BigDecimal porcentajeAutorizado,
			BigDecimal montoAutorizado, String nroAutorizacion) {
		this.id = id;
		this.ingresos = ingresos;
		this.convenios = convenios;
		this.porcentajeAutorizado = porcentajeAutorizado;
		this.montoAutorizado = montoAutorizado;
		this.nroAutorizacion = nroAutorizacion;
	}

	public HistoricoDistribucionId getId() {
		return this.id;
	}

	public void setId(HistoricoDistribucionId id) {
		this.id = id;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public BigDecimal getPorcentajeAutorizado() {
		return this.porcentajeAutorizado;
	}

	public void setPorcentajeAutorizado(BigDecimal porcentajeAutorizado) {
		this.porcentajeAutorizado = porcentajeAutorizado;
	}

	public BigDecimal getMontoAutorizado() {
		return this.montoAutorizado;
	}

	public void setMontoAutorizado(BigDecimal montoAutorizado) {
		this.montoAutorizado = montoAutorizado;
	}

	public String getNroAutorizacion() {
		return this.nroAutorizacion;
	}

	public void setNroAutorizacion(String nroAutorizacion) {
		this.nroAutorizacion = nroAutorizacion;
	}

}
