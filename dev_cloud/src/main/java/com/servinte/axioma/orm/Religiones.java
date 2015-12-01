package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * Religiones generated by hbm2java
 */
public class Religiones implements java.io.Serializable {

	private long codigo;
	private Instituciones instituciones;
	private String descripcion;
	private String activo;
	private Set pacienteses = new HashSet(0);

	public Religiones() {
	}

	public Religiones(long codigo, Instituciones instituciones,
			String descripcion, String activo) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
	}

	public Religiones(long codigo, Instituciones instituciones,
			String descripcion, String activo, Set pacienteses) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.descripcion = descripcion;
		this.activo = activo;
		this.pacienteses = pacienteses;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
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

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Set getPacienteses() {
		return this.pacienteses;
	}

	public void setPacienteses(Set pacienteses) {
		this.pacienteses = pacienteses;
	}

}
