package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Cobertura generated by hbm2java
 */
public class Cobertura implements java.io.Serializable {

	private CoberturaId id;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private String descripCobertura;
	private String observCobertura;
	private Character activo;
	private Date fechaModifica;
	private String horaModifica;
	private String tipoCobertura;
	private Set coberturasXContratos = new HashSet(0);
	private Set coberturasEntidadSubs = new HashSet(0);
	private Set detalleCoberturas = new HashSet(0);
	private Set logCoberturasEntSubs = new HashSet(0);

	public Cobertura() {
	}

	public Cobertura(CoberturaId id, Usuarios usuarios,
			Instituciones instituciones, String descripCobertura,
			Date fechaModifica, String horaModifica) {
		this.id = id;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.descripCobertura = descripCobertura;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public Cobertura(CoberturaId id, Usuarios usuarios,
			Instituciones instituciones, String descripCobertura,
			String observCobertura, Character activo, Date fechaModifica,
			String horaModifica, String tipoCobertura,
			Set coberturasXContratos, Set coberturasEntidadSubs,
			Set detalleCoberturas, Set logCoberturasEntSubs) {
		this.id = id;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.descripCobertura = descripCobertura;
		this.observCobertura = observCobertura;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.tipoCobertura = tipoCobertura;
		this.coberturasXContratos = coberturasXContratos;
		this.coberturasEntidadSubs = coberturasEntidadSubs;
		this.detalleCoberturas = detalleCoberturas;
		this.logCoberturasEntSubs = logCoberturasEntSubs;
	}

	public CoberturaId getId() {
		return this.id;
	}

	public void setId(CoberturaId id) {
		this.id = id;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getDescripCobertura() {
		return this.descripCobertura;
	}

	public void setDescripCobertura(String descripCobertura) {
		this.descripCobertura = descripCobertura;
	}

	public String getObservCobertura() {
		return this.observCobertura;
	}

	public void setObservCobertura(String observCobertura) {
		this.observCobertura = observCobertura;
	}

	public Character getActivo() {
		return this.activo;
	}

	public void setActivo(Character activo) {
		this.activo = activo;
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

	public String getTipoCobertura() {
		return this.tipoCobertura;
	}

	public void setTipoCobertura(String tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}

	public Set getCoberturasXContratos() {
		return this.coberturasXContratos;
	}

	public void setCoberturasXContratos(Set coberturasXContratos) {
		this.coberturasXContratos = coberturasXContratos;
	}

	public Set getCoberturasEntidadSubs() {
		return this.coberturasEntidadSubs;
	}

	public void setCoberturasEntidadSubs(Set coberturasEntidadSubs) {
		this.coberturasEntidadSubs = coberturasEntidadSubs;
	}

	public Set getDetalleCoberturas() {
		return this.detalleCoberturas;
	}

	public void setDetalleCoberturas(Set detalleCoberturas) {
		this.detalleCoberturas = detalleCoberturas;
	}

	public Set getLogCoberturasEntSubs() {
		return this.logCoberturasEntSubs;
	}

	public void setLogCoberturasEntSubs(Set logCoberturasEntSubs) {
		this.logCoberturasEntSubs = logCoberturasEntSubs;
	}

}
