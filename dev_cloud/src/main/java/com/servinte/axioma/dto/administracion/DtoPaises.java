/**
 * 
 */
package com.servinte.axioma.dto.administracion;

/**
 * @author armando
 *
 */
public class DtoPaises 
{
	
	public DtoPaises(String codigoPais, String descripcionPais) {
		super();
		this.codigoPais = codigoPais;
		this.descripcionPais = descripcionPais;
	}
	
	public DtoPaises() 
	{
		super();
	
	}

	/**
	 * 
	 */
	private String codigoPais;
	
	/**
	 * 
	 */
	private String descripcionPais;

	public String getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getDescripcionPais() {
		return descripcionPais;
	}

	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

}
