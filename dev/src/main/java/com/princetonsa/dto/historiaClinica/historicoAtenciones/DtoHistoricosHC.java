/**
 * 
 */
package com.princetonsa.dto.historiaClinica.historicoAtenciones;

import java.io.Serializable;
import java.util.ArrayList;

import util.InfoDatosInt;

/**
 * @author axioma
 *
 */
public class DtoHistoricosHC implements Serializable
{
	/**
	 * 
	 */
	private int codigoPaciente;
	
	/**
	 * 
	 */
	private int consecutivoHC;

	
	/**
	 * 
	 */
	private InfoDatosInt viaIngreso;
	
	/**
	 * 
	 */
	private String fechaAtencion;
	
	/**
	 * 
	 */
	private ArrayList<DtoSeccionesHC> secciones;

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the consecutivoHC
	 */
	public int getConsecutivoHC() {
		return consecutivoHC;
	}

	/**
	 * @param consecutivoHC the consecutivoHC to set
	 */
	public void setConsecutivoHC(int consecutivoHC) {
		this.consecutivoHC = consecutivoHC;
	}

	/**
	 * @return the viaIngreso
	 */
	public InfoDatosInt getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the secciones
	 */
	public ArrayList<DtoSeccionesHC> getSecciones() {
		return secciones;
	}

	/**
	 * @param secciones the secciones to set
	 */
	public void setSecciones(ArrayList<DtoSeccionesHC> secciones) {
		this.secciones = secciones;
	}

	public String getFechaAtencion() {
		return fechaAtencion;
	}

	public void setFechaAtencion(String fechaAtencion) {
		this.fechaAtencion = fechaAtencion;
	}
	
}
