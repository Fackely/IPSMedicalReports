package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hermorhu
 * @created 24-Nov-2012 
 */
public class BackupSolicitudesSubCuentaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5241872739124365148L;
	private long logDistribucionCuenta;
	private long idSolicitudesSubcuenta;
	private int solicitud;
	private Integer subCuenta;
	private Integer servicio;
	private Integer articulo;
	private BigDecimal porcentaje;
	private BigDecimal cantidad;
	private Double monto;
	private char cubierto;
	private Integer cuenta;
	private int tipoSolicitud;
	private char paquetizada;
	private Long solSubcuentaPadre;
	private Integer servicioCx;
	private Integer tipoAsocio;
	private String tipoDistribucion;
	private Date fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private char facturado;
	private char eliminado;
	private Boolean actualizado;
	private Integer detCxHonorarios;
	private Integer detAsocioCxSalasMat;
	
	/**
	 * 
	 */
	public BackupSolicitudesSubCuentaDto() {
		super();
	}

	/**
	 * @param logDistribucionCuenta
	 * @param idSolicitudesSubcuenta
	 * @param solicitud
	 * @param subCuenta
	 * @param servicio
	 * @param articulo
	 * @param porcentaje
	 * @param cantidad
	 * @param monto
	 * @param cubierto
	 * @param cuenta
	 * @param tipoSolicitud
	 * @param paquetizada
	 * @param solSubcuentaPadre
	 * @param servicioCx
	 * @param tipoAsocio
	 * @param tipoDistribucion
	 * @param fechaModifica
	 * @param horaModifica
	 * @param usuarioModifica
	 * @param facturado
	 * @param eliminado
	 * @param actualizado
	 * @param detCxHonorarios
	 * @param detAsocioCxSalasMat
	 */
	public BackupSolicitudesSubCuentaDto(long idSolicitudesSubcuenta, Integer servicioCx, 
			Long solSubcuentaPadre, Integer subCuenta, int solicitud, String usuarioModifica, 
			Integer servicio, Integer articulo, BigDecimal porcentaje, BigDecimal cantidad, 
			Double monto, char cubierto, Integer cuenta, int tipoSolicitud, char paquetizada, 
			Integer tipoAsocio, String tipoDistribucion, Date fechaModifica, String horaModifica, 
			char facturado, char eliminado, Boolean actualizado, Integer detCxHonorarios, 
			Integer detAsocioCxSalasMat) {
		this.idSolicitudesSubcuenta = idSolicitudesSubcuenta;
		this.servicioCx = servicioCx;
		this.solSubcuentaPadre = solSubcuentaPadre;
		this.subCuenta = subCuenta;
		this.solicitud = solicitud;
		this.usuarioModifica = usuarioModifica;
		this.servicio = servicio;
		this.articulo = articulo;
		this.porcentaje = porcentaje;
		this.cantidad = cantidad;
		this.monto = monto;
		this.cubierto = cubierto;
		this.cuenta = cuenta;
		this.tipoSolicitud = tipoSolicitud;
		this.paquetizada = paquetizada;
		this.tipoAsocio = tipoAsocio;
		this.tipoDistribucion = tipoDistribucion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.facturado = facturado;
		this.eliminado = eliminado;
		this.actualizado = actualizado;
		this.detCxHonorarios = detCxHonorarios;
		this.detAsocioCxSalasMat = detAsocioCxSalasMat;
	}

	/**
	 * @return the logDistribucionCuenta
	 */
	public long getLogDistribucionCuenta() {
		return logDistribucionCuenta;
	}

	/**
	 * @param logDistribucionCuenta the logDistribucionCuenta to set
	 */
	public void setLogDistribucionCuenta(long logDistribucionCuenta) {
		this.logDistribucionCuenta = logDistribucionCuenta;
	}

	/**
	 * @return the idSolicitudesSubcuenta
	 */
	public long getIdSolicitudesSubcuenta() {
		return idSolicitudesSubcuenta;
	}

	/**
	 * @param idSolicitudesSubcuenta the idSolicitudesSubcuenta to set
	 */
	public void setIdSolicitudesSubcuenta(long idSolicitudesSubcuenta) {
		this.idSolicitudesSubcuenta = idSolicitudesSubcuenta;
	}

	/**
	 * @return the solicitud
	 */
	public int getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the subCuenta
	 */
	public Integer getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(Integer subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the servicio
	 */
	public Integer getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(Integer servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the articulo
	 */
	public Integer getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(Integer articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the porcentaje
	 */
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * @return the cantidad
	 */
	public BigDecimal getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the monto
	 */
	public Double getMonto() {
		return monto;
	}

	/**
	 * @param monto the monto to set
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}

	/**
	 * @return the cubierto
	 */
	public char getCubierto() {
		return cubierto;
	}

	/**
	 * @param cubierto the cubierto to set
	 */
	public void setCubierto(char cubierto) {
		this.cubierto = cubierto;
	}

	/**
	 * @return the cuenta
	 */
	public Integer getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(Integer cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	/**
	 * @return the paquetizada
	 */
	public char getPaquetizada() {
		return paquetizada;
	}

	/**
	 * @param paquetizada the paquetizada to set
	 */
	public void setPaquetizada(char paquetizada) {
		this.paquetizada = paquetizada;
	}

	/**
	 * @return the solSubcuentaPadre
	 */
	public Long getSolSubcuentaPadre() {
		return solSubcuentaPadre;
	}

	/**
	 * @param solSubcuentaPadre the solSubcuentaPadre to set
	 */
	public void setSolSubcuentaPadre(Long solSubcuentaPadre) {
		this.solSubcuentaPadre = solSubcuentaPadre;
	}

	/**
	 * @return the servicioCx
	 */
	public Integer getServicioCx() {
		return servicioCx;
	}

	/**
	 * @param servicioCx the servicioCx to set
	 */
	public void setServicioCx(Integer servicioCx) {
		this.servicioCx = servicioCx;
	}

	/**
	 * @return the tipoAsocio
	 */
	public Integer getTipoAsocio() {
		return tipoAsocio;
	}

	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(Integer tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	/**
	 * @return the tipoDistribucion
	 */
	public String getTipoDistribucion() {
		return tipoDistribucion;
	}

	/**
	 * @param tipoDistribucion the tipoDistribucion to set
	 */
	public void setTipoDistribucion(String tipoDistribucion) {
		this.tipoDistribucion = tipoDistribucion;
	}

	/**
	 * @return the fechaModifica
	 */
	public Date getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the facturado
	 */
	public char getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(char facturado) {
		this.facturado = facturado;
	}

	/**
	 * @return the eliminado
	 */
	public char getEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(char eliminado) {
		this.eliminado = eliminado;
	}

	/**
	 * @return the actualizado
	 */
	public Boolean getActualizado() {
		return actualizado;
	}

	/**
	 * @param actualizado the actualizado to set
	 */
	public void setActualizado(Boolean actualizado) {
		this.actualizado = actualizado;
	}

	/**
	 * @return the detCxHonorarios
	 */
	public Integer getDetCxHonorarios() {
		return detCxHonorarios;
	}

	/**
	 * @param detCxHonorarios the detCxHonorarios to set
	 */
	public void setDetCxHonorarios(Integer detCxHonorarios) {
		this.detCxHonorarios = detCxHonorarios;
	}

	/**
	 * @return the detAsocioCxSalasMat
	 */
	public Integer getDetAsocioCxSalasMat() {
		return detAsocioCxSalasMat;
	}

	/**
	 * @param detAsocioCxSalasMat the detAsocioCxSalasMat to set
	 */
	public void setDetAsocioCxSalasMat(Integer detAsocioCxSalasMat) {
		this.detAsocioCxSalasMat = detAsocioCxSalasMat;
	}

}
