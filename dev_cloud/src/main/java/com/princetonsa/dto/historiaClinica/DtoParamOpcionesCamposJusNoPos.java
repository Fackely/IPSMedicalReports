package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoParamOpcionesCamposJusNoPos implements Serializable
{
	private String codigo;
	private String opcion;
	private String valor;
	private String mostrarSeccion;
	private String seleccionado;
	
	/**
	 * Constructor
	 */
	public DtoParamOpcionesCamposJusNoPos()
	{
		this.clean();
	}
	
	/**
	 * Resetea todas las variables del DTO
	 */
	public void clean()
	{
		this.codigo="";
		this.opcion="";
		this.valor=ConstantesBD.acronimoNo;
		this.mostrarSeccion="";
		this.seleccionado=ConstantesBD.acronimoNo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the opcion
	 */
	public String getOpcion() {
		return opcion;
	}

	/**
	 * @param opcion the opcion to set
	 */
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	/**
	 * @return the mostrarSeccion
	 */
	public String getMostrarSeccion() {
		return mostrarSeccion;
	}

	/**
	 * @param mostrarSeccion the mostrarSeccion to set
	 */
	public void setMostrarSeccion(String mostrarSeccion) {
		this.mostrarSeccion = mostrarSeccion;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the seleccionado
	 */
	public String getSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado the seleccionado to set
	 */
	public void setSeleccionado(String seleccionado) {
		this.seleccionado = seleccionado;
	}
	
	
}
