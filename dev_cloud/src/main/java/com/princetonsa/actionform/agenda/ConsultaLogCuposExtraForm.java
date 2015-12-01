/*
 * @(#)ConsultaLogCuposExtra.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.agenda;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import util.UtilidadFecha;


/**
 * Forma para manejo presentaci�n de la funcionalidad 
 * Consulta de LOG de Cupos Extras 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 09 /May/ 2006
 */
public class ConsultaLogCuposExtraForm extends ActionForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaLogCuposExtras;
	
	/**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
     * String de la fecha inicial para la busqueda de Facturas
     */
    private String fechaInicial;
    
    /**
     * String de la fehca final para la busqueda de Facturas
     */
    private String fechaFinal;
    
    /**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
     /**
  	 * Codigo de la unida de consulta a buscar
  	 */
  	private int codigoUnidadConsulta;
  	
  	/**
 	 * codigo del medico a buscar
 	 */
 	private int codigoMedico;
 	
 	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
 	
	
	/**
 	 * variable para almacenar el centro de antencion  
 	 */
 	private int centroAtencion;
 	
 	/**
 	 * Variable para almacenar el Centro de Atencion.
 	 */
 	private String nombreCentroAtencion;
 	
 	/**
 	 * Cadena de codigos de centros de atencion separados por comas
 	 */
 	private String centrosAtencion;
 	
 	/**
 	 * Cadena de codigos de unidades de agenda separadas por comas
 	 */
 	private String unidadesAgenda;
 	
 	/**
	 * Listado de centros de atencion y unidades de agenda autorizados para el usuario
	 */
	private HashMap unidadAgendaMap;
	
	/**
	 * Centros de atencion validos para el usuario
	 */
	private HashMap centrosAtencionAutorizados;
	
	/**
	 * Unidades de agenda validas para el usuario
	 */
	private HashMap unidadesAgendaAutorizadas;
 	
 	/**
 	 * Reset generla de la Forma
 	 */
	public void reset ()
	{
		this.mapaLogCuposExtras = new HashMap ();
		this.estado = "";
		this.linkSiguiente = "";
		this.fechaFinal = "";
		this.fechaInicial = "";
	 	this.codigoUnidadConsulta = 0;
	 	this.codigoMedico = 0;
	 	this.patronOrdenar = "";
	 	this.ultimoPatron = "";
		this.centroAtencion = 0;
	 	this.nombreCentroAtencion = "";
	 	this.centrosAtencion="";
	 	this.unidadesAgenda="";
	 	this.unidadAgendaMap = new HashMap();
	 	this.unidadAgendaMap.put("numRegistros", "0");
	 	this.centrosAtencionAutorizados=new HashMap();
		this.centrosAtencionAutorizados.put("numRegistros", "0");
		this.unidadesAgendaAutorizadas=new HashMap();
		this.unidadesAgendaAutorizadas.put("numRegistros", "0");
	}
	
	/**
	 * Reset �nico para los mapas
	 */
	public void resetMapa()
	{
		this.mapaLogCuposExtras = new HashMap ();
	}
 	
	
	
	
	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron=ultimoPatron;
	}

	/**
	 * @return Returns the mapaLogCuposExtras.
	 */
	public HashMap getMapaLogCuposExtras()
	{
		return mapaLogCuposExtras;
	}
	
	/**
	 * @param mapaLogCuposExtras The mapaLogCuposExtras to set.
	 */
	public void setMapaLogCuposExtras(HashMap mapaLogCuposExtras)
	{
		this.mapaLogCuposExtras = mapaLogCuposExtras;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaLogCuposExtras(String key) 
	{
		return mapaLogCuposExtras.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaLogCuposExtras(String key, Object value) 
	{
		mapaLogCuposExtras.put(key, value);
	}

	/**
	 * @return Returns the codigoMedico.
	 */
	public int getCodigoMedico()
	{
		return codigoMedico;
	}

	/**
	 * @param codigoMedico The codigoMedico to set.
	 */
	public void setCodigoMedico(int codigoMedico)
	{
		this.codigoMedico=codigoMedico;
	}

	/**
	 * @return Returns the codigoUnidadConsulta.
	 */
	public int getCodigoUnidadConsulta()
	{
		return codigoUnidadConsulta;
	}

	/**
	 * @param codigoUnidadConsulta The codigoUnidadConsulta to set.
	 */
	public void setCodigoUnidadConsulta(int codigoUnidadConsulta)
	{
		this.codigoUnidadConsulta=codigoUnidadConsulta;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal=fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial=fechaInicial;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente=linkSiguiente;
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
		this.offset=offset;
	}
	
	
	/**
	 * Funci�n de validaci�n: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		/*********************************************************************************/
		if(estado.equals("resultadoBusqueda"))
		{
			if(this.getFechaFinal().trim().equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required", "La Fecha Final"));
			}
			if(this.getFechaInicial().trim().equals(""))
			{
				errores.add("errors.required", new ActionMessage("errors.required", "La Fecha Inicial"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))>0)
			{
				errores.add("Fecha Inicial mayor a la fecha del Sistema", new ActionMessage("errors.fechaPosteriorIgualActual","La Fecha Inicial", " Actual"));
			}			
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial()))<0)
			{
				errores.add("Fecha Final menor a Fecha Incial", new ActionMessage("errors.fechaAnteriorIgualActual","La Fecha Final", " Inicial"));				
			}
			if((!this.getFechaInicial().equals(""))&&(this.getFechaFinal().equals("")))
			{
				errores.add("Definir Fecha  Final", new ActionMessage("error.cuposExtras.rangosFechaFinalNoDefinidos","Definir Fecha Final"));
			}
			if((this.getFechaInicial().trim().equals("")&&!this.getFechaFinal().trim().equals("")))
			{
				errores.add("Definir Fecha Inicial", new ActionMessage("error.cuposExtras.rangosFechaInicialNoDefinidos","Definir Fecha Inicial"));
			}
			if(!UtilidadFecha.validarFecha(this.getFechaFinal()))
			{
				errores.add("fechaFinal", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
			}
			if(!UtilidadFecha.validarFecha(this.getFechaInicial()))
			{
				errores.add("fechaInicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
			}			
			if(errores.isEmpty())
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.incrementarDiasAFecha(this.getFechaInicial(),90,false),this.getFechaFinal()))
				{
					errores.add("Fecha mayor dias otra", new ActionMessage("errors.fechaSuperaOtraPorDias", "Final","90","Inicial"));
				}
			}
		}
		return errores;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the centrosAtencion
	 */
	public String getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(String centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the unidadesAgenda
	 */
	public String getUnidadesAgenda() {
		return unidadesAgenda;
	}

	/**
	 * @param unidadesAgenda the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(String unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}

	/**
	 * @return the unidadAgendaMap
	 */
	public HashMap getUnidadAgendaMap() {
		return unidadAgendaMap;
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(HashMap unidadAgendaMap) {
		this.unidadAgendaMap = unidadAgendaMap;
	}
	
	/**
	 * @return the unidadAgendaMap
	 */
	public Object getUnidadAgendaMap(String llave) {
		return unidadAgendaMap.get(llave);
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(String llave, Object obj) {
		this.unidadAgendaMap.put(llave,obj);
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public HashMap getCentrosAtencionAutorizados() {
		return centrosAtencionAutorizados;
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(HashMap centrosAtencionAutorizados) {
		this.centrosAtencionAutorizados = centrosAtencionAutorizados;
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public Object getCentrosAtencionAutorizados(String llave) {
		return centrosAtencionAutorizados.get(llave);
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(String llave, Object obj) {
		this.centrosAtencionAutorizados.put(llave, obj);
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public HashMap getUnidadesAgendaAutorizadas() {
		return unidadesAgendaAutorizadas;
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(HashMap unidadesAgendaAutorizadas) {
		this.unidadesAgendaAutorizadas = unidadesAgendaAutorizadas;
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public Object getUnidadesAgendaAutorizadas(String llave) {
		return unidadesAgendaAutorizadas.get(llave);
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(String llave, Object obj) {
		this.unidadesAgendaAutorizadas.put(llave, obj);
	}
	
}


