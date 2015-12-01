package com.servinte.axioma.orm;

// Generated Aug 30, 2010 8:37:19 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * UnidadAgendaServCitaOdonto generated by hbm2java
 */
public class UnidadAgendaServCitaOdonto implements java.io.Serializable {

	private long codigoPk;
	private UnidadesConsulta unidadesConsulta;
	private ReferenciasServicio referenciasServicio;
	private Usuarios usuarios;
	private String tipoCita;
	private Date fecha;
	private String hora;

	public UnidadAgendaServCitaOdonto() {
	}

	public UnidadAgendaServCitaOdonto(long codigoPk,
			UnidadesConsulta unidadesConsulta,
			ReferenciasServicio referenciasServicio, Usuarios usuarios,
			String tipoCita, Date fecha, String hora) {
		this.codigoPk = codigoPk;
		this.unidadesConsulta = unidadesConsulta;
		this.referenciasServicio = referenciasServicio;
		this.usuarios = usuarios;
		this.tipoCita = tipoCita;
		this.fecha = fecha;
		this.hora = hora;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public UnidadesConsulta getUnidadesConsulta() {
		return this.unidadesConsulta;
	}

	public void setUnidadesConsulta(UnidadesConsulta unidadesConsulta) {
		this.unidadesConsulta = unidadesConsulta;
	}

	public ReferenciasServicio getReferenciasServicio() {
		return this.referenciasServicio;
	}

	public void setReferenciasServicio(ReferenciasServicio referenciasServicio) {
		this.referenciasServicio = referenciasServicio;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public String getTipoCita() {
		return this.tipoCita;
	}

	public void setTipoCita(String tipoCita) {
		this.tipoCita = tipoCita;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

}
