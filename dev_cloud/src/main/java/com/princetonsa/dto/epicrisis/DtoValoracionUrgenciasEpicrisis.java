package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;

/**
 * 
 * @author axioma
 *
 */
public class DtoValoracionUrgenciasEpicrisis implements Serializable 
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
	private DtoValoracionUrgencias dtoValUrgencias;

	/**
	 * 
	 */
	public DtoValoracionUrgenciasEpicrisis() 
	{
		this.dtoPlantilla = new DtoPlantilla();
		this.dtoValUrgencias = new DtoValoracionUrgencias();
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
	 * @return the dtoValUrgencias
	 */
	public DtoValoracionUrgencias getDtoValUrgencias() {
		return dtoValUrgencias;
	}

	/**
	 * @param dtoValUrgencias the dtoValUrgencias to set
	 */
	public void setDtoValUrgencias(DtoValoracionUrgencias dtoValUrgencias) {
		this.dtoValUrgencias = dtoValUrgencias;
	}
}