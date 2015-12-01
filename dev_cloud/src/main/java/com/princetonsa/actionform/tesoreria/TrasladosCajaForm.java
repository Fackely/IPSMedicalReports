package com.princetonsa.actionform.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * los traslados caja
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Feb 26, 2007
 * @author wrios 
 */
public class TrasladosCajaForm extends ValidatorForm  
{
	/**
	 * 
	 */
	private String consecutivoTraslado="";
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * caja ppal
	 */
	private String cajaPpal;
	
	/**
	 * 
	 */
	private String fechaTraslado;
	
	/**
	 * 
	 */
	private String cajaMayor;
	
	/**
	 * 
	 */
	private HashMap cajasPpalTagMap;
	
	/**
	 * 
	 */
	private HashMap cajasMayorTagMap;
	
	/**
	 * 
	 */
	private HashMap trasladoCajaMap;
	
	/**
	 * 
	 */
	private String fechaGeneracionTraslado;
	
	/**
	 * 
	 */
	private String horaGeneracionTraslado;
	
	/**
	 * 
	 */
	private Connection conexionBLOQUEO;
	
	/**
	 * 
	 */
	private HashMap criteriosBusquedaMap;
	
	/**
	 * 
	 */
	private HashMap listadoMap;
	
	/**
	 * 
	 */
	private String usuarioTraslada;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset(boolean resetearConsecutivo)
    {
    	this.trasladoCajaMap= new HashMap();
    	this.conexionBLOQUEO= null;
    	this.listadoMap= new HashMap();
    	this.listadoMap.put("numRegistros", "0");
    	
    	if(resetearConsecutivo)
    	{	
    		consecutivoTraslado="";
    		this.criteriosBusquedaMap= new HashMap();
    		this.cajaPpal="";
    		this.cajaMayor="";
    		this.fechaTraslado=UtilidadFecha.getFechaActual();
    		this.fechaGeneracionTraslado="";
        	this.horaGeneracionTraslado="";
        	this.usuarioTraslada="";
    	}	
    }
    
    /**
	 * 
	 * 
	 */
	public void inicializarTags(String loginUsuario, int codigoInstitucion, int codigoCentroAtencion)
	{
		this.cajasPpalTagMap= Utilidades.obtenerCajasCajero(loginUsuario, codigoInstitucion, ConstantesBD.codigoTipoCajaPpal, codigoCentroAtencion);
		this.cajasMayorTagMap= Utilidades.obtenerCajas(codigoInstitucion, ConstantesBD.codigoTipoCajaMayor); 
	}
    
	/**
	 * 
	 * 
	 */
	public void inicializarTagsConsulta(int codigoInstitucion)
	{
		this.cajasPpalTagMap= Utilidades.obtenerCajas(codigoInstitucion, ConstantesBD.codigoTipoCajaPpal);
		this.cajasMayorTagMap= Utilidades.obtenerCajas(codigoInstitucion, ConstantesBD.codigoTipoCajaMayor); 
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
        if(this.estado.equals("generar"))
        {
        	if(this.getCajaPpal().trim().equals(""))
        	{
        		errores.add("", new ActionMessage("errors.required","El campo Caja Ppal"));
        	}
        	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaTraslado()))
        	{
        		errores.add("Fecha ", new ActionMessage("errors.formatoFechaInvalido", this.getFechaTraslado()));
        	}
        	else
        	{
        		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaTraslado(), UtilidadFecha.getFechaActual()))
        		{
        			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Traslado", "Actual"));
        		}
        	}
        	if(this.getCajaMayor().trim().equals(""))
        	{
        		errores.add("", new ActionMessage("errors.required","El campo Caja Mayor"));
        	}
        }
        if(this.estado.equals("resultadoConsulta"))
        {
        	double rangoInicial=0;
        	double rangoFinal=0;
        	//si cumple esta condicion entonces se realiza el filtro solo por estos campos
        	if(!this.getCriteriosBusquedaMap("rangoInicial").toString().trim().equals("") || !this.getCriteriosBusquedaMap("rangoFinal").toString().trim().equals(""))
        	{
        		try
        		{
        			rangoInicial=Double.parseDouble(this.getCriteriosBusquedaMap("rangoInicial").toString().trim());
        		}
        		catch (Exception e) 
        		{
        			errores.add("", new ActionMessage("errors.integerMayorQue", "El campo Rango Inicial", "0"));
				}
        		try
        		{
        			rangoFinal=Double.parseDouble(this.getCriteriosBusquedaMap("rangoFinal").toString().trim());
        		}
        		catch (Exception e) 
        		{
        			errores.add("", new ActionMessage("errors.integerMayorQue", "El campo Rango Final", "0"));
				}
        		
        		if(errores.isEmpty())
        		{	
	        		if(rangoFinal<rangoInicial)
	        		{
	        			errores.add("", new ActionMessage("errors.MayorIgualQue", "El campo Rango Final", "el campo Rango Inicial"));
	        		}
        		}	
        	}
        	//de lo contrario entonces es requerido el rango de fechas
        	else
        	{
        		if(!this.getCriteriosBusquedaMap("fechaInicial").toString().equals("") || !this.getCriteriosBusquedaMap("fechaFinal").toString().equals(""))
        		{	
	        		if(!UtilidadFecha.esFechaValidaSegunAp(this.getCriteriosBusquedaMap("fechaInicial").toString()))
	        		{
	        			errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial"));
	        		}
	        		if(!UtilidadFecha.esFechaValidaSegunAp(this.getCriteriosBusquedaMap("fechaFinal").toString()))
	        		{
	        			errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final"));
	        		}
	        		if(errores.isEmpty())
	        		{
	        			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").toString(), this.getCriteriosBusquedaMap("fechaFinal").toString()))
	        			{
	        				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Final", "Inicial"));
	        			}
	        			if(errores.isEmpty())
	        			{
	        				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaFinal").toString(), UtilidadFecha.getFechaActual()))
		        			{
		        				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "Actual"));
		        			}
	        			}
	        		}
        		}	
        	}
        	
        	if(errores.isEmpty())
        	{
        		//evaluar que algun parametro este lleno (de los posibles requeridos)
        		if(	this.getCriteriosBusquedaMap("rangoInicial").toString().trim().equals("") 
        			&& this.getCriteriosBusquedaMap("rangoFinal").toString().trim().equals("")
        			&& !UtilidadFecha.esFechaValidaSegunAp(this.getCriteriosBusquedaMap("fechaInicial").toString())
        			&& !UtilidadFecha.esFechaValidaSegunAp(this.getCriteriosBusquedaMap("fechaFinal").toString()))
        		{
        			errores.add("", new ActionMessage("error.trasladosCaja.camposRequeridos"));
        		}
        	}
        	
        	if(!errores.isEmpty())
        	{
        		this.setEstado("consultaConErrores");
        	}
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
	 * @return the cajaMayor
	 */
	public String getCajaMayor() {
		return cajaMayor;
	}

	/**
	 * @param cajaMayor the cajaMayor to set
	 */
	public void setCajaMayor(String cajaMayor) {
		this.cajaMayor = cajaMayor;
	}

	/**
	 * @return the cajaPpal
	 */
	public String getCajaPpal() {
		return cajaPpal;
	}

	/**
	 * @param cajaPpal the cajaPpal to set
	 */
	public void setCajaPpal(String cajaPpal) {
		this.cajaPpal = cajaPpal;
	}

	/**
	 * @return the fechaTraslado
	 */
	public String getFechaTraslado() {
		return fechaTraslado;
	}

	/**
	 * @param fechaTraslado the fechaTraslado to set
	 */
	public void setFechaTraslado(String fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}

	/**
	 * @return the cajasMayorTagMap
	 */
	public HashMap getCajasMayorTagMap() {
		return cajasMayorTagMap;
	}

	/**
	 * @param cajasMayorTagMap the cajasMayorTagMap to set
	 */
	public void setCajasMayorTagMap(HashMap cajasMayorTagMap) {
		this.cajasMayorTagMap = cajasMayorTagMap;
	}

	/**
	 * @return the cajasPpalTagMap
	 */
	public HashMap getCajasPpalTagMap() {
		return cajasPpalTagMap;
	}

	/**
	 * @param cajasPpalTagMap the cajasPpalTagMap to set
	 */
	public void setCajasPpalTagMap(HashMap cajasPpalTagMap) {
		this.cajasPpalTagMap = cajasPpalTagMap;
	}
    
	/**
	 * @return the cajasMayorTagMap
	 */
	public Object getCajasMayorTagMap(String key) {
		return cajasMayorTagMap.get(key);
	}

	/**
	 * @return the cajasPpalTagMap
	 */
	public Object getCajasPpalTagMap(String key) {
		return cajasPpalTagMap.get(key);
	}

	/**
	 * @return the trasladoCajaMap
	 */
	public HashMap getTrasladoCajaMap() {
		return trasladoCajaMap;
	}

	/**
	 * @param trasladoCajaMap the trasladoCajaMap to set
	 */
	public void setTrasladoCajaMap(HashMap trasladoCajaMap) {
		this.trasladoCajaMap = trasladoCajaMap;
	}

	/**
	 * @return the trasladoCajaMap
	 */
	public Object getTrasladoCajaMap(Object key) {
		return trasladoCajaMap.get(key);
	}

	/**
	 * @param trasladoCajaMap the trasladoCajaMap to set
	 */
	public void setTrasladoCajaMap(Object key, Object value) {
		this.trasladoCajaMap.put(key, value);
	}

	/**
	 * @return the fechaGeneracionTraslado
	 */
	public String getFechaGeneracionTraslado() {
		return fechaGeneracionTraslado;
	}

	/**
	 * @param fechaGeneracionTraslado the fechaGeneracionTraslado to set
	 */
	public void setFechaGeneracionTraslado(String fechaGeneracionTraslado) {
		this.fechaGeneracionTraslado = fechaGeneracionTraslado;
	}

	/**
	 * @return the horaGeneracionTraslado
	 */
	public String getHoraGeneracionTraslado() {
		return horaGeneracionTraslado;
	}

	/**
	 * @param horaGeneracionTraslado the horaGeneracionTraslado to set
	 */
	public void setHoraGeneracionTraslado(String horaGeneracionTraslado) {
		this.horaGeneracionTraslado = horaGeneracionTraslado;
	}

	/**
	 * @return the conexionBLOQUEO
	 */
	public Connection getConexionBLOQUEO() {
		return conexionBLOQUEO;
	}

	/**
	 * @param conexionBLOQUEO the conexionBLOQUEO to set
	 */
	public void setConexionBLOQUEO(Connection conexionBLOQUEO) {
		this.conexionBLOQUEO = conexionBLOQUEO;
	}

	/**
	 * @return the consecutivoTraslado
	 */
	public String getConsecutivoTraslado() {
		return consecutivoTraslado;
	}

	/**
	 * @param consecutivoTraslado the consecutivoTraslado to set
	 */
	public void setConsecutivoTraslado(String consecutivoTraslado) {
		this.consecutivoTraslado = consecutivoTraslado;
	}

	/**
	 * @return the criteriosBusquedaMap
	 */
	public HashMap getCriteriosBusquedaMap() {
		return criteriosBusquedaMap;
	}

	/**
	 * @param criteriosBusquedaMap the criteriosBusquedaMap to set
	 */
	public void setCriteriosBusquedaMap(HashMap criteriosBusquedaMap) {
		this.criteriosBusquedaMap = criteriosBusquedaMap;
	}
	
	/**
	 * @return the criteriosBusquedaMap
	 */
	public Object getCriteriosBusquedaMap(Object key) {
		return criteriosBusquedaMap.get(key);
	}

	/**
	 * @param criteriosBusquedaMap the criteriosBusquedaMap to set
	 */
	public void setCriteriosBusquedaMap(Object key, Object value) {
		this.criteriosBusquedaMap.put(key, value);
	}

	/**
	 * @return the listadoMap
	 */
	public HashMap getListadoMap() {
		return listadoMap;
	}

	/**
	 * @param listadoMap the listadoMap to set
	 */
	public void setListadoMap(HashMap listadoMap) {
		this.listadoMap = listadoMap;
	}
	
	/**
	 * @return the listadoMap
	 */
	public Object getListadoMap(Object key) {
		return listadoMap.get(key);
	}

	/**
	 * @param listadoMap the listadoMap to set
	 */
	public void setListadoMap(Object key, Object value) {
		this.listadoMap.put(key, value);
	}

	/**
	 * @return the usuarioTraslada
	 */
	public String getUsuarioTraslada() {
		return usuarioTraslada;
	}

	/**
	 * @param usuarioTraslada the usuarioTraslada to set
	 */
	public void setUsuarioTraslada(String usuarioTraslada) {
		this.usuarioTraslada = usuarioTraslada;
	}

}