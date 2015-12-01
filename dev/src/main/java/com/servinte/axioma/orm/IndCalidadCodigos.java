package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * IndCalidadCodigos generated by hbm2java
 */
public class IndCalidadCodigos implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private String acronimo;
	private String tipo;
	private Set indCalidadEspecialidads = new HashSet(0);
	private Set indCalidadCcs = new HashSet(0);
	private Set indCalidadDxes = new HashSet(0);

	public IndCalidadCodigos() {
	}

	public IndCalidadCodigos(int codigo, String descripcion, String acronimo,
			String tipo) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.acronimo = acronimo;
		this.tipo = tipo;
	}

	public IndCalidadCodigos(int codigo, String descripcion, String acronimo,
			String tipo, Set indCalidadEspecialidads, Set indCalidadCcs,
			Set indCalidadDxes) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.acronimo = acronimo;
		this.tipo = tipo;
		this.indCalidadEspecialidads = indCalidadEspecialidads;
		this.indCalidadCcs = indCalidadCcs;
		this.indCalidadDxes = indCalidadDxes;
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

	public String getAcronimo() {
		return this.acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Set getIndCalidadEspecialidads() {
		return this.indCalidadEspecialidads;
	}

	public void setIndCalidadEspecialidads(Set indCalidadEspecialidads) {
		this.indCalidadEspecialidads = indCalidadEspecialidads;
	}

	public Set getIndCalidadCcs() {
		return this.indCalidadCcs;
	}

	public void setIndCalidadCcs(Set indCalidadCcs) {
		this.indCalidadCcs = indCalidadCcs;
	}

	public Set getIndCalidadDxes() {
		return this.indCalidadDxes;
	}

	public void setIndCalidadDxes(Set indCalidadDxes) {
		this.indCalidadDxes = indCalidadDxes;
	}

}
