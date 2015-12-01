/*
 * @(#)TipoHabitacionForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */

package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * Form que contiene todos los datos especificos para generar 
 * la informacion de tipo habitacion
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Mayo 8, 2007
 * @author Julián Pacheco jpacheco@princetonsa.com
 * */
public class TipoHabitacionForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.  
	 */
	private String estado;
	
	/**
	 * Mapa de tipo habitacion
	 */
	private HashMap tipoHabitacionMap;
	
	
	/**
	 * Mapa de eliminacion de tipo habitacion
	 */
	private HashMap tipoHabitacionEliminadosMap;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private int indice;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
		
//	--------------------------------METODOS---------------------------------------	
	
	/**
	 * resetea los atributos del form
	 *
	 */
	public void reset()
	{
		this.tipoHabitacionMap = new HashMap();
    	this.tipoHabitacionMap.put("numRegistros", 0);
    	this.tipoHabitacionEliminadosMap = new HashMap();
    	this.tipoHabitacionEliminadosMap.put("numRegistros", 0);
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.indice=-1;
    	this.linkSiguiente="";
	}

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
	    ActionErrors errores= new ActionErrors();
	    errores=super.validate(mapping,request);
	    if(estado.equals("guardar"))			
		{					 	
			int numReg=Integer.parseInt(this.tipoHabitacionMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(UtilidadTexto.isEmpty(this.tipoHabitacionMap.get("codigo_"+i).toString()))
				{
					errores.add("codigo",new ActionMessage("errors.required","El Código de Tipo de Habitacion "));
				}
				if(UtilidadTexto.isEmpty(this.tipoHabitacionMap.get("nombre_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El Nombre de Tipo de Habitacion"));
				}
				if(errores.isEmpty())
				{
					if(this.tipoHabitacionMap.get("estabd_"+i).toString().trim().equals(ConstantesBD.acronimoNo))
					{
						for(int j=0; j<numReg; j++)
						{
							if(this.tipoHabitacionMap.get("codigo_"+j).toString().trim().equals(this.tipoHabitacionMap.get("codigo_"+i).toString().trim()) && (j!=i))
							{
								errores.add("codigo",new ActionMessage("errors.yaExiste","El Codigo de Tipo Habitacion "+this.tipoHabitacionMap.get("codigo_"+i)));							
							}							
						}
					}	
				}
			}
		}
		
		return errores;
    }

//	------------------------------------Fin Validate--------------------------------------------------------
	
//	---------------------------------Getters and Setters------------------------	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the tipoHabitacionMap
	 */
	public HashMap getTipoHabitacionMap() {
		return tipoHabitacionMap;
	}

	/**
	 * @param tipoHabitacionMap the tipoHabitacionMap to set
	 */
	public void setTipoHabitacionMap(HashMap tipoHabitacionMap) {
		this.tipoHabitacionMap = tipoHabitacionMap;
	}
	
	/**
	 * @return the tipoHabitacionMap
	 */
	public Object getTipoHabitacionMap(Object key) {
		return tipoHabitacionMap.get(key);
	}
	
	/**
	 * @param tipoHabitacionMap the tipoHabitacionMap to set
	 */
	public void setTipoHabitacionMap(Object key, Object values) {
		this.tipoHabitacionMap.put(key, values);
	}

	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}

	/**
	 * @return the tipoHabitacionEliminadosMap
	 */
	public HashMap getTipoHabitacionEliminadosMap() {
		return tipoHabitacionEliminadosMap;
	}

	/**
	 * @param tipoHabitacionEliminadosMap the tipoHabitacionEliminadosMap to set
	 */
	public void setTipoHabitacionEliminadosMap(HashMap tipoHabitacionEliminadosMap) {
		this.tipoHabitacionEliminadosMap = tipoHabitacionEliminadosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getTipoHabitacionEliminadosMap(Object key) {
		return tipoHabitacionEliminadosMap.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param values
	 */
	public void setTipoHabitacionEliminadosMap(Object key, Object values) {
		this.tipoHabitacionEliminadosMap.put(key, values);
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

}
