/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.InfoDatosInt;

/**
 * 
 * @author Wilson Rios 
 *
 * May 7, 2010 - 10:50:48 AM
 */
public class DtoDetallePresupuestoPlanNumSuperficies implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5143958415517241346L;

	/**
	 * codigo pk
	 */
	private BigDecimal codigoPk;
	
	/**
	 * codigo del enzabezado 
	 */
	private BigDecimal codigoEncabezadoPresuPlanTtoNumSuperficies;
	
	/**
	 * superficie
	 */
	private InfoDatosInt superficie;
	
	/**
	 * Fecha - Hora - Usuario
	 */
	private DtoInfoFechaUsuario FHU;
	
	/**
	 * Constructor de la clase
	 */
	public DtoDetallePresupuestoPlanNumSuperficies() 
	{
		this.codigoPk= BigDecimal.ZERO;
		this.codigoEncabezadoPresuPlanTtoNumSuperficies= BigDecimal.ZERO;
		this.superficie= new InfoDatosInt();
		this.FHU= new DtoInfoFechaUsuario();
	}

	/**
	 * Constructor de la clase
	 */
	public DtoDetallePresupuestoPlanNumSuperficies(BigDecimal codigoEncabezadoPresuPlanTtoNumSuperficies) 
	{
		this.codigoPk= BigDecimal.ZERO;
		this.codigoEncabezadoPresuPlanTtoNumSuperficies= codigoEncabezadoPresuPlanTtoNumSuperficies;
		this.superficie= new InfoDatosInt();
		this.FHU= new DtoInfoFechaUsuario();
	}	
	
	/**
	 * @return the codigoPkPresuPlanTtoProgramaServicio
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPkPresuPlanTtoProgramaServicio the codigoPkPresuPlanTtoProgramaServicio to set
	 */
	public void setCodigoPk(
			BigDecimal codigoPkPresuPlanTtoProgramaServicio) {
		this.codigoPk = codigoPkPresuPlanTtoProgramaServicio;
	}

	/**
	 * @return the codigoEncabezadoPresuPlanTtoNumSuperficies
	 */
	public BigDecimal getCodigoEncabezadoPresuPlanTtoNumSuperficies() {
		return codigoEncabezadoPresuPlanTtoNumSuperficies;
	}

	/**
	 * @param codigoEncabezadoPresuPlanTtoNumSuperficies the codigoEncabezadoPresuPlanTtoNumSuperficies to set
	 */
	public void setCodigoEncabezadoPresuPlanTtoNumSuperficies(
			BigDecimal codigoEncabezadoPresuPlanTtoNumSuperficies) {
		this.codigoEncabezadoPresuPlanTtoNumSuperficies = codigoEncabezadoPresuPlanTtoNumSuperficies;
	}

	/**
	 * @return the superficie
	 */
	public InfoDatosInt getSuperficie() {
		return superficie;
	}

	/**
	 * @param superficie the superficie to set
	 */
	public void setSuperficie(InfoDatosInt superficie) {
		this.superficie = superficie;
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
}