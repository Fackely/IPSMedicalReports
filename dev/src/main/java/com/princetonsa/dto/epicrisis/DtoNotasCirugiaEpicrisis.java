package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoNotasCirugiaEpicrisis implements Serializable 
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
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private String responsable;
	
	/**
	 * 
	 */
	private String descripcion;

	/**
	 * 
	 *
	 */
	public DtoNotasCirugiaEpicrisis() 
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.fecha="";
		this.hora="";
		this.responsable="";
		this.descripcion="";
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
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the responsable
	 */
	public String getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(String responsable) {
		this.responsable = responsable;
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
	
	
	
}
