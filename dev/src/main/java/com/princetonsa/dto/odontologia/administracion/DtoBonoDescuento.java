/**
 * 
 */
package com.princetonsa.dto.odontologia.administracion;

import java.io.Serializable;
import java.math.BigInteger;

import com.princetonsa.dto.administracion.DtoPersonas;

/**
 * @author Juan David Ramírez
 * @since 02 Diciembre 2010
 *
 */
@SuppressWarnings("serial")
public class DtoBonoDescuento implements Serializable
{
	/**
	 * Programa del presupuesto al cual se relaciona el bono
	 */
	private int presupuestoProgramaServicio;
	
	/**
	 * Persona la cual tiene registrado el bono
	 */
	private DtoPersonas persona;
	
	/**
	 * Serial del bono
	 */
	private BigInteger serial;
	
	/**
	 * Programa para el cual se hizo la emisión del bono
	 */
	private int programa;
	
	/**
	 * Bono del paciente, estricta relación al campo codigo_pk de la tabla bonos_conv_ing_pac
	 */
	private int bonoPaciente;
	
	/**
	 * Obtiene el valor del atributo presupuestoProgramaServicio
	 *
	 * @return Retorna atributo presupuestoProgramaServicio
	 */
	public int getPresupuestoProgramaServicio()
	{
		return presupuestoProgramaServicio;
	}
	/**
	 * Establece el valor del atributo presupuestoProgramaServicio
	 *
	 * @param valor para el atributo presupuestoProgramaServicio
	 */
	public void setPresupuestoProgramaServicio(int presupuestoProgramaServicio)
	{
		this.presupuestoProgramaServicio = presupuestoProgramaServicio;
	}
	/**
	 * Obtiene el valor del atributo persona
	 *
	 * @return Retorna atributo persona
	 */
	public DtoPersonas getPersona()
	{
		return persona;
	}
	/**
	 * Establece el valor del atributo persona
	 *
	 * @param valor para el atributo persona
	 */
	public void setPersona(DtoPersonas persona)
	{
		this.persona = persona;
	}
	/**
	 * Obtiene el valor del atributo serial
	 *
	 * @return Retorna atributo serial
	 */
	public BigInteger getSerial()
	{
		return serial;
	}
	/**
	 * Establece el valor del atributo serial
	 *
	 * @param valor para el atributo serial
	 */
	public void setSerial(BigInteger serial)
	{
		this.serial = serial;
	}
	/**
	 * Obtiene el valor del atributo programa
	 *
	 * @return Retorna atributo programa
	 */
	public int getPrograma()
	{
		return programa;
	}
	/**
	 * Establece el valor del atributo programa
	 *
	 * @param valor para el atributo programa
	 */
	public void setPrograma(int programa)
	{
		this.programa = programa;
	}
	/**
	 * Obtiene el valor del atributo bonoPaciente
	 *
	 * @return Retorna atributo bonoPaciente
	 */
	public int getBonoPaciente()
	{
		return bonoPaciente;
	}
	/**
	 * Establece el valor del atributo bonoPaciente
	 *
	 * @param valor para el atributo bonoPaciente
	 */
	public void setBonoPaciente(int bonoPaciente)
	{
		this.bonoPaciente = bonoPaciente;
	}
}
