package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * TrasladoCentroAtencion generated by hbm2java
 */
public class TrasladoCentroAtencion implements java.io.Serializable {

	private long consecutivo;
	private Cuentas cuentas;
	private TrasladoCama trasladoCama;
	private Usuarios usuarios;
	private CentroAtencion centroAtencionByCentroAtencionInicial;
	private CentrosCosto centrosCostoByAreaInicial;
	private CentrosCosto centrosCostoByNuevaArea;
	private Instituciones instituciones;
	private CentroAtencion centroAtencionByNuevoCentroAtencion;
	private String observaciones;
	private Date fechaTraslado;
	private String horaTraslado;

	public TrasladoCentroAtencion() {
	}

	public TrasladoCentroAtencion(long consecutivo, Cuentas cuentas,
			Usuarios usuarios,
			CentroAtencion centroAtencionByCentroAtencionInicial,
			CentrosCosto centrosCostoByAreaInicial,
			CentrosCosto centrosCostoByNuevaArea, Instituciones instituciones,
			CentroAtencion centroAtencionByNuevoCentroAtencion,
			Date fechaTraslado, String horaTraslado) {
		this.consecutivo = consecutivo;
		this.cuentas = cuentas;
		this.usuarios = usuarios;
		this.centroAtencionByCentroAtencionInicial = centroAtencionByCentroAtencionInicial;
		this.centrosCostoByAreaInicial = centrosCostoByAreaInicial;
		this.centrosCostoByNuevaArea = centrosCostoByNuevaArea;
		this.instituciones = instituciones;
		this.centroAtencionByNuevoCentroAtencion = centroAtencionByNuevoCentroAtencion;
		this.fechaTraslado = fechaTraslado;
		this.horaTraslado = horaTraslado;
	}

	public TrasladoCentroAtencion(long consecutivo, Cuentas cuentas,
			TrasladoCama trasladoCama, Usuarios usuarios,
			CentroAtencion centroAtencionByCentroAtencionInicial,
			CentrosCosto centrosCostoByAreaInicial,
			CentrosCosto centrosCostoByNuevaArea, Instituciones instituciones,
			CentroAtencion centroAtencionByNuevoCentroAtencion,
			String observaciones, Date fechaTraslado, String horaTraslado) {
		this.consecutivo = consecutivo;
		this.cuentas = cuentas;
		this.trasladoCama = trasladoCama;
		this.usuarios = usuarios;
		this.centroAtencionByCentroAtencionInicial = centroAtencionByCentroAtencionInicial;
		this.centrosCostoByAreaInicial = centrosCostoByAreaInicial;
		this.centrosCostoByNuevaArea = centrosCostoByNuevaArea;
		this.instituciones = instituciones;
		this.centroAtencionByNuevoCentroAtencion = centroAtencionByNuevoCentroAtencion;
		this.observaciones = observaciones;
		this.fechaTraslado = fechaTraslado;
		this.horaTraslado = horaTraslado;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Cuentas getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(Cuentas cuentas) {
		this.cuentas = cuentas;
	}

	public TrasladoCama getTrasladoCama() {
		return this.trasladoCama;
	}

	public void setTrasladoCama(TrasladoCama trasladoCama) {
		this.trasladoCama = trasladoCama;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public CentroAtencion getCentroAtencionByCentroAtencionInicial() {
		return this.centroAtencionByCentroAtencionInicial;
	}

	public void setCentroAtencionByCentroAtencionInicial(
			CentroAtencion centroAtencionByCentroAtencionInicial) {
		this.centroAtencionByCentroAtencionInicial = centroAtencionByCentroAtencionInicial;
	}

	public CentrosCosto getCentrosCostoByAreaInicial() {
		return this.centrosCostoByAreaInicial;
	}

	public void setCentrosCostoByAreaInicial(
			CentrosCosto centrosCostoByAreaInicial) {
		this.centrosCostoByAreaInicial = centrosCostoByAreaInicial;
	}

	public CentrosCosto getCentrosCostoByNuevaArea() {
		return this.centrosCostoByNuevaArea;
	}

	public void setCentrosCostoByNuevaArea(CentrosCosto centrosCostoByNuevaArea) {
		this.centrosCostoByNuevaArea = centrosCostoByNuevaArea;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public CentroAtencion getCentroAtencionByNuevoCentroAtencion() {
		return this.centroAtencionByNuevoCentroAtencion;
	}

	public void setCentroAtencionByNuevoCentroAtencion(
			CentroAtencion centroAtencionByNuevoCentroAtencion) {
		this.centroAtencionByNuevoCentroAtencion = centroAtencionByNuevoCentroAtencion;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaTraslado() {
		return this.fechaTraslado;
	}

	public void setFechaTraslado(Date fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}

	public String getHoraTraslado() {
		return this.horaTraslado;
	}

	public void setHoraTraslado(String horaTraslado) {
		this.horaTraslado = horaTraslado;
	}

}
