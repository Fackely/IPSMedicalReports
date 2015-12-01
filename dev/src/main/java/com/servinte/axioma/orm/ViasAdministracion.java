package com.servinte.axioma.orm;

// Generated 19/05/2011 11:36:22 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ViasAdministracion generated by hbm2java
 */
public class ViasAdministracion implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set detOrdenAmbArticulos = new HashSet(0);

	public ViasAdministracion() {
	}

	public ViasAdministracion(int codigo) {
		this.codigo = codigo;
	}

	public ViasAdministracion(int codigo, String nombre,
			Set detOrdenAmbArticulos) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.detOrdenAmbArticulos = detOrdenAmbArticulos;
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

	public Set getDetOrdenAmbArticulos() {
		return this.detOrdenAmbArticulos;
	}

	public void setDetOrdenAmbArticulos(Set detOrdenAmbArticulos) {
		this.detOrdenAmbArticulos = detOrdenAmbArticulos;
	}

}
