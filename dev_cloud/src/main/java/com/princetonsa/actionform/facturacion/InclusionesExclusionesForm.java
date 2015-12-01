package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

@SuppressWarnings("serial")
public class InclusionesExclusionesForm extends ValidatorForm
{
	
	/**
	 *  Estado en el que se encuentra el proceso
	 */
	
	private String estado="";
	
	
	/**
	 *  mapa de consultorios
	 */
	
	private HashMap incluExcluMap;
	
	
	/**
	 * clon de incluExclumap al momento de cargalos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	
	private HashMap incluExcluEliminadosMap;
	
	
	/**
	 * para la navegacion del pager, cuando se ingresa
	 * un registro nuevo.
	 */
	
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	
	 private int offset;
	 
	 
	/**
	 * Posicion del registro que se eliminara
	 */
	    
	 private int posEliminar;
	 
	 private ResultadoBoolean mensaje=new ResultadoBoolean(false);


	/**
     * resetea los atributos del form
     */
	 
    public void reset()
    {
    	incluExcluMap=new HashMap();
    	incluExcluMap.put("numRegistros","0");
     	incluExcluEliminadosMap=new HashMap();
    	incluExcluEliminadosMap.put("numRegistros", "0");
      	linkSiguiente="";
       	this.maxPageItems=20;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.offset=0;
    	this.posEliminar=ConstantesBD.codigoNuncaValido;
    }


    
	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public HashMap getIncluExcluEliminadosMap() {
		return incluExcluEliminadosMap;
	}


	public void setIncluExcluEliminadosMap(HashMap incluExcluEliminadosMap) {
		this.incluExcluEliminadosMap = incluExcluEliminadosMap;
	}
	
	
	public Object getIncluExcluEliminadosMap(String key)
	{
		return incluExcluEliminadosMap.get(key);
	}

	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	
	public void setIncluExcluEliminadosMap(String key,Object value)
	{
		this.incluExcluEliminadosMap.put(key, value);
	}


	
	public HashMap getIncluExcluMap() {
		return incluExcluMap;
	}


	public void setIncluExcluMap(HashMap incluExcluMap) {
		this.incluExcluMap = incluExcluMap;
	}
	
	
	public Object getIncluExcluMap(String key) {
		return incluExcluMap.get(key);
	}

	
	
	/**
	 * 
	 * @param incluExcluMap the incluExcluMap to set
	 */
	
	public void setIncluExcluMap(String key,Object value) {
		this.incluExcluMap.put(key, value);
	}
	
	


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	public int getMaxPageItems() {
		return maxPageItems;
	}


	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public int getPosEliminar() {
		return posEliminar;
	}


	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}


	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
    
    
    
    /**
	 *  Validate the properties that have been set from this HTTP request, and
	 *  return an <code>ActionErrors</code> object that encapsulates any 
	 *  validation errors that have been found. If no errors are found, return
	 *  <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 *  error messages.
	 *  @param mapping The mapping used to select this instance
	 *  @param request The servlet request we are processing
	 */
    
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardar"))
		{
			int numReg=Integer.parseInt(this.incluExcluMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.incluExcluMap.get("codigo_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.incluExcluMap.get("codigo_"+i)+"").equalsIgnoreCase(this.incluExcluMap.get("codigo_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.incluExcluMap.get("codigo_"+i)));
						}
					}
				}
				if((this.incluExcluMap.get("nombre_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Nombre del registro "+(i+1)));
				}
				
			}
		}
		return errores;
	}



	public ResultadoBoolean getMensaje() {
		return mensaje;
	}



	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

}
