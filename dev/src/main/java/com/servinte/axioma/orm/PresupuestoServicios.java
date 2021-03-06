package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:51 AM by Hibernate Tools 3.2.4.GA

/**
 * PresupuestoServicios generated by hbm2java
 */
public class PresupuestoServicios implements java.io.Serializable {

	private PresupuestoServiciosId id;
	private PresupuestoPaciente presupuestoPaciente;
	private Servicios servicios;
	private int cantidad;
	private double valorUnitario;
	private Integer esquemaTarifario;

	public PresupuestoServicios() {
	}

	public PresupuestoServicios(PresupuestoServiciosId id,
			PresupuestoPaciente presupuestoPaciente, Servicios servicios,
			int cantidad, double valorUnitario) {
		this.id = id;
		this.presupuestoPaciente = presupuestoPaciente;
		this.servicios = servicios;
		this.cantidad = cantidad;
		this.valorUnitario = valorUnitario;
	}

	public PresupuestoServicios(PresupuestoServiciosId id,
			PresupuestoPaciente presupuestoPaciente, Servicios servicios,
			int cantidad, double valorUnitario, Integer esquemaTarifario) {
		this.id = id;
		this.presupuestoPaciente = presupuestoPaciente;
		this.servicios = servicios;
		this.cantidad = cantidad;
		this.valorUnitario = valorUnitario;
		this.esquemaTarifario = esquemaTarifario;
	}

	public PresupuestoServiciosId getId() {
		return this.id;
	}

	public void setId(PresupuestoServiciosId id) {
		this.id = id;
	}

	public PresupuestoPaciente getPresupuestoPaciente() {
		return this.presupuestoPaciente;
	}

	public void setPresupuestoPaciente(PresupuestoPaciente presupuestoPaciente) {
		this.presupuestoPaciente = presupuestoPaciente;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getValorUnitario() {
		return this.valorUnitario;
	}

	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Integer getEsquemaTarifario() {
		return this.esquemaTarifario;
	}

	public void setEsquemaTarifario(Integer esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

}
