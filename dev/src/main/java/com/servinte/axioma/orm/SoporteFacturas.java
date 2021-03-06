package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * SoporteFacturas generated by hbm2java
 */
public class SoporteFacturas implements java.io.Serializable {

	private long codigo;
	private ViasIngreso viasIngreso;
	private Instituciones instituciones;
	private Convenios convenios;
	private Date fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private Set tiposSoporteFacts = new HashSet(0);

	public SoporteFacturas() {
	}

	public SoporteFacturas(long codigo, ViasIngreso viasIngreso,
			Instituciones instituciones, Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.viasIngreso = viasIngreso;
		this.instituciones = instituciones;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public SoporteFacturas(long codigo, ViasIngreso viasIngreso,
			Instituciones instituciones, Convenios convenios,
			Date fechaModifica, String horaModifica, String usuarioModifica,
			Set tiposSoporteFacts) {
		this.codigo = codigo;
		this.viasIngreso = viasIngreso;
		this.instituciones = instituciones;
		this.convenios = convenios;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.usuarioModifica = usuarioModifica;
		this.tiposSoporteFacts = tiposSoporteFacts;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public ViasIngreso getViasIngreso() {
		return this.viasIngreso;
	}

	public void setViasIngreso(ViasIngreso viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
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

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public Set getTiposSoporteFacts() {
		return this.tiposSoporteFacts;
	}

	public void setTiposSoporteFacts(Set tiposSoporteFacts) {
		this.tiposSoporteFacts = tiposSoporteFacts;
	}

}
