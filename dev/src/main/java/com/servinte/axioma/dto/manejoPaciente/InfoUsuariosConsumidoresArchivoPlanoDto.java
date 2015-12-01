package com.servinte.axioma.dto.manejoPaciente;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * Dto con los datos de busqueda y reporte generado para usuarios consumidores de presupuesto 
 * (para el archivo plano)
 * @author hermorhu
 * @created 22-Abr-2013 
 */
public class InfoUsuariosConsumidoresArchivoPlanoDto {

	//parametros de busqueda
	private String autorizaciones;
	private String convenio;
	private String viaIngreso;
	private String grupoServicios;
	private String claseInventarios;
	private String diagnostico;
	private String valorInicial;
	private String valorFinal;
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	private String fechaInicial;
	private String fechaFinal;
	//datos subreporte
	private JRDataSource JRDUsuariosConsumidoresPresupuesto;
	
	/**
	 * 
	 */
	public InfoUsuariosConsumidoresArchivoPlanoDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the autorizaciones
	 */
	public String getAutorizaciones() {
		return autorizaciones;
	}

	/**
	 * @param autorizaciones the autorizaciones to set
	 */
	public void setAutorizaciones(String autorizaciones) {
		this.autorizaciones = autorizaciones;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the grupoServicios
	 */
	public String getGrupoServicios() {
		return grupoServicios;
	}

	/**
	 * @param grupoServicios the grupoServicios to set
	 */
	public void setGrupoServicios(String grupoServicios) {
		this.grupoServicios = grupoServicios;
	}

	/**
	 * @return the claseInventarios
	 */
	public String getClaseInventarios() {
		return claseInventarios;
	}

	/**
	 * @param claseInventarios the claseInventarios to set
	 */
	public void setClaseInventarios(String claseInventarios) {
		this.claseInventarios = claseInventarios;
	}

	/**
	 * @return the diagnostico
	 */
	public String getDiagnostico() {
		return diagnostico;
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	/**
	 * @return the valorInicial
	 */
	public String getValorInicial() {
		return valorInicial;
	}

	/**
	 * @param valorInicial the valorInicial to set
	 */
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}

	/**
	 * @return the valorFinal
	 */
	public String getValorFinal() {
		return valorFinal;
	}

	/**
	 * @param valorFinal the valorFinal to set
	 */
	public void setValorFinal(String valorFinal) {
		this.valorFinal = valorFinal;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the jRDUsuariosConsumidoresPresupuesto
	 */
	public JRDataSource getJRDUsuariosConsumidoresPresupuesto() {
		return JRDUsuariosConsumidoresPresupuesto;
	}

	/**
	 * @param jRDUsuariosConsumidoresPresupuesto the jRDUsuariosConsumidoresPresupuesto to set
	 */
	public void setJRDUsuariosConsumidoresPresupuesto(
			JRDataSource jRDUsuariosConsumidoresPresupuesto) {
		JRDUsuariosConsumidoresPresupuesto = jRDUsuariosConsumidoresPresupuesto;
	}
	
}
