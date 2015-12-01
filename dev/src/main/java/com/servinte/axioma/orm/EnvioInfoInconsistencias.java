package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * EnvioInfoInconsistencias generated by hbm2java
 */
public class EnvioInfoInconsistencias implements java.io.Serializable {

	private long codigoPk;
	private Convenios convenios;
	private InformeInconsistencias informeInconsistencias;
	private Usuarios usuarios;
	private Empresas empresas;
	private Date fechaEnvio;
	private String horaEnvio;
	private String medioEnvio;
	private String pathArchivo;

	public EnvioInfoInconsistencias() {
	}

	public EnvioInfoInconsistencias(long codigoPk,
			InformeInconsistencias informeInconsistencias, Usuarios usuarios,
			Date fechaEnvio, String horaEnvio, String medioEnvio) {
		this.codigoPk = codigoPk;
		this.informeInconsistencias = informeInconsistencias;
		this.usuarios = usuarios;
		this.fechaEnvio = fechaEnvio;
		this.horaEnvio = horaEnvio;
		this.medioEnvio = medioEnvio;
	}

	public EnvioInfoInconsistencias(long codigoPk, Convenios convenios,
			InformeInconsistencias informeInconsistencias, Usuarios usuarios,
			Empresas empresas, Date fechaEnvio, String horaEnvio,
			String medioEnvio, String pathArchivo) {
		this.codigoPk = codigoPk;
		this.convenios = convenios;
		this.informeInconsistencias = informeInconsistencias;
		this.usuarios = usuarios;
		this.empresas = empresas;
		this.fechaEnvio = fechaEnvio;
		this.horaEnvio = horaEnvio;
		this.medioEnvio = medioEnvio;
		this.pathArchivo = pathArchivo;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public InformeInconsistencias getInformeInconsistencias() {
		return this.informeInconsistencias;
	}

	public void setInformeInconsistencias(
			InformeInconsistencias informeInconsistencias) {
		this.informeInconsistencias = informeInconsistencias;
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

	public Date getFechaEnvio() {
		return this.fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public String getHoraEnvio() {
		return this.horaEnvio;
	}

	public void setHoraEnvio(String horaEnvio) {
		this.horaEnvio = horaEnvio;
	}

	public String getMedioEnvio() {
		return this.medioEnvio;
	}

	public void setMedioEnvio(String medioEnvio) {
		this.medioEnvio = medioEnvio;
	}

	public String getPathArchivo() {
		return this.pathArchivo;
	}

	public void setPathArchivo(String pathArchivo) {
		this.pathArchivo = pathArchivo;
	}

}
