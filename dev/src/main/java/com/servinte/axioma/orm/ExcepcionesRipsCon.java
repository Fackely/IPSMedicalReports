package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * ExcepcionesRipsCon generated by hbm2java
 */
public class ExcepcionesRipsCon implements java.io.Serializable {

	private int numeroSolicitud;
	private Instituciones instituciones;
	private boolean rips;

	public ExcepcionesRipsCon() {
	}

	public ExcepcionesRipsCon(int numeroSolicitud, Instituciones instituciones,
			boolean rips) {
		this.numeroSolicitud = numeroSolicitud;
		this.instituciones = instituciones;
		this.rips = rips;
	}

	public int getNumeroSolicitud() {
		return this.numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public boolean isRips() {
		return this.rips;
	}

	public void setRips(boolean rips) {
		this.rips = rips;
	}

}
