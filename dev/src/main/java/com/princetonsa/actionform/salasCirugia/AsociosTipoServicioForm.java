/*
 * @(#)AsociosTipoServicioForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;

/**
 * Form que contiene todos los datos específicos para generar 
 * asocios tipo servicio 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Sep 21 , 2005
 * @author wrios 
 */
public class AsociosTipoServicioForm extends ValidatorForm
{
    /**
     * codigo del asocio x tipo servicio
     */
    private int codigo;

    /**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Mapa con los asociosTipoServicio
	 */
	private HashMap mapaAsociosTipoServicio= new HashMap();
	
	/**
	 * Mapa con los asociosTipoServicio que se obtuvo de la BD,
	 * el cual no ha tenido modificación alguna
	 */
	private HashMap mapaAsociosTipoServicioNoModificado= new HashMap();
	
	/**
	 * Mapa con los asociosTipoServicio auxiliar
	 */
	private HashMap mapaAsociosTipoServicioAux;
    
	/**
	 * La consulta de la info de asociosTipoServicio está devolviendo 
	 * actualmente estos campos= 
	 * ("codigo",
		"codigoEsquemaTarifarioBoolEsGeneral",
		"nombreEsquemaTarifario",
		"acronimoTipoServicio",
		"nombreTipoServicio",
		"codigoTipoAsocio",
		"acronimoTipoServicioAsocio",
		"nombreTipoServicioAsocio",
		"codigoServicio",
		"descripcionServicio",
		"activo",
		"estaBD", 
		"esEliminada" ),
	 * como cada uno de ellos ocupa una posición en el HashMap, entonces
	 * toca calcular el verdadero tamanio en filas= HashMap.size/13;
	 */
	private int numeroRealFilasMapa;
	
	/**
	 * //num de columnas que devuelve la consulta de la info de los 
	 * asocios x tipo serv en este caso=  
	 * ("codigo",
		"codigoEsquemaTarifarioBoolEsGeneral",
		"nombreEsquemaTarifarioGeneral",
		"acronimoTipoServicio",
		"nombreTipoServicio",
		"codigoTipoAsocio",
		"acronimoTipoServicioAsocio",
		"nombreTipoServicioAsocio",
		"codigoServicio",
		"descripcionServicio",
		"activo",
		"estaBD", 
		"esEliminada" ),
	 * este valor es necesario a la hora de calcular el numero de filas del mapa 
	 */
	final int tamanioNumeroColumnas=13;
	
	/**
	 * Cód del index del Mapa
	 */
	private int indexMapa;
	
	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset()
	{
	    this.codigo=ConstantesBD.codigoNuncaValido;
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
	    errores=super.validate(mapping,request);
	    
	    if(estado.equals("guardar"))
	    {
	        for(int i=0; i<this.getNumeroRealFilasMapa(); i++)
			{
		        if((this.getMapaAsociosTipoServicio("esEliminada_"+i)+"").equals("f"))
		        {
		            if((this.getMapaAsociosTipoServicio("codigoEsquemaTarifarioBoolEsGeneral_"+i)+"").trim().split("@@@")[0].equals("") || 
		                (this.getMapaAsociosTipoServicio("codigoEsquemaTarifarioBoolEsGeneral_"+i)+"").trim().split("@@@")[0].equals("-1") ||
		                (this.getMapaAsociosTipoServicio("codigoEsquemaTarifarioBoolEsGeneral_"+i)+"").trim().split("@@@")[0].equals("null"))
		            {    
		                errores.add("El esquema Tarifario requerido", new ActionMessage("errors.required","El campo esquema Tarifario "));
		            }    
		            if( (this.getMapaAsociosTipoServicio("acronimoTipoServicio_"+i)+"").trim().equals("")  ||
		                (this.getMapaAsociosTipoServicio("acronimoTipoServicio_"+i)+"").trim().equals("-1")   ||  
		                (this.getMapaAsociosTipoServicio("acronimoTipoServicio_"+i)+"").trim().equals("null")   )
			        {
			            errores.add("El Tipo Servicio requerido", new ActionMessage("errors.required","El campo Tipo Servicio "));
			        }
			        if( (this.getMapaAsociosTipoServicio("codigoTipoAsocio_"+i)+"").trim().equals("")  ||
		                (this.getMapaAsociosTipoServicio("codigoTipoAsocio_"+i)+"").trim().equals("-1")   ||  
		                (this.getMapaAsociosTipoServicio("codigoTipoAsocio_"+i)+"").trim().equals("null")   )
			        {
			            errores.add("El Tipo Asocio requerido", new ActionMessage("errors.required","El campo Tipo Asocio "));
			        }
		        }
			}
	    }
	    return errores;
	}
	
	/**
	 * reset del mapa
	 *
	 */
	public void resetMapa()
	{
	    this.mapaAsociosTipoServicio= new HashMap();
	    this.mapaAsociosTipoServicioNoModificado= new HashMap();
	    this.mapaAsociosTipoServicioAux= new HashMap();
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
	    String campoMapaOriginalNoMod=this.getMapaAsociosTipoServicioNoModificado(keyMapa+k)+"";
	    String campoMapaModificado="";
	    if(campoMapaOriginalNoMod!=null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
        {
	        campoMapaModificado=this.getMapaAsociosTipoServicio(keyMapa+k)+"";
	        if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
            {
	            mapaAsociosTipoServicioAux.put("codigo_"+ k, this.getMapaAsociosTipoServicio("codigo_"+k));
	            mapaAsociosTipoServicioAux.put("codigoEsquemaTarifarioBoolEsGeneral_"+ k, this.getMapaAsociosTipoServicio("codigoEsquemaTarifarioBoolEsGeneral_"+k));
	            mapaAsociosTipoServicioAux.put("acronimoTipoServicio_"+ k, this.getMapaAsociosTipoServicio("acronimoTipoServicio_"+k));
	            mapaAsociosTipoServicioAux.put("codigoTipoAsocio_"+ k, this.getMapaAsociosTipoServicio("codigoTipoAsocio_"+k));
			    mapaAsociosTipoServicioAux.put("codigoServicio_"+ k, this.getMapaAsociosTipoServicio("codigoServicio_"+k));
			    mapaAsociosTipoServicioAux.put("activo_"+ k, this.getMapaAsociosTipoServicio("activo_"+k));
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
	    this.mapaAsociosTipoServicioAux= new HashMap();
	    boolean fueInsertadoEnMapa=false;
	    for(int k=0; k<mapaAsociosTipoServicioNoModificado.size()/this.tamanioNumeroColumnas; k++)
	    {
	        if((this.getMapaAsociosTipoServicio("estaBD_"+k)+"").equals("t"))
	        {
               fueInsertadoEnMapa=this.modificarMapaAux(k, "codigoServicio_");
		       if(!fueInsertadoEnMapa)
		       {
		           fueInsertadoEnMapa=this.modificarMapaAux(k, "activo_");
			   }
	        }    
	    }
	    return fueInsertadoEnMapa;
	}
	
	/**
	 * Set del mapa de AsociosTipoServicio
	 * @param key
	 * @param value
	 */
	public void setMapaAsociosTipoServicio(String key, Object value){
		mapaAsociosTipoServicio.put(key, value);
	}
	/**
	 * Get del mapa de AsociosTipoServicio
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaAsociosTipoServicio(String key){
		return mapaAsociosTipoServicio.get(key);
	}
	 /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa()
    {
    	if(numeroRealFilasMapa==0);//Evitar el warning
      this.numeroRealFilasMapa = mapaAsociosTipoServicio.size()/tamanioNumeroColumnas;
    }
    /**
     * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
     */
    public void setNumeroRealFilasMapa(int numeroRealFilasMapa) {
        this.numeroRealFilasMapa = numeroRealFilasMapa;
    }
	
    /**
	 * Set del mapa de AsociosTipoServicio no modificado
	 * @param key
	 * @param value
	 */
	public void setMapaAsociosTipoServicioNoModificado(String key, Object value){
		mapaAsociosTipoServicioNoModificado.put(key, value);
	}
	/**
	 * Get del mapa de AsociosTipoServicio no modificado
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaAsociosTipoServicioNoModificado(String key){
		return mapaAsociosTipoServicioNoModificado.get(key);
	}    
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapa(){
        try
        {
	        if(this.mapaAsociosTipoServicio==null)
	            return 0;
	        if(this.mapaAsociosTipoServicio.isEmpty())
	            return 0;
        }
        catch(NullPointerException npe)
        {
            return 0;
        }
        return numeroRealFilasMapa=mapaAsociosTipoServicio.size()/tamanioNumeroColumnas;
    }
    /**
     * @return Returns the numeroRealFilasMapa.
     */
    public int getNumeroRealFilasMapaNoMod(){
        return mapaAsociosTipoServicioNoModificado.size()/tamanioNumeroColumnas;
    }
    /**
     * @return Returns the mapaAsociosTipoServicio.
     */
    public HashMap getMapaAsociosTipoServicio() {
        return mapaAsociosTipoServicio;
    }
    /**
     * @param mapaAsociosTipoServicio The mapaAsociosTipoServicio to set.
     */
    public void setMapaAsociosTipoServicio(HashMap mapaAsociosTipoServicio) {
        this.mapaAsociosTipoServicio = mapaAsociosTipoServicio;
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
     * @return Returns the mapaAsociosTipoServicioNoModificado.
     */
    public HashMap getMapaAsociosTipoServicioNoModificado() {
        return mapaAsociosTipoServicioNoModificado;
    }
    /**
     * @param mapaAsociosTipoServicioNoModificado The mapaAsociosTipoServicioNoModificado to set.
     */
    public void setMapaAsociosTipoServicioNoModificado(HashMap mapaAsociosTipoServicioNoModificado) {
        this.mapaAsociosTipoServicioNoModificado = mapaAsociosTipoServicioNoModificado;
    }
	
    /**
	 * Set del mapa de AsociosTipoServicio auxiliar
	 * @param key
	 * @param value
	 */
	public void setMapaAsociosTipoServicioAux(String key, Object value){
		mapaAsociosTipoServicioAux.put(key, value);
	}
	/**
	 * Get del mapa de AsociosTipoServicio auxiliar
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaAsociosTipoServicioAux(String key){
		return mapaAsociosTipoServicioAux.get(key);
	}
	/**
     * @return Returns the numeroRealFilasMapaAux.
     */
    public int getNumeroRealFilasMapaAux(){
        return mapaAsociosTipoServicioAux.size()/6;
    }
    /**
     * @return Returns the mapaGruposAux.
     */
    public HashMap getMapaAsociosTipoServicioAux() {
        return mapaAsociosTipoServicioAux;
    }
    /**
     * @param mapaAsociosTipoServicioAux The mapaAsociosTipoServicioAux to set.
     */
    public void setMapaAsociosTipoServicioAux(HashMap mapaAsociosTipoServicioAux) {
        this.mapaAsociosTipoServicioAux = mapaAsociosTipoServicioAux;
    }
	/**
     * @return Returns the codigo.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
