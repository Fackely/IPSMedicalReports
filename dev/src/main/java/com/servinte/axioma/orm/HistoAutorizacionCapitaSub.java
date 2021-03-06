package com.servinte.axioma.orm;

// Generated Jan 14, 2011 5:52:37 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * HistoAutorizacionCapitaSub generated by hbm2java
 */
public class HistoAutorizacionCapitaSub implements java.io.Serializable {

	private long codigoPk;
	private Convenios convenios;
	private Usuarios usuarios;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;
	private AutorizacionesCapitacionSub autorizacionesCapitacionSub;
	private long consecutivo;
	private String tipoAutorizacion;
	private String otroConvenioRecobro;
	private char indicativoTemporal;
	private String descripcionEntidad;
	private String direccionEntidad;
	private String telefonoEntidad;
	private int indicadorPrioridad;
	private Date fechaModifica;
	private String horaModifica;
	private String accionRealizada;
	private String observaciones;
	private Date fechaVencimiento;

	public HistoAutorizacionCapitaSub() {
	}

	public HistoAutorizacionCapitaSub(long codigoPk, Usuarios usuarios,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			AutorizacionesCapitacionSub autorizacionesCapitacionSub,
			long consecutivo, String tipoAutorizacion, char indicativoTemporal,
			int indicadorPrioridad, Date fechaModifica, String horaModifica,
			String accionRealizada, Date fechaVencimiento) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.autorizacionesCapitacionSub = autorizacionesCapitacionSub;
		this.consecutivo = consecutivo;
		this.tipoAutorizacion = tipoAutorizacion;
		this.indicativoTemporal = indicativoTemporal;
		this.indicadorPrioridad = indicadorPrioridad;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.accionRealizada = accionRealizada;
		this.fechaVencimiento = fechaVencimiento;
	}

	public HistoAutorizacionCapitaSub(long codigoPk, Convenios convenios,
			Usuarios usuarios,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			AutorizacionesCapitacionSub autorizacionesCapitacionSub,
			long consecutivo, String tipoAutorizacion,
			String otroConvenioRecobro, char indicativoTemporal,
			String descripcionEntidad, String direccionEntidad,
			String telefonoEntidad, int indicadorPrioridad, Date fechaModifica,
			String horaModifica, String accionRealizada, String observaciones,
			Date fechaVencimiento) {
		this.codigoPk = codigoPk;
		this.convenios = convenios;
		this.usuarios = usuarios;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.autorizacionesCapitacionSub = autorizacionesCapitacionSub;
		this.consecutivo = consecutivo;
		this.tipoAutorizacion = tipoAutorizacion;
		this.otroConvenioRecobro = otroConvenioRecobro;
		this.indicativoTemporal = indicativoTemporal;
		this.descripcionEntidad = descripcionEntidad;
		this.direccionEntidad = direccionEntidad;
		this.telefonoEntidad = telefonoEntidad;
		this.indicadorPrioridad = indicadorPrioridad;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.accionRealizada = accionRealizada;
		this.observaciones = observaciones;
		this.fechaVencimiento = fechaVencimiento;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public AutorizacionesCapitacionSub getAutorizacionesCapitacionSub() {
		return this.autorizacionesCapitacionSub;
	}

	public void setAutorizacionesCapitacionSub(
			AutorizacionesCapitacionSub autorizacionesCapitacionSub) {
		this.autorizacionesCapitacionSub = autorizacionesCapitacionSub;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getTipoAutorizacion() {
		return this.tipoAutorizacion;
	}

	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}

	public String getOtroConvenioRecobro() {
		return this.otroConvenioRecobro;
	}

	public void setOtroConvenioRecobro(String otroConvenioRecobro) {
		this.otroConvenioRecobro = otroConvenioRecobro;
	}

	public char getIndicativoTemporal() {
		return this.indicativoTemporal;
	}

	public void setIndicativoTemporal(char indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}

	public String getDescripcionEntidad() {
		return this.descripcionEntidad;
	}

	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}

	public String getDireccionEntidad() {
		return this.direccionEntidad;
	}

	public void setDireccionEntidad(String direccionEntidad) {
		this.direccionEntidad = direccionEntidad;
	}

	public String getTelefonoEntidad() {
		return this.telefonoEntidad;
	}

	public void setTelefonoEntidad(String telefonoEntidad) {
		this.telefonoEntidad = telefonoEntidad;
	}

	public int getIndicadorPrioridad() {
		return this.indicadorPrioridad;
	}

	public void setIndicadorPrioridad(int indicadorPrioridad) {
		this.indicadorPrioridad = indicadorPrioridad;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getAccionRealizada() {
		return this.accionRealizada;
	}

	public void setAccionRealizada(String accionRealizada) {
		this.accionRealizada = accionRealizada;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

}
