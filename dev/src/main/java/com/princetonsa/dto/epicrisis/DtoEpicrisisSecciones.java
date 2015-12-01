package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoEpicrisisSecciones implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private double codigo;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 */
	private int evolucion;
	
	/**
	 * 
	 */
	private int ingreso;
	
	/**
	 * 
	 */
	private int plantillaSecFija;
	
	/**
	 * 
	 */
	private double funParamSeccionFija;
	
	/**
	 * 
	 */
	private int funParam;
	
	/**
	 * 
	 */
	private String usuario;

	/**
	 * 
	 *
	 */
	public DtoEpicrisisSecciones() 
	{
		this.codigo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.evolucion = ConstantesBD.codigoNuncaValido;
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.plantillaSecFija = ConstantesBD.codigoNuncaValido;
		this.funParamSeccionFija=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.funParam = ConstantesBD.codigoNuncaValido;
		this.usuario = "";
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
	 * @return the evolucion
	 */
	public int getEvolucion() {
		return evolucion;
	}

	/**
	 * @param evolucion the evolucion to set
	 */
	public void setEvolucion(int evolucion) {
		this.evolucion = evolucion;
	}

	/**
	 * @return the funParam
	 */
	public int getFunParam() {
		return funParam;
	}

	/**
	 * @param funParam the funParam to set
	 */
	public void setFunParam(int funParam) {
		this.funParam = funParam;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the plantillaSecFija
	 */
	public int getPlantillaSecFija() {
		return plantillaSecFija;
	}

	/**
	 * @param plantillaSecFija the plantillaSecFija to set
	 */
	public void setPlantillaSecFija(int plantillaSecFija) {
		this.plantillaSecFija = plantillaSecFija;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the funParamSeccionFija
	 */
	public double getFunParamSeccionFija() {
		return funParamSeccionFija;
	}

	/**
	 * @param funParamSeccionFija the funParamSeccionFija to set
	 */
	public void setFunParamSeccionFija(double funParamSeccionFija) {
		this.funParamSeccionFija = funParamSeccionFija;
	}
	
	
	
}
