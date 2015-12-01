/**
 * 
 */
package com.princetonsa.dto.tesoreria;

/**
 * @author armando
 *
 */
public class DtoFiltroConsultaAbonos 
{
	
	/**
	 * 
	 */
	private int tipoMovimientoBusqueda;

	
	/**
	 * 
	 */
	private int centroAtencionBusqueda;

	/**
	 * 
	 */
	private String ingresoBusqueda;


	/**
	 * 
	 */
	private String fechaInicialBusqueda;

	/**
	 * 
	 */
	private String fechaFinalBusqueda;
	
	
	
	public int getTipoMovimientoBusqueda() {
		return tipoMovimientoBusqueda;
	}

	public void setTipoMovimientoBusqueda(int tipoMovimientoBusqueda) {
		this.tipoMovimientoBusqueda = tipoMovimientoBusqueda;
	}

	public int getCentroAtencionBusqueda() {
		return centroAtencionBusqueda;
	}

	public void setCentroAtencionBusqueda(int centroAtencionBusqueda) {
		this.centroAtencionBusqueda = centroAtencionBusqueda;
	}


	public String getFechaInicialBusqueda() {
		return fechaInicialBusqueda;
	}

	public void setFechaInicialBusqueda(String fechaInicialBusqueda) {
		this.fechaInicialBusqueda = fechaInicialBusqueda;
	}

	public String getFechaFinalBusqueda() {
		return fechaFinalBusqueda;
	}

	public void setFechaFinalBusqueda(String fechaFinalBusqueda) {
		this.fechaFinalBusqueda = fechaFinalBusqueda;
	}

	

	public String getIngresoBusqueda() {
		return ingresoBusqueda;
	}

	public void setIngresoBusqueda(String ingresoBusqueda) {
		this.ingresoBusqueda = ingresoBusqueda;
	}

}
