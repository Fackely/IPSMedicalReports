package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;


/**
 * Dto que contiene los datos de los tipos de consulta de un reporte
 * 
 * @version 1.0, Abr 19, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoTipoConsulta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9170528305776331708L;
	
	/**
	 * Atributo que representa el código del tipo de consulta a realizar 
	 */
	private int codigo;
	
	/**
	 * Atributo que representa la descripción del tipo de consulta a realizar
	 */
	private String descripcion;
	
	public DtoTipoConsulta(){
		
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
