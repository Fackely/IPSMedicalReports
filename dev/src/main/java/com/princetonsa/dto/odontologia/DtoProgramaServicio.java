/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase para permitir manejar un programa o servicio en el caso de que sean excluyentes
 * 
 * @author Juan David Ramírez
 *
 */
@SuppressWarnings("serial")
public class DtoProgramaServicio implements Serializable
{

	/**
	 * Bandera que indica si es programa o servicio
	 * Si el objeto lleva un programa, entonces programa=true, programa=false de lo contrario
	 */
	private boolean programa=false;
	
	/**
	 * Código del programa o del servicio
	 */
	private int codigo;

	/**
	 * Constructor
	 * @param codigo BigDecimal con el código del programa o servicio
	 * @param programa booleano con true si maneja programas, false de lo contrario
	 */
	public DtoProgramaServicio(BigDecimal codigo, boolean programa)
	{
		this.codigo=codigo.intValue();
		this.programa=programa;
	}

	/**
	 * Constructor
	 * @param codigo entero con el código del programa o servicio
	 * @param programa booleano con true si maneja programas, false de lo contrario
	 */
	public DtoProgramaServicio(int codigo, boolean programa)
	{
		this.codigo=codigo;
		this.programa=programa;
	}

	/**
	 * Obtiene el valor del atributo esPrograma
	 *
	 * @return Retorna atributo esPrograma
	 */
	public boolean isPrograma()
	{
		return programa;
	}

	/**
	 * Establece el valor del atributo esPrograma
	 *
	 * @param valor para el atributo esPrograma
	 */
	public void setPrograma(boolean programa)
	{
		this.programa = programa;
	}

	/**
	 * Obtiene el valor del atributo codigo
	 *
	 * @return Retorna atributo codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Establece el valor del atributo codigo
	 *
	 * @param valor para el atributo codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}
	
	/**
	 * Obtiene un objeto BigDecimal con el código del programa o del servicio
	 * @return {@link BigDecimal} con el código del programa o del servicio
	 */
	public BigDecimal getCodigoBigDecimal()
	{
		return new BigDecimal(codigo);
	}
	
}
