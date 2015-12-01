package com.servinte.axioma.orm;

// Generated 21/07/2011 03:06:42 PM by Hibernate Tools 3.4.0.CR1

/**
 * EvolDiagnosticos generated by hbm2java
 */
public class EvolDiagnosticos implements java.io.Serializable {

	private EvolDiagnosticosId id;
	private Diagnosticos diagnosticos;
	private Evoluciones evoluciones;
	private int numero;

	public EvolDiagnosticos() {
	}

	public EvolDiagnosticos(EvolDiagnosticosId id, Diagnosticos diagnosticos,
			Evoluciones evoluciones, int numero) {
		this.id = id;
		this.diagnosticos = diagnosticos;
		this.evoluciones = evoluciones;
		this.numero = numero;
	}

	public EvolDiagnosticosId getId() {
		return this.id;
	}

	public void setId(EvolDiagnosticosId id) {
		this.id = id;
	}

	public Diagnosticos getDiagnosticos() {
		return this.diagnosticos;
	}

	public void setDiagnosticos(Diagnosticos diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	public Evoluciones getEvoluciones() {
		return this.evoluciones;
	}

	public void setEvoluciones(Evoluciones evoluciones) {
		this.evoluciones = evoluciones;
	}

	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

}
