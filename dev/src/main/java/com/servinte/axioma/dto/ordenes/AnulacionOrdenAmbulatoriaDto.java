package com.servinte.axioma.dto.ordenes;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto que guarda informacion de anulacion de peticiones 
 * @author wilgomcr
 * @created 12/09/2012
 */
public class AnulacionOrdenAmbulatoriaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5871269955714592785L;
	
	/**Atributo que almacena el codigo de la orden ambulatoria*/
	private long codigoOrden;
	
	/**Atributo que almacena el login del usuario que anula la orden ambulatoria*/
	private String usuario;
	
	/**Atributo que almacena la fecha de la orden ambulatoria*/
	private Date fecha;
	
	/**Atributo que almacena la hora de la orden ambulatoria*/
	private String hora;
	
	/**Atributo que almacena el motivo de anulacion de la orden ambulatoria*/
	private String motivoAnulacion;

	/**Atributo que almacena el estado de anulacion de la orden ambulatoria*/
	private byte codigoEstadoOrden;
	
	
	/**
	 * @return codigoOrden
	 */
	public long getCodigoOrden() {
		return codigoOrden;
	}

	/**
	 * @param codigoOrden
	 */
	public void setCodigoOrden(long codigoOrden) {
		this.codigoOrden = codigoOrden;
	}

	/**
	 * @return usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

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
	 * @return codigoEstadoOrden
	 */
	public byte getCodigoEstadoOrden() {
		return codigoEstadoOrden;
	}

	/**
	 * @param codigoEstadoOrden
	 */
	public void setCodigoEstadoOrden(byte codigoEstadoOrden) {
		this.codigoEstadoOrden = codigoEstadoOrden;
	}

}
