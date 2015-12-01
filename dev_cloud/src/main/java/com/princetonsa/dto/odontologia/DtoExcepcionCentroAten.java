package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class DtoExcepcionCentroAten implements Serializable{
	
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	private int centroAtencion;
	private String fechaExcepcion;
	private String msgExcepcion;

	public DtoExcepcionCentroAten()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.fechaExcepcion = "" ;
		this.msgExcepcion = "";
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the fechaExcepcion
	 */
	public String getFechaExcepcion() {
		return fechaExcepcion;
	}

	/**
	 * @param fechaExcepcion the fechaExcepcion to set
	 */
	public void setFechaExcepcion(String fechaExcepcion) {
		this.fechaExcepcion = fechaExcepcion;
	}

	/**
	 * @return the msgExcepcion
	 */
	public String getMsgExcepcion() {
		return msgExcepcion;
	}

	/**
	 * @param msgExcepcion the msgExcepcion to set
	 */
	public void setMsgExcepcion(String msgExcepcion) {
		this.msgExcepcion = msgExcepcion;
	}
	
}
