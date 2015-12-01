package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * OcupacionSalarioBaseId generated by hbm2java
 */
public class OcupacionSalarioBaseId implements java.io.Serializable {

	private int ocupacion;
	private int tipoVinculacion;
	private Date fechaInicio;

	public OcupacionSalarioBaseId() {
	}

	public OcupacionSalarioBaseId(int ocupacion, int tipoVinculacion,
			Date fechaInicio) {
		this.ocupacion = ocupacion;
		this.tipoVinculacion = tipoVinculacion;
		this.fechaInicio = fechaInicio;
	}

	public int getOcupacion() {
		return this.ocupacion;
	}

	public void setOcupacion(int ocupacion) {
		this.ocupacion = ocupacion;
	}

	public int getTipoVinculacion() {
		return this.tipoVinculacion;
	}

	public void setTipoVinculacion(int tipoVinculacion) {
		this.tipoVinculacion = tipoVinculacion;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OcupacionSalarioBaseId))
			return false;
		OcupacionSalarioBaseId castOther = (OcupacionSalarioBaseId) other;

		return (this.getOcupacion() == castOther.getOcupacion())
				&& (this.getTipoVinculacion() == castOther.getTipoVinculacion())
				&& ((this.getFechaInicio() == castOther.getFechaInicio()) || (this
						.getFechaInicio() != null
						&& castOther.getFechaInicio() != null && this
						.getFechaInicio().equals(castOther.getFechaInicio())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getOcupacion();
		result = 37 * result + this.getTipoVinculacion();
		result = 37
				* result
				+ (getFechaInicio() == null ? 0 : this.getFechaInicio()
						.hashCode());
		return result;
	}

}
