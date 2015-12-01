  /**
 * 
 */
package com.princetonsa.dto.cartera;

import util.ConstantesBD;

/**
 * @author armando      
 *
 */
public class DtoFiltroReporteCuentasCobro 
{
	/**
	 * 
	 */
	private String fechaElaboracionInicial;

	/**
	 * 
	 */
	private String fechaElaboracionFinal;

	/**
	 * 
	 */
	private String fechaRadicacionInicial;

	/**
	 * 
	 */
	private String fechaRadicacionFinal;

	/**
	 * 
	 */
	private String cuentaCobroInicial;

	/**
	 * 
	 */
	private String cuentaCobroFinal;

	/**
	 * 
	 */
	private int centroAtencion;

	/**
	 * 
	 */
	private int convenio;

	/**
	 * 
	 */
	private int estadoCuentaCobro;

	/**
	 * 
	 */
	private String usuarioElaboracion;

	/**
	 * 
	 */
	private String usuarioRadicacion;

	
	public DtoFiltroReporteCuentasCobro()
	{
		this.fechaElaboracionInicial="";
		this.fechaElaboracionFinal="";
		this.fechaRadicacionInicial="";
		this.fechaRadicacionFinal="";
		this.cuentaCobroInicial="";
		this.cuentaCobroFinal="";
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.convenio=ConstantesBD.codigoNuncaValido;
		this.estadoCuentaCobro=ConstantesBD.codigoNuncaValido;
		this.usuarioElaboracion="";
		this.usuarioRadicacion="";
	}


	public String getFechaElaboracionInicial() {
		return fechaElaboracionInicial;
	}


	public void setFechaElaboracionInicial(String fechaElaboracionInicial) {
		this.fechaElaboracionInicial = fechaElaboracionInicial;
	}


	public String getFechaElaboracionFinal() {
		return fechaElaboracionFinal;
	}


	public void setFechaElaboracionFinal(String fechaElaboracionFinal) {
		this.fechaElaboracionFinal = fechaElaboracionFinal;
	}


	public String getFechaRadicacionInicial() {
		return fechaRadicacionInicial;
	}


	public void setFechaRadicacionInicial(String fechaRadicacionInicial) {
		this.fechaRadicacionInicial = fechaRadicacionInicial;
	}


	public String getFechaRadicacionFinal() {
		return fechaRadicacionFinal;
	}


	public void setFechaRadicacionFinal(String fechaRadicacionFinal) {
		this.fechaRadicacionFinal = fechaRadicacionFinal;
	}


	public String getCuentaCobroInicial() {
		return cuentaCobroInicial;
	}


	public void setCuentaCobroInicial(String cuentaCobroInicial) {
		this.cuentaCobroInicial = cuentaCobroInicial;
	}


	public String getCuentaCobroFinal() {
		return cuentaCobroFinal;
	}


	public void setCuentaCobroFinal(String cuentaCobroFinal) {
		this.cuentaCobroFinal = cuentaCobroFinal;
	}


	public int getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public int getConvenio() {
		return convenio;
	}


	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}


	public int getEstadoCuentaCobro() {
		return estadoCuentaCobro;
	}


	public void setEstadoCuentaCobro(int estadoCuentaCobro) {
		this.estadoCuentaCobro = estadoCuentaCobro;
	}


	public String getUsuarioElaboracion() {
		return usuarioElaboracion;
	}


	public void setUsuarioElaboracion(String usuarioElaboracion) {
		this.usuarioElaboracion = usuarioElaboracion;
	}


	public String getUsuarioRadicacion() {
		return usuarioRadicacion;
	}


	public void setUsuarioRadicacion(String usuarioRadicacion) {
		this.usuarioRadicacion = usuarioRadicacion;
	}
}
