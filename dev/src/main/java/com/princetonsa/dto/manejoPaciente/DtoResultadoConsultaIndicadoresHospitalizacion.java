package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

public class DtoResultadoConsultaIndicadoresHospitalizacion implements Serializable{
	
	private String nombreConvenio;
	private String estadoEgreso;
	private String sexoPaciente;
	private int cantidadPacientes;
	
	/**
	 * M�todo constructor de la clase
	 */
	public DtoResultadoConsultaIndicadoresHospitalizacion() {
	}

	/**
	 * M�todo que retorna el valor del atributo nombreConvenio
	 * @return nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * M�todo que almacena el valor del atributo nombreConvenio
	 * @param nombreConvenio
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * M�todo que retorna el valor del atributo estadoEgreso
	 * @return estadoEgreso
	 */
	public String getEstadoEgreso() {
		return estadoEgreso;
	}

	/**
	 * M�todo que almacena el valor del atributo estadoEgreso
	 * @param estadoEgreso
	 */
	public void setEstadoEgreso(String estadoEgreso) {
		this.estadoEgreso = estadoEgreso;
	}

	/**
	 * M�todo que retorna el valor del atributo sexoPaciente
	 * @return sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}

	/**
	 * M�todo que almacena el valor del atributo sexoPaciente
	 * @param sexoPaciente
	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente = sexoPaciente;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadPacientes
	 * @return cantidadPacientes
	 */
	public int getCantidadPacientes() {
		return cantidadPacientes;
	}

	/**
	 * M�todo que almacena el valor del atributo cantidadPacientes
	 * @param cantidadPacientes
	 */
	public void setCantidadPacientes(int cantidadPacientes) {
		this.cantidadPacientes = cantidadPacientes;
	}

	
	
}
