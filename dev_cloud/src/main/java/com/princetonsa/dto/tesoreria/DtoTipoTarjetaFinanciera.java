package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

/**
 * Dto para la transferencia de datos de tarjetas financieras
 * @author Juan David Ramírez
 * @since 18 Septiembre 2010
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class DtoTipoTarjetaFinanciera implements Serializable{
	private int codigoPk;
	private String descripcion;
	public int getCodigoPk() {
		return codigoPk;
	}
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
