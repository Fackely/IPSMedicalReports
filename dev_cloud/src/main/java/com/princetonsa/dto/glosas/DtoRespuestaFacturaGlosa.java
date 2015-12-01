/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.glosas;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoFactura;

import util.ConstantesBD;

/**
 * Data Transfer Object: 
 * Usado para el manejo del detalle de la respuesta de glosa 
 * 
 * @author Gio
 *
 */
public class DtoRespuestaFacturaGlosa implements Serializable
{
	private DtoFactura factura;
	private int respuesta;
	private String auditoria;
	private int codigoFactura;
	private String codigoFactRespuestaGlosa;
	private String consecutivoFactura;
	private String fechaFactura;
	private String cuentaCobro;
	private String fechaRadicacion;
	private String saldoFactura;
	private String conceptoGlosa;
	private String valorGlosa;
	private String valorRespuesta;
	private String valorGlosaFactura;
	private String consultaSql;
	private ArrayList<DtoRespuestaSolicitudGlosa> respuestasSolicitudes;
	private String ajuste;
	
	private String concepto;
	
	// Impresión de reportes
	private String sqlImpresionRespuestaGlosaFactura;
	
	private double porcentajeAceptado;
	private double porcentajeSoportado;
	private double valorAceptado;
	private double valorSoportado;

	private String eliminar;
	
	private DtoGlosa glosa= new DtoGlosa();	
	
	/**
	 * Constructor
	 */
	public DtoRespuestaFacturaGlosa()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del dto
	 */
	public void clean()
	{
		this.codigoFactura=ConstantesBD.codigoNuncaValido;
		this.consecutivoFactura="";
		this.fechaFactura="";
		this.cuentaCobro="";
		this.fechaRadicacion="";
		this.saldoFactura="";
		this.conceptoGlosa="";
		this.valorGlosa="";
		this.valorRespuesta="";
		this.consultaSql="";
		this.codigoFactRespuestaGlosa="";
		this.sqlImpresionRespuestaGlosaFactura="";
		this.porcentajeAceptado=0;
		this.porcentajeSoportado=0;
		this.valorAceptado=0;
		this.valorSoportado=0;
		this.glosa= new DtoGlosa();
		this.valorGlosaFactura="";
		this.factura= new DtoFactura();
		this.concepto="";	
		this.auditoria="";
		this.respuesta=0;
		this.ajuste="";
		this.eliminar=ConstantesBD.acronimoNo;
	}

	public String getEliminar() {
		return eliminar;
	}

	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	public String getAjuste() {
		return ajuste;
	}

	public void setAjuste(String ajuste) {
		this.ajuste = ajuste;
	}

	public int getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(int respuesta) {
		this.respuesta = respuesta;
	}

	public String getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(String auditoria) {
		this.auditoria = auditoria;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public DtoFactura getFactura() {
		return factura;
	}

	public void setFactura(DtoFactura factura) {
		this.factura = factura;
	}

	public String getValorGlosaFactura() {
		return valorGlosaFactura;
	}

	public void setValorGlosaFactura(String valorGlosaFactura) {
		this.valorGlosaFactura = valorGlosaFactura;
	}

	public DtoGlosa getGlosa() {
		return glosa;
	}

	public void setGlosa(DtoGlosa glosa) {
		this.glosa = glosa;
	}

	public double getPorcentajeAceptado() {
		return porcentajeAceptado;
	}

	public void setPorcentajeAceptado(double porcentajeAceptado) {
		this.porcentajeAceptado = porcentajeAceptado;
	}

	public double getPorcentajeSoportado() {
		return porcentajeSoportado;
	}

	public void setPorcentajeSoportado(double porcentajeSoportado) {
		this.porcentajeSoportado = porcentajeSoportado;
	}

	public double getValorAceptado() {
		return valorAceptado;
	}

	public void setValorAceptado(double valorAceptado) {
		this.valorAceptado = valorAceptado;
	}

	public double getValorSoportado() {
		return valorSoportado;
	}

	public void setValorSoportado(double valorSoportado) {
		this.valorSoportado = valorSoportado;
	}

	/**
	 * @return the codigoFactura
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	/**
	 * @return the consecutivoFactura
	 */
	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}

	/**
	 * @param consecutivoFactura the consecutivoFactura to set
	 */
	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	/**
	 * @return the fechaFactura
	 */
	public String getFechaFactura() {
		return fechaFactura;
	}

	/**
	 * @param fechaFactura the fechaFactura to set
	 */
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	/**
	 * @return the fechaRadicacion
	 */
	public String getFechaRadicacion() {
		return fechaRadicacion;
	}

	/**
	 * @param fechaRadicacion the fechaRadicacion to set
	 */
	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}

	/**
	 * @return the saldoFactura
	 */
	public String getSaldoFactura() {
		return saldoFactura;
	}

	/**
	 * @param saldoFactura the saldoFactura to set
	 */
	public void setSaldoFactura(String saldoFactura) {
		this.saldoFactura = saldoFactura;
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
	 * @return the cuentaCobro
	 */
	public String getCuentaCobro() {
		return cuentaCobro;
	}

	/**
	 * @param cuentaCobro the cuentaCobro to set
	 */
	public void setCuentaCobro(String cuentaCobro) {
		this.cuentaCobro = cuentaCobro;
	}

	/**
	 * @return the consultaSql
	 */
	public String getConsultaSql() {
		return consultaSql;
	}

	/**
	 * @param consultaSql the consultaSql to set
	 */
	public void setConsultaSql(String consultaSql) {
		this.consultaSql = consultaSql;
	}

	/**
	 * @return the codigoFactRespuestaGlosa
	 */
	public String getCodigoFactRespuestaGlosa() {
		return codigoFactRespuestaGlosa;
	}

	/**
	 * @param codigoFactRespuestaGlosa the codigoFactRespuestaGlosa to set
	 */
	public void setCodigoFactRespuestaGlosa(String codigoFactRespuestaGlosa) {
		this.codigoFactRespuestaGlosa = codigoFactRespuestaGlosa;
	}

	/**
	 * @return the respuestasSolicitudes
	 */
	public ArrayList<DtoRespuestaSolicitudGlosa> getRespuestasSolicitudes() {
		return respuestasSolicitudes;
	}

	/**
	 * @param respuestasSolicitudes the respuestasSolicitudes to set
	 */
	public void setRespuestasSolicitudes(
			ArrayList<DtoRespuestaSolicitudGlosa> respuestasSolicitudes) {
		this.respuestasSolicitudes = respuestasSolicitudes;
	}

	/**
	 * @return the sqlImpresionRespuestaGlosaFactura
	 */
	public String getSqlImpresionRespuestaGlosaFactura() {
		return sqlImpresionRespuestaGlosaFactura;
	}

	/**
	 * @param sqlImpresionRespuestaGlosaFactura the sqlImpresionRespuestaGlosaFactura to set
	 */
	public void setSqlImpresionRespuestaGlosaFactura(
			String sqlImpresionRespuestaGlosaFactura) {
		this.sqlImpresionRespuestaGlosaFactura = sqlImpresionRespuestaGlosaFactura;
	}
	
	
}
