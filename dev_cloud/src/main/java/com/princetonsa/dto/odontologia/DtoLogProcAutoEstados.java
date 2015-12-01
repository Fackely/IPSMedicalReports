package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoLogProcAutoEstados implements Serializable 
{
	
	private double codigoPk;
	private String inconsistencia ;
	private String fecha ;
	private String hora  ;
	private int presupuesto;
	private int planTratamiento;
	private int institucion;
	
	
	
	
	void reset()
	{
	  codigoPk = ConstantesBD.codigoNuncaValidoDouble;
	  inconsistencia = "";
	  fecha = "";
	  hora = "";
	  presupuesto = ConstantesBD.codigoNuncaValido;
	  planTratamiento = ConstantesBD.codigoNuncaValido;
	  institucion =  ConstantesBD.codigoNuncaValido;
	  
	}



	/**
	 * @return the codigoPk
	 */
	public double getCodigoPk() {
		return codigoPk;
	}



	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}



	/**
	 * @return the inconsistencia
	 */
	public String getInconsistencia() {
		return inconsistencia;
	}



	/**
	 * @param inconsistencia the inconsistencia to set
	 */
	public void setInconsistencia(String inconsistencia) {
		this.inconsistencia = inconsistencia;
	}



	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}



	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}



	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}



	/**
	 * @return the presupuesto
	 */
	public int getPresupuesto() {
		return presupuesto;
	}



	/**
	 * @param presupuesto the presupuesto to set
	 */
	public void setPresupuesto(int presupuesto) {
		this.presupuesto = presupuesto;
	}



	/**
	 * @return the planTratamiento
	 */
	public int getPlanTratamiento() {
		return planTratamiento;
	}



	/**
	 * @param planTratamiento the planTratamiento to set
	 */
	public void setPlanTratamiento(int planTratamiento) {
		this.planTratamiento = planTratamiento;
	}



	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}



	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	
	}
	
	
  	
	


