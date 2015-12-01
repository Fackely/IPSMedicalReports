/*
 * @(#)MotivoAnluacionFacturasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.facturacion;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


/**
 * Form que contiene todos los datos específicos para generar 
 * el Registro de los motivos de anulacion de facturas
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. May 06, 2005
 * @author cperalta 
 */
public class MotivoAnulacionFacturasForm extends ValidatorForm 
{
	/**
	 * Mapa con los motivos de anulacion
	 */
	private HashMap mapaMotivos;
	
	/**
	 * Mapa con los motivos de anulacion  que se obtuvo de la BD,
	 * el cual no ha tenido modificación alguna
	 */
	private HashMap mapaMotivosNoModificado;
	
	/**
	 * Descripción del motivo de anulacion
	 */
	private String descripcion;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";

	/**
	 * Offset del HashMap
	 */
	private int offsetHash;
	
	/**
	 * La consulta de la info de motivos de anulacion está devolviendo 
	 * actualmente estos campos= (codigo, descripcion, activo, puedoborrar),
	 * como cada uno de ellos ocupa una posición en el HashMap, entonces
	 * toca calcular el verdadero tamanio en filas= HashMap.size/4;
	 */
	private int numeroRealFilasMapa;
	
	/**
	 * Elimina un elemento del Hash Map
	 * Dada su llave;
	 */
	private String keyDelete;
	
	  /**
	 * //num de columnas que devuelve la consulta de la info de los 
	 * motivos de anulacion en este caso (codigo, descripcion, activo, puedoborrar),
	 * este valor es necesaario a la hora de calcular el numero de filas del mapa 
	 */
	final int tamanioNumeroColumnas=4;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta 
	 * como para el resumen
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * Número de Items mostrados por página, 
	 * Se definió en este punto porque al ingresar un nuevo elemento
	 * al pager, se debe hacer un cálculo de l última ésta página y en este punto
	 * se debe conocer el número de items. 
	 * 
	 */
	public final int maxPagesItemsHash=8;
	
    /**
     * @return Returns the maxPagesItemsHash.
     */
    public int getMaxPagesItemsHash() {
        return maxPagesItemsHash;
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
		if(estado.equals("ingresarNuevo"))
		{				
			errores=super.validate(mapping,request);
			if(this.descripcion.trim().equals("") || this.descripcion==null)
			{
				errores.add("Campo Descripción Motivo de Anulación vacio", new ActionMessage("errors.required","Si desea ingresar un motivo de anulación nuevo entonces el campo descripción "));
			}
		    String descBD="";
		    	for(int j=0; j<this.getNumeroRealFilasMapa(); j++)
		    	{
		    		descBD=this.getMapaMotivos("descripcion_"+j)+"".trim();
			        if(this.getDescripcion().toLowerCase().trim().equals(descBD))
			        {
			            errores.add("Motivo de Anulación duplicado", new ActionMessage("error.facturacion.motivoAnulDuplicado",this.getDescripcion()));
			        }
		    	}
			
		}
		else if(estado.equals("guardar"))
		{
		    errores=super.validate(mapping,request);
		    String tempoDesc="";
		    for(int k=0; k<this.getNumeroRealFilasMapaNoMod(); k++)
		    {
		        tempoDesc=this.getMapaMotivos("descripcion_"+k)+"";
		        if(tempoDesc.trim().equals("") || tempoDesc.equals("null") || tempoDesc==null)
		        {
		            errores.add("Campo Descripción Motivo de Anulación vacio", new ActionMessage("errors.required","Si desea modificar un motivo de anulación entonces el campo descripción "));
		        }
		    }
		       
		}
		else if(estado.equals("ingresarNuevo"))
		{
		    errores=super.validate(mapping,request);
		    String descMapa="";
		    String descBD="";
		    for(int k=0; k<this.getNumeroRealFilasMapaNoMod(); k++)
		    {
		    	descMapa=this.getMapaMotivosNoModificado("descripcion_"+k)+"".trim();
		    	for(int j=0; j<this.getNumeroRealFilasMapa(); j++)
		    	{
		    		descBD=this.getMapaMotivos("descripcion_"+j)+"".trim();
			        if(descMapa.equals(descBD))
			        {
			            errores.add("Campo Descripción Motivo de Anulación vacio", new ActionMessage("errors.required","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx "));
			        }
		    	}
		    }
		       
		}
		return errores;
	}
	
	/**
	 * Método que compara los dos Mapas Original - Modificado, y los compara hasta el
	 * size del original y devuelve un nuevo HashMap con los campos que han sido modificados.
	 * @return
	 */
	public HashMap comparar2HashMap()
	{
	    HashMap mapaCamposModificados=new HashMap();
	    String campoMapaOriginalNoMod="", campoMapaModificado=""; 
	    for(int k=0; k<mapaMotivosNoModificado.size()/this.tamanioNumeroColumnas; k++)
	    {
	        campoMapaOriginalNoMod=this.getMapaMotivosNoModificado("descripcion_"+k)+"";
	        
	        if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
	        {
	            campoMapaModificado=this.getMapaMotivos("descripcion_"+k)+"";
	            
	            if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
	            {
	                mapaCamposModificados.put("codigo_"+k, mapaMotivos.get("codigo_"+k));
	                mapaCamposModificados.put("descripcion_"+k, mapaMotivos.get("descripcion_"+k));
	                mapaCamposModificados.put("activo_"+k, mapaMotivos.get("activo_"+k));
	                mapaCamposModificados.put("puedoborrar_"+k,"f");
	            }
	            /*de lo contrario entonces compare el activo*/
	            else
	            {
	                campoMapaOriginalNoMod=this.getMapaMotivosNoModificado("activo_"+k)+"";
	                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
	    	        {
	                    campoMapaModificado=this.getMapaMotivos("activo_"+k)+"";
	                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
	    	            {
	                        mapaCamposModificados.put("codigo_"+k, mapaMotivos.get("codigo_"+k));
	    	                mapaCamposModificados.put("descripcion_"+k, mapaMotivos.get("descripcion_"+k));
	    	                mapaCamposModificados.put("activo_"+k, mapaMotivos.get("activo_"+k));
	    	                mapaCamposModificados.put("puedoborrar_"+k,"f");
	    	            }
	    	        }
	            }
	        }
	    }
	    return mapaCamposModificados;
	}
	
	/**
	 * resetea los valores de la forma
	 *
	 */
	public void reset()
	{
	    this.descripcion="";
	    this.linkSiguiente="";
	    this.offsetHash=0;
	    this.numeroRealFilasMapa=0;
	    this.keyDelete="";
	}
	
	public void resetMapa()
	{
	    this.mapaMotivos=new HashMap();
	    this.mapaMotivosNoModificado= new HashMap();
	}
	
	/**
	 * Set del mapa de motivos de anulacion facturas
	 * @param key
	 * @param value
	 */
	public void setMapaMotivos(String key, Object value){
		mapaMotivos.put(key, value);
	}
	/**
	 * Get del mapa de motivos de anulacion de facturas
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaMotivos(String key){
		return mapaMotivos.get(key);
	}
	/**
	 * Set del mapa de motivos de anulacion de facturas no modificado
	 * @param key
	 * @param value
	 */
	public void setMapaMotivosNoModificado(String key, Object value){
		mapaMotivosNoModificado.put(key, value);
	}
	/**
	 * Get del mapa de motivos de anulacion no modificado
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaMotivosNoModificado(String key){
		return mapaMotivosNoModificado.get(key);
	}
    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Returns the mapaMotivos.
     */
    public HashMap getMapaMotivos() {
        return mapaMotivos;
    }
    /**
     * @param mapaMotivos The mapaMotivos to set.
     */
    public void setMapaMotivos(HashMap mapaMotivos) {
        this.mapaMotivos = mapaMotivos;
    }
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
    /**
     * @return Returns the offsetHash.
     */
    public int getOffsetHash() {
        return offsetHash;
    }
    /**
     * @param offsetHash The offsetHash to set.
     */
    public void setOffsetHash(int offsetHash) {
        this.offsetHash = offsetHash;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapa()
    {
    	if(numeroRealFilasMapa==0);//Evitar el warning
        return numeroRealFilasMapa=mapaMotivos.size()/tamanioNumeroColumnas;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaNoMod(){
        return mapaMotivosNoModificado.size()/tamanioNumeroColumnas;
    }
    /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa(int numeroRealFilasMapa) {
        this.numeroRealFilasMapa = numeroRealFilasMapa;
    }
    /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa(){	
      this.numeroRealFilasMapa = mapaMotivos.size()/tamanioNumeroColumnas;
    }
    /**
     * @return Returns the keyDelete.
     */
    public String getKeyDelete() {
        return keyDelete;
    }
    /**
     * @param keyDelete The keyDelete to set.
     */
    public void setKeyDelete(String keyDelete) {
        this.keyDelete = keyDelete;
    }
    /**
     * @return Returns the mapaMotivosNoModificado.
     */
    public HashMap getMapaMotivosNoModificado() {
        return mapaMotivosNoModificado;
    }
    /**
     * @param mapaMotivosNoModificado The mapaMotivosNoModificado to set.
     */
    public void setMapaMotivosNoModificado(HashMap mapaMotivosNoModificado) {
        this.mapaMotivosNoModificado = mapaMotivosNoModificado;
    }
	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	/**
	 * Tamanio de la col
	 * @return
	 */
	public int getColSize(){
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset(){
		return offset;
	}
	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i)	{
		offset = i;
	}
}
