/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoValoracionEnfermeria 
{
	/**
	 * 
	 */
	private int codigoHistoEnca;
	
	/**
	 * 
	 */
	private int codigoSeccion;
	
	/**
	 * 
	 */
	private String etiquietaSeccion;
	
	/**
	 * 
	 */
	private int ordenSeccion;
	
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
	private int ordenCampo;
	
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

	
	/**
	 * 
	 */
	public DtoValoracionEnfermeria()
	{
		this.codigoHistoEnca=ConstantesBD.codigoNuncaValido;
		this.codigoSeccion=ConstantesBD.codigoNuncaValido;
		this.etiquietaSeccion="";
		this.ordenSeccion=ConstantesBD.codigoNuncaValido;
		this.codigoCampo=ConstantesBD.codigoNuncaValido;
		this.etiquietaCampo="";
		this.ordenCampo=ConstantesBD.codigoNuncaValido;
		this.centroCosto=ConstantesBD.codigoNuncaValido;
		this.valor="";
		this.campoOtro=ConstantesBD.acronimoSi;
	}
	
	public int getCodigoHistoEnca() {
		return codigoHistoEnca;
	}

	public void setCodigoHistoEnca(int codigoHistoEnca) {
		this.codigoHistoEnca = codigoHistoEnca;
	}

	public int getCodigoSeccion() {
		return codigoSeccion;
	}

	public void setCodigoSeccion(int codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	public String getEtiquietaSeccion() {
		return etiquietaSeccion;
	}

	public void setEtiquietaSeccion(String etiquietaSeccion) {
		this.etiquietaSeccion = etiquietaSeccion;
	}

	public int getOrdenSeccion() {
		return ordenSeccion;
	}

	public void setOrdenSeccion(int ordenSeccion) {
		this.ordenSeccion = ordenSeccion;
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

	public int getOrdenCampo() {
		return ordenCampo;
	}

	public void setOrdenCampo(int ordenCampo) {
		this.ordenCampo = ordenCampo;
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
