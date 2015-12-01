package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

/**
 * Dto para el manejo de los tipos de detalle de las formas de pago
 * @author Juan David Ramírez
 * @since 17 Septiembre 2010
 */
@SuppressWarnings("serial")
public class DtoTipoDetalleFormaPago implements Serializable {
	/**
	 * Llave primaria
	 */
	private int codigo;
	
	/**
	 * Descripción del tipo de detalle
	 */
	private String descripcion;
	
	/**
	 * Prioridad en la que se muestra el tipo de detalle
	 */
	private int prioridad;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
}
