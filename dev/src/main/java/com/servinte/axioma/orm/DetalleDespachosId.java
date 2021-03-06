package com.servinte.axioma.orm;

// Generated Nov 24, 2010 2:03:22 PM by Hibernate Tools 3.2.4.GA

/**
 * DetalleDespachosId generated by hbm2java
 */
public class DetalleDespachosId implements java.io.Serializable {

	private int articulo;
	private int despacho;

	public DetalleDespachosId() {
	}

	public DetalleDespachosId(int articulo, int despacho) {
		this.articulo = articulo;
		this.despacho = despacho;
	}

	public int getArticulo() {
		return this.articulo;
	}

	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}

	public int getDespacho() {
		return this.despacho;
	}

	public void setDespacho(int despacho) {
		this.despacho = despacho;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DetalleDespachosId))
			return false;
		DetalleDespachosId castOther = (DetalleDespachosId) other;

		return (this.getArticulo() == castOther.getArticulo())
				&& (this.getDespacho() == castOther.getDespacho());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getArticulo();
		result = 37 * result + this.getDespacho();
		return result;
	}

}
