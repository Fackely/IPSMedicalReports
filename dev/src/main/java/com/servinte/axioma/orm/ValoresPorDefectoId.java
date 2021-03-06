package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ValoresPorDefectoId generated by hbm2java
 */
public class ValoresPorDefectoId implements java.io.Serializable {

	private String parametro;
	private int institucion;

	public ValoresPorDefectoId() {
	}

	public ValoresPorDefectoId(String parametro, int institucion) {
		this.parametro = parametro;
		this.institucion = institucion;
	}

	public String getParametro() {
		return this.parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public int getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ValoresPorDefectoId))
			return false;
		ValoresPorDefectoId castOther = (ValoresPorDefectoId) other;

		return ((this.getParametro() == castOther.getParametro()) || (this
				.getParametro() != null
				&& castOther.getParametro() != null && this.getParametro()
				.equals(castOther.getParametro())))
				&& (this.getInstitucion() == castOther.getInstitucion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getParametro() == null ? 0 : this.getParametro().hashCode());
		result = 37 * result + this.getInstitucion();
		return result;
	}

}
