package com.servinte.axioma.orm;

// Generated 24/06/2011 03:09:11 PM by Hibernate Tools 3.4.0.CR1

/**
 * AutoEntsubSolicitudes generated by hbm2java
 */
public class AutoEntsubSolicitudes implements java.io.Serializable {

	private long codigoPk;
	private Solicitudes solicitudes;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;
	private char migrado;

	public AutoEntsubSolicitudes() {
	}

	public AutoEntsubSolicitudes(long codigoPk, Solicitudes solicitudes,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub, char migrado) {
		this.codigoPk = codigoPk;
		this.solicitudes = solicitudes;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.migrado = migrado;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Solicitudes getSolicitudes() {
		return this.solicitudes;
	}

	public void setSolicitudes(Solicitudes solicitudes) {
		this.solicitudes = solicitudes;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public char getMigrado() {
		return this.migrado;
	}

	public void setMigrado(char migrado) {
		this.migrado = migrado;
	}

}
