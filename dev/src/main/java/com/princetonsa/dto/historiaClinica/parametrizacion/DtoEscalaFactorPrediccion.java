/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * Data Transfer Object: Factor de prediccion de la escala
 * @author Sebastián Gómez R.
 *
 */
public class DtoEscalaFactorPrediccion implements Serializable
{
	private String codigoPK;
	private String codigo;
	private String nombre;
	private int codigoInstitucion;
	private double valorInicial;
	private double valorFinal;
	private boolean activo;
	
	
	/**
	 * Resetea datos del DTO
	 *
	 */
	public void clean()
	{
		this.codigoPK = "";
		this.codigo = "";
		this.nombre = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.valorInicial = ConstantesBD.codigoNuncaValido;
		this.valorFinal = ConstantesBD.codigoNuncaValido;
		this.activo = false;
	}
	
	/**
	 * Constructor del DTO
	 *
	 */
	public DtoEscalaFactorPrediccion()
	{
		this.clean();
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
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
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the valorFinal
	 */
	public double getValorFinal() {
		return valorFinal;
	}

	/**
	 * @param valorFinal the valorFinal to set
	 */
	public void setValorFinal(double valorFinal) {
		this.valorFinal = valorFinal;
	}

	/**
	 * @return the valorInicial
	 */
	public double getValorInicial() {
		return valorInicial;
	}

	/**
	 * @param valorInicial the valorInicial to set
	 */
	public void setValorInicial(double valorInicial) {
		this.valorInicial = valorInicial;
	}
}
