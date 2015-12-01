/*
 * Junio 12, 2008
 */
package com.princetonsa.dto.historiaClinica.componentes;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

import util.InfoDatosInt;

/**
 * Data Transfer Object: 
 * Usado para el manejo de las edades de alimentacion de pediatría
 * 
 * @author Sebastián Gómez R.
 *
 */
public class DtoEdadAlimentacionPediatria implements Serializable
{
	private InfoDatosInt edad;
	private String edadTexto;
	private String fechaGrabacion;
	private String horaGrabacion;
	private UsuarioBasico profesional;
	private boolean existeBD;
	
	/**
	 * Método para limpiar los datos del DTO
	 *
	 */
	public void clean()
	{
		this.edad = new InfoDatosInt();
		this.edadTexto = "";
		this.fechaGrabacion = "";
		this.horaGrabacion = "";
		this.profesional = new UsuarioBasico();
		this.existeBD = false;
	}
	
	/**
	 * Cosntructor
	 *
	 */
	public DtoEdadAlimentacionPediatria()
	{
		this.clean();
	}

	/**
	 * @return the edad
	 */
	public int getCodigoEdad() {
		return edad.getCodigo();
	}

	/**
	 * @param edad the edad to set
	 */
	public void setCodigoEdad(int edad) {
		this.edad.setCodigo(edad);
	}
	
	/**
	 * @return the edad
	 */
	public String getNombreEdad() {
		return edad.getNombre();
	}

	/**
	 * @param edad the edad to set
	 */
	public void setNombreEdad(String edad) {
		this.edad.setNombre(edad);
	}

	/**
	 * @return the edadTexto
	 */
	public String getEdadTexto() {
		return edadTexto;
	}

	/**
	 * @param edadTexto the edadTexto to set
	 */
	public void setEdadTexto(String edadTexto) {
		this.edadTexto = edadTexto;
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
	 * @return the profesional
	 */
	public UsuarioBasico getProfesional() {
		return profesional;
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(UsuarioBasico profesional) {
		this.profesional = profesional;
	}

	/**
	 * @return the existeBD
	 */
	public boolean isExisteBD() {
		return existeBD;
	}

	/**
	 * @param existeBD the existeBD to set
	 */
	public void setExisteBD(boolean existeBD) {
		this.existeBD = existeBD;
	}
}
