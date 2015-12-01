package com.mercury.dto.odontologia;

import java.io.Serializable;

/**
 * 
 * @author Juan David Ram&iacute;rez
 * @since 29 Mayo 2010
 */
public class DtoPiezaDental implements Serializable, Cloneable
{

	/**
	 * Versi&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Llave primaria de la pieza dental
	 */
	private int codigoPk;
	
	/**
	 * Nombre de la pieza dental
	 */
	private String nombre;
	
	/**
	 * Clonar el objeto
	 */
	public DtoPiezaDental clone() throws CloneNotSupportedException
	{
		return (DtoPiezaDental) super.clone();
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
