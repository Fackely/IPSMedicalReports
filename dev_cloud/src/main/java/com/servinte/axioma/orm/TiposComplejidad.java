package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TiposComplejidad generated by hbm2java
 */
public class TiposComplejidad implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private Set excepTarifasContratos = new HashSet(0);
	private Set cuentases = new HashSet(0);

	public TiposComplejidad() {
	}

	public TiposComplejidad(int codigo) {
		this.codigo = codigo;
	}

	public TiposComplejidad(int codigo, String descripcion,
			Set excepTarifasContratos, Set cuentases) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.excepTarifasContratos = excepTarifasContratos;
		this.cuentases = cuentases;
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

	public Set getExcepTarifasContratos() {
		return this.excepTarifasContratos;
	}

	public void setExcepTarifasContratos(Set excepTarifasContratos) {
		this.excepTarifasContratos = excepTarifasContratos;
	}

	public Set getCuentases() {
		return this.cuentases;
	}

	public void setCuentases(Set cuentases) {
		this.cuentases = cuentases;
	}

}
