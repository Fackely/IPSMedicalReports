package com.servinte.axioma.orm;

// Generated May 5, 2010 3:51:40 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * Roles generated by hbm2java
 */
public class Roles implements java.io.Serializable {

	private String nombreRol;
	private int codigo;
	private Set tiposUsuarioses = new HashSet(0);
	private Set usuarioses = new HashSet(0);
	private Set rolesFuncionalidadeses = new HashSet(0);

	public Roles() {
	}

	public Roles(String nombreRol, int codigo) {
		this.nombreRol = nombreRol;
		this.codigo = codigo;
	}

	public Roles(String nombreRol, int codigo, Set tiposUsuarioses,
			Set usuarioses, Set rolesFuncionalidadeses) {
		this.nombreRol = nombreRol;
		this.codigo = codigo;
		this.tiposUsuarioses = tiposUsuarioses;
		this.usuarioses = usuarioses;
		this.rolesFuncionalidadeses = rolesFuncionalidadeses;
	}

	public String getNombreRol() {
		return this.nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Set getTiposUsuarioses() {
		return this.tiposUsuarioses;
	}

	public void setTiposUsuarioses(Set tiposUsuarioses) {
		this.tiposUsuarioses = tiposUsuarioses;
	}

	public Set getUsuarioses() {
		return this.usuarioses;
	}

	public void setUsuarioses(Set usuarioses) {
		this.usuarioses = usuarioses;
	}

	public Set getRolesFuncionalidadeses() {
		return this.rolesFuncionalidadeses;
	}

	public void setRolesFuncionalidadeses(Set rolesFuncionalidadeses) {
		this.rolesFuncionalidadeses = rolesFuncionalidadeses;
	}

}
