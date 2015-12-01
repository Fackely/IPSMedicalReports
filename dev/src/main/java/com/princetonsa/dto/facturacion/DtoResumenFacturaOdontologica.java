package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author axioma
 *
 */
public class DtoResumenFacturaOdontologica implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1797258679665946289L;

	/**
	 * 
	 */
	private BigDecimal codigoPkFactura;
	
	/**
	 * 
	 */
	private BigDecimal consecutivoFactura;
	
	/**
	 * 
	 */
	private String responsable;
	
	/**
	 * 
	 */
	private BigDecimal totalCargos;

	/**
	 * 
	 */
	private boolean imprimir;
	
	/**
	 * 
	 */
	private String pathImpresion;
	
	/**
	 * 
	 */
	public DtoResumenFacturaOdontologica() {
		super();
		this.codigoPkFactura = BigDecimal.ZERO;
		this.consecutivoFactura = BigDecimal.ZERO;
		this.responsable = "";
		this.totalCargos = BigDecimal.ZERO;
		this.imprimir= false;
		this.pathImpresion="";
	}

	/**
	 * @return the codigoPkFactura
	 */
	public BigDecimal getCodigoPkFactura() {
		return codigoPkFactura;
	}

	/**
	 * @param codigoPkFactura the codigoPkFactura to set
	 */
	public void setCodigoPkFactura(BigDecimal codigoPkFactura) {
		this.codigoPkFactura = codigoPkFactura;
	}

	/**
	 * @return the consecutivoFactura
	 */
	public BigDecimal getConsecutivoFactura() {
		return consecutivoFactura;
	}

	/**
	 * @param consecutivoFactura the consecutivoFactura to set
	 */
	public void setConsecutivoFactura(BigDecimal consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	/**
	 * @return the responsable
	 */
	public String getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	/**
	 * @return the subTotalCargos
	 */
	public BigDecimal getTotalCargos() {
		return totalCargos;
	}

	/**
	 * @param subTotalCargos the subTotalCargos to set
	 */
	public void setTotalCargos(BigDecimal totalCargos) {
		this.totalCargos = totalCargos;
	}

	
	/**
	 * @return the imprimir
	 */
	public boolean getImprimir() {
		return imprimir;
	}
	
	/**
	 * @return the imprimir
	 */
	public boolean isImprimir() {
		return imprimir;
	}

	/**
	 * @param imprimir the imprimir to set
	 */
	public void setImprimir(boolean imprimir) {
		this.imprimir = imprimir;
	}

	/**
	 * @return the pathImpresion
	 */
	public String getPathImpresion() {
		return pathImpresion;
	}

	/**
	 * @param pathImpresion the pathImpresion to set
	 */
	public void setPathImpresion(String pathImpresion) {
		this.pathImpresion = pathImpresion;
	}
	
}