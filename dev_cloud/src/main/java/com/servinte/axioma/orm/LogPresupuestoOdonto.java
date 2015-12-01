package com.servinte.axioma.orm;

// Generated Feb 7, 2011 11:17:36 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * LogPresupuestoOdonto generated by hbm2java
 */
public class LogPresupuestoOdonto implements java.io.Serializable {

	private long codigoPk;
	private Ingresos ingresos;
	private PlanTratamiento planTratamiento;
	private Cuentas cuentas;
	private CentroAtencion centroAtencion;
	private PresupuestoOdontologico presupuestoOdontologico;
	private Pacientes pacientes;
	private Usuarios usuariosByUsuarioModifica;
	private Especialidades especialidades;
	private Usuarios usuariosByUsuarioGeneracion;
	private Instituciones instituciones;
	private String estado;
	private Integer motivo;
	private Date fechaModifica;
	private String horaModifica;
	private long consecutivo;
	private Date fechaGeneracion;
	private String horaGeneracion;
	private String migrado;

	public LogPresupuestoOdonto() {
	}

	public LogPresupuestoOdonto(long codigoPk, Ingresos ingresos,
			PlanTratamiento planTratamiento, Cuentas cuentas,
			CentroAtencion centroAtencion,
			PresupuestoOdontologico presupuestoOdontologico,
			Pacientes pacientes, Usuarios usuariosByUsuarioModifica,
			Usuarios usuariosByUsuarioGeneracion, Instituciones instituciones,
			String estado, Date fechaModifica, String horaModifica,
			long consecutivo, Date fechaGeneracion, String horaGeneracion) {
		this.codigoPk = codigoPk;
		this.ingresos = ingresos;
		this.planTratamiento = planTratamiento;
		this.cuentas = cuentas;
		this.centroAtencion = centroAtencion;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.pacientes = pacientes;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.usuariosByUsuarioGeneracion = usuariosByUsuarioGeneracion;
		this.instituciones = instituciones;
		this.estado = estado;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.consecutivo = consecutivo;
		this.fechaGeneracion = fechaGeneracion;
		this.horaGeneracion = horaGeneracion;
	}

	public LogPresupuestoOdonto(long codigoPk, Ingresos ingresos,
			PlanTratamiento planTratamiento, Cuentas cuentas,
			CentroAtencion centroAtencion,
			PresupuestoOdontologico presupuestoOdontologico,
			Pacientes pacientes, Usuarios usuariosByUsuarioModifica,
			Especialidades especialidades,
			Usuarios usuariosByUsuarioGeneracion, Instituciones instituciones,
			String estado, Integer motivo, Date fechaModifica,
			String horaModifica, long consecutivo, Date fechaGeneracion,
			String horaGeneracion, String migrado) {
		this.codigoPk = codigoPk;
		this.ingresos = ingresos;
		this.planTratamiento = planTratamiento;
		this.cuentas = cuentas;
		this.centroAtencion = centroAtencion;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.pacientes = pacientes;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.especialidades = especialidades;
		this.usuariosByUsuarioGeneracion = usuariosByUsuarioGeneracion;
		this.instituciones = instituciones;
		this.estado = estado;
		this.motivo = motivo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.consecutivo = consecutivo;
		this.fechaGeneracion = fechaGeneracion;
		this.horaGeneracion = horaGeneracion;
		this.migrado = migrado;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public PlanTratamiento getPlanTratamiento() {
		return this.planTratamiento;
	}

	public void setPlanTratamiento(PlanTratamiento planTratamiento) {
		this.planTratamiento = planTratamiento;
	}

	public Cuentas getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(Cuentas cuentas) {
		this.cuentas = cuentas;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public PresupuestoOdontologico getPresupuestoOdontologico() {
		return this.presupuestoOdontologico;
	}

	public void setPresupuestoOdontologico(
			PresupuestoOdontologico presupuestoOdontologico) {
		this.presupuestoOdontologico = presupuestoOdontologico;
	}

	public Pacientes getPacientes() {
		return this.pacientes;
	}

	public void setPacientes(Pacientes pacientes) {
		this.pacientes = pacientes;
	}

	public Usuarios getUsuariosByUsuarioModifica() {
		return this.usuariosByUsuarioModifica;
	}

	public void setUsuariosByUsuarioModifica(Usuarios usuariosByUsuarioModifica) {
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
	}

	public Especialidades getEspecialidades() {
		return this.especialidades;
	}

	public void setEspecialidades(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	public Usuarios getUsuariosByUsuarioGeneracion() {
		return this.usuariosByUsuarioGeneracion;
	}

	public void setUsuariosByUsuarioGeneracion(
			Usuarios usuariosByUsuarioGeneracion) {
		this.usuariosByUsuarioGeneracion = usuariosByUsuarioGeneracion;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getMotivo() {
		return this.motivo;
	}

	public void setMotivo(Integer motivo) {
		this.motivo = motivo;
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

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Date getFechaGeneracion() {
		return this.fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getHoraGeneracion() {
		return this.horaGeneracion;
	}

	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	public String getMigrado() {
		return this.migrado;
	}

	public void setMigrado(String migrado) {
		this.migrado = migrado;
	}

}
