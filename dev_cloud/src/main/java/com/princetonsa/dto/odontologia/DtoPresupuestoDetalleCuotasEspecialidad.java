/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.InfoPorcentajeValor;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 5:21:35 PM
 */
public class DtoPresupuestoDetalleCuotasEspecialidad implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8848583889952034713L;
	
	/**
	 * 
	 */
	private BigDecimal codigoPk;
	
	/**
	 * 
	 */
	private BigDecimal presupuestoCuotasEspecialidad;
	
	/**
	 * Enum 
	 */
	public static enum ETipoCuota 
	{
		INIC,
		PORCI,
		ADIC,
	};
	
	/**
	 * 
	 */
	private ETipoCuota tipoCuota;
	
	/**
	 * 
	 */
	private int nroCuotas;
	
	/**
	 * 
	 */
	private InfoPorcentajeValor valorOporcentaje;

	/**
	 * Constructor de la clase
	 */
	public DtoPresupuestoDetalleCuotasEspecialidad() {
		this.codigoPk = BigDecimal.ZERO;
		this.presupuestoCuotasEspecialidad = BigDecimal.ZERO;
		this.tipoCuota = null;
		this.nroCuotas = 0;
		this.valorOporcentaje = new InfoPorcentajeValor();
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
	 * @return the presupuestoCuotasEspecialidad
	 */
	public BigDecimal getPresupuestoCuotasEspecialidad() {
		return presupuestoCuotasEspecialidad;
	}

	/**
	 * @param presupuestoCuotasEspecialidad the presupuestoCuotasEspecialidad to set
	 */
	public void setPresupuestoCuotasEspecialidad(
			BigDecimal presupuestoCuotasEspecialidad) {
		this.presupuestoCuotasEspecialidad = presupuestoCuotasEspecialidad;
	}

	/**
	 * @return the tipoCuota
	 */
	public ETipoCuota getTipoCuota() {
		return tipoCuota;
	}

	/**
	 * @param tipoCuota the tipoCuota to set
	 */
	public void setTipoCuota(ETipoCuota tipoCuota) {
		this.tipoCuota = tipoCuota;
	}

	/**
	 * @return the nroCuotas
	 */
	public int getNroCuotas() {
		return nroCuotas;
	}

	/**
	 * @param nroCuotas the nroCuotas to set
	 */
	public void setNroCuotas(int nroCuotas) {
		this.nroCuotas = nroCuotas;
	}

	/**
	 * @return the valorOporcentaje
	 */
	public InfoPorcentajeValor getValorOporcentaje() {
		return valorOporcentaje;
	}

	/**
	 * @param valorOporcentaje the valorOporcentaje to set
	 */
	public void setValorOporcentaje(InfoPorcentajeValor valorOporcentaje) {
		this.valorOporcentaje = valorOporcentaje;
	}
	
	
	
}
