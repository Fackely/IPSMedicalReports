package com.servinte.axioma.orm;

// Generated Feb 8, 2011 11:12:11 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * CierreTempNaturArt generated by hbm2java
 */
public class CierreTempNaturArt implements java.io.Serializable {

	private long codigoPk;
	private Contratos contratos;
	private NaturalezaArticulo naturalezaArticulo;
	private double valorAcumulado;
	private Date fechaCierre;

	public CierreTempNaturArt() {
	}

	public CierreTempNaturArt(long codigoPk, Contratos contratos,
			NaturalezaArticulo naturalezaArticulo, double valorAcumulado,
			Date fechaCierre) {
		this.codigoPk = codigoPk;
		this.contratos = contratos;
		this.naturalezaArticulo = naturalezaArticulo;
		this.valorAcumulado = valorAcumulado;
		this.fechaCierre = fechaCierre;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Contratos getContratos() {
		return this.contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public NaturalezaArticulo getNaturalezaArticulo() {
		return this.naturalezaArticulo;
	}

	public void setNaturalezaArticulo(NaturalezaArticulo naturalezaArticulo) {
		this.naturalezaArticulo = naturalezaArticulo;
	}

	public double getValorAcumulado() {
		return this.valorAcumulado;
	}

	public void setValorAcumulado(double valorAcumulado) {
		this.valorAcumulado = valorAcumulado;
	}

	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

}
