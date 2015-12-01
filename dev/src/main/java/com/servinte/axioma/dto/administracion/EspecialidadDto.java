package com.servinte.axioma.dto.administracion;

import java.io.Serializable;

/**
 * Clase que mapea los atributos de las Especialidades
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class EspecialidadDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5623606785450704795L;
	
	/**
	 * Atributo que representa el codigoPK de las especialidades
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el nombre de las especialidades
	 */
	private String nombre;
	
	/**
	 * Atributo que representa el consecutivo a mostrar de las especialidades
	 */
	private String conseutivo;
	
	/**
	 * Constructor de la clase
	 */
	public EspecialidadDto(){
		
	}

	/**
	 * Constructor para mapear la consulta de especialidades válidas
	 * 
	 * @param codigo
	 * @param nombre
	 * @param conseutivo
	 */
	public EspecialidadDto(int codigo, String nombre, String conseutivo) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.conseutivo = conseutivo;
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

	/**
	 * @return the conseutivo
	 */
	public String getConseutivo() {
		return conseutivo;
	}

	/**
	 * @param conseutivo the conseutivo to set
	 */
	public void setConseutivo(String conseutivo) {
		this.conseutivo = conseutivo;
	}

}
