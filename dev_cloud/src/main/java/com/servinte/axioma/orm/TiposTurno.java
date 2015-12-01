package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * TiposTurno generated by hbm2java
 */
public class TiposTurno implements java.io.Serializable {

	private char acronimo;
	private String nombre;
	private boolean porDefecto;
	private Set turnos = new HashSet(0);

	public TiposTurno() {
	}

	public TiposTurno(char acronimo, String nombre, boolean porDefecto) {
		this.acronimo = acronimo;
		this.nombre = nombre;
		this.porDefecto = porDefecto;
	}

	public TiposTurno(char acronimo, String nombre, boolean porDefecto,
			Set turnos) {
		this.acronimo = acronimo;
		this.nombre = nombre;
		this.porDefecto = porDefecto;
		this.turnos = turnos;
	}

	public char getAcronimo() {
		return this.acronimo;
	}

	public void setAcronimo(char acronimo) {
		this.acronimo = acronimo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isPorDefecto() {
		return this.porDefecto;
	}

	public void setPorDefecto(boolean porDefecto) {
		this.porDefecto = porDefecto;
	}

	public Set getTurnos() {
		return this.turnos;
	}

	public void setTurnos(Set turnos) {
		this.turnos = turnos;
	}

}
