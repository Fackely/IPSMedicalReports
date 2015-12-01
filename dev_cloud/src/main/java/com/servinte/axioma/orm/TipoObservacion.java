package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TipoObservacion generated by hbm2java
 */
public class TipoObservacion implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private boolean porDefecto;
	private boolean activo;
	private Set ctTurnoObservacions = new HashSet(0);

	public TipoObservacion() {
	}

	public TipoObservacion(int codigo, String descripcion, boolean porDefecto,
			boolean activo) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.porDefecto = porDefecto;
		this.activo = activo;
	}

	public TipoObservacion(int codigo, String descripcion, boolean porDefecto,
			boolean activo, Set ctTurnoObservacions) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.porDefecto = porDefecto;
		this.activo = activo;
		this.ctTurnoObservacions = ctTurnoObservacions;
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

	public boolean isPorDefecto() {
		return this.porDefecto;
	}

	public void setPorDefecto(boolean porDefecto) {
		this.porDefecto = porDefecto;
	}

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Set getCtTurnoObservacions() {
		return this.ctTurnoObservacions;
	}

	public void setCtTurnoObservacions(Set ctTurnoObservacions) {
		this.ctTurnoObservacions = ctTurnoObservacions;
	}

}
