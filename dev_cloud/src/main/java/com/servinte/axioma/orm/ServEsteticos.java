package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ServEsteticos generated by hbm2java
 */
public class ServEsteticos implements java.io.Serializable {

	private ServEsteticosId id;
	private Servicios servicios;
	private Instituciones instituciones;
	private ServiciosGruposEsteticos serviciosGruposEsteticos;

	public ServEsteticos() {
	}

	public ServEsteticos(ServEsteticosId id, Servicios servicios,
			Instituciones instituciones,
			ServiciosGruposEsteticos serviciosGruposEsteticos) {
		this.id = id;
		this.servicios = servicios;
		this.instituciones = instituciones;
		this.serviciosGruposEsteticos = serviciosGruposEsteticos;
	}

	public ServEsteticosId getId() {
		return this.id;
	}

	public void setId(ServEsteticosId id) {
		this.id = id;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public ServiciosGruposEsteticos getServiciosGruposEsteticos() {
		return this.serviciosGruposEsteticos;
	}

	public void setServiciosGruposEsteticos(
			ServiciosGruposEsteticos serviciosGruposEsteticos) {
		this.serviciosGruposEsteticos = serviciosGruposEsteticos;
	}

}
