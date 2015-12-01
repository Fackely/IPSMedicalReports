package com.servinte.axioma.orm;

// Generated Jul 22, 2010 12:52:36 PM by Hibernate Tools 3.2.4.GA

/**
 * DestinoTrasladosAbonosPac generated by hbm2java
 */
public class DestinoTrasladosAbonosPac implements java.io.Serializable {

	private long codigoPk;
	private TrasladosAbonos trasladosAbonos;
	private MovimientosAbonos movimientosAbonos;

	public DestinoTrasladosAbonosPac() {
	}

	public DestinoTrasladosAbonosPac(long codigoPk,
			TrasladosAbonos trasladosAbonos, MovimientosAbonos movimientosAbonos) {
		this.codigoPk = codigoPk;
		this.trasladosAbonos = trasladosAbonos;
		this.movimientosAbonos = movimientosAbonos;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public TrasladosAbonos getTrasladosAbonos() {
		return this.trasladosAbonos;
	}

	public void setTrasladosAbonos(TrasladosAbonos trasladosAbonos) {
		this.trasladosAbonos = trasladosAbonos;
	}

	public MovimientosAbonos getMovimientosAbonos() {
		return this.movimientosAbonos;
	}

	public void setMovimientosAbonos(MovimientosAbonos movimientosAbonos) {
		this.movimientosAbonos = movimientosAbonos;
	}

}
