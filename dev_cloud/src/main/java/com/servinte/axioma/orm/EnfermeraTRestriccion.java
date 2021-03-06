package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * EnfermeraTRestriccion generated by hbm2java
 */
public class EnfermeraTRestriccion implements java.io.Serializable {

	private int codigo;
	private Personas personas;
	private Turno turno;
	private Date fechaInicio;
	private Date fechaFin;
	private int valor;
	private boolean todosDias;

	public EnfermeraTRestriccion() {
	}

	public EnfermeraTRestriccion(int codigo, Personas personas, Turno turno,
			int valor, boolean todosDias) {
		this.codigo = codigo;
		this.personas = personas;
		this.turno = turno;
		this.valor = valor;
		this.todosDias = todosDias;
	}

	public EnfermeraTRestriccion(int codigo, Personas personas, Turno turno,
			Date fechaInicio, Date fechaFin, int valor, boolean todosDias) {
		this.codigo = codigo;
		this.personas = personas;
		this.turno = turno;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.valor = valor;
		this.todosDias = todosDias;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Personas getPersonas() {
		return this.personas;
	}

	public void setPersonas(Personas personas) {
		this.personas = personas;
	}

	public Turno getTurno() {
		return this.turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getValor() {
		return this.valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public boolean isTodosDias() {
		return this.todosDias;
	}

	public void setTodosDias(boolean todosDias) {
		this.todosDias = todosDias;
	}

}
