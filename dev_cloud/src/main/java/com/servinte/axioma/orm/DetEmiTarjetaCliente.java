package com.servinte.axioma.orm;

// Generated Sep 8, 2010 1:46:00 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * DetEmiTarjetaCliente generated by hbm2java
 */
public class DetEmiTarjetaCliente implements java.io.Serializable {

	private long codigo;
	private Usuarios usuariosByUsuarioResponsable;
	private CentroAtencion centroAtencion;
	private Usuarios usuariosByUsuarioModifica;
	private EncaEmiTarjetaCliente encaEmiTarjetaCliente;
	private Instituciones instituciones;
	private long serialInicial;
	private long serialFinal;
	private Date fechaModifica;
	private String horaModifica;

	public DetEmiTarjetaCliente() {
	}

	public DetEmiTarjetaCliente(long codigo,
			Usuarios usuariosByUsuarioResponsable,
			CentroAtencion centroAtencion, Usuarios usuariosByUsuarioModifica,
			EncaEmiTarjetaCliente encaEmiTarjetaCliente,
			Instituciones instituciones, long serialInicial, long serialFinal,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuariosByUsuarioResponsable = usuariosByUsuarioResponsable;
		this.centroAtencion = centroAtencion;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.encaEmiTarjetaCliente = encaEmiTarjetaCliente;
		this.instituciones = instituciones;
		this.serialInicial = serialInicial;
		this.serialFinal = serialFinal;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Usuarios getUsuariosByUsuarioResponsable() {
		return this.usuariosByUsuarioResponsable;
	}

	public void setUsuariosByUsuarioResponsable(
			Usuarios usuariosByUsuarioResponsable) {
		this.usuariosByUsuarioResponsable = usuariosByUsuarioResponsable;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Usuarios getUsuariosByUsuarioModifica() {
		return this.usuariosByUsuarioModifica;
	}

	public void setUsuariosByUsuarioModifica(Usuarios usuariosByUsuarioModifica) {
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
	}

	public EncaEmiTarjetaCliente getEncaEmiTarjetaCliente() {
		return this.encaEmiTarjetaCliente;
	}

	public void setEncaEmiTarjetaCliente(
			EncaEmiTarjetaCliente encaEmiTarjetaCliente) {
		this.encaEmiTarjetaCliente = encaEmiTarjetaCliente;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
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
