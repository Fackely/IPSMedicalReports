

package util;

import java.io.Serializable;

/**
 * 
 * @author axioma
 *
 */
public class ResultadoString implements Serializable
{
	/**
	 * El proceso tuvo una finalización exitosa "true" o no "false"
	 */
	private String resultado;
	
	/**
	 * Descripción del resultado
	 */
	private String descripcion;
	
	public ResultadoString(String resultado)
	{
		this.resultado = resultado;
		this.descripcion = null;
	}

	public ResultadoString(String resultado, String descripcion)
	{
		this.resultado = resultado;
		this.descripcion = descripcion;
	}

	/**
	 * @return the resultado
	 */
	public String getResultado() {
		return resultado;
	}

	/**
	 * @param resultado the resultado to set
	 */
	public void setResultado(String resultado) {
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
