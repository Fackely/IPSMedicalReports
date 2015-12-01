package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoLogDetVigConRetencion implements Serializable
{
	private String consecutivoPk;
	private String detVigConRetencion;
	private String indicativoIntegral;
	private String baseMinima;
	private String porcentajeRetInt;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	
	/**
	 * Constructor
	 */
	public DtoLogDetVigConRetencion()
	{
		this.reset();
	}
	
	private void reset() 
	{
		this.consecutivoPk="";
		this.detVigConRetencion="";
		this.indicativoIntegral="";
		this.baseMinima="";
		this.porcentajeRetInt="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
	}

	public String getConsecutivoPk() {
		return consecutivoPk;
	}

	public void setConsecutivoPk(String consecutivoPk) {
		this.consecutivoPk = consecutivoPk;
	}

	public String getDetVigConRetencion() {
		return detVigConRetencion;
	}

	public void setDetVigConRetencion(String detVigConRetencion) {
		this.detVigConRetencion = detVigConRetencion;
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
	
	
}