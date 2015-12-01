package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * PorcentajeAgruArtExce generated by hbm2java
 */
public class PorcentajeAgruArtExce implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private AgruArtExceTariCont agruArtExceTariCont;
	private BigDecimal porcentajeExcepcion;
	private Date fechaModifica;
	private String horaModifica;
	private Integer prioridad;

	public PorcentajeAgruArtExce() {
	}

	public PorcentajeAgruArtExce(long codigo, Usuarios usuarios,
			AgruArtExceTariCont agruArtExceTariCont,
			BigDecimal porcentajeExcepcion, Date fechaModifica,
			String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.agruArtExceTariCont = agruArtExceTariCont;
		this.porcentajeExcepcion = porcentajeExcepcion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public PorcentajeAgruArtExce(long codigo, Usuarios usuarios,
			AgruArtExceTariCont agruArtExceTariCont,
			BigDecimal porcentajeExcepcion, Date fechaModifica,
			String horaModifica, Integer prioridad) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.agruArtExceTariCont = agruArtExceTariCont;
		this.porcentajeExcepcion = porcentajeExcepcion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.prioridad = prioridad;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public AgruArtExceTariCont getAgruArtExceTariCont() {
		return this.agruArtExceTariCont;
	}

	public void setAgruArtExceTariCont(AgruArtExceTariCont agruArtExceTariCont) {
		this.agruArtExceTariCont = agruArtExceTariCont;
	}

	public BigDecimal getPorcentajeExcepcion() {
		return this.porcentajeExcepcion;
	}

	public void setPorcentajeExcepcion(BigDecimal porcentajeExcepcion) {
		this.porcentajeExcepcion = porcentajeExcepcion;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public Integer getPrioridad() {
		return this.prioridad;
	}

	public void setPrioridad(Integer prioridad) {
		this.prioridad = prioridad;
	}

}
