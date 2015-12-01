package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TiposCaja generated by hbm2java
 */
public class TiposCaja implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private Set cajases = new HashSet(0);

	public TiposCaja() {
	}

	public TiposCaja(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public TiposCaja(int codigo, String descripcion, Set cajases) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cajases = cajases;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set getCajases() {
		return this.cajases;
	}

	public void setCajases(Set cajases) {
		this.cajases = cajases;
	}

}
