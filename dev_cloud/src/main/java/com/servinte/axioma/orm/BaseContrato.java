package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * BaseContrato generated by hbm2java
 */
public class BaseContrato implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set contratoses = new HashSet(0);

	public BaseContrato() {
	}

	public BaseContrato(int codigo) {
		this.codigo = codigo;
	}

	public BaseContrato(int codigo, String nombre, Set contratoses) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.contratoses = contratoses;
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

	public Set getContratoses() {
		return this.contratoses;
	}

	public void setContratoses(Set contratoses) {
		this.contratoses = contratoses;
	}

}
