package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * DetAnulCargosFarmaciaId generated by hbm2java
 */
public class DetAnulCargosFarmaciaId implements java.io.Serializable {

	private long codigoAnulacion;
	private int numeroSolicitud;
	private int articulo;

	public DetAnulCargosFarmaciaId() {
	}

	public DetAnulCargosFarmaciaId(long codigoAnulacion, int numeroSolicitud,
			int articulo) {
		this.codigoAnulacion = codigoAnulacion;
		this.numeroSolicitud = numeroSolicitud;
		this.articulo = articulo;
	}

	public long getCodigoAnulacion() {
		return this.codigoAnulacion;
	}

	public void setCodigoAnulacion(long codigoAnulacion) {
		this.codigoAnulacion = codigoAnulacion;
	}

	public int getNumeroSolicitud() {
		return this.numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
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
		if (!(other instanceof DetAnulCargosFarmaciaId))
			return false;
		DetAnulCargosFarmaciaId castOther = (DetAnulCargosFarmaciaId) other;

		return (this.getCodigoAnulacion() == castOther.getCodigoAnulacion())
				&& (this.getNumeroSolicitud() == castOther.getNumeroSolicitud())
				&& (this.getArticulo() == castOther.getArticulo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getCodigoAnulacion();
		result = 37 * result + this.getNumeroSolicitud();
		result = 37 * result + this.getArticulo();
		return result;
	}

}
