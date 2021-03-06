package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * ProrrogaAutEntidadSub generated by hbm2java
 */
public class ProrrogaAutEntidadSub implements java.io.Serializable {

	private long consecutivo;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;
	private Date fechaVencimientoInicial;
	private Date fechaProrroga;
	private String horaProrroga;
	private String usuarioProrroga;
	private Date fechaVencimientoNueva;

	public ProrrogaAutEntidadSub() {
	}

	public ProrrogaAutEntidadSub(long consecutivo,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.consecutivo = consecutivo;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public ProrrogaAutEntidadSub(long consecutivo,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			Date fechaVencimientoInicial, Date fechaProrroga,
			String horaProrroga, String usuarioProrroga,
			Date fechaVencimientoNueva) {
		this.consecutivo = consecutivo;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.fechaVencimientoInicial = fechaVencimientoInicial;
		this.fechaProrroga = fechaProrroga;
		this.horaProrroga = horaProrroga;
		this.usuarioProrroga = usuarioProrroga;
		this.fechaVencimientoNueva = fechaVencimientoNueva;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public Date getFechaVencimientoInicial() {
		return this.fechaVencimientoInicial;
	}

	public void setFechaVencimientoInicial(Date fechaVencimientoInicial) {
		this.fechaVencimientoInicial = fechaVencimientoInicial;
	}

	public Date getFechaProrroga() {
		return this.fechaProrroga;
	}

	public void setFechaProrroga(Date fechaProrroga) {
		this.fechaProrroga = fechaProrroga;
	}

	public String getHoraProrroga() {
		return this.horaProrroga;
	}

	public void setHoraProrroga(String horaProrroga) {
		this.horaProrroga = horaProrroga;
	}

	public String getUsuarioProrroga() {
		return this.usuarioProrroga;
	}

	public void setUsuarioProrroga(String usuarioProrroga) {
		this.usuarioProrroga = usuarioProrroga;
	}

	public Date getFechaVencimientoNueva() {
		return this.fechaVencimientoNueva;
	}

	public void setFechaVencimientoNueva(Date fechaVencimientoNueva) {
		this.fechaVencimientoNueva = fechaVencimientoNueva;
	}

}
