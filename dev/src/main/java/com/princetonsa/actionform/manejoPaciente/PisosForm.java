/*
 * @(#)PisosForm.java
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
import util.Utilidades;

/**
 * Form que contiene todos los datos especificos para generar 
 * la informacion de pisos
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Abril 30, 200
 * @author Julián Pacheco jpacheco@princetonsa.com
 */
public class PisosForm extends ValidatorForm
{
	/**
	 * Estado en el que se encuentra el proceso.  
	 */
	private String estado;
	
	/**
	 * centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private HashMap pisosEliminadosMap;
	
	/**
	 * Mapa de pisos
	 */
	private HashMap pisosMap;
	
	/**
	 * mapa para mostrar el select de los n centros de atencion
	 */
	private HashMap centrosAntencionTagMap;

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
	
	
//--------------------------------METODOS---------------------------------------	
	/**
	 * resetea los atributos del form
	 *
	 */
	public void reset(int codCentroAtencion)
	{
		this.centroAtencion=codCentroAtencion;
		this.pisosMap = new HashMap();
    	this.pisosMap.put("numRegistros", 0);
    	this.pisosEliminadosMap = new HashMap();
    	this.pisosEliminadosMap.put("numRegistros", 0);
    	this.centrosAntencionTagMap= new HashMap();
    	this.indice=-1;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.linkSiguiente="";	
	}
	
	/**
     * inicializa los tags de la forma
     * @param codigoInstitucionInt
     */
    public void inicializarTags(int codigoInstitucion) 
    {
		this.centrosAntencionTagMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion); 
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
			int numReg=Integer.parseInt(this.pisosMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(UtilidadTexto.isEmpty(this.pisosMap.get("codigopiso_"+i).toString()))
				{
					errores.add("codigopiso",new ActionMessage("errors.required","El Código Piso "));
				}
				if(UtilidadTexto.isEmpty(this.pisosMap.get("nombre_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El Nombre del Piso"));
				}
				
				if(errores.isEmpty())
				{
					if(this.pisosMap.get("estabd_"+i).toString().trim().equals(ConstantesBD.acronimoNo))
					{
						for(int j=0; j<numReg; j++)
						{
							if(this.pisosMap.get("codigopiso_"+j).toString().trim().equals(this.pisosMap.get("codigopiso_"+i).toString().trim()) && (j!=i))
							{
								errores.add("codigopiso",new ActionMessage("errors.yaExiste","El Codigo "+this.pisosMap.get("codigopiso_"+i)));							
							}							
						}
					}	
				}
				
			}
		}
		
		return errores;
    }
	    
	

//--------------------------------------------Fin Validate--------------------------------------------------------
	
//---------------------------------Getters and Setters------------------------	
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
	 * @return the pisosMap
	 */
	public HashMap getPisosMap() {
		return pisosMap;
	}

	/**
	 * @param pisosMap the pisosMap to set
	 */
	public void setPisosMap(HashMap pisosMap) {
		this.pisosMap = pisosMap;
	}
	
	/**
	 * @return the pisosMap
	 */
	public Object getPisosMap(Object key) {
		return pisosMap.get(key);
	}
	
	/**
	 * @param pisosMap the pisosMap to set
	 */
	public void setPisosMap(Object key, Object values) {
		this.pisosMap.put(key, values);
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
	 * @return the pisosEliminadosMap
	 */
	public HashMap getPisosEliminadosMap() {
		return pisosEliminadosMap;
	}

	/**
	 * @param pisosEliminadosMap the pisosEliminadosMap to set
	 */
	public void setPisosEliminadosMap(HashMap pisosEliminadosMap) {
		this.pisosEliminadosMap = pisosEliminadosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getPisosEliminadosMap(Object key) {
		return pisosEliminadosMap.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param values
	 */
	public void setPisosEliminadosMap(Object key, Object values) {
		this.pisosEliminadosMap.put(key, values);
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

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centrosAntencionTagMap
	 */
	public HashMap getCentrosAntencionTagMap() {
		return centrosAntencionTagMap;
	}

	/**
	 * @param centrosAntencionTagMap the centrosAntencionTagMap to set
	 */
	public void setCentrosAntencionTagMap(HashMap centrosAntencionTagMap) {
		this.centrosAntencionTagMap = centrosAntencionTagMap;
	}
	
	/**
	 * @return the centrosAntencionTagMap
	 */
	public Object getCentrosAntencionTagMap(Object key) {
		return centrosAntencionTagMap.get(key);
	}

	/**
	 * @param centrosAntencionTagMap the centrosAntencionTagMap to set
	 */
	public void setCentrosAntencionTagMap(Object value, Object key) {
		this.centrosAntencionTagMap.put(key, value);
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

	}
