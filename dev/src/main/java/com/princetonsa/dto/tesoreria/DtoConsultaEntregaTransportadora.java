package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;


/**
 * 
 * Dto que permite construir la consulta de la entrega a transportadora
 * 
 * @author Diana Carolina G
 * @anexo 1042
 *
 */

public class DtoConsultaEntregaTransportadora implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int codigoCentroAtencion;
	private Long numeroEntrega;
	private String fechaGeneracionInicial;
	private String fechaGeneracionFinal;
	private int  consecutivoCaja;
	private String  loginCajero;
	private int codigoTransportadora;
	private int codigoEntidadFinanciera;
	private String numeroCuentaBancaria;
	private ArrayList<String> documentosAdjuntos;
	
	
	
	
	/**
	 * 
	 */
	
	public DtoConsultaEntregaTransportadora()
	{

		this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.numeroEntrega=ConstantesBD.codigoNuncaValidoLong;
		this.fechaGeneracionInicial="";
		this.fechaGeneracionFinal="";
		this.consecutivoCaja=ConstantesBD.codigoNuncaValido;
		this.loginCajero="";
		this.codigoTransportadora=ConstantesBD.codigoNuncaValido;
		this.codigoEntidadFinanciera=ConstantesBD.codigoNuncaValido;
		this.setNumeroCuentaBancaria("");
		this.setDocumentosAdjuntos(new ArrayList<String>());
		this.getDocumentosAdjuntos().add("");
		this.getDocumentosAdjuntos().add("");
		this.getDocumentosAdjuntos().add("");
		this.getDocumentosAdjuntos().add("");
		
		
	}



	



	/**
	 * @return the numeroEntrega
	 */
	public Long getNumeroEntrega() {
		return numeroEntrega;
	}



	/**
	 * @return the fechaGeneracionInicial
	 */
	public String getFechaGeneracionInicial() {
		return fechaGeneracionInicial;
	}



	/**
	 * @return the fechaGeneracionFinal
	 */
	public String getFechaGeneracionFinal() {
		return fechaGeneracionFinal;
	}



	/**
	 * @return the consecutivoCaja
	 */
	public int getConsecutivoCaja() {
		return consecutivoCaja;
	}



	/**
	 * @return the loginCajero
	 */
	public String getLoginCajero() {
		return loginCajero;
	}



	/**
	 * @return the codigoTransportadora
	 */
	public int getCodigoTransportadora() {
		return codigoTransportadora;
	}



	/**
	 * @return the codigoEntidadFinanciera
	 */
	public int getCodigoEntidadFinanciera() {
		return codigoEntidadFinanciera;
	}



	


	


	/**
	 * @param numeroEntrega the numeroEntrega to set
	 */
	public void setNumeroEntrega(Long numeroEntrega) {
		this.numeroEntrega = numeroEntrega;
	}



	/**
	 * @param fechaGeneracionInicial the fechaGeneracionInicial to set
	 */
	public void setFechaGeneracionInicial(String fechaGeneracionInicial) {
		this.fechaGeneracionInicial = fechaGeneracionInicial;
	}



	/**
	 * @param fechaGeneracionFinal the fechaGeneracionFinal to set
	 */
	public void setFechaGeneracionFinal(String fechaGeneracionFinal) {
		this.fechaGeneracionFinal = fechaGeneracionFinal;
	}



	/**
	 * @param consecutivoCaja the consecutivoCaja to set
	 */
	public void setConsecutivoCaja(int consecutivoCaja) {
		this.consecutivoCaja = consecutivoCaja;
	}



	/**
	 * @param loginCajero the loginCajero to set
	 */
	public void setLoginCajero(String loginCajero) {
		this.loginCajero = loginCajero;
	}



	/**
	 * @param codigoTransportadora the codigoTransportadora to set
	 */
	public void setCodigoTransportadora(int codigoTransportadora) {
		this.codigoTransportadora = codigoTransportadora;
	}



	/**
	 * @param codigoEntidadFinanciera the codigoEntidadFinanciera to set
	 */
	public void setCodigoEntidadFinanciera(int codigoEntidadFinanciera) {
		this.codigoEntidadFinanciera = codigoEntidadFinanciera;
	}



	
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}







	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}







	public void setNumeroCuentaBancaria(String numeroCuentaBancaria) {
		this.numeroCuentaBancaria = numeroCuentaBancaria;
	}







	public String getNumeroCuentaBancaria() {
		return numeroCuentaBancaria;
	}







	public void setDocumentosAdjuntos(ArrayList<String> documentosAdjuntos) {
		this.documentosAdjuntos = documentosAdjuntos;
	}







	public ArrayList<String> getDocumentosAdjuntos() {
		return documentosAdjuntos;
	}
	

	
	
	
	





	
	
	
}
