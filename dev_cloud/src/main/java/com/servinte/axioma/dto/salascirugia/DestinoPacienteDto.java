/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class DestinoPacienteDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021037831297588602L;
	/**
	 * Atributo con el codigo del destino del paciente en hoja quirurgica
	 */
	private int codigoDestino;
	/**
	 *  Atributo con la descripcion del destino del paciente en hoja quirurgica
	 */
	private String descripcionDestino;
	

	public DestinoPacienteDto(int codigoDestino, String descripcionDestino) {
		this.codigoDestino = codigoDestino;
		this.descripcionDestino = descripcionDestino;
	}
	public int getCodigoDestino() {
		return codigoDestino;
	}
	public void setCodigoDestino(int codigoDestino) {
		this.codigoDestino = codigoDestino;
	}
	public String getDescripcionDestino() {
		return descripcionDestino;
	}
	public void setDescripcionDestino(String descripcionDestino) {
		this.descripcionDestino = descripcionDestino;
	}
}
