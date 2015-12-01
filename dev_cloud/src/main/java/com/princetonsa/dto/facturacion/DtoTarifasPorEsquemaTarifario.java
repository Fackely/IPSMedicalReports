package com.princetonsa.dto.facturacion;

import java.io.Serializable;

/**
 * Esta clase se encarga de determinar las tarfias para un esquema tarifario.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 23/11/2010
 */
public class DtoTarifasPorEsquemaTarifario implements Serializable {

	private static final long serialVersionUID = 1L;
	private int codigoEsquemaTarifario;
	private long codigoPrograma;
	private Double valorTarifa;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * codigoEsquemaTarifario.
	 * 
	 * @return codigoEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoEsquemaTarifario() {
		return codigoEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEsquemaTarifario.
	 * 
	 * @param codigoEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario) {
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoPrograma.
	 * 
	 * @return codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoPrograma.
	 * 
	 * @param codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valorTarifa.
	 * 
	 * @return valorTarifa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getValorTarifa() {
		return valorTarifa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo valorTarifa.
	 * 
	 * @param valorTarifa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValorTarifa(Double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

}
