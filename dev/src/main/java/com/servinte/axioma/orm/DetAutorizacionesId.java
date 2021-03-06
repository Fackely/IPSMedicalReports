package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * DetAutorizacionesId generated by hbm2java
 */
public class DetAutorizacionesId implements java.io.Serializable {

	private long codigo;
	private long autorizacion;
	private Integer numeroSolicitud;
	private Long ordenAmbulatoria;
	private Long detCargo;
	private Integer servicio;
	private Integer articulo;
	private Integer cantidad;
	private String estado;
	private String activo;
	private String justificacionSolicitud;
	private Date fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private Integer tipoAsocio;
	private Integer servicioCx;
	private Integer cantidadAutorizacion;

	public DetAutorizacionesId() {
	}

	public DetAutorizacionesId(long codigo, long autorizacion, String activo,
			Date fechaModifica, String horaModifica, String usuarioModifica) {
		this.codigo = codigo;
		this.autorizacion = autorizacion;
		this.activo = activo;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.usuarioModifica = usuarioModifica;
	}

	public DetAutorizacionesId(long codigo, long autorizacion,
			Integer numeroSolicitud, Long ordenAmbulatoria, Long detCargo,
			Integer servicio, Integer articulo, Integer cantidad,
			String estado, String activo, String justificacionSolicitud,
			Date fechaModifica, String horaModifica, String usuarioModifica,
			Integer tipoAsocio, Integer servicioCx, Integer cantidadAutorizacion) {
		this.codigo = codigo;
		this.autorizacion = autorizacion;
		this.numeroSolicitud = numeroSolicitud;
		this.ordenAmbulatoria = ordenAmbulatoria;
		this.detCargo = detCargo;
		this.servicio = servicio;
		this.articulo = articulo;
		this.cantidad = cantidad;
		this.estado = estado;
		this.activo = activo;
		this.justificacionSolicitud = justificacionSolicitud;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.usuarioModifica = usuarioModifica;
		this.tipoAsocio = tipoAsocio;
		this.servicioCx = servicioCx;
		this.cantidadAutorizacion = cantidadAutorizacion;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public long getAutorizacion() {
		return this.autorizacion;
	}

	public void setAutorizacion(long autorizacion) {
		this.autorizacion = autorizacion;
	}

	public Integer getNumeroSolicitud() {
		return this.numeroSolicitud;
	}

	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public Long getOrdenAmbulatoria() {
		return this.ordenAmbulatoria;
	}

	public void setOrdenAmbulatoria(Long ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	public Long getDetCargo() {
		return this.detCargo;
	}

	public void setDetCargo(Long detCargo) {
		this.detCargo = detCargo;
	}

	public Integer getServicio() {
		return this.servicio;
	}

	public void setServicio(Integer servicio) {
		this.servicio = servicio;
	}

	public Integer getArticulo() {
		return this.articulo;
	}

	public void setArticulo(Integer articulo) {
		this.articulo = articulo;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getJustificacionSolicitud() {
		return this.justificacionSolicitud;
	}

	public void setJustificacionSolicitud(String justificacionSolicitud) {
		this.justificacionSolicitud = justificacionSolicitud;
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

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public Integer getTipoAsocio() {
		return this.tipoAsocio;
	}

	public void setTipoAsocio(Integer tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	public Integer getServicioCx() {
		return this.servicioCx;
	}

	public void setServicioCx(Integer servicioCx) {
		this.servicioCx = servicioCx;
	}

	public Integer getCantidadAutorizacion() {
		return this.cantidadAutorizacion;
	}

	public void setCantidadAutorizacion(Integer cantidadAutorizacion) {
		this.cantidadAutorizacion = cantidadAutorizacion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DetAutorizacionesId))
			return false;
		DetAutorizacionesId castOther = (DetAutorizacionesId) other;

		return (this.getCodigo() == castOther.getCodigo())
				&& (this.getAutorizacion() == castOther.getAutorizacion())
				&& ((this.getNumeroSolicitud() == castOther
						.getNumeroSolicitud()) || (this.getNumeroSolicitud() != null
						&& castOther.getNumeroSolicitud() != null && this
						.getNumeroSolicitud().equals(
								castOther.getNumeroSolicitud())))
				&& ((this.getOrdenAmbulatoria() == castOther
						.getOrdenAmbulatoria()) || (this.getOrdenAmbulatoria() != null
						&& castOther.getOrdenAmbulatoria() != null && this
						.getOrdenAmbulatoria().equals(
								castOther.getOrdenAmbulatoria())))
				&& ((this.getDetCargo() == castOther.getDetCargo()) || (this
						.getDetCargo() != null
						&& castOther.getDetCargo() != null && this
						.getDetCargo().equals(castOther.getDetCargo())))
				&& ((this.getServicio() == castOther.getServicio()) || (this
						.getServicio() != null
						&& castOther.getServicio() != null && this
						.getServicio().equals(castOther.getServicio())))
				&& ((this.getArticulo() == castOther.getArticulo()) || (this
						.getArticulo() != null
						&& castOther.getArticulo() != null && this
						.getArticulo().equals(castOther.getArticulo())))
				&& ((this.getCantidad() == castOther.getCantidad()) || (this
						.getCantidad() != null
						&& castOther.getCantidad() != null && this
						.getCantidad().equals(castOther.getCantidad())))
				&& ((this.getEstado() == castOther.getEstado()) || (this
						.getEstado() != null
						&& castOther.getEstado() != null && this.getEstado()
						.equals(castOther.getEstado())))
				&& ((this.getActivo() == castOther.getActivo()) || (this
						.getActivo() != null
						&& castOther.getActivo() != null && this.getActivo()
						.equals(castOther.getActivo())))
				&& ((this.getJustificacionSolicitud() == castOther
						.getJustificacionSolicitud()) || (this
						.getJustificacionSolicitud() != null
						&& castOther.getJustificacionSolicitud() != null && this
						.getJustificacionSolicitud().equals(
								castOther.getJustificacionSolicitud())))
				&& ((this.getFechaModifica() == castOther.getFechaModifica()) || (this
						.getFechaModifica() != null
						&& castOther.getFechaModifica() != null && this
						.getFechaModifica()
						.equals(castOther.getFechaModifica())))
				&& ((this.getHoraModifica() == castOther.getHoraModifica()) || (this
						.getHoraModifica() != null
						&& castOther.getHoraModifica() != null && this
						.getHoraModifica().equals(castOther.getHoraModifica())))
				&& ((this.getUsuarioModifica() == castOther
						.getUsuarioModifica()) || (this.getUsuarioModifica() != null
						&& castOther.getUsuarioModifica() != null && this
						.getUsuarioModifica().equals(
								castOther.getUsuarioModifica())))
				&& ((this.getTipoAsocio() == castOther.getTipoAsocio()) || (this
						.getTipoAsocio() != null
						&& castOther.getTipoAsocio() != null && this
						.getTipoAsocio().equals(castOther.getTipoAsocio())))
				&& ((this.getServicioCx() == castOther.getServicioCx()) || (this
						.getServicioCx() != null
						&& castOther.getServicioCx() != null && this
						.getServicioCx().equals(castOther.getServicioCx())))
				&& ((this.getCantidadAutorizacion() == castOther
						.getCantidadAutorizacion()) || (this
						.getCantidadAutorizacion() != null
						&& castOther.getCantidadAutorizacion() != null && this
						.getCantidadAutorizacion().equals(
								castOther.getCantidadAutorizacion())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getCodigo();
		result = 37 * result + (int) this.getAutorizacion();
		result = 37
				* result
				+ (getNumeroSolicitud() == null ? 0 : this.getNumeroSolicitud()
						.hashCode());
		result = 37
				* result
				+ (getOrdenAmbulatoria() == null ? 0 : this
						.getOrdenAmbulatoria().hashCode());
		result = 37 * result
				+ (getDetCargo() == null ? 0 : this.getDetCargo().hashCode());
		result = 37 * result
				+ (getServicio() == null ? 0 : this.getServicio().hashCode());
		result = 37 * result
				+ (getArticulo() == null ? 0 : this.getArticulo().hashCode());
		result = 37 * result
				+ (getCantidad() == null ? 0 : this.getCantidad().hashCode());
		result = 37 * result
				+ (getEstado() == null ? 0 : this.getEstado().hashCode());
		result = 37 * result
				+ (getActivo() == null ? 0 : this.getActivo().hashCode());
		result = 37
				* result
				+ (getJustificacionSolicitud() == null ? 0 : this
						.getJustificacionSolicitud().hashCode());
		result = 37
				* result
				+ (getFechaModifica() == null ? 0 : this.getFechaModifica()
						.hashCode());
		result = 37
				* result
				+ (getHoraModifica() == null ? 0 : this.getHoraModifica()
						.hashCode());
		result = 37
				* result
				+ (getUsuarioModifica() == null ? 0 : this.getUsuarioModifica()
						.hashCode());
		result = 37
				* result
				+ (getTipoAsocio() == null ? 0 : this.getTipoAsocio()
						.hashCode());
		result = 37
				* result
				+ (getServicioCx() == null ? 0 : this.getServicioCx()
						.hashCode());
		result = 37
				* result
				+ (getCantidadAutorizacion() == null ? 0 : this
						.getCantidadAutorizacion().hashCode());
		return result;
	}

}
