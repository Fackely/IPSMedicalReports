package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.Date;

import util.UtilidadFecha;
import util.Utilidades;

/**
 * Fechas y horas iniciales y fechas y horas finales para cualquier proceso
 *  
 * @author Cristhian Murillo
*/
public class DtoFechaInicioFechaFin implements Serializable
{

	/** * Serial  */
	private static final long serialVersionUID = 1L;
	
	/** * Fecha Inicio */
	private Date fechaInicio;
	
	/** * Hora Inicio */
	private String horaInicio;

	/** * Fecha Fin */
	private Date fechaFin;
	
	/** * Hora Fin */
	private String horaFin;

	
	/**
	 * Constructor de la clase
	 */
	public DtoFechaInicioFechaFin() 
	{
		this.fechaInicio	= null;
		this.horaInicio		= null;
		this.fechaFin		= null;
		this.horaFin		= null;
		
	}

	
	/** * Inicializa los datos */
	public void iniciarDatos() 
	{
		this.fechaInicio	= UtilidadFecha.conversionFormatoFechaStringDate(Utilidades.capturarFechaBD());
		this.horaInicio		= UtilidadFecha.getHoraActual();
	}
	
	
	/** * Finaliza los datos */
	public void finalizarDatos() 
	{
		this.fechaFin	= UtilidadFecha.conversionFormatoFechaStringDate(Utilidades.capturarFechaBD());
		this.horaFin	= UtilidadFecha.getHoraActual();
	}
	
	

	/**
	 * @return valor de fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}


	/**
	 * @param fechaInicio el fechaInicio para asignar
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	/**
	 * @return valor de horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}


	/**
	 * @param horaInicio el horaInicio para asignar
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}


	/**
	 * @return valor de fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}


	/**
	 * @param fechaFin el fechaFin para asignar
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}


	/**
	 * @return valor de horaFin
	 */
	public String getHoraFin() {
		return horaFin;
	}


	/**
	 * @param horaFin el horaFin para asignar
	 */
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	
}
