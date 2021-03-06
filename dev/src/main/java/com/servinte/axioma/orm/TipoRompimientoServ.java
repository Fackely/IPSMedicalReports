package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TipoRompimientoServ generated by hbm2java
 */
public class TipoRompimientoServ implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private Set detSecServFormatoFacts = new HashSet(0);

	public TipoRompimientoServ() {
	}

	public TipoRompimientoServ(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public TipoRompimientoServ(int codigo, String descripcion,
			Set detSecServFormatoFacts) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.detSecServFormatoFacts = detSecServFormatoFacts;
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

	public Set getDetSecServFormatoFacts() {
		return this.detSecServFormatoFacts;
	}

	public void setDetSecServFormatoFacts(Set detSecServFormatoFacts) {
		this.detSecServFormatoFacts = detSecServFormatoFacts;
	}

}
