/*
 * @(#)ResponderCirugiasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.ordenesmedicas.cirugias;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionForm;

import util.ConstantesBD;


/**
 * Forma para manejo presentación de la funcionalidad 
 * de Responder Cirugías. 
 *  @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 02 /Nov/ 2005
 */
@SuppressWarnings("unchecked")
public class ResponderCirugiasForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * Cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Almacena los datos de las peticiones en el flujo medico
	 */
	private HashMap mapaPeticionesMedico;
	
	/**
	 * Almacena los datos de las peticiones
	 */
	private HashMap mapaPeticionesPaciente;
	
	/**
	 * Almacena el detalle de cada peticion
	 */
	private HashMap mapaDetallePeticion;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	         
     /**
      * Poscicion del mapa en la consulta de facturas
      */
     private int posicionMapa;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
 	 * String para saber si una peticion tiene orden
 	 */
 	private String tieneOrden;
 	
 	/**
 	 * String para almacenar el tipo de respuesta por la que se opta 
 	 */
 	private String tipoRespuesta;
 	
 	/**
 	 * Entero con el consecutivo de la peticion
 	 */
 	private int consecutivoPeticion;
 	
 	/**
 	 * 
 	 */
 	private String selectRespuesta;
 	
 	/**
 	 * 
 	 */
 	
 	/**
 	 * 
 	 */
	private String numeroSolicitud;
	
	/**
	 * 
	 */
	private String justificacionSolicitud;
	
	/**
	 * 
	 */
	private int posJustificacionSolicitud;
	
	/**
	 * 
	 */
 	private int consecutivoOrdenesMedicas;
 	
 	/**
	 * Contiene el mapa de estados de Autorizacion 
	 */
	private HashMap estadoAuto;
	
	

	// Anexo 179 - Cambio 1.50
	private String numeroSolicitudValidacionCapitacion;
	private boolean sinAutorizacionEntidadsubcontratada = false;;
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	
	
	
	public void reset ()
	{
		this.mapaPeticionesPaciente = new HashMap ();
		this.mapaPeticionesPaciente.put("numRegistros", "0");
		this.mapaDetallePeticion=new HashMap();
		this.mapaDetallePeticion.put("numRegistros", "0");
		this.mapaPeticionesMedico= new HashMap();
		this.estado="";
		this.cuenta=0;
		this.tieneOrden="";
		this.tipoRespuesta="";
		this.posicionMapa=0;
		this.consecutivoPeticion=0;
		this.consecutivoOrdenesMedicas=ConstantesBD.codigoNuncaValido;
		this.estadoAuto=new HashMap();
		this.numeroSolicitud="";
		this.justificacionSolicitud="";
		this.posJustificacionSolicitud=ConstantesBD.codigoNuncaValido;
		
		this.numeroSolicitudValidacionCapitacion 	= null;
		//this.sinAutorizacionEntidadsubcontratada	= false;
		this.listaAdvertencias						= new ArrayList<String>();
	}
	
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaPeticionesPaciente = new HashMap ();
		this.mapaDetallePeticion= new HashMap ();
		this.mapaPeticionesMedico= new HashMap();
	}
	
	
	
	
	public String getSelectRespuesta()
	{
		return selectRespuesta;
	}
	public void setSelectRespuesta(String selectRespuesta)
	{
		this.selectRespuesta=selectRespuesta;
	}
	/**
	 * @return Returns the consecutivoPeticion.
	 */
	public int getConsecutivoPeticion()
	{
		return consecutivoPeticion;
	}
	/**
	 * @param consecutivoPeticion The consecutivoPeticion to set.
	 */
	public void setConsecutivoPeticion(int consecutivoPeticion)
	{
		this.consecutivoPeticion=consecutivoPeticion;
	}
	/**
	 * @return Returns the tipoRespuesta.
	 */
	public String getTipoRespuesta()
	{
		return tipoRespuesta;
	}
	/**
	 * @param tipoRespuesta The tipoRespuesta to set.
	 */
	public void setTipoRespuesta(String tipoRespuesta)
	{
		this.tipoRespuesta=tipoRespuesta;
	}
	/**
	 * @return Returns the tieneOrden.
	 */
	public String getTieneOrden()
	{
		return tieneOrden;
	}
	/**
	 * @param tieneOrden The tieneOrden to set.
	 */
	public void setTieneOrden(String tieneOrden)
	{
		this.tieneOrden=tieneOrden;
	}
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}
	
	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa= posicionMapa;
	}
	
	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset= offset;
	}
	
	/**
	 * @return Returns the cuenta.
	 */
	public int getCuenta()
	{
		return cuenta;
	}
	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta= cuenta;
	}
	
    /**
     * @return Retorna el estado.
     */
    public  String getEstado()
    {
        return estado;
    }
    
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    
	/**
	 * @return Returns the mapaDetallePeticion.
	 */
	public HashMap getMapaDetallePeticion()
	{
		return mapaDetallePeticion;
	}
	
	/**
	 * @param mapaFacturasPaciente The mapaDetallePeticion to set.
	 */
	public void setMapaDetallePeticion(HashMap mapaDetallePeticion)
	{
		this.mapaDetallePeticion= mapaDetallePeticion;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetallePeticion(String key, Object value) 
	{
		mapaDetallePeticion.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetallePeticion(String key) 
	{
		return mapaDetallePeticion.get(key);
	}
	
	/**
	 * @return Returns the mapaPeticionesPaciente.
	 */
	public HashMap getMapaPeticionesPaciente()
	{
		return mapaPeticionesPaciente;
	}
	
	/**
	 * @param mapaFacturasPaciente The mapaPeticionesPaciente to set.
	 */
	public void setMapaPeticionesPaciente(HashMap mapaPeticionesPaciente)
	{
		this.mapaPeticionesPaciente= mapaPeticionesPaciente;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaPeticionesPaciente(String key, Object value) 
	{
		mapaPeticionesPaciente.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaPeticionesPaciente(String key) 
	{
		return mapaPeticionesPaciente.get(key);
	}
	
	/**
	 * @return Returns the mapaPeticionesMedico.
	 */
	public HashMap getMapaPeticionesMedico()
	{
		return mapaPeticionesMedico;
	}
	
	/**
	 * @param mapaPeticionesMedico The mapaPeticionesMedico to set.
	 */
	public void setMapaPeticionesMedico(HashMap mapaPeticionesMedico)
	{
		this.mapaPeticionesMedico= mapaPeticionesMedico;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaPeticionesMedico(String key, Object value) 
	{
		mapaPeticionesMedico.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaPeticionesMedico(String key) 
	{
		return mapaPeticionesMedico.get(key);
	}
	
	/**
     * @return Retorna patronOrdenar.
     */
    public String getPatronOrdenar() 
    {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar Asigna patronOrdenar.
     */
    public void setPatronOrdenar(String patronOrdenar)
    {
        this.patronOrdenar = patronOrdenar;
    }
	
    /**
	 * @return Retorna el ultimoPatron.
	 */
	public String getUltimoPatron() 
	{
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna el ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) 
	{
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the consecutivoOrdenesMedicas
	 */
	public int getConsecutivoOrdenesMedicas() {
		return consecutivoOrdenesMedicas;
	}


	/**
	 * @param consecutivoOrdenesMedicas the consecutivoOrdenesMedicas to set
	 */
	public void setConsecutivoOrdenesMedicas(int consecutivoOrdenesMedicas) {
		this.consecutivoOrdenesMedicas = consecutivoOrdenesMedicas;
	}


	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
    
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitudInt() {
		return Integer.parseInt(this.numeroSolicitud);
	}

	public HashMap getEstadoAuto() {
		return estadoAuto;
	}


	public void setEstadoAuto(HashMap estadoAuto) {
		this.estadoAuto = estadoAuto;
	}


	public String getJustificacionSolicitud() {
		return justificacionSolicitud;
	}


	public void setJustificacionSolicitud(String justificacionSolicitud) {
		this.justificacionSolicitud = justificacionSolicitud;
	}


	public int getPosJustificacionSolicitud() {
		return posJustificacionSolicitud;
	}


	public void setPosJustificacionSolicitud(int posJustificacionSolicitud) {
		this.posJustificacionSolicitud = posJustificacionSolicitud;
	}


	public String getNumeroSolicitudValidacionCapitacion() {
		return numeroSolicitudValidacionCapitacion;
	}


	public void setNumeroSolicitudValidacionCapitacion(
			String numeroSolicitudValidacionCapitacion) {
		this.numeroSolicitudValidacionCapitacion = numeroSolicitudValidacionCapitacion;
	}


	public boolean isSinAutorizacionEntidadsubcontratada() {
		return sinAutorizacionEntidadsubcontratada;
	}


	public void setSinAutorizacionEntidadsubcontratada(
			boolean sinAutorizacionEntidadsubcontratada) {
		this.sinAutorizacionEntidadsubcontratada = sinAutorizacionEntidadsubcontratada;
	}


	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}


	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}
	
}
