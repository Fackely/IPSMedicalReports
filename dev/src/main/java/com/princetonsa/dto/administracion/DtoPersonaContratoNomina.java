package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author Edgar Carvajal
 *
 */
public class DtoPersonaContratoNomina implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6174899374194550291L;
	
	
	private long codigoPk;
	private int codigoPkPersona;
	private long contratoNomina;
	
	
	

	/**
	 * 
	 */
	public  DtoPersonaContratoNomina(){
		this.codigoPk=ConstantesBD.codigoNuncaValidoLong;
		this.codigoPkPersona=ConstantesBD.codigoNuncaValido;
		this.contratoNomina=ConstantesBD.codigoNuncaValidoLong;
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
	 * @return the codigoPkPersona
	 */
	public int getCodigoPkPersona() {
		return codigoPkPersona;
	}
	/**
	 * @param codigoPkPersona the codigoPkPersona to set
	 */
	public void setCodigoPkPersona(int codigoPkPersona) {
		this.codigoPkPersona = codigoPkPersona;
	}
	/**
	 * @return the contratoNomina
	 */
	public long getContratoNomina() {
		return contratoNomina;
	}
	/**
	 * @param contratoNomina the contratoNomina to set
	 */
	public void setContratoNomina(long contratoNomina) {
		this.contratoNomina = contratoNomina;
	} 
	
	

	
	
	

}
