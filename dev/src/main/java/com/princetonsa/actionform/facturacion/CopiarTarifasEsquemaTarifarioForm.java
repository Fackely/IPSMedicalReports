package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.Utilidades;

public class CopiarTarifasEsquemaTarifarioForm extends ValidatorForm 
{

	
	private String estado;
	
	private String tarifarioOrigen;
	
	private String tarifarioDestino;
	
	private String porcentaje;
	
	private String chequeo;
	
	/**
	 * 
	 */
	private HashMap<String, Object> tarifarioOrigenMap;
	
	private HashMap<String, Object> tarifarioDestinoMap;
	
	private HashMap<String, Object> mapaTarifas;
	
	private ResultadoBoolean mostrarMensaje;
	
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.tarifarioOrigen="";
		this.tarifarioDestino="";
		this.porcentaje="";
		this.chequeo="";
		this.tarifarioOrigenMap=new HashMap<String, Object>();
		this.tarifarioOrigenMap.put("numRegistros", "0");
		this.tarifarioDestinoMap=new HashMap<String, Object>();
		this.tarifarioDestinoMap.put("numRegistros", "0");
		this.mapaTarifas= new HashMap<String, Object>();
		this.mapaTarifas.put("numRegistros", "0");
	}
	
	
	/**
	 * 
	 *
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje=new ResultadoBoolean(false,"");
	}
	
	/**
	 * 
	 *
	 */
	public void resetPorcentaje()
	{
		this.porcentaje="";
		this.chequeo="";
	}
	
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		double porcentajedouble= Utilidades.convertirADouble(porcentaje);
		
		if(this.estado.equals("guardar"))
		{
			if(this.tarifarioOrigen.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Esquema Tarifario Origen "));
			}
			if(this.tarifarioDestino.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Esquema Tarifario Destino "));
			}
			if(porcentajedouble>100)
			{
				errores.add("codigo", new ActionMessage("errors.porcentajeMayor100","El porcentaje "));
			}
			if(!this.porcentaje.equals("")&&this.chequeo.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Campo de chequeo excluyente "));
			}
			
		}
		return errores;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getTarifarioDestino() {
		return tarifarioDestino;
	}

	/**
	 * 
	 * @param tarifarioDestino
	 */
	public void setTarifarioDestino(String tarifarioDestino) {
		this.tarifarioDestino = tarifarioDestino;
	}

	/**
	 * 
	 * @return
	 */
	public String getTarifarioOrigen() {
		return tarifarioOrigen;
	}

	/**
	 * 
	 * @param tarifarioOrigen
	 */
	public void setTarifarioOrigen(String tarifarioOrigen) {
		this.tarifarioOrigen = tarifarioOrigen;
	}

	/**
	 * 
	 * @return
	 */
	public String getPorcentaje() {
		return porcentaje;
	}

	/**
	 * 
	 * @param porcentaje
	 */
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * 
	 * @return
	 */
	public String getChequeo() {
		return chequeo;
	}

	/**
	 * 
	 * @param chequeo
	 */
	public void setChequeo(String chequeo) {
		this.chequeo = chequeo;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getTarifarioOrigenMap() {
		return tarifarioOrigenMap;
	}

	/**
	 * 
	 * @param tarifarioOrigenMap
	 */
	public void setTarifarioOrigenMap(HashMap<String, Object> tarifarioOrigenMap) {
		this.tarifarioOrigenMap = tarifarioOrigenMap;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaTarifas() {
		return mapaTarifas;
	}

	/**
	 * 
	 * @param mapaTarifas
	 */
	public void setMapaTarifas(HashMap<String, Object> mapaTarifas) {
		this.mapaTarifas = mapaTarifas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaTarifas(String key) {
		return mapaTarifas.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaTarifas(String key,Object value) {
		this.mapaTarifas.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getTarifarioDestinoMap() {
		return tarifarioDestinoMap;
	}

	/**
	 * 
	 * @param tarifarioDestinoMap
	 */
	public void setTarifarioDestinoMap(HashMap<String, Object> tarifarioDestinoMap) {
		this.tarifarioDestinoMap = tarifarioDestinoMap;
	}
	
	
}
