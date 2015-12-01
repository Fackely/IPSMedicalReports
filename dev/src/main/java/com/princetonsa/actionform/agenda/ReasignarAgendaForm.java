/*
 *@author Jorge Armando Osorio Velasquez 
 */
package com.princetonsa.actionform.agenda;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Jorge Armando Osorio Velasquez
 * 
 */
public class ReasignarAgendaForm extends ValidatorForm 
{
	
	/**
	 * Vairable para manejar el flujo de la funcionalidad.
	 */
	private String estado;
	/**
	 * Fecha inicial para realizar la busqueda de las agendas.
	 */
	private String fechaInicial;
	/**
	 * Fecha final para realizar la busqueda de las agendas.
	 */
	private String fechaFinal;
	/**
	 * codigo del profesional para realizar la busqueda de la agenda
	 */
	private int codigoProfesional;
	/**
	 * codigo de la unidad de consulta pra realizar la busqueda.
	 */
	private int codigoUnidadConsulta;
	/**
	 * mapa para manejar el listado de las agendas. 
	 */
	private HashMap agendas;
	
	/**
	 * Profesional seleccionado
	 */
	private String profesional;
	
	/**
	 * Profesional anterior
	 */
	private String codigoProfesionalAnterior;
	
	/**
	 * nombre del profesional
	 */
	private String nombreProfesional;
	
	/**
	 * Numero de items a mostrar por página
	 */
	private int maxPageItems;
	
	/**
	 *  profesional al que se reasignaran la nueva agenda.
	 */
	private int nuevoProfesional;
	
	/**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
     /**
      * 
      */
     private boolean seleccionarTodo;
	
     /**
      * almacena el indice por el cual 
      * se va ordenar el HashMap
      */
     private String patronOrdenar;
     
     /**
      * almacena el ultimo indice por el 
      * cual se ordeno el HashMap
      */
     private String ultimoPatron;
     
     /**
 	 * mapa para manejar el listado de las agendas. 
 	 */
 	private HashMap logs;
     /**
      * Usuario que genera la reasignacion, usado en la consulgta del log.
      */
     private String usuarioLog;
     
     /**
      * 
      */
     private String centroAtencion;
     
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
	 * Método para cargar los profesionales de la salud
	 */
	private ArrayList<HashMap<String,Object>> profesionales = new ArrayList<HashMap<String,Object>>();
     
	
	//----------------------------------
	//Cambio Funcionalidades Consulta Ext --Anexo 810
	private ArrayList<HashMap<String,Object>> profesionaleSaludUniAgen;
	private String indexCodUniAgen;
	private HashMap<String,Object> datosUnidadAgenda;
	//----------------------------------
	/**
	 * Metodo para ejecutar las validaciones necesarias en la aplicacion.
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("ejecutarBusqueda"))
		{
			if(this.getFechaInicial().equals("")){
				errores.add("Fecha de inicial", new ActionMessage("errors.required", "La Fecha Inicial es Requerida"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.getFechaInicial())){
					errores.add("fecha de inicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
				}
				else
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaInicial(),UtilidadFecha.getFechaActual()))
					{
						errores.add("fecha de inicial", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Inicial", "Actual"));
					}
				}
			}
			if(this.getFechaFinal().equals("")){
				errores.add("Fecha de final", new ActionMessage("errors.required", "La Fecha Fianl es Requerida"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.getFechaFinal())){
					errores.add("fecha de final", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
				}
				else
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(),UtilidadFecha.getFechaActual()))
					{
						errores.add("fecha de inicial", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Actual"));
					}
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(),this.getFechaInicial()))
					{
						errores.add("fecha de final", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Inicial"));
					}
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.incrementarDiasAFecha(this.getFechaInicial(),30,false),this.getFechaFinal()))
					{
						errores.add("Fecha mayor dias otra", new ActionMessage("errors.fechaSuperaOtraPorDias", "Final","30","Inicial"));
					}
				}
			}
			if(this.getCodigoUnidadConsulta()<0)
			{
				errores.add("La Unidad de Agenda", new ActionMessage("errors.required", "La Unidad de Agenda"));
			}
			if((this.getProfesional().split(ConstantesBD.separadorSplit)[0]).equals(ConstantesBD.codigoNuncaValido+""))
			{
				errores.add("El profesional de la salud", new ActionMessage("errors.required", "El profesional de la salud"));
			}
			this.codigoProfesionalAnterior = this.codigoProfesional+"";
		}
		else if(this.estado.equals("cambiarProfesional"))
		{
			this.nuevoProfesional=Integer.parseInt(profesional.split(ConstantesBD.separadorSplit)[0]);
			if(this.nuevoProfesional<0)
			{
				errores.add("el profesional", new ActionMessage("errors.required", "El Profesional de la Salud"));
			}
			int contadorRegistros=0;
			int numRegistros=Utilidades.convertirAEntero(getAgendas("numRegistros")+"");
			for(int i=0; i<numRegistros; i++)
			{
				if(UtilidadTexto.getBoolean(getAgendas("reasignar_"+i)+""))
				{
					contadorRegistros++;
					break; // para optimizar rompo el ciclo, ya se que si se seleccionó algún registro
				}
			}
			if(contadorRegistros==0)
			{
				errores.add("registro", new ActionMessage("errors.required", "Por lo menos una agenda"));
			}
		}
		else if(this.estado.equals("buscarLog"))
		{
			if(this.getFechaInicial().equals("")){
				errores.add("Fecha de inicial", new ActionMessage("errors.required", "La Fecha Inicial es Requerida"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.getFechaInicial())){
					errores.add("fecha de inicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
				}
				else
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(),UtilidadFecha.getFechaActual()))
					{
						errores.add("fecha de inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Actual"));
					}
				}
			}
			if(this.getFechaFinal().equals("")){
				errores.add("Fecha de final", new ActionMessage("errors.required", "La Fecha Fianl es Requerida"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.getFechaFinal())){
					errores.add("fecha de final", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
				}
				else
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(),this.getFechaInicial()))
					{
						errores.add("fecha de final", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Inicial"));
					}
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.incrementarDiasAFecha(this.getFechaInicial(),90,false),this.getFechaFinal()))
					{
						errores.add("Fecha mayor dias otra", new ActionMessage("errors.fechaSuperaOtraPorDias", "Final","90","Inicial"));
					}
				}
			}
		}
		return errores;
	}

    /**
     * Metodo que resetea los atributos de la clase
     */
    public void reset() 
    {
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.codigoProfesional=ConstantesBD.codigoNuncaValido;
    	this.nombreProfesional="";
    	this.codigoUnidadConsulta=ConstantesBD.codigoNuncaValido;
    	this.agendas=new HashMap();
    	this.profesional="";
    	this.maxPageItems=20;
    	this.nuevoProfesional=ConstantesBD.codigoNuncaValido;
    	this.linkSiguiente="";
    	this.seleccionarTodo=false;
    	this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.usuarioLog="";
        this.logs=new HashMap();
        this.centroAtencion="";
        this.unidadAgendaMap=new HashMap();
        this.centrosAtencionAutorizados=new HashMap();
    	this.centrosAtencionAutorizados.put("numRegistros", "0");
    	this.unidadesAgendaAutorizadas=new HashMap();
    	this.unidadesAgendaAutorizadas.put("numRegistros", "0");
    	this.profesionales = new ArrayList<HashMap<String,Object>>();
    	this.codigoProfesionalAnterior="";
    	
    	// Cambio Anexo 810 
    	this.profesionaleSaludUniAgen = new ArrayList<HashMap<String,Object>>();
    	this.indexCodUniAgen = "";
    	this.datosUnidadAgenda = new HashMap<String,Object>();
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getCodigoProfesional() {
		return codigoProfesional;
	}

	public void setCodigoProfesional(int codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}

	public int getCodigoUnidadConsulta() {
		return codigoUnidadConsulta;
	}

	public void setCodigoUnidadConsulta(int codigoUnidadConsulta) {
		this.codigoUnidadConsulta = codigoUnidadConsulta;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public HashMap getAgendas() {
		return agendas;
	}

	public void setAgendas(HashMap agendas) {
		this.agendas = agendas;
	}
	
	public Object getAgendas(String key) {
		return agendas.get(key);
	}

	public void setAgendas(String key,Object value) {
		this.agendas.put(key,value);
	}

	public String getNombreProfesional() {
		return nombreProfesional;
	}

	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}

	public String getProfesional() {
		return profesional;
	}

	public void setProfesional(String profesional) 
	{
		this.profesional = profesional;
		try
		{
			String[] prof=profesional.split(ConstantesBD.separadorSplit);
			this.codigoProfesional=Integer.parseInt(prof[0]);
			this.nombreProfesional=prof[1]+"";
		}catch (Exception e) {
			this.codigoProfesional=ConstantesBD.codigoNuncaValido;
			this.nombreProfesional="";
		}
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public int getNuevoProfesional() {
		return nuevoProfesional;
	}

	public void setNuevoProfesional(int nuevoProfesional) {
		this.nuevoProfesional = nuevoProfesional;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public boolean isSeleccionarTodo() {
		return seleccionarTodo;
	}

	public void setSeleccionarTodo(boolean seleccionarTodo) {
		this.seleccionarTodo = seleccionarTodo;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public String getUsuarioLog() {
		return usuarioLog;
	}

	public void setUsuarioLog(String usuarioLog) {
		this.usuarioLog = usuarioLog;
	}

	public HashMap getLogs() {
		return logs;
	}

	public void setLogs(HashMap logs) {
		this.logs = logs;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
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
		this.unidadAgendaMap.put(llave, obj);
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

	/**
	 * @return the profesionaleSaludUniAgen
	 */
	public ArrayList<HashMap<String, Object>> getProfesionaleSaludUniAgen() {
		return profesionaleSaludUniAgen;
	}

	/**
	 * @param profesionaleSaludUniAgen the profesionaleSaludUniAgen to set
	 */
	public void setProfesionaleSaludUniAgen(
			ArrayList<HashMap<String, Object>> profesionaleSaludUniAgen) {
		this.profesionaleSaludUniAgen = profesionaleSaludUniAgen;
	}

	/**
	 * @return the indexCodUniAgen
	 */
	public String getIndexCodUniAgen() {
		return indexCodUniAgen;
	}

	/**
	 * @param indexCodUniAgen the indexCodUniAgen to set
	 */
	public void setIndexCodUniAgen(String indexCodUniAgen) {
		this.indexCodUniAgen = indexCodUniAgen;
	}

	/**
	 * @return the datosUnidadAgenda
	 */
	public HashMap<String, Object> getDatosUnidadAgenda() {
		return datosUnidadAgenda;
	}

	/**
	 * @param datosUnidadAgenda the datosUnidadAgenda to set
	 */
	public void setDatosUnidadAgenda(HashMap<String, Object> datosUnidadAgenda) {
		this.datosUnidadAgenda = datosUnidadAgenda;
	}

	/**
	 * @return the codigoProfesionalAnterior
	 */
	public String getCodigoProfesionalAnterior() {
		return codigoProfesionalAnterior;
	}

	/**
	 * @param codigoProfesionalAnterior the codigoProfesionalAnterior to set
	 */
	public void setCodigoProfesionalAnterior(String codigoProfesionalAnterior) {
		this.codigoProfesionalAnterior = codigoProfesionalAnterior;
	}
	
	
}
