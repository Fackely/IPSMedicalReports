package com.princetonsa.actionform.seccionesparametrizables;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.InfoDatosString;
import util.UtilidadTexto;

/**
 * 
 * @author wilson
 *
 */
public class InfoGeneralHAForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 * */
	private String codigoPeticion;
	
	/**
	 * 
	 */
	private int centroCosto;
	
	/**
	 * 
	 */
	private boolean esConsulta;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapa;
	
	
	//Campos Funcionalidad Dummy
	
	/**
	 * String esSinEncabezado
	 * */
	private String esSinEncabezado;
	
	/**
	 * Indicador para mostrar informacion solo cuando posea datos
	 * */
	private InfoDatosString mostrarDatosInfo = new InfoDatosString("","","",false);
	
	/**
	 * Indicador de Mostrar menu
	 * */
	
	private boolean ocultarMenu = false; 
	
	
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {    	
    	mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros", "0");
    	if(!this.getEstado().contains("resumen"))
    	{	
	    	this.esSinEncabezado = "";
	    	this.ocultarMenu = false;
    	}	
    	mostrarDatosInfo = new InfoDatosString("","","",false);
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
        
        if(this.estado.equals("guardarPesoTalla"))
        {
        	if(!UtilidadTexto.isNumber(this.mapa.get("peso")+""))
        	{
        		errores.add("", new ActionMessage("errors.float", "El peso"));
        	}
        	if(!UtilidadTexto.isNumber(this.mapa.get("talla")+""))
        	{
        		errores.add("", new ActionMessage("errors.float", "La Talla"));
        	}
        	
        	if(!errores.isEmpty())
        		this.estado="continuarTallaPeso";
        }
        
        return errores;
    }

	/**
	 * @return the centroCosto
	 */
	public int getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the esConsulta
	 */
	public boolean getEsConsulta() {
		return esConsulta;
	}

	/**
	 * @param esConsulta the esConsulta to set
	 */
	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

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
	 * @return the mapa
	 */
	public HashMap<Object, Object> getMapa() {
		return mapa;
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(HashMap<Object, Object> mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return the mapa
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(Object key, Object value) {
		this.mapa.put(key, value);
	}

	
	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the esSinEncabezado
	 */
	public String getEsSinEncabezado() {
		return esSinEncabezado;
	}

	/**
	 * @param esSinEncabezado the esSinEncabezado to set
	 */
	public void setEsSinEncabezado(String esSinEncabezado) {
		this.esSinEncabezado = esSinEncabezado;
	}

	/**
	 * @return the codigoPeticion
	 */
	public String getCodigoPeticion() {
		return codigoPeticion;
	}

	/**
	 * @param codigoPeticion the codigoPeticion to set
	 */
	public void setCodigoPeticion(String codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}
	
	public InfoDatosString getMostrarDatosInfo(){
		return mostrarDatosInfo;
	}	
	public void setMostrarDatosInfo(InfoDatosString mostrarDatosInfo){
		this.mostrarDatosInfo = mostrarDatosInfo;
	}	
	public void setMostrarDatosInfoActivo(boolean valor){
		this.mostrarDatosInfo.setActivo(valor);
	}
	public boolean getMostrarDatosInfoActivo(){
		return this.mostrarDatosInfo.getActivo();
	}

	/**
	 * @return the ocultarMenu
	 */
	public boolean isOcultarMenu() {
		return ocultarMenu;
	}

	/**
	 * @param ocultarMenu the ocultarMenu to set
	 */
	public void setOcultarMenu(boolean ocultarMenu) {
		this.ocultarMenu = ocultarMenu;
	}
    
    
}
