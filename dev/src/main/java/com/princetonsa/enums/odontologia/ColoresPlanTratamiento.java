package com.princetonsa.enums.odontologia;

/**
 * Definici&oacute;n de los colores mostrados al seleccionar programas o servicios que
 * apliquen para m&aacute;s de una superficie
 * @author Juan David Ram&iacute;rez
 * @since 10 May 2010
 */
public enum ColoresPlanTratamiento
{
	NEGRO("#000000", 1),
	MORADO("#7F00FF", 2),
	VERDE("#00AAAA", 3),
	NARANJA("#FF7F00", 4);
	
	/**
	 * Color para poner la letra del programa
	 */
	private String color;
	
	private int indice;
	
	/**
	 * Constructor que asigna el color de cada enumeraci&oacute;n
	 * @param color
	 */
	private ColoresPlanTratamiento(String color, int indice)
	{
		this.color=color;
		this.indice=indice;
	}

	/**
	 * @return Retorna el atributo color
	 */
	public String getColor()
	{
		return color;
	}

	/**
	 * @return Retorna el atributo indice
	 */
	public int getIndice()
	{
		return indice;
	}

}
