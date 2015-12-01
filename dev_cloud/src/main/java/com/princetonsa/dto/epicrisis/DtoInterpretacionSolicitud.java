package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class DtoInterpretacionSolicitud implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String interpretacion;
	
	/**
	 * 
	 */
	private String medicoInterpreta;
	
	/**
	 * 
	 */
	private String horaInterpretacion;
	
	/**
	 * 
	 */
	private String fechaInterpretacion;

	/**
	 * 
	 */
	private boolean enviadaEpicrisis;
	
	/**
	 * 
	 * @param interpretacion
	 * @param medicoInterpreta
	 * @param horaInterpretacion
	 * @param fechaInterpretacion
	 */
	public DtoInterpretacionSolicitud() 
	{
		this.interpretacion = "";
		this.medicoInterpreta = "";
		this.horaInterpretacion = "";
		this.fechaInterpretacion = "";
		this.enviadaEpicrisis=false;
	}

	/**
	 * @return the fechaInterpretacion
	 */
	public String getFechaInterpretacion() {
		return fechaInterpretacion;
	}

	/**
	 * @param fechaInterpretacion the fechaInterpretacion to set
	 */
	public void setFechaInterpretacion(String fechaInterpretacion) {
		this.fechaInterpretacion = fechaInterpretacion;
	}

	/**
	 * @return the horaInterpretacion
	 */
	public String getHoraInterpretacion() {
		return horaInterpretacion;
	}

	/**
	 * @param horaInterpretacion the horaInterpretacion to set
	 */
	public void setHoraInterpretacion(String horaInterpretacion) {
		this.horaInterpretacion = horaInterpretacion;
	}

	/**
	 * @return the interpretacion
	 */
	public String getInterpretacion() {
		return interpretacion;
	}

	/**
	 * @param interpretacion the interpretacion to set
	 */
	public void setInterpretacion(String interpretacion) {
		this.interpretacion = interpretacion;
	}

	/**
	 * @return the medicoInterpreta
	 */
	public String getMedicoInterpreta() {
		return medicoInterpreta;
	}

	/**
	 * @param medicoInterpreta the medicoInterpreta to set
	 */
	public void setMedicoInterpreta(String medicoInterpreta) {
		this.medicoInterpreta = medicoInterpreta;
	}

	/**
	 * @return the enviadaEpicrisis
	 */
	public boolean isEnviadaEpicrisis() {
		return enviadaEpicrisis;
	}

	/**
	 * @param enviadaEpicrisis the enviadaEpicrisis to set
	 */
	public void setEnviadaEpicrisis(boolean enviadaEpicrisis) {
		this.enviadaEpicrisis = enviadaEpicrisis;
	}
	
	/**
	 * @return the enviadaEpicrisis
	 */
	public boolean getEnviadaEpicrisis() {
		return enviadaEpicrisis;
	}

}
