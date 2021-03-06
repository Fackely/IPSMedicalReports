package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * AprobAnulFacturasVarias generated by hbm2java
 */
public class AprobAnulFacturasVarias implements java.io.Serializable {

	private int codigo;
	private Usuarios usuariosByUsuarioAnula;
	private Usuarios usuariosByUsuarioAprueba;
	private Usuarios usuariosByUsuarioModifica;
	private FacturasVarias facturasVarias;
	private Instituciones instituciones;
	private String motivoAnulacion;
	private Date fechaAnulacion;
	private Date fechaAprobacion;
	private Date fechaModifica;
	private String horaModifica;

	public AprobAnulFacturasVarias() {
	}

	public AprobAnulFacturasVarias(int codigo,
			Usuarios usuariosByUsuarioModifica, FacturasVarias facturasVarias,
			Instituciones instituciones, Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.facturasVarias = facturasVarias;
		this.instituciones = instituciones;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public AprobAnulFacturasVarias(int codigo, Usuarios usuariosByUsuarioAnula,
			Usuarios usuariosByUsuarioAprueba,
			Usuarios usuariosByUsuarioModifica, FacturasVarias facturasVarias,
			Instituciones instituciones, String motivoAnulacion,
			Date fechaAnulacion, Date fechaAprobacion, Date fechaModifica,
			String horaModifica) {
		this.codigo = codigo;
		this.usuariosByUsuarioAnula = usuariosByUsuarioAnula;
		this.usuariosByUsuarioAprueba = usuariosByUsuarioAprueba;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.facturasVarias = facturasVarias;
		this.instituciones = instituciones;
		this.motivoAnulacion = motivoAnulacion;
		this.fechaAnulacion = fechaAnulacion;
		this.fechaAprobacion = fechaAprobacion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Usuarios getUsuariosByUsuarioAnula() {
		return this.usuariosByUsuarioAnula;
	}

	public void setUsuariosByUsuarioAnula(Usuarios usuariosByUsuarioAnula) {
		this.usuariosByUsuarioAnula = usuariosByUsuarioAnula;
	}

	public Usuarios getUsuariosByUsuarioAprueba() {
		return this.usuariosByUsuarioAprueba;
	}

	public void setUsuariosByUsuarioAprueba(Usuarios usuariosByUsuarioAprueba) {
		this.usuariosByUsuarioAprueba = usuariosByUsuarioAprueba;
	}

	public Usuarios getUsuariosByUsuarioModifica() {
		return this.usuariosByUsuarioModifica;
	}

	public void setUsuariosByUsuarioModifica(Usuarios usuariosByUsuarioModifica) {
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
	}

	public FacturasVarias getFacturasVarias() {
		return this.facturasVarias;
	}

	public void setFacturasVarias(FacturasVarias facturasVarias) {
		this.facturasVarias = facturasVarias;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public String getMotivoAnulacion() {
		return this.motivoAnulacion;
	}

	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	public Date getFechaAnulacion() {
		return this.fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public Date getFechaAprobacion() {
		return this.fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
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
