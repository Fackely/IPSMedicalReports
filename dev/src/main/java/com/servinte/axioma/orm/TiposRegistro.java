package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * TiposRegistro generated by hbm2java
 */
public class TiposRegistro implements java.io.Serializable {

	private byte codigo;
	private String nombre;

	public TiposRegistro() {
	}

	public TiposRegistro(byte codigo) {
		this.codigo = codigo;
	}

	public TiposRegistro(byte codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public byte getCodigo() {
		return this.codigo;
	}

	public void setCodigo(byte codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
