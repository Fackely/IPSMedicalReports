package com.princetonsa.actionform;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos de los interconsultas. Y adicionalmente hace el manejo de reset de
 * la forma y de validación de errores de datos de entrada.
 * @version 1.0, Noviembre 18, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class InterconsultasForm extends ValidatorForm
{
	/**
	 * Estado actual dentro del flujo de la funcionalidad
	 */	
	private String estado;
	
	/**
	 * Listado de interconsultas
	 */
	private ArrayList listadoInterconsultas;
	
	/**
	 * Formato de consulta seleccionado
	 */
	private int formatoRespuesta;
	
	/**
	 * Indice dentro del arreglo de la interconsulta seleccionada
	 */
	private int indiceInterconsulta;

	/**
	* Valida  las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validación encontrados. Si no se encontraron errores de validación,
	* retorna <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que está siendo procesado en este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	* o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		return errores;
	}
	
	/**
	 * Borra el contenido de todos los atributos de la forma.
	 */
	public void reset()
	{
		if( listadoInterconsultas != null )
			listadoInterconsultas.clear();
			
		formatoRespuesta = 0;
		indiceInterconsulta = 0;
	}	
	
	/**
	 * Retorna el estado actual dentro del flujo de la funcionalidad
	 * @return 	String, estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado actual dentro del flujo de la funcionalidad
	 * @param String, estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	
	/**
	 * Retorna el listado de interconsultas
	 * @return ArrayList, listadoInterconsultas
	 */
	public ArrayList getListadoInterconsultas()
	{
		return listadoInterconsultas;
	}

	/**
	 * Asigna el listado de interconsultas
	 * @param ArrayList, listadoInterconsultas
	 */
	public void setListadoInterconsultas(ArrayList listadoInterconsultas)
	{
		this.listadoInterconsultas = listadoInterconsultas;
	}

	/**
	 * Retorna el formato de consulta seleccionado
	 * @return int, formatoRespuesta
	 */
	public int getFormatoRespuesta()
	{
		return formatoRespuesta;
	}

	/**
	 * Asigna el formato de consulta seleccionado
	 * @param int, formatoRespuesta
	 */
	public void setFormatoRespuesta(int formatoRespuesta)
	{
		this.formatoRespuesta = formatoRespuesta;
	}

	/**
	 * Retorna el indice dentro del arreglo de la interconsulta seleccionada
	 * @return int, indiceInterconsulta
	 */
	public int getIndiceInterconsulta()
	{
		return indiceInterconsulta;
	}

	/**
	 * Asigna el indice dentro del arreglo de la interconsulta seleccionada
	 * @param int, indiceInterconsulta
	 */
	public void setIndiceInterconsulta(int indiceInterconsulta)
	{
		this.indiceInterconsulta = indiceInterconsulta;
	}

}
