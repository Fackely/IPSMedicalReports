package com.servinte.axioma.orm;

// Generated Jun 8, 2010 2:12:12 PM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * AceptacionTrasladoCaja generated by hbm2java
 */
public class AceptacionTrasladoCaja implements java.io.Serializable {

	private long movimientoCaja;
	private SolicitudTrasladoCaja solicitudTrasladoCaja;
	private MovimientosCaja movimientosCaja;
	private Usuarios usuarios;
	private Set cierreCajaAcepTrasCajas = new HashSet(0);

	public AceptacionTrasladoCaja() {
	}

	public AceptacionTrasladoCaja(SolicitudTrasladoCaja solicitudTrasladoCaja,
			MovimientosCaja movimientosCaja) {
		this.solicitudTrasladoCaja = solicitudTrasladoCaja;
		this.movimientosCaja = movimientosCaja;
	}

	public AceptacionTrasladoCaja(SolicitudTrasladoCaja solicitudTrasladoCaja,
			MovimientosCaja movimientosCaja, Usuarios usuarios,
			Set cierreCajaAcepTrasCajas) {
		this.solicitudTrasladoCaja = solicitudTrasladoCaja;
		this.movimientosCaja = movimientosCaja;
		this.usuarios = usuarios;
		this.cierreCajaAcepTrasCajas = cierreCajaAcepTrasCajas;
	}

	public long getMovimientoCaja() {
		return this.movimientoCaja;
	}

	public void setMovimientoCaja(long movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}

	public SolicitudTrasladoCaja getSolicitudTrasladoCaja() {
		return this.solicitudTrasladoCaja;
	}

	public void setSolicitudTrasladoCaja(
			SolicitudTrasladoCaja solicitudTrasladoCaja) {
		this.solicitudTrasladoCaja = solicitudTrasladoCaja;
	}

	public MovimientosCaja getMovimientosCaja() {
		return this.movimientosCaja;
	}

	public void setMovimientosCaja(MovimientosCaja movimientosCaja) {
		this.movimientosCaja = movimientosCaja;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Set getCierreCajaAcepTrasCajas() {
		return this.cierreCajaAcepTrasCajas;
	}

	public void setCierreCajaAcepTrasCajas(Set cierreCajaAcepTrasCajas) {
		this.cierreCajaAcepTrasCajas = cierreCajaAcepTrasCajas;
	}

}
