package com.servinte.axioma.orm;

// Generated Mar 17, 2010 9:47:25 AM by Hibernate Tools 3.2.4.GA

/**
 * PresupuestoIntervencion generated by hbm2java
 */
public class PresupuestoIntervencion implements java.io.Serializable {

	private PresupuestoIntervencionId id;
	private PresupuestoPaciente presupuestoPaciente;

	public PresupuestoIntervencion() {
	}

	public PresupuestoIntervencion(PresupuestoIntervencionId id,
			PresupuestoPaciente presupuestoPaciente) {
		this.id = id;
		this.presupuestoPaciente = presupuestoPaciente;
	}

	public PresupuestoIntervencionId getId() {
		return this.id;
	}

	public void setId(PresupuestoIntervencionId id) {
		this.id = id;
	}

	public PresupuestoPaciente getPresupuestoPaciente() {
		return this.presupuestoPaciente;
	}

	public void setPresupuestoPaciente(PresupuestoPaciente presupuestoPaciente) {
		this.presupuestoPaciente = presupuestoPaciente;
	}

}
