package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.InfoDatosInt;

/**
 * Dto que carga la informacion de las citas
 * @author axioma
 *
 */
public class DtoCitasPresupuestoOdo implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * codigo pk
	 */
	private BigDecimal codigo;
	
	/**
	 * tipo de cita
	 */
	private String tipo;
	
	/**
	 * codigo - nombre centro atencion
	 */
	private InfoDatosInt centroAtencion;
	
	
	/**
	 * 
	 */
	private String acronimoEstado;
	
	/**
	 * estado dela cita
	 */
	private String estado;
	
	/**
	 * fecha - hora de la cita
	 */
	private String fechaHora;
	
	/**
	 * profesional
	 */
	private String profesional;

	/**
	 * 
	 * @param codigo
	 * @param tipo
	 * @param centroAtencion
	 * @param estado
	 * @param fechaHora
	 * @param profesional
	 */
	public DtoCitasPresupuestoOdo(	) 
	{
		this.codigo = BigDecimal.ZERO;
		this.tipo = "";
		this.centroAtencion = new InfoDatosInt();
		this.estado = "";
		this.fechaHora = "";
		this.profesional = "";
		this.acronimoEstado= "";
	}

	/**
	 * @return the codigo
	 */
	public BigDecimal getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the fechaHora
	 */
	public String getFechaHora() {
		return fechaHora;
	}

	/**
	 * @param fechaHora the fechaHora to set
	 */
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	/**
	 * @return the profesional
	 */
	public String getProfesional() {
		return profesional;
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}

	/**
	 * @return the acronimoEstado
	 */
	public String getAcronimoEstado() {
		return acronimoEstado;
	}

	/**
	 * @param acronimoEstado the acronimoEstado to set
	 */
	public void setAcronimoEstado(String acronimoEstado) {
		this.acronimoEstado = acronimoEstado;
	}
	
}