/*
 * Ene 06, 2008
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
 * @author Sebastián Gómez - Luis Felipe Perez Granda
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Registro Auditoria (GLOSAS)
 */
public class RegistroAuditoriaForm extends ValidatorForm 
{
	/**
	 * Atributo para el manejo de la direccion del flujo de trabajo de controlador
	 */
	private String estado;
	
	//******Atributos para la busqueda avanzada de facturas******************
	private String facturaInicial;
	private String facturaFinal;
	private String fechaElaboracionInicial;
	private String fechaElaboracionFinal;
	private int codigoConvenio;
	private int codigoViaIngreso;
	private String codigoTipoPaciente;
	private String vianIngresoTipoPaciente;
	private int estadoPaciente;
	private boolean estadoAuditoria;
	private String observacionesDetalle;
	private int indiceObservaciones;
	
	//Arreglos usados
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> arregloViasIngreso = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> arregloEstadosPaciente = new ArrayList<HashMap<String,Object>>();
	//*************************************************************************
	
	//*******Atributos para el manejo del listado de facturas*********************************
	/**
	 * Mapa que maneja el listado de las facturas
	 */
	private HashMap<String, Object> listadoFacturas = new HashMap<String, Object>();
	
	/**
	 * Campo que verifica si se seleccionaron todos
	 */
	private String selTodos;
	
	/**
	 * Campo que verifica si se seleccionaron todos de una pagina
	 */
	private String selTodosPagina;
	
	/**
	 * Campo que verifica si se realizó una busqueda
	 */
	private boolean realizoBusqueda;
	
	/**
	 * Campo para saber si la busqueda avanzada se realizó para facturas auditadas o sin auditar
	 */
	private boolean auditadas;
	
	/**
	 * Mapa donde se almacenan los criterios de busqueda con los cuales se encontró el listado de facturas
	 */
	private HashMap<String, Object> criterios = new HashMap<String, Object>();
	
	/**
	 * Atributo donde se almacena la posicion de la factura a la cual deseamos entrar al detale
	 */
	private int posicion;
	
	//Atributos para el manejo de la ordenacion/paginacion
	private String indice;
	private String ultimoIndice;
	
	/**
	 * Link Siguiente para el pager pagina principal
	 */
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
	
	//**********Atributos para el detalle de la glosa/factura**********************************
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
	
	private String codigoFac;
	
	//********************************************************************************************
	
	private float sumatoriaValorGlosa;
	
	/**
	 * Variable para modificar el valor del registro de la Busqueda
	 */
	private String valor;
	
	/**
	 * 
	 */
	private int offset;
	
	/**
	 * Método para limpiar los campos de la forma
	 */
	public void reset()
	{
		this.observacionesDetalle="";
		this.indiceObservaciones=ConstantesBD.codigoNuncaValido;
		this.estado  = "";
		this.facturaInicial = "";
		this.facturaFinal = "";
		this.fechaElaboracionInicial = "";
		this.fechaElaboracionFinal = "";
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.codigoViaIngreso = ConstantesBD.codigoNuncaValido;
		this.estadoPaciente = ConstantesBD.codigoNuncaValido;
		
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.arregloViasIngreso = new ArrayList<HashMap<String,Object>>();
		this.arregloEstadosPaciente = new ArrayList<HashMap<String,Object>>();
		
		this.listadoFacturas = new HashMap<String, Object>();
		this.realizoBusqueda = false;
		this.estadoAuditoria = false;
		this.selTodos = ConstantesBD.acronimoNo;
		this.selTodosPagina = ConstantesBD.acronimoNo;
		this.auditadas = false;
		this.criterios = new HashMap<String, Object>();
		this.posicion = ConstantesBD.codigoNuncaValido;
		
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.linkSiguienteSolicitudes = "";
		this.linkSiguienteAsocios = "";
		this.maxPageItems = 10;
		this.patronOrdenar="";
		
		this.glosa = new DtoGlosa();
		this.codigoAuditoria = "";
		
		this.conceptosDevolucion = new ArrayList<HashMap<String,Object>>();
		
		this.vianIngresoTipoPaciente="";
		this.codigoTipoPaciente="";
		
		this.offset = 0;
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

	public String getCodigoTipoPaciente() {
		return codigoTipoPaciente;
	}
	
	public void setCodigoTipoPaciente(String codigoTipoPaciente) {
		this.codigoTipoPaciente = codigoTipoPaciente;
	}
	
	public String getVianIngresoTipoPaciente() {
		return vianIngresoTipoPaciente;
	}

	public void setVianIngresoTipoPaciente(String vianIngresoTipoPaciente) {
		this.vianIngresoTipoPaciente = vianIngresoTipoPaciente;
	}

	public int getPosicion() {
		return posicion;
	}
	
	public void setPosicion(int posicion) {
		this.posicion = posicion;
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
	 * @return the fechaElaboracionInicial
	 */
	public String getFechaElaboracionInicial() {
		return fechaElaboracionInicial;
	}

	/**
	 * @param fechaElaboracionInicial the fechaElaboracionInicial to set
	 */
	public void setFechaElaboracionInicial(String fechaElaboracionInicial) {
		this.fechaElaboracionInicial = fechaElaboracionInicial;
	}

	/**
	 * @return the fechaElaboracionFinal
	 */
	public String getFechaElaboracionFinal() {
		return fechaElaboracionFinal;
	}

	/**
	 * @param fechaElaboracionFinal the fechaElaboracionFinal to set
	 */
	public void setFechaElaboracionFinal(String fechaElaboracionFinal) {
		this.fechaElaboracionFinal = fechaElaboracionFinal;
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
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the estadoPaciente
	 */
	public int getEstadoPaciente() {
		return estadoPaciente;
	}

	/**
	 * @param estadoPaciente the estadoPaciente to set
	 */
	public void setEstadoPaciente(int estadoPaciente) {
		this.estadoPaciente = estadoPaciente;
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
	 * @return the arregloViasIngreso
	 */
	public ArrayList<HashMap<String, Object>> getArregloViasIngreso() {
		return arregloViasIngreso;
	}

	/**
	 * @param arregloViasIngreso the arregloViasIngreso to set
	 */
	public void setArregloViasIngreso(
			ArrayList<HashMap<String, Object>> arregloViasIngreso) {
		this.arregloViasIngreso = arregloViasIngreso;
	}

	/**
	 * @return the arregloEstadosPaciente
	 */
	public ArrayList<HashMap<String, Object>> getArregloEstadosPaciente() {
		return arregloEstadosPaciente;
	}

	/**
	 * @param arregloEstadosPaciente the arregloEstadosPaciente to set
	 */
	public void setArregloEstadosPaciente(
			ArrayList<HashMap<String, Object>> arregloEstadosPaciente) {
		this.arregloEstadosPaciente = arregloEstadosPaciente;
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
		this.listadoFacturas.put(key, obj);
	}
	
	/**
	 * Método usado para retornar el tamaño de un mapa
	 * @return
	 */
	public int getNumListadoFacturas()
	{
		return Utilidades.convertirAEntero(this.listadoFacturas.get("numRegistros")+"", true);
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

	public boolean isRealizoBusqueda() {
		return realizoBusqueda;
	}

	public void setRealizoBusqueda(boolean realizoBusqueda) {
		this.realizoBusqueda = realizoBusqueda;
	}


	public void setEstadoAuditoria(boolean estadoAuditoria) {
		this.estadoAuditoria = estadoAuditoria;
	}

	public boolean isEstadoAuditoria() {
		return estadoAuditoria;
	}

	public String getSelTodos() {
		return selTodos;
	}

	public void setSelTodos(String selTodos) {
		this.selTodos = selTodos;
	}
	

	public boolean isAuditadas() {
		return auditadas;
	}
	
	public void setAuditadas(boolean auditadas) {
		this.auditadas = auditadas;
	}

	/**
	 * @return the criterios
	 */
	public HashMap<String, Object> getCriterios() {
		return criterios;
	}

	/**
	 * @param criterios the criterios to set
	 */
	public void setCriterios(HashMap<String, Object> criterios) {
		this.criterios = criterios;
	}
	
	/**
	 * Método para obtener el numero de elementos del arreglo de convenios
	 * @return
	 */
	public int getNumArregloConvenios()
	{
		return this.arregloConvenios.size();
	}
	
	public ArrayList<HashMap<String, Object>> getConceptosDevolucion() {
		return conceptosDevolucion;
	}

	public void setConceptosDevolucion(
			ArrayList<HashMap<String, Object>> conceptosDevolucion) {
		this.conceptosDevolucion = conceptosDevolucion;
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
	 * @return the selTodosPagina
	 */
	public String getSelTodosPagina() {
		return selTodosPagina;
	}

	/**
	 * @param selTodosPagina the selTodosPagina to set
	 */
	public void setSelTodosPagina(String selTodosPagina) {
		this.selTodosPagina = selTodosPagina;
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

	public String getObservacionesDetalle() {
		return observacionesDetalle;
	}

	public void setObservacionesDetalle(String observacionesDetalle) {
		this.observacionesDetalle = observacionesDetalle;
	}

	public int getIndiceObservaciones() {
		return indiceObservaciones;
	}

	public void setIndiceObservaciones(int indiceObservaciones) {
		this.indiceObservaciones = indiceObservaciones;
	}
	
}
