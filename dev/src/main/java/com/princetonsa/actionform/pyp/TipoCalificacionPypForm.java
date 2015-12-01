/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class TipoCalificacionPypForm extends ActionForm {

	
	private static final long serialVersionUID = 1L;

	/**
	 * variable para manejar el flujo de la funcionalidad.  
	 */	
	private String estado;

	/**
	 * Variable para saber el indice dentro del mapa del Articulo a eliminar 
	 */
	private int indiceEliminado;

	/**
	 * Mapa Para Manejar El Ingreso y la Consulta de la Funcionalidad.
	 */
	private HashMap mapa;


	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		return errores;
	}
	
	/**
	 * Resetear la Forma.
	 */
	
	public void reset()
	{
		this.indiceEliminado = -1;
		this.mapa = new HashMap();
		this.setMapa("nroRegistrosNv","0");
	}
	
	/**
	 * @return Retorna mapaCargue.
	 */
	public HashMap getMapa() {
		return mapa;
	}


	/**
	 * @param Asigna mapaCargue.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Returns the mapa.
	 */
	public Object getMapa(String key) {
		return this.mapa.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapa(String key, String valor) 
	{
		this.mapa.put(key, valor); 
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Retorna indiceEliminado.
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * @param Asigna indiceEliminado.
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
	}

}
