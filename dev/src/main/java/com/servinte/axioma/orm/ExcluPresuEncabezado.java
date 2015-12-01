package com.servinte.axioma.orm;

// Generated Jan 12, 2011 5:49:21 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ExcluPresuEncabezado generated by hbm2java
 */
public class ExcluPresuEncabezado implements java.io.Serializable {

	private long codigoPk;
	private PresupuestoOdontologico presupuestoOdontologico;
	private Usuarios usuarios;
	private OtrosSi otrosSi;
	private long consecutivo;
	private Date fecha;
	private String hora;
	private Set exclusionesPresupuestos = new HashSet(0);

	public ExcluPresuEncabezado() {
	}

	public ExcluPresuEncabezado(long codigoPk,
			PresupuestoOdontologico presupuestoOdontologico, Usuarios usuarios,
			long consecutivo, Date fecha, String hora) {
		this.codigoPk = codigoPk;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.usuarios = usuarios;
		this.consecutivo = consecutivo;
		this.fecha = fecha;
		this.hora = hora;
	}

	public ExcluPresuEncabezado(long codigoPk,
			PresupuestoOdontologico presupuestoOdontologico, Usuarios usuarios,
			OtrosSi otrosSi, long consecutivo, Date fecha, String hora,
			Set exclusionesPresupuestos) {
		this.codigoPk = codigoPk;
		this.presupuestoOdontologico = presupuestoOdontologico;
		this.usuarios = usuarios;
		this.otrosSi = otrosSi;
		this.consecutivo = consecutivo;
		this.fecha = fecha;
		this.hora = hora;
		this.exclusionesPresupuestos = exclusionesPresupuestos;
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

	public OtrosSi getOtrosSi() {
		return this.otrosSi;
	}

	public void setOtrosSi(OtrosSi otrosSi) {
		this.otrosSi = otrosSi;
	}

	public long getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Set getExclusionesPresupuestos() {
		return this.exclusionesPresupuestos;
	}

	public void setExclusionesPresupuestos(Set exclusionesPresupuestos) {
		this.exclusionesPresupuestos = exclusionesPresupuestos;
	}

}
