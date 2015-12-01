package com.princetonsa.dto.pyp;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Gio
 */
public class DtoObservacionProgramaPYP implements Serializable 
{
	/**
	 * 
	 */
	private int paciente;
	
	/**
	 * 
	 */
	private int programa;
	
	/**
	 * 
	 */
	private String observacion;
	
	/**
	 * 
	 */
	private String usuario;
    
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String fechaGrabacion;
    
	/**
	 * 
	 */
	private String horaGrabacion;
	
	/**
	 * 
	 */
	private String registroMedico;

	/**
	 * 
	 *
	 */
	public DtoObservacionProgramaPYP() 
	{
		this.paciente = ConstantesBD.codigoNuncaValido;
		this.programa = ConstantesBD.codigoNuncaValido;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.observacion = "";
		this.usuario = "";
		this.fechaGrabacion = "";
		this.horaGrabacion = "";
		this.registroMedico="";
	}

	/**
	 * @return the paciente
	 */
	public int getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(int paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the programa
	 */
	public int getPrograma() {
		return programa;
	}

	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(int programa) {
		this.programa = programa;
	}

	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	/**
	 * @return the fechaGrabacion
	 */
	public String getFechaGrabacion() {
		return fechaGrabacion;
	}

	/**
	 * @param fechaGrabacion the fechaGrabacion to set
	 */
	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	/**
	 * @return the horaGrabacion
	 */
	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	/**
	 * @param horaGrabacion the horaGrabacion to set
	 */
	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	/**
	 * @return the registroMedico
	 */
	public String getRegistroMedico() {
		return registroMedico;
	}

	/**
	 * @param registroMedico the registroMedico to set
	 */
	public void setRegistroMedico(String registroMedico) {
		this.registroMedico = registroMedico;
	}

	
}
