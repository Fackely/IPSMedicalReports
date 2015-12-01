package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

public class DtoResultadoConsultaIndicadoresHospitalizacion implements Serializable{
	
	private String nombreConvenio;
	private String estadoEgreso;
	private String sexoPaciente;
	private int cantidadPacientes;
	
	/**
	 * Método constructor de la clase
	 */
	public DtoResultadoConsultaIndicadoresHospitalizacion() {
	}

	/**
	 * Método que retorna el valor del atributo nombreConvenio
	 * @return nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * Método que almacena el valor del atributo nombreConvenio
	 * @param nombreConvenio
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * Método que retorna el valor del atributo estadoEgreso
	 * @return estadoEgreso
	 */
	public String getEstadoEgreso() {
		return estadoEgreso;
	}

	/**
	 * Método que almacena el valor del atributo estadoEgreso
	 * @param estadoEgreso
	 */
	public void setEstadoEgreso(String estadoEgreso) {
		this.estadoEgreso = estadoEgreso;
	}

	/**
	 * Método que retorna el valor del atributo sexoPaciente
	 * @return sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}

	/**
	 * Método que almacena el valor del atributo sexoPaciente
	 * @param sexoPaciente
	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente = sexoPaciente;
	}

	/**
	 * Método que retorna el valor del atributo cantidadPacientes
	 * @return cantidadPacientes
	 */
	public int getCantidadPacientes() {
		return cantidadPacientes;
	}

	/**
	 * Método que almacena el valor del atributo cantidadPacientes
	 * @param cantidadPacientes
	 */
	public void setCantidadPacientes(int cantidadPacientes) {
		this.cantidadPacientes = cantidadPacientes;
	}

	
	
}
