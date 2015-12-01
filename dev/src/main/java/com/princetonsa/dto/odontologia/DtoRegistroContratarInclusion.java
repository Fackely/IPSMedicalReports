
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * Clase que contiene cada uno de los registros seleccionados y cada uno
 * de los atributos necesarios para la contratación de inclusiones
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class DtoRegistroContratarInclusion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Programa / servicio disponible para contratar
	 */
	private DtoPresuOdoProgServ programaServicio;
	
	/**
	 * Listado con la información de los convenios para ese Programa / Servicio
	 */
	private ArrayList<DtoPresupuestoOdoConvenio> listPresupuestoOdoConvenio = new ArrayList<DtoPresupuestoOdoConvenio>();  
	
	/**
	 * Atributo que indica si la inclusión fue contratada
	 */
	private boolean inclusionContratada;
	
	
	/**
	 * Atributo que indica si la inclusión si se va a contratar en el presupuesto
	 */
	private boolean inclusionParaContratar;
	
	
	/**
	 * Código de la inclusion registrada
	 */
	private double codigoInclusion;
	
	/**
	 * 
	 */
	public DtoRegistroContratarInclusion() {
		
		programaServicio = new DtoPresuOdoProgServ();
		listPresupuestoOdoConvenio =  new ArrayList<DtoPresupuestoOdoConvenio>();
		inclusionContratada = false;
		codigoInclusion = ConstantesBD.codigoNuncaValidoDouble;
		
		inclusionParaContratar = false;
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
	 * @return the listPresupuestoOdoConvenio
	 */
	public ArrayList<DtoPresupuestoOdoConvenio> getListPresupuestoOdoConvenio() {
		return listPresupuestoOdoConvenio;
	}


	/**
	 * @param listPresupuestoOdoConvenio the listPresupuestoOdoConvenio to set
	 */
	public void setListPresupuestoOdoConvenio(
			ArrayList<DtoPresupuestoOdoConvenio> listPresupuestoOdoConvenio) {
		this.listPresupuestoOdoConvenio = listPresupuestoOdoConvenio;
	}

	/**
	 * @param inclusionContratada the inclusionContratada to set
	 */
	public void setInclusionContratada(boolean inclusionContratada) {
		this.inclusionContratada = inclusionContratada;
	}

	/**
	 * @return the inclusionContratada
	 */
	public boolean isInclusionContratada() {
		return inclusionContratada;
	}

	/**
	 * @param codigoInclusion the codigoInclusion to set
	 */
	public void setCodigoInclusion(double codigoInclusion) {
		this.codigoInclusion = codigoInclusion;
	}

	/**
	 * @return the codigoInclusion
	 */
	public double getCodigoInclusion() {
		return codigoInclusion;
	}

	/**
	 * @param inclusionParaContratar the inclusionParaContratar to set
	 */
	public void setInclusionParaContratar(boolean inclusionParaContratar) {
		this.inclusionParaContratar = inclusionParaContratar;
	}

	/**
	 * @return the inclusionParaContratar
	 */
	public boolean isInclusionParaContratar() {
		return inclusionParaContratar;
	}
	
}