package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author axioma
 *
 */
public class DtoValorAnticipoPresupuesto implements Serializable
{
	private BigDecimal valorAnticipo;
	
	private int contrato;

	/**
	 * 
	 * @param valorAnticipo
	 * @param contrato
	 */
	public DtoValorAnticipoPresupuesto(BigDecimal valorAnticipo, int contrato) {
		super();
		this.valorAnticipo = valorAnticipo;
		this.contrato = contrato;
	}

	/**
	 * @return the valorAnticipo
	 */
	public BigDecimal getValorAnticipo() {
		return valorAnticipo;
	}

	/**
	 * @param valorAnticipo the valorAnticipo to set
	 */
	public void setValorAnticipo(BigDecimal valorAnticipo) {
		this.valorAnticipo = valorAnticipo;
	}

	/**
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}
	
	
}
