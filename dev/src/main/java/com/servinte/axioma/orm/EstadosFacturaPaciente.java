package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * EstadosFacturaPaciente generated by hbm2java
 */
public class EstadosFacturaPaciente implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set facturases = new HashSet(0);

	public EstadosFacturaPaciente() {
	}

	public EstadosFacturaPaciente(int codigo) {
		this.codigo = codigo;
	}

	public EstadosFacturaPaciente(int codigo, String nombre, Set facturases) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.facturases = facturases;
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

	public Set getFacturases() {
		return this.facturases;
	}

	public void setFacturases(Set facturases) {
		this.facturases = facturases;
	}

}
