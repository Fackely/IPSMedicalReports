package com.servinte.axioma.dto.capitacion;

import java.sql.Date;

/**
 * Dto que contiene los valores requeridos para la 
 * busqueda de logs de parametrización del presupuesto
 * @author diecorqu
 *
 */
public class DtoLogBusquedaParametrizacion {

	/**
	 * Fecha inicial de la busqueda
	 */
	private Date fechaInicial;
	
	/**
	 * Fecha final de la busqueda
	 */
	private Date fechaFinal;
	
	/**
	 * Código del contrato seleccionado
	 */
	private int codigoContrato;
	
	/**
	 * Código del convenio seleccionado
	 */
	private int codigoConvenio;
	
	/**
	 * Año de vigencia seleccionado
	 */
	private String fechaVigencia;
	

	public DtoLogBusquedaParametrizacion() {}
	
	public DtoLogBusquedaParametrizacion(Date fechaInicial, Date fechaFinal,
			int codigoContrato, int codigoConvenio, String fechaVigencia) {
		super();
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
		this.codigoContrato = codigoContrato;
		this.codigoConvenio = codigoConvenio;
		this.fechaVigencia = fechaVigencia;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public int getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public int getCodigoConvenio() {
		return codigoConvenio;
	}



	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}



	public String getFechaVigencia() {
		return fechaVigencia;
	}

	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

}
