package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * SeccionParametriza generated by hbm2java
 */
public class SeccionParametriza implements java.io.Serializable {

	private int codigo;
	private int funcionalidad;
	private String nombre;
	private Set paramAsociadases = new HashSet(0);

	public SeccionParametriza() {
	}

	public SeccionParametriza(int codigo, int funcionalidad, String nombre) {
		this.codigo = codigo;
		this.funcionalidad = funcionalidad;
		this.nombre = nombre;
	}

	public SeccionParametriza(int codigo, int funcionalidad, String nombre,
			Set paramAsociadases) {
		this.codigo = codigo;
		this.funcionalidad = funcionalidad;
		this.nombre = nombre;
		this.paramAsociadases = paramAsociadases;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getFuncionalidad() {
		return this.funcionalidad;
	}

	public void setFuncionalidad(int funcionalidad) {
		this.funcionalidad = funcionalidad;
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
