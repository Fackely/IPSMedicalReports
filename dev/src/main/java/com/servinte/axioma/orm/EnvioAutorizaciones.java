package com.servinte.axioma.orm;

// Generated May 3, 2010 4:30:54 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * EnvioAutorizaciones generated by hbm2java
 */
public class EnvioAutorizaciones implements java.io.Serializable {

	private long codigoPk;
	private DetAutorizaciones detAutorizaciones;
	private Convenios convenios;
	private Usuarios usuarios;
	private Empresas empresas;
	private String medioEnvio;
	private Date fechaModifica;
	private String horaModifica;
	private String pathArchivo;

	public EnvioAutorizaciones() {
	}

	public EnvioAutorizaciones(long codigoPk,
			DetAutorizaciones detAutorizaciones, Usuarios usuarios,
			String medioEnvio, Date fechaModifica, String horaModifica) {
		this.codigoPk = codigoPk;
		this.detAutorizaciones = detAutorizaciones;
		this.usuarios = usuarios;
		this.medioEnvio = medioEnvio;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public EnvioAutorizaciones(long codigoPk,
			DetAutorizaciones detAutorizaciones, Convenios convenios,
			Usuarios usuarios, Empresas empresas, String medioEnvio,
			Date fechaModifica, String horaModifica, String pathArchivo) {
		this.codigoPk = codigoPk;
		this.detAutorizaciones = detAutorizaciones;
		this.convenios = convenios;
		this.usuarios = usuarios;
		this.empresas = empresas;
		this.medioEnvio = medioEnvio;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.pathArchivo = pathArchivo;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public DetAutorizaciones getDetAutorizaciones() {
		return this.detAutorizaciones;
	}

	public void setDetAutorizaciones(DetAutorizaciones detAutorizaciones) {
		this.detAutorizaciones = detAutorizaciones;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Empresas getEmpresas() {
		return this.empresas;
	}

	public void setEmpresas(Empresas empresas) {
		this.empresas = empresas;
	}

	public String getMedioEnvio() {
		return this.medioEnvio;
	}

	public void setMedioEnvio(String medioEnvio) {
		this.medioEnvio = medioEnvio;
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

	public String getPathArchivo() {
		return this.pathArchivo;
	}

	public void setPathArchivo(String pathArchivo) {
		this.pathArchivo = pathArchivo;
	}

}
