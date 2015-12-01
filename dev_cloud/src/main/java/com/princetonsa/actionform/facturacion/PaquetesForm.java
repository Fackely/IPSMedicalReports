package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;


public class PaquetesForm extends ValidatorForm 
{
	
	/**
	 *  Estado en el que se encuentra el proceso
	 */
	
	private String estado="";
	
	
	/**
	 *  mapa de paquetes
	 */
	
	private HashMap paquetesMap;
	
	
	/**
	 * clon de paquetesmap al momento de cargalos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	
	private HashMap paquetesEliminadosMap;
	
	
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


	public ResultadoBoolean getMensaje() {
		return mensaje;
	}



	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}



	/**
     * resetea los atributos del form
     */
	 
    public void reset()
    {
    	paquetesMap=new HashMap();
    	paquetesMap.put("numRegistros","0");
     	paquetesEliminadosMap=new HashMap();
    	paquetesEliminadosMap.put("numRegistros", "0");
      	linkSiguiente="";
       	this.maxPageItems=20;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.offset=0;
    	this.posEliminar=ConstantesBD.codigoNuncaValido;
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
			int numReg=Integer.parseInt(this.paquetesMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.paquetesMap.get("codigopaquete_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.paquetesMap.get("codigopaquete_"+i)+"").equalsIgnoreCase(this.paquetesMap.get("codigopaquete_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.paquetesMap.get("codigopaquete_"+i)));
						}
					}
				}
				if((this.paquetesMap.get("descripcion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","La Descripción del registro "+(i+1)));
				}
				if((this.paquetesMap.get("servicio_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El CUPS asociado del registro "+(i+1)));
				}
			}
		}
		return errores;
	}

    
	
    /**
     * 
     * @return the estado
     */
	
	public String getEstado() {
		return estado;
	}
	
	
	
	/**
	 * 
	 * @param estado the estado to set
	 */

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	
	/**
	 * 
	 * @return the linkSiguiente
	 */

	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	
	/**
	 * 
	 * @param linkSiguiente to linkSiguiente to set 
	 */
	
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	
	/**
	 * 
	 * @return the consultoriosEliminadosMap
	 */
	
	public HashMap getPaquetesEliminadosMap() {
		return paquetesEliminadosMap;
	}
	
	
	
	/**
	 * 
	 * @param paquetesEliminadosMap the paquetesEliminadosMap to set
	 */

	public void setPaquetesEliminadosMap(HashMap paquetesEliminadosMap) {
		this.paquetesEliminadosMap = paquetesEliminadosMap;
	}
	
	
	
	/*
	 * 
	 */
	
	public Object getPaquetesEliminadosMap(String key)
	{
		return paquetesEliminadosMap.get(key);
	}

	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	
	public void setPaquetesEliminadosMap(String key,Object value)
	{
		this.paquetesEliminadosMap.put(key, value);
	}

	
	
	
	/**
	 * 
	 * @return the paquetesMap
	 */

	public HashMap getPaquetesMap() {
		return paquetesMap;
	}


	
	/**
	 * 
	 * @param paquetesMap the paquetesMap to set
	 */
	
	public void setPaquetesMap(HashMap paquetesMap) {
		this.paquetesMap = paquetesMap;
	}

	
	
	/**
	 * 
	 * @return
	 */
	
	public int getMaxPageItems() {
		return maxPageItems;
	}
	
	
	
	/**
	 * 
	 * @return the paquetesMap
	 */

	public Object getPaquetesMap(String key) {
		return paquetesMap.get(key);
	}

	
	
	/**
	 * 
	 * @param paquetesMap the paquetesMap to set
	 */
	
	public void setPaquetesMap(String key,Object value) {
		this.paquetesMap.put(key, value);
	}
	
		
	/**
	 * 
	 * @param maxPageItems
	 */

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	
	/**
	 * 
	 * @return
	 */
	
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	
	/**
	 * 
	 * @return
	 */
	
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	
	/**
	 * 
	 * @param ultimoPatron
	 */
	
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	
	/**
	 * 
	 * @return
	 */
	
	public int getOffset() {
		return offset;
	}

	
	/**
	 * 
	 * @param offset
	 */
	
	public void setOffset(int offset) {
		this.offset = offset;
	}

	
	/**
	 * 
	 * @return
	 */
	
	public int getPosEliminar() {
		return posEliminar;
	}

	
	/**
	 * 
	 * @param posEliminar
	 */
	
	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}
	
	
	
}
