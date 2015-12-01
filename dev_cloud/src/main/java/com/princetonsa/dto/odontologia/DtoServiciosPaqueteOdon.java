package com.princetonsa.dto.odontologia;

import java.math.BigDecimal;

public class DtoServiciosPaqueteOdon 
{

	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private int codigoPkPaquete;
	
	/**
	 * 
	 */
	private int servicio;
	
	/**
	 * 
	 */
	private String descripcionServicio;
	
	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * 
	 */
	private boolean existeBD;
	

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoPkPaquete() {
		return codigoPkPaquete;
	}

	public void setCodigoPkPaquete(int codigoPkPaquete) {
		this.codigoPkPaquete = codigoPkPaquete;
	}

	public int getServicio() {
		return servicio;
	}

	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	public boolean isExisteBD() {
		return existeBD;
	}

	public void setExisteBD(boolean existeBD) {
		this.existeBD = existeBD;
	}

	
}
