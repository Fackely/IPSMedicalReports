package com.princetonsa.actionform.agendaProcedimiento;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import org.apache.struts.validator.ValidatorForm;
import org.apache.log4j.Logger;
import util.ConstantesBD;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class ExamenCondiTomaForm extends ValidatorForm 
{

	//--- Atributos
	
	/**
	 * Objeto para manejar el log de la clase 
	 * */
	private Logger logger = Logger.getLogger(ExamenCondiTomaForm.class);
	
	
	/**
	 * estado en el que se encuentra el proceso 
	 * */
	private String estado;
	
	/**
	 * Mapa de examenCt 
	 * */
	private HashMap examenCtMap;
	
	/**
	 * Mapa de examenCt eliminados 
	 * */
	private HashMap examenCtEliminadosMap;
	
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
    
    //--- Fin Atributos
    
    //-- Metodos
    
    /**
     * Resetea los atributos de la clase
     * */
    public void reset()
    {
    	this.estado="";
    	this.examenCtMap = new HashMap();
    	this.examenCtEliminadosMap = new HashMap();
    	this.patronOrdenar = "";
    	this.ultimoPatron = "";
    	this.linkSiguiente = "";
    	this.indexEliminado = ConstantesBD.codigoNuncaValido;    	
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
    	if(estado.equals("guardar"))
    	{
    		int numReg = Integer.parseInt(this.examenCtMap.get("numRegistros").toString());
    		for(int i=0; i<numReg; i++)
    		{			
    			if(this.examenCtMap.get("descripcion_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Descripcion del registro "+(i+1)));
				}
    		}
    		
    	}   	
    	
    	return errores;
	}

    
//  -- Fin validate
    
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
	 * @return the examenCtEliminadosMap
	 */
	public HashMap getExamenCtEliminadosMap() {
		return examenCtEliminadosMap;
	}

	/**
	 * @param examenCtEliminadosMap the examenCtEliminadosMap to set
	 */
	public void setExamenCtEliminadosMap(HashMap examenCtEliminadosMap) {
		this.examenCtEliminadosMap = examenCtEliminadosMap;		
	}
	
	/**
	 * 
	 * */
	public Object getExamenCtEliminadosMap(String key)
	{
		return examenCtEliminadosMap.get(key);
	}
	
	/**
	 * 
	 * */
	public void setExamenCtEliminadosMap(String key, Object value)
	{
		this.examenCtEliminadosMap.put(key, value);
	}	

	/**
	 * @return the examenCtMap
	 */
	public HashMap getExamenCtMap() {
		return examenCtMap;
	}

	/**
	 * @param examenCtMap the examenCtMap to set
	 */
	public void setExamenCtMap(HashMap examenCtMap) {
		this.examenCtMap = examenCtMap;
	}
	
	
	/**
	 * 
	 * */
	public Object getExamenCtMap(String key)
	{
		return examenCtMap.get(key);
	}
	
	/**
	 * 
	 * */
	public void setExamenCtMap(String key, Object value)
	{
		this.examenCtMap.put(key, value);
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
	
	//--- Fin Metodos
}