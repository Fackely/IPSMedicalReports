package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:51 AM by Hibernate Tools 3.2.4.GA

/**
 * CierreCajaXAnulaReciboC generated by hbm2java
 */
public class CierreCajaXAnulaReciboC implements java.io.Serializable {

	private long codigoPk;
	private MovimientosCaja movimientosCaja;
	private AnulacionRecibosCaja anulacionRecibosCaja;

	public CierreCajaXAnulaReciboC() {
	}

	public CierreCajaXAnulaReciboC(long codigoPk,
			MovimientosCaja movimientosCaja,
			AnulacionRecibosCaja anulacionRecibosCaja) {
		this.codigoPk = codigoPk;
		this.movimientosCaja = movimientosCaja;
		this.anulacionRecibosCaja = anulacionRecibosCaja;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public MovimientosCaja getMovimientosCaja() {
		return this.movimientosCaja;
	}

	public void setMovimientosCaja(MovimientosCaja movimientosCaja) {
		this.movimientosCaja = movimientosCaja;
	}

	public AnulacionRecibosCaja getAnulacionRecibosCaja() {
		return this.anulacionRecibosCaja;
	}

	public void setAnulacionRecibosCaja(
			AnulacionRecibosCaja anulacionRecibosCaja) {
		this.anulacionRecibosCaja = anulacionRecibosCaja;
	}

}
