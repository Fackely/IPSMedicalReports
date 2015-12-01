package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ServEsteticosId generated by hbm2java
 */
public class ServEsteticosId implements java.io.Serializable {

	private String gruposEsteticos;
	private int institucion;
	private int servicio;

	public ServEsteticosId() {
	}

	public ServEsteticosId(String gruposEsteticos, int institucion, int servicio) {
		this.gruposEsteticos = gruposEsteticos;
		this.institucion = institucion;
		this.servicio = servicio;
	}

	public String getGruposEsteticos() {
		return this.gruposEsteticos;
	}

	public void setGruposEsteticos(String gruposEsteticos) {
		this.gruposEsteticos = gruposEsteticos;
	}

	public int getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getServicio() {
		return this.servicio;
	}

	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ServEsteticosId))
			return false;
		ServEsteticosId castOther = (ServEsteticosId) other;

		return ((this.getGruposEsteticos() == castOther.getGruposEsteticos()) || (this
				.getGruposEsteticos() != null
				&& castOther.getGruposEsteticos() != null && this
				.getGruposEsteticos().equals(castOther.getGruposEsteticos())))
				&& (this.getInstitucion() == castOther.getInstitucion())
				&& (this.getServicio() == castOther.getServicio());
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getGruposEsteticos() == null ? 0 : this.getGruposEsteticos()
						.hashCode());
		result = 37 * result + this.getInstitucion();
		result = 37 * result + this.getServicio();
		return result;
	}

}
