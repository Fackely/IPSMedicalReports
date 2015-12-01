package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import com.ibm.icu.math.BigDecimal;

import util.ConstantesBD;


public class DtoDevolRecibosCaja implements Serializable 
{
	private int codigoPk;
	
	private double consecutivo;
	
	private String numeroRC;
	
	private int motivo;
	
	private String nombreMotivo;
	
	private String fechaDevolucion;
	
	private String horaDevolucion;
	
	private String usuarioDevolucion;
	
	private BigDecimal valorDevolucion;
	
	
	/**
	 * Constructor
	 */
	
	public void reset()
	{	
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.consecutivo=ConstantesBD.codigoNuncaValidoDouble;
		this.numeroRC="";
		this.motivo=ConstantesBD.codigoNuncaValido;
		this.nombreMotivo="";
		this.fechaDevolucion="";
		this.horaDevolucion="";
		this.usuarioDevolucion="";
		this.valorDevolucion=new BigDecimal(0);
	}
	
	public DtoDevolRecibosCaja()
	{
		reset();
	}


	public int getCodigoPk() {
		return codigoPk;
	}


	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	public double getConsecutivo() {
		return consecutivo;
	}


	public void setConsecutivo(double consecutivo) {
		this.consecutivo = consecutivo;
	}


	public String getNumeroRC() {
		return numeroRC;
	}


	public void setNumeroRC(String numeroRC) {
		this.numeroRC = numeroRC;
	}


	public int getMotivo() {
		return motivo;
	}


	public void setMotivo(int motivo) {
		this.motivo = motivo;
	}


	public String getNombreMotivo() {
		return nombreMotivo;
	}


	public void setNombreMotivo(String nombreMotivo) {
		this.nombreMotivo = nombreMotivo;
	}


	public String getFechaDevolucion() {
		return fechaDevolucion;
	}


	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}


	public String getHoraDevolucion() {
		return horaDevolucion;
	}


	public void setHoraDevolucion(String horaDevolucion) {
		this.horaDevolucion = horaDevolucion;
	}


	public String getUsuarioDevolucion() {
		return usuarioDevolucion;
	}


	public void setUsuarioDevolucion(String usuarioDevolucion) {
		this.usuarioDevolucion = usuarioDevolucion;
	}

	public BigDecimal getValorDevolucion() {
		return valorDevolucion;
	}

	public void setValorDevolucion(BigDecimal valorDevolucion) {
		this.valorDevolucion = valorDevolucion;
	}
	
	
	
	
	
}
