package com.mercury.dto.odontologia;

import java.io.Serializable;

/**
 * 
 * @author Juan David Ram&iacute;rez
 * @since 29 Mayo 2010
 */
public class DtoSuperficie implements Serializable, Cloneable
{

	/**
	 * Versi&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;

	private int codigoPk;
	
	private String nombre;
	
	public DtoSuperficie clone() throws CloneNotSupportedException
	{
		return (DtoSuperficie) this.clone();
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
