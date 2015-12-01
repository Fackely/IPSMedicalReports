/*
 * @(#)TiposUsuarioCamaForm.java
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
 * la informacion de tipos usuario cama
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Mayo 8, 2007
 * @author Julián Pacheco jpacheco@princetonsa.com
 * */
public class TiposUsuarioCamaForm extends ValidatorForm 
{
	/**
	 * Estado en el que se encuentra el proceso.  
	 */
	private String estado;
	
	/**
	 * Mapa de tipos usuario cama
	 */
	private HashMap tiposUsuarioCamaMap;
	
	/**
	 * Mapa de eliminacion de tipos usuario cama
	 */
	private HashMap tiposUsuarioCamaEliminadosMap;
	
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
		this.tiposUsuarioCamaMap=new HashMap();
		this.tiposUsuarioCamaMap.put("numRegistros", "0");
		this.tiposUsuarioCamaEliminadosMap= new HashMap();
		this.tiposUsuarioCamaEliminadosMap.put("numRegistros", 0);
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.linkSiguiente="";
		this.indice=-1;
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
			int numReg=Integer.parseInt(this.tiposUsuarioCamaMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(UtilidadTexto.isEmpty(this.tiposUsuarioCamaMap.get("codigo_"+i).toString()))
				{
					errores.add("codigo",new ActionMessage("errors.required","El Código de Tipos Usuario Cama "));
				}
				if(UtilidadTexto.isEmpty(this.tiposUsuarioCamaMap.get("nombre_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El Nombre de Tipos Usuario Cama"));
				}
				if(UtilidadTexto.isEmpty(this.tiposUsuarioCamaMap.get("indsexorestrictivo_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El Indicativo Sexo Restrictivo de Tipos Usuario Cama"));
				}
				if(UtilidadTexto.isEmpty(this.tiposUsuarioCamaMap.get("edadinicial_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","La Edad Inicial de Tipos Usuario Cama"));
				}
				if(UtilidadTexto.isEmpty(this.tiposUsuarioCamaMap.get("edadfinal_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","La Edad Final de Tipos Usuario Cama"));
				}
				if(errores.isEmpty())
				{
					if(this.tiposUsuarioCamaMap.get("estabd_"+i).toString().trim().equals(ConstantesBD.acronimoNo))
					{
						for(int j=0; j<numReg; j++)
						{
							if(this.tiposUsuarioCamaMap.get("codigo_"+j).toString().trim().equals(this.tiposUsuarioCamaMap.get("codigo_"+i).toString().trim()) && (j!=i))
							{
								errores.add("codigo",new ActionMessage("errors.yaExiste","El Codigo de Tipos Usuario Cama "+this.tiposUsuarioCamaMap.get("codigo_"+i)));							
							}							
						}
					}	
				}
		
				if(errores.isEmpty())
				{
					if(Integer.parseInt(this.tiposUsuarioCamaMap.get("edadinicial_"+i).toString())> Integer.parseInt(this.tiposUsuarioCamaMap.get("edadfinal_"+i).toString()))
					{
						errores.add("",new ActionMessage("errors.MayorIgualQue","La Edad Final (en días)", "la edad Inicial"));
					}
					if(errores.isEmpty())
					{	
						if(Integer.parseInt(this.tiposUsuarioCamaMap.get("edadfinal_"+i).toString())>54750)
						{
							errores.add("",new ActionMessage("errors.MenorIgualQue","La Edad Final(en días)","54750"));
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
	 * @return the tiposUsuarioCamaMap
	 */
	public HashMap getTiposUsuarioCamaMap() {
		return tiposUsuarioCamaMap;
	}

	/**
	 * @param tiposUsuarioCamaMap the tiposUsuarioCamaMap to set
	 */
	public void setTiposUsuarioCamaMap(HashMap tiposUsuarioCamaMap) {
		this.tiposUsuarioCamaMap = tiposUsuarioCamaMap;
	}

	/**
	 * @return the tiposUsuarioCamaMap
	 */
	public Object getTiposUsuarioCamaMap(Object key) {
		return tiposUsuarioCamaMap.get(key);
	}
	
	/**
	 * @param tiposUsuarioCamaMap the tiposUsuariosMap to set
	 */
	public void setTiposUsuarioCamaMap(Object key, Object values) {
		this.tiposUsuarioCamaMap.put(key, values);
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
	 * @return the tiposUsuarioCamaEliminadosMap
	 */
	public HashMap getTiposUsuarioCamaEliminadosMap() {
		return tiposUsuarioCamaEliminadosMap;
	}

	/**
	 * @param tiposUsuarioCamaEliminadosMap the tiposUsuarioCamaEliminadosMap to set
	 */
	public void setTiposUsuarioCamaEliminadosMap(
			HashMap tiposUsuarioCamaEliminadosMap) {
		this.tiposUsuarioCamaEliminadosMap = tiposUsuarioCamaEliminadosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getTiposUsuarioCamaEliminadosMap(Object key) {
		return tiposUsuarioCamaEliminadosMap.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param values
	 */
	public void setTiposUsuarioCamaEliminadosMap(Object key, Object values) {
		this.tiposUsuarioCamaEliminadosMap.put(key, values);
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
