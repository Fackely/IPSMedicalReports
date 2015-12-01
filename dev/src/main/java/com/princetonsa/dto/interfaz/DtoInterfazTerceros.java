package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoInterfazTerceros implements Serializable{
	
	/**
	 * Codigo del tercero en la tabla terceros de axioma
	 */
	private String codigo;
	
	/**
	 * Numero de identificacion del tercero
	 */
	private String numero_identificacion;
	
	/**
	 * Descripcion del tercero
	 */
	private String descripcion;
	
	/**
	 * Codigo de institucion del tercero
	 */
	private int institucion;
	
	/**
	 * Indicador de estado de terceros
	 */
	private String activo;
	
	/**
	 * Inicializacion de los campos
	 *
	 */
	public DtoInterfazTerceros()
	{
		this.codigo="";
		this.numero_identificacion="";
		this.descripcion="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.activo="";
	}
	
	/**
	 * 
	 * @param codigo
	 * @param numero_identificacion
	 * @param descripcion
	 * @param institucion
	 * @param activo
	 */
	public DtoInterfazTerceros(String codigo,String numero_identificacion,String descripcion,int institucion,String activo)
	{
		this.codigo=codigo;
		this.numero_identificacion=numero_identificacion;
		this.descripcion=descripcion;
		this.institucion=institucion;
		this.activo=activo;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getNumero_identificacion() {
		return numero_identificacion;
	}

	public void setNumero_identificacion(String numero_identificacion) {
		this.numero_identificacion = numero_identificacion;
	}

}
