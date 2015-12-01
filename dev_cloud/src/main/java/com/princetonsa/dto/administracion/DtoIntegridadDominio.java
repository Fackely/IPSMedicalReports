package com.princetonsa.dto.administracion;

import java.io.Serializable;

public class DtoIntegridadDominio implements Serializable{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private String acronimo;
	
	private String descripcion;

	public DtoIntegridadDominio(String acronimo) {
		this.acronimo=acronimo;
	}
	
	public DtoIntegridadDominio() {
	}
	
	/**
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}
	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}
	/**
	 * @return the nombre
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setDescripcion(String nombre) {
		this.descripcion = nombre;
	}
}
