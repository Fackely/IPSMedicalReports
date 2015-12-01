/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;



/**
 * Dto que guarda la la información de los niveles de atención
 * 
 * @author Ricardo Ruiz
 */
public class DtoNivelAtencion implements Serializable, Comparable<DtoNivelAtencion>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8316440189010730483L;

	/**
	 * Atributo que representa el consetutivo del nivel de atención
	 */
	private long consecutivo;
	
	/**
	 * Atributo que representa la descripción o nombre del nivel de atención
	 */
	private String descripcion;
	
	/**
	 * Atributo que representa el codigo del nivel de atención
	 */
	private Long codigo;
	
	public DtoNivelAtencion(){
		
	}

	/**
	 * @return the consecutivo
	 */
	public long getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
	    hash += (this.codigo != null ? this.codigo.hashCode() : 0);
	    return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj != null && obj instanceof DtoNivelAtencion){
			if(((DtoNivelAtencion)obj).getConsecutivo()==this.consecutivo){
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(DtoNivelAtencion o) {
	    return this.descripcion.compareToIgnoreCase(o.descripcion);   
	}
	
}
