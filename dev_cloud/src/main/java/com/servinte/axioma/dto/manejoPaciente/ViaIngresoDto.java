package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;

/**
 * Dto para guardar las vias de ingreso
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class ViaIngresoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2245868989151049636L;
	
	/**
	 * Atributo que representa el codigo de la via de ingreso
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el nombre de la via de ingreso
	 */
	private String nombre;

	public ViaIngresoDto(){
		
	}
	
	/**
	 * Constructor para mapear el resultado de la consulta de vias de ingreso
	 * @param codigo
	 * @param nombre
	 */
	public ViaIngresoDto(int codigo, String nombre){
		this.codigo=codigo;
		this.nombre=nombre;
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
}