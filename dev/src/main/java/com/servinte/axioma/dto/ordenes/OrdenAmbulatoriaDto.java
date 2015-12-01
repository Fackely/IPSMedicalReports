package com.servinte.axioma.dto.ordenes;

/**
 * Dto que guarda informacion de ordenes ambulatorias 
 * @author jeilones
 * @created 17/08/2012
 *
 */
public class OrdenAmbulatoriaDto {

	private Byte codigoEstado;
	private String nombreEstado;
	/**
	 * @param codigoEstado
	 * @param nombreEstado
	 * @author jeilones
	 * @created 17/08/2012
	 */
	public OrdenAmbulatoriaDto(Byte codigoEstado, String nombreEstado) {
		super();
		this.codigoEstado = codigoEstado;
		this.nombreEstado = nombreEstado;
	}
	/**
	 * @return the codigoEstado
	 */
	public Byte getCodigoEstado() {
		return codigoEstado;
	}
	/**
	 * @param codigoEstado the codigoEstado to set
	 */
	public void setCodigoEstado(Byte codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	/**
	 * @return the nombreEstado
	 */
	public String getNombreEstado() {
		return nombreEstado;
	}
	/**
	 * @param nombreEstado the nombreEstado to set
	 */
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
	
	
}
