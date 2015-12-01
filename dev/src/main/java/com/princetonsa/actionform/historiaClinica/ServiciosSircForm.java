package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

public class ServiciosSircForm extends ValidatorForm {
	/**
	 * 
	 */	
	private String estado;
	
	/**
	* 
	*/
	private HashMap serviciosSirc;
	
	
	/**
	* HashMap de Detalle de Servicios Sirc
	*/
	private HashMap serviciosSircDetalleMap;
	
	/**
	* HashMap de Detalle Eliminado de Servicios Sirc
	*/
	private HashMap serviciosSircDetalleEliminadoMap;
	
	
	/**Mapa con los regs eliminados
	 * 
	 */
	private HashMap serviciosSircEliminado;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String patronOrdenarServicioSircDetalle;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	 /**
     * para la nevagaci√≥n del pager, cuando se ingresa
     * un registro nuevo.
     */
	private int maxPageItems;
	
	
	 /**
     * para la nevagaci√≥n del pager, cuando se ingresa
     * un registro nuevo.
     */
	private String linkSiguiente;
	
	 /**
     * para controlar la p√°gina actual
     * del pager.
     */
	private int offset;
	
	/**
	 * Valor del Codigo del Servicio Sirc Padre
	 * */
	private String indexServSirc;
	
	/**
	 * Valor String de la ubicacion dentro del hashmap de Servicios Sirc Padre
	 * */
	private String ubicacionConfig;
	
	/**
	 * Index eliminado del HashMap
	 * */
	private String indexEliminado;
    
	/**
	 * Codigos de servicios insertados
	 * */
	private String codServInsert;
	
    /**
     * Posicion del registro que se eliminara
     */
    private int posEliminar;
    
	public void reset()
	{
		//this.estado = "";
		this.serviciosSirc = new HashMap();
		this.serviciosSirc.put("numRegistros", "0");
		this.serviciosSircDetalleMap = new HashMap();
		this.serviciosSircDetalleMap.put("numRegistros","0");
		this.serviciosSircDetalleEliminadoMap = new HashMap();
		this.serviciosSircDetalleEliminadoMap.put("numRegistros","0");
		this.serviciosSircEliminado = new HashMap();
		this.serviciosSircEliminado.put("numRegistros", "0");		

		this.ubicacionConfig="";
		this.linkSiguiente = "/";		
		this.maxPageItems = 20;
		this.offset = 0;
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";		
	}
	
	
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			if(this.estado.equals("guardar"))
			{
				int numReg=Integer.parseInt(this.serviciosSirc.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					try
					{
						Integer.parseInt(this.serviciosSirc.get("codigo_"+i)+"");
						if((this.serviciosSirc.get("codigo_"+i)+"").trim().equals(""))
						{
							errores.add("codigo", new ActionMessage("errors.required","El CÛdigo del registro "+(i+1)));
						}
						else
						{
							for(int j=0;j<i;j++)
							{
								if((this.serviciosSirc.get("codigo_"+i)+"").equalsIgnoreCase(this.serviciosSirc.get("codigo_"+j)+""))
								{
									errores.add("", new ActionMessage("errors.yaExiste","El cÛdigo "+this.serviciosSirc.get("codigo_"+i)));
								}
							}
						}
					}
					catch(Exception e)
					{
						errores.add("codigo", new ActionMessage("errors.integer","El CÛdigo del registro "+(i+1)));
					}
					if((this.serviciosSirc.get("descripcion_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","La DescripciÛn del registro "+(i+1)));
					}
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
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * 
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxPageItems() {
		return maxPageItems; ///2
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
	public HashMap getServiciosSirc() {
		return serviciosSirc;
	}

	/**
	 * 
	 * @param serviciosSirc
	 */
	public void setServiciosSirc(HashMap serviciosSirc) {
		this.serviciosSirc = serviciosSirc;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getServiciosSirc(String key)
	{
		return serviciosSirc.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void  setServiciosSirc(String key , Object value)
	{
		this.serviciosSirc.put(key, value);
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


	public HashMap getServiciosSircEliminado() {
		return serviciosSircEliminado;
	}


	public void setServiciosSircEliminado(HashMap serviciosSircEliminado) {
		this.serviciosSircEliminado = serviciosSircEliminado;
	}

	public Object getServiciosSircEliminado(String key )
	{
		return serviciosSircEliminado.get(key);
	}
	
	public void setServiciosSircEliminado(String key, Object value)
	{
		serviciosSircEliminado.put(key, value);
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the indexServSirc
	 */
	public String getIndexServSirc() {
		return indexServSirc;
	}


	/**
	 * @param indexServSirc the indexServSirc to set
	 */
	public void setIndexServSirc(String indexServSirc) {
		this.indexServSirc = indexServSirc;
	}


	/**
	 * @return the ubicacionConfig
	 */
	public String getUbicacionConfig() {
		return ubicacionConfig;
	}


	/**
	 * @param ubicacionConfig the ubicacionConfig to set
	 */
	public void setUbicacionConfig(String ubicacionConfig) {
		this.ubicacionConfig = ubicacionConfig;
	}


	/**
	 * @return the serviciosSircDetalleEliminadoMap
	 */
	public HashMap getServiciosSircDetalleEliminadoMap() {
		return serviciosSircDetalleEliminadoMap;
	}
	
	/**
	 * @return the Object serviciosSircDetalleEliminadoMap
	 */
	public Object getServiciosSircDetalleEliminadoMap(String key) {
		return serviciosSircDetalleEliminadoMap.get(key);
	}


	/**
	 * @param serviciosSircDetalleEliminadoMap the serviciosSircDetalleEliminadoMap to set
	 */
	public void setServiciosSircDetalleEliminadoMap(
			HashMap serviciosSircDetalleEliminadoMap) {
		this.serviciosSircDetalleEliminadoMap = serviciosSircDetalleEliminadoMap;
	}
	
	/**
	 * @param String key
	 * @param Object value
	 */
	public void setServiciosSircDetalleEliminadoMap(String key, Object value) {
		this.serviciosSircDetalleEliminadoMap.put(key, value);
	}


	/**
	 * @return the serviciosSircDetalleMap
	 */
	public HashMap getServiciosSircDetalleMap() {
		return serviciosSircDetalleMap;
	}
	
	/**
	 * @param String key
	 * */
	public Object getServiciosSircDetalleMap(String key)
	{
		return serviciosSircDetalleMap.get(key);		
	}

	/**
	 * @param String key
	 * @param Object value
	 */
	public void setServiciosSircDetalleMap(String key, Object value) {
		this.serviciosSircDetalleMap.put(key, value);
	}
	
	/**
	 * @return the serviciosSircDetalleMap
	 */
	public void setServiciosSircDetalleMap(HashMap setServiciosSircDetalleMap) {
		this.serviciosSircDetalleMap = setServiciosSircDetalleMap;
	}


	/**
	 * @return the indexEliminado
	 */
	public String getIndexEliminado() {
		return indexEliminado;
	}


	/**
	 * @param indexEliminado the indexEliminado to set
	 */
	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}


	/**
	 * @return the patronOrdenarServicioSircDetalle
	 */
	public String getPatronOrdenarServicioSircDetalle() {
		return patronOrdenarServicioSircDetalle;
	}


	/**
	 * @param patronOrdenarServicioSircDetalle the patronOrdenarServicioSircDetalle to set
	 */
	public void setPatronOrdenarServicioSircDetalle(
			String patronOrdenarServicioSircDetalle) {
		this.patronOrdenarServicioSircDetalle = patronOrdenarServicioSircDetalle;
	}


	/**
	 * @return the codServInsert
	 */
	public String getCodServInsert() {
		return codServInsert;
	}


	/**
	 * @param codServInsert the codServInsert to set
	 */
	public void setCodServInsert(String codServInsert) {
		this.codServInsert = codServInsert;
	}
}