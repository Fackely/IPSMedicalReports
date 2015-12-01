/**
 * 
 */
package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 12:06:56 PM
 */
public class InfoServiciosProgramaPaquetePresupuesto implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9141876246793467694L;

	/**
	 * 
	 */
	private int servicio;
	
	/**
	 * 
	 */
	private BigDecimal valorBase;

	/**
	 * 
	 */
	private boolean existeTarifa;
	
	/**
	 * Constructor de la clase
	 * @param servicio
	 * @param valorBase
	 */
	public InfoServiciosProgramaPaquetePresupuesto(int servicio,BigDecimal valorBase, boolean existeTarifa) 
	{
		this.servicio = servicio;
		this.valorBase = valorBase;
		this.existeTarifa= existeTarifa;
	}

	public InfoServiciosProgramaPaquetePresupuesto() 
	{
		this.servicio= 0;
		this.valorBase= BigDecimal.ZERO;
		this.existeTarifa= false;
	}

	/**
	 * @return the servicio
	 */
	public int getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the valorBase
	 */
	public BigDecimal getValorBase() {
		return valorBase;
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotalNeto() {
		return valorBase;
	}
	
	/**
	 * @param valorBase the valorBase to set
	 */
	public void setValorBase(BigDecimal valorBase) {
		this.valorBase = valorBase;
	}

	/**
	 * @return the existeTarifa
	 */
	public boolean isExisteTarifa() {
		return existeTarifa;
	}

	/**
	 * @param existeTarifa the existeTarifa to set
	 */
	public void setExisteTarifa(boolean existeTarifa) {
		this.existeTarifa = existeTarifa;
	}
	
	
}
