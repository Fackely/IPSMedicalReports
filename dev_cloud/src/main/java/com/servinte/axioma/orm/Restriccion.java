package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * Restriccion generated by hbm2java
 */
public class Restriccion implements java.io.Serializable {

	private int codigorestriccion;
	private String descrrestriccion;
	private String tiporestriccion;
	private Boolean activo;
	private Boolean text;

	public Restriccion() {
	}

	public Restriccion(int codigorestriccion) {
		this.codigorestriccion = codigorestriccion;
	}

	public Restriccion(int codigorestriccion, String descrrestriccion,
			String tiporestriccion, Boolean activo, Boolean text) {
		this.codigorestriccion = codigorestriccion;
		this.descrrestriccion = descrrestriccion;
		this.tiporestriccion = tiporestriccion;
		this.activo = activo;
		this.text = text;
	}

	public int getCodigorestriccion() {
		return this.codigorestriccion;
	}

	public void setCodigorestriccion(int codigorestriccion) {
		this.codigorestriccion = codigorestriccion;
	}

	public String getDescrrestriccion() {
		return this.descrrestriccion;
	}

	public void setDescrrestriccion(String descrrestriccion) {
		this.descrrestriccion = descrrestriccion;
	}

	public String getTiporestriccion() {
		return this.tiporestriccion;
	}

	public void setTiporestriccion(String tiporestriccion) {
		this.tiporestriccion = tiporestriccion;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Boolean getText() {
		return this.text;
	}

	public void setText(Boolean text) {
		this.text = text;
	}

}
