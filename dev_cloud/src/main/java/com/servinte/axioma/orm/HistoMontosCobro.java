package com.servinte.axioma.orm;

// Generated Sep 3, 2010 2:49:56 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * HistoMontosCobro generated by hbm2java
 */
public class HistoMontosCobro implements java.io.Serializable {

	private int codigoPk;
	private MontosCobro montosCobro;
	private Usuarios usuarios;
	private Date fechaRegistro;
	private String horaRegistro;
	private String accionRealizada;
	private Set histoDetalleMontos = new HashSet(0);

	public HistoMontosCobro() {
	}

	public HistoMontosCobro(int codigoPk, MontosCobro montosCobro,
			Usuarios usuarios, Date fechaRegistro, String horaRegistro,
			String accionRealizada) {
		this.codigoPk = codigoPk;
		this.montosCobro = montosCobro;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.accionRealizada = accionRealizada;
	}

	public HistoMontosCobro(int codigoPk, MontosCobro montosCobro,
			Usuarios usuarios, Date fechaRegistro, String horaRegistro,
			String accionRealizada, Set histoDetalleMontos) {
		this.codigoPk = codigoPk;
		this.montosCobro = montosCobro;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.accionRealizada = accionRealizada;
		this.histoDetalleMontos = histoDetalleMontos;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public MontosCobro getMontosCobro() {
		return this.montosCobro;
	}

	public void setMontosCobro(MontosCobro montosCobro) {
		this.montosCobro = montosCobro;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getHoraRegistro() {
		return this.horaRegistro;
	}

	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	public String getAccionRealizada() {
		return this.accionRealizada;
	}

	public void setAccionRealizada(String accionRealizada) {
		this.accionRealizada = accionRealizada;
	}

	public Set getHistoDetalleMontos() {
		return this.histoDetalleMontos;
	}

	public void setHistoDetalleMontos(Set histoDetalleMontos) {
		this.histoDetalleMontos = histoDetalleMontos;
	}

}
