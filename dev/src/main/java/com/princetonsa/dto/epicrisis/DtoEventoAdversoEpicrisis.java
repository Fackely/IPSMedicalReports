package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class DtoEventoAdversoEpicrisis implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;

	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private String usuario;
	
	/**
	 * 
	 */
	private String gestionado;
	
	/**
	 * 
	 */
	private String centroCostoPaciente;
	
	/**
	 * 
	 */
	private String evento;
	
	/**
	 * 
	 */
	private String tipoEvento;
	
	/**
	 * 
	 */
	private String clasificacion;
	
	/**
	 * 
	 */
	private String observaciones;

	/**
	 * 
	 *
	 */
	public DtoEventoAdversoEpicrisis() 
	{
		this.fecha="";
		this.hora="";
		this.usuario="";
		this.gestionado="";
		this.centroCostoPaciente="";
		this.evento="";
		this.tipoEvento="";
		this.clasificacion="";
		this.observaciones="";
	}

	/**
	 * @return the centroCostoPaciente
	 */
	public String getCentroCostoPaciente() {
		return centroCostoPaciente;
	}

	/**
	 * @param centroCostoPaciente the centroCostoPaciente to set
	 */
	public void setCentroCostoPaciente(String centroCostoPaciente) {
		this.centroCostoPaciente = centroCostoPaciente;
	}

	/**
	 * @return the clasificacion
	 */
	public String getClasificacion() {
		return clasificacion;
	}

	/**
	 * @param clasificacion the clasificacion to set
	 */
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	/**
	 * @return the evento
	 */
	public String getEvento() {
		return evento;
	}

	/**
	 * @param evento the evento to set
	 */
	public void setEvento(String evento) {
		this.evento = evento;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the gestionado
	 */
	public String getGestionado() {
		return gestionado;
	}

	/**
	 * @param gestionado the gestionado to set
	 */
	public void setGestionado(String gestionado) {
		this.gestionado = gestionado;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the tipoEvento
	 */
	public String getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
}