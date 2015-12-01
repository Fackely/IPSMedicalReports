package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ElementoApResource;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;


/**
 * Clase para el manejo de la parametrizacion de contratos de entidades subcontratadas
 * Date: 2009-01-26
 * @author jfhernandez@princetonsa.com
 */
@SuppressWarnings({ "unchecked", "serial" })
public class GeneracionTarifasPendientesEntSubForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * HashMap para gaurdar los filtros de la busqueda
	 */
	private HashMap filtrosBusqueda;
	
	/**
	 * ArrayList para guardar las entidades subcotratadas
	 */
	private ArrayList entidades;
	
	/**
	 *HashMap para guardar los resultados de la busqueda 
	 */
	private HashMap resultadosBusqueda;
	
	/**
	 *HashMap para guardar los resultados de la busqueda 
	 */
	private int numRegistros;
	
	/**
	 * Manejo de los errores del cargo
	 */
	private ArrayList<ElementoApResource> mensajesAlerta;
	
	/**
	 * Indice del detalle
	 */
	private int indice;
	
	/**
	 * HashMap para guardar los servicios asociados a una autorizacion
	 */
	private HashMap serviciosAutorizacion;
	
	/**
	 * HashMap para guardar los errores de una autorizacion de tarifas de servicios
	 */
	private HashMap erroresServiciosAutorizacion;
	
	/**
	 * hashMap para guardar lso resultados de la bsuqeuda de pedidos
	 */
	private HashMap resultadosPedidos;
	
	/**
	 * HashMap para guardar los resultados de solicitudes
	 */
	private HashMap resultadosSolicitudes;
	
	/**
	 * Manejo de los errores del cargo
	 */
	private ArrayList<ElementoApResource> mensajesAlertaPedidos;
	
	/**
	 * Manejo de los errores del cargo
	 */
	private ArrayList<ElementoApResource> mensajesAlertaSolicitudes;
	
	/**
	 * HashMap para guardar los errores de una autorizacion de tarifas de servicios
	 */
	private HashMap erroresArticulosPedidos;
	
	/**
	 * HashMap para guardar los errores de una autorizacion de tarifas de servicios
	 */
	private HashMap erroresArticulosSolicitudes;
	
	/**
	 * HashMap para guardar los servicios antes de generar las tarifas
	 */
	private HashMap resultadosBusquedaAux;
	
	/**
	 * HashMap para guardarlos articulos que vienen de pedidos antes de generar la tarifa 
	 */
	private HashMap resultadosPedidosAux;
	
	/**
	 * HashMap para guardarlos articulos que vienen de solicitudes antes de generar la tarifa
	 */
	private HashMap resultadosSolicitudesAux;
	
	/**
	 * Boolean para determinar si se debe mostrar el mensaje de advertencia por si no se genraron errores al tratar de generar tarifas
	 */
	private boolean mostrarMensaje;
	
	/**
	 * Mapa para guardar el listado de tarifas generadas 
	 */
	private HashMap serviciosTarifados;
	
	/**
	 * Mapa para gaurdar los articulos pedidos tarifados
	 */
	
	private HashMap pedidosTarifados;
	
	/**
	 * Mapa para gaurdar los articulos solicitudes tarifados
	 */
	
	private HashMap solicitudesTarifados;
	
	private boolean mostrarMensajePedidos;
	private boolean mostrarMensajeSolicitudes;
	
	
	/** * Contiene el resultado del cargo generado. Se agrega por Anexo 804 Cambio 1.2 
	 * para no perder los cargos generados cuando son varias autorizaciones seleccionadas. */
	ArrayList<CargosEntidadesSubcontratadas> listaCargos;
	
	
	/**
	 * reset
	 */
	public void reset()
	{
		this.estado="";
		this.filtrosBusqueda = new HashMap<String, Object>();
		this.filtrosBusqueda.put("numRegistros", "0");
		this.entidades = new ArrayList<HashMap<String,Object>>();
		this.resultadosBusqueda = new HashMap<String, Object>();
		this.resultadosBusqueda.put("numRegistros", "0");
		this.numRegistros=0;
		this.mensajesAlerta = new ArrayList<ElementoApResource>();
		this.indice=0;
		this.serviciosAutorizacion = new HashMap<String, Object>();
		this.serviciosAutorizacion.put("numRegistros", "0");
		this.erroresServiciosAutorizacion = new HashMap<String, Object>();
		this.erroresServiciosAutorizacion.put("numRegistros", "0");
		this.resultadosPedidos = new HashMap<String, Object>();
		this.resultadosPedidos.put("numRegistros", "0");
		this.resultadosSolicitudes = new HashMap<String, Object>();
		this.resultadosSolicitudes.put("numRegistros", "0");
		this.mensajesAlertaPedidos = new ArrayList<ElementoApResource>();
		this.mensajesAlertaSolicitudes = new ArrayList<ElementoApResource>();
		this.erroresArticulosPedidos = new HashMap<String, Object>();
		this.erroresArticulosPedidos.put("numRegistros", "0");
		this.erroresArticulosSolicitudes = new HashMap<String, Object>();
		this.erroresArticulosSolicitudes.put("numRegistros", "0");
		this.resultadosBusquedaAux = new HashMap<String, Object>();
		this.resultadosBusquedaAux.put("numRegistros", "0");
		this.resultadosPedidosAux = new HashMap<String, Object>();
		this.resultadosPedidosAux.put("numRegistros", "0");
		this.resultadosSolicitudesAux = new HashMap<String, Object>();
		this.resultadosSolicitudesAux.put("numRegistros", "0");
		this.mostrarMensaje=false;
		this.serviciosTarifados=new HashMap<String, Object>();
		this.pedidosTarifados=new HashMap<String, Object>();
		this.solicitudesTarifados=new HashMap<String, Object>();
		this.mostrarMensajePedidos=false;
		this.mostrarMensajeSolicitudes=false;
		this.listaCargos = new ArrayList<CargosEntidadesSubcontratadas>();
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        
        if (this.estado.equals("buscar"))
        {
        	if (UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechaini").toString())&&!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechafin").toString()))
        		errores.add(this.filtrosBusqueda.get("fechaini").toString(), new ActionMessage("errors.required","La Fecha Inicial del Contrato "));

    		if (!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechaini").toString())&&UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechafin").toString()))
    			errores.add(this.filtrosBusqueda.get("fechaini").toString(), new ActionMessage("errors.required","La Fecha Final del Contrato "));

    		if (UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechaini").toString())&&UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechafin").toString()))
    		{
    			errores.add(this.filtrosBusqueda.get("fechaini").toString()+ " "+" "+this.filtrosBusqueda.get("fechafin").toString() , new ActionMessage("errors.required","La Fecha Inicial y la Fecha Final del Contrato "));
    		}
    		
    		if (!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechaini").toString())&&!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechafin").toString()))
    		{
	    		if (UtilidadFecha.esFechaValidaSegunAp(this.filtrosBusqueda.get("fechafin").toString())&&UtilidadFecha.esFechaValidaSegunAp(this.filtrosBusqueda.get("fechaini").toString()))
	    		{
    				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.filtrosBusqueda.get("fechafin").toString(),this.filtrosBusqueda.get("fechaini").toString()))
	    				errores.add("", new ActionMessage("errors.fechaFinalPosteriorInicial", "Fecha Final del contrato "+this.filtrosBusqueda.get("fechafin").toString(),"Fecha Inicial del contrato "+this.filtrosBusqueda.get("fechaini").toString()));
	    		}
	    		if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosBusqueda.get("fechaini").toString()))
	    			errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial del contrato "+this.filtrosBusqueda.get("fechaini").toString()));
    				
    			
	    		if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosBusqueda.get("fechafin").toString()))
	    			errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final del contrato "+this.filtrosBusqueda.get("fechafin").toString()));
    				
    		}

    		String fechaActual=UtilidadFecha.getFechaActual();
    		if (!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechaini").toString())&&!UtilidadTexto.isEmpty(fechaActual))
    		{
	    		if (UtilidadFecha.esFechaValidaSegunAp(fechaActual)&&UtilidadFecha.esFechaValidaSegunAp(this.filtrosBusqueda.get("fechaini").toString()))
	    		{
    				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual,this.filtrosBusqueda.get("fechaini").toString()))
    					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial del contrato "+this.filtrosBusqueda.get("fechaini").toString(), "Actual "+fechaActual));
	    		}
    		}
    		if (!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechafin").toString())&&!UtilidadTexto.isEmpty(fechaActual))
    		{
	    		if (UtilidadFecha.esFechaValidaSegunAp(fechaActual)&&UtilidadFecha.esFechaValidaSegunAp(this.filtrosBusqueda.get("fechafin").toString()))
	    		{
    				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual,this.filtrosBusqueda.get("fechafin").toString()))
	    				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Final del contrato "+this.filtrosBusqueda.get("fechafin").toString(), "Actual "+fechaActual));
	    		}
    		}
    		
    		if (!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechaini").toString())&&!UtilidadTexto.isEmpty(this.filtrosBusqueda.get("fechafin").toString()))
    		{
	    		if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.incrementarDiasAFecha(this.filtrosBusqueda.get("fechaini").toString(),90,false),this.filtrosBusqueda.get("fechafin").toString()))
				{
					errores.add("Fecha mayor dias otra", new ActionMessage("errors.fechaSuperaOtraPorDias", "Final","90","Inicial"));
				}
    		}
    		
    		if (UtilidadTexto.isEmpty(this.filtrosBusqueda.get("tipotarifa").toString()))
    		{
    			errores.add(this.filtrosBusqueda.get("tipotarifa").toString(), new ActionMessage("errors.required","El tipo de tarifa "));
    		}
        }
        	
        return errores;
    }
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getFiltrosBusqueda() {
		return filtrosBusqueda;
	}

	public void setFiltrosBusqueda(HashMap filtrosBusqueda) {
		this.filtrosBusqueda = filtrosBusqueda;
	}

	public ArrayList getEntidades() {
		return entidades;
	}

	public void setEntidades(ArrayList entidades) {
		this.entidades = entidades;
	}
	
	public HashMap getResultadosBusqueda() {
		return resultadosBusqueda;
	}

	public void setResultadosBusqueda(HashMap resultadosBusqueda) {
		@SuppressWarnings("unused")
		ArrayList<ElementoApResource> mensajesAlerta;
		this.resultadosBusqueda = resultadosBusqueda;
	}
	
	public int getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	
	public ArrayList<ElementoApResource> getMensajesAlerta() {
		return mensajesAlerta;
	}

	public void setMensajesAlerta(ArrayList<ElementoApResource> mensajesAlerta) {
		this.mensajesAlerta = mensajesAlerta;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}
	
	public HashMap getServiciosAutorizacion() {
		return serviciosAutorizacion;
	}

	public void setServiciosAutorizacion(HashMap serviciosAutorizacion) {
		this.serviciosAutorizacion = serviciosAutorizacion;
	}

	public HashMap getErroresServiciosAutorizacion() {
		return erroresServiciosAutorizacion;
	}

	public void setErroresServiciosAutorizacion(HashMap erroresServiciosAutorizacion) {
		this.erroresServiciosAutorizacion = erroresServiciosAutorizacion;
	}

	public HashMap getResultadosPedidos() {
		return resultadosPedidos;
	}

	public void setResultadosPedidos(HashMap resultadosPedidos) {
		this.resultadosPedidos = resultadosPedidos;
	}

	public HashMap getResultadosSolicitudes() {
		return resultadosSolicitudes;
	}

	public void setResultadosSolicitudes(HashMap resultadosSolicudes) {
		this.resultadosSolicitudes = resultadosSolicudes;
	}
	
	public ArrayList<ElementoApResource> getMensajesAlertaPedidos() {
		return mensajesAlertaPedidos;
	}

	public void setMensajesAlertaPedidos(
			ArrayList<ElementoApResource> mensajesAlertaPedidos) {
		this.mensajesAlertaPedidos = mensajesAlertaPedidos;
	}

	public ArrayList<ElementoApResource> getMensajesAlertaSolicitudes() {
		return mensajesAlertaSolicitudes;
	}

	public void setMensajesAlertaSolicitudes(
			ArrayList<ElementoApResource> mensajesAlertaSolicitudes) {
		this.mensajesAlertaSolicitudes = mensajesAlertaSolicitudes;
	}
	
	public HashMap getErroresArticulosPedidos() {
		return erroresArticulosPedidos;
	}

	public void setErroresArticulosPedidos(HashMap erroresArticulosPedidos) {
		this.erroresArticulosPedidos = erroresArticulosPedidos;
	}

	public HashMap getErroresArticulosSolicitudes() {
		return erroresArticulosSolicitudes;
	}

	public void setErroresArticulosSolicitudes(HashMap erroresArticulosSolicitudes) {
		this.erroresArticulosSolicitudes = erroresArticulosSolicitudes;
	}

	/**
	 * @return the filtros
	 */
	public Object getResultadosPedidos(String llave) {
		return resultadosPedidos.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setResultadosPedidos(String llave, Object obj) {
		this.resultadosPedidos.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getResultadosSolicitudes(String llave) {
		return resultadosSolicitudes.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setResultadosSolicitudes(String llave, Object obj) {
		this.resultadosSolicitudes.put(llave, obj);
	}
	
	
	/**
	 * @return the filtros
	 */
	public Object getFiltrosBusqueda(String llave) {
		return filtrosBusqueda.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setFiltrosBusqueda(String llave, Object obj) {
		this.filtrosBusqueda.put(llave, obj);
	}
	
	/**
	 * @return the resultadosBusqueda
	 */
	public Object getResultadosBusqueda(String llave) {
		return resultadosBusqueda.get(llave);
	}

	/**
	 * @param resultadosBusqueda the resultadosBusqueda to set
	 */
	public void setResultadosBusqueda(String llave, Object obj) {
		this.resultadosBusqueda.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getServiciosAutorizacion(String llave) {
		return serviciosAutorizacion.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setServiciosAutorizacion(String llave, Object obj) {
		this.serviciosAutorizacion.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getErroresServiciosAutorizacion(String llave) {
		return erroresServiciosAutorizacion.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setErroresServiciosAutorizacion(String llave, Object obj) {
		this.erroresServiciosAutorizacion.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getErroresArticulosPedidos(String llave) {
		return erroresArticulosPedidos.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setErroresArticulosPedidos(String llave, Object obj) {
		this.erroresArticulosPedidos.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getErroresArticulosSolicitudes(String llave) {
		return erroresArticulosSolicitudes.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setErroresArticulosSolicitudes(String llave, Object obj) {
		this.erroresArticulosSolicitudes.put(llave, obj);
	}

	public HashMap getResultadosBusquedaAux() {
		return resultadosBusquedaAux;
	}

	public void setResultadosBusquedaAux(HashMap resultadosBusquedaAux) {
		this.resultadosBusquedaAux = resultadosBusquedaAux;
	}

	public HashMap getResultadosPedidosAux() {
		return resultadosPedidosAux;
	}

	public void setResultadosPedidosAux(HashMap resultadosPedidosAux) {
		this.resultadosPedidosAux = resultadosPedidosAux;
	}

	public HashMap getResultadosSolicitudesAux() {
		return resultadosSolicitudesAux;
	}

	public void setResultadosSolicitudesAux(HashMap resultadosSolicitudesAux) {
		this.resultadosSolicitudesAux = resultadosSolicitudesAux;
	}

	public boolean isMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(boolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
	
	/**
	 * @return the resultadosBusquedaAux
	 */
	public Object getResultadosBusquedaAux(String llave) {
		return resultadosBusquedaAux.get(llave);
	}
	
	/**
	 * @param resultadosBusquedaAux the filtros to set
	 */
	public void setResultadosBusquedaAux(String llave, Object obj) {
		this.resultadosBusquedaAux.put(llave, obj);
	}

	public HashMap getServiciosTarifados() {
		return serviciosTarifados;
	}

	public void setServiciosTarifados(HashMap serviciosTarifados) {
		this.serviciosTarifados = serviciosTarifados;
	}
	
	public Object getResultadosSolicitudesAux(String llave) {
		return resultadosSolicitudesAux.get(llave);
	}
	
	public Object getResultadosPedidosAux (String llave) {
		return resultadosPedidosAux.get(llave);
	}
	
	public void setResultadosPedidosAux (String llave, Object obj) {
		this.resultadosPedidosAux.put(llave, obj);
	}
	
	public void setResultadosSolicitudesAux (String llave, Object obj) {
		this.resultadosSolicitudesAux.put(llave, obj);
	}

	public HashMap getPedidosTarifados() {
		return pedidosTarifados;
	}

	public void setPedidosTarifados(HashMap pedidosTarifados) {
		this.pedidosTarifados = pedidosTarifados;
	}

	public HashMap getSolicitudesTarifados() {
		return solicitudesTarifados;
	}

	public void setSolicitudesTarifados(HashMap solicitudesTarifados) {
		this.solicitudesTarifados = solicitudesTarifados;
	}

	public boolean isMostrarMensajePedidos() {
		return mostrarMensajePedidos;
	}

	public void setMostrarMensajePedidos(boolean mostrarMensajePedidos) {
		this.mostrarMensajePedidos = mostrarMensajePedidos;
	}

	public boolean isMostrarMensajeSolicitudes() {
		return mostrarMensajeSolicitudes;
	}

	public void setMostrarMensajeSolicitudes(boolean mostrarMensajeSolicitudes) {
		this.mostrarMensajeSolicitudes = mostrarMensajeSolicitudes;
	}
	
	
	
	
	/**
	 *limpia todos los mensajes a mostrar
	 */
	public void resetMensajes()
	{
		this.mensajesAlerta = new ArrayList<ElementoApResource>();
		this.erroresServiciosAutorizacion = new HashMap<String, Object>();
		this.erroresServiciosAutorizacion.put("numRegistros", "0");
		//this.resultadosPedidos.put("numRegistros", "0");
		//this.resultadosSolicitudes = new HashMap<String, Object>();
		//this.resultadosSolicitudes.put("numRegistros", "0");
		this.mensajesAlertaPedidos = new ArrayList<ElementoApResource>();
		this.mensajesAlertaSolicitudes = new ArrayList<ElementoApResource>();
		this.erroresArticulosPedidos = new HashMap<String, Object>();
		this.erroresArticulosPedidos.put("numRegistros", "0");
		this.erroresArticulosSolicitudes = new HashMap<String, Object>();
		this.erroresArticulosSolicitudes.put("numRegistros", "0");
		//this.resultadosBusquedaAux = new HashMap<String, Object>();
		//this.resultadosBusquedaAux.put("numRegistros", "0");
		//this.resultadosPedidosAux = new HashMap<String, Object>();
		//this.resultadosPedidosAux.put("numRegistros", "0");
		//this.resultadosSolicitudesAux = new HashMap<String, Object>();
		//this.resultadosSolicitudesAux.put("numRegistros", "0");
		this.mostrarMensaje=false;
		this.mostrarMensajePedidos=false;
		this.mostrarMensajeSolicitudes=false;
		
		this.listaCargos = new ArrayList<CargosEntidadesSubcontratadas>();
	}
	

	public ArrayList<CargosEntidadesSubcontratadas> getListaCargos() {
		return listaCargos;
	}

	public void setListaCargos(ArrayList<CargosEntidadesSubcontratadas> listaCargos) {
		this.listaCargos = listaCargos;
	}

}