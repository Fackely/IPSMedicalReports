package com.princetonsa.dto.facturacion;

import com.servinte.axioma.orm.AutorizacionConvIngPac;

/**
 * Dto de AutorizacionConvIngPac
 * @author Cristhian Murillo
 */
public class DtoAutorizacionConvIngPac extends AutorizacionConvIngPac  
{

	private static final long serialVersionUID = 1L;
	private String tipoMedioAuto;
	private String observacionesCambio;

	public DtoAutorizacionConvIngPac() {
	}


	public String getTipoMedioAuto() {
		return this.tipoMedioAuto;
	}

	public void setTipoMedioAuto(String tipoMedioAuto) {
		this.tipoMedioAuto = tipoMedioAuto;
	}

	public String getObservacionesCambio() {
		return this.observacionesCambio;
	}

	public void setObservacionesCambio(String observacionesCambio) {
		this.observacionesCambio = observacionesCambio;
	}

}
