package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

public class DTOConsultaLogSubirPacientes implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/**Campos de tabla (logSubirPacientes)**/
	private Long codigoPkLogSubir;
	private Integer contrato;
	private Integer convenio;
	private String nombreConvenio;
	private String numeroContrato;
	private Date fechaInicial;
	private Date fechaFinal;
	private Date fechaCargue;
	private String horaCargue;
	private String usuario;
	private Long totalLineasLeidas;
	private Long totallineasGrabadas;
	private int numInconsistencias;
	private String tieneInconsistencias;
	private Date fechaInicialContrato;
	private Date fechaFinalContrato;
	
	private boolean inconsistencias;
	
	
	public void reset()
	{	
		this.codigoPkLogSubir=ConstantesBD.codigoNuncaValidoLong;	
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.convenio=ConstantesBD.codigoNuncaValido;
		this.nombreConvenio="";
		this.numeroContrato="";
		this.fechaInicial=new Date();
		this.fechaFinal=new Date();
		this.fechaCargue=new Date();
		this.usuario="";
		this.totalLineasLeidas=ConstantesBD.codigoNuncaValidoLong;
		this.totallineasGrabadas=ConstantesBD.codigoNuncaValidoLong;
		this.horaCargue="";
		this.numInconsistencias=0;
		this.tieneInconsistencias="NO";
		this.fechaInicialContrato=new Date();
		this.fechaFinalContrato=new Date();
	}
	
	
	public Integer getContrato() {
		return contrato;
	}

	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	public Integer getConvenio() {
		return convenio;
	}

	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	public String getNombreConvenio() {
		return nombreConvenio;
	}

	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public Date getFechaCargue() {
		return fechaCargue;
	}

	public void setFechaCargue(Date fechaCargue) {
		this.fechaCargue = fechaCargue;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Long getTotalLineasLeidas() {
		return totalLineasLeidas;
	}

	public void setTotalLineasLeidas(Long totalLineasLeidas) {
		this.totalLineasLeidas = totalLineasLeidas;
	}

	public Long getTotallineasGrabadas() {
		return totallineasGrabadas;
	}

	public void setTotallineasGrabadas(Long totallineasGrabadas) {
		this.totallineasGrabadas = totallineasGrabadas;
	}

	public void setCodigoPkLogSubir(Long codigoPkLogSubir) {
		this.codigoPkLogSubir = codigoPkLogSubir;
	}


	public Long getCodigoPkLogSubir() {
		return codigoPkLogSubir;
	}


	public void setInconsistencias(boolean inconsistencias) {
		this.inconsistencias = inconsistencias;
	}


	public boolean isInconsistencias() {
		return inconsistencias;
	}


	/**
	 * @return the horaCargue
	 */
	public String getHoraCargue() {
		return horaCargue;
	}


	/**
	 * @param horaCargue the horaCargue to set
	 */
	public void setHoraCargue(String horaCargue) {
		this.horaCargue = horaCargue;
	}


	/**
	 * @return the numInconsistencias
	 */
	public int getNumInconsistencias() {
		return numInconsistencias;
	}


	/**
	 * @param numInconsistencias the numInconsistencias to set
	 */
	public void setNumInconsistencias(int numInconsistencias) {
		this.numInconsistencias = numInconsistencias;
		if(this.numInconsistencias>0){
			this.tieneInconsistencias="SI";
			this.inconsistencias=true;
		}
		else{
			this.tieneInconsistencias="NO";
			this.inconsistencias=false;
		}
	}


	/**
	 * @return the tieneInconsistenicias
	 */
	public String getTieneInconsistencias() {
		return tieneInconsistencias;
	}


	/**
	 * @param tieneInconsistenicias the tieneInconsistenicias to set
	 */
	public void setTieneInconsistencias(String tieneInconsistencias) {
		this.tieneInconsistencias = tieneInconsistencias;
	}


	/**
	 * @return the fechaInicialContrato
	 */
	public Date getFechaInicialContrato() {
		return fechaInicialContrato;
	}


	/**
	 * @param fechaInicialContrato the fechaInicialContrato to set
	 */
	public void setFechaInicialContrato(Date fechaInicialContrato) {
		this.fechaInicialContrato = fechaInicialContrato;
	}


	/**
	 * @return the fechaFinalContrato
	 */
	public Date getFechaFinalContrato() {
		return fechaFinalContrato;
	}


	/**
	 * @param fechaFinalContrato the fechaFinalContrato to set
	 */
	public void setFechaFinalContrato(Date fechaFinalContrato) {
		this.fechaFinalContrato = fechaFinalContrato;
	}

	

	
}
