/*
 * Nov 18, 2008
 */
package com.princetonsa.dto.ordenesmedicas;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;


/**
 * Data Transfer Object para almacenar los datos de las fecha/hora inicio
 * de la prescripción dialisis
 * @author Sebastián Gómez R.
 *
 */
public class DtoPrescripDialFechaHora implements Serializable
{
	private String consecutivo;
	private String fechaInicialDialisis;
	private String horaInicialDialisis;
	private UsuarioBasico profesional;
	
	
	/**
	 * Método para limpiar los datos del DTO
	 */
	public void clean()
	{
		this.consecutivo = "";
		this.fechaInicialDialisis = "";
		this.horaInicialDialisis = "";
		this.profesional = new UsuarioBasico();
	}
	
	/**
	 * El constructor
	 */
	public DtoPrescripDialFechaHora()
	{
		this.clean();
	}


	/**
	 * @return the fechaInicialDialisis
	 */
	public String getFechaInicialDialisis() {
		return fechaInicialDialisis;
	}


	/**
	 * @param fechaInicialDialisis the fechaInicialDialisis to set
	 */
	public void setFechaInicialDialisis(String fechaInicialDialisis) {
		this.fechaInicialDialisis = fechaInicialDialisis;
	}


	/**
	 * @return the horaInicialDialisis
	 */
	public String getHoraInicialDialisis() {
		return horaInicialDialisis;
	}


	/**
	 * @param horaInicialDialisis the horaInicialDialisis to set
	 */
	public void setHoraInicialDialisis(String horaInicialDialisis) {
		this.horaInicialDialisis = horaInicialDialisis;
	}


	/**
	 * @return the profesional
	 */
	public UsuarioBasico getProfesional() {
		return profesional;
	}


	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(UsuarioBasico profesional) {
		this.profesional = profesional;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
}
