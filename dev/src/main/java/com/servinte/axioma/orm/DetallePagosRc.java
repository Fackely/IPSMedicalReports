package com.servinte.axioma.orm;

// Generated Jun 8, 2010 2:12:12 PM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * DetallePagosRc generated by hbm2java
 */
public class DetallePagosRc implements java.io.Serializable {

	private int consecutivo;
	private FormasPago formasPago;
	private RecibosCaja recibosCaja;
	private double valor;
	private Set movimientosTarjetases = new HashSet(0);
	private Set docSopMovimCajases = new HashSet(0);
	private Set movimientosBonoses = new HashSet(0);
	private Set movimientosChequeses = new HashSet(0);
	private Set datosFinanciacions = new HashSet(0);

	public DetallePagosRc() {
	}

	public DetallePagosRc(int consecutivo, FormasPago formasPago,
			RecibosCaja recibosCaja, double valor) {
		this.consecutivo = consecutivo;
		this.formasPago = formasPago;
		this.recibosCaja = recibosCaja;
		this.valor = valor;
	}

	public DetallePagosRc(int consecutivo, FormasPago formasPago,
			RecibosCaja recibosCaja, double valor, Set movimientosTarjetases,
			Set docSopMovimCajases, Set movimientosBonoses,
			Set movimientosChequeses, Set datosFinanciacions) {
		this.consecutivo = consecutivo;
		this.formasPago = formasPago;
		this.recibosCaja = recibosCaja;
		this.valor = valor;
		this.movimientosTarjetases = movimientosTarjetases;
		this.docSopMovimCajases = docSopMovimCajases;
		this.movimientosBonoses = movimientosBonoses;
		this.movimientosChequeses = movimientosChequeses;
		this.datosFinanciacions = datosFinanciacions;
	}

	public int getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public FormasPago getFormasPago() {
		return this.formasPago;
	}

	public void setFormasPago(FormasPago formasPago) {
		this.formasPago = formasPago;
	}

	public RecibosCaja getRecibosCaja() {
		return this.recibosCaja;
	}

	public void setRecibosCaja(RecibosCaja recibosCaja) {
		this.recibosCaja = recibosCaja;
	}

	public double getValor() {
		return this.valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Set getMovimientosTarjetases() {
		return this.movimientosTarjetases;
	}

	public void setMovimientosTarjetases(Set movimientosTarjetases) {
		this.movimientosTarjetases = movimientosTarjetases;
	}

	public Set getDocSopMovimCajases() {
		return this.docSopMovimCajases;
	}

	public void setDocSopMovimCajases(Set docSopMovimCajases) {
		this.docSopMovimCajases = docSopMovimCajases;
	}

	public Set getMovimientosBonoses() {
		return this.movimientosBonoses;
	}

	public void setMovimientosBonoses(Set movimientosBonoses) {
		this.movimientosBonoses = movimientosBonoses;
	}

	public Set getMovimientosChequeses() {
		return this.movimientosChequeses;
	}

	public void setMovimientosChequeses(Set movimientosChequeses) {
		this.movimientosChequeses = movimientosChequeses;
	}

	public Set getDatosFinanciacions() {
		return this.datosFinanciacions;
	}

	public void setDatosFinanciacions(Set datosFinanciacions) {
		this.datosFinanciacions = datosFinanciacions;
	}

}
