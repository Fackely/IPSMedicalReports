package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * ArchivoPlanoColsanitas generated by hbm2java
 */
public class ArchivoPlanoColsanitas implements java.io.Serializable {

	private ArchivoPlanoColsanitasId id;
	private Instituciones instituciones;
	private Convenios convenios;
	private String unidadEconomica;
	private char identifCompania;
	private String identifPlan;
	private Date fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;

	public ArchivoPlanoColsanitas() {
	}

	public ArchivoPlanoColsanitas(ArchivoPlanoColsanitasId id,
			Instituciones instituciones, Convenios convenios,
			String unidadEconomica, char identifCompania, String identifPlan,
			Date fechaModificacion, String horaModificacion,
			String usuarioModificacion) {
		this.id = id;
		this.instituciones = instituciones;
		this.convenios = convenios;
		this.unidadEconomica = unidadEconomica;
		this.identifCompania = identifCompania;
		this.identifPlan = identifPlan;
		this.fechaModificacion = fechaModificacion;
		this.horaModificacion = horaModificacion;
		this.usuarioModificacion = usuarioModificacion;
	}

	public ArchivoPlanoColsanitasId getId() {
		return this.id;
	}

	public void setId(ArchivoPlanoColsanitasId id) {
		this.id = id;
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

	public String getUnidadEconomica() {
		return this.unidadEconomica;
	}

	public void setUnidadEconomica(String unidadEconomica) {
		this.unidadEconomica = unidadEconomica;
	}

	public char getIdentifCompania() {
		return this.identifCompania;
	}

	public void setIdentifCompania(char identifCompania) {
		this.identifCompania = identifCompania;
	}

	public String getIdentifPlan() {
		return this.identifPlan;
	}

	public void setIdentifPlan(String identifPlan) {
		this.identifPlan = identifPlan;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getHoraModificacion() {
		return this.horaModificacion;
	}

	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public String getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

}
