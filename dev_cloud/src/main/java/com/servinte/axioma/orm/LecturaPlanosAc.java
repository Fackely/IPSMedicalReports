package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * LecturaPlanosAc generated by hbm2java
 */
public class LecturaPlanosAc implements java.io.Serializable {

	private LecturaPlanosAcId id;
	private CentroAtencion centroAtencion;
	private Usuarios usuarios;

	public LecturaPlanosAc() {
	}

	public LecturaPlanosAc(LecturaPlanosAcId id, CentroAtencion centroAtencion,
			Usuarios usuarios) {
		this.id = id;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
	}

	public LecturaPlanosAcId getId() {
		return this.id;
	}

	public void setId(LecturaPlanosAcId id) {
		this.id = id;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

}
