/*
 * @(#)RequisitosPacienteForm.java
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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


import util.ConstantesBD;
import util.Utilidades;


/**
 * Form que contiene todos los datos específicos para generar 
 * el Registro de los requisitos del paciente
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Nov 22, 2004
 * @author wrios 
 */
public class RequisitosPacienteForm extends ValidatorForm
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RequisitosPacienteForm.class);
	/**
	 * Mapa con los requisitos
	 */
	private HashMap mapaRequisitos;
	
	/**
	 * Mapa con los requisitos que se obtuvo de la BD,
	 * el cual no ha tenido modificación alguna
	 */
	private HashMap mapaRequisitosNoModificado;
	
	/**
	 * Descripción del requisito
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
	 * Elimina un elemento del Hash Map
	 * Dada su llave;
	 */
	private String keyDelete;
	
	
	
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
	 * String que me indica si son requisitos
	 * por paciente o por radicacion
	 */
	private String tipoRequisitos;
	
	/**
	 * 
	 */
	private String procesoRealizado;
	
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
			if(this.descripcion.equals("") || this.descripcion==null || !Utilidades.validarEspacios(this.descripcion))
			{
				errores.add("Campo Descripción Requisito vacio", new ActionMessage("errors.required","Si desea ingresar un requisito nuevo entonces el campo descripción "));
			}
			else
	        {
	        	for(int i=0;i<this.getNumeroRealFilasMapa();i++)
	        	{
	        		if((this.getMapaRequisitos("descripcion_"+i)+"").trim().equals(this.descripcion.trim()) )  
		             {		                  		                  
		                  errores.add("", new ActionMessage("errors.yaExiste","El Requisito "+this.descripcion+""));                 
		             }
	        	}
	        }
		}	
		else if(estado.equals("guardar"))
		{
		    errores=super.validate(mapping,request);
		    String tempoDesc="";
		    for(int k=0; k<this.getNumeroRealFilasMapa(); k++)
		    {
		        tempoDesc=this.getMapaRequisitos("descripcion_"+k)+"";
		        if(tempoDesc.equals("") || tempoDesc.equals("null") || tempoDesc==null || !Utilidades.validarEspacios(tempoDesc))
		        {
		            errores.add("Campo Descripción Requisito vacio", new ActionMessage("errors.required","El campo descripción en el registro N° "+(k+1)));
		        }
		        
		        tempoDesc = this.getMapaRequisitos("tipo_requisito_"+k).toString();
		        if(tempoDesc.equals(""))
		        	errores.add("", new ActionMessage("errors.required","El campo clasificación en el registro N° "+(k+1)));
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
	    
	    for(int k=0; k<Integer.parseInt(this.mapaRequisitosNoModificado.get("numRegistros").toString()); k++)
	    {
	        campoMapaOriginalNoMod=this.getMapaRequisitosNoModificado("descripcion_"+k)+"";
	        
	        if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
	        {
	            campoMapaModificado=this.getMapaRequisitos("descripcion_"+k)+"";
	            
	            
	            if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
	            {
	                mapaCamposModificados.put("codigo_"+k, mapaRequisitos.get("codigo_"+k));
	                mapaCamposModificados.put("descripcion_"+k, mapaRequisitos.get("descripcion_"+k));
	                mapaCamposModificados.put("tipo_requisito_"+k, mapaRequisitos.get("tipo_requisito_"+k));
	                mapaCamposModificados.put("activo_"+k, mapaRequisitos.get("activo_"+k));
	                mapaCamposModificados.put("puedoborrar_"+k,"f");
	            
	            }
	            /*de lo contrario entonces compare el activo*/
	            else
	            {
	                campoMapaOriginalNoMod=this.getMapaRequisitosNoModificado("activo_"+k)+"";
	                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
	    	        {
	                    campoMapaModificado=this.getMapaRequisitos("activo_"+k)+"";
	                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
	    	            {
	                        mapaCamposModificados.put("codigo_"+k, mapaRequisitos.get("codigo_"+k));
	    	                mapaCamposModificados.put("descripcion_"+k, mapaRequisitos.get("descripcion_"+k));
	    	                mapaCamposModificados.put("tipo_requisito_"+k, mapaRequisitos.get("tipo_requisito_"+k));
	    	                mapaCamposModificados.put("activo_"+k, mapaRequisitos.get("activo_"+k));
	    	                mapaCamposModificados.put("puedoborrar_"+k,"f");
	    	            }
	                    /*De lo contrario se compara el campo tipo requisito*/
		                else
		                {
		                	campoMapaOriginalNoMod=this.getMapaRequisitosNoModificado("tipo_requisito_"+k)+"";
			                if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
			    	        {
			                    campoMapaModificado=this.getMapaRequisitos("tipo_requisito_"+k)+"";
			                    if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
			    	            {
			                        mapaCamposModificados.put("codigo_"+k, mapaRequisitos.get("codigo_"+k));
			    	                mapaCamposModificados.put("descripcion_"+k, mapaRequisitos.get("descripcion_"+k));
			    	                mapaCamposModificados.put("tipo_requisito_"+k, mapaRequisitos.get("tipo_requisito_"+k));
			    	                mapaCamposModificados.put("activo_"+k, mapaRequisitos.get("activo_"+k));
			    	                mapaCamposModificados.put("puedoborrar_"+k,"f");
			    	            }
			    	        }
		                }
	    	        }
	               
	            }
	        }
	    }
	    
	    mapaCamposModificados.put("numRegistros",this.mapaRequisitos.get("numRegistros"));
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
	    this.keyDelete="";
	    this.procesoRealizado = ConstantesBD.acronimoNo;
	}

	public void resetTipoRequisito()
	{
	    this.tipoRequisitos="";
	    this.procesoRealizado = ConstantesBD.acronimoNo;
	}
	
	
	public void resetMapa()
	{
	    this.mapaRequisitos=new HashMap();
	    this.mapaRequisitosNoModificado= new HashMap();
	    this.procesoRealizado = ConstantesBD.acronimoNo;
	}
	
	/**
	 * Set del mapa de requisitos paciente
	 * @param key
	 * @param value
	 */
	public void setMapaRequisitos(String key, Object value){
		mapaRequisitos.put(key, value);
	}
	/**
	 * Get del mapa de requisitos paciente
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaRequisitos(String key){
		return mapaRequisitos.get(key);
	}
	/**
	 * Set del mapa de requisitos paciente no modificado
	 * @param key
	 * @param value
	 */
	public void setMapaRequisitosNoModificado(String key, Object value){
		mapaRequisitosNoModificado.put(key, value);
	}
	/**
	 * Get del mapa de requisitos paciente no modificado
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaRequisitosNoModificado(String key){
		return mapaRequisitosNoModificado.get(key);
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
     * @return Returns the mapaRequisitos.
     */
    public HashMap getMapaRequisitos() {
        return mapaRequisitos;
    }
    /**
     * @param mapaRequisitos The mapaRequisitos to set.
     */
    public void setMapaRequisitos(HashMap mapaRequisitos) {
        this.mapaRequisitos = mapaRequisitos;
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
    	try
    	{
    		return Integer.parseInt(mapaRequisitos.get("numRegistros").toString());
    	}
    	catch(Exception e)
    	{
    		return 0;
    	}
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaNoMod()
    {
    	try
    	{
    		return Integer.parseInt(mapaRequisitosNoModificado.get("numRegistros").toString());
    	}
    	catch(Exception e)
    	{
    		return 0;
    	}
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
     * @return Returns the mapaRequisitosNoModificado.
     */
    public HashMap getMapaRequisitosNoModificado() {
        return mapaRequisitosNoModificado;
    }
    /**
     * @param mapaRequisitosNoModificado The mapaRequisitosNoModificado to set.
     */
    public void setMapaRequisitosNoModificado(HashMap mapaRequisitosNoModificado) {
        this.mapaRequisitosNoModificado = mapaRequisitosNoModificado;
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
	
    /**
     * @return Returns the tipoRequisitos.
     */
    public String getTipoRequisitos() {
        return tipoRequisitos;
    }
    /**
     * @param tipoRequisitos The tipoRequisitos to set.
     */
    public void setTipoRequisitos(String tipoRequisitos) {
        this.tipoRequisitos = tipoRequisitos;
    }

	/**
	 * @return the procesoRealizado
	 */
	public String getProcesoRealizado() {
		return procesoRealizado;
	}

	/**
	 * @param procesoRealizado the procesoRealizado to set
	 */
	public void setProcesoRealizado(String procesoRealizado) {
		this.procesoRealizado = procesoRealizado;
	}
    
}
