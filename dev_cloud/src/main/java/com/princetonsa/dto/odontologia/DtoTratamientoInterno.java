package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoTratamientoInterno implements Serializable{

	
	
	private int codigoAnteOdo;
	private String fechaInicio;
	private String fechaFinal;
	private int codPrograma;
	private String nombrePrograma;
	private int codServicio;
	private String nombreServicio;
	private int codigoPiezaDen;
	private String descripcionPiezaDen;
	private int codigoEspecialidad;
	private String descripcionEsp;
	private int codigoProgSerPlanTrat;
	
	
	
	public DtoTratamientoInterno()
	{
		this.reset();
	}
	
	public void reset()
	{
		
		this.codigoAnteOdo = ConstantesBD.codigoNuncaValido;
		this.codigoProgSerPlanTrat= ConstantesBD.codigoNuncaValido;
		this.fechaInicio = new String("");
		this.fechaFinal = new String("");
		this.codigoPiezaDen = ConstantesBD.codigoNuncaValido;
		this.descripcionPiezaDen = new String("");
		this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		this.descripcionEsp = new String("");
		this.codPrograma =  ConstantesBD.codigoNuncaValido;;
	    this.nombrePrograma = new String("");
		this.codServicio=  ConstantesBD.codigoNuncaValido;;
		this.nombreServicio = new String("");
	}

	/**
	 * @return the codigoAnteOdo
	 */
	public int getCodigoAnteOdo() {
		return codigoAnteOdo;
	}

	/**
	 * @param codigoAnteOdo the codigoAnteOdo to set
	 */
	public void setCodigoAnteOdo(int codigoAnteOdo) {
		this.codigoAnteOdo = codigoAnteOdo;
	}

	/**
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	

	/**
	 * @return the nombrePrograma
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * @param nombrePrograma the nombrePrograma to set
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	

	/**
	 * @return the nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * @param nombreServicio the nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * @return the codigoPiezaDen
	 */
	public int getCodigoPiezaDen() {
		return codigoPiezaDen;
	}

	/**
	 * @param codigoPiezaDen the codigoPiezaDen to set
	 */
	public void setCodigoPiezaDen(int codigoPiezaDen) {
		this.codigoPiezaDen = codigoPiezaDen;
	}

	/**
	 * @return the descripcionPiezaDen
	 */
	public String getDescripcionPiezaDen() {
		return descripcionPiezaDen;
	}

	/**
	 * @param descripcionPiezaDen the descripcionPiezaDen to set
	 */
	public void setDescripcionPiezaDen(String descripcionPiezaDen) {
		this.descripcionPiezaDen = descripcionPiezaDen;
	}

	/**
	 * @return the codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * @return the descripcionEsp
	 */
	public String getDescripcionEsp() {
		return descripcionEsp;
	}

	/**
	 * @param descripcionEsp the descripcionEsp to set
	 */
	public void setDescripcionEsp(String descripcionEsp) {
		this.descripcionEsp = descripcionEsp;
	}

	/**
	 * @return the codigoProgSerPlanTrat
	 */
	public int getCodigoProgSerPlanTrat() {
		return codigoProgSerPlanTrat;
	}

	/**
	 * @param codigoProgSerPlanTrat the codigoProgSerPlanTrat to set
	 */
	public void setCodigoProgSerPlanTrat(int codigoProgSerPlanTrat) {
		this.codigoProgSerPlanTrat = codigoProgSerPlanTrat;
	}

	/**
	 * @return the codPrograma
	 */
	public int getCodPrograma() {
		return codPrograma;
	}

	/**
	 * @param codPrograma the codPrograma to set
	 */
	public void setCodPrograma(int codPrograma) {
		this.codPrograma = codPrograma;
	}

	/**
	 * @return the codServicio
	 */
	public int getCodServicio() {
		return codServicio;
	}

	/**
	 * @param codServicio the codServicio to set
	 */
	public void setCodServicio(int codServicio) {
		this.codServicio = codServicio;
	}
	
	
}
