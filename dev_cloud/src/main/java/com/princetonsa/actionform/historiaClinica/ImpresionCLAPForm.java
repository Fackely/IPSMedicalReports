package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadCadena;
import util.ConstantesBD;
import util.Utilidades;

public class ImpresionCLAPForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;

	/**
	 * Mapa que contiene todas las solicitudes-admision, relacionadas a una cirugia con de parto o cesaria.
	 */
	private HashMap solicitudes;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * Variable para manejar el codigo de la cirugia a la cual se le quiere consultar el detalle.
	 */
	private String itemSeleccionado;
	
	/**
	 * Mapa que contiene los datos del recien nacido.
	 */
	private HashMap mapaSeccionRecienNacido;
	
/**
	 * Mapa que contiene los datos de la informaci?n del parto
	 */
	private HashMap mapaSeccionInformacionParto;
	
	
	//------------------ SECCION GESTACION ACTUAL ---------------------------------//
	/**
	 * Mapa que guarda la informaci?n de la gestaci?n actual 
	 */
	private HashMap mapaGestacionActual;
	
	
	//------------------ SECCION CONSULTA Y CONTROLES ---------------------------------//
	/**
	 * Mapa que guarda la informaci?n de los encabezados de resuemen gestacional que se mostraran
	 * en la secci?n consulta y controles
	 */
	private HashMap mapaEncabezadoConsultaControles;
	
	/**
	 * Mapa que guarda el detalle de la informaci?n de resumen gestacional del embarazo, que se mostrara
	 * en la seccion consulta y controles
	 */
	private HashMap mapaDetalleConsultaControles;
	
	/**
	 * mapa de la seccion vigilancia clinica Map
	 */
	private HashMap vigilanciaClinicaMap= new HashMap();
	
	/**
	 * mapa de la seccion partograma
	 */
	private HashMap partogramaMap= new HashMap();
		
	
	/**
     * Mapa para traer la informacion de antecedentes de familiares
     */
	private HashMap mapaAntFamiliares;
	
	/**
	 * Mapa Para Antecedentes de Medicos y QuirurGicos.
	 */
	private HashMap mapaAntMedicos;
	
	/**
	 * Mapa Para Antecedentes Toxicos
	 */
	private HashMap mapaAntToxicos;
	
	/**
     * Mapa para traer la informacion de antecedentes de Gineco Obstetricos.
     */
	private HashMap mapaAntGineco;
	
	/**
	 * Hist?rico de la duracion de la menstruacion para Antecedentes Gineco Obstetricos  
	 */
	private ArrayList historicos;
	
	/**
	 * 
	 */
	private String controlPrenatal;
	
	/**
	 * Id de la cuenta
	 */
	private String idCuenta;
	
	/**
	 * 
	 */
	private String partoEn;
	
	public void reset()
	{
		this.solicitudes=new HashMap();
		this.solicitudes.put("numRegistros", "0");
		
		this.mapaSeccionRecienNacido=new HashMap();
		this.mapaSeccionRecienNacido.put("numRegistros", "0");
		
		this.mapaSeccionInformacionParto=new HashMap();
		this.mapaSeccionInformacionParto.put("numRegistros", "0");
		
		
		//------------Gestaci?n Actual ---------------------//
		this.mapaGestacionActual=new HashMap();
		this.mapaGestacionActual.put("numRegistros", "0");
		
		
		//------------Consulta y Controles ---------------------//
		this.mapaEncabezadoConsultaControles=new HashMap();
		this.mapaEncabezadoConsultaControles.put("numRegistros", "0");
		
		this.mapaDetalleConsultaControles=new HashMap();
		this.mapaDetalleConsultaControles.put("numRegistros", "0");

		//-----------------------------------------------------------//
		
		this.vigilanciaClinicaMap= new HashMap();
		this.vigilanciaClinicaMap.put("numRegistros", "0");
		this.partogramaMap= new HashMap();
		this.partogramaMap.put("existePartograma", ConstantesBD.acronimoNo);
		this.inicializarTagsPartograma();
		
		this.maxPageItems=0;
		
		this.itemSeleccionado="";
		
		//-- Manizales
		this.mapaAntFamiliares = new HashMap();
		this.mapaAntMedicos = new HashMap();
		this.mapaAntToxicos = new HashMap();
		this.mapaAntGineco = new HashMap();
		this.historicos = new ArrayList();
		
		this.controlPrenatal="";
		this.partoEn="";
		
		this.idCuenta = "";
	}

	/**
	 * 
	 * 
	 */
	public void inicializarTagsPartograma()
	{
		this.partogramaMap.put("mapaPosiciones", Utilidades.obtenerCodigoNombreTablaMap("posiciones"));
		this.partogramaMap.put("mapaParidades", Utilidades.obtenerCodigoNombreTablaMap("paridades"));
		this.partogramaMap.put("mapaMembranas", Utilidades.obtenerCodigoNombreTablaMap("membranas"));
	}

	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		

		return errores;
	}

	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}


	public HashMap getSolicitudes()
	{
		return solicitudes;
	}


	public void setSolicitudes(HashMap solicitudes)
	{
		this.solicitudes = solicitudes;
	}
	public Object getSolicitudes(String key)
	{
		return solicitudes.get(key);
	}


	public void setSolicitudes(String key,Object value)
	{
		this.solicitudes.put(key, value);
	}


	public int getMaxPageItems()
	{
		return maxPageItems;
	}


	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	public String getItemSeleccionado()
	{
		return itemSeleccionado;
	}


	public void setItemSeleccionado(String itemSeleccionado)
	{
		this.itemSeleccionado = itemSeleccionado;
	}
	

	public HashMap getMapaGestacionActual() {
		return mapaGestacionActual;
	}



	public void setMapaGestacionActual(HashMap mapaGestacionActual) {
		this.mapaGestacionActual = mapaGestacionActual;
	}
	
	public Object getMapaGestacionActual(String key) {
		return mapaGestacionActual.get(key);
	}



	public void setMapaGestacionActual(String key,Object obj) {
		this.mapaGestacionActual.put(key,obj);
	}



	public HashMap getMapaDetalleConsultaControles() {
		return mapaDetalleConsultaControles;
	}



	public void setMapaDetalleConsultaControles(HashMap mapaDetalleConsultaControles) {
		this.mapaDetalleConsultaControles = mapaDetalleConsultaControles;
	}



	public HashMap getMapaEncabezadoConsultaControles() {
		return mapaEncabezadoConsultaControles;
	}



	public void setMapaEncabezadoConsultaControles(
			HashMap mapaEncabezadoConsultaControles) {
		this.mapaEncabezadoConsultaControles = mapaEncabezadoConsultaControles;
	}
	
	/**
	 * @return Retorna mapaEncabezadoConsultaControles.
	 */
	public Object getMapaEncabezadoConsultaControles(String key)
	{
		return mapaEncabezadoConsultaControles.get(key+"");
	}

	/**
	 * @param mapaEncabezadoConsultaControles Asigna mapaEncabezadoConsultaControles.
	 */
	public void setMapaEncabezadoConsultaControles(String key, String valor)
	{
		this.mapaEncabezadoConsultaControles.put(key,valor);
	}


	
	
	public HashMap getMapaSeccionRecienNacido()
	{
		return mapaSeccionRecienNacido;
	}



	public void setMapaSeccionRecienNacido(HashMap mapaSeccionRecienNacido)
	{
		this.mapaSeccionRecienNacido = mapaSeccionRecienNacido;
	}



	/**
	 * @return the mapaSeccionInformacionParto
	 */
	public HashMap getMapaSeccionInformacionParto() {
		return mapaSeccionInformacionParto;
	}



	/**
	 * @param mapaSeccionInformacionParto the mapaSeccionInformacionParto to set
	 */
	public void setMapaSeccionInformacionParto(HashMap mapaSeccionInformacionParto) {
		this.mapaSeccionInformacionParto = mapaSeccionInformacionParto;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaSeccionInformacionParto
	 */
	public Object getMapaSeccionInformacionParto(String key) {
		return mapaSeccionInformacionParto.get(key);
	}



	/**
	 * @param Asigna elemento al mapa mapaSeccionInformacionParto
	 */
	public void setMapaSeccionInformacionParto(String key,Object obj) {
		this.mapaSeccionInformacionParto.put(key,obj);
	}	
	
	/**
	 * @return Returns the partogramaMap.
	 */
	public HashMap getPartogramaMap()
	{
		return partogramaMap;
	}

	/**
	 * @param partogramaMap The partogramaMap to set.
	 */
	public void setPartogramaMap(HashMap partogramaMap)
	{
		this.partogramaMap = partogramaMap;
	}
	
	/**
	 * @return Returns the partogramaMap.
	 */
	public Object getPartogramaMap(Object key)
	{
		return partogramaMap.get(key);
	}

	/**
	 * @param partogramaMap The partogramaMap to set.
	 */
	public void setPartogramaMap(Object key, Object value)
	{
		this.partogramaMap.put(key, value);
	}
	
	/**
	 * @return Returns the vigilanciaClinicaMap.
	 */
	public HashMap getVigilanciaClinicaMap()
	{
		return vigilanciaClinicaMap;
	}

	/**
	 * @param vigilanciaClinicaMap The vigilanciaClinicaMap to set.
	 */
	public void setVigilanciaClinicaMap(HashMap vigilanciaClinicaMap)
	{
		this.vigilanciaClinicaMap = vigilanciaClinicaMap;
	}
	
	/**
	 * @return Returns the vigilanciaClinicaMap.
	 */
	public Object getVigilanciaClinicaMap(Object key)
	{
		return vigilanciaClinicaMap.get(key);
	}

	/**
	 * @param vigilanciaClinicaMap The vigilanciaClinicaMap to set.
	 */
	public void setVigilanciaClinicaMap(Object key, Object value)
	{
		this.vigilanciaClinicaMap.put(key, value);
	}

	
	
	
	
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntFamiliares(HashMap mapa) {
		this.mapaAntFamiliares = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntFamiliares(String key) {
		return this.mapaAntFamiliares.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntFamiliares(String key, String dato) {
		this.mapaAntFamiliares.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntFamiliares() {
		return mapaAntFamiliares;
	}
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntMedicos(HashMap mapa) {
		this.mapaAntMedicos = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntMedicos(String key) {
		return this.mapaAntMedicos.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntMedicos(String key, String dato) {
		this.mapaAntMedicos.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntMedicos() {
		return mapaAntMedicos;
	}		
	
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntToxicos(HashMap mapa) {
		this.mapaAntToxicos = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntToxicos(String key) {
		return this.mapaAntToxicos.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntToxicos(String key, String dato) {
		this.mapaAntToxicos.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntToxicos() {
		return mapaAntToxicos;
	}		
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntGineco(HashMap mapa) {
		this.mapaAntGineco = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntGineco(String key) {
		return this.mapaAntGineco.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntGineco(String key, String dato) {
		this.mapaAntGineco.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntGineco() {
		return mapaAntGineco;
	}



	public ArrayList getHistoricos() {
		return historicos;
	}



	public void setHistoricos(ArrayList historicos) {
		this.historicos = historicos;
	}	
	
	/**
	 * Verificar si hay antecedentes 
	 * @return
	 */	
	public boolean getHayAntecedente() 
	{
		if (
				UtilidadCadena.vString(this.mapaAntFamiliares.get("hayAntecedentes")+"").equals("SI") ||
				UtilidadCadena.vString(this.mapaAntGineco.get("hayAntecedentes")+"").equals("SI") ||
				UtilidadCadena.vString(this.mapaAntMedicos.get("hayAntecedentes")+"").equals("SI") ||
				UtilidadCadena.vString(this.mapaAntToxicos.get("hayAntecedentes")+"").equals("SI") 
		   )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return Returns the controlPrenatal.
	 */
	public String getControlPrenatal()
	{
		return controlPrenatal;
	}

	/**
	 * @param controlPrenatal The controlPrenatal to set.
	 */
	public void setControlPrenatal(String controlPrenatal)
	{
		this.controlPrenatal = controlPrenatal;
	}

	/**
	 * @return Returns the partoEn.
	 */
	public String getPartoEn()
	{
		return partoEn;
	}

	/**
	 * @param partoEn The partoEn to set.
	 */
	public void setPartoEn(String partoEn)
	{
		this.partoEn = partoEn;
	}

	/**
	 * @return the idCuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta the idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}	

	
}