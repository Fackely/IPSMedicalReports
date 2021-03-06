package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * DetCargosCxAnuladosTempId generated by hbm2java
 */
public class DetCargosCxAnuladosTempId implements java.io.Serializable {

	private Long codigoDetalleCargo;
	private Long subCuenta;
	private Integer convenio;
	private Integer contrato;
	private Integer esquemaTarifario;
	private BigDecimal cantidadCargada;
	private BigDecimal valorUnitarioTarifa;
	private BigDecimal valorUnitarioCargado;
	private BigDecimal valorTotalCargado;
	private BigDecimal porcentajeCargado;
	private BigDecimal porcentajeRecargo;
	private BigDecimal valorUnitarioRecargo;
	private BigDecimal porcentajeDcto;
	private BigDecimal valorUnitarioDcto;
	private BigDecimal valorUnitarioIva;
	private String requiereAutorizacion;
	private String nroAutorizacion;
	private Integer estado;
	private String cubierto;
	private String tipoDistribucion;
	private Integer solicitud;
	private Integer servicio;
	private Integer articulo;
	private Integer servicioCx;
	private Integer tipoAsocio;
	private String facturado;
	private Integer tipoSolicitud;
	private String paquetizado;
	private Long cargoPadre;
	private String usuarioModifica;
	private Date fechaModifica;
	private String horaModifica;
	private Long codSolSubcuenta;
	private String observaciones;
	private Integer codigoFactura;
	private Character eliminado;

	public DetCargosCxAnuladosTempId() {
	}

	public DetCargosCxAnuladosTempId(Long codigoDetalleCargo, Long subCuenta,
			Integer convenio, Integer contrato, Integer esquemaTarifario,
			BigDecimal cantidadCargada, BigDecimal valorUnitarioTarifa,
			BigDecimal valorUnitarioCargado, BigDecimal valorTotalCargado,
			BigDecimal porcentajeCargado, BigDecimal porcentajeRecargo,
			BigDecimal valorUnitarioRecargo, BigDecimal porcentajeDcto,
			BigDecimal valorUnitarioDcto, BigDecimal valorUnitarioIva,
			String requiereAutorizacion, String nroAutorizacion,
			Integer estado, String cubierto, String tipoDistribucion,
			Integer solicitud, Integer servicio, Integer articulo,
			Integer servicioCx, Integer tipoAsocio, String facturado,
			Integer tipoSolicitud, String paquetizado, Long cargoPadre,
			String usuarioModifica, Date fechaModifica, String horaModifica,
			Long codSolSubcuenta, String observaciones, Integer codigoFactura,
			Character eliminado) {
		this.codigoDetalleCargo = codigoDetalleCargo;
		this.subCuenta = subCuenta;
		this.convenio = convenio;
		this.contrato = contrato;
		this.esquemaTarifario = esquemaTarifario;
		this.cantidadCargada = cantidadCargada;
		this.valorUnitarioTarifa = valorUnitarioTarifa;
		this.valorUnitarioCargado = valorUnitarioCargado;
		this.valorTotalCargado = valorTotalCargado;
		this.porcentajeCargado = porcentajeCargado;
		this.porcentajeRecargo = porcentajeRecargo;
		this.valorUnitarioRecargo = valorUnitarioRecargo;
		this.porcentajeDcto = porcentajeDcto;
		this.valorUnitarioDcto = valorUnitarioDcto;
		this.valorUnitarioIva = valorUnitarioIva;
		this.requiereAutorizacion = requiereAutorizacion;
		this.nroAutorizacion = nroAutorizacion;
		this.estado = estado;
		this.cubierto = cubierto;
		this.tipoDistribucion = tipoDistribucion;
		this.solicitud = solicitud;
		this.servicio = servicio;
		this.articulo = articulo;
		this.servicioCx = servicioCx;
		this.tipoAsocio = tipoAsocio;
		this.facturado = facturado;
		this.tipoSolicitud = tipoSolicitud;
		this.paquetizado = paquetizado;
		this.cargoPadre = cargoPadre;
		this.usuarioModifica = usuarioModifica;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.codSolSubcuenta = codSolSubcuenta;
		this.observaciones = observaciones;
		this.codigoFactura = codigoFactura;
		this.eliminado = eliminado;
	}

	public Long getCodigoDetalleCargo() {
		return this.codigoDetalleCargo;
	}

	public void setCodigoDetalleCargo(Long codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	public Long getSubCuenta() {
		return this.subCuenta;
	}

	public void setSubCuenta(Long subCuenta) {
		this.subCuenta = subCuenta;
	}

	public Integer getConvenio() {
		return this.convenio;
	}

	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	public Integer getContrato() {
		return this.contrato;
	}

	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	public Integer getEsquemaTarifario() {
		return this.esquemaTarifario;
	}

	public void setEsquemaTarifario(Integer esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	public BigDecimal getCantidadCargada() {
		return this.cantidadCargada;
	}

	public void setCantidadCargada(BigDecimal cantidadCargada) {
		this.cantidadCargada = cantidadCargada;
	}

	public BigDecimal getValorUnitarioTarifa() {
		return this.valorUnitarioTarifa;
	}

	public void setValorUnitarioTarifa(BigDecimal valorUnitarioTarifa) {
		this.valorUnitarioTarifa = valorUnitarioTarifa;
	}

	public BigDecimal getValorUnitarioCargado() {
		return this.valorUnitarioCargado;
	}

	public void setValorUnitarioCargado(BigDecimal valorUnitarioCargado) {
		this.valorUnitarioCargado = valorUnitarioCargado;
	}

	public BigDecimal getValorTotalCargado() {
		return this.valorTotalCargado;
	}

	public void setValorTotalCargado(BigDecimal valorTotalCargado) {
		this.valorTotalCargado = valorTotalCargado;
	}

	public BigDecimal getPorcentajeCargado() {
		return this.porcentajeCargado;
	}

	public void setPorcentajeCargado(BigDecimal porcentajeCargado) {
		this.porcentajeCargado = porcentajeCargado;
	}

	public BigDecimal getPorcentajeRecargo() {
		return this.porcentajeRecargo;
	}

	public void setPorcentajeRecargo(BigDecimal porcentajeRecargo) {
		this.porcentajeRecargo = porcentajeRecargo;
	}

	public BigDecimal getValorUnitarioRecargo() {
		return this.valorUnitarioRecargo;
	}

	public void setValorUnitarioRecargo(BigDecimal valorUnitarioRecargo) {
		this.valorUnitarioRecargo = valorUnitarioRecargo;
	}

	public BigDecimal getPorcentajeDcto() {
		return this.porcentajeDcto;
	}

	public void setPorcentajeDcto(BigDecimal porcentajeDcto) {
		this.porcentajeDcto = porcentajeDcto;
	}

	public BigDecimal getValorUnitarioDcto() {
		return this.valorUnitarioDcto;
	}

	public void setValorUnitarioDcto(BigDecimal valorUnitarioDcto) {
		this.valorUnitarioDcto = valorUnitarioDcto;
	}

	public BigDecimal getValorUnitarioIva() {
		return this.valorUnitarioIva;
	}

	public void setValorUnitarioIva(BigDecimal valorUnitarioIva) {
		this.valorUnitarioIva = valorUnitarioIva;
	}

	public String getRequiereAutorizacion() {
		return this.requiereAutorizacion;
	}

	public void setRequiereAutorizacion(String requiereAutorizacion) {
		this.requiereAutorizacion = requiereAutorizacion;
	}

	public String getNroAutorizacion() {
		return this.nroAutorizacion;
	}

	public void setNroAutorizacion(String nroAutorizacion) {
		this.nroAutorizacion = nroAutorizacion;
	}

	public Integer getEstado() {
		return this.estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getCubierto() {
		return this.cubierto;
	}

	public void setCubierto(String cubierto) {
		this.cubierto = cubierto;
	}

	public String getTipoDistribucion() {
		return this.tipoDistribucion;
	}

	public void setTipoDistribucion(String tipoDistribucion) {
		this.tipoDistribucion = tipoDistribucion;
	}

	public Integer getSolicitud() {
		return this.solicitud;
	}

	public void setSolicitud(Integer solicitud) {
		this.solicitud = solicitud;
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

	public Integer getServicioCx() {
		return this.servicioCx;
	}

	public void setServicioCx(Integer servicioCx) {
		this.servicioCx = servicioCx;
	}

	public Integer getTipoAsocio() {
		return this.tipoAsocio;
	}

	public void setTipoAsocio(Integer tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	public String getFacturado() {
		return this.facturado;
	}

	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}

	public Integer getTipoSolicitud() {
		return this.tipoSolicitud;
	}

	public void setTipoSolicitud(Integer tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getPaquetizado() {
		return this.paquetizado;
	}

	public void setPaquetizado(String paquetizado) {
		this.paquetizado = paquetizado;
	}

	public Long getCargoPadre() {
		return this.cargoPadre;
	}

	public void setCargoPadre(Long cargoPadre) {
		this.cargoPadre = cargoPadre;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
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

	public Long getCodSolSubcuenta() {
		return this.codSolSubcuenta;
	}

	public void setCodSolSubcuenta(Long codSolSubcuenta) {
		this.codSolSubcuenta = codSolSubcuenta;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getCodigoFactura() {
		return this.codigoFactura;
	}

	public void setCodigoFactura(Integer codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	public Character getEliminado() {
		return this.eliminado;
	}

	public void setEliminado(Character eliminado) {
		this.eliminado = eliminado;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DetCargosCxAnuladosTempId))
			return false;
		DetCargosCxAnuladosTempId castOther = (DetCargosCxAnuladosTempId) other;

		return ((this.getCodigoDetalleCargo() == castOther
				.getCodigoDetalleCargo()) || (this.getCodigoDetalleCargo() != null
				&& castOther.getCodigoDetalleCargo() != null && this
				.getCodigoDetalleCargo().equals(
						castOther.getCodigoDetalleCargo())))
				&& ((this.getSubCuenta() == castOther.getSubCuenta()) || (this
						.getSubCuenta() != null
						&& castOther.getSubCuenta() != null && this
						.getSubCuenta().equals(castOther.getSubCuenta())))
				&& ((this.getConvenio() == castOther.getConvenio()) || (this
						.getConvenio() != null
						&& castOther.getConvenio() != null && this
						.getConvenio().equals(castOther.getConvenio())))
				&& ((this.getContrato() == castOther.getContrato()) || (this
						.getContrato() != null
						&& castOther.getContrato() != null && this
						.getContrato().equals(castOther.getContrato())))
				&& ((this.getEsquemaTarifario() == castOther
						.getEsquemaTarifario()) || (this.getEsquemaTarifario() != null
						&& castOther.getEsquemaTarifario() != null && this
						.getEsquemaTarifario().equals(
								castOther.getEsquemaTarifario())))
				&& ((this.getCantidadCargada() == castOther
						.getCantidadCargada()) || (this.getCantidadCargada() != null
						&& castOther.getCantidadCargada() != null && this
						.getCantidadCargada().equals(
								castOther.getCantidadCargada())))
				&& ((this.getValorUnitarioTarifa() == castOther
						.getValorUnitarioTarifa()) || (this
						.getValorUnitarioTarifa() != null
						&& castOther.getValorUnitarioTarifa() != null && this
						.getValorUnitarioTarifa().equals(
								castOther.getValorUnitarioTarifa())))
				&& ((this.getValorUnitarioCargado() == castOther
						.getValorUnitarioCargado()) || (this
						.getValorUnitarioCargado() != null
						&& castOther.getValorUnitarioCargado() != null && this
						.getValorUnitarioCargado().equals(
								castOther.getValorUnitarioCargado())))
				&& ((this.getValorTotalCargado() == castOther
						.getValorTotalCargado()) || (this
						.getValorTotalCargado() != null
						&& castOther.getValorTotalCargado() != null && this
						.getValorTotalCargado().equals(
								castOther.getValorTotalCargado())))
				&& ((this.getPorcentajeCargado() == castOther
						.getPorcentajeCargado()) || (this
						.getPorcentajeCargado() != null
						&& castOther.getPorcentajeCargado() != null && this
						.getPorcentajeCargado().equals(
								castOther.getPorcentajeCargado())))
				&& ((this.getPorcentajeRecargo() == castOther
						.getPorcentajeRecargo()) || (this
						.getPorcentajeRecargo() != null
						&& castOther.getPorcentajeRecargo() != null && this
						.getPorcentajeRecargo().equals(
								castOther.getPorcentajeRecargo())))
				&& ((this.getValorUnitarioRecargo() == castOther
						.getValorUnitarioRecargo()) || (this
						.getValorUnitarioRecargo() != null
						&& castOther.getValorUnitarioRecargo() != null && this
						.getValorUnitarioRecargo().equals(
								castOther.getValorUnitarioRecargo())))
				&& ((this.getPorcentajeDcto() == castOther.getPorcentajeDcto()) || (this
						.getPorcentajeDcto() != null
						&& castOther.getPorcentajeDcto() != null && this
						.getPorcentajeDcto().equals(
								castOther.getPorcentajeDcto())))
				&& ((this.getValorUnitarioDcto() == castOther
						.getValorUnitarioDcto()) || (this
						.getValorUnitarioDcto() != null
						&& castOther.getValorUnitarioDcto() != null && this
						.getValorUnitarioDcto().equals(
								castOther.getValorUnitarioDcto())))
				&& ((this.getValorUnitarioIva() == castOther
						.getValorUnitarioIva()) || (this.getValorUnitarioIva() != null
						&& castOther.getValorUnitarioIva() != null && this
						.getValorUnitarioIva().equals(
								castOther.getValorUnitarioIva())))
				&& ((this.getRequiereAutorizacion() == castOther
						.getRequiereAutorizacion()) || (this
						.getRequiereAutorizacion() != null
						&& castOther.getRequiereAutorizacion() != null && this
						.getRequiereAutorizacion().equals(
								castOther.getRequiereAutorizacion())))
				&& ((this.getNroAutorizacion() == castOther
						.getNroAutorizacion()) || (this.getNroAutorizacion() != null
						&& castOther.getNroAutorizacion() != null && this
						.getNroAutorizacion().equals(
								castOther.getNroAutorizacion())))
				&& ((this.getEstado() == castOther.getEstado()) || (this
						.getEstado() != null
						&& castOther.getEstado() != null && this.getEstado()
						.equals(castOther.getEstado())))
				&& ((this.getCubierto() == castOther.getCubierto()) || (this
						.getCubierto() != null
						&& castOther.getCubierto() != null && this
						.getCubierto().equals(castOther.getCubierto())))
				&& ((this.getTipoDistribucion() == castOther
						.getTipoDistribucion()) || (this.getTipoDistribucion() != null
						&& castOther.getTipoDistribucion() != null && this
						.getTipoDistribucion().equals(
								castOther.getTipoDistribucion())))
				&& ((this.getSolicitud() == castOther.getSolicitud()) || (this
						.getSolicitud() != null
						&& castOther.getSolicitud() != null && this
						.getSolicitud().equals(castOther.getSolicitud())))
				&& ((this.getServicio() == castOther.getServicio()) || (this
						.getServicio() != null
						&& castOther.getServicio() != null && this
						.getServicio().equals(castOther.getServicio())))
				&& ((this.getArticulo() == castOther.getArticulo()) || (this
						.getArticulo() != null
						&& castOther.getArticulo() != null && this
						.getArticulo().equals(castOther.getArticulo())))
				&& ((this.getServicioCx() == castOther.getServicioCx()) || (this
						.getServicioCx() != null
						&& castOther.getServicioCx() != null && this
						.getServicioCx().equals(castOther.getServicioCx())))
				&& ((this.getTipoAsocio() == castOther.getTipoAsocio()) || (this
						.getTipoAsocio() != null
						&& castOther.getTipoAsocio() != null && this
						.getTipoAsocio().equals(castOther.getTipoAsocio())))
				&& ((this.getFacturado() == castOther.getFacturado()) || (this
						.getFacturado() != null
						&& castOther.getFacturado() != null && this
						.getFacturado().equals(castOther.getFacturado())))
				&& ((this.getTipoSolicitud() == castOther.getTipoSolicitud()) || (this
						.getTipoSolicitud() != null
						&& castOther.getTipoSolicitud() != null && this
						.getTipoSolicitud()
						.equals(castOther.getTipoSolicitud())))
				&& ((this.getPaquetizado() == castOther.getPaquetizado()) || (this
						.getPaquetizado() != null
						&& castOther.getPaquetizado() != null && this
						.getPaquetizado().equals(castOther.getPaquetizado())))
				&& ((this.getCargoPadre() == castOther.getCargoPadre()) || (this
						.getCargoPadre() != null
						&& castOther.getCargoPadre() != null && this
						.getCargoPadre().equals(castOther.getCargoPadre())))
				&& ((this.getUsuarioModifica() == castOther
						.getUsuarioModifica()) || (this.getUsuarioModifica() != null
						&& castOther.getUsuarioModifica() != null && this
						.getUsuarioModifica().equals(
								castOther.getUsuarioModifica())))
				&& ((this.getFechaModifica() == castOther.getFechaModifica()) || (this
						.getFechaModifica() != null
						&& castOther.getFechaModifica() != null && this
						.getFechaModifica()
						.equals(castOther.getFechaModifica())))
				&& ((this.getHoraModifica() == castOther.getHoraModifica()) || (this
						.getHoraModifica() != null
						&& castOther.getHoraModifica() != null && this
						.getHoraModifica().equals(castOther.getHoraModifica())))
				&& ((this.getCodSolSubcuenta() == castOther
						.getCodSolSubcuenta()) || (this.getCodSolSubcuenta() != null
						&& castOther.getCodSolSubcuenta() != null && this
						.getCodSolSubcuenta().equals(
								castOther.getCodSolSubcuenta())))
				&& ((this.getObservaciones() == castOther.getObservaciones()) || (this
						.getObservaciones() != null
						&& castOther.getObservaciones() != null && this
						.getObservaciones()
						.equals(castOther.getObservaciones())))
				&& ((this.getCodigoFactura() == castOther.getCodigoFactura()) || (this
						.getCodigoFactura() != null
						&& castOther.getCodigoFactura() != null && this
						.getCodigoFactura()
						.equals(castOther.getCodigoFactura())))
				&& ((this.getEliminado() == castOther.getEliminado()) || (this
						.getEliminado() != null
						&& castOther.getEliminado() != null && this
						.getEliminado().equals(castOther.getEliminado())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCodigoDetalleCargo() == null ? 0 : this
						.getCodigoDetalleCargo().hashCode());
		result = 37 * result
				+ (getSubCuenta() == null ? 0 : this.getSubCuenta().hashCode());
		result = 37 * result
				+ (getConvenio() == null ? 0 : this.getConvenio().hashCode());
		result = 37 * result
				+ (getContrato() == null ? 0 : this.getContrato().hashCode());
		result = 37
				* result
				+ (getEsquemaTarifario() == null ? 0 : this
						.getEsquemaTarifario().hashCode());
		result = 37
				* result
				+ (getCantidadCargada() == null ? 0 : this.getCantidadCargada()
						.hashCode());
		result = 37
				* result
				+ (getValorUnitarioTarifa() == null ? 0 : this
						.getValorUnitarioTarifa().hashCode());
		result = 37
				* result
				+ (getValorUnitarioCargado() == null ? 0 : this
						.getValorUnitarioCargado().hashCode());
		result = 37
				* result
				+ (getValorTotalCargado() == null ? 0 : this
						.getValorTotalCargado().hashCode());
		result = 37
				* result
				+ (getPorcentajeCargado() == null ? 0 : this
						.getPorcentajeCargado().hashCode());
		result = 37
				* result
				+ (getPorcentajeRecargo() == null ? 0 : this
						.getPorcentajeRecargo().hashCode());
		result = 37
				* result
				+ (getValorUnitarioRecargo() == null ? 0 : this
						.getValorUnitarioRecargo().hashCode());
		result = 37
				* result
				+ (getPorcentajeDcto() == null ? 0 : this.getPorcentajeDcto()
						.hashCode());
		result = 37
				* result
				+ (getValorUnitarioDcto() == null ? 0 : this
						.getValorUnitarioDcto().hashCode());
		result = 37
				* result
				+ (getValorUnitarioIva() == null ? 0 : this
						.getValorUnitarioIva().hashCode());
		result = 37
				* result
				+ (getRequiereAutorizacion() == null ? 0 : this
						.getRequiereAutorizacion().hashCode());
		result = 37
				* result
				+ (getNroAutorizacion() == null ? 0 : this.getNroAutorizacion()
						.hashCode());
		result = 37 * result
				+ (getEstado() == null ? 0 : this.getEstado().hashCode());
		result = 37 * result
				+ (getCubierto() == null ? 0 : this.getCubierto().hashCode());
		result = 37
				* result
				+ (getTipoDistribucion() == null ? 0 : this
						.getTipoDistribucion().hashCode());
		result = 37 * result
				+ (getSolicitud() == null ? 0 : this.getSolicitud().hashCode());
		result = 37 * result
				+ (getServicio() == null ? 0 : this.getServicio().hashCode());
		result = 37 * result
				+ (getArticulo() == null ? 0 : this.getArticulo().hashCode());
		result = 37
				* result
				+ (getServicioCx() == null ? 0 : this.getServicioCx()
						.hashCode());
		result = 37
				* result
				+ (getTipoAsocio() == null ? 0 : this.getTipoAsocio()
						.hashCode());
		result = 37 * result
				+ (getFacturado() == null ? 0 : this.getFacturado().hashCode());
		result = 37
				* result
				+ (getTipoSolicitud() == null ? 0 : this.getTipoSolicitud()
						.hashCode());
		result = 37
				* result
				+ (getPaquetizado() == null ? 0 : this.getPaquetizado()
						.hashCode());
		result = 37
				* result
				+ (getCargoPadre() == null ? 0 : this.getCargoPadre()
						.hashCode());
		result = 37
				* result
				+ (getUsuarioModifica() == null ? 0 : this.getUsuarioModifica()
						.hashCode());
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
				+ (getCodSolSubcuenta() == null ? 0 : this.getCodSolSubcuenta()
						.hashCode());
		result = 37
				* result
				+ (getObservaciones() == null ? 0 : this.getObservaciones()
						.hashCode());
		result = 37
				* result
				+ (getCodigoFactura() == null ? 0 : this.getCodigoFactura()
						.hashCode());
		result = 37 * result
				+ (getEliminado() == null ? 0 : this.getEliminado().hashCode());
		return result;
	}

}
