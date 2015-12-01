package com.princetonsa.dto.manejoPaciente;

import java.util.Date;

import util.ConstantesBD;

public class DtoTrazabilidadAutorizacion {

	 
	 private int consecutivo;                   
	 private String tipoAutorizacion;           
	 private int convenioRecobro;               
	 private String otroConvenioRecobro;       
	 private char indicativoTemporal;          
	 private String descripcionEntidad;         
	 private String direccionEntidad;            
	 private String telefonoEntidad; 
	 private int indicadorPrioridad;
	 private String usuarioModifica;
	 private Date fechaModifica;   
	 private String horaModifica;  
	 private String accionRealizada;
	 private long codigoPk;        
	 private String observaciones;   
	 private Date fechaVencimiento;
	 private Date nuevaFechaVencimiento;

	public DtoTrazabilidadAutorizacion(){
		this.descripcionEntidad	="";           
		this.direccionEntidad	="";             
		this.telefonoEntidad	="";             
		this.indicadorPrioridad	=ConstantesBD.codigoNuncaValido;         
		this.usuarioModifica	="";            
		this.fechaModifica		=null;          
		this.horaModifica		="";             
		this.accionRealizada	="";            
		this.codigoPk			=ConstantesBD.codigoNuncaValidoLong;         
		this.observaciones		="";             
		this.fechaVencimiento	=null;
		this.setNuevaFechaVencimiento(null);
		this.indicativoTemporal	=' ';
		this.consecutivo		=ConstantesBD.codigoNuncaValido;
		this.tipoAutorizacion	="";
		this.convenioRecobro 	=ConstantesBD.codigoNuncaValido;
		this.otroConvenioRecobro="";
	}

	public String getDescripcionEntidad() {
		return descripcionEntidad;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}

	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
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

	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}

	public String getDireccionEntidad() {
		return direccionEntidad;
	}

	public void setDireccionEntidad(String direccionEntidad) {
		this.direccionEntidad = direccionEntidad;
	}

	public String getTelefonoEntidad() {
		return telefonoEntidad;
	}

	public void setTelefonoEntidad(String telefonoEntidad) {
		this.telefonoEntidad = telefonoEntidad;
	}

	public int getIndicadorPrioridad() {
		return indicadorPrioridad;
	}

	public void setIndicadorPrioridad(int indicadorPrioridad) {
		this.indicadorPrioridad = indicadorPrioridad;
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

	public long getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public char getIndicativoTemporal() {
		return indicativoTemporal;
	}

	public void setIndicativoTemporal(char indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}

	public void setNuevaFechaVencimiento(Date nuevaFechaVencimiento) {
		this.nuevaFechaVencimiento = nuevaFechaVencimiento;
	}

	public Date getNuevaFechaVencimiento() {
		return nuevaFechaVencimiento;
	}

	
}
