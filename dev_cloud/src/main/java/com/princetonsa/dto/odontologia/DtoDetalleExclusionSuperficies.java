package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * May 22, 2010 - 2:48:15 PM
 */
public class DtoDetalleExclusionSuperficies implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 510739660717782905L;
	private BigDecimal codigoPk;
	private BigDecimal codigoPkDetPlanTratamiento;
	private BigDecimal codigoPkExclusionPresupuesto;
	
	/**
	 * 
	 * Constructor de la clase
	 * @param codigoPk
	 * @param codigoPkDetPlanTratamiento
	 * @param codigoPkExclusionPresupuesto
	 * @param superficie
	 */
	public DtoDetalleExclusionSuperficies(BigDecimal codigoPkDetPlanTratamiento) {
		super();
		this.codigoPk = BigDecimal.ZERO;
		this.codigoPkDetPlanTratamiento = codigoPkDetPlanTratamiento;
		this.codigoPkExclusionPresupuesto = BigDecimal.ZERO;
	}

	/**
	 * 
	 * Constructor de la clase
	 */
	public DtoDetalleExclusionSuperficies() 
	{
		super();
		this.codigoPk = BigDecimal.ZERO;
		this.codigoPkDetPlanTratamiento = BigDecimal.ZERO;
		this.codigoPkExclusionPresupuesto = BigDecimal.ZERO;
	}

	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigoPkDetPlanTratamiento
	 */
	public BigDecimal getCodigoPkDetPlanTratamiento() {
		return codigoPkDetPlanTratamiento;
	}

	/**
	 * @param codigoPkDetPlanTratamiento the codigoPkDetPlanTratamiento to set
	 */
	public void setCodigoPkDetPlanTratamiento(BigDecimal codigoPkDetPlanTratamiento) {
		this.codigoPkDetPlanTratamiento = codigoPkDetPlanTratamiento;
	}

	/**
	 * @return the codigoPkExclusionPresupuesto
	 */
	public BigDecimal getCodigoPkExclusionPresupuesto() {
		return codigoPkExclusionPresupuesto;
	}

	/**
	 * @param codigoPkExclusionPresupuesto the codigoPkExclusionPresupuesto to set
	 */
	public void setCodigoPkExclusionPresupuesto(
			BigDecimal codigoPkExclusionPresupuesto) {
		this.codigoPkExclusionPresupuesto = codigoPkExclusionPresupuesto;
	}


}
