package com.princetonsa.dto.odontologia;

import java.io.Serializable;

public class DtoPaqueteReport implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DtoPaqueteReport(){}
	
	private int codigoPkPaquete;
	/**
	 * Atributo que almacena el cdigo del paquete
	 */
	private String codigoPaquete;

	public int getCodigoPkPaquete() {
		return codigoPkPaquete;
	}
	public void setCodigoPkPaquete(int codigoPkPaquete) {
		this.codigoPkPaquete = codigoPkPaquete;
	}
	public String getCodigoPaquete() {
		return codigoPaquete;
	}
	public void setCodigoPaquete(String codigoPaquete) {
		this.codigoPaquete = codigoPaquete;
	}
	



}
