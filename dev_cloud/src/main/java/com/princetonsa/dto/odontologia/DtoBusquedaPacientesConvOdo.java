package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Contiene los parametros de busqueda para los pacientes de convenio odontologico
 * @author Cristhian Murillo
 *
 */
public class DtoBusquedaPacientesConvOdo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int codigoConvenio;
	private String codContrato;
	private String numeroIdPaciente;
	private String tipoIdPaciente;
	private Date fechaActual;
	
	
	
	/**
	 * Constructor d ela clase
	 */
	public DtoBusquedaPacientesConvOdo(){
	
		this.codigoConvenio 	= ConstantesBD.codigoNuncaValido;
		this.codContrato		= "";
		this.numeroIdPaciente	= "";
		this.tipoIdPaciente		= "";
		this.fechaActual		= null;
	}

	

	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public String getCodContrato() {
		return codContrato;
	}


	public void setCodContrato(String codContrato) {
		this.codContrato = codContrato;
	}


	public String getNumeroIdPaciente() {
		return numeroIdPaciente;
	}


	public void setNumeroIdPaciente(String numeroIdPaciente) {
		this.numeroIdPaciente = numeroIdPaciente;
	}


	public String getTipoIdPaciente() {
		return tipoIdPaciente;
	}


	public void setTipoIdPaciente(String tipoIdPaciente) {
		this.tipoIdPaciente = tipoIdPaciente;
	}


	public Date getFechaActual() {
		return fechaActual;
	}


	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	
}
