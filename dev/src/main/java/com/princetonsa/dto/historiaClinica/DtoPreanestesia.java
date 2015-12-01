package com.princetonsa.dto.historiaClinica;
  
import java.io.Serializable;

import util.ConstantesBD;

public class DtoPreanestesia implements Serializable
{
	/**
	 * 
	 * */
	private String existeInfoExamenFisico;
	
	/**
	 * 
	 * */
	private String existeInfoExamenLaboratorio;
	
	/**
	 * 
	 * */
	private String existeInfoConclusiones;
	
	/**
	 * 
	 * */
	private String existeInfoObservGenerales;
	
	
	public DtoPreanestesia()
	{
		this.existeInfoExamenFisico = ConstantesBD.acronimoNo;
		this.existeInfoExamenLaboratorio = ConstantesBD.acronimoNo;
		this.existeInfoConclusiones = ConstantesBD.acronimoNo;
		this.existeInfoObservGenerales = ConstantesBD.acronimoNo;
	}

	public String getExisteInfoExamenFisico() {
		return existeInfoExamenFisico;
	}

	public void setExisteInfoExamenFisico(String existeInfoExamenFisico) {
		this.existeInfoExamenFisico = existeInfoExamenFisico;
	}

	public String getExisteInfoExamenLaboratorio() {
		return existeInfoExamenLaboratorio;
	}

	public void setExisteInfoExamenLaboratorio(String existeInfoExamenLaboratorio) {
		this.existeInfoExamenLaboratorio = existeInfoExamenLaboratorio;
	}

	public String getExisteInfoConclusiones() {
		return existeInfoConclusiones;
	}

	public void setExisteInfoConclusiones(String existeInfoConclusiones) {
		this.existeInfoConclusiones = existeInfoConclusiones;
	}

	public String getExisteInfoObservGenerales() {
		return existeInfoObservGenerales;
	}

	public void setExisteInfoObservGenerales(String existeInfoObservGenerales) {
		this.existeInfoObservGenerales = existeInfoObservGenerales;
	}
}