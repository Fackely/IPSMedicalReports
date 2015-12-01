
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import com.servinte.axioma.orm.Usuarios;

/**
 * Clase con la informaci&oacute;n necesaria de los registros de
 * Unidad de Agenda-Servicio X Tipo de Cita Odontol&oacute;gica
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class DtoUnidadAgendaServCitaOdonto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5744415965332915019L;

	/**
	 * C&oacute;digo del registro
	 */
	private long codigoRegistro;
	
	/**
	 * Acr&oacute;nimo del tipo de la cita seleccionada
	 */
	private String acronimoTipoCita;
	
	/**
	 * Descripci&oacute;n del tipo de cita seleccionada
	 */
	private String descripcionTipoCita;
	
	/**
	 * C&oacute;digo de la Unidad de Agenda de tipo Odontol&oacute;gica seleccionada
	 */
	private int codigoUnidadAgenda;
	
	/**
	 * Descripci&oacute;n de la Unidad de Agenda seleccionada
	 */
	private String descripcionUnidadAgenda;
	
	/**
	 * C&oacute;digo del servicio odontol&oacute;gico activo que pertenece a 
	 * la unidad de agenda odontol&oacute;gica seleccionada. 
	 */
	private int codigoServicio;
	
	/**
	 * Atributo con el acr&oacute;nimo del tipo de servicio
	 */
	private String tipoServicio;
	
	/**
	 * Atributo con el nombre del tipo de Servicio
	 */
	private String nombreTipoServicio;
	
	/**
	 * Descripci&oacute;n del Servicio seleccionado
	 */
	private String descripcionServicio;
	
	/**
	 * C&oacute;digo Propietario del servicio seleccionado
	 */
	private String codigoPropietarioServicio;
	
	/**
	 * Fecha de generaci&oacute;n del registro
	 */
	private Date fecha;
	
	/**
	 * Hora de generaci&oacute;n del registro
	 */
	private String hora;
	
	/**
	 * Usuario que realiza el registro
	 */
	private Usuarios usuario;
	
	/**
	 * C&oacute;digo del tipo tarifario al que est&aacute; asociado el servicio
	 */
	private int codigoTipoTarifario;
	
	/**
	 * Nombre del tipo tarifario al que est&aacute; asociado el servicio
	 */
	private String nombreTipoTarifario;

	/**
	 * Atributo que indica si el registro debe eliminarse una vez se confirmen los cambios
	 * realizados en la parametrizaci&oacute;n
	 */
	private boolean eliminarRegistro;

	/**
	 * Constructor de la Clase
	 */
	public DtoUnidadAgendaServCitaOdonto() {
		
		this.setEliminarRegistro(false);
	}

	
	/**
	 * @param codigoRegistro the codigoRegistro to set
	 */
	public void setCodigoRegistro(long codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}

	/**
	 * @return the codigoRegistro
	 */
	public long getCodigoRegistro() {
		return codigoRegistro;
	}
	
	/**
	 * @return the acronimoTipoCita
	 */
	public String getAcronimoTipoCita() {
		return acronimoTipoCita;
	}

	/**
	 * @param descripcionTipoCita the descripcionTipoCita to set
	 */
	public void setDescripcionTipoCita(String descripcionTipoCita) {
		this.descripcionTipoCita = descripcionTipoCita;
	}

	/**
	 * @return the descripcionTipoCita
	 */
	public String getDescripcionTipoCita() {
		return descripcionTipoCita;
	}

	/**
	 * @param acronimoTipoCita the acronimoTipoCita to set
	 */
	public void setAcronimoTipoCita(String acronimoTipoCita) {
		this.acronimoTipoCita = acronimoTipoCita;
	}

	/**
	 * @return the codigoUnidadAgenda
	 */
	public int getCodigoUnidadAgenda() {
		return codigoUnidadAgenda;
	}

	/**
	 * @param codigoUnidadAgenda the codigoUnidadAgenda to set
	 */
	public void setCodigoUnidadAgenda(int codigoUnidadAgenda) {
		this.codigoUnidadAgenda = codigoUnidadAgenda;
	}

	/**
	 * @return the descripcionUnidadAgenda
	 */
	public String getDescripcionUnidadAgenda() {
		return descripcionUnidadAgenda;
	}

	/**
	 * @param descripcionUnidadAgenda the descripcionUnidadAgenda to set
	 */
	public void setDescripcionUnidadAgenda(String descripcionUnidadAgenda) {
		this.descripcionUnidadAgenda = descripcionUnidadAgenda;
	}

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the descripcionServicio
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * @param descripcionServicio the descripcionServicio to set
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	/**
	 * @return the codigoPropietarioServicio
	 */
	public String getCodigoPropietarioServicio() {
		return codigoPropietarioServicio;
	}

	/**
	 * @param codigoPropietarioServicio the codigoPropietarioServicio to set
	 */
	public void setCodigoPropietarioServicio(String codigoPropietarioServicio) {
		this.codigoPropietarioServicio = codigoPropietarioServicio;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
	 * @return the codigoTipoTarifario
	 */
	public int getCodigoTipoTarifario() {
		return codigoTipoTarifario;
	}

	/**
	 * @param codigoTipoTarifario the codigoTipoTarifario to set
	 */
	public void setCodigoTipoTarifario(int codigoTipoTarifario) {
		this.codigoTipoTarifario = codigoTipoTarifario;
	}

	/**
	 * @return the nombreTipoTarifario
	 */
	public String getNombreTipoTarifario() {
		return nombreTipoTarifario;
	}

	/**
	 * @param nombreTipoTarifario the nombreTipoTarifario to set
	 */
	public void setNombreTipoTarifario(String nombreTipoTarifario) {
		this.nombreTipoTarifario = nombreTipoTarifario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the usuario
	 */
	public Usuarios getUsuario() {
		return usuario;
	}


	/**
	 * @param eliminarRegistro the eliminarRegistro to set
	 */
	public void setEliminarRegistro(boolean eliminarRegistro) {
		this.eliminarRegistro = eliminarRegistro;
	}


	/**
	 * @return the eliminarRegistro
	 */
	public boolean isEliminarRegistro() {
		return eliminarRegistro;
	}


	/**
	 * @param nombreTipoServicio the nombreTipoServicio to set
	 */
	public void setNombreTipoServicio(String nombreTipoServicio) {
		this.nombreTipoServicio = nombreTipoServicio;
	}


	/**
	 * @return the nombreTipoServicio
	 */
	public String getNombreTipoServicio() {
		return nombreTipoServicio;
	}


	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}


	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}
	
}
