package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * VerificacionesDerechos generated by hbm2java
 */
public class VerificacionesDerechos implements java.io.Serializable {

	private VerificacionesDerechosId id;
	private Ingresos ingresos;
	private SubCuentas subCuentas;
	private Usuarios usuarios;
	private Convenios convenios;
	private String estado;
	private String tipoVerificacion;
	private String numeroVerificacion;
	private String personaSolicita;
	private Date fechaSolicitud;
	private String horaSolicitud;
	private String personaContactada;
	private Date fechaVerificacion;
	private String horaVerificacion;
	private BigDecimal porcentajeCobertura;
	private BigDecimal cuotaVerificacion;
	private String observaciones;
	private Date fechaModifica;
	private String horaModifica;

	public VerificacionesDerechos() {
	}

	public VerificacionesDerechos(VerificacionesDerechosId id,
			Ingresos ingresos, Usuarios usuarios, Convenios convenios,
			String estado, String tipoVerificacion, String personaSolicita,
			Date fechaSolicitud, String horaSolicitud, Date fechaVerificacion,
			String horaVerificacion, Date fechaModifica, String horaModifica) {
		this.id = id;
		this.ingresos = ingresos;
		this.usuarios = usuarios;
		this.convenios = convenios;
		this.estado = estado;
		this.tipoVerificacion = tipoVerificacion;
		this.personaSolicita = personaSolicita;
		this.fechaSolicitud = fechaSolicitud;
		this.horaSolicitud = horaSolicitud;
		this.fechaVerificacion = fechaVerificacion;
		this.horaVerificacion = horaVerificacion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public VerificacionesDerechos(VerificacionesDerechosId id,
			Ingresos ingresos, SubCuentas subCuentas, Usuarios usuarios,
			Convenios convenios, String estado, String tipoVerificacion,
			String numeroVerificacion, String personaSolicita,
			Date fechaSolicitud, String horaSolicitud,
			String personaContactada, Date fechaVerificacion,
			String horaVerificacion, BigDecimal porcentajeCobertura,
			BigDecimal cuotaVerificacion, String observaciones,
			Date fechaModifica, String horaModifica) {
		this.id = id;
		this.ingresos = ingresos;
		this.subCuentas = subCuentas;
		this.usuarios = usuarios;
		this.convenios = convenios;
		this.estado = estado;
		this.tipoVerificacion = tipoVerificacion;
		this.numeroVerificacion = numeroVerificacion;
		this.personaSolicita = personaSolicita;
		this.fechaSolicitud = fechaSolicitud;
		this.horaSolicitud = horaSolicitud;
		this.personaContactada = personaContactada;
		this.fechaVerificacion = fechaVerificacion;
		this.horaVerificacion = horaVerificacion;
		this.porcentajeCobertura = porcentajeCobertura;
		this.cuotaVerificacion = cuotaVerificacion;
		this.observaciones = observaciones;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public VerificacionesDerechosId getId() {
		return this.id;
	}

	public void setId(VerificacionesDerechosId id) {
		this.id = id;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public SubCuentas getSubCuentas() {
		return this.subCuentas;
	}

	public void setSubCuentas(SubCuentas subCuentas) {
		this.subCuentas = subCuentas;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoVerificacion() {
		return this.tipoVerificacion;
	}

	public void setTipoVerificacion(String tipoVerificacion) {
		this.tipoVerificacion = tipoVerificacion;
	}

	public String getNumeroVerificacion() {
		return this.numeroVerificacion;
	}

	public void setNumeroVerificacion(String numeroVerificacion) {
		this.numeroVerificacion = numeroVerificacion;
	}

	public String getPersonaSolicita() {
		return this.personaSolicita;
	}

	public void setPersonaSolicita(String personaSolicita) {
		this.personaSolicita = personaSolicita;
	}

	public Date getFechaSolicitud() {
		return this.fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public String getHoraSolicitud() {
		return this.horaSolicitud;
	}

	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	public String getPersonaContactada() {
		return this.personaContactada;
	}

	public void setPersonaContactada(String personaContactada) {
		this.personaContactada = personaContactada;
	}

	public Date getFechaVerificacion() {
		return this.fechaVerificacion;
	}

	public void setFechaVerificacion(Date fechaVerificacion) {
		this.fechaVerificacion = fechaVerificacion;
	}

	public String getHoraVerificacion() {
		return this.horaVerificacion;
	}

	public void setHoraVerificacion(String horaVerificacion) {
		this.horaVerificacion = horaVerificacion;
	}

	public BigDecimal getPorcentajeCobertura() {
		return this.porcentajeCobertura;
	}

	public void setPorcentajeCobertura(BigDecimal porcentajeCobertura) {
		this.porcentajeCobertura = porcentajeCobertura;
	}

	public BigDecimal getCuotaVerificacion() {
		return this.cuotaVerificacion;
	}

	public void setCuotaVerificacion(BigDecimal cuotaVerificacion) {
		this.cuotaVerificacion = cuotaVerificacion;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
