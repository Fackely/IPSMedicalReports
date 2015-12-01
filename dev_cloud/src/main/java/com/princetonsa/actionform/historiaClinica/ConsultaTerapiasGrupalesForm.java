package com.princetonsa.actionform.historiaClinica;


import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import java.util.HashMap;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;


public class ConsultaTerapiasGrupalesForm extends ValidatorForm
{
	
	//---------------Atributos
	
	/**
	 * String Estado
	 * */
	private String estado;
	
	/**
	 * String opcion de la consulta Por Paciente (paci) -  Por Periodo (peri)
	 * */
	private String opcionConsulta;
	
	
	/**
	 * HashMap TerapiasGrupales
	 * */
	private HashMap terapiasGrupalesMap;
	
	
	/**
	 * HashMap detalleTerapiasGrupales
	 * */
	private HashMap detalleMap;
	
	
	/**
	 * ArrayList ResponsableRegsitra;
	 * */
	private ArrayList<HashMap<String,Object>> responsableRegistra;
	
	
	/**
	 * HashMap Busqueda
	 * */
	private HashMap busquedaMap;
	
	
	/**
	 * String indexTerapiasGrupalesMap
	 * */
	private String indexTerapiasGrupalesMap;
	
	//---------------Metodos	
	
	
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
	
	public void reset()
	{
		this.opcionConsulta = "";  
		this.terapiasGrupalesMap = new HashMap();
		this.responsableRegistra = new ArrayList<HashMap<String,Object>>();
		this.detalleMap = new HashMap();
		this.busquedaMap = new HashMap();
		this.indexTerapiasGrupalesMap = "";
	}
	

	/**
	 * @return the opcionConsulta
	 */
	public String getOpcionConsulta() {
		return opcionConsulta;
	}

	/**
	 * @param opcionConsulta the opcionConsulta to set
	 */
	public void setOpcionConsulta(String opcionConsulta) {
		this.opcionConsulta = opcionConsulta;
	}

	/**
	 * @return the terapiasGrupalesMap
	 */
	public HashMap getTerapiasGrupalesMap() {
		return terapiasGrupalesMap;
	}

	/**
	 * @param terapiasGrupalesMap the terapiasGrupalesMap to set
	 */
	public void setTerapiasGrupalesMap(HashMap terapiasGrupalesMap) {
		this.terapiasGrupalesMap = terapiasGrupalesMap;
	}
	
	/**
	 * @return the terapiasGrupalesMap
	 */
	public Object getTerapiasGrupalesMap(String key) {
		return terapiasGrupalesMap.get(key);
	}

	/**
	 * @param terapiasGrupalesMap the terapiasGrupalesMap to set
	 */
	public void setTerapiasGrupalesMap(String key, Object value) {
		this.terapiasGrupalesMap.put(key, value);
	}

	/**
	 * @return the detalle
	 */
	public HashMap getDetalleMap() {
		return detalleMap;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalleMap(HashMap detalleMap) {
		this.detalleMap = detalleMap;
	}

	/**
	 * @return the detalle
	 */
	public Object getDetalleMap(String key) {
		return detalleMap.get(key);
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalleMap(String key, Object value) {
		this.detalleMap.put(key, value);
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
	 * @return the responsableRegistra
	 */
	public ArrayList<HashMap<String, Object>> getResponsableRegistra() {
		return responsableRegistra;
	}

	/**
	 * @param responsableRegistra the responsableRegistra to set
	 */
	public void setResponsableRegistra(
			ArrayList<HashMap<String, Object>> responsableRegistra) {
		this.responsableRegistra = responsableRegistra;
	}


	/**
	 * @return the busquedaMap
	 */
	public HashMap getBusquedaMap() {
		return busquedaMap;
	}


	/**
	 * @param busquedaMap the busquedaMap to set
	 */
	public void setBusquedaMap(HashMap busquedaMap) {
		this.busquedaMap = busquedaMap;
	}

	
	/**
	 * @return the busquedaMap
	 */
	public Object getBusquedaMap(String key) {
		return busquedaMap.get(key);
	}


	/**
	 * @param busquedaMap the busquedaMap to set
	 */
	public void setBusquedaMap(String key, Object value) {
		this.busquedaMap.put(key, value);
	}


	/**
	 * @return the indexTerapiasGrupalesMap
	 */
	public String getIndexTerapiasGrupalesMap() {
		return indexTerapiasGrupalesMap;
	}


	/**
	 * @param indexTerapiasGrupalesMap the indexTerapiasGrupalesMap to set
	 */
	public void setIndexTerapiasGrupalesMap(String indexTerapiasGrupalesMap) {
		this.indexTerapiasGrupalesMap = indexTerapiasGrupalesMap;
	}
}