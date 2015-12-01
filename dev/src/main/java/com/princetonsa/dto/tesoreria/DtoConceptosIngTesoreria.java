package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoConceptosIngTesoreria implements Serializable 
{
	private double codigo;
	
	private String descripcion;
	
	/**
	 * Constructor
	 */
	
	public void reset()
	{	
		this.codigo=ConstantesBD.codigoNuncaValidoDouble;
		this.descripcion="";
	}

	public double getCodigo() {
		return codigo;
	}

	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}