package com.princetonsa.dto.agenda;

import java.io.Serializable;

import util.ConstantesBD;

public class DTOAdministrarSolicitudesAutorizar implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**Atributo que almacena el numero de la solicitud generada*/
	private int numeroSolicitudCitaAutorizar;
	
	/**Atributo que almacena el codigo del servicio de la solicitud generada*/
	private int codigoServicioCitaAutorizar;
	
	/**Atributo que almacena el codigo del convenio de la solicitud generada*/
	private String codigoConvenioServicioAutorizar;
	
	/**Atributo que almacena el nombre del servicio de la solicitud generada*/
	private String nombreServicioCitaAutorizar;
	
	/**Atributo que almacena el campo urgente de la solicitud generada*/
	private char urgente;
	
	/**Atributo que almacena el consecutivo de la solicitud generada*/
	private String consecutivoCitaAutorizar;
	
	/**Atributo que almacena el centro de costo solicitado*/
	private int centroCostoSolicitado;
	
	/**Atributo que indica el tipo de solicitud de la consulta (Interconsulta-Procedimientos-NoCruentos)*/
	private int tipoSolicitudCita;
	
	/**
	 * Metodo constructor de la clase
	 */
	public DTOAdministrarSolicitudesAutorizar(){
		this.setNumeroSolicitudCitaAutorizar(ConstantesBD.codigoNuncaValido);
		this.setCodigoServicioCitaAutorizar(ConstantesBD.codigoNuncaValido);
		this.setCodigoConvenioServicioAutorizar("");
	}

	
	/**
	 * @param codigoServicioCitaAutorizar
	 */
	public void setCodigoServicioCitaAutorizar(int codigoServicioCitaAutorizar) {
		this.codigoServicioCitaAutorizar = codigoServicioCitaAutorizar;
	}

	/**
	 * @return codigoServicioCitaAutorizar
	 */
	public int getCodigoServicioCitaAutorizar() {
		return codigoServicioCitaAutorizar;
	}

	/**
	 * @param numeroSolicitudCitaAutorizar
	 */
	public void setNumeroSolicitudCitaAutorizar(int numeroSolicitudCitaAutorizar) {
		this.numeroSolicitudCitaAutorizar = numeroSolicitudCitaAutorizar;
	}

	/**
	 * @return numeroSolicitudCitaAutorizar
	 */
	public int getNumeroSolicitudCitaAutorizar() {
		return numeroSolicitudCitaAutorizar;
	}

	/**
	 * @return codigoConvenioServicioAutorizar
	 */
	public String getCodigoConvenioServicioAutorizar() {
		return codigoConvenioServicioAutorizar;
	}

	/**
	 * @param codigoConvenioServicioAutorizar
	 */
	public void setCodigoConvenioServicioAutorizar(
			String codigoConvenioServicioAutorizar) {
		this.codigoConvenioServicioAutorizar = codigoConvenioServicioAutorizar;
	}

	/**
	 * @return nombreServicioCitaAutorizar
	 */
	public String getNombreServicioCitaAutorizar() {
		return nombreServicioCitaAutorizar;
	}

	/**
	 * @param nombreServicioCitaAutorizar
	 */
	public void setNombreServicioCitaAutorizar(String nombreServicioCitaAutorizar) {
		this.nombreServicioCitaAutorizar = nombreServicioCitaAutorizar;
	}

	/**
	 * @return urgente
	 */
	public char getUrgente() {
		return urgente;
	}

	/**
	 * @param urgente
	 */
	public void setUrgente(char urgente) {
		this.urgente = urgente;
	}

	/**
	 * @return consecutivoCitaAutorizar
	 */
	public String getConsecutivoCitaAutorizar() {
		return consecutivoCitaAutorizar;
	}

	/**
	 * @param consecutivoCitaAutorizar
	 */
	public void setConsecutivoCitaAutorizar(String consecutivoCitaAutorizar) {
		this.consecutivoCitaAutorizar = consecutivoCitaAutorizar;
	}

	/**
	 * @return centroCostoSolicitado
	 */
	public int getCentroCostoSolicitado() {
		return centroCostoSolicitado;
	}

	/**
	 * @param centroCostoSolicitado
	 */
	public void setCentroCostoSolicitado(int centroCostoSolicitado) {
		this.centroCostoSolicitado = centroCostoSolicitado;
	}

	/**
	 * @return tipoSolicitudCita
	 */
	public int getTipoSolicitudCita() {
		return tipoSolicitudCita;
	}

	/**
	 * @param tipoSolicitudCita
	 */
	public void setTipoSolicitudCita(int tipoSolicitudCita) {
		this.tipoSolicitudCita = tipoSolicitudCita;
	}

}
