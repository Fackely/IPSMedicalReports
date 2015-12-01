/**
 * 
 */
package com.princetonsa.dto.cartera;

import util.ConstantesBD;
  
/**
 * @author armando
 *
 */
public class DtoDetalleReporteCuentaCobro 
{
	
	/**
	 * 
	 */
	private int cuentaCobro;
	
	/**
	 * 
	 */
	private int codigoEstado;
	
	/**
	 * 
	 */
	private String descripcionEstado;
	
	/**
	 * 
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private String descripcionCentroAtencion;
	
	/**
	 * 
	 */
	private String fechaElaboracion;
	
	/**
	 * 
	 */
	private String usuarioElaboracion;
	
	/**
	 * 
	 */
	private String nombreUsuarioElaboraccion;

	/**
	 * 
	 */
	private String fechaRadicacion;

	/**
	 * 
	 */
	private String usuarioRadicacion;

	/**
	 * 
	 */
	private String nombreUsuarioRadicacion;

	/**
	 * 
	 */
	private double valor;

	public DtoDetalleReporteCuentaCobro() 
	{
		this.cuentaCobro = ConstantesBD.codigoNuncaValido;
		this.codigoEstado = ConstantesBD.codigoNuncaValido;
		this.descripcionEstado = "";
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.descripcionCentroAtencion = "";
		this.fechaElaboracion = "";
		this.usuarioElaboracion = "";
		this.nombreUsuarioElaboraccion = "";
		this.fechaRadicacion = "";
		this.usuarioRadicacion = "";
		this.nombreUsuarioRadicacion = "";
		this.valor = ConstantesBD.codigoNuncaValidoDouble;
	}

	public int getCuentaCobro() {
		return cuentaCobro;
	}

	public void setCuentaCobro(int cuentaCobro) {
		this.cuentaCobro = cuentaCobro;
	}

	public int getCodigoEstado() {
		return codigoEstado;
	}

	public void setCodigoEstado(int codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	public String getDescripcionEstado() {
		return descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}

	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}

	public String getFechaElaboracion() {
		return fechaElaboracion;
	}

	public void setFechaElaboracion(String fechaElaboracion) {
		this.fechaElaboracion = fechaElaboracion;
	}

	public String getUsuarioElaboracion() {
		return usuarioElaboracion;
	}

	public void setUsuarioElaboracion(String usuarioElaboracion) {
		this.usuarioElaboracion = usuarioElaboracion;
	}

	public String getNombreUsuarioElaboraccion() {
		return nombreUsuarioElaboraccion;
	}

	public void setNombreUsuarioElaboraccion(String nombreUsuarioElaboraccion) {
		this.nombreUsuarioElaboraccion = nombreUsuarioElaboraccion;
	}

	public String getFechaRadicacion() {
		return fechaRadicacion;
	}

	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}

	public String getUsuarioRadicacion() {
		return usuarioRadicacion;
	}

	public void setUsuarioRadicacion(String usuarioRadicacion) {
		this.usuarioRadicacion = usuarioRadicacion;
	}

	public String getNombreUsuarioRadicacion() {
		return nombreUsuarioRadicacion;
	}

	public void setNombreUsuarioRadicacion(String nombreUsuarioRadicacion) {
		this.nombreUsuarioRadicacion = nombreUsuarioRadicacion;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}
	
	

}
