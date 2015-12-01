package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;

/**
 * Dto que contiene el consolidado total de cantidad y valor en
 * un mes para determinado item
 * 
 * @version 1.0, May 06, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoTotalReporte implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 428969914039164568L;
	
	/**
	 * Atributo que representa el consolidado de la cantidad 
	 */
	private double cantidad;
	
	/**
	 * Atributo que representa el condolidado del valor
	 */
	private double valor;
	
	/**
	 * Atributo que representa el identificador del mes consolidado
	 */
	private int idMes;
	
	/**
	 * Constructor de la clase
	 */
	public DtoTotalReporte(){
		
	}

	/**
	 * @return the cantidad
	 */
	public double getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the valor
	 */
	public double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}

	/**
	 * @return the idMes
	 */
	public int getIdMes() {
		return idMes;
	}

	/**
	 * @param idMes the idMes to set
	 */
	public void setIdMes(int idMes) {
		this.idMes = idMes;
	}
	
	

}
