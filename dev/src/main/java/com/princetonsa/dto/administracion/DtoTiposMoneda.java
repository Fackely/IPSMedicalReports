package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;



/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */
public class DtoTiposMoneda implements Serializable
{
	/*--------------------------------
	 *   ATRIBUTOS DE TIPO DE MONEDA
	 ---------------------------------*/
	
	/**
	 * consecutivo asignado automaticamente
	 */
	private int codigo;
	
	/**
	 * Codigo digitado por el usuario
	 */
	private String codigoTipoMoneda;
	
	/**
	 * Descripcion digitada por el usuario
	 */
	private String descripcion;
	
	/**
	 * institucion
	 */
	private int institucion;
	
	/**
	 * loguin del usuario
	 */
	private String loginUsuario;
	
	/**
	 * 
	 */
	private String simbolo;

	/*--------------------------------
	 * FIN ATRIBUTOS DE TIPO DE MONEDA
	 ---------------------------------*/
	
	/*--------------------------------
	 *   METODOS GETTERS AND SETTERS
	 ---------------------------------*/
	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getCodigoTipoMoneda() {
		return codigoTipoMoneda;
	}

	public void setCodigoTipoMoneda(String codigoTipoMoneda) {
		this.codigoTipoMoneda = codigoTipoMoneda;
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

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	
	
	/*--------------------------------
	 *  FIN METODOS GETTERS AND SETTERS
	 ---------------------------------*/
	
	/*--------------------------------
	 *   			METODOS 
	 ---------------------------------*/
	
	/**
	 *Costructor del Dto de Tipos de Moneda. 
	 */
	public DtoTiposMoneda (int codigo,String codigoTipoMoneda,String descripcion, int institucion,String loginUsuario)
	{
		super();
		this.codigo = codigo;
		this.codigoTipoMoneda = codigoTipoMoneda;
		this.descripcion = descripcion;
		this.institucion = institucion;
		this.loginUsuario = loginUsuario;
		
	}

	
	/**
	 * constructor vacio
	 *
	 */
	public DtoTiposMoneda() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.codigoTipoMoneda = "";
		this.descripcion = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.loginUsuario = "";
		this.simbolo= "";
	}

	/**
	 * @return the simbolo
	 */
	public String getSimbolo() {
		return simbolo;
	}

	/**
	 * @param simbolo the simbolo to set
	 */
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	

	
	/*--------------------------------
	 *   		FIN	METODOS 
	 ---------------------------------*/
	
}