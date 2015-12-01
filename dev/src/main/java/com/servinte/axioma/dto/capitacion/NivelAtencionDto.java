package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;

/**
 * Dto para mapear los atributos de los niveles de atención
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class NivelAtencionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6779439631334316599L;

	/**
	 * Atributo que representa el codigoPK del nivel de atención
	 */
	private Long consecutivo;
	
	/**
	 * Atributo que representa el codigo para mostrar del nivel de atención
	 */
	private Long codigo;
	
	/**
	 * Atributo que representa la descripción del nivel de atención
	 */
	private String descripcion;
	
	
	public NivelAtencionDto(){

	}

	/**
	 * Constructor necesario para mapear la consulta de Niveles de Atencion
	 * de CatalogoCapitacionDelegate
	 * 
	 * @param consecutivo
	 * @param descripcion
	 */
	public NivelAtencionDto(Long consecutivo, String descripcion){
		this.consecutivo=consecutivo;
		this.descripcion=descripcion;
	}

	/**
	 * @return the consecutivo
	 */
	public Long getConsecutivo() {
		return consecutivo;
	}


	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}


	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}


	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
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