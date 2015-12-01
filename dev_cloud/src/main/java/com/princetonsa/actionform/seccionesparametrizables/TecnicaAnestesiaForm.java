package com.princetonsa.actionform.seccionesparametrizables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;

/**
 * 
 * @author wilson
 *
 */
public class TecnicaAnestesiaForm extends ValidatorForm 
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
	
	
	///CAMPOS TECNICA ANESTESIA
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaTagTecnicaAnestesia;
	
	/**
	 * 
	 */
	private int index;
	
	/**
	 * 
	 */
	private int indexEliminado;
	
	////FIN CAMPOS TECNICA DE ANESTESIA
	
	
	/**
	 * Colección para traer los tipos de técnicas de anestesia general que tienen opciones para la institución
	 */
	private Collection listadoTecAnestesiaOpcionesGral;
	
	/**
	 * Colección para traer los tipos de técnicas de anestesia general sin opciones para la institución
	 */
	private Collection listadoTecAnestesiaGral;
	
	/**
	 * Colección para traer los tipos de técnicas de anestesia regional para la institución
	 */
	private Collection listadoTecAnestesiaRegional;
	
	/**
	 * Mapa para almacenar la información de la sección Técnica de Anestesia
	 */
	private HashMap<Object, Object> mapaTecAnestesia = new HashMap<Object, Object>();
	
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
	 * Indicador de ocultar menu
	 * */
	private boolean ocultarMenu = false;
	                      
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		resetTecnicaAnestesia();
		resetTecnicaGeneralRegional();
		mostrarDatosInfo = new InfoDatosString("","","",false);
	}
	
	/**
     * resetea los atributos del form
     *
     */
    public void resetTecnicaAnestesia()
    {
    	this.mapaTagTecnicaAnestesia= new HashMap<Object, Object>();
    	this.mapaTagTecnicaAnestesia.put("numRegistros", "0");
    	this.mapaTecAnestesia= new HashMap<Object, Object>();
    	this.mapaTecAnestesia.put("numRegistros", 0);
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.indexEliminado=ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * 
     *
     */
    public void resetTecnicaGeneralRegional()
    {
    	this.listadoTecAnestesiaOpcionesGral= new ArrayList<Object>();
    	this.listadoTecAnestesiaGral= new ArrayList<Object>();
    	this.listadoTecAnestesiaRegional= new ArrayList<Object>();
    	this.mapaTecAnestesia= new HashMap<Object, Object>();
    	if(!this.getEstado().equals("resumenGeneralRegional"))
    	{	
    		this.esSinEncabezado = "";
    		this.ocultarMenu = false;
    	}	
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
        
        if(estado.equals("guardarTecnicaAnestesia"))
        {
        	if(Integer.parseInt(this.mapaTecAnestesia.get("numRegistros")+"")<=0)
        	{	
        		errores.add("errors.required", new ActionMessage("errors.required", "La tecnica de Anestesia"));
        	}	
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
	public boolean isEsConsulta() {
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
	 * @return the mapaTagTecnicaAnestesia
	 */
	public HashMap<Object, Object> getMapaTagTecnicaAnestesia() {
		return mapaTagTecnicaAnestesia;
	}

	/**
	 * @param mapaTagTecnicaAnestesia the mapaTagTecnicaAnestesia to set
	 */
	public void setMapaTagTecnicaAnestesia(
			HashMap<Object, Object> mapaTagTecnicaAnestesia) {
		this.mapaTagTecnicaAnestesia = mapaTagTecnicaAnestesia;
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
	 * @return the mapaTagTecnicaAnestesia
	 */
	public Object getMapaTagTecnicaAnestesia(Object key) {
		return mapaTagTecnicaAnestesia.get(key);
	}

	/**
	 * @param mapaTagTecnicaAnestesia the mapaTagTecnicaAnestesia to set
	 */
	public void setMapaTagTecnicaAnestesia(
			Object key, Object value) {
		this.mapaTagTecnicaAnestesia.put(key, value);
	}

	/**
	 * @return the listadoTecAnestesiaGral
	 */
	public Collection getListadoTecAnestesiaGral() {
		return listadoTecAnestesiaGral;
	}

	/**
	 * @param listadoTecAnestesiaGral the listadoTecAnestesiaGral to set
	 */
	public void setListadoTecAnestesiaGral(Collection listadoTecAnestesiaGral) {
		this.listadoTecAnestesiaGral = listadoTecAnestesiaGral;
	}

	/**
	 * @return the listadoTecAnestesiaOpcionesGral
	 */
	public Collection getListadoTecAnestesiaOpcionesGral() {
		return listadoTecAnestesiaOpcionesGral;
	}

	/**
	 * @param listadoTecAnestesiaOpcionesGral the listadoTecAnestesiaOpcionesGral to set
	 */
	public void setListadoTecAnestesiaOpcionesGral(
			Collection listadoTecAnestesiaOpcionesGral) {
		this.listadoTecAnestesiaOpcionesGral = listadoTecAnestesiaOpcionesGral;
	}

	/**
	 * @return the listadoTecAnestesiaRegional
	 */
	public Collection getListadoTecAnestesiaRegional() {
		return listadoTecAnestesiaRegional;
	}

	/**
	 * @param listadoTecAnestesiaRegional the listadoTecAnestesiaRegional to set
	 */
	public void setListadoTecAnestesiaRegional(
			Collection listadoTecAnestesiaRegional) {
		this.listadoTecAnestesiaRegional = listadoTecAnestesiaRegional;
	}

	/**
	 * @return the mapaTecAnestesia
	 */
	public HashMap<Object, Object> getMapaTecAnestesia() {
		return mapaTecAnestesia;
	}

	/**
	 * @param mapaTecAnestesia the mapaTecAnestesia to set
	 */
	public void setMapaTecAnestesia(HashMap<Object, Object> mapaTecAnestesia) {
		this.mapaTecAnestesia = mapaTecAnestesia;
	}

	/**
	 * @return the esSinEncabezado
	 */
	public String getEsSinEncabezado() {		
		return esSinEncabezado;
	}

	/**
	 * @return the mapaTecAnestesia
	 */
	public Object getMapaTecAnestesia(Object key) {
		return mapaTecAnestesia.get(key);
	}
	
	/**
	 * @param mapaTecAnestesia the mapaTecAnestesia to set
	 */
	public void setMapaTecAnestesia(Object key, Object value) {
		this.mapaTecAnestesia.put(key, value);
	}

	
	/**
	 * @param esSinEncabezado the esSinEncabezado to set
	 */
	public void setEsSinEncabezado(String esSinEncabezado) {
		this.esSinEncabezado = esSinEncabezado;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
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