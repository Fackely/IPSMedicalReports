package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 5:28:55 PM
 */
public class DtoPresupuestoCuotasEspecialidad implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 377808024756286369L;
	
	/**
	 * 
	 */
	private BigDecimal codigoPk;
	
	/**
	 * 
	 */
	private BigDecimal presupuestoContratado;
	
	/**
	 * 
	 */
	private int especialidad;
	
	/**
	 * Enum 
	 */
	public static enum ETipoValor 
	{
		VALOR,
		PORC
	};

	/**
	 * 
	 */
	private String tipoValor;
	
	/**
	 * 
	 */
	private DtoInfoFechaUsuario FHU;

	
	private ArrayList<DtoPresupuestoDetalleCuotasEspecialidad> detalle;
	
	/**
	 * Constructor de la clase
	 */
	public DtoPresupuestoCuotasEspecialidad() {
		this.codigoPk = BigDecimal.ZERO;
		this.presupuestoContratado = BigDecimal.ZERO;
		this.especialidad = -1;
		this.FHU = new DtoInfoFechaUsuario();
		this.tipoValor= null;
		this.detalle= new ArrayList<DtoPresupuestoDetalleCuotasEspecialidad>();
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
	 * @return the presupuestoContratado
	 */
	public BigDecimal getPresupuestoContratado() {
		return presupuestoContratado;
	}

	/**
	 * @param presupuestoContratado the presupuestoContratado to set
	 */
	public void setPresupuestoContratado(BigDecimal presupuestoContratado) {
		this.presupuestoContratado = presupuestoContratado;
	}

	/**
	 * @return the especialidad
	 */
	public int getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
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
	public ArrayList<DtoPresupuestoDetalleCuotasEspecialidad> getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(
			ArrayList<DtoPresupuestoDetalleCuotasEspecialidad> detalle) {
		this.detalle = detalle;
	}

	/**
	 * @return the tipoValor
	 */
	public String getTipoValor() {
		return tipoValor;
	}

	/**
	 * @param tipoValor the tipoValor to set
	 */
	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
	}
}