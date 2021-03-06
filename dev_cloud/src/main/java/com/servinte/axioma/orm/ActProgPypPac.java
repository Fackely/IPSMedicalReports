package com.servinte.axioma.orm;

// Generated 11/09/2012 02:52:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * ActProgPypPac generated by hbm2java
 */
public class ActProgPypPac implements java.io.Serializable {

	private long codigo;
	private OrdenesAmbulatorias ordenesAmbulatorias;
	private Usuarios usuariosByUsuarioProgramar;
	private Solicitudes solicitudes;
	private Usuarios usuariosByUsuarioCancelar;
	private CentroAtencion centroAtencion;
	private Usuarios usuariosByUsuarioEjecutar;
	private Usuarios usuariosByUsuarioSolicitar;
	private long codigoProgPypPac;
	private long actividad;
	private byte estado;
	private Date fechaSolicitar;
	private String horaSolicitar;
	private Date fechaProgramar;
	private String horaProgramar;
	private Date fechaEjecutar;
	private String horaEjecutar;
	private Date fechaCancelar;
	private String horaCancelar;
	private String motivoCancelacion;
	private Long frecuencia;
	private Short tipoFrecuencia;

	public ActProgPypPac() {
	}

	public ActProgPypPac(long codigo, CentroAtencion centroAtencion,
			long codigoProgPypPac, long actividad, byte estado) {
		this.codigo = codigo;
		this.centroAtencion = centroAtencion;
		this.codigoProgPypPac = codigoProgPypPac;
		this.actividad = actividad;
		this.estado = estado;
	}

	public ActProgPypPac(long codigo, OrdenesAmbulatorias ordenesAmbulatorias,
			Usuarios usuariosByUsuarioProgramar, Solicitudes solicitudes,
			Usuarios usuariosByUsuarioCancelar, CentroAtencion centroAtencion,
			Usuarios usuariosByUsuarioEjecutar,
			Usuarios usuariosByUsuarioSolicitar, long codigoProgPypPac,
			long actividad, byte estado, Date fechaSolicitar,
			String horaSolicitar, Date fechaProgramar, String horaProgramar,
			Date fechaEjecutar, String horaEjecutar, Date fechaCancelar,
			String horaCancelar, String motivoCancelacion, Long frecuencia,
			Short tipoFrecuencia) {
		this.codigo = codigo;
		this.ordenesAmbulatorias = ordenesAmbulatorias;
		this.usuariosByUsuarioProgramar = usuariosByUsuarioProgramar;
		this.solicitudes = solicitudes;
		this.usuariosByUsuarioCancelar = usuariosByUsuarioCancelar;
		this.centroAtencion = centroAtencion;
		this.usuariosByUsuarioEjecutar = usuariosByUsuarioEjecutar;
		this.usuariosByUsuarioSolicitar = usuariosByUsuarioSolicitar;
		this.codigoProgPypPac = codigoProgPypPac;
		this.actividad = actividad;
		this.estado = estado;
		this.fechaSolicitar = fechaSolicitar;
		this.horaSolicitar = horaSolicitar;
		this.fechaProgramar = fechaProgramar;
		this.horaProgramar = horaProgramar;
		this.fechaEjecutar = fechaEjecutar;
		this.horaEjecutar = horaEjecutar;
		this.fechaCancelar = fechaCancelar;
		this.horaCancelar = horaCancelar;
		this.motivoCancelacion = motivoCancelacion;
		this.frecuencia = frecuencia;
		this.tipoFrecuencia = tipoFrecuencia;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public OrdenesAmbulatorias getOrdenesAmbulatorias() {
		return this.ordenesAmbulatorias;
	}

	public void setOrdenesAmbulatorias(OrdenesAmbulatorias ordenesAmbulatorias) {
		this.ordenesAmbulatorias = ordenesAmbulatorias;
	}

	public Usuarios getUsuariosByUsuarioProgramar() {
		return this.usuariosByUsuarioProgramar;
	}

	public void setUsuariosByUsuarioProgramar(
			Usuarios usuariosByUsuarioProgramar) {
		this.usuariosByUsuarioProgramar = usuariosByUsuarioProgramar;
	}

	public Solicitudes getSolicitudes() {
		return this.solicitudes;
	}

	public void setSolicitudes(Solicitudes solicitudes) {
		this.solicitudes = solicitudes;
	}

	public Usuarios getUsuariosByUsuarioCancelar() {
		return this.usuariosByUsuarioCancelar;
	}

	public void setUsuariosByUsuarioCancelar(Usuarios usuariosByUsuarioCancelar) {
		this.usuariosByUsuarioCancelar = usuariosByUsuarioCancelar;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Usuarios getUsuariosByUsuarioEjecutar() {
		return this.usuariosByUsuarioEjecutar;
	}

	public void setUsuariosByUsuarioEjecutar(Usuarios usuariosByUsuarioEjecutar) {
		this.usuariosByUsuarioEjecutar = usuariosByUsuarioEjecutar;
	}

	public Usuarios getUsuariosByUsuarioSolicitar() {
		return this.usuariosByUsuarioSolicitar;
	}

	public void setUsuariosByUsuarioSolicitar(
			Usuarios usuariosByUsuarioSolicitar) {
		this.usuariosByUsuarioSolicitar = usuariosByUsuarioSolicitar;
	}

	public long getCodigoProgPypPac() {
		return this.codigoProgPypPac;
	}

	public void setCodigoProgPypPac(long codigoProgPypPac) {
		this.codigoProgPypPac = codigoProgPypPac;
	}

	public long getActividad() {
		return this.actividad;
	}

	public void setActividad(long actividad) {
		this.actividad = actividad;
	}

	public byte getEstado() {
		return this.estado;
	}

	public void setEstado(byte estado) {
		this.estado = estado;
	}

	public Date getFechaSolicitar() {
		return this.fechaSolicitar;
	}

	public void setFechaSolicitar(Date fechaSolicitar) {
		this.fechaSolicitar = fechaSolicitar;
	}

	public String getHoraSolicitar() {
		return this.horaSolicitar;
	}

	public void setHoraSolicitar(String horaSolicitar) {
		this.horaSolicitar = horaSolicitar;
	}

	public Date getFechaProgramar() {
		return this.fechaProgramar;
	}

	public void setFechaProgramar(Date fechaProgramar) {
		this.fechaProgramar = fechaProgramar;
	}

	public String getHoraProgramar() {
		return this.horaProgramar;
	}

	public void setHoraProgramar(String horaProgramar) {
		this.horaProgramar = horaProgramar;
	}

	public Date getFechaEjecutar() {
		return this.fechaEjecutar;
	}

	public void setFechaEjecutar(Date fechaEjecutar) {
		this.fechaEjecutar = fechaEjecutar;
	}

	public String getHoraEjecutar() {
		return this.horaEjecutar;
	}

	public void setHoraEjecutar(String horaEjecutar) {
		this.horaEjecutar = horaEjecutar;
	}

	public Date getFechaCancelar() {
		return this.fechaCancelar;
	}

	public void setFechaCancelar(Date fechaCancelar) {
		this.fechaCancelar = fechaCancelar;
	}

	public String getHoraCancelar() {
		return this.horaCancelar;
	}

	public void setHoraCancelar(String horaCancelar) {
		this.horaCancelar = horaCancelar;
	}

	public String getMotivoCancelacion() {
		return this.motivoCancelacion;
	}

	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}

	public Long getFrecuencia() {
		return this.frecuencia;
	}

	public void setFrecuencia(Long frecuencia) {
		this.frecuencia = frecuencia;
	}

	public Short getTipoFrecuencia() {
		return this.tipoFrecuencia;
	}

	public void setTipoFrecuencia(Short tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}

}
