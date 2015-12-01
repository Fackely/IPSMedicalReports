/*
 * Pooles.java 
 * Autor		:  jarloc
 * Creado el	:  16-dic-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.actionform.pooles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.Utilidades;


/**
* 
* @version 1.0, 16/12/2004
* @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
*/
public class PoolesForm extends ActionForm
{
    /**
	 * Almacena el maximo de items para el pager
	 */	
	private final int maxPageItems = 5;
	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado;
	
	/**
	 * Accion del porceso
	 */
	private String accion;

	/**
	 * numero de registros actualmente en el HashMap
	 */
	private int numRegistros = 0;
	
	/**
	 * almacena el código de la indtitución
	 */
	private int codigoInstitucion;

	/**
	 * HashMap para almacenar los registros
	 */
	private HashMap mapRegistros;
	
	/**
	 * almacena registros generales
	 */
	private HashMap mapGeneral;
	
	/**
	 * almacena un clon de mapRegistros, original de la BD
	 */
	private HashMap mapRegistrosSinModificar;
	/**
	 * 
	 */
	private String linkSiguiente;
	
	/**
	 * posición del hash_Map
	 */
	private int posActual;
	
	/**
	 * posición del hash_Map en el Form
	 */
	private int posActualForm;
	
	/**
	 * para la navegación del pager
	 */
	private int offset;
	
	/**
	 * código del registro a eliminar
	 */
	private int codigoEliminar;
	
	/**
	 * descripcion del registro a eliminar
	 */
	private String descEliminar;
	
	/**
	 * contiene la posición en el hashMap (General)
	 * de los registros que han sido insertados, modificados ó eliminados
	 */
	private int iterAccionesRegistros;
	
	/**
	 * almacena los códigos que seran eliminados de la BD
	 */
	private ArrayList codigosEliminar;
	
	/**
	 * almacena los datos de la consulta
	 */
	private Collection colPooles;
	
	/**
	 * almacena el log
	 */
	private String log;
	
	public PoolesForm() 
	{
		super();
		reset();
	}

	public void reset() 
	{
		this.numRegistros = 0;
		this.mapRegistros = new HashMap();
		this.mapGeneral = new HashMap ();
		this.codigoInstitucion = -1;
		this.linkSiguiente = "";
		this.posActual = -1;
		this.offset = 0;
		this.codigoEliminar = -1;
		this.accion = "";
		this.codigosEliminar = new ArrayList ();
		this.iterAccionesRegistros = 0;
		this.posActualForm = 0;
		this.mapRegistrosSinModificar = new HashMap ();
		this.colPooles = null;
		this.log = "";
		this.descEliminar = "";
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
       
       if( this.estado.equals("salirGuardar") ) //validaciones campos requeridos
       {			
           for(int k=0; k < this.getNumRegistros(); k ++)
           {
               if( (mapRegistros.get("descripcion_"+k)+"").equals("") ||  !Utilidades.validarEspacios((mapRegistros.get("descripcion_"+k)+"")))   
               {
                   errores.add("", new ActionMessage("errors.required","La descripción para el registro "));
               }
               
               if( Integer.parseInt(mapRegistros.get("tercero_"+k)+"") == -1)   
               {
                   errores.add("", new ActionMessage("errors.required","El nombre del Responsable y el Nit para el registro "));
               }
               
               if( Integer.parseInt(mapRegistros.get("activo_"+k)+"") == -1)   
               {
                   errores.add("", new ActionMessage("errors.required","El campo activo para el registro "));
               }              
           }
       }
       
       if( this.estado.equals("ingresarNuevo") ) //validaciones campos requeridos
       {			
           for(int k=0; k < this.getNumRegistros(); k ++)
           {
               if( (mapRegistros.get("descripcion_"+k)+"").equals("") )   
               {
                   errores.add("", new ActionMessage("errors.required","La descripción para el ingreso de un nuevo poool "));
               }
           }
       }
       
       if( this.estado.equals("salirGuardar") || this.estado.equals("ingresarNuevo")) //validaciones campos requeridos
       {			
           if(getPosActualForm() != 0)
           {
	           String descripcionComparar = (mapRegistros.get("descripcion_"+this.posActualForm)+"").trim();
	           
	           for(int k=0; k < getPosActualForm(); k ++)
	           {
	               String descripcion = (mapRegistros.get("descripcion_"+k)+"").trim();
	               int resp = descripcion.compareToIgnoreCase(descripcionComparar);    
	               if(resp == 0)
	               {
	                   errores.add("", new ActionMessage("error.pooles.existeDescripcion",descripcionComparar));
	               }
	           }
           }
       }
       
       return errores;
   }

    /**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna numRegistros.
     */
    public int getNumRegistros() {
        return numRegistros;
    }
    /**
     * @param numRegistros Asigna numRegistros.
     */
    public void setNumRegistros(int numRegistros) {
        this.numRegistros = numRegistros;
    }
    /**
     * @return Retorna registros.
     */
    public HashMap getMapRegistros() {
        return mapRegistros;
    }
    /**
     * @param registros Asigna registros.
     */
    public void setMapRegistros(HashMap mapRegistros) {
        this.mapRegistros = mapRegistros;
    }
    
    /**
	 * Set del registro pooles
	 * @param key
	 * @param value
	 */
	public void setMapRegistros(String key, Object value) 
	{
	    mapRegistros.put(key, value);
	}
	/**
	 * Get del mapa de registro pooles
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapRegistros(String key) 
	{
		return mapRegistros.get(key);
	}
	/**
     * @return Retorna mapGeneral.
     */
    public HashMap getMapGeneral() {
        return mapGeneral;
    }
    /**
     * @param mapGeneral Asigna mapGeneral.
     */
    public void setMapGeneral(HashMap mapGeneral) {
        this.mapGeneral = mapGeneral;
    }
    /**
	 * Set del registro pooles
	 * @param key
	 * @param value
	 */
	public void setMapGeneral(String key, Object value) 
	{
	    mapGeneral.put(key, value);
	}
	/**
	 * Get del mapa de registro pooles
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapGeneral(String key) 
	{
		return mapGeneral.get(key);
	}
    /**
     * @return Retorna codigoInstitucion.
     */
    public int getCodigoInstitucion() {
        return codigoInstitucion;
    }
    /**
     * @param codigoInstitucion Asigna codigoInstitucion.
     */
    public void setCodigoInstitucion(int codigoInstitucion) {
        this.codigoInstitucion = codigoInstitucion;
    }
    /**
     * @return Retorna linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente Asigna linkSiguiente.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
    }
    /**
     * @return Retorna accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion Asigna accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @return Retorna posActual.
     */
    public int getPosActual() {
        return posActual;
    }
    /**
     * @param posActual Asigna posActual.
     */
    public void setPosActual(int posActual) {
        this.posActual = posActual;
    }
    
    
    /**
     * @return Retorna maxPageItems.
     */
    public int getMaxPageItems() {
        return maxPageItems;
    }
    /**
     * @return Retorna offset.
     */
    public int getOffset() {
        return offset;
    }
    /**
     * @param offset Asigna offset.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    /**
     * @return Retorna codigoEliminar.
     */
    public int getCodigoEliminar() {
        return codigoEliminar;
    }
    /**
     * @param codigoEliminar Asigna codigoEliminar.
     */
    public void setCodigoEliminar(int codigoEliminar) {
        this.codigoEliminar = codigoEliminar;
    }
    /**
     * @return ArrayList, Retorna codigosEliminar.
     */
    public ArrayList getCodigosEliminar() {
        return codigosEliminar;
    }
    /**
     * @param codigo, Object con el código que sera eliminado. 
     */
    public void setCodigosEliminar(Object codigo) {
        this.codigosEliminar.add(codigo);
    }
    /**
     * @return Retorna iterAccionesRegistros.
     */
    public int getIterAccionesRegistros() {
        return iterAccionesRegistros;
    }
    /**
     * @param iterAccionesRegistros Asigna iterAccionesRegistros.
     */
    public void setIterAccionesRegistros(int iterAccionesRegistros) {
        this.iterAccionesRegistros = iterAccionesRegistros;
    }
   
    /**
     * @return Retorna posActualForm.
     */
    public int getPosActualForm() {
        return posActualForm;
    }
    /**
     * @param posActualForm Asigna posActualForm.
     */
    public void setPosActualForm(int posActualForm) {
        this.posActualForm = posActualForm;
    }
    /**
     * @return Retorna mapRegistrosSinModificar.
     */
    public HashMap getMapRegistrosSinModificar() {
        return mapRegistrosSinModificar;
    }
    /**
     * @param mapRegistrosSinModificar Asigna mapRegistrosSinModificar.
     */
    public void setMapRegistrosSinModificar(HashMap mapRegistrosSinModificar) {
        this.mapRegistrosSinModificar = mapRegistrosSinModificar;
    }
    
    /**
     * @param key, String indice
     * @return Object, con el objeto que contiene el indice
     */
    public Object getMapRegistrosSinModificar(String key) 
    {
        return mapRegistrosSinModificar.get(key);
    }
    /**
     * @param key, String indice del registro
     * @param value, Object valor del registro
     */
    public void setMapRegistrosSinModificar(String key, Object value) 
    {
        mapRegistrosSinModificar.put(key,value);
    }
    /**
     * @return Retorna colPooles.
     */
    public Collection getColPooles() {
        return colPooles;
    }
    /**
     * @param colPooles Asigna colPooles.
     */
    public void setColPooles(Collection colPooles) {
        this.colPooles = colPooles;
    }
    /**
     * @return Retorna log.
     */
    public String getLog() {
        return log;
    }
    /**
     * @param log Asigna log.
     */
    public void setLog(String log) {
        this.log = log;
    }
    /**
     * @return Retorna descEliminar.
     */
    public String getDescEliminar() {
        return descEliminar;
    }
    /**
     * @param descEliminar Asigna descEliminar.
     */
    public void setDescEliminar(String descEliminar) {
        this.descEliminar = descEliminar;
    }
}
