package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ricruico
 *
 */
public class ValorClaseInventariosPacienteDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7329842228918498118L;
	
	private String nombreClaseInventario;
	private double cantidadAutorizada;
	private double valorAutorizado;
	private double valorFacturado;
	

	public ValorClaseInventariosPacienteDto(){
	this.reset();
		}
	public void reset()
	{
		this.setnombreClaseInventario("");
		this.setCantidadAutorizada(0);
		this.setValorAutorizado(0);
		this.setValorFacturado(0);
		
	}

	
	/**
	 * @param nombreClaseInventario
	 * @param cantidadAutorizada
	 * @param valorAutorizado
	 * @param valorFacturado
	 */
	public ValorClaseInventariosPacienteDto(String nombreClaseInventario,
			BigDecimal cantidadAutorizada, BigDecimal valorAutorizado) {
		this.nombreClaseInventario = nombreClaseInventario;
		this.cantidadAutorizada = cantidadAutorizada.doubleValue();
		this.valorAutorizado = valorAutorizado.doubleValue();
		this.valorFacturado = new Double(0) ;
	}
	/**
	 * @return the nombreClaseInventario
	 */
	public String getnombreClaseInventario() {
		return nombreClaseInventario;
	}
	/**
	 * @param nombreClaseInventario the nombreClaseInventario to set
	 */
	public void setnombreClaseInventario(String nombreClaseInventario) {
		this.nombreClaseInventario = nombreClaseInventario;
	}
	/**
	 * @return the cantidadAutorizada
	 */
	public double getCantidadAutorizada() {
		return cantidadAutorizada;
	}
	/**
	 * @param cantidadAutorizada the cantidadAutorizada to set
	 */
	public void setCantidadAutorizada(double cantidadAutorizada) {
		this.cantidadAutorizada = cantidadAutorizada;
	}
	/**
	 * @return the valorAutorizado
	 */
	public double getValorAutorizado() {
		return valorAutorizado;
	}
	/**
	 * @param valorAutorizado the valorAutorizado to set
	 */
	public void setValorAutorizado(double valorAutorizado) {
		this.valorAutorizado = valorAutorizado;
	}
	/**
	 * @return the valorFacturado
	 */
	public double getValorFacturado() {
		return valorFacturado;
	}
	/**
	 * @param valorFacturado the valorFacturado to set
	 */
	public void setValorFacturado(double valorFacturado) {
		this.valorFacturado = valorFacturado;
	}

	
}