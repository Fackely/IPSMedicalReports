package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoPiezaDental implements Serializable , Cloneable {

	private double codigo;
	private String descripcion;
	private String edadInicioNinez;
	private String edadFinalNinez;
	
	
	
	/**
	 * 
	 * @param codigo
	 * @param descripcion
	 * @param edadInicioNinez
	 * @param edadFinalNinez
	 */
	 public DtoPiezaDental(double codigo, String descripcion,
			String edadInicioNinez, String edadFinalNinez)
	{
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.edadInicioNinez = edadInicioNinez;
		this.edadFinalNinez = edadFinalNinez;
	}

	 /**
	  * 
	  */
	public DtoPiezaDental() {
		
		 this.reset();
	}
	 
	 public void reset(){
		 this.codigo = ConstantesBD.codigoNuncaValido;
		 this.descripcion = "";
		 this.edadInicioNinez = ConstantesBD.acronimoNo;
		 this.edadFinalNinez = ConstantesBD.acronimoNo;
	 }

	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
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
	 * @return the edadInicioNinez
	 */
	public String getEdadInicioNinez() {
		return edadInicioNinez;
	}

	/**
	 * @param edadInicioNinez the edadInicioNinez to set
	 */
	public void setEdadInicioNinez(String edadInicioNinez) {
		this.edadInicioNinez = edadInicioNinez;
	}

	/**
	 * @return the edadFinalNinez
	 */
	public String getEdadFinalNinez() {
		return edadFinalNinez;
	}

	/**
	 * @param edadFinalNinez the edadFinalNinez to set
	 */
	public void setEdadFinalNinez(String edadFinalNinez) {
		this.edadFinalNinez = edadFinalNinez;
	}
}
