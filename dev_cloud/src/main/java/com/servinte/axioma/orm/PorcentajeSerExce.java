package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * PorcentajeSerExce generated by hbm2java
 */
public class PorcentajeSerExce implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private SerExceTariCont serExceTariCont;
	private BigDecimal porcentajeExcepcion;
	private Date fechaModifica;
	private String horaModifica;
	private Integer prioridad;

	public PorcentajeSerExce() {
	}

	public PorcentajeSerExce(long codigo, Usuarios usuarios,
			SerExceTariCont serExceTariCont, BigDecimal porcentajeExcepcion,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.serExceTariCont = serExceTariCont;
		this.porcentajeExcepcion = porcentajeExcepcion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public PorcentajeSerExce(long codigo, Usuarios usuarios,
			SerExceTariCont serExceTariCont, BigDecimal porcentajeExcepcion,
			Date fechaModifica, String horaModifica, Integer prioridad) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.serExceTariCont = serExceTariCont;
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

	public SerExceTariCont getSerExceTariCont() {
		return this.serExceTariCont;
	}

	public void setSerExceTariCont(SerExceTariCont serExceTariCont) {
		this.serExceTariCont = serExceTariCont;
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
