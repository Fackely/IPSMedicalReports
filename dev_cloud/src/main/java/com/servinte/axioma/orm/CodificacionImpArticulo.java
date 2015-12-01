package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * CodificacionImpArticulo generated by hbm2java
 */
public class CodificacionImpArticulo implements java.io.Serializable {

	private int codigo;
	private String descripcion;
	private Set formatoImpresionFacturas = new HashSet(0);

	public CodificacionImpArticulo() {
	}

	public CodificacionImpArticulo(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public CodificacionImpArticulo(int codigo, String descripcion,
			Set formatoImpresionFacturas) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.formatoImpresionFacturas = formatoImpresionFacturas;
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

	public Set getFormatoImpresionFacturas() {
		return this.formatoImpresionFacturas;
	}

	public void setFormatoImpresionFacturas(Set formatoImpresionFacturas) {
		this.formatoImpresionFacturas = formatoImpresionFacturas;
	}

}
