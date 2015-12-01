package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * DTO PARA ADMINISTRAR EL LOG_PLAN TRATAMIENTO
 * @author Edgar Carvajal
 *
 */
public class DtoLogPrespuesto implements Serializable
{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private  BigDecimal codigoPk;
	private BigDecimal codigoPkPresupuesto;
	private BigDecimal codigoPkPlantTratamiento;
	private BigDecimal consecutivo; 
	private String estado ;
	private String fechaGeneracion;
	private String  horaGeneracion;
	private BigDecimal codigoPkLogPlanTratamiento; 

	
	
	public DtoLogPrespuesto()
	{
		this.codigoPk= BigDecimal.ZERO;
		this.codigoPkLogPlanTratamiento=BigDecimal.ZERO;
		this.codigoPkPlantTratamiento=BigDecimal.ZERO;
		this.codigoPkPresupuesto= BigDecimal.ZERO;
		this.consecutivo = BigDecimal.ZERO;
		this.estado="";
		this.horaGeneracion="";
		this.setFechaGeneracion("");
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
	 * @return the codigoPkPresupuesto
	 */
	public BigDecimal getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}



	/**
	 * @param codigoPkPresupuesto the codigoPkPresupuesto to set
	 */
	public void setCodigoPkPresupuesto(BigDecimal codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}



	/**
	 * @return the codigoPkPlantTratamiento
	 */
	public BigDecimal getCodigoPkPlantTratamiento() {
		return codigoPkPlantTratamiento;
	}



	/**
	 * @param codigoPkPlantTratamiento the codigoPkPlantTratamiento to set
	 */
	public void setCodigoPkPlantTratamiento(BigDecimal codigoPkPlantTratamiento) {
		this.codigoPkPlantTratamiento = codigoPkPlantTratamiento;
	}



	/**
	 * @return the consecutivo
	 */
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}



	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}



	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}



	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}



	



	/**
	 * @return the horaGeneracion
	 */
	public String getHoraGeneracion() {
		return horaGeneracion;
	}



	/**
	 * @param horaGeneracion the horaGeneracion to set
	 */
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}



	/**
	 * @return the codigoPkLogPlanTratamiento
	 */
	public BigDecimal getCodigoPkLogPlanTratamiento() {
		return codigoPkLogPlanTratamiento;
	}



	/**
	 * @param codigoPkLogPlanTratamiento the codigoPkLogPlanTratamiento to set
	 */
	public void setCodigoPkLogPlanTratamiento(BigDecimal codigoPkLogPlanTratamiento) {
		this.codigoPkLogPlanTratamiento = codigoPkLogPlanTratamiento;
	}



	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}



	public String getFechaGeneracion() {
		return fechaGeneracion;
	}
	
	
	
	
}
