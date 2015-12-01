package com.princetonsa.dto.ordenes;

import java.io.Serializable;

/***
 * Dto con la informacion  de los articulos de una orden ambulatoria 
 * @author javrammo
 *
 */
public class InfoArticuloOrdenAmbulatoriaDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3256067447815398909L;
	
	/**
	 * codigo Axioma
	 */
	private String codigoAxioma;
	/**
	 * codigo de la interfaz
	 */
	private String codigoInterfaz;
	/**
	 *	cOdigo a mostrar en GUI
	 */
	private String codigoTooltip;	
	/**
	 * descripcion del articulo
	 */
	private String descripion;
	/**
	 * concetracion
	 */
	private String concentracion;
	/**
	 * forma Farmaceutica
	 */
	private String formaFarmaceutica;
	/**
	 * Unidad de Medida
	 */
	private String unidadDeMedida;
	/**
	 * Indica si el Articulo es un medicamento o insumo
	 */
	private boolean medicamento;
	/**
	 * descripciOn completa del tooltip
	 */
	private String descripcionCompletaTooltip;
	
	/**
	 * @return the codigoAxioma
	 */
	public String getCodigoAxioma() {
		return codigoAxioma;
	}
	/**
	 * @param codigoAxioma the codigoAxioma to set
	 */
	public void setCodigoAxioma(String codigoAxioma) {
		this.codigoAxioma = codigoAxioma;
	}
	/**
	 * @return the codigoInterfaz
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}
	/**
	 * @param codigoInterfaz the codigoInterfaz to set
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}
	/**
	 * @return the descripion
	 */
	public String getDescripion() {
		return descripion;
	}
	/**
	 * @param descripion the descripion to set
	 */
	public void setDescripion(String descripion) {
		this.descripion = descripion;
	}
	/**
	 * @return the concentracion
	 */
	public String getConcentracion() {
		return concentracion;
	}
	/**
	 * @param concentracion the concentracion to set
	 */
	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}
	/**
	 * @return the formaFarmaceutica
	 */
	public String getFormaFarmaceutica() {
		return formaFarmaceutica;
	}
	/**
	 * @param formaFarmaceutica the formaFarmaceutica to set
	 */
	public void setFormaFarmaceutica(String formaFarmaceutica) {
		this.formaFarmaceutica = formaFarmaceutica;
	}
	/**
	 * @return the unidadDeMedida
	 */
	public String getUnidadDeMedida() {
		return unidadDeMedida;
	}
	/**
	 * @param unidadDeMedida the unidadDeMedida to set
	 */
	public void setUnidadDeMedida(String unidadDeMedida) {
		this.unidadDeMedida = unidadDeMedida;
	}
	/**
	 * @return the medicamento
	 */
	public boolean isMedicamento() {
		return medicamento;
	}
	/**
	 * @param medicamento the medicamento to set
	 */
	public void setMedicamento(boolean medicamento) {
		this.medicamento = medicamento;
	}
	/**
	 * @return the codigoTooltip
	 */
	public String getCodigoTooltip() {
		return codigoTooltip;
	}
	/**
	 * @param codigoTooltip the codigoTooltip to set
	 */
	public void setCodigoTooltip(String codigoTooltip) {
		this.codigoTooltip = codigoTooltip;
	}
	/**
	 * @return the descripcionCompletaTooltip
	 */
	public String getDescripcionCompletaTooltip() {
		return descripcionCompletaTooltip;
	}
	/**
	 * @param descripcionCompletaTooltip the descripcionCompletaTooltip to set
	 */
	public void setDescripcionCompletaTooltip(String descripcionCompletaTooltip) {
		this.descripcionCompletaTooltip = descripcionCompletaTooltip;
	}	
	
}

