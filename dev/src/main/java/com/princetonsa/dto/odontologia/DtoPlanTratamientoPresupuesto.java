package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoPlanTratamientoPresupuesto implements Serializable 
{
	private BigDecimal codigoPk;
	private BigDecimal detPlanTratamiento;
	private BigDecimal programa;
	private int servicio;
	private BigDecimal presupuesto;
	private DtoInfoFechaUsuario FHU;
	
	//este campo solo se llena para los programas/servicios 
	//del plan de tratamiento que hacen parte de la val inicial 
	private BigDecimal programaServicioPlanTratamientoFK; 
	private boolean activo;
	
	/**
	 * 
	 */
	public DtoPlanTratamientoPresupuesto() {
		this.codigoPk = BigDecimal.ZERO;
		this.detPlanTratamiento = BigDecimal.ZERO;
		this.programa = BigDecimal.ZERO;
		this.servicio= 0;
		this.presupuesto = BigDecimal.ZERO;
		this.FHU = new DtoInfoFechaUsuario();
		this.programaServicioPlanTratamientoFK = BigDecimal.ZERO;
		this.activo = true;
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
	 * @return the detPlanTratamiento
	 */
	public BigDecimal getDetPlanTratamiento() {
		return detPlanTratamiento;
	}
	/**
	 * @param detPlanTratamiento the detPlanTratamiento to set
	 */
	public void setDetPlanTratamiento(BigDecimal detPlanTratamiento) {
		this.detPlanTratamiento = detPlanTratamiento;
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
	 * @return the programaServicioPlanTratamientoFK
	 */
	public BigDecimal getProgramaServicioPlanTratamientoFK() {
		return programaServicioPlanTratamientoFK;
	}
	/**
	 * @param programaServicioPlanTratamientoFK the programaServicioPlanTratamientoFK to set
	 */
	public void setProgramaServicioPlanTratamientoFK(
			BigDecimal programaServicioPlanTratamientoFK) {
		this.programaServicioPlanTratamientoFK = programaServicioPlanTratamientoFK;
	}
	/**
	 * @return the programa
	 */
	public BigDecimal getPrograma() {
		return programa;
	}
	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(BigDecimal programa) {
		this.programa = programa;
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
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	} 
	
	

}
