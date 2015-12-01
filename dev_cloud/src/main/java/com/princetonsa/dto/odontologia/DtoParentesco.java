package com.princetonsa.dto.odontologia;

import java.io.Serializable;


public class DtoParentesco implements Serializable{
	
	
	/**
	 */
	private int codigoPk;
	private String descripcion;
	
	
	
	public DtoParentesco(){
		this.reset();
	}
	
	
	void reset(){
		codigoPk=0;
		descripcion="";
	}




	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}




	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}




	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}




	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
