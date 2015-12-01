package com.princetonsa.actionform.administracion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class TiposMonedaForm extends ValidatorForm {
	
	/**
	 * Estado en el que se encuetra el proceso
	 */
	private String estado;
	
	/**
	 * Mapa de los tipod de monedas
	 */
	private HashMap tiposMonedaMap;
	
	/**
	 * Mapa con la informacion original
	 */
	private HashMap tiposMonedaOriginalMap;
	
	/**
	 * Mapa para verificar si hay modificaciones y para el log
	 */
	private HashMap tiposMonedaEliminadosMap;
	
	/**
	 * Se utiliza para la navegacion de pager
	 */
	private String linkSiguiente;
	
	/**
	 * Indice del mapa que se desea eliminar
	 */
	private int indiceEliminado;
	
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
	private int maxPageItems;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * simboloMenorA4 indica si el simbolo seleccionado sobrepasa o no los 4 caracteres permitidos en la tabla
	 */
	private boolean simboloMenorA4 = false;
	
	/**
	 * Resetea los atributos del form
	 */
	public void reset()
	{
		this.tiposMonedaMap=new HashMap();
		this.tiposMonedaMap.put("numRegistros", "0");
		
		this.tiposMonedaEliminadosMap=new HashMap();
		this.tiposMonedaEliminadosMap.put("numRegistros", "0");
		
		this.tiposMonedaOriginalMap=new HashMap();
		this.tiposMonedaOriginalMap.put("numRegistros", "0");
		
		this.linkSiguiente="";
    	this.indiceEliminado=ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.maxPageItems=10;
	}
	
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(this.estado.equals("guardar"))
		{
			int numReg=Utilidades.convertirAEntero(this.tiposMonedaMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				for(int j=0;j<i;j++)
				{
					if((this.tiposMonedaMap.get("codigomoneda_"+i)+"").equalsIgnoreCase(this.tiposMonedaMap.get("codigomoneda_"+j)+""))
					{
						errores.add("", new ActionMessage("errors.yaExiste","El código "+this.tiposMonedaMap.get("codigomoneda_"+i)));
					}
				}
				if((this.tiposMonedaMap.get("codigomoneda_"+i)+"").trim().equals(""))
				{
					errores.add("codigo",new ActionMessage("errors.required","El código del registro "+(i+1)));
				}
				if((this.tiposMonedaMap.get("descripcion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo",new ActionMessage("errors.required","La descripción del registro "+(i+1)));
				}
				if((this.tiposMonedaMap.get("simbolo_"+i)+"").trim().equals(""))
				{
					errores.add("codigo",new ActionMessage("errors.required","El símbolo del registro "+(i+1)));
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

	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public HashMap getTiposMonedaEliminadosMap() {
		return tiposMonedaEliminadosMap;
	}

	public void setTiposMonedaEliminadosMap(HashMap tiposMonedaEliminadosMap) {
		this.tiposMonedaEliminadosMap = tiposMonedaEliminadosMap;
	}
	
	public Object getTiposMonedaEliminadosMap(String key)
	{
		return tiposMonedaEliminadosMap.get(key);
	}
	
	public void setTiposMonedaEliminadosMap(String key,Object value)
	{
		this.tiposMonedaEliminadosMap.put(key,value);
	}

	public HashMap getTiposMonedaMap() {
		return tiposMonedaMap;
	}

	public void setTiposMonedaMap(HashMap tiposMonedaMap) {
		this.tiposMonedaMap = tiposMonedaMap;
	}
	
	public Object getTiposMonedaMap(String key)
	{
		return tiposMonedaMap.get(key);
	}
	
	public void setTiposMonedaMap(String key,Object value)
	{
		this.tiposMonedaMap.put(key,value);
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

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public HashMap getTiposMonedaOriginalMap() {
		return tiposMonedaOriginalMap;
	}

	public void setTiposMonedaOriginalMap(HashMap tiposMonedaOriginalMap) {
		this.tiposMonedaOriginalMap = tiposMonedaOriginalMap;
	}

	/**
	 * setSimboloMenorA4
	 * @param simboloMenorA4
	 */
	public void setSimboloMenorA4(boolean simboloMenorA4) {
		this.simboloMenorA4 = simboloMenorA4;
	}

	/**
	 * getSimboloMenorA4
	 * @return boolean simboloMenorA4
	 */
	public boolean getSimboloMenorA4() {
		return simboloMenorA4;
	}

}
