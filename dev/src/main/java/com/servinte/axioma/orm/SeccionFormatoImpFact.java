package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * SeccionFormatoImpFact generated by hbm2java
 */
public class SeccionFormatoImpFact implements java.io.Serializable {

	private int codigo;
	private String descripcion;

	public SeccionFormatoImpFact() {
	}

	public SeccionFormatoImpFact(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
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

}
