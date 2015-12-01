package com.princetonsa.actionform.inventarios;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class NaturalezaArticulosForm extends ValidatorForm {
	
	
	
	private HashMap naturalezaArticulosMap;
	
	
	private String estado="";
	
	
	
	private HashMap naturalezaArticulosEliminadosMap;
	
	
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
	
	
	
	
	
	public void reset(){
		
		this.naturalezaArticulosMap=new HashMap();
		naturalezaArticulosMap.put("numRegistros","0");
		
		this.naturalezaArticulosEliminadosMap=new HashMap();
		naturalezaArticulosEliminadosMap.put("numRegistros","0");
		
		linkSiguiente="";
       	this.maxPageItems=20;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.offset=0;
    	this.posEliminar=ConstantesBD.codigoNuncaValido;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardar"))
		{
			int numReg=Integer.parseInt(this.naturalezaArticulosMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.naturalezaArticulosMap.get("acronimo_"+i)+"").trim().equals(""))
				{
					errores.add("acronimo naturaleza", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.naturalezaArticulosMap.get("acronimo_"+i)+"").equalsIgnoreCase(this.naturalezaArticulosMap.get("acronimo_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.naturalezaArticulosMap.get("acronimo_"+i)));
						}
					}
				}
				if((this.naturalezaArticulosMap.get("nombre_"+i)+"").trim().equals(""))
				{
					errores.add("acronimo naturaleza", new ActionMessage("errors.required","El nombre del registro "+(i+1)));
				}
			}
		}
		return errores;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public HashMap getNaturalezaArticulosMap() {
		return naturalezaArticulosMap;
	}
	
	public Object getNaturalezaArticulosMap(String key) {
		return naturalezaArticulosMap.get(key);
	}

	public void setNaturalezaArticulosMap(String key, Object Value) {
		this.naturalezaArticulosMap.put(key,Value);
	}

	public void setNaturalezaArticulosMap(HashMap naturalezaArticulosMap) {
		this.naturalezaArticulosMap = naturalezaArticulosMap;
	}



	public HashMap getNaturalezaArticulosEliminadosMap() {
		return naturalezaArticulosEliminadosMap;
	}
	
	public Object getNaturalezaArticulosEliminadosMap(String key) {
		return naturalezaArticulosEliminadosMap.get(key);
	}
	
	public void setNaturalezaArticulosEliminadosMap(String key, Object Value) {
		this.naturalezaArticulosMap.put(key,Value);
	}



	public void setNaturalezaArticulosEliminadosMap(
			HashMap naturalezaArticulosEliminadosMap) {
		this.naturalezaArticulosEliminadosMap = naturalezaArticulosEliminadosMap;
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



	public ResultadoBoolean getMensaje() {
		return mensaje;
	}



	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

}
