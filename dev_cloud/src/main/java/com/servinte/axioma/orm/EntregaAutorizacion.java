package com.servinte.axioma.orm;

// Generated 19/02/2013 04:42:46 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * EntregaAutorizacion generated by hbm2java
 */
public class EntregaAutorizacion implements java.io.Serializable {

	private int id;
	private Usuarios usuarios;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;
	private Date fechaEntrega;
	private String horaEntrega;
	private String personaRecibe;
	private String observaciones;

	public EntregaAutorizacion() {
	}

	public EntregaAutorizacion(int id, Usuarios usuarios,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			Date fechaEntrega, String horaEntrega, String personaRecibe) {
		this.id = id;
		this.usuarios = usuarios;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.fechaEntrega = fechaEntrega;
		this.horaEntrega = horaEntrega;
		this.personaRecibe = personaRecibe;
	}

	public EntregaAutorizacion(int id, Usuarios usuarios,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			Date fechaEntrega, String horaEntrega, String personaRecibe,
			String observaciones) {
		this.id = id;
		this.usuarios = usuarios;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.fechaEntrega = fechaEntrega;
		this.horaEntrega = horaEntrega;
		this.personaRecibe = personaRecibe;
		this.observaciones = observaciones;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public Date getFechaEntrega() {
		return this.fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public String getHoraEntrega() {
		return this.horaEntrega;
	}

	public void setHoraEntrega(String horaEntrega) {
		this.horaEntrega = horaEntrega;
	}

	public String getPersonaRecibe() {
		return this.personaRecibe;
	}

	public void setPersonaRecibe(String personaRecibe) {
		this.personaRecibe = personaRecibe;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
