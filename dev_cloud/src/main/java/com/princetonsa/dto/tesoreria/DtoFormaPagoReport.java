package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadTexto;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoFormaPagoReport implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Llave primaria
	 */
	private int consecutivo;
	
	/**
	 * Descripci&oacute;n de la forma de pago
	 */
	private String descripcion;
	
	/**
	 * Valor de la forma de pago
	 */
	private double valor;
	
	private String valorFormateado;
	
	   /** Objeto jasper para el subreporte del consolidado por forma de pago **/
  //  transient private JRDataSource dsListadoFormasPago;

	
    public DtoFormaPagoReport(){
    	this.consecutivo=ConstantesBD.codigoNuncaValido;
    	this.descripcion="";
    	this.valor=ConstantesBD.codigoNuncaValidoDouble;
		this.valorFormateado = "";
		
	}
    
    
	public int getConsecutivo() {
		return consecutivo;
	}

	
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public void setValor(double valor) {
		this.valor = valor;
	}


	public double getValor() {
		return valor;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  valorFormateado
	 *
	 * @return retorna la variable valorFormateado
	 */
	public String getValorFormateado() {
		this.valorFormateado = UtilidadTexto.formatearValores(this.valor);
		return valorFormateado;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo valorFormateado
	 * @param valorFormateado es el valor para el atributo valorFormateado 
	 */
	public void setValorFormateado(String valorFormateado) {
		this.valorFormateado = valorFormateado;
	}

/*
	public void setDsListadoFormasPago(JRDataSource dsListadoFormasPago) {
		this.dsListadoFormasPago = dsListadoFormasPago;
	}


	public JRDataSource getDsListadoFormasPago() {
		return dsListadoFormasPago;
	} */


}
