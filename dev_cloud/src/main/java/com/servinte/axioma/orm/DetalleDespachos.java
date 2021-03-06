package com.servinte.axioma.orm;

// Generated Nov 25, 2010 5:24:04 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;

/**
 * DetalleDespachos generated by hbm2java
 */
public class DetalleDespachos implements java.io.Serializable {

	private DetalleDespachosId id;
	private Despacho despacho;
	private Articulo articuloByArtPrincipal;
	private Terceros tercerosByProveedorCompra;
	private Articulo articuloByArticulo;
	private CentrosCosto centrosCosto;
	private Terceros tercerosByProveedorCatalogo;
	private int cantidad;
	private BigDecimal costoUnitario;
	private String lote;
	private Date fechaVencimiento;
	private String tipoDespacho;
	private Long saldosDosis;
	private BigDecimal costoDonacion;
	private String observaciones;

	public DetalleDespachos() {
	}

	public DetalleDespachos(DetalleDespachosId id, Despacho despacho,
			Articulo articuloByArticulo, int cantidad) {
		this.id = id;
		this.despacho = despacho;
		this.articuloByArticulo = articuloByArticulo;
		this.cantidad = cantidad;
	}

	public DetalleDespachos(DetalleDespachosId id, Despacho despacho,
			Articulo articuloByArtPrincipal,
			Terceros tercerosByProveedorCompra, Articulo articuloByArticulo,
			CentrosCosto centrosCosto, Terceros tercerosByProveedorCatalogo,
			int cantidad, BigDecimal costoUnitario, String lote,
			Date fechaVencimiento, String tipoDespacho, Long saldosDosis,
			BigDecimal costoDonacion, String observaciones) {
		this.id = id;
		this.despacho = despacho;
		this.articuloByArtPrincipal = articuloByArtPrincipal;
		this.tercerosByProveedorCompra = tercerosByProveedorCompra;
		this.articuloByArticulo = articuloByArticulo;
		this.centrosCosto = centrosCosto;
		this.tercerosByProveedorCatalogo = tercerosByProveedorCatalogo;
		this.cantidad = cantidad;
		this.costoUnitario = costoUnitario;
		this.lote = lote;
		this.fechaVencimiento = fechaVencimiento;
		this.tipoDespacho = tipoDespacho;
		this.saldosDosis = saldosDosis;
		this.costoDonacion = costoDonacion;
		this.observaciones = observaciones;
	}

	public DetalleDespachosId getId() {
		return this.id;
	}

	public void setId(DetalleDespachosId id) {
		this.id = id;
	}

	public Despacho getDespacho() {
		return this.despacho;
	}

	public void setDespacho(Despacho despacho) {
		this.despacho = despacho;
	}

	public Articulo getArticuloByArtPrincipal() {
		return this.articuloByArtPrincipal;
	}

	public void setArticuloByArtPrincipal(Articulo articuloByArtPrincipal) {
		this.articuloByArtPrincipal = articuloByArtPrincipal;
	}

	public Terceros getTercerosByProveedorCompra() {
		return this.tercerosByProveedorCompra;
	}

	public void setTercerosByProveedorCompra(Terceros tercerosByProveedorCompra) {
		this.tercerosByProveedorCompra = tercerosByProveedorCompra;
	}

	public Articulo getArticuloByArticulo() {
		return this.articuloByArticulo;
	}

	public void setArticuloByArticulo(Articulo articuloByArticulo) {
		this.articuloByArticulo = articuloByArticulo;
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

	public BigDecimal getCostoUnitario() {
		return this.costoUnitario;
	}

	public void setCostoUnitario(BigDecimal costoUnitario) {
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

	public Long getSaldosDosis() {
		return this.saldosDosis;
	}

	public void setSaldosDosis(Long saldosDosis) {
		this.saldosDosis = saldosDosis;
	}

	public BigDecimal getCostoDonacion() {
		return this.costoDonacion;
	}

	public void setCostoDonacion(BigDecimal costoDonacion) {
		this.costoDonacion = costoDonacion;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
