
package util;

import java.io.Serializable;


public class ResultadoInteger implements Serializable
{
	
	private int resultado;
	
	/**
	 * Descripción del resultado
	 */
	private String descripcion;
	
	public ResultadoInteger(int resultado)
	{
		this.resultado = resultado;
		this.descripcion = null;
	}

	public ResultadoInteger(int resultado, String descripcion)
	{
		this.resultado = resultado;
		this.descripcion = descripcion;
	}

	/**
	 * @return the resultado
	 */
	public int getResultado() {
		return resultado;
	}

	/**
	 * @param resultado the resultado to set
	 */
	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	


}
