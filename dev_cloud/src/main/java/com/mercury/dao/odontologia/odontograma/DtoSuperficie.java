package com.mercury.dao.odontologia.odontograma;

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

	private String codigoPk;
	
	private String nombre;
	
	public DtoSuperficie clone() throws CloneNotSupportedException
	{
		return (DtoSuperficie) this.clone();
	}
	
}
