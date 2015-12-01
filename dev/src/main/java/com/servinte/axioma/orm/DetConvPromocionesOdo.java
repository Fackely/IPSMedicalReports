package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * DetConvPromocionesOdo generated by hbm2java
 */
public class DetConvPromocionesOdo implements java.io.Serializable {

	private int codigoPk;
	private DetPromocionesOdo detPromocionesOdo;
	private Usuarios usuarios;
	private Convenios convenios;
	private String activo;
	private Date fechaInactivacion;
	private String horaInactivacion;
	private String usuarioInactivacion;
	private Date fechaModifica;
	private String horaModifica;

	public DetConvPromocionesOdo() {
	}

	public DetConvPromocionesOdo(int codigoPk,
			DetPromocionesOdo detPromocionesOdo, Usuarios usuarios,
			Convenios convenios, String activo, Date fechaModifica,
			String horaModifica) {
		this.codigoPk = codigoPk;
		this.detPromocionesOdo = detPromocionesOdo;
		this.usuarios = usuarios;
		this.convenios = convenios;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public DetConvPromocionesOdo(int codigoPk,
			DetPromocionesOdo detPromocionesOdo, Usuarios usuarios,
			Convenios convenios, String activo, Date fechaInactivacion,
			String horaInactivacion, String usuarioInactivacion,
			Date fechaModifica, String horaModifica) {
		this.codigoPk = codigoPk;
		this.detPromocionesOdo = detPromocionesOdo;
		this.usuarios = usuarios;
		this.convenios = convenios;
		this.activo = activo;
		this.fechaInactivacion = fechaInactivacion;
		this.horaInactivacion = horaInactivacion;
		this.usuarioInactivacion = usuarioInactivacion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public DetPromocionesOdo getDetPromocionesOdo() {
		return this.detPromocionesOdo;
	}

	public void setDetPromocionesOdo(DetPromocionesOdo detPromocionesOdo) {
		this.detPromocionesOdo = detPromocionesOdo;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public Date getFechaInactivacion() {
		return this.fechaInactivacion;
	}

	public void setFechaInactivacion(Date fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}

	public String getHoraInactivacion() {
		return this.horaInactivacion;
	}

	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}

	public String getUsuarioInactivacion() {
		return this.usuarioInactivacion;
	}

	public void setUsuarioInactivacion(String usuarioInactivacion) {
		this.usuarioInactivacion = usuarioInactivacion;
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

}
