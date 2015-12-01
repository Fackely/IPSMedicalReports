/**
 * 
 */
package com.servinte.axioma.dto.capitacion;

import java.util.Date;

import com.servinte.axioma.dto.facturacion.ContratoDto;

/**
 * Informacion del cargue del usuario capitado
 * 
 * @author jeilones
 * @created 3/10/2012
 *
 */
public class CargueDto {

	/**
	 * Fecha inicial del perido de generacion del cargue
	 * */
	private Date fechaInicial;
	/**
	 * Fecha final del perido de generacion del cargue
	 */
	private Date fechaFinal;
	
	/**
	 * Fecha en que se genera el cargue
	 */
	private Date fechaCargue;
	
	/**
	 * Contrato para el cua se realizó el último cargue del usuario y que tiene el convenio.
	 */
	private ContratoDto contratoDto;
	
	/**
	 * Indica si el cargue se encuentra activo o inactivo en el sistema.
	 */
	private String estado;
	
	/**
	 * Campo donde muestra el usuario que generó el cargue en el sistema
	 */
	private String usuarioGeneraCargue;
	
	
	/**
	 * Nombre del tipo cargue que se realizo
	 */
	private String nombreTipoCargue;
	/**
	 * Nombre de la naturaleza (excepcion monto) del cargue
	 */
	private String nombreNaturalezaPaciente;
	
	/**
	 * 
	 * @author jeilones
	 * @created 3/10/2012
	 */
	public CargueDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return the fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return the fechaCargue
	 */
	public Date getFechaCargue() {
		return fechaCargue;
	}
	/**
	 * @param fechaCargue the fechaCargue to set
	 */
	public void setFechaCargue(Date fechaCargue) {
		this.fechaCargue = fechaCargue;
	}
	/**
	 * @return the contratoDto
	 */
	public ContratoDto getContratoDto() {
		return contratoDto;
	}
	/**
	 * @param contratoDto the contratoDto to set
	 */
	public void setContratoDto(ContratoDto contratoDto) {
		this.contratoDto = contratoDto;
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
	 * @return the usuarioGeneraCargue
	 */
	public String getUsuarioGeneraCargue() {
		return usuarioGeneraCargue;
	}
	/**
	 * @param usuarioGeneraCargue the usuarioGeneraCargue to set
	 */
	public void setUsuarioGeneraCargue(String usuarioGeneraCargue) {
		this.usuarioGeneraCargue = usuarioGeneraCargue;
	}
	/**
	 * @return the nombreTipoCargue
	 */
	public String getNombreTipoCargue() {
		return nombreTipoCargue;
	}
	/**
	 * @param nombreTipoCargue the nombreTipoCargue to set
	 */
	public void setNombreTipoCargue(String nombreTipoCargue) {
		this.nombreTipoCargue = nombreTipoCargue;
	}
	/**
	 * @return the nombreNaturalezaPaciente
	 */
	public String getNombreNaturalezaPaciente() {
		return nombreNaturalezaPaciente;
	}
	/**
	 * @param nombreNaturalezaPaciente the nombreNaturalezaPaciente to set
	 */
	public void setNombreNaturalezaPaciente(String nombreNaturalezaPaciente) {
		this.nombreNaturalezaPaciente = nombreNaturalezaPaciente;
	}

}
