package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class DtoProgramaReport implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	public DtoProgramaReport(){}
	
	private long codigoPkPrograma;
	
	/**
	 * Atributo que almacena el cdigo del programa
	 */
	private String codigoPrograma;
	
	
	

	public long getCodigoPkPrograma() {
		return codigoPkPrograma;
	}

	public void setCodigoPkPrograma(Long codigoPkPrograma) {
		if(codigoPkPrograma!=null){
			this.codigoPkPrograma = codigoPkPrograma;
		}else
			this.codigoPkPrograma=ConstantesBD.codigoNuncaValidoLong;
	}

	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
	
	

}
