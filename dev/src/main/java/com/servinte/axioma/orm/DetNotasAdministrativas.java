package com.servinte.axioma.orm;

// Generated Aug 5, 2010 9:53:29 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * DetNotasAdministrativas generated by hbm2java
 */
public class DetNotasAdministrativas implements java.io.Serializable {

	private int codigoPk;
	private Usuarios usuarios;
	private NotasAdministrativas notasAdministrativas;
	private Date fechaModifica;
	private String horaModifica;
	private String descripcionNota;

	public DetNotasAdministrativas() {
	}

	public DetNotasAdministrativas(int codigoPk, Usuarios usuarios,
			NotasAdministrativas notasAdministrativas, Date fechaModifica,
			String horaModifica, String descripcionNota) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.notasAdministrativas = notasAdministrativas;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.descripcionNota = descripcionNota;
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

	public NotasAdministrativas getNotasAdministrativas() {
		return this.notasAdministrativas;
	}

	public void setNotasAdministrativas(
			NotasAdministrativas notasAdministrativas) {
		this.notasAdministrativas = notasAdministrativas;
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

	public String getDescripcionNota() {
		return this.descripcionNota;
	}

	public void setDescripcionNota(String descripcionNota) {
		this.descripcionNota = descripcionNota;
	}

}
