/**
 * 
 */
package com.princetonsa.dao.sqlbase.facturacion;

import util.ConstantesBD;

/**
 * @author armando
 *
 */
public class DtoVerficacionDerechos 
{
	
	/**
	 * 
	 */
	public DtoVerficacionDerechos()
	{
		this.valor=ConstantesBD.codigoNuncaValido;
		this.porcetaje=ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 */
	private double valor;
	
	/**
	 * 
	 */
	private double porcetaje;

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getPorcetaje() {
		return porcetaje;
	}

	public void setPorcetaje(double porcetaje) {
		this.porcetaje = porcetaje;
	}

}
