package com.servinte.axioma.dto.comun;

import java.io.Serializable;


/**
 * Dto que contiene los datos de un Mes específico
 * 
 * @version 1.0, Abr 19, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoMes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8452634919978292731L;

	/**
	 * Atributo que representa el código del Mes
	 */
	private int codigo;
	
	/**
	 *  Atributo que representa la descripción de Mes 
	 */
	private String descripcion;
	
	public DtoMes(){
		
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
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	
	
}
