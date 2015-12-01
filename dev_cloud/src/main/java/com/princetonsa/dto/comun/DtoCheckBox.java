package com.princetonsa.dto.comun;

import java.io.Serializable;

/**
 * Clase generíca que se puede usar como checkbox y cargarlo de opciones o como un dto 
 *  que contiene información básica sobre una entidad.
 * @author Cristhian Murillo
 */
public class DtoCheckBox implements Serializable 
{

	private static final long serialVersionUID = 1L;
	private String codigo;
	private String nombre;
	private boolean check;
	private String descripcion;
	
	/**
	 * Constructor de la clase
	 */
	public DtoCheckBox() {
		this.codigo			= "";
		this.nombre			= "";
		this.check			= false;
		this.descripcion 	= "";
	}
	
	
	public DtoCheckBox(String nombre, boolean checkInicial) 
	{
		this.codigo			= "";
		this.nombre			= nombre;
		this.check			= checkInicial;
		this.descripcion 	= "";
	}
	
	/**
	 * Constructor para mapear la consulta de Info Cuenta por Ingreso
	 * 
	 * @param codigoTipoPaciente
	 * @param estadoCuenta
	 */
	public DtoCheckBox(String codigoTipoPaciente, Integer estadoCuenta) 
	{
		this.codigo			= codigoTipoPaciente;
		this.nombre			= estadoCuenta.toString();
		
	}


	public String getCodigo() {
		return codigo;
	}


	public String getNombre() {
		return nombre;
	}


	public boolean isCheck() {
		return check;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void setCheck(boolean check) {
		this.check = check;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
