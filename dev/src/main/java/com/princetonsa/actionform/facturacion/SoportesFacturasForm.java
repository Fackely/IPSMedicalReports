package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;
import util.Utilidades;


/**
 * Clase para el manejo de la parametrizacion 
 * de los soportes de facturas
 * Date: 2009-01-26
 * @author jfhernandez@princetonsa.com
 */
public class SoportesFacturasForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Convenios Activos del Sistema
	 */
	private ArrayList<HashMap> convenios;
	
	/**
	 * Convenios Activos del Sistema
	 */
	private ArrayList<HashMap> viasIngreso;
	
	/**
	 * Soportes de Facturas
	 */
	private HashMap<String, Object> soportesFacturasMap;
	
	/**
	 * Tipos Soportes
	 */
	private HashMap<String, Object> tiposSoportes;
	
	/**
	 * Mensaje
	 */
	private String mensaje;
	
	/**
	 * Nos indica desde que funcionalidad se esta generando el flujo
	 */
	private String funcionalidad;
	
	/**
	 * Mapa de respaldo para log
	 */
	private HashMap<String, Object> soportesFacturasMapAnterior;

	
	/**
	 * 
	 */
	private HashMap<String, Object> tiposSoportesMap;
	

	/**
	 *HashMap para guardar el respaldo de la información actual en soportes facturas para usar en la generación de log archivo
	 */
	private HashMap respaldoSoportes;
	
	
	public void reset()
	{
		this.convenios= new ArrayList<HashMap>();
		this.viasIngreso= new ArrayList<HashMap>();
		this.soportesFacturasMap = new HashMap<String, Object>();
		this.tiposSoportes=new HashMap<String, Object>();
		this.tiposSoportes.put("numRegistros", 0);
		this.mensaje="";
		this.soportesFacturasMapAnterior= new HashMap();
		this.tiposSoportes.put("", 0);
		funcionalidad="";
		
		
		
		this.tiposSoportesMap =new HashMap<String, Object>();
		this.tiposSoportesMap.put("", 0);
		this.respaldoSoportes=new HashMap();
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
        
        // Validar Campos Requeridos
        if(this.estado.equals("guardar")){
        	
        	Utilidades.imprimirMapa(this.tiposSoportesMap);
        	
        	this.mensaje="";
        	
        	// Validar Via de ingreso
        	if(this.soportesFacturasMap.get("viaIngreso").toString().equals(""))
        		errores.add("Validación Via de Ingreso", new ActionMessage("errors.required","Via de Ingreso"));
        	
        	
        	// Validar por lo menos un tipo de soporte seleccionado
        	boolean hayTipoSoporte = false; 
        	for(int i=0; i<Utilidades.convertirAEntero(this.tiposSoportesMap.get("numRegistros")+""); i++){
        		
        		// Se validad el tipo de soporte activo que no sea hijo
        		if(UtilidadTexto.getBoolean(this.tiposSoportesMap.get("activo_"+i).toString()) 
        				&& UtilidadTexto.isEmpty(this.tiposSoportesMap.get("codpadre_"+i).toString()))
        			hayTipoSoporte=true;
    			
        		// Se validad el tipo de soporte activo hijo
        		if(!UtilidadTexto.isEmpty(this.tiposSoportesMap.get("codpadre_"+i).toString())
        					&& UtilidadTexto.getBoolean(this.tiposSoportesMap.get("mostraropcioneshijas_"+this.tiposSoportesMap.get("codpadre_"+i))+""))
        			hayTipoSoporte=true;
        		
        	}
        	
        	if(!hayTipoSoporte)
        		errores.add("Validación Via de Ingreso", new ActionMessage("errors.required","Tipo de Soporte"));
        	
        }
        
        return errores;        
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
	 * @return the convenios
	 */
	public ArrayList<HashMap> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the soportesFacturasMap
	 */
	public HashMap<String, Object> getSoportesFacturasMap() {
		return soportesFacturasMap;
	}

	/**
	 * @param soportesFacturasMap the soportesFacturasMap to set
	 */
	public void setSoportesFacturasMap(HashMap<String, Object> soportesFacturasMap) {
		this.soportesFacturasMap = soportesFacturasMap;
	}

	/**
	 * @return the soportesFacturasMap
	 */
	public Object getSoportesFacturasMap(String llave) {
		return soportesFacturasMap.get(llave);
	}

	/**
	 * @param soportesFacturasMap the soportesFacturasMap to set
	 */
	public void setSoportesFacturasMap(String llave, Object obj) {
		this.soportesFacturasMap.put(llave, obj);
	}

	/**
	 * @return the viasIngreso
	 */
	public ArrayList<HashMap> getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(ArrayList<HashMap> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	/**
	 * @return the tiposSoportes
	 */
	public HashMap<String, Object> getTiposSoportes() {
		return tiposSoportes;
	}

	/**
	 * @param tiposSoportes the tiposSoportes to set
	 */
	public void setTiposSoportes(HashMap<String, Object> tiposSoportes) {
		this.tiposSoportes = tiposSoportes;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the funcionalidad
	 */
	public String getFuncionalidad() {
		return funcionalidad;
	}

	/**
	 * @param funcionalidad the funcionalidad to set
	 */
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}

	/**
	 * @return the soportesFacturasMapAnterior
	 */
	public HashMap getSoportesFacturasMapAnterior() {
		return soportesFacturasMapAnterior;
	}
	
	/**
	 * @param soportesFacturasMapAnterior the soportesFacturasMapAnterior to set
	 */
	public void setSoportesFacturasMapAnterior(
			HashMap<String, Object> soportesFacturasMapAnterior) {
		this.soportesFacturasMapAnterior = soportesFacturasMapAnterior;
	}

	/**
	 * @return the tiposSoportesMap
	 */
	public HashMap<String, Object> getTiposSoportesMap() {
		return tiposSoportesMap;
	}

	/**
	 * @param tiposSoportesMap the tiposSoportesMap to set
	 */
	public void setTiposSoportesMap(HashMap<String, Object> tiposSoportesMap) {
		this.tiposSoportesMap = tiposSoportesMap;
	}
	
	/**
	 * @return the tiposSoportesMap
	 */
	public Object getTiposSoportesMap(String llave) {
		return tiposSoportesMap.get(llave);
	}

	/**
	 * @param tiposSoportesMap the tiposSoportesMap to set
	 */
	public void setTiposSoportesMap(String llave, Object obj) {
		this.tiposSoportesMap.put(llave, obj);
	}

	public HashMap getRespaldoSoportes() {
		return respaldoSoportes;
	}

	public void setRespaldoSoportes(HashMap respaldoSoportes) {
		this.respaldoSoportes = respaldoSoportes;
	}
	
	public Object getRespaldoSoportes(String llave) {
		return respaldoSoportes.get(llave);
	}
}	