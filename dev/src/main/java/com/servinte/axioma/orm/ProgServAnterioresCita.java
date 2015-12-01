package com.servinte.axioma.orm;

// Generated Oct 26, 2010 3:40:55 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;

/**
 * ProgServAnterioresCita generated by hbm2java
 */
public class ProgServAnterioresCita implements java.io.Serializable {

	private int codigoPk;
	private MotivosCambiosServicios motivosCambiosServicios;
	private Servicios servicios;
	private Programas programas;
	private ProgramasHallazgoPieza programasHallazgoPieza;
	private SolicitudCambioServicio solicitudCambioServicio;
	private String cambio;
	private String observaciones;
	private BigDecimal valorUnitario;

	public ProgServAnterioresCita() {
	}

	public ProgServAnterioresCita(int codigoPk, Servicios servicios,
			SolicitudCambioServicio solicitudCambioServicio, String cambio) {
		this.codigoPk = codigoPk;
		this.servicios = servicios;
		this.solicitudCambioServicio = solicitudCambioServicio;
		this.cambio = cambio;
	}

	public ProgServAnterioresCita(int codigoPk,
			MotivosCambiosServicios motivosCambiosServicios,
			Servicios servicios, Programas programas,
			ProgramasHallazgoPieza programasHallazgoPieza,
			SolicitudCambioServicio solicitudCambioServicio, String cambio,
			String observaciones, BigDecimal valorUnitario) {
		this.codigoPk = codigoPk;
		this.motivosCambiosServicios = motivosCambiosServicios;
		this.servicios = servicios;
		this.programas = programas;
		this.programasHallazgoPieza = programasHallazgoPieza;
		this.solicitudCambioServicio = solicitudCambioServicio;
		this.cambio = cambio;
		this.observaciones = observaciones;
		this.valorUnitario = valorUnitario;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public MotivosCambiosServicios getMotivosCambiosServicios() {
		return this.motivosCambiosServicios;
	}

	public void setMotivosCambiosServicios(
			MotivosCambiosServicios motivosCambiosServicios) {
		this.motivosCambiosServicios = motivosCambiosServicios;
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

	public ProgramasHallazgoPieza getProgramasHallazgoPieza() {
		return this.programasHallazgoPieza;
	}

	public void setProgramasHallazgoPieza(
			ProgramasHallazgoPieza programasHallazgoPieza) {
		this.programasHallazgoPieza = programasHallazgoPieza;
	}

	public SolicitudCambioServicio getSolicitudCambioServicio() {
		return this.solicitudCambioServicio;
	}

	public void setSolicitudCambioServicio(
			SolicitudCambioServicio solicitudCambioServicio) {
		this.solicitudCambioServicio = solicitudCambioServicio;
	}

	public String getCambio() {
		return this.cambio;
	}

	public void setCambio(String cambio) {
		this.cambio = cambio;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public BigDecimal getValorUnitario() {
		return this.valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}
