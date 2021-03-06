package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TipoFirma generated by hbm2java
 */
public class TipoFirma implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private Set detSecNotaFormatoFacts = new HashSet(0);

	public TipoFirma() {
	}

	public TipoFirma(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public TipoFirma(int codigo, String descripcion, Set detSecNotaFormatoFacts) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.detSecNotaFormatoFacts = detSecNotaFormatoFacts;
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

	public Set getDetSecNotaFormatoFacts() {
		return this.detSecNotaFormatoFacts;
	}

	public void setDetSecNotaFormatoFacts(Set detSecNotaFormatoFacts) {
		this.detSecNotaFormatoFacts = detSecNotaFormatoFacts;
	}

}
