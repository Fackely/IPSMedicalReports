package com.princetonsa.dto.odontologia;

public class DtoDetallePaquetesOdontologicosConvenios 
{
	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private int codigoPkPaqueteConvenio;
	
	/**
	 * 
	 */
	private DtoPaquetesOdontologicos paquete;
	
	/**
	 * 
	 */
	private int esquemaTarifario;
	
	/**
	 * 
	 */
	private String descripcionEsquemaTarifario;
	
	/**
	 * 
	 */
	private String fechaIncial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	/**
	 * 
	 */
	private boolean esUsado;
	
	/**
	 * 
	 */
	private String activo;

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoPkPaqueteConvenio() {
		return codigoPkPaqueteConvenio;
	}

	public void setCodigoPkPaqueteConvenio(int codigoPkPaqueteConvenio) {
		this.codigoPkPaqueteConvenio = codigoPkPaqueteConvenio;
	}


	public int getEsquemaTarifario() {
		return esquemaTarifario;
	}

	public void setEsquemaTarifario(int esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	public String getFechaIncial() {
		return fechaIncial;
	}

	public void setFechaIncial(String fechaIncial) {
		this.fechaIncial = fechaIncial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public DtoPaquetesOdontologicos getPaquete() {
		return paquete;
	}

	public void setPaquete(DtoPaquetesOdontologicos paquete) {
		this.paquete = paquete;
	}

	public String getDescripcionEsquemaTarifario() {
		return descripcionEsquemaTarifario;
	}

	public void setDescripcionEsquemaTarifario(String descripcionEsquemaTarifario) {
		this.descripcionEsquemaTarifario = descripcionEsquemaTarifario;
	}
	
	public void getNombrePaqueteAyudante()
	{
		this.paquete.getDescripcion();
	}

	public boolean isEsUsado() {
		return esUsado;
	}

	public void setEsUsado(boolean esUsado) {
		this.esUsado = esUsado;
	}
	

}
