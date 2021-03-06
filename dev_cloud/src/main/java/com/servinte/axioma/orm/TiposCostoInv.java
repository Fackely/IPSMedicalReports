package com.servinte.axioma.orm;

// Generated Jan 18, 2011 11:29:29 AM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * TiposCostoInv generated by hbm2java
 */
public class TiposCostoInv implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private Set tiposTransInventarioses = new HashSet(0);

	public TiposCostoInv() {
	}

	public TiposCostoInv(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public TiposCostoInv(int codigo, String descripcion,
			Set tiposTransInventarioses) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.tiposTransInventarioses = tiposTransInventarioses;
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

	public Set getTiposTransInventarioses() {
		return this.tiposTransInventarioses;
	}

	public void setTiposTransInventarioses(Set tiposTransInventarioses) {
		this.tiposTransInventarioses = tiposTransInventarioses;
	}

}
