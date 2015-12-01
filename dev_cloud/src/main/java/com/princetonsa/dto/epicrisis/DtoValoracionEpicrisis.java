package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;

/**
 * 
 * @author wilson
 *
 */
public class DtoValoracionEpicrisis implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private DtoPlantilla dtoPlantilla;
	
	/**
	 * 
	 */
	private DtoValoracion dtoValoracion;

	/**
	 * 
	 */
	private String numeroAutorizacion;
	
	/**
	 * 
	 */
	private String justificacionNoPos;
	
	/**
	 * 
	 */
	private DtoInterpretacionSolicitud dtoInterpretacion;
	
	/**
	 * 
	 *
	 */
	public DtoValoracionEpicrisis() 
	{
		this.dtoPlantilla=new DtoPlantilla();
		this.dtoValoracion=new DtoValoracion();
		this.numeroAutorizacion="";
		this.dtoInterpretacion=new DtoInterpretacionSolicitud();
		this.justificacionNoPos="";
	}

	/**
	 * @return the dtoPlantilla
	 */
	public DtoPlantilla getDtoPlantilla() {
		return dtoPlantilla;
	}

	/**
	 * @param dtoPlantilla the dtoPlantilla to set
	 */
	public void setDtoPlantilla(DtoPlantilla dtoPlantilla) {
		this.dtoPlantilla = dtoPlantilla;
	}

	/**
	 * @return the dtoValoracion
	 */
	public DtoValoracion getDtoValoracion() {
		return dtoValoracion;
	}

	/**
	 * @param dtoValoracion the dtoValoracion to set
	 */
	public void setDtoValoracion(DtoValoracion dtoValoracion) {
		this.dtoValoracion = dtoValoracion;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	/**
	 * @return the dtoInterpretacion
	 */
	public DtoInterpretacionSolicitud getDtoInterpretacion() {
		return dtoInterpretacion;
	}

	/**
	 * @param dtoInterpretacion the dtoInterpretacion to set
	 */
	public void setDtoInterpretacion(DtoInterpretacionSolicitud dtoInterpretacion) {
		this.dtoInterpretacion = dtoInterpretacion;
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
	
}