package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoVariablesIncorrectasenBD implements Serializable{
   
	/**
    * 
    */
	private int codigoPk;
	
	/**
	 * 
	 */
	private int codigoInformeInconsistencia;
	
    /**
     * 
     */
	private int tipoVariable;
	
	/**
	 * 
	 */
	private String descripcionVariable;
	
	
	/**
	 * 
	 */
	private String indicador;
	
	/**
	 * 
	 */
	private String valor;
	
	
	public DtoVariablesIncorrectasenBD()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.codigoInformeInconsistencia=ConstantesBD.codigoNuncaValido;
		this.tipoVariable=ConstantesBD.codigoNuncaValido;
		this.descripcionVariable=new String("");
		this.indicador=new String("");
		this.valor=new String("");
	}




	public int getCodigoPk() {
		return codigoPk;
	}




	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	public int getCodigoInformeInconsistencia() {
		return codigoInformeInconsistencia;
	}




	public void setCodigoInformeInconsistencia(int codigoInformeInconsistencia) {
		this.codigoInformeInconsistencia = codigoInformeInconsistencia;
	}




	public int getTipoVariable() {
		return tipoVariable;
	}




	public void setTipoVariable(int tipoVariable) {
		this.tipoVariable = tipoVariable;
	}




	public String getDescripcionVariable() {
		return descripcionVariable;
	}




	public void setDescripcionVariable(String descripcionVariable) {
		this.descripcionVariable = descripcionVariable;
	}




	public String getValor() {
		return valor;
	}




	public void setValor(String valor) {
		this.valor = valor;
	}

	
	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
    
	
	
	
}
