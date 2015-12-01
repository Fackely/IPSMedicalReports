package com.princetonsa.dto.facturacion;

import java.io.Serializable;

public class DtoViewFinalidadesServ implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer codigoFinalidad;
	
	private String nombreFinalidad;

	/**
	 * @return the codigoFinalidad
	 */
	public Integer getCodigoFinalidad() {
		return codigoFinalidad;
	}

	/**
	 * @param codigoFinalidad the codigoFinalidad to set
	 */
	public void setCodigoFinalidad(Integer codigoFinalidad) {
		this.codigoFinalidad = codigoFinalidad;
	}

	/**
	 * @return the nombreFinalidad
	 */
	public String getNombreFinalidad() {
		return nombreFinalidad;
	}

	/**
	 * @param nombreFinalidad the nombreFinalidad to set
	 */
	public void setNombreFinalidad(String nombreFinalidad) {
		this.nombreFinalidad = nombreFinalidad;
	}
	
	

}
