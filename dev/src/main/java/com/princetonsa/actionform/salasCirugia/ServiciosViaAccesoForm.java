package com.princetonsa.actionform.salasCirugia;

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
public class ServiciosViaAccesoForm extends ValidatorForm {
	
	/**
	 * Estado dentro de la funcionalidad
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String codigoServicio;
	
	/**
	 * 
	 */
	private String servicio;
	
	/**
	 * Mapa que contiene los servicios parametrizados de via de acceso
	 */
	private HashMap serviciosViaAccesoMap;
	
	/**
	 * Mapa que contiene los servicios eliminados
	 */
	private HashMap serviciosViaAccesoEliminadosMap;
	
	/**
	 * 
	 */
	private String codigosServiciosInsertados;
	
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
	
	public void reset()
	{
		this.serviciosViaAccesoMap=new HashMap();
		this.serviciosViaAccesoMap.put("numRegistros", "0");
		
		this.serviciosViaAccesoEliminadosMap=new HashMap();
		this.serviciosViaAccesoEliminadosMap.put("numRegistros", "0");
		
		this.codigoServicio="";
		this.servicio="";
		this.codigosServiciosInsertados="";
		
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
			int numReg=Utilidades.convertirAEntero(this.serviciosViaAccesoMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(this.serviciosViaAccesoMap.get("codigoServicio_"+i).equals("")||this.serviciosViaAccesoMap.get("codigoServicio_"+i).equals("null")||this.serviciosViaAccesoMap.get("codigoServicio_"+i)==null)
				{
					errores.add("servicio",new ActionMessage("errors.required","El servicio "));
				}
			}
	}
		return errores;
	}

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
	 * @return the indiceEliminado
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * @param indiceEliminado the indiceEliminado to set
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
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
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
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
	 * @return the serviciosViaAccesoEliminadosMap
	 */
	public HashMap getServiciosViaAccesoEliminadosMap() {
		return serviciosViaAccesoEliminadosMap;
	}

	/**
	 * @param serviciosViaAccesoEliminadosMap the serviciosViaAccesoEliminadosMap to set
	 */
	public void setServiciosViaAccesoEliminadosMap(
			HashMap serviciosViaAccesoEliminadosMap) {
		this.serviciosViaAccesoEliminadosMap = serviciosViaAccesoEliminadosMap;
	}

	/**
	 * @return the serviciosViaAccesoMap
	 */
	public HashMap getServiciosViaAccesoMap() {
		return serviciosViaAccesoMap;
	}

	/**
	 * @param serviciosViaAccesoMap the serviciosViaAccesoMap to set
	 */
	public void setServiciosViaAccesoMap(HashMap serviciosViaAccesoMap) {
		this.serviciosViaAccesoMap = serviciosViaAccesoMap;
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
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	
	public Object getServiciosViaAccesoMap(String key)
	{
		return serviciosViaAccesoMap.get(key);
	}
	
	public void setServiciosViaAccesoMap(String key,Object value)
	{
		this.serviciosViaAccesoMap.put(key,value);
	}

	/**
	 * @return the codigosServiciosInsertados
	 */
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	/**
	 * @param codigosServiciosInsertados the codigosServiciosInsertados to set
	 */
	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}
	

}
