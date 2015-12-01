package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * RequisitosPacConvenio generated by hbm2java
 */
public class RequisitosPacConvenio implements java.io.Serializable {

	private RequisitosPacConvenioId id;
	private RequisitosPaciente requisitosPaciente;
	private ViasIngreso viasIngreso;
	private Convenios convenios;

	public RequisitosPacConvenio() {
	}

	public RequisitosPacConvenio(RequisitosPacConvenioId id,
			RequisitosPaciente requisitosPaciente, ViasIngreso viasIngreso,
			Convenios convenios) {
		this.id = id;
		this.requisitosPaciente = requisitosPaciente;
		this.viasIngreso = viasIngreso;
		this.convenios = convenios;
	}

	public RequisitosPacConvenioId getId() {
		return this.id;
	}

	public void setId(RequisitosPacConvenioId id) {
		this.id = id;
	}

	public RequisitosPaciente getRequisitosPaciente() {
		return this.requisitosPaciente;
	}

	public void setRequisitosPaciente(RequisitosPaciente requisitosPaciente) {
		this.requisitosPaciente = requisitosPaciente;
	}

	public ViasIngreso getViasIngreso() {
		return this.viasIngreso;
	}

	public void setViasIngreso(ViasIngreso viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

}
