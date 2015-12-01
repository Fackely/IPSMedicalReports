package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;

/**
 * 
 * @author wilson
 *
 */
public class DtoValoracionHospitalizacionEpicrisis implements Serializable 
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
	private DtoValoracionHospitalizacion dtoValHospitalizacion;

	/**
	 * 
	 *
	 */
	public DtoValoracionHospitalizacionEpicrisis() 
	{
		this.dtoPlantilla= new DtoPlantilla();
		this.dtoValHospitalizacion= new DtoValoracionHospitalizacion();
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
	 * @return the dtoValHospitalizacion
	 */
	public DtoValoracionHospitalizacion getDtoValHospitalizacion() {
		return dtoValHospitalizacion;
	}

	/**
	 * @param dtoValHospitalizacion the dtoValHospitalizacion to set
	 */
	public void setDtoValHospitalizacion(
			DtoValoracionHospitalizacion dtoValHospitalizacion) {
		this.dtoValHospitalizacion = dtoValHospitalizacion;
	}

	
}