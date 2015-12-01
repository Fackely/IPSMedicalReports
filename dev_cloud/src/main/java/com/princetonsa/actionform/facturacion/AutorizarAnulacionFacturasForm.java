package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

public class AutorizarAnulacionFacturasForm extends ValidatorForm 
{

	
	////////////// Autorizar Solicitudes Anulaciones
	
	private String estado;
	
	private HashMap mapaListadoSolicitudes;
	
	private HashMap mapaDetalleSolicitud;
	
	private HashMap mapaResumenAutorizar;
	
	private String observaciones;
	
	private String autorizaAnulacion;
	
	private int indiceDetalle;
	
	
	
	//////////// Anular Autorizaciones
	
	private HashMap mapaListadoAprobadas;
	
	private HashMap mapaDetalleAutorizacion;
	
	private HashMap mapaResumenAnulacion;
	
	private String fechaInicial;
	
	private String fechaFinal;
	
	private String consecutivoFactura;
	
	private String numeroAutorizacion;
	
	private String usuarioAutoriza;
	
	
	///////////// Consultar Autorizaciones
	
	private String motivoAnulacion;
	
	private String centroAtencion;
	
	private String estadoAutorizacion;
	
	private HashMap mapaConsultaListado;
	
	private HashMap mapaConsultadDetalle;
	
	
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.estado="";
		
		/////////// Autorizar Solictudes de Anulacion.
		this.mapaListadoSolicitudes= new HashMap();
		this.mapaListadoSolicitudes.put("numRegistros", "0");
		this.mapaDetalleSolicitud = new HashMap();
		this.mapaDetalleSolicitud.put("numRegistros", "0");
		this.mapaResumenAutorizar = new HashMap();
		this.mapaResumenAutorizar.put("numRegistros", "0");
		this.observaciones="";
		this.autorizaAnulacion="";
		this.indiceDetalle=ConstantesBD.codigoNuncaValido;
		
		/////////// Anular Autorizaciones.
		this.mapaListadoAprobadas = new HashMap();
		this.mapaListadoAprobadas.put("numRegistros", "0");
		this.mapaDetalleAutorizacion = new HashMap();
		this.mapaDetalleAutorizacion.put("numRegistros", "0");
		this.mapaResumenAnulacion = new HashMap();
		this.mapaResumenAnulacion.put("numRegistros", "0");
		this.fechaInicial="";
		this.fechaFinal="";
		this.consecutivoFactura="";
		this.numeroAutorizacion="";
		this.usuarioAutoriza="";
		
		////////// Consultar Autorizaciones.
		this.motivoAnulacion="";
		this.estadoAutorizacion="";
		this.centroAtencion="";
		this.mapaConsultaListado = new HashMap();
		this.mapaConsultaListado.put("numRegistros", "0");
		this.mapaConsultadDetalle = new HashMap();
		this.mapaConsultadDetalle.put("numRegistros", "0");
		
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
	public HashMap getMapaDetalleSolicitud() {
		return mapaDetalleSolicitud;
	}

	/**
	 * 
	 * @param mapaDetalleSolicitud
	 */
	public void setMapaDetalleSolicitud(HashMap mapaDetalleSolicitud) {
		this.mapaDetalleSolicitud = mapaDetalleSolicitud;
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
	public HashMap getMapaResumenAutorizar() {
		return mapaResumenAutorizar;
	}

	/**
	 * 
	 * @param mapaResumenAutorizar
	 */
	public void setMapaResumenAutorizar(HashMap mapaResumenAutorizar) {
		this.mapaResumenAutorizar = mapaResumenAutorizar;
	}

	/**
	 * 
	 * @return
	 */
	public String getAutorizaAnulacion() {
		return autorizaAnulacion;
	}

	/**
	 * 
	 * @param autorizaAnulacion
	 */
	public void setAutorizaAnulacion(String autorizaAnulacion) {
		this.autorizaAnulacion = autorizaAnulacion;
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
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * 
	 * @param numeroAutorizacion
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
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
	public HashMap getMapaListadoAprobadas() {
		return mapaListadoAprobadas;
	}

	/**
	 * 
	 * @param mapaListadoAprobadas
	 */
	public void setMapaListadoAprobadas(HashMap mapaListadoAprobadas) {
		this.mapaListadoAprobadas = mapaListadoAprobadas;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaDetalleAutorizacion() {
		return mapaDetalleAutorizacion;
	}

	/**
	 * 
	 * @param mapaDetalleAutorizacion
	 */
	public void setMapaDetalleAutorizacion(HashMap mapaDetalleAutorizacion) {
		this.mapaDetalleAutorizacion = mapaDetalleAutorizacion;
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
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}

	/**
	 * 
	 * @param estadoAutorizacion
	 */
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsultaListado() {
		return mapaConsultaListado;
	}

	/**
	 * 
	 * @param mapaConsultaListado
	 */
	public void setMapaConsultaListado(HashMap mapaConsultaListado) {
		this.mapaConsultaListado = mapaConsultaListado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsultadDetalle() {
		return mapaConsultadDetalle;
	}

	/**
	 * 
	 * @param mapaConsultadDetalle
	 */
	public void setMapaConsultadDetalle(HashMap mapaConsultadDetalle) {
		this.mapaConsultadDetalle = mapaConsultadDetalle;
	}

	
	
}
