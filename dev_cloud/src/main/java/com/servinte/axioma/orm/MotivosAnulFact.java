package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * MotivosAnulFact generated by hbm2java
 */
public class MotivosAnulFact implements java.io.Serializable {

	private int codigo;
	private Instituciones instituciones;
	private String descripcion;
	private boolean activo;
	private Set solicitudAnulFacts = new HashSet(0);
	private Set anulacionesFacturases = new HashSet(0);

	public MotivosAnulFact() {
	}

	public MotivosAnulFact(int codigo, Instituciones instituciones,
			String descripcion, boolean activo) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
	}

	public MotivosAnulFact(int codigo, Instituciones instituciones,
			String descripcion, boolean activo, Set solicitudAnulFacts,
			Set anulacionesFacturases) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
		this.solicitudAnulFacts = solicitudAnulFacts;
		this.anulacionesFacturases = anulacionesFacturases;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
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

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Set getSolicitudAnulFacts() {
		return this.solicitudAnulFacts;
	}

	public void setSolicitudAnulFacts(Set solicitudAnulFacts) {
		this.solicitudAnulFacts = solicitudAnulFacts;
	}

	public Set getAnulacionesFacturases() {
		return this.anulacionesFacturases;
	}

	public void setAnulacionesFacturases(Set anulacionesFacturases) {
		this.anulacionesFacturases = anulacionesFacturases;
	}

}
