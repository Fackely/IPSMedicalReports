package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import util.ConstantesBD;

@SuppressWarnings("serial")
public class DtoValidacionTipoCobroPaciente implements Serializable
{

	/**
	 * 
	 */
	private String manejaMontos;

	/**
	 * 
	 */
	private String pacientePagaAtencion;

	/**
	 * 
	 */
	private String mostrarCalisificacion;

	/**
	 * 
	 */
	private String mostrarTipoAfiliado;

	/**
	 * 
	 */
	private String mostrarNaturalezaPaciente;

	/**
	 * 
	 */
	private String mostrarPorcentajeCobertura;

	/**
	 * 
	 */
	private String mostrarCuotaVefificacion;
	
	/**
	 * 
	 */
	private double porcentajeMontoCobro;
	
	/**
	 * 
	 */
	private String tipoCobroPaciente;

	public DtoValidacionTipoCobroPaciente() 
	{
		this.manejaMontos="";
		this.pacientePagaAtencion="";
		this.mostrarCalisificacion=ConstantesBD.acronimoSi;
		this.mostrarTipoAfiliado=ConstantesBD.acronimoSi;
		this.mostrarNaturalezaPaciente=ConstantesBD.acronimoSi;
		this.mostrarPorcentajeCobertura=ConstantesBD.acronimoSi;
		this.mostrarCuotaVefificacion=ConstantesBD.acronimoSi;
		this.porcentajeMontoCobro=0;
		this.tipoCobroPaciente="";
	}

	public String getManejaMontos() {
		return manejaMontos;
	}

	public void setManejaMontos(String manejaMontos) {
		this.manejaMontos = manejaMontos;
	}

	public String getPacientePagaAtencion() {
		return pacientePagaAtencion;
	}

	public void setPacientePagaAtencion(String pacientePagaAtencion) {
		this.pacientePagaAtencion = pacientePagaAtencion;
	}

	public String getMostrarCalisificacion() {
		return mostrarCalisificacion;
	}

	public void setMostrarCalisificacion(String mostrarCalisificacion) {
		this.mostrarCalisificacion = mostrarCalisificacion;
	}

	public String getMostrarTipoAfiliado() {
		return mostrarTipoAfiliado;
	}

	public void setMostrarTipoAfiliado(String mostrarTipoAfiliado) {
		this.mostrarTipoAfiliado = mostrarTipoAfiliado;
	}

	public String getMostrarNaturalezaPaciente() {
		return mostrarNaturalezaPaciente;
	}

	public void setMostrarNaturalezaPaciente(String mostrarNaturalezaPaciente) {
		this.mostrarNaturalezaPaciente = mostrarNaturalezaPaciente;
	}

	public String getMostrarPorcentajeCobertura() {
		return mostrarPorcentajeCobertura;
	}

	public void setMostrarPorcentajeCobertura(String mostrarPorcentajeCobertura) {
		this.mostrarPorcentajeCobertura = mostrarPorcentajeCobertura;
	}

	public String getMostrarCuotaVefificacion() {
		return mostrarCuotaVefificacion;
	}

	public void setMostrarCuotaVefificacion(String mostrarCuotaVefificacion) {
		this.mostrarCuotaVefificacion = mostrarCuotaVefificacion;
	}

	public double getPorcentajeMontoCobro() {
		return porcentajeMontoCobro;
	}

	public void setPorcentajeMontoCobro(double porcentajeMontoCobro) {
		this.porcentajeMontoCobro = porcentajeMontoCobro;
	}

	public String getTipoCobroPaciente() {
		return tipoCobroPaciente;
	}

	public void setTipoCobroPaciente(String tipoCobroPaciente) {
		this.tipoCobroPaciente = tipoCobroPaciente;
	}
	
	

	
	
}
