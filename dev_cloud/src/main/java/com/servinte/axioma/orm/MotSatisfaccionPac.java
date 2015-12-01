package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * MotSatisfaccionPac generated by hbm2java
 */
public class MotSatisfaccionPac implements java.io.Serializable {

	private long codigopk;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private String codigo;
	private String descripcion;
	private String tipo;
	private Date fechaModifica;
	private String horaModifica;
	private Set encuestaCalidads = new HashSet(0);

	public MotSatisfaccionPac() {
	}

	public MotSatisfaccionPac(long codigopk, Usuarios usuarios,
			Instituciones instituciones, String codigo, String descripcion,
			String tipo, Date fechaModifica, String horaModifica) {
		this.codigopk = codigopk;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public MotSatisfaccionPac(long codigopk, Usuarios usuarios,
			Instituciones instituciones, String codigo, String descripcion,
			String tipo, Date fechaModifica, String horaModifica,
			Set encuestaCalidads) {
		this.codigopk = codigopk;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.encuestaCalidads = encuestaCalidads;
	}

	public long getCodigopk() {
		return this.codigopk;
	}

	public void setCodigopk(long codigopk) {
		this.codigopk = codigopk;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public Set getEncuestaCalidads() {
		return this.encuestaCalidads;
	}

	public void setEncuestaCalidads(Set encuestaCalidads) {
		this.encuestaCalidads = encuestaCalidads;
	}

}
