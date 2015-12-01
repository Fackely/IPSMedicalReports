package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Dto que contiene la informaci�n del LOG de base de datos en donde se
 * deben registrar los Profesionales de la Salud a los cuales no se les va a
 * generar Valor de Honorarios
 * 
 * @author Luis Fernando Hincapi� Ospina
 * 
 */
public class DtoProfesionalSaludSinValorHonorarios implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date fechaLiquidacionHonorarios;
	private String horaLiquidacionHonorarios;
	private String nombreProfesional;
	private int numeroSolicitud;
	private int codigoServicio;

	/**
	 * Constructor
	 */
	public DtoProfesionalSaludSinValorHonorarios() {
		this.fechaLiquidacionHonorarios = new Date();
		this.nombreProfesional = "";
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * fechaLiquidacionHonorarios.
	 * 
	 * @return fechaLiquidacionHonorarios
	 */
	public Date getFechaLiquidacionHonorarios() {
		return fechaLiquidacionHonorarios;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * fechaLiquidacionHonorarios.
	 * 
	 * @param fechaLiquidacionHonorarios
	 */
	public void setFechaLiquidacionHonorarios(Date fechaLiquidacionHonorarios) {
		this.fechaLiquidacionHonorarios = fechaLiquidacionHonorarios;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * horaLiquidacionHonorarios.
	 * 
	 * @return horaLiquidacionHonorarios
	 */
	public String getHoraLiquidacionHonorarios() {
		return horaLiquidacionHonorarios;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * horaLiquidacionHonorarios.
	 * 
	 * @param horaLiquidacionHonorarios
	 */
	public void setHoraLiquidacionHonorarios(String horaLiquidacionHonorarios) {
		this.horaLiquidacionHonorarios = horaLiquidacionHonorarios;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * nombreProfesional.
	 * 
	 * @return nombreProfesional
	 */
	public String getNombreProfesional() {
		return nombreProfesional;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * nombreProfesional.
	 * 
	 * @param nombreProfesional
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo numeroSolicitud.
	 * 
	 * @return numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * numeroSolicitud.
	 * 
	 * @param numeroSolicitud
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo codigoServicio.
	 * 
	 * @return codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * codigoServicio.
	 * 
	 * @param codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

}
