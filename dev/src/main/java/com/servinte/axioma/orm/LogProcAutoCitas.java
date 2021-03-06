package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * LogProcAutoCitas generated by hbm2java
 */
public class LogProcAutoCitas implements java.io.Serializable {

	private long codigoPk;
	private CitasOdontologicas citasOdontologicas;
	private Date fechaEjecucion;
	private String horaEjecucion;
	private String estadoInicialCita;
	private int institucion;
	private Set logProcAutoServCitas = new HashSet(0);

	public LogProcAutoCitas() {
	}

	public LogProcAutoCitas(long codigoPk,
			CitasOdontologicas citasOdontologicas, Date fechaEjecucion,
			String horaEjecucion, String estadoInicialCita, int institucion) {
		this.codigoPk = codigoPk;
		this.citasOdontologicas = citasOdontologicas;
		this.fechaEjecucion = fechaEjecucion;
		this.horaEjecucion = horaEjecucion;
		this.estadoInicialCita = estadoInicialCita;
		this.institucion = institucion;
	}

	public LogProcAutoCitas(long codigoPk,
			CitasOdontologicas citasOdontologicas, Date fechaEjecucion,
			String horaEjecucion, String estadoInicialCita, int institucion,
			Set logProcAutoServCitas) {
		this.codigoPk = codigoPk;
		this.citasOdontologicas = citasOdontologicas;
		this.fechaEjecucion = fechaEjecucion;
		this.horaEjecucion = horaEjecucion;
		this.estadoInicialCita = estadoInicialCita;
		this.institucion = institucion;
		this.logProcAutoServCitas = logProcAutoServCitas;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public CitasOdontologicas getCitasOdontologicas() {
		return this.citasOdontologicas;
	}

	public void setCitasOdontologicas(CitasOdontologicas citasOdontologicas) {
		this.citasOdontologicas = citasOdontologicas;
	}

	public Date getFechaEjecucion() {
		return this.fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public String getHoraEjecucion() {
		return this.horaEjecucion;
	}

	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}

	public String getEstadoInicialCita() {
		return this.estadoInicialCita;
	}

	public void setEstadoInicialCita(String estadoInicialCita) {
		this.estadoInicialCita = estadoInicialCita;
	}

	public int getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public Set getLogProcAutoServCitas() {
		return this.logProcAutoServCitas;
	}

	public void setLogProcAutoServCitas(Set logProcAutoServCitas) {
		this.logProcAutoServCitas = logProcAutoServCitas;
	}

}
