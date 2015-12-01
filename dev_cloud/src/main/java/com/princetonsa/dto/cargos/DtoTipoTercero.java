package com.princetonsa.dto.cargos;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoTipoTercero implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4097951411096885123L;
	
	private int codigo;
	private String  descripcion ;
	
	
	/**
	 * 
	 */
	public DtoTipoTercero(){
		this.codigo= ConstantesBD.codigoNuncaValido;
		this.descripcion="";
	}
	
	/**
	 * 
	 * @param codigo
	 * @param descripcion
	 */
	public DtoTipoTercero(int codigo, String descripcion){
		this.codigo= codigo;
		this.descripcion=descripcion;
	}
	
	
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	
}
