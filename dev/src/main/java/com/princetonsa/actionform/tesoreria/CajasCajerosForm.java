/*
 * Created on 15/09/2005
 *
 */
package com.princetonsa.actionform.tesoreria;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * @author artotor
 *
 */
public class CajasCajerosForm extends ValidatorForm 
{
//	------------------------------------------------------------DEFINICION DE VARAIBLES---------------------------------------------------------------------------//
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa paara manejar las cajas 
	 */
	private HashMap mapaCajasCajeros;

	/**
	 * Mapa paara manejar las Eliminadas
	 */
	private HashMap mapaCajasCajerosEliminado;
	/**
	 * Indice del registro que se desea eliminar
	 */
	private int regEliminar;
	
    
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    /**
     * para controlar el numero de registros del
     * HashMap.
     */
    private int numRegistros;
    
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
    
    /**
     * Variable para manejar el mensaje de Operación realizada con exito
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    /****************************************
     * ATRIBUTOS PARA EL PAGER
     ****************************************/
	
    /**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /****************************************
     * FIN ATRIBUTOS PARA EL PAGER
     ****************************************/
    
    
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------//
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardar"))
		{
			for(int k=0;k<numRegistros;k++)
		    {
				if(((this.getMapaCajasCajeros("consecutivocaja_"+k)+"").trim()).equals("")||(this.getMapaCajasCajeros("consecutivocaja_"+k)+"").equals("-1"))  //La caja es requerda
			      {
			          errores.add("CAJA REQUERIDO", new ActionMessage("errors.required","La Caja para el registro "+(k+1)));  
			      }
				if(((this.getMapaCajasCajeros("logincajero_"+k)+"").trim()).equals("")||(this.getMapaCajasCajeros("logincajero_"+k)+"").equals("-1"))  //descripcion es requerido
			      {
			          errores.add("CAJERO REQUERIDO", new ActionMessage("errors.required","El Cajero para el registro "+(k+1)));  
			      }
				
				String caja=this.getMapaCajasCajeros("consecutivocaja_"+k)+"";
				String cajero=this.getMapaCajasCajeros("logincajero_"+k)+"";
				for(int l=0;l<k;l++)
				{
					if(caja.equals(this.getMapaCajasCajeros("consecutivocaja_"+l)+"") && cajero.equals(this.getMapaCajasCajeros("logincajero_"+l)+""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","La Caja "+this.getMapaCajasCajeros("caja_"+l)+" con el Cajero "+cajero));                 
		             }
				}
		    }
		}	
		return errores;
	}
    
    /**
     * inicializar atributos de esta forma
     */
    public void reset ()
    {
    	this.mapaCajasCajeros=new HashMap();
    	this.mapaCajasCajerosEliminado=new HashMap();
    	this.regEliminar=ConstantesBD.codigoNuncaValido;
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.numRegistros=0;
        this.centroAtencion = ConstantesBD.codigoNuncaValido;
        this.linkSiguiente = "";
        this.offset = 0;
        this.maxPageItems = 20;
    }
    
    /**
     * Método que resetea los atributos
     * utilizados para el pager
     */
    public void resetPager()
    {
    	this.maxPageItems = 20;
    	this.linkSiguiente = "";
        this.offset = 0;
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
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	/**
	 * @return Returns the regEliminar.
	 */
	public int getRegEliminar() {
		return regEliminar;
	}
	/**
	 * @param regEliminar The regEliminar to set.
	 */
	public void setRegEliminar(int regEliminar) {
		this.regEliminar = regEliminar;
	}
	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	/**
	 * @return Returns the mapaCajasCajeros.
	 */
	public HashMap getMapaCajasCajeros() {
		return mapaCajasCajeros;
	}
	/**
	 * @param mapaCajasCajeros The mapaCajasCajeros to set.
	 */
	public void setMapaCajasCajeros(HashMap mapaCajasCajeros) {
		this.mapaCajasCajeros = mapaCajasCajeros;
	}
	/**
	 * @return Returns the mapaCajasCajeros.
	 */
	public Object getMapaCajasCajeros(String key) {
		return mapaCajasCajeros.get(key);
	}
	/**
	 * @param mapaCajasCajeros The mapaCajasCajeros to set.
	 */
	public void setMapaCajasCajeros(String key,Object value) {
		this.mapaCajasCajeros.put(key,value);
	}
	/**
	 * @return Returns the mapaCajasCajerosEliminado.
	 */
	public HashMap getMapaCajasCajerosEliminado() {
		return mapaCajasCajerosEliminado;
	}
	/**
	 * @param mapaCajasCajerosEliminado The mapaCajasCajerosEliminado to set.
	 */
	public void setMapaCajasCajerosEliminado(HashMap mapaCajasCajerosEliminado) {
		this.mapaCajasCajerosEliminado = mapaCajasCajerosEliminado;
	}
	/**
	 * @return Returns the mapaCajasCajerosEliminado.
	 */
	public Object getMapaCajasCajerosEliminado(String key) {
		return mapaCajasCajerosEliminado.get(key);
	}
	/**
	 * @param mapaCajasCajerosEliminado The mapaCajasCajerosEliminado to set.
	 */
	public void setMapaCajasCajerosEliminado(String key,Object value) {
		this.mapaCajasCajerosEliminado.put(key,value);
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
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
}