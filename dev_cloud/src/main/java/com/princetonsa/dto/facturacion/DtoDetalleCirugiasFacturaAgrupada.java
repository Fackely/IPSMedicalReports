package com.princetonsa.dto.facturacion;

public class DtoDetalleCirugiasFacturaAgrupada {
	
	private String nombreServicioCirugia;
	private String valor;
	
	
	public DtoDetalleCirugiasFacturaAgrupada() {
		this.nombreServicioCirugia="";
		this.valor="";
	}


	/**
	 * @return the nombreServicioCirugia
	 */
	public String getNombreServicioCirugia() {
		return nombreServicioCirugia;
	}


	/**
	 * @param nombreServicioCirugia the nombreServicioCirugia to set
	 */
	public void setNombreServicioCirugia(String nombreServicioCirugia) {
		this.nombreServicioCirugia = nombreServicioCirugia;
	}


	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
	

}
