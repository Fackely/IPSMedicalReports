package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * Movimientos generated by hbm2java
 */
public class Movimientos implements java.io.Serializable {

	private int codigo;
	private TiposMovimiento tiposMovimiento;
	private Facturas facturas;
	private Usuarios usuarios;
	private double valor;
	private Date fecha;
	private Date hora;

	public Movimientos() {
	}

	public Movimientos(int codigo, TiposMovimiento tiposMovimiento,
			Facturas facturas, Usuarios usuarios, double valor, Date fecha,
			Date hora) {
		this.codigo = codigo;
		this.tiposMovimiento = tiposMovimiento;
		this.facturas = facturas;
		this.usuarios = usuarios;
		this.valor = valor;
		this.fecha = fecha;
		this.hora = hora;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public TiposMovimiento getTiposMovimiento() {
		return this.tiposMovimiento;
	}

	public void setTiposMovimiento(TiposMovimiento tiposMovimiento) {
		this.tiposMovimiento = tiposMovimiento;
	}

	public Facturas getFacturas() {
		return this.facturas;
	}

	public void setFacturas(Facturas facturas) {
		this.facturas = facturas;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public double getValor() {
		return this.valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

}
