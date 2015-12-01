
package util;

import java.io.Serializable;

/**
 * 
 * @author axioma
 *
 */
public class ResultadoDouble implements Serializable
{
	/**
	 * 
	 */
	private double resultado;
	
	/**
	 * Descripción del resultado
	 */
	private String descripcion;
	
	public ResultadoDouble(double resultado)
	{
		this.resultado = resultado;
		this.descripcion = null;
	}

	public ResultadoDouble(double resultado, String descripcion)
	{
		this.resultado = resultado;
		this.descripcion = descripcion;
	}

	/**
	 * @return the resultado
	 */
	public double getResultado() {
		return resultado;
	}

	/**
	 * @param resultado the resultado to set
	 */
	public void setResultado(double resultado) {
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
