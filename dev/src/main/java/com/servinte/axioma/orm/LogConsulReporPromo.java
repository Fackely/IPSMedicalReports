package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * LogConsulReporPromo generated by hbm2java
 */
public class LogConsulReporPromo implements java.io.Serializable {

	private LogConsulReporPromoId id;
	private Usuarios usuarios;

	public LogConsulReporPromo() {
	}

	public LogConsulReporPromo(LogConsulReporPromoId id, Usuarios usuarios) {
		this.id = id;
		this.usuarios = usuarios;
	}

	public LogConsulReporPromoId getId() {
		return this.id;
	}

	public void setId(LogConsulReporPromoId id) {
		this.id = id;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

}
