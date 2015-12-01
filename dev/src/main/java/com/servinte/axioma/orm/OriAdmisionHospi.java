package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * OriAdmisionHospi generated by hbm2java
 */
public class OriAdmisionHospi implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set admisionesHospis = new HashSet(0);
	private Set cuentases = new HashSet(0);
	private Set admisionesUrgenciases = new HashSet(0);

	public OriAdmisionHospi() {
	}

	public OriAdmisionHospi(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public OriAdmisionHospi(int codigo, String nombre, Set admisionesHospis,
			Set cuentases, Set admisionesUrgenciases) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.admisionesHospis = admisionesHospis;
		this.cuentases = cuentases;
		this.admisionesUrgenciases = admisionesUrgenciases;
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

	public Set getAdmisionesHospis() {
		return this.admisionesHospis;
	}

	public void setAdmisionesHospis(Set admisionesHospis) {
		this.admisionesHospis = admisionesHospis;
	}

	public Set getCuentases() {
		return this.cuentases;
	}

	public void setCuentases(Set cuentases) {
		this.cuentases = cuentases;
	}

	public Set getAdmisionesUrgenciases() {
		return this.admisionesUrgenciases;
	}

	public void setAdmisionesUrgenciases(Set admisionesUrgenciases) {
		this.admisionesUrgenciases = admisionesUrgenciases;
	}

}
