/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import java.math.BigDecimal;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoServiciosReclamados 
{
	
	/**
	 * 
	 */
	private String protesis;
	
	/**
	 * 
	 */
	private String adaptacionProtesis;
	
	/**
	 * 
	 */
	private String rehabilitacion;
	
	/**
	 * 
	 */
	private BigDecimal valorProtesis;
	
	/**
	 * 
	 */
	private BigDecimal valorAdaptacionProtesis;
	
	/**
	 * 
	 */
	private BigDecimal valorRehabilitacion;
	
	/**
	 * 
	 */
	private String descProtesisServicioPrestado;

	
	/**
	 * 
	 */
	public DtoServiciosReclamados()
	{
		this.protesis=ConstantesBD.acronimoNo;
		this.adaptacionProtesis=ConstantesBD.acronimoNo;
		this.rehabilitacion=ConstantesBD.acronimoNo;
		this.valorProtesis=new BigDecimal(0);
		this.valorProtesis=new BigDecimal(0);
		this.valorRehabilitacion=new BigDecimal(0);
		this.descProtesisServicioPrestado="";
	}


	public String getProtesis() {
		return protesis;
	}


	public void setProtesis(String protesis) {
		this.protesis = protesis;
	}


	public String getAdaptacionProtesis() {
		return adaptacionProtesis;
	}


	public void setAdaptacionProtesis(String adaptacionProtesis) {
		this.adaptacionProtesis = adaptacionProtesis;
	}


	public String getRehabilitacion() {
		return rehabilitacion;
	}


	public void setRehabilitacion(String rehabilitacion) {
		this.rehabilitacion = rehabilitacion;
	}


	public BigDecimal getValorProtesis() {
		return valorProtesis;
	}


	public void setValorProtesis(BigDecimal valorProtesis) {
		this.valorProtesis = valorProtesis;
	}


	public BigDecimal getValorAdaptacionProtesis() {
		return valorAdaptacionProtesis;
	}


	public void setValorAdaptacionProtesis(BigDecimal valorAdaptacionProtesis) {
		this.valorAdaptacionProtesis = valorAdaptacionProtesis;
	}


	public BigDecimal getValorRehabilitacion() {
		return valorRehabilitacion;
	}


	public void setValorRehabilitacion(BigDecimal valorRehabilitacion) {
		this.valorRehabilitacion = valorRehabilitacion;
	}


	public String getDescProtesisServicioPrestado() {
		return descProtesisServicioPrestado;
	}


	public void setDescProtesisServicioPrestado(String descProtesisServicioPrestado) {
		this.descProtesisServicioPrestado = descProtesisServicioPrestado;
	}
}
