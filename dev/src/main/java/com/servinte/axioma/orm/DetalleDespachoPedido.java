package com.servinte.axioma.orm;

// Generated Dec 1, 2010 10:58:26 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * DetalleDespachoPedido generated by hbm2java
 */
public class DetalleDespachoPedido implements java.io.Serializable {

	private DetalleDespachoPedidoId id;
	private DespachoPedido despachoPedido;
	private Terceros tercerosByProveedorCompra;
	private Articulo articulo;
	private CentrosCosto centrosCosto;
	private Terceros tercerosByProveedorCatalogo;
	private int cantidad;
	private Double costoUnitario;
	private String lote;
	private Date fechaVencimiento;
	private String tipoDespacho;

	public DetalleDespachoPedido() {
	}

	public DetalleDespachoPedido(DetalleDespachoPedidoId id,
			DespachoPedido despachoPedido, Articulo articulo, int cantidad) {
		this.id = id;
		this.despachoPedido = despachoPedido;
		this.articulo = articulo;
		this.cantidad = cantidad;
	}

	public DetalleDespachoPedido(DetalleDespachoPedidoId id,
			DespachoPedido despachoPedido, Terceros tercerosByProveedorCompra,
			Articulo articulo, CentrosCosto centrosCosto,
			Terceros tercerosByProveedorCatalogo, int cantidad,
			Double costoUnitario, String lote, Date fechaVencimiento,
			String tipoDespacho) {
		this.id = id;
		this.despachoPedido = despachoPedido;
		this.tercerosByProveedorCompra = tercerosByProveedorCompra;
		this.articulo = articulo;
		this.centrosCosto = centrosCosto;
		this.tercerosByProveedorCatalogo = tercerosByProveedorCatalogo;
		this.cantidad = cantidad;
		this.costoUnitario = costoUnitario;
		this.lote = lote;
		this.fechaVencimiento = fechaVencimiento;
		this.tipoDespacho = tipoDespacho;
	}

	public DetalleDespachoPedidoId getId() {
		return this.id;
	}

	public void setId(DetalleDespachoPedidoId id) {
		this.id = id;
	}

	public DespachoPedido getDespachoPedido() {
		return this.despachoPedido;
	}

	public void setDespachoPedido(DespachoPedido despachoPedido) {
		this.despachoPedido = despachoPedido;
	}

	public Terceros getTercerosByProveedorCompra() {
		return this.tercerosByProveedorCompra;
	}

	public void setTercerosByProveedorCompra(Terceros tercerosByProveedorCompra) {
		this.tercerosByProveedorCompra = tercerosByProveedorCompra;
	}

	public Articulo getArticulo() {
		return this.articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public CentrosCosto getCentrosCosto() {
		return this.centrosCosto;
	}

	public void setCentrosCosto(CentrosCosto centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	public Terceros getTercerosByProveedorCatalogo() {
		return this.tercerosByProveedorCatalogo;
	}

	public void setTercerosByProveedorCatalogo(
			Terceros tercerosByProveedorCatalogo) {
		this.tercerosByProveedorCatalogo = tercerosByProveedorCatalogo;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Double getCostoUnitario() {
		return this.costoUnitario;
	}

	public void setCostoUnitario(Double costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public String getLote() {
		return this.lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public Date getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getTipoDespacho() {
		return this.tipoDespacho;
	}

	public void setTipoDespacho(String tipoDespacho) {
		this.tipoDespacho = tipoDespacho;
	}

}
