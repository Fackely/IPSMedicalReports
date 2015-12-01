package com.princetonsa.dto.cargos;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class DtoDetalleCargoArticuloConsumo implements Serializable 
{
	private double codigo;
	private double detalleCargo;
	private int codigoArticulo;
	private int cantidad;
	private double valorUnitario;
	private double valorTotal;
	private double porcentaje;
	
	/**
	 * 
	 * @param codigo
	 * @param detalleCargo
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valorUnitario
	 * @param valorTotal
	 * @param porcentaje
	 */
	public DtoDetalleCargoArticuloConsumo(double codigo, double detalleCargo,
			int codigoArticulo, int cantidad, double valorUnitario,
			double valorTotal, double porcentaje) {
		super();
		this.codigo = codigo;
		this.detalleCargo = detalleCargo;
		this.codigoArticulo = codigoArticulo;
		this.cantidad = cantidad;
		this.valorUnitario = valorUnitario;
		this.valorTotal = valorTotal;
		this.porcentaje = porcentaje;
	}
	
	/**
	 * 
	 * @param codigo
	 * @param detalleCargo
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valorUnitario
	 * @param valorTotal
	 * @param porcentaje
	 */
	public DtoDetalleCargoArticuloConsumo() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.detalleCargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.codigoArticulo = ConstantesBD.codigoNuncaValido;
		this.cantidad = ConstantesBD.codigoNuncaValido;
		this.valorUnitario = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorTotal = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.porcentaje = ConstantesBD.codigoNuncaValidoDoubleNegativo;
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
	 * @return the detalleCargo
	 */
	public double getDetalleCargo() {
		return detalleCargo;
	}
	/**
	 * @param detalleCargo the detalleCargo to set
	 */
	public void setDetalleCargo(double detalleCargo) {
		this.detalleCargo = detalleCargo;
	}
	/**
	 * @return the codigoArticulo
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
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
	/**
	 * @return the porcentaje
	 */
	public double getPorcentaje() {
		return porcentaje;
	}
	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	
}
