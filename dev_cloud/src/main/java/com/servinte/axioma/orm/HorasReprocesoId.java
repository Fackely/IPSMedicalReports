package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * HorasReprocesoId generated by hbm2java
 */
public class HorasReprocesoId implements java.io.Serializable {

	private int viaIngreso;
	private int institucion;

	public HorasReprocesoId() {
	}

	public HorasReprocesoId(int viaIngreso, int institucion) {
		this.viaIngreso = viaIngreso;
		this.institucion = institucion;
	}

	public int getViaIngreso() {
		return this.viaIngreso;
	}

	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
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
		if (!(other instanceof HorasReprocesoId))
			return false;
		HorasReprocesoId castOther = (HorasReprocesoId) other;

		return (this.getViaIngreso() == castOther.getViaIngreso())
				&& (this.getInstitucion() == castOther.getInstitucion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getViaIngreso();
		result = 37 * result + this.getInstitucion();
		return result;
	}

}
