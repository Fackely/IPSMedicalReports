package com.princetonsa.actionform.parametrizacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


import util.ConstantesBD;


public class ServiciosAutomaticoForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar el log de la clase
	 * */	
	private Logger logger = Logger.getLogger(ServiciosAutomaticoForm.class);
	
	//-------------------Atributos
	
	/**
	 * Estado en el que se encuentra el proceso 
	 * */		
	private String estado;
	
	/**
	 * Mapa de coberura
	 * */
	private HashMap ServiciosAutomaticosMap;
	
	
	/**
	 * clon de consultoriosmap al momento de cargarlos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	private HashMap serviciosEliminadosMap;
	
	/**
	 * 
	 * */
	private String patronOrdenar;
	
	/**
	 * 
	 * */
	private String ultimoPatron;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
    
    /**
     * indice del mapa que se desea eliminar
     */
    private int indexEliminado;	
    
    
    /**
	 * Consulta
	 */
	private HashMap consulta;
    
//  atributos usados para la paginacion 
	
	private int offset;
	
	private int indice;
	
//	atributos usados para la ordenacion
	private String columna;
	private String ultimaColumna;
	
	//------------------Metodos  
	
	/**
	 * Resetea todos los atributos de la clase 
	 * */
	public void reset()
	{		
		this.indice=-1;
		this.ServiciosAutomaticosMap = new HashMap();			
		this.serviciosEliminadosMap = new HashMap();
		this.consulta = new HashMap();
		this.indexEliminado = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.linkSiguiente = "";
		this.ultimoPatron = "";
		this.offset = 0;
		this.columna = "";
    	this.ultimaColumna = "";
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
		ActionErrors errores = new ActionErrors();
		errores = super.validate(mapping, request);	
		return errores;		
	}
	
	//---------------Fin Validate 

	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setServiciosAutomaticosMap(String key, Object value) {
		this.ServiciosAutomaticosMap.put(key, value);
	}
	
	public void setserviciosEliminadosMap(String key, Object value) {
		this.serviciosEliminadosMap.put(key, value);
	}
		
	
	public HashMap getServiciosAutomaticosMap(){
		return ServiciosAutomaticosMap;
	}
	
	public void setServiciosAutomaticosMap(HashMap value){
		this.ServiciosAutomaticosMap = value;
	}
	
	public Object getServiciosAutomaticosMap(String key) {
		return ServiciosAutomaticosMap.get(key);
	}
	
	public HashMap getserviciosEliminadosMap(){
		return serviciosEliminadosMap;
	}
	
	public Object getserviciosEliminadosMap(String key) {
		return serviciosEliminadosMap.get(key);
	}


	public String getPatronOrdenar(){
		return patronOrdenar;
	}
	
	public void setPatronOrdenar(String patronOrdenar){
		this.patronOrdenar=patronOrdenar;
	}
	
	public String getUltimoPatron(){
		return ultimoPatron;
	}
	
	public void setUltimoPatron(String ultimoPatron){
		this.ultimoPatron = ultimoPatron;
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
	 * @return the indexEliminado
	 */
	public int getIndexEliminado() {
		return indexEliminado;
	}

	/**
	 * @param indexEliminado the indexEliminado to set
	 */
	public void setIndexEliminado(int indexEliminado) {
		this.indexEliminado = indexEliminado;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public HashMap getServiciosEliminadosMap() {
		return serviciosEliminadosMap;
	}

	public void setServiciosEliminadosMap(HashMap serviciosEliminadosMap) {
		this.serviciosEliminadosMap = serviciosEliminadosMap;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public String getColumna() {
		return columna;
	}

	public void setColumna(String columna) {
		this.columna = columna;
	}

	public String getUltimaColumna() {
		return ultimaColumna;
	}

	public void setUltimaColumna(String ultimaColumna) {
		this.ultimaColumna = ultimaColumna;
	}

	public HashMap getConsulta() {
		return consulta;
	}

	public void setConsulta(HashMap consulta) {
		this.consulta = consulta;
	}
	
	public Object getConsulta(String key) {
		return consulta.get(key);
	}


	public void setConsulta(String key,Object obj) {
		this.consulta.put(key, obj);
	}
}