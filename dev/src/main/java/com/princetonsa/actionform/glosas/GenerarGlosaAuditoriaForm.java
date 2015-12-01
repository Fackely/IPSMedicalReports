/*
 * Enero 22, 2009
 */
package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dto.glosas.DtoGlosa;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Generar Glosas de Auditoria (GLOSAS)
 */
public class GenerarGlosaAuditoriaForm extends ValidatorForm 
{
	
	/**
	 * Atributo para el manejo de la direccion del flujo de trabajo de controlador
	 */
	private String estado;
	
	//******Atributos para la busqueda avanzada de facturas******************
	private String facturaInicial;
	private String facturaFinal;
	private String fechaAuditoriaInicial;
	private String fechaAuditoriaFinal;
	private int codigoConvenio;
	private int codigoContrato;
	private String numeroPreGlosa;
	
	//Arreglos usados
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> arregloContratos = new ArrayList<HashMap<String,Object>>();
	//*************************************************************************
	///*******Atributos para el manejo del listado de facturas*********************************
	/**
	 * Mapa que maneja el listado de las facturas
	 */
	private HashMap<String, Object> listadoFacturas = new HashMap<String, Object>();
	
	/**
	 * Campo que verifica si se realizó una busqueda
	 */
	private boolean realizoBusqueda;
	
	/**
	 * Atributo donde se almacena la posicion de la factura a la cual deseamos entrar al detale
	 */
	private int posicion;
	
	//Atributos para el manejo de la ordenacion/paginacion
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	/**
	 * Link Siguiente para el pager pagina Solicitudes
	 */
	private String linkSiguienteSolicitudes;
	
	/**
	 * Link Siguiente para el pager pagina Asocios
	 */
	private String linkSiguienteAsocios;
	private int maxPageItems; //para saber el maximo de registros por pagina
	private String patronOrdenar;
	//****************************************************************************************
	
	///**********Atributos para el detalle de la glosa/factura**********************************
	/**
	 * Atributo DTO que maneja toda la informacion de la glosa
	 */
	private DtoGlosa glosa;
	
	/**
	 * Atributos que almacena el consecutivo de la glosa
	 */
	private String codigoAuditoria;
	
	/**
	 * Arreglo que contiene los conceptos de devolucion parametrizados por institucion
	 */
	private ArrayList<HashMap<String, Object>> conceptosDevolucion = new ArrayList<HashMap<String,Object>>();

	/**
	 * Variable para guardar el valro de la glosa
	 */
	private float valorGlosa; 
	
	private String codigoFac;
	
	private float sumatoriaValorGlosa;
	
	/**
	 * Variable para modificar el valor del registro de la Busqueda
	 */
	private String valor;
	
	//********************************************************************************************
	
	/**
	 * Método para limpiar los datos de la forma
	 */

	public void reset()
	{
		//**************Atributos para la busqueda avanzada de facturas***************************
		this.facturaInicial = "";
		this.facturaFinal = "";
		this.fechaAuditoriaInicial = "";
		this.fechaAuditoriaFinal = "";
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.codigoContrato = ConstantesBD.codigoNuncaValido;
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.arregloContratos = new ArrayList<HashMap<String,Object>>();
		this.numeroPreGlosa = "";
		//****************************************************************************************
		//*******Atributos para el manejo del listado de facturas*********************************
		this.listadoFacturas = new HashMap<String, Object>();
		this.realizoBusqueda = false;
		this.posicion = ConstantesBD.codigoNuncaValido;
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.linkSiguienteSolicitudes = "";
		this.linkSiguienteAsocios = "";
		this.maxPageItems = 10;
		this.patronOrdenar="";
		//*****************************************************************************************
		//**********Atributos para el detalle de la glosa/factura**********************************
		this.glosa = new DtoGlosa();
		this.codigoAuditoria = "";
		this.conceptosDevolucion = new ArrayList<HashMap<String,Object>>();
		//*****************************************************************************************
		this.valorGlosa=0;
		this.codigoFac="";
		this.sumatoriaValorGlosa=0;
		this.valor="";
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

		
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public float getSumatoriaValorGlosa() {
		return sumatoriaValorGlosa;
	}

	public void setSumatoriaValorGlosa(float sumatoriaValorGlosa) {
		this.sumatoriaValorGlosa = sumatoriaValorGlosa;
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
	 * @return the facturaInicial
	 */
	public String getFacturaInicial() {
		return facturaInicial;
	}

	/**
	 * @param facturaInicial the facturaInicial to set
	 */
	public void setFacturaInicial(String facturaInicial) {
		this.facturaInicial = facturaInicial;
	}

	/**
	 * @return the facturaFinal
	 */
	public String getFacturaFinal() {
		return facturaFinal;
	}

	/**
	 * @param facturaFinal the facturaFinal to set
	 */
	public void setFacturaFinal(String facturaFinal) {
		this.facturaFinal = facturaFinal;
	}

	/**
	 * @return the fechaAuditoriaInicial
	 */
	public String getFechaAuditoriaInicial() {
		return fechaAuditoriaInicial;
	}

	/**
	 * @param fechaAuditoriaInicial the fechaAuditoriaInicial to set
	 */
	public void setFechaAuditoriaInicial(String fechaAuditoriaInicial) {
		this.fechaAuditoriaInicial = fechaAuditoriaInicial;
	}

	/**
	 * @return the fechaAuditoriaFinal
	 */
	public String getFechaAuditoriaFinal() {
		return fechaAuditoriaFinal;
	}

	/**
	 * @param fechaAuditoriaFinal the fechaAuditoriaFinal to set
	 */
	public void setFechaAuditoriaFinal(String fechaAuditoriaFinal) {
		this.fechaAuditoriaFinal = fechaAuditoriaFinal;
	}

	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return the codigoContrato
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	/**
	 * @return the numeroPreGlosa
	 */
	public String getNumeroPreGlosa() {
		return numeroPreGlosa;
	}

	/**
	 * @param numeroPreGlosa the numeroPreGlosa to set
	 */
	public void setNumeroPreGlosa(String numeroPreGlosa) {
		this.numeroPreGlosa = numeroPreGlosa;
	}

	/**
	 * @return the arregloConvenios
	 */
	public ArrayList<HashMap<String, Object>> getArregloConvenios() {
		return arregloConvenios;
	}

	/**
	 * @param arregloConvenios the arregloConvenios to set
	 */
	public void setArregloConvenios(
			ArrayList<HashMap<String, Object>> arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
	}

	/**
	 * @return the arregloContratos
	 */
	public ArrayList<HashMap<String, Object>> getArregloContratos() {
		return arregloContratos;
	}

	/**
	 * @param arregloContratos the arregloContratos to set
	 */
	public void setArregloContratos(
			ArrayList<HashMap<String, Object>> arregloContratos) {
		this.arregloContratos = arregloContratos;
	}

	/**
	 * @return the listadoFacturas
	 */
	public HashMap<String, Object> getListadoFacturas() {
		return listadoFacturas;
	}

	/**
	 * @param listadoFacturas the listadoFacturas to set
	 */
	public void setListadoFacturas(HashMap<String, Object> listadoFacturas) {
		this.listadoFacturas = listadoFacturas;
	}
	
	/**
	 * @return the listadoFacturas
	 */
	public Object getListadoFacturas(String key) {
		return listadoFacturas.get(key);
	}

	/**
	 * @param listadoFacturas the listadoFacturas to set
	 */
	public void setListadoFacturas(String key,Object obj) {
		this.listadoFacturas.put(key,obj);
	}

	/**
	 * @return the realizoBusqueda
	 */
	public boolean isRealizoBusqueda() {
		return realizoBusqueda;
	}

	/**
	 * @param realizoBusqueda the realizoBusqueda to set
	 */
	public void setRealizoBusqueda(boolean realizoBusqueda) {
		this.realizoBusqueda = realizoBusqueda;
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
	 * @return the glosa
	 */
	public DtoGlosa getGlosa() {
		return glosa;
	}

	/**
	 * @param glosa the glosa to set
	 */
	public void setGlosa(DtoGlosa glosa) {
		this.glosa = glosa;
	}

	/**
	 * @return the codigoAuditoria
	 */
	public String getCodigoAuditoria() {
		return codigoAuditoria;
	}

	/**
	 * @param codigoAuditoria the codigoAuditoria to set
	 */
	public void setCodigoAuditoria(String codigoAuditoria) {
		this.codigoAuditoria = codigoAuditoria;
	}

	/**
	 * @return the conceptosDevolucion
	 */
	public ArrayList<HashMap<String, Object>> getConceptosDevolucion() {
		return conceptosDevolucion;
	}

	/**
	 * @param conceptosDevolucion the conceptosDevolucion to set
	 */
	public void setConceptosDevolucion(
			ArrayList<HashMap<String, Object>> conceptosDevolucion) {
		this.conceptosDevolucion = conceptosDevolucion;
	}
	
	/**
	 * Método para obtener el numero de elementos del arreglo de convenios
	 * @return
	 */
	public int getNumArregloConvenios()
	{
		return this.arregloConvenios.size();
	}
	
	/**
	 * Método para obtener el numero de elementos del arreglo de contratos
	 * @return
	 */
	public int getNumArregloContratos()
	{
		return this.arregloContratos.size();
	}
	
	/**
	 * Método para obtener el numero de registros del listado de facturas
	 * @return
	 */
	public int getNumListadoFacturas()
	{
		return Utilidades.convertirAEntero(this.getListadoFacturas("numRegistros")+"", true);
	}

	public float getValorGlosa() {
		return valorGlosa;
	}

	public void setValorGlosa(float valorGlosa) {
		this.valorGlosa = valorGlosa;
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

	public String getCodigoFac() {
		return codigoFac;
	}

	public void setCodigoFac(String codigoFac) {
		this.codigoFac = codigoFac;
	}

	/**
	 * @return the linkSiguienteSolicitudes
	 */
	public String getLinkSiguienteSolicitudes() {
		return linkSiguienteSolicitudes;
	}

	/**
	 * @param linkSiguienteSolicitudes the linkSiguienteSolicitudes to set
	 */
	public void setLinkSiguienteSolicitudes(String linkSiguienteSolicitudes) {
		this.linkSiguienteSolicitudes = linkSiguienteSolicitudes;
	}

	/**
	 * @return the linkSiguienteAsocios
	 */
	public String getLinkSiguienteAsocios() {
		return linkSiguienteAsocios;
	}

	/**
	 * @param linkSiguienteAsocios the linkSiguienteAsocios to set
	 */
	public void setLinkSiguienteAsocios(String linkSiguienteAsocios) {
		this.linkSiguienteAsocios = linkSiguienteAsocios;
	}
	
}
