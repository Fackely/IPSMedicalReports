package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Esta clase reune toda la informaci&oacute;n necesaria para mostrar los faltantes y sobrantes de un movimiento
 * 
 * @author Cristhian Murillo
 */
public class DtoFaltanteSobrante  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena el n&uacute;mero que identifica
	 * el faltante sobrante generado.
	 */
	private long idDetalleFaltanteSobrante;
	
	/**
	 * Atributo que almacena el valor diferencia del faltante sobrante generado.
	 */
	private double valorDiferencia;
	
	/**
	 * Atributo que almacena el tipo de diferencia del faltante sobrante generado.
	 */
	private String tipoDiferencia;
	
	/**
	 * Descripci&ooacute;n de la forma de pago
	 */
	private String formaPago;
	
	/**
	 * Consecutivo de la forma de pago
	 */
	private int tipoFormaPago;
	
	
	/**
	 * Constructor vac&iacute;o, requerido para hacer la proyecci&oacute;n del ORM
	 */
	public DtoFaltanteSobrante() { }


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo idDetalleFaltanteSobrante
	 * 
	 * @return  Retorna la variable idDetalleFaltanteSobrante
	 */
	public long getIdDetalleFaltanteSobrante() {
		return idDetalleFaltanteSobrante;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo valorDiferencia
	 * 
	 * @return  Retorna la variable valorDiferencia
	 */
	public double getValorDiferencia() {
		return valorDiferencia;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo tipoDiferencia
	 * 
	 * @return  Retorna la variable tipoDiferencia
	 */
	public String getTipoDiferencia() {
		return tipoDiferencia;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo formaPago
	 * 
	 * @return  Retorna la variable formaPago
	 */
	public String getFormaPago() {
		return formaPago;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo tipoFormaPago
	 * 
	 * @return  Retorna la variable tipoFormaPago
	 */
	public int getTipoFormaPago() {
		return tipoFormaPago;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo idDetalleFaltanteSobrante
	 * 
	 * @param  valor para el atributo idDetalleFaltanteSobrante 
	 */
	public void setIdDetalleFaltanteSobrante(long idDetalleFaltanteSobrante) {
		this.idDetalleFaltanteSobrante = idDetalleFaltanteSobrante;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo valorDiferencia
	 * 
	 * @param  valor para el atributo valorDiferencia 
	 */
	public void setValorDiferencia(BigDecimal valorDiferencia) {
		this.valorDiferencia = valorDiferencia.doubleValue();
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo tipoDiferencia
	 * 
	 * @param  valor para el atributo tipoDiferencia 
	 */
	public void setTipoDiferencia(String tipoDiferencia) {
		this.tipoDiferencia = tipoDiferencia;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo formaPago
	 * 
	 * @param  valor para el atributo formaPago 
	 */
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo tipoFormaPago
	 * 
	 * @param  valor para el atributo tipoFormaPago 
	 */
	public void setTipoFormaPago(int tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}
}
