/*
 * @(#)ParametrizacionCurvaAlertaForm
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Form que contiene todos los datos específicos para generar 
 * la parametrizacion curva de aterta 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @author wrios 
 */
public class ParametrizacionCurvaAlertaForm extends ValidatorForm 
{
	/**
     * codigo
     */
    private int codigo;

   /**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Mapa 
	 */
	private HashMap mapa= new HashMap();
	
	/**
	 * Cód del index del Mapa
	 */
	private int indexMapa;
	
	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";

	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Offset del HashMap
	 */
	private int offsetHash;
	
	/**
	 * muestra el mensaje de grabacion existosa
	 */
	private boolean mostrarMensaje;
	
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
	 * Número de Items mostrados por página, 
	 * Se definió en este punto porque al ingresar un nuevo elemento
	 * al pager, se debe hacer un cálculo de l última ésta página y en este punto
	 * se debe conocer el número de items. 
	 */
	private int maxPageItems;
	
	
	/** SIMULACION TAGS **/
	private HashMap posicionesTagMap= new HashMap();
	private HashMap paridadesTagMap= new HashMap();
	private HashMap membranasTagMap= new HashMap();
	
	
	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset(int codigoInstitucion)
	{
	    this.codigo=ConstantesBD.codigoNuncaValido;
	    this.mapa= new HashMap();
	    this.mapa.put("numRegistros", "0");
	    
	    this.mapaNoModificado= new HashMap();
	    this.mapaNoModificado.put("numRegistros", "0");
	    
	    this.mapaAux= new HashMap();
	    this.mapaAux.put("numRegistros", "0");
	    
	    this.indexMapa=0;
	    this.linkSiguiente="";
	    this.patronOrdenar="";
	    this.ultimoPatron="";
	    this.offsetHash=0;
	    this.mostrarMensaje=false;
	    
	    try
		{
			this.maxPageItems= Integer.parseInt(ValoresPorDefecto.getMaxPageItems(codigoInstitucion));
		}
		catch(Exception e)
		{
			maxPageItems=10;
		}
	    
	    //tags
	    this.posicionesTagMap=new HashMap();
	    this.paridadesTagMap= new HashMap();
	    this.membranasTagMap= new HashMap();
	    
	}
	
	/**
	 * 
	 * 
	 */
	public void inicializarTags()
	{
		//inicializamos posiciones
		this.setPosicionesTagMap(Utilidades.obtenerCodigoNombreTablaMap("posiciones"));
		this.setParidadesTagMap(Utilidades.obtenerCodigoNombreTablaMap("paridades"));
		this.setMembranasTagMap(Utilidades.obtenerCodigoNombreTablaMap("membranas"));
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
		    for(int i=0; i<Integer.parseInt(this.getMapa("numRegistros").toString()); i++)
			{
		        if((this.getMapa("eseliminada_"+i)+"").equals("false"))
		        {
		            if(    (this.getMapa("codigoposicion_"+i)+"").equals("")  ||
		                   (this.getMapa("codigoposicion_"+i)+"").equals("null")   )
		            {
		                errores.add("requerido", new ActionMessage("errors.required","El campo Posición Nro ["+(i+1)+"]"));
		            }
		            if( (this.getMapa("codigoparidad_"+i)+"").equals("")  ||
			            (this.getMapa("codigoparidad_"+i)+"").equals("null")   )
			        {
			            errores.add("requerido", new ActionMessage("errors.required","El campo Paridad Nro ["+(i+1)+"]"));
			        }
		            if( (this.getMapa("codigomembrana_"+i)+"").equals("")  ||
				            (this.getMapa("codigomembrana_"+i)+"").equals("null")   )
		            {
				        errores.add("requerido", new ActionMessage("errors.required","El campo Membrana Nro ["+(i+1)+"]"));
				    }
		            
		            boolean existeErrorRangos=false;
		            
		            //validaciones de los rangos
		            if( (this.getMapa("rangoinicial_"+i)+"").equals("")  ||
				            (this.getMapa("rangoinicial_"+i)+"").equals("null")   )
		            {
				        errores.add("requerido", new ActionMessage("errors.required","El campo Rango Inicial Nro ["+(i+1)+"]"));
				        existeErrorRangos=true;
				    }
		            if( (this.getMapa("rangofinal_"+i)+"").equals("")  ||
				            (this.getMapa("rangofinal_"+i)+"").equals("null")   )
		            {
				        errores.add("requerido", new ActionMessage("errors.required","El campo Rango Final Nro ["+(i+1)+"]"));
				        existeErrorRangos=true;
				    }
		            
		            if(!existeErrorRangos)
		            {
		            	float rangoInicial=0;
		            	float rangoFinal=0;
		            	boolean existeErrorRangoInicial=false;
		            	boolean existeErrorRangoFinal=false;
		            	
		            	try
		            	{
		            		rangoInicial=Float.parseFloat(this.getMapa("rangoinicial_"+i)+"");
		            		
		            		if(rangoInicial>99.99 || rangoInicial<0)
		            		{
		            			errores.add("", new ActionMessage("errors.range","El campo Rango Inicial Nro ["+(i+1)+"]","0","99.99"));
		            			existeErrorRangoInicial=true;
		            			existeErrorRangos=true;
		            		}
		            	}
		            	catch (Exception e) 
		            	{
		            		errores.add("", new ActionMessage("errors.range","El campo Rango Inicial Nro ["+(i+1)+"]","0","99.99"));
					        existeErrorRangoInicial=true;
					        existeErrorRangos=true;
		            	}
		            	
		            	try
		            	{
		            		rangoFinal=Float.parseFloat(this.getMapa("rangofinal_"+i)+"");
		            		
		            		if(rangoFinal>99.99 || rangoFinal<0)
		            		{
		            			errores.add("", new ActionMessage("errors.range","El campo Rango Final Nro ["+(i+1)+"]","0","99.99"));
		            			existeErrorRangoFinal=true;
		            			existeErrorRangos=true;
		            		}
		            	}
		            	catch (Exception e) 
		            	{
		            		errores.add("", new ActionMessage("errors.range","El campo Rango Final Nro ["+(i+1)+"]","0","99.99"));
					        existeErrorRangoFinal=true;
					        existeErrorRangos=true;
						}
		            	
		            	//se evalua que no tenga mas de dos decimales
		            	if(!existeErrorRangoInicial)
		            	{
		            		if(UtilidadValidacion.esMayorNdecimales(this.getMapa("rangoinicial_"+i)+"", 2))
		            		{
		            			errores.add("", new ActionMessage("errors.numDecimales", "El campo Rango Inicial Nro ["+(i+1)+"] = "+rangoInicial, "2"));
		            			existeErrorRangoInicial=true;
						        existeErrorRangos=true;
		            		}
		            	}
		            	
		            	//se evalua que no tenga mas de dos decimales
		            	if(!existeErrorRangoFinal)
		            	{
		            		if(UtilidadValidacion.esMayorNdecimales(this.getMapa("rangofinal_"+i)+"", 2))
		            		{
		            			errores.add("", new ActionMessage("errors.numDecimales", "El campo Rango Final Nro ["+(i+1)+"] = "+rangoFinal, "2"));
		            			existeErrorRangoFinal=true;
						        existeErrorRangos=true;
		            		}
		            	}
		            	
		            	//si no existen errores entonces se evalua que el rango inciial sea menor al final
		            	if(!existeErrorRangos)
		            	{
		            		if(rangoInicial>=rangoFinal)
		            		{
		            			errores.add("", new ActionMessage("errors.MayorQue", "El campo Rango Final Nro ["+(i+1)+"] ="+rangoFinal, " el campo Rango Inicial = "+rangoInicial));
		            			existeErrorRangos=true;
		            		}
		            	}
		            }
		            
		            if(!existeErrorRangos)
		            {
		            	if(!esUnique(i))
		            	{
		            		errores.add("", new ActionMessage("error.historiaClinica.parametrosCurva.yaExisteUnique", "Nro ["+(i+1)+"]"));
		            	}
		            }
		            
		            //validaciones del valor en hora
		            try
	            	{
	            		if(!UtilidadFecha.validacionHora(this.getMapa("valor_"+i)+"").puedoSeguir)
	            		{	
	            			errores.add("", new ActionMessage("errors.formatoHoraInvalido", this.getMapa("valor_"+i)+" Nro ["+(i+1)+"]"));
	            		}
	            	}
	            	catch (Exception e) 
	            	{
	            		errores.add("", new ActionMessage("errors.formatoHoraInvalido", this.getMapa("valor_"+i)+" Nro ["+(i+1)+"]"));
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
	 * Método que compara los dos Mapas Original - Modificado, y los compara hasta el
	 * size del original y devuelve un nuevo HashMap con los campos que han sido modificados.
	 * @return
	 */
	public boolean comparar2HashMap()
	{
	    this.mapaAux= new HashMap();
	    this.mapaAux.put("numRegistros", "0");
	    boolean fueInsertadoEnMapa=false;
	    
	    for(int k=0; k<Integer.parseInt(this.mapa.get("numRegistros").toString()); k++)
	    {
	        if((this.getMapa("estabd_"+k)+"").equals("true"))
	        {
	           fueInsertadoEnMapa=this.modificarMapaAux(k, "codigoposicion_");
	           { 
	        	   if(!fueInsertadoEnMapa)
                   {
	        		   fueInsertadoEnMapa=this.modificarMapaAux(k, "codigoparidad_");
	        		   if(!fueInsertadoEnMapa)
	                   {
	        			   fueInsertadoEnMapa=this.modificarMapaAux(k, "codigomembrana_");
		        		   if(!fueInsertadoEnMapa)
		                   {
		        			   fueInsertadoEnMapa=this.modificarMapaAux(k, "rangoinicial_");
			        		   if(!fueInsertadoEnMapa)
			                   {
			        			   fueInsertadoEnMapa=this.modificarMapaAux(k, "rangofinal_");
				        		   if(!fueInsertadoEnMapa)
				                   {
				        			   fueInsertadoEnMapa=this.modificarMapaAux(k, "valor_");
					        		   if(!fueInsertadoEnMapa)
					                   {
					        			   fueInsertadoEnMapa=this.modificarMapaAux(k, "activo_");
						               }
				                   }
			                   }
		                   }
	                   }
                   }   
	           }    
	        }    
	    }
	    return fueInsertadoEnMapa;
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
	            mapaAux.put("codigo_"+ k, this.getMapa("codigo_"+k));
	            mapaAux.put("codigoposicion_"+ k, this.getMapa("codigoposicion_"+k));
	            mapaAux.put("nombreposicion_"+ k, this.getMapa("nombreposicion_"+k));
	            mapaAux.put("codigoparidad_"+ k, this.getMapa("codigoparidad_"+k));
	            mapaAux.put("nombreparidad_"+ k, this.getMapa("nombreparidad_"+k));
	            mapaAux.put("codigomembrana_"+ k, this.getMapa("codigomembrana_"+k));
	            mapaAux.put("nombremembrana_"+ k, this.getMapa("nombremembrana_"+k));
	            mapaAux.put("rangoinicial_"+ k, this.getMapa("rangoinicial_"+k));
	            mapaAux.put("rangofinal_"+ k, this.getMapa("rangofinal_"+k));
	            mapaAux.put("valor_"+ k, this.getMapa("valor_"+k));
	            mapaAux.put("activo_"+ k, this.getMapa("activo_"+k));
	            
	            mapaAux.put("numRegistros", (Integer.parseInt(mapaAux.get("numRegistros").toString())+1)+"");
	            fueInsertadoEnMapa=true;
            }    
        }    
	    return fueInsertadoEnMapa;
	}
	
	
	/**
	 * metodo que evalua si existe o no un registro dado
	 * @param indiceMapa
	 * @return
	 */
	private boolean esUnique(int indiceMapa)
	{
		float rangoInicialindiceMapa=Float.parseFloat(this.getMapa("rangoinicial_"+indiceMapa).toString());
		float rangoFinalindiceMapa=Float.parseFloat(this.getMapa("rangofinal_"+indiceMapa).toString());
		boolean esUnique=true;
		
		for(int i=0; i<indiceMapa; i++)
		{
	        if((this.getMapa("eseliminada_"+i)+"").equals("false") && (this.getMapa("activo_"+i)+"").equals("true") && (this.getMapa("activo_"+indiceMapa)+"").equals("true"))
	        {
	        	if(	this.getMapa("codigoposicion_"+i).toString().equals(this.getMapa("codigoposicion_"+indiceMapa).toString())
	        		&& 	this.getMapa("codigoparidad_"+i).toString().equals(this.getMapa("codigoparidad_"+indiceMapa).toString())
	        		&& 	this.getMapa("codigomembrana_"+i).toString().equals(this.getMapa("codigomembrana_"+indiceMapa).toString())
	        	   )
	        	{
	        		//solo falta hacer la validacion de los rangos
	        		float rangoIniciali=Float.parseFloat(this.getMapa("rangoinicial_"+i).toString());
	        		float rangoFinali=Float.parseFloat(this.getMapa("rangofinal_"+i).toString());
	        		
	        		/*se evalua si el rango inicial esta dentro del rango*/
	        		if(rangoInicialindiceMapa>=rangoIniciali && rangoInicialindiceMapa<=rangoFinali)
	        		{	
	        			esUnique=false;
	        		}	
	        		if(rangoFinalindiceMapa>=rangoIniciali && rangoFinalindiceMapa<=rangoFinali)
	        		{	
	        			esUnique=false;
	        		}	
	        	}
	        }
		}    
		return esUnique;
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
	 * Set del mapa 
	 * @param key
	 * @param value
	 */
	public void setMapaNoModificado(String key, Object value){
		mapaNoModificado.put(key, value);
	}
	/**
	 * Get del mapa 
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaNoModificado(String key){
		return mapaNoModificado.get(key);
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
	 * @return Returns the membranasTagMap.
	 */
	public HashMap getMembranasTagMap() {
		return membranasTagMap;
	}

	/**
	 * @param membranasTagMap The membranasTagMap to set.
	 */
	public void setMembranasTagMap(HashMap membranasTagMap) {
		this.membranasTagMap = membranasTagMap;
	}

	/**
	 * @return Returns the paridadesTagMap.
	 */
	public HashMap getParidadesTagMap() {
		return paridadesTagMap;
	}

	/**
	 * @param paridadesTagMap The paridadesTagMap to set.
	 */
	public void setParidadesTagMap(HashMap paridadesTagMap) {
		this.paridadesTagMap = paridadesTagMap;
	}

	/**
	 * @return Returns the posicionesTagMap.
	 */
	public HashMap getPosicionesTagMap() {
		return posicionesTagMap;
	}

	/**
	 * @param posicionesTagMap The posicionesTagMap to set.
	 */
	public void setPosicionesTagMap(HashMap posicionesTagMap) {
		this.posicionesTagMap = posicionesTagMap;
	}
	
	/**
	 * @return Returns the membranasTagMap.
	 */
	public Object getMembranasTagMap(Object key) {
		return membranasTagMap.get(key);
	}

	/**
	 * @param membranasTagMap The membranasTagMap to set.
	 */
	public void setMembranasTagMap(Object key, Object value) {
		this.membranasTagMap.put(key, value);
	}

	/**
	 * @return Returns the paridadesTagMap.
	 */
	public Object getParidadesTagMap(Object key) {
		return paridadesTagMap.get(key);
	}

	/**
	 * @param paridadesTagMap The paridadesTagMap to set.
	 */
	public void setParidadesTagMap(Object key, Object value) {
		this.paridadesTagMap.put(key, value);
	}

	/**
	 * @return Returns the posicionesTagMap.
	 */
	public Object getPosicionesTagMap(Object key) {
		return posicionesTagMap.get(key);
	}

	/**
	 * @param posicionesTagMap The posicionesTagMap to set.
	 */
	public void setPosicionesTagMap(Object key, Object value) {
		this.posicionesTagMap.put(key, value);
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
}
