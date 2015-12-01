package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ServiciosGruposEsteticos generated by hbm2java
 */
public class ServiciosGruposEsteticos implements java.io.Serializable {

	private ServiciosGruposEsteticosId id;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private String descripcion;
	private char activo;
	private Date fechaModifica;
	private String horaModifica;
	private Set servEsteticoses = new HashSet(0);

	public ServiciosGruposEsteticos() {
	}

	public ServiciosGruposEsteticos(ServiciosGruposEsteticosId id,
			Usuarios usuarios, Instituciones instituciones, String descripcion,
			char activo, Date fechaModifica, String horaModifica) {
		this.id = id;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public ServiciosGruposEsteticos(ServiciosGruposEsteticosId id,
			Usuarios usuarios, Instituciones instituciones, String descripcion,
			char activo, Date fechaModifica, String horaModifica,
			Set servEsteticoses) {
		this.id = id;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.servEsteticoses = servEsteticoses;
	}

	public ServiciosGruposEsteticosId getId() {
		return this.id;
	}

	public void setId(ServiciosGruposEsteticosId id) {
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

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public char getActivo() {
		return this.activo;
	}

	public void setActivo(char activo) {
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

	public Set getServEsteticoses() {
		return this.servEsteticoses;
	}

	public void setServEsteticoses(Set servEsteticoses) {
		this.servEsteticoses = servEsteticoses;
	}

}
