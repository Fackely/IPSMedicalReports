package com.princetonsa.actionform.seccionesparametrizables;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;

/**
 * 
 * @author wilson
 *
 */
public class RecordAnestesiaForm extends ValidatorForm 
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
	 * mapa con los signos vitales
	 */
	private HashMap<Object, Object> mapaSignosVitales;
	
	/**
	 * mapa con los signos vitales
	 */
	private HashMap<Object, Object> mapaEventos;
	
	/**
	 * mapa con los gases
	 */
	private HashMap<Object, Object> mapaGases;
	
	/**
	 * mapa con los medicamentos
	 */
	private HashMap<Object, Object> mapaMedicamentos;
	
	/**
	 * mapa con los medicamentos
	 */
	private HashMap<Object, Object> mapaLiquidos;
	
	/**
	 * mapa con los infusiones
	 */
	private HashMap<Object, Object> mapaInfusiones;
	
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
	 * Indicador ocultar menu 
	 * */
	private boolean ocultarMenu = false;
	
	/**
	 * 
	 */
	private String fechaInicial;
	
	/**
	 * 
	 */
	private String horaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	/**
	 * 
	 */
	private String horaFinal;
	
	/**
	 * diferencia entre la fecha incial y final 
	 */
	private int minutos;
	
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.mapaSignosVitales= new HashMap<Object, Object>();
    	this.mapaSignosVitales.put("numRegistros", "0");
    	HashMap<Object, Object> subMapa= new HashMap<Object, Object>();
    	subMapa.put("numRegistros", 0);
    	this.mapaSignosVitales.put("TITULOS", subMapa);
    	this.mapaSignosVitales.put("CAMPOS", subMapa);
    	
    	this.mapaEventos= new HashMap<Object, Object>();
    	this.mapaEventos.put("numRegistros", "0");
    	
    	this.mapaGases= new HashMap<Object, Object>();
    	this.mapaGases.put("numRegistros", "0");
    	
    	this.mapaMedicamentos= new HashMap<Object, Object>();
    	this.mapaMedicamentos.put("numRegistros", "0");
    	
    	this.mapaLiquidos= new HashMap<Object, Object>();
    	this.mapaLiquidos.put("numRegistros", "0");
    	
    	this.mapaInfusiones= new HashMap<Object, Object>();
    	this.mapaInfusiones.put("numRegistros", "0");
    	
    	this.esSinEncabezado = "";
    	
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.horaFinal="";
    	this.horaInicial="";
    	
    	this.minutos=ConstantesBD.codigoNuncaValido;
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
	 * @return the mapaSignosVitales
	 */
	public HashMap<Object, Object> getMapaSignosVitales() {
		return mapaSignosVitales;
	}

	/**
	 * @param mapaSignosVitales the mapaSignosVitales to set
	 */
	public void setMapaSignosVitales(HashMap<Object, Object> mapaSignosVitales) {
		this.mapaSignosVitales = mapaSignosVitales;
	}
	
    /**
	 * @return the mapaSignosVitales
	 */
	public Object getMapaSignosVitales(Object key) {
		return mapaSignosVitales.get(key);
	}

	/**
	 * @param mapaSignosVitales the mapaSignosVitales to set
	 */
	public void setMapaSignosVitales(Object key, Object value) {
		this.mapaSignosVitales.put(key, value);
	}
	
	/**
	 * @return the mapaSignosVitales
	 */
	public HashMap<Object, Object> getMapaTitulosSignosVitales() {
		return  (HashMap<Object, Object>)((HashMap<Object, Object>)mapaSignosVitales.get("TITULOS")).clone();
	}

	/**
	 * @return the mapaSignosVitales
	 */
	public HashMap<Object, Object> getMapaCamposSignosVitales() {
		return  (HashMap<Object, Object>)((HashMap<Object, Object>)mapaSignosVitales.get("CAMPOS")).clone();
	}
	
	/**
	 * @return the mapaSignosVitales
	 */
	public void setMapaCamposSignosVitales(Object key, Object value) {
		((HashMap<Object, Object>)mapaSignosVitales.get("CAMPOS")).put(key, value);
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
	 * @return the mapaEventos
	 */
	public HashMap<Object, Object> getMapaEventos() {
		return mapaEventos;
	}

	/**
	 * @param mapaEventos the mapaEventos to set
	 */
	public void setMapaEventos(HashMap<Object, Object> mapaEventos) {
		this.mapaEventos = mapaEventos;
	}
	
	/**
	 * @return the mapaEventos
	 */
	public Object getMapaEventos(Object key) {
		return mapaEventos.get(key);
	}

	/**
	 * @param mapaEventos the mapaEventos to set
	 */
	public void setMapaEventos(Object key, Object value) {
		this.mapaEventos.put(key, value);
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
	 * @return the horaFinalCx
	 */
	public String getHoraFinal() {
		return horaFinal;
	}

	/**
	 * @param horaFinal the horaFinalCx to set
	 */
	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	/**
	 * @return the horaInicial
	 */
	public String getHoraInicial() {
		return horaInicial;
	}

	/**
	 * @param horaInicial the horaInicial to set
	 */
	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}

	/**
	 * @return the minutos
	 */
	public int getMinutos() {
		return minutos;
	}

	/**
	 * @param minutos the minutos to set
	 */
	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}

	/**
	 * @return the mapaGases
	 */
	public HashMap<Object, Object> getMapaGases() {
		return mapaGases;
	}

	/**
	 * @param mapaGases the mapaGases to set
	 */
	public void setMapaGases(HashMap<Object, Object> mapaGases) {
		this.mapaGases = mapaGases;
	}
	
	/**
	 * @return the mapa
	 */
	public Object getMapaGases(Object key) {
		return mapaGases.get(key);
	}

	/**
	 * @param mapaEventos the mapaEventos to set
	 */
	public void setMapaGases(Object key, Object value) {
		this.mapaGases.put(key, value);
	}

	/**
	 * @return the mapaMedicamentos
	 */
	public HashMap<Object, Object> getMapaMedicamentos() {
		return mapaMedicamentos;
	}

	/**
	 * @param mapaMedicamentos the mapaMedicamentos to set
	 */
	public void setMapaMedicamentos(HashMap<Object, Object> mapaMedicamentos) {
		this.mapaMedicamentos = mapaMedicamentos;
	}
	
	/**
	 * @return the mapa
	 */
	public Object getMapaMedicamentos(Object key) {
		return mapaMedicamentos.get(key);
	}

	/**
	 * @param mapaEventos the mapaEventos to set
	 */
	public void setMapaMedicamentos(Object key, Object value) {
		this.mapaMedicamentos.put(key, value);
	}

	/**
	 * @return the mapaLiquidos
	 */
	public HashMap<Object, Object> getMapaLiquidos() {
		return mapaLiquidos;
	}

	/**
	 * @param mapaLiquidos the mapaLiquidos to set
	 */
	public void setMapaLiquidos(HashMap<Object, Object> mapaLiquidos) {
		this.mapaLiquidos = mapaLiquidos;
	}
	
	/**
	 * @return the mapa
	 */
	public Object getMapaLiquidos(Object key) {
		return mapaLiquidos.get(key);
	}

	/**
	 * @param mapaEventos the mapaEventos to set
	 */
	public void setMapaLiquidos(Object key, Object value) {
		this.mapaLiquidos.put(key, value);
	}

	/**
	 * @return the mapaInfusiones
	 */
	public HashMap<Object, Object> getMapaInfusiones() {
		return mapaInfusiones;
	}

	/**
	 * @param mapaInfusiones the mapaInfusiones to set
	 */
	public void setMapaInfusiones(HashMap<Object, Object> mapaInfusiones) {
		this.mapaInfusiones = mapaInfusiones;
	}

	/**
	 * @return the mapa
	 */
	public Object getMapaInfusiones(Object key) {
		return mapaInfusiones.get(key);
	}

	/**
	 * @param mapaEventos the mapaEventos to set
	 */
	public void setMapaInfusiones(Object key, Object value) {
		this.mapaInfusiones.put(key, value);
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
	
	public void setMostrarDatosInfoActivo(boolean valor){
		this.mostrarDatosInfo.setActivo(valor);
	}
	public boolean getMostrarDatosInfoActivo(){
		return this.mostrarDatosInfo.getActivo();
	}

	/**
	 * @return the mostrarDatosInfo
	 */
	public InfoDatosString getMostrarDatosInfo() {
		return mostrarDatosInfo;
	}

	/**
	 * @param mostrarDatosInfo the mostrarDatosInfo to set
	 */
	public void setMostrarDatosInfo(InfoDatosString mostrarDatosInfo) {
		this.mostrarDatosInfo = mostrarDatosInfo;
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