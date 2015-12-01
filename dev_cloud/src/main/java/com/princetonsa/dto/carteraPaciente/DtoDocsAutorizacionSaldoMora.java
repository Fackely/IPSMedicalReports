package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoDocsAutorizacionSaldoMora implements Serializable
{
	private double autorizacionSaldoMora;
	private double datosFinanciacion;
	private BigDecimal saldo;
	
	public void reset()
	{
		this.autorizacionSaldoMora=0;
		this.datosFinanciacion=0;
		this.saldo=new BigDecimal(0);
	}
	
	public DtoDocsAutorizacionSaldoMora()
	{
		this.reset();
	}

	public double getAutorizacionSaldoMora() {
		return autorizacionSaldoMora;
	}

	public void setAutorizacionSaldoMora(double autorizacionSaldoMora) {
		this.autorizacionSaldoMora = autorizacionSaldoMora;
	}

	public double getDatosFinanciacion() {
		return datosFinanciacion;
	}

	public void setDatosFinanciacion(double datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	
}