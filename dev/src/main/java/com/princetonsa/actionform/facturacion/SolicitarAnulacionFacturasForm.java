package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

public class SolicitarAnulacionFacturasForm extends ValidatorForm 
{

	
	
	private String estado;
	
	
	//////////////Atributos para la Solicitud de Anulaciones de Facturas
	private String consecutivoFactura;
	
	private String usuarioAutoriza;
	
	private String motivoAnulacion;
	
	private String observaciones;
	
	private int codigoFactura;
	
	private HashMap mapaSolictudes;
	
	private ResultadoBoolean mostrarMensaje;
	
	private HashMap mapaResumenSolictud;
	
	
	//////////////// Atributos para Anular Solicitudes de Facturas.
	private String fechaInicial;
	
	private String fechaFinal;
	
	private String codigoSolicitud;
	
	private String centroAtencion;
	
	private HashMap mapaListadoSolicitudes;
	
	private HashMap mapaDetalleSolictud;
	
	private HashMap mapaResumenAnulacion;
	
	private int indiceDetalle;
	
	
	
	/**
	 * 
	 */
	public void reset(String centroAtencion, String institucion)
	{
		
		//////////////Atributos para la Solicitud de Anulaciones de Facturas
		this.consecutivoFactura="";
		this.usuarioAutoriza="";
		this.motivoAnulacion="";
		this.observaciones="";
		this.codigoFactura=ConstantesBD.codigoNuncaValido;
		this.mapaSolictudes= new HashMap();
		this.mapaSolictudes.put("numRegistros", "0");
		this.mostrarMensaje = new ResultadoBoolean(false,"");
		this.mapaResumenSolictud= new HashMap();
		this.mapaResumenSolictud.put("numRegistros", "0");
		
		////////////////Atributos para Anular Solicitudes de Facturas.
		this.fechaInicial="";
		this.fechaFinal="";
		this.codigoSolicitud="";
		this.centroAtencion=centroAtencion;
		this.mapaListadoSolicitudes = new HashMap();
		this.mapaListadoSolicitudes.put("numRegistros", "0");
		this.mapaDetalleSolictud = new HashMap();
		this.mapaDetalleSolictud.put("numRegistros", "0");
		this.mapaResumenAnulacion = new HashMap();
		this.mapaResumenAnulacion.put("numRegistros", "0");
		this.indiceDetalle=ConstantesBD.codigoNuncaValido;
		
	}
	
	
	/**
	 * 
	 */
	public void resetEstado()
	{
		this.estado="";
	}
	
	/**
	 * 
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje = new ResultadoBoolean(false,"");
	}
	
	
	/**
	 * 
	 */
	public void resetBusqueda()
	{
		this.usuarioAutoriza="";
		this.motivoAnulacion="";
		this.observaciones="";
		this.mapaSolictudes= new HashMap();
		this.mapaSolictudes.put("numRegistros", "0");
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuarioAutoriza() {
		return usuarioAutoriza;
	}

	/**
	 * 
	 * @param usuarioAutoriza
	 */
	public void setUsuarioAutoriza(String usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}

	/**
	 * 
	 * @return
	 */
	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}

	/**
	 * 
	 * @param consecutivoFactura
	 */
	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	/**
	 * 
	 * @return
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	/**
	 * 
	 * @param motivoAnulacion
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * 
	 * @param codigoFactura
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaSolictudes() {
		return mapaSolictudes;
	}

	/**
	 * 
	 * @param mapaSolictudes
	 */
	public void setMapaSolictudes(HashMap mapaSolictudes) {
		this.mapaSolictudes = mapaSolictudes;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaResumenSolictud() {
		return mapaResumenSolictud;
	}

	/**
	 * 
	 * @param mapaResumenSolictud
	 */
	public void setMapaResumenSolictud(HashMap mapaResumenSolictud) {
		this.mapaResumenSolictud = mapaResumenSolictud;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * 
	 * @param fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * 
	 * @param fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoSolicitud() {
		return codigoSolicitud;
	}

	/**
	 * 
	 * @param codigoSolicitud
	 */
	public void setCodigoSolicitud(String codigoSolicitud) {
		this.codigoSolicitud = codigoSolicitud;
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaListadoSolicitudes() {
		return mapaListadoSolicitudes;
	}

	/**
	 * 
	 * @param mapaListadoSolicitudes
	 */
	public void setMapaListadoSolicitudes(HashMap mapaListadoSolicitudes) {
		this.mapaListadoSolicitudes = mapaListadoSolicitudes;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaDetalleSolictud() {
		return mapaDetalleSolictud;
	}

	/**
	 * 
	 * @param mapaDetalleSolictud
	 */
	public void setMapaDetalleSolictud(HashMap mapaDetalleSolictud) {
		this.mapaDetalleSolictud = mapaDetalleSolictud;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceDetalle() {
		return indiceDetalle;
	}

	/**
	 * 
	 * @param indiceDetalle
	 */
	public void setIndiceDetalle(int indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaResumenAnulacion() {
		return mapaResumenAnulacion;
	}

	/**
	 * 
	 * @param mapaResumenAnulacion
	 */
	public void setMapaResumenAnulacion(HashMap mapaResumenAnulacion) {
		this.mapaResumenAnulacion = mapaResumenAnulacion;
	}
	
	
	
}
