package com.servinte.axioma.orm;

// Generated Jun 8, 2010 5:26:01 PM by Hibernate Tools 3.2.4.GA

/**
 * ProgPaqueteOdonto generated by hbm2java
 */
public class ProgPaqueteOdonto implements java.io.Serializable {

	private int codigoPk;
	private Programas programas;
	private PaquetesOdontologicos paquetesOdontologicos;
	private int cantidad;

	public ProgPaqueteOdonto() {
	}

	public ProgPaqueteOdonto(int codigoPk, Programas programas,
			PaquetesOdontologicos paquetesOdontologicos, int cantidad) {
		this.codigoPk = codigoPk;
		this.programas = programas;
		this.paquetesOdontologicos = paquetesOdontologicos;
		this.cantidad = cantidad;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Programas getProgramas() {
		return this.programas;
	}

	public void setProgramas(Programas programas) {
		this.programas = programas;
	}

	public PaquetesOdontologicos getPaquetesOdontologicos() {
		return this.paquetesOdontologicos;
	}

	public void setPaquetesOdontologicos(
			PaquetesOdontologicos paquetesOdontologicos) {
		this.paquetesOdontologicos = paquetesOdontologicos;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
