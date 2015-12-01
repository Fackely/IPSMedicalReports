package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class ConsultaCierreAperturaIngresoForm extends ValidatorForm
{

	private String estado;
	
	/**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtrado
     */
    private String codigoCentroAtencion;
    
    /**
     * Contiene el tipo de consulta por la que se desea filtrar
     */
    private String tipoConsulta;
    
    /**
     * Fecha Inicial para hacer el filtrado
     */
    private String fechaInicial;
    
    /**
     * Fecha Final para hacer el filtrado
     */
    private String fechaFinal;
    
    /**
     * Carga los datos del select de motivosCierre
     */
    private ArrayList<HashMap<String, Object>> tiposMotivoCierre;
    
    /**
     * Variable que maneja el motivo del cierre/apertura del ingreso
     */
    private String motivoCierre;
    
    /**
     * Carga los datos del select de motivosCierre
     */
    private ArrayList<HashMap<String, Object>> usuarioTipo;
    
    /**
     * Guarda el login del usuario que realiza el cierre/apertura del ingreso
     */
    private String usuario;
    
    /**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * HashMap con los datos arrojados por la busqueda
     */
    private HashMap busquedaCierreAperturaIngresos;
    
    /**
     * Posicion del Registro Seleccionado
     */
    private int posicion;
    
    /**
     * Mapa con los datos del detalle del registro seleccionado
     */
    private HashMap detalleCierreAperturaIngresos;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.tipoConsulta = "";
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.tiposMotivoCierre = new ArrayList<HashMap<String,Object>>();
    	this.motivoCierre = "";
    	this.usuarioTipo = new ArrayList<HashMap<String,Object>>();
    	this.usuario = "";
    	this.maxPageItems = 20;
        this.linkSiguiente = "";
        this.busquedaCierreAperturaIngresos = new HashMap();
    	this.busquedaCierreAperturaIngresos.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
    	this.detalleCierreAperturaIngresos = new HashMap();
    	this.detalleCierreAperturaIngresos.put("numRegistros", "0");
    }

    /**
     * Funcion Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
				errores.add("Centro de Atención", new ActionMessage("errors.required","El Centro de Atención "));
			if(this.tipoConsulta.trim().equals("") || this.tipoConsulta.trim().equals("null"))
				errores.add("Tipo Consulta", new ActionMessage("errors.required","El Tipo "));
			if(this.fechaInicial.trim().equals("") || this.fechaInicial.trim().equals("null"))
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
			if(this.fechaFinal.trim().equals("") || this.fechaFinal.trim().equals("null"))
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
			if(!UtilidadTexto.isEmpty(this.fechaInicial) && !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
				}
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
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the centroAtencion
	 */
	public HashMap getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(HashMap centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroAtencion(String key) 
	{
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value) 
	{
		this.centroAtencion.put(key, value);
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the tipoConsulta
	 */
	public String getTipoConsulta() {
		return tipoConsulta;
	}

	/**
	 * @param tipoConsulta the tipoConsulta to set
	 */
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the tiposMotivoCierre
	 */
	public ArrayList<HashMap<String, Object>> getTiposMotivoCierre() {
		return tiposMotivoCierre;
	}

	/**
	 * @param tiposMotivoCierre the tiposMotivoCierre to set
	 */
	public void setTiposMotivoCierre(
			ArrayList<HashMap<String, Object>> tiposMotivoCierre) {
		this.tiposMotivoCierre = tiposMotivoCierre;
	}

	/**
	 * @return the motivoCierre
	 */
	public String getMotivoCierre() {
		return motivoCierre;
	}

	/**
	 * @param motivoCierre the motivoCierre to set
	 */
	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the usuarioTipo
	 */
	public ArrayList<HashMap<String, Object>> getUsuarioTipo() {
		return usuarioTipo;
	}

	/**
	 * @param usuarioTipo the usuarioTipo to set
	 */
	public void setUsuarioTipo(ArrayList<HashMap<String, Object>> usuarioTipo) {
		this.usuarioTipo = usuarioTipo;
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
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * @return the busquedaCierreAperturaIngresos
	 */
	public HashMap getBusquedaCierreAperturaIngresos() {
		return busquedaCierreAperturaIngresos;
	}

	/**
	 * @param busquedaCierreAperturaIngresos the busquedaCierreAperturaIngresos to set
	 */
	public void setBusquedaCierreAperturaIngresos(HashMap busquedaCierreAperturaIngresos) {
		this.busquedaCierreAperturaIngresos = busquedaCierreAperturaIngresos;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getBusquedaCierreAperturaIngresos(String key) 
	{
		return busquedaCierreAperturaIngresos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setBusquedaCierreAperturaIngresos(String key, Object value) 
	{
		this.busquedaCierreAperturaIngresos.put(key, value);
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	
	/**
	 * @return the detalleCierreAperturaIngresos
	 */
	public HashMap getDetalleCierreAperturaIngresos() {
		return detalleCierreAperturaIngresos;
	}

	/**
	 * @param detalleCierreAperturaIngresos the detalleCierreAperturaIngresos to set
	 */
	public void setDetalleCierreAperturaIngresos(HashMap detalleCierreAperturaIngresos) {
		this.detalleCierreAperturaIngresos = detalleCierreAperturaIngresos;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getDetalleCierreAperturaIngresos(String key) 
	{
		return detalleCierreAperturaIngresos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setDetalleCierreAperturaIngresos(String key, Object value) 
	{
		this.detalleCierreAperturaIngresos.put(key, value);
	}
}