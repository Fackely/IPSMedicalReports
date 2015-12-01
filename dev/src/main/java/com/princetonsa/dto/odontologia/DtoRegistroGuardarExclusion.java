
package com.princetonsa.dto.odontologia;

import util.ConstantesBD;

/**
 * Clase que contiene cada uno de los registros seleccionados y cada uno
 * de los atributos necesarios para guardar las exclusiones
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class DtoRegistroGuardarExclusion {

	/**
	 * Programa / servicio asociado a la exclusión
	 */
	private DtoPresuOdoProgServ programaServicio;
	
	
	/**
	 * Código de la exclusion registrada
	 */
	private double codigoExclusion;
	
	/**
	 * Exclusión registrada en el proceso
	 */
	private DtoExclusionPresupuesto exclusionPresupuesto;
	
	/**
	 * Indica si la exclusión ha sido registrada correctamente.
	 * 
	 */
	private boolean exclusionRegistrada;
	
	/**
	 * 
	 */
	public DtoRegistroGuardarExclusion() {
		
		this.programaServicio = new DtoPresuOdoProgServ();
		this.setCodigoExclusion(ConstantesBD.codigoNuncaValidoDouble);
		this.exclusionPresupuesto = new DtoExclusionPresupuesto();
		this.exclusionRegistrada = false;
	}

	/**
	 * @return the programaServicio
	 */
	public DtoPresuOdoProgServ getProgramaServicio() {
		return programaServicio;
	}


	/**
	 * @param programaServicio the programaServicio to set
	 */
	public void setProgramaServicio(DtoPresuOdoProgServ programaServicio) {
		this.programaServicio = programaServicio;
	}


	/**
	 * @param codigoExclusion the codigoExclusion to set
	 */
	public void setCodigoExclusion(double codigoExclusion) {
		this.codigoExclusion = codigoExclusion;
	}

	/**
	 * @return the codigoExclusion
	 */
	public double getCodigoExclusion() {
		return codigoExclusion;
	}

	/**
	 * @param exclusionPresupuesto the exclusionPresupuesto to set
	 */
	public void setExclusionPresupuesto(DtoExclusionPresupuesto exclusionPresupuesto) {
		this.exclusionPresupuesto = exclusionPresupuesto;
	}

	/**
	 * @return the exclusionPresupuesto
	 */
	public DtoExclusionPresupuesto getExclusionPresupuesto() {
		return exclusionPresupuesto;
	}

	/**
	 * @param exclusionRegistrada the exclusionRegistrada to set
	 */
	public void setExclusionRegistrada(boolean exclusionRegistrada) {
		this.exclusionRegistrada = exclusionRegistrada;
	}

	/**
	 * @return the exclusionRegistrada
	 */
	public boolean isExclusionRegistrada() {
		return exclusionRegistrada;
	}
	
}