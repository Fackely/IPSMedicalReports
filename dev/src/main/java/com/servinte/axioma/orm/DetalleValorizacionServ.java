package com.servinte.axioma.orm;

// Generated 13/05/2011 11:32:29 AM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * DetalleValorizacionServ generated by hbm2java
 */
public class DetalleValorizacionServ implements java.io.Serializable {

	private long codigo;
	private ParamPresupuestosCap paramPresupuestosCap;
	private NivelAtencion nivelAtencion;
	private GruposServicios gruposServicios;
	private int mes;
	private BigDecimal porcentajeGasto;
	private BigDecimal valorGasto;
	private char activo;

	public DetalleValorizacionServ() {
	}

	public DetalleValorizacionServ(long codigo,
			ParamPresupuestosCap paramPresupuestosCap,
			NivelAtencion nivelAtencion, GruposServicios gruposServicios,
			byte mes, BigDecimal porcentajeGasto, BigDecimal valorGasto,
			char activo) {
		this.codigo = codigo;
		this.paramPresupuestosCap = paramPresupuestosCap;
		this.nivelAtencion = nivelAtencion;
		this.gruposServicios = gruposServicios;
		this.mes = mes;
		this.porcentajeGasto = porcentajeGasto;
		this.valorGasto = valorGasto;
		this.activo = activo;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public ParamPresupuestosCap getParamPresupuestosCap() {
		return this.paramPresupuestosCap;
	}

	public void setParamPresupuestosCap(
			ParamPresupuestosCap paramPresupuestosCap) {
		this.paramPresupuestosCap = paramPresupuestosCap;
	}

	public NivelAtencion getNivelAtencion() {
		return this.nivelAtencion;
	}

	public void setNivelAtencion(NivelAtencion nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	public GruposServicios getGruposServicios() {
		return this.gruposServicios;
	}

	public void setGruposServicios(GruposServicios gruposServicios) {
		this.gruposServicios = gruposServicios;
	}

	public int getMes() {
		return this.mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public BigDecimal getPorcentajeGasto() {
		return this.porcentajeGasto;
	}

	public void setPorcentajeGasto(BigDecimal porcentajeGasto) {
		this.porcentajeGasto = porcentajeGasto;
	}

	public BigDecimal getValorGasto() {
		return this.valorGasto;
	}

	public void setValorGasto(BigDecimal valorGasto) {
		this.valorGasto = valorGasto;
	}

	public char getActivo() {
		return this.activo;
	}

	public void setActivo(char activo) {
		this.activo = activo;
	}

}
