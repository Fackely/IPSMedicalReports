/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.Date;

import com.servinte.axioma.orm.LogCierrePresuCapita;


/**
 * @author Cristhian Murillo
 */
public class DtoLogCierrePresuCapita extends LogCierrePresuCapita implements Serializable
{

	/** * */
	private static final long serialVersionUID = 1L;
	
	private String login;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	
	private Integer codigoConvenio;
	private String nombreConvenio;
	
	private Integer codigoContrato;
	private String numeroContrato;
	private Date fechaInicialContrato;
	private Date fechaFinalContrato;
	
	private Boolean noInformacion;
	private String tipoProceso;
	
	/**
	 * @return valor de login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login el login para asignar
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return valor de primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}
	/**
	 * @param primerNombre el primerNombre para asignar
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	/**
	 * @return valor de segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}
	/**
	 * @param segundoNombre el segundoNombre para asignar
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	/**
	 * @return valor de primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}
	/**
	 * @param primerApellido el primerApellido para asignar
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	/**
	 * @return valor de segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}
	/**
	 * @param segundoApellido el segundoApellido para asignar
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	/**
	 * @return valor de codigoConvenio
	 */
	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}
	/**
	 * @param codigoConvenio el codigoConvenio para asignar
	 */
	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	/**
	 * @return valor de nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}
	/**
	 * @param nombreConvenio el nombreConvenio para asignar
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}
	/**
	 * @return valor de codigoContrato
	 */
	public Integer getCodigoContrato() {
		return codigoContrato;
	}
	/**
	 * @param codigoContrato el codigoContrato para asignar
	 */
	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	/**
	 * @return valor de numeroContrato
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}
	/**
	 * @param numeroContrato el numeroContrato para asignar
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	/**
	 * @return valor de fechaInicialContrato
	 */
	public Date getFechaInicialContrato() {
		return fechaInicialContrato;
	}
	/**
	 * @param fechaInicialContrato el fechaInicialContrato para asignar
	 */
	public void setFechaInicialContrato(Date fechaInicialContrato) {
		this.fechaInicialContrato = fechaInicialContrato;
	}
	/**
	 * @return valor de fechaFinalContrato
	 */
	public Date getFechaFinalContrato() {
		return fechaFinalContrato;
	}
	/**
	 * @param fechaFinalContrato el fechaFinalContrato para asignar
	 */
	public void setFechaFinalContrato(Date fechaFinalContrato) {
		this.fechaFinalContrato = fechaFinalContrato;
	}
	public Boolean getNoInformacion() {
		return noInformacion;
	}
	public void setNoInformacion(Boolean noInformacion) {
		this.noInformacion = noInformacion;
	}
	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}
	public String getTipoProceso() {
		return tipoProceso;
	}

	
}
