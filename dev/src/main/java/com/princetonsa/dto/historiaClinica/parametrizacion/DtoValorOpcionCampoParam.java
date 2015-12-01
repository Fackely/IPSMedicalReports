/*
 * Oct 21, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;

/**
 * Data Transfer Object: Valor Opcion campo parametrizable
 * @author Sebastián Gómez R.
 *
 */
public class DtoValorOpcionCampoParam implements Serializable
{
	private String codigoPk;
	private String codigoPkOpcion;
	private String valor;
	private boolean mostrarModificacion;
	
	/**
	 * Constructor
	 */
	public DtoValorOpcionCampoParam()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTO
	 */
	public void clean()
	{
		this.codigoPk = "";
		this.codigoPkOpcion = "";
		this.valor = "";
		this.mostrarModificacion = false;
	}

	/**
	 * @return the codigoPk
	 */
	public String getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(String codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigoPkOpcion
	 */
	public String getCodigoPkOpcion() {
		return codigoPkOpcion;
	}

	/**
	 * @param codigoPkOpcion the codigoPkOpcion to set
	 */
	public void setCodigoPkOpcion(String codigoPkOpcion) {
		this.codigoPkOpcion = codigoPkOpcion;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the mostrarModificacion
	 */
	public boolean isMostrarModificacion() {
		return mostrarModificacion;
	}

	/**
	 * @param mostrarModificacion the mostrarModificacion to set
	 */
	public void setMostrarModificacion(boolean mostrarModificacion) {
		this.mostrarModificacion = mostrarModificacion;
	}
	
	
}
