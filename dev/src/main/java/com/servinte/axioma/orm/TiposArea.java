package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TiposArea generated by hbm2java
 */
public class TiposArea implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set centrosCostos = new HashSet(0);

	public TiposArea() {
	}

	public TiposArea(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public TiposArea(int codigo, String nombre, Set centrosCostos) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.centrosCostos = centrosCostos;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set getCentrosCostos() {
		return this.centrosCostos;
	}

	public void setCentrosCostos(Set centrosCostos) {
		this.centrosCostos = centrosCostos;
	}

}
