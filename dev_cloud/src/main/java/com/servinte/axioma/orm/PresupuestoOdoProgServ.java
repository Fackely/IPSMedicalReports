package com.servinte.axioma.orm;

// Generated Feb 1, 2011 8:58:41 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PresupuestoOdoProgServ generated by hbm2java
 */
public class PresupuestoOdoProgServ implements java.io.Serializable {

	private long codigoPk;
	private PresupuestoOdontologico presupuestoOdontologico;
	private Usuarios usuarios;
	private Servicios servicios;
	private Programas programas;
	private InclusionesPresupuesto inclusionesPresupuesto;
	private Date fechaModifica;
	private String horaModifica;
	private int cantidad;
	private String migrado;
	private Set presupuestoPiezases = new HashSet(0);
	private Set presupuestoOdoConvenios = new HashSet(0);

	public PresupuestoOdoProgServ() {
	}

	public PresupuestoOdoProgServ(long codigoPk,
			PresupuestoOdontologico presupuestoOdontologico, Usuarios usuarios,
			Date fechaModifica, String horaModifica, int cantidad) {
		this.codigoPk = codigoPk;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.usuarios = usuarios;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.cantidad = cantidad;
	}

	public PresupuestoOdoProgServ(long codigoPk,
			PresupuestoOdontologico presupuestoOdontologico, Usuarios usuarios,
			Servicios servicios, Programas programas,
			InclusionesPresupuesto inclusionesPresupuesto, Date fechaModifica,
			String horaModifica, int cantidad, String migrado,
			Set presupuestoPiezases, Set presupuestoOdoConvenios) {
		this.codigoPk = codigoPk;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.usuarios = usuarios;
		this.servicios = servicios;
		this.programas = programas;
		this.inclusionesPresupuesto = inclusionesPresupuesto;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.cantidad = cantidad;
		this.migrado = migrado;
		this.presupuestoPiezases = presupuestoPiezases;
		this.presupuestoOdoConvenios = presupuestoOdoConvenios;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public PresupuestoOdontologico getPresupuestoOdontologico() {
		return this.presupuestoOdontologico;
	}

	public void setPresupuestoOdontologico(
			PresupuestoOdontologico presupuestoOdontologico) {
		this.presupuestoOdontologico = presupuestoOdontologico;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public Programas getProgramas() {
		return this.programas;
	}

	public void setProgramas(Programas programas) {
		this.programas = programas;
	}

	public InclusionesPresupuesto getInclusionesPresupuesto() {
		return this.inclusionesPresupuesto;
	}

	public void setInclusionesPresupuesto(
			InclusionesPresupuesto inclusionesPresupuesto) {
		this.inclusionesPresupuesto = inclusionesPresupuesto;
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

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getMigrado() {
		return this.migrado;
	}

	public void setMigrado(String migrado) {
		this.migrado = migrado;
	}

	public Set getPresupuestoPiezases() {
		return this.presupuestoPiezases;
	}

	public void setPresupuestoPiezases(Set presupuestoPiezases) {
		this.presupuestoPiezases = presupuestoPiezases;
	}

	public Set getPresupuestoOdoConvenios() {
		return this.presupuestoOdoConvenios;
	}

	public void setPresupuestoOdoConvenios(Set presupuestoOdoConvenios) {
		this.presupuestoOdoConvenios = presupuestoOdoConvenios;
	}

}
