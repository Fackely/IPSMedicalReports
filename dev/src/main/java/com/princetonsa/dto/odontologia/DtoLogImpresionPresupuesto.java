/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 4:58:03 PM
 */
public class DtoLogImpresionPresupuesto implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4431494637745330535L;
	
	/**
	 * 
	 */
	private BigDecimal codigoPk;
	
	/**
	 * 
	 */
	private BigDecimal presupuesto;
	
	/**
	 * 
	 */
	private DtoInfoFechaUsuario FHU;

	/**
	 * 
	 */
	private ArrayList<DtoDetalleLogImpresionPresupuesto> detalle;
	
	/**
	 * Constructor de la clase
	 * @param codigoPk
	 * @param presupuesto
	 * @param fHU
	 */
	public DtoLogImpresionPresupuesto() 
	{
		this.codigoPk = BigDecimal.ZERO;
		this.presupuesto = BigDecimal.ZERO;
		this.FHU = new DtoInfoFechaUsuario();
		this.detalle= new ArrayList<DtoDetalleLogImpresionPresupuesto>();
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
	 * @return the presupuesto
	 */
	public BigDecimal getPresupuesto() {
		return presupuesto;
	}

	/**
	 * @param presupuesto the presupuesto to set
	 */
	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}

	/**
	 * @return the fHU
	 */
	public DtoInfoFechaUsuario getFHU() {
		return FHU;
	}

	/**
	 * @param fHU the fHU to set
	 */
	public void setFHU(DtoInfoFechaUsuario fHU) {
		FHU = fHU;
	}

	/**
	 * @return the detalle
	 */
	public ArrayList<DtoDetalleLogImpresionPresupuesto> getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(ArrayList<DtoDetalleLogImpresionPresupuesto> detalle) {
		this.detalle = detalle;
	}
	
}
