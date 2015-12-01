package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * /**
	 * Alberto Ovalle
	 * mt5749
	 * se crea nuevo dto para implemetar las conductas y las observaciones
 */
public class ValoracionConductaObservacionesDto implements Serializable {

	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * atributos ValoracionConductaObservaciones
	 */
	/**
	 * atributo para numeroSolicitud
	 */
	private String numeroSolicitud;
	/**
	 * atributo para tipo observacion
	 */
    private String tipo;
    /**
	 * atributo para tipo valor
	 */ 
    private String valor;
    /**
   	 * atributo para fechaValoracionObservacion
   	 */ 
    private String fechaValoracionObservacion;
    /**
   	 * atributo para horaValoracionObservacion
   	 */ 
    private String horaValoracionObservacion;
    /**
   	 * atributo para label
   	 */ 
    private String label;
    /**
   	 * atributo para fechaHora
   	 */ 
    private Date fechaHora;
    /**
   	 * atributo para fechaValoracionUrgenciasr
   	 */ 
    private String fechaValoracionUrgencias; 
    /**
   	 * atributo para horaValoracionUrgencias
   	 */ 
    private String horaValoracionUrgencias;
    /**
   	 * atributo para especialidades
   	 */ 
    private String especialidades;
    /**
   	 * atributo para profesion
   	 */ 
    private String profesion;
    /**
   	 * atributo para registromedico
   	 */ 
    private String registromedico;
    /**
   	 * atributo para comentarios
   	 */ 
    private String comentarios;
    /**
   	 * atributo para desc_conducta_valoracion
   	 */ 
    private String desc_conducta_valoracion;
    /**
   	 * atributo para  lista vistaobservaciones
   	 */ 
    private List<ValoracionConductaObservacionesDto> vistaobservaciones;

	
	/**
	 * @return numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	
	 * @param numeroSolicitud
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * 
	 * @return tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * 
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * 
	 * @return valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * 
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * 
	 * @return fechaValoracionObservacion
	 */
	public String getFechaValoracionObservacion() {
		return fechaValoracionObservacion;
	}

	/**
	 * 
	 * @param fechaValoracionObservacion
	 */
	public void setFechaValoracionObservacion(String fechaValoracionObservacion) {
		this.fechaValoracionObservacion = fechaValoracionObservacion;
	}

	/**
	 * 
	 * @return horaValoracionObservacion
	 */
	public String getHoraValoracionObservacion() {
		return horaValoracionObservacion;
	}

	/**
	 * 
	 * @param horaValoracionObservacion
	 */
	public void setHoraValoracionObservacion(String horaValoracionObservacion) {
		this.horaValoracionObservacion = horaValoracionObservacion;
	}

	/**
	 * 
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 
	 * @return fechaHora
	 */
	
	public Date getFechaHora() {
		return fechaHora;
	}

	/**
	 * 
	 * @param fechaHora
	 */
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	/**
	 * 
	 * @return fechaValoracionUrgencias
	 */

	public String getFechaValoracionUrgencias() {
		return fechaValoracionUrgencias;
	}

	/**
	 * 
	 * @param fechaValoracionUrgencias
	 */
	public void setFechaValoracionUrgencias(String fechaValoracionUrgencias) {
		this.fechaValoracionUrgencias = fechaValoracionUrgencias;
	}

	/**
	 * 
	 * @return horaValoracionUrgencias
	 */
	public String getHoraValoracionUrgencias() {
		return horaValoracionUrgencias;
	}

	/**
	 * 
	 * @param horaValoracionUrgencias
	 */
	public void setHoraValoracionUrgencias(String horaValoracionUrgencias) {
		this.horaValoracionUrgencias = horaValoracionUrgencias;
	}

	/**
	 * 
	 * @return especialidades
	 */
	public String getEspecialidades() {
		return especialidades;
	}

	/**
	 * 
	 * @param especialidades
	 */
	public void setEspecialidades(String especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * 
	 * @return profesion
	 */
	public String getProfesion() {
		return profesion;
	}

	/**
	 * 
	 * @param profesion
	 */
	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	/**
	 * 
	 * @return registromedico
	 */
	public String getRegistromedico() {
		return registromedico;
	}

	/**
	 * 
	 * @param registromedico
	 */
	public void setRegistromedico(String registromedico) {
		this.registromedico = registromedico;
	}

	/**
	 * 
	 * @return comentarios
	 */
	public String getComentarios() {
		return comentarios;
	}

	/**
	 * 
	 * @param comentarios
	 */
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	/**
	 * 
	 * @return vistaobservaciones
	 */
	public List<ValoracionConductaObservacionesDto> getVistaobservaciones() {
		return vistaobservaciones;
	}
	
	/**
	 * 
	 * @param vistaobservaciones
	 */
	public void setVistaobservaciones(
			List<ValoracionConductaObservacionesDto> vistaobservaciones) {
		this.vistaobservaciones = vistaobservaciones;
	}

	/**
	 * 
	 * @return desc_conducta_valoracion
	 */
	public String getDesc_conducta_valoracion() {
		return desc_conducta_valoracion;
	}

	/**
	 * 
	 * @param desc_conducta_valoracion
	 */
	public void setDesc_conducta_valoracion(String desc_conducta_valoracion) {
		this.desc_conducta_valoracion = desc_conducta_valoracion;
	}
		
	
}
