package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * CierreIngresos generated by hbm2java
 */
public class CierreIngresos implements java.io.Serializable {

	private long codigo;
	private MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotaper;
	private Usuarios usuariosByUsuarioApertura;
	private Ingresos ingresos;
	private Instituciones instituciones;
	private Usuarios usuariosByUsuarioCierre;
	private MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotcierre;
	private Date fechaCierre;
	private String horaCierre;
	private Date fechaApertura;
	private String horaApertura;
	private String activo;

	public CierreIngresos() {
	}

	public CierreIngresos(long codigo,
			MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotaper,
			Ingresos ingresos, Instituciones instituciones,
			Usuarios usuariosByUsuarioCierre,
			MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotcierre,
			Date fechaCierre, String horaCierre) {
		this.codigo = codigo;
		this.motCierreAperturaIngresosByFkMotaper = motCierreAperturaIngresosByFkMotaper;
		this.ingresos = ingresos;
		this.instituciones = instituciones;
		this.usuariosByUsuarioCierre = usuariosByUsuarioCierre;
		this.motCierreAperturaIngresosByFkMotcierre = motCierreAperturaIngresosByFkMotcierre;
		this.fechaCierre = fechaCierre;
		this.horaCierre = horaCierre;
	}

	public CierreIngresos(long codigo,
			MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotaper,
			Usuarios usuariosByUsuarioApertura, Ingresos ingresos,
			Instituciones instituciones, Usuarios usuariosByUsuarioCierre,
			MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotcierre,
			Date fechaCierre, String horaCierre, Date fechaApertura,
			String horaApertura, String activo) {
		this.codigo = codigo;
		this.motCierreAperturaIngresosByFkMotaper = motCierreAperturaIngresosByFkMotaper;
		this.usuariosByUsuarioApertura = usuariosByUsuarioApertura;
		this.ingresos = ingresos;
		this.instituciones = instituciones;
		this.usuariosByUsuarioCierre = usuariosByUsuarioCierre;
		this.motCierreAperturaIngresosByFkMotcierre = motCierreAperturaIngresosByFkMotcierre;
		this.fechaCierre = fechaCierre;
		this.horaCierre = horaCierre;
		this.fechaApertura = fechaApertura;
		this.horaApertura = horaApertura;
		this.activo = activo;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public MotCierreAperturaIngresos getMotCierreAperturaIngresosByFkMotaper() {
		return this.motCierreAperturaIngresosByFkMotaper;
	}

	public void setMotCierreAperturaIngresosByFkMotaper(
			MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotaper) {
		this.motCierreAperturaIngresosByFkMotaper = motCierreAperturaIngresosByFkMotaper;
	}

	public Usuarios getUsuariosByUsuarioApertura() {
		return this.usuariosByUsuarioApertura;
	}

	public void setUsuariosByUsuarioApertura(Usuarios usuariosByUsuarioApertura) {
		this.usuariosByUsuarioApertura = usuariosByUsuarioApertura;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Usuarios getUsuariosByUsuarioCierre() {
		return this.usuariosByUsuarioCierre;
	}

	public void setUsuariosByUsuarioCierre(Usuarios usuariosByUsuarioCierre) {
		this.usuariosByUsuarioCierre = usuariosByUsuarioCierre;
	}

	public MotCierreAperturaIngresos getMotCierreAperturaIngresosByFkMotcierre() {
		return this.motCierreAperturaIngresosByFkMotcierre;
	}

	public void setMotCierreAperturaIngresosByFkMotcierre(
			MotCierreAperturaIngresos motCierreAperturaIngresosByFkMotcierre) {
		this.motCierreAperturaIngresosByFkMotcierre = motCierreAperturaIngresosByFkMotcierre;
	}

	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public String getHoraCierre() {
		return this.horaCierre;
	}

	public void setHoraCierre(String horaCierre) {
		this.horaCierre = horaCierre;
	}

	public Date getFechaApertura() {
		return this.fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getHoraApertura() {
		return this.horaApertura;
	}

	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

}
