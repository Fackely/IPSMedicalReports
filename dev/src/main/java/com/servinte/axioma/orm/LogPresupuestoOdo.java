package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * LogPresupuestoOdo generated by hbm2java
 */
public class LogPresupuestoOdo implements java.io.Serializable {

	private long codigoPk;
	private PresupuestoOdontologico presupuestoOdontologico;
	private Usuarios usuarios;
	private Especialidades especialidades;
	private MotivosAtencion motivosAtencion;
	private String estado;
	private Date fechaModifica;
	private String horaModifica;

	public LogPresupuestoOdo() {
	}

	public LogPresupuestoOdo(long codigoPk, Usuarios usuarios, String estado,
			Date fechaModifica, String horaModifica) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.estado = estado;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public LogPresupuestoOdo(long codigoPk,
			PresupuestoOdontologico presupuestoOdontologico, Usuarios usuarios,
			Especialidades especialidades, MotivosAtencion motivosAtencion,
			String estado, Date fechaModifica, String horaModifica) {
		this.codigoPk = codigoPk;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.usuarios = usuarios;
		this.especialidades = especialidades;
		this.motivosAtencion = motivosAtencion;
		this.estado = estado;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public PresupuestoOdontologico getPresupuestoOdontologico() {
		return this.presupuestoOdontologico;
	}

	public void setPresupuestoOdontologico(
			PresupuestoOdontologico presupuestoOdontologico) {
		this.presupuestoOdontologico = presupuestoOdontologico;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Especialidades getEspecialidades() {
		return this.especialidades;
	}

	public void setEspecialidades(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	public MotivosAtencion getMotivosAtencion() {
		return this.motivosAtencion;
	}

	public void setMotivosAtencion(MotivosAtencion motivosAtencion) {
		this.motivosAtencion = motivosAtencion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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
