package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Dto que contiene los valores totales consolidados por cada proceso
 * 
 * @version 1.0, May 21, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoTotalProceso implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8820627787640641153L;
	
	/**
	 * Atributo que representa el identificador del proceso
	 */
	private String proceso;
	
	/**
	 * Atributo que representa la cantidad total consolidada para un proces
	 */
	private int cantidad;
	
	/**
	 * Atributo que representa el valor total consolidado para un proces
	 */
	private BigDecimal totalProceso;
	
	public DtoTotalProceso(){
		
	}
	
	public DtoTotalProceso(String proceso, BigDecimal totalProceso){
		this.proceso=proceso;
		this.totalProceso=totalProceso;
	}
	
	public DtoTotalProceso(String proceso, int cantidad, BigDecimal totalProceso){
		this.proceso=proceso;
		this.cantidad=cantidad;
		this.totalProceso=totalProceso;
	}

	/**
	 * @return the proceso
	 */
	public String getProceso() {
		return proceso;
	}

	/**
	 * @param proceso the proceso to set
	 */
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	/**
	 * @return the totalProceso
	 */
	public BigDecimal getTotalProceso() {
		return totalProceso;
	}

	/**
	 * @param totalProceso the totalProceso to set
	 */
	public void setTotalProceso(BigDecimal totalProceso) {
		this.totalProceso = totalProceso;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
