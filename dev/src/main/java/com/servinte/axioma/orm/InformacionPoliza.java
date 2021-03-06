package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * InformacionPoliza generated by hbm2java
 */
public class InformacionPoliza implements java.io.Serializable {

	private int codigo;
	private SubCuentas subCuentas;
	private Date fechaAutorizacion;
	private String numeroAutorizacion;
	private BigDecimal valorMontoAutorizado;
	private Date fechaGrabacion;
	private String horaGrabacion;
	private String usuario;

	public InformacionPoliza() {
	}

	public InformacionPoliza(int codigo, SubCuentas subCuentas,
			Date fechaAutorizacion, String numeroAutorizacion,
			BigDecimal valorMontoAutorizado, Date fechaGrabacion,
			String horaGrabacion, String usuario) {
		this.codigo = codigo;
		this.subCuentas = subCuentas;
		this.fechaAutorizacion = fechaAutorizacion;
		this.numeroAutorizacion = numeroAutorizacion;
		this.valorMontoAutorizado = valorMontoAutorizado;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.usuario = usuario;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public SubCuentas getSubCuentas() {
		return this.subCuentas;
	}

	public void setSubCuentas(SubCuentas subCuentas) {
		this.subCuentas = subCuentas;
	}

	public Date getFechaAutorizacion() {
		return this.fechaAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public String getNumeroAutorizacion() {
		return this.numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public BigDecimal getValorMontoAutorizado() {
		return this.valorMontoAutorizado;
	}

	public void setValorMontoAutorizado(BigDecimal valorMontoAutorizado) {
		this.valorMontoAutorizado = valorMontoAutorizado;
	}

	public Date getFechaGrabacion() {
		return this.fechaGrabacion;
	}

	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getHoraGrabacion() {
		return this.horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
