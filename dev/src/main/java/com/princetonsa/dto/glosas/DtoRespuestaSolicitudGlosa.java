/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.glosas;

import java.io.Serializable;

/**
 * Data Transfer Object: 
 * Usado para el manejo del detalle de la respuesta de glosa 
 * 
 * @author Gio
 *
 */
public class DtoRespuestaSolicitudGlosa implements Serializable
{
	
	private String solicitud;
	private String descripcionServicioArticulo;
	private String centroCosto;
	private String conceptoGlosa;
	private String cantidadGlosa;
	private String valorGlosa;
	private String conceptoRespuesta;
	private String descConceptoResp;
	private String motivo;
	private String cantidadRespuesta;
	private String valorRespuesta;
	private String ajuste;
	private String valorAceptado;
	private String valorSoportado;
	private DtoConceptoGlosa concepto;

	private DtoRespuestaFacturaGlosa facturaResp= new DtoRespuestaFacturaGlosa(); 
	
	/**
	 * Constructor
	 */
	public DtoRespuestaSolicitudGlosa()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del dto
	 */
	public void clean()
	{
		this.solicitud="";
		this.descripcionServicioArticulo="";
		this.centroCosto="";
		this.conceptoGlosa="";
		this.cantidadGlosa="";
		this.valorGlosa="";
		this.conceptoRespuesta="";
		this.motivo="";
		this.cantidadRespuesta="";
		this.valorRespuesta="";
		this.ajuste="";
		this.valorAceptado="";
		this.valorSoportado="";
		this.facturaResp= new DtoRespuestaFacturaGlosa();
		this.descConceptoResp="";
		this.concepto= new DtoConceptoGlosa();
	}

	public DtoConceptoGlosa getConcepto() {
		return concepto;
	}

	public void setConcepto(DtoConceptoGlosa concepto) {
		this.concepto = concepto;
	}

	public String getDescConceptoResp() {
		return descConceptoResp;
	}

	public void setDescConceptoResp(String descConceptoResp) {
		this.descConceptoResp = descConceptoResp;
	}

	public DtoRespuestaFacturaGlosa getFacturaResp() {
		return facturaResp;
	}

	public void setFacturaResp(DtoRespuestaFacturaGlosa facturaResp) {
		this.facturaResp = facturaResp;
	}

	public String getValorAceptado() {
		return valorAceptado;
	}

	public void setValorAceptado(String valorAceptado) {
		this.valorAceptado = valorAceptado;
	}

	public String getValorSoportado() {
		return valorSoportado;
	}

	public void setValorSoportado(String valorSoportado) {
		this.valorSoportado = valorSoportado;
	}

	/**
	 * @return the solicitud
	 */
	public String getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the descripcionServicioArticulo
	 */
	public String getDescripcionServicioArticulo() {
		return descripcionServicioArticulo;
	}

	/**
	 * @param descripcionServicioArticulo the descripcionServicioArticulo to set
	 */
	public void setDescripcionServicioArticulo(String descripcionServicioArticulo) {
		this.descripcionServicioArticulo = descripcionServicioArticulo;
	}

	/**
	 * @return the centroCosto
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the conceptoGlosa
	 */
	public String getConceptoGlosa() {
		return conceptoGlosa;
	}

	/**
	 * @param conceptoGlosa the conceptoGlosa to set
	 */
	public void setConceptoGlosa(String conceptoGlosa) {
		this.conceptoGlosa = conceptoGlosa;
	}

	/**
	 * @return the cantidadGlosa
	 */
	public String getCantidadGlosa() {
		return cantidadGlosa;
	}

	/**
	 * @param cantidadGlosa the cantidadGlosa to set
	 */
	public void setCantidadGlosa(String cantidadGlosa) {
		this.cantidadGlosa = cantidadGlosa;
	}

	/**
	 * @return the valorGlosa
	 */
	public String getValorGlosa() {
		return valorGlosa;
	}

	/**
	 * @param valorGlosa the valorGlosa to set
	 */
	public void setValorGlosa(String valorGlosa) {
		this.valorGlosa = valorGlosa;
	}

	/**
	 * @return the conceptoRespuesta
	 */
	public String getConceptoRespuesta() {
		return conceptoRespuesta;
	}

	/**
	 * @param conceptoRespuesta the conceptoRespuesta to set
	 */
	public void setConceptoRespuesta(String conceptoRespuesta) {
		this.conceptoRespuesta = conceptoRespuesta;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the cantidadRespuesta
	 */
	public String getCantidadRespuesta() {
		return cantidadRespuesta;
	}

	/**
	 * @param cantidadRespuesta the cantidadRespuesta to set
	 */
	public void setCantidadRespuesta(String cantidadRespuesta) {
		this.cantidadRespuesta = cantidadRespuesta;
	}

	/**
	 * @return the valorRespuesta
	 */
	public String getValorRespuesta() {
		return valorRespuesta;
	}

	/**
	 * @param valorRespuesta the valorRespuesta to set
	 */
	public void setValorRespuesta(String valorRespuesta) {
		this.valorRespuesta = valorRespuesta;
	}

	/**
	 * @return the ajuste
	 */
	public String getAjuste() {
		return ajuste;
	}

	/**
	 * @param ajuste the ajuste to set
	 */
	public void setAjuste(String ajuste) {
		this.ajuste = ajuste;
	}

	
}
