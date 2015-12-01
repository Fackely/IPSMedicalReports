package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DtoConceptoFacturaVaria implements Serializable{

	private String descripcion;
	
	private String codigo;
	
	/**
	 * Llave primaria
	 */
	private Long consecutivo;

	public void resset() {
		this.setConsecutivo(null);
		this.setCodigo("");
		this.setDescripcion("");
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	public Long getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}
}