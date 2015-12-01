/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dto.manejoPaciente
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoRequsitosPaciente implements Serializable
{
	
	private String subCuenta;
	
	/**
	 * Código del requisito
	 */
	private int codigo;
	
	/**
	 * Descripcion del requisito
	 */
	private String descripcion;
	
	/**
	 * tipo de requisito
	 */
	private String tipo;
	
	/**
	 * Para saber si es cumplido o no
	 */
	private boolean cumplido;
	
	/**
	 * Bandera, que indica si el requisito ya fue asignado a la subcuenta.
	 */
	private boolean asignado;
	
	
	/**
	 * 
	 */
	public DtoRequsitosPaciente() 
	{
		this.subCuenta = "";
		this.codigo = 0;
		this.tipo = "";
		this.descripcion = "";
		this.cumplido = false;
		this.asignado=false;
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
	 * @return the cumplido
	 */
	public boolean isCumplido() {
		return cumplido;
	}


	/**
	 * @param cumplido the cumplido to set
	 */
	public void setCumplido(boolean cumplido) {
		this.cumplido = cumplido;
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


	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}


	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}


	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}


	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	/**
	 * @return the asignado
	 */
	public boolean isAsignado() {
		return asignado;
	}


	/**
	 * @param asignado the asignado to set
	 */
	public void setAsignado(boolean asignado) {
		this.asignado = asignado;
	}

}
