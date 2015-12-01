package util;

import java.util.Collection;

/**
 * Clase que inidica si el resultado de alg�n proceso fue exitoso (true) o no
 * (false), da una descripci�n del resultado en caso de ser necesario y retorna
 * la colecci�n con los elementos del resultSet
 *
 * @version 1.0, Enero 15, 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class ResultadoCollectionDB 
{
	/**
	 * El proceso tuvo una finalizaci�n exitosa "true" o no "false"
	 */
	private boolean resultado;
	
	/**
	 * Descripci�n del resultado
	 */
	private String descripcion;
	
	/**
	 * Colecci�n que contiene el resultado, la colecci�n es de HashMaps
	 */
	private Collection filasRespuesta;
	
	public ResultadoCollectionDB(boolean resultado)
	{
		this.resultado = resultado;
		this.descripcion = null;
		this.filasRespuesta = null;
	}

	public ResultadoCollectionDB(boolean resultado, String descripcion)
	{
		this.resultado = resultado;
		this.descripcion = descripcion;
		this.filasRespuesta = null;
	}
	
	public ResultadoCollectionDB(boolean resultado, String descripcion, Collection filasRespuesta)
	{
		this.resultado = resultado;
		this.descripcion = descripcion;
		this.filasRespuesta = filasRespuesta;
	}
	
	/**
	 * Retorna si el proceso tuvo una finalizaci�n exitosa "true" o no "false"
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
	 * Retorna la descripci�n del resultado
	 * @return String
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * Asigna la Descripci�n del resultado
	 * @param descripcion The descripcion to set
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}


	/**
	 * Retorna la colecci�n que contiene el resultado, la colecci�n es de
	 * HashMaps
	 */
	public Collection getFilasRespuesta()
	{
		return filasRespuesta;
	}

	/**
	 * Asigna la colecci�n que contiene el resultado, la colecci�n es de
	 * HashMaps
	 * @param filasRespuesta The filasRespuesta to set
	 */
	public void setFilasRespuesta(Collection filasRespuesta)
	{
		this.filasRespuesta = filasRespuesta;
	}

}
