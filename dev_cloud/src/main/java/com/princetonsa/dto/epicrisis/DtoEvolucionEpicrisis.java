package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;

/**
 * 
 * @author wilson
 *
 */
public class DtoEvolucionEpicrisis implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private DtoEvolucion dtoEvolucion;
	
	/**
	 * 
	 */
	private DtoPlantilla dtoPlantilla;
	
	
	/**
	 * 
	 *
	 */
	public DtoEvolucionEpicrisis() 
	{
		this.dtoEvolucion= new DtoEvolucion();
		this.dtoPlantilla= new DtoPlantilla();
	}


	/**
	 * @return the dtoEvolucion
	 */
	public DtoEvolucion getDtoEvolucion() {
		return dtoEvolucion;
	}


	/**
	 * @param dtoEvolucion the dtoEvolucion to set
	 */
	public void setDtoEvolucion(DtoEvolucion dtoEvolucion) {
		this.dtoEvolucion = dtoEvolucion;
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

	
}
