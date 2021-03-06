package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * UnidadesFuncionales generated by hbm2java
 */
public class UnidadesFuncionales implements java.io.Serializable {

	private UnidadesFuncionalesId id;
	private Instituciones instituciones;
	private String descripcion;
	private boolean activo;
	private Set unidadFunCuentaIngs = new HashSet(0);
	private Set centrosCostos = new HashSet(0);
	private Set unidadFunCuentaIngCcs = new HashSet(0);

	public UnidadesFuncionales() {
	}

	public UnidadesFuncionales(UnidadesFuncionalesId id,
			Instituciones instituciones, String descripcion, boolean activo) {
		this.id = id;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
	}

	public UnidadesFuncionales(UnidadesFuncionalesId id,
			Instituciones instituciones, String descripcion, boolean activo,
			Set unidadFunCuentaIngs, Set centrosCostos,
			Set unidadFunCuentaIngCcs) {
		this.id = id;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
		this.unidadFunCuentaIngs = unidadFunCuentaIngs;
		this.centrosCostos = centrosCostos;
		this.unidadFunCuentaIngCcs = unidadFunCuentaIngCcs;
	}

	public UnidadesFuncionalesId getId() {
		return this.id;
	}

	public void setId(UnidadesFuncionalesId id) {
		this.id = id;
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

	public Set getUnidadFunCuentaIngs() {
		return this.unidadFunCuentaIngs;
	}

	public void setUnidadFunCuentaIngs(Set unidadFunCuentaIngs) {
		this.unidadFunCuentaIngs = unidadFunCuentaIngs;
	}

	public Set getCentrosCostos() {
		return this.centrosCostos;
	}

	public void setCentrosCostos(Set centrosCostos) {
		this.centrosCostos = centrosCostos;
	}

	public Set getUnidadFunCuentaIngCcs() {
		return this.unidadFunCuentaIngCcs;
	}

	public void setUnidadFunCuentaIngCcs(Set unidadFunCuentaIngCcs) {
		this.unidadFunCuentaIngCcs = unidadFunCuentaIngCcs;
	}

}
