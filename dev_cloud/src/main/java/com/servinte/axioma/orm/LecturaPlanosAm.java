package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * LecturaPlanosAm generated by hbm2java
 */
public class LecturaPlanosAm implements java.io.Serializable {

	private LecturaPlanosAmId id;
	private CentroAtencion centroAtencion;
	private Usuarios usuarios;

	public LecturaPlanosAm() {
	}

	public LecturaPlanosAm(LecturaPlanosAmId id, CentroAtencion centroAtencion,
			Usuarios usuarios) {
		this.id = id;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
	}

	public LecturaPlanosAmId getId() {
		return this.id;
	}

	public void setId(LecturaPlanosAmId id) {
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
