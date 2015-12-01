package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class EstadoSolicitudDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3447256249431989185L;

	private int codigo;
	private String nombre;
	
	public EstadoSolicitudDto() {
	}
	public EstadoSolicitudDto(int codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
