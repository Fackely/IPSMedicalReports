package com.princetonsa.dto.administracion;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Dto que contiene la información del LOG de base de datos en donde se deben
 * registrar los usuarios que se van a inactivar
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 15/01/2011
 */
public class DtoProcesoInactivacionUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date fechaEjecucion;
	private String horaEjecucion;
	private String exitoso;
	private int cantidad;

	/**
	 * Constructor
	 */
	public DtoProcesoInactivacionUsuario() {
		this.fechaEjecucion = new Date();
		this.horaEjecucion = "";
		this.exitoso = "";
		this.cantidad = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Método encargado de obtener el valor del atributo fechaEjecucion.
	 * 
	 * @return fechaEjecucion
	 */
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo fechaEjecucion.
	 * 
	 * @param fechaEjecucion
	 */
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo horaEjecucion.
	 * 
	 * @return horaEjecucion
	 */
	public String getHoraEjecucion() {
		return horaEjecucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo horaEjecucion.
	 * 
	 * @param horaEjecucion
	 */
	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo exitoso.
	 * 
	 * @return exitoso
	 */
	public String getExitoso() {
		return exitoso;
	}

	/**
	 * Método encargado de establecer el valor del atributo exitoso.
	 * 
	 * @param exitoso
	 */
	public void setExitoso(String exitoso) {
		this.exitoso = exitoso;
	}

	/**
	 * Método encargado de obtener el valor del atributo cantidad.
	 * 
	 * @return cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * Método encargado de establecer el valor del atributo cantidad.
	 * 
	 * @param cantidad
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
