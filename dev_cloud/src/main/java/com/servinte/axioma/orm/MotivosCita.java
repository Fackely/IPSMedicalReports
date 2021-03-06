package com.servinte.axioma.orm;

// Generated May 3, 2010 4:30:54 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * MotivosCita generated by hbm2java
 */
public class MotivosCita implements java.io.Serializable {

	private int codigoPk;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private String codigo;
	private String descripcion;
	private String activo;
	private Date fechaModifica;
	private String horaModifica;
	private String tipoMotivo;
	private Set clasOdoMotivosNoConfirs = new HashSet(0);
	private Set motivoCitaPacientes = new HashSet(0);
	private Set logCitasOdontologicases = new HashSet(0);
	private Set citasOdontologicases = new HashSet(0);

	public MotivosCita() {
	}

	public MotivosCita(int codigoPk, String codigo, String descripcion,
			String activo, String tipoMotivo) {
		this.codigoPk = codigoPk;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.activo = activo;
		this.tipoMotivo = tipoMotivo;
	}

	public MotivosCita(int codigoPk, Usuarios usuarios,
			Instituciones instituciones, String codigo, String descripcion,
			String activo, Date fechaModifica, String horaModifica,
			String tipoMotivo, Set clasOdoMotivosNoConfirs,
			Set motivoCitaPacientes, Set logCitasOdontologicases,
			Set citasOdontologicases) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.tipoMotivo = tipoMotivo;
		this.clasOdoMotivosNoConfirs = clasOdoMotivosNoConfirs;
		this.motivoCitaPacientes = motivoCitaPacientes;
		this.logCitasOdontologicases = logCitasOdontologicases;
		this.citasOdontologicases = citasOdontologicases;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
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

	public String getTipoMotivo() {
		return this.tipoMotivo;
	}

	public void setTipoMotivo(String tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}

	public Set getClasOdoMotivosNoConfirs() {
		return this.clasOdoMotivosNoConfirs;
	}

	public void setClasOdoMotivosNoConfirs(Set clasOdoMotivosNoConfirs) {
		this.clasOdoMotivosNoConfirs = clasOdoMotivosNoConfirs;
	}

	public Set getMotivoCitaPacientes() {
		return this.motivoCitaPacientes;
	}

	public void setMotivoCitaPacientes(Set motivoCitaPacientes) {
		this.motivoCitaPacientes = motivoCitaPacientes;
	}

	public Set getLogCitasOdontologicases() {
		return this.logCitasOdontologicases;
	}

	public void setLogCitasOdontologicases(Set logCitasOdontologicases) {
		this.logCitasOdontologicases = logCitasOdontologicases;
	}

	public Set getCitasOdontologicases() {
		return this.citasOdontologicases;
	}

	public void setCitasOdontologicases(Set citasOdontologicases) {
		this.citasOdontologicases = citasOdontologicases;
	}

}
