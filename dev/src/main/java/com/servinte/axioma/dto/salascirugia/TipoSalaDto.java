package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class TipoSalaDto implements Serializable {

	private static final long serialVersionUID = -1831921228793862591L;

	/**
	 * Atributo con el codigo del tipo de sala
	 */
	private int codigoTipoSala;
	/**
	 * Atributo con la descripcion del tipo de sala
	 */
	private String descripcionTipoSala;

	/**
	 * Atributo con la institucion a la que pertenece la sala
	 */
	private int institucion;

	public TipoSalaDto(int codigoTipoSala, String descripcionTipoSala, int institucion) {
		this.codigoTipoSala = codigoTipoSala;
		this.descripcionTipoSala = descripcionTipoSala;
		this.institucion = institucion;
	}
	public TipoSalaDto(int codigoTipoSala, int institucion) {
		this.codigoTipoSala = codigoTipoSala;
		this.institucion = institucion;
	}

	public int getCodigoTipoSala() {
		return codigoTipoSala;
	}

	public void setCodigoTipoSala(int codigoTipoSala) {
		this.codigoTipoSala = codigoTipoSala;
	}

	public String getDescripcionTipoSala() {
		return descripcionTipoSala;
	}

	public void setDescripcionTipoSala(String descripcionTipoSala) {
		this.descripcionTipoSala = descripcionTipoSala;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

}
