package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * TiposIdInstitucionId generated by hbm2java
 */
public class TiposIdInstitucionId implements java.io.Serializable {

	private String acronimo;
	private int institucion;

	public TiposIdInstitucionId() {
	}

	public TiposIdInstitucionId(String acronimo, int institucion) {
		this.acronimo = acronimo;
		this.institucion = institucion;
	}

	public String getAcronimo() {
		return this.acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
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
		if (!(other instanceof TiposIdInstitucionId))
			return false;
		TiposIdInstitucionId castOther = (TiposIdInstitucionId) other;

		return ((this.getAcronimo() == castOther.getAcronimo()) || (this
				.getAcronimo() != null
				&& castOther.getAcronimo() != null && this.getAcronimo()
				.equals(castOther.getAcronimo())))
				&& (this.getInstitucion() == castOther.getInstitucion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getAcronimo() == null ? 0 : this.getAcronimo().hashCode());
		result = 37 * result + this.getInstitucion();
		return result;
	}

}
