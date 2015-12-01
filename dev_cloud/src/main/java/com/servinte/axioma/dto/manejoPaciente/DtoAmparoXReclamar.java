/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import java.math.BigDecimal;

/**
 * @author axioma
 *
 */
public class DtoAmparoXReclamar 
{
	/**
	 * 
	 */
	private BigDecimal totalFacAmpGastMedQX;
	
	/**
	 * 
	 */
	private BigDecimal totalRecAmpGastMedQX;
	
	/**
	 * 
	 */
	private BigDecimal totalFacAmpGastTransMov;
	
	/**
	 * 
	 */
	private BigDecimal totalRecAmpGastTransMov;
	
	/**
	 * 
	 */
	public DtoAmparoXReclamar()
	{
		this.totalFacAmpGastMedQX=new BigDecimal(0);
		this.totalRecAmpGastMedQX=new BigDecimal(0);
		this.totalFacAmpGastTransMov=new BigDecimal(0);
		this.totalRecAmpGastTransMov=new BigDecimal(0);
	}

	public BigDecimal getTotalFacAmpGastMedQX() {
		return totalFacAmpGastMedQX;
	}

	public void setTotalFacAmpGastMedQX(BigDecimal totalFacAmpGastMedQX) {
		this.totalFacAmpGastMedQX = totalFacAmpGastMedQX;
	}

	public BigDecimal getTotalRecAmpGastMedQX() {
		return totalRecAmpGastMedQX;
	}

	public void setTotalRecAmpGastMedQX(BigDecimal totalRecAmpGastMedQX) {
		this.totalRecAmpGastMedQX = totalRecAmpGastMedQX;
	}

	public BigDecimal getTotalFacAmpGastTransMov() {
		return totalFacAmpGastTransMov;
	}

	public void setTotalFacAmpGastTransMov(BigDecimal totalFacAmpGastTransMov) {
		this.totalFacAmpGastTransMov = totalFacAmpGastTransMov;
	}

	public BigDecimal getTotalRecAmpGastTransMov() {
		return totalRecAmpGastTransMov;
	}

	public void setTotalRecAmpGastTransMov(BigDecimal totalRecAmpGastTransMov) {
		this.totalRecAmpGastTransMov = totalRecAmpGastTransMov;
	}

}
