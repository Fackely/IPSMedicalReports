package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class PacientesHospitalizadosForm extends ValidatorForm
{

	private String estado;
	
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
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtrado
     */
    private String codigoCentroAtencion;
	
    /**
     * Nombre Centro de Atencion seleccionado para realizar el filtro
     */
    private String nombreCentroAtencion;
    
    /**
     * Contiene el tipo por la que se desea filtrar
     */
    private String tipo;
    
    /**
     * Fecha Inicial para hacer el filtrado
     */
    private String fechaInicial;
    
    /**
     * Fecha Final para hacer el filtrado
     */
    private String fechaFinal;
    
    /**
     * Carga los datos del select de Convenios
     */
    private ArrayList<HashMap<String, Object>> convenios;
    
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String convenioSeleccionado;
    
    /**
     * Variable que maneja el nombre del convenio seleccionado
     */
    private String nombreConvenioSeleccionado;
    
    /**
     * HashMap de los Centros de Costo 
     */
    private HashMap centroCosto;
    
    /**
     * Codigo del Centro de Costo seleccionado para realizar el filtrado
     */
    private String codigoCentroCosto;
    
    /**
     * Variable que maneja el nombre del centro de costo
     */
    private String nombreCentroCosto;
    
    /**
     * Carga los datos del select de pisos
     */
    private ArrayList<HashMap<String, Object>> pisos;
    
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String pisoSeleccionado;
    
    /**
     * String con el indicativo de egreso seleccionado para realizar el filtrado
     */
    private String indicativoEgreso;
    
    /**
     * HashMap con los datos arrojados por la busqueda
     */
    private HashMap busquedaPacientesHospitalizados;
    
    /**
     * Posicion del Registro Seleccionado
     */
    private int posicion;
    
    /**
     * Patron para realizar el ordenamiento
     */
    private String patronOrdenar;
	
    /**
     * Ultimo patron por el que se realizo el ordenamiento
     */
    private String ultimoPatron;
    
	/**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.maxPageItems = 20;
        this.linkSiguiente = "";
    	this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.tipo = "";
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.convenios = new ArrayList<HashMap<String,Object>>();
    	this.convenioSeleccionado = "";
    	this.centroCosto = new HashMap();
    	this.centroCosto.put("numRegistros", "0");
    	this.codigoCentroCosto = "";
    	this.pisos = new ArrayList<HashMap<String,Object>>();
    	this.pisoSeleccionado = "";
    	this.indicativoEgreso = "";
    	this.busquedaPacientesHospitalizados = new HashMap();
    	this.busquedaPacientesHospitalizados.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar = "";
    	this.ultimoPatron = "";
    	this.nombreCentroAtencion = "";
    	this.nombreCentroCosto = "";
    	this.nombreConvenioSeleccionado = "";
    }
    
    /**
     * Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
    	ActionErrors errores = new ActionErrors();
    	if(this.estado.equals("buscar"))
		{
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
				errores.add("Centro de Atención", new ActionMessage("errors.required","El Centro de Atención "));
			if(this.indicativoEgreso.trim().equals("") || this.indicativoEgreso.trim().equals("null"))
				errores.add("Indicativo Egreso", new ActionMessage("errors.required","El Indicativo Egreso "));
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
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the convenioSeleccionado
	 */
	public String getConvenioSeleccionado() {
		return convenioSeleccionado;
	}

	/**
	 * @param convenioSeleccionado the convenioSeleccionado to set
	 */
	public void setConvenioSeleccionado(String convenioSeleccionado) {
		this.convenioSeleccionado = convenioSeleccionado;
	}

	/**
	 * @return the codigoCentroCosto
	 */
	public String getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto the codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(String codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}
	
	/**
	 * @return the centroCosto
	 */
	public HashMap getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(HashMap centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroCosto(String key) 
	{
		return centroCosto.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroCosto(String key, Object value) 
	{
		this.centroCosto.put(key, value);
	}

	/**
	 * @return the pisos
	 */
	public ArrayList<HashMap<String, Object>> getPisos() {
		return pisos;
	}

	/**
	 * @param pisos the pisos to set
	 */
	public void setPisos(ArrayList<HashMap<String, Object>> pisos) {
		this.pisos = pisos;
	}

	/**
	 * @return the pisoSeleccionado
	 */
	public String getPisoSeleccionado() {
		return pisoSeleccionado;
	}

	/**
	 * @param pisoSeleccionado the pisoSeleccionado to set
	 */
	public void setPisoSeleccionado(String pisoSeleccionado) {
		this.pisoSeleccionado = pisoSeleccionado;
	}

	/**
	 * @return the indicativoEgreso
	 */
	public String getIndicativoEgreso() {
		return indicativoEgreso;
	}

	/**
	 * @param indicativoEgreso the indicativoEgreso to set
	 */
	public void setIndicativoEgreso(String indicativoEgreso) {
		this.indicativoEgreso = indicativoEgreso;
	}
	
	/**
	 * @return the busquedaCierreAperturaIngresos
	 */
	public HashMap getBusquedaPacientesHospitalizados() {
		return busquedaPacientesHospitalizados;
	}

	/**
	 * @param busquedaPacientesHospitalizados the busquedaPacientesHospitalizados to set
	 */
	public void setBusquedaPacientesHospitalizados(HashMap busquedaPacientesHospitalizados) {
		this.busquedaPacientesHospitalizados = busquedaPacientesHospitalizados;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getBusquedaPacientesHospitalizados(String key) 
	{
		return busquedaPacientesHospitalizados.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setBusquedaPacientesHospitalizados(String key, Object value) 
	{
		this.busquedaPacientesHospitalizados.put(key, value);
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

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the nombreCentroCosto
	 */
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}

	/**
	 * @param nombreCentroCosto the nombreCentroCosto to set
	 */
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * @return the nombreConvenioSeleccionado
	 */
	public String getNombreConvenioSeleccionado() {
		return nombreConvenioSeleccionado;
	}

	/**
	 * @param nombreConvenioSeleccionado the nombreConvenioSeleccionado to set
	 */
	public void setNombreConvenioSeleccionado(String nombreConvenioSeleccionado) {
		this.nombreConvenioSeleccionado = nombreConvenioSeleccionado;
	}
    
}