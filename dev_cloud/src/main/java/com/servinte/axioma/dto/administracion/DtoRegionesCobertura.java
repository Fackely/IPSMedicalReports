/**
 * 
 */
package com.servinte.axioma.dto.administracion;

/**
 * @author armando
 *
 */
public class DtoRegionesCobertura 
{
	/**
	 * 
	 */
	private int codigoRegionCobertura;
	
	/**
	 * 
	 */
	private String descripcionRegionCebertura;
	
	/**
	 * 
	 */
	private int codigoInstitucion;

	public int getCodigoRegionCobertura() {
		return codigoRegionCobertura;
	}

	public void setCodigoRegionCobertura(int codigoRegionCobertura) {
		this.codigoRegionCobertura = codigoRegionCobertura;
	}

	public String getDescripcionRegionCebertura() {
		return descripcionRegionCebertura;
	}

	public void setDescripcionRegionCebertura(String descripcionRegionCebertura) {
		this.descripcionRegionCebertura = descripcionRegionCebertura;
	}

	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

}
