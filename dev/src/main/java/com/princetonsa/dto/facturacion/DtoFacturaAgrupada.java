package com.princetonsa.dto.facturacion;

import java.util.LinkedList;
import java.util.List;

public class DtoFacturaAgrupada {
	
	private String codigofactura;
	private String descripcion;
	private String detalleProcedimiento;
	private String valorTotal;
	private String codigoCirugia;
	private String codigoDetallefactura;
	private List<DtoDetalleCirugiasFacturaAgrupada> detallesCirugias;
	
	
	public DtoFacturaAgrupada() {
		this.codigofactura="";
		this.descripcion="";
		this.detalleProcedimiento="";
		this.valorTotal="";
		this.codigoCirugia="";
		this.codigoDetallefactura="";
		this.detallesCirugias=new LinkedList<DtoDetalleCirugiasFacturaAgrupada>();
	}



	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}



	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	/**
	 * @return the detalleProcedimiento
	 */
	public String getDetalleProcedimiento() {
		return detalleProcedimiento;
	}



	/**
	 * @param detalleProcedimiento the detalleProcedimiento to set
	 */
	public void setDetalleProcedimiento(String detalleProcedimiento) {
		this.detalleProcedimiento = detalleProcedimiento;
	}



	/**
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}



	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}



	/**
	 * @return the codigofactura
	 */
	public String getCodigofactura() {
		return codigofactura;
	}



	/**
	 * @param codigofactura the codigofactura to set
	 */
	public void setCodigofactura(String codigofactura) {
		this.codigofactura = codigofactura;
	}



	/**
	 * @return the codigoCirugia
	 */
	public String getCodigoCirugia() {
		return codigoCirugia;
	}



	/**
	 * @param codigoCirugia the codigoCirugia to set
	 */
	public void setCodigoCirugia(String codigoCirugia) {
		this.codigoCirugia = codigoCirugia;
	}



	/**
	 * @return the codigoDetallefactura
	 */
	public String getCodigoDetallefactura() {
		return codigoDetallefactura;
	}



	/**
	 * @param codigoDetallefactura the codigoDetallefactura to set
	 */
	public void setCodigoDetallefactura(String codigoDetallefactura) {
		this.codigoDetallefactura = codigoDetallefactura;
	}



	/**
	 * @return the detallesCirugias
	 */
	public List<DtoDetalleCirugiasFacturaAgrupada> getDetallesCirugias() {
		return detallesCirugias;
	}



	/**
	 * @param detallesCirugias the detallesCirugias to set
	 */
	public void setDetallesCirugias(
			List<DtoDetalleCirugiasFacturaAgrupada> detallesCirugias) {
		this.detallesCirugias = detallesCirugias;
	}
	
	
	
	
	
	

}
