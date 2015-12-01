/*
 * @(#)TiposConvenioForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.actionform.facturacion;

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
 * la informacion de tipos convenio
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. April, 2008
 * @author Julian Pacheco jpacheco@princetonsa.com
 */


public class TiposConvenioForm extends ValidatorForm 
{
//	-------------------Atributos
	/**
	 * Objeto para manejar el log de la clase
	 * */	
	/*private Logger logger = Logger.getLogger(TiposConvenioForm.class);
	*/
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * 
	 */
	private HashMap tiposConvenioMap;
	
	/**
	 * 
	 */
	private HashMap tiposConvenioEliminadosMap;
	
	/**
	 * 
	 */
	private int indice;
	
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	
    
    private int codigoEliminar;
    
    /**
     * para mantener el codigo del registro que va al pop-up
     */
    private int registroCuentaContablePopUp;
    
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.tiposConvenioMap = new HashMap();
    	this.tiposConvenioMap.put("numRegistros", 0);
    	this.tiposConvenioEliminadosMap = new HashMap();
    	this.tiposConvenioEliminadosMap.put("numRegistros", 0);
    	this.indice=-1;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.linkSiguiente="";
    	this.codigoEliminar=ConstantesBD.codigoNuncaValido;
    	this.registroCuentaContablePopUp = ConstantesBD.codigoNuncaValido;
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
			int numReg=Integer.parseInt(this.tiposConvenioMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				/*
				if(UtilidadTexto.isEmpty(this.tiposConvenioMap.get("codigo_"+i).toString()))
				{
					errores.add("codigo",new ActionMessage("errors.required","El Código Tipo Convenio "));
				}
				*/
				if(UtilidadTexto.isEmpty(this.tiposConvenioMap.get("descripcion_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","La Descripción del tipo Convenio"));
				}
				if(Integer.parseInt(this.tiposConvenioMap.get("codigoclasificacion_"+i).toString())<1)
				{
					errores.add("",new ActionMessage("errors.required","La Clasificación del tipo Convenio"));
				}
				if(UtilidadTexto.isEmpty(this.tiposConvenioMap.get("acronimotiporegimen_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El Regimen del tipo Convenio"));
				}
				
				if(errores.isEmpty())
				{
					if(this.tiposConvenioMap.get("estabd_"+i).toString().trim().equals(ConstantesBD.acronimoNo))
					{
						for(int j=0; j<numReg; j++)
						{
							if(this.tiposConvenioMap.get("codigo_"+j).toString().trim().equals(this.tiposConvenioMap.get("codigo_"+i).toString().trim()) && (j!=i))
							{
								errores.add("codigo",new ActionMessage("errors.yaExiste","El Codigo "+this.tiposConvenioMap.get("codigo_"+i)));							
							}							
						}
					}	
				}
			}
		}
		
		return errores;
    }
//-----------------Fin Validate----------------------------------
    
//--------------------------Getters and Setters-----------------------------

    /**
     * @return the estado
     */	
    public String getEstado() 
	{
		return estado;
	}
    
    /**
     * @param estado the estado to set
     */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return the tiposConvenioMap
	 */
	public HashMap getTiposConvenioMap() {
		return tiposConvenioMap;
	}
	
	/**
	 * @param tiposConvenioMap the tiposConvenioMap to set
	 */
	public void setTiposConvenioMap(HashMap tiposConvenioMap) {
		this.tiposConvenioMap = tiposConvenioMap;
	}
	
	/**
	 * @return the tiposConvenioMap
	 */
	public Object getTiposConvenioMap(Object key) {
		return tiposConvenioMap.get(key);
	}
	
	/**
	 * @param tiposConvenioMap the tiposConvenioMap to set
	 */
	public void setTiposConvenioMap(Object key, Object values) {
		this.tiposConvenioMap.put(key, values);
	}
	
	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}
	
	/**
	 *  @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}
	
	/**
	 *  @return the tiposConvenioEliminadosMap
	 */
	public HashMap getTiposConvenioEliminadosMap() {
		return tiposConvenioEliminadosMap;
	}
	
	/**
	 *  @param tiposConvenioMap the tiposConvenioMap to set
	 */
	public void setTiposConvenioEliminadosMap(HashMap tiposConvenioEliminadosMap) {
		this.tiposConvenioEliminadosMap = tiposConvenioEliminadosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getTiposConvenioEliminadosMap(Object key) {
		return tiposConvenioEliminadosMap.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param values
	 */
	public void setTiposConvenioEliminadosMap(Object key, Object values) {
		this.tiposConvenioEliminadosMap.put(key, values);
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

	/**
	 * @return the codigoEliminar
	 */
	public int getCodigoEliminar() {
		return codigoEliminar;
	}

	/**
	 * @param codigoEliminar the codigoEliminar to set
	 */
	public void setCodigoEliminar(int codigoEliminar) {
		this.codigoEliminar = codigoEliminar;
	}

	/**
	 * @return the registroCuentaContablePopUp
	 */
	public int getRegistroCuentaContablePopUp() {
		return registroCuentaContablePopUp;
	}

	/**
	 * @param registroCuentaContablePopUp the registroCuentaContablePopUp to set
	 */
	public void setRegistroCuentaContablePopUp(int registroCuentaContablePopUp) {
		this.registroCuentaContablePopUp = registroCuentaContablePopUp;
	}
	
}
