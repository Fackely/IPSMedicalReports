package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoParametrosBusquedaHonorarios implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7325188875459389795L;
	
	private int servicio; 
	private String tipoLiquidacion; 
	private int pool; 
	private int convenio; 
	private int esquemaTarifario; 
	private int especialidad;
	private int centroAtencion;
	private int codigoMedicoResponde;
	
	public DtoParametrosBusquedaHonorarios()
	{
		super();
		this.servicio = ConstantesBD.codigoNuncaValido;
		this.tipoLiquidacion = "";
		this.pool = ConstantesBD.codigoNuncaValido;
		this.convenio = ConstantesBD.codigoNuncaValido;
		this.esquemaTarifario = ConstantesBD.codigoNuncaValido;
		this.especialidad = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.codigoMedicoResponde= ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the servicio
	 */
	public int getServicio()
	{
		return servicio;
	}

	/**
	 * @return the tipoLiquidacion
	 */
	public String getTipoLiquidacion()
	{
		return tipoLiquidacion;
	}

	/**
	 * @return the pool
	 */
	public int getPool()
	{
		return pool;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public int getEsquemaTarifario()
	{
		return esquemaTarifario;
	}

	/**
	 * @return the especialidad
	 */
	public int getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(int servicio)
	{
		this.servicio = servicio;
	}

	/**
	 * @param tipoLiquidacion the tipoLiquidacion to set
	 */
	public void setTipoLiquidacion(String tipoLiquidacion)
	{
		this.tipoLiquidacion = tipoLiquidacion;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(int pool)
	{
		this.pool = pool;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(int esquemaTarifario)
	{
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad)
	{
		this.especialidad = especialidad;
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
	 * @return the codigoMedicoResponde
	 */
	public int getCodigoMedicoResponde() {
		return codigoMedicoResponde;
	}

	/**
	 * @param codigoMedicoResponde the codigoMedicoResponde to set
	 */
	public void setCodigoMedicoResponde(int codigoMedicoResponde) {
		this.codigoMedicoResponde = codigoMedicoResponde;
	}
}
