
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.UtilidadTexto;

/**
 * Esta clase contiene la información de los totales 
 * calculados para realizar la contratación de las inclusiones al
 * presupuesto
 * 
 * @autor Jorge Armando Agudelo Quintero
 */
public class DtoTotalesContratarInclusion implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Total de las Inclusiones a contratar
	 */
	private BigDecimal totalInclusiones;
	
	/**
	 * Total de las inclusiones que no aplican ni para bono ni para promoción
	 */
	private BigDecimal totalInclusionesParaDescuento;
	
	/**
	 * Descuento autorizado sobre el total de las inclusiones para descuento
	 */
	private BigDecimal descuento;
	
	/**
	 * Total de las inclusiones a contratar con el descuento aplicado
	 */
	private BigDecimal totalInclusionesAContratar;
	
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoTotalesContratarInclusion() {
		
		resetTotales();
		
	}
	
	/**
	 * Método que reinicia los totales calculados.
	 */
	public void resetTotales (){
		
		this.totalInclusiones = new BigDecimal(0);
		
		this.totalInclusionesParaDescuento = new BigDecimal(0);
		
		this.totalInclusionesAContratar = new BigDecimal(0);
		
		this.descuento = new BigDecimal(0);
		
	}
	/**
	 * @return the totalInclusiones
	 */
	public BigDecimal getTotalInclusiones() {
		return totalInclusiones;
	}


	/**
	 * Devuelve el valor total de las inclusiones a contratar formateado
	 * 
	 * @return the totalInclusiones
	 */
	public String getTotalInclusionesFormateado() {
		return UtilidadTexto.formatearValores(totalInclusiones.doubleValue());
	}
	
	/**
	 * @param totalInclusiones the totalInclusiones to set
	 */
	public void setTotalInclusiones(BigDecimal totalInclusiones) {
		this.totalInclusiones = totalInclusiones;
	}


	/**
	 * @return the totalInclusionesParaDescuento
	 */
	public BigDecimal getTotalInclusionesParaDescuento() {
		return totalInclusionesParaDescuento;
	}

	/**
	 * Devuelve el valor total de las inclusiones para descuento formateado
	 * 
	 * @return the totalInclusiones
	 */
	public String getTotalInclusionesParaDescuentoFormateado() {
		return UtilidadTexto.formatearValores(totalInclusionesParaDescuento.doubleValue());
	}
	

	/**
	 * @param totalInclusionesParaDescuento the totalInclusionesParaDescuento to set
	 */
	public void setTotalInclusionesParaDescuento(
			BigDecimal totalInclusionesParaDescuento) {
		this.totalInclusionesParaDescuento = totalInclusionesParaDescuento;
	}


	/**
	 * @return the descuento
	 */
	public BigDecimal getDescuento() {
		return descuento;
	}


	/**
	 * Devuelve el valor total del descuento formateado
	 * 
	 * @return the totalInclusiones
	 */
	public String getDescuentoFormateado() {
		return UtilidadTexto.formatearValores(descuento.doubleValue());
	}
	
	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}


	/**
	 * @return the totalInclusionesAContratar
	 */
	public BigDecimal getTotalInclusionesAContratar() {
		return totalInclusionesAContratar;
	}

	/**
	 * Devuelve el valor total a contratar formateado
	 * 
	 * @return the totalInclusiones
	 */
	public String getTotalInclusionesAContratarFormateado() {
		return UtilidadTexto.formatearValores(totalInclusionesAContratar.doubleValue());
	}

	/**
	 * @param totalInclusionesAContratar the totalInclusionesAContratar to set
	 */
	public void setTotalInclusionesAContratar(BigDecimal totalInclusionesAContratar) {
		this.totalInclusionesAContratar = totalInclusionesAContratar;
	}
	
}
