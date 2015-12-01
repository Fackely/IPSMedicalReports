/*
 * @(#)SignosSintomasXSistemaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;

/**
 * Form que contiene todos los datos específicos para generar 
 * los signos sintomas x sistema
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. May 31, 2065
 * @author wrios
 */
public class SignosSintomasXSistemaForm extends ValidatorForm
{
	/**
	 * codigo del motivo consulta
	 */
	private String codigoMotivoConsulta;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Mapa 
	 */
	private HashMap mapa;
	
	/**
	 * Mapa que se obtuvo de la BD,
	 * el cual no ha tenido modificación alguna
	 */
	private HashMap mapaNoModificado;
	
	/**
	 * Mapa auxiliar
	 */
	private HashMap mapaAux;

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
	 * Cód del index del Mapa
	 */
	private int indexMapa;
	
	/**
	 * muestra el mensaje de grabacion existosa
	 */
	private boolean mostrarMensaje;
	
	/**
	 * Número de Items mostrados por página, 
	 * Se definió en este punto porque al ingresar un nuevo elemento
	 * al pager, se debe hacer un cálculo de l última ésta página y en este punto
	 * se debe conocer el número de items. 
	 */
	public int maxPagesItemsHash;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Mapa con todos los signos y sintomas
	 */
	private ArrayList<HashMap<String, Object>> signosYSintomas;
	
	/**
	 * Mapa con todos los signos y sintomas
	 */
	private ArrayList<HashMap<String, Object>> calificacionTriaje;
	
	/**
	 * Mapa con todos los signos y sintomas
	 */
	private ArrayList<HashMap<String, Object>> motivosConsultaUrg;
	
	 /**
     * @return Returns the maxPagesItemsHash.
     */
    public int getMaxPagesItemsHash() {
        return 100; 
        //se deja abierto por si se cambia la filosofia
        //Integer.parseInt(ValoresPorDefecto.getMaxPageItems(this.codigoInstitucion));
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
		if(estado.equals("guardar"))
		{
		    errores=super.validate(mapping, request);
		    for(int i=0; i<this.getNumeroRealFilasMapa(); i++)
			{
		        if(!UtilidadTexto.getBoolean((this.getMapa("eseliminada_"+i)+"")))
		        {
		            if(    (this.getMapa("consecutivosignosintoma_"+i)+"").equals("")  ||
		                   (this.getMapa("consecutivosignosintoma_"+i)+"").equals("null")   )
		            {
		                errores.add("Signos y Sintomas requerido", new ActionMessage("errors.required","El campo Signos y Sintomas "));
		            }
		            if( (this.getMapa("consecutivocategoriatriage_"+i)+"").equals("")  ||
			            (this.getMapa("consecutivocategoriatriage_"+i)+"").equals("null")   )
			        {
			            errores.add("Calificacion triage requerido", new ActionMessage("errors.required","El campo Calificación Triage "));
			        }
		            if(!errores.isEmpty())
					{
		                this.setEstado("redireccion");
					}		
		        }
			}
		}
		return errores;
	}
	
	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset(boolean resetTodo)
	{
		if(resetTodo)
		{	
			resetMapa();
			this.codigoMotivoConsulta="";
			this.patronOrdenar = "";
		 	this.ultimoPatron = "";
		}	
	    this.maxPagesItemsHash=100; //se deja abierto por si se quiere mapa Integer.parseInt(ValoresPorDefecto.getMaxPageItems(this.getCodigoInstitucion()));
	    this.linkSiguiente="";
	    this.offsetHash=0;
	    this.mostrarMensaje=false;
	}
	
	/**
	 * reset de los mapas
	 *
	 */
	public void resetMapa()
	{
	    this.mapa=new HashMap();
	    this.mapaNoModificado= new HashMap();
	    this.mapaAux= new HashMap();
	}
    
	/**
	 * Metodo para insertar los valores en el mapa aux que va ha contener los datos modificados
	 * @param k
	 * @param keyMapa
	 * @return
	 */
	private boolean modificarMapaAux(int k, String keyMapa)	
	{
		boolean fueInsertadoEnMapa=false;
	    String campoMapaOriginalNoMod=this.getMapaNoModificado(keyMapa+k)+"";
	    String campoMapaModificado="";
	    
	    if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
        {
	        campoMapaModificado=this.getMapa(keyMapa+k)+"";
	        
	        
	        if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
            {
	            mapaAux.put("consecutivosignosintoma_"+ k, this.getMapa("consecutivosignosintoma_"+k));
	            mapaAux.put("consecutivocategoriatriage_"+ k, this.getMapa("consecutivocategoriatriage_"+k));
	            mapaAux.put("codigo_"+ k, this.getMapa("codigo_"+k));
	            mapaAux.put("numRegistros", (Integer.parseInt(mapaAux.get("numRegistros").toString())+1)+"");
	            fueInsertadoEnMapa=true;
            }    
        }    
	    return fueInsertadoEnMapa;
	}
	
	/**
	 * Método que compara los dos Mapas Original - Modificado, y los compara hasta el
	 * size del original y devuelve un nuevo HashMap con los campos que han sido modificados.
	 * @return
	 */
	public boolean comparar2HashMap()
	{
	    this.mapaAux= new HashMap();
	    this.mapaAux.put("numRegistros", "0");
	    boolean fueInsertadoEnMapa=false;
	    
	    for(int k=0; k<getNumeroRealFilasMapa(); k++)
	    {
	        if(UtilidadTexto.getBoolean((this.getMapa("estabd_"+k)+"")))
	        {
	           fueInsertadoEnMapa=this.modificarMapaAux(k, "consecutivosignosintoma_");
	           { 
	        	   if(!fueInsertadoEnMapa)
                   {
	        		   fueInsertadoEnMapa=this.modificarMapaAux(k, "consecutivocategoriatriage_");
                   }   
	           }    
	        }    
	    }
	    return fueInsertadoEnMapa;
	}
	
	/**
	 * Set del mapa 
	 * @param key
	 * @param value
	 */
	public void setMapa(String key, Object value){
		mapa.put(key, value);
	}
	/**
	 * Get del mapa  
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapa(String key){
		return mapa.get(key);
	}
	/**
	 * Set del mapa no modificado
	 * @param key
	 * @param value
	 */
	public void setMapaNoModificado(String key, Object value){
		mapaNoModificado.put(key, value);
	}
	/**
	 * Get del mapa no modificado
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaNoModificado(String key){
		return mapaNoModificado.get(key);
	}
	/**
	 * Set del mapa auxiliar
	 * @param key
	 * @param value
	 */
	public void setMapaAux(String key, Object value){
		mapaAux.put(key, value);
	}
	/**
	 * Get del mapa auxiliar
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaAux(String key){
		return mapaAux.get(key);
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
     * @param numeroRealFilasMapa The numeroRealFilasMapa to get.
     */
    public int getNumeroRealFilasMapa()
    {
    	if(this.mapa.containsKey("numRegistros"))
    		return Integer.parseInt(this.mapa.get("numRegistros").toString());
    	else 
    		return 0;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaNoMod()
    {
    	if(this.mapaNoModificado.containsKey("numRegistros"))
    		return Integer.parseInt(this.mapaNoModificado.get("numRegistros").toString());
    	else 
    		return 0;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaAux(){
    	if(this.mapaAux.containsKey("numRegistros"))
    		return Integer.parseInt(this.mapaAux.get("numRegistros").toString());
    	else 
    		return 0;
    }
    /**
     * @return Returns the indexMapa.
     */
    public int getIndexMapa() {
        return indexMapa;
    }
    /**
     * @param indexMapa The indexMapa to set.
     */
    public void setIndexMapa(int indexMapa) {
        this.indexMapa = indexMapa;
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
	 * @return Returns the codigoMotivoConsulta.
	 */
	public String getCodigoMotivoConsulta() {
		return codigoMotivoConsulta;
	}

	/**
	 * @param codigoMotivoConsulta The codigoMotivoConsulta to set.
	 */
	public void setCodigoMotivoConsulta(String codigoMotivoConsulta) {
		this.codigoMotivoConsulta = codigoMotivoConsulta;
	}

	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapa() {
		return mapa;
	}

	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Returns the mapaAux.
	 */
	public HashMap getMapaAux() {
		return mapaAux;
	}

	/**
	 * @param mapaAux The mapaAux to set.
	 */
	public void setMapaAux(HashMap mapaAux) {
		this.mapaAux = mapaAux;
	}

	/**
	 * @return Returns the mapaNoModificado.
	 */
	public HashMap getMapaNoModificado() {
		return mapaNoModificado;
	}

	/**
	 * @param mapaNoModificado The mapaNoModificado to set.
	 */
	public void setMapaNoModificado(HashMap mapaNoModificado) {
		this.mapaNoModificado = mapaNoModificado;
	}

	/**
	 * @param maxPagesItemsHash The maxPagesItemsHash to set.
	 */
	public void setMaxPagesItemsHash(int maxPagesItemsHash) {
		this.maxPagesItemsHash = maxPagesItemsHash;
	}

	/**
	 * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
	 */
	public void setNumeroRealFilasMapa(int numeroRealFilasMapa) {
		this.setMapa("numRegistros", numeroRealFilasMapa);
	}

	/**
	 * @return Returns the mostrarMensaje.
	 */
	public boolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * @param mostrarMensaje The mostrarMensaje to set.
	 */
	public void setMostrarMensaje(boolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
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
	 * @return the calificacionTriaje
	 */
	public ArrayList<HashMap<String, Object>> getCalificacionTriaje() {
		return calificacionTriaje;
	}

	/**
	 * @param calificacionTriaje the calificacionTriaje to set
	 */
	public void setCalificacionTriaje(
			ArrayList<HashMap<String, Object>> calificacionTriaje) {
		this.calificacionTriaje = calificacionTriaje;
	}

	/**
	 * @return the motivosConsultaUrg
	 */
	public ArrayList<HashMap<String, Object>> getMotivosConsultaUrg() {
		return motivosConsultaUrg;
	}

	/**
	 * @param motivosConsultaUrg the motivosConsultaUrg to set
	 */
	public void setMotivosConsultaUrg(
			ArrayList<HashMap<String, Object>> motivosConsultaUrg) {
		this.motivosConsultaUrg = motivosConsultaUrg;
	}

	/**
	 * @return the signosYSintomas
	 */
	public ArrayList<HashMap<String, Object>> getSignosYSintomas() {
		return signosYSintomas;
	}

	/**
	 * @param signosYSintomas the signosYSintomas to set
	 */
	public void setSignosYSintomas(
			ArrayList<HashMap<String, Object>> signosYSintomas) {
		this.signosYSintomas = signosYSintomas;
	}

	
	
}