package com.princetonsa.dto.glosas;

import java.io.Serializable;

public class DtoRespuestaGlosa implements Serializable{

	private Integer codigoPk;
	private String respuestaConsecutivo;
	private String fechaRespuesta;
	private String estadoRespuesta;
	private Integer codigoGlosa;
	private Integer codigoInstitucion;
	private String razonSocialInstitucion;
	private double valorRespuesta;
	private String fechaAprobacionAnulacion;
	private String usuarioAprobacionAnulacion;
	private String motivoAprobacionAnulacion;
	private String fechaRadicacion;
	private String numeroRadicacion;
	private DtoConceptoRespuesta concepto;

	/**
	 * Constructor
	 */
	public DtoRespuestaGlosa() {
		this.clean();
	}

	public void clean() {
		this.codigoPk = null;
		this.respuestaConsecutivo = "";
		this.fechaRespuesta = "";
		this.fechaRespuesta = "";
		this.codigoGlosa = null;
		this.codigoInstitucion = null;
		this.razonSocialInstitucion = null;
		this.valorRespuesta = 0;
		this.fechaAprobacionAnulacion = "";
		this.usuarioAprobacionAnulacion = "";
		this.motivoAprobacionAnulacion = "";
		this.fechaRadicacion = "";
		this.numeroRadicacion = "";
		this.concepto = new DtoConceptoRespuesta();
	}

	/**
	 * @return the codigoPk
	 */
	public Integer getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk
	 *            the codigoPk to set
	 */
	public void setCodigoPk(Integer codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the respuestaConsecutivo
	 */
	public String getRespuestaConsecutivo() {
		return respuestaConsecutivo;
	}

	/**
	 * @param respuestaConsecutivo
	 *            the respuestaConsecutivo to set
	 */
	public void setRespuestaConsecutivo(String respuestaConsecutivo) {
		this.respuestaConsecutivo = respuestaConsecutivo;
	}

	/**
	 * @return the fechaRespuesta
	 */
	public String getFechaRespuesta() {
		return fechaRespuesta;
	}

	/**
	 * @param fechaRespuesta
	 *            the fechaRespuesta to set
	 */
	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	/**
	 * @return the estadoRespuesta
	 */
	public String getEstadoRespuesta() {
		return estadoRespuesta;
	}

	/**
	 * @param estadoRespuesta
	 *            the estadoRespuesta to set
	 */
	public void setEstadoRespuesta(String estadoRespuesta) {
		this.estadoRespuesta = estadoRespuesta;
	}

	/**
	 * @return the codigoGlosa
	 */
	public Integer getCodigoGlosa() {
		return codigoGlosa;
	}

	/**
	 * @param codigoGlosa
	 *            the codigoGlosa to set
	 */
	public void setCodigoGlosa(Integer codigoGlosa) {
		this.codigoGlosa = codigoGlosa;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public Integer getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion
	 *            the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(Integer codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the razonSocialInstitucion
	 */
	public String getRazonSocialInstitucion() {
		return razonSocialInstitucion;
	}

	/**
	 * @param razonSocialInstitucion
	 *            the razonSocialInstitucion to set
	 */
	public void setRazonSocialInstitucion(String razonSocialInstitucion) {
		this.razonSocialInstitucion = razonSocialInstitucion;
	}

	/**
	 * @return the valorRespuesta
	 */
	public double getValorRespuesta() {
		return valorRespuesta;
	}

	/**
	 * @param valorRespuesta
	 *            the valorRespuesta to set
	 */
	public void setValorRespuesta(double valorRespuesta) {
		this.valorRespuesta = valorRespuesta;
	}

	/**
	 * @return the fechaAprobacionAnulacion
	 */
	public String getFechaAprobacionAnulacion() {
		return fechaAprobacionAnulacion;
	}

	/**
	 * @param fechaAprobacionAnulacion
	 *            the fechaAprobacionAnulacion to set
	 */
	public void setFechaAprobacionAnulacion(String fechaAprobacionAnulacion) {
		this.fechaAprobacionAnulacion = fechaAprobacionAnulacion;
	}

	/**
	 * @return the usuarioAprobacionAnulacion
	 */
	public String getUsuarioAprobacionAnulacion() {
		return usuarioAprobacionAnulacion;
	}

	/**
	 * @param usuarioAprobacionAnulacion
	 *            the usuarioAprobacionAnulacion to set
	 */
	public void setUsuarioAprobacionAnulacion(String usuarioAprobacionAnulacion) {
		this.usuarioAprobacionAnulacion = usuarioAprobacionAnulacion;
	}

	/**
	 * @return the motivoAprobacionAnulacion
	 */
	public String getMotivoAprobacionAnulacion() {
		return motivoAprobacionAnulacion;
	}

	/**
	 * @param motivoAprobacionAnulacion
	 *            the motivoAprobacionAnulacion to set
	 */
	public void setMotivoAprobacionAnulacion(String motivoAprobacionAnulacion) {
		this.motivoAprobacionAnulacion = motivoAprobacionAnulacion;
	}

	/**
	 * @return the fechaRadicacion
	 */
	public String getFechaRadicacion() {
		return fechaRadicacion;
	}

	/**
	 * @param fechaRadicacion
	 *            the fechaRadicacion to set
	 */
	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}

	/**
	 * @return the numeroRadicacion
	 */
	public String getNumeroRadicacion() {
		return numeroRadicacion;
	}

	/**
	 * @param numeroRadicacion
	 *            the numeroRadicacion to set
	 */
	public void setNumeroRadicacion(String numeroRadicacion) {
		this.numeroRadicacion = numeroRadicacion;
	}

	public DtoConceptoRespuesta getConcepto() {
		return concepto;
	}

	public void setConcepto(DtoConceptoRespuesta concepto) {
		this.concepto = concepto;
	}

	
}
