package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * IndGenerales generated by hbm2java
 */
public class IndGenerales implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private char reportarIndCero;
	private int tensionArterialDiastolica;
	private int maximoNormalTad;
	private int tensionArterialSistolica;
	private int maximoNormalTas;
	private Date fechaModifica;
	private String horaModifica;

	public IndGenerales() {
	}

	public IndGenerales(long codigo, Usuarios usuarios,
			Instituciones instituciones, char reportarIndCero,
			int tensionArterialDiastolica, int maximoNormalTad,
			int tensionArterialSistolica, int maximoNormalTas,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.reportarIndCero = reportarIndCero;
		this.tensionArterialDiastolica = tensionArterialDiastolica;
		this.maximoNormalTad = maximoNormalTad;
		this.tensionArterialSistolica = tensionArterialSistolica;
		this.maximoNormalTas = maximoNormalTas;
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

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public char getReportarIndCero() {
		return this.reportarIndCero;
	}

	public void setReportarIndCero(char reportarIndCero) {
		this.reportarIndCero = reportarIndCero;
	}

	public int getTensionArterialDiastolica() {
		return this.tensionArterialDiastolica;
	}

	public void setTensionArterialDiastolica(int tensionArterialDiastolica) {
		this.tensionArterialDiastolica = tensionArterialDiastolica;
	}

	public int getMaximoNormalTad() {
		return this.maximoNormalTad;
	}

	public void setMaximoNormalTad(int maximoNormalTad) {
		this.maximoNormalTad = maximoNormalTad;
	}

	public int getTensionArterialSistolica() {
		return this.tensionArterialSistolica;
	}

	public void setTensionArterialSistolica(int tensionArterialSistolica) {
		this.tensionArterialSistolica = tensionArterialSistolica;
	}

	public int getMaximoNormalTas() {
		return this.maximoNormalTas;
	}

	public void setMaximoNormalTas(int maximoNormalTas) {
		this.maximoNormalTas = maximoNormalTas;
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
