package com.servinte.axioma.orm;

// Generated 10/12/2012 11:43:39 AM by Hibernate Tools 3.4.0.CR1

/**
 * SolXJustArtSol generated by hbm2java
 */
public class SolXJustArtSol implements java.io.Serializable {

	private SolXJustArtSolId id;
	private Solicitudes solicitudes;

	public SolXJustArtSol() {
	}

	public SolXJustArtSol(SolXJustArtSolId id, Solicitudes solicitudes) {
		this.id = id;
		this.solicitudes = solicitudes;
	}

	public SolXJustArtSolId getId() {
		return this.id;
	}

	public void setId(SolXJustArtSolId id) {
		this.id = id;
	}

	public Solicitudes getSolicitudes() {
		return this.solicitudes;
	}

	public void setSolicitudes(Solicitudes solicitudes) {
		this.solicitudes = solicitudes;
	}

}
