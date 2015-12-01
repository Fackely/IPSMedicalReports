package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * LogExCoberturasEntSub generated by hbm2java
 */
public class LogExCoberturasEntSub implements java.io.Serializable {

	private long consecutivo;
	private NaturalezaPacientes naturalezaPacientes;
	private TiposPaciente tiposPaciente;
	private Usuarios usuarios;
	private ViasIngreso viasIngreso;
	private ExCoberturasEntidadSub exCoberturasEntidadSub;
	private Date fechaModifica;
	private String horaModifica;

	public LogExCoberturasEntSub() {
	}

	public LogExCoberturasEntSub(long consecutivo, Usuarios usuarios,
			ExCoberturasEntidadSub exCoberturasEntidadSub, Date fechaModifica,
			String horaModifica) {
		this.consecutivo = consecutivo;
		this.usuarios = usuarios;
		this.exCoberturasEntidadSub = exCoberturasEntidadSub;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public LogExCoberturasEntSub(long consecutivo,
			NaturalezaPacientes naturalezaPacientes,
			TiposPaciente tiposPaciente, Usuarios usuarios,
			ViasIngreso viasIngreso,
			ExCoberturasEntidadSub exCoberturasEntidadSub, Date fechaModifica,
			String horaModifica) {
		this.consecutivo = consecutivo;
		this.naturalezaPacientes = naturalezaPacientes;
		this.tiposPaciente = tiposPaciente;
		this.usuarios = usuarios;
		this.viasIngreso = viasIngreso;
		this.exCoberturasEntidadSub = exCoberturasEntidadSub;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public NaturalezaPacientes getNaturalezaPacientes() {
		return this.naturalezaPacientes;
	}

	public void setNaturalezaPacientes(NaturalezaPacientes naturalezaPacientes) {
		this.naturalezaPacientes = naturalezaPacientes;
	}

	public TiposPaciente getTiposPaciente() {
		return this.tiposPaciente;
	}

	public void setTiposPaciente(TiposPaciente tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public ViasIngreso getViasIngreso() {
		return this.viasIngreso;
	}

	public void setViasIngreso(ViasIngreso viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	public ExCoberturasEntidadSub getExCoberturasEntidadSub() {
		return this.exCoberturasEntidadSub;
	}

	public void setExCoberturasEntidadSub(
			ExCoberturasEntidadSub exCoberturasEntidadSub) {
		this.exCoberturasEntidadSub = exCoberturasEntidadSub;
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

}
