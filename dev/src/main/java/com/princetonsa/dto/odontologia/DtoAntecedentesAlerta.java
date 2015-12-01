package com.princetonsa.dto.odontologia;

import java.io.Serializable;

/**
 * @author Víctor Hugo Gómez L.
 */

public class DtoAntecedentesAlerta implements Serializable
{
	private String nombreMedico;
	private String fecha;
	private String hora;
	private String antAlertaEtiqueta;
	private String antAlertaNombre;
	
	public DtoAntecedentesAlerta()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.nombreMedico = "";
		this.fecha = "";
		this.hora = "";
		this.antAlertaEtiqueta = "";
		this.antAlertaNombre = "";
	}

	/**
	 * @return the nombreMedico
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}

	/**
	 * @param nombreMedico the nombreMedico to set
	 */
	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
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
	 * @return the antAlertaEtiqueta
	 */
	public String getAntAlertaEtiqueta() {
		return antAlertaEtiqueta;
	}

	/**
	 * @param antAlertaEtiqueta the antAlertaEtiqueta to set
	 */
	public void setAntAlertaEtiqueta(String antAlertaEtiqueta) {
		this.antAlertaEtiqueta = antAlertaEtiqueta;
	}

	/**
	 * @return the antAlertaNombre
	 */
	public String getAntAlertaNombre() {
		return antAlertaNombre;
	}

	/**
	 * @param antAlertaNombre the antAlertaNombre to set
	 */
	public void setAntAlertaNombre(String antAlertaNombre) {
		this.antAlertaNombre = antAlertaNombre;
	}
	
}
