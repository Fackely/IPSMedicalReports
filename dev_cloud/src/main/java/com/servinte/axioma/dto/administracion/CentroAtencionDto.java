package com.servinte.axioma.dto.administracion;

import java.io.Serializable;

/**
 * Clase que mapea los atributos de los centros de atención
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class CentroAtencionDto implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3054091905685160693L;

	/**
	 * Atributo que representas el codigoPK del centro de costo
	 */
	private int codigo;
	
	/**
	 * Atributo que representas el codigo a mostrar del centro de costo
	 */
	private String codigoMostrar;
	
	/**
	 * Atributo que representa el nombre del centro de costo
	 */
	private String nombre;
	
	
	/**
	 * Contructor de la clase 
	 */
	public CentroAtencionDto(){

	}

	/**
	 * Constructor de la clase para mapear el resultado de la consulta de
	 * Centro de Atención Asignado al Paciente
	 * 
	 * @param codigo
	 * @param codigoMostrar
	 * @param nombre
	 */
	public CentroAtencionDto(int codigo, String codigoMostrar, String nombre) {
		this.codigo = codigo;
		this.codigoMostrar = codigoMostrar;
		this.nombre = nombre;
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
	 * @return the codigoMostrar
	 */
	public String getCodigoMostrar() {
		return codigoMostrar;
	}


	/**
	 * @param codigoMostrar the codigoMostrar to set
	 */
	public void setCodigoMostrar(String codigoMostrar) {
		this.codigoMostrar = codigoMostrar;
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