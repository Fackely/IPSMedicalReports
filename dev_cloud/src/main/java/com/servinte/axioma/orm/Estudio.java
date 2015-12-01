package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * Estudio generated by hbm2java
 */
public class Estudio implements java.io.Serializable {

	private long codigo;
	private String nombre;
	private boolean activo;
	private Set pacienteses = new HashSet(0);
	private Set beneficiariosPacientes = new HashSet(0);

	public Estudio() {
	}

	public Estudio(long codigo, String nombre, boolean activo) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.activo = activo;
	}

	public Estudio(long codigo, String nombre, boolean activo, Set pacienteses,
			Set beneficiariosPacientes) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.activo = activo;
		this.pacienteses = pacienteses;
		this.beneficiariosPacientes = beneficiariosPacientes;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Set getPacienteses() {
		return this.pacienteses;
	}

	public void setPacienteses(Set pacienteses) {
		this.pacienteses = pacienteses;
	}

	public Set getBeneficiariosPacientes() {
		return this.beneficiariosPacientes;
	}

	public void setBeneficiariosPacientes(Set beneficiariosPacientes) {
		this.beneficiariosPacientes = beneficiariosPacientes;
	}

}
