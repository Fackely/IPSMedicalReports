package com.princetonsa.dto.manejoPaciente;

import java.util.Date;

import util.ConstantesBD;

public class DtoTrazabilidadAutorizacionIngEstancia {

	private int consecutivo;    
	private Date fechaInicioAutorizacion;
	private int consecutivoAdmision;
	private int diasEstanciaAutorizados;
	private String usuarioContacta;
	private String cargoUsuarioContacta;
	private String observaciones;
	private int convenioRecobro;               
	private String otroConvenioRecobro;       
	private char indicativoTemporal;
	private String centroCostoSolicitante;
	private String estado;
	private String usuarioModifica;
	private Date fechaModifica;   
	private String horaModifica;  
	private String accionRealizada;
	
	private String descripcionEntidad;
	private String direccionEntidad;
	private String telefonoEntidad;
	private Date fechaVencimiento;
	
	
	public DtoTrazabilidadAutorizacionIngEstancia() {
		
		this.consecutivo				= ConstantesBD.codigoNuncaValido;    
		this.fechaInicioAutorizacion	= null;
		this.consecutivoAdmision		= ConstantesBD.codigoNuncaValido;
		this.diasEstanciaAutorizados 	= ConstantesBD.codigoNuncaValido;
		this.usuarioContacta			= "";
		this.cargoUsuarioContacta		= "";
		this.observaciones				= "";
		this.convenioRecobro			= ConstantesBD.codigoNuncaValido;               
		this.otroConvenioRecobro		= "";       
		this.indicativoTemporal			= ' ';
		this.centroCostoSolicitante		= "";
		this.estado						= "";
		this.usuarioModifica			= "";
		this.fechaModifica				= null;   
		this.horaModifica				= "";  
		this.accionRealizada			= "";
		this.descripcionEntidad			= "";  
		this.direccionEntidad			= "";
		this.telefonoEntidad			= "";  
		this.fechaVencimiento		= null;
	}


	public int getConsecutivo() {
		return consecutivo;
	}


	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}


	public Date getFechaInicioAutorizacion() {
		return fechaInicioAutorizacion;
	}


	public void setFechaInicioAutorizacion(Date fechaInicioAutorizacion) {
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
	}


	public int getConsecutivoAdmision() {
		return consecutivoAdmision;
	}


	public void setConsecutivoAdmision(int consecutivoAdmision) {
		this.consecutivoAdmision = consecutivoAdmision;
	}


	public int getDiasEstanciaAutorizados() {
		return diasEstanciaAutorizados;
	}


	public void setDiasEstanciaAutorizados(int diasEstanciaAutorizados) {
		this.diasEstanciaAutorizados = diasEstanciaAutorizados;
	}


	public String getUsuarioContacta() {
		return usuarioContacta;
	}


	public void setUsuarioContacta(String usuarioContacta) {
		this.usuarioContacta = usuarioContacta;
	}


	public String getCargoUsuarioContacta() {
		return cargoUsuarioContacta;
	}


	public void setCargoUsuarioContacta(String cargoUsuarioContacta) {
		this.cargoUsuarioContacta = cargoUsuarioContacta;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public int getConvenioRecobro() {
		return convenioRecobro;
	}


	public void setConvenioRecobro(int convenioRecobro) {
		this.convenioRecobro = convenioRecobro;
	}


	public String getOtroConvenioRecobro() {
		return otroConvenioRecobro;
	}


	public void setOtroConvenioRecobro(String otroConvenioRecobro) {
		this.otroConvenioRecobro = otroConvenioRecobro;
	}


	public char getIndicativoTemporal() {
		return indicativoTemporal;
	}


	public void setIndicativoTemporal(char indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}


	public String getCentroCostoSolicitante() {
		return centroCostoSolicitante;
	}


	public void setCentroCostoSolicitante(String centroCostoSolicitante) {
		this.centroCostoSolicitante = centroCostoSolicitante;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}


	public Date getFechaModifica() {
		return fechaModifica;
	}


	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}


	public String getHoraModifica() {
		return horaModifica;
	}


	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}


	public String getAccionRealizada() {
		return accionRealizada;
	}


	public void setAccionRealizada(String accionRealizada) {
		this.accionRealizada = accionRealizada;
	}


	/**
	 * @return the descripcionEntidad
	 */
	public String getDescripcionEntidad() {
		return descripcionEntidad;
	}


	/**
	 * @param descripcionEntidad the descripcionEntidad to set
	 */
	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}


	/**
	 * @return the direccionEntidad
	 */
	public String getDireccionEntidad() {
		return direccionEntidad;
	}


	/**
	 * @param direccionEntidad the direccionEntidad to set
	 */
	public void setDireccionEntidad(String direccionEntidad) {
		this.direccionEntidad = direccionEntidad;
	}


	/**
	 * @return the telefonoEntidad
	 */
	public String getTelefonoEntidad() {
		return telefonoEntidad;
	}


	/**
	 * @param telefonoEntidad the telefonoEntidad to set
	 */
	public void setTelefonoEntidad(String telefonoEntidad) {
		this.telefonoEntidad = telefonoEntidad;
	}


	/**
	 * @return the fechaVencimiento
	 */
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}


	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
	
	   
	
}
