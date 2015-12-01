package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

public class DtoInfoTarifaConvenio implements Serializable , Cloneable{
	
	
	private BigDecimal valor;
	private int codigoConvenio;
	private String nombreConvenio;
	
	
	public DtoInfoTarifaConvenio() {
		
		valor = new BigDecimal(0);
		codigoConvenio = ConstantesBD.codigoNuncaValido;
		nombreConvenio = "";
	}
	
	
	
	public DtoInfoTarifaConvenio(BigDecimal nValor , int nCodigo , String nombre) {
     this.valor=valor;
     this.codigoConvenio = nCodigo;
     this.nombreConvenio = nombre;
	}



	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}



	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}



	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}



	/**
	 * @return the nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}



	/**
	 * @param nombreConvenio the nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}


	
	

}
