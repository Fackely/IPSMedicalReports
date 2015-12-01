package com.servinte.axioma.orm;

// Generated 13/05/2011 11:32:29 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * LogNivelesContratos generated by hbm2java
 */
public class LogNivelesContratos implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private Long contrato;
	private Integer nivelServicio;
	private Date fechaModifica;
	private String horaModifica;
	private char eliminado;

	public LogNivelesContratos() {
	}

	public LogNivelesContratos(long codigo, Usuarios usuarios,
			Date fechaModifica, String horaModifica, char eliminado) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.eliminado = eliminado;
	}

	public LogNivelesContratos(long codigo, Usuarios usuarios, Long contrato,
			Integer nivelServicio, Date fechaModifica, String horaModifica,
			char eliminado) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.contrato = contrato;
		this.nivelServicio = nivelServicio;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.eliminado = eliminado;
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

	public Long getContrato() {
		return this.contrato;
	}

	public void setContrato(Long contrato) {
		this.contrato = contrato;
	}

	public Integer getNivelServicio() {
		return this.nivelServicio;
	}

	public void setNivelServicio(Integer nivelServicio) {
		this.nivelServicio = nivelServicio;
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

	public char getEliminado() {
		return this.eliminado;
	}

	public void setEliminado(char eliminado) {
		this.eliminado = eliminado;
	}

}
