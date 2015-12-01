package com.princetonsa.dto.cargos;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

public class DtoTercero implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3000925713181093312L;
	
	
	private BigDecimal codigo ;
	private String numeroIdentificacion;
	private String  descripcion ;
	private int institucion ;
	private String  activo;
	private int tipoTercero;
	private int digitoVerificacion  ;
	private String  direccion ;
	private String telefono  ;
	
	
	private DtoTipoTercero dtoTipoTercero;
	
	/**
	 * 
	 */
	public DtoTercero(){
		  this.codigo = BigDecimal.ZERO;
		  this.setNumeroIdentificacion("");
		  this.descripcion= "";
		  this.institucion= ConstantesBD.codigoNuncaValido;
		  this.tipoTercero= ConstantesBD.codigoNuncaValido;
		  this.setDigitoVerificacion(ConstantesBD.codigoNuncaValido);
		  this.direccion="";
		  this.telefono="" ;
		  this.dtoTipoTercero = new DtoTipoTercero();
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getCodigo() {
		return codigo;
	}
	
	
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getInstitucion() {
		return institucion;
	}
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	
	
	public int getTipoTercero() {
		return tipoTercero;
	}
	
	public void setTipoTercero(int tipoTercero) {
		this.tipoTercero = tipoTercero;
	}
	
	
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public void setDigitoVerificacion(int digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}


	public int getDigitoVerificacion() {
		return digitoVerificacion;
	}


	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}


	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}


	public void setDtoTipoTercero(DtoTipoTercero dtoTipoTercero) {
		this.dtoTipoTercero = dtoTipoTercero;
	}


	public DtoTipoTercero getDtoTipoTercero() {
		return dtoTipoTercero;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	/*
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	*/
	
	
	

}
