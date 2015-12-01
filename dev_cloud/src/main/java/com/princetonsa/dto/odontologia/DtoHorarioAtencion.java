package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author V�ctor Hugo G�mez L.
 *
 */

public class DtoHorarioAtencion implements Serializable
{
	private int codigo;
	private int unidadConsulta;
	private int consultorio;
	private int dia;
	private int codigoMedico;
	private String horaInicio;
	private String horaFin;
	private int tiempoSesion;
	private int pacientesSesion;
	private int centroAtencion;
	private int convencion;
	private String nomCentroAtencion;
	private String colorUniAgenda;
	private String nombreUniAgenda;
	private String nombreConsultorio;
	private String nombreMedico;
	private String nombreDia;

	public DtoHorarioAtencion()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.unidadConsulta = ConstantesBD.codigoNuncaValido;
		this.consultorio = ConstantesBD.codigoNuncaValido;
		this.dia = ConstantesBD.codigoNuncaValido;
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.horaInicio = "" ;
		this.horaFin = "" ;
		this.tiempoSesion = ConstantesBD.codigoNuncaValido;
		this.pacientesSesion = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.convencion = ConstantesBD.codigoNuncaValido;
		this.nomCentroAtencion = "" ;
		this.colorUniAgenda = "" ;
		this.nombreConsultorio="";
		this.nombreMedico="";
		this.nombreDia="";
		this.nombreUniAgenda="";
	}


	public String getNombreUniAgenda() {
		return nombreUniAgenda;
	}

	public void setNombreUniAgenda(String nombreUniAgenda) {
		this.nombreUniAgenda = nombreUniAgenda;
	}

	public String getNombreDia() {
		return nombreDia;
	}

	public void setNombreDia(String nombreDia) {
		this.nombreDia = nombreDia;
	}

	public String getNombreMedico() {
		return nombreMedico;
	}

	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}

	public String getNombreConsultorio() {
		return nombreConsultorio;
	}

	public void setNombreConsultorio(String nombreConsultorio) {
		this.nombreConsultorio = nombreConsultorio;
	}

	public String getColorUniAgenda() {
		return colorUniAgenda;
	}

	public void setColorUniAgenda(String colorUniAgenda) {
		this.colorUniAgenda = colorUniAgenda;
	}

	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	public String getNomCentroAtencion() {
		return nomCentroAtencion;
	}

	public void setNomCentroAtencion(String nomCentroAtencion) {
		this.nomCentroAtencion = nomCentroAtencion;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the consultorio
	 */
	public int getConsultorio() {
		return consultorio;
	}

	/**
	 * @param consultorio the consultorio to set
	 */
	public void setConsultorio(int consultorio) {
		this.consultorio = consultorio;
	}

	/**
	 * @return the dia
	 */
	public int getDia() {
		return dia;
	}

	/**
	 * @param dia the dia to set
	 */
	public void setDia(int dia) {
		this.dia = dia;
	}

	/**
	 * @return the unidadConsulta
	 */
	public int getUnidadConsulta() {
		return unidadConsulta;
	}

	/**
	 * @param unidadConsulta the unidadConsulta to set
	 */
	public void setUnidadConsulta(int unidadConsulta) {
		this.unidadConsulta = unidadConsulta;
	}

	/**
	 * @return the codigoMedico
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return the horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio the horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return the horaFin
	 */
	public String getHoraFin() {
		return horaFin;
	}

	/**
	 * @param horaFin the horaFin to set
	 */
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	/**
	 * @return the tiempoSesion
	 */
	public int getTiempoSesion() {
		return tiempoSesion;
	}

	/**
	 * @param tiempoSesion the tiempoSesion to set
	 */
	public void setTiempoSesion(int tiempoSesion) {
		this.tiempoSesion = tiempoSesion;
	}

	/**
	 * @return the pacientesSesion
	 */
	public int getPacientesSesion() {
		return pacientesSesion;
	}

	/**
	 * @param pacientesSesion the pacientesSesion to set
	 */
	public void setPacientesSesion(int pacientesSesion) {
		this.pacientesSesion = pacientesSesion;
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
}
