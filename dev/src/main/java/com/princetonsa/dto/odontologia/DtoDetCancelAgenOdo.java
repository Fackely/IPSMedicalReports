/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoDetCancelAgenOdo implements Serializable{
	
	private int codigoPk;
	private String horaInicio;
	private String horaFin;
	private int citaOdontologica;
	private int codigoPaciente;
	private int cancelacionAgenda;
	private int numeroSolicitud;
	
	private String nomPaciente;	
	private String idPaciente;
	private String telefonoPaciente;
	
	private String hora;
	private String fecha;
	
	public DtoDetCancelAgenOdo(){
		this.reset();
	}
	
	/**
	 * m�todo que reinicia los atributos del dto
	 */
	public void reset(){
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.horaInicio = "";
		this.horaFin = "";
		this.citaOdontologica = ConstantesBD.codigoNuncaValido;
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.cancelacionAgenda= ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud= ConstantesBD.codigoNuncaValido;
		this.nomPaciente="";
		this.idPaciente="";
		this.telefonoPaciente="";
		this.hora="";
		this.fecha="";
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}

	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}

	public String getNomPaciente() {
		return nomPaciente;
	}

	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public int getCitaOdontologica() {
		return citaOdontologica;
	}

	public void setCitaOdontologica(int citaOdontologica) {
		this.citaOdontologica = citaOdontologica;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public int getCancelacionAgenda() {
		return cancelacionAgenda;
	}

	public void setCancelacionAgenda(int cancelacionAgenda) {
		this.cancelacionAgenda = cancelacionAgenda;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
}
