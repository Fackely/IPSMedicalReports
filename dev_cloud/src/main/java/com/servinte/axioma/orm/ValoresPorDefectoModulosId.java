package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ValoresPorDefectoModulosId generated by hbm2java
 */
public class ValoresPorDefectoModulosId implements java.io.Serializable {

	private String parametro;
	private int modulo;

	public ValoresPorDefectoModulosId() {
	}

	public ValoresPorDefectoModulosId(String parametro, int modulo) {
		this.parametro = parametro;
		this.modulo = modulo;
	}

	public String getParametro() {
		return this.parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public int getModulo() {
		return this.modulo;
	}

	public void setModulo(int modulo) {
		this.modulo = modulo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ValoresPorDefectoModulosId))
			return false;
		ValoresPorDefectoModulosId castOther = (ValoresPorDefectoModulosId) other;

		return ((this.getParametro() == castOther.getParametro()) || (this
				.getParametro() != null
				&& castOther.getParametro() != null && this.getParametro()
				.equals(castOther.getParametro())))
				&& (this.getModulo() == castOther.getModulo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getParametro() == null ? 0 : this.getParametro().hashCode());
		result = 37 * result + this.getModulo();
		return result;
	}

}
