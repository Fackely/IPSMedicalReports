package com.servinte.axioma.orm;

// Generated 14/07/2011 02:21:27 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ConceptoNotaPaciente generated by hbm2java
 */
public class ConceptoNotaPaciente implements java.io.Serializable {

	private long codigopk;
	private String codigo;
	private String descripcion;
	private String naturaleza;
	private String activo;
	private Set concNotaPacCuentaConts = new HashSet(0);
	private Set notaPacientes = new HashSet(0);

	public ConceptoNotaPaciente() {
	}

	public ConceptoNotaPaciente(long codigopk, String codigo,
			String descripcion, String naturaleza, String activo) {
		this.codigopk = codigopk;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.naturaleza = naturaleza;
		this.activo = activo;
	}

	public ConceptoNotaPaciente(long codigopk, String codigo,
			String descripcion, String naturaleza, String activo,
			Set concNotaPacCuentaConts, Set notaPacientes) {
		this.codigopk = codigopk;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.naturaleza = naturaleza;
		this.activo = activo;
		this.concNotaPacCuentaConts = concNotaPacCuentaConts;
		this.notaPacientes = notaPacientes;
	}

	public long getCodigopk() {
		return this.codigopk;
	}

	public void setCodigopk(long codigopk) {
		this.codigopk = codigopk;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNaturaleza() {
		return this.naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Set getConcNotaPacCuentaConts() {
		return this.concNotaPacCuentaConts;
	}

	public void setConcNotaPacCuentaConts(Set concNotaPacCuentaConts) {
		this.concNotaPacCuentaConts = concNotaPacCuentaConts;
	}

	public Set getNotaPacientes() {
		return this.notaPacientes;
	}

	public void setNotaPacientes(Set notaPacientes) {
		this.notaPacientes = notaPacientes;
	}

}
