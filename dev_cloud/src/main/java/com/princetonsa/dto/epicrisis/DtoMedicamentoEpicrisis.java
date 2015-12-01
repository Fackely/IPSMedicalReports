package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class DtoMedicamentoEpicrisis implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private String descripcionMedicamento;
	
	/**
	 * 
	 */
	private String dosis;
	
	/**
	 * 
	 */
	private String frecuencia;
	
	/**
	 * 
	 */
	private String via;
	
	/**
	 * 
	 */
	private String dosisAdmin;
	
	/**
	 * 
	 */
	private String unidadesConsumidas;

	/**
	 * 
	 */
	private String justificacionNoPos;
	
	/**
	 * 
	 */
	private String adelantoXNecesidad;
	
	/**
	 * 
	 */
	private String nadaViaOral;
	
	/**
	 * 
	 */
	private String usuarioRechazo;
	
	/**
	 * 
	 *
	 */
	public DtoMedicamentoEpicrisis() 
	{
		this.descripcionMedicamento="";
		this.dosis="";
		this.frecuencia="";
		this.via="";
		this.dosisAdmin="";
		this.unidadesConsumidas="";
		this.justificacionNoPos="";
		this.adelantoXNecesidad="";
		this.nadaViaOral="";
		this.usuarioRechazo="";
	}

	/**
	 * @return the descripcionMedicamento
	 */
	public String getDescripcionMedicamento() {
		return descripcionMedicamento;
	}

	/**
	 * @param descripcionMedicamento the descripcionMedicamento to set
	 */
	public void setDescripcionMedicamento(String descripcionMedicamento) {
		this.descripcionMedicamento = descripcionMedicamento;
	}

	/**
	 * @return the dosis
	 */
	public String getDosis() {
		return dosis;
	}

	/**
	 * @param dosis the dosis to set
	 */
	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	/**
	 * @return the dosisAdmin
	 */
	public String getDosisAdmin() {
		return dosisAdmin;
	}

	/**
	 * @param dosisAdmin the dosisAdmin to set
	 */
	public void setDosisAdmin(String dosisAdmin) {
		this.dosisAdmin = dosisAdmin;
	}

	/**
	 * @return the frecuencia
	 */
	public String getFrecuencia() {
		return frecuencia;
	}

	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	/**
	 * @return the unidadesConsumidas
	 */
	public String getUnidadesConsumidas() {
		return unidadesConsumidas;
	}

	/**
	 * @param unidadesConsumidas the unidadesConsumidas to set
	 */
	public void setUnidadesConsumidas(String unidadesConsumidas) {
		this.unidadesConsumidas = unidadesConsumidas;
	}

	/**
	 * @return the via
	 */
	public String getVia() {
		return via;
	}

	/**
	 * @param via the via to set
	 */
	public void setVia(String via) {
		this.via = via;
	}

	/**
	 * @return the justificacionNoPos
	 */
	public String getJustificacionNoPos() {
		return justificacionNoPos;
	}

	/**
	 * @param justificacionNoPos the justificacionNoPos to set
	 */
	public void setJustificacionNoPos(String justificacionNoPos) {
		this.justificacionNoPos = justificacionNoPos;
	}

	/**
	 * @return the adelantoXNecesidad
	 */
	public String getAdelantoXNecesidad() {
		return adelantoXNecesidad;
	}

	/**
	 * @param adelantoXNecesidad the adelantoXNecesidad to set
	 */
	public void setAdelantoXNecesidad(String adelantoXNecesidad) {
		this.adelantoXNecesidad = adelantoXNecesidad;
	}

	/**
	 * @return the nadaViaOral
	 */
	public String getNadaViaOral() {
		return nadaViaOral;
	}

	/**
	 * @param nadaViaOral the nadaViaOral to set
	 */
	public void setNadaViaOral(String nadaViaOral) {
		this.nadaViaOral = nadaViaOral;
	}

	/**
	 * @return the usuarioRechazo
	 */
	public String getUsuarioRechazo() {
		return usuarioRechazo;
	}

	/**
	 * @param usuarioRechazo the usuarioRechazo to set
	 */
	public void setUsuarioRechazo(String usuarioRechazo) {
		this.usuarioRechazo = usuarioRechazo;
	}
	
	
}