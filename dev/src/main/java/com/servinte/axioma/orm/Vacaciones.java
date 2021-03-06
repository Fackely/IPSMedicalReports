package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * Vacaciones generated by hbm2java
 */
public class Vacaciones implements java.io.Serializable {

	private VacacionesId id;
	private Personas personas;
	private Date fechaFin;

	public Vacaciones() {
	}

	public Vacaciones(VacacionesId id, Personas personas) {
		this.id = id;
		this.personas = personas;
	}

	public Vacaciones(VacacionesId id, Personas personas, Date fechaFin) {
		this.id = id;
		this.personas = personas;
		this.fechaFin = fechaFin;
	}

	public VacacionesId getId() {
		return this.id;
	}

	public void setId(VacacionesId id) {
		this.id = id;
	}

	public Personas getPersonas() {
		return this.personas;
	}

	public void setPersonas(Personas personas) {
		this.personas = personas;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

}
