package com.servinte.axioma.orm;

// Generated Jun 8, 2010 2:12:12 PM by Hibernate Tools 3.2.4.GA

/**
 * CierreCajaSolicitudTras generated by hbm2java
 */
public class CierreCajaSolicitudTras implements java.io.Serializable {

	private long codigoPk;
	private SolicitudTrasladoCaja solicitudTrasladoCaja;
	private MovimientosCaja movimientosCaja;

	public CierreCajaSolicitudTras() {
	}

	public CierreCajaSolicitudTras(long codigoPk,
			SolicitudTrasladoCaja solicitudTrasladoCaja,
			MovimientosCaja movimientosCaja) {
		this.codigoPk = codigoPk;
		this.solicitudTrasladoCaja = solicitudTrasladoCaja;
		this.movimientosCaja = movimientosCaja;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
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

}
