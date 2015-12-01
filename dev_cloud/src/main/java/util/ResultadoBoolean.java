/*
 * @(#)Resultado.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.io.Serializable;

/**
 * Clase que inidica si el resultado de algún proceso fue exitoso (true) o no
 * (false), y da una descripción del resultado en caso de ser necesario
 *
 * @version 1.0, Agosto 15, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class ResultadoBoolean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * El proceso tuvo una finalización exitosa "true" o no "false"
	 */
	private boolean resultado;
	
	/**
	 * Descripción del resultado
	 */
	private String descripcion;
	
	
	public ResultadoBoolean()
	{}
	
	public ResultadoBoolean(boolean resultado)
	{
		this.resultado = resultado;
		this.descripcion = null;
	}

	public ResultadoBoolean(boolean resultado, String descripcion)
	{
		this.resultado = resultado;
		this.descripcion = descripcion;
	}
	
	/**
	 * Retorna si el proceso tuvo una finalización exitosa "true" o no "false"
	 * @return boolean
	 */
	public boolean isTrue()
	{
		return resultado;
	}

	/**
	 * Asigna el resultado del proceso
	 * @param resultado The resultado to set
	 */
	public void setResultado(boolean resultado)
	{
		this.resultado = resultado;
	}

	/**
	 * Retorna la descripción del resultado
	 * @return String
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * Asigna la Descripción del resultado
	 * @param descripcion The descripcion to set
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	public boolean isResultado() {
		return resultado;
	}

}
