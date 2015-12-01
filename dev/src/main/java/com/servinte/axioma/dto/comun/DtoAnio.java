package com.servinte.axioma.dto.comun;

import java.io.Serializable;

/**
 * Dto que contiene los datos de un A�o espec�fico
 * 
 * @version 1.0, Abr 19, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoAnio implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1035233373999291137L;

	/**
	 * Atributo que representa el c�digo de un a�o
	 */
	private int codigo;
	
	/**
	 * Atributo que representa la descripci�n del a�o
	 */
	private String descripcion;

	public DtoAnio(){
		
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param despcripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
}
