package com.princetonsa.dto.salas;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoTiempos implements Serializable 
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
	private int orden;
	
	/**
	 * 
	 */
	private boolean obligatorio;

	/**
	 * 
	 * @param codigo
	 * @param nombre
	 * @param orden
	 * @param obligatorio
	 */
	public DtoTiempos(int codigo, String nombre, int orden, boolean obligatorio) 
	{
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.orden = orden;
		this.obligatorio = obligatorio;
	}

	
	/**
	 * 
	 *
	 */
	public DtoTiempos() 
	{
		super();
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.orden = ConstantesBD.codigoNuncaValido;
		this.obligatorio = false;
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

	/**
	 * @return the obligatorio
	 */
	public boolean getObligatorio() {
		return obligatorio;
	}

	/**
	 * @param obligatorio the obligatorio to set
	 */
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	
	
}
