package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

public class DtoResponsableJustificacionNoPos implements Serializable
{
	private String subcuenta;
	private String cantidad;
	private String acronimoEstado;
	private String estado;
	private String convenio;
	private String tipoUsuario;
	
	/**
	 * Constructor
	 */
	public void DtoResponsableJustificacionNoPos(){
		this.clean();
	}
	
	/**
	 * Resetea todas las variables del DTO
	 */
	public void clean()
	{
		this.subcuenta = "";
		this.cantidad = "";
		this.estado = "";
		this.convenio = "";
		this.acronimoEstado="";
		this.tipoUsuario="";
	}

	/**
	 * @return the subcuenta
	 */
	public String getSubcuenta() {
		return subcuenta;
	}

	/**
	 * @param subcuenta the subcuenta to set
	 */
	public void setSubcuenta(String subcuenta) {
		this.subcuenta = subcuenta;
	}

	/**
	 * @return the cantidad
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the acronimoEstado
	 */
	public String getAcronimoEstado() {
		return acronimoEstado;
	}

	/**
	 * @param acronimoEstado the acronimoEstado to set
	 */
	public void setAcronimoEstado(String acronimoEstado) {
		this.acronimoEstado = acronimoEstado;
	}

	/**
	 * @return the tipoUsuario
	 */
	public String getTipoUsuario() {
		return tipoUsuario;
	}

	/**
	 * @param tipoUsuario the tipoUsuario to set
	 */
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
	
}
