package com.princetonsa.dto.odontologia;

import java.util.ArrayList;

public class DtoProgramasPaqueteOdonto 
{
	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private int codigoPkPaquete;
	
	/**
	 * 
	 */
	private DtoPrograma programa;	
	
	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * 
	 */
	private boolean existeBD;
	
	
	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoPkPaquete() {
		return codigoPkPaquete;
	}

	public void setCodigoPkPaquete(int codigoPkPaquete) {
		this.codigoPkPaquete = codigoPkPaquete;
	}

	public DtoPrograma getPrograma() {
		return programa;
	}

	public void setPrograma(DtoPrograma programa) {
		this.programa = programa;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public boolean isExisteBD() {
		return existeBD;
	}

	public void setExisteBD(boolean existeBD) {
		this.existeBD = existeBD;
	}

	
	
}
