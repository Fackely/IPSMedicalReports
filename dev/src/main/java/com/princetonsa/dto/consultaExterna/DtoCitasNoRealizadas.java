package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoCitasNoRealizadas implements Serializable{

	private String unidadAgenda;
	private int codUnidadAgenda;
	private String fechaCita;
	private String horaCita;
	private int codEstadoCita;
	private String estadoCita;
	private String profesional;
	private int codProfesional;
	private int codCentroAtencion;
	private String nombreCentroAtencion;
	private String idPaciente;
	private int codPaciente;
	private String chkEstado =  ConstantesBD.acronimoNo;
	private String chkMulta =  ConstantesBD.acronimoNo;
	private String chkFactura =  ConstantesBD.acronimoNo;
	private String nombrePaciente;
	private String consultorio;
	private int codAgenda;
	private String chkMultaActivo;
	private String valMulta;
	private String conManMulta;
	private int codCita;

	public void resset() {
		this.setCodCita(ConstantesBD.codigoNuncaValido);
		this.unidadAgenda = new String("");
		this.codUnidadAgenda = ConstantesBD.codigoNuncaValido;
		this.fechaCita = new String("");
		this.horaCita = new String("");
		this.codEstadoCita = ConstantesBD.codigoNuncaValido;
		this.estadoCita = new String("");
		this.profesional = new String("");
		this.codProfesional = ConstantesBD.codigoNuncaValido;
		this.codCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencion = new String("");
		this.idPaciente = new String("");
		this.codPaciente = ConstantesBD.codigoNuncaValido;
		this.chkEstado = new String("");
		this.chkMulta = new String("");
		this.chkFactura = new String("");
		this.setConsultorio("");
		this.setCodAgenda(ConstantesBD.codigoNuncaValido);
		this.setChkMultaActivo("");
		this.setValMulta(new String(""));
		this.setConManMulta(new String(""));
	}

	/**
	 * @return the unidadAgenda
	 */
	public String getUnidadAgenda() {
		return unidadAgenda;
	}

	/**
	 * @param unidadAgenda
	 *            the unidadAgenda to set
	 */
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the codUnidadAgenda
	 */
	public int getCodUnidadAgenda() {
		return codUnidadAgenda;
	}

	/**
	 * @param codUnidadAgenda
	 *            the codUnidadAgenda to set
	 */
	public void setCodUnidadAgenda(int codUnidadAgenda) {
		this.codUnidadAgenda = codUnidadAgenda;
	}

	/**
	 * @return the fechaCita
	 */
	public String getFechaCita() {
		return fechaCita;
	}

	/**
	 * @param fechaCita
	 *            the fechaCita to set
	 */
	public void setFechaCita(String fechaCita) {
		this.fechaCita = fechaCita;
	}

	/**
	 * @return the horaCita
	 */
	public String getHoraCita() {
		return horaCita;
	}

	/**
	 * @param horaCita
	 *            the horaCita to set
	 */
	public void setHoraCita(String horaCita) {
		this.horaCita = horaCita;
	}

	/**
	 * @return the codEstadoCita
	 */
	public int getCodEstadoCita() {
		return codEstadoCita;
	}

	/**
	 * @param codEstadoCita
	 *            the codEstadoCita to set
	 */
	public void setCodEstadoCita(int codEstadoCita) {
		this.codEstadoCita = codEstadoCita;
	}

	/**
	 * @return the estadoCita
	 */
	public String getEstadoCita() {
		return estadoCita;
	}

	/**
	 * @param estadoCita
	 *            the estadoCita to set
	 */
	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	/**
	 * @return the profesional
	 */
	public String getProfesional() {
		return profesional;
	}

	/**
	 * @param profesional
	 *            the profesional to set
	 */
	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}

	/**
	 * @return the codProfesional
	 */
	public int getCodProfesional() {
		return codProfesional;
	}

	/**
	 * @param codProfesional
	 *            the codProfesional to set
	 */
	public void setCodProfesional(int codProfesional) {
		this.codProfesional = codProfesional;
	}

	/**
	 * @return the codCentroAtencion
	 */
	public int getCodCentroAtencion() {
		return codCentroAtencion;
	}

	/**
	 * @param codCentroAtencion
	 *            the codCentroAtencion to set
	 */
	public void setCodCentroAtencion(int codCentroAtencion) {
		this.codCentroAtencion = codCentroAtencion;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion
	 *            the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the idPaciente
	 */
	public String getIdPaciente() {
		return idPaciente;
	}

	/**
	 * @param idPaciente
	 *            the idPaciente to set
	 */
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	/**
	 * @return the codPaciente
	 */
	public int getCodPaciente() {
		return codPaciente;
	}

	/**
	 * @param codPaciente
	 *            the codPaciente to set
	 */
	public void setCodPaciente(int codPaciente) {
		this.codPaciente = codPaciente;
	}

	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param nombrePaciente
	 *            the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * @return the chkEstado
	 */
	public String getChkEstado() {
		return chkEstado;
	}

	/**
	 * @param chkEstado the chkEstado to set
	 */
	public void setChkEstado(String chkEstado) {
		this.chkEstado = chkEstado;
	}

	/**
	 * @return the chkMulta
	 */
	public String getChkMulta() {
		return chkMulta;
	}

	/**
	 * @param chkMulta the chkMulta to set
	 */
	public void setChkMulta(String chkMulta) {
		this.chkMulta = chkMulta;
	}

	/**
	 * @return the chkFactura
	 */
	public String getChkFactura() {
		return chkFactura;
	}

	/**
	 * @param chkFactura the chkFactura to set
	 */
	public void setChkFactura(String chkFactura) {
		this.chkFactura = chkFactura;
	}

	/**
	 * @param consultorio the consultorio to set
	 */
	public void setConsultorio(String consultorio) {
		this.consultorio = consultorio;
	}

	/**
	 * @return the consultorio
	 */
	public String getConsultorio() {
		return consultorio;
	}

	/**
	 * @param codAgenda the codAgenda to set
	 */
	public void setCodAgenda(int codAgenda) {
		this.codAgenda = codAgenda;
	}

	/**
	 * @return the codAgenda
	 */
	public int getCodAgenda() {
		return codAgenda;
	}

	/**
	 * @param chkMultaActivo the chkMultaActivo to set
	 */
	public void setChkMultaActivo(String chkMultaActivo) {
		this.chkMultaActivo = chkMultaActivo;
	}

	/**
	 * @return the chkMultaActivo
	 */
	public String getChkMultaActivo() {
		return chkMultaActivo;
	}

	/**
	 * @param valMulta the valMulta to set
	 */
	public void setValMulta(String valMulta) {
		this.valMulta = valMulta;
	}

	/**
	 * @return the valMulta
	 */
	public String getValMulta() {
		return valMulta;
	}

	/**
	 * @param conManMulta the conManMulta to set
	 */
	public void setConManMulta(String conManMulta) {
		this.conManMulta = conManMulta;
	}

	/**
	 * @return the conManMulta
	 */
	public String getConManMulta() {
		return conManMulta;
	}

	/**
	 * @param codCita the codCita to set
	 */
	public void setCodCita(int codCita) {
		this.codCita = codCita;
	}

	/**
	 * @return the codCita
	 */
	public int getCodCita() {
		return codCita;
	}

}