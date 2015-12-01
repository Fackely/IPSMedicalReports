package com.servinte.axioma.orm;

// Generated 15/06/2011 05:09:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * LogSubirPacientes generated by hbm2java
 */
public class LogSubirPacientes implements java.io.Serializable {

	private long codigoPk;
	private Contratos contratos;
	private TiposCargue tiposCargue;
	private Usuarios usuarios;
	private Convenios convenios;
	private Date fechaCargue;
	private long totalLeidos;
	private long totalGrabados;
	private Date fechaInicio;
	private Date fechaFin;
	private String horaCargue;
	private Set inconsistenSubirPacientes = new HashSet(0);

	public LogSubirPacientes() {
	}

	public LogSubirPacientes(long codigoPk, Contratos contratos,
			Usuarios usuarios, Convenios convenios, Date fechaCargue,
			long totalLeidos, long totalGrabados) {
		this.codigoPk = codigoPk;
		this.contratos = contratos;
		this.usuarios = usuarios;
		this.convenios = convenios;
		this.fechaCargue = fechaCargue;
		this.totalLeidos = totalLeidos;
		this.totalGrabados = totalGrabados;
	}

	public LogSubirPacientes(long codigoPk, Contratos contratos,
			TiposCargue tiposCargue, Usuarios usuarios, Convenios convenios,
			Date fechaCargue, long totalLeidos, long totalGrabados,
			Date fechaInicio, Date fechaFin, String horaCargue,
			Set inconsistenSubirPacientes) {
		this.codigoPk = codigoPk;
		this.contratos = contratos;
		this.tiposCargue = tiposCargue;
		this.usuarios = usuarios;
		this.convenios = convenios;
		this.fechaCargue = fechaCargue;
		this.totalLeidos = totalLeidos;
		this.totalGrabados = totalGrabados;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.horaCargue = horaCargue;
		this.inconsistenSubirPacientes = inconsistenSubirPacientes;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Contratos getContratos() {
		return this.contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public TiposCargue getTiposCargue() {
		return this.tiposCargue;
	}

	public void setTiposCargue(TiposCargue tiposCargue) {
		this.tiposCargue = tiposCargue;
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

	public Date getFechaCargue() {
		return this.fechaCargue;
	}

	public void setFechaCargue(Date fechaCargue) {
		this.fechaCargue = fechaCargue;
	}

	public long getTotalLeidos() {
		return this.totalLeidos;
	}

	public void setTotalLeidos(long totalLeidos) {
		this.totalLeidos = totalLeidos;
	}

	public long getTotalGrabados() {
		return this.totalGrabados;
	}

	public void setTotalGrabados(long totalGrabados) {
		this.totalGrabados = totalGrabados;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getHoraCargue() {
		return this.horaCargue;
	}

	public void setHoraCargue(String horaCargue) {
		this.horaCargue = horaCargue;
	}

	public Set getInconsistenSubirPacientes() {
		return this.inconsistenSubirPacientes;
	}

	public void setInconsistenSubirPacientes(Set inconsistenSubirPacientes) {
		this.inconsistenSubirPacientes = inconsistenSubirPacientes;
	}

}
