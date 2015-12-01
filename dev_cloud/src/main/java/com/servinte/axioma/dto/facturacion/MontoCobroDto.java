package com.servinte.axioma.dto.facturacion;

/**
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class MontoCobroDto {

	/**Atributo para almacenar el tipo de monto */
	private String tipoMontoNombre;
	
	/**Atributo para almacenar el tipo del detalle del monto */
	private String tipoDetalleMonto;
	
	/**Atributo para almacenar el valor del monto de cobro cuando el tipo de detalle es general */
	private Double valorMonto;
	
	/**Atributo para almacenar el porcentaje del monto cuando este es general */
	private Double porcentajeMonto;
	
	/**Atributo para almacenar el codigo del tipo de monto */
	private Integer tipoMonto;
	
	/**Atributo para almacenar el valor del monto que se calcula con la información del monto encontrada */
	private Double valorMontoCalculado;
	
	/**Atributo que almacena la cantidad de montos */
	private Integer cantidadMonto;
	
	/**Atributo que almacena el codigo del detalle del monto */
	private Integer codDetalleMonto;
	
	/**
	 * Atributo que representa la descripcion del monto
	 */
	private String descripcionMonto;
	
	public MontoCobroDto(){
		
	}
	
	/**
	 * @return tipoMonto
	 */
	public String getTipoMontoNombre() {
		return tipoMontoNombre;
	}
	
	/**
	 * @param tipoMonto
	 */
	public void setTipoMontoNombre(String tipoMontoNombre) {
		this.tipoMontoNombre = tipoMontoNombre;
	}
	
	/**
	 * @return tipoDetalleMonto
	 */
	public String getTipoDetalleMonto() {
		return tipoDetalleMonto;
	}
	
	/**
	 * @param tipoDetalleMonto
	 */
	public void setTipoDetalleMonto(String tipoDetalleMonto) {
		this.tipoDetalleMonto = tipoDetalleMonto;
	}
	
	
	/**
	 * @return valorMontoDetallado
	 */
	public Double getValorMonto() {
		return valorMonto;
	}

	/**
	 * @param valorMontoDetallado
	 */
	public void setValorMonto(Double valorMonto) {
		this.valorMonto = valorMonto;
	}
	
	/**
	 * @return porcentajeMonto
	 */
	public Double getPorcentajeMonto() {
		return porcentajeMonto;
	}
	
	/**
	 * @param porcentajeMonto
	 */
	public void setPorcentajeMonto(Double porcentajeMonto) {
		this.porcentajeMonto = porcentajeMonto;
	}

	/**
	 * @return tipoMonto
	 */
	public Integer getTipoMonto() {
		return tipoMonto;
	}
	
	/**
	 * @param tipoMonto
	 */
	public void setTipoMonto(Integer tipoMonto) {
		this.tipoMonto = tipoMonto;
	}
	
	/**
	 * @return valorMontoCalculado
	 */
	public Double getValorMontoCalculado() {
		return valorMontoCalculado;
	}
	
	/**
	 * @param valorMontoCalculado
	 */
	public void setValorMontoCalculado(Double valorMontoCalculado) {
		this.valorMontoCalculado = valorMontoCalculado;
	}
	
	/**
	 * @return cantidadMonto
	 */
	public Integer getCantidadMonto() {
		return cantidadMonto;
	}
	
	/**
	 * @param cantidadMonto
	 */
	public void setCantidadMonto(Integer cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}
	
	/**
	 * @return codDetalleMonto
	 */
	public Integer getCodDetalleMonto() {
		return codDetalleMonto;
	}
	
	/**
	 * @param codDetalleMonto
	 */
	public void setCodDetalleMonto(Integer codDetalleMonto) {
		this.codDetalleMonto = codDetalleMonto;
	}

	/**
	 * @return the descripcionMonto
	 */
	public String getDescripcionMonto() {
		return descripcionMonto;
	}

	/**
	 * @param descripcionMonto the descripcionMonto to set
	 */
	public void setDescripcionMonto(String descripcionMonto) {
		this.descripcionMonto = descripcionMonto;
	}
	
}