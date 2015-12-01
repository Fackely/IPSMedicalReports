package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * TiposRetencionGrupoSer generated by hbm2java
 */
public class TiposRetencionGrupoSer implements java.io.Serializable {

	private int consecutivo;
	private GruposServicios gruposServicios;
	private Usuarios usuariosByUsuarioModifica;
	private TiposRetencion tiposRetencion;
	private Usuarios usuariosByUsuarioInactivacion;
	private Date fechaModifica;
	private String horaModifica;
	private String activo;
	private Date fechaInactivacion;
	private String horaInactivacion;

	public TiposRetencionGrupoSer() {
	}

	public TiposRetencionGrupoSer(int consecutivo,
			GruposServicios gruposServicios,
			Usuarios usuariosByUsuarioModifica, TiposRetencion tiposRetencion,
			Date fechaModifica, String horaModifica, String activo) {
		this.consecutivo = consecutivo;
		this.gruposServicios = gruposServicios;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.tiposRetencion = tiposRetencion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.activo = activo;
	}

	public TiposRetencionGrupoSer(int consecutivo,
			GruposServicios gruposServicios,
			Usuarios usuariosByUsuarioModifica, TiposRetencion tiposRetencion,
			Usuarios usuariosByUsuarioInactivacion, Date fechaModifica,
			String horaModifica, String activo, Date fechaInactivacion,
			String horaInactivacion) {
		this.consecutivo = consecutivo;
		this.gruposServicios = gruposServicios;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.tiposRetencion = tiposRetencion;
		this.usuariosByUsuarioInactivacion = usuariosByUsuarioInactivacion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.activo = activo;
		this.fechaInactivacion = fechaInactivacion;
		this.horaInactivacion = horaInactivacion;
	}

	public int getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public GruposServicios getGruposServicios() {
		return this.gruposServicios;
	}

	public void setGruposServicios(GruposServicios gruposServicios) {
		this.gruposServicios = gruposServicios;
	}

	public Usuarios getUsuariosByUsuarioModifica() {
		return this.usuariosByUsuarioModifica;
	}

	public void setUsuariosByUsuarioModifica(Usuarios usuariosByUsuarioModifica) {
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
	}

	public TiposRetencion getTiposRetencion() {
		return this.tiposRetencion;
	}

	public void setTiposRetencion(TiposRetencion tiposRetencion) {
		this.tiposRetencion = tiposRetencion;
	}

	public Usuarios getUsuariosByUsuarioInactivacion() {
		return this.usuariosByUsuarioInactivacion;
	}

	public void setUsuariosByUsuarioInactivacion(
			Usuarios usuariosByUsuarioInactivacion) {
		this.usuariosByUsuarioInactivacion = usuariosByUsuarioInactivacion;
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

}
