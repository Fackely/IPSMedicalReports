package com.servinte.axioma.orm;

// Generated 27/08/2011 12:59:03 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * TrasladoCajaPrincipalMayor generated by hbm2java
 */
public class TrasladoCajaPrincipalMayor implements java.io.Serializable {

	private long movimientoEntregaCajaMayor;
	private EntregaCajaMayor entregaCajaMayor;
	private Usuarios usuarios;
	private Cajas cajas;
	private Date fecha;
	private String hora;
	private long consecutivo;

	public TrasladoCajaPrincipalMayor() {
	}

	public TrasladoCajaPrincipalMayor(EntregaCajaMayor entregaCajaMayor,
			Usuarios usuarios, Cajas cajas, Date fecha, String hora,
			long consecutivo) {
		this.entregaCajaMayor = entregaCajaMayor;
		this.usuarios = usuarios;
		this.cajas = cajas;
		this.fecha = fecha;
		this.hora = hora;
		this.consecutivo = consecutivo;
	}

	public long getMovimientoEntregaCajaMayor() {
		return this.movimientoEntregaCajaMayor;
	}

	public void setMovimientoEntregaCajaMayor(long movimientoEntregaCajaMayor) {
		this.movimientoEntregaCajaMayor = movimientoEntregaCajaMayor;
	}

	public EntregaCajaMayor getEntregaCajaMayor() {
		return this.entregaCajaMayor;
	}

	public void setEntregaCajaMayor(EntregaCajaMayor entregaCajaMayor) {
		this.entregaCajaMayor = entregaCajaMayor;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Cajas getCajas() {
		return this.cajas;
	}

	public void setCajas(Cajas cajas) {
		this.cajas = cajas;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

}
