package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * PoolesConvenio generated by hbm2java
 */
public class PoolesConvenio implements java.io.Serializable {

	private PoolesConvenioId id;
	private CuentasContables cuentasContablesByCuentaContablePool;
	private Usuarios usuarios;
	private CuentasContables cuentasContablesByCueContInstVigAnterior;
	private Instituciones instituciones;
	private Pooles pooles;
	private Convenios convenios;
	private CuentasContables cuentasContablesByCuentaContableIns;
	private Double porcentajeParticipacion;
	private Date fechaModifica;
	private String horaModifica;
	private Double valorParticipacion;

	public PoolesConvenio() {
	}

	public PoolesConvenio(PoolesConvenioId id, Usuarios usuarios,
			Instituciones instituciones, Pooles pooles, Convenios convenios,
			Date fechaModifica, String horaModifica) {
		this.id = id;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.pooles = pooles;
		this.convenios = convenios;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public PoolesConvenio(PoolesConvenioId id,
			CuentasContables cuentasContablesByCuentaContablePool,
			Usuarios usuarios,
			CuentasContables cuentasContablesByCueContInstVigAnterior,
			Instituciones instituciones, Pooles pooles, Convenios convenios,
			CuentasContables cuentasContablesByCuentaContableIns,
			Double porcentajeParticipacion, Date fechaModifica,
			String horaModifica, Double valorParticipacion) {
		this.id = id;
		this.cuentasContablesByCuentaContablePool = cuentasContablesByCuentaContablePool;
		this.usuarios = usuarios;
		this.cuentasContablesByCueContInstVigAnterior = cuentasContablesByCueContInstVigAnterior;
		this.instituciones = instituciones;
		this.pooles = pooles;
		this.convenios = convenios;
		this.cuentasContablesByCuentaContableIns = cuentasContablesByCuentaContableIns;
		this.porcentajeParticipacion = porcentajeParticipacion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.valorParticipacion = valorParticipacion;
	}

	public PoolesConvenioId getId() {
		return this.id;
	}

	public void setId(PoolesConvenioId id) {
		this.id = id;
	}

	public CuentasContables getCuentasContablesByCuentaContablePool() {
		return this.cuentasContablesByCuentaContablePool;
	}

	public void setCuentasContablesByCuentaContablePool(
			CuentasContables cuentasContablesByCuentaContablePool) {
		this.cuentasContablesByCuentaContablePool = cuentasContablesByCuentaContablePool;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public CuentasContables getCuentasContablesByCueContInstVigAnterior() {
		return this.cuentasContablesByCueContInstVigAnterior;
	}

	public void setCuentasContablesByCueContInstVigAnterior(
			CuentasContables cuentasContablesByCueContInstVigAnterior) {
		this.cuentasContablesByCueContInstVigAnterior = cuentasContablesByCueContInstVigAnterior;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Pooles getPooles() {
		return this.pooles;
	}

	public void setPooles(Pooles pooles) {
		this.pooles = pooles;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public CuentasContables getCuentasContablesByCuentaContableIns() {
		return this.cuentasContablesByCuentaContableIns;
	}

	public void setCuentasContablesByCuentaContableIns(
			CuentasContables cuentasContablesByCuentaContableIns) {
		this.cuentasContablesByCuentaContableIns = cuentasContablesByCuentaContableIns;
	}

	public Double getPorcentajeParticipacion() {
		return this.porcentajeParticipacion;
	}

	public void setPorcentajeParticipacion(Double porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
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

	public Double getValorParticipacion() {
		return this.valorParticipacion;
	}

	public void setValorParticipacion(Double valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
	}

}
