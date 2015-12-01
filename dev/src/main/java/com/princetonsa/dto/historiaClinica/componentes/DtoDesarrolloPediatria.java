/*
 * Junio 12, 2008
 */
package com.princetonsa.dto.historiaClinica.componentes;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

import util.InfoDatosInt;

/**
 * Data Transfer Object: 
 * Usado para el manejo de las opciones de desarrollo de pediatría
 * 
 * @author Sebastián Gómez R.
 *
 */
public class DtoDesarrolloPediatria extends InfoDatosInt implements Serializable
{
	private String edadTexto;
	private InfoDatosInt tipo;
	private String fechaGrabacion;
	private String horaGrabacion;
	private UsuarioBasico profesional;
	private boolean existeBD;
	
	/**
	 * Método que limpia los datos
	 */
	public void clean()
	{
		super.clean();
		this.edadTexto = "";
		this.tipo = new InfoDatosInt();
		this.fechaGrabacion = "";
		this.horaGrabacion = "";
		this.profesional = new UsuarioBasico();
		this.existeBD = false;
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
	 * @return the tipo
	 */
	public int getCodigoTipo() {
		return tipo.getCodigo();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setCodigoTipo(int tipo) {
		this.tipo.setCodigo(tipo);
	}
	
	/**
	 * @return the tipo
	 */
	public String getNombreTipo() {
		return tipo.getNombre();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setNombreTipo(String tipo) {
		this.tipo.setNombre(tipo);
	}

	/**
	 * Constructor
	 *
	 */
	public DtoDesarrolloPediatria()
	{
		this.clean();
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
