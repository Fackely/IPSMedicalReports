package com.princetonsa.dto.odontologia;



import java.io.Serializable;

import util.ConstantesBD;





/**
 * DTO PARA MANEJAR LOS CONTRATOS A NIVEL DE INSTITUCION ODONTOLOGICA
 * @author axioma
 *
 */
public class DtoFirmasContOtrosInstOdont implements Serializable {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private long codigoPk;
	/**
	 * 
	 */
	private long codigoContrato;
	
	/**
	 * 
	 */
	private String labelDebajoFirma;
	/**
	 * 
	 */
	private String firmaDigital;
	/**
	 * 
	 */
	private String adjuntoFirma;
	/**
	 * 
	 */
	private int numero;
	/**
	 * 
	 */
	private boolean activo;
	
	
	
	
	
	/**
	 * 
	 */
	public DtoFirmasContOtrosInstOdont()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.codigoContrato=ConstantesBD.codigoNuncaValidoLong;
		this.firmaDigital=ConstantesBD.acronimoNo;
		this.labelDebajoFirma="" ;
		this.adjuntoFirma="";
		this.numero=ConstantesBD.codigoNuncaValido;
		this.setActivo(Boolean.TRUE);
	}
	
	/**
	 * @return the codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}
	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * @return the codigoContrato
	 */
	public long getCodigoContrato() {
		return codigoContrato;
	}
	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	
	/**
	 * @return the labelDebajoFirma
	 */
	public String getLabelDebajoFirma() {
		return labelDebajoFirma;
	}
	/**
	 * @param labelDebajoFirma the labelDebajoFirma to set
	 */
	public void setLabelDebajoFirma(String labelDebajoFirma) {
		this.labelDebajoFirma = labelDebajoFirma;
	}
	/**
	 * @return the firmaDigital
	 */
	public String getFirmaDigital() {
		return firmaDigital;
	}
	/**
	 * @param firmaDigital the firmaDigital to set
	 */
	public void setFirmaDigital(String firmaDigital) {
		this.firmaDigital = firmaDigital;
	}
	/**
	 * @return the adjuntoFirma
	 */
	public String getAdjuntoFirma() {
		return adjuntoFirma;
	}
	/**
	 * @param adjuntoFirma the adjuntoFirma to set
	 */
	public void setAdjuntoFirma(String adjuntoFirma) {
		this.adjuntoFirma = adjuntoFirma;
	}
	
	
	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isActivo() {
		return activo;
	}
	
	
	
	


}
