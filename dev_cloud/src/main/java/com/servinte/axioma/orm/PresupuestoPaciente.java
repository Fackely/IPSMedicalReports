package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:51 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PresupuestoPaciente generated by hbm2java
 */
public class PresupuestoPaciente implements java.io.Serializable {

	private int consecutivo;
	private Ingresos ingresos;
	private CentroAtencion centroAtencion;
	private Pacientes pacientes;
	private Diagnosticos diagnosticos;
	private Usuarios usuarios;
	private Medicos medicos;
	private Convenios convenios;
	private Date fechaPresupuesto;
	private Date horaPresupuesto;
	private int contrato;
	private String paquete;
	private Set presupuestoArticuloses = new HashSet(0);
	private Set presupuestoServicioses = new HashSet(0);
	private Set servicioses = new HashSet(0);

	public PresupuestoPaciente() {
	}

	public PresupuestoPaciente(int consecutivo, CentroAtencion centroAtencion,
			Pacientes pacientes, Diagnosticos diagnosticos, Medicos medicos,
			Convenios convenios, Date fechaPresupuesto, Date horaPresupuesto,
			int contrato, String paquete) {
		this.consecutivo = consecutivo;
		this.centroAtencion = centroAtencion;
		this.pacientes = pacientes;
		this.diagnosticos = diagnosticos;
		this.medicos = medicos;
		this.convenios = convenios;
		this.fechaPresupuesto = fechaPresupuesto;
		this.horaPresupuesto = horaPresupuesto;
		this.contrato = contrato;
		this.paquete = paquete;
	}

	public PresupuestoPaciente(int consecutivo, Ingresos ingresos,
			CentroAtencion centroAtencion, Pacientes pacientes,
			Diagnosticos diagnosticos, Usuarios usuarios, Medicos medicos,
			Convenios convenios, Date fechaPresupuesto, Date horaPresupuesto,
			int contrato, String paquete, Set presupuestoArticuloses,
			Set presupuestoServicioses, Set servicioses) {
		this.consecutivo = consecutivo;
		this.ingresos = ingresos;
		this.centroAtencion = centroAtencion;
		this.pacientes = pacientes;
		this.diagnosticos = diagnosticos;
		this.usuarios = usuarios;
		this.medicos = medicos;
		this.convenios = convenios;
		this.fechaPresupuesto = fechaPresupuesto;
		this.horaPresupuesto = horaPresupuesto;
		this.contrato = contrato;
		this.paquete = paquete;
		this.presupuestoArticuloses = presupuestoArticuloses;
		this.presupuestoServicioses = presupuestoServicioses;
		this.servicioses = servicioses;
	}

	public int getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Pacientes getPacientes() {
		return this.pacientes;
	}

	public void setPacientes(Pacientes pacientes) {
		this.pacientes = pacientes;
	}

	public Diagnosticos getDiagnosticos() {
		return this.diagnosticos;
	}

	public void setDiagnosticos(Diagnosticos diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Medicos getMedicos() {
		return this.medicos;
	}

	public void setMedicos(Medicos medicos) {
		this.medicos = medicos;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Date getFechaPresupuesto() {
		return this.fechaPresupuesto;
	}

	public void setFechaPresupuesto(Date fechaPresupuesto) {
		this.fechaPresupuesto = fechaPresupuesto;
	}

	public Date getHoraPresupuesto() {
		return this.horaPresupuesto;
	}

	public void setHoraPresupuesto(Date horaPresupuesto) {
		this.horaPresupuesto = horaPresupuesto;
	}

	public int getContrato() {
		return this.contrato;
	}

	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	public String getPaquete() {
		return this.paquete;
	}

	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}

	public Set getPresupuestoArticuloses() {
		return this.presupuestoArticuloses;
	}

	public void setPresupuestoArticuloses(Set presupuestoArticuloses) {
		this.presupuestoArticuloses = presupuestoArticuloses;
	}

	public Set getPresupuestoServicioses() {
		return this.presupuestoServicioses;
	}

	public void setPresupuestoServicioses(Set presupuestoServicioses) {
		this.presupuestoServicioses = presupuestoServicioses;
	}

	public Set getServicioses() {
		return this.servicioses;
	}

	public void setServicioses(Set servicioses) {
		this.servicioses = servicioses;
	}

}
