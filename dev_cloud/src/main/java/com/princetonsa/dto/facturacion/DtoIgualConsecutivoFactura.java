package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

import util.InfoDatosInt;

/**
 * 
 * @author axioma
 *
 */
public class DtoIgualConsecutivoFactura implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7763986208968029881L;

	/**
	 * 
	 */
	private BigDecimal codigoFactura;
	
	/**
	 * 
	 */
	private BigDecimal consecutivoFactura;
	
	/**
	 * 
	 */
	private InfoDatosInt institucion;
	
	/**
	 * 
	 */
	private InfoDatosInt centroAtencion;

	/**
	 * 
	 */
	public DtoIgualConsecutivoFactura() 
	{
		super();
		this.codigoFactura = BigDecimal.ZERO;
		this.consecutivoFactura = BigDecimal.ZERO;
		this.institucion = new InfoDatosInt();
		this.centroAtencion = new InfoDatosInt();
	}

	/**
	 * @return the codigoFactura
	 */
	public BigDecimal getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(BigDecimal codigoFactura) {
		this.codigoFactura = codigoFactura;
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
	 * @return the institucion
	 */
	public InfoDatosInt getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(InfoDatosInt institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	
	
}
