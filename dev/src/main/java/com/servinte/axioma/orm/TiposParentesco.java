package com.servinte.axioma.orm;

// Generated 3/06/2011 09:54:52 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * TiposParentesco generated by hbm2java
 */
public class TiposParentesco implements java.io.Serializable {

	private int codigo;
	private String nombre;
	private Set convUsuariosCapitadoses = new HashSet(0);
	private Set usuarioXConvenios = new HashSet(0);

	public TiposParentesco() {
	}

	public TiposParentesco(int codigo) {
		this.codigo = codigo;
	}

	public TiposParentesco(int codigo, String nombre,
			Set convUsuariosCapitadoses, Set usuarioXConvenios) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.convUsuariosCapitadoses = convUsuariosCapitadoses;
		this.usuarioXConvenios = usuarioXConvenios;
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

	public Set getConvUsuariosCapitadoses() {
		return this.convUsuariosCapitadoses;
	}

	public void setConvUsuariosCapitadoses(Set convUsuariosCapitadoses) {
		this.convUsuariosCapitadoses = convUsuariosCapitadoses;
	}

	public Set getUsuarioXConvenios() {
		return this.usuarioXConvenios;
	}

	public void setUsuarioXConvenios(Set usuarioXConvenios) {
		this.usuarioXConvenios = usuarioXConvenios;
	}

}
