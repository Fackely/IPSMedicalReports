package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

/**
 * Dto para el manejo de los seriales de los bonos ingresados
 * en los recibos de caja
 * @author Juan David Ram&iacute;rez
 * @since
 */
public class DtoBonoSerialValor implements Serializable
{
	/**
	 * Valor del bono
	 */
	private double valor;
	
	/**
	 * Serial del bono
	 */
	private String serial;

	/**
	 * @return Retorna atributo valor
	 */
	public double getValor()
	{
		return valor;
	}

	/**
	 * @param valor Asigna atributo valor
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}

	/**
	 * @return Retorna atributo serial
	 */
	public String getSerial()
	{
		return serial;
	}

	/**
	 * @param serial Asigna atributo serial
	 */
	public void setSerial(String serial)
	{
		this.serial = serial;
	}
}
