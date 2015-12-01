/**
 * 
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoClasificacionesTriage implements Serializable
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
	private String descripcion;
	
	/**
	 * 
	 */
	private String activo;

	
	/**
	 * 
	 */
	public DtoClasificacionesTriage()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.descripcion="";
		this.activo="";
	}


	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getActivo() {
		return activo;
	}


	public void setActivo(String activo) {
		this.activo = activo;
	}
}
