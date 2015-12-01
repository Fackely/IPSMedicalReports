package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import util.ValoresPorDefecto;

/**
 * Esta clase reune toda la informaci&oacute;n necesaria para mostrar los totales parciales
 * de Traslados, Entregas a Transportadora de valores y a Caja mayor por forma de pago.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @anexo 226
 */
public class DtoTotalesParciales  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Tipo de Movimiento con el que se relaciona la informaci&oacute;n
	 */
	private int tipo;
	
	/**
	 * Descripci&oacute;n de la forma de pago
	 */
	private String formaPago;
	
	/**
	 * Tipo de la forma de pago asociada
	 */
	private int tipoFormaPago;
	
	/**
	 * Valor Total Entregado por la forma de Pago.
	 */
	private double total;
	
	/**
	 * Valor del total Trasladado por la forma de pago
	 */
	private double totalTraslado;
	
	/**
	 * Valor del total faltante por forma de pago
	 */
	private double totalFaltante;
	
	/**
	 * Indicativo del tipo de detalle de la forma de pago
	 */
	private int tipoDetalleFormaPago;
	
	/**
	 * En el caso de ser un faltante sobrante define el tipo de la diferencia (Faltante/Sobrante)
	 */
	private String tipoDiferencia;
	
	
	/**
	 * Constructor vac&iacute;o, requerido para hacer la proyecci&oacute;n del ORM
	 */
	public DtoTotalesParciales() {
		
	}

	/*
	 * tipo = 0 ---> Traslados a Cajas de Recaudo
	 * tipo = 1 ---> Entregas a Transportadora de valores
	 * tipo = 2 ---> Entregas a Caja Mayor Principal
	 * tipo = 3 ---> Faltantes Sobrantes
	 */
	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setTipoFormaPago(int tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}

	public int getTipoFormaPago() {
		return tipoFormaPago;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getTotal() {
		return total;
	}

	public double getTotalTraslado() {
		return totalTraslado;
	}

	public void setTotalTraslado(double totalTraslado) {
		this.totalTraslado = totalTraslado;
	}

	public double getTotalFaltante() {
		return totalFaltante;
	}

	public void setTotalFaltante(double totalFaltante) {
		this.totalFaltante = totalFaltante;
	}
	
	/*
	 * campos unicos de Faltante Sobrante
	 * tipoDiferencia = DF ---> Diferencia Faltante
	 * tipoDiferencia = DS ---> Diferencia Sobrante
	 */
	public String getTipoDiferencia() {
		return tipoDiferencia;
	}

	public void setTipoDiferencia(String tipoDiferencia) {
		this.tipoDiferencia = tipoDiferencia;
	}
	
	public String getDescripcionTipoDiferencia() {
		return ValoresPorDefecto.getIntegridadDominio(tipoDiferencia).toString();
	}
	//-----------------------------------------------
	
	/**
	 * @return the tipoDetalleFormaPago
	 */
	public int getTipoDetalleFormaPago() {
		return tipoDetalleFormaPago;
	}

	/**
	 * @param tipoDetalleFormaPago the tipoDetalleFormaPago to set
	 */
	public void setTipoDetalleFormaPago(int tipoDetalleFormaPago) {
		this.tipoDetalleFormaPago = tipoDetalleFormaPago;
	}

}

