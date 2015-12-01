package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import com.princetonsa.dto.odontologia.DtoSuperficieDental;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * May 21, 2010 - 3:56:27 PM
 */
public class InfoSuperficiePkDetPlan implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 170359749188532153L;

	/**
	 * 
	 */
	private DtoSuperficieDental superficie;
	
	/**
	 * 
	 */
	private BigDecimal codigoPkDetallePlanTratamiento;

	/**
	 * 
	 * Constructor de la clase
	 * @param superficie
	 * @param codigoPkDetallePlanTratamiento
	 */
	public InfoSuperficiePkDetPlan(DtoSuperficieDental superficie,
			BigDecimal codigoPkDetallePlanTratamiento) {
		super();
		this.superficie = superficie;
		this.codigoPkDetallePlanTratamiento = codigoPkDetallePlanTratamiento;
	}

	/**
	 * 
	 * Constructor de la clase
	 */
	public InfoSuperficiePkDetPlan() 
	{
		super();
		this.superficie = new DtoSuperficieDental();
		this.codigoPkDetallePlanTratamiento = BigDecimal.ZERO;
	}

	/**
	 * @return the superficie
	 */
	public DtoSuperficieDental getSuperficie() {
		return superficie;
	}

	/**
	 * @param superficie the superficie to set
	 */
	public void setSuperficie(DtoSuperficieDental superficie) {
		this.superficie = superficie;
	}

	/**
	 * @return the codigoPkDetallePlanTratamiento
	 */
	public BigDecimal getCodigoPkDetallePlanTratamiento() {
		return codigoPkDetallePlanTratamiento;
	}

	/**
	 * @param codigoPkDetallePlanTratamiento the codigoPkDetallePlanTratamiento to set
	 */
	public void setCodigoPkDetallePlanTratamiento(
			BigDecimal codigoPkDetallePlanTratamiento) {
		this.codigoPkDetallePlanTratamiento = codigoPkDetallePlanTratamiento;
	}
	
	
}
