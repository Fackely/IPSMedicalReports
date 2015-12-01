/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoResultadoLaboratorio 
{
	
	/**
	 * 
	 */
	private int codigoHistoEnca;
	
	/**
	 * 
	 */
	private int codigoCampo;
	
	/**
	 * 
	 */
	private String etiquietaCampo;
	
	/**
	 * 
	 */
	private int orden;
	
	/**
	 * 
	 */
	private int centroCosto;
	
	/**
	 * 
	 */
	private String valor;
	
	/**
	 * 
	 */
	private String campoOtro;


	public DtoResultadoLaboratorio()
	{
		this.codigoHistoEnca=ConstantesBD.codigoNuncaValido;
		this.codigoCampo=ConstantesBD.codigoNuncaValido;
		this.etiquietaCampo="";
		this.orden=ConstantesBD.codigoNuncaValido;
		this.centroCosto=ConstantesBD.codigoNuncaValido;
		this.valor="";
		this.campoOtro="";

	}


	public int getCodigoHistoEnca() {
		return codigoHistoEnca;
	}


	public void setCodigoHistoEnca(int codigoHistoEnca) {
		this.codigoHistoEnca = codigoHistoEnca;
	}


	public int getCodigoCampo() {
		return codigoCampo;
	}


	public void setCodigoCampo(int codigoCampo) {
		this.codigoCampo = codigoCampo;
	}


	public String getEtiquietaCampo() {
		return etiquietaCampo;
	}


	public void setEtiquietaCampo(String etiquietaCampo) {
		this.etiquietaCampo = etiquietaCampo;
	}


	public int getOrden() {
		return orden;
	}


	public void setOrden(int orden) {
		this.orden = orden;
	}


	public int getCentroCosto() {
		return centroCosto;
	}


	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}


	public String getValor() {
		return valor;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}


	public String getCampoOtro() {
		return campoOtro;
	}


	public void setCampoOtro(String campoOtro) {
		this.campoOtro = campoOtro;
	}



	
	
	

}
