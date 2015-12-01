package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

public class DtoBalanceLiquidos implements Serializable
{

	
	private String valor;
	
	private String codigoBalance;
	
	private String nombreBalance;
	
	
	
	/**
	 * 
	 *
	 */
	public void clean()
	{
		
		this.valor="";
		this.codigoBalance="";
		this.nombreBalance="";
		
	}


	/**
	 * 
	 * @return
	 */
	public String getCodigoBalance() {
		return codigoBalance;
	}

	/**
	 * 
	 * @param codigoBalance
	 */
	public void setCodigoBalance(String codigoBalance) {
		this.codigoBalance = codigoBalance;
	}

	/**
	 * 
	 * @return
	 */
	public String getNombreBalance() {
		return nombreBalance;
	}

	/**
	 * 
	 * @param nombreBalance
	 */
	public void setNombreBalance(String nombreBalance) {
		this.nombreBalance = nombreBalance;
	}

	/**
	 * 
	 * @return
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * 
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
	
}
