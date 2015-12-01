package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * DetEntidadesSubcontratadas generated by hbm2java
 */
public class DetEntidadesSubcontratadas implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private EntidadesSubcontratadas entidadesSubcontratadas;
	private Date fechaInicial;
	private Date fechaFinal;
	private int esqTarServ;
	private int esqTarInv;
	private Date fechaModifica;
	private String horaModifica;

	public DetEntidadesSubcontratadas() {
	}

	public DetEntidadesSubcontratadas(long codigo, Usuarios usuarios,
			EntidadesSubcontratadas entidadesSubcontratadas, Date fechaInicial,
			Date fechaFinal, int esqTarServ, int esqTarInv, Date fechaModifica,
			String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.entidadesSubcontratadas = entidadesSubcontratadas;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
		this.esqTarServ = esqTarServ;
		this.esqTarInv = esqTarInv;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public EntidadesSubcontratadas getEntidadesSubcontratadas() {
		return this.entidadesSubcontratadas;
	}

	public void setEntidadesSubcontratadas(
			EntidadesSubcontratadas entidadesSubcontratadas) {
		this.entidadesSubcontratadas = entidadesSubcontratadas;
	}

	public Date getFechaInicial() {
		return this.fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return this.fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public int getEsqTarServ() {
		return this.esqTarServ;
	}

	public void setEsqTarServ(int esqTarServ) {
		this.esqTarServ = esqTarServ;
	}

	public int getEsqTarInv() {
		return this.esqTarInv;
	}

	public void setEsqTarInv(int esqTarInv) {
		this.esqTarInv = esqTarInv;
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
