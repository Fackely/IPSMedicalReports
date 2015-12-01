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
public class SalaCirugiaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5202774036273526138L;
	
	/**
	 * Atributo con el consecutivo de la sala
	 */
	private int consecutivoSala;
	/**
	 * Atributo con el codigo de la sala 
	 */
	private int codigoSala;
	/**
	 * Atributo con la descripcion de la sala
	 */
	private String descripcionSala;
	/**
	 * Atributo con Institucion de la sala 
	 */
	private int institucion;
	/**
	 * Atributo con el centro de atencion al que pertenece la sala
	 */
	private int centroAtencion;
	
	
	
	public SalaCirugiaDto(int consecutivoSala, int codigoSala, String descripcionSala, int institucion, int centroAtencion) {

		this.consecutivoSala = consecutivoSala;
		this.codigoSala = codigoSala;
		this.descripcionSala = descripcionSala;
		this.institucion = institucion;
		this.centroAtencion = centroAtencion;
	}
	public int getConsecutivoSala() {
		return consecutivoSala;
	}
	public void setConsecutivoSala(int consecutivoSala) {
		this.consecutivoSala = consecutivoSala;
	}
	public int getCodigoSala() {
		return codigoSala;
	}
	public void setCodigoSala(int codigoSala) {
		this.codigoSala = codigoSala;
	}
	public String getDescripcionSala() {
		return descripcionSala;
	}
	public void setDescripcionSala(String descripcionSala) {
		this.descripcionSala = descripcionSala;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

}
