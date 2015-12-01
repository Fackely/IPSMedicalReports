/*
 * JUnio 24, 2009
 */
package com.princetonsa.dto.facturacion;

import java.io.Serializable;


/**
 * 
 * DTO que maneja la estructura de los contratos de las entidades subcontratadas
 * @author Sebastián Gómez R
 *
 */
public class DtoContratoEntidadSub implements Serializable
{
	private String consecutivo;
	private String numeroContrato;
	private String fechaInicial;
	private String fechaFinal;
	private DtoEntidadSubcontratada entidad;
	private String valorContrato;
	private String fechaFirmaContrato;
	private String observaciones;
	private String tipoTarifa;
	
	
	/*
	 * Reset
	 */
	public void clean()
	{
		this.consecutivo = "";
		this.numeroContrato = "";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.entidad = new DtoEntidadSubcontratada();
		this.valorContrato = "";
		this.fechaFirmaContrato = "";
		this.observaciones = "";
		this.tipoTarifa="";
	}

	public DtoContratoEntidadSub()
	{
		this.clean();
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}


	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}


	/**
	 * @return the numeroContrato
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}


	/**
	 * @param numeroContrato the numeroContrato to set
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}


	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * @return the entidad
	 */
	public DtoEntidadSubcontratada getEntidad() {
		return entidad;
	}


	/**
	 * @param entidad the entidad to set
	 */
	public void setEntidad(DtoEntidadSubcontratada entidad) {
		this.entidad = entidad;
	}


	/**
	 * @return the valorContrato
	 */
	public String getValorContrato() {
		return valorContrato;
	}


	/**
	 * @param valorContrato the valorContrato to set
	 */
	public void setValorContrato(String valorContrato) {
		this.valorContrato = valorContrato;
	}


	/**
	 * @return the fechaFirmaContrato
	 */
	public String getFechaFirmaContrato() {
		return fechaFirmaContrato;
	}


	/**
	 * @param fechaFirmaContrato the fechaFirmaContrato to set
	 */
	public void setFechaFirmaContrato(String fechaFirmaContrato) {
		this.fechaFirmaContrato = fechaFirmaContrato;
	}


	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}


	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoTarifa
	
	 * @return retorna la variable tipoTarifa 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoTarifa
	
	 * @param valor para el atributo tipoTarifa 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}
	
}
