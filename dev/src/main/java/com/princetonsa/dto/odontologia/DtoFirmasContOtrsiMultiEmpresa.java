package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import util.ConstantesBD;



/**
 * 
 * @author axioma
 *
 */
public class DtoFirmasContOtrsiMultiEmpresa implements Serializable 
{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long codigoPk;
	private int numero;
	private String labelDebajoFirma;
	private String firmaDigital;
	private String adjuntoFirma;
	private boolean activo;
	
	
	
	
	/**
	 *	CONSTRUTOR 
	 */
	public DtoFirmasContOtrsiMultiEmpresa()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.numero=ConstantesBD.codigoNuncaValido;
		this.labelDebajoFirma="";
		this.firmaDigital="";
		this.adjuntoFirma="";
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



	public void setActivo(boolean activo) {
		this.activo = activo;
	}



	public boolean isActivo() {
		return activo;
	}
	
	
	
	

	
	
}
