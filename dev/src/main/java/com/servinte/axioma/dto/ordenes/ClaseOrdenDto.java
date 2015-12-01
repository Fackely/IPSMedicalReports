package com.servinte.axioma.dto.ordenes;

import java.io.Serializable;

/**
 * Dto Necesario para mapear las clases de ordenes
 * para los filtros de búsqueda
 * 
 * @author ricruico
 * @version 1.0
 * @created 05-jul-2012 02:24:01 p.m.
 */
public class ClaseOrdenDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8013303157581908117L;
	
	/**
	 * Atributo que representa el código de la clase de orden
	 */
	private int codigo;
	
	/**
	 * Atributo que representa la descripción de la clase de orden
	 */
	private String descripcion;
	
	public ClaseOrdenDto(){
		
	}

	/**
	 * Constructor para setear los valores de la clase
	 * 
	 * @param codigo
	 * @param descripcion
	 */
	public ClaseOrdenDto(int codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
