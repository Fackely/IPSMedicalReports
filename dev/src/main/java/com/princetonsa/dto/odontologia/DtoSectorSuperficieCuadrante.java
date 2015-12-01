package com.princetonsa.dto.odontologia;

import java.io.Serializable;

public class DtoSectorSuperficieCuadrante implements Serializable, Cloneable
{
	/**
	 * Versi&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Llave primaria
	 */
	private int codigoPk;
	
	/**
	 * Superficie a la que aplica
	 */
	private DtoSuperficieDental superficie;
	
	/**
	 * Sector al que se relaciona la superficie
	 */
	private int sector;
	
	/**
	 * Pieza que contiene la superficie
	 */
	private int pieza;

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	/**
	 * @return Retorna el atributo codigoPk
	 */
	public int getCodigoPk()
	{
		return codigoPk;
	}

	/**
	 * @param codigoPk Asigna el atributo codigoPk
	 */
	public void setCodigoPk(int codigoPk)
	{
		this.codigoPk = codigoPk;
	}

	/**
	 * @return Retorna el atributo superficie
	 */
	public DtoSuperficieDental getSuperficie()
	{
		return superficie;
	}

	/**
	 * @param superficie Asigna el atributo superficie
	 */
	public void setSuperficie(DtoSuperficieDental superficie)
	{
		this.superficie = superficie;
	}

	/**
	 * @return Retorna el atributo sector
	 */
	public int getSector()
	{
		return sector;
	}

	/**
	 * @param sector Asigna el atributo sector
	 */
	public void setSector(int sector)
	{
		this.sector = sector;
	}

	/**
	 * @return Retorna el atributo pieza
	 */
	public int getPieza()
	{
		return pieza;
	}

	/**
	 * @param pieza Asigna el atributo pieza
	 */
	public void setPieza(int pieza)
	{
		this.pieza = pieza;
	}
}
