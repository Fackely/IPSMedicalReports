package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * SerIncluexcluCc generated by hbm2java
 */
public class SerIncluexcluCc implements java.io.Serializable {

	private long codigo;
	private Servicios servicios;
	private Usuarios usuarios;
	private IncluExcluCc incluExcluCc;
	private char incluye;
	private Long cantidad;
	private Date fechaModifica;
	private String horaModifica;

	public SerIncluexcluCc() {
	}

	public SerIncluexcluCc(long codigo, Servicios servicios, Usuarios usuarios,
			IncluExcluCc incluExcluCc, char incluye, Date fechaModifica,
			String horaModifica) {
		this.codigo = codigo;
		this.servicios = servicios;
		this.usuarios = usuarios;
		this.incluExcluCc = incluExcluCc;
		this.incluye = incluye;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public SerIncluexcluCc(long codigo, Servicios servicios, Usuarios usuarios,
			IncluExcluCc incluExcluCc, char incluye, Long cantidad,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.servicios = servicios;
		this.usuarios = usuarios;
		this.incluExcluCc = incluExcluCc;
		this.incluye = incluye;
		this.cantidad = cantidad;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public IncluExcluCc getIncluExcluCc() {
		return this.incluExcluCc;
	}

	public void setIncluExcluCc(IncluExcluCc incluExcluCc) {
		this.incluExcluCc = incluExcluCc;
	}

	public char getIncluye() {
		return this.incluye;
	}

	public void setIncluye(char incluye) {
		this.incluye = incluye;
	}

	public Long getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
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
