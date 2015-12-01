package com.princetonsa.actionform.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;

import util.ObjetoReferencia;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;


public class ConsultarActivarCamasReservadasForm extends ValidatorForm
{
	
	/*---------------------------------------------------------------
	 * 							ATRIBUTOS LOGER 
	 ---------------------------------------------------------------*/
	
	/**
	 * Para manjar los logger de la clase ConsultarActivarCamasReservadasForm
	 */
	Logger logger = Logger.getLogger(ConsultarActivarCamasReservadasForm.class);
	
	/*---------------------------------------------------------------
	 * 						   FIN ATRIBUTOS LOGER 
	 ---------------------------------------------------------------*/
	
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	
	/*-----------------------------------------------------------------
	 *		ATRIBUTOS DE CONSULTAR ACTIVAR CAMAS RESERVADAS 
	 -----------------------------------------------------------------*/
	/**
	 * HashMap ConsultarActivarCamasReservadasMAP;
	 * que almacena la informacion de la consulta.
	 */
	private HashMap consultarActivarCamasReservadasMAP;
	
	/**
	 * String estado; encargado de administrar las 
	 * acciones dentro del Action.
	 */
	private String estado;
	
	/**
	 * String indexConsultarActivarCamasReservadasMAP; 
	 * es el encargado almacenar el indice el HasgMap 
	 * a ir.
	 */
	private String indexConsultarActivarCamasReservadasMAP;
	
	/**
	 * boleano permisoModificar; almacena la informacion
	 * acerda de si el usuario puede o no modificar
	 * el estado de la reserva de la cama.
	 */
	private boolean permisoModificar;
	
	/**
	 * String que almacena el id de la reserva que va a ser modificada
	 */
	private String reservaCamaId;
	
	/**
	 * HashMap que almacena los datos de la reserva
	 * que va a ser modificada.
	 **/
	private HashMap datosReservaMap;
	
	/**
	 * HashMap que almacena los datos de la
	 * cama que se encuentra reservada.
	 */
	private HashMap datosCamaMap;
	
	/**
	 * String que almacena el motivo por que se cansela la reserva.
	 */
	private String motivoCanselacion;
	
	/**
	 * indica si se realizo bien la operacion
	 */
	private boolean operacionTrue;
	
	/**
	 * indica de conde fue llamada la funcionalidad
	 */
	private String origenllamada;
	
	/**
	 * atributo que indica si se debe cocultar el 
	 * encabezado o no
	 */
	private boolean ocultarEncabezado;
	
	/*-----------------------------------------------------------------
	 *	  FIN ATRIBUTOS DE CONSULTAR ACTIVAR CAMAS RESERVADAS 
	 -----------------------------------------------------------------*/
	
	
	/*--------------------------------------------------------------------
	 *	  		ATRIBUTOS PARA LA CONSULTA DE CAMAS RESERVADAS 
	 -----------------------------------------------------------------*/
	/**
	 * ArrayList de HashMap de Centros de Atencion, utilizado en la 
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencionMap = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * ArrayList de HashMap de Censtros de Costo, Utilizado en la
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String,Object>> centroCostoMap = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * ArrayList de Hasmap de pisos, Utilizado en la
	 * Busqueda en uno de sus criterios.
	 */
	private ArrayList<HashMap<String, Object>> pisosMap = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * HashMap donde se almacenan los diferentes criterios de busqueda
	 * para hacer la consulta.
	 */
	private HashMap criteriosBusqueda;
	
	private ArrayList<HashMap<String, Object>> tiposIdMap = new ArrayList<HashMap<String,Object>>();
	

	/*--------------------------------------------------------------------
	 *	  	   FIN ATRIBUTOS PARA LA CONSULTA DE CAMAS RESERVADAS 
	 -----------------------------------------------------------------*/
	
	
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	public ArrayList<HashMap<String, Object>> getCentroCostoMap() {
		return centroCostoMap;
	}






	public void setCentroCostoMap(ArrayList<HashMap<String, Object>> centroCostoMap) {
		this.centroCostoMap = centroCostoMap;
	}






	public ArrayList<HashMap<String, Object>> getCentrosAtencionMap() {
		return centrosAtencionMap;
	}






	public void setCentrosAtencionMap(
			ArrayList<HashMap<String, Object>> centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}






	public HashMap getConsultarActivarCamasReservadasMAP() {
		return consultarActivarCamasReservadasMAP;
	}






	public void setConsultarActivarCamasReservadasMAP(
			HashMap consultarActivarCamasReservadasMAP) {
		this.consultarActivarCamasReservadasMAP = consultarActivarCamasReservadasMAP;
	}






	public String getEstado() {
		return estado;
	}






	public void setEstado(String estado) {
		this.estado = estado;
	}






	public String getIndexConsultarActivarCamasReservadasMAP() {
		return this.indexConsultarActivarCamasReservadasMAP;
	}






	public void setIndexConsultarActivarCamasReservadasMAP(
			String indexConsultarActivarCamasReservadasMAP) {
		this.indexConsultarActivarCamasReservadasMAP = indexConsultarActivarCamasReservadasMAP;
	}






	public String getLinkSiguiente() {
		return linkSiguiente;
	}






	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}






	public String getPatronOrdenar() {
		return patronOrdenar;
	}






	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}






	public boolean isPermisoModificar() {
		return permisoModificar;
	}






	public void setPermisoModificar(boolean permisoModificar) {
		this.permisoModificar = permisoModificar;
	}






	public ArrayList<HashMap<String, Object>> getPisosMap() {
		return pisosMap;
	}






	public void setPisosMap(ArrayList<HashMap<String, Object>> pisosMap) {
		this.pisosMap = pisosMap;
	}






	public String getUltimoPatron() {
		return ultimoPatron;
	}






	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	
	public HashMap getCriteriosBusqueda() {
		return criteriosBusqueda;
	}






	public void setCriteriosBusqueda(HashMap criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}
	
	
	public Object getCriteriosBusqueda(String key)
	{
		return this.criteriosBusqueda.get(key);
	}
	
	public void setCriteriosBusqueda (String key,Object value)
	{
		//logger.info("sillego ahasta aqui el key es "+key+" y el object es"+value.toString());
		this.criteriosBusqueda.put(key, value);
	}
	
	

	public ArrayList<HashMap<String, Object>> getTiposIdMap() {
		return tiposIdMap;
	}






	public void setTiposIdMap(ArrayList<HashMap<String, Object>> tiposIdMap) {
		this.tiposIdMap = tiposIdMap;
	}
	
	
	public Object getConsultarActivarCamasReservadasMAP (String key)
	{
		return this.consultarActivarCamasReservadasMAP.get(key);
	}
	
	
	public void setConsultarActivarCamasReservadasMAP (String key , Object value)
	{
		this.consultarActivarCamasReservadasMAP.put(key, value);
	}
	
	





	public String getReservaCamaId() {
		return reservaCamaId;
	}






	public void setReservaCamaId(String reservaCamaId) {
		this.reservaCamaId = reservaCamaId;
	}






	public HashMap getDatosCamaMap() {
		return datosCamaMap;
	}






	public void setDatosCamaMap(HashMap datosCamaMap) {
		this.datosCamaMap = datosCamaMap;
	}






	public HashMap getDatosReservaMap() {
		return datosReservaMap;
	}






	public void setDatosReservaMap(HashMap datosReservaMap) {
		this.datosReservaMap = datosReservaMap;
	}
	
	
	public Object getDatosReservaMap(String key)
	{
		return this.datosReservaMap.get(key);
	}
	
	
	public void setDatosReservaMap(String key,Object value) 
	{
		this.datosReservaMap.put(key, value);
	}
	
	public Object getDatosCamaMap(String key)
	{
		return this.datosCamaMap.get(key);
	}
	
	public void setDatosCamaMap (String key,Object value)
	{
		this.datosCamaMap.put(key, value);
	}
	
	
	public String getMotivoCanselacion() {
		return motivoCanselacion;
	}






	public void setMotivoCanselacion(String motivoCanselacion) {
		this.motivoCanselacion = motivoCanselacion;
	}
	
	
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}






	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	
	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 *  METODO PARA EL MANEJO DE CONSULTAR ACTIVAR CAMAS RESERVADAS
	 --------------------------------------------------------------------*/
	
	
	/**
	 * Metodo encargado de hacer las validaciones y retornarlas al jsp.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errors = new ActionErrors();
		errors = super.validate(mapping, request);
		
		
			
		return errors;
	}
	
	
	public void resetPager ()
	{
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
	}


	public void initBusqueda ()
	{
		this.criteriosBusqueda = new HashMap (); 
		this.reservaCamaId = "";
		this.permisoModificar = false;
		this.origenllamada ="";
		
	}
	
	public void initRespuesta ()
	{
		this.consultarActivarCamasReservadasMAP = new HashMap();
		this.setConsultarActivarCamasReservadasMAP("numRegistros", 0);
	}
	/**
	 * 
	 * @param connection
	 * @param institucion
	 */
	public void initBusquedaCensoCamas (Connection connection,int institucion, int centroAtencion)
	{
		HashMap parametros = new HashMap ();
	
		this.initBusqueda();
		//se cargan todos los centros de atencion de la institucion.
		this.setCentrosAtencionMap(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, institucion,""));
		//se cargan todos los centros de costo de la institucion.
		this.setCentroCostoMap(UtilidadesManejoPaciente.obtenerCentrosCosto(connection, institucion, "", true, centroAtencion));
		//parametros para la carga de pisos
		parametros.put("institucion", institucion);
		parametros.put("centroatencion", centroAtencion);
		this.setPisosMap(UtilidadesManejoPaciente.obtenerPisos(connection, parametros));
		//cargamos los tipos identificacion existentes
		this.setTiposIdMap(Utilidades.obtenerTiposIdentificacion(connection, "", institucion));
	
	}


	public void initcanse ()
	{
		this.motivoCanselacion = "";
		this.operacionTrue = false;
		this.origenllamada ="";
		this.ocultarEncabezado=false;
		
	}






	public String getOrigenllamada() {
		return origenllamada;
	}






	public void setOrigenllamada(String origenllamada) {
		this.origenllamada = origenllamada;
	}






	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}






	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}






























	
	
	/*-------------------------------------------------------------------
	 *  FIN METODO PARA EL MANEJO DE CONSULTAR ACTIVAR CAMAS RESERVADAS
	 --------------------------------------------------------------------*/
	
}