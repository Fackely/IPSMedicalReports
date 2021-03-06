package com.servinte.axioma.orm;

// Generated Sep 3, 2010 2:49:56 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * HistoMontoArticuloEsp generated by hbm2java
 */
public class HistoMontoArticuloEsp implements java.io.Serializable {

	private int codigoPk;
	private HistoDetalleMonto histoDetalleMonto;
	private Usuarios usuarios;
	private Integer articulo;
	private Integer cantidadArticulos;
	private int cantidadMonto;
	private double valorMonto;
	private Date fechaRegistro;
	private String horaRegistro;
	private String accionRealizada;

	public HistoMontoArticuloEsp() {
	}

	public HistoMontoArticuloEsp(int codigoPk,
			HistoDetalleMonto histoDetalleMonto, Usuarios usuarios,
			int cantidadMonto, double valorMonto, Date fechaRegistro,
			String horaRegistro, String accionRealizada) {
		this.codigoPk = codigoPk;
		this.histoDetalleMonto = histoDetalleMonto;
		this.usuarios = usuarios;
		this.cantidadMonto = cantidadMonto;
		this.valorMonto = valorMonto;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.accionRealizada = accionRealizada;
	}

	public HistoMontoArticuloEsp(int codigoPk,
			HistoDetalleMonto histoDetalleMonto, Usuarios usuarios,
			Integer articulo, Integer cantidadArticulos, int cantidadMonto,
			double valorMonto, Date fechaRegistro, String horaRegistro,
			String accionRealizada) {
		this.codigoPk = codigoPk;
		this.histoDetalleMonto = histoDetalleMonto;
		this.usuarios = usuarios;
		this.articulo = articulo;
		this.cantidadArticulos = cantidadArticulos;
		this.cantidadMonto = cantidadMonto;
		this.valorMonto = valorMonto;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.accionRealizada = accionRealizada;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public HistoDetalleMonto getHistoDetalleMonto() {
		return this.histoDetalleMonto;
	}

	public void setHistoDetalleMonto(HistoDetalleMonto histoDetalleMonto) {
		this.histoDetalleMonto = histoDetalleMonto;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Integer getArticulo() {
		return this.articulo;
	}

	public void setArticulo(Integer articulo) {
		this.articulo = articulo;
	}

	public Integer getCantidadArticulos() {
		return this.cantidadArticulos;
	}

	public void setCantidadArticulos(Integer cantidadArticulos) {
		this.cantidadArticulos = cantidadArticulos;
	}

	public int getCantidadMonto() {
		return this.cantidadMonto;
	}

	public void setCantidadMonto(int cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}

	public double getValorMonto() {
		return this.valorMonto;
	}

	public void setValorMonto(double valorMonto) {
		this.valorMonto = valorMonto;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getHoraRegistro() {
		return this.horaRegistro;
	}

	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	public String getAccionRealizada() {
		return this.accionRealizada;
	}

	public void setAccionRealizada(String accionRealizada) {
		this.accionRealizada = accionRealizada;
	}

}
