package com.servinte.axioma.dto.facturacion;

import java.util.Date;

/**
 * @author diecorqu
 * @version 1.0
 * @created 05-jul-2012 12:48:36 p.m.
 */
public class FiltroBusquedaMontosCobroDto {

	/**
	 * Código vía ingreso
	 */
	private int viaIngreso;
	
	/**
	 * Acronimo tipo paciente
	 */
	private String tipoPaciente;
	
	/**
	 * Código convenio
	 */
	private int convenio;
	
	/**
	 * Acronimo tipo afiliado
	 */
	private String tipoAfiliado;
	
	/**
	 * Código clasificacionsocioeconomica
	 */
	private int clasificacionSocioEconomica;
	
	/**
	 * Código naturaleza
	 */
	private int naturalezaPaciente;
	
	/**
	 * Código tipo monto
	 */
	private int tipoMonto;
	
	/**
	 * Fecha apertura de la cuenta
	 */
	private Date fechaAperturaCuenta;
	
	/**
	 * Fecha de búsqueda para la vigencia de los montos de cobro. 
	 * Si la cuenta tiene fecha apertura se toma esta
	 * si no tiene fecha apertura se toma la fecha del sistema
	 */
	private Date fechaBusqueda;
	
	/**
	 * Estado de la cuenta del paciente
	 */
	private boolean cuentaAbierta;
	
	/**
	 * Indica si se esta llamando la consulta desde la
	 * autorizacion de capitacion subcontratada
	 */
	private boolean autorizacionCapitacionSubcontratada;
	
	/**
	 * Estado montos de cobro
	 */
	private boolean activo;

	
	public FiltroBusquedaMontosCobroDto() {
		/*
		 * Por defecto se buscan los montos de cobro activos
		 */
		this.setActivo(true);
	}
	
	/**
	 * @return the viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the tipoAfiliado
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * @param tipoAfiliado the tipoAfiliado to set
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	/**
	 * @return the clasificacionSocioEconomica
	 */
	public int getClasificacionSocioEconomica() {
		return clasificacionSocioEconomica;
	}

	/**
	 * @param clasificacionSocioEconomica the clasificacionSocioEconomica to set
	 */
	public void setClasificacionSocioEconomica(int clasificacionSocioEconomica) {
		this.clasificacionSocioEconomica = clasificacionSocioEconomica;
	}

	/**
	 * @return the naturalezaPaciente
	 */
	public int getNaturalezaPaciente() {
		return naturalezaPaciente;
	}

	/**
	 * @param naturalezaPaciente the naturalezaPaciente to set
	 */
	public void setNaturalezaPaciente(int naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}

	/**
	 * @return the fechaAperturaCuenta
	 */
	public Date getFechaAperturaCuenta() {
		return fechaAperturaCuenta;
	}

	/**
	 * @return the tipoMonto
	 */
	public int getTipoMonto() {
		return tipoMonto;
	}

	/**
	 * @param tipoMonto the tipoMonto to set
	 */
	public void setTipoMonto(int tipoMonto) {
		this.tipoMonto = tipoMonto;
	}

	/**
	 * @param fechaAperturaCuenta the fechaAperturaCuenta to set
	 */
	public void setFechaAperturaCuenta(Date fechaAperturaCuenta) {
		this.fechaAperturaCuenta = fechaAperturaCuenta;
	}
	
	/**
	 * @return the fechaBusqueda
	 */
	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	/**
	 * @param fechaBusqueda the fechaBusqueda to set
	 */
	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}

	/**
	 * @return the cuentaAbierta
	 */
	public boolean isCuentaAbierta() {
		return cuentaAbierta;
	}

	/**
	 * @param cuentaAbierta the cuentaAbierta to set
	 */
	public void setCuentaAbierta(boolean cuentaAbierta) {
		this.cuentaAbierta = cuentaAbierta;
	}
	
	/**
	 * @return the autorizacionCapitacionSubcontratada
	 */
	public boolean isAutorizacionCapitacionSubcontratada() {
		return autorizacionCapitacionSubcontratada;
	}

	/**
	 * @param autorizacionCapitacionSubcontratada the autorizacionCapitacionSubcontratada to set
	 */
	public void setAutorizacionCapitacionSubcontratada(
			boolean autorizacionCapitacionSubcontratada) {
		this.autorizacionCapitacionSubcontratada = autorizacionCapitacionSubcontratada;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
}