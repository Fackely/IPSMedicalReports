package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoDetVigConRetencion implements Serializable
{
	private String consecutivoPk;
	private String conceptoRetencion;
	private String detConceptoretencion;
	private String indicativoIntegral;
	private String baseMinima;
	private String porcentajeRetInt;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private String activo;
	private String fechaInactivacion;
	private String horaInactivacion;
	private String usuarioInactivacion;
	//Adicionales
	private String descConceptoRetencion;
	
	/**
	 * Constructor
	 */
	public DtoDetVigConRetencion()
	{
		this.reset();
	}
	
	private void reset() 
	{
		this.consecutivoPk="";
		this.conceptoRetencion="";
		this.detConceptoretencion="";
		this.indicativoIntegral=ConstantesBD.acronimoNo;
		this.baseMinima="";
		this.porcentajeRetInt="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.activo="";
		this.fechaInactivacion="";
		this.horaInactivacion="";
		this.usuarioInactivacion="";
		this.descConceptoRetencion="";
	}

	public String getConsecutivoPk() {
		return consecutivoPk;
	}

	public void setConsecutivoPk(String consecutivoPk) {
		this.consecutivoPk = consecutivoPk;
	}

	public String getConceptoRetencion() {
		return conceptoRetencion;
	}

	public void setConceptoRetencion(String conceptoRetencion) {
		this.conceptoRetencion = conceptoRetencion;
	}

	public String getIndicativoIntegral() {
		return indicativoIntegral;
	}

	public void setIndicativoIntegral(String indicativoIntegral) {
		this.indicativoIntegral = indicativoIntegral;
	}

	public String getBaseMinima() {
		return baseMinima;
	}

	public void setBaseMinima(String baseMinima) {
		this.baseMinima = baseMinima;
	}

	public String getPorcentajeRetInt() {
		return porcentajeRetInt;
	}

	public void setPorcentajeRetInt(String porcentajeRetInt) {
		this.porcentajeRetInt = porcentajeRetInt;
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

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getFechaInactivacion() {
		return fechaInactivacion;
	}

	public void setFechaInactivacion(String fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}

	public String getHoraInactivacion() {
		return horaInactivacion;
	}

	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}

	public String getUsuarioInactivacion() {
		return usuarioInactivacion;
	}

	public void setUsuarioInactivacion(String usuarioInactivacion) {
		this.usuarioInactivacion = usuarioInactivacion;
	}

	public String getDetConceptoretencion() {
		return detConceptoretencion;
	}

	public void setDetConceptoretencion(String detConceptoretencion) {
		this.detConceptoretencion = detConceptoretencion;
	}

	public String getDescConceptoRetencion() {
		return descConceptoRetencion;
	}

	public void setDescConceptoRetencion(String descConceptoRetencion) {
		this.descConceptoRetencion = descConceptoRetencion;
	}
	
	
}