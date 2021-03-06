package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:51 AM by Hibernate Tools 3.2.4.GA

/**
 * PresupuestoArticulosId generated by hbm2java
 */
public class PresupuestoArticulosId implements java.io.Serializable {

	private int presupuesto;
	private int articulo;

	public PresupuestoArticulosId() {
	}

	public PresupuestoArticulosId(int presupuesto, int articulo) {
		this.presupuesto = presupuesto;
		this.articulo = articulo;
	}

	public int getPresupuesto() {
		return this.presupuesto;
	}

	public void setPresupuesto(int presupuesto) {
		this.presupuesto = presupuesto;
	}

	public int getArticulo() {
		return this.articulo;
	}

	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PresupuestoArticulosId))
			return false;
		PresupuestoArticulosId castOther = (PresupuestoArticulosId) other;

		return (this.getPresupuesto() == castOther.getPresupuesto())
				&& (this.getArticulo() == castOther.getArticulo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPresupuesto();
		result = 37 * result + this.getArticulo();
		return result;
	}

}
