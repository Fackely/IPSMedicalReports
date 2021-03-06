package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TiposParametrizables generated by hbm2java
 */
public class TiposParametrizables implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set paramAsociadases = new HashSet(0);

	public TiposParametrizables() {
	}

	public TiposParametrizables(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public TiposParametrizables(int codigo, String nombre, Set paramAsociadases) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.paramAsociadases = paramAsociadases;
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

	public Set getParamAsociadases() {
		return this.paramAsociadases;
	}

	public void setParamAsociadases(Set paramAsociadases) {
		this.paramAsociadases = paramAsociadases;
	}

}
