/**
 * 
 */
package com.princetonsa.dto.facturacion;

/**
 * @author axioma
 *
 */
public class DtoFiltroBusquedaMontosCobro 
{
	/**
	 * 
	 */
	private int viaIngreso;
	
	/**
	 * 
	 */
	private String tipoPaciente;
	
	/**
	 * 
	 */
	private int convenio;
	
	/**
	 * 
	 */
	private String tipoAfiliado;
	
	/**
	 * 
	 */
	private int clasificacionSocioEconomica;
	
	/**
	 * 
	 */
	private int naturalezaPaciente;
	
	/**
	 * 
	 */
	private String fechaAperturaCuenta;

	public int getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	public String getTipoPaciente() {
		return tipoPaciente;
	}

	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	public int getConvenio() {
		return convenio;
	}

	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	public int getClasificacionSocioEconomica() {
		return clasificacionSocioEconomica;
	}

	public void setClasificacionSocioEconomica(int clasificacionSocioEconomica) {
		this.clasificacionSocioEconomica = clasificacionSocioEconomica;
	}

	public int getNaturalezaPaciente() {
		return naturalezaPaciente;
	}

	public void setNaturalezaPaciente(int naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}

	public String getFechaAperturaCuenta() {
		return fechaAperturaCuenta;
	}

	public void setFechaAperturaCuenta(String fechaAperturaCuenta) {
		this.fechaAperturaCuenta = fechaAperturaCuenta;
	}

}

