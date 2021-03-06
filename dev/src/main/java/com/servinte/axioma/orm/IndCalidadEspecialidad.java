package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * IndCalidadEspecialidad generated by hbm2java
 */
public class IndCalidadEspecialidad implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private Especialidades especialidades;
	private Instituciones instituciones;
	private IndCalidadCodigos indCalidadCodigos;
	private Date fechaModifica;
	private String horaModifica;

	public IndCalidadEspecialidad() {
	}

	public IndCalidadEspecialidad(long codigo, Usuarios usuarios,
			Especialidades especialidades, Instituciones instituciones,
			IndCalidadCodigos indCalidadCodigos, Date fechaModifica,
			String horaModifica) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.especialidades = especialidades;
		this.instituciones = instituciones;
		this.indCalidadCodigos = indCalidadCodigos;
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

	public Especialidades getEspecialidades() {
		return this.especialidades;
	}

	public void setEspecialidades(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public IndCalidadCodigos getIndCalidadCodigos() {
		return this.indCalidadCodigos;
	}

	public void setIndCalidadCodigos(IndCalidadCodigos indCalidadCodigos) {
		this.indCalidadCodigos = indCalidadCodigos;
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
