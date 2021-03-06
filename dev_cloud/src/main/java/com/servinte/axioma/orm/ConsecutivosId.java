package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ConsecutivosId generated by hbm2java
 */
public class ConsecutivosId implements java.io.Serializable {

	private String nombre;
	private int institucion;

	public ConsecutivosId() {
	}

	public ConsecutivosId(String nombre, int institucion) {
		this.nombre = nombre;
		this.institucion = institucion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
		if (!(other instanceof ConsecutivosId))
			return false;
		ConsecutivosId castOther = (ConsecutivosId) other;

		return ((this.getNombre() == castOther.getNombre()) || (this
				.getNombre() != null
				&& castOther.getNombre() != null && this.getNombre().equals(
				castOther.getNombre())))
				&& (this.getInstitucion() == castOther.getInstitucion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getNombre() == null ? 0 : this.getNombre().hashCode());
		result = 37 * result + this.getInstitucion();
		return result;
	}

}
