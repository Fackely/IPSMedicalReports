package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * Formularios generated by hbm2java
 */
public class Formularios implements java.io.Serializable {

	private int codigo;
	private String nombre;

	public Formularios() {
	}

	public Formularios(int codigo) {
		this.codigo = codigo;
	}

	public Formularios(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
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

}
