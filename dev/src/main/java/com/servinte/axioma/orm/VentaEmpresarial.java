package com.servinte.axioma.orm;

// Generated Aug 19, 2010 5:10:52 PM by Hibernate Tools 3.2.4.GA

/**
 * VentaEmpresarial generated by hbm2java
 */
public class VentaEmpresarial implements java.io.Serializable {

	private long codigoVenta;
	private Convenios convenios;
	private VentaTarjetaCliente ventaTarjetaCliente;
	private long serialInicial;
	private long serialFinal;

	public VentaEmpresarial() {
	}

	public VentaEmpresarial(VentaTarjetaCliente ventaTarjetaCliente,
			long serialInicial, long serialFinal) {
		this.ventaTarjetaCliente = ventaTarjetaCliente;
		this.serialInicial = serialInicial;
		this.serialFinal = serialFinal;
	}

	public VentaEmpresarial(Convenios convenios,
			VentaTarjetaCliente ventaTarjetaCliente, long serialInicial,
			long serialFinal) {
		this.convenios = convenios;
		this.ventaTarjetaCliente = ventaTarjetaCliente;
		this.serialInicial = serialInicial;
		this.serialFinal = serialFinal;
	}

	public long getCodigoVenta() {
		return this.codigoVenta;
	}

	public void setCodigoVenta(long codigoVenta) {
		this.codigoVenta = codigoVenta;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public VentaTarjetaCliente getVentaTarjetaCliente() {
		return this.ventaTarjetaCliente;
	}

	public void setVentaTarjetaCliente(VentaTarjetaCliente ventaTarjetaCliente) {
		this.ventaTarjetaCliente = ventaTarjetaCliente;
	}

	public long getSerialInicial() {
		return this.serialInicial;
	}

	public void setSerialInicial(long serialInicial) {
		this.serialInicial = serialInicial;
	}

	public long getSerialFinal() {
		return this.serialFinal;
	}

	public void setSerialFinal(long serialFinal) {
		this.serialFinal = serialFinal;
	}

}
