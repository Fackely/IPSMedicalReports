/*
 * @(#)PacienteForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.actionform;

import org.apache.struts.validator.ValidatorForm;

/**
 * Forma para el anejo de la informacion adicional del paciente 
 * cargado en session en el cabezote. Se implenta esta calse para evitar interrumpir 
 * el manejo viejo que se venia haciendo con del paciente
 *
 *	@version 1.0, Dic 01, 2005
 */
public class PacienteForm extends ValidatorForm
{
	/**
	 * Entero con el codigo del paciente
	 */
	private int codigopaciente;
	
	/**
	 * Cadena para las observaciones
	 */
	private String observaciones;
	
	/**
	 * Estado del flujo
	 */
	private String estado;
	
	
	public void reset()
	{
		this.codigopaciente=0;
		this.observaciones="";
		this.estado="";
	}
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	/**
	 * @return Returns the codigopaciente.
	 */
	public int getCodigopaciente()
	{
		return codigopaciente;
	}
	/**
	 * @param codigopaciente The codigopaciente to set.
	 */
	public void setCodigopaciente(int codigopaciente)
	{
		this.codigopaciente=codigopaciente;
	}
	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones()
	{
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones=observaciones;
	}
}