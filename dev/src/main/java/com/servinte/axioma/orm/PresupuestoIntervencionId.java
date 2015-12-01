package com.servinte.axioma.orm;

// Generated Mar 17, 2010 9:47:25 AM by Hibernate Tools 3.2.4.GA

/**
 * PresupuestoIntervencionId generated by hbm2java
 */
public class PresupuestoIntervencionId implements java.io.Serializable {

	private int presupuesto;
	private int servicioIntervencion;

	public PresupuestoIntervencionId() {
	}

	public PresupuestoIntervencionId(int presupuesto, int servicioIntervencion) {
		this.presupuesto = presupuesto;
		this.servicioIntervencion = servicioIntervencion;
	}

	public int getPresupuesto() {
		return this.presupuesto;
	}

	public void setPresupuesto(int presupuesto) {
		this.presupuesto = presupuesto;
	}

	public int getServicioIntervencion() {
		return this.servicioIntervencion;
	}

	public void setServicioIntervencion(int servicioIntervencion) {
		this.servicioIntervencion = servicioIntervencion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PresupuestoIntervencionId))
			return false;
		PresupuestoIntervencionId castOther = (PresupuestoIntervencionId) other;

		return (this.getPresupuesto() == castOther.getPresupuesto())
				&& (this.getServicioIntervencion() == castOther
						.getServicioIntervencion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPresupuesto();
		result = 37 * result + this.getServicioIntervencion();
		return result;
	}

}
