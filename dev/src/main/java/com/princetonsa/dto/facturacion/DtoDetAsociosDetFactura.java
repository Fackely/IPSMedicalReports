package com.princetonsa.dto.facturacion;

import java.io.Serializable;

/**
 * 
 * @author axioma
 *
 */
public class DtoDetAsociosDetFactura implements Serializable 
{
	/**
	 * 
	 */
	private double codigo;
	
	/**
	 * 
	 */
	private int asocioDetFactura;
	
	/**
	 * 
	 */
	private int articulo;
	
	private String descArticulo;
	
	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * 
	 */
	private double valorUnitario;
	
	/**
	 * 
	 */
	private double valorTotal;

	/**
	 * 
	 * @param codigo
	 * @param asocioDetFactura
	 * @param articulo
	 * @param cantidad
	 * @param valorUnitario
	 * @param valorTotal
	 */
	public DtoDetAsociosDetFactura() 
	{
		super();
		this.codigo = codigo;
		this.asocioDetFactura = asocioDetFactura;
		this.articulo = articulo;
		this.cantidad = cantidad;
		this.valorUnitario = valorUnitario;
		this.valorTotal = valorTotal;
		this.descArticulo="";
	}
 
	
	
	public String getDescArticulo() {
		return descArticulo;
	}



	public void setDescArticulo(String descArticulo) {
		this.descArticulo = descArticulo;
	}



	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the asocioDetFactura
	 */
	public int getAsocioDetFactura() {
		return asocioDetFactura;
	}

	/**
	 * @param asocioDetFactura the asocioDetFactura to set
	 */
	public void setAsocioDetFactura(int asocioDetFactura) {
		this.asocioDetFactura = asocioDetFactura;
	}

	/**
	 * @return the articulo
	 */
	public int getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the valorUnitario
	 */
	public double getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the valorTotal
	 */
	public double getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
	
}