package com.servinte.axioma.dto.manejoPaciente;

import java.util.Date;

import com.servinte.axioma.orm.Medicos;

/**
 * Dto para almacenar los datos de la anulacion de la autorización de capitación
 * 
 * @author wilgomcr
 * @version 1.0
 * @created 27-jul-2012 09:55:10 a.m.
 */
public class AnulacionAutorizacionSolicitudDto {

	/**Atributo que representa el motivo de anulación de la autorización*/
	private String motivoAnulacion;
	
	/**Atributo que representa el codigo del motivo de anulación*/
	private int codigoMotivoAnulacion;
	
	/**Atributo que representa la fecha de anulación de la autorización*/
	private Date fechaAnulacion;
	
	/**Atributo que representa la hora de anulación de la autorización*/
	private String horaAnulacion;

	/**Atributo que representa el login de usuario que anula autorización*/
	private String loginUsuarioAnulacion;
	
	/**Atributo que representa el medico que anula solicitud*/
	private Medicos medicoAnulacion;
	
	/**Atributo que representa el numero de solicitud*/
	private Integer numeroSolicitud;
	
	/**Atributo que representa el numero de la orden ambulatoria*/
	private Long codigoOrdenAmbulatoria;

	/**Atributo que representa el numero de la peticion*/
	private Integer codigoPeticion;

	/**Atributo que representa la orden ambulatoria es de pyp*/
	private boolean isPyP;
	
	/**
	 * @return motivoAnulacion
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return fechaAnulacion
	 */
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	/**
	 * @param fechaAnulacion
	 */
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	/**
	 * @return horaAnulacion
	 */
	public String getHoraAnulacion() {
		return horaAnulacion;
	}

	/**
	 * @param horaAnulacion
	 */
	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}

	/**
	 * @return medicoAnulacion
	 */
	public Medicos getMedicoAnulacion() {
		return medicoAnulacion;
	}

	/**
	 * @param medicoAnulacion
	 */
	public void setMedicoAnulacion(Medicos medicoAnulacion) {
		this.medicoAnulacion = medicoAnulacion;
	}

	/**
	 * @return loginUsuarioAnulacion
	 */
	public String getLoginUsuarioAnulacion() {
		return loginUsuarioAnulacion;
	}

	/**
	 * @param loginUsuarioAnulacion
	 */
	public void setLoginUsuarioAnulacion(String loginUsuarioAnulacion) {
		this.loginUsuarioAnulacion = loginUsuarioAnulacion;
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
	 * @return isPyP
	 */
	public boolean isPyP() {
		return isPyP;
	}

	/**
	 * @param isPyP
	 */
	public void setPyP(boolean isPyP) {
		this.isPyP = isPyP;
	}

	/**
	 * @return codigoPeticion
	 */
	public Integer getCodigoPeticion() {
		return codigoPeticion;
	}

	/**
	 * @param codigoPeticion
	 */
	public void setCodigoPeticion(Integer codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}
	
	/**
	 * @return codigoMotivoAnulacion
	 */
	public int getCodigoMotivoAnulacion() {
		return codigoMotivoAnulacion;
	}

	/**
	 * @param codigoMotivoAnulacion
	 */
	public void setCodigoMotivoAnulacion(int codigoMotivoAnulacion) {
		this.codigoMotivoAnulacion = codigoMotivoAnulacion;
	}

}
