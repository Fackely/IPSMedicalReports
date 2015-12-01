package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;

public class DtoServiciosCitas implements Serializable{
	
	private String servicio;
	
	private String convenio;
	
	public void reset()
	{
		this.setConvenio(new String(""));
		this.setServicio(new String(""));
	}

	/**
	 * @return the servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
}