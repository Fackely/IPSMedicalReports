package com.servinte.axioma.generadorReporte.ordenes.solicitudMedicamentos;

/**
 * Dto con los datos de los Medicamentos de la solicitud a imprimir
 * @author hermorhu
 * @created 08-Mar-2013 
 */
public class ImpresionMedicamentoInsumoSolicitadoDto {
	
	private String articulo;
	private String medicamento;
	private String dosis;
	private String frecuencia;
	private String via;
	private String diasTratamiento;
	private String cantidad;
	private String observaciones;
	private String pos;
	private String esMedicamento;
	
	/**
	 * 
	 */
	public ImpresionMedicamentoInsumoSolicitadoDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the articulo
	 */
	public String getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the medicamento
	 */
	public String getMedicamento() {
		return medicamento;
	}

	/**
	 * @param medicamento the medicamento to set
	 */
	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	/**
	 * @return the dosis
	 */
	public String getDosis() {
		return dosis;
	}

	/**
	 * @param dosis the dosis to set
	 */
	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	/**
	 * @return the frecuencia
	 */
	public String getFrecuencia() {
		return frecuencia;
	}

	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	/**
	 * @return the via
	 */
	public String getVia() {
		return via;
	}

	/**
	 * @param via the via to set
	 */
	public void setVia(String via) {
		this.via = via;
	}

	/**
	 * @return the cantidad
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
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
	 * @return the pos
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(String pos) {
		this.pos = pos;
	}

	/**
	 * @return the diasTratamiento
	 */
	public String getDiasTratamiento() {
		return diasTratamiento;
	}

	/**
	 * @param diasTratamiento the diasTratamiento to set
	 */
	public void setDiasTratamiento(String diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}

	/**
	 * @return the esMedicamento
	 */
	public String getEsMedicamento() {
		return esMedicamento;
	}

	/**
	 * @param esMedicamento the esMedicamento to set
	 */
	public void setEsMedicamento(String esMedicamento) {
		this.esMedicamento = esMedicamento;
	}
	
}
