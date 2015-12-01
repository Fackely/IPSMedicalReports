/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dto.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dto.facturacion;

import java.io.Serializable;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoDetallePaquete implements Serializable
{
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String codigoPaquetizacion;
	
	/**
	 * 
	 */
	private String numeroSolicitud;
	
	/**
	 * 
	 */
	private String servicion;
	
	/**
	 * 
	 */
	private String articulo;
	
	/**
	 * 
	 */
	private String cantidadCargo;
	
	/**
	 * 
	 */
	private String cantidadAsignada;
	
	/**
	 * 
	 */
	private String servicioPadre;
	
	
	

	/**
	 *
	 */
	public DtoDetallePaquete() 
	{
		this.codigo="";
		this.codigoPaquetizacion="";
		this.numeroSolicitud="";
		this.servicion="";
		this.articulo="";
		this.cantidadCargo="";
		this.cantidadAsignada="";
		this.servicioPadre="";
	}

	/**
	 * @return the articulo
	 */
	public String getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the cantidadAsignada
	 */
	public String getCantidadAsignada() {
		return cantidadAsignada;
	}

	/**
	 * @param cantidadAsignada the cantidadAsignada to set
	 */
	public void setCantidadAsignada(String cantidadAsignada) {
		this.cantidadAsignada = cantidadAsignada;
	}

	/**
	 * @return the cantidadCargo
	 */
	public String getCantidadCargo() {
		return cantidadCargo;
	}

	/**
	 * @param cantidadCargo the cantidadCargo to set
	 */
	public void setCantidadCargo(String cantidadCargo) {
		this.cantidadCargo = cantidadCargo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoPaquetizacion
	 */
	public String getCodigoPaquetizacion() {
		return codigoPaquetizacion;
	}

	/**
	 * @param codigoPaquetizacion the codigoPaquetizacion to set
	 */
	public void setCodigoPaquetizacion(String codigoPaquetizacion) {
		this.codigoPaquetizacion = codigoPaquetizacion;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the servicion
	 */
	public String getServicion() {
		return servicion;
	}

	/**
	 * @param servicion the servicion to set
	 */
	public void setServicion(String servicion) {
		this.servicion = servicion;
	}

	/**
	 * @return the servicioPadre
	 */
	public String getServicioPadre() {
		return servicioPadre;
	}

	/**
	 * @param servicioPadre the servicioPadre to set
	 */
	public void setServicioPadre(String servicioPadre) {
		this.servicioPadre = servicioPadre;
	}
	
	
	

}
