package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto para guardar los filtros de busqueda para las autorizaciones
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 03:18:58 p.m.
 */
public class ParametroBusquedaOrdenAutorizacionDto implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7126077379985394275L;
	
	/**
	 * Atributo que representa el codigo del convenio seleccionado
	 */
	private int codigoConvenio;
	
	/**
	 * Atributo que representa el codigo del contrato seleccionado
	 */
	private int codigoContrato;
	
	/**
	 * Atributo que representa el codigo de la via de ingreso seleccionada
	 */
	private int codigoViaIngreso;
	
	/**
	 * Atributo que representa el codigo de la clase de orden seleccionada
	 */
	private int codigoClaseOrden;
	
	/**
	 * Atributo que representa el codigo del nivel de atención seleccionado
	 */
	private long codigoNivelAtencion;
	
	/**
	 * Atributo que representa la fecha inicio seleccionada
	 */
	private Date fechaInicio;
	
	/**
	 * Atributo que representa la fecha fin seleccionada
	 */
	private Date fechaFin;
	
	/**
	 * Atributo que representa el valor del parametro general para las
	 * valdiaciones de via de ingreso de ordenes ambulatorias
	 */
	private String parametroViaIngresoOrdenAmb;
	
	/**
	 * Atributo que representa el valor del parametro general para las
	 * valdiaciones de via de ingreso de peticiones
	 */
	private String parametroViaIngresoPeticion;
	

	public ParametroBusquedaOrdenAutorizacionDto(){

	}


	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	/**
	 * @return the codigoContrato
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}


	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}


	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}


	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}


	/**
	 * @return the codigoClaseOrden
	 */
	public int getCodigoClaseOrden() {
		return codigoClaseOrden;
	}


	/**
	 * @param codigoClaseOrden the codigoClaseOrden to set
	 */
	public void setCodigoClaseOrden(int codigoClaseOrden) {
		this.codigoClaseOrden = codigoClaseOrden;
	}


	/**
	 * @return the codigoNivelAtencion
	 */
	public long getCodigoNivelAtencion() {
		return codigoNivelAtencion;
	}


	/**
	 * @param codigoNivelAtencion the codigoNivelAtencion to set
	 */
	public void setCodigoNivelAtencion(long codigoNivelAtencion) {
		this.codigoNivelAtencion = codigoNivelAtencion;
	}


	/**
	 * @return the fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}


	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	/**
	 * @return the fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}


	/**
	 * @param fechaFin the fechaFin to set
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}


	/**
	 * @return the parametroViaIngresoOrdenAmb
	 */
	public String getParametroViaIngresoOrdenAmb() {
		return parametroViaIngresoOrdenAmb;
	}


	/**
	 * @param parametroViaIngresoOrdenAmb the parametroViaIngresoOrdenAmb to set
	 */
	public void setParametroViaIngresoOrdenAmb(String parametroViaIngresoOrdenAmb) {
		this.parametroViaIngresoOrdenAmb = parametroViaIngresoOrdenAmb;
	}


	/**
	 * @return the parametroViaIngresoPeticion
	 */
	public String getParametroViaIngresoPeticion() {
		return parametroViaIngresoPeticion;
	}


	/**
	 * @param parametroViaIngresoPeticion the parametroViaIngresoPeticion to set
	 */
	public void setParametroViaIngresoPeticion(String parametroViaIngresoPeticion) {
		this.parametroViaIngresoPeticion = parametroViaIngresoPeticion;
	}

	
}