/**
 * 
 */
package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

/**
 * Encaps&uacute;la informaci&oacute;n para mostrar las solicitudes pendientes
 * @author Cristhian Murillo
 */
@SuppressWarnings("serial")
public class DtoDetalleFormaPago implements Serializable{
	
	/**
	 * Nombre de quien se recibe
	 */
	private String recibidoDeNombre;
	
	/**
	 * Nombre de quien se recibe
	 */
	private String recibidoDeApellido;
	
	/**
	 * Numero del documento entregado
	 */
	private String nroDocEntregado;
	
	/**
	 * Numero del documento recibido
	 */
	private String nroDocRecibido;
	
	/**
	 * Valor entregado
	 */
	private double valorentregado;
	
	/**
	 * Valor recibido
	 */
	private double valorRecibido;
	
	/**
	 * Diferencia encontrada entre el valor entregado y el recibido
	 */
	private double diferencia;
	
	/**
	 * Porma de pago registrada
	 */
	private String formaPago;
	
	
	/**
	 * Constructor
	 */
	public DtoDetalleFormaPago(){}


	/**
	 * @return the recibidoDeNombre
	 */
	public String getRecibidoDeNombre() {
		return recibidoDeNombre;
	}


	/**
	 * @param recibidoDeNombre the recibidoDeNombre to set
	 */
	public void setRecibidoDeNombre(String recibidoDeNombre) {
		this.recibidoDeNombre = recibidoDeNombre;
	}


	/**
	 * @return the recibidoDeApellido
	 */
	public String getRecibidoDeApellido() {
		return recibidoDeApellido;
	}


	/**
	 * @param recibidoDeApellido the recibidoDeApellido to set
	 */
	public void setRecibidoDeApellido(String recibidoDeApellido) {
		this.recibidoDeApellido = recibidoDeApellido;
	}


	/**
	 * @return the nroDocEntregado
	 */
	public String getNroDocEntregado() {
		return nroDocEntregado;
	}


	/**
	 * @param nroDocEntregado the nroDocEntregado to set
	 */
	public void setNroDocEntregado(String nroDocEntregado) {
		this.nroDocEntregado = nroDocEntregado;
	}


	/**
	 * @return the nroDocRecibido
	 */
	public String getNroDocRecibido() {
		return nroDocRecibido;
	}


	/**
	 * @param nroDocRecibido the nroDocRecibido to set
	 */
	public void setNroDocRecibido(String nroDocRecibido) {
		this.nroDocRecibido = nroDocRecibido;
	}


	/**
	 * @return the valorentregado
	 */
	public double getValorentregado() {
		return valorentregado;
	}


	/**
	 * @param valorentregado the valorentregado to set
	 */
	public void setValorentregado(double valorentregado) {
		this.valorentregado = valorentregado;
	}


	/**
	 * @return the valorRecibido
	 */
	public double getValorRecibido() {
		return valorRecibido;
	}


	/**
	 * @param valorRecibido the valorRecibido to set
	 */
	public void setValorRecibido(double valorRecibido) {
		this.valorRecibido = valorRecibido;
	}


	/**
	 * @return the diferencia
	 */
	public double getDiferencia() {
		return diferencia;
	}


	/**
	 * @param diferencia the diferencia to set
	 */
	public void setDiferencia(double diferencia) {
		this.diferencia = diferencia;
	}


	/**
	 * @return the formaPago
	 */
	public String getFormaPago() {
		return formaPago;
	}


	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	
}
