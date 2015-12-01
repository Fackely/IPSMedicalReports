package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoImpresionCitaOdontologica implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String razonSocial;
	
	private String nit;
	
	private String centroAtencion;
	
	private int codigoCita;
	
	private String fechaCita;

	private String horaCita;
	
	private String nombrePaciente;
	
	private String identificacionPaciente;
	
	private String tipoIdentificacion;
	
	private String nombreProfesional;

	private String tarjeta;

	private String nombreEspecialidad;

	private boolean imprimirColumHeader;
	
	private boolean imprimirDetalle;
	
	/** Objeto jasper para el listado de servicios */
    private JRDataSource dsServicios;
	
    /**
	 * 
	 */
	public DtoImpresionCitaOdontologica() {
		// TODO Auto-generated constructor stub
	}
    
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo FechaCita
	 * 
	 * @return  Retorna la variable FechaCita
	 */
	public String getFechaCita() {
		return fechaCita;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo FechaCita
	 * 
	 * @param  valor para el atributo FechaCita 
	 */
	public void setFechaCita (String fechaCita) {
		this.fechaCita = fechaCita;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreProfesional
	 * 
	 * @return  Retorna la variable nombreProfesional
	 */
	public String getNombreProfesional() {
		return nombreProfesional;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreProfesional
	 * 
	 * @param  valor para el atributo nombreProfesional 
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreEspecialidad
	 * 
	 * @param  valor para el atributo nombreEspecialidad 
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}
	
	
	/**
	 * @return the nit
	 */
	public String getNit() {
		return nit;
	}
	/**
	 * @param nit the nit to set
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}
	/**
	 * @return the codigoCita
	 */
	public int getCodigoCita() {
		return codigoCita;
	}
	/**
	 * @param codigoCita the codigoCita to set
	 */
	public void setCodigoCita(int codigoCita) {
		this.codigoCita = codigoCita;
	}
	/**
	 * @return the horaCita
	 */
	public String getHoraCita() {
		return horaCita;
	}
	/**
	 * @param horaCita the horaCita to set
	 */
	public void setHoraCita(String horaCita) {
		this.horaCita = horaCita;
	}
	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}
	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}
	/**
	 * @return the identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}
	/**
	 * @param identificacionPaciente the identificacionPaciente to set
	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}
	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	/**
	 * @return the tarjeta
	 */
	public String getTarjeta() {
		return tarjeta;
	}
	/**
	 * @param tarjeta the tarjeta to set
	 */
	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param dsServicios the dsServicios to set
	 */
	public void setDsServicios(JRDataSource dsServicios) {
		this.dsServicios = dsServicios;
	}
	/**
	 * @return the dsServicios
	 */
	public JRDataSource getDsServicios() {
		return dsServicios;
	}
	/**
	 * @param imprimirColumHeader the imprimirColumHeader to set
	 */
	public void setImprimirColumHeader(boolean imprimirColumHeader) {
		this.imprimirColumHeader = imprimirColumHeader;
	}
	/**
	 * @return the imprimirColumHeader
	 */
	public boolean isImprimirColumHeader() {
		return imprimirColumHeader;
	}

	/**
	 * @param imprimirDetalle the imprimirDetalle to set
	 */
	public void setImprimirDetalle(boolean imprimirDetalle) {
		this.imprimirDetalle = imprimirDetalle;
	}

	/**
	 * @return the imprimirDetalle
	 */
	public boolean isImprimirDetalle() {
		return imprimirDetalle;
	}
}
