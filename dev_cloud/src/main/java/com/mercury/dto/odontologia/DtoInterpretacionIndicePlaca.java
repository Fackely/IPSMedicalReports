package com.mercury.dto.odontologia;

import java.io.Serializable;

/**
 * Objeto de transferencia para manejar las interpretaciones del indice de placa 
 * @author Juan David Ram&iacute;rez
 * @since 2010/06/28
 *
 */
public class DtoInterpretacionIndicePlaca implements Serializable
{
	int codigoPk;
	
	double porcentajeInicial;

	double porcentajeFinal;
	
	String interpretacion;

	/**
	 * @return Retorna atributo codigoPk
	 */
	public int getCodigoPk()
	{
		return codigoPk;
	}

	/**
	 * @param codigoPk Asigna atributo codigoPk
	 */
	public void setCodigoPk(int codigoPk)
	{
		this.codigoPk = codigoPk;
	}

	/**
	 * @return Retorna atributo porcentajeInicial
	 */
	public double getPorcentajeInicial()
	{
		return porcentajeInicial;
	}

	/**
	 * @param porcentajeInicial Asigna atributo porcentajeInicial
	 */
	public void setPorcentajeInicial(double porcentajeInicial)
	{
		this.porcentajeInicial = porcentajeInicial;
	}

	/**
	 * @return Retorna atributo porcentajeFinal
	 */
	public double getPorcentajeFinal()
	{
		return porcentajeFinal;
	}

	/**
	 * @param porcentajeFinal Asigna atributo porcentajeFinal
	 */
	public void setPorcentajeFinal(double porcentajeFinal)
	{
		this.porcentajeFinal = porcentajeFinal;
	}

	/**
	 * @return Retorna atributo interpretacion
	 */
	public String getInterpretacion()
	{
		return interpretacion;
	}

	/**
	 * @param interpretacion Asigna atributo interpretacion
	 */
	public void setInterpretacion(String interpretacion)
	{
		this.interpretacion = interpretacion;
	}
}
