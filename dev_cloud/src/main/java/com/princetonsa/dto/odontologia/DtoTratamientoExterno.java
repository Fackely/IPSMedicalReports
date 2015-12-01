package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Víctor Hugo gómez L.
 */

public class DtoTratamientoExterno implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private int codigoAnteOdo;
	private String fechaInicio;
	private String fechaFinal;
	private String programaServicio;
	private int codigoPiezaDen;
	private String descripcionPiezaDen;
	private int codigoEspecialidad;
	private String descripcionEsp;
	
	//Atributos validacion
	private String eliminar;
	private String modificar;
	private String nuevo;
	
	public DtoTratamientoExterno()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoAnteOdo = ConstantesBD.codigoNuncaValido;
		this.fechaInicio = "";
		this.fechaFinal = "";
		this.programaServicio = "";
		this.codigoPiezaDen = ConstantesBD.codigoNuncaValido;
		this.descripcionPiezaDen = "";
		this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		this.descripcionEsp = "";
		
		//Atributos validacion
		this.eliminar = ConstantesBD.acronimoNo;
		this.modificar = ConstantesBD.acronimoNo;
		this.nuevo = ConstantesBD.acronimoNo;
	}

	/**
	 * @return the fecghaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fecghaInicio the fecghaInicio to set
	 */
	public void setFechaInicio(String fecghaInicio) {
		this.fechaInicio = fecghaInicio;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the codigoPiezaDen
	 */
	public int getCodigoPiezaDen() {
		return codigoPiezaDen;
	}

	/**
	 * @param codigoPiezaDen the codigoPiezaDen to set
	 */
	public void setCodigoPiezaDen(int codigoPiezaDen) {
		this.codigoPiezaDen = codigoPiezaDen;
	}

	/**
	 * @return the descripcionPiezaDen
	 */
	public String getDescripcionPiezaDen() {
		return descripcionPiezaDen;
	}

	/**
	 * @param descripcionPiezaDen the descripcionPiezaDen to set
	 */
	public void setDescripcionPiezaDen(String descripcionPiezaDen) {
		this.descripcionPiezaDen = descripcionPiezaDen;
	}

	/**
	 * @return the codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * @return the descripcionEsp
	 */
	public String getDescripcionEsp() {
		return descripcionEsp;
	}

	/**
	 * @param descripcionEsp the descripcionEsp to set
	 */
	public void setDescripcionEsp(String descripcionEsp) {
		this.descripcionEsp = descripcionEsp;
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigoAnteOdo
	 */
	public int getCodigoAnteOdo() {
		return codigoAnteOdo;
	}

	/**
	 * @param codigoAnteOdo the codigoAnteOdo to set
	 */
	public void setCodigoAnteOdo(int codigoAnteOdo) {
		this.codigoAnteOdo = codigoAnteOdo;
	}

	/**
	 * @return the eliminar
	 */
	public String getEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	/**
	 * @return the modificar
	 */
	public String getModificar() {
		return modificar;
	}

	/**
	 * @param modificar the modificar to set
	 */
	public void setModificar(String modificar) {
		this.modificar = modificar;
	}

	/**
	 * @return the nuevo
	 */
	public String getNuevo() {
		return nuevo;
	}

	/**
	 * @param nuevo the nuevo to set
	 */
	public void setNuevo(String nuevo) {
		this.nuevo = nuevo;
	}

	/**
	 * @return the programaServicio
	 */
	public String getProgramaServicio() {
		return programaServicio;
	}

	/**
	 * @param programaServicio the programaServicio to set
	 */
	public void setProgramaServicio(String programaServicio) {
		this.programaServicio = programaServicio;
	}
	
	
}
