/*
 * Feb 06, 2008
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ElementoApResource;
import util.Utilidades;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Liquidacion Servicios
 */
public class LiquidacionServiciosForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	//*****************ATRIBUTOS PARA LA PARTE DEL LISTADO DE ORDENES***************************************************
	private HashMap<String,Object> listadoOrdenes = new HashMap<String, Object>();
	private String indice, ultimoIndice;
	private int index;
	//********************************************************************************************************************
	//*******************ATRIBUTOS PARA EL DETALLE DE LA ORDEN************************************************************
	private String numeroSolicitud, numeroPeticion, indQx, codigoEspecialidad, nombreResponsable;
	private boolean modificarServicios;
	private boolean modificarLiquidacion;
	private boolean modificarDatosOcupacionSala;
	private boolean modificarIndicativoCobrable;
	private int tarifarioOficial;
	private int codigoSexo; //codigo del sexo del paciente para la busqueda de servicios
	private HashMap<String, Object> encabezadoSolicitud = new HashMap<String, Object>();
	private HashMap<String, Object> cirugiasSolicitud = new HashMap<String, Object>();
	private HashMap<String, Object> datosActoQx = new HashMap<String, Object>();
	private HashMap<String, Object> otrosProfesionales = new HashMap<String, Object>();
	private ArrayList<HashMap<String, Object>> materialesEspeciales = new ArrayList<HashMap<String,Object>>();
	
	//Atributos para los combos
	private ArrayList<HashMap<String, Object>> tiposSala = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> salas = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> esquemasTarifarios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> especialidades = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> asocios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> profesionales = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> asociosServicio = new ArrayList<HashMap<String,Object>>();
	
	
	//Atributos para los mensajes
	private ArrayList<ElementoApResource> mensajesCirugias = new ArrayList<ElementoApResource>();
	
	//Atributos para los filtro del detalle del servicio
	private String codigoProfesional;
	
	//Atributos para el manejo de ajax
	private String indexAjax;
	//***************************************************************************************************************
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		
		//Atributos para la parte del listado de ordenes
		this.listadoOrdenes = new HashMap<String, Object>();
		this.indice = "";
		this.ultimoIndice = "";
		this.index = ConstantesBD.codigoNuncaValido;
		
		this.resetOrden();
	}
	
	/**
	 * Método que limpia los datos de la orden
	 *
	 */
	public void resetOrden()
	{
		///Atributos para el detalle de la orden
		this.numeroSolicitud = "";
		this.numeroPeticion = "";
		this.indQx = "";
		this.modificarServicios = false;
		this.modificarLiquidacion = false;
		this.modificarDatosOcupacionSala = false;
		this.modificarIndicativoCobrable = false;
		this.codigoEspecialidad = "";
		this.nombreResponsable = ""; //nombre convenio responsable de la liquidacion
		this.tarifarioOficial = ConstantesBD.codigoNuncaValido;
		this.codigoSexo = ConstantesBD.codigoNuncaValido;
		this.encabezadoSolicitud = new HashMap<String, Object>();
		this.cirugiasSolicitud = new HashMap<String, Object>();
		this.datosActoQx = new HashMap<String, Object>();
		this.otrosProfesionales = new HashMap<String, Object>();
		this.materialesEspeciales = new ArrayList<HashMap<String,Object>>();
		
		//Atributos para los combos
		this.tiposSala = new ArrayList<HashMap<String,Object>>();
		this.salas = new ArrayList<HashMap<String,Object>>();
		this.esquemasTarifarios = new ArrayList<HashMap<String,Object>>();
		this.especialidades = new ArrayList<HashMap<String,Object>>();
		this.asocios = new ArrayList<HashMap<String,Object>>();
		this.profesionales = new ArrayList<HashMap<String,Object>>();
		this.asociosServicio = new ArrayList<HashMap<String,Object>>();
		
		//Atributos para el filtro del detalle del servicio
		this.codigoProfesional = "";
		
		//Atributos para el manejo de ajax
		this.indexAjax = "";
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
		return errores;
	}

	/**
	 * @return the listadoOrdenes
	 */
	public HashMap<String, Object> getListadoOrdenes() {
		return listadoOrdenes;
	}

	/**
	 * @param listadoOrdenes the listadoOrdenes to set
	 */
	public void setListadoOrdenes(HashMap<String, Object> listadoOrdenes) {
		this.listadoOrdenes = listadoOrdenes;
	}
	
	/**
	 * @return the listadoOrdenes
	 */
	public Object getListadoOrdenes(String key) {
		return listadoOrdenes.get(key);
	}

	/**
	 * @param listadoOrdenes the listadoOrdenes to set
	 */
	public void setListadoOrdenes(String key, Object obj) {
		this.listadoOrdenes.put(key,obj);
	}
	
	/**
	 * Método para obtener el numero de registros del listado de ordenes consultada   
	 * @return
	 */
	public int getNumOrdenes()
	{
		return Utilidades.convertirAEntero(this.getListadoOrdenes("numRegistros")+"", true);
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
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
	 * @return the cirugiasSolicitud
	 */
	public HashMap<String, Object> getCirugiasSolicitud() {
		return cirugiasSolicitud;
	}

	/**
	 * @param cirugiasSolicitud the cirugiasSolicitud to set
	 */
	public void setCirugiasSolicitud(HashMap<String, Object> cirugiasSolicitud) {
		this.cirugiasSolicitud = cirugiasSolicitud;
	}
	
	/**
	 * @return the cirugiasSolicitud
	 */
	public Object getCirugiasSolicitud(String key) {
		return cirugiasSolicitud.get(key);
	}
	


	/**
	 * @param cirugiasSolicitud the cirugiasSolicitud to set
	 */
	public void setCirugiasSolicitud(String key,Object obj) {
		this.cirugiasSolicitud.put(key,obj);
	}
	
	/**
	 * Método que retorna el número de cirugias de la orden
	 * @return
	 */
	public int getNumCirugias()
	{
		return Utilidades.convertirAEntero(this.getCirugiasSolicitud("numRegistros")+"", true);
	}

	/**
	 * @return the encabezadoSolicitud
	 */
	public HashMap<String, Object> getEncabezadoSolicitud() {
		return encabezadoSolicitud;
	}

	/**
	 * @param encabezadoSolicitud the encabezadoSolicitud to set
	 */
	public void setEncabezadoSolicitud(HashMap<String, Object> encabezadoSolicitud) {
		this.encabezadoSolicitud = encabezadoSolicitud;
	}
	
	/**
	 * @return the encabezadoSolicitud
	 */
	public Object getEncabezadoSolicitud(String key) {
		return encabezadoSolicitud.get(key);
	}

	/**
	 * @param encabezadoSolicitud the encabezadoSolicitud to set
	 */
	public void setEncabezadoSolicitud(String key, Object obj) {
		this.encabezadoSolicitud.put(key,obj);
	}
	
	

	
	

	/**
	 * @return the modificarDatosOcupacionSala
	 */
	public boolean isModificarDatosOcupacionSala() {
		return modificarDatosOcupacionSala;
	}

	/**
	 * @param modificarDatosOcupacionSala the modificarDatosOcupacionSala to set
	 */
	public void setModificarDatosOcupacionSala(boolean modificarDatosOcupacionSala) {
		this.modificarDatosOcupacionSala = modificarDatosOcupacionSala;
	}

	/**
	 * @return the modificarServicios
	 */
	public boolean isModificarServicios() {
		return modificarServicios;
	}

	/**
	 * @param modificarServicios the modificarServicios to set
	 */
	public void setModificarServicios(boolean modificarServicios) {
		this.modificarServicios = modificarServicios;
	}

	/**
	 * @return the numeroPeticion
	 */
	public String getNumeroPeticion() {
		return numeroPeticion;
	}

	/**
	 * @param numeroPeticion the numeroPeticion to set
	 */
	public void setNumeroPeticion(String numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the indQx
	 */
	public String getIndQx() {
		return indQx;
	}

	/**
	 * @param indQx the indQx to set
	 */
	public void setIndQx(String indQx) {
		this.indQx = indQx;
	}

	/**
	 * @return the otrosProfesionales
	 */
	public HashMap<String, Object> getOtrosProfesionales() {
		return otrosProfesionales;
	}
	
	/**
	 * @return the otrosProfesionales
	 */
	public int getNumOtrosProfesionales() {
		return Utilidades.convertirAEntero(this.getOtrosProfesionales("numRegistros")+"",true);
	}
	
	/**
	 * Método para actualizar el número de otros profesinales
	 * @param numOtrosProfesionales
	 */
	public void setNumOtrosProfesionales(int numOtrosProfesionales)
	{
		this.setOtrosProfesionales("numRegistros", numOtrosProfesionales);
	}

	/**
	 * @param otrosProfesionales the otrosProfesionales to set
	 */
	public void setOtrosProfesionales(HashMap<String, Object> otrosProfesionales) {
		this.otrosProfesionales = otrosProfesionales;
	}
	
	/**
	 * @return the otrosProfesionales
	 */
	public Object getOtrosProfesionales(String key) {
		return otrosProfesionales.get(key);
	}

	/**
	 * @param otrosProfesionales the otrosProfesionales to set
	 */
	public void setOtrosProfesionales(String key,Object obj) {
		this.otrosProfesionales.put(key, obj);
	}

	/**
	 * @return the datosActoQx
	 */
	public HashMap<String, Object> getDatosActoQx() {
		return datosActoQx;
	}

	/**
	 * @param datosActoQx the datosActoQx to set
	 */
	public void setDatosActoQx(HashMap<String, Object> datosActoQx) {
		this.datosActoQx = datosActoQx;
	}
	
	/**
	 * @return the datosActoQx
	 */
	public Object getDatosActoQx(String key) {
		return datosActoQx.get(key);
	}

	/**
	 * @param datosActoQx the datosActoQx to set
	 */
	public void setDatosActoQx(String key, Object obj) {
		this.datosActoQx.put(key,obj);
	}

	/**
	 * @return the tiposSala
	 */
	public ArrayList<HashMap<String, Object>> getTiposSala() {
		return tiposSala;
	}

	/**
	 * @param tiposSala the tiposSala to set
	 */
	public void setTiposSala(ArrayList<HashMap<String, Object>> tiposSala) {
		this.tiposSala = tiposSala;
	}

	/**
	 * @return the salas
	 */
	public ArrayList<HashMap<String, Object>> getSalas() {
		return salas;
	}

	/**
	 * @param salas the salas to set
	 */
	public void setSalas(ArrayList<HashMap<String, Object>> salas) {
		this.salas = salas;
	}

	/**
	 * @return the tarifarioOficial
	 */
	public int getTarifarioOficial() {
		return tarifarioOficial;
	}

	/**
	 * @param tarifarioOficial the tarifarioOficial to set
	 */
	public void setTarifarioOficial(int tarifarioOficial) {
		this.tarifarioOficial = tarifarioOficial;
	}

	/**
	 * @return the esquemasTarifarios
	 */
	public ArrayList<HashMap<String, Object>> getEsquemasTarifarios() {
		return esquemasTarifarios;
	}

	/**
	 * @param esquemasTarifarios the esquemasTarifarios to set
	 */
	public void setEsquemasTarifarios(
			ArrayList<HashMap<String, Object>> esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	/**
	 * @return the especialidades
	 */
	public ArrayList<HashMap<String, Object>> getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList<HashMap<String, Object>> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * @return the mensajesCirugias
	 */
	public ArrayList<ElementoApResource> getMensajesCirugias() {
		return mensajesCirugias;
	}

	/**
	 * @param mensajesCirugias the mensajesCirugias to set
	 */
	public void setMensajesCirugias(ArrayList<ElementoApResource> mensajesCirugias) {
		this.mensajesCirugias = mensajesCirugias;
	}
	
	/**
	 * Método implementado para retornar el tamaño del vector de los mensajes de las cirugías
	 * @return
	 */
	public int getSizeMensajesCirugia()
	{
		return this.mensajesCirugias.size();
	}

	

	/**
	 * @return the codigoEspecialidad
	 */
	public String getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(String codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * @return the modificarIndicativoCobrable
	 */
	public boolean isModificarIndicativoCobrable() {
		return modificarIndicativoCobrable;
	}

	/**
	 * @param modificarIndicativoCobrable the modificarIndicativoCobrable to set
	 */
	public void setModificarIndicativoCobrable(boolean modificarIndicativoCobrable) {
		this.modificarIndicativoCobrable = modificarIndicativoCobrable;
	}

	/**
	 * @return the codigoProfesional
	 */
	public String getCodigoProfesional() {
		return codigoProfesional;
	}

	/**
	 * @param codigoProfesional the codigoProfesional to set
	 */
	public void setCodigoProfesional(String codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}

	/**
	 * @return the modificarLiquidacion
	 */
	public boolean isModificarLiquidacion() {
		return modificarLiquidacion;
	}

	/**
	 * @param modificarLiquidacion the modificarLiquidacion to set
	 */
	public void setModificarLiquidacion(boolean modificarLiquidacion) {
		this.modificarLiquidacion = modificarLiquidacion;
	}

	/**
	 * @return the codigoSexo
	 */
	public int getCodigoSexo() {
		return codigoSexo;
	}

	/**
	 * @param codigoSexo the codigoSexo to set
	 */
	public void setCodigoSexo(int codigoSexo) {
		this.codigoSexo = codigoSexo;
	}

	/**
	 * @return the materialesEspeciales
	 */
	public ArrayList<HashMap<String, Object>> getMaterialesEspeciales() {
		return materialesEspeciales;
	}

	/**
	 * @param materialesEspeciales the materialesEspeciales to set
	 */
	public void setMaterialesEspeciales(
			ArrayList<HashMap<String, Object>> materialesEspeciales) {
		this.materialesEspeciales = materialesEspeciales;
	}
	
	
	/**
	 * Método para obtener el tamaño de los materiales especiales
	 * @return
	 */
	public int getNumMaterialesEspeciales()
	{
		return this.materialesEspeciales.size();
	}

	/**
	 * @return the nombreResponsable
	 */
	public String getNombreResponsable() {
		return nombreResponsable;
	}

	/**
	 * @param nombreResponsable the nombreResponsable to set
	 */
	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	/**
	 * @return the asocios
	 */
	public ArrayList<HashMap<String, Object>> getAsocios() {
		return asocios;
	}

	/**
	 * @param asocios the asocios to set
	 */
	public void setAsocios(ArrayList<HashMap<String, Object>> asocios) {
		this.asocios = asocios;
	}

	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}

	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}

	/**
	 * @return the asociosServicio
	 */
	public ArrayList<HashMap<String, Object>> getAsociosServicio() {
		return asociosServicio;
	}

	/**
	 * @param asociosServicio the asociosServicio to set
	 */
	public void setAsociosServicio(
			ArrayList<HashMap<String, Object>> asociosServicio) {
		this.asociosServicio = asociosServicio;
	}

	/**
	 * @return the indexAjax
	 */
	public String getIndexAjax() {
		return indexAjax;
	}

	/**
	 * @param indexAjax the indexAjax to set
	 */
	public void setIndexAjax(String indexAjax) {
		this.indexAjax = indexAjax;
	}



}
