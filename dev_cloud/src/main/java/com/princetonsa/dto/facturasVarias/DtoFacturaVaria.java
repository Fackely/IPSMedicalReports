package com.princetonsa.dto.facturasVarias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoMultasCitas;

/**
 * DTO para la transferencia de información de las factuas varias
 * 
 * @author Juan David Ramírez
 * @since 11 Septiembre 2010
 */
@SuppressWarnings("serial")
public class DtoFacturaVaria implements Serializable {

	private Long consecutivo;
	private String estadoFactura;
	private int centroAtencion;
	private String nombreCentroAtencion;
	private int centroCosto;
	private String fecha;
	private Long concepto;
	private BigDecimal valorFactura;
	private int deudor;
	private String observaciones;
	private String usuarioModifica;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private BigDecimal valorPagos;
	private String usuarioAnulacion;
	private String fechaAnulacion;
	private String motivoAnulacion;
	private BigDecimal ajustesCredito;
	private BigDecimal ajustesDebito;
	private Long codigoFacturaVaria;
	private String anulacionFacturaAprobada;
	private String usuarioAprobacion;
	private String fechaAprobacion;
	private String fechaGeneracionAprobacion;
	private String fechaGeneracionAnulacion;
	private String horaGeneracionAnulacion;
	private String horaGeneracionAprobacion;
	private String motivoAprobacion;
	private String contabilizado;
	private String contabilizadoAnulacion;

	private ArrayList<DtoMultasCitas> multasCitas;

	/**
	 * 
	 */
	public DtoFacturaVaria() {
		
		multasCitas = new ArrayList<DtoMultasCitas>();
	}
	
	public Long getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getEstadoFactura() {
		return estadoFactura;
	}

	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public int getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Long getConcepto() {
		return concepto;
	}

	public void setConcepto(Long concepto) {
		this.concepto = concepto;
	}

	public BigDecimal getValorFactura() {
		return valorFactura;
	}

	public void setValorFactura(BigDecimal valorFactura) {
		this.valorFactura = valorFactura;
	}

	public int getDeudor() {
		return deudor;
	}

	public void setDeudor(int deudor) {
		this.deudor = deudor;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public BigDecimal getValorPagos() {
		return valorPagos;
	}

	public void setValorPagos(BigDecimal valorPagos) {
		this.valorPagos = valorPagos;
	}

	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}

	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}

	public String getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	public BigDecimal getAjustesCredito() {
		return ajustesCredito;
	}

	public void setAjustesCredito(BigDecimal ajustesCredito) {
		this.ajustesCredito = ajustesCredito;
	}

	public BigDecimal getAjustesDebito() {
		return ajustesDebito;
	}

	public void setAjustesDebito(BigDecimal ajustesDebito) {
		this.ajustesDebito = ajustesDebito;
	}

	public Long getCodigoFacturaVaria() {
		return codigoFacturaVaria;
	}

	public void setCodigoFacturaVaria(Long codigoFacturaVaria) {
		this.codigoFacturaVaria = codigoFacturaVaria;
	}

	public String getAnulacionFacturaAprobada() {
		return anulacionFacturaAprobada;
	}

	public void setAnulacionFacturaAprobada(String anulacionFacturaAprobada) {
		this.anulacionFacturaAprobada = anulacionFacturaAprobada;
	}

	public String getUsuarioAprobacion() {
		return usuarioAprobacion;
	}

	public void setUsuarioAprobacion(String usuarioAprobacion) {
		this.usuarioAprobacion = usuarioAprobacion;
	}

	public String getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(String fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public String getFechaGeneracionAprobacion() {
		return fechaGeneracionAprobacion;
	}

	public void setFechaGeneracionAprobacion(String fechaGeneracionAprobacion) {
		this.fechaGeneracionAprobacion = fechaGeneracionAprobacion;
	}

	public String getFechaGeneracionAnulacion() {
		return fechaGeneracionAnulacion;
	}

	public void setFechaGeneracionAnulacion(String fechaGeneracionAnulacion) {
		this.fechaGeneracionAnulacion = fechaGeneracionAnulacion;
	}

	public String getHoraGeneracionAnulacion() {
		return horaGeneracionAnulacion;
	}

	public void setHoraGeneracionAnulacion(String horaGeneracionAnulacion) {
		this.horaGeneracionAnulacion = horaGeneracionAnulacion;
	}

	public String getHoraGeneracionAprobacion() {
		return horaGeneracionAprobacion;
	}

	public void setHoraGeneracionAprobacion(String horaGeneracionAprobacion) {
		this.horaGeneracionAprobacion = horaGeneracionAprobacion;
	}

	public String getMotivoAprobacion() {
		return motivoAprobacion;
	}

	public void setMotivoAprobacion(String motivoAprobacion) {
		this.motivoAprobacion = motivoAprobacion;
	}

	public String getContabilizado() {
		return contabilizado;
	}

	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}

	public String getContabilizadoAnulacion() {
		return contabilizadoAnulacion;
	}

	public void setContabilizadoAnulacion(String contabilizadoAnulacion) {
		this.contabilizadoAnulacion = contabilizadoAnulacion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the multasCitas
	 */
	public ArrayList<DtoMultasCitas> getMultasCitas() {
		return multasCitas;
	}

	/**
	 * @param multasCitas the multasCitas to set
	 */
	public void setMultasCitas(ArrayList<DtoMultasCitas> multasCitas) {
		this.multasCitas = multasCitas;
	}
}
