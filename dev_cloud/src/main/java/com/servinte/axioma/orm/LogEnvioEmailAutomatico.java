package com.servinte.axioma.orm;

// Generated May 7, 2010 9:53:56 AM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * LogEnvioEmailAutomatico generated by hbm2java
 */
public class LogEnvioEmailAutomatico implements java.io.Serializable {

	private long codigoPk;
	private Date fechaProceso;
	private String horaProceso;
	private String nombreFuncionalidad;
	private String correoDestino;
	private String estado;

	public LogEnvioEmailAutomatico() {
	}

	public LogEnvioEmailAutomatico(long codigoPk, Date fechaProceso,
			String horaProceso, String nombreFuncionalidad,
			String correoDestino, String estado) {
		this.codigoPk = codigoPk;
		this.fechaProceso = fechaProceso;
		this.horaProceso = horaProceso;
		this.nombreFuncionalidad = nombreFuncionalidad;
		this.correoDestino = correoDestino;
		this.estado = estado;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Date getFechaProceso() {
		return this.fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getHoraProceso() {
		return this.horaProceso;
	}

	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}

	public String getNombreFuncionalidad() {
		return this.nombreFuncionalidad;
	}

	public void setNombreFuncionalidad(String nombreFuncionalidad) {
		this.nombreFuncionalidad = nombreFuncionalidad;
	}

	public String getCorreoDestino() {
		return this.correoDestino;
	}

	public void setCorreoDestino(String correoDestino) {
		this.correoDestino = correoDestino;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
