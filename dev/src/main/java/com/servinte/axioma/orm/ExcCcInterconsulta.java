package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * ExcCcInterconsulta generated by hbm2java
 */
public class ExcCcInterconsulta implements java.io.Serializable {

	private long codigo;
	private CentrosCosto centrosCostoByCentroCostoEjecuta;
	private CentrosCosto centrosCostoByCentroCostoSolicita;
	private CentroAtencion centroAtencion;
	private Usuarios usuarios;
	private Servicios servicios;
	private Medicos medicos;
	private Instituciones instituciones;
	private Date fechaModifica;
	private String horaModifica;

	public ExcCcInterconsulta() {
	}

	public ExcCcInterconsulta(long codigo,
			CentrosCosto centrosCostoByCentroCostoEjecuta,
			CentrosCosto centrosCostoByCentroCostoSolicita,
			CentroAtencion centroAtencion, Usuarios usuarios,
			Servicios servicios, Medicos medicos, Instituciones instituciones,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.centrosCostoByCentroCostoEjecuta = centrosCostoByCentroCostoEjecuta;
		this.centrosCostoByCentroCostoSolicita = centrosCostoByCentroCostoSolicita;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
		this.servicios = servicios;
		this.medicos = medicos;
		this.instituciones = instituciones;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public CentrosCosto getCentrosCostoByCentroCostoEjecuta() {
		return this.centrosCostoByCentroCostoEjecuta;
	}

	public void setCentrosCostoByCentroCostoEjecuta(
			CentrosCosto centrosCostoByCentroCostoEjecuta) {
		this.centrosCostoByCentroCostoEjecuta = centrosCostoByCentroCostoEjecuta;
	}

	public CentrosCosto getCentrosCostoByCentroCostoSolicita() {
		return this.centrosCostoByCentroCostoSolicita;
	}

	public void setCentrosCostoByCentroCostoSolicita(
			CentrosCosto centrosCostoByCentroCostoSolicita) {
		this.centrosCostoByCentroCostoSolicita = centrosCostoByCentroCostoSolicita;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public Medicos getMedicos() {
		return this.medicos;
	}

	public void setMedicos(Medicos medicos) {
		this.medicos = medicos;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
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

}
