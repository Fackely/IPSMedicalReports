package com.servinte.axioma.dto.manejoPaciente;

import java.util.Date;

/**
 * Dto para determinar si existe autorización de capitación para las ordenes.
 * 
 * @author wilgomcr
 * @version 1.0
 * @created 26-jul-2012 15:50:01 p.m.
 */
public class AutorizacionPorOrdenDto {

	
	/**Atributo que representa el consecutivo (pk) de la Autorizacion de Entidad Subcontratada*/
	private Long consecutivo;
	
	/**Atributo que representa el consecutivo de la autorización de Entidad Subcontratada*/
	private String consecutivoAutorizacion;
	
	/**Atributo que representa el codigo (pk) de la autorización de Capitación Subcontratada*/
	private Long codigoPkCapitacion;
	
	/**Atributo que representa el consecutivo de la autorización de Capitación Subcontratada*/
	private Long consecutivoCapitacion;
	
	/**Atributo que almacena el tipo de Autorización de Entidad Subcontratada*/
	private String tipoAutorizacion;
	
	/**Atributo que almacena la fecha de generación de la Autorización de Capitacion Subcontratada*/
	private Date fechaAutorizacionCapitacion;
	
	/**Atributo que almacena el codigo del convenio de la Autorizacion de Capitacion Subcontratada*/
	private Integer convenioAutorizacion;
	
	/**Atributo que permite identificar si existe una autorizacion previa para la solicitud a autorizar 
	 * de la auto_entsub_solicitudes*/
	private char migrado;

	/**Atributo que permite identificar el numero de solicitud asociada a la Autorizacion*/
	private Integer numeroSolicitud;
	
	/**Atributo que permite identificar el codigo de la orden ambulatoria asociada a la Autorizacion*/
	private Long codigoOrdenAmbulatoria;
	
	public AutorizacionPorOrdenDto(){
	}
	
	
	/**
	 * Constructor de la clase para cargar los datos obtenidos
	 * de la consulta de Existe Autorizacion Capitacion Sub para Solicitud
	 * (autorizacionCapitacion.existeAutorizacionCapitaSolicitud)
	 * 
	 * @param numeroSolicitud
	 * @param consecutivo
	 * @param fechaAutoriza
	 * @param migrado
	 * @param tipoAutorizacion
	 * @param convenio
	 * @param codigoPk
	 * @param consecutivoCapitacion
	 */
	public AutorizacionPorOrdenDto(Integer numeroSolicitud, Long consecutivo, String consecutivoAutorizacionEntSub, Date fechaAutoriza, char migrado,
			String tipoAutorizacion, Integer convenio, Long codigoPk, Long consecutivoCapitacion){
		this.numeroSolicitud				= numeroSolicitud;
		this.consecutivo					= consecutivo;
		this.consecutivoAutorizacion		= consecutivoAutorizacionEntSub;
		this.fechaAutorizacionCapitacion	= fechaAutoriza;
		this.migrado						= migrado;
		this.tipoAutorizacion				= tipoAutorizacion;
		this.convenioAutorizacion			= convenio;
		this.codigoPkCapitacion				= codigoPk;
		this.consecutivoCapitacion			= consecutivoCapitacion;
	}
	
	/**
	 * Constructor de la clase para cargar los datos obtenidos
	 * de la consulta de Existe Autorizacion Capitacion Sub para Orden Ambulatoria
	 * (autorizacionCapitacion.existeAutorizacionCapitaOrdenAmbulatoria)
	 * 
	 * @param codigo
	 * @param consecutivo
	 * @param fechaAutoriza
	 * @param tipoAutorizacion
	 * @param convenio
	 * @param codigoPk
	 * @param consecutivoCapitacion
	 */
	public AutorizacionPorOrdenDto(Long codigo, Long consecutivo, String consecutivoAutorizacion, Date fechaAutoriza,
			String tipoAutorizacion, Integer convenio, Long codigoPk, Long consecutivoCapitacion){
		this.codigoOrdenAmbulatoria			= codigo;
		this.consecutivo					= consecutivo;
		this.consecutivoAutorizacion		= consecutivoAutorizacion;
		this.fechaAutorizacionCapitacion	= fechaAutoriza;
		this.tipoAutorizacion				= tipoAutorizacion;
		this.convenioAutorizacion			= convenio;
		this.codigoPkCapitacion				= codigoPk;
		this.consecutivoCapitacion			= consecutivoCapitacion;
	}
	
	/**
	 * Constructor de la clase para cargar los datos obtenidos
	 * de la consutla de Existe Autorizacion Capitacion Sub para Peticion
	 * (autorizacionCapitacion.existeAutorizacionCapitaPeticion)
	 * 
	 * @param consecutivo
	 * @param consecutivoAutorizacion
	 * @param fechaAutoriza
	 * @param convenio
	 * @param codigoPk
	 * @param consecutivoCapitacion
	 */
	public AutorizacionPorOrdenDto(Long consecutivo, String consecutivoAutorizacion, Date fechaAutoriza,
			Integer convenio, Long codigoPk, Long consecutivoCapitacion) {
		this.consecutivo					= consecutivo;
		this.consecutivoAutorizacion 		= consecutivoAutorizacion;
		this.fechaAutorizacionCapitacion	= fechaAutoriza;
		this.convenioAutorizacion			= convenio;
		this.codigoPkCapitacion				= codigoPk;
		this.consecutivoCapitacion			= consecutivoCapitacion;
	}
	
	/**
	 * Constructor de la clase para cargar los datos obtenidos
	 * de la consutla de Existe Solicitudes asociadas a la Autorizacion Capitacion Sub
	 * (autorizacionCapitacion.obtenerSolicitudesAsociadasAutorizacion)
	 * 
	 *  @param numeroSolicitud
	 */
	public AutorizacionPorOrdenDto(Integer numeroSolicitud){
		this.numeroSolicitud	= numeroSolicitud;
	}
	
	/**
	 * Constructor de la clase para cargar los datos obtenidos
	 * de la consutla de Existe Ordenes Ambulatorias asociadas a la Autorizacion Capitacion Sub
	 * (autorizacionCapitacion.obtenerOrdenesAmbuAsociadasAutorizacion)
	 * 
	 *  @param codigoOrdenAmbulatoria
	 */
	public AutorizacionPorOrdenDto(Long codigoOrdenAmbulatoria){
		this.codigoOrdenAmbulatoria	= codigoOrdenAmbulatoria;
	}
	
	
	
	/**
	 * @return consecutivo
	 */
	public Long getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo
	 */
	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return consecutivoAutorizacion
	 */
	public String getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}

	/**
	 * @param consecutivoAutorizacion
	 */
	public void setConsecutivoAutorizacion(String consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}

	/**
	 * @return tipoAutorizacion
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}

	/**
	 * @param tipoAutorizacion
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}

	/**
	 * @return migrado
	 */
	public char getMigrado() {
		return migrado;
	}

	/**
	 * @param migrado
	 */
	public void setMigrado(char migrado) {
		this.migrado = migrado;
	}

	/**
	 * @return fechaAutorizacionCapitacion
	 */
	public Date getFechaAutorizacionCapitacion() {
		return fechaAutorizacionCapitacion;
	}

	/**
	 * @param fechaAutorizacionCapitacion
	 */
	public void setFechaAutorizacionCapitacion(
			Date fechaAutorizacionCapitacion) {
		this.fechaAutorizacionCapitacion = fechaAutorizacionCapitacion;
	}

	/**
	 * @return convenioAutorizacion
	 */
	public Integer getConvenioAutorizacion() {
		return convenioAutorizacion;
	}

	/**
	 * @param convenioAutorizacion
	 */
	public void setConvenioAutorizacion(Integer convenioAutorizacion) {
		this.convenioAutorizacion = convenioAutorizacion;
	}

	/**
	 * @return numeroSolicitud
	 */
	public Integer getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud
	 */
	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return codigoOrdenAmbulatoria
	 */
	public Long getCodigoOrdenAmbulatoria() {
		return codigoOrdenAmbulatoria;
	}

	/**
	 * @param codigoOrdenAmbulatoria
	 */
	public void setCodigoOrdenAmbulatoria(Long codigoOrdenAmbulatoria) {
		this.codigoOrdenAmbulatoria = codigoOrdenAmbulatoria;
	}

	/**
	 * @return codigoPkCapitacion
	 */
	public Long getCodigoPkCapitacion() {
		return codigoPkCapitacion;
	}

	/**
	 * @param codigoPkCapitacion
	 */
	public void setCodigoPkCapitacion(Long codigoPkCapitacion) {
		this.codigoPkCapitacion = codigoPkCapitacion;
	}

	/**
	 * @return consecutivoCapitacion
	 */
	public Long getConsecutivoCapitacion() {
		return consecutivoCapitacion;
	}

	/**
	 * @param consecutivoCapitacion
	 */
	public void setConsecutivoCapitacion(Long consecutivoCapitacion) {
		this.consecutivoCapitacion = consecutivoCapitacion;
	}
	
}
