package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * ZonasDomicilio generated by hbm2java
 */
public class ZonasDomicilio implements java.io.Serializable {

	private char acronimo;
	private String nombre;
	private Set registroEventoCatastroficos = new HashSet(0);
	private Set pacienteses = new HashSet(0);

	public ZonasDomicilio() {
	}

	public ZonasDomicilio(char acronimo, String nombre) {
		this.acronimo = acronimo;
		this.nombre = nombre;
	}

	public ZonasDomicilio(char acronimo, String nombre,
			Set registroEventoCatastroficos, Set pacienteses) {
		this.acronimo = acronimo;
		this.nombre = nombre;
		this.registroEventoCatastroficos = registroEventoCatastroficos;
		this.pacienteses = pacienteses;
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

	public Set getRegistroEventoCatastroficos() {
		return this.registroEventoCatastroficos;
	}

	public void setRegistroEventoCatastroficos(Set registroEventoCatastroficos) {
		this.registroEventoCatastroficos = registroEventoCatastroficos;
	}

	public Set getPacienteses() {
		return this.pacienteses;
	}

	public void setPacienteses(Set pacienteses) {
		this.pacienteses = pacienteses;
	}

}
