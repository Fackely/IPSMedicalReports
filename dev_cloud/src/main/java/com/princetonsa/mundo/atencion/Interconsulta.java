package com.princetonsa.mundo.atencion;

import util.TipoNumeroId;

/**
 * Contiene y maneja toda la información especifica a una interconsulta
 * @version 1.0, Noviembre 18, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class Interconsulta
{
	/**
	 * Número de la solicitud de interconsulta
	 */
	private int numeroSolicitud;
	
	/**
	 * Nombre completo del paciente al que corresponde esta inteconsulta
	 */
	private String nombrePaciente;
	
	/**
	 * Identificación del paciente
	 */
	private TipoNumeroId idPaciente;
	
	/**
	 * Nombre completo del médico que solicita la interconsulta
	 */
	private String nombreMedicoSolicitante;
	
	/**
	 * Fecha de solicitud de la interconsulta
	 */
	private String fechaSolicitud;
	
	/**
	 * Hora de solicitud de la interconsulta
	 */
	private String horaSolicitud;
	
	/**
	 * Especialidad de la solicitud
	 */
	private String especialidad;
	
	/**
	 * Creadora de la interconsulta. Inicializa los atributos
	 * @see java.lang.Object#Object()
	 */
	public Interconsulta()
	{
		this.nombreMedicoSolicitante = "";
		this.nombrePaciente = "";
		this.fechaSolicitud = "";
		this.horaSolicitud = "";
		this.especialidad = "";	
		this.idPaciente = new TipoNumeroId();
		this.numeroSolicitud = 0;
	}

	/**
	 * Creadora de la interconsulta. Inicializa los atributos con los dados.
	 * @param nombreMedicoSolicitante
	 * @param nombrePaciente
	 * @param fechaSolicitud
	 * @param horaSolicitud
	 * @param especialidad
	 * @param idPaciente
	 * @param numeroSolicitud
	 */	
	public Interconsulta(String nombreMedicoSolicitante, String nombrePaciente, String fechaSolicitud, String horaSolicitud, String especialidad, TipoNumeroId idPaciente, int numeroSolicitud)
	{
		this.nombreMedicoSolicitante = nombreMedicoSolicitante;
		this.nombrePaciente = nombrePaciente;
		this.idPaciente = idPaciente;
		this.fechaSolicitud = fechaSolicitud;
		this.horaSolicitud = horaSolicitud;
		this.especialidad = especialidad;	
		this.numeroSolicitud = numeroSolicitud;
	}
	
	/**
	 * Retorna la especialidad de la solicitud
	 * @return String, especialidad
	 */
	public String getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * Retorna la fecha de solicitud de la interconsulta
	 * @return String, fecha de solicitud
	 */
	public String getFechaSolicitud()
	{
		return fechaSolicitud;
	}

	/**
	 * Retorna el nombre completo del médico que solicita la interconsulta
	 * @return String, nombre médico solicitante
	 */
	public String getNombreMedicoSolicitante()
	{
		return nombreMedicoSolicitante;
	}

	/**
	 * Retorna el nombre completo del paciente al que corresponde esta
	 * inteconsulta
	 * @return String, nombre paciente
	 */
	public String getNombrePaciente()
	{
		return nombrePaciente;
	}

	/**
	 * Asigna la especialidad de la solicitud
	 * @param String, especialidad
	 */
	public void setEspecialidad(String especialidad)
	{
		this.especialidad = especialidad;
	}

	/**
	 * Asigna la fecha de solicitud de la interconsulta
	 * @param String, fechaSolicitud
	 */
	public void setFechaSolicitud(String fechaSolicitud)
	{
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * Asigna el nombre completo del médico que solicita la interconsulta
	 * @param String, nombreMedicoSolicitante
	 */
	public void setNombreMedicoSolicitante(String nombreMedicoSolicitante)
	{
		this.nombreMedicoSolicitante = nombreMedicoSolicitante;
	}

	/**
	 * Asigna el nombre completo del paciente al que corresponde esta
	 * inteconsulta
	 * @param String, nombrePaciente 
	 */
	public void setNombrePaciente(String nombrePaciente)
	{
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * Retorna la hora de solicitud de la interconsulta
	 * @return String, horaSolicitud
	 */
	public String getHoraSolicitud()
	{
		return horaSolicitud;
	}

	/**
	 * Asigna la hora de solicitud de la interconsulta
	 * @param String, horaSolicitud
	 */
	public void setHoraSolicitud(String horaSolicitud)
	{
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * Retorna la identificación del paciente
	 * @return TipoNumeroId
	 */
	public TipoNumeroId getIdPaciente()
	{
		return idPaciente;
	}

	/**
	 * Asigna la identificación del paciente
	 * @param idPaciente
	 */
	public void setIdPaciente(TipoNumeroId idPaciente)
	{
		this.idPaciente = idPaciente;
	}

	/**
	 * Retorna el número de la solicitud de interconsulta
	 * @return int, numeroSolicitud
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}

	/**
	 * Asigna el número de la solicitud de interconsulta
	 * @param int, numeroSolicitud 
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud = numeroSolicitud;
	}

}
