package com.servinte.axioma.dto.administracion;

import java.io.Serializable;

/**
 * Clase que mapea los atributos de los centros de costo
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class CentroCostoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4648975001821848072L;

	/**
	 * Atributo que representas el codigoPK del centro de costo
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el nombre del centro de costo
	 */
	private String nombre;
	
	/**
	 * Atributo que representa el tipo de entidad que ejecuta del centro de costo
	 */
	private String tipoEntidadEjecuta;
	
	/**
	 * Constructor de la clase 
	 */
	public CentroCostoDto(){

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
	 * @return the tipoEntidadEjecuta
	 */
	public String getTipoEntidadEjecuta() {
		return tipoEntidadEjecuta;
	}

	/**
	 * @param tipoEntidadEjecuta the tipoEntidadEjecuta to set
	 */
	public void setTipoEntidadEjecuta(String tipoEntidadEjecuta) {
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}

	
}