package com.princetonsa.dto.salas;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoColor implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private String hexadecimal;

	/**
	 * 
	 * @param codigo
	 * @param nombre
	 * @param hexadecimal
	 */
	public DtoColor(int codigo, String nombre, String hexadecimal) 
	{
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.hexadecimal = hexadecimal;
	}

	/**
	 * 
	 *
	 */
	public DtoColor() 
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.hexadecimal = "";
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
	 * @return the hexadecimal
	 */
	public String getHexadecimal() {
		return hexadecimal;
	}

	/**
	 * @param hexadecimal the hexadecimal to set
	 */
	public void setHexadecimal(String hexadecimal) {
		this.hexadecimal = hexadecimal;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
